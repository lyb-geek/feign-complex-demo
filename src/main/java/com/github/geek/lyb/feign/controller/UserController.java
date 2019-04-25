package com.github.geek.lyb.feign.controller;

import com.github.geek.lyb.feign.dto.UserDTO;
import com.github.geek.lyb.feign.model.User;
import com.github.geek.lyb.feign.service.UserService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/u")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/login")
   public User login(UserDTO userDTO){
        log.info("{}",userDTO);
       return userService.login(userDTO);
   }


    @RequestMapping(value = "/loginJSON")
    public User loginJSON(@RequestBody UserDTO userDTO){
        log.info("{}",userDTO);
        return userService.login(userDTO);
    }


   @RequestMapping(value = "/findUserById")
   public User findUserById(Long id){
       log.info("{}",id);
       return userService.findUserById(id);
   }


    @RequestMapping(value = "/saveOrUpdateUser")
   public Long saveOrUpdateUser(@RequestBody UserDTO userDTO){
        log.info("{}",userDTO);
       return userService.saveOrUpdateUser(userDTO);
   }


    @RequestMapping(value = "/listPage")
   public List<User> listPage( UserDTO userDTO, int pageIndex, int pageSize){
        log.info("userDTO:{},pageIndex:{},pageSize:{}",userDTO,pageIndex,pageSize);
       return userService.listPage(userDTO,pageIndex,pageSize);
   }


    @RequestMapping(value = "/deleteUserById")
   public boolean deleteUserById(Long id){
        log.info("{}",id);
       return userService.deleteUserById(id);
   }
}
