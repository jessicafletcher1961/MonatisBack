package fr.colline.monatis.configuration;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import fr.colline.monatis.erreurs.ControllerVerificateurService;

@Configuration
public class LocaleConfig {

    @Bean
    public AcceptHeaderLocaleResolver localeResolver() {
        final AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.FRENCH);
        return resolver;
    }

    @Bean(name = "verificateur")
    public ControllerVerificateurService verificateur() {
    	final ControllerVerificateurService verificateur = new ControllerVerificateurService();
    	return verificateur;
    }
    
    @Bean(name = "bundleErreurs")
    public ResourceBundleMessageSource bundleErreurs() {
    	final ResourceBundleMessageSource source = new ResourceBundleMessageSource();
    	source.setBasename("internationalization/erreurs");
    	source.setDefaultEncoding("UTF-8");    	
    	return source;
    }

    @Bean(name = "bundleMessages")
    public ResourceBundleMessageSource bundleMessages() {
    	final ResourceBundleMessageSource source = new ResourceBundleMessageSource();
    	source.setBasename("internationalization/messages");
    	source.setDefaultEncoding("UTF-8");    	
    	return source;
    }
}