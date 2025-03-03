package org.onesentence.onesentence.domain.category.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.category.dto.CategoryRequest;
import org.onesentence.onesentence.domain.category.dto.CategoryResponse;
import org.onesentence.onesentence.domain.category.entity.Category;
import org.onesentence.onesentence.domain.category.repository.CategoryJpaRepository;
import org.onesentence.onesentence.global.exception.ExceptionStatus;
import org.onesentence.onesentence.global.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

	private final CategoryJpaRepository categoryJpaRepository;

	@Override
	@Transactional
	public Long createCategory(CategoryRequest request) {
		Category category = new Category(request);
		Category savedCategory = categoryJpaRepository.save(category);

		return savedCategory.getId();
	}

	@Override
	@Transactional
	public Long updateCategory(CategoryRequest request, Long categoryId) {
		Category category = findById(categoryId);
		category.updateCategory(request);

		return category.getId();
	}

	@Override
	public Category findById(Long categoryId) {
		return categoryJpaRepository.findById(categoryId).orElseThrow(() -> new NotFoundException(
			ExceptionStatus.NOT_FOUND));
	}

	@Override
	@Transactional
	public void deleteCategory(Long categoryId) {
		Category category = findById(categoryId);
		categoryJpaRepository.delete(category);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryResponse> getAllCategories() {
		List<CategoryResponse> categoryResponses = new ArrayList<>();
		List<Category> categories = categoryJpaRepository.findAll();

		for(Category c : categories) {
			categoryResponses.add(CategoryResponse.from(c));
		}

		return categoryResponses;
	}

	@Override
	@Transactional(readOnly = true)
	public CategoryResponse getCategory(Long categoryId) {
		Category category = findById(categoryId);

		return CategoryResponse.from(category);
	}
}
