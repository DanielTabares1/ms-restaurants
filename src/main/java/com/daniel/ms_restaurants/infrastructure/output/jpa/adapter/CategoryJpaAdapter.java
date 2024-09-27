package com.daniel.ms_restaurants.infrastructure.output.jpa.adapter;

import com.daniel.ms_restaurants.domain.model.Category;
import com.daniel.ms_restaurants.domain.spi.ICategoryPersistencePort;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.ICategoryEntityMapper;
import com.daniel.ms_restaurants.infrastructure.output.jpa.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CategoryJpaAdapter implements ICategoryPersistencePort {

    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;

    @Override
    public Optional<Category> getCategoryById(long id) {
        return categoryRepository.findById(id).map(
                categoryEntityMapper::toModel
        );
    }
}
