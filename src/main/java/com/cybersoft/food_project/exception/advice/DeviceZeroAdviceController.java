package com.cybersoft.food_project.exception.advice;

import com.cybersoft.food_project.exception.DeviceZeroException;
import com.cybersoft.food_project.payload.response.DataResponse;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.websocket.DecodeException;

@ControllerAdvice
public class DeviceZeroAdviceController {
    Logger logger = LoggerFactory.getLogger(DeviceZeroAdviceController.class);
    private Gson gson = new Gson();

    @ExceptionHandler({DeviceZeroException.class,RuntimeException.class})
    public ResponseEntity<?> handDeviceZeroException(Exception e){
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        dataResponse.setDesc("Có lỗi xảy ra " + e.getMessage());

        logger.error(gson.toJson(dataResponse));

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

}
