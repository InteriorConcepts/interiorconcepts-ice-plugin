package net.iceedge.catalogs.icd.worksurfaces;

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

public class ICDWVVRParametricWorksurface extends ICDParametricWorksurface implements ICD1Width2Way1DepthGrippable
{
    private LineParameter topEdge;
    private LineParameter leftEdge;
    private LineParameter rightEdge;
    private LineParameter bottomEdge;
    private CircleParameter leftArc;
    private CircleParameter centerArc;
    private CircleParameter rightArc;
    private FilletParameter topLeftFillet;
    private FilletParameter topRightFillet;
    private FilletParameter bottomLeftFillet;
    private FilletParameter bottomRightFillet;
    private ICD1Width2Way1DepthGripset gripSet;
    
    public ICDWVVRParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.bottomEdge = new LineParameter();
        this.leftArc = new CircleParameter();
        this.centerArc = new CircleParameter();
        this.rightArc = new CircleParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.gripSet = null;
        this.createParameters();
        this.gripSet = new ICD1Width2Way1DepthGripset(this);
    }
    
    public ICDWVVRParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.bottomEdge = new LineParameter();
        this.leftArc = new CircleParameter();
        this.centerArc = new CircleParameter();
        this.rightArc = new CircleParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.gripSet = null;
        this.createParameters();
        this.gripSet = new ICD1Width2Way1DepthGripset(this);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVVRParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWVVRParametricWorksurface buildClone(final ICDWVVRParametricWorksurface icdwvvrParametricWorksurface) {
        super.buildClone(icdwvvrParametricWorksurface);
        icdwvvrParametricWorksurface.calculateParameters();
        return icdwvvrParametricWorksurface;
    }
    
    private void createParameters() {
        this.lineParams.add(this.topEdge);
    }
    
    @Override
    protected void calculateParameters() {
        this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor");
        this.shape.clear();
        this.validateDimensionAttributes();
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth");
        final boolean mirrored = this.isMirrored();
        final RayParameter rayParameter = new RayParameter(new Point3f(), new Vector3f(0.0f, 1.0f, 0.0f));
        final float n = 24.805f;
        final float n2 = 3.0f;
        final float n3 = attributeValueAsFloat2 + n2;
        final float n4 = n + 1.0f;
        final float n5 = attributeValueAsFloat - n4;
        final float n6 = n3 / 2.0f;
        final float n7 = (n5 * n5 + n2 * n2) / (2.0f * n2);
        final Point3f point3f = new Point3f(0.0f, n3, 0.0f);
        final Point3f point3f2 = new Point3f(attributeValueAsFloat, n3, 0.0f);
        final Point3f endPoint = new Point3f(attributeValueAsFloat, 2.0f, 0.0f);
        final Point3f startPoint = this.leftArc.getRayIntersections(point3f, new Vector3f(0.0f, -1.0f, 0.0f), CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
        this.topEdge.setStartPoint(point3f);
        this.topEdge.setEndPoint(point3f2);
        this.leftEdge.setStartPoint(startPoint);
        this.leftEdge.setEndPoint(point3f);
        this.rightEdge.setStartPoint(point3f2);
        this.rightEdge.setEndPoint(endPoint);
        this.bottomEdge.setStartPoint(new Point3f(n4, 0.0f, 0.0f));
        this.bottomEdge.setEndPoint(new Point3f(n, 0.0f, 0.0f));
        this.leftArc = new CircleParameter(new Point3f(1.43f, -9.44f, 0.0f), 16.32f);
        this.centerArc = new CircleParameter(new Point3f(n, 26.81f, 0.0f), 26.81f);
        this.rightArc = new CircleParameter(new Point3f(n4, n7, 0.0f), n7);
        this.topLeftFillet = new FilletParameter(this.leftEdge, this.topEdge, 1.125f, true);
        this.topRightFillet = new FilletParameter(this.topEdge, this.rightEdge, 1.125f, true);
        this.bottomLeftFillet = new FilletParameter(this.leftEdge, this.leftArc, 1.125f, startPoint, true, false, true);
        this.bottomRightFillet = new FilletParameter(this.rightEdge, this.rightArc, 1.125f, endPoint, true, true, true);
        this.leftArc.setClockwisePath(false);
        this.centerArc.setClockwisePath(true);
        this.rightArc.setClockwisePath(true);
        this.topLeftFillet.setClockwisePath(true);
        this.topRightFillet.setClockwisePath(true);
        this.bottomLeftFillet.setClockwisePath(true);
        this.bottomRightFillet.setClockwisePath(true);
        final Point3f e = new Point3f(point3f);
        final Point3f key = new Point3f(attributeValueAsFloat / 2.0f, n3, 0.0f);
        final Point3f key2 = new Point3f(attributeValueAsFloat / 3.0f, n3, 0.0f);
        final Point3f key3 = new Point3f(2.0f * attributeValueAsFloat / 3.0f, n3, 0.0f);
        if (mirrored) {
            this.mirrorParameters(rayParameter);
            this.mirrorPoints(rayParameter, e, key, key2, key3);
        }
        this.wireDipRefPoints.clear();
        this.wireDipRefPoints.add(e);
        this.snapPointIndexMap.clear();
        this.wireDipSnapPoints.clear();
        if (attributeValueAsFloat < 59.0f) {
            this.snapPointIndexMap.put(key, 0);
            this.updateSnappedWireDips(key, 0);
            this.addWireDipSnapPoint(key);
        }
        else {
            this.snapPointIndexMap.put(key2, 0);
            this.snapPointIndexMap.put(key3, 1);
            this.updateSnappedWireDips(key2, 0);
            this.updateSnappedWireDips(key3, 1);
            this.addWireDipSnapPoint(key2);
            this.addWireDipSnapPoint(key3);
        }
        this.allParameters.clear();
        this.plotNodes.clear();
        this.allParameters.addAll(this.getParametersForLine(this.topEdge));
        this.allParameters.add((Parameter2D)this.topRightFillet);
        this.allParameters.add((Parameter2D)this.rightEdge);
        this.allParameters.add((Parameter2D)this.bottomRightFillet);
        this.allParameters.add((Parameter2D)this.rightArc);
        this.allParameters.add((Parameter2D)this.bottomEdge);
        this.allParameters.add((Parameter2D)this.centerArc);
        this.allParameters.add((Parameter2D)this.leftArc);
        this.allParameters.add((Parameter2D)this.bottomLeftFillet);
        this.allParameters.add((Parameter2D)this.leftEdge);
        this.allParameters.add((Parameter2D)this.topLeftFillet);
        this.updateBRep(true, true);
        if (mirrored) {
            Collections.reverse(this.shape);
        }
        this.width1Anchor = new Point3f(0.0f, n6, 0.0f);
        this.width2Anchor = new Point3f(attributeValueAsFloat, n6, 0.0f);
        this.depth1Anchor = new Point3f(6.0f, n3, 0.0f);
        this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.width2Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.cutoutRefPoint = new Point3f(point3f);
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, -1.0f, 0.0f);
        this.updateDipLocations();
        if (mirrored) {
            this.mirrorPoints(rayParameter, this.cutoutRefPoint, this.width1Anchor, this.width2Anchor, this.depth1Anchor);
            this.width1Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
            this.width2Direction = new Vector3f(1.0f, 0.0f, 0.0f);
            this.cutoutXDirection = new Vector3f(-1.0f, 0.0f, 0.0f);
        }
    }
    
    protected void mirrorParameters(final RayParameter rayParameter) {
        this.topEdge.mirror(rayParameter);
        this.topRightFillet.mirror(rayParameter);
        this.rightEdge.mirror(rayParameter);
        this.bottomRightFillet.mirror(rayParameter);
        this.rightArc.mirror(rayParameter);
        this.bottomEdge.mirror(rayParameter);
        this.centerArc.mirror(rayParameter);
        this.leftArc.mirror(rayParameter);
        this.bottomLeftFillet.mirror(rayParameter);
        this.leftEdge.mirror(rayParameter);
        this.topLeftFillet.mirror(rayParameter);
    }
    
    @Override
    protected boolean isMirrored() {
        return "Left".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Worksurface_Orientation"));
    }
    
    @Override
    public float getLeftDepth() {
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth");
        float n = attributeValueAsFloat - 3.8228f;
        if (this.isMirrored()) {
            n = attributeValueAsFloat;
        }
        return n;
    }
    
    @Override
    public float getRightDepth() {
        float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth");
        if (this.isMirrored()) {
            attributeValueAsFloat -= 3.8228f;
        }
        return attributeValueAsFloat;
    }
    
    @Override
    public String getShapeTag() {
        String s = "WVVRL";
        if ("Right".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Worksurface_Orientation"))) {
            s = "WVVRR";
        }
        return s;
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
        Float n = 0.0f;
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth");
        try {
            n = Float.parseFloat(s);
            n = Math.min(n, this.getDepthMax());
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
        float n2 = attributeValueAsFloat - n;
        if (this.isMirrored()) {
            n2 = -n2;
        }
        final Point3f basePoint = new Point3f(n2, 0.0f, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
    }
    
    @Override
    protected Point3f getCadOutputInsertionPoint(final String s) {
        final Point3f point3f2;
        final Point3f point3f = point3f2 = (Point3f)this.getGeometricCenterPointLocal().clone();
        point3f2.x -= s.length() * 2.0f / 2.0f - 4.0f;
        final Point3f point3f3 = point3f;
        point3f3.y -= this.getYDimension() / 2.0f - 8.0f;
        return this.convertPointToWorldSpace(point3f);
    }
}
