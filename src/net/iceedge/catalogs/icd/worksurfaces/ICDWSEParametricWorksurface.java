package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.dirtt.icelib.main.attributes.Attribute;
import java.util.TreeSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.AttributeGripPoint;
import java.util.SortedSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.GripListener;
import net.dirtt.icelib.main.TransformableEntity;
import net.iceedge.icecore.basemodule.baseclasses.grips.RelativeAttributeGrip;
import net.dirtt.utilities.MathUtilities;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.Parameter2D;
import java.util.Collection;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.RayParameter;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;

public class ICDWSEParametricWorksurface extends ICDParametricWorksurface
{
    private LineParameter topEdge;
    private LineParameter rightEdge;
    private LineParameter leftEdge;
    private LineParameter bottomLeftEdge;
    private LineParameter bottomRightEdge;
    private LineParameter diagonalEdge;
    private FilletParameter topLeftFillet;
    private FilletParameter topRightFillet;
    private FilletParameter bottomRightFillet;
    private FilletParameter bottomLeftFillet;
    private FilletParameter lowerDiagonalFillet;
    private FilletParameter upperDiagonalFillet;
    protected BasicAttributeGrip width1Grip;
    protected BasicAttributeGrip width2Grip;
    protected BasicAttributeGrip depth1Grip;
    protected BasicAttributeGrip depth2Grip;
    private float oldDepth2;
    
