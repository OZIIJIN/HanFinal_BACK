package org.onesentence.onesentence.domain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.onesentence.onesentence.domain.category.entity.Category;

@Getter
@AllArgsConstructor
public class CategoryResponse {

	private Long categoryId;

	private String category;

	public CategoryResponse from(Category category) {
		return new CategoryResponse(
			category.getId(),
			category.getCategory()
		);
	}

}
