// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.testing;

import net.dirtt.icebox.IceBoxApp;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.framework.Test;
import java.io.File;
import net.iceedge.test.icelib.quote.TestQuote.furniture.GeneralQuoteTestSuite;

public class ICDQuoteTestSuite extends GeneralQuoteTestSuite
{
    public ICDQuoteTestSuite() {
        this("ICD Quote Tests", "tests" + File.separator + "Quality" + File.separator + "Furniture_Tests" + File.separator + "ICD" + File.separator + "");
    }
    
    public ICDQuoteTestSuite(final String s, final String s2) {
        super(s, s2);
    }
    
    protected void addQuoteTest(final File file) {
        this.addTest((Test)new ICDQuoteTestCase(this.getSolution(), file, new File(file.getAbsolutePath().replaceAll("\\.[iI][cC][eE]$", ".csv")), new File(file.getAbsolutePath().replaceAll("\\.[iI][cC][eE]$", ".sif")), new File(file.getAbsolutePath().replaceAll("\\.[iI][cC][eE]$", "_Option_Override.sif"))));
    }
    
    public static Test suite() {
        final TestSuite testSuite = new TestSuite("Quality");
        testSuite.addTest((Test)new ICDQuoteTestSuite());
        return (Test)testSuite;
    }
    
    public void runTest(final Test test, final TestResult testResult) {
        IceBoxApp.SUPPORTS_V3 = false;
        super.runTest(test, testResult);
    }
}
