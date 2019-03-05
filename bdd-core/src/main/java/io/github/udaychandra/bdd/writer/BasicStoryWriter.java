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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class BasicStoryWriter {

    /**
     * Facilitates the clean-up and creation of {@link Constants#BDD_REPORTS_FOLDER} folder
     * under the test execution's target or build directory.
     */
    private static class BDDReportsFolderInitializer {

        private static BDDReportsFolderInitializer ME;

        static synchronized BDDReportsFolderInitializer get(Class<?> anyTestClass) {
            if (ME == null) {
                ME = new BDDReportsFolderInitializer(anyTestClass);
            }

            return ME;
        }

        private final File bddReports;

        // TODO: figure out a cleaner way to get the "target" or "build" directory
        //       that can handle IDE quirks.
        private BDDReportsFolderInitializer(Class<?> anyTestClass) {
            URL url = anyTestClass.getResource("");

            if (url == null) {
                throw new RuntimeException("Unable to get the root resource folder.");
            }

            File classParentFile = new File(url.getPath());
            int packageFolderCount = anyTestClass.getPackage().getName().split("\\.").length;

            File classesFolder = classParentFile;
            for (int c = 0; c < packageFolderCount; c++) {
                classesFolder = classesFolder.getParentFile();
            }

            File target = classesFolder.getParentFile();

            Set<String> knownTargetDirs = new HashSet<>();
            knownTargetDirs.add("target");
            knownTargetDirs.add("build");
            knownTargetDirs.add("out");

            // Try to handle maven/gradle/IDEs quirks.
            if (!knownTargetDirs.contains(target.getName())) {
                // One more attempt to see if the parent is any good.
                File targetParent = target.getParentFile();
                if (knownTargetDirs.contains(targetParent.getName())) {
                    target = targetParent;
                }
            }

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
    }

    protected final StoryDetails storyDetails;

    private final BDDReportsFolderInitializer initializer;
    private final File classFile;

    public BasicStoryWriter(StoryDetails storyDetails) {
        this.storyDetails = storyDetails;
        this.initializer = BDDReportsFolderInitializer.get(storyDetails.getStoryClass());

        try {
            classFile = new File(initializer.bddReports, storyDetails.getClassName() + Constants.EXT);

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
        new TextWriter().write(report, initializer.bddReports);
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
