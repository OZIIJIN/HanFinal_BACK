package org.onesentence.onesentence.domain.category.repository;

import org.onesentence.onesentence.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

}
