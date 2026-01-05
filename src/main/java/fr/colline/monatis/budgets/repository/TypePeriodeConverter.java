package fr.colline.monatis.budgets.repository;

import fr.colline.monatis.budgets.model.TypePeriode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TypePeriodeConverter implements AttributeConverter<TypePeriode, String> {

	@Override
	public String convertToDatabaseColumn(TypePeriode attribute) {
		return attribute == null ? null : attribute.getCode();
	}

	@Override
	public TypePeriode convertToEntityAttribute(String dbData) {
		return TypePeriode.findByCode(dbData);
	}
}
