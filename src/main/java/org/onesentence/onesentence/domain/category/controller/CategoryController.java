package org.onesentence.onesentence.domain.category.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.category.dto.CategoryRequest;
import org.onesentence.onesentence.domain.category.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

	private final CategoryService categoryService;


	@PostMapping
	public ResponseEntity<String> createCategory(@RequestBody CategoryRequest request) {

		Long categoryId = categoryService.createCategory(request);

		return ResponseEntity.created(URI.create("/api/v1/categories/" + categoryId)).build();
	}

	@PutMapping("/{categoryId}")
	public ResponseEntity<String> updateCategory(@RequestBody CategoryRequest request,
		@PathVariable Long categoryId) {

		Long updatedCategoryId = categoryService.updateCategory(request, categoryId);

		return ResponseEntity.created(URI.create("/api/v1/categories/" + updatedCategoryId)).build();
	}
}
