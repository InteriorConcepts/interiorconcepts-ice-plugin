// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.BoundingCube;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.icecore.basemodule.baseclasses.grips.AttributeGripPoint;
import java.util.SortedSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;
import javax.vecmath.Vector3f;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.Parameter2D;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;

public class ICDCCCParametricWorksurface extends ICDParametricWorksurface implements ICD2Widths2DepthsGrippable
{
    private LineParameter topHorizontalEdge;
    private LineParameter bottomHorizontalEdge;
    private LineParameter rightEdge;
    private LineParameter leftVerticalEdge;
    private LineParameter rightVerticalEdge;
    private LineParameter bottomEdge;
    private FilletParameter outerRightFillet;
    private FilletParameter innerRightFillet;
    private FilletParameter outerLeftFillet;
    private FilletParameter innerLeftFillet;
    private FilletParameter innerArc;
    private FilletParameter outerArc;
    private ICD2Widths2DepthsGripSet gripSet;
    
    public ICDCCCParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.topHorizontalEdge = new LineParameter();
        this.bottomHorizontalEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.leftVerticalEdge = new LineParameter();
        this.rightVerticalEdge = new LineParameter();
        this.bottomEdge = new LineParameter();
        this.outerRightFillet = new FilletParameter();
        this.innerRightFillet = new FilletParameter();
        this.outerLeftFillet = new FilletParameter();
        this.innerLeftFillet = new FilletParameter();
        this.innerArc = new FilletParameter();
        this.outerArc = new FilletParameter();
        this.gripSet = null;
        this.gripSet = new ICD2Widths2DepthsGripSet(this);
    }
    
    public ICDCCCParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.topHorizontalEdge = new LineParameter();
        this.bottomHorizontalEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.leftVerticalEdge = new LineParameter();
        this.rightVerticalEdge = new LineParameter();
        this.bottomEdge = new LineParameter();
        this.outerRightFillet = new FilletParameter();
        this.innerRightFillet = new FilletParameter();
        this.outerLeftFillet = new FilletParameter();
        this.innerLeftFillet = new FilletParameter();
        this.innerArc = new FilletParameter();
        this.outerArc = new FilletParameter();
        this.gripSet = null;
        this.gripSet = new ICD2Widths2DepthsGripSet(this);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDCCCParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDCCCParametricWorksurface buildClone(final ICDCCCParametricWorksurface icdcccParametricWorksurface) {
        super.buildClone(icdcccParametricWorksurface);
        icdcccParametricWorksurface.calculateParameters();
        return icdcccParametricWorksurface;
    }
    
    @Override
    protected void calculateParameters() {
        this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor");
        this.shape.clear();
        this.validateDimensionAttributes();
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width1");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Width2");
        final float n = 12.0f;
        final float n2 = 29.5f;
        final float n3 = 17.5f;
        final float n4 = n2;
        final float n5 = -n2;
        final Point3f point3f = new Point3f(n5, n4, 0.0f);
        final Point3f point3f2 = new Point3f(n5 + attributeValueAsFloat, n4, 0.0f);
        final Point3f point3f3 = new Point3f(n5 + attributeValueAsFloat, n4 - n, 0.0f);
        final Point3f point3f4 = new Point3f(n5 + n, n4 - n, 0.0f);
        final Point3f point3f5 = new Point3f(n5 + n, n4 - attributeValueAsFloat2, 0.0f);
        final Point3f point3f6 = new Point3f(n5, n4 - attributeValueAsFloat2, 0.0f);
        this.topHorizontalEdge = new LineParameter(point3f, point3f2);
        this.rightEdge = new LineParameter(point3f2, point3f3);
        this.bottomHorizontalEdge = new LineParameter(point3f3, point3f4);
        this.rightVerticalEdge = new LineParameter(point3f4, point3f5);
        this.bottomEdge = new LineParameter(point3f5, point3f6);
        this.leftVerticalEdge = new LineParameter(point3f6, point3f);
        this.outerRightFillet = new FilletParameter(this.topHorizontalEdge, this.rightEdge, 1.125f, true);
        this.innerRightFillet = new FilletParameter(this.rightEdge, this.bottomHorizontalEdge, 1.125f, true);
        this.innerLeftFillet = new FilletParameter(this.rightVerticalEdge, this.bottomEdge, 1.125f, true);
        this.outerLeftFillet = new FilletParameter(this.bottomEdge, this.leftVerticalEdge, 1.125f, true);
        this.outerArc = new FilletParameter(this.leftVerticalEdge, this.topHorizontalEdge, n2, true);
        this.innerArc = new FilletParameter(this.bottomHorizontalEdge, this.rightVerticalEdge, n3, true);
        this.outerRightFillet.setClockwisePath(true);
        this.innerRightFillet.setClockwisePath(true);
        this.innerLeftFillet.setClockwisePath(true);
        this.outerLeftFillet.setClockwisePath(true);
        this.outerArc.setClockwisePath(true);
        this.innerArc.setClockwisePath(false);
        this.allParameters.clear();
        this.plotNodes.clear();
        this.allParameters.add((Parameter2D)this.topHorizontalEdge);
        this.allParameters.add((Parameter2D)this.outerRightFillet);
        this.allParameters.add((Parameter2D)this.rightEdge);
        this.allParameters.add((Parameter2D)this.innerRightFillet);
        this.allParameters.add((Parameter2D)this.bottomHorizontalEdge);
        this.allParameters.add((Parameter2D)this.innerArc);
        this.allParameters.add((Parameter2D)this.rightVerticalEdge);
        this.allParameters.add((Parameter2D)this.innerLeftFillet);
        this.allParameters.add((Parameter2D)this.bottomEdge);
        this.allParameters.add((Parameter2D)this.outerLeftFillet);
        this.allParameters.add((Parameter2D)this.leftVerticalEdge);
        this.allParameters.add((Parameter2D)this.outerArc);
        this.updateBRep(true, true);
        this.width1Anchor = new Point3f(n5, n4 - n / 2.0f, 0.0f);
        this.width2Anchor = new Point3f(n5 + n / 2.0f, n4, 0.0f);
        this.depth1Anchor = new Point3f(n5, -((attributeValueAsFloat - (n + n3)) / 2.0f), 0.0f);
        this.depth2Anchor = new Point3f((attributeValueAsFloat2 - (n + n3)) / 2.0f, n4, 0.0f);
        this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.width2Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.depth1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.depth2Direction = new Vector3f(0.0f, -1.0f, 0.0f);
    }
    
    @Override
    public float getWidth1Max() {
        final float maximumValueFromAttribute = this.getMaximumValueFromAttribute("ICD_Parametric_Width1_Max");
        return (this.getAttributeValueAsFloat("ICD_Parametric_Width2") > 60.0f) ? 60.0f : maximumValueFromAttribute;
    }
    
    @Override
    public float getWidth2Max() {
        final float maximumValueFromAttribute = this.getMaximumValueFromAttribute("ICD_Parametric_Width2_Max");
        return (this.getAttributeValueAsFloat("ICD_Parametric_Width1") > 60.0f) ? 60.0f : maximumValueFromAttribute;
    }
    
    @Override
    public String getShapeTag() {
        return "CCC";
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
    }
    
    @Override
    public void width2GripChanged(final String s) {
    }
    
    @Override
    public void depth1GripChanged(final String s) {
    }
    
    @Override
    public void depth2GripChanged(final String s) {
    }
    
    @Override
    public TransformableEntity getEntity() {
        return (TransformableEntity)this;
    }
    
    @Override
    public BoundingCube getCubeForTriggers() {
        return null;
    }
    
    @Override
    protected Point3f getCadOutputInsertionPoint(final String s) {
        final Point3f point3f2;
        final Point3f point3f = point3f2 = (Point3f)this.getGeometricCenterPointLocal().clone();
        point3f2.x += 2.0f;
        final Point3f point3f3 = point3f;
        point3f3.y += this.getYDimension() / 2.0f - 4.0f;
        return this.convertPointToWorldSpace(point3f);
    }
}
