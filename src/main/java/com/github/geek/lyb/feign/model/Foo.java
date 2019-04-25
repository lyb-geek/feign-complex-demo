package com.github.geek.lyb.feign.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Foo {

    private Long id;

    private String name;

    private List<Bar> bars;



}
