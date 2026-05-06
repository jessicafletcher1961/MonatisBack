package fr.colline.monatis.admin;

import java.util.List;

public class AdminResponseDtoMapper {

	public static List<AdminSauvegardeResponseDto> mapperListAdminSauvegarde(List<AdminSauvegarde> sauvegardes) {

		return sauvegardes
				.stream()
				.map((s) -> {
					AdminSauvegardeResponseDto dto = new AdminSauvegardeResponseDto();
					dto.nom = s.getNom();
					dto.date = s.getDate();
					return dto;})
				.toList();
	}

}
