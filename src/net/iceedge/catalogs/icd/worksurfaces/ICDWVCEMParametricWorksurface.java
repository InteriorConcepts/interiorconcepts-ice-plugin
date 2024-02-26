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
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.CircleParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;

public class ICDWVCEMParametricWorksurface extends ICDParametricWorksurface implements ICD1Width2Way1DepthGrippable
{
    private LineParameter backEdge;
    private LineParameter rightEdge;
    private LineParameter leftEdge;
    private FilletParameter backLeftFillet;
    private FilletParameter backRightFillet;
    private FilletParameter frontLeftFillet;
    private FilletParameter frontRightFillet;
    private CircleParameter frontArc;
    private ICD1Width2Way1DepthGripset gripSet;
    
    public ICDWVCEMParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.backEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.backLeftFillet = new FilletParameter();
        this.backRightFillet = new FilletParameter();
        this.frontLeftFillet = new FilletParameter();
        this.frontRightFillet = new FilletParameter();
        this.frontArc = new CircleParameter();
        this.gripSet = new ICD1Width2Way1DepthGripset(this);
        this.createParameters();
        this.gripSet = new ICD1Width2Way1DepthGripset(this);
    }
    
    public ICDWVCEMParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.backEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.backLeftFillet = new FilletParameter();
        this.backRightFillet = new FilletParameter();
        this.frontLeftFillet = new FilletParameter();
        this.frontRightFillet = new FilletParameter();
        this.frontArc = new CircleParameter();
        this.gripSet = new ICD1Width2Way1DepthGripset(this);
        this.createParameters();
        this.gripSet = new ICD1Width2Way1DepthGripset(this);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVCEMParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWVCEMParametricWorksurface buildClone(final ICDWVCEMParametricWorksurface icdwvcemParametricWorksurface) {
        super.buildClone(icdwvcemParametricWorksurface);
        icdwvcemParametricWorksurface.calculateParameters();
        return icdwvcemParametricWorksurface;
    }
    
    private void createParameters() {
        this.lineParams.add(this.backEdge);
    }
    
    @Override
    protected void calculateParameters() {
        this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor");
        this.shape.clear();
        this.validateDimensionAttributes();
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth");
        final Point3f point3f = new Point3f(-attributeValueAsFloat / 2.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f point3f2 = new Point3f(attributeValueAsFloat / 2.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f startPoint = new Point3f(-attributeValueAsFloat / 2.0f, -attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f endPoint = new Point3f(attributeValueAsFloat / 2.0f, -attributeValueAsFloat2 / 2.0f, 0.0f);
        this.backEdge.setStartPoint(point3f);
        this.backEdge.setEndPoint(point3f2);
        this.leftEdge.setStartPoint(startPoint);
        this.leftEdge.setEndPoint(point3f);
        this.rightEdge.setStartPoint(point3f2);
        this.rightEdge.setEndPoint(endPoint);
        this.frontArc = new CircleParameter(startPoint, new Point3f(0.0f, -(attributeValueAsFloat2 / 2.0f - 3.0f), 0.0f), endPoint);
        this.backLeftFillet = new FilletParameter(this.leftEdge, this.backEdge, 1.125f, true);
        this.backRightFillet = new FilletParameter(this.backEdge, this.rightEdge, 1.125f, true);
        this.frontRightFillet = new FilletParameter(this.rightEdge, this.frontArc, 1.125f, endPoint, true, false, true);
        this.frontLeftFillet = new FilletParameter(this.leftEdge, this.frontArc, 1.125f, startPoint, true, false, true);
        this.frontArc.calculate();
        this.frontArc.setClockwisePath(false);
        this.backLeftFillet.setClockwisePath(true);
        this.backRightFillet.setClockwisePath(true);
        this.frontRightFillet.setClockwisePath(true);
        this.frontLeftFillet.setClockwisePath(true);
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
        this.allParameters.addAll(this.getParametersForLine(this.backEdge));
        this.allParameters.add((Parameter2D)this.backRightFillet);
        this.allParameters.add((Parameter2D)this.rightEdge);
        this.allParameters.add((Parameter2D)this.frontRightFillet);
        this.allParameters.add((Parameter2D)this.frontArc);
        this.allParameters.add((Parameter2D)this.frontLeftFillet);
        this.allParameters.add((Parameter2D)this.leftEdge);
        this.allParameters.add((Parameter2D)this.backLeftFillet);
        this.updateBRep(true, true);
        this.width1Anchor = ICDParametricWorksurface.pointAt(point3f, new Vector3f(0.0f, -1.0f, 0.0f), point3f.distance(startPoint) / 2.0f);
        this.width2Anchor = ICDParametricWorksurface.pointAt(point3f2, new Vector3f(0.0f, -1.0f, 0.0f), point3f.distance(startPoint) / 2.0f);
        this.depth1Anchor = ICDParametricWorksurface.pointAt(point3f, new Vector3f(1.0f, 0.0f, 0.0f), point3f.distance(point3f2) / 2.0f);
        this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.width2Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.cutoutRefPoint = new Point3f(point3f);
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, -1.0f, 0.0f);
    }
    
    @Override
    public String getShapeTag() {
        return "WVCEM";
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
    public TransformableEntity getEntity() {
        return (TransformableEntity)this;
    }
    
    @Override
    public void depth1GripChanged(final String s) {
        Float n = 0.0f;
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth");
        try {
            n = Float.parseFloat(s);
            n = Math.min(n, this.getDepthMax());
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
    public void width1GripChanged(final String s) {
        Float n = 0.0f;
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width");
        try {
            n = Float.parseFloat(s);
            n = Math.min(n, this.getWidthMax());
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
            n = Math.min(n, this.getWidthMax());
        }
        catch (NumberFormatException ex) {
            System.err.println(" ERROR parsing attribute value");
            ex.printStackTrace();
        }
        final Point3f basePoint = new Point3f((attributeValueAsFloat - n) / 2.0f, 0.0f, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
    }
}
