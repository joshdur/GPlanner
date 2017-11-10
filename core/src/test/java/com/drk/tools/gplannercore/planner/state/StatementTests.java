package com.drk.tools.gplannercore.planner.state;

import org.junit.Assert;
import org.junit.Test;

public class StatementTests {

    @Test
    public void test_multiplicative_inverse() {
        int y = GCodes.multiplicativeInverse(7);
        Assert.assertEquals(1, 7 * y);
        Assert.assertEquals(7, GCodes.multiplicativeInverse(y));
    }
}
