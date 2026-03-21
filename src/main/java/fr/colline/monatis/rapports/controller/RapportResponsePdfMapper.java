package fr.colline.monatis.rapports.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.rapports.RapportControleErreur;
import fr.colline.monatis.rapports.model.ReleveOperationCompte;
import fr.colline.monatis.references.model.Banque;
import fr.colline.monatis.references.model.Titulaire;
import jakarta.servlet.ServletOutputStream;

public class RapportResponsePdfMapper {

	public static void mapperReleveCompteToPdf(ReleveOperationCompte releve, ServletOutputStream stream) throws ControllerException {

		try {
			PdfWriter pdfWriter = new PdfWriter(stream);
			PdfDocument pdfDocument = new PdfDocument(pdfWriter);
			Document document = new Document(pdfDocument);

			switch ( releve.getCompte().getTypeCompte() ) {
			case INTERNE:
				CompteInterne compteInterne = (CompteInterne) releve.getCompte();

				if ( compteInterne.getBanque() != null ) {
					Banque banque = compteInterne.getBanque();
					String parBanque = "BANQUE : "
							.concat(banque.getNom())
							.concat(" - ")
							.concat(banque.getLibelle());
					document.add(new Paragraph(parBanque));
				}

				String parCompteInterne = "COMPTE : "
						.concat(compteInterne.getIdentifiant())
						.concat(" - ")
						.concat(compteInterne.getLibelle());
				document.add(new Paragraph(parCompteInterne));

				if ( compteInterne.getTitulaires() != null && ! compteInterne.getTitulaires().isEmpty() ) {
					for ( Titulaire titulaire : compteInterne.getTitulaires() ) {
						String parTitulaire = "TITULAIRE : "
								.concat(titulaire.getNom())
								.concat(" - ")
								.concat(titulaire.getLibelle());
						document.add(new Paragraph(parTitulaire));
					}
				}

				break;
			default:
				String parCompte = "COMPTE : "
						.concat(releve.getCompte().getIdentifiant())
						.concat(" - ")
						.concat(releve.getCompte().getLibelle());
				document.add(new Paragraph(parCompte));
				break;
			}

			document.close();
		}
		catch ( Throwable t ) {
			throw new ControllerException(
					RapportControleErreur.GENERATION_PDF_EN_ECHEC,
					t);
		}
	}

}
