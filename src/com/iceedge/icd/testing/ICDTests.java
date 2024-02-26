// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.testing;

import junit.framework.Test;
import net.iceedge.test.IceTests;

public class ICDTests extends IceTests
{
    public static Test suite() {
        return suite(true, null);
    }
    
    public static Test suite(final boolean b, final String s) {
        configure(s, "icd");
        return initialize((Class)ICDTests.class, new Test[] { (Test)new ICDReportTestSuite(), (Test)new ICDQuoteTestSuite() });
    }
}
