package fr.colline.monatis.budget.repository;

import fr.colline.monatis.budget.model.TypeReference;
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

		return dbData == null ? null : TypeReference.findByCode(dbData);
	}
}
