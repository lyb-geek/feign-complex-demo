package com.github.geek.lyb.feign;

import com.github.geek.lyb.feign.db.MapDb;
import com.github.geek.lyb.feign.model.Bar;
import com.github.geek.lyb.feign.model.Foo;
import com.github.geek.lyb.feign.model.User;
import com.github.geek.lyb.feign.spring.annotation.EnableCustomFeignClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Slf4j
@EnableCustomFeignClients
public class FeignComplexDemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(FeignComplexDemoApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        log.info("demo数据初始化....");

        List<User> users = new ArrayList<>();

        User user = User.builder().id(MapDb.id.incrementAndGet()).age(20).name("张三").password("zhangsan").sex("男").build();

        User user1 = User.builder().id(MapDb.id.incrementAndGet()).age(25).name("李四").password("lisi").sex("男").build();

        User user2 = User.builder().id(MapDb.id.incrementAndGet()).age(18).name("翠花").password("cuihua").sex("女").build();

        User user3 = User.builder().id(MapDb.id.incrementAndGet()).age(30).name("王五").password("wangwu").sex("男").build();

        User user4 = User.builder().id(MapDb.id.incrementAndGet()).age(20).name("lily").password("lily123456").sex("女").build();

        users.add(user);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);

        MapDb.userDB.put(MapDb.TABLE_USER,users);



        List<Foo> foos = new ArrayList<>();
        List<Bar> bars = new ArrayList<>();
        Bar bar = Bar.builder().name("bar1").price(10.0).build();
        Bar bar1 = Bar.builder().name("bar2").price(12.0).build();
        bars.add(bar);
        bars.add(bar1);

        for (int i = 0; i < 5; i++) {
            Foo foo = Foo.builder().id(MapDb.fooIds.incrementAndGet()).name("foo"+i).bars(bars).build();
            foos.add(foo);
        }

        Foo foo = Foo.builder().id(MapDb.fooIds.incrementAndGet()).name("foo1").bars(bars).build();
        foos.add(foo);

        MapDb.fooDB.put(MapDb.TABLE_FOO,foos);






    }

}
