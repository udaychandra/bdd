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

package ud.junit.bdd.ext;

import org.junit.platform.commons.annotation.Testable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a story in BDD or specification by example style of writing tests.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Testable
public @interface Story {

    /**
     * @return short summary of the story or feature in a plain human language.
     */
    String name();

    /**
     * Describes the story in detail.
     * Typically, in english, one could describe a story in this format:
     * As a {user-defined string}, in order to {user-defined string}, I want to {user-defined string}.
     *
     * @return represents the story in a plain human language.
     */
    String description() default "";
}
