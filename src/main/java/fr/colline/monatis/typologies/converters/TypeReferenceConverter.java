package fr.colline.monatis.typologies.converters;

import fr.colline.monatis.typologies.model.TypeReference;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TypeReferenceConverter implements AttributeConverter<TypeReference, String> {

	@Override
	public String convertToDatabaseColumn(TypeReference attribute) {
		return attribute == null ? null : attribute.getCode();
	}

	@Override
	public TypeReference convertToEntityAttribute(String dbData) {
		return TypeReference.findByCode(dbData);
	}
}