    public ICDWSEParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.bottomLeftEdge = new LineParameter();
        this.bottomRightEdge = new LineParameter();
        this.diagonalEdge = new LineParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.lowerDiagonalFillet = new FilletParameter();
        this.upperDiagonalFillet = new FilletParameter();
        this.createParameters();
    }
    
    public ICDWSEParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.topEdge = new LineParameter();
        this.rightEdge = new LineParameter();
        this.leftEdge = new LineParameter();
        this.bottomLeftEdge = new LineParameter();
        this.bottomRightEdge = new LineParameter();
        this.diagonalEdge = new LineParameter();
        this.topLeftFillet = new FilletParameter();
        this.topRightFillet = new FilletParameter();
        this.bottomRightFillet = new FilletParameter();
        this.bottomLeftFillet = new FilletParameter();
        this.lowerDiagonalFillet = new FilletParameter();
        this.upperDiagonalFillet = new FilletParameter();
        this.createParameters();
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWSEParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWSEParametricWorksurface buildClone(final ICDWSEParametricWorksurface icdwseParametricWorksurface) {
        super.buildClone(icdwseParametricWorksurface);
        icdwseParametricWorksurface.calculateParameters();
        return icdwseParametricWorksurface;
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
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        final boolean attributeValueAsBoolean = this.getAttributeValueAsBoolean("ICD_Corner_WireDip", false);
        final boolean mirrored = this.isMirrored();
        final RayParameter rayParameter = new RayParameter(new Point3f(), new Vector3f(0.0f, 1.0f, 0.0f));
        final Point3f point3f = new Point3f(-attributeValueAsFloat / 2.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f point3f2 = new Point3f(attributeValueAsFloat / 2.0f, attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f startPoint = new Point3f(-attributeValueAsFloat / 2.0f, -attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f endPoint = new Point3f(attributeValueAsFloat / 2.0f, -(attributeValueAsFloat2 / 2.0f - 6.0f), 0.0f);
        final Point3f point3f3 = new Point3f(startPoint.x + 17.0f, -attributeValueAsFloat2 / 2.0f, 0.0f);
        final Point3f point3f4 = new Point3f(point3f3.x + 2.48f, -(attributeValueAsFloat2 / 2.0f - 6.0f), 0.0f);
        this.topEdge.setStartPoint(point3f);
        this.topEdge.setEndPoint(point3f2);
        this.rightEdge.setStartPoint(point3f2);
        this.rightEdge.setEndPoint(endPoint);
        this.bottomRightEdge = new LineParameter(endPoint, point3f4);
        this.diagonalEdge = new LineParameter(point3f4, point3f3);
        this.bottomLeftEdge = new LineParameter(point3f3, startPoint);
        this.leftEdge.setStartPoint(startPoint);
        this.leftEdge.setEndPoint(point3f);
        this.topLeftFillet = new FilletParameter(this.leftEdge, this.topEdge, 1.125f, !attributeValueAsBoolean);
        this.topRightFillet = new FilletParameter(this.topEdge, this.rightEdge, 1.125f, true);
        this.bottomRightFillet = new FilletParameter(this.rightEdge, this.bottomRightEdge, 1.125f, true);
        this.upperDiagonalFillet = new FilletParameter(this.bottomRightEdge, this.diagonalEdge, 1.125f, true);
        this.lowerDiagonalFillet = new FilletParameter(this.diagonalEdge, this.bottomLeftEdge, 1.125f, true);
        this.bottomLeftFillet = new FilletParameter(this.bottomLeftEdge, this.leftEdge, 1.125f, true);
        this.topLeftFillet.setClockwisePath(true);
        this.topRightFillet.setClockwisePath(true);
        this.bottomRightFillet.setClockwisePath(true);
        this.upperDiagonalFillet.setClockwisePath(false);
        this.lowerDiagonalFillet.setClockwisePath(true);
        this.bottomLeftFillet.setClockwisePath(true);
        final Point3f key = new Point3f(point3f2.x - 2.5f, point3f2.y - 2.5f, 0.0f);
        final Point3f key2 = new Point3f(point3f.x + 8.5f, point3f.y - 2.5f, 0.0f);
        this.cutoutRefPoint = new Point3f(point3f);
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, -1.0f, 0.0f);
        if (mirrored) {
            this.mirrorParameters(rayParameter);
            this.mirrorPoints(rayParameter, key, key2, this.cutoutRefPoint);
            this.cutoutXDirection = new Vector3f(-1.0f, 0.0f, 0.0f);
        }
        this.cutoutSnapPointIndexMap.clear();
        this.cutoutSnapPointIndexMap.put(key2, 0);
        this.cutoutSnapPointIndexMap.put(key, 1);
        this.updateSnappedCutouts(key2, 0);
        this.updateSnappedCutouts(key, 1);
        this.cutoutSnapPoints.clear();
        this.addCutoutSnapPoint(key2);
        this.addCutoutSnapPoint(key);
        this.allParameters.clear();
        this.plotNodes.clear();
        if (attributeValueAsBoolean) {
            this.allParameters.addAll(this.getParametersForCornerWireDip(this.leftEdge, this.topEdge, !mirrored, !mirrored, mirrored));
        }
        else {
            this.allParameters.add((Parameter2D)this.topLeftFillet);
        }
        this.allParameters.addAll(this.getParametersForLine(this.topEdge));
        this.allParameters.add((Parameter2D)this.topRightFillet);
        this.allParameters.add((Parameter2D)this.rightEdge);
        this.allParameters.add((Parameter2D)this.bottomRightFillet);
        this.allParameters.add((Parameter2D)this.bottomRightEdge);
        this.allParameters.add((Parameter2D)this.upperDiagonalFillet);
        this.allParameters.add((Parameter2D)this.diagonalEdge);
        this.allParameters.add((Parameter2D)this.lowerDiagonalFillet);
        this.allParameters.add((Parameter2D)this.bottomLeftEdge);
        this.allParameters.add((Parameter2D)this.bottomLeftFillet);
        this.allParameters.add((Parameter2D)this.leftEdge);
        this.updateBRep(true, true);
        this.width1Anchor = ICDParametricWorksurface.pointAt(point3f, new Vector3f(0.0f, -1.0f, 0.0f), attributeValueAsFloat2 / 2.0f);
        this.width2Anchor = ICDParametricWorksurface.pointAt(point3f2, new Vector3f(0.0f, -1.0f, 0.0f), attributeValueAsFloat2 / 2.0f);
        this.depth1Anchor = ICDParametricWorksurface.pointAt(point3f, new Vector3f(1.0f, 0.0f, 0.0f), 8.5f);
        this.depth2Anchor = ICDParametricWorksurface.pointAt(point3f2, new Vector3f(-1.0f, 0.0f, 0.0f), (attributeValueAsFloat - 17.0f) / 2.0f);
        this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.width2Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.depth2Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        final Point3f e = new Point3f(point3f);
        if (mirrored) {
            this.mirrorPoints(rayParameter, this.width1Anchor, this.width2Anchor, this.depth1Anchor, this.depth2Anchor, e);
            this.width1Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
            this.width2Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        }
        this.wireDipRefPoints.clear();
        this.wireDipRefPoints.add(e);
    }
    
    protected void mirrorParameters(final RayParameter rayParameter) {
        this.topEdge.mirror(rayParameter);
        this.topRightFillet.mirror(rayParameter);
        this.rightEdge.mirror(rayParameter);
        this.bottomRightFillet.mirror(rayParameter);
        this.bottomRightEdge.mirror(rayParameter);
        this.upperDiagonalFillet.mirror(rayParameter);
        this.diagonalEdge.mirror(rayParameter);
        this.lowerDiagonalFillet.mirror(rayParameter);
        this.bottomLeftEdge.mirror(rayParameter);
        this.bottomLeftFillet.mirror(rayParameter);
        this.leftEdge.mirror(rayParameter);
        this.topLeftFillet.mirror(rayParameter);
    }
    
    @Override
    protected void validateDimensionAttributes() {
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth2");
        if (!MathUtilities.isSameFloat(this.oldDepth2, attributeValueAsFloat, 0.001f)) {
            this.modifyAttributeValue("ICD_Parametric_Depth1", String.valueOf(attributeValueAsFloat + 6.0f));
        }
        super.validateDimensionAttributes();
        this.modifyAttributeValue("ICD_Parametric_Depth2", String.valueOf(this.getAttributeValueAsFloat("ICD_Parametric_Depth1") - 6.0f));
        this.oldDepth2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth2");
    }
    
    @Override
    protected boolean isMirrored() {
        return "Right".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Worksurface_Orientation"));
    }
    
    @Override
    public float getLeftDepth() {
        if (this.isMirrored()) {
            return this.getAttributeValueAsFloat("ICD_Parametric_Depth2");
        }
        return super.getLeftDepth();
    }
    
    @Override
    public float getRightDepth() {
        if (this.isMirrored()) {
            return this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
        }
        return super.getRightDepth();
    }
    
    @Override
    public String getShapeTag() {
        return "WSE";
    }
    
    @Override
    public void setupGripPainters() {
        (this.width1Grip = (BasicAttributeGrip)new RelativeAttributeGrip((TransformableEntity)this, 0)).setLinkID((byte)100);
        this.width1Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICDWSEParametricWorksurface.this.width1GripChanged(s2);
                return true;
            }
        });
        (this.width2Grip = (BasicAttributeGrip)new RelativeAttributeGrip((TransformableEntity)this, 0)).setLinkID((byte)101);
        this.width2Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICDWSEParametricWorksurface.this.width2GripChanged(s2);
                return true;
            }
        });
        (this.depth1Grip = (BasicAttributeGrip)new RelativeAttributeGrip((TransformableEntity)this, 2)).setLinkID((byte)102);
        this.depth1Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICDWSEParametricWorksurface.this.depth1GripChanged(s2);
                return true;
            }
        });
        (this.depth2Grip = (BasicAttributeGrip)new RelativeAttributeGrip((TransformableEntity)this, 2)).setLinkID((byte)103);
        this.depth2Grip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                ICDWSEParametricWorksurface.this.depth2GripChanged(s2);
                return true;
            }
        });
    }
    
    @Override
    public SortedSet<AttributeGripPoint> getAttributeMap(final BasicAttributeGrip basicAttributeGrip) {
        final TreeSet<AttributeGripPoint> set = new TreeSet<AttributeGripPoint>();
        if (basicAttributeGrip.equals(this.width1Grip)) {
            final Attribute attributeObject = this.getAttributeObject("ICD_Parametric_Width");
            if (attributeObject != null) {
                final float widthMin = this.getWidthMin();
                for (float widthMax = this.getWidthMax(), f = widthMin; f <= widthMax; f += ICD2Widths2DepthsGripSet.ICD_GRIP_STEP) {
                    final String string = "" + f;
                    final Attribute attribute = (Attribute)attributeObject.clone();
                    attribute.setCurrentValueAsString(string);
                    set.add(new AttributeGripPoint(ICDParametricWorksurface.pointAt(this.getWidth1Anchor(), this.getWidth1Direction(), f), (int)(f * ICD2Widths2DepthsGripSet.ICD_GRIP_WEIGHT_MULTIPLIER), new Attribute[] { attribute }));
                }
            }
        }
        if (basicAttributeGrip.equals(this.width2Grip)) {
            final Attribute attributeObject2 = this.getAttributeObject("ICD_Parametric_Width");
            if (attributeObject2 != null) {
                final float z = this.getGeometricCenterPointWorld().z;
                final float widthMin2 = this.getWidthMin();
                for (float widthMax2 = this.getWidthMax(), f2 = widthMin2; f2 <= widthMax2; f2 += ICD2Widths2DepthsGripSet.ICD_GRIP_STEP) {
                    final String string2 = "" + f2;
                    final Attribute attribute2 = (Attribute)attributeObject2.clone();
                    attribute2.setCurrentValueAsString(string2);
                    set.add(new AttributeGripPoint(ICDParametricWorksurface.pointAt(this.getWidth2Anchor(), this.getWidth2Direction(), f2), (int)(f2 * ICD2Widths2DepthsGripSet.ICD_GRIP_WEIGHT_MULTIPLIER), new Attribute[] { attribute2 }));
                }
            }
        }
        if (basicAttributeGrip.equals(this.depth1Grip)) {
            final Attribute attributeObject3 = this.getAttributeObject("ICD_Parametric_Depth1");
            if (attributeObject3 != null) {
                final float z2 = this.getGeometricCenterPointWorld().z;
                final float depth1Min = this.getDepth1Min();
                for (float depth1Max = this.getDepth1Max(), f3 = depth1Min; f3 <= depth1Max; f3 += ICD2Widths2DepthsGripSet.ICD_GRIP_STEP) {
                    final String string3 = "" + f3;
                    final Attribute attribute3 = (Attribute)attributeObject3.clone();
                    attribute3.setCurrentValueAsString(string3);
                    set.add(new AttributeGripPoint(ICDParametricWorksurface.pointAt(this.getDepth1Anchor(), this.getDepth1Direction(), f3), (int)(f3 * ICD2Widths2DepthsGripSet.ICD_GRIP_WEIGHT_MULTIPLIER), new Attribute[] { attribute3 }));
                }
            }
        }
        if (basicAttributeGrip.equals(this.depth2Grip)) {
            final Attribute attributeObject4 = this.getAttributeObject("ICD_Parametric_Depth2");
            if (attributeObject4 != null) {
                final float z3 = this.getGeometricCenterPointWorld().z;
                final float depth2Min = this.getDepth2Min();
                for (float depth2Max = this.getDepth2Max(), f4 = depth2Min; f4 <= depth2Max; f4 += ICD2Widths2DepthsGripSet.ICD_GRIP_STEP) {
                    final String string4 = "" + f4;
                    final Attribute attribute4 = (Attribute)attributeObject4.clone();
                    attribute4.setCurrentValueAsString(string4);
                    set.add(new AttributeGripPoint(ICDParametricWorksurface.pointAt(this.getDepth2Anchor(), this.getDepth2Direction(), f4), (int)(f4 * ICD2Widths2DepthsGripSet.ICD_GRIP_WEIGHT_MULTIPLIER), new Attribute[] { attribute4 }));
                }
            }
        }
        return set;
    }
    
    @Override
    public void updateGrips(final BasicAttributeGrip basicAttributeGrip) {
        if (basicAttributeGrip != null) {
            this.updateGripPositions(new AttributeGripPoint(new Attribute[] { this.getAttributeObject("ICD_Parametric_Width") }), new AttributeGripPoint(new Attribute[] { this.getAttributeObject("ICD_Parametric_Width") }), new AttributeGripPoint(new Attribute[] { this.getAttributeObject("ICD_Parametric_Depth1") }), new AttributeGripPoint(new Attribute[] { this.getAttributeObject("ICD_Parametric_Depth2") }));
        }
    }
    
    private void updateGripPositions(final AttributeGripPoint attributeGripPoint, final AttributeGripPoint attributeGripPoint2, final AttributeGripPoint attributeGripPoint3, final AttributeGripPoint attributeGripPoint4) {
        if (attributeGripPoint != null) {
            this.width1Grip.updateGrip(attributeGripPoint);
            this.width1Grip.setLocation(ICDParametricWorksurface.pointAt(this.getWidth1Anchor(), this.getWidth1Direction(), this.getAttributeValueAsFloat("ICD_Parametric_Width")));
            this.width1Grip.setAnchorLocation(this.getWidth1Anchor());
        }
        if (attributeGripPoint2 != null) {
            this.width2Grip.updateGrip(attributeGripPoint2);
            this.width2Grip.setLocation(ICDParametricWorksurface.pointAt(this.getWidth2Anchor(), this.getWidth2Direction(), this.getAttributeValueAsFloat("ICD_Parametric_Width")));
            this.width2Grip.setAnchorLocation(this.getWidth2Anchor());
        }
        if (attributeGripPoint3 != null) {
            this.depth1Grip.updateGrip(attributeGripPoint3);
            this.depth1Grip.setLocation(ICDParametricWorksurface.pointAt(this.getDepth1Anchor(), this.getDepth1Direction(), this.getAttributeValueAsFloat("ICD_Parametric_Depth1")));
            this.depth1Grip.setAnchorLocation(this.getDepth1Anchor());
        }
        if (attributeGripPoint4 != null) {
            this.depth2Grip.updateGrip(attributeGripPoint4);
            this.depth2Grip.setLocation(ICDParametricWorksurface.pointAt(this.getDepth2Anchor(), this.getDepth2Direction(), this.getAttributeValueAsFloat("ICD_Parametric_Depth2")));
            this.depth2Grip.setAnchorLocation(this.getDepth2Anchor());
        }
    }
    
    @Override
    protected void selectGrips(final boolean b) {
        this.width1Grip.setSelected(b);
        this.width2Grip.setSelected(b);
        this.depth1Grip.setSelected(b);
        this.depth2Grip.setSelected(b);
    }
    
    @Override
    protected void deselectGrips() {
        this.width1Grip.setSelected(false);
        this.width2Grip.setSelected(false);
        this.depth1Grip.setSelected(false);
        this.depth2Grip.setSelected(false);
    }
    
    @Override
    protected void destroyGrips() {
        this.width1Grip.destroy();
        this.width2Grip.destroy();
        this.depth1Grip.destroy();
        this.depth2Grip.destroy();
    }
    
    @Override
    protected void drawGrips(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        this.width1Grip.draw2D(n, ice2DContainer, solutionSetting);
        this.width2Grip.draw2D(n, ice2DContainer, solutionSetting);
        this.depth1Grip.draw2D(n, ice2DContainer, solutionSetting);
        this.depth2Grip.draw2D(n, ice2DContainer, solutionSetting);
    }
    
    public void depth1GripChanged(final String s) {
        Float n = 0.0f;
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
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
    
    public void depth2GripChanged(final String s) {
        Float n = 0.0f;
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Depth2");
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
        float n2 = -(attributeValueAsFloat - n) / 2.0f;
        if (this.isMirrored()) {
            n2 = -n2;
        }
        final Point3f basePoint = new Point3f(n2, 0.0f, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
    }
    
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
        float n2 = (attributeValueAsFloat - n) / 2.0f;
        if (this.isMirrored()) {
            n2 = -n2;
        }
        final Point3f basePoint = new Point3f(n2, 0.0f, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
    }
    
    @Override
    public boolean shouldFlipEdge3D() {
        return !this.isMirrored();
    }
    
    @Override
    public boolean shouldFlipCutouts3D() {
        return !this.isMirrored();
    }
}
