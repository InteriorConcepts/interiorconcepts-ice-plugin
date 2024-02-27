package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.icecore.basemodule.baseclasses.grips.AttributeGripPoint;
import java.util.SortedSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDWireDip;
import javax.vecmath.Vector3f;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.Parameter2D;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.CircleParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;

public class ICDWVCFMParametricWorksurface extends ICDParametricWorksurface implements ICD2Widths2DepthsGrippable
{
    private CircleParameter topCircle;
    private CircleParameter bottomCircle;
    private CircleParameter leftCircle;
    private CircleParameter rightCircle;
    private FilletParameter topLeftFillet;
    private FilletParameter topRightFillet;
    private FilletParameter bottomRightFillet;
    private FilletParameter bottomLeftFillet;
    private ICD1Width2Way1Depth2WayGripSet gripSet;
    
    public ICDWVCFMParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.topCircle = new CircleParameter();
        this.bottomCircle = new CircleParameter();
        this.leftCircle = new CircleParameter();
        this.rightCircle = new CircleParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.gripSet = null;
        this.gripSet = new ICD1Width2Way1Depth2WayGripSet(this);
    }
    
    public ICDWVCFMParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.topCircle = new CircleParameter();
        this.bottomCircle = new CircleParameter();
        this.leftCircle = new CircleParameter();
        this.rightCircle = new CircleParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.gripSet = null;
        this.gripSet = new ICD1Width2Way1Depth2WayGripSet(this);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVCFMParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWVCFMParametricWorksurface buildClone(final ICDWVCFMParametricWorksurface icdwvcfmParametricWorksurface) {
        super.buildClone(icdwvcfmParametricWorksurface);
        icdwvcfmParametricWorksurface.calculateParameters();
        return icdwvcfmParametricWorksurface;
    }
    
    @Override
    protected void calculateParameters() {
        this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor");
        this.shape.clear();
        this.validateDimensionAttributes();
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth");
        final Point3f point3f = new Point3f(-(attributeValueAsFloat / 2.0f - 6.0f), attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f point3f2 = new Point3f(attributeValueAsFloat / 2.0f - 6.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f point3f3 = new Point3f(-(attributeValueAsFloat / 2.0f - 6.0f), -attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f point3f4 = new Point3f(attributeValueAsFloat / 2.0f - 6.0f, -attributeValueAsFloat2 / 2.0f, 0.0f);
        this.topCircle = new CircleParameter(point3f, new Point3f(0.0f, attributeValueAsFloat2 / 2.0f - 3.0f, 0.0f), point3f2);
        final Point3f point3f5 = new Point3f(this.topCircle.getCenter());
        point3f5.y = -point3f5.y;
        this.bottomCircle = new CircleParameter(point3f5, this.topCircle.getRadius());
        this.rightCircle = new CircleParameter(point3f2, new Point3f(attributeValueAsFloat / 2.0f, 0.0f, 0.0f), point3f4);
        final Point3f point3f6 = new Point3f(this.rightCircle.getCenter());
        point3f6.x = -point3f6.x;
        this.leftCircle = new CircleParameter(point3f6, this.rightCircle.getRadius());
        this.topLeftFillet = new FilletParameter(this.leftCircle, this.topCircle, 1.125f, point3f, true, false);
        this.topRightFillet = new FilletParameter(this.topCircle, this.rightCircle, 1.125f, point3f2, false, true);
        this.bottomRightFillet = new FilletParameter(this.rightCircle, this.bottomCircle, 1.125f, point3f4, true, false);
        this.bottomLeftFillet = new FilletParameter(this.bottomCircle, this.leftCircle, 1.125f, point3f3, false, true);
        this.topCircle.setClockwisePath(false);
        this.rightCircle.setClockwisePath(true);
        this.bottomCircle.setClockwisePath(false);
        this.leftCircle.setClockwisePath(true);
        this.topLeftFillet.setClockwisePath(true);
        this.topRightFillet.setClockwisePath(true);
        this.bottomRightFillet.setClockwisePath(true);
        this.bottomLeftFillet.setClockwisePath(true);
        this.topCircle.calculate();
        this.bottomCircle.calculate();
        this.rightCircle.calculate();
        this.leftCircle.calculate();
        this.allParameters.clear();
        this.plotNodes.clear();
        this.allParameters.add((Parameter2D)this.topCircle);
        this.allParameters.add((Parameter2D)this.topRightFillet);
        this.allParameters.add((Parameter2D)this.rightCircle);
        this.allParameters.add((Parameter2D)this.bottomRightFillet);
        this.allParameters.add((Parameter2D)this.bottomCircle);
        this.allParameters.add((Parameter2D)this.bottomLeftFillet);
        this.allParameters.add((Parameter2D)this.leftCircle);
        this.allParameters.add((Parameter2D)this.topLeftFillet);
        this.updateBRep(true, true);
        this.width1Anchor = new Point3f(-attributeValueAsFloat / 2.0f, 0.0f, 0.0f);
        this.width2Anchor = new Point3f(attributeValueAsFloat / 2.0f, 0.0f, 0.0f);
        this.depth1Anchor = new Point3f(0.0f, -attributeValueAsFloat2 / 2.0f, 0.0f);
        this.depth2Anchor = new Point3f(0.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.width2Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(0.0f, 1.0f, 0.0f);
        this.depth2Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.cutoutRefPoint = new Point3f();
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, 1.0f, 0.0f);
    }
    
    @Override
    public String getShapeTag() {
        return "WVCFM";
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
    protected Point3f getCadOutputInsertionPoint(final String s) {
        final Point3f point3f2;
        final Point3f point3f = point3f2 = (Point3f)this.getGeometricCenterPointLocal().clone();
        point3f2.x -= s.length() * 2.0f / 2.0f;
        final Point3f point3f3 = point3f;
        point3f3.y -= this.getYDimension() / 2.0f - 4.0f;
        return this.convertPointToWorldSpace(point3f);
    }
}
