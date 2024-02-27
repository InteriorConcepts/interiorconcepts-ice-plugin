package com.iceedge.icd.typefilters;

import net.iceedge.catalogs.icd.intersection.ICDPost;
import net.dirtt.utilities.TypeFilter;

public class ICDPostTypeFilter implements TypeFilter<ICDPost>
{
    public ICDPost get(final Object o) {
        return (ICDPost)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDPost;
    }
}
