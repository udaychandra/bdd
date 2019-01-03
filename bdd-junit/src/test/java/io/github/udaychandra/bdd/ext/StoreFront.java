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

public class StoreFront {
    private int blues;
    private int blacks;

    public StoreFront(int blues, int blacks) {
        this.blues = blues;
        this.blacks = blacks;
    }

    public int getBlues() {
        return blues;
    }

    private StoreFront setBlues(int blues) {
        this.blues = blues;
        return this;
    }

    public int getBlacks() {
        return blacks;
    }

    private StoreFront setBlacks(int blacks) {
        this.blacks = blacks;
        return this;
    }

    public StoreFront buyBlack(int quantity) throws Exception {
        if (canBuy(quantity, getBlacks())) {
            setBlacks(getBlacks() - quantity);
        } else {
            throw new Exception("There aren't enough black garments in stock.");
        }

        return this;
    }

    public StoreFront refundBlack(int quantity) throws Exception {
        if (quantity > 0) {
            setBlacks(getBlacks() + quantity);

        } else {
            throw new Exception("One or more garments should be returned or exchanged.");
        }

        return this;
    }

    public StoreFront buyBlue(int quantity) throws Exception {
        if (canBuy(quantity, getBlues())) {
            setBlues(getBlues() - quantity);
        } else {
            throw new Exception("There aren't enough blue garments in stock.");
        }

        return this;
    }

    public StoreFront refundBlue(int quantity) throws Exception {
        if (quantity > 0) {
            setBlues(getBlues() + quantity);

        } else {
            throw new Exception("One or more garments should be returned or exchanged.");
        }

        return this;
    }

    private boolean canBuy(int purchaseQuantity, int inStockQuantity) {
        return inStockQuantity - purchaseQuantity >= 0;
    }
}
