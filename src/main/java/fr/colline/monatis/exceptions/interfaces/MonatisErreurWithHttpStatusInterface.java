package fr.colline.monatis.exceptions.interfaces;

import org.springframework.http.HttpStatus;

public interface MonatisErreurWithHttpStatusInterface 
extends MonatisErreurInterface {

	public HttpStatus getStatus();
}
