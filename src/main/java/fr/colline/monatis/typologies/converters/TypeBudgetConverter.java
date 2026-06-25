package fr.colline.monatis.typologies.converters;

import fr.colline.monatis.typologies.model.TypeBudget;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TypeBudgetConverter implements AttributeConverter<TypeBudget, String>  {

	@Override
	public String convertToDatabaseColumn(TypeBudget attribute) {
		return attribute == null ? null : attribute.getCode();
	}

	@Override
	public TypeBudget convertToEntityAttribute(String dbData) {
		return TypeBudget.findByCode(dbData);
	}

}
