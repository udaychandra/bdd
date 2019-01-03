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

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Scenario("Replaced items should be returned to stock")
    public void replaceAndRestock(Scene scene) {
        scene.
                given("that a customer previously bought a blue garment from me",
                        () -> scene.put("store", new StoreFront(3, 3).buyBlue(1))).

                and("I have two blue garments in stock",
                        () -> assertEquals(2, scene.<StoreFront>get("store").getBlues(),
                                "Store should carry 2 blue garments")).

                and("three black garments in stock",
                        () -> assertEquals(3, scene.<StoreFront>get("store").getBlacks(),
                                "Store should carry 3 black garments")).

                when("she returns the blue garment for a replacement in black",
                        () -> scene.<StoreFront>get("store").refundBlue(1).buyBlack(1)).

                then("I should have three blue garments in stock",
                        () -> assertEquals(3, scene.<StoreFront>get("store").getBlues(),
                                "Store should carry 3 blue garments")).

                and("two black garments in stock",
                        () -> assertEquals(2, scene.<StoreFront>get("store").getBlacks(),
                                "Store should carry 2 black garments")).

                run();
    }
}
