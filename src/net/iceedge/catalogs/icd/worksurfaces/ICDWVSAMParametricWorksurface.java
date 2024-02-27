package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.icecore.basemodule.baseclasses.grips.AttributeGripPoint;
import java.util.SortedSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;
import net.dirtt.utilities.MathUtilities;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.Parameter2D;
import java.util.Collection;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.CircleParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDCornerParametricWorksurface;

public class ICDWVSAMParametricWorksurface extends ICDCornerParametricWorksurface implements ICD2Widths2DepthsGrippable
{
    private LineParameter topEdge;
    private LineParameter rightEdge;
    private LineParameter bottomEdge;
    private LineParameter leftEdge;
    private CircleParameter bottomRightArc;
    private FilletParameter topLeftFillet;
    private FilletParameter topRightFillet;
    private FilletParameter rightArcFillet;
    private FilletParameter leftArcFillet;
    private FilletParameter bottomLeftFillet;
    private ICD2Widths2DepthsGripSet gripSet;
    private float oldDepth1;
    private float oldDepth2;
    
    public ICDWVSAMParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.bottomEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.bottomRightArc = new CircleParameter(new Point3f(1.43f, -1.43f, 0.0f), 16.32f);
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.rightArcFillet = new FilletParameter();
        this.leftArcFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.gripSet = null;
        this.createParameters();
        this.setupNamedPoints();
        this.gripSet = new ICD2Widths2DepthsGripSet(this);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVSAMParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWVSAMParametricWorksurface buildClone(final ICDWVSAMParametricWorksurface icdwvsamParametricWorksurface) {
        super.buildClone(icdwvsamParametricWorksurface);
        icdwvsamParametricWorksurface.calculateParameters();
        return icdwvsamParametricWorksurface;
    }
    
    private void createParameters() {
        this.lineParams.add(this.topEdge);
        this.lineParams.add(this.leftEdge);
    }
    
    @Override
    protected void calculateParameters() {
        this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor");
        this.shape.clear();
        this.validateDimensionAttributes();
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width1");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Width2");
        final boolean attributeValueAsBoolean = this.getAttributeValueAsBoolean("ICD_Corner_WireDip", false);
        final Point3f point3f = new Point3f(-attributeValueAsFloat2, attributeValueAsFloat, 0.0f);
        final Point3f point3f2 = new Point3f(0.0f, attributeValueAsFloat, 0.0f);
        final Point3f point3f3 = new Point3f(-attributeValueAsFloat2, 0.0f, 0.0f);
        final Point3f endPoint = this.bottomRightArc.getRayIntersections(point3f2, new Vector3f(0.0f, -1.0f, 0.0f), CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
        final Point3f startPoint = this.bottomRightArc.getRayIntersections(point3f3, new Vector3f(1.0f, 0.0f, 0.0f), CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
        this.topEdge.setStartPoint(point3f);
        this.topEdge.setEndPoint(point3f2);
        this.rightEdge.setStartPoint(point3f2);
        this.rightEdge.setEndPoint(endPoint);
        this.bottomEdge.setStartPoint(startPoint);
        this.bottomEdge.setEndPoint(point3f3);
        this.leftEdge.setStartPoint(point3f3);
        this.leftEdge.setEndPoint(point3f);
        this.bottomRightArc.calculate();
        this.topLeftFillet = new FilletParameter(this.leftEdge, this.topEdge, 1.125f, !attributeValueAsBoolean);
        this.topRightFillet = new FilletParameter(this.topEdge, this.rightEdge, 1.125f, true);
        this.bottomLeftFillet = new FilletParameter(this.bottomEdge, this.leftEdge, 1.125f, true);
        this.rightArcFillet = new FilletParameter(this.rightEdge, this.bottomRightArc, 1.125f, endPoint, true, false, true);
        this.leftArcFillet = new FilletParameter(this.bottomEdge, this.bottomRightArc, 1.125f, startPoint, true, false, true);
        this.bottomRightArc.setClockwisePath(false);
        this.topLeftFillet.setClockwisePath(true);
        this.topRightFillet.setClockwisePath(true);
        this.bottomLeftFillet.setClockwisePath(true);
        this.rightArcFillet.setClockwisePath(true);
        this.leftArcFillet.setClockwisePath(true);
        this.allParameters.clear();
        this.plotNodes.clear();
        if (attributeValueAsBoolean) {
            this.allParameters.addAll(this.getParametersForCornerWireDip(this.leftEdge, this.topEdge, true, true, false));
        }
        else {
            this.allParameters.add((Parameter2D)this.topLeftFillet);
        }
        this.allParameters.addAll(this.getParametersForLine(this.topEdge));
        this.allParameters.add((Parameter2D)this.topRightFillet);
        this.allParameters.add((Parameter2D)this.rightEdge);
        this.allParameters.add((Parameter2D)this.rightArcFillet);
        this.allParameters.add((Parameter2D)this.bottomRightArc);
        this.allParameters.add((Parameter2D)this.leftArcFillet);
        this.allParameters.add((Parameter2D)this.bottomEdge);
        this.allParameters.add((Parameter2D)this.bottomLeftFillet);
        this.allParameters.addAll(this.getParametersForLine(this.leftEdge));
        this.updateBRep(true, true);
        this.width1Anchor = ICDParametricWorksurface.pointAt(point3f, new Vector3f(1.0f, 0.0f, 0.0f), point3f3.distance(startPoint) / 2.0f);
        this.width2Anchor = ICDParametricWorksurface.pointAt(point3f, new Vector3f(0.0f, -1.0f, 0.0f), point3f2.distance(endPoint) / 2.0f);
        this.depth1Anchor = ICDParametricWorksurface.pointAt(point3f3, new Vector3f(0.0f, 1.0f, 0.0f), 6.0f);
        this.depth2Anchor = ICDParametricWorksurface.pointAt(point3f2, new Vector3f(-1.0f, 0.0f, 0.0f), 6.0f);
        this.width1Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.width2Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.depth2Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.cutoutRefPoint = new Point3f(point3f);
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, -1.0f, 0.0f);
        this.wireDipRefPoints.clear();
        this.wireDipRefPoints.add(new Point3f(point3f));
    }
    
