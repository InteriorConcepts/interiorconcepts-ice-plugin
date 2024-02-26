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

public class ICDWVVEMParametricWorksurface extends ICDParametricWorksurface implements ICD1Width2Way1DepthGrippable
{
    private LineParameter topEdge;
    private LineParameter leftEdge;
    private LineParameter rightEdge;
    private CircleParameter bottomArc;
    private FilletParameter topLeftFillet;
    private FilletParameter topRightFillet;
    private FilletParameter bottomRightFillet;
    private FilletParameter bottomLeftFillet;
    private ICD1Width2Way1DepthGripset gripSet;
    
    public ICDWVVEMParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.bottomArc = new CircleParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.gripSet = null;
        this.createParameters();
        this.gripSet = new ICD1Width2Way1DepthGripset(this);
    }
    
    public ICDWVVEMParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.bottomArc = new CircleParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.gripSet = null;
        this.createParameters();
        this.gripSet = new ICD1Width2Way1DepthGripset(this);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVVEMParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWVVEMParametricWorksurface buildClone(final ICDWVVEMParametricWorksurface icdwvvemParametricWorksurface) {
        super.buildClone(icdwvvemParametricWorksurface);
        icdwvvemParametricWorksurface.calculateParameters();
        return icdwvvemParametricWorksurface;
    }
    
    @Override
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("Top_Left_Snap_Corner").set(-this.getXDimension() / 2.0f, this.getYDimension() / 2.0f, 0.0f);
        this.getNamedPointLocal("Top_Right_Snap_Corner").set(this.getXDimension() / 2.0f, this.getYDimension() / 2.0f, 0.0f);
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
        final Point3f startPoint = new Point3f(-attributeValueAsFloat / 2.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f endPoint = new Point3f(attributeValueAsFloat / 2.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f point3f = new Point3f(-attributeValueAsFloat / 2.0f, -(attributeValueAsFloat2 / 2.0f - 3.0f), 0.0f);
        final Point3f point3f2 = new Point3f(attributeValueAsFloat / 2.0f, -(attributeValueAsFloat2 / 2.0f - 3.0f), 0.0f);
        this.topEdge.setStartPoint(startPoint);
        this.topEdge.setEndPoint(endPoint);
        this.rightEdge = new LineParameter(endPoint, point3f2);
        this.leftEdge = new LineParameter(point3f, startPoint);
        this.bottomArc = new CircleParameter(point3f, new Point3f(0.0f, -attributeValueAsFloat2 / 2.0f, 0.0f), point3f2);
        this.topLeftFillet = new FilletParameter(this.leftEdge, this.topEdge, 1.125f, true);
        this.topRightFillet = new FilletParameter(this.topEdge, this.rightEdge, 1.125f, true);
        this.bottomRightFillet = new FilletParameter(this.rightEdge, this.bottomArc, 1.125f, point3f2, true, true, true);
        this.bottomLeftFillet = new FilletParameter(this.leftEdge, this.bottomArc, 1.125f, point3f, true, true, true);
        this.bottomArc.setClockwisePath(true);
        this.topLeftFillet.setClockwisePath(true);
        this.topRightFillet.setClockwisePath(true);
        this.bottomRightFillet.setClockwisePath(true);
        this.bottomLeftFillet.setClockwisePath(true);
        this.bottomArc.calculate();
        this.wireDipRefPoints.clear();
        this.wireDipRefPoints.add(new Point3f(startPoint));
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
        this.allParameters.addAll(this.getParametersForLine(this.topEdge));
        this.allParameters.add((Parameter2D)this.topRightFillet);
        this.allParameters.add((Parameter2D)this.rightEdge);
        this.allParameters.add((Parameter2D)this.bottomRightFillet);
        this.allParameters.add((Parameter2D)this.bottomArc);
        this.allParameters.add((Parameter2D)this.bottomLeftFillet);
        this.allParameters.add((Parameter2D)this.leftEdge);
        this.allParameters.add((Parameter2D)this.topLeftFillet);
        this.updateBRep(true, true);
        this.width1Anchor = new Point3f(-attributeValueAsFloat / 2.0f, 0.0f, 0.0f);
        this.width2Anchor = new Point3f(attributeValueAsFloat / 2.0f, 0.0f, 0.0f);
        this.depth1Anchor = new Point3f(0.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.width2Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.cutoutRefPoint = new Point3f(startPoint);
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, -1.0f, 0.0f);
    }
    
    @Override
    public float getLeftDepth() {
        return this.getAttributeValueAsFloat("ICD_Parametric_Depth") - 3.0f;
    }
    
    @Override
    public float getRightDepth() {
        return this.getAttributeValueAsFloat("ICD_Parametric_Depth") - 3.0f;
    }
    
    @Override
    public String getShapeTag() {
        return "WVVEM";
    }
    
    @Override
    public void setupGripPainters() {
        if (this.gripSet == null) {
            this.gripSet = new ICD1Width2Way1DepthGripset(this);
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
        final Point3f basePoint = new Point3f(0.0f, (attributeValueAsFloat - n) / 2.0f, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
    }
    
    @Override
    public TransformableEntity getEntity() {
        return (TransformableEntity)this;
    }
}
