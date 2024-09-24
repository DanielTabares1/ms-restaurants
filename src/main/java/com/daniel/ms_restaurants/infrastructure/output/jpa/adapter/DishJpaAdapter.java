package com.daniel.ms_restaurants.infrastructure.output.jpa.adapter;

import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.spi.IDishPersistencePort;
import com.daniel.ms_restaurants.infrastructure.exception.DishNotFoundException;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.DishEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.IDishEntityMapper;
import com.daniel.ms_restaurants.infrastructure.output.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

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

    @Override
    public List<Dish> getAllDishesByRestaurantId(long restaurantId, int pageNumber, int pageSize) {

        Sort sort = Sort.by("category").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<DishEntity> dishEntityList = dishRepository.findAllByRestaurantId(restaurantId, pageable);
        return dishEntityList.stream().map(
                dishEntityMapper::toModel
        ).toList();
    }

    @Override
    public List<Dish> getAllDishesByRestaurantIdByCategoryId(long restaurantId, long categoryId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<DishEntity> dishEntityList = dishRepository.findAllByRestaurantIdAndCategoryId(restaurantId, categoryId, pageable);
        return dishEntityList.stream().map(
                dishEntityMapper::toModel
        ).toList();
    }
}
