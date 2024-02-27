package net.iceedge.catalogs.icd.intersection;

import net.dirtt.utilities.TypeFilter;

public class ICDIntersectionFilter implements TypeFilter<ICDIntersection>
{
    public ICDIntersection get(final Object o) {
        return (ICDIntersection)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDIntersection;
    }
}
