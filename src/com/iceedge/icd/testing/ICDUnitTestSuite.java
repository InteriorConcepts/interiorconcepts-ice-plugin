// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.testing;

import com.iceedge.testing.UnitTestLoader;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ICDUnitTestSuite extends TestSuite
{
    protected static final String PATH = "net";
    
    public static Test suite() {
        return new UnitTestLoader((Class)ICDUnitTestSuite.class).getUnitTestSuite("net");
    }
}
