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
import net.dirtt.utilities.MathUtilities;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.CircleParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDCornerParametricWorksurface;

public class ICDCCLCParametricWorksurface extends ICDCornerParametricWorksurface implements ICD1Width1DepthGrippable
{
    private LineParameter cornerEdge;
    private LineParameter rightEdge;
    private LineParameter bottomEdge;
    private CircleParameter topCircle;
    private CircleParameter bottomCircle;
    private CircleParameter leftCircle;
    private CircleParameter rightCircle;
    private FilletParameter highTopLeftFillet;
    private FilletParameter lowTopLeftFillet;
    private FilletParameter highTopRightFillet;
    private FilletParameter lowTopRightFillet;
    private FilletParameter bottomRightFillet;
    private FilletParameter bottomLeftFillet;
    private FilletParameter innerFillet;
    private ICD1Width1DepthGripSet gripSet;
    
    public ICDCCLCParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.cornerEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.bottomEdge = new LineParameter();
        this.topCircle = new CircleParameter();
        this.bottomCircle = new CircleParameter();
        this.leftCircle = new CircleParameter();
        this.rightCircle = new CircleParameter();
        this.highTopLeftFillet = new FilletParameter();
        this.lowTopLeftFillet = new FilletParameter();
        this.highTopRightFillet = new FilletParameter();
        this.lowTopRightFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.innerFillet = new FilletParameter();
        this.gripSet = new ICD1Width1DepthGripSet(this);
        this.createParameters();
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDCCLCParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDCCLCParametricWorksurface buildClone(final ICDCCLCParametricWorksurface icdcclcParametricWorksurface) {
        super.buildClone(icdcclcParametricWorksurface);
        icdcclcParametricWorksurface.calculateParameters();
        return icdcclcParametricWorksurface;
    }
    
