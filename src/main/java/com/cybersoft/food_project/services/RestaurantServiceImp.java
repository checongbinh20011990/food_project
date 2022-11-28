package com.cybersoft.food_project.services;

import com.cybersoft.food_project.dto.ResraurantDTO;
import com.cybersoft.food_project.dto.RestaurantDetailDTO;
import com.cybersoft.food_project.entity.RestaurantEntity;
import com.cybersoft.food_project.entity.RestaurantReviewEntity;
import com.cybersoft.food_project.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImp implements RestaurantService{

    @Autowired
    RestaurantRepository restaurantRepository;

    @Override
    public List<ResraurantDTO> getRestaurants() {
        List<ResraurantDTO> dtos = new ArrayList<>();
        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();
        //Xử lý data [{title: "",image: "", avgRate: 3.8}]
        for (RestaurantEntity data : restaurantEntities) {
            ResraurantDTO resraurantDTO = new ResraurantDTO();
            resraurantDTO.setTitle(data.getName());
//            "http://localhost:8080/api/" + data.getImage()
            resraurantDTO.setImage(data.getImage());

            float avgRate = 0;
            float sumRate = 0;
            for (RestaurantReviewEntity dataReview: data.getRestaurantReviews()) {
                sumRate += dataReview.getRate();
            }
            if(data.getRestaurantReviews().size() > 0){
                avgRate = sumRate/data.getRestaurantReviews().size();
            }
            resraurantDTO.setAvgRate(avgRate);
            dtos.add(resraurantDTO);
        }

        return dtos;
    }

    @Override
    public RestaurantDetailDTO getDetailRestaurant(int id) {
        //Optional : tức là có hoặc không có cũng được ( Dữ liệu có thể bị null )
        Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findById(id);
        RestaurantDetailDTO restaurantDetailDTO = new RestaurantDetailDTO();
        if(restaurantEntity.isPresent()){
            //Có giá trị thì xử lý
            restaurantDetailDTO.setTitle(restaurantEntity.get().getName());
            restaurantDetailDTO.setImage(restaurantEntity.get().getImage());
//            restaurantDetailDTO.setDesc();
            float avgRate = 0;
            float sumRate = 0;
            for (RestaurantReviewEntity dataReview: restaurantEntity.get().getRestaurantReviews()) {
                sumRate += dataReview.getRate();
            }
            if(restaurantEntity.get().getRestaurantReviews().size() > 0){
                avgRate = sumRate/restaurantEntity.get().getRestaurantReviews().size();
            }
            restaurantDetailDTO.setAvgRate(avgRate);
        }

        return restaurantDetailDTO;
    }


}
