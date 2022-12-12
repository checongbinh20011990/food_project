package com.cybersoft.food_project.services;

import com.cybersoft.food_project.dto.ResraurantDTO;
import com.cybersoft.food_project.dto.RestaurantDetailDTO;
import com.cybersoft.food_project.entity.FoodEntity;
import com.cybersoft.food_project.entity.RestaurantEntity;
import com.cybersoft.food_project.entity.RestaurantReviewEntity;
import com.cybersoft.food_project.model.FoodModel;
import com.cybersoft.food_project.repository.RestaurantRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImp implements RestaurantService{

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    RedisTemplate redisTemplate;

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
//    @Cacheable("detail_restaurent")
    public RestaurantDetailDTO getDetailRestaurant(int id) {
        String key = "res" + id;
        Gson gson = new Gson();
        RestaurantDetailDTO restaurantDetailDTO = new RestaurantDetailDTO();

        if(redisTemplate.hasKey(key)){
            //Key có tồn tại
            String data = (String) redisTemplate.opsForValue().get(key);
            restaurantDetailDTO = gson.fromJson(data,RestaurantDetailDTO.class);
        }else{
            //Key không tồn tại
            //Optional : tức là có hoặc không có cũng được ( Dữ liệu có thể bị null )
            Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findById(id);

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

                List<FoodModel> foodModels = new ArrayList<>();
                for (FoodEntity foodEntity:restaurantEntity.get().getFoods()) {
                    FoodModel foodModel = new FoodModel();
                    foodModel.setId(foodEntity.getId());
                    foodModel.setName(foodEntity.getName());
                    foodModel.setImage(foodEntity.getImage());
                    foodModel.setPrice(foodEntity.getPrice());

                    foodModels.add(foodModel);
                }

                restaurantDetailDTO.setFoods(foodModels);
            }

            String json = gson.toJson(restaurantDetailDTO);
            redisTemplate.opsForValue().set(key,json);
        }



        return restaurantDetailDTO;
    }
    @Override
    @CacheEvict( value = "detail_restaurent", allEntries = true)
    public void clearAllCache(){}

}
