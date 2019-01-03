/*
 * Copyright 2018 Contributors

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.udaychandra.bdd.json;

import java.util.Map;

public class Json extends JSONObject {
    public Json() {
        super();
    }

    public Json(Map map) {
        super(map);
    }

    public Integer integer(String key) {
        return (Integer) get(key);
    }

    @SuppressWarnings("unchecked")
    public Json integer(String key, int value) {
        put(key, value);
        return this;
    }

    public Long lng(String key) {
        return (Long) get(key);
    }

    @SuppressWarnings("unchecked")
    public Json lng(String key, long value) {
        put(key, value);
        return this;
    }

    public Double dbl(String key) {
        return (Double) get(key);
    }

    @SuppressWarnings("unchecked")
    public Json dbl(String key, double value) {
        put(key, value);
        return this;
    }

    public String str(String key) {
        return (String) get(key);
    }

    @SuppressWarnings("unchecked")
    public Json str(String key, String value) {
        put(key, value);
        return this;
    }

    public Object obj(String key) {
        return get(key);
    }

    @SuppressWarnings("unchecked")
    public Json obj(String key, Object value) {
        put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T val(String key) {
        return (T) get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> Json val(String key, T value) {
        put(key, value);
        return this;
    }
}
