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

import ud.junit.bdd.StoryDetails;
import ud.junit.bdd.json.JSONArray;
import ud.junit.bdd.json.Json;
import ud.junit.bdd.writer.BasicStoryWriter;

import static ud.junit.bdd.config.Constants.GIVEN;
import static ud.junit.bdd.config.Constants.METHOD_NAME;
import static ud.junit.bdd.config.Constants.NAME;
import static ud.junit.bdd.config.Constants.THEN;
import static ud.junit.bdd.config.Constants.WHEN;

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
            scenario.put(METHOD_NAME, scene.getMethodName());
            scenario.put(NAME, scene.getDescription());

            if (scene.given().length() > 0) {
                Json given = new Json();
                given.put(NAME, scene.given());
                addAnds(scene.givenAnds(), given);

                scenario.put(GIVEN, given);
            }

            if (scene.when().length() > 0) {
                Json when = new Json();
                when.put(NAME, scene.when());

                scenario.put(WHEN, when);
            }

            Json then = new Json();
            then.put(NAME, scene.then());
            addAnds(scene.thenAnds(), then);

            scenario.put(THEN, then);

            scenarios.add(scenario);
        }
    }
}
