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

package io.github.udaychandra.bdd.writer;

import io.github.udaychandra.bdd.config.Constants;
import io.github.udaychandra.bdd.json.JSONArray;
import io.github.udaychandra.bdd.json.JSONObject;
import io.github.udaychandra.bdd.StoryDetails;
import io.github.udaychandra.bdd.json.Json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public abstract class BasicStoryWriter {

    private static File bddReports;

    static {
        URL url = BasicStoryWriter.class.getClassLoader().getResource("");

        if (url == null) {
            throw new RuntimeException("Unable to get the root resource folder.");
        }

        File classesFolder = new File(url.getPath());
        File target = classesFolder.getParentFile();
        bddReports = new File(target, Constants.BDD_REPORTS_FOLDER);

        if (bddReports.exists()) {
            try {
                Files.walk(bddReports.toPath(), FileVisitOption.FOLLOW_LINKS)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (!bddReports.mkdir()) {
            throw new RuntimeException("Unable to create the folder for saving bdd reports.");
        }
    }

    protected final StoryDetails storyDetails;

    private final File classFile;

    public BasicStoryWriter(StoryDetails storyDetails) {
        this.storyDetails = storyDetails;

        try {
            classFile = new File(bddReports, storyDetails.getClassName() + Constants.EXT);

            if (!classFile.createNewFile()) {
                throw new RuntimeException("Unable to create the class file for bdd report: "
                        + storyDetails.getClassName());
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public void write() throws Exception {

        Json report = new Json();
        report.put(Constants.CLASS_NAME, storyDetails.getClassName());
        report.put(Constants.NAME, storyDetails.getName());
        report.put(Constants.DESCRIPTION, storyDetails.getDescription());

        JSONArray scenarios = new JSONArray();
        report.put(Constants.SCENARIOS, scenarios);

        writeScenarios(scenarios, report);

        try (BufferedWriter writer = Files.newBufferedWriter(classFile.toPath())) {
            writer.write(report.toJSONString());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // TODO: Create a text report only when the caller asks for it - use sys variable.
        new TextWriter().write(report, bddReports);
    }

    protected abstract void writeScenarios(JSONArray scenarios, Json report) throws Exception;

    @SuppressWarnings("unchecked")
    protected void addAnds(List<String> ands, JSONObject jsonObject) throws IOException {
        if (!ands.isEmpty()) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(ands);

            jsonObject.put(Constants.ANDS, jsonArray);
        }
    }
}
