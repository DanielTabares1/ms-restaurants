package com.daniel.ms_restaurants.domain.spi;

import com.daniel.ms_restaurants.domain.model.Category;

import java.util.Optional;

public interface ICategoryPersistencePort {
    Optional<Category> getCategoryById(long id);
}
