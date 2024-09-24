package com.daniel.ms_restaurants.infrastructure.input.rest;

import com.daniel.ms_restaurants.application.handler.IDishHandler;
import com.daniel.ms_restaurants.domain.model.Dish;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client")
public class ClientController {

    private final IDishHandler dishHandler;

    @GetMapping("/dishes/{restaurantId}")
    public List<Dish> getAllDishesByRestaurantId(@PathVariable long restaurantId){
        return dishHandler.getAllDishesByRestaurantId(restaurantId);
    }

}
