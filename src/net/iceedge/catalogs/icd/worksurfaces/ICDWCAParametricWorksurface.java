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
import net.dirtt.utilities.MathUtilities;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.Parameter2D;
import java.util.Collection;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDCornerParametricWorksurface;

public class ICDWCAParametricWorksurface extends ICDCornerParametricWorksurface implements ICD2Widths2DepthsGrippable
{
    private static final float chamferEdgeLength = 25.0f;
    private LineParameter topEdge;
    private LineParameter rightEdge;
    private LineParameter leftEdge;
    private LineParameter bottomEdge;
    private LineParameter innerRightEdge;
    private LineParameter innerLeftEdge;
    private LineParameter chamferEdge;
    private FilletParameter topLeftFillet;
    private FilletParameter topRightFillet;
    private FilletParameter bottomLeftFillet;
    private FilletParameter innerRightFillet;
    private FilletParameter innerLeftFillet;
    private FilletParameter chamferRightFillet;
    private FilletParameter chamferLeftFillet;
    private ICD2Widths2DepthsGripSet gripSet;
    
    public ICDWCAParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.bottomEdge = new LineParameter();
        this.innerRightEdge = new LineParameter();
        this.innerLeftEdge = new LineParameter();
        this.chamferEdge = new LineParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.innerRightFillet = new FilletParameter();
        this.innerLeftFillet = new FilletParameter();
        this.chamferRightFillet = new FilletParameter();
        this.chamferLeftFillet = new FilletParameter();
        this.gripSet = null;
        this.createParameters();
        this.gripSet = new ICD2Widths2DepthsGripSet(this);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWCAParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWCAParametricWorksurface buildClone(final ICDWCAParametricWorksurface icdwcaParametricWorksurface) {
        super.buildClone(icdwcaParametricWorksurface);
        icdwcaParametricWorksurface.calculateParameters();
        return icdwcaParametricWorksurface;
    }
    
    private void createParameters() {
        this.lineParams.add(this.topEdge);
        this.lineParams.add(this.leftEdge);
        this.sideLineParams.add(this.bottomEdge);
        this.sideLineParams.add(this.rightEdge);
    }
    
