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
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.utilities.MathUtilities;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDCornerParametricWorksurface;

public class ICDWXCParametricWorksurface extends ICDCornerParametricWorksurface implements ICD2Widths2DepthsGrippable
{
    private LineParameter topEdge;
    private LineParameter rightEdge;
    private LineParameter leftEdge;
    private LineParameter bottomEdge;
    private LineParameter innerEdge;
    private FilletParameter topLeftFillet;
    private FilletParameter topRightFillet;
    private FilletParameter bottomLeftFillet;
    private FilletParameter bottomRightFillet;
    private FilletParameter rightEdgeBottomFillet;
    private FilletParameter bottomEdgeRightFillet;
    private ICD2Widths2DepthsGripSet gripSet;
    
    public ICDWXCParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.bottomEdge = new LineParameter();
        this.innerEdge = new LineParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.rightEdgeBottomFillet = new FilletParameter();
        this.bottomEdgeRightFillet = new FilletParameter();
        this.gripSet = null;
        this.createParameters();
        this.gripSet = new ICD2Widths2DepthsGripSet(this);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWXCParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWXCParametricWorksurface buildClone(final ICDWXCParametricWorksurface icdwxcParametricWorksurface) {
        super.buildClone(icdwxcParametricWorksurface);
        icdwxcParametricWorksurface.calculateParameters();
        return icdwxcParametricWorksurface;
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
        boolean b = true;
        boolean b2 = true;
        boolean b3 = false;
        if (MathUtilities.isSameFloat(attributeValueAsFloat2, attributeValueAsFloat3, 0.001f)) {
            b = false;
        }
        if (MathUtilities.isSameFloat(attributeValueAsFloat, attributeValueAsFloat4, 0.001f)) {
            b2 = false;
        }
        if (!b && !b2) {
            b3 = true;
        }
        final boolean attributeValueAsBoolean = this.getAttributeValueAsBoolean("ICD_Corner_WireDip", false);
        final Point3f point3f = new Point3f();
        final Point3f point3f2 = new Point3f(attributeValueAsFloat2, 0.0f, 0.0f);
        final Point3f point3f3 = new Point3f(0.0f, -attributeValueAsFloat, 0.0f);
        final Point3f point3f4 = new Point3f(attributeValueAsFloat2, -attributeValueAsFloat4, 0.0f);
        final Point3f point3f5 = new Point3f(attributeValueAsFloat3, -attributeValueAsFloat, 0.0f);
        this.topEdge.setStartPoint(point3f);
        this.topEdge.setEndPoint(point3f2);
        this.rightEdge.setStartPoint(point3f2);
        this.rightEdge.setEndPoint(point3f4);
        this.innerEdge.setStartPoint(point3f4);
        this.innerEdge.setEndPoint(point3f5);
        this.bottomEdge.setStartPoint(point3f5);
        this.bottomEdge.setEndPoint(point3f3);
        this.leftEdge.setStartPoint(point3f3);
        this.leftEdge.setEndPoint(point3f);
        this.topLeftFillet = new FilletParameter(this.leftEdge, this.topEdge, 1.125f, !attributeValueAsBoolean);
        this.topRightFillet = new FilletParameter(this.topEdge, this.rightEdge, 1.125f, true);
        if (b) {
            this.rightEdgeBottomFillet = new FilletParameter(this.rightEdge, this.innerEdge, 1.125f, true);
        }
        if (b2) {
            this.bottomEdgeRightFillet = new FilletParameter(this.innerEdge, this.bottomEdge, 1.125f, true);
        }
        if (b3) {
            this.bottomRightFillet = new FilletParameter(this.bottomEdge, this.rightEdge, 1.125f, true);
        }
        this.bottomLeftFillet = new FilletParameter(this.bottomEdge, this.leftEdge, 1.125f, true);
        this.topLeftFillet.setClockwisePath(true);
        this.topRightFillet.setClockwisePath(true);
        this.rightEdgeBottomFillet.setClockwisePath(true);
        this.bottomEdgeRightFillet.setClockwisePath(true);
        this.bottomLeftFillet.setClockwisePath(true);
        this.bottomRightFillet.setClockwisePath(true);
        this.cutoutRefPoint = new Point3f(point3f);
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, -1.0f, 0.0f);
        final float n = 2.5f;
        final Point3f key = new Point3f(point3f2.x - n, point3f2.y - n, 0.0f);
        final Point3f key2 = new Point3f(point3f3.x + n, point3f3.y + n, 0.0f);
        final Point3f key3 = new Point3f(point3f.x + n, point3f.y - n, 0.0f);
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
        if (b) {
            this.allParameters.add((Parameter2D)this.rightEdgeBottomFillet);
        }
        if (b3) {
            this.allParameters.add((Parameter2D)this.bottomRightFillet);
        }
        else {
            this.allParameters.add((Parameter2D)this.innerEdge);
        }
        if (b2) {
            this.allParameters.add((Parameter2D)this.bottomEdgeRightFillet);
        }
        this.allParameters.add((Parameter2D)this.bottomEdge);
        this.allParameters.add((Parameter2D)this.bottomLeftFillet);
        this.allParameters.addAll(this.getParametersForLine(this.leftEdge));
        this.updateBRep(true, true);
        this.width1Anchor = new Point3f(attributeValueAsFloat3 / 2.0f, 0.0f, 0.0f);
        this.width2Anchor = new Point3f(0.0f, -attributeValueAsFloat4 / 2.0f, 0.0f);
        this.depth1Anchor = new Point3f(0.0f, -attributeValueAsFloat, 0.0f);
        this.depth2Anchor = new Point3f(attributeValueAsFloat2, 0.0f, 0.0f);
        this.width1Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.width2Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.depth2Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.wireDipRefPoints.clear();
        this.wireDipRefPoints.add(new Point3f(point3f));
    }
    
    @Override
    public String getShapeTag() {
        return "WXC";
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
    
    @Override
    public float getWidth1Min() {
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth2");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Width1_Min");
        if (attributeValueAsFloat > attributeValueAsFloat2) {
            return attributeValueAsFloat;
        }
        return attributeValueAsFloat2;
    }
    
    @Override
    public float getWidth2Min() {
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Width2_Min");
        if (attributeValueAsFloat > attributeValueAsFloat2) {
            return attributeValueAsFloat;
        }
        return attributeValueAsFloat2;
    }
    
    @Override
    public float getDepth2Max() {
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width1");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth2_Max");
        if (attributeValueAsFloat < attributeValueAsFloat2) {
            return attributeValueAsFloat;
        }
        return attributeValueAsFloat2;
    }
    
    @Override
    public float getDepth1Max() {
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width2");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth1_Max");
        if (attributeValueAsFloat < attributeValueAsFloat2) {
            return attributeValueAsFloat;
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
}
