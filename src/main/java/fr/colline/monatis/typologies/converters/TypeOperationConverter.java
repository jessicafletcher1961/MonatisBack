package fr.colline.monatis.typologies.converters;

import fr.colline.monatis.typologies.model.TypeOperation;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TypeOperationConverter implements AttributeConverter<TypeOperation, String> {

	@Override
	public String convertToDatabaseColumn(TypeOperation attribute) {
		return attribute == null ? null : attribute.getCode();
	}

	@Override
	public TypeOperation convertToEntityAttribute(String dbData) {
		return TypeOperation.findByCode(dbData);
	}
}
