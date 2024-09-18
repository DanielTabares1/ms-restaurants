package com.daniel.ms_restaurants.domain.spi;

import com.daniel.ms_restaurants.domain.model.Category;

public interface ICategoryPersistencePort {
    Category getCategoryById(long id);
}
