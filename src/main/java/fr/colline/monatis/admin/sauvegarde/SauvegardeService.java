package fr.colline.monatis.admin.sauvegarde;

import org.springframework.stereotype.Service;

@Service
public class SauvegardeService {

//	@Autowired private JdbcTemplate jdbcTemplate;

	public void dumpTables() {

//	        String sql = "SELECT * FROM compte_interne";
//
//	        List<CompteInterne> customers = jdbcTemplate.query(
//	                sql,
//	                new BeanPropertyRowMapper(CompteInterne.class));

	        return;

//		// Get a list of all table names
//	    List<Map<String, Object>> tableNames = jdbcTemplate.queryForList("SHOW TABLES");
//
//	    // Iterate over the table names
//	    for (Map<String, Object> rowTable : tableNames) {
//
//	    	String tableName = (String) rowTable.get("TABLE_NAME");
//	    	String schemaName = (String) rowTable.get("TABLE_SCHEMA");
//	    	
//	    	// Execute a SELECT * FROM <tableName> query
//	        List<Map<String, Rowset>> rows = jdbcTemplate.queryForList("SELECT * FROM " + rowTable.get("TABLE_NAME"));
//
//	        for ( Map<String, Object> row : rows ) {
//		        String listeColonnes = "";
//		        String listeValues = "";
//		        boolean first = true;
//	        	for ( Entry<String, Object> column : row.entrySet() ) {
//		        	if ( !first ) {
//		        		listeColonnes += ", ";
//		        		listeValues += ", ";
//		        	}
//		        	listeColonnes += column.getKey();
//		        	listeValues += column.getValue().toString();
//		        	first = false;
//	        	}
//		        String request = "INSERT INTO " + schemaName + "." + tableName + " (" + listeColonnes + ") values (" + listeValues + ")";
//		        System.out.println(request);
//	        }
//	    }
	}
	
}
  