// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.utilities.TypeFilter;

public class ICDPanelToPanelConnectionHWFilter implements TypeFilter<ICDPanelToPanelConnectionHW>
{
    public ICDPanelToPanelConnectionHW get(final Object o) {
        return (ICDPanelToPanelConnectionHW)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDPanelToPanelConnectionHW;
    }
}
