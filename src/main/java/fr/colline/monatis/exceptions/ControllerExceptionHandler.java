package fr.colline.monatis.exceptions;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;

import fr.colline.monatis.erreurs.ErreurDto;
import fr.colline.monatis.erreurs.MonatisErreur;

@ControllerAdvice
public class ControllerExceptionHandler {

	@Autowired private ResourceBundleMessageSource bundleErreurs;

	private Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@ExceptionHandler
	public final ResponseEntity<ErreurDto> handleExceptions(
			@RequestHeader(name = "Accept-Language", required = false) final Locale locale,
			Throwable t) {

		if ( ! MonatisErreur.class.isAssignableFrom(t.getClass()) ) {
			t.printStackTrace();
		}
		
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

		if ( MonatisException.class.isAssignableFrom(t.getClass())){
			MonatisException ex = (MonatisException) t;
			erreurDto.code = ex.getErreur().getCode();
			try {
				erreurDto.libelle = bundleErreurs.getMessage(
						ex.getErreur().getPrefixe(), 
						ex.getValues(), 
						locale);
			}
			catch (NoSuchMessageException e) {
				erreurDto.libelle = t.getMessage();
			}
			erreurDto.typeErreur = ex.getErreur().getTypeErreur().getLibelle();
			erreurDto.typeDomaine = ex.getErreur().getTypeDomaine().getLibelle();
			log.info("Erreur MONATIS '{}' : {} - {}", erreurDto.typeErreur, erreurDto.code, erreurDto.libelle);
		}
		else {
			erreurDto.code = "ERREUR";
			erreurDto.libelle = t.getMessage();
			erreurDto.typeErreur = t.getClass().getSimpleName();
			erreurDto.typeDomaine = "non cataloguée";
			log.error("Erreur non cataloguée : {}", erreurDto.libelle);
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

	private HttpStatus getResponseStatus(Throwable t) {

		if ( MonatisException.class.isAssignableFrom(t.getClass()) ) {

			MonatisErreur erreur = ((MonatisException) t).getErreur();

			switch (erreur.getTypeErreur()) {
			case CONTROLE:
			case FONCTIONNELLE:
				return HttpStatus.BAD_REQUEST;
			default:
				return HttpStatus.INTERNAL_SERVER_ERROR;
			}				

		}
		else {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

}
