package net.iceedge.catalogs.icd;

import net.dirtt.utilities.MathUtilities;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.catalogs.icd.panel.ICDSubInternalExtrusion;

public class ICDHorizontalBeam extends ICDSubInternalExtrusion
{
    public ICDHorizontalBeam(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDHorizontalBeam(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDHorizontalBeam buildClone(final ICDHorizontalBeam icdHorizontalBeam) {
        super.buildClone(icdHorizontalBeam);
        return icdHorizontalBeam;
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDHorizontalBeam(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDHorizontalBeam buildFrameClone(final ICDHorizontalBeam icdHorizontalBeam, final EntityObject entityObject) {
        super.buildFrameClone(icdHorizontalBeam, entityObject);
        return icdHorizontalBeam;
    }
    
    public void calculateDimensions() {
        super.calculateDimensions();
        final ICDBeamSegment icdBeamSegment = (ICDBeamSegment)this.getParent(ICDBeamSegment.class);
        if (icdBeamSegment != null) {
            this.setZDimension(icdBeamSegment.getXDimension() - 1.0f);
        }
    }
    
    public void calculateLocalSpace() {
        super.calculateLocalSpace();
        this.setBasePoint(0.5f, 0.0f, this.getAttributeValueAsFloat("ICD_Mount_Height"));
    }
    
    public void setModified(final boolean modified) {
        super.setModified(modified);
        if (modified) {
            final ICDBeamSegment icdBeamSegment = (ICDBeamSegment)this.getParent(ICDBeamSegment.class);
            if (icdBeamSegment != null && !icdBeamSegment.isModified()) {
                icdBeamSegment.setModified(true);
            }
        }
    }
    
    public void applyChangesForAttribute(final String s, final String s2, final boolean b, final boolean b2, final boolean b3) {
        super.applyChangesForAttribute(s, s2, b, b2, b3);
        if (s.equals("ICD_Mount_Height")) {
            final float attributeValueAsFloat = this.getAttributeValueAsFloat(s, -1.0f);
            final ICDBeamSegment icdBeamSegment = (ICDBeamSegment)this.getParent(ICDBeamSegment.class);
            if (icdBeamSegment != null && attributeValueAsFloat >= 0.0f && !MathUtilities.isSameFloat(attributeValueAsFloat, icdBeamSegment.getAttributeValueAsFloat(icdBeamSegment.getSegmentHeighKeytAttributeName()), 0.05f)) {
                icdBeamSegment.modifyAttributeValue(icdBeamSegment.getSegmentHeighKeytAttributeName(), s2);
            }
        }
    }
    
    @Override
    public boolean addTabbing(final int n) {
        return true;
    }
}
