package net.iceedge.catalogs.icd.icecad;

import javax.vecmath.Point3f;
import net.iceedge.icecore.basemodule.interfaces.lightweight.CADPaintable;

public interface ICDCadTagPaintable extends CADPaintable
{
    float getRotationWorldSpace();
    
    Point3f getGeometricCenterPointLocal();
    
    float getXDimension();
}
