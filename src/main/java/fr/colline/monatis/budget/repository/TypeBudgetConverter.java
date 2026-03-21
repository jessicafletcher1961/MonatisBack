package fr.colline.monatis.budget.repository;

import fr.colline.monatis.budget.model.TypeBudget;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TypeBudgetConverter implements AttributeConverter<TypeBudget, String> {

	@Override
	public String convertToDatabaseColumn(TypeBudget attribute) {
		
		return attribute == null ? null : attribute.getCode();
	}

	@Override
	public TypeBudget convertToEntityAttribute(String dbData) {

		return dbData == null ? null : TypeBudget.findByCode(dbData);
	}
}
