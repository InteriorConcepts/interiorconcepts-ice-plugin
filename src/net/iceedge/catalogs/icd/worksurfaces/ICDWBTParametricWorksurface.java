// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.icecore.basemodule.baseclasses.grips.AttributeGripPoint;
import java.util.SortedSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;
import javax.vecmath.Vector3f;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.Parameter2D;
import java.util.Collection;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.CircleParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;

public class ICDWBTParametricWorksurface extends ICDParametricWorksurface implements ICD2Widths2DepthsGrippable
{
    LineParameter leftEdge;
    LineParameter rightEdge;
    CircleParameter topHemiCircle;
    CircleParameter bottomHemiCircle;
    FilletParameter topLeftFillet;
    FilletParameter topRightFillet;
    FilletParameter bottomLeftFillet;
    FilletParameter bottomRightFillet;
    private ICD1Width2Way1Depth2WayGripSet gripSet;
    
    public ICDWBTParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.leftEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.topHemiCircle = new CircleParameter(new Point3f(0.0f, 0.0f, 0.0f), 18.0f);
        this.bottomHemiCircle = new CircleParameter(new Point3f(0.0f, 0.0f, 0.0f), 18.0f);
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.gripSet = null;
        this.createParameters();
        this.gripSet = new ICD1Width2Way1Depth2WayGripSet(this);
    }
    
    public ICDWBTParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.leftEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.topHemiCircle = new CircleParameter(new Point3f(0.0f, 0.0f, 0.0f), 18.0f);
        this.bottomHemiCircle = new CircleParameter(new Point3f(0.0f, 0.0f, 0.0f), 18.0f);
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.gripSet = null;
        this.createParameters();
        this.gripSet = new ICD1Width2Way1Depth2WayGripSet(this);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWBTParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWBTParametricWorksurface buildClone(final ICDWBTParametricWorksurface icdwbtParametricWorksurface) {
        super.buildClone(icdwbtParametricWorksurface);
        icdwbtParametricWorksurface.calculateParameters();
        return icdwbtParametricWorksurface;
    }
    
    private void createParameters() {
        this.lineParams.add(this.leftEdge);
    }
    
    @Override
    protected void calculateParameters() {
        this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor");
        this.shape.clear();
        this.validateDimensionAttributes();
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth");
        final float n = 6.0f;
        final float n2 = attributeValueAsFloat * attributeValueAsFloat / (8.0f * n) + n / 2.0f;
        final Point3f point3f = new Point3f(-attributeValueAsFloat / 2.0f, attributeValueAsFloat2 / 2.0f - n, 0.0f);
        final Point3f point3f2 = new Point3f(attributeValueAsFloat / 2.0f, attributeValueAsFloat2 / 2.0f - n, 0.0f);
        final Point3f point3f3 = new Point3f(-attributeValueAsFloat / 2.0f, -attributeValueAsFloat2 / 2.0f + n, 0.0f);
        final Point3f point3f4 = new Point3f(attributeValueAsFloat / 2.0f, -attributeValueAsFloat2 / 2.0f + n, 0.0f);
        this.leftEdge = new LineParameter(point3f3, point3f);
        this.rightEdge = new LineParameter(point3f2, point3f4);
        this.topHemiCircle.setRadius(n2);
        this.topHemiCircle.setCenter(new Point3f(0.0f, attributeValueAsFloat2 / 2.0f - n2, 0.0f));
        this.topHemiCircle.setClockwisePath(true);
        this.bottomHemiCircle.setRadius(n2);
        this.bottomHemiCircle.setCenter(new Point3f(0.0f, n2 - attributeValueAsFloat2 / 2.0f, 0.0f));
        this.bottomHemiCircle.setClockwisePath(true);
        this.topLeftFillet = new FilletParameter(this.leftEdge, this.topHemiCircle, 1.125f, new Point3f(point3f), true, true, true);
        this.topRightFillet = new FilletParameter(this.rightEdge, this.topHemiCircle, 1.125f, new Point3f(point3f2), true, true, true);
        this.bottomRightFillet = new FilletParameter(this.rightEdge, this.bottomHemiCircle, 1.125f, new Point3f(point3f4), true, true, true);
        this.bottomLeftFillet = new FilletParameter(this.leftEdge, this.bottomHemiCircle, 1.125f, new Point3f(point3f3), true, true, true);
        this.topLeftFillet.setClockwisePath(true);
        this.topRightFillet.setClockwisePath(true);
        this.bottomLeftFillet.setClockwisePath(true);
        this.bottomRightFillet.setClockwisePath(true);
        this.wireDipRefPoints.clear();
        this.wireDipRefPoints.add(new Point3f(point3f));
        final Point3f key = new Point3f(0.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f key2 = new Point3f(-attributeValueAsFloat / 6.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f key3 = new Point3f(attributeValueAsFloat / 6.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        this.snapPointIndexMap.clear();
        this.wireDipSnapPoints.clear();
        if (attributeValueAsFloat < 59.0f) {
            this.snapPointIndexMap.put(key, 0);
            this.updateSnappedWireDips(key, 0);
            this.addWireDipSnapPoint(key);
        }
        else {
            this.snapPointIndexMap.put(key2, 0);
            this.snapPointIndexMap.put(key3, 1);
            this.updateSnappedWireDips(key2, 0);
            this.updateSnappedWireDips(key3, 1);
            this.addWireDipSnapPoint(key2);
            this.addWireDipSnapPoint(key3);
        }
        this.updateDipLocations();
        this.allParameters.clear();
        this.plotNodes.clear();
        this.allParameters.addAll(this.getParametersForLine(this.leftEdge));
        this.allParameters.add((Parameter2D)this.topLeftFillet);
        this.allParameters.add((Parameter2D)this.topHemiCircle);
        this.allParameters.add((Parameter2D)this.topRightFillet);
        this.allParameters.add((Parameter2D)this.rightEdge);
        this.allParameters.add((Parameter2D)this.bottomRightFillet);
        this.allParameters.add((Parameter2D)this.bottomHemiCircle);
        this.allParameters.add((Parameter2D)this.bottomLeftFillet);
        this.updateBRep(true, true);
        this.width1Anchor = new Point3f(-attributeValueAsFloat / 2.0f, 0.0f, 0.0f);
        this.width2Anchor = new Point3f(attributeValueAsFloat / 2.0f, 0.0f, 0.0f);
        this.depth1Anchor = new Point3f(0.0f, -attributeValueAsFloat2 / 2.0f, 0.0f);
        this.depth2Anchor = new Point3f(0.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.width2Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(0.0f, 1.0f, 0.0f);
        this.depth2Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.cutoutRefPoint = new Point3f(point3f);
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, -1.0f, 0.0f);
    }
    
    @Override
    public String getShapeTag() {
        return "WBT";
    }
    
    @Override
    public float getLeftDepth() {
        return this.getAttributeValueAsFloat("ICD_Parametric_Depth");
    }
    
    @Override
    public float getRightDepth() {
        return this.getAttributeValueAsFloat("ICD_Parametric_Depth");
    }
    
    @Override
    public void setupGripPainters() {
        if (this.gripSet == null) {
            this.gripSet = new ICD1Width2Way1Depth2WayGripSet(this);
        }
        this.gripSet.setupGripPainters();
    }
    
    @Override
    public SortedSet<AttributeGripPoint> getAttributeMap(final BasicAttributeGrip basicAttributeGrip) {
        return this.gripSet.getAttributeMap(basicAttributeGrip);
    }
    
    @Override
    public void updateGrips(final BasicAttributeGrip basicAttributeGrip) {
        this.gripSet.updateGrips(basicAttributeGrip);
    }
    
    @Override
    protected void selectGrips(final boolean b) {
        this.gripSet.selectGrips(b);
        super.selectGrips(b);
    }
    
    @Override
    protected void deselectGrips() {
        this.gripSet.deselectGrips();
        super.deselectGrips();
    }
    
    @Override
    protected void destroyGrips() {
        this.gripSet.destroyGrips();
        super.destroyGrips();
    }
    
    @Override
    protected void drawGrips(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        super.drawGrips(n, ice2DContainer, solutionSetting);
        this.gripSet.drawGrips(n, ice2DContainer, solutionSetting);
    }
    
    @Override
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
    
    @Override
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
    public void depth1GripChanged(final String s) {
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
        final Point3f basePoint = new Point3f(0.0f, -(attributeValueAsFloat - n) / 2.0f, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
    }
    
    @Override
    public void depth2GripChanged(final String s) {
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
    
    @Override
    public TransformableEntity getEntity() {
        return (TransformableEntity)this;
    }
    
    @Override
    public float getWidthMin() {
        this.getAttributeValueAsFloat("ICD_Parametric_Width_Min");
        if (this.getAttributeValueAsFloat("ICD_Parametric_Width") <= this.getAttributeValueAsFloat("ICD_Parametric_Depth")) {}
        return super.getWidthMin();
    }
    
    @Override
    public float getDepthMax() {
        this.getAttributeValueAsFloat("ICD_Parametric_Depth_Max");
        if (this.getAttributeValueAsFloat("ICD_Parametric_Depth") >= this.getAttributeValueAsFloat("ICD_Parametric_Width")) {}
        return super.getDepthMax();
    }
    
    @Override
    protected Point3f getCadOutputInsertionPoint(final String s) {
        final Point3f point3f2;
        final Point3f point3f = point3f2 = (Point3f)this.getGeometricCenterPointLocal().clone();
        point3f2.x -= s.length() * 2.0f / 2.0f;
        final Point3f point3f3 = point3f;
        point3f3.y += this.getYDimension() / 2.0f - 4.0f;
        return this.convertPointToWorldSpace(point3f);
    }
}
