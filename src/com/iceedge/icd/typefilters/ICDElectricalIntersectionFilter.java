// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.typefilters;

import net.iceedge.catalogs.icd.electrical.ICDElectricalIntersection;
import net.dirtt.utilities.TypeFilter;

public class ICDElectricalIntersectionFilter implements TypeFilter<ICDElectricalIntersection>
{
    public ICDElectricalIntersection get(final Object o) {
        return (ICDElectricalIntersection)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDElectricalIntersection;
    }
}
