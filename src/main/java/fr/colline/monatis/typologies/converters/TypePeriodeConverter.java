package fr.colline.monatis.typologies.converters;

import fr.colline.monatis.typologies.model.TypePeriode;
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
