// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.testing.unittests;

import junit.framework.Assert;
import net.dirtt.icelib.main.TypeableEntity;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import net.iceedge.catalogs.icd.panel.ICDTile;
import net.dirtt.icelib.main.Solution;
import java.io.File;
import Quality.testCases.FileLoaderFailTestCase;

public class ICDTileQuoteTest24130 extends FileLoaderFailTestCase
{
    protected static String path;
    private static final File fileToLoad;
    
    public ICDTileQuoteTest24130(final Solution solution) {
        super(ICDTileQuoteTest24130.fileToLoad, "testQuoteSpecificMethods", solution);
    }
    
    public void testQuoteSpecificMethods() throws Exception {
        try {
            final ICDTile icdTile = (ICDTile)this.findEntityByClassAndUserTag(this.solution, (Class)ICDTile.class, "testthis");
            final String productSubType = ICDManufacturingUtils.getProductSubType((TypeableEntity)icdTile);
            Assert.assertEquals("Pnl", ICDManufacturingUtils.getProductType((TypeableEntity)icdTile));
            Assert.assertEquals("Stack Panel", productSubType);
            final ICDTile icdTile2 = (ICDTile)this.findEntityByClassAndUserTag(this.solution, (Class)ICDTile.class, "testthat");
            final String productSubType2 = ICDManufacturingUtils.getProductSubType((TypeableEntity)icdTile2);
            final String productType = ICDManufacturingUtils.getProductType((TypeableEntity)icdTile2);
            Assert.assertEquals(icdTile2.getSubType(), productSubType2);
            Assert.assertEquals(icdTile2.getAttributeValueAsString("Product_Type"), productType);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            assertTrue("Test should not throw exception", false);
        }
    }
    
    static {
        ICDTileQuoteTest24130.path = "tests" + File.separator + "Quality" + File.separator + "ICDTestCases" + File.separator;
        fileToLoad = new File(ICDTileQuoteTest24130.path, "24130.ice");
    }
}
