/*
 * Copyright (C) 2020 The Dora Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dora.crash.group;

/**
 * It can combine two or more groups.
 * 简体中文：可以用来组合2种或2种以上的分组。
 */
public class ComplexGroup implements Group {

    /**
     * Intersection.
     * 简体中文：交集。
     */
    public static final String SYMBOL_INTERSECTION = "∩";

    /**
     * Union set.
     * 简体中文：并集。
     */
    public static final String SYMBOL_UNION_SET = "∪";

    /**
     * Supplementary set.
     * 简体中文：补集。
     */
    public static final String SYMBOL_SUPPLEMENTARY_SET = "^";

    private String mSymbol;
    private Group mLeft;
    private Group mRight;

    public static ComplexGroup and(Group left, Group right) {
        return new ComplexGroup(SYMBOL_INTERSECTION, left, right);
    }

    public static ComplexGroup or(Group left, Group right) {
        return new ComplexGroup(SYMBOL_UNION_SET, left, right);
    }

    public static ComplexGroup not(Group left, Group right) {
        return new ComplexGroup(SYMBOL_SUPPLEMENTARY_SET, left, right);
    }

    ComplexGroup(String symbol, Group left, Group right) {
        this.mSymbol = symbol;
        this.mLeft = left;
        this.mRight = right;
    }

    @Override
    public boolean counts() {
        if (mSymbol.equals(SYMBOL_INTERSECTION)) {
            return mLeft.counts() && mRight.counts();
        }
        if (mSymbol.equals(SYMBOL_UNION_SET)) {
            return mLeft.counts() || mRight.counts();
        }
        if (mSymbol.equals(SYMBOL_SUPPLEMENTARY_SET)) {
            return !(mLeft.counts() || mRight.counts());
        }
        return false;
    }

    @Override
    public String name() {
        return mSymbol.equals(SYMBOL_SUPPLEMENTARY_SET) ? (mSymbol + "(" + mLeft + " " +
                mRight + ")") : "(" + mLeft + mSymbol + mRight + ")";
    }
}
