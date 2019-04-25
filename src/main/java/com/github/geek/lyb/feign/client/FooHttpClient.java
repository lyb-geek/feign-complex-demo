package com.github.geek.lyb.feign.client;



import com.github.geek.lyb.feign.dto.FooDTO;
import com.github.geek.lyb.feign.model.Foo;
import com.github.geek.lyb.feign.spring.annotation.CustomFeignClient;
import feign.Headers;
import feign.RequestLine;

import java.util.List;

@CustomFeignClient(url="http://localhost:8080")
public interface FooHttpClient {

    @RequestLine("POST /foo/saveFoos")
    @Headers("Content-Type: application/json")
    Long saveFoos(List<Foo> foos);


    @RequestLine("POST /foo/deleteFoos")
    @Headers("Content-Type: application/json")
    boolean deleteFoos(List<Long> ids);


    @RequestLine("GET /foo/getStrs")
    @Headers("Content-Type: application/json")
    String[] getStrs();


    @RequestLine("GET /foo/getFooByName")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    List<Foo> getFooByName(FooDTO fooDTO);


}
