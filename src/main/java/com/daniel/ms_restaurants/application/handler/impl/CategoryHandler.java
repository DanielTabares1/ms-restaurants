package com.daniel.ms_restaurants.application.handler.impl;

import com.daniel.ms_restaurants.application.handler.ICategoryHandler;
import com.daniel.ms_restaurants.domain.api.ICategoryServicePort;
import com.daniel.ms_restaurants.domain.model.Category;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryHandler implements ICategoryHandler {

    private final ICategoryServicePort categoryServicePort;

    @Override
    public Category getCategoryById(long id) {
        return categoryServicePort.getCategoryById(id);
    }
}
