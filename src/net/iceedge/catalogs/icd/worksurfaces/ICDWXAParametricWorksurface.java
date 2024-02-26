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
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.Parameter2D;
import java.util.Collection;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDCornerParametricWorksurface;

public class ICDWXAParametricWorksurface extends ICDCornerParametricWorksurface implements ICD2Widths2DepthsAnd2InternalDepthsGrippable
{
    static float minLength;
    static float inclinedLength;
    static float projMax;
    float proj;
    private LineParameter topEdge;
    private LineParameter rightEdge;
    private LineParameter leftEdge;
    private LineParameter bottomEdge;
    private LineParameter innerRightEdge;
    private LineParameter innerLeftEdge;
    private LineParameter inclinedEdge;
    private FilletParameter topLeftFillet;
    private FilletParameter topRightFillet;
    private FilletParameter bottomLeftFillet;
    private FilletParameter rightEdgeBottomFillet;
    private FilletParameter bottomEdgeRightFillet;
    private FilletParameter innerRightFillet;
    private FilletParameter innerLeftFillet;
    private ICD2Widths2DepthsAnd2InternalDepthsGripSet gripSet;
    protected Point3f innerDepth1Anchor;
    protected Point3f innerDepth2Anchor;
    protected Vector3f innerDepth1Direction;
    protected Vector3f innerDepth2Direction;
    
    public ICDWXAParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.proj = 0.0f;
        this.topEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.bottomEdge = new LineParameter();
        this.innerRightEdge = new LineParameter();
        this.innerLeftEdge = new LineParameter();
        this.inclinedEdge = new LineParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.rightEdgeBottomFillet = new FilletParameter();
        this.bottomEdgeRightFillet = new FilletParameter();
        this.innerRightFillet = new FilletParameter();
        this.innerLeftFillet = new FilletParameter();
        this.gripSet = null;
        this.createParameters();
        this.gripSet = new ICD2Widths2DepthsAnd2InternalDepthsGripSet(this);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWXAParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWXAParametricWorksurface buildClone(final ICDWXAParametricWorksurface icdwxaParametricWorksurface) {
        super.buildClone(icdwxaParametricWorksurface);
        icdwxaParametricWorksurface.calculateParameters();
        return icdwxaParametricWorksurface;
    }
    
