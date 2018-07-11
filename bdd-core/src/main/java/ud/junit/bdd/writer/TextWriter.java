/*
 * Copyright 2001, 2016, Oracle and/or its affiliates. All rights reserved.
 * Oracle and Java are registered trademarks of Oracle and/or its
 * affiliates. Other names may be trademarks of their respective owners.
 * UNIX is a registered trademark of The Open Group.
 *
 * This software and related documentation are provided under a license
 * agreement containing restrictions on use and disclosure and are
 * protected by intellectual property laws. Except as expressly permitted
 * in your license agreement or allowed by law, you may not use, copy,
 * reproduce, translate, broadcast, modify, license, transmit, distribute,
 * exhibit, perform, publish, or display any part, in any form, or by any
 * means. Reverse engineering, disassembly, or decompilation of this
 * software, unless required by law for interoperability, is prohibited.
 * The information contained herein is subject to change without notice
 * and is not warranted to be error-free. If you find any errors, please
 * report them to us in writing.
 * U.S. GOVERNMENT END USERS: Oracle programs, including any operating
 * system, integrated software, any programs installed on the hardware,
 * and/or documentation, delivered to U.S. Government end users are
 * "commercial computer software" pursuant to the applicable Federal
 * Acquisition Regulation and agency-specific supplemental regulations.
 * As such, use, duplication, disclosure, modification, and adaptation
 * of the programs, including any operating system, integrated software,
 * any programs installed on the hardware, and/or documentation, shall be
 * subject to license terms and license restrictions applicable to the
 * programs. No other rights are granted to the U.S. Government.
 * This software or hardware is developed for general use in a variety
 * of information management applications. It is not developed or
 * intended for use in any inherently dangerous applications, including
 * applications that may create a risk of personal injury. If you use
 * this software or hardware in dangerous applications, then you shall
 * be responsible to take all appropriate fail-safe, backup, redundancy,
 * and other measures to ensure its safe use. Oracle Corporation and its
 * affiliates disclaim any liability for any damages caused by use of this
 * software or hardware in dangerous applications.
 * This software or hardware and documentation may provide access to or
 * information on content, products, and services from third parties.
 * Oracle Corporation and its affiliates are not responsible for and
 * expressly disclaim all warranties of any kind with respect to
 * third-party content, products, and services. Oracle Corporation and
 * its affiliates will not be responsible for any loss, costs, or damages
 * incurred due to your access to or use of third-party content, products,
 * or services.
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