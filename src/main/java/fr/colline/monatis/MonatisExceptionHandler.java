package fr.colline.monatis;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;

import fr.colline.monatis.exceptions.ErreurDto;
import fr.colline.monatis.exceptions.interfaces.MonatisErreurInterface;
import fr.colline.monatis.exceptions.interfaces.MonatisErreurWithHttpStatusInterface;
import fr.colline.monatis.exceptions.interfaces.MonatisExceptionInterface;

@ControllerAdvice
public class MonatisExceptionHandler {

    @Autowired private ResourceBundleMessageSource messagesErreurs;

	@ExceptionHandler
	public final ResponseEntity<ErreurDto> handleExceptions(
			@RequestHeader(name = "Accept-Language", required = false) final Locale locale,
			Throwable t) {

		t.printStackTrace();
		
		ErreurDto erreurDto = construitErreurDto(
				t, 
				locale);

		HttpStatus status = getResponseStatus(t);
		
		return new ResponseEntity<>(erreurDto, status);
	}

	private ErreurDto construitErreurDto(
			Throwable t,
			Locale locale) {
		
		ErreurDto erreurDto = new ErreurDto();

		if ( MonatisExceptionInterface.class.isAssignableFrom(t.getClass())){
			MonatisExceptionInterface ex = (MonatisExceptionInterface) t;
			erreurDto.code = ex.getCode();
			try {
				erreurDto.libelle = messagesErreurs.getMessage(
								ex.getCode(), 
								ex.getValues(), 
								locale);
			}
			catch (NoSuchMessageException e) {
				erreurDto.libelle = t.getMessage();
			}
			erreurDto.type = ex.getErreur().getType().getLibelle();
		}
		else {
			erreurDto.code = "ERREUR";
			erreurDto.libelle = t.getMessage();
			erreurDto.type = t.getClass().getSimpleName();
		}

		if ( t.getCause() != null ) {
			erreurDto.cause = construitErreurDto(
					t.getCause(), 
					locale);
		}
		else {
			erreurDto.cause = null;
		}
		
		return erreurDto;
	}
	
	public HttpStatus getResponseStatus(Throwable t) {
		
		if ( MonatisExceptionInterface.class.isAssignableFrom(t.getClass()) ) {
			MonatisErreurInterface erreur = ((MonatisExceptionInterface) t).getErreur();
			if ( MonatisErreurWithHttpStatusInterface.class.isAssignableFrom(erreur.getClass()) ) {
				return ((MonatisErreurWithHttpStatusInterface) erreur).getStatus();
			}
			else {
				switch (erreur.getType()) {
				case CONTROLE:
				case FONCTIONNELLE:
					return HttpStatus.BAD_REQUEST;
				default:
					return HttpStatus.INTERNAL_SERVER_ERROR;
				}				
			}
		}
		else {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}
}
