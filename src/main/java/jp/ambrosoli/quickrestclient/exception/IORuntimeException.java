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
package jp.ambrosoli.quickrestclient.exception;

import java.io.IOException;

/**
 * {@link IOException}をラップする例外です。
 * 
 * @author willard379
 * @since 0.1.0
 */
public class IORuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public IORuntimeException() {
        super();
    }

    public IORuntimeException(final String message, final IOException cause) {
        super(message, cause);
    }

    public IORuntimeException(final String message) {
        super(message);
    }

    public IORuntimeException(final IOException cause) {
        super(cause);
    }

}
