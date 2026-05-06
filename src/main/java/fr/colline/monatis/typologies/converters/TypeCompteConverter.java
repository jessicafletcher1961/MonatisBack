package fr.colline.monatis.typologies.converters;

import fr.colline.monatis.typologies.model.TypeCompte;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TypeCompteConverter implements AttributeConverter<TypeCompte, String> {

	@Override
	public String convertToDatabaseColumn(TypeCompte attribute) {
		return attribute == null ? null : attribute.getCode();
	}

	@Override
	public TypeCompte convertToEntityAttribute(String dbData) {
		return TypeCompte.findByCode(dbData);
	}
}
