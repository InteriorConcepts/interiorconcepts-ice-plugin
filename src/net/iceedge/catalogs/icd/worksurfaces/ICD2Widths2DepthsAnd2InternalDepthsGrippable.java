package net.iceedge.catalogs.icd.worksurfaces;

import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;

public interface ICD2Widths2DepthsAnd2InternalDepthsGrippable extends ICD2Widths2DepthsGrippable
{
    void innerDepth1GripChanged(final String p0);
    
    float getInnerDepth1Min();
    
    float getInnerDepth1Max();
    
    Point3f getInnerDepth1Anchor();
    
    Vector3f getInnerDepth1Direction();
    
    void innerDepth2GripChanged(final String p0);
    
    float getInnerDepth2Min();
    
    float getInnerDepth2Max();
    
    Point3f getInnerDepth2Anchor();
    
    Vector3f getInnerDepth2Direction();
}