    @Override
    public float getLeftDepth() {
        return this.getAttributeValueAsFloat("ICD_Parametric_Depth1") - 3.8228f;
    }
    
    @Override
    public float getRightDepth() {
        return this.getAttributeValueAsFloat("ICD_Parametric_Depth2") - 3.8228f;
    }
    
    @Override
    public String getShapeTag() {
        return "WVSAM";
    }
    
    @Override
    protected void validateDimensionAttributes() {
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth2");
        if (!MathUtilities.isSameFloat(this.oldDepth1, attributeValueAsFloat, 0.001f)) {
            this.modifyAttributeValue("ICD_Parametric_Width2", String.valueOf(attributeValueAsFloat + 11.0f));
        }
        if (!MathUtilities.isSameFloat(this.oldDepth2, attributeValueAsFloat2, 0.001f)) {
            this.modifyAttributeValue("ICD_Parametric_Width1", String.valueOf(attributeValueAsFloat2 + 11.0f));
        }
        super.validateDimensionAttributes();
        final float attributeValueAsFloat3 = this.getAttributeValueAsFloat("ICD_Parametric_Width1");
        final float attributeValueAsFloat4 = this.getAttributeValueAsFloat("ICD_Parametric_Width2");
        this.modifyAttributeValue("ICD_Parametric_Depth2", String.valueOf(attributeValueAsFloat3 - 11.0f));
        this.modifyAttributeValue("ICD_Parametric_Depth1", String.valueOf(attributeValueAsFloat4 - 11.0f));
        this.oldDepth1 = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        this.oldDepth2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth2");
    }
    
    @Override
    public void setupGripPainters() {
        if (this.gripSet == null) {
            this.gripSet = new ICD2Widths2DepthsGripSet(this);
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
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width1");
        try {
            n = Float.parseFloat(s);
        }
        catch (NumberFormatException ex) {
            System.err.println(" ERROR parsing attribute value");
            ex.printStackTrace();
        }
        final Point3f basePoint = new Point3f(0.0f, attributeValueAsFloat - n, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
    }
    
    @Override
    public void width2GripChanged(final String s) {
        Float n = 0.0f;
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width2");
        try {
            n = Float.parseFloat(s);
        }
        catch (NumberFormatException ex) {
            System.err.println(" ERROR parsing attribute value");
            ex.printStackTrace();
        }
        final Point3f basePoint = new Point3f(-(attributeValueAsFloat - n), 0.0f, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
    }
    
    @Override
    public void depth1GripChanged(final String s) {
        Float n = 0.0f;
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        try {
            n = Float.parseFloat(s);
        }
        catch (NumberFormatException ex) {
            System.err.println(" ERROR parsing attribute value");
            ex.printStackTrace();
        }
        final Point3f basePoint = new Point3f(-(attributeValueAsFloat - n), 0.0f, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
    }
    
    @Override
    public void depth2GripChanged(final String s) {
        Float n = 0.0f;
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth2");
        try {
            n = Float.parseFloat(s);
        }
        catch (NumberFormatException ex) {
            System.err.println(" ERROR parsing attribute value");
            ex.printStackTrace();
        }
        final Point3f basePoint = new Point3f(0.0f, attributeValueAsFloat - n, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
    }
    
    @Override
    public TransformableEntity getEntity() {
        return (TransformableEntity)this;
    }
}
