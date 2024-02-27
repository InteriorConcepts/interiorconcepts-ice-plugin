package net.iceedge.catalogs.icd.worksurfaces;

import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.TransformableEntity;

public interface ICD1Width2Way1DepthGrippable
{
    TransformableEntity getEntity();
    
    void width1GripChanged(final String p0);
    
    void width2GripChanged(final String p0);
    
    void depth1GripChanged(final String p0);
    
    float getWidthMin();
    
    float getWidthMax();
    
    float getWidth1Min();
    
    float getWidth1Max();
    
    float getDepthMin();
    
    float getDepthMax();
    
    float getDepth1Min();
    
    float getDepth1Max();
    
    Point3f getWidth1Anchor();
    
    Point3f getWidth2Anchor();
    
    Point3f getDepth1Anchor();
    
    Vector3f getWidth1Direction();
    
    Vector3f getWidth2Direction();
    
    Vector3f getDepth1Direction();
}
