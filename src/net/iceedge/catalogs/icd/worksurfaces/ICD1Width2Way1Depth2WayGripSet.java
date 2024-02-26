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

public class ICD1Width2Way1Depth2WayGripSet
{
    protected BasicAttributeGrip width1Grip;
    protected BasicAttributeGrip width2Grip;
    protected BasicAttributeGrip depth1Grip;
    protected BasicAttributeGrip depth2Grip;
    protected ICD2Widths2DepthsGrippable grippable;
    
    public ICD1Width2Way1Depth2WayGripSet(final ICD2Widths2DepthsGrippable grippable) {
        this.grippable = null;
        this.grippable = grippable;
        this.setupGripPainters();
    }
    
    public void setupGripPainters() {
        (this.width1Grip = (BasicAttributeGrip)new RelativeAttributeGrip(this.grippable.getEntity(), 0)).setLinkID((byte)100);
        this.width1Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICD1Width2Way1Depth2WayGripSet.this.grippable.width1GripChanged(s2);
                return true;
            }
        });
        (this.width2Grip = (BasicAttributeGrip)new RelativeAttributeGrip(this.grippable.getEntity(), 0)).setLinkID((byte)101);
        this.width2Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICD1Width2Way1Depth2WayGripSet.this.grippable.width2GripChanged(s2);
                return true;
            }
        });
        (this.depth1Grip = (BasicAttributeGrip)new RelativeAttributeGrip(this.grippable.getEntity(), 2)).setLinkID((byte)102);
        this.depth1Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICD1Width2Way1Depth2WayGripSet.this.grippable.depth1GripChanged(s2);
                return true;
            }
        });
        (this.depth2Grip = (BasicAttributeGrip)new RelativeAttributeGrip(this.grippable.getEntity(), 2)).setLinkID((byte)103);
        this.depth2Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICD1Width2Way1Depth2WayGripSet.this.grippable.depth2GripChanged(s2);
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
            if (basicAttributeGrip.equals(this.width2Grip)) {
                final Attribute attributeObject2 = this.grippable.getEntity().getAttributeObject("ICD_Parametric_Width");
                if (attributeObject2 != null) {
                    final float z2 = this.grippable.getEntity().getGeometricCenterPointWorld().z;
                    final float widthMin2 = this.grippable.getWidthMin();
                    final float widthMax2 = this.grippable.getWidthMax();
                    int n2 = -1;
                    for (float correctionOfTheFirstStep2 = widthMin2; correctionOfTheFirstStep2 <= widthMax2; correctionOfTheFirstStep2 += ICD2Widths2DepthsGripSet.ICD_GRIP_STEP) {
                        if (++n2 == 1) {
                            correctionOfTheFirstStep2 = this.correctionOfTheFirstStep(widthMin2);
                        }
                        final String string2 = "" + correctionOfTheFirstStep2;
                        final Attribute attribute2 = (Attribute)attributeObject2.clone();
                        attribute2.setCurrentValueAsString(string2);
                        set.add(new AttributeGripPoint(ICDParametricWorksurface.pointAt(this.grippable.getWidth2Anchor(), this.grippable.getWidth2Direction(), correctionOfTheFirstStep2), (int)(correctionOfTheFirstStep2 * ICD2Widths2DepthsGripSet.ICD_GRIP_WEIGHT_MULTIPLIER), new Attribute[] { attribute2 }));
                    }
                }
            }
            if (basicAttributeGrip.equals(this.depth1Grip)) {
                final Attribute attributeObject3 = this.grippable.getEntity().getAttributeObject("ICD_Parametric_Depth");
                if (attributeObject3 != null) {
                    final float z3 = this.grippable.getEntity().getGeometricCenterPointWorld().z;
                    final float depthMin = this.grippable.getDepthMin();
                    final float depthMax = this.grippable.getDepthMax();
                    int n3 = -1;
                    for (float correctionOfTheFirstStep3 = depthMin; correctionOfTheFirstStep3 <= depthMax; correctionOfTheFirstStep3 += ICD2Widths2DepthsGripSet.ICD_GRIP_STEP) {
                        if (++n3 == 1) {
                            correctionOfTheFirstStep3 = this.correctionOfTheFirstStep(depthMin);
                        }
                        final String string3 = "" + correctionOfTheFirstStep3;
                        final Attribute attribute3 = (Attribute)attributeObject3.clone();
                        attribute3.setCurrentValueAsString(string3);
                        set.add(new AttributeGripPoint(ICDParametricWorksurface.pointAt(this.grippable.getDepth1Anchor(), this.grippable.getDepth1Direction(), correctionOfTheFirstStep3), (int)(correctionOfTheFirstStep3 * ICD2Widths2DepthsGripSet.ICD_GRIP_WEIGHT_MULTIPLIER), new Attribute[] { attribute3 }));
                    }
                }
            }
            if (basicAttributeGrip.equals(this.depth2Grip)) {
                final Attribute attributeObject4 = this.grippable.getEntity().getAttributeObject("ICD_Parametric_Depth");
                if (attributeObject4 != null) {
                    final float z4 = this.grippable.getEntity().getGeometricCenterPointWorld().z;
                    final float depthMin2 = this.grippable.getDepthMin();
                    final float depthMax2 = this.grippable.getDepthMax();
                    int n4 = -1;
                    for (float correctionOfTheFirstStep4 = depthMin2; correctionOfTheFirstStep4 <= depthMax2; correctionOfTheFirstStep4 += ICD2Widths2DepthsGripSet.ICD_GRIP_STEP) {
                        if (++n4 == 1) {
                            correctionOfTheFirstStep4 = this.correctionOfTheFirstStep(depthMin2);
                        }
                        final String string4 = "" + correctionOfTheFirstStep4;
                        final Attribute attribute4 = (Attribute)attributeObject4.clone();
                        attribute4.setCurrentValueAsString(string4);
                        set.add(new AttributeGripPoint(ICDParametricWorksurface.pointAt(this.grippable.getDepth2Anchor(), this.grippable.getDepth2Direction(), correctionOfTheFirstStep4), (int)(correctionOfTheFirstStep4 * ICD2Widths2DepthsGripSet.ICD_GRIP_WEIGHT_MULTIPLIER), new Attribute[] { attribute4 }));
                    }
                }
            }
        }
        return set;
    }
    
    public void updateGrips(final BasicAttributeGrip basicAttributeGrip) {
        if (basicAttributeGrip != null) {
            this.updateGripPositions(new AttributeGripPoint(new Attribute[] { this.grippable.getEntity().getAttributeObject("ICD_Parametric_Width") }), new AttributeGripPoint(new Attribute[] { this.grippable.getEntity().getAttributeObject("ICD_Parametric_Width") }), new AttributeGripPoint(new Attribute[] { this.grippable.getEntity().getAttributeObject("ICD_Parametric_Depth") }), new AttributeGripPoint(new Attribute[] { this.grippable.getEntity().getAttributeObject("ICD_Parametric_Depth") }));
        }
    }
    
    private void updateGripPositions(final AttributeGripPoint attributeGripPoint, final AttributeGripPoint attributeGripPoint2, final AttributeGripPoint attributeGripPoint3, final AttributeGripPoint attributeGripPoint4) {
        if (attributeGripPoint != null) {
            this.width1Grip.updateGrip(attributeGripPoint);
            this.width1Grip.setLocation(ICDParametricWorksurface.pointAt(this.grippable.getWidth1Anchor(), this.grippable.getWidth1Direction(), this.grippable.getEntity().getAttributeValueAsFloat("ICD_Parametric_Width")));
            this.width1Grip.setAnchorLocation(this.grippable.getWidth1Anchor());
        }
        if (attributeGripPoint2 != null) {
            this.width2Grip.updateGrip(attributeGripPoint2);
            this.width2Grip.setLocation(ICDParametricWorksurface.pointAt(this.grippable.getWidth2Anchor(), this.grippable.getWidth2Direction(), this.grippable.getEntity().getAttributeValueAsFloat("ICD_Parametric_Width")));
            this.width2Grip.setAnchorLocation(this.grippable.getWidth2Anchor());
        }
        if (attributeGripPoint3 != null) {
            this.depth1Grip.updateGrip(attributeGripPoint3);
            this.depth1Grip.setLocation(ICDParametricWorksurface.pointAt(this.grippable.getDepth1Anchor(), this.grippable.getDepth1Direction(), this.grippable.getEntity().getAttributeValueAsFloat("ICD_Parametric_Depth")));
            this.depth1Grip.setAnchorLocation(this.grippable.getDepth1Anchor());
        }
        if (attributeGripPoint4 != null) {
            this.depth2Grip.updateGrip(attributeGripPoint4);
            this.depth2Grip.setLocation(ICDParametricWorksurface.pointAt(this.grippable.getDepth2Anchor(), this.grippable.getDepth2Direction(), this.grippable.getEntity().getAttributeValueAsFloat("ICD_Parametric_Depth")));
            this.depth2Grip.setAnchorLocation(this.grippable.getDepth2Anchor());
        }
    }
    
    public void selectGrips(final boolean b) {
        this.width1Grip.setSelected(b);
        this.width2Grip.setSelected(b);
        this.depth1Grip.setSelected(b);
        this.depth2Grip.setSelected(b);
    }
    
    public void deselectGrips() {
        this.width1Grip.setSelected(false);
        this.width2Grip.setSelected(false);
        this.depth1Grip.setSelected(false);
        this.depth2Grip.setSelected(false);
    }
    
    public void destroyGrips() {
        this.width1Grip.destroy();
        this.width2Grip.destroy();
        this.depth1Grip.destroy();
        this.depth2Grip.destroy();
    }
    
    public void drawGrips(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        this.width1Grip.draw2D(n, ice2DContainer, solutionSetting);
        this.width2Grip.draw2D(n, ice2DContainer, solutionSetting);
        this.depth1Grip.draw2D(n, ice2DContainer, solutionSetting);
        this.depth2Grip.draw2D(n, ice2DContainer, solutionSetting);
    }
    
    public float correctionOfTheFirstStep(final float n) {
        return ICD2Widths2DepthsGripSet.correctionOfTheFirstStep(n);
    }
}
