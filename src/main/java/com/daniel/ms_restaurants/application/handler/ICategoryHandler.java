package com.daniel.ms_restaurants.application.handler;

import com.daniel.ms_restaurants.domain.model.Category;

public interface ICategoryHandler {
    Category getCategoryById(long id);
}
