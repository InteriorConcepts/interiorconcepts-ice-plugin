package net.iceedge.catalogs.icd;

import net.dirtt.utilities.MathUtilities;
import javax.vecmath.Point3f;
import net.iceedge.icecore.basemodule.baseclasses.BasicIntersection;
import java.util.Vector;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import org.apache.log4j.Logger;

public class ICDBeamSegment extends ICDSegment
{
    private static Logger logger;
    
    public ICDBeamSegment(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDBeamSegment(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDBeamSegment buildClone(final ICDBeamSegment icdBeamSegment) {
        super.buildClone(icdBeamSegment);
        return icdBeamSegment;
    }
    
    public Vector<Float> getTubeLocations() {
        final Vector<Float> vector = new Vector<Float>();
        final float tubeLocation = this.getTubeLocation();
        if (tubeLocation >= 0.0f) {
            vector.add(tubeLocation);
        }
        return vector;
    }
    
    public float getTubeLocation() {
        final ICDHorizontalBeam icdHorizontalBeam = (ICDHorizontalBeam)this.getChild(ICDHorizontalBeam.class, false);
        if (icdHorizontalBeam != null) {
            return icdHorizontalBeam.getBasePoint3f().z;
        }
        return -1.0f;
    }
    
    public Vector<Float> getSplitLocation(final BasicIntersection basicIntersection) {
        return this.getTubeLocations();
    }
    
    public Vector<Float> getSplitLocation(final boolean b) {
        return this.getTubeLocations();
    }
    
    public boolean hasExtrusionAtLocation(final Point3f point3f) {
        final Vector<Float> tubeLocations = this.getTubeLocations();
        return tubeLocations.size() > 0 && MathUtilities.isSameFloat((float)tubeLocations.get(0), point3f.z, 1.0f);
    }
    
    public void applyChangesForAttribute(final String s, final String s2, final boolean b, final boolean b2, final boolean b3) {
        super.applyChangesForAttribute(s, s2, b, b2, b3);
        if (s.equals(this.getSegmentHeighKeytAttributeName())) {
            final float attributeValueAsFloat = this.getAttributeValueAsFloat(s);
            final ICDHorizontalBeam icdHorizontalBeam = (ICDHorizontalBeam)this.getChildByClass(ICDHorizontalBeam.class);
            if (icdHorizontalBeam != null && !MathUtilities.isSameFloat(icdHorizontalBeam.getAttributeValueAsFloat("ICD_Mount_Height"), attributeValueAsFloat, 0.05f)) {
                icdHorizontalBeam.modifyAttributeValue("ICD_Mount_Height", s2);
            }
        }
    }
    
    public void handle3DSelect(final boolean b) {
    }
    
    @Override
    public boolean isHeightGripEnabled() {
        return false;
    }
    
    public void setSelected(final boolean selected) {
        super.setSelected(selected);
        if (selected) {
            final ICDHorizontalBeam icdHorizontalBeam = (ICDHorizontalBeam)this.getChild(ICDHorizontalBeam.class, false);
            if (icdHorizontalBeam != null && !icdHorizontalBeam.isSelected()) {
                icdHorizontalBeam.setSelected(true);
            }
        }
    }
    
    static {
        ICDBeamSegment.logger = Logger.getLogger(ICDBeamSegment.class);
    }
}
