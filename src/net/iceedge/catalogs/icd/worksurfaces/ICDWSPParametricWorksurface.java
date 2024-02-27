package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.icecore.basemodule.baseclasses.grips.AttributeGripPoint;
import java.util.SortedSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;
import java.util.Collection;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.Parameter2D;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.CircleParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;

public class ICDWSPParametricWorksurface extends ICDParametricWorksurface implements ICD1Width2Way1DepthGrippable
{
    LineParameter topEdge;
    LineParameter leftEdge;
    LineParameter rightEdge;
    FilletParameter topLeftFillet;
    FilletParameter topRightFillet;
    private CircleParameter bottomHemiCircle;
    private ICD1Width2Way1DepthGripset gripSet;
    
    public ICDWSPParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomHemiCircle = new CircleParameter(new Point3f(0.0f, 0.0f, 0.0f), 18.0f);
        this.gripSet = new ICD1Width2Way1DepthGripset(this);
        this.createParameters();
    }
    
    public ICDWSPParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomHemiCircle = new CircleParameter(new Point3f(0.0f, 0.0f, 0.0f), 18.0f);
        this.gripSet = new ICD1Width2Way1DepthGripset(this);
        this.createParameters();
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWSPParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWSPParametricWorksurface buildClone(final ICDWSPParametricWorksurface icdwspParametricWorksurface) {
        super.buildClone(icdwspParametricWorksurface);
        icdwspParametricWorksurface.calculateParameters();
        return icdwspParametricWorksurface;
    }
    
    @Override
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("Top_Left_Snap_Corner").set(0.0f, 0.0f, 0.0f);
        this.getNamedPointLocal("Top_Right_Snap_Corner").set(this.getXDimension(), 0.0f, 0.0f);
        this.getNamedPointLocal("D_Column").set(this.getXDimension() / 2.0f, -0.8333333f * this.getYDimension(), 0.0f);
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
        final float radius = attributeValueAsFloat / 2.0f;
        final Point3f startPoint = new Point3f();
        final Point3f endPoint = new Point3f(attributeValueAsFloat, 0.0f, 0.0f);
        final Point3f point3f = new Point3f(attributeValueAsFloat, radius - attributeValueAsFloat2, 0.0f);
        final Point3f point3f2 = new Point3f(0.0f, radius - attributeValueAsFloat2, 0.0f);
        this.topEdge.setStartPoint(startPoint);
        this.topEdge.setEndPoint(endPoint);
        this.rightEdge = new LineParameter(endPoint, point3f);
        this.leftEdge = new LineParameter(point3f2, startPoint);
        this.topLeftFillet = new FilletParameter(this.leftEdge, this.topEdge, 1.125f, true);
        this.topRightFillet = new FilletParameter(this.topEdge, this.rightEdge, 1.125f, true);
        this.bottomHemiCircle.setRadius(radius);
        this.bottomHemiCircle.setCenter(new Point3f(radius, radius - attributeValueAsFloat2, 0.0f));
        this.bottomHemiCircle.setClockwisePath(true);
        this.topLeftFillet.setClockwisePath(true);
        this.topRightFillet.setClockwisePath(true);
        this.cutoutRefPoint = new Point3f(startPoint);
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, -1.0f, 0.0f);
        final float n = 2.5f;
        this.cutoutSnapPointIndexMap.clear();
        this.cutoutSnapPoints.clear();
        final Point3f key = new Point3f(startPoint.x + attributeValueAsFloat / 2.0f, startPoint.y - n, 0.0f);
        this.cutoutSnapPointIndexMap.put(key, 0);
        this.updateSnappedCutouts(key, 0);
        this.addCutoutSnapPoint(key);
        this.allParameters.clear();
        this.plotNodes.clear();
        this.allParameters.add((Parameter2D)this.leftEdge);
        this.allParameters.add((Parameter2D)this.topLeftFillet);
        this.allParameters.addAll(this.getParametersForLine(this.topEdge));
        this.allParameters.add((Parameter2D)this.topRightFillet);
        this.allParameters.add((Parameter2D)this.rightEdge);
        this.allParameters.add((Parameter2D)this.bottomHemiCircle);
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
        return "WSP";
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
    public float getDepthMin() {
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth_Min");
        final float n = attributeValueAsFloat / 2.0f + 1.125f + 0.010000001f;
        if (n > attributeValueAsFloat2) {
            return n;
        }
        return attributeValueAsFloat2;
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
    protected Point3f getCadOutputInsertionPoint(final String s) {
        final Point3f point3f2;
        final Point3f point3f = point3f2 = (Point3f)this.getGeometricCenterPointLocal().clone();
        point3f2.x -= s.length() / 2.0f;
        final Point3f point3f3 = point3f;
        point3f3.y -= this.getYDimension() / 2.0f - 4.0f;
        return this.convertPointToWorldSpace(point3f);
    }
}
