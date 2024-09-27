package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.domain.api.ICategoryServicePort;
import com.daniel.ms_restaurants.domain.exception.CategoryNotFoundException;
import com.daniel.ms_restaurants.domain.model.Category;
import com.daniel.ms_restaurants.domain.spi.ICategoryPersistencePort;

public class CategoryUseCase implements ICategoryServicePort {

    private final ICategoryPersistencePort categoryPersistencePort;

    public CategoryUseCase(ICategoryPersistencePort categoryPersistencePort) {
        this.categoryPersistencePort = categoryPersistencePort;
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryPersistencePort.getCategoryById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id " + id));
    }
}
