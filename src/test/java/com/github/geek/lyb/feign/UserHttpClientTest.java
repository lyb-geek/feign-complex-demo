package com.github.geek.lyb.feign;


import com.github.geek.lyb.feign.client.UserHttpClient;
import com.github.geek.lyb.feign.constant.Constant;
import com.github.geek.lyb.feign.dto.UserDTO;
import com.github.geek.lyb.feign.dto.UserPageDTO;
import com.github.geek.lyb.feign.model.User;
import com.github.geek.lyb.feign.util.FeignClientUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.util.List;


public class UserHttpClientTest {

    private UserHttpClient userHttpClient;


    @Before
    public void before(){
        userHttpClient = FeignClientUtils.getInstance().getClient(UserHttpClient.class, Constant.REMOTE_URL);
    }


    @Test
    public void testLogin(){
        UserDTO userDTO = UserDTO.builder().name("张三").password("zhangsan").build();

        User user = userHttpClient.login(userDTO);

        Assert.assertNotNull(user.getId());
    }


    @Test
    public void testLoginJSON(){
        UserDTO userDTO = UserDTO.builder().name("张三").password("zhangsan").build();

        User user = userHttpClient.loginJSON(userDTO);

        Assert.assertNotNull(user.getId());
    }


    @Test
    public void testDeleteUserById(){

        boolean isDelete = userHttpClient.deleteUserById(1L);

        Assert.assertTrue(isDelete);
    }


    @Test
    public void testSaveUser(){

        UserDTO userDTO = UserDTO.builder().name("李雷").password("韩梅梅").sex("男").age(25).build();

        long id = userHttpClient.saveOrUpdateUser(userDTO);

        Assert.assertEquals(6L,id);
    }


    @Test
    public void testUpdateUser(){

        UserDTO userDTO = new UserDTO();

        User user = userHttpClient.findUserById(1L);

        BeanUtils.copyProperties(user,userDTO);

        userDTO.setPassword("123456");


        long id = userHttpClient.saveOrUpdateUser(userDTO);

        Assert.assertEquals(user.getId().longValue(),id);
    }


    @Test
    public void testListPage(){
        UserDTO userDTO = new UserDTO();
//        userDTO.setAge(25);
//        userDTO.setId(2L);
//        userDTO.setName("李四");
        userDTO.setSex("男");

        UserPageDTO userPageDTO = UserPageDTO.builder().userDTO(userDTO).pageNo(1).pageSize(5).build();

        List<User> users = userHttpClient.listPage(userPageDTO);

        users.forEach(user-> System.out.println(user));
    }
}
