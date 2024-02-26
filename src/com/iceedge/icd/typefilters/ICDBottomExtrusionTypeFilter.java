// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.typefilters;

import net.iceedge.catalogs.icd.panel.ICDBottomExtrusion;
import net.dirtt.utilities.TypeFilter;

public class ICDBottomExtrusionTypeFilter implements TypeFilter<ICDBottomExtrusion>
{
    public ICDBottomExtrusion get(final Object o) {
        return (ICDBottomExtrusion)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDBottomExtrusion;
    }
}
