package com.daniel.ms_restaurants.infrastructure.output.jpa.adapter;

import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.spi.IDishPersistencePort;
import com.daniel.ms_restaurants.infrastructure.exception.DishNotFoundException;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.DishEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.IDishEntityMapper;
import com.daniel.ms_restaurants.infrastructure.output.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    @Override
    public Dish createDish(Dish dish) {
        DishEntity dishEntity = dishEntityMapper.toEntity(dish);
        DishEntity newDish = dishRepository.save(dishEntity);
        return dishEntityMapper.toModel(newDish);
    }

    @Override
    public Dish editDish(long dishId, Dish editedDish) {
        DishEntity dishEntity = dishEntityMapper.toEntity(editedDish);
        DishEntity dishEntityEdited = dishRepository.save(dishEntity);
        return dishEntityMapper.toModel(dishEntityEdited);
    }

    @Override
    public Dish getDishById(long id) {
        DishEntity dishEntity = dishRepository.findById(id).orElseThrow(
                () -> new DishNotFoundException("Dish not found with id " + id)
        );
        return dishEntityMapper.toModel(dishEntity);
    }
}
