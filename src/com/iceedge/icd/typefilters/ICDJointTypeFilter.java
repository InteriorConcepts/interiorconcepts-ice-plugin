package com.iceedge.icd.typefilters;

import net.iceedge.catalogs.icd.panel.ICDJoint;
import net.dirtt.utilities.TypeFilter;

public class ICDJointTypeFilter implements TypeFilter<ICDJoint>
{
    public ICDJoint get(final Object o) {
        return (ICDJoint)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDJoint;
    }
}
