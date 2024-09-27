package com.daniel.ms_restaurants.domain.api;

import com.daniel.ms_restaurants.domain.model.Category;

public interface ICategoryServicePort {
    Category getCategoryById(long id);
}
