// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.utilities.Pair;
import java.util.List;

public interface ICDHorizontalBreakableExtrusion
{
    void breakHorizontalExtrusion(final float p0, final boolean p1);
    
    List<Pair<Float, Integer>> getBreakLocationsForBeam();
    
    void SetBreakLocationForBeam(final List<Pair<Float, Integer>> p0);
}
