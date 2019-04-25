package com.github.geek.lyb.feign;


import com.github.geek.lyb.feign.client.FooHttpClient;
import com.github.geek.lyb.feign.constant.Constant;
import com.github.geek.lyb.feign.dto.FooDTO;
import com.github.geek.lyb.feign.model.Bar;
import com.github.geek.lyb.feign.model.Foo;
import com.github.geek.lyb.feign.util.FeignClientUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FooHttpClientSpringTest {

    @Autowired
    private FooHttpClient fooHttpClient;





    @Test
    public void testSaveFoos(){
        List<Foo> foos = new ArrayList<>();
        List<Bar> bars = new ArrayList<>();
        Bar bar = Bar.builder().name("barTEST").price(17.0).build();
        Bar bar1 = Bar.builder().name("bar").price(16.0).build();
        bars.add(bar);
        bars.add(bar1);

        List<Bar> bars1 = new ArrayList<>();
        Bar bar2 = Bar.builder().name("bar100").price(17.17).build();
        Bar bar3 = Bar.builder().name("bar99").price(16.16).build();
        bars1.add(bar2);
        bars1.add(bar3);

        Foo foo = Foo.builder().bars(bars).name("fooTEST").build();
        Foo foo1 = Foo.builder().bars(bars1).name("foo").build();
        foos.add(foo);
        foos.add(foo1);

        Long addSize = fooHttpClient.saveFoos(foos);

        Assert.assertEquals(Long.valueOf(foos.size()),addSize);
    }


    @Test
    public void testDeleteUserById(){

        List<Long> ids = Arrays.asList(1L,2L,3L);

        boolean deleteFlag = fooHttpClient.deleteFoos(ids);

        Assert.assertTrue(deleteFlag);
    }


    @Test
    public void testStrs(){



        String[] test = fooHttpClient.getStrs();

        Assert.assertNotNull(test);

        System.out.println(Arrays.toString(test));
    }



    @Test
    public void testGetFooByName(){
        FooDTO fooDTO = FooDTO.builder().name("foo1").build();

        List<Foo> foos = fooHttpClient.getFooByName(fooDTO);

        Assert.assertNotNull(foos);

        foos.forEach(foo -> System.out.println(foo));

    }




}