    @Override
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("Top_Left_Snap_Corner").set(0.0f, 0.0f, 0.0f);
        this.getNamedPointLocal("Top_Right_Snap_Corner").set(this.getXDimension(), 0.0f, 0.0f);
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
        final float attributeValueAsFloat5 = this.getAttributeValueAsFloat("ICD_Parametric_Inner_Depth1");
        final float attributeValueAsFloat6 = this.getAttributeValueAsFloat("ICD_Parametric_Inner_Depth2");
        final boolean attributeValueAsBoolean = this.getAttributeValueAsBoolean("ICD_Corner_WireDip", false);
        final Point3f point3f = new Point3f();
        final Point3f point3f2 = new Point3f(attributeValueAsFloat2, 0.0f, 0.0f);
        final Point3f point3f3 = new Point3f(0.0f, -attributeValueAsFloat, 0.0f);
        final Point3f point3f4 = new Point3f(attributeValueAsFloat2, -attributeValueAsFloat4, 0.0f);
        final Point3f point3f5 = new Point3f(attributeValueAsFloat3, -attributeValueAsFloat, 0.0f);
        final float n = (float)Math.atan2(attributeValueAsFloat - attributeValueAsFloat4 - attributeValueAsFloat5 - 1.125f, attributeValueAsFloat2 - attributeValueAsFloat3 - attributeValueAsFloat6 - 1.125f) / 2.0f;
        final float n2 = 1.125f * (float)Math.tan(0.7853981633974483 - n);
        final float n3 = 1.125f * (float)Math.tan(n);
        final Point3f point3f6 = new Point3f(attributeValueAsFloat3, -(attributeValueAsFloat - attributeValueAsFloat5 - n2), 0.0f);
        final Point3f point3f7 = new Point3f(attributeValueAsFloat2 - attributeValueAsFloat6 - n3, -attributeValueAsFloat4, 0.0f);
        this.topEdge.setStartPoint(point3f);
        this.topEdge.setEndPoint(point3f2);
        this.rightEdge.setStartPoint(point3f2);
        this.rightEdge.setEndPoint(point3f4);
        this.innerRightEdge.setStartPoint(point3f4);
        this.innerRightEdge.setEndPoint(point3f7);
        this.inclinedEdge.setStartPoint(point3f7);
        this.inclinedEdge.setEndPoint(point3f6);
        this.innerLeftEdge.setStartPoint(point3f6);
        this.innerLeftEdge.setEndPoint(point3f5);
        this.bottomEdge.setStartPoint(point3f5);
        this.bottomEdge.setEndPoint(point3f3);
        this.leftEdge.setStartPoint(point3f3);
        this.leftEdge.setEndPoint(point3f);
        this.topLeftFillet = new FilletParameter(this.leftEdge, this.topEdge, 1.125f, !attributeValueAsBoolean);
        this.topRightFillet = new FilletParameter(this.topEdge, this.rightEdge, 1.125f, true);
        this.rightEdgeBottomFillet = new FilletParameter(this.rightEdge, this.innerRightEdge, 1.125f, true);
        this.innerRightFillet = new FilletParameter(this.innerRightEdge, this.inclinedEdge, 1.125f, true);
        this.innerLeftFillet = new FilletParameter(this.inclinedEdge, this.innerLeftEdge, 1.125f, true);
        this.bottomEdgeRightFillet = new FilletParameter(this.innerLeftEdge, this.bottomEdge, 1.125f, true);
        this.bottomLeftFillet = new FilletParameter(this.bottomEdge, this.leftEdge, 1.125f, true);
        this.topLeftFillet.setClockwisePath(true);
        this.topRightFillet.setClockwisePath(true);
        this.rightEdgeBottomFillet.setClockwisePath(true);
        this.bottomEdgeRightFillet.setClockwisePath(true);
        this.bottomLeftFillet.setClockwisePath(true);
        this.innerRightFillet.setClockwisePath(false);
        this.innerLeftFillet.setClockwisePath(false);
        this.cutoutRefPoint = new Point3f(point3f);
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, -1.0f, 0.0f);
        final float n4 = 2.5f;
        final Point3f key = new Point3f(point3f2.x - n4, point3f2.y - n4, 0.0f);
        final Point3f key2 = new Point3f(point3f3.x + n4, point3f3.y + n4, 0.0f);
        final Point3f key3 = new Point3f(point3f.x + n4, point3f.y - n4, 0.0f);
        this.cutoutSnapPointIndexMap.clear();
        this.cutoutSnapPointIndexMap.put(key3, 0);
        this.cutoutSnapPointIndexMap.put(key, 1);
        this.cutoutSnapPointIndexMap.put(key2, 2);
        this.updateSnappedCutouts(key3, 0);
        this.updateSnappedCutouts(key, 1);
        this.updateSnappedCutouts(key2, 2);
        this.cutoutSnapPoints.clear();
        this.addCutoutSnapPoint(key3);
        this.addCutoutSnapPoint(key);
        this.addCutoutSnapPoint(key2);
        if (attributeValueAsFloat2 >= 60.0f) {
            final Point3f key4 = new Point3f(point3f.x + attributeValueAsFloat2 / 2.0f - 3.0f, point3f.y - n4, 0.0f);
            this.cutoutSnapPointIndexMap.put(key4, 3);
            this.updateSnappedCutouts(key4, 3);
            this.addCutoutSnapPoint(key4);
        }
        if (attributeValueAsFloat >= 60.0f) {
            final Point3f key5 = new Point3f(point3f.x + n4, point3f3.y + attributeValueAsFloat / 2.0f + 3.0f, 0.0f);
            this.cutoutSnapPointIndexMap.put(key5, 4);
            this.updateSnappedCutouts(key5, 4);
            this.addCutoutSnapPoint(key5);
        }
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
        this.allParameters.add((Parameter2D)this.rightEdgeBottomFillet);
        this.allParameters.add((Parameter2D)this.innerRightEdge);
        this.allParameters.add((Parameter2D)this.innerRightFillet);
        this.allParameters.add((Parameter2D)this.inclinedEdge);
        this.allParameters.add((Parameter2D)this.innerLeftFillet);
        this.allParameters.add((Parameter2D)this.innerLeftEdge);
        this.allParameters.add((Parameter2D)this.bottomEdgeRightFillet);
        this.allParameters.add((Parameter2D)this.bottomEdge);
        this.allParameters.add((Parameter2D)this.bottomLeftFillet);
        this.allParameters.addAll(this.getParametersForLine(this.leftEdge));
        this.updateBRep(true, true);
        this.width1Anchor = new Point3f(attributeValueAsFloat3 / 2.0f, 0.0f, 0.0f);
        this.width2Anchor = new Point3f(0.0f, -attributeValueAsFloat4 / 2.0f, 0.0f);
        this.depth1Anchor = new Point3f(0.0f, (-attributeValueAsFloat + point3f6.y) / 2.0f, 0.0f);
        this.depth2Anchor = new Point3f((attributeValueAsFloat2 + point3f7.x) / 2.0f, 0.0f, 0.0f);
        this.innerDepth1Anchor = new Point3f(attributeValueAsFloat3 - 3.0f, -attributeValueAsFloat, 0.0f);
        this.innerDepth2Anchor = new Point3f(attributeValueAsFloat2, -attributeValueAsFloat4 + 3.0f, 0.0f);
        this.width1Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.width2Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.depth2Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.innerDepth1Direction = new Vector3f(0.0f, 1.0f, 0.0f);
        this.innerDepth2Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.wireDipRefPoints.clear();
        this.wireDipRefPoints.add(new Point3f(point3f));
    }
    
    @Override
    public String getShapeTag() {
        return "WXA";
    }
    
    @Override
    public void setupGripPainters() {
        if (this.gripSet == null) {
            this.gripSet = new ICD2Widths2DepthsAnd2InternalDepthsGripSet(this);
        }
        else {
            this.gripSet.updateGripPainters();
        }
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
    
    public void thickness1GripChanged(final String s) {
    }
    
    public void thickness2GripChanged(final String s) {
    }
    
    @Override
    public float getWidth1Min() {
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth2");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Width1_Min");
        if (attributeValueAsFloat + this.proj + ICDWXAParametricWorksurface.minLength > attributeValueAsFloat2) {
            return attributeValueAsFloat + this.proj + ICDWXAParametricWorksurface.minLength;
        }
        return attributeValueAsFloat2;
    }
    
    @Override
    public float getWidth2Min() {
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Width2_Min");
        if (attributeValueAsFloat + this.proj + ICDWXAParametricWorksurface.minLength > attributeValueAsFloat2) {
            return attributeValueAsFloat + this.proj + ICDWXAParametricWorksurface.minLength;
        }
        return attributeValueAsFloat2;
    }
    
    @Override
    public float getDepth2Max() {
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width1");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth2_Max");
        if (attributeValueAsFloat - this.proj - ICDWXAParametricWorksurface.minLength < attributeValueAsFloat2) {
            return attributeValueAsFloat - this.proj - ICDWXAParametricWorksurface.minLength;
        }
        return attributeValueAsFloat2;
    }
    
    @Override
    public float getDepth1Max() {
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width2");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth1_Max");
        if (attributeValueAsFloat - this.proj - ICDWXAParametricWorksurface.minLength < attributeValueAsFloat2) {
            return attributeValueAsFloat - this.proj - ICDWXAParametricWorksurface.minLength;
        }
        return attributeValueAsFloat2;
    }
    
    @Override
    public float getWidth1Max() {
        if (this.getAttributeValueAsFloat("ICD_Parametric_Width2") > 60.0f) {
            return 60.0f;
        }
        return super.getWidth1Max();
    }
    
    @Override
    public float getWidth2Max() {
        if (this.getAttributeValueAsFloat("ICD_Parametric_Width1") > 60.0f) {
            return 60.0f;
        }
        return super.getWidth2Max();
    }
    
    @Override
    protected void validateDimensionAttributes() {
        super.validateDimensionAttributes();
        this.clampAttributeValue("ICD_Parametric_Inner_Depth1", this.getInnerDepth1Min(), this.getInnerDepth1Max());
        this.clampAttributeValue("ICD_Parametric_Inner_Depth2", this.getInnerDepth2Min(), this.getInnerDepth2Max());
    }
    
    public float getInnerDepth1() {
        return this.getMinimumValueFromAttribute("ICD_Parametric_Inner_Depth1");
    }
    
    public float getInnerDepth2() {
        return this.getMinimumValueFromAttribute("ICD_Parametric_Inner_Depth2");
    }
    
    @Override
    public float getInnerDepth1Min() {
        return this.getMinimumValueFromAttribute("ICD_Parametric_Inner_Depth1_Min");
    }
    
    @Override
    public float getInnerDepth2Min() {
        return this.getMinimumValueFromAttribute("ICD_Parametric_Inner_Depth2_Min");
    }
    
    @Override
    public float getInnerDepth2Max() {
        float n = this.getAttributeValueAsFloat("ICD_Parametric_Width2") - this.getAttributeValueAsFloat("ICD_Parametric_Depth1") - 1.125f - 0.010000001f;
        if (n < 0.0f) {
            n = 0.0f;
        }
        return n;
    }
    
    @Override
    public float getInnerDepth1Max() {
        float n = this.getAttributeValueAsFloat("ICD_Parametric_Width1") - this.getAttributeValueAsFloat("ICD_Parametric_Depth2") - 1.125f - 0.010000001f;
        if (n < 0.0f) {
            n = 0.0f;
        }
        return n;
    }
    
    @Override
    public void innerDepth1GripChanged(final String s) {
    }
    
    @Override
    public void innerDepth2GripChanged(final String s) {
    }
    
    @Override
    public Point3f getInnerDepth1Anchor() {
        return this.innerDepth1Anchor;
    }
    
    @Override
    public Point3f getInnerDepth2Anchor() {
        return this.innerDepth2Anchor;
    }
    
    @Override
    public Vector3f getInnerDepth1Direction() {
        return this.innerDepth1Direction;
    }
    
    @Override
    public Vector3f getInnerDepth2Direction() {
        return this.innerDepth2Direction;
    }
    
    static {
        ICDWXAParametricWorksurface.minLength = 2.0f;
        ICDWXAParametricWorksurface.inclinedLength = 25.0f;
        ICDWXAParametricWorksurface.projMax = 0.707107f * ICDWXAParametricWorksurface.inclinedLength;
    }
}
