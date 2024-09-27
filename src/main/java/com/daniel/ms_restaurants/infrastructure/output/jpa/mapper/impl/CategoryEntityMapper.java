package com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.impl;

import com.daniel.ms_restaurants.domain.model.Category;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.CategoryEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.ICategoryEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryEntityMapper implements ICategoryEntityMapper {
    @Override
    public Category toModel(CategoryEntity categoryEntity) {
        return new Category(categoryEntity.getId(), categoryEntity.getName(), categoryEntity.getDescription());
    }

    @Override
    public CategoryEntity toEntity(Category category) {
        return new CategoryEntity(category.getId(), category.getName(), category.getDescription());
    }
}
