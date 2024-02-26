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
import javax.vecmath.Tuple3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDCornerParametricWorksurface;

public class ICDWSLParametricWorksurface extends ICDCornerParametricWorksurface implements ICD2Widths2DepthsGrippable
{
    private LineParameter topEdge;
    private LineParameter rightEdge;
    private LineParameter leftEdge;
    private LineParameter bottomEdge;
    private LineParameter innerRightEdge;
    private LineParameter innerLeftEdge;
    private FilletParameter topLeftFillet;
    private FilletParameter topRightFillet;
    private FilletParameter bottomLeftFillet;
    private FilletParameter innerRightFillet;
    private FilletParameter innerLeftFillet;
    private FilletParameter innerFillet;
    private ICD2Widths2DepthsGripSet gripSet;
    
    public ICDWSLParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.bottomEdge = new LineParameter();
        this.innerRightEdge = new LineParameter();
        this.innerLeftEdge = new LineParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.innerRightFillet = new FilletParameter();
        this.innerLeftFillet = new FilletParameter();
        this.innerFillet = new FilletParameter();
        this.gripSet = null;
        this.createParameters();
        this.gripSet = new ICD2Widths2DepthsGripSet(this);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWSLParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWSLParametricWorksurface buildClone(final ICDWSLParametricWorksurface icdwslParametricWorksurface) {
        super.buildClone(icdwslParametricWorksurface);
        icdwslParametricWorksurface.calculateParameters();
        return icdwslParametricWorksurface;
    }
    
    @Override
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.validateDimensionAttributes();
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width2");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        final float attributeValueAsFloat3 = this.getAttributeValueAsFloat("ICD_Parametric_Depth2");
        final Point3f point3f = new Point3f(-attributeValueAsFloat2, attributeValueAsFloat3, 0.0f);
        final Point3f point3f2 = new Point3f(-attributeValueAsFloat2 + attributeValueAsFloat, attributeValueAsFloat3, 0.0f);
        this.getNamedPointLocal("Top_Left_Snap_Corner").set((Tuple3f)point3f);
        this.getNamedPointLocal("Top_Right_Snap_Corner").set((Tuple3f)point3f2);
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
        final boolean attributeValueAsBoolean = this.getAttributeValueAsBoolean("ICD_Corner_WireDip", false);
        final Point3f point3f = new Point3f(-attributeValueAsFloat3, attributeValueAsFloat4, 0.0f);
        final Point3f point3f2 = new Point3f(-attributeValueAsFloat3 + attributeValueAsFloat2, attributeValueAsFloat4, 0.0f);
        final Point3f point3f3 = new Point3f(-attributeValueAsFloat3, attributeValueAsFloat4 - attributeValueAsFloat, 0.0f);
        final Point3f point3f4 = new Point3f(-attributeValueAsFloat3 + attributeValueAsFloat2, 0.0f, 0.0f);
        final Point3f point3f5 = new Point3f(0.0f, attributeValueAsFloat4 - attributeValueAsFloat, 0.0f);
        final Point3f point3f6 = new Point3f();
        this.topEdge.setStartPoint(point3f);
        this.topEdge.setEndPoint(point3f2);
        this.rightEdge.setStartPoint(point3f2);
        this.rightEdge.setEndPoint(point3f4);
        this.innerRightEdge.setStartPoint(point3f4);
        this.innerRightEdge.setEndPoint(point3f6);
        this.innerLeftEdge.setStartPoint(point3f6);
        this.innerLeftEdge.setEndPoint(point3f5);
        this.bottomEdge.setStartPoint(point3f5);
        this.bottomEdge.setEndPoint(point3f3);
        this.leftEdge.setStartPoint(point3f3);
        this.leftEdge.setEndPoint(point3f);
        this.topLeftFillet = new FilletParameter(this.leftEdge, this.topEdge, 1.125f, !attributeValueAsBoolean);
        this.topRightFillet = new FilletParameter(this.topEdge, this.rightEdge, 1.125f, true);
        this.innerRightFillet = new FilletParameter(this.rightEdge, this.innerRightEdge, 1.125f, true);
        this.innerFillet = new FilletParameter(this.innerRightEdge, this.innerLeftEdge, 1.125f, true);
        this.innerLeftFillet = new FilletParameter(this.innerLeftEdge, this.bottomEdge, 1.125f, true);
        this.bottomLeftFillet = new FilletParameter(this.bottomEdge, this.leftEdge, 1.125f, true);
        this.topLeftFillet.setClockwisePath(true);
        this.topRightFillet.setClockwisePath(true);
        this.innerRightFillet.setClockwisePath(true);
        this.innerFillet.setClockwisePath(false);
        this.innerLeftFillet.setClockwisePath(true);
        this.bottomLeftFillet.setClockwisePath(true);
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
        if (attributeValueAsFloat2 >= 60.0f) {
            final Point3f key4 = new Point3f(point3f.x + attributeValueAsFloat2 / 2.0f - 3.0f, point3f.y - n, 0.0f);
            this.cutoutSnapPointIndexMap.put(key4, 3);
            this.updateSnappedCutouts(key4, 3);
            this.addCutoutSnapPoint(key4);
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
        this.allParameters.add((Parameter2D)this.innerRightFillet);
        this.allParameters.add((Parameter2D)this.innerRightEdge);
        this.allParameters.add((Parameter2D)this.innerFillet);
        this.allParameters.add((Parameter2D)this.innerLeftEdge);
        this.allParameters.add((Parameter2D)this.innerLeftFillet);
        this.allParameters.add((Parameter2D)this.bottomEdge);
        this.allParameters.add((Parameter2D)this.bottomLeftFillet);
        this.allParameters.addAll(this.getParametersForLine(this.leftEdge));
        this.updateBRep(true, true);
        this.width1Anchor = new Point3f(-attributeValueAsFloat3 + attributeValueAsFloat3 / 2.0f, attributeValueAsFloat4, 0.0f);
        this.width2Anchor = new Point3f(-attributeValueAsFloat3, attributeValueAsFloat4 - attributeValueAsFloat4 / 2.0f, 0.0f);
        this.depth1Anchor = new Point3f(-attributeValueAsFloat3, -((attributeValueAsFloat - attributeValueAsFloat4) / 2.0f), 0.0f);
        this.depth2Anchor = new Point3f((attributeValueAsFloat2 - attributeValueAsFloat3) / 2.0f, attributeValueAsFloat4, 0.0f);
        this.width1Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.width2Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.depth2Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.wireDipRefPoints.clear();
        this.wireDipRefPoints.add(new Point3f(point3f));
    }
    
    @Override
    public String getShapeTag() {
        return "WSL";
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
        float a = 0.0f;
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Width2");
        try {
            a = Float.parseFloat(s);
            a = Math.min(a, attributeValueAsFloat2);
        }
        catch (NumberFormatException ex) {
            System.err.println(" ERROR parsing attribute value");
            ex.printStackTrace();
        }
        final Point3f basePoint = new Point3f(-(attributeValueAsFloat - a), 0.0f, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
    }
    
    @Override
    public void depth2GripChanged(final String s) {
        float a = 0.0f;
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth2");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Width1");
        try {
            a = Float.parseFloat(s);
            a = Math.min(a, attributeValueAsFloat2);
        }
        catch (NumberFormatException ex) {
            System.err.println(" ERROR parsing attribute value");
            ex.printStackTrace();
        }
        final Point3f basePoint = new Point3f(0.0f, attributeValueAsFloat - a, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
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
}
