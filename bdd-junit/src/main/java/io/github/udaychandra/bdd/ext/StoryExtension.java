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

import java.lang.reflect.Parameter;

import io.github.udaychandra.bdd.StoryDetails;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import static org.junit.platform.commons.support.AnnotationSupport.isAnnotated;

/**
 * A custom extension that allow test authors to create and run behaviors and stories i.e. BDD specification tests.
 */
public class StoryExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, ParameterResolver {

    private static final Namespace NAMESPACE = Namespace.create(StoryExtension.class);

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {

        if (!isStory(context)) {
            throw new Exception("Use @Story annotation to use StoryExtension. Class: "
                    + context.getRequiredTestClass());
        }

        Class<?> clazz = context.getRequiredTestClass();
        Story story = clazz.getAnnotation(Story.class);

        StoryDetails storyDetails = new StoryDetails()
                .setName(story.name())
                .setDescription(story.description())
                .setClassName(clazz.getName());

        context.getStore(NAMESPACE).put(clazz.getName(), storyDetails);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (!isStory(context)) return;

        new StoryWriter(getStoryDetails(context)).write();
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        if (!isScenario(context)) {
            throw new Exception("Use @Scenario annotation to use the StoryExtension service. Method: "
                    + context.getRequiredTestMethod());
        }

        // Prepare a scene instance corresponding to the given test method.
        Scene scene = new Scene()
                .setMethodName(context.getRequiredTestMethod().getName())
                .setDescription(context.getRequiredTestMethod().getAnnotation(Scenario.class).value());

        getStoryDetails(context).getStore().put(scene.getMethodName(), scene);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) {
        Parameter parameter = parameterContext.getParameter();

        return Scene.class.equals(parameter.getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext,
                                   ExtensionContext context) {

        // Inject the previously created scene object into the test method.
        return getStoryDetails(context).getStore().get(context.getRequiredTestMethod().getName());
    }

    private static StoryDetails getStoryDetails(ExtensionContext context) {
        Class<?> clazz = context.getRequiredTestClass();
        return context.getStore(NAMESPACE).get(clazz.getName(), StoryDetails.class);
    }

    private static boolean isStory(ExtensionContext context) {
        return isAnnotated(context.getRequiredTestClass(), Story.class);
    }

    private static boolean isScenario(ExtensionContext context) {
        return isAnnotated(context.getRequiredTestMethod(), Scenario.class);
    }
}
