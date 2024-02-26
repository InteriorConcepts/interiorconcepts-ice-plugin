// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.utilities;

import net.iceedge.catalogs.icd.panel.ICDSubFrameSideContainer;
import net.iceedge.catalogs.icd.panel.ICDTab;
import net.iceedge.catalogs.icd.panel.ICDTabContainer;
import java.util.List;
import java.util.Iterator;
import java.util.Vector;
import net.iceedge.catalogs.icd.intersection.ICDCornerSlot;
import java.util.Collection;
import net.dirtt.icelib.main.EntityObject;

public class ICDExtrusionUtilities
{
    public static Collection<ICDCornerSlot> getAllSlots(final EntityObject entityObject) {
        final Vector<ICDCornerSlot> vector = new Vector<ICDCornerSlot>();
        final Iterator children = entityObject.getChildren();
        while (children.hasNext()) {
            final EntityObject entityObject2 = children.next();
            if (entityObject2 instanceof ICDCornerSlot) {
                vector.add((ICDCornerSlot)entityObject2);
            }
        }
        return vector;
    }
    
    public static Vector<ICDCornerSlot> getAllSlottedSlots(final EntityObject entityObject) {
        final Vector<ICDCornerSlot> vector = new Vector<ICDCornerSlot>();
        for (final ICDCornerSlot e : getAllSlots(entityObject)) {
            if (e != null && e.isSlotted()) {
                vector.add(e);
            }
        }
        return vector;
    }
    
    private static List<ICDTabContainer> getTabContainers(final EntityObject entityObject) {
        return (List<ICDTabContainer>)entityObject.getChildrenByClass((Class)ICDTabContainer.class, false);
    }
    
    public static String getTabCode(final EntityObject entityObject) {
        String string = "";
        final List<ICDTabContainer> tabContainers = getTabContainers(entityObject);
        boolean b = false;
        boolean b2 = false;
        boolean b3 = false;
        for (int i = 0; i < tabContainers.size(); ++i) {
            final int side = tabContainers.get(i).getSide();
            final List<ICDTab> tabs = tabContainers.get(i).getTabs(true);
            final List<ICDTab> tabs2 = tabContainers.get(i).getTabs(false);
            if (tabs != null && tabs.size() > 0) {
                if (tabs2 != null && tabs2.size() != tabs.size()) {
                    b3 = true;
                }
                if (side == 0) {
                    b = true;
                }
                if (side == 1) {
                    b2 = true;
                }
            }
        }
        if (b && b2) {
            string = "S2";
        }
        else if (b || b2) {
            string = "S1";
        }
        if (b3) {
            string += "-spec";
        }
        return string;
    }
    
    public static boolean isPartOfChase(final EntityObject entityObject) {
        return entityObject.getParent((Class)ICDSubFrameSideContainer.class) != null;
    }
}
