// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.elevation.assembly;

import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import java.util.List;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import java.util.Vector;
import javax.vecmath.Matrix4f;
import net.dirtt.icelib.main.TransformableEntity;
import javax.vecmath.Point3f;

public interface AssemblyPaintable
{
    public static final int ASSEMBLY_FONT_SIZE = 3;
    
    boolean shouldDrawAssembly();
    
    boolean isAssembled();
    
    Vector<Ice2DPaintableNode> getAssemblyIcons(final int p0, final Point3f p1, final TransformableEntity p2, final Matrix4f p3);
    
    List<IceOutputNode> getPlotOutputNodes(final int p0, final Point3f p1, final TransformableEntity p2, final Matrix4f p3);
}
