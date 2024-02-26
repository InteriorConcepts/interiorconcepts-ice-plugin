// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.testing.unittests;

import net.dirtt.icelib.main.EntityObject;
import junit.framework.Assert;
import net.dirtt.utilities.TypeFilter;
import net.iceedge.catalogs.icd.panel.ICDPanelFilter;
import net.dirtt.icelib.main.TypeableEntity;
import net.iceedge.catalogs.icd.panel.ICDTile;
import net.iceedge.catalogs.icd.panel.ICDPanelSKUGenerator;
import net.dirtt.icelib.main.Solution;
import java.io.File;
import Quality.testCases.FileLoaderFailTestCase;

public class ICDPanelTest23324 extends FileLoaderFailTestCase
{
    protected static String path;
    private static final File fileToLoad;
    
    public ICDPanelTest23324(final Solution solution) {
        super(ICDPanelTest23324.fileToLoad, "testManufacturingOptionAfterChangingOptions", solution);
    }
    
    public void testManufacturingOptionAfterChangingOptions() throws Exception {
        try {
            final ICDPanelSKUGenerator icdPanelSKUGenerator = new ICDPanelSKUGenerator();
            final ICDTile icdTile = (ICDTile)this.findEntityByClassAndUserTag(this.solution, (Class)ICDTile.class, "TestThisTile");
            Assert.assertEquals("FC6618T", icdPanelSKUGenerator.generateSKU((TypeableEntity)icdTile.getParent((TypeFilter)new ICDPanelFilter())));
            final String s = "Tiles";
            this.changeAttribute((EntityObject)icdTile, "Tile_Indicator", s, "Hardboard Tile", "No Tile, With Frame");
            Assert.assertEquals("FC6618O", icdPanelSKUGenerator.generateSKU((TypeableEntity)icdTile.getParent((TypeFilter)new ICDPanelFilter())));
            this.changeAttribute((EntityObject)icdTile, "Tile_Indicator", s, "No Tile, With Frame", "Hardboard Tile");
            Assert.assertEquals("Sku should be FC6618T after changing attributes back to TILE_INDICATOR_NO_TILE_WITH_FRAME", "FC6618T", icdPanelSKUGenerator.generateSKU((TypeableEntity)icdTile.getParent((TypeFilter)new ICDPanelFilter())));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            assertTrue("Test should not throw exception", false);
        }
    }
    
    static {
        ICDPanelTest23324.path = "tests" + File.separator + "Quality" + File.separator + "ICDTestCases" + File.separator;
        fileToLoad = new File(ICDPanelTest23324.path, "23324.ice");
    }
}
