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

package io.github.udaychandra.bdd.ext;

import io.github.udaychandra.bdd.StoryDetails;
import io.github.udaychandra.bdd.config.Constants;
import io.github.udaychandra.bdd.json.JSONArray;
import io.github.udaychandra.bdd.json.Json;
import io.github.udaychandra.bdd.writer.BasicStoryWriter;

class StoryWriter extends BasicStoryWriter {

    StoryWriter(StoryDetails storyDetails) {
        super(storyDetails);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void writeScenarios(JSONArray scenarios, Json report) throws Exception {
        for (String key : storyDetails.getStore().keySet()) {
            Scene scene = (Scene) storyDetails.getStore().get(key);

            Json scenario = new Json();
            scenario.put(Constants.METHOD_NAME, scene.getMethodName());
            scenario.put(Constants.NAME, scene.getDescription());

            if (scene.given().length() > 0) {
                Json given = new Json();
                given.put(Constants.NAME, scene.given());
                addAnds(scene.givenAnds(), given);

                scenario.put(Constants.GIVEN, given);
            }

            if (scene.when().length() > 0) {
                Json when = new Json();
                when.put(Constants.NAME, scene.when());

                scenario.put(Constants.WHEN, when);
            }

            Json then = new Json();
            then.put(Constants.NAME, scene.then());
            addAnds(scene.thenAnds(), then);

            scenario.put(Constants.THEN, then);

            scenarios.add(scenario);
        }
    }
}
