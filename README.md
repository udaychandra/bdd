## BDD
A BDD library that provides a custom extension based on JUnit 5 Jupiter Extension Model. This library can be used to create and run stories and behaviors a.k.a BDD specification tests.

## Basic Usage
You need to add [JUnit 5](https://junit.org/junit5/docs/current/user-guide/#installation) dependencies before using this library. If you are using a build tool like Maven or Gradle, add the following dependency after setting-up JUnit:

- Maven pom.xml
  ```xml
   <dependency>
     <groupId>io.github.udaychandra.bdd</groupId>
     <artifactId>bdd-junit</artifactId>
     <version>0.1.0</version>
   </dependency>
   ```

- Gradle build.gradle
  ```groovy
   dependencies {
     compile 'io.github.udaychandra.bdd:bdd-junit:0.1.0'
   }
   ```


You can now write stories using @Story and @Scenario annotations provided by this library. Here's an example:

```java
import io.github.udaychandra.bdd.ext.Scenario;
import io.github.udaychandra.bdd.ext.Story;

@Story(name = "Returns go back to the stockpile",
        description = "As a store owner, in order to keep track of stock," +
                " I want to add items back to stock when they're returned.")
public class StoreFrontTest {

    @Scenario("Refunded items should be returned to stock")
    public void refundAndRestock(Scene scene) {
        scene.
            given("that a customer previously bought a black sweater from me",
                    () -> scene.put("store", new StoreFront(0, 4).buyBlack(1))).

            and("I have three black sweaters in stock",
                    () -> assertEquals(3, scene.<StoreFront>get("store").getBlacks(),
                            "Store should carry 3 black sweaters")).

            when("the customer returns the black sweater for a refund",
                    () -> scene.<StoreFront>get("store").refundBlack(1)).

            then("I should have four black sweaters in stock",
                    () -> assertEquals(4, scene.<StoreFront>get("store").getBlacks(),
                            "Store should carry 4 black sweaters")).
            run();
    }
}

```   

The "Scene" object injected into each test method can be used to store and retrieve arbitrary objects.

When you run these stories (tests), text reports like the one shown below are generated:
```text
STORY: Returns go back to the stockpile

As a store owner, in order to keep track of stock, I want to add items back to stock when they're returned.

SCENARIO: Refunded items should be returned to stock
   GIVEN that a customer previously bought a black sweater from me
     AND I have three black sweaters in stock
    WHEN the customer returns the black sweater for a refund
    THEN I should have four black sweaters in stock
```

You will find the text reports in a folder named "bdd-reports" located under your test classes build folder.

## Development
This is a community project. All contributions are welcome.

To start contributing, do the following:
* Install JDK 8+
* Fork or clone the source code
* Run the build using the gradle wrapper
```bash
gradlew clean build
```

You can read this InfoQ [article](https://www.infoq.com/articles/deep-dive-junit5-extensions) for more insights.

## License
Apache License 2.0