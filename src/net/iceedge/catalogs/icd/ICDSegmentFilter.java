package net.iceedge.catalogs.icd;

import net.dirtt.utilities.TypeFilter;

public class ICDSegmentFilter implements TypeFilter<ICDSegment>
{
    public ICDSegment get(final Object o) {
        return (ICDSegment)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDSegment;
    }
}
