// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.typefilters;

import net.iceedge.catalogs.icd.panel.ICDChaseConnectorExtrusion;
import net.dirtt.utilities.TypeFilter;

public class ICDChaseConnectorExtrusionTypeFilter implements TypeFilter<ICDChaseConnectorExtrusion>
{
    public ICDChaseConnectorExtrusion get(final Object o) {
        return (ICDChaseConnectorExtrusion)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDChaseConnectorExtrusion;
    }
}
