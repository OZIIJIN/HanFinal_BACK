package org.onesentence.onesentence.domain.category.controller;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.onesentence.onesentence.domain.category.dto.CategoryRequest;
import org.onesentence.onesentence.domain.category.dto.CategoryResponse;
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

	@DeleteMapping("/{categoryId}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {

		categoryService.deleteCategory(categoryId);

		return ResponseEntity.ok().build();
	}

	@GetMapping()
	public ResponseEntity<List<CategoryResponse>> getAllCategories() {

		List<CategoryResponse> responses = categoryService.getAllCategories();

		return ResponseEntity.ok().body(responses);
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long categoryId) {
		CategoryResponse response = categoryService.getCategory(categoryId);

		return ResponseEntity.ok().body(response);
	}
}
