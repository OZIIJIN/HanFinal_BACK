package org.onesentence.onesentence.domain.category.controller;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.onesentence.onesentence.domain.category.dto.CategoryRequest;
import org.onesentence.onesentence.domain.category.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
