package com.cybersoft.food_project.services;

import com.cybersoft.food_project.dto.ResraurantDTO;
import com.cybersoft.food_project.dto.RestaurantDetailDTO;
import com.cybersoft.food_project.entity.RestaurantEntity;

import java.util.List;

public interface RestaurantService {
    List<ResraurantDTO> getRestaurants();
    RestaurantDetailDTO getDetailRestaurant(int id);
}
