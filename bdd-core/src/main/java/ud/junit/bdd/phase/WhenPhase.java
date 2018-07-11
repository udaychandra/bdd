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

package ud.junit.bdd.phase;

import ud.junit.bdd.When;

/**
 * Represents the {@link When} phase in a scene.
 */
public class WhenPhase extends Phase {
    private When when;

    public WhenPhase(String description, When when) {
        super(description);
        this.when = when;
    }

    public When getWhen() {
        return when;
    }
}
