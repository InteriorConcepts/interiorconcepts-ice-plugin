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
import java.util.Collection;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDCornerParametricWorksurface;

public class ICDCCLParametricWorksurface extends ICDCornerParametricWorksurface implements ICD1Width1DepthGrippable
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
    private ICD1Width1DepthGripSet gripSet;
    
    public ICDCCLParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
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
        this.gripSet = new ICD1Width1DepthGripSet(this);
        this.createParameters();
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDCCLParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDCCLParametricWorksurface buildClone(final ICDCCLParametricWorksurface icdcclParametricWorksurface) {
        super.buildClone(icdcclParametricWorksurface);
        icdcclParametricWorksurface.calculateParameters();
        return icdcclParametricWorksurface;
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
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        final float n = 12.0f;
        final Point3f point3f = new Point3f();
        final Point3f point3f2 = new Point3f(attributeValueAsFloat, 0.0f, 0.0f);
        final Point3f point3f3 = new Point3f(0.0f, -attributeValueAsFloat2, 0.0f);
        final Point3f point3f4 = new Point3f(attributeValueAsFloat, -n, 0.0f);
        final Point3f point3f5 = new Point3f(n, -attributeValueAsFloat2, 0.0f);
        final Point3f point3f6 = new Point3f(n, -n, 0.0f);
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
        this.topLeftFillet = new FilletParameter(this.leftEdge, this.topEdge, 1.125f, true);
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
        this.allParameters.clear();
        this.plotNodes.clear();
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
        this.allParameters.add((Parameter2D)this.leftEdge);
        this.allParameters.add((Parameter2D)this.topLeftFillet);
        this.updateBRep(true, true);
        this.width1Anchor = new Point3f(0.0f, -n / 2.0f, 0.0f);
        this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.depth1Anchor = new Point3f(n / 2.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(0.0f, -1.0f, 0.0f);
    }
    
    @Override
    public String getShapeTag() {
        return "CCL";
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
}
