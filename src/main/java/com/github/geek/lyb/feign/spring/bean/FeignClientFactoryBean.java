package com.github.geek.lyb.feign.spring.bean;

import com.github.geek.lyb.feign.util.FeignClientUtils;
import org.springframework.beans.factory.FactoryBean;

public class FeignClientFactoryBean<T> implements FactoryBean<T> {

    private Class<T> feignClzType;

    private String url;


    public Class<T> getFeignClzType() {
        return feignClzType;
    }

    public void setFeignClzType(Class<T> feignClzType) {
        this.feignClzType = feignClzType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public T getObject() throws Exception {

        return FeignClientUtils.getInstance().getClient(feignClzType,url);
    }

    @Override
    public Class<?> getObjectType() {
        return feignClzType;
    }
}
