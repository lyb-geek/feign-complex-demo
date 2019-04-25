package com.github.geek.lyb.feign.service;

;

import com.github.geek.lyb.feign.dto.UserDTO;
import com.github.geek.lyb.feign.model.User;

import java.util.List;

public interface UserService {

    User login(UserDTO userDTO);


    User findUserById(Long id);


    Long saveOrUpdateUser(UserDTO userDTO);


    List<User> listPage(UserDTO userDTO, int pageNo, int pageSize);


    boolean deleteUserById(Long id);


}
