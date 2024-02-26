// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.typefilters;

import net.iceedge.catalogs.icd.panel.ICDPanelToPanelConnectionHW;
import net.dirtt.utilities.TypeFilter;

public class ICDPanelToPanelConnectionHWTypeFilter implements TypeFilter<ICDPanelToPanelConnectionHW>
{
    public ICDPanelToPanelConnectionHW get(final Object o) {
        return (ICDPanelToPanelConnectionHW)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDPanelToPanelConnectionHW;
    }
}
