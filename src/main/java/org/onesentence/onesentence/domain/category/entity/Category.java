package org.onesentence.onesentence.domain.category.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.onesentence.onesentence.domain.category.dto.CategoryRequest;

@Entity
@Getter
@Table(name = "category")
@NoArgsConstructor
@AllArgsConstructor
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String category;

	public Category(CategoryRequest request) {
		this.category = request.getCategory();
	}

	public void updateCategory(CategoryRequest request) {
		this.category = request.getCategory();
	}
}
