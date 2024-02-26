// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;
import net.dirtt.icelib.main.attributes.Attribute;
import java.util.TreeSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.AttributeGripPoint;
import java.util.SortedSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.GripListener;
import net.iceedge.icecore.basemodule.baseclasses.grips.RelativeAttributeGrip;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;

public class ICD1Width1DepthGripSet
{
    protected BasicAttributeGrip width1Grip;
    protected BasicAttributeGrip depth1Grip;
    protected ICD1Width1DepthGrippable grippable;
    
    public ICD1Width1DepthGripSet(final ICD1Width1DepthGrippable grippable) {
        this.grippable = null;
        this.grippable = grippable;
        this.setupGripPainters();
    }
    
    public void setupGripPainters() {
        (this.width1Grip = (BasicAttributeGrip)new RelativeAttributeGrip(this.grippable.getEntity(), 0)).setLinkID((byte)100);
        this.width1Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICD1Width1DepthGripSet.this.grippable.width1GripChanged(s2);
                return true;
            }
        });
        (this.depth1Grip = (BasicAttributeGrip)new RelativeAttributeGrip(this.grippable.getEntity(), 2)).setLinkID((byte)101);
        this.depth1Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICD1Width1DepthGripSet.this.grippable.depth1GripChanged(s2);
                return true;
            }
        });
    }
    
    public SortedSet<AttributeGripPoint> getAttributeMap(final BasicAttributeGrip basicAttributeGrip) {
        final TreeSet<AttributeGripPoint> set = new TreeSet<AttributeGripPoint>();
        if (this.grippable.getEntity().getGeometricCenterPointWorld() != null) {
            if (basicAttributeGrip.equals(this.width1Grip)) {
                final Attribute attributeObject = this.grippable.getEntity().getAttributeObject("ICD_Parametric_Width");
                if (attributeObject != null) {
                    final float z = this.grippable.getEntity().getGeometricCenterPointWorld().z;
                    final float widthMin = this.grippable.getWidthMin();
                    final float widthMax = this.grippable.getWidthMax();
                    int n = -1;
                    for (float correctionOfTheFirstStep = widthMin; correctionOfTheFirstStep <= widthMax; correctionOfTheFirstStep += ICD2Widths2DepthsGripSet.ICD_GRIP_STEP) {
                        if (++n == 1) {
                            correctionOfTheFirstStep = this.correctionOfTheFirstStep(widthMin);
                        }
                        final String string = "" + correctionOfTheFirstStep;
                        final Attribute attribute = (Attribute)attributeObject.clone();
                        attribute.setCurrentValueAsString(string);
                        set.add(new AttributeGripPoint(ICDParametricWorksurface.pointAt(this.grippable.getWidth1Anchor(), this.grippable.getWidth1Direction(), correctionOfTheFirstStep), (int)(correctionOfTheFirstStep * ICD2Widths2DepthsGripSet.ICD_GRIP_WEIGHT_MULTIPLIER), new Attribute[] { attribute }));
                    }
                }
            }
            if (basicAttributeGrip.equals(this.depth1Grip)) {
                final Attribute attributeObject2 = this.grippable.getEntity().getAttributeObject("ICD_Parametric_Depth1");
                if (attributeObject2 != null) {
                    final float z2 = this.grippable.getEntity().getGeometricCenterPointWorld().z;
                    final float depth1Min = this.grippable.getDepth1Min();
                    final float depth1Max = this.grippable.getDepth1Max();
                    int n2 = -1;
                    for (float correctionOfTheFirstStep2 = depth1Min; correctionOfTheFirstStep2 <= depth1Max; correctionOfTheFirstStep2 += ICD2Widths2DepthsGripSet.ICD_GRIP_STEP) {
                        if (++n2 == 1) {
                            correctionOfTheFirstStep2 = this.correctionOfTheFirstStep(depth1Min);
                        }
                        final String string2 = "" + correctionOfTheFirstStep2;
                        final Attribute attribute2 = (Attribute)attributeObject2.clone();
                        attribute2.setCurrentValueAsString(string2);
                        set.add(new AttributeGripPoint(ICDParametricWorksurface.pointAt(this.grippable.getDepth1Anchor(), this.grippable.getDepth1Direction(), correctionOfTheFirstStep2), (int)(correctionOfTheFirstStep2 * ICD2Widths2DepthsGripSet.ICD_GRIP_WEIGHT_MULTIPLIER), new Attribute[] { attribute2 }));
                    }
                }
            }
        }
        return set;
    }
    
    public void updateGrips(final BasicAttributeGrip basicAttributeGrip) {
        if (basicAttributeGrip != null) {
            this.updateGripPositions(new AttributeGripPoint(new Attribute[] { this.grippable.getEntity().getAttributeObject("ICD_Parametric_Width") }), null, new AttributeGripPoint(new Attribute[] { this.grippable.getEntity().getAttributeObject("ICD_Parametric_Depth1") }), null);
        }
    }
    
    private void updateGripPositions(final AttributeGripPoint attributeGripPoint, final AttributeGripPoint attributeGripPoint2, final AttributeGripPoint attributeGripPoint3, final AttributeGripPoint attributeGripPoint4) {
        if (attributeGripPoint != null) {
            this.width1Grip.updateGrip(attributeGripPoint);
            this.width1Grip.setLocation(ICDParametricWorksurface.pointAt(this.grippable.getWidth1Anchor(), this.grippable.getWidth1Direction(), this.grippable.getEntity().getAttributeValueAsFloat("ICD_Parametric_Width")));
            this.width1Grip.setAnchorLocation(this.grippable.getWidth1Anchor());
        }
        if (attributeGripPoint3 != null) {
            this.depth1Grip.updateGrip(attributeGripPoint3);
            this.depth1Grip.setLocation(ICDParametricWorksurface.pointAt(this.grippable.getDepth1Anchor(), this.grippable.getDepth1Direction(), this.grippable.getEntity().getAttributeValueAsFloat("ICD_Parametric_Depth1")));
            this.depth1Grip.setAnchorLocation(this.grippable.getDepth1Anchor());
        }
    }
    
    public void selectGrips(final boolean b) {
        this.width1Grip.setSelected(b);
        this.depth1Grip.setSelected(b);
    }
    
    public void deselectGrips() {
        this.width1Grip.setSelected(false);
        this.depth1Grip.setSelected(false);
    }
    
    public void destroyGrips() {
        this.width1Grip.destroy();
        this.depth1Grip.destroy();
    }
    
    public void drawGrips(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        this.width1Grip.draw2D(n, ice2DContainer, solutionSetting);
        this.depth1Grip.draw2D(n, ice2DContainer, solutionSetting);
    }
    
    public float correctionOfTheFirstStep(final float n) {
        return ICD2Widths2DepthsGripSet.correctionOfTheFirstStep(n);
    }
}
