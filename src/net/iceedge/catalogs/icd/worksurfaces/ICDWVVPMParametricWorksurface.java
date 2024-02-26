// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.dirtt.icelib.main.attributes.Attribute;
import java.util.TreeSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.AttributeGripPoint;
import java.util.SortedSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.GripListener;
import net.dirtt.icelib.main.TransformableEntity;
import net.iceedge.icecore.basemodule.baseclasses.grips.RelativeAttributeGrip;
import javax.vecmath.Vector3f;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.Parameter2D;
import java.util.Collection;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.CircleParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;

public class ICDWVVPMParametricWorksurface extends ICDParametricWorksurface
{
    private LineParameter topEdge;
    private LineParameter rightEdge;
    private LineParameter leftEdge;
    private CircleParameter bottomCircle;
    private CircleParameter leftArc;
    private CircleParameter rightArc;
    private FilletParameter topLeftFillet;
    private FilletParameter topRightFillet;
    private FilletParameter rightReturnFillet;
    private FilletParameter leftReturnFillet;
    protected BasicAttributeGrip width1Grip;
    protected BasicAttributeGrip width2Grip;
    protected BasicAttributeGrip depthGrip;
    protected BasicAttributeGrip returnDepthGrip;
    
    public ICDWVVPMParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.bottomCircle = new CircleParameter();
        this.leftArc = new CircleParameter();
        this.rightArc = new CircleParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.rightReturnFillet = new FilletParameter();
        this.leftReturnFillet = new FilletParameter();
        this.createParameters();
    }
    
    public ICDWVVPMParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.bottomCircle = new CircleParameter();
        this.leftArc = new CircleParameter();
        this.rightArc = new CircleParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.rightReturnFillet = new FilletParameter();
        this.leftReturnFillet = new FilletParameter();
        this.createParameters();
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVVPMParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWVVPMParametricWorksurface buildClone(final ICDWVVPMParametricWorksurface icdwvvpmParametricWorksurface) {
        super.buildClone(icdwvvpmParametricWorksurface);
        icdwvvpmParametricWorksurface.calculateParameters();
        return icdwvvpmParametricWorksurface;
    }
    
    private void createParameters() {
        this.lineParams.add(this.topEdge);
    }
    
    @Override
    protected void calculateParameters() {
        this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor");
        this.shape.clear();
        this.validateDimensionAttributes();
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth");
        final float attributeValueAsFloat3 = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        final Point3f startPoint = new Point3f(-attributeValueAsFloat / 2.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f endPoint = new Point3f(attributeValueAsFloat / 2.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f point3f = new Point3f(attributeValueAsFloat / 2.0f, attributeValueAsFloat2 / 2.0f - attributeValueAsFloat3, 0.0f);
        final Point3f point3f2 = new Point3f(-attributeValueAsFloat / 2.0f, attributeValueAsFloat2 / 2.0f - attributeValueAsFloat3, 0.0f);
        this.topEdge.setStartPoint(startPoint);
        this.topEdge.setEndPoint(endPoint);
        this.rightEdge = new LineParameter(endPoint, point3f);
        this.leftEdge = new LineParameter(point3f2, startPoint);
        this.bottomCircle = new CircleParameter(new Point3f(0.0f, -(attributeValueAsFloat2 / 2.0f - attributeValueAsFloat / 2.0f), 0.0f), attributeValueAsFloat / 2.0f);
        this.rightArc = new CircleParameter(this.bottomCircle, point3f, 3.0f, false);
        this.leftArc = new CircleParameter(new Point3f(this.rightArc.getCenter()), this.rightArc.getRadius());
        this.leftArc.getCenter().x = -this.leftArc.getCenter().x;
        this.topLeftFillet = new FilletParameter(this.leftEdge, this.topEdge, 1.125f, true);
        this.topRightFillet = new FilletParameter(this.topEdge, this.rightEdge, 1.125f, true);
        this.leftReturnFillet = new FilletParameter(this.leftEdge, this.leftArc, 14.0f, point3f2, true, false, false);
        this.rightReturnFillet = new FilletParameter(this.rightEdge, this.rightArc, 14.0f, point3f, true, false, false);
        this.bottomCircle.setClockwisePath(true);
        this.bottomCircle.setShorterPath(false);
        this.rightArc.setClockwisePath(false);
        this.leftArc.setClockwisePath(false);
        this.topLeftFillet.setClockwisePath(true);
        this.topRightFillet.setClockwisePath(true);
        this.rightReturnFillet.setClockwisePath(true);
        this.leftReturnFillet.setClockwisePath(true);
        this.wireDipRefPoints.clear();
        this.wireDipRefPoints.add(new Point3f(startPoint));
        final Point3f key = new Point3f(0.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        this.snapPointIndexMap.clear();
        this.wireDipSnapPoints.clear();
        this.snapPointIndexMap.put(key, 0);
        this.updateSnappedWireDips(key, 0);
        this.addWireDipSnapPoint(key);
        this.updateDipLocations();
        this.leftEdge.getEndPoint();
        this.topEdge.getStartPoint();
        this.topEdge.getEndPoint();
        this.rightEdge.getStartPoint();
        final Point3f lineFilletTangentPoint = this.rightReturnFillet.getLineFilletTangentPoint(this.rightEdge);
        this.rightReturnFillet.getCircleFilletTangentPoint(this.rightArc);
        this.bottomCircle.touch(this.rightArc);
        this.bottomCircle.touch(this.leftArc);
        this.leftReturnFillet.getCircleFilletTangentPoint(this.leftArc);
        final Point3f lineFilletTangentPoint2 = this.leftReturnFillet.getLineFilletTangentPoint(this.leftEdge);
        final Point3f point3f3 = new Point3f(0.0f, -attributeValueAsFloat2 / 2.0f, 0.0f);
        this.rightEdge.setEndPoint(lineFilletTangentPoint);
        this.leftEdge.setStartPoint(lineFilletTangentPoint2);
        this.plotNodes.clear();
        this.allParameters.clear();
        this.allParameters.addAll(this.getParametersForLine(this.topEdge));
        this.allParameters.add((Parameter2D)this.topRightFillet);
        this.allParameters.add((Parameter2D)this.rightEdge);
        this.allParameters.add((Parameter2D)this.rightReturnFillet);
        this.allParameters.add((Parameter2D)this.rightArc);
        this.allParameters.add((Parameter2D)this.bottomCircle);
        this.allParameters.add((Parameter2D)this.leftArc);
        this.allParameters.add((Parameter2D)this.leftReturnFillet);
        this.allParameters.add((Parameter2D)this.leftEdge);
        this.allParameters.add((Parameter2D)this.topLeftFillet);
        this.updateBRep(true, true);
        this.width1Anchor = new Point3f(-attributeValueAsFloat / 2.0f, 0.0f, 0.0f);
        this.width2Anchor = new Point3f(attributeValueAsFloat / 2.0f, 0.0f, 0.0f);
        this.depthAnchor = new Point3f(0.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        this.depth1Anchor = this.depthAnchor;
        this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.width2Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.depthDirection = new Vector3f(0.0f, -1.0f, 0.0f);
        this.depth1Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.cutoutRefPoint = new Point3f(startPoint);
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, -1.0f, 0.0f);
    }
    
    @Override
    public float getDepthMin() {
        final float minimumValueFromAttribute = this.getMinimumValueFromAttribute("ICD_Parametric_Depth_Min");
        this.clampAttributeValue("ICD_Parametric_Width", this.getWidthMin(), this.getWidthMax());
        this.clampAttributeValue("ICD_Parametric_Depth1", this.getDepth1Min(), this.getDepth1Max());
        return Math.max(minimumValueFromAttribute, (float)Math.ceil(this.getAttributeValueAsFloat("ICD_Parametric_Depth1") + (minimumValueFromAttribute - this.getDepth1Max()) + (this.getAttributeValueAsFloat("ICD_Parametric_Width") - this.getWidthMin()) * 0.65f));
    }
    
    @Override
    public float getLeftDepth() {
        return this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
    }
    
    @Override
    public float getRightDepth() {
        return this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
    }
    
    @Override
    public String getShapeTag() {
        return "WVVPM";
    }
    
    @Override
    public void setupGripPainters() {
        (this.width1Grip = (BasicAttributeGrip)new RelativeAttributeGrip((TransformableEntity)this, 0)).setLinkID((byte)100);
        this.width1Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICDWVVPMParametricWorksurface.this.width1GripChanged(s2);
                return true;
            }
        });
        (this.width2Grip = (BasicAttributeGrip)new RelativeAttributeGrip((TransformableEntity)this, 0)).setLinkID((byte)101);
        this.width2Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICDWVVPMParametricWorksurface.this.width2GripChanged(s2);
                return true;
            }
        });
        (this.depthGrip = (BasicAttributeGrip)new RelativeAttributeGrip((TransformableEntity)this, 2)).setLinkID((byte)102);
        this.depthGrip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICDWVVPMParametricWorksurface.this.depthGripChanged(s2);
                return true;
            }
        });
        (this.returnDepthGrip = (BasicAttributeGrip)new RelativeAttributeGrip((TransformableEntity)this, 2)).setLinkID((byte)103);
        this.returnDepthGrip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICDWVVPMParametricWorksurface.this.returnDepthGripChanged(s2);
                return true;
            }
        });
    }
    
    @Override
    public SortedSet<AttributeGripPoint> getAttributeMap(final BasicAttributeGrip basicAttributeGrip) {
        final TreeSet<AttributeGripPoint> set = new TreeSet<AttributeGripPoint>();
        if (basicAttributeGrip.equals(this.width1Grip)) {
            final Attribute attributeObject = this.getAttributeObject("ICD_Parametric_Width");
            if (attributeObject != null) {
                final float widthMin = this.getWidthMin();
                for (float widthMax = this.getWidthMax(), f = widthMin; f <= widthMax; f += ICD2Widths2DepthsGripSet.ICD_GRIP_STEP) {
                    final String string = "" + f;
                    final Attribute attribute = (Attribute)attributeObject.clone();
                    attribute.setCurrentValueAsString(string);
                    set.add(new AttributeGripPoint(ICDParametricWorksurface.pointAt(this.getWidth1Anchor(), this.getWidth1Direction(), f), (int)(f * ICD2Widths2DepthsGripSet.ICD_GRIP_WEIGHT_MULTIPLIER), new Attribute[] { attribute }));
                }
            }
        }
        if (basicAttributeGrip.equals(this.width2Grip)) {
            final Attribute attributeObject2 = this.getAttributeObject("ICD_Parametric_Width");
            if (attributeObject2 != null) {
                final float z = this.getGeometricCenterPointWorld().z;
                final float widthMin2 = this.getWidthMin();
                for (float widthMax2 = this.getWidthMax(), f2 = widthMin2; f2 <= widthMax2; f2 += ICD2Widths2DepthsGripSet.ICD_GRIP_STEP) {
                    final String string2 = "" + f2;
                    final Attribute attribute2 = (Attribute)attributeObject2.clone();
                    attribute2.setCurrentValueAsString(string2);
                    set.add(new AttributeGripPoint(ICDParametricWorksurface.pointAt(this.getWidth2Anchor(), this.getWidth2Direction(), f2), (int)(f2 * ICD2Widths2DepthsGripSet.ICD_GRIP_WEIGHT_MULTIPLIER), new Attribute[] { attribute2 }));
                }
            }
        }
        if (basicAttributeGrip.equals(this.depthGrip)) {
            final Attribute attributeObject3 = this.getAttributeObject("ICD_Parametric_Depth");
            if (attributeObject3 != null) {
                final float z2 = this.getGeometricCenterPointWorld().z;
                final float depthMin = this.getDepthMin();
                for (float depthMax = this.getDepthMax(), f3 = depthMin; f3 <= depthMax; f3 += ICD2Widths2DepthsGripSet.ICD_GRIP_STEP) {
                    final String string3 = "" + f3;
                    final Attribute attribute3 = (Attribute)attributeObject3.clone();
                    attribute3.setCurrentValueAsString(string3);
                    set.add(new AttributeGripPoint(ICDParametricWorksurface.pointAt(this.getDepthAnchor(), this.getDepthDirection(), f3), (int)(f3 * ICD2Widths2DepthsGripSet.ICD_GRIP_WEIGHT_MULTIPLIER), new Attribute[] { attribute3 }));
                }
            }
        }
        if (basicAttributeGrip.equals(this.returnDepthGrip)) {
            final Attribute attributeObject4 = this.getAttributeObject("ICD_Parametric_Depth1");
            if (attributeObject4 != null) {
                final float z3 = this.getGeometricCenterPointWorld().z;
                final float depth1Min = this.getDepth1Min();
                for (float depth1Max = this.getDepth1Max(), f4 = depth1Min; f4 <= depth1Max; f4 += ICD2Widths2DepthsGripSet.ICD_GRIP_STEP) {
                    final String string4 = "" + f4;
                    final Attribute attribute4 = (Attribute)attributeObject4.clone();
                    attribute4.setCurrentValueAsString(string4);
                    set.add(new AttributeGripPoint(ICDParametricWorksurface.pointAt(this.getDepth1Anchor(), this.getDepth1Direction(), f4), (int)(f4 * ICD2Widths2DepthsGripSet.ICD_GRIP_WEIGHT_MULTIPLIER), new Attribute[] { attribute4 }));
                }
            }
        }
        return set;
    }
    
    @Override
    public void updateGrips(final BasicAttributeGrip basicAttributeGrip) {
        if (basicAttributeGrip != null) {
            this.updateGripPositions(new AttributeGripPoint(new Attribute[] { this.getAttributeObject("ICD_Parametric_Width") }), new AttributeGripPoint(new Attribute[] { this.getAttributeObject("ICD_Parametric_Width") }), new AttributeGripPoint(new Attribute[] { this.getAttributeObject("ICD_Parametric_Depth") }), new AttributeGripPoint(new Attribute[] { this.getAttributeObject("ICD_Parametric_Depth1") }));
        }
    }
    
    private void updateGripPositions(final AttributeGripPoint attributeGripPoint, final AttributeGripPoint attributeGripPoint2, final AttributeGripPoint attributeGripPoint3, final AttributeGripPoint attributeGripPoint4) {
        if (attributeGripPoint != null) {
            this.width1Grip.updateGrip(attributeGripPoint);
            this.width1Grip.setLocation(ICDParametricWorksurface.pointAt(this.getWidth1Anchor(), this.getWidth1Direction(), this.getAttributeValueAsFloat("ICD_Parametric_Width")));
            this.width1Grip.setAnchorLocation(this.getWidth1Anchor());
        }
        if (attributeGripPoint2 != null) {
            this.width2Grip.updateGrip(attributeGripPoint2);
            this.width2Grip.setLocation(ICDParametricWorksurface.pointAt(this.getWidth2Anchor(), this.getWidth2Direction(), this.getAttributeValueAsFloat("ICD_Parametric_Width")));
            this.width2Grip.setAnchorLocation(this.getWidth2Anchor());
        }
        if (attributeGripPoint3 != null) {
            this.depthGrip.updateGrip(attributeGripPoint3);
            this.depthGrip.setLocation(ICDParametricWorksurface.pointAt(this.getDepthAnchor(), this.getDepthDirection(), this.getAttributeValueAsFloat("ICD_Parametric_Depth")));
            this.depthGrip.setAnchorLocation(this.getDepthAnchor());
        }
        if (attributeGripPoint4 != null) {
            this.returnDepthGrip.updateGrip(attributeGripPoint4);
            this.returnDepthGrip.setLocation(ICDParametricWorksurface.pointAt(this.getDepth1Anchor(), this.getDepth1Direction(), this.getAttributeValueAsFloat("ICD_Parametric_Depth1")));
            this.returnDepthGrip.setAnchorLocation(this.getDepth1Anchor());
        }
    }
    
    @Override
    protected void selectGrips(final boolean b) {
        this.width1Grip.setSelected(b);
        this.width2Grip.setSelected(b);
        this.depthGrip.setSelected(b);
        this.returnDepthGrip.setSelected(b);
    }
    
    @Override
    protected void deselectGrips() {
        this.width1Grip.setSelected(false);
        this.width2Grip.setSelected(false);
        this.depthGrip.setSelected(false);
        this.returnDepthGrip.setSelected(false);
    }
    
    @Override
    protected void destroyGrips() {
        this.width1Grip.destroy();
        this.width2Grip.destroy();
        this.depthGrip.destroy();
        this.returnDepthGrip.destroy();
    }
    
    @Override
    protected void drawGrips(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        this.width1Grip.draw2D(n, ice2DContainer, solutionSetting);
        this.width2Grip.draw2D(n, ice2DContainer, solutionSetting);
        this.depthGrip.draw2D(n, ice2DContainer, solutionSetting);
        this.returnDepthGrip.draw2D(n, ice2DContainer, solutionSetting);
    }
    
    public void depthGripChanged(final String s) {
        Float n = 0.0f;
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth");
        try {
            n = Float.parseFloat(s);
            n = this.getValidDepth(n);
        }
        catch (NumberFormatException ex) {
            System.err.println(" ERROR parsing attribute value");
            ex.printStackTrace();
        }
        final Point3f basePoint = new Point3f(0.0f, (attributeValueAsFloat - n) / 2.0f, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
    }
    
    public void returnDepthGripChanged(final String s) {
        //0.0f;
        this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        try {
            this.getValidDepth1(Float.parseFloat(s));
        }
        catch (NumberFormatException ex) {
            System.err.println(" ERROR parsing attribute value");
            ex.printStackTrace();
        }
    }
    
    public void width1GripChanged(final String s) {
        Float n = 0.0f;
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width");
        try {
            n = Float.parseFloat(s);
            n = this.getValidWidth(n);
        }
        catch (NumberFormatException ex) {
            System.err.println(" ERROR parsing attribute value");
            ex.printStackTrace();
        }
        final Point3f basePoint = new Point3f(-(attributeValueAsFloat - n) / 2.0f, 0.0f, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
    }
    
    public void width2GripChanged(final String s) {
        Float n = 0.0f;
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width");
        try {
            n = Float.parseFloat(s);
            n = this.getValidWidth(n);
        }
        catch (NumberFormatException ex) {
            System.err.println(" ERROR parsing attribute value");
            ex.printStackTrace();
        }
        final Point3f basePoint = new Point3f((attributeValueAsFloat - n) / 2.0f, 0.0f, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
    }
    
    @Override
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("D_Column").set(0.0f, -this.getYDimension() / 2.0f + this.getYDimension() / 6.0f, 0.0f);
    }
    
    @Override
    protected float getCadOutputRotation() {
        return super.getCadOutputRotation() + 1.5707964f;
    }
}
