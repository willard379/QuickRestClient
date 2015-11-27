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
package jp.ambrosoli.quickrestclient;

/**
 * HTTP関連の定数群です。
 * 
 * @author willard379
 * @since 0.1.0
 */
public interface HttpConstants {

    /* ################## HTTP Method ################## */

    /** HTTPメソッド "GET"を表す定数です。 */
    public static final String GET = "GET"; //$NON-NLS-1$

    /** HTTPメソッド "POST"を表す定数です。 */
    public static final String POST = "POST"; //$NON-NLS-1$

    /** HTTPメソッド "PUT"を表す定数です。 */
    public static final String PUT = "PUT"; //$NON-NLS-1$

    /** HTTPメソッド "DELETE"を表す定数です。 */
    public static final String DELETE = "DELETE"; //$NON-NLS-1$

    /** HTTPメソッド "HEAD"を表す定数です。 */
    public static final String HEAD = "HEAD"; //$NON-NLS-1$

    /** HTTPメソッド "OPTIONS"を表す定数です。 */
    public static final String OPTIONS = "OPTIONS"; //$NON-NLS-1$

    /* ################## Protocol Version ################## */

    /** プロトコルバージョン "HTTP/1.0"を表す定数です。 */
    public static final String HTTP_1_0 = "HTTP/1.0"; //$NON-NLS-1$

    /** プロトコルバージョン "HTTP/1.1"を表す定数です。 */
    public static final String HTTP_1_1 = "HTTP/1.1"; //$NON-NLS-1$

    /* ################## MIME Type ################## */

    /** MIME Type "text/html"を表す定数です。 */
    public static final String HTML = "text/html"; //$NON-NLS-1$

    /** MIME Type "application/xhtml+xml"を表す定数です。 */
    public static final String XHTML = "application/xhtml+xml"; //$NON-NLS-1$

    /** MIME Type "application/json"を表す定数です。 */
    public static final String JSON = "application/json"; //$NON-NLS-1$

    /** MIME Type "application/xml"を表す定数です。 */
    public static final String XML = "application/xml"; //$NON-NLS-1$

    /** MIME Type "text/plain"を表す定数です。 */
    public static final String TEXT = "text/plain"; //$NON-NLS-1$

}
