package com.github.geek.lyb.feign.encoder;

import com.github.geek.lyb.feign.util.PojoUtils;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.ContentProcessor;
import feign.form.ContentType;
import feign.form.MultipartFormContentProcessor;
import feign.form.UrlencodedFormContentProcessor;
import feign.form.util.CharsetUtil;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomFormEncoder implements Encoder {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final Pattern CHARSET_PATTERN = Pattern.compile("(?<=charset=)([\\w\\-]+)");
    private final Encoder delegate;
    private final Map<ContentType, ContentProcessor> processors;

    public CustomFormEncoder() {
        this(new Default());
    }

    public CustomFormEncoder(Encoder delegate) {
        this.delegate = delegate;
        List<ContentProcessor> list = Arrays.asList(new MultipartFormContentProcessor(delegate), new UrlencodedFormContentProcessor());
        this.processors = new HashMap(list.size(), 1.0F);
        Iterator iterator = list.iterator();

        while(iterator.hasNext()) {
            ContentProcessor processor = (ContentProcessor)iterator.next();
            this.processors.put(processor.getSupportedContentType(), processor);
        }

    }

    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        String contentTypeValue = this.getContentTypeValue(template.headers());
        ContentType contentType = ContentType.of(contentTypeValue);
        if (!this.processors.containsKey(contentType)) {
            this.delegate.encode(object, bodyType, template);
        } else {
            Map data;
            if (MAP_STRING_WILDCARD.equals(bodyType)) {
                data = (Map)object;
            } else {
                if (!PojoUtils.isUserPojo(object)) {
                    this.delegate.encode(object, bodyType, template);
                    return;
                }

                data = PojoUtils.toMap(object);
            }

            Charset charset = this.getCharset(contentTypeValue);
            ((ContentProcessor)this.processors.get(contentType)).process(template, charset, data);
        }
    }

    public final ContentProcessor getContentProcessor(ContentType type) {
        return (ContentProcessor)this.processors.get(type);
    }

    private String getContentTypeValue(Map<String, Collection<String>> headers) {
        Iterator iterator = headers.entrySet().iterator();

        while(true) {
            Map.Entry entry;
            do {
                if (!iterator.hasNext()) {
                    return null;
                }

                entry = (Map.Entry)iterator.next();
            } while(!((String)entry.getKey()).equalsIgnoreCase(CONTENT_TYPE_HEADER));

            Iterator var = ((Collection)entry.getValue()).iterator();

            while(var.hasNext()) {
                String contentTypeValue = (String)var.next();
                if (contentTypeValue != null) {
                    return contentTypeValue;
                }
            }
        }
    }

    private Charset getCharset(String contentTypeValue) {
        Matcher matcher = CHARSET_PATTERN.matcher(contentTypeValue);
        return matcher.find() ? Charset.forName(matcher.group(1)) : CharsetUtil.UTF_8;
    }


}
