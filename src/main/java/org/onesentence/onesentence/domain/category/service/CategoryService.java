package org.onesentence.onesentence.domain.category.service;

import org.onesentence.onesentence.domain.category.dto.CategoryRequest;
import org.onesentence.onesentence.domain.category.entity.Category;

public interface CategoryService {

	Long createCategory(CategoryRequest request);

	Long updateCategory(CategoryRequest request, Long categoryId);

	Category findById(Long categoryId);

	void deleteCategory(Long categoryId);
}
