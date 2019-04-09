/*
 * Copyright 2012 Stoyan.
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
package com.stoyanr.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ForkJoinComputerTest {
    
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        // @formatter:off
        Object[][] data = new Object[][] { { 1_000_000 } };
        // @formatter:on
        return Arrays.asList(data);
    }
    
    private final int n;
    
    public ForkJoinComputerTest(int n) {
        this.n = n;
    }
    
    @Test
    public void test() {
        int result = new ForkJoinComputer<Integer>(n, 1000, 
            (lo, hi) -> { 
                int sum = 0; 
                for (int i = lo + 1; i <= hi; i++) 
                    sum += i;
                return sum; 
            }, 
            (a, b) -> a + b).compute();
        assertEquals((n + 1) * (n / 2), result);
    }
}
