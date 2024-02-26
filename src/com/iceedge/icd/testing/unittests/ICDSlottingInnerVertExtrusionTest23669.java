// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.testing.unittests;

import java.util.Iterator;
import java.util.List;
import net.iceedge.catalogs.icd.intersection.ICDCornerSlot;
import net.iceedge.catalogs.icd.panel.ICDInternalExtrusion;
import net.dirtt.icelib.main.Solution;
import java.io.File;
import Quality.testCases.FileLoaderFailTestCase;

public class ICDSlottingInnerVertExtrusionTest23669 extends FileLoaderFailTestCase
{
    protected static String path;
    private static final File fileToLoad;
    
    public ICDSlottingInnerVertExtrusionTest23669(final Solution solution) {
        super(ICDSlottingInnerVertExtrusionTest23669.fileToLoad, "testThatExtrusionHasSlottingControlsAndTheyCanBeSet", solution);
    }
    
    public void testThatExtrusionHasSlottingControlsAndTheyCanBeSet() throws Exception {
        final List childrenByClass = ((ICDInternalExtrusion)this.findEntityByClassAndUserTag(this.solution, (Class)ICDInternalExtrusion.class, "testthis")).getChildrenByClass((Class)ICDCornerSlot.class, false);
        assertEquals("There should be 4 slotting control children of vertical inner extrusion", childrenByClass.size(), 4);
        ICDCornerSlot icdCornerSlot = null;
        ICDCornerSlot icdCornerSlot2 = null;
        ICDCornerSlot icdCornerSlot3 = null;
        ICDCornerSlot icdCornerSlot4 = null;
        for (final ICDCornerSlot icdCornerSlot5 : childrenByClass) {
            final String attributeValueAsString = icdCornerSlot5.getAttributeValueAsString("ICD_Slott_Type");
            if (attributeValueAsString.equals("1")) {
                icdCornerSlot = icdCornerSlot5;
            }
            else if (attributeValueAsString.equals("2")) {
                icdCornerSlot2 = icdCornerSlot5;
            }
            else if (attributeValueAsString.equals("3")) {
                icdCornerSlot3 = icdCornerSlot5;
            }
            else if (attributeValueAsString.equals("4")) {
                icdCornerSlot4 = icdCornerSlot5;
            }
            assertEquals("Inner vertical extrusion should have slot" + attributeValueAsString + " initially set to equal <System Controlled>", icdCornerSlot5.getAttributeValueAsString("ICD_Slot_Type_UserControllable"), "<System Controlled>");
        }
        icdCornerSlot.setAttributeValue("ICD_SLOT_1", (Object)"Slotted", (Class)this.getClass());
        icdCornerSlot.setAttributeValue("ICD_Slot_Type_UserControllable", (Object)"Slotted", (Class)this.getClass());
        assertEquals("Inner vertical extrusion slot 1 should now be slotted", icdCornerSlot.getAttributeValueAsString("ICD_Slot_Type_UserControllable"), "Slotted");
        icdCornerSlot3.setAttributeValue("ICD_SLOT_3", (Object)"Slotted", (Class)this.getClass());
        icdCornerSlot3.setAttributeValue("ICD_Slot_Type_UserControllable", (Object)"None", (Class)this.getClass());
        assertEquals("Inner vertical extrusion slot 3 should now be slotted", icdCornerSlot3.getAttributeValueAsString("ICD_Slot_Type_UserControllable"), "None");
        assertEquals("Inner vertical extrusion slot2 should still be equal to <System Controlled>", icdCornerSlot2.getAttributeValueAsString("ICD_Slot_Type_UserControllable"), "<System Controlled>");
        assertEquals("Inner vertical extrusion slot4 should still be equal to <System Controlled>", icdCornerSlot4.getAttributeValueAsString("ICD_Slot_Type_UserControllable"), "<System Controlled>");
    }
    
    static {
        ICDSlottingInnerVertExtrusionTest23669.path = "tests" + File.separator + "Quality" + File.separator + "ICDTestCases" + File.separator;
        fileToLoad = new File(ICDSlottingInnerVertExtrusionTest23669.path, "23669.ice");
    }
}
