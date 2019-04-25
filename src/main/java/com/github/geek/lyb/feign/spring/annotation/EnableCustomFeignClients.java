package com.github.geek.lyb.feign.spring.annotation;

import com.github.geek.lyb.feign.spring.registrar.FeignClientScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FeignClientScannerRegistrar.class)
public @interface EnableCustomFeignClients {

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};
}
