/*
 * Copyright (c) 2011-2012 ambrosoli.jp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package jp.ambrosoli.quickrestclient.service;

import java.util.HashMap;
import java.util.Map;

import jp.ambrosoli.quickrestclient.apache.service.ApacheHttpServiceFactory;

/**
 * {@link HttpService}のファクトリクラスです。
 * 
 * @author willard379
 * @since 0.1.0
 */
public abstract class HttpServiceFactory {

    /** ファクトリーのキャッシュ */
    private static final Map<String, HttpServiceFactory> cache = new HashMap<String, HttpServiceFactory>();

    /**
     * デフォルトのファクトリーのインスタンスを返します。
     * 
     * @return デフォルトのファクトリー
     */
    public static HttpServiceFactory getFactory() {
        HttpServiceFactory factory = cache.get(null);
        if (factory == null) {
            factory = new ApacheHttpServiceFactory();
            register(null, factory);
        }
        return factory;
    }

    /**
     * factoryNameで指定されたファクトリーのインスタンスを返します。
     * 
     * @param factoryName
     *            ファクトリーの識別子
     * @return 指定されたファクトリー
     */
    public static HttpServiceFactory getFactory(final String factoryName) {

        if (factoryName == null) {
            return getFactory();
        }

        HttpServiceFactory factory = cache.get(factoryName);
        if (factory == null) {
            throw new IllegalStateException("HttpServiceFactory {" + factoryName
                    + "} is not registerd.");
        }
        return factory;
    }

    /**
     * ファクトリーを登録します。
     * 
     * @param factoryName
     *            ファクトリーの識別子
     * @param factory
     *            登録するファクトリー
     */
    public static void register(final String factoryName, final HttpServiceFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("HttpServiceFactory {" + factoryName
                    + "} could not be registerd.");
        }
        cache.put(factoryName, factory);
    }

    /**
     * {@link HttpService}を生成します。
     * 
     * @return HttpService
     */
    public abstract HttpService create();

}
