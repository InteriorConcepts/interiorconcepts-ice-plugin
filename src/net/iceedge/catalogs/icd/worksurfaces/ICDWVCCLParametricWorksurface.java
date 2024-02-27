package net.iceedge.catalogs.icd.worksurfaces;

import net.iceedge.icecore.basemodule.baseclasses.grips.AttributeGripPoint;
import java.util.SortedSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import javax.media.j3d.Transform3D;
import java.awt.Graphics2D;
import net.dirtt.icebox.canvas2d.Ice2DNode;
import java.util.ArrayList;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icebox.canvas2d.Ice2DDelegateNode;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.Parameter2D;
import java.util.Collection;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.RayParameter;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.CircleParameter;
import net.dirtt.icebox.canvas2d.Ice2DDirectPaintable;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDCornerParametricWorksurface;

public class ICDWVCCLParametricWorksurface extends ICDCornerParametricWorksurface implements Ice2DDirectPaintable, ICD2Widths2DepthsGrippable
{
    private float offset;
    private float c1Radius;
    private CircleParameter c1;
    private LineParameter l1;
    private LineParameter l2;
    private LineParameter l3;
    private LineParameter l4;
    private ICD2Widths2DepthsGripSet gripSet;
    
    public ICDWVCCLParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.offset = 12.43f;
        this.c1Radius = 16.32f;
        this.c1 = new CircleParameter(new Point3f(), this.c1Radius);
        this.gripSet = null;
        this.createParameters();
        this.gripSet = new ICD2Widths2DepthsGripSet(this);
    }
    
    private void createParameters() {
        this.l1 = new LineParameter();
        this.l2 = new LineParameter();
        this.l3 = new LineParameter();
        this.l4 = new LineParameter();
        this.sideLineParams.add(this.l1);
        this.lineParams.add(this.l2);
        this.lineParams.add(this.l3);
        this.sideLineParams.add(this.l4);
    }
    
    public Object clone() {
        return this.buildClone(new ICDWVCCLParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWVCCLParametricWorksurface buildClone(final ICDWVCCLParametricWorksurface icdwvcclParametricWorksurface) {
        super.buildClone(icdwvcclParametricWorksurface);
        icdwvcclParametricWorksurface.calculateParameters();
        return icdwvcclParametricWorksurface;
    }
    
    protected void calculateParameters() {
        this.validateDimensionAttributes();
        this.shape.clear();
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width1");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Width2");
        final float attributeValueAsFloat3 = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        final float attributeValueAsFloat4 = this.getAttributeValueAsFloat("ICD_Parametric_Depth2");
        final boolean booleanValue = (boolean)this.getAttributeValueAsBoolean("ICD_Corner_WireDip", false);
        final boolean mirrored = this.isMirrored();
        final Point3f point3f = new Point3f();
        final RayParameter rayParameter = new RayParameter(new Point3f(), new Vector3f(0.0f, 1.0f, 0.0f));
        final Point3f e = new Point3f(-(attributeValueAsFloat - attributeValueAsFloat4 - this.offset), this.offset, 0.0f);
        final Point3f point = ICDParametricWorksurface.pointAt(e, new Vector3f(0.0f, 1.0f, 0.0f), attributeValueAsFloat3);
        final Point3f point2 = ICDParametricWorksurface.pointAt(point, new Vector3f(1.0f, 0.0f, 0.0f), attributeValueAsFloat);
        final Point3f point3 = ICDParametricWorksurface.pointAt(point2, new Vector3f(0.0f, -1.0f, 0.0f), attributeValueAsFloat2);
        final Point3f point4 = ICDParametricWorksurface.pointAt(point3, new Vector3f(-1.0f, 0.0f, 0.0f), attributeValueAsFloat4);
        final Point3f point3f2 = this.c1.getRayIntersections(point4, new Vector3f(0.0f, 1.0f, 0.0f), CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
        final Point3f point3f3 = this.c1.getRayIntersections(e, new Vector3f(1.0f, 0.0f, 0.0f), CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
        this.l1.setStartPoint(e);
        this.l1.setEndPoint(point);
        this.l2.setStartPoint(point);
        this.l2.setEndPoint(point2);
        this.l3.setStartPoint(point2);
        this.l3.setEndPoint(point3);
        this.l4.setStartPoint(point3);
        this.l4.setEndPoint(point4);
        final LineParameter lineParameter = new LineParameter();
        final LineParameter lineParameter2 = new LineParameter();
        lineParameter.setStartPoint(point4);
        lineParameter.setEndPoint(point3f2);
        lineParameter2.setStartPoint(point3f3);
        lineParameter2.setEndPoint(e);
        final FilletParameter e2 = new FilletParameter(this.l1, this.l2, 1.125f, true);
        final FilletParameter e3 = new FilletParameter(this.l2, this.l3, 1.125f, !booleanValue);
        final FilletParameter e4 = new FilletParameter(this.l3, this.l4, 1.125f, true);
        final FilletParameter e5 = new FilletParameter(this.l4, lineParameter, 1.125f, true);
        final FilletParameter e6 = new FilletParameter(lineParameter2, this.l1, 1.125f, true);
        final FilletParameter e7 = new FilletParameter(lineParameter2, this.c1, 1.125f, point3f3, true, false, true);
        final FilletParameter e8 = new FilletParameter(lineParameter, this.c1, 1.125f, point3f2, true, false, true);
        this.c1.setClockwisePath(false);
        e2.setClockwisePath(true);
        e3.setClockwisePath(true);
        e4.setClockwisePath(true);
        e5.setClockwisePath(true);
        e6.setClockwisePath(true);
        e7.setClockwisePath(true);
        e8.setClockwisePath(true);
        if (mirrored) {
            this.l1.mirror(rayParameter);
            this.l2.mirror(rayParameter);
            this.l3.mirror(rayParameter);
            this.l4.mirror(rayParameter);
            lineParameter.mirror(rayParameter);
            lineParameter2.mirror(rayParameter);
            this.c1.mirror(rayParameter);
            e2.mirror(rayParameter);
            e3.mirror(rayParameter);
            e4.mirror(rayParameter);
            e6.mirror(rayParameter);
            e5.mirror(rayParameter);
            e7.mirror(rayParameter);
            e8.mirror(rayParameter);
        }
        final Point3f e9 = new Point3f(point2);
        final Point3f point5 = ICDParametricWorksurface.pointAt(point2, new Vector3f(-1.0f, 0.0f, 0.0f), attributeValueAsFloat / 2.0f + 4.5f);
        final Point3f point6 = ICDParametricWorksurface.pointAt(point2, new Vector3f(0.0f, -1.0f, 0.0f), attributeValueAsFloat2 / 2.0f + 4.5f);
        if (mirrored) {
            this.mirrorPoints(rayParameter, new Point3f[] { e9, point5, point6 });
        }
        this.wireDipRefPoints.clear();
        this.wireDipRefPoints.add(e9);
        this.snapPointIndexMap.clear();
        this.snapPointIndexMap.put(point5, 0);
        this.snapPointIndexMap.put(point6, 1);
        this.updateSnappedWireDips(point5, 0);
        this.updateSnappedWireDips(point6, 1);
        this.wireDipSnapPoints.clear();
        this.addWireDipSnapPoint(point5);
        this.addWireDipSnapPoint(point6);
        this.updateDipLocations();
        this.cutoutRefPoint = new Point3f(point2);
        this.cutoutXDirection = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, -1.0f, 0.0f);
        if (mirrored) {
            this.mirrorPoints(rayParameter, new Point3f[] { this.cutoutRefPoint });
            this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        }
        this.cutoutSnapPoints.clear();
        this.allParameters.clear();
        this.plotNodes.clear();
        if (booleanValue) {
            this.allParameters.addAll(this.getParametersForCornerWireDip(this.l2, this.l3, !mirrored, !mirrored, mirrored));
        }
        else {
            this.allParameters.add((Parameter2D)e3);
        }
        this.allParameters.addAll(this.getParametersForLine(this.l3));
        this.allParameters.add((Parameter2D)e4);
        this.allParameters.addAll(this.getParametersForLine(this.l4));
        this.allParameters.add((Parameter2D)e5);
        this.allParameters.addAll(this.getParametersForLine(lineParameter));
        this.allParameters.add((Parameter2D)e8);
        this.allParameters.add((Parameter2D)this.c1);
        this.allParameters.add((Parameter2D)e7);
        this.allParameters.addAll(this.getParametersForLine(lineParameter2));
        this.allParameters.add((Parameter2D)e6);
        this.allParameters.addAll(this.getParametersForLine(this.l1));
        this.allParameters.add((Parameter2D)e2);
        this.allParameters.addAll(this.getParametersForLine(this.l2));
        this.updateBRep(true, true);
        this.shapePoints.clear();
        this.shapePoints.add(e);
        this.shapePoints.add(point);
        this.shapePoints.add(point2);
        this.shapePoints.add(point3);
        this.shapePoints.add(point4);
        this.shapePoints.add(point3f2);
        this.shapePoints.add(point3f3);
        this.width1Anchor = ICDParametricWorksurface.pointAt(point2, new Vector3f(0.0f, -1.0f, 0.0f), e.distance(point) / 2.0f);
        this.width2Anchor = ICDParametricWorksurface.pointAt(point2, new Vector3f(-1.0f, 0.0f, 0.0f), point4.distance(point3) / 2.0f);
        this.depth1Anchor = ICDParametricWorksurface.pointAt(point, new Vector3f(1.0f, 0.0f, 0.0f), point3f3.distance(e) / 2.0f);
        this.depth2Anchor = ICDParametricWorksurface.pointAt(point3, new Vector3f(0.0f, 1.0f, 0.0f), point4.distance(point3f2) / 2.0f);
        this.width1Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.width2Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.depth1Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.depth2Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        if (mirrored) {
            this.mirrorPoints(rayParameter, new Point3f[] { this.width1Anchor, this.width2Anchor, this.depth1Anchor, this.depth2Anchor });
            this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
            this.depth2Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        }
    }
    
    protected boolean isMirrored() {
        return "Right".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Worksurface_Orientation"));
    }
    
    protected boolean shouldMirrorNamedPoints() {
        return !this.isMirrored();
    }
    
    public float getLeftDepth() {
        if (this.isMirrored()) {
            return this.getAttributeValueAsFloat("ICD_Parametric_Depth2");
        }
        return super.getLeftDepth();
    }
    
    public float getRightDepth() {
        if (this.isMirrored()) {
            return this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        }
        return super.getRightDepth();
    }
    
    public float getWidth1Min() {
        final float minimumValueFromAttribute = this.getMinimumValueFromAttribute("ICD_Parametric_Width1_Min");
        this.clampAttributeValue("ICD_Parametric_Depth2", this.getDepth2Min(), this.getDepth2Max());
        return Math.max(minimumValueFromAttribute, 23.0f + this.getAttributeValueAsFloat("ICD_Parametric_Depth2") + 2.0f);
    }
    
    public float getWidth2Min() {
        final float minimumValueFromAttribute = this.getMinimumValueFromAttribute("ICD_Parametric_Width2_Min");
        this.clampAttributeValue("ICD_Parametric_Depth1", this.getDepth1Min(), this.getDepth1Max());
        return Math.max(minimumValueFromAttribute, 23.0f + this.getAttributeValueAsFloat("ICD_Parametric_Depth1") + 2.0f);
    }
    
    public void draw2D(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        if (this.isDirty(n) && ICDWVCCLParametricWorksurface.DRAW2D_DEBUG) {
            if (this.delegate2DNode != null && this.delegate2DNode.getParent() != null) {
                this.delegate2DNode.removeFromParent();
            }
            this.directPaintables.clear();
            this.directPaintables.add((Ice2DDirectPaintable)this.c1);
            this.directPaintables.add((Ice2DDirectPaintable)this);
            ice2DContainer.add((Ice2DNode)(this.delegate2DNode = new Ice2DDelegateNode((String)null, (TransformableEntity)this, this.getEntWorldSpaceMatrix(), (ArrayList)this.directPaintables)));
        }
        super.draw2D(n, ice2DContainer, solutionSetting);
    }
    
    public void paint(final Graphics2D graphics2D, final Transform3D transform3D, final Ice2DPaintableNode ice2DPaintableNode) {
        super.paint(graphics2D, transform3D, ice2DPaintableNode);
    }
    
    public String getShapeTag() {
        String s = "WVCCL";
        if ("Right".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Worksurface_Orientation"))) {
            s = "WVCCR";
        }
        return s;
    }
    
    public void setupGripPainters() {
        if (this.gripSet == null) {
            this.gripSet = new ICD2Widths2DepthsGripSet(this);
        }
        this.gripSet.setupGripPainters();
    }
    
    public SortedSet<AttributeGripPoint> getAttributeMap(final BasicAttributeGrip basicAttributeGrip) {
        return this.gripSet.getAttributeMap(basicAttributeGrip);
    }
    
    public void updateGrips(final BasicAttributeGrip basicAttributeGrip) {
        this.gripSet.updateGrips(basicAttributeGrip);
    }
    
    protected void selectGrips(final boolean b) {
        this.gripSet.selectGrips(b);
        super.selectGrips(b);
    }
    
    protected void deselectGrips() {
        this.gripSet.deselectGrips();
        super.deselectGrips();
    }
    
    protected void destroyGrips() {
        this.gripSet.destroyGrips();
        super.destroyGrips();
    }
    
    protected void drawGrips(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        super.drawGrips(n, ice2DContainer, solutionSetting);
        this.gripSet.drawGrips(n, ice2DContainer, solutionSetting);
    }
    
    public void width1GripChanged(final String s) {
    }
    
    public void width2GripChanged(final String s) {
    }
    
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
    
    public TransformableEntity getEntity() {
        return (TransformableEntity)this;
    }
    
    public boolean shouldFlipEdge3D() {
        return !this.isMirrored();
    }
    
    public boolean shouldFlipCutouts3D() {
        return !this.isMirrored();
    }
    
    protected Point3f getCadOutputInsertionPoint(final String s) {
        final Point3f point3f2;
        final Point3f point3f = point3f2 = (Point3f)this.getGeometricCenterPointLocal().clone();
        point3f2.x -= this.getXDimension() / 2.0f - 4.0f;
        final Point3f point3f3 = point3f;
        point3f3.y += this.getYDimension() / 2.0f - 4.0f;
        return this.convertPointToWorldSpace(point3f);
    }
}
