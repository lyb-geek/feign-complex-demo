package com.github.geek.lyb.feign.service;


import com.github.geek.lyb.feign.model.Foo;

import java.util.List;

public interface FooService {

    Long saveFoos(List<Foo> foos);


    boolean deleteFoos(List<Long> ids);

    List<Foo> getFooByName(String fooName);
}
