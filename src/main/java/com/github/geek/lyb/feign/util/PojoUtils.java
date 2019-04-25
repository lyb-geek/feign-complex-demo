package com.github.geek.lyb.feign.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.geek.lyb.feign.annotation.FieldAlias;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.rmi.UnexpectedException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class PojoUtils {
    public static boolean isUserPojo(@NonNull Object object) {
        if (object == null) {
            throw new NullPointerException("object is marked @NonNull but is null");
        } else {
            Class<?> type = object.getClass();
            String packageName = type.getPackage().getName();
            return !packageName.startsWith("java.");
        }
    }

    public static Map<String, Object> toMap(@NonNull Object object){
        try {
            if (object == null) {
                throw new NullPointerException("object is marked @NonNull but is null");
            } else {
                HashMap<String, Object> result = new HashMap();
                ObjectMapper mapper = new ObjectMapper();
                Class<?> type = object.getClass();
                boolean isPojo = false;
                PojoUtils.SetAccessibleAction setAccessibleAction = new PojoUtils.SetAccessibleAction();
                Field[] fields = type.getDeclaredFields();
                int len = fields.length;

                for(int index = 0; index < len; ++index) {
                    isPojo = false;
                    Field field = fields[index];
                    int modifiers = field.getModifiers();
                    if (!Modifier.isFinal(modifiers) && !Modifier.isStatic(modifiers)) {
                        setAccessibleAction.setField(field);
                        AccessController.doPrivileged(setAccessibleAction);
                        Object fieldValue = field.get(object);
                        if (fieldValue != null) {
                            String fieldName = field.getName();
                            if(field.isAnnotationPresent(FieldAlias.class)){
                                FieldAlias fieldAlias = field.getAnnotation(FieldAlias.class);
                                fieldName = fieldAlias.value();
                                if(fieldAlias.isPojo()){
                                    isPojo = true;
                                   Map<String,Object> pojoMap = mapper.convertValue(fieldValue,Map.class);
                                   if(!pojoMap.isEmpty()){
                                       pojoMap.forEach((key,value)->{
                                           if(value != null){
                                               result.put(key,value);
                                           }
                                       });
                                   }
                                }
                            }
                            if(!isPojo) {
                                result.put(fieldName, fieldValue);
                            }
                        }
                    }
                }

                return result;
            }
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(),throwable);
        }

        return null;
    }




    private PojoUtils() throws UnexpectedException {
        throw new UnexpectedException("It is not allowed to instantiate this class");
    }

    private static class SetAccessibleAction implements PrivilegedAction<Object> {
        @Nullable
        private Field field;

        public Object run() {
            this.field.setAccessible(true);
            return null;
        }

        @SuppressFBWarnings(
            justification = "generated code"
        )
        public void setField(@Nullable Field field) {
            this.field = field;
        }

        @SuppressFBWarnings(
            justification = "generated code"
        )
        public SetAccessibleAction() {
        }
    }
}
