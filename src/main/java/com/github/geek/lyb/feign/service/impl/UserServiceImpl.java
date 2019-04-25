package com.github.geek.lyb.feign.service.impl;

import com.github.geek.lyb.feign.db.MapDb;
import com.github.geek.lyb.feign.dto.UserDTO;
import com.github.geek.lyb.feign.model.User;
import com.github.geek.lyb.feign.service.UserService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private Map<String,List<User>> userMap = MapDb.userDB;

    @Override
    public User login(UserDTO userDTO) {
        List<User> users = userMap.get(MapDb.TABLE_USER);

       List<User> loginUsers = users.stream()
                .filter(user->user.getName().equals(userDTO.getName()))
                .filter(user->user.getPassword().equals(userDTO.getPassword()))
                .collect(Collectors.toList());


       if(!CollectionUtils.isEmpty(loginUsers)){
           return loginUsers.get(0);
       }else{
           log.error("login fail: {} ",userDTO);
       }

        return new User();
    }

    @Override
    public User findUserById(Long id) {
        List<User> users = userMap.get(MapDb.TABLE_USER);

        users = users.stream().filter(user->user.getId() == id).collect(Collectors.toList());

        if(!CollectionUtils.isEmpty(users)){
            return users.get(0);
        }else{
            log.error("not found userId : {} user",id);
        }
        return new User();
    }

    @Override
    public Long saveOrUpdateUser(UserDTO userDTO) {

        List<User> users = userMap.get(MapDb.TABLE_USER);
        User user = new User();

       final Long id = userDTO.getId();
       if(id != null) {
           users = users.stream().filter(u -> u.getId() == id).collect(Collectors.toList());

           if (!CollectionUtils.isEmpty(users)) {
                user = users.get(0);
               BeanUtils.copyProperties(userDTO,user);
           }

       }else{
           Long newId = MapDb.id.incrementAndGet();
           BeanUtils.copyProperties(userDTO,user);
           user.setId(newId);
           users.add(user);

       }


        log.info("saveOrUpdateUser success, {}",user);

        MapDb.refreshDB();
        return user.getId();
    }

    @Override
    public List<User> listPage(UserDTO userDTO, int pageNo, int pageSize) {
        List<User> users = userMap.get(MapDb.TABLE_USER);
        if(userDTO.getId() != null){
            users = users.stream().filter(u->u.getId() == userDTO.getId()).collect(Collectors.toList());
        }

        if(userDTO.getAge() != null){
            users = users.stream().filter(u->u.getAge() == userDTO.getAge()).collect(Collectors.toList());
        }

        if(!StringUtils.isEmpty(userDTO.getName())){
            users = users.stream().filter(u->u.getName().equals(userDTO.getName())).collect(Collectors.toList());
        }

        if(!StringUtils.isEmpty(userDTO.getSex())){
            users = users.stream().filter(u->u.getSex().equals(userDTO.getSex())).collect(Collectors.toList());
        }

        if(!CollectionUtils.isEmpty(users)){
            pageNo = pageNo - 1;
            int fromIndex = pageNo * pageSize;
            if(fromIndex > users.size()){
               log.error("页数或分页大小不正确!");
               return new ArrayList<>();
            }

            int toIndex = ((pageNo + 1) * pageSize);
            if (toIndex > users.size()) {
                toIndex = users.size();
            }
            return users.subList(fromIndex, toIndex);

        }
        return users;
    }

    @Override
    public boolean deleteUserById(Long id) {
        List<User> users = userMap.get(MapDb.TABLE_USER);

       List<User> deleteUsers = users.stream().filter(user->user.getId() == id).collect(Collectors.toList());

        if(!CollectionUtils.isEmpty(deleteUsers)){
            users.removeAll(deleteUsers);
            MapDb.refreshDB();
            return true;
        }
        return false;
    }
}