    @Override
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("Top_Left_Snap_Corner").set(0.0f, 0.0f, 0.0f);
        this.getNamedPointLocal("Top_Right_Snap_Corner").set(this.getXDimension(), 0.0f, 0.0f);
    }
    
    private void createParameters() {
        this.sideLineParams.add(this.bottomEdge);
        this.sideLineParams.add(this.rightEdge);
    }
    
    @Override
    protected void calculateParameters() {
        this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor");
        this.shape.clear();
        this.validateDimensionAttributes();
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        final float calculateW1_1 = this.calculateW1_1(attributeValueAsFloat, 6.0f, 1.125f);
        final float calculateRadius = this.calculateRadius(calculateW1_1, 1.0f, 1.125f);
        this.topCircle = new CircleParameter(new Point3f(6.0f + calculateW1_1, calculateRadius - 1.0f, 0.0f), calculateRadius);
        final double[] quadraticEquation = MathUtilities.quadraticEquation(2.0, (double)(-2.0f * (calculateRadius + calculateW1_1 + 0.46598962f - 1.0f)), (double)((0.46598962f + calculateW1_1) * (0.46598962f + calculateW1_1) + 1.0f * (1.0f - 2.0f * calculateRadius)));
        final float n = (quadraticEquation[0] < quadraticEquation[1]) ? ((float)quadraticEquation[0]) : ((float)quadraticEquation[1]);
        final Point3f endPoint = new Point3f(5.5340104f + n, n, 0.0f);
        this.cornerEdge.setEndPoint(endPoint);
        final double[] quadraticEquation2 = MathUtilities.quadraticEquation(1.0, (double)(2.0f * (1.0f - calculateRadius)), (double)(1.0f * (1.0f - 2.0f * calculateRadius) + (calculateW1_1 + 1.125f) * (calculateW1_1 + 1.125f)));
        final Point3f startPoint = new Point3f(attributeValueAsFloat, (quadraticEquation2[0] < quadraticEquation2[1]) ? ((float)quadraticEquation2[0]) : ((float)quadraticEquation2[1]), 0.0f);
        final float calculateW1_2 = this.calculateW1_1(attributeValueAsFloat2, 6.0f, 1.125f);
        final float calculateRadius2 = this.calculateRadius(calculateW1_2, 1.0f, 1.125f);
        this.leftCircle = new CircleParameter(new Point3f(-(calculateRadius2 - 1.0f), -(6.0f + calculateW1_2), 0.0f), calculateRadius2);
        final double[] quadraticEquation3 = MathUtilities.quadraticEquation(2.0, (double)(-2.0f * (calculateRadius2 + calculateW1_2 + 0.46598962f - 1.0f)), (double)((0.46598962f + calculateW1_2) * (0.46598962f + calculateW1_2) + 1.0f * (1.0f - 2.0f * calculateRadius2)));
        final float n2 = (quadraticEquation3[0] < quadraticEquation3[1]) ? ((float)quadraticEquation3[0]) : ((float)quadraticEquation3[1]);
        final Point3f startPoint2 = new Point3f(-n2, -(5.5340104f + n2), 0.0f);
        this.cornerEdge.setStartPoint(startPoint2);
        final double[] quadraticEquation4 = MathUtilities.quadraticEquation(1.0, (double)(2.0f * (1.0f - calculateRadius2)), (double)(1.0f * (1.0f - 2.0f * calculateRadius2) + (calculateW1_2 + 1.125f) * (calculateW1_2 + 1.125f)));
        final Point3f endPoint2 = new Point3f(-((quadraticEquation4[0] < quadraticEquation4[1]) ? ((float)quadraticEquation4[0]) : ((float)quadraticEquation4[1])), -attributeValueAsFloat2, 0.0f);
        final float calculateW1_3 = this.calculateW1_2(attributeValueAsFloat, 12.0f, 1.0f, 1.125f);
        final float calculateRadius3 = this.calculateRadius(calculateW1_3, 1.0f, 1.125f);
        this.bottomCircle = new CircleParameter(new Point3f(attributeValueAsFloat - calculateW1_3 - 1.125f, -(calculateRadius3 - 1.0f + 12.0f), 0.0f), calculateRadius3);
        final double[] quadraticEquation5 = MathUtilities.quadraticEquation(1.0, (double)(2.0f * (1.0f - calculateRadius3)), (double)(1.0f * (1.0f - 2.0f * calculateRadius3) + (calculateW1_3 + 1.125f) * (calculateW1_3 + 1.125f)));
        final Point3f endPoint3 = new Point3f(attributeValueAsFloat, -12.0f - ((quadraticEquation5[0] < quadraticEquation5[1]) ? ((float)quadraticEquation5[0]) : ((float)quadraticEquation5[1])), 0.0f);
        final float calculateW1_4 = this.calculateW1_2(attributeValueAsFloat2, 12.0f, 1.0f, 1.125f);
        final float calculateRadius4 = this.calculateRadius(calculateW1_4, 1.0f, 1.125f);
        this.rightCircle = new CircleParameter(new Point3f(calculateRadius4 - 1.0f + 12.0f, -(attributeValueAsFloat2 - calculateW1_4 - 1.125f), 0.0f), calculateRadius4);
        final double[] quadraticEquation6 = MathUtilities.quadraticEquation(1.0, (double)(2.0f * (1.0f - calculateRadius4)), (double)(1.0f * (1.0f - 2.0f * calculateRadius4) + (calculateW1_4 + 1.125f) * (calculateW1_4 + 1.125f)));
        final Point3f startPoint3 = new Point3f(12.0f + ((quadraticEquation6[0] < quadraticEquation6[1]) ? ((float)quadraticEquation6[0]) : ((float)quadraticEquation6[1])), -attributeValueAsFloat2, 0.0f);
        final Point3f point3f = new Point3f(12.0f, -12.0f, 0.0f);
        this.rightEdge.setStartPoint(startPoint);
        this.rightEdge.setEndPoint(endPoint3);
        this.bottomEdge.setStartPoint(startPoint3);
        this.bottomEdge.setEndPoint(endPoint2);
        this.highTopLeftFillet = new FilletParameter(this.cornerEdge, this.topCircle, 1.125f, endPoint, true, false, true);
        this.lowTopLeftFillet = new FilletParameter(this.cornerEdge, this.leftCircle, 1.125f, startPoint2, true, false, true);
        this.highTopRightFillet = new FilletParameter(this.rightEdge, this.topCircle, 1.125f, startPoint, true, false, true);
        this.lowTopRightFillet = new FilletParameter(this.rightEdge, this.bottomCircle, 1.125f, endPoint3, true, false, true);
        this.innerFillet = new FilletParameter(this.rightCircle, this.bottomCircle, 1.125f, point3f, true, true);
        this.bottomRightFillet = new FilletParameter(this.bottomEdge, this.rightCircle, 1.125f, startPoint3, true, false, true);
        this.bottomLeftFillet = new FilletParameter(this.bottomEdge, this.leftCircle, 1.125f, endPoint2, true, false, true);
        this.highTopLeftFillet.setClockwisePath(true);
        this.lowTopLeftFillet.setClockwisePath(true);
        this.highTopRightFillet.setClockwisePath(true);
        this.lowTopRightFillet.setClockwisePath(true);
        this.bottomLeftFillet.setClockwisePath(true);
        this.bottomRightFillet.setClockwisePath(true);
        this.innerFillet.setClockwisePath(false);
        this.allParameters.clear();
        this.plotNodes.clear();
        this.allParameters.add((Parameter2D)this.cornerEdge);
        this.allParameters.add((Parameter2D)this.highTopLeftFillet);
        this.allParameters.add((Parameter2D)this.topCircle);
        this.allParameters.add((Parameter2D)this.highTopRightFillet);
        this.allParameters.add((Parameter2D)this.rightEdge);
        this.allParameters.add((Parameter2D)this.lowTopRightFillet);
        this.allParameters.add((Parameter2D)this.bottomCircle);
        this.allParameters.add((Parameter2D)this.innerFillet);
        this.allParameters.add((Parameter2D)this.rightCircle);
        this.allParameters.add((Parameter2D)this.bottomRightFillet);
        this.allParameters.add((Parameter2D)this.bottomEdge);
        this.allParameters.add((Parameter2D)this.bottomLeftFillet);
        this.allParameters.add((Parameter2D)this.leftCircle);
        this.allParameters.add((Parameter2D)this.lowTopLeftFillet);
        this.updateBRep(true, true);
        this.width1Anchor = new Point3f(0.0f, -6.0f, 0.0f);
        this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.depth1Anchor = new Point3f(6.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(0.0f, -1.0f, 0.0f);
    }
    
    @Override
    public String getShapeTag() {
        return "CCLC";
    }
    
    @Override
    public void setupGripPainters() {
        if (this.gripSet == null) {
            this.gripSet = new ICD1Width1DepthGripSet(this);
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
    public void depth1GripChanged(final String s) {
    }
    
    @Override
    public TransformableEntity getEntity() {
        return (TransformableEntity)this;
    }
    
    @Override
    public float getWidthMax() {
        if (this.getYDimensionFromData() > 60.0f) {
            return 60.0f;
        }
        return super.getWidthMax();
    }
    
    @Override
    public float getDepth1Max() {
        if (this.getXDimensionFromData() > 60.0f) {
            return 60.0f;
        }
        return super.getDepth1Max();
    }
    
    @Override
    protected Point3f getCadOutputInsertionPoint(final String s) {
        final Point3f point3f2;
        final Point3f point3f = point3f2 = (Point3f)this.getGeometricCenterPointLocal().clone();
        point3f2.y += this.getYDimension() / 2.0f - 4.0f;
        return this.convertPointToWorldSpace(point3f);
    }
    
    private float calculateW1_1(final float n, final float n2, final float n3) {
        return (n - n2 - n3) / 2.0f;
    }
    
    private float calculateW1_2(final float n, final float n2, final float n3, final float n4) {
        final float n5 = n - n2 - n4;
        return n5 / 2.0f + n3 * n4 / n5;
    }
    
    private float calculateRadius(final float n, final float n2, final float n3) {
        return n2 / 2.0f - n3 + n * n / 2.0f / n2;
    }
}
