package fr.colline.monatis.typologies.converters;

import fr.colline.monatis.typologies.model.TypeFonctionnement;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TypeFonctionnementConverter implements AttributeConverter<TypeFonctionnement, String> {

	@Override
	public String convertToDatabaseColumn(TypeFonctionnement attribute) {
		return attribute == null ? null : attribute.getCode();
	}

	@Override
	public TypeFonctionnement convertToEntityAttribute(String dbData) {
		return TypeFonctionnement.findByCode(dbData);
	}
}
