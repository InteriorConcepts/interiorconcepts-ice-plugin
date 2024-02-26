// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.testing;

import java.io.File;
import com.iceedge.icd.testing.unittests.ICDTileQuoteTest24130;
import com.iceedge.icd.testing.unittests.ICDSlottingInnerVertExtrusionTest23669;
import com.iceedge.icd.testing.unittests.ICDParametricDeckOrShelf23437;
import com.iceedge.icd.testing.unittests.ICDPanelTest23324;
import junit.framework.Test;
import com.iceedge.icd.testing.unittests.ICDPanelTest23280;
import net.iceedge.test.IceTestSuite;

public class ICDTestCasesSuite extends IceTestSuite
{
    protected static String path;
    
    public ICDTestCasesSuite() {
        super("ICD Test Cases", ICDTestCasesSuite.path);
        this.addTest((Test)new ICDPanelTest23280(this.getSolution()));
        this.addTest((Test)new ICDPanelTest23324(this.getSolution()));
        this.addTest((Test)new ICDParametricDeckOrShelf23437(this.getSolution()));
        this.addTest((Test)new ICDSlottingInnerVertExtrusionTest23669(this.getSolution()));
        this.addTest((Test)new ICDTileQuoteTest24130(this.getSolution()));
    }
    
    static {
        ICDTestCasesSuite.path = "tests" + File.separator + "Quality" + File.separator + "ICDTestCases" + File.separator;
    }
}
