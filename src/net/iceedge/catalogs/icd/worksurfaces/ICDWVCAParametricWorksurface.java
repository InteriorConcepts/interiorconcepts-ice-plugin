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
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;
import java.util.List;
import java.util.Collections;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.Parameter2D;
import java.util.Collection;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.RayParameter;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.CircleParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDCornerParametricWorksurface;

public class ICDWVCAParametricWorksurface extends ICDCornerParametricWorksurface implements ICD2Widths2DepthsGrippable
{
    private LineParameter topEdge;
    private LineParameter leftEdge;
    private LineParameter rightEdge;
    private LineParameter bottomRightEdge;
    private LineParameter bottomLeftEdge;
    private CircleParameter bottomArc;
    private FilletParameter topLeftFillet;
    private FilletParameter topRightFillet;
    private FilletParameter bottomRightFillet;
    private FilletParameter bottomLeftFillet;
    private FilletParameter leftArcFillet;
    private FilletParameter rightArcFillet;
    private ICD2Widths2DepthsGripSet gripSet;
    private float oldDepth1;
    
    public ICDWVCAParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.bottomRightEdge = new LineParameter();
        this.bottomLeftEdge = new LineParameter();
        this.bottomArc = new CircleParameter(new Point3f(0.0f, -1.43f, 0.0f), 16.32f);
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.leftArcFillet = new FilletParameter();
        this.rightArcFillet = new FilletParameter();
        this.gripSet = null;
        this.createParameters();
        this.gripSet = new ICD2Widths2DepthsGripSet(this);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVCAParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWVCAParametricWorksurface buildClone(final ICDWVCAParametricWorksurface icdwvcaParametricWorksurface) {
        super.buildClone(icdwvcaParametricWorksurface);
        icdwvcaParametricWorksurface.calculateParameters();
        return icdwvcaParametricWorksurface;
    }
    
    private void createParameters() {
        this.lineParams.add(this.topEdge);
        this.lineParams.add(this.rightEdge);
    }
    
    @Override
    protected void calculateParameters() {
        this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor");
        this.shape.clear();
        this.validateDimensionAttributes();
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width1");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Width2");
        final float attributeValueAsFloat3 = this.getAttributeValueAsFloat("ICD_Parametric_Depth2");
        final boolean attributeValueAsBoolean = this.getAttributeValueAsBoolean("ICD_Corner_WireDip", false);
        final boolean mirrored = this.isMirrored();
        final RayParameter rayParameter = new RayParameter(new Point3f(), new Vector3f(0.0f, 1.0f, 0.0f));
        final float n = attributeValueAsFloat3 + 12.43f;
        final Point3f point3f = new Point3f(n, 0.0f, 0.0f);
        final Point3f point3f2 = new Point3f(n, attributeValueAsFloat2, 0.0f);
        final Point3f startPoint = new Point3f(n - attributeValueAsFloat, attributeValueAsFloat2, 0.0f);
        final Point3f point3f3 = new Point3f(n - attributeValueAsFloat, 11.0f, 0.0f);
        final Point3f point3f4 = this.bottomArc.getRayIntersections(point3f3, new Vector3f(1.0f, 0.0f, 0.0f), CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
        final Point3f point3f5 = this.bottomArc.getRayIntersections(point3f, new Vector3f(-1.0f, 0.0f, 0.0f), CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
        this.topEdge.setStartPoint(startPoint);
        this.topEdge.setEndPoint(point3f2);
        this.rightEdge.setStartPoint(point3f2);
        this.rightEdge.setEndPoint(point3f);
        this.leftEdge = new LineParameter(point3f3, startPoint);
        this.bottomRightEdge = new LineParameter(point3f, point3f5);
        this.bottomLeftEdge = new LineParameter(point3f4, point3f3);
        this.bottomArc.calculate();
        this.topLeftFillet = new FilletParameter(this.leftEdge, this.topEdge, 1.125f, true);
        this.topRightFillet = new FilletParameter(this.topEdge, this.rightEdge, 1.125f, !attributeValueAsBoolean);
        this.bottomRightFillet = new FilletParameter(this.rightEdge, this.bottomRightEdge, 1.125f, true);
        this.bottomLeftFillet = new FilletParameter(this.leftEdge, this.bottomLeftEdge, 1.125f, true);
        this.leftArcFillet = new FilletParameter(this.bottomLeftEdge, this.bottomArc, 1.125f, point3f4, true, false, true);
        this.rightArcFillet = new FilletParameter(this.bottomRightEdge, this.bottomArc, 1.125f, point3f5, true, false, true);
        this.bottomArc.setClockwisePath(false);
        this.topLeftFillet.setClockwisePath(true);
        this.topRightFillet.setClockwisePath(true);
        this.bottomRightFillet.setClockwisePath(true);
        this.rightArcFillet.setClockwisePath(true);
        this.leftArcFillet.setClockwisePath(true);
        this.bottomLeftFillet.setClockwisePath(true);
        final Point3f e = new Point3f(point3f2);
        final Point3f key = new Point3f(n - attributeValueAsFloat / 2.0f - 4.5f, attributeValueAsFloat2, 0.0f);
        if (mirrored) {
            this.mirrorParameters(rayParameter);
            this.mirrorPoints(rayParameter, e, key);
        }
        this.wireDipRefPoints.clear();
        this.wireDipRefPoints.add(e);
        this.snapPointIndexMap.clear();
        this.wireDipSnapPoints.clear();
        this.snapPointIndexMap.put(key, 0);
        this.updateSnappedWireDips(key, 0);
        this.addWireDipSnapPoint(key);
        this.updateDipLocations();
        this.allParameters.clear();
        this.plotNodes.clear();
        if (attributeValueAsBoolean) {
            this.allParameters.addAll(this.getParametersForCornerWireDip(this.topEdge, this.rightEdge, !mirrored, !mirrored, mirrored));
        }
        else {
            this.allParameters.add((Parameter2D)this.topRightFillet);
        }
        this.allParameters.addAll(this.getParametersForLine(this.rightEdge));
        this.allParameters.add((Parameter2D)this.bottomRightFillet);
        this.allParameters.add((Parameter2D)this.bottomRightEdge);
        this.allParameters.add((Parameter2D)this.rightArcFillet);
        this.allParameters.add((Parameter2D)this.bottomArc);
        this.allParameters.add((Parameter2D)this.leftArcFillet);
        this.allParameters.add((Parameter2D)this.bottomLeftEdge);
        this.allParameters.add((Parameter2D)this.bottomLeftFillet);
        this.allParameters.add((Parameter2D)this.leftEdge);
        this.allParameters.add((Parameter2D)this.topLeftFillet);
        this.allParameters.addAll(this.getParametersForLine(this.topEdge));
        this.updateBRep(true, true);
        if (mirrored) {
            Collections.reverse(this.shape);
        }
        this.width1Anchor = ICDParametricWorksurface.pointAt(point3f2, new Vector3f(0.0f, -1.0f, 0.0f), point3f3.distance(startPoint) / 2.0f);
        this.width2Anchor = ICDParametricWorksurface.pointAt(point3f2, new Vector3f(-1.0f, 0.0f, 0.0f), point3f.distance(point3f5) / 2.0f);
        this.depth1Anchor = ICDParametricWorksurface.pointAt(startPoint, new Vector3f(1.0f, 0.0f, 0.0f), 6.0f);
        this.depth2Anchor = point3f;
        this.width1Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.width2Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.depth1Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.depth2Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.cutoutRefPoint = new Point3f(point3f2);
        this.cutoutXDirection = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, -1.0f, 0.0f);
        if (mirrored) {
            this.mirrorPoints(rayParameter, this.cutoutRefPoint, this.width1Anchor, this.width2Anchor, this.depth1Anchor, this.depth2Anchor);
            this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
            this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
            this.depth2Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        }
    }
    
