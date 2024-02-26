// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.testing.unittests;

import net.dirtt.icebox.canvas2d.IceQueueExecutable;
import net.dirtt.icebox.canvas2d.ICEEventQueue;
import net.dirtt.icelib.main.commandmodules.CommandModule;
import net.dirtt.icebox.canvas2d.IceQueueWaitingExecutable;
import net.iceedge.catalogs.icd.panel.ICDTile;
import net.dirtt.icelib.main.Solution;
import java.io.File;
import Quality.testCases.FileLoaderFailTestCase;

public class ICDPanelTest23280 extends FileLoaderFailTestCase
{
    protected static String path;
    private static final File fileToLoad;
    
    public ICDPanelTest23280(final Solution solution) {
        super(ICDPanelTest23280.fileToLoad, "testThatPanelIsStillHardboardAfterCutAndPaste", solution);
    }
    
    public void testThatPanelIsStillHardboardAfterCutAndPaste() throws Exception {
        try {
            final ICDTile icdTile = (ICDTile)this.findEntityByClassAndUserTag(this.solution, (Class)ICDTile.class, "TestThisPanel");
            assertNotNull("Before Cut and Paste, found more than one entity (or none) with specified user tag: TestThisPanel", (Object)icdTile);
            assertTrue("Hardboard Tile".equalsIgnoreCase(icdTile.getAttributeValueAsString("Tile_Indicator")));
            final ICDTile icdTile2 = (ICDTile)this.findEntityByClassAndUserTag(this.solution, (Class)ICDTile.class, "EmptyTile");
            assertNotNull("Before Cut and Paste, found more than one entity (or none) with specified user tag: EmptyPanel", (Object)icdTile2);
            assertTrue("No Tile, With Frame".equalsIgnoreCase(icdTile2.getAttributeValueAsString("Tile_Indicator")));
            ICEEventQueue.scheduleEvent((IceQueueExecutable)new IceQueueWaitingExecutable("CUT_PASTE") {
                public void executeWaitingTask() {
                    try {
                        new FileLoaderFailTestCase.SelectAllAndCutCommand((FileLoaderFailTestCase)ICDPanelTest23280.this, ICDPanelTest23280.this.solution, true).scheduleExecuteAndWait(CommandModule.getIceConsole());
                    }
                    catch (Exception ex) {
                        this.stopExecution(new Error(ex.getMessage()));
                    }
                }
            });
            final ICDTile icdTile3 = (ICDTile)this.findEntityByClassAndUserTag(this.solution, (Class)ICDTile.class, "TestThisPanel");
            assertNotNull("After cut and paste, found more than one entity (or none) with specified user tag: TestThisPanel", (Object)icdTile3);
            assertTrue("Hardboard Tile".equalsIgnoreCase(icdTile3.getAttributeValueAsString("Tile_Indicator")));
            final ICDTile icdTile4 = (ICDTile)this.findEntityByClassAndUserTag(this.solution, (Class)ICDTile.class, "EmptyTile");
            assertNotNull("After cut and paste, found more than one entity (or none) with specified user tag: EmptyPanel", (Object)icdTile4);
            assertTrue("No Tile, With Frame".equalsIgnoreCase(icdTile4.getAttributeValueAsString("Tile_Indicator")));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            assertTrue("Test should not throw exception", false);
        }
    }
    
    static {
        ICDPanelTest23280.path = "tests" + File.separator + "Quality" + File.separator + "ICDTestCases" + File.separator;
        fileToLoad = new File(ICDPanelTest23280.path, "J000021735_simple.ice");
    }
}
