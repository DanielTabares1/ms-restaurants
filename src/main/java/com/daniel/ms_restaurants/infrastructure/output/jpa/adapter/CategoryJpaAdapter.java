package com.daniel.ms_restaurants.infrastructure.output.jpa.adapter;

import com.daniel.ms_restaurants.domain.model.Category;
import com.daniel.ms_restaurants.domain.spi.ICategoryPersistencePort;
import com.daniel.ms_restaurants.infrastructure.exception.CategoryNotFoundException;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.CategoryEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.ICategoryEntityMapper;
import com.daniel.ms_restaurants.infrastructure.output.jpa.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryJpaAdapter implements ICategoryPersistencePort {

    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;

    @Override
    public Category getCategoryById(long id) {
        CategoryEntity existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id " + id));
        return categoryEntityMapper.toModel(existingCategory);
    }
}
