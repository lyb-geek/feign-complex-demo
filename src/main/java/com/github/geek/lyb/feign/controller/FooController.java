package com.github.geek.lyb.feign.controller;

import com.github.geek.lyb.feign.model.Foo;
import com.github.geek.lyb.feign.service.FooService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/foo")
@Slf4j
public class FooController {

    @Autowired
    private FooService fooService;


    @RequestMapping(value="/saveFoos")
    Long saveFoos(@RequestBody List<Foo> foos){
        log.info("{}",foos);
        return fooService.saveFoos(foos);
    }


    @RequestMapping(value="/deleteFoos")
    boolean deleteFoos(@RequestBody List<Long> ids){
        log.info("{}",ids);

        return fooService.deleteFoos(ids);
    }


    @RequestMapping(value="/getStrs")
    public String[] getStrs(){
        String[] test = {"a","b","c"};
        return test;
    }



    @RequestMapping(value="/getFooByName")
    public List<Foo> getFooByName(String fooName){
        log.info("fooName:{}",fooName);

        return fooService.getFooByName(fooName);

    }
}
