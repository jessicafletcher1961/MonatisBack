package fr.colline.monatis.operations.repository;

import fr.colline.monatis.operations.model.TypeProgrammation;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TypeProgrammationConverter implements AttributeConverter<TypeProgrammation, String> {

	@Override
	public String convertToDatabaseColumn(TypeProgrammation attribute) {
		return attribute == null ? null : attribute.getCode();	}

	@Override
	public TypeProgrammation convertToEntityAttribute(String dbData) {
		return TypeProgrammation.findByCode(dbData);
	}

}
