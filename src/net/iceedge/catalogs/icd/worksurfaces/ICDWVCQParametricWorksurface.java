// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.snapping.simple.SimpleSnapper;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.icecore.basemodule.baseclasses.grips.AttributeGripPoint;
import java.util.SortedSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;
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
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;

public class ICDWVCQParametricWorksurface extends ICDParametricWorksurface implements ICD1Width1DepthGrippable
{
    private LineParameter topEdge;
    private LineParameter rightEdge;
    private LineParameter bottomRightEdge;
    private LineParameter bottomLeftEdge;
    private CircleParameter topArc;
    private CircleParameter leftArc;
    private CircleParameter bottomCircle;
    private FilletParameter topLeftFillet;
    private FilletParameter topMidFillet;
    private FilletParameter topRightFillet;
    private FilletParameter bottomRightFillet;
    private FilletParameter rightArcFillet;
    private FilletParameter leftArcFillet;
    private FilletParameter bottomLeftFillet;
    private ICD1Width1DepthGripSet gripSet;
    
    public ICDWVCQParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.bottomRightEdge = new LineParameter();
        this.bottomLeftEdge = new LineParameter();
        this.topArc = new CircleParameter();
        this.leftArc = new CircleParameter();
        this.bottomCircle = new CircleParameter(new Point3f(0.0f, -1.43f, 0.0f), 16.32f);
        this.topLeftFillet = new FilletParameter();
        this.topMidFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.rightArcFillet = new FilletParameter();
        this.leftArcFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.gripSet = null;
        this.createParameters();
        this.gripSet = new ICD1Width1DepthGripSet(this);
    }
    
    public ICDWVCQParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.bottomRightEdge = new LineParameter();
        this.bottomLeftEdge = new LineParameter();
        this.topArc = new CircleParameter();
        this.leftArc = new CircleParameter();
        this.bottomCircle = new CircleParameter(new Point3f(0.0f, -1.43f, 0.0f), 16.32f);
        this.topLeftFillet = new FilletParameter();
        this.topMidFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.rightArcFillet = new FilletParameter();
        this.leftArcFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.gripSet = null;
        this.createParameters();
        this.gripSet = new ICD1Width1DepthGripSet(this);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVCQParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWVCQParametricWorksurface buildClone(final ICDWVCQParametricWorksurface icdwvcqParametricWorksurface) {
        super.buildClone(icdwvcqParametricWorksurface);
        icdwvcqParametricWorksurface.calculateParameters();
        return icdwvcqParametricWorksurface;
    }
    
    public void calculateNamedPoints() {
        super.calculateNamedPoints();
        final Point3f geometricCenterPointLocal = this.getGeometricCenterPointLocal();
        final float n = this.getXDimension() / 2.0f;
        final float n2 = this.getYDimension() / 2.0f;
        final float n3 = this.getZDimension() / 2.0f;
        final float leftDepth = this.getLeftDepth();
        final float rightDepth = this.getRightDepth();
        final float n4 = 3.0f;
        final Point3f namedPointLocal = this.getNamedPointLocal("Top_Left_Snap_Corner");
        final Point3f namedPointLocal2 = this.getNamedPointLocal("Top_Right_Snap_Corner");
        if (!this.isMirrored()) {
            this.getNamedPointLocal("WSBL").set(geometricCenterPointLocal.x + n - this.chaseDepth, geometricCenterPointLocal.y - n2, geometricCenterPointLocal.z - n3);
            this.getNamedPointLocal("WSBR").set(geometricCenterPointLocal.x + n - this.chaseDepth, geometricCenterPointLocal.y + n2 - n4, geometricCenterPointLocal.z - n3);
            this.getNamedPointLocal("WSFL").set(geometricCenterPointLocal.x + n - rightDepth, geometricCenterPointLocal.y - n2, geometricCenterPointLocal.z - n3);
            this.getNamedPointLocal("WSFR").set(geometricCenterPointLocal.x + n - rightDepth, geometricCenterPointLocal.y + n2 - n4, geometricCenterPointLocal.z - n3);
            namedPointLocal.set(geometricCenterPointLocal.x + n, geometricCenterPointLocal.y + n2 - n4, geometricCenterPointLocal.z);
            namedPointLocal2.set(geometricCenterPointLocal.x + n, geometricCenterPointLocal.y - n2, geometricCenterPointLocal.z);
        }
        else {
            this.getNamedPointLocal("WSBL").set(geometricCenterPointLocal.x - n + this.chaseDepth, geometricCenterPointLocal.y - n2, geometricCenterPointLocal.z - n3);
            this.getNamedPointLocal("WSBR").set(geometricCenterPointLocal.x - n + this.chaseDepth, geometricCenterPointLocal.y + n2 - n4, geometricCenterPointLocal.z - n3);
            this.getNamedPointLocal("WSFL").set(geometricCenterPointLocal.x - n + leftDepth, geometricCenterPointLocal.y - n2, geometricCenterPointLocal.z - n3);
            this.getNamedPointLocal("WSFR").set(geometricCenterPointLocal.x - n + leftDepth, geometricCenterPointLocal.y + n2 - n4, geometricCenterPointLocal.z - n3);
            namedPointLocal.set(geometricCenterPointLocal.x - n, geometricCenterPointLocal.y + n2 - n4, geometricCenterPointLocal.z);
            namedPointLocal2.set(geometricCenterPointLocal.x - n, geometricCenterPointLocal.y - n2, geometricCenterPointLocal.z);
        }
        final Point3f namedPointLocal3 = this.getNamedPointLocal("D_Column");
        if (namedPointLocal3 != null) {
            if (!this.isMirrored()) {
                namedPointLocal3.set(-this.getXDimension() * 4.0f / 6.0f + leftDepth, 23.5f, 0.0f);
            }
            else {
                namedPointLocal3.set(this.getXDimension() * 4.0f / 6.0f - rightDepth, 23.5f, 0.0f);
            }
        }
        final Point3f namedPointLocal4 = this.getNamedPointLocal("Bottom_Left_3D");
        if (namedPointLocal4 != null) {
            this.getNamedPointLocal("Bottom_Left_3D").set(namedPointLocal4.x, namedPointLocal4.y + 11.0f, namedPointLocal4.z);
        }
    }
    
    @Override
    protected void calculateNamedRotations() {
        super.calculateNamedRotations();
        this.getNamedRotationLocal("Left_Support_Angle").set(0.0f, 0.0f, -3.1415927f);
        this.getNamedRotationLocal("Right_Support_Angle").set(0.0f, 0.0f, -3.1415927f);
        if (this.shouldMirrorNamedPoints()) {
            this.getNamedRotationLocal("Left_Support_Angle").set(0.0f, 0.0f, 0.0f);
            this.getNamedRotationLocal("Right_Support_Angle").set(0.0f, 0.0f, 0.0f);
        }
    }
    
    private void createParameters() {
        this.lineParams.add(this.rightEdge);
    }
    
    @Override
    protected void calculateParameters() {
        this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor");
        this.shape.clear();
        this.validateDimensionAttributes();
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width");
        final float n = 36.0f;
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        final boolean attributeValueAsBoolean = this.getAttributeValueAsBoolean("ICD_Corner_WireDip", false);
        final boolean mirrored = this.isMirrored();
        final RayParameter rayParameter = new RayParameter(new Point3f(), new Vector3f(0.0f, 1.0f, 0.0f));
        final float n2 = attributeValueAsFloat2 + 12.43f;
        final float n3 = attributeValueAsFloat - 21.0f - 3.0f;
        final Point3f point3f = new Point3f(n2, n, 0.0f);
        final Point3f point3f2 = new Point3f(n2, 0.0f, 0.0f);
        final Point3f point3f3 = new Point3f(n2 - attributeValueAsFloat + 3.0f, n, 0.0f);
        final Point3f endPoint = new Point3f(n2 - attributeValueAsFloat + 3.0f, 11.0f, 0.0f);
        final Point3f startPoint = new Point3f(n2 - 21.0f, n, 0.0f);
        final Point3f point3f4 = new Point3f(n2 - 21.0f - n3 / 2.0f, n + 3.0f, 0.0f);
        final Point3f endPoint2 = this.bottomCircle.getRayIntersections(point3f2, new Vector3f(-1.0f, 0.0f, 0.0f), CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
        final Point3f startPoint2 = this.bottomCircle.getRayIntersections(endPoint, new Vector3f(1.0f, 0.0f, 0.0f), CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
        this.topEdge.setStartPoint(startPoint);
        this.topEdge.setEndPoint(point3f);
        this.rightEdge.setStartPoint(point3f);
        this.rightEdge.setEndPoint(point3f2);
        this.bottomRightEdge.setStartPoint(point3f2);
        this.bottomRightEdge.setEndPoint(endPoint2);
        this.bottomLeftEdge.setStartPoint(startPoint2);
        this.bottomLeftEdge.setEndPoint(endPoint);
        this.topArc = new CircleParameter(point3f3, point3f4, startPoint);
        this.leftArc = new CircleParameter(endPoint, new Point3f(n2 - attributeValueAsFloat, n - 12.0f, 0.0f), point3f3);
        this.topLeftFillet = new FilletParameter(this.leftArc, this.topArc, 1.125f, point3f3, true, true);
        this.topMidFillet = new FilletParameter(this.topEdge, this.topArc, 12.0f, startPoint, false, false, true);
        this.topRightFillet = new FilletParameter(this.topEdge, this.rightEdge, 1.125f, !attributeValueAsBoolean);
        this.bottomRightFillet = new FilletParameter(this.rightEdge, this.bottomRightEdge, 1.125f, true);
        this.rightArcFillet = new FilletParameter(this.bottomRightEdge, this.bottomCircle, 1.125f, endPoint2, true, false, true);
        this.leftArcFillet = new FilletParameter(this.bottomLeftEdge, this.bottomCircle, 1.125f, startPoint2, true, false, true);
        this.bottomLeftFillet = new FilletParameter(this.bottomLeftEdge, this.leftArc, 1.125f, endPoint, true, true, true);
        this.topArc.setClockwisePath(true);
        this.leftArc.setClockwisePath(true);
        this.bottomCircle.setClockwisePath(false);
        this.topLeftFillet.setClockwisePath(true);
        this.topMidFillet.setClockwisePath(false);
        this.topRightFillet.setClockwisePath(true);
        this.bottomRightFillet.setClockwisePath(true);
        this.rightArcFillet.setClockwisePath(true);
        this.leftArcFillet.setClockwisePath(true);
        this.bottomLeftFillet.setClockwisePath(true);
        if (mirrored) {
            this.mirrorParameters(rayParameter);
        }
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
        this.allParameters.add((Parameter2D)this.bottomCircle);
        this.allParameters.add((Parameter2D)this.leftArcFillet);
        this.allParameters.add((Parameter2D)this.bottomLeftEdge);
        this.allParameters.add((Parameter2D)this.bottomLeftFillet);
        this.allParameters.add((Parameter2D)this.leftArc);
        this.allParameters.add((Parameter2D)this.topLeftFillet);
        this.allParameters.add((Parameter2D)this.topArc);
        this.allParameters.add((Parameter2D)this.topMidFillet);
        this.allParameters.add((Parameter2D)this.topEdge);
        this.updateBRep(true, true);
        if (mirrored) {
            Collections.reverse(this.shape);
        }
        this.width1Anchor = new Point3f(n2, n - 12.5f, 0.0f);
        this.depth1Anchor = new Point3f(point3f2);
        this.width1Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.cutoutRefPoint = new Point3f(point3f);
        this.cutoutXDirection = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, -1.0f, 0.0f);
        final Point3f e = new Point3f(point3f);
        if (mirrored) {
            this.mirrorPoints(rayParameter, this.width1Anchor, this.depth1Anchor, this.cutoutRefPoint, e);
            this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
            this.depth1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
            this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        }
        this.wireDipRefPoints.clear();
        this.wireDipRefPoints.add(e);
    }
    
    protected void mirrorParameters(final RayParameter rayParameter) {
        this.rightEdge.mirror(rayParameter);
        this.bottomRightFillet.mirror(rayParameter);
        this.bottomRightEdge.mirror(rayParameter);
        this.rightArcFillet.mirror(rayParameter);
        this.bottomCircle.mirror(rayParameter);
        this.leftArcFillet.mirror(rayParameter);
        this.bottomLeftEdge.mirror(rayParameter);
        this.bottomLeftFillet.mirror(rayParameter);
        this.leftArc.mirror(rayParameter);
        this.topLeftFillet.mirror(rayParameter);
        this.topArc.mirror(rayParameter);
        this.topMidFillet.mirror(rayParameter);
        this.topEdge.mirror(rayParameter);
        this.topRightFillet.mirror(rayParameter);
    }
    
    @Override
    protected boolean isMirrored() {
        return "Right".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Worksurface_Orientation"));
    }
    
    @Override
    public float getLeftDepth() {
        if (this.isMirrored()) {
            return this.getAttributeValueAsFloat("ICD_Parametric_Depth1") - 3.8228f;
        }
        return super.getLeftDepth();
    }
    
    @Override
    public float getRightDepth() {
        if (!this.isMirrored()) {
            return this.getAttributeValueAsFloat("ICD_Parametric_Depth1") - 3.8228f;
        }
        return this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
    }
    
    @Override
    public float getWidthMin() {
        final float minimumValueFromAttribute = this.getMinimumValueFromAttribute("ICD_Parametric_Width_Min");
        this.clampAttributeValue("ICD_Parametric_Depth1", this.getDepth1Min(), this.getDepth1Max());
        return Math.max(minimumValueFromAttribute, 23.0f + this.getAttributeValueAsFloat("ICD_Parametric_Depth1") + 3.0f + 2.0f);
    }
    
    @Override
    public String getShapeTag() {
        String s = "WVCQL";
        if ("Right".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Worksurface_Orientation"))) {
            s = "WVCQR";
        }
        return s;
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
        Float n = 0.0f;
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
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
    public float getSnapRotation() {
        if (this.isMirrored()) {
            return 1.5707964f;
        }
        return -1.5707964f;
    }
    
    @Override
    protected Point3f getCadOutputInsertionPoint(final String s) {
        final Point3f point3f2;
        final Point3f point3f = point3f2 = (Point3f)this.getGeometricCenterPointLocal().clone();
        point3f2.x -= this.getXDimension() / 2.0f - 4.0f;
        return this.convertPointToWorldSpace(point3f);
    }
    
    @Override
    protected SimpleSnapper addSimpleSnappablesThatDoNotFilterOccupiedTargets(final boolean b, SimpleSnapper addSimpleSnappablesThatDoNotFilterOccupiedTargets) {
        addSimpleSnappablesThatDoNotFilterOccupiedTargets = super.addSimpleSnappablesThatDoNotFilterOccupiedTargets(true, addSimpleSnappablesThatDoNotFilterOccupiedTargets);
        if (this.isMirrored()) {
            final Point3f namedPointLocal = this.getNamedPointLocal("Top_Left_Snap_Corner");
            final float snapRotation = this.getSnapRotation();
            addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("BRL", namedPointLocal, Float.valueOf(snapRotation), 20.0f, true);
            addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("BRC", namedPointLocal, Float.valueOf(snapRotation), 20.0f, true);
            addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("BRR", namedPointLocal, Float.valueOf(snapRotation), 20.0f, true);
            final float f = snapRotation + 3.1415927f;
            addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TLL", namedPointLocal, Float.valueOf(f), 20.0f, true);
            addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TLC", namedPointLocal, Float.valueOf(f), 20.0f, true);
            addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TLR", namedPointLocal, Float.valueOf(f), 20.0f, true);
        }
        return addSimpleSnappablesThatDoNotFilterOccupiedTargets;
    }
}
