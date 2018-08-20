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

package ud.junit.bdd.writer;


import ud.junit.bdd.config.Constants;
import ud.junit.bdd.json.JSONArray;
import ud.junit.bdd.json.Json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static ud.junit.bdd.config.Constants.ANDS;
import static ud.junit.bdd.config.Constants.CLASS_NAME;
import static ud.junit.bdd.config.Constants.DESCRIPTION;
import static ud.junit.bdd.config.Constants.NAME;
import static ud.junit.bdd.config.Constants.SCENARIOS;

class TextWriter {
    private static final String TXT = ".txt";

    private static final String STORY = "STORY: ";
    private static final String STORY_DESC = "       ";
    private static final String SCENARIO = "SCENARIO: ";
    private static final String GIVEN = "   GIVEN ";
    private static final String WHEN = "    WHEN ";
    private static final String THEN = "    THEN ";
    private static final String AND = "     AND ";

    private static final String LF = "\n";

    void write(Json report, File bddReports) throws Exception {
        File classFile = createFile(bddReports, report.str(CLASS_NAME));

        try (BufferedWriter writer = Files.newBufferedWriter(classFile.toPath())) {
            writer.write(STORY + report.str(NAME) + LF);

            if (report.str(DESCRIPTION).length() > 0) {
                writer.write(LF + report.str(DESCRIPTION) + LF + LF);
            } else {
                writer.write(LF);
            }


            JSONArray scenarios = (JSONArray) report.get(SCENARIOS);

            for (Object object : scenarios) {
                Json scenario = (Json) object;

                writer.write(SCENARIO + scenario.str(NAME) + LF);

                if (scenario.containsKey(Constants.GIVEN)) {
                    Json given = scenario.val(Constants.GIVEN);
                    writer.write(GIVEN + given.str(NAME) + LF);

                    if (given.containsKey(ANDS)) {
                        printAnds(given.val(ANDS), writer);
                    }
                }

                if (scenario.containsKey(Constants.WHEN)) {
                    Json when = scenario.val(Constants.WHEN);

                    writer.write(WHEN + when.str(NAME) + LF);
                }

                Json then = scenario.val(Constants.THEN);
                writer.write(THEN + then.str(NAME) + LF);

                if (then.containsKey(ANDS)) {
                    printAnds(then.val(ANDS), writer);
                }

                writer.write(LF);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private File createFile(File bddReports, String className) {
        try {
            File classFile = new File(bddReports, className + TXT);

            if (!classFile.createNewFile()) {
                throw new RuntimeException("Unable to create the class file for bdd report: "
                        + className);
            }

            return classFile;

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void printAnds(List<String> ands, BufferedWriter writer) throws IOException {
        for (String a : ands) {
            writer.write(AND + a + LF);
        }
    }
}