package net.iceedge.catalogs.icd.elevation.assembly;

import net.dirtt.utilities.TypeFilter;

public class AssemblyPaintableRootFilter implements TypeFilter<AssemblyPaintableRoot>
{
    public boolean matches(final Object o) {
        return o instanceof AssemblyPaintableRoot;
    }
    
    public AssemblyPaintableRoot get(final Object o) {
        return (AssemblyPaintableRoot)o;
    }
}
