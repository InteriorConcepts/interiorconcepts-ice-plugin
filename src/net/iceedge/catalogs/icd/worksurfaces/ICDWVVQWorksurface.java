package net.iceedge.catalogs.icd.worksurfaces;

import java.util.Iterator;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.attributes.Attribute;
import net.dirtt.icelib.ui.attribute.explorer.ui.entity.PossibleValue;
import java.util.TreeSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.AttributeGripPoint;
import java.util.SortedSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWVVQWorksurface extends ICDBasicWorksurface
{
    public ICDWVVQWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWVVQWorksurface icdwvvqWorksurface) {
        super.buildClone(icdwvvqWorksurface);
        return (TransformableEntity)icdwvvqWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWVVQWorksurface icdwvvqWorksurface) {
        super.buildClone2(icdwvvqWorksurface);
        return (TransformableEntity)icdwvvqWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWVVQWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVVQWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWVVQWorksurface icdwvvqWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdwvvqWorksurface, entityObject);
        return (EntityObject)icdwvvqWorksurface;
    }
    
    @Override
    public float getMaxXDimension() {
        if (this.getYDimensionFromData() > 60.0f) {
            return 60.0f;
        }
        return 144.0f;
    }
    
    @Override
    public float getMaxYDimension() {
        if (this.getXDimensionFromData() > 60.0f) {
            return 60.0f;
        }
        return 144.0f;
    }
    
    @Override
    public float getMinXDimension() {
        return 15.0f;
    }
    
    @Override
    public float getMinYDimension() {
        return 25.0f;
    }
    
    @Override
    public String getShapeTag() {
        if (this.isRightHanded()) {
            return "WVVQR";
        }
        return "WVVQL";
    }
    
    @Override
    public SortedSet<AttributeGripPoint> getAttributeMap(final BasicAttributeGrip basicAttributeGrip) {
        final TreeSet<AttributeGripPoint> set = new TreeSet<AttributeGripPoint>();
        if (this.getGeometricCenterPointWorld() != null) {
            if (basicAttributeGrip.equals(this.rightWidthGrip)) {
                final Attribute attributeObject = this.getAttributeObject("ICD_Indicated_Worksurface_Width");
                if (attributeObject != null) {
                    final float z = this.getGeometricCenterPointWorld().z;
                    final Iterator<PossibleValue> iterator = this.getAttributePossibleValues("ICD_Indicated_Worksurface_Width", false, this.getCurrentType()).iterator();
                    while (iterator.hasNext()) {
                        final String value = iterator.next().getValue();
                        final Attribute attribute = (Attribute)attributeObject.clone();
                        attribute.setCurrentValueAsString(value);
                        final int n = (int)Float.parseFloat(value);
                        if (n >= this.getMinXDimension()) {
                            if (n > this.getMaxXDimension()) {
                                continue;
                            }
                            set.add(new AttributeGripPoint(new Point3f((float)n, -this.getYDimensionFromData() / 2.0f, z), n, new Attribute[] { attribute }));
                        }
                    }
                }
            }
            else if (basicAttributeGrip.equals(this.bottomDepthGrip)) {
                final Attribute attributeObject2 = this.getAttributeObject("ICD_Indicated_Worksurface_Depth");
                if (attributeObject2 != null) {
                    final float z2 = this.getGeometricCenterPointWorld().z;
                    final Iterator<PossibleValue> iterator2 = this.getAttributePossibleValues("ICD_Indicated_Worksurface_Depth", false, this.getCurrentType()).iterator();
                    while (iterator2.hasNext()) {
                        final String value2 = iterator2.next().getValue();
                        final Attribute attribute2 = (Attribute)attributeObject2.clone();
                        attribute2.setCurrentValueAsString(value2);
                        final int n2 = (int)Float.parseFloat(value2);
                        if (n2 >= this.getMinYDimension()) {
                            if (n2 > this.getMaxYDimension()) {
                                continue;
                            }
                            set.add(new AttributeGripPoint(new Point3f(this.getXDimensionFromData() / 2.0f, (float)(-n2), z2), n2, new Attribute[] { attribute2 }));
                        }
                    }
                }
            }
        }
        return set;
    }
    
    @Override
    public float getYDimensionForReport() {
        return this.getAttributeValueAsFloat("ICD_Indicated_Worksurface_Depth");
    }
    
    @Override
    public float getXDimensionForReport() {
        return this.getAttributeValueAsFloat("ICD_Indicated_Worksurface_Width");
    }
    
    @Override
    public void updateGrips(final BasicAttributeGrip basicAttributeGrip) {
        if (this.getGeometricCenterPointWorld() != null) {
            AttributeGripPoint attributeGripPoint = null;
            AttributeGripPoint attributeGripPoint2 = null;
            if (basicAttributeGrip.equals(this.rightWidthGrip)) {
                attributeGripPoint = new AttributeGripPoint(new Attribute[] { this.getAttributeObject("ICD_Indicated_Worksurface_Width") });
            }
            else if (basicAttributeGrip.equals(this.bottomDepthGrip)) {
                attributeGripPoint2 = new AttributeGripPoint(new Attribute[] { this.getAttributeObject("ICD_Indicated_Worksurface_Depth") });
            }
            this.updateGripPositions(attributeGripPoint2, attributeGripPoint);
        }
    }
}
