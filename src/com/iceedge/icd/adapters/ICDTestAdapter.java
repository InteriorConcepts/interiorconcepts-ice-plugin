// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.adapters;

import junit.framework.Test;
import com.iceedge.icd.testing.ICDUnitTestSuite;
import com.iceedge.icd.testing.ICDTestCasesSuite;
import com.iceedge.icd.testing.ICDQuoteTestSuite;
import com.iceedge.icd.testing.ICDReportTestSuite;
import junit.framework.TestSuite;
import net.iceedge.icecore.plugin.systemInterfaces.IceTestsAdapter;

public class ICDTestAdapter implements IceTestsAdapter
{
    public TestSuite getTestSuite() {
        final TestSuite testSuite = new TestSuite("ICD Tests");
        final ICDReportTestSuite icdReportTestSuite = new ICDReportTestSuite();
        final ICDQuoteTestSuite icdQuoteTestSuite = new ICDQuoteTestSuite();
        final ICDTestCasesSuite icdTestCasesSuite = new ICDTestCasesSuite();
        testSuite.addTest(ICDUnitTestSuite.suite());
        testSuite.addTest((Test)icdTestCasesSuite);
        testSuite.addTest((Test)icdReportTestSuite);
        testSuite.addTest((Test)icdQuoteTestSuite);
        return testSuite;
    }
    
    public String getPropertyName() {
        return "icd";
    }
}
