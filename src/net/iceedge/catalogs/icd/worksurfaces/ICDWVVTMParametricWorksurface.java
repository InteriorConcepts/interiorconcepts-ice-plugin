package net.iceedge.catalogs.icd.worksurfaces;

import javax.vecmath.Tuple3f;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.icecore.basemodule.baseclasses.grips.AttributeGripPoint;
import java.util.SortedSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDWireDip;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.Parameter2D;
import java.util.Collection;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.CircleParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;

public class ICDWVVTMParametricWorksurface extends ICDParametricWorksurface implements ICD2Widths2DepthsGrippable
{
    LineParameter leftEdge;
    LineParameter rightEdge;
    CircleParameter topHemiCircle;
    CircleParameter bottomHemiCircle;
    private FilletParameter topLeftFillet;
    private FilletParameter topRightFillet;
    private FilletParameter bottomRightFillet;
    private FilletParameter bottomLeftFillet;
    private ICD1Width2Way1Depth2WayGripSet gripSet;
    
    public ICDWVVTMParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.leftEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.topHemiCircle = new CircleParameter(new Point3f(0.0f, 0.0f, 0.0f), 18.0f);
        this.bottomHemiCircle = new CircleParameter(new Point3f(0.0f, 0.0f, 0.0f), 18.0f);
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.gripSet = null;
        this.gripSet = new ICD1Width2Way1Depth2WayGripSet(this);
    }
    
    public ICDWVVTMParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.leftEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.topHemiCircle = new CircleParameter(new Point3f(0.0f, 0.0f, 0.0f), 18.0f);
        this.bottomHemiCircle = new CircleParameter(new Point3f(0.0f, 0.0f, 0.0f), 18.0f);
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.gripSet = null;
        this.gripSet = new ICD1Width2Way1Depth2WayGripSet(this);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVVTMParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWVVTMParametricWorksurface buildClone(final ICDWVVTMParametricWorksurface icdwvvtmParametricWorksurface) {
        super.buildClone(icdwvvtmParametricWorksurface);
        icdwvvtmParametricWorksurface.calculateParameters();
        return icdwvvtmParametricWorksurface;
    }
    
    @Override
    protected void calculateParameters() {
        this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor");
        this.shape.clear();
        this.validateDimensionAttributes();
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth");
        final float n = 2.0f;
        final float n2 = attributeValueAsFloat * attributeValueAsFloat / (8.0f * n) + n / 2.0f;
        final float n3 = 1.125f;
        final float n4 = (n2 + n3) * (n2 + n3) - (attributeValueAsFloat / 2.0f - n3) * (attributeValueAsFloat / 2.0f - n3);
        float n5;
        if (n4 < 0.0f) {
            n5 = 0.0f;
        }
        else {
            n5 = (float)Math.sqrt(n4);
        }
        final float n6 = n5 + n - n2 - attributeValueAsFloat2 / 2.0f;
        final Point3f point3f = new Point3f(-attributeValueAsFloat / 2.0f, attributeValueAsFloat2 / 2.0f - n, 0.0f);
        final Point3f point3f2 = new Point3f(attributeValueAsFloat / 2.0f, attributeValueAsFloat2 / 2.0f - n, 0.0f);
        final Point3f point3f3 = new Point3f(-attributeValueAsFloat / 2.0f, n6, 0.0f);
        final Point3f point3f4 = new Point3f(attributeValueAsFloat / 2.0f, n6, 0.0f);
        this.leftEdge = new LineParameter(point3f3, point3f);
        this.rightEdge = new LineParameter(point3f2, point3f4);
        this.topHemiCircle.setRadius(n2);
        this.topHemiCircle.setCenter(new Point3f(0.0f, attributeValueAsFloat2 / 2.0f - n2, 0.0f));
        this.topHemiCircle.setClockwisePath(true);
        this.bottomHemiCircle.setRadius(n2);
        this.bottomHemiCircle.setCenter(new Point3f(0.0f, -(attributeValueAsFloat2 / 2.0f + n2 - n), 0.0f));
        this.bottomHemiCircle.setClockwisePath(false);
        this.topLeftFillet = new FilletParameter(this.leftEdge, this.topHemiCircle, 1.125f, new Point3f(point3f), true, true, true);
        this.topRightFillet = new FilletParameter(this.rightEdge, this.topHemiCircle, 1.125f, new Point3f(point3f2), true, true, true);
        this.bottomRightFillet = new FilletParameter(this.rightEdge, this.bottomHemiCircle, 1.125f, new Point3f(attributeValueAsFloat / 2.0f, -attributeValueAsFloat2 / 2.0f, 0.0f), true, false, true);
        this.bottomLeftFillet = new FilletParameter(this.leftEdge, this.bottomHemiCircle, 1.125f, new Point3f(-attributeValueAsFloat / 2.0f, -attributeValueAsFloat2 / 2.0f, 0.0f), true, false, true);
        this.topLeftFillet.setClockwisePath(true);
        this.topRightFillet.setClockwisePath(true);
        this.bottomRightFillet.setClockwisePath(true);
        this.bottomLeftFillet.setClockwisePath(true);
        this.cutoutRefPoint = new Point3f();
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, 1.0f, 0.0f);
        final Point3f key = new Point3f(0.0f, 7.0f - attributeValueAsFloat2 / 2.0f, 0.0f);
        this.cutoutSnapPointIndexMap.clear();
        this.cutoutSnapPointIndexMap.put(key, 0);
        this.updateSnappedCutouts(key, 0);
        this.cutoutSnapPoints.clear();
        this.addCutoutSnapPoint(key);
        this.allParameters.clear();
        this.plotNodes.clear();
        this.allParameters.addAll(this.getParametersForLine(this.leftEdge));
        this.allParameters.add((Parameter2D)this.topLeftFillet);
        this.allParameters.add((Parameter2D)this.topHemiCircle);
        this.allParameters.add((Parameter2D)this.topRightFillet);
        this.allParameters.add((Parameter2D)this.rightEdge);
        this.allParameters.add((Parameter2D)this.bottomRightFillet);
        this.allParameters.add((Parameter2D)this.bottomHemiCircle);
        this.allParameters.add((Parameter2D)this.bottomLeftFillet);
        this.updateBRep(true, true);
        this.width1Anchor = new Point3f(-attributeValueAsFloat / 2.0f, 0.0f, 0.0f);
        this.width2Anchor = new Point3f(attributeValueAsFloat / 2.0f, 0.0f, 0.0f);
        this.depth1Anchor = new Point3f(0.0f, -attributeValueAsFloat2 / 2.0f, 0.0f);
        this.depth2Anchor = new Point3f(0.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.width2Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(0.0f, 1.0f, 0.0f);
        this.depth2Direction = new Vector3f(0.0f, -1.0f, 0.0f);
    }
    
    @Override
    public String getShapeTag() {
        return "WVVTM";
    }
    
    @Override
    protected void addWireDip(final ICDWireDip icdWireDip) {
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
    protected void setupNamedPoints() {
        super.setupNamedPoints();
        this.addNamedPoint("Top_Left_Snap_Corner_2", new Point3f());
        this.addNamedPoint("Top_Right_Snap_Corner_2", new Point3f());
        this.addNamedPoint("Bottom_Left_Snap_Corner", new Point3f());
        this.addNamedPoint("Bottom_Right_Snap_Corner", new Point3f());
    }
    
    @Override
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        final Point3f geometricCenterPointLocal = this.getGeometricCenterPointLocal();
        final float n = 2.0f / 2.0f;
        final Point3f point3f2;
        final Point3f point3f = point3f2 = new Point3f(geometricCenterPointLocal);
        point3f2.x -= this.getXDimension() / 2.0f;
        final Point3f point3f3 = point3f;
        point3f3.y += this.getYDimension() / 2.0f - n;
        final Point3f point3f5;
        final Point3f point3f4 = point3f5 = new Point3f(geometricCenterPointLocal);
        point3f5.x += this.getXDimension() / 2.0f;
        final Point3f point3f6 = point3f4;
        point3f6.y += this.getYDimension() / 2.0f - n;
        this.getNamedPointLocal("Top_Left_Snap_Corner_2").set((Tuple3f)point3f);
        this.getNamedPointLocal("Top_Right_Snap_Corner_2").set((Tuple3f)point3f4);
        final Point3f point3f8;
        final Point3f point3f7 = point3f8 = new Point3f(geometricCenterPointLocal);
        point3f8.x -= this.getXDimension() / 2.0f;
        final Point3f point3f9 = point3f7;
        point3f9.y -= this.getYDimension() / 2.0f + n;
        final Point3f point3f11;
        final Point3f point3f10 = point3f11 = new Point3f(geometricCenterPointLocal);
        point3f11.x += this.getXDimension() / 2.0f;
        final Point3f point3f12 = point3f10;
        point3f12.y -= this.getYDimension() / 2.0f + n;
        this.getNamedPointLocal("Bottom_Left_Snap_Corner").set((Tuple3f)point3f7);
        this.getNamedPointLocal("Bottom_Right_Snap_Corner").set((Tuple3f)point3f10);
    }
}
