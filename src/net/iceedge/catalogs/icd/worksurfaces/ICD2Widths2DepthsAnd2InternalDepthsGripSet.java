// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;
import net.dirtt.icelib.main.attributes.Attribute;
import net.iceedge.icecore.basemodule.baseclasses.grips.AttributeGripPoint;
import java.util.SortedSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.GripListener;
import net.iceedge.icecore.basemodule.baseclasses.grips.RelativeAttributeGrip;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;

public class ICD2Widths2DepthsAnd2InternalDepthsGripSet extends ICD2Widths2DepthsGripSet
{
    protected BasicAttributeGrip innerDepth1Grip;
    protected BasicAttributeGrip innerDepth2Grip;
    protected ICD2Widths2DepthsAnd2InternalDepthsGrippable grippable;
    
    public ICD2Widths2DepthsAnd2InternalDepthsGripSet(final ICD2Widths2DepthsAnd2InternalDepthsGrippable grippable) {
        super(grippable);
        this.grippable = null;
        this.grippable = grippable;
        this.addGripPainters();
    }
    
    public void addGripPainters() {
        (this.innerDepth1Grip = (BasicAttributeGrip)new RelativeAttributeGrip(this.grippable.getEntity(), 2)).setLinkID((byte)104);
        this.innerDepth1Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICD2Widths2DepthsAnd2InternalDepthsGripSet.this.grippable.innerDepth1GripChanged(s2);
                return true;
            }
        });
        (this.innerDepth2Grip = (BasicAttributeGrip)new RelativeAttributeGrip(this.grippable.getEntity(), 2)).setLinkID((byte)105);
        this.innerDepth2Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICD2Widths2DepthsAnd2InternalDepthsGripSet.this.grippable.innerDepth2GripChanged(s2);
                return true;
            }
        });
    }
    
    public void updateGripPainters() {
        super.setupGripPainters();
        this.addGripPainters();
    }
    
    @Override
    public SortedSet<AttributeGripPoint> getAttributeMap(final BasicAttributeGrip basicAttributeGrip) {
        SortedSet<AttributeGripPoint> set = super.getAttributeMap(basicAttributeGrip);
        if (this.grippable.getEntity().getGeometricCenterPointWorld() != null) {
            if (basicAttributeGrip.equals(this.innerDepth1Grip)) {
                final Attribute attributeObject = this.grippable.getEntity().getAttributeObject("ICD_Parametric_Inner_Depth1");
                if (attributeObject != null) {
                    set = this.getGripPoins(set, attributeObject, this.grippable.getInnerDepth1Min(), this.grippable.getInnerDepth1Max(), this.grippable.getInnerDepth1Anchor(), this.grippable.getInnerDepth1Direction());
                }
            }
            else if (basicAttributeGrip.equals(this.innerDepth2Grip)) {
                final Attribute attributeObject2 = this.grippable.getEntity().getAttributeObject("ICD_Parametric_Inner_Depth2");
                if (attributeObject2 != null) {
                    set = this.getGripPoins(set, attributeObject2, this.grippable.getInnerDepth2Min(), this.grippable.getInnerDepth2Max(), this.grippable.getInnerDepth2Anchor(), this.grippable.getInnerDepth2Direction());
                }
            }
        }
        return set;
    }
    
    @Override
    public void updateGrips(final BasicAttributeGrip basicAttributeGrip) {
        if (basicAttributeGrip != null) {
            this.updateGripPositions(new AttributeGripPoint(new Attribute[] { this.grippable.getEntity().getAttributeObject("ICD_Parametric_Width1") }), new AttributeGripPoint(new Attribute[] { this.grippable.getEntity().getAttributeObject("ICD_Parametric_Width2") }), new AttributeGripPoint(new Attribute[] { this.grippable.getEntity().getAttributeObject("ICD_Parametric_Depth1") }), new AttributeGripPoint(new Attribute[] { this.grippable.getEntity().getAttributeObject("ICD_Parametric_Depth2") }), new AttributeGripPoint(new Attribute[] { this.grippable.getEntity().getAttributeObject("ICD_Parametric_Inner_Depth1") }), new AttributeGripPoint(new Attribute[] { this.grippable.getEntity().getAttributeObject("ICD_Parametric_Inner_Depth2") }));
        }
    }
    
    protected void updateGripPositions(final AttributeGripPoint attributeGripPoint, final AttributeGripPoint attributeGripPoint2, final AttributeGripPoint attributeGripPoint3, final AttributeGripPoint attributeGripPoint4, final AttributeGripPoint attributeGripPoint5, final AttributeGripPoint attributeGripPoint6) {
        super.updateGripPositions(attributeGripPoint, attributeGripPoint2, attributeGripPoint3, attributeGripPoint4);
        if (attributeGripPoint5 != null) {
            this.innerDepth1Grip.updateGrip(attributeGripPoint5);
            this.innerDepth1Grip.setLocation(ICDParametricWorksurface.pointAt(this.grippable.getInnerDepth1Anchor(), this.grippable.getInnerDepth1Direction(), this.grippable.getEntity().getAttributeValueAsFloat("ICD_Parametric_Inner_Depth1")));
            this.innerDepth1Grip.setAnchorLocation(this.grippable.getInnerDepth1Anchor());
        }
        if (attributeGripPoint6 != null) {
            this.innerDepth2Grip.updateGrip(attributeGripPoint6);
            this.innerDepth2Grip.setLocation(ICDParametricWorksurface.pointAt(this.grippable.getInnerDepth2Anchor(), this.grippable.getInnerDepth2Direction(), this.grippable.getEntity().getAttributeValueAsFloat("ICD_Parametric_Inner_Depth2")));
            this.innerDepth2Grip.setAnchorLocation(this.grippable.getInnerDepth2Anchor());
        }
    }
    
    @Override
    public void selectGrips(final boolean b) {
        super.selectGrips(b);
        this.innerDepth1Grip.setSelected(b);
        this.innerDepth2Grip.setSelected(b);
    }
    
    @Override
    public void deselectGrips() {
        super.deselectGrips();
        this.innerDepth1Grip.setSelected(false);
        this.innerDepth2Grip.setSelected(false);
    }
    
    @Override
    public void destroyGrips() {
        super.destroyGrips();
        this.innerDepth1Grip.destroy();
        this.innerDepth2Grip.destroy();
    }
    
    @Override
    public void drawGrips(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        super.drawGrips(n, ice2DContainer, solutionSetting);
        this.innerDepth1Grip.draw2D(n, ice2DContainer, solutionSetting);
        this.innerDepth2Grip.draw2D(n, ice2DContainer, solutionSetting);
    }
}
