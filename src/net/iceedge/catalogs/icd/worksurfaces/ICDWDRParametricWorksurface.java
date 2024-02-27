package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.icecore.basemodule.baseclasses.grips.AttributeGripPoint;
import java.util.SortedSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.Parameter2D;
import java.util.Collection;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;

public class ICDWDRParametricWorksurface extends ICDParametricWorksurface implements ICD1Width2Way1DepthGrippable
{
    LineParameter topEdge;
    LineParameter bottomEdge;
    LineParameter leftEdge;
    LineParameter rightEdge;
    FilletParameter topLeftFillet;
    FilletParameter topRightFillet;
    FilletParameter bottomRightFillet;
    FilletParameter bottomLeftFillet;
    private ICD1Width2Way1DepthGripset gripSet;
    
    public ICDWDRParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.bottomEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.gripSet = new ICD1Width2Way1DepthGripset(this);
        this.createParameters();
    }
    
    public ICDWDRParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.bottomEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.gripSet = new ICD1Width2Way1DepthGripset(this);
        this.createParameters();
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWDRParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWDRParametricWorksurface buildClone(final ICDWDRParametricWorksurface icdwdrParametricWorksurface) {
        super.buildClone(icdwdrParametricWorksurface);
        icdwdrParametricWorksurface.calculateParameters();
        return icdwdrParametricWorksurface;
    }
    
    @Override
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("Top_Left_Snap_Corner").set(0.0f, 0.0f, 0.0f);
        this.getNamedPointLocal("Top_Right_Snap_Corner").set(this.getXDimension(), 0.0f, 0.0f);
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
        final Point3f startPoint = new Point3f();
        final Point3f endPoint = new Point3f(attributeValueAsFloat, 0.0f, 0.0f);
        final Point3f point3f = new Point3f(attributeValueAsFloat, -attributeValueAsFloat2, 0.0f);
        final Point3f point3f2 = new Point3f(0.0f, -attributeValueAsFloat2, 0.0f);
        this.topEdge.setStartPoint(startPoint);
        this.topEdge.setEndPoint(endPoint);
        this.rightEdge = new LineParameter(endPoint, point3f);
        this.bottomEdge = new LineParameter(point3f, point3f2);
        this.leftEdge = new LineParameter(point3f2, startPoint);
        this.topLeftFillet = new FilletParameter(this.leftEdge, this.topEdge, 1.125f, true);
        this.topRightFillet = new FilletParameter(this.topEdge, this.rightEdge, 1.125f, true);
        this.bottomRightFillet = new FilletParameter(this.rightEdge, this.bottomEdge, 1.125f, true);
        this.bottomLeftFillet = new FilletParameter(this.bottomEdge, this.leftEdge, 1.125f, true);
        this.topLeftFillet.setClockwisePath(true);
        this.topRightFillet.setClockwisePath(true);
        this.bottomRightFillet.setClockwisePath(true);
        this.bottomLeftFillet.setClockwisePath(true);
        this.cutoutRefPoint = new Point3f(startPoint);
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, -1.0f, 0.0f);
        final float n = 4.5f;
        final Point3f key = new Point3f(endPoint.x - n, endPoint.y - n, 0.0f);
        final Point3f key2 = new Point3f(startPoint.x + n, startPoint.y - n, 0.0f);
        this.cutoutSnapPointIndexMap.clear();
        this.cutoutSnapPointIndexMap.put(key2, 0);
        this.cutoutSnapPointIndexMap.put(key, 1);
        this.updateSnappedCutouts(key2, 0);
        this.updateSnappedCutouts(key, 1);
        this.cutoutSnapPoints.clear();
        this.addCutoutSnapPoint(key2);
        this.addCutoutSnapPoint(key);
        if (attributeValueAsFloat >= 60.0f) {
            final Point3f key3 = new Point3f(startPoint.x + attributeValueAsFloat / 2.0f - 3.0f, startPoint.y - n, 0.0f);
            this.cutoutSnapPointIndexMap.put(key3, 2);
            this.updateSnappedCutouts(key3, 2);
            this.addCutoutSnapPoint(key3);
        }
        this.allParameters.clear();
        this.plotNodes.clear();
        this.allParameters.addAll(this.getParametersForLine(this.topEdge));
        this.allParameters.add((Parameter2D)this.topRightFillet);
        this.allParameters.add((Parameter2D)this.rightEdge);
        this.allParameters.add((Parameter2D)this.bottomRightFillet);
        this.allParameters.add((Parameter2D)this.bottomEdge);
        this.allParameters.add((Parameter2D)this.bottomLeftFillet);
        this.allParameters.add((Parameter2D)this.leftEdge);
        this.allParameters.add((Parameter2D)this.topLeftFillet);
        this.updateBRep(true, true);
        this.width1Anchor = ICDParametricWorksurface.pointAt(startPoint, new Vector3f(0.0f, -1.0f, 0.0f), attributeValueAsFloat2 / 2.0f);
        this.width2Anchor = ICDParametricWorksurface.pointAt(endPoint, new Vector3f(0.0f, -1.0f, 0.0f), attributeValueAsFloat2 / 2.0f);
        this.depth1Anchor = ICDParametricWorksurface.pointAt(startPoint, new Vector3f(1.0f, 0.0f, 0.0f), attributeValueAsFloat / 2.0f);
        this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.width2Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.wireDipRefPoints.clear();
        this.wireDipRefPoints.add(new Point3f(startPoint));
    }
    
    @Override
    public String getShapeTag() {
        return "WDR";
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
        final Point3f basePoint = new Point3f(attributeValueAsFloat - n, 0.0f, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
    }
    
    @Override
    public void depth1GripChanged(final String s) {
    }
    
    @Override
    public TransformableEntity getEntity() {
        return (TransformableEntity)this;
    }
    
    @Override
    public float getWidthMax() {
        this.getYDimensionFromData();
        if (this.getYDimensionFromData() > 60.0f) {
            return 60.0f;
        }
        return super.getWidthMax();
    }
    
    @Override
    public float getDepthMax() {
        if (this.getXDimensionFromData() > 60.0f) {
            return 60.0f;
        }
        return super.getDepthMax();
    }
}
