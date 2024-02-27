package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.main.TypeObject;
import java.util.Iterator;
import net.dirtt.icelib.main.EntityObject;
import java.util.Vector;

public interface ICDVerticalExtrusion
{
    Vector<ICDSubInternalExtrusion> getTubings();
    
    Vector<ICDMiddleJoint> getJoints();
    
    void addToTree(final EntityObject p0);
    
    String getAttributeValueAsString(final String p0);
    
    void createNewAttribute(final String p0, final String p1);
    
    void modifyCurrentOption();
    
    void validateChildTypes();
    
    Iterator getChildren();
    
    TypeObject getChildTypeFor(final Class p0);
}
