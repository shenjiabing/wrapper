/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sayweee.wrapper.http.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.sayweee.wrapper.bean.BaseBean;
import com.sayweee.wrapper.bean.N;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/25.
 * Desc:    默认的解析器  支持自定义解析和原有的Gson解析
 */
public class WrapperConverterFactory extends Converter.Factory {

    private final Gson gson;

    public static WrapperConverterFactory create() {
        return create(new Gson());
    }

    public static WrapperConverterFactory create(Gson gson) {
        return new WrapperConverterFactory(gson);
    }

    private WrapperConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        try {
            Class<?> clazz = ConverterUtils.getRawType(type);
            if (N.class.isAssignableFrom(clazz)) { //继承自N
                return new NResponseConverter(clazz);
            } else if (BaseBean.class.isAssignableFrom(clazz)) { //继承自BaseVo
                return new WrapperResponseConverter<>(gson, type, clazz);
            }
        } catch (Exception e) {
        }
        //默认解析
        return new GsonResponseBodyConverter<>(gson, gson.getAdapter(TypeToken.get(type)));
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }


}
