// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.typefilters;

import net.iceedge.catalogs.icd.intersection.ICDPostHostInterface;
import net.dirtt.utilities.TypeFilter;

public class ICDPostHostInterfaceTypeFilter implements TypeFilter<ICDPostHostInterface>
{
    public ICDPostHostInterface get(final Object o) {
        return (ICDPostHostInterface)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDPostHostInterface;
    }
}