    @Override
    protected void calculateParameters() {
        this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor");
        this.shape.clear();
        this.validateDimensionAttributes();
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width1");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Width2");
        final float attributeValueAsFloat3 = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        final float attributeValueAsFloat4 = this.getAttributeValueAsFloat("ICD_Parametric_Depth2");
        final float chamferOffset = this.getChamferOffset(25.0f);
        final float n = chamferOffset + attributeValueAsFloat4;
        final float n2 = -(chamferOffset + attributeValueAsFloat3);
        final float n3 = 23.0f;
        final Point3f point3f = new Point3f(n2, n, 0.0f);
        final Point3f point3f2 = new Point3f(n2 + attributeValueAsFloat2, n, 0.0f);
        final Point3f point3f3 = new Point3f(n2, n - attributeValueAsFloat, 0.0f);
        final Point3f point3f4 = new Point3f(n2 + attributeValueAsFloat2, chamferOffset, 0.0f);
        final Point3f point3f5 = new Point3f(-chamferOffset, n - attributeValueAsFloat, 0.0f);
        final Point3f point3f6 = new Point3f(-chamferOffset, 0.0f, 0.0f);
        final Point3f point3f7 = new Point3f(0.0f, chamferOffset, 0.0f);
        this.topEdge.setStartPoint(point3f);
        this.topEdge.setEndPoint(point3f2);
        this.rightEdge.setStartPoint(point3f2);
        this.rightEdge.setEndPoint(point3f4);
        this.innerRightEdge.setStartPoint(point3f4);
        this.innerRightEdge.setEndPoint(point3f7);
        this.chamferEdge.setStartPoint(point3f7);
        this.chamferEdge.setEndPoint(point3f6);
        this.innerLeftEdge.setStartPoint(point3f6);
        this.innerLeftEdge.setEndPoint(point3f5);
        this.bottomEdge.setStartPoint(point3f5);
        this.bottomEdge.setEndPoint(point3f3);
        this.leftEdge.setStartPoint(point3f3);
        this.leftEdge.setEndPoint(point3f);
        this.topLeftFillet = new FilletParameter(this.leftEdge, this.topEdge, n3, true);
        this.topRightFillet = new FilletParameter(this.topEdge, this.rightEdge, 1.125f, true);
        this.innerRightFillet = new FilletParameter(this.rightEdge, this.innerRightEdge, 1.125f, true);
        this.chamferRightFillet = new FilletParameter(this.innerRightEdge, this.chamferEdge, 1.125f, true);
        this.chamferLeftFillet = new FilletParameter(this.chamferEdge, this.innerLeftEdge, 1.125f, true);
        this.innerLeftFillet = new FilletParameter(this.innerLeftEdge, this.bottomEdge, 1.125f, true);
        this.bottomLeftFillet = new FilletParameter(this.bottomEdge, this.leftEdge, 1.125f, true);
        this.topLeftFillet.setClockwisePath(true);
        this.topRightFillet.setClockwisePath(true);
        this.innerRightFillet.setClockwisePath(true);
        this.chamferRightFillet.setClockwisePath(false);
        this.chamferLeftFillet.setClockwisePath(false);
        this.innerLeftFillet.setClockwisePath(true);
        this.bottomLeftFillet.setClockwisePath(true);
        this.cutoutRefPoint = new Point3f(point3f);
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, -1.0f, 0.0f);
        final Point3f key = new Point3f(point3f2.x - 2.5f, point3f2.y - 2.5f, 0.0f);
        final Point3f key2 = new Point3f(point3f3.x + 2.5f, point3f3.y + 2.5f, 0.0f);
        final float n4 = n3 * (float)Math.sqrt(2.0) - n3 + 2.5f;
        final Vector3f vector3f = new Vector3f(1.0f, -1.0f, 0.0f);
        vector3f.normalize();
        final Point3f point = ICDParametricWorksurface.pointAt(point3f, vector3f, n4);
        this.cutoutSnapPointIndexMap.clear();
        this.cutoutSnapPointIndexMap.put(point, 0);
        this.cutoutSnapPointIndexMap.put(key, 1);
        this.cutoutSnapPointIndexMap.put(key2, 2);
        this.updateSnappedCutouts(point, 0);
        this.updateSnappedCutouts(key, 1);
        this.updateSnappedCutouts(key2, 2);
        this.cutoutSnapPoints.clear();
        this.addCutoutSnapPoint(point);
        this.addCutoutSnapPoint(key);
        this.addCutoutSnapPoint(key2);
        this.allParameters.clear();
        this.plotNodes.clear();
        this.allParameters.addAll(this.getParametersForLine(this.topEdge));
        this.allParameters.add((Parameter2D)this.topRightFillet);
        this.allParameters.add((Parameter2D)this.rightEdge);
        this.allParameters.add((Parameter2D)this.innerRightFillet);
        this.allParameters.add((Parameter2D)this.innerRightEdge);
        this.allParameters.add((Parameter2D)this.chamferRightFillet);
        this.allParameters.add((Parameter2D)this.chamferEdge);
        this.allParameters.add((Parameter2D)this.chamferLeftFillet);
        this.allParameters.add((Parameter2D)this.innerLeftEdge);
        this.allParameters.add((Parameter2D)this.innerLeftFillet);
        this.allParameters.add((Parameter2D)this.bottomEdge);
        this.allParameters.add((Parameter2D)this.bottomLeftFillet);
        this.allParameters.addAll(this.getParametersForLine(this.leftEdge));
        this.allParameters.add((Parameter2D)this.topLeftFillet);
        this.updateBRep(true, true);
        this.width1Anchor = new Point3f(n2 + attributeValueAsFloat3 / 2.0f, n, 0.0f);
        this.width2Anchor = new Point3f(n2, n - attributeValueAsFloat4 / 2.0f, 0.0f);
        this.depth1Anchor = new Point3f(n2, -((attributeValueAsFloat - (attributeValueAsFloat4 + chamferOffset)) / 2.0f), 0.0f);
        this.depth2Anchor = new Point3f((attributeValueAsFloat2 - (attributeValueAsFloat3 + chamferOffset)) / 2.0f, n, 0.0f);
        this.width1Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.width2Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.depth2Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.wireDipRefPoints.clear();
        this.wireDipRefPoints.add(new Point3f(point3f));
    }
    
    @Override
    public float getWidth1Min() {
        final float minimumValueFromAttribute = this.getMinimumValueFromAttribute("ICD_Parametric_Width1_Min");
        this.clampAttributeValue("ICD_Parametric_Depth2", this.getDepth2Min(), this.getDepth2Max());
        return Math.max(minimumValueFromAttribute, MathUtilities.roundToNearestDecmial(this.getAttributeValueAsFloat("ICD_Parametric_Depth2") + this.getChamferOffset(25.0f) + 2.0f + 0.25f, 0.5f));
    }
    
    @Override
    public float getWidth1Max() {
        final float maximumValueFromAttribute = this.getMaximumValueFromAttribute("ICD_Parametric_Width1_Max");
        return (this.getAttributeValueAsFloat("ICD_Parametric_Width2") > 60.0f) ? 60.0f : maximumValueFromAttribute;
    }
    
    @Override
    public float getWidth2Min() {
        final float minimumValueFromAttribute = this.getMinimumValueFromAttribute("ICD_Parametric_Width2_Min");
        this.clampAttributeValue("ICD_Parametric_Depth1", this.getDepth1Min(), this.getDepth1Max());
        return Math.max(minimumValueFromAttribute, MathUtilities.roundToNearestDecmial(this.getAttributeValueAsFloat("ICD_Parametric_Depth1") + this.getChamferOffset(25.0f) + 2.0f + 0.25f, 0.5f));
    }
    
    @Override
    public float getWidth2Max() {
        final float maximumValueFromAttribute = this.getMaximumValueFromAttribute("ICD_Parametric_Width2_Max");
        return (this.getAttributeValueAsFloat("ICD_Parametric_Width1") > 60.0f) ? 60.0f : maximumValueFromAttribute;
    }
    
    private float getChamferOffset(final float n) {
        return (float)Math.sqrt(n * n / 2.0f);
    }
    
    @Override
    public String getShapeTag() {
        return "WCA";
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
