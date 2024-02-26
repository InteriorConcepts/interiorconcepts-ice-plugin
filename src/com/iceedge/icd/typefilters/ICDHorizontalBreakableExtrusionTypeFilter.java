// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.typefilters;

import net.iceedge.catalogs.icd.panel.ICDHorizontalBreakableExtrusion;
import net.dirtt.utilities.TypeFilter;

public class ICDHorizontalBreakableExtrusionTypeFilter implements TypeFilter<ICDHorizontalBreakableExtrusion>
{
    public ICDHorizontalBreakableExtrusion get(final Object o) {
        return (ICDHorizontalBreakableExtrusion)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDHorizontalBreakableExtrusion;
    }
}
