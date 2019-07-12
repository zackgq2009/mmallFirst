package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.misc.Request;

import java.lang.reflect.Method;

public class MappingUtil {

    public static String getControllerMapping(Class clazz) {
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);

            String[] values = requestMapping.value();
            return values[0];
        }
        return null;
    }


    public static String getMethodMapping(Class clazz, String methodName) {
        String classMappingValue = "";
        String methodMappingValue = "";

        if (clazz != null && StringUtils.isNotBlank(methodName)) {
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                String[] values = requestMapping.value();
                classMappingValue = values[0];
            }
            Method[] methods = clazz.getDeclaredMethods();

            for (Method method : methods
            ) {
                if (method.getName() == methodName) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                        String[] mappingValues = annotation.value();
                        methodMappingValue = mappingValues[0];
                    }
                }
            }
            return classMappingValue+methodMappingValue;
        } else {
            return null;
        }
    }
}
