package com.cybersoft.food_project.controller;

import com.cybersoft.food_project.jwt.JwtTokenHelper;
import com.cybersoft.food_project.payload.request.SignInRequest;
import com.cybersoft.food_project.payload.response.DataResponse;
import com.cybersoft.food_project.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin //Cho phép những domain khác với domain của api truy cập vào.
@RequestMapping("/signin")
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenHelper jwtTokenHelper;

    @GetMapping("/test")
    public String test(){
        return "Hello";
    }

    @PostMapping("")
    public ResponseEntity<?> singin(@RequestBody SignInRequest request){

//        boolean isSuccess = loginService.checkLogin(request.getUsername(),request.getPassword());
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword());

        Authentication auth = authenticationManager.authenticate(authRequest);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        String token = jwtTokenHelper.generateToken(request.getUsername());
        String decoDeToken = jwtTokenHelper.decodeToken(token);

        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setSuccess(true);
        dataResponse.setDesc(decoDeToken);
        dataResponse.setData(token);

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

}
