package net.iceedge.catalogs.icd.intersection;

import net.iceedge.icecore.basemodule.interfaces.ILineInterface;
import net.iceedge.icecore.basemodule.finalclasses.GeneralSnapSet;
import java.util.Vector;
import javax.vecmath.Point3f;

public interface ICDPostHostInterface
{
    String getJointTypeAtLocation(final Point3f p0);
    
    Vector<Float> getSplitLocations();
    
    float getTallestWallHeight();
    
    String getBottomJointType();
    
    Point3f getBasePointWorldSpace();
    
    GeneralSnapSet getGeneralSnapSet();
    
    Vector<ILineInterface> getConnectedILines();
}
