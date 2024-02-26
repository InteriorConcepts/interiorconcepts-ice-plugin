// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.typefilters;

import net.iceedge.catalogs.icd.panel.ICDTopExtrusion;
import net.dirtt.utilities.TypeFilter;

public class ICDTopExtrusionTypeFilter implements TypeFilter<ICDTopExtrusion>
{
    public ICDTopExtrusion get(final Object o) {
        return (ICDTopExtrusion)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDTopExtrusion;
    }
}
