package net.iceedge.catalogs.icd.elevation.assembly;

import java.util.List;

public interface AssemblyPaintableRoot
{
    void addAdditonalPaintableEntities(final List<AssemblyPaintable> p0);
    
    boolean shouldICDMakePreAssembledReport();
    
    boolean checkAssembledForAssemblyElevation();
    
    boolean shouldPaintAssemblyInIce2D();
}
