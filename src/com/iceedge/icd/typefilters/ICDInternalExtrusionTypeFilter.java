package com.iceedge.icd.typefilters;

import net.iceedge.catalogs.icd.panel.ICDInternalExtrusion;
import net.dirtt.utilities.TypeFilter;

public class ICDInternalExtrusionTypeFilter implements TypeFilter<ICDInternalExtrusion>
{
    public ICDInternalExtrusion get(final Object o) {
        return (ICDInternalExtrusion)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDInternalExtrusion;
    }
}
