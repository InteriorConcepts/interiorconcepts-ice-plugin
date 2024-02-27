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
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.CircleParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;

public class ICDWRTParametricWorksurface extends ICDParametricWorksurface implements ICD2Widths2DepthsGrippable
{
    LineParameter topEdge;
    LineParameter bottomEdge;
    CircleParameter rightHemiCircle;
    CircleParameter leftHemiCircle;
    private ICD1Width2Way1Depth2WayGripSet gripSet;
    
    public ICDWRTParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.bottomEdge = new LineParameter();
        this.rightHemiCircle = new CircleParameter(new Point3f(0.0f, 0.0f, 0.0f), 18.0f);
        this.leftHemiCircle = new CircleParameter(new Point3f(0.0f, 0.0f, 0.0f), 18.0f);
        this.gripSet = null;
        this.createParameters();
        this.gripSet = new ICD1Width2Way1Depth2WayGripSet(this);
    }
    
    public ICDWRTParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.bottomEdge = new LineParameter();
        this.rightHemiCircle = new CircleParameter(new Point3f(0.0f, 0.0f, 0.0f), 18.0f);
        this.leftHemiCircle = new CircleParameter(new Point3f(0.0f, 0.0f, 0.0f), 18.0f);
        this.gripSet = null;
        this.createParameters();
        this.gripSet = new ICD1Width2Way1Depth2WayGripSet(this);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWRTParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWRTParametricWorksurface buildClone(final ICDWRTParametricWorksurface icdwrtParametricWorksurface) {
        super.buildClone(icdwrtParametricWorksurface);
        icdwrtParametricWorksurface.calculateParameters();
        return icdwrtParametricWorksurface;
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
        final float n = attributeValueAsFloat2 / 2.0f;
        final float n2 = attributeValueAsFloat / 2.0f - n;
        final Point3f point3f = new Point3f(-n2, n, 0.0f);
        final Point3f point3f2 = new Point3f(n2, n, 0.0f);
        final Point3f point3f3 = new Point3f(-n2, -n, 0.0f);
        final Point3f point3f4 = new Point3f(n2, -n, 0.0f);
        this.topEdge = new LineParameter(point3f, point3f2);
        this.bottomEdge = new LineParameter(point3f4, point3f3);
        this.rightHemiCircle.setRadius(n);
        this.rightHemiCircle.setCenter(new Point3f(n2, 0.0f, 0.0f));
        this.rightHemiCircle.setClockwisePath(true);
        this.leftHemiCircle.setRadius(n);
        this.leftHemiCircle.setCenter(new Point3f(-n2, 0.0f, 0.0f));
        this.leftHemiCircle.setClockwisePath(true);
        this.cutoutRefPoint = new Point3f(point3f);
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, -1.0f, 0.0f);
        this.cutoutSnapPointIndexMap.clear();
        this.cutoutSnapPoints.clear();
        final Point3f key = new Point3f(0.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        this.cutoutSnapPointIndexMap.put(key, 0);
        this.updateSnappedCutouts(key, 0);
        this.addCutoutSnapPoint(key);
        this.wireDipRefPoints.clear();
        this.wireDipRefPoints.add(new Point3f(point3f));
        final Point3f point3f5 = new Point3f(0.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f point3f6 = new Point3f(-attributeValueAsFloat / 6.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f point3f7 = new Point3f(attributeValueAsFloat / 6.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        this.snapPointIndexMap.clear();
        this.wireDipSnapPoints.clear();
        this.updateDipLocations();
        this.allParameters.clear();
        this.plotNodes.clear();
        this.allParameters.addAll(this.getParametersForLine(this.topEdge));
        this.allParameters.add((Parameter2D)this.rightHemiCircle);
        this.allParameters.add((Parameter2D)this.bottomEdge);
        this.allParameters.add((Parameter2D)this.leftHemiCircle);
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
        return "WRT";
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
        float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width_Min");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Width");
        final float attributeValueAsFloat3 = this.getAttributeValueAsFloat("ICD_Parametric_Depth");
        if (attributeValueAsFloat2 <= attributeValueAsFloat3) {
            attributeValueAsFloat = attributeValueAsFloat3;
        }
        return attributeValueAsFloat;
    }
    
    @Override
    public float getDepthMax() {
        float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth_Max");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Width");
        if (this.getAttributeValueAsFloat("ICD_Parametric_Depth") >= attributeValueAsFloat2) {
            attributeValueAsFloat = attributeValueAsFloat2;
        }
        return attributeValueAsFloat;
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
