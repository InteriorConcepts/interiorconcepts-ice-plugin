package net.iceedge.catalogs.icd.worksurfaces;

import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.TransformableEntity;

public interface ICD1Width1DepthGrippable
{
    TransformableEntity getEntity();
    
    void width1GripChanged(final String p0);
    
    void depth1GripChanged(final String p0);
    
    float getWidthMin();
    
    float getWidthMax();
    
    float getDepth1Min();
    
    float getDepth1Max();
    
    Point3f getWidth1Anchor();
    
    Point3f getDepth1Anchor();
    
    Vector3f getWidth1Direction();
    
    Vector3f getDepth1Direction();
}
