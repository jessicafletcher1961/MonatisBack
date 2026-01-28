package fr.colline.monatis.admin.sauvegarde;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SauvegardeService {

	@Autowired private JdbcTemplate jdbcTemplate;

	public void save() {

	        String sql = "pg_dump monatis > C:/Users/odani/Desktop/MONATIS/test_sauvegarde";

	        return;
	}
	
}
  