package com.github.geek.lyb.feign.client;


import com.github.geek.lyb.feign.dto.UserDTO;
import com.github.geek.lyb.feign.dto.UserPageDTO;
import com.github.geek.lyb.feign.model.User;
import com.github.geek.lyb.feign.spring.annotation.CustomFeignClient;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

@CustomFeignClient(url="http://localhost:8080")
public interface UserHttpClient {

    @RequestLine("POST /u/login")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    User login(UserDTO userDTO);


    @RequestLine("POST /u/loginJSON")
    @Headers("Content-Type: application/json")
    User loginJSON(UserDTO userDTO);


    @RequestLine("GET /u/findUserById?id={id}")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    User findUserById(@Param("id") Long id);


    @RequestLine("POST /u/saveOrUpdateUser")
    @Headers("Content-Type: application/json")
    Long saveOrUpdateUser(UserDTO userDTO);


    @RequestLine("POST /u/listPage")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    List<User> listPage(UserPageDTO userPageDTO);


    @RequestLine("GET /u/deleteUserById?id={id}")
    @Headers("Content-Type: application/json")
    boolean deleteUserById(@Param("id") Long idx);


}
