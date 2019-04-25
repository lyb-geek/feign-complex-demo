package com.github.geek.lyb.feign.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.geek.lyb.feign.encoder.CustomFormEncoder;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class FeignClientUtils {

	private FeignClientUtils() {

	}

	public static FeignClientUtils getInstance() {
		return Singleston.INSTANCE.getInstance();
	}

	private static enum Singleston {
		INSTANCE;
		private FeignClientUtils feignUtils;

		private Singleston() {
			feignUtils = new FeignClientUtils();
		}

		public FeignClientUtils getInstance() {
			return feignUtils;
		}

	}

	public <T> T getClient(Class<T> apiType, String url) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		T client = Feign.builder().encoder(new CustomFormEncoder(new JacksonEncoder(mapper))).decoder(new JacksonDecoder(mapper))
				.target(apiType, url);


		return client;

	}
}