    protected void mirrorParameters(final RayParameter rayParameter) {
        this.topEdge.mirror(rayParameter);
        this.rightEdge.mirror(rayParameter);
        this.leftEdge.mirror(rayParameter);
        this.bottomRightEdge.mirror(rayParameter);
        this.bottomLeftEdge.mirror(rayParameter);
        this.bottomArc.mirror(rayParameter);
        this.topLeftFillet.mirror(rayParameter);
        this.topRightFillet.mirror(rayParameter);
        this.bottomLeftFillet.mirror(rayParameter);
        this.bottomRightFillet.mirror(rayParameter);
        this.leftArcFillet.mirror(rayParameter);
        this.rightArcFillet.mirror(rayParameter);
    }
    
    @Override
    protected void validateDimensionAttributes() {
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        if (!MathUtilities.isSameFloat(this.oldDepth1, attributeValueAsFloat, 0.001f)) {
            this.modifyAttributeValue("ICD_Parametric_Width2", String.valueOf(attributeValueAsFloat + 11.0f));
        }
        super.validateDimensionAttributes();
        this.modifyAttributeValue("ICD_Parametric_Depth1", String.valueOf(this.getAttributeValueAsFloat("ICD_Parametric_Width2") - 11.0f));
        this.oldDepth1 = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
    }
    
    @Override
    public float getWidth1Min() {
        final float minimumValueFromAttribute = this.getMinimumValueFromAttribute("ICD_Parametric_Width1_Min");
        this.clampAttributeValue("ICD_Parametric_Depth2", this.getDepth2Min(), this.getDepth2Max());
        return Math.max(minimumValueFromAttribute, 23.0f + this.getAttributeValueAsFloat("ICD_Parametric_Depth2") + 2.0f);
    }
    
    @Override
    protected boolean isMirrored() {
        return "Right".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Worksurface_Orientation"));
    }
    
    @Override
    protected boolean shouldMirrorNamedPoints() {
        return !this.isMirrored();
    }
    
    @Override
    public float getLeftDepth() {
        float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        if (this.isMirrored()) {
            attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth2") - 3.8228f;
        }
        return attributeValueAsFloat;
    }
    
    @Override
    public float getRightDepth() {
        float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth2") - 3.8228f;
        if (this.isMirrored()) {
            attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        }
        return attributeValueAsFloat;
    }
    
    @Override
    public String getShapeTag() {
        String s = "WVCAL";
        if ("Right".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Worksurface_Orientation"))) {
            s = "WVCAR";
        }
        return s;
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
        Float n = 0.0f;
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width2");
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
        final Point3f basePoint = new Point3f(0.0f, attributeValueAsFloat - n, 0.0f);
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
        float n2 = attributeValueAsFloat - n;
        if (this.isMirrored()) {
            n2 = -n2;
        }
        final Point3f basePoint = new Point3f(n2, 0.0f, 0.0f);
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
        point3f2.x += 4.0f;
        final Point3f point3f3 = point3f;
        point3f3.y -= this.getYDimension() / 2.0f - 4.0f;
        return this.convertPointToWorldSpace(point3f);
    }
}
