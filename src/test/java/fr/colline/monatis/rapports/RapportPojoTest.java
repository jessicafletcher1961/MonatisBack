package fr.colline.monatis.rapports;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import fr.colline.monatis.rapports.controller.commun.EnteteCompteInterneResponseDto;

class RapportPojoTest {

	@Test
	void modelesEtDtosSontInstanciablesEtExposentLeursChamps() throws Exception {
		List<Class<?>> classes = classesRapportSimples();

		assertFalse(classes.isEmpty());

		for (Class<?> classe : classes) {
			Object instance = classe.getDeclaredConstructor().newInstance();

			for (Field field : classe.getDeclaredFields()) {
				if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
					continue;
				}

				Object valeur = valeurPour(field.getType());
				if (valeur == null && field.getType().isPrimitive()) {
					continue;
				}

				field.setAccessible(true);
				if (Modifier.isPublic(field.getModifiers())) {
					field.set(instance, valeur);
					assertEquals(valeur, field.get(instance), classe.getSimpleName() + "." + field.getName());
				}
				else {
					Method setter = rechercherSetter(classe, field);
					Method getter = rechercherGetter(classe, field);
					assertNotNull(setter, "Setter manquant pour " + classe.getName() + "." + field.getName());
					assertNotNull(getter, "Getter manquant pour " + classe.getName() + "." + field.getName());
					setter.invoke(instance, valeur);
					assertEquals(valeur, getter.invoke(instance), classe.getSimpleName() + "." + field.getName());
				}
			}
		}
	}

	private static List<Class<?>> classesRapportSimples() throws Exception {
		Path racine = Path.of("target", "classes", "fr", "colline", "monatis", "rapports");
		List<Class<?>> classes = new ArrayList<>();

		try (Stream<Path> paths = Files.walk(racine)) {
			for (Path path : paths.filter(path -> path.toString().endsWith(".class")).toList()) {
				String className = racine.relativize(path).toString()
						.replace(".class", "")
						.replace('\\', '.')
						.replace('/', '.');
				className = "fr.colline.monatis.rapports." + className;
				Class<?> classe = Class.forName(className);
				if (estPojoOuDtoRapport(classe)) {
					classes.add(classe);
				}
			}
		}

		return classes;
	}

	private static boolean estPojoOuDtoRapport(Class<?> classe) {
		if (classe.isInterface() || classe.isEnum() || classe.isAnnotation() || classe.isSynthetic()) {
			return false;
		}
		String nom = classe.getName();
		String simpleName = classe.getSimpleName();

		if (simpleName.equals("RapportController")
				|| simpleName.equals("RapportResponseDtoMapper")
				|| simpleName.equals("RapportResponsePdfMapper")
				|| simpleName.equals("csvController")) {
			return false;
		}

		return nom.startsWith("fr.colline.monatis.rapports.model.")
				|| (nom.startsWith("fr.colline.monatis.rapports.controller.")
						&& !nom.contains(".controller.csv."));
	}

	private static Method rechercherSetter(Class<?> classe, Field field) {
		String nom = "set" + capitaliser(field.getName());
		for (Method method : classe.getMethods()) {
			if (method.getName().equals(nom)
					&& method.getParameterCount() == 1
					&& method.getParameterTypes()[0].isAssignableFrom(field.getType()) == false) {
				continue;
			}
			if (method.getName().equals(nom) && method.getParameterCount() == 1) {
				return method;
			}
		}
		return null;
	}

	private static Method rechercherGetter(Class<?> classe, Field field) {
		String capitalized = capitaliser(field.getName());
		for (String nom : List.of("get" + capitalized, "is" + capitalized)) {
			try {
				return classe.getMethod(nom);
			}
			catch (NoSuchMethodException e) {
				// On essaie la convention suivante.
			}
		}
		return null;
	}

	private static String capitaliser(String valeur) {
		return Character.toUpperCase(valeur.charAt(0)) + valeur.substring(1);
	}

	private static Object valeurPour(Class<?> type) throws Exception {
		if (type == String.class) {
			return "valeur";
		}
		if (type == LocalDate.class) {
			return LocalDate.of(2026, 5, 16);
		}
		if (type == Long.class || type == long.class) {
			return 123L;
		}
		if (type == Integer.class || type == int.class) {
			return 123;
		}
		if (type == Double.class || type == double.class) {
			return 123.45D;
		}
		if (type == Boolean.class || type == boolean.class) {
			return true;
		}
		if (List.class.isAssignableFrom(type)) {
			return new ArrayList<>();
		}
		if (Set.class.isAssignableFrom(type)) {
			return new HashSet<>();
		}
		if (type.isArray()) {
			return Array.newInstance(type.getComponentType(), 0);
		}
		if (type.isEnum()) {
			return type.getEnumConstants()[0];
		}
		if (type.getName().equals("fr.colline.monatis.rapports.controller.commun.EnteteCompteResponseDto")) {
			return new EnteteCompteInterneResponseDto();
		}
		if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
			return null;
		}

		return type.getDeclaredConstructor().newInstance();
	}
}
