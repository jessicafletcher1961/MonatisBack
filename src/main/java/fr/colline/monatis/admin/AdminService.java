package fr.colline.monatis.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import fr.colline.monatis.exceptions.ServiceException;

@Service
public class AdminService {

	@Autowired private DataSource dataSource;

	private static Logger logger = LoggerFactory.getLogger(AdminService.class);

	public void executerScript(File fichierScript) throws ServiceException {

		try {
			JdbcTemplate template = new JdbcTemplate(dataSource);

			String query = 
					"RUNSCRIPT FROM '"
							+ fichierScript.getAbsolutePath()
							+ "';";

			logger.info("-> COMMANDE EXECUTEE : {}", query);

			template.execute(query);
		}
		catch ( BadSqlGrammarException e ) {

			throw new ServiceException(e, AdminFonctionnelleErreur.ERREUR_EXECUTION_SCRIPT, fichierScript.getAbsolutePath());
		}
	}

	public void exporterToCsv(String nomTable, File fichierCsv) throws ServiceException {

		try {
			JdbcTemplate template = new JdbcTemplate(dataSource);

			String query = 
					"CALL CSVWRITE ('"
							+ fichierCsv.getAbsolutePath() 
							+ "', 'SELECT * FROM "
							+ nomTable
							+ "');";

			logger.info("-> COMMANDE EXECUTEE {}", query);

			template.execute(query);
		}
		catch ( BadSqlGrammarException e ) {

			logger.error("-> ERREUR EXPORT TABLE {} DANS {}", nomTable, fichierCsv.getAbsolutePath());

			throw new ServiceException(e, AdminFonctionnelleErreur.ERREUR_EXPORT_CSV, nomTable, fichierCsv.getAbsolutePath());
		}
	}

	public void importerFromCsv(File fichierCsv, String nomTable) throws ServiceException {
		
		JdbcTemplate template = new JdbcTemplate(dataSource);

		try {
			String query1 =  
					"DELETE FROM "
							+ nomTable
							+ ";";
			logger.info("-> COMMANDE EXECUTEE {}", query1);
			template.execute(query1);			

			String query2 = 
					"INSERT INTO "
							+ nomTable
							+ " (SELECT * FROM CSVREAD('"
							+ fichierCsv.getAbsolutePath()
							+ "'));";
			logger.info("-> COMMANDE EXECUTEE {}", query2);
			template.execute(query2);			
		}
		catch ( BadSqlGrammarException | DataIntegrityViolationException e ) {

			logger.error("-> ERREUR IMPORT CSV TABLE {} DEPUIS {}", nomTable, fichierCsv.getAbsolutePath());

			throw new ServiceException(e, AdminFonctionnelleErreur.ERREUR_IMPORT_CSV, nomTable, fichierCsv.getAbsolutePath());
		}
	}

	public void vidangerBase() throws ServiceException {

		JdbcTemplate template = new JdbcTemplate(dataSource);

		try (Statement statement = dataSource.getConnection().createStatement();) {
			
			ResultSet rs = statement.executeQuery("SELECT table_name FROM INFORMATION_SCHEMA.TABLES where table_type = 'BASE TABLE' and table_schema = 'PUBLIC';");
			if ( rs.first() ) {
				while ( ! rs.isAfterLast() ) {
					String nomTable = rs.getString(1);
					String query = "DELETE FROM " + nomTable + ";";
					logger.info("-> COMMANDE EXECUTEE {}", query);
					template.execute(query);
					
					rs.next();
				}
			}
			statement.close();
		}
		catch ( BadSqlGrammarException | SQLException e ) {

			logger.error("-> ERREUR VIDANGE BASE");

			throw new ServiceException(e, AdminFonctionnelleErreur.ERREUR_VIDANGE_BASE);
		}
	}

	public void sauvegarderBase(File fichierZip) throws ServiceException {

		try (Statement statement = dataSource.getConnection().createStatement();
				ZipOutputStream zip =  new ZipOutputStream(new FileOutputStream(fichierZip)) ) {

			// Sauvegarde des tables au format csv

			ResultSet rsTables = statement.executeQuery("SELECT table_name FROM INFORMATION_SCHEMA.TABLES where table_type = 'BASE TABLE' and table_schema = 'PUBLIC';");
			if ( rsTables.first() ) {
				while ( ! rsTables.isAfterLast() ) {
					String nomTable = rsTables.getString(1);

					String csvEntryName = nomTable + ".csv";
					ZipEntry entry = new ZipEntry(csvEntryName);
					zip.putNextEntry(entry);

					String nomFichierCsv = "sauvegardes/" + csvEntryName;
					File fichierCsv = new File(nomFichierCsv);
					
					exporterToCsv(nomTable, fichierCsv);
					Files.copy(fichierCsv.toPath(), zip);

					fichierCsv.delete();

					rsTables.next();
				}
			}

			// Génération du script sql qui permettra de réinitialiser les séquences à la bonne valeur

			String csvEntryName = "sequences.sql";
			ZipEntry entry = new ZipEntry(csvEntryName);
			zip.putNextEntry(entry);

			String nomFichierSql = "sauvegardes/sequences.sql";
			File fichierSql = new File(nomFichierSql);

			try  (FileWriter writer = new FileWriter(fichierSql) ) {

				ResultSet rsSequences = statement.executeQuery("SELECT sequence_name, base_value FROM INFORMATION_SCHEMA.SEQUENCES;");
				if ( rsSequences.first() ) {
					while ( ! rsSequences.isAfterLast() ) {
						String nomSequence = rsSequences.getString(1);
						Long nextVal = rsSequences.getLong(2);

						writer.write("ALTER SEQUENCE " 
								+ nomSequence 
								+ " RESTART WITH " 
								+ nextVal.toString() 
								+ ";\n");

						rsSequences.next();
					}
				}
				writer.close();
			};

			Files.copy(fichierSql.toPath(), zip);

			fichierSql.delete();
		} 
		catch (IOException | SQLException e) {

			throw new ServiceException(e, AdminTechniqueErreur.PROBLEME_SAUVEGARDE);
		}
	}

