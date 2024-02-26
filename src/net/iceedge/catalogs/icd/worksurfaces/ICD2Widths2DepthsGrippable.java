// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.TransformableEntity;

public interface ICD2Widths2DepthsGrippable
{
    TransformableEntity getEntity();
    
    void width1GripChanged(final String p0);
    
    void width2GripChanged(final String p0);
    
    void depth1GripChanged(final String p0);
    
    void depth2GripChanged(final String p0);
    
    float getWidthMin();
    
    float getWidthMax();
    
    float getWidth1Min();
    
    float getWidth1Max();
    
    float getWidth2Min();
    
    float getWidth2Max();
    
    float getDepthMin();
    
    float getDepthMax();
    
    float getDepth1Min();
    
    float getDepth1Max();
    
    float getDepth2Min();
    
    float getDepth2Max();
    
    Point3f getWidth1Anchor();
    
    Point3f getWidth2Anchor();
    
    Point3f getDepth1Anchor();
    
    Point3f getDepth2Anchor();
    
    Vector3f getWidth1Direction();
    
    Vector3f getWidth2Direction();
    
    Vector3f getDepth1Direction();
    
    Vector3f getDepth2Direction();
}
