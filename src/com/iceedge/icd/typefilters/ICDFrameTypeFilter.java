package com.iceedge.icd.typefilters;

import net.iceedge.catalogs.icd.panel.ICDFrame;
import net.dirtt.utilities.TypeFilter;

public class ICDFrameTypeFilter implements TypeFilter<ICDFrame>
{
    public ICDFrame get(final Object o) {
        return (ICDFrame)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDFrame;
    }
}
