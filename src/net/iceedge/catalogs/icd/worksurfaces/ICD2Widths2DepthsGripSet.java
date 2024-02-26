// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.utilities.MathUtilities;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.attributes.Attribute;
import java.util.TreeSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.AttributeGripPoint;
import java.util.SortedSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.GripListener;
import net.iceedge.icecore.basemodule.baseclasses.grips.RelativeAttributeGrip;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;

public class ICD2Widths2DepthsGripSet
{
    public static float ICD_GRIP_STEP;
    public static float ICD_GRIP_WEIGHT_MULTIPLIER;
    protected BasicAttributeGrip width1Grip;
    protected BasicAttributeGrip width2Grip;
    protected BasicAttributeGrip depth1Grip;
    protected BasicAttributeGrip depth2Grip;
    protected ICD2Widths2DepthsGrippable grippable;
    
    public ICD2Widths2DepthsGripSet(final ICD2Widths2DepthsGrippable grippable) {
        this.grippable = null;
        this.grippable = grippable;
        this.setupGripPainters();
    }
    
    public void setupGripPainters() {
        (this.width1Grip = (BasicAttributeGrip)new RelativeAttributeGrip(this.grippable.getEntity(), 0)).setLinkID((byte)100);
        this.width1Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICD2Widths2DepthsGripSet.this.grippable.width1GripChanged(s2);
                return true;
            }
        });
        (this.width2Grip = (BasicAttributeGrip)new RelativeAttributeGrip(this.grippable.getEntity(), 0)).setLinkID((byte)101);
        this.width2Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICD2Widths2DepthsGripSet.this.grippable.width2GripChanged(s2);
                return true;
            }
        });
        (this.depth1Grip = (BasicAttributeGrip)new RelativeAttributeGrip(this.grippable.getEntity(), 2)).setLinkID((byte)102);
        this.depth1Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICD2Widths2DepthsGripSet.this.grippable.depth1GripChanged(s2);
                return true;
            }
        });
        (this.depth2Grip = (BasicAttributeGrip)new RelativeAttributeGrip(this.grippable.getEntity(), 2)).setLinkID((byte)103);
        this.depth2Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICD2Widths2DepthsGripSet.this.grippable.depth2GripChanged(s2);
                return true;
            }
        });
    }
    
    public SortedSet<AttributeGripPoint> getAttributeMap(final BasicAttributeGrip basicAttributeGrip) {
        SortedSet<AttributeGripPoint> set = new TreeSet<AttributeGripPoint>();
        if (this.grippable.getEntity().getGeometricCenterPointWorld() != null) {
            if (basicAttributeGrip.equals(this.width1Grip)) {
                final Attribute attributeObject = this.grippable.getEntity().getAttributeObject("ICD_Parametric_Width1");
                if (attributeObject != null) {
                    set = this.getGripPoins(set, attributeObject, this.grippable.getWidth1Min(), this.grippable.getWidth1Max(), this.grippable.getWidth1Anchor(), this.grippable.getWidth1Direction());
                }
            }
            else if (basicAttributeGrip.equals(this.width2Grip)) {
                final Attribute attributeObject2 = this.grippable.getEntity().getAttributeObject("ICD_Parametric_Width2");
                if (attributeObject2 != null) {
                    set = this.getGripPoins(set, attributeObject2, this.grippable.getWidth2Min(), this.grippable.getWidth2Max(), this.grippable.getWidth2Anchor(), this.grippable.getWidth2Direction());
                }
            }
            else if (basicAttributeGrip.equals(this.depth1Grip)) {
                final Attribute attributeObject3 = this.grippable.getEntity().getAttributeObject("ICD_Parametric_Depth1");
                if (attributeObject3 != null) {
                    set = this.getGripPoins(set, attributeObject3, this.grippable.getDepth1Min(), this.grippable.getDepth1Max(), this.grippable.getDepth1Anchor(), this.grippable.getDepth1Direction());
                }
            }
            else if (basicAttributeGrip.equals(this.depth2Grip)) {
                final Attribute attributeObject4 = this.grippable.getEntity().getAttributeObject("ICD_Parametric_Depth2");
                if (attributeObject4 != null) {
                    set = this.getGripPoins(set, attributeObject4, this.grippable.getDepth2Min(), this.grippable.getDepth2Max(), this.grippable.getDepth2Anchor(), this.grippable.getDepth2Direction());
                }
            }
        }
        return set;
    }
    
    protected SortedSet<AttributeGripPoint> getGripPoins(final SortedSet<AttributeGripPoint> set, final Attribute attribute, final float n, final float n2, final Point3f point3f, final Vector3f vector3f) {
        if (attribute != null) {
            int n3 = -1;
            for (float correctionOfTheFirstStep = n; correctionOfTheFirstStep <= n2; correctionOfTheFirstStep += ICD2Widths2DepthsGripSet.ICD_GRIP_STEP) {
                if (++n3 == 1) {
                    correctionOfTheFirstStep = correctionOfTheFirstStep(n);
                }
                final String string = "" + correctionOfTheFirstStep;
                final Attribute attribute2 = (Attribute)attribute.clone();
                attribute2.setCurrentValueAsString(string);
                set.add(new AttributeGripPoint(ICDParametricWorksurface.pointAt(point3f, vector3f, correctionOfTheFirstStep), (int)(correctionOfTheFirstStep * ICD2Widths2DepthsGripSet.ICD_GRIP_WEIGHT_MULTIPLIER), new Attribute[] { attribute2 }));
            }
        }
        return set;
    }
    
    public void updateGrips(final BasicAttributeGrip basicAttributeGrip) {
        if (basicAttributeGrip != null) {
            this.updateGripPositions(new AttributeGripPoint(new Attribute[] { this.grippable.getEntity().getAttributeObject("ICD_Parametric_Width1") }), new AttributeGripPoint(new Attribute[] { this.grippable.getEntity().getAttributeObject("ICD_Parametric_Width2") }), new AttributeGripPoint(new Attribute[] { this.grippable.getEntity().getAttributeObject("ICD_Parametric_Depth1") }), new AttributeGripPoint(new Attribute[] { this.grippable.getEntity().getAttributeObject("ICD_Parametric_Depth2") }));
        }
    }
    
    protected void updateGripPositions(final AttributeGripPoint attributeGripPoint, final AttributeGripPoint attributeGripPoint2, final AttributeGripPoint attributeGripPoint3, final AttributeGripPoint attributeGripPoint4) {
        if (attributeGripPoint != null) {
            this.width1Grip.updateGrip(attributeGripPoint);
            this.width1Grip.setLocation(ICDParametricWorksurface.pointAt(this.grippable.getWidth1Anchor(), this.grippable.getWidth1Direction(), this.grippable.getEntity().getAttributeValueAsFloat("ICD_Parametric_Width1")));
            this.width1Grip.setAnchorLocation(this.grippable.getWidth1Anchor());
        }
        if (attributeGripPoint2 != null) {
            this.width2Grip.updateGrip(attributeGripPoint2);
            this.width2Grip.setLocation(ICDParametricWorksurface.pointAt(this.grippable.getWidth2Anchor(), this.grippable.getWidth2Direction(), this.grippable.getEntity().getAttributeValueAsFloat("ICD_Parametric_Width2")));
            this.width2Grip.setAnchorLocation(this.grippable.getWidth2Anchor());
        }
        if (attributeGripPoint3 != null) {
            this.depth1Grip.updateGrip(attributeGripPoint3);
            this.depth1Grip.setLocation(ICDParametricWorksurface.pointAt(this.grippable.getDepth1Anchor(), this.grippable.getDepth1Direction(), this.grippable.getEntity().getAttributeValueAsFloat("ICD_Parametric_Depth1")));
            this.depth1Grip.setAnchorLocation(this.grippable.getDepth1Anchor());
        }
        if (attributeGripPoint4 != null) {
            this.depth2Grip.updateGrip(attributeGripPoint4);
            this.depth2Grip.setLocation(ICDParametricWorksurface.pointAt(this.grippable.getDepth2Anchor(), this.grippable.getDepth2Direction(), this.grippable.getEntity().getAttributeValueAsFloat("ICD_Parametric_Depth2")));
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
    
    public static float correctionOfTheFirstStep(float n) {
        final float n2 = n - (int)n;
        if (!MathUtilities.isSameFloat(n2, ICD2Widths2DepthsGripSet.ICD_GRIP_STEP, 0.001f)) {
            if (n2 < ICD2Widths2DepthsGripSet.ICD_GRIP_STEP) {
                n = (int)n + ICD2Widths2DepthsGripSet.ICD_GRIP_STEP;
            }
            else {
                n = (int)n + 2.0f * ICD2Widths2DepthsGripSet.ICD_GRIP_STEP;
            }
        }
        return n;
    }
    
    static {
        ICD2Widths2DepthsGripSet.ICD_GRIP_STEP = 0.5f;
        ICD2Widths2DepthsGripSet.ICD_GRIP_WEIGHT_MULTIPLIER = 10000.0f;
    }
}
