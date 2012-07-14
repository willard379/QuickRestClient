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
package jp.ambrosoli.quickrestclient.params;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.ambrosoli.quickrestclient.util.ReflectionToStringBuilder;

/**
 * 名前と値のペアを保持するクラスです。
 * 
 * @author willard379
 * @since 0.1.0
 */
public class NameValueObject {

    /** 名前 */
    private final String name;

    /** 値 */
    private final String value;

    /**
     * NameValueObjectを生成します。
     * 
     * @param name
     *            名前
     * @param value
     *            値
     */
    public NameValueObject(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 名前 を返します。
     * 
     * @return 名前
     */
    public String getName() {
        return this.name;
    }

    /**
     * 値を返します。
     * 
     * @return 値
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Map<String, String>から{@link NameValueObject}のリストに変換します。
     * 
     * @param src
     *            Map
     * @return {@link NameValueObject}[]
     */
    public static List<NameValueObject> asList(final Map<String, String> src) {

        if (src == null || src.isEmpty()) {
            return null;
        }

        List<NameValueObject> dest = new ArrayList<NameValueObject>(src.size());
        for (Entry<String, String> e : src.entrySet()) {
            dest.add(new NameValueObject(e.getKey(), e.getValue()));
        }

        return dest;

    }

    /**
     * {@link NameValueObject}の配列をリストに変換します。
     * 
     * @param ary
     *            {@link NameValueObject}の配列
     * @return {@link NameValueObject}のリスト
     */
    public static List<NameValueObject> asList(final NameValueObject[] ary) {
        if (ary == null) {
            return null;
        }
        List<NameValueObject> list = new ArrayList<NameValueObject>(ary.length);
        for (NameValueObject nvo : ary) {
            list.add(nvo);
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
