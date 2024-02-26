// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd;

import net.dirtt.utilities.TypeFilter;

public class ICDILineFilter implements TypeFilter<ICDILine>
{
    public ICDILine get(final Object o) {
        return (ICDILine)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDILine;
    }
}
