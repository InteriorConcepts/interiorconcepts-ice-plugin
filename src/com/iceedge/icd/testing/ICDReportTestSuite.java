// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.testing;

import java.util.Iterator;
import java.util.HashMap;
import Quality.testCases.CSVCompareTestCase;
import net.iceedge.test.TestType;
import junit.framework.Test;
import Quality.testCases.FileLoaderTestCase;
import net.iceedge.test.GroupedTestSuite;
import net.iceedge.test.TestUtility;
import java.io.File;
import net.iceedge.test.IceTestSuite;

public class ICDReportTestSuite extends IceTestSuite
{
    public static final String ICDTestFolder;
    
    public ICDReportTestSuite() {
        super("ICD Reports", ICDReportTestSuite.ICDTestFolder);
        this.gatherTests(ICDReportTestSuite.ICDTestFolder);
    }
    
    protected void addTests(final File file) {
        final String name = file.getName();
        TestUtility.cleanLogFileOnTestInit(file);
        final GroupedTestSuite groupedTestSuite = new GroupedTestSuite(file);
        groupedTestSuite.addTest((Test)new FileLoaderTestCase(file, name, this.getSolution()));
        final HashMap<Integer, String> allICDReportTypes = getAllICDReportTypes();
        for (final Integer key : allICDReportTypes.keySet()) {
            groupedTestSuite.addTest((Test)new CSVCompareTestCase(file, name, (int)key, (IceTestSuite)this, TestType.REPORT, (String)allICDReportTypes.get(key)));
        }
        this.addTest((Test)groupedTestSuite);
    }
    
    public static HashMap<Integer, String> getAllICDReportTypes() {
        final HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
        hashMap.put(51, "ManufacturingReport");
        return hashMap;
    }
    
    static {
        ICDTestFolder = TestUtility.baseDirectory + "ICDReports";
    }
}
