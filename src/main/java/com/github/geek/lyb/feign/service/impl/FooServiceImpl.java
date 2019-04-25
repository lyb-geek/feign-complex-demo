package com.github.geek.lyb.feign.service.impl;


import com.github.geek.lyb.feign.db.MapDb;
import com.github.geek.lyb.feign.model.Foo;
import com.github.geek.lyb.feign.service.FooService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FooServiceImpl implements FooService {

    private Map<String,List<Foo>> fooMap = MapDb.fooDB;

    @Override
    public Long saveFoos(List<Foo> foos) {
        if(CollectionUtils.isEmpty(foos)){
            return -1L;
        }
        List<Foo> dbFoos = fooMap.get(MapDb.TABLE_FOO);

        foos.forEach(foo->{
            foo.setId(MapDb.fooIds.incrementAndGet());
            dbFoos.add(foo);
        });

        MapDb.refreshFooDB();

        return Long.valueOf(foos.size());
    }

    @Override
    public boolean deleteFoos(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return false;
        }

        List<Foo> dbFoos = fooMap.get(MapDb.TABLE_FOO);

        List<Foo> deleteFoos = dbFoos.stream().filter(foo->ids.contains(foo.getId())).collect(Collectors.toList());

        if(!CollectionUtils.isEmpty(deleteFoos)){
            dbFoos.removeAll(deleteFoos);
            MapDb.refreshFooDB();
            return true;
        }
        return false;
    }

    @Override
    public List<Foo> getFooByName(String fooName) {

        List<Foo> dbFoos = fooMap.get(MapDb.TABLE_FOO);

        dbFoos = dbFoos.stream().filter(foo -> foo.getName().equals(fooName)).collect(Collectors.toList());
        System.out.println(dbFoos);
        return dbFoos;
    }
}