	public void restaurerBase(File fichierZip) throws ServiceException {

		try (Statement statement = dataSource.getConnection().createStatement();
				ZipFile zip = new ZipFile(fichierZip); ) {

			// Restauration des tables

			ResultSet rsTables = statement.executeQuery("SELECT table_name FROM INFORMATION_SCHEMA.TABLES where table_type = 'BASE TABLE' and table_schema = 'PUBLIC';");
			if ( rsTables.first() ) {

				while ( ! rsTables.isAfterLast() ) {
					String nomTable = rsTables.getString(1);

					String nomCsvEntry = nomTable + ".csv";
					ZipEntry entry = zip.getEntry(nomCsvEntry);

					if ( entry != null ) {
						InputStream fichierCvsZippe = zip.getInputStream(entry);
						String nomFichierCsv = "sauvegardes/" + nomTable + ".csv";
						File fichierCsv = new File(nomFichierCsv);

						Files.copy(fichierCvsZippe, fichierCsv.toPath(), StandardCopyOption.REPLACE_EXISTING);
						importerFromCsv(fichierCsv, nomTable);

						fichierCsv.delete();
					}

					rsTables.next();
				}
			}
			statement.close();
			
			// Restauration des séquences 

			String nomSqlEntry = "sequences.sql";
			ZipEntry entry = zip.getEntry(nomSqlEntry);

			if ( entry != null ) {
				InputStream fichierSqlZippe = zip.getInputStream(entry);
				String nomFichierSql = "sauvegardes/sequences.sql";
				File fichierSql = new File(nomFichierSql);

				Files.copy(fichierSqlZippe, fichierSql.toPath(), StandardCopyOption.REPLACE_EXISTING);
				executerScript(fichierSql);

				fichierSql.delete();
			}

		} catch (SQLException | IOException e) {

			throw new ServiceException(e, AdminTechniqueErreur.PROBLEME_RESTAURATION);
		};
	}

	public void desactiverContraintes() throws ServiceException {

		JdbcTemplate template = new JdbcTemplate(dataSource);

		String query = 
				"SET REFERENTIAL_INTEGRITY FALSE";

		logger.info("-> COMMANDE EXECUTEE {}", query);
		template.execute(query);			
	}

	public void desactiverContraintes(String nomTable) {

		JdbcTemplate template = new JdbcTemplate(dataSource);

		String query = 
				"ALTER TABLE " 
						+ nomTable 
						+ " SET REFERENTIAL_INTEGRITY FALSE";
		logger.info("-> COMMANDE EXECUTEE {}", query);
		template.execute(query);			
	}

	public void reactiverContraintes() throws ServiceException {

		try (Statement statement = dataSource.getConnection().createStatement()) {

			ResultSet rsTables = statement.executeQuery("SELECT table_name FROM INFORMATION_SCHEMA.TABLES where table_type = 'BASE TABLE' and table_schema = 'PUBLIC';");
			if ( rsTables.first() ) {
				while ( ! rsTables.isAfterLast() ) {
					String nomTable = rsTables.getString(1);
					reactiverContraintes(nomTable);
					rsTables.next();
				}
			}
			statement.close();
			
		} catch (SQLException e) {

				throw new ServiceException(e, AdminTechniqueErreur.ERREUR_REACTIVATION_CONTRAINTES);
		} 
	}

	public void reactiverContraintes(String nomTable) throws ServiceException {

		try {
			JdbcTemplate template = new JdbcTemplate(dataSource);

			String query = 
					"ALTER TABLE " 
							+ nomTable 
							+ " SET REFERENTIAL_INTEGRITY TRUE CHECK";
			logger.info("-> COMMANDE EXECUTEE {}", query);
			template.execute(query);
		}
		catch ( DataIntegrityViolationException e ) {

			throw new ServiceException(e, AdminFonctionnelleErreur.ERREUR_REACTIVATION_CONTRAINTES);
		}
	}

	public List<AdminSauvegarde> rechercherSauvegardesEnregistrees(File repertoireSauvegarde) {

		List<File> fichiersZips = Arrays.asList(repertoireSauvegarde.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".zip") || name.endsWith(".ZIP");
			}
		}));

		return fichiersZips
				.stream()
				.sorted((f1, f2) -> {return ((Long) f2.lastModified()).compareTo((Long)f1.lastModified());})
				.map((f) -> {
					AdminSauvegarde sauvegarde = new AdminSauvegarde();
					sauvegarde.setNom(f.getName());
					sauvegarde.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(f.lastModified())));
					return sauvegarde;})
				.toList();
	}
}
