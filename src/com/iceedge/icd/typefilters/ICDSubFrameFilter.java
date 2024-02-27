package com.iceedge.icd.typefilters;

import net.iceedge.catalogs.icd.panel.ICDSubFrame;
import net.dirtt.utilities.TypeFilter;

public class ICDSubFrameFilter implements TypeFilter<ICDSubFrame>
{
    public ICDSubFrame get(final Object o) {
        return (ICDSubFrame)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDSubFrame;
    }
}
