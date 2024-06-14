package org.onesentence.onesentence.domain.category.service;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.category.dto.CategoryRequest;
import org.onesentence.onesentence.domain.category.entity.Category;
import org.onesentence.onesentence.domain.category.repository.CategoryJpaRepository;
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
}
