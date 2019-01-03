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

package io.github.udaychandra.bdd.phase;

import io.github.udaychandra.bdd.And;
import io.github.udaychandra.bdd.Given;
import io.github.udaychandra.bdd.Then;
import io.github.udaychandra.bdd.When;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a phase in a scene - {@link Given}, {@link When}, {@link Then}.
 * Optionally, a phase holds additional "and" conditions.
 */
public abstract class Phase {
    private String description;
    private List<AndSubPhase> andSubPhases;

    Phase(String description) {
        this.description = description;
        this.andSubPhases = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public List<AndSubPhase> getAndSubPhases() {
        return andSubPhases;
    }

    /**
     * Represents an "and" condition that is part of a {@link Given} or {@link Then} phase in a scene.
     */
    public static class AndSubPhase extends Phase {
        private And and;

        public AndSubPhase(String description, And and) {
            super(description);
            this.and = and;
        }

        public And getAnd() {
            return and;
        }
    }
}
