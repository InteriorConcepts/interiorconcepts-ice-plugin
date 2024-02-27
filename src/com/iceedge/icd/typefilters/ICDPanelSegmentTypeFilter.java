package com.iceedge.icd.typefilters;

import net.iceedge.catalogs.icd.panel.ICDPanelSegment;
import net.dirtt.utilities.TypeFilter;

public class ICDPanelSegmentTypeFilter implements TypeFilter<ICDPanelSegment>
{
    public ICDPanelSegment get(final Object o) {
        return (ICDPanelSegment)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDPanelSegment;
    }
}
