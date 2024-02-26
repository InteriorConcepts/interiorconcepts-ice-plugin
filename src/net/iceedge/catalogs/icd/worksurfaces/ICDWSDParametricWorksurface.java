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
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;

public class ICDWSDParametricWorksurface extends ICDParametricWorksurface implements ICD1Width2Way1DepthGrippable
{
    private static final float keyboardCutoutWidth = 24.75f;
    private LineParameter topEdge;
    private LineParameter rightEdge;
    private LineParameter bottomEdge1;
    private LineParameter innerRightEdge;
    private LineParameter innerBottomEdge;
    private LineParameter innerLeftEdge;
    private LineParameter bottomEdge2;
    private LineParameter leftEdge;
    private FilletParameter topLeftFillet;
    private FilletParameter topRightFillet;
    private FilletParameter bottomRightFillet;
    private FilletParameter bottomFilletOnLeftFromRightFillet;
    private FilletParameter innerRightFillet;
    private FilletParameter innerLeftFillet;
    private FilletParameter bottomFilletOnRightFromLeftFillet;
    private FilletParameter bottomLeftFillet;
    private ICD1Width2Way1DepthGripset gripSet;
    
    public ICDWSDParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.bottomEdge1 = new LineParameter();
        this.innerRightEdge = new LineParameter();
        this.innerBottomEdge = new LineParameter();
        this.innerLeftEdge = new LineParameter();
        this.bottomEdge2 = new LineParameter();
        this.leftEdge = new LineParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.bottomFilletOnLeftFromRightFillet = new FilletParameter();
        this.innerRightFillet = new FilletParameter();
        this.innerLeftFillet = new FilletParameter();
        this.bottomFilletOnRightFromLeftFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.gripSet = new ICD1Width2Way1DepthGripset(this);
        this.createParameters();
    }
    
    public ICDWSDParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.bottomEdge1 = new LineParameter();
        this.innerRightEdge = new LineParameter();
        this.innerBottomEdge = new LineParameter();
        this.innerLeftEdge = new LineParameter();
        this.bottomEdge2 = new LineParameter();
        this.leftEdge = new LineParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.bottomFilletOnLeftFromRightFillet = new FilletParameter();
        this.innerRightFillet = new FilletParameter();
        this.innerLeftFillet = new FilletParameter();
        this.bottomFilletOnRightFromLeftFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.gripSet = new ICD1Width2Way1DepthGripset(this);
        this.createParameters();
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWSDParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWSDParametricWorksurface buildClone(final ICDWSDParametricWorksurface icdwsdParametricWorksurface) {
        super.buildClone(icdwsdParametricWorksurface);
        icdwsdParametricWorksurface.calculateParameters();
        return icdwsdParametricWorksurface;
    }
    
    private void createParameters() {
        this.lineParams.add(this.topEdge);
        this.sideLineParams.add(this.leftEdge);
        this.sideLineParams.add(this.rightEdge);
    }
    
    @Override
    protected void calculateParameters() {
        this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor");
        this.shape.clear();
        this.validateDimensionAttributes();
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth");
        final String attributeValueAsString = this.getAttributeValueAsString("ICD_Keyboard_Cutout_Position");
        float n = attributeValueAsFloat / 2.0f;
        final float n2 = 12.375f;
        if (attributeValueAsString != null) {
            if (attributeValueAsString.equalsIgnoreCase("Left")) {
                n = 5.0f + n2;
            }
            else if (attributeValueAsString.equalsIgnoreCase("Right")) {
                n = attributeValueAsFloat - 5.0f - n2;
            }
        }
        final Point3f point3f = new Point3f();
        final Point3f point3f2 = new Point3f(attributeValueAsFloat, 0.0f, 0.0f);
        final Point3f point3f3 = new Point3f(attributeValueAsFloat, -attributeValueAsFloat2, 0.0f);
        final Point3f point3f4 = new Point3f(n + n2, -attributeValueAsFloat2, 0.0f);
        final Point3f point3f5 = new Point3f(n + n2, -attributeValueAsFloat2 + 10.5f, 0.0f);
        final Point3f point3f6 = new Point3f(n - n2, -attributeValueAsFloat2 + 10.5f, 0.0f);
        final Point3f point3f7 = new Point3f(n - n2, -attributeValueAsFloat2, 0.0f);
        final Point3f point3f8 = new Point3f(0.0f, -attributeValueAsFloat2, 0.0f);
        this.topEdge.setStartPoint(point3f);
        this.topEdge.setEndPoint(point3f2);
        this.rightEdge.setStartPoint(point3f2);
        this.rightEdge.setEndPoint(point3f3);
        this.bottomEdge1.setStartPoint(point3f3);
        this.bottomEdge1.setEndPoint(point3f4);
        this.innerRightEdge.setStartPoint(point3f4);
        this.innerRightEdge.setEndPoint(point3f5);
        this.innerBottomEdge.setStartPoint(point3f5);
        this.innerBottomEdge.setEndPoint(point3f6);
        this.innerLeftEdge.setStartPoint(point3f6);
        this.innerLeftEdge.setEndPoint(point3f7);
        this.bottomEdge2.setStartPoint(point3f7);
        this.bottomEdge2.setEndPoint(point3f8);
        this.leftEdge.setStartPoint(point3f8);
        this.leftEdge.setEndPoint(point3f);
        this.topLeftFillet = new FilletParameter(this.leftEdge, this.topEdge, 1.125f, true);
        this.topRightFillet = new FilletParameter(this.topEdge, this.rightEdge, 1.125f, true);
        this.bottomRightFillet = new FilletParameter(this.rightEdge, this.bottomEdge1, 1.125f, true);
        this.bottomFilletOnLeftFromRightFillet = new FilletParameter(this.bottomEdge1, this.innerRightEdge, 1.125f, true);
        this.innerRightFillet = new FilletParameter(this.innerRightEdge, this.innerBottomEdge, 1.125f, true);
        this.innerLeftFillet = new FilletParameter(this.innerBottomEdge, this.innerLeftEdge, 1.125f, true);
        this.bottomFilletOnRightFromLeftFillet = new FilletParameter(this.innerLeftEdge, this.bottomEdge2, 1.125f, true);
        this.bottomLeftFillet = new FilletParameter(this.bottomEdge2, this.leftEdge, 1.125f, true);
        this.topLeftFillet.setClockwisePath(true);
        this.topRightFillet.setClockwisePath(true);
        this.bottomRightFillet.setClockwisePath(true);
        this.bottomFilletOnLeftFromRightFillet.setClockwisePath(true);
        this.innerRightFillet.setClockwisePath(false);
        this.innerLeftFillet.setClockwisePath(false);
        this.bottomFilletOnRightFromLeftFillet.setClockwisePath(true);
        this.bottomLeftFillet.setClockwisePath(true);
        this.cutoutRefPoint = new Point3f(point3f);
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, -1.0f, 0.0f);
        final Point3f key = new Point3f(point3f2.x - 2.5f, point3f2.y - 2.5f, 0.0f);
        final Point3f key2 = new Point3f(point3f.x + 2.5f, point3f.y - 2.5f, 0.0f);
        this.cutoutSnapPointIndexMap.clear();
        this.cutoutSnapPointIndexMap.put(key2, 0);
        this.cutoutSnapPointIndexMap.put(key, 1);
        this.updateSnappedCutouts(key2, 0);
        this.updateSnappedCutouts(key, 1);
        this.cutoutSnapPoints.clear();
        this.addCutoutSnapPoint(key2);
        this.addCutoutSnapPoint(key);
        if (attributeValueAsFloat >= 60.0f) {
            final Point3f key3 = new Point3f(point3f.x + attributeValueAsFloat / 2.0f - 3.0f, point3f.y - 2.5f, 0.0f);
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
        this.allParameters.add((Parameter2D)this.bottomEdge1);
        this.allParameters.add((Parameter2D)this.bottomFilletOnLeftFromRightFillet);
        this.allParameters.add((Parameter2D)this.innerRightEdge);
        this.allParameters.add((Parameter2D)this.innerRightFillet);
        this.allParameters.add((Parameter2D)this.innerBottomEdge);
        this.allParameters.add((Parameter2D)this.innerLeftFillet);
        this.allParameters.add((Parameter2D)this.innerLeftEdge);
        this.allParameters.add((Parameter2D)this.bottomFilletOnRightFromLeftFillet);
        this.allParameters.add((Parameter2D)this.bottomEdge2);
        this.allParameters.add((Parameter2D)this.bottomLeftFillet);
        this.allParameters.add((Parameter2D)this.leftEdge);
        this.allParameters.add((Parameter2D)this.topLeftFillet);
        this.updateBRep(true, true);
        this.width1Anchor = ICDParametricWorksurface.pointAt(point3f, new Vector3f(0.0f, -1.0f, 0.0f), attributeValueAsFloat2 / 2.0f);
        this.width2Anchor = ICDParametricWorksurface.pointAt(point3f2, new Vector3f(0.0f, -1.0f, 0.0f), attributeValueAsFloat2 / 2.0f);
        this.depth1Anchor = ICDParametricWorksurface.pointAt(point3f, new Vector3f(1.0f, 0.0f, 0.0f), attributeValueAsFloat / 2.0f);
        this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.width2Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.wireDipRefPoints.clear();
        this.wireDipRefPoints.add(new Point3f(point3f));
    }
    
    @Override
    public String getShapeTag() {
        return "WSD";
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
    public TransformableEntity getEntity() {
        return (TransformableEntity)this;
    }
    
    @Override
    public void depth1GripChanged(final String s) {
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
        point3f2.x -= this.getXDimension() / 2.0f - 4.0f;
        return this.convertPointToWorldSpace(point3f);
    }
}
