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
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.CircleParameter;
import net.dirtt.icebox.canvas2d.Ice2DDirectPaintable;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;

public class ICDWVVFMParametricWorksurface extends ICDParametricWorksurface implements Ice2DDirectPaintable, ICD2Widths2DepthsGrippable
{
    private CircleParameter c1;
    private CircleParameter c2;
    private CircleParameter c3;
    private CircleParameter c4;
    private ICD1Width2Way1Depth2WayGripSet gripSet;
    
    public ICDWVVFMParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.c1 = new CircleParameter();
        this.c2 = new CircleParameter();
        this.c3 = new CircleParameter();
        this.c4 = new CircleParameter();
        this.gripSet = null;
        this.gripSet = new ICD1Width2Way1Depth2WayGripSet(this);
    }
    
    public ICDWVVFMParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.c1 = new CircleParameter();
        this.c2 = new CircleParameter();
        this.c3 = new CircleParameter();
        this.c4 = new CircleParameter();
        this.gripSet = null;
        this.gripSet = new ICD1Width2Way1Depth2WayGripSet(this);
    }
    
    public Object clone() {
        return this.buildClone(new ICDWVVFMParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWVVFMParametricWorksurface buildClone(final ICDWVVFMParametricWorksurface icdwvvfmParametricWorksurface) {
        super.buildClone(icdwvvfmParametricWorksurface);
        icdwvvfmParametricWorksurface.calculateParameters();
        return icdwvvfmParametricWorksurface;
    }
    
    @Override
    protected void calculateParameters() {
        this.allParameters.clear();
        this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor");
        this.validateDimensionAttributes();
        this.shape.clear();
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("ICD_Parametric_Width");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth");
        final float n = Math.abs(attributeValueAsFloat - attributeValueAsFloat2) / 2.0f;
        final float n2 = attributeValueAsFloat2 * 0.5f;
        float n3 = n2 - 3.0f;
        float n4;
        if (n2 * n2 < n3 * n3 + n * n) {
            n4 = (n3 * n3 + n * n - n2 * n2) / (2.0f * (n2 - n3));
        }
        else {
            n3 = n2 - (n3 * n3 + n * n) / n2 * n2 * 3.0f;
            n4 = (n3 * n3 + n * n - n2 * n2) / (2.0f * (n2 - n3));
        }
        final float abs = Math.abs(n4 + n3);
        final Point3f center = new Point3f(n, 0.0f, 0.0f);
        final Point3f center2 = new Point3f(-n, 0.0f, 0.0f);
        final Point3f center3 = new Point3f(0.0f, -abs, 0.0f);
        final Point3f center4 = new Point3f(0.0f, abs, 0.0f);
        this.c1.setCenter(center);
        this.c1.setRadius(n2);
        this.c2.setCenter(center2);
        this.c2.setRadius(n2);
        this.c3.setCenter(center3);
        this.c3.setRadius(n4);
        this.c4.setCenter(center4);
        this.c4.setRadius(n4);
        this.c1.calculate();
        this.c2.calculate();
        this.c3.calculate();
        this.c4.calculate();
        final Point3f point3f = new Point3f(-(attributeValueAsFloat / 2.0f), 0.0f, 0.0f);
        final Point3f touch = this.c2.touch(this.c4);
        final Point3f touch2 = this.c4.touch(this.c1);
        final Point3f point3f2 = new Point3f(attributeValueAsFloat / 2.0f, 0.0f, 0.0f);
        final Point3f touch3 = this.c3.touch(this.c1);
        final Point3f touch4 = this.c3.touch(this.c2);
        this.shapePoints.clear();
        this.shapePoints.add(point3f);
        this.shapePoints.add(touch);
        this.shapePoints.add(touch2);
        this.shapePoints.add(point3f2);
        this.shapePoints.add(touch3);
        this.shapePoints.add(touch4);
        this.c2.setClockwisePath(true);
        this.c2.setShorterPath(false);
        this.c4.setClockwisePath(false);
        this.c1.setClockwisePath(true);
        this.c1.setShorterPath(false);
        this.c1.setClockwisePath(true);
        this.c3.setClockwisePath(false);
        this.c2.setClockwisePath(true);
        this.plotNodes.clear();
        this.getEntWorldSpaceMatrix();
        this.width1Anchor = point3f;
        this.width2Anchor = point3f2;
        this.depth1Anchor = ICDParametricWorksurface.pointAt(touch, new Vector3f(1.0f, 0.0f, 0.0f), touch.distance(touch2) / 2.0f);
        this.depth2Anchor = ICDParametricWorksurface.pointAt(touch3, new Vector3f(-1.0f, 0.0f, 0.0f), touch3.distance(touch4) / 2.0f);
        this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.width2Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.depth1Direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.depth2Direction = new Vector3f(0.0f, 1.0f, 0.0f);
        this.cutoutRefPoint = new Point3f();
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, 1.0f, 0.0f);
        this.allParameters.add((Parameter2D)this.c2);
        this.allParameters.add((Parameter2D)this.c4);
        this.allParameters.add((Parameter2D)this.c1);
        this.allParameters.add((Parameter2D)this.c3);
        this.updateBRep(true, true);
    }
    
    @Override
    public void draw2D(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        if (this.isDirty(n) && ICDWVVFMParametricWorksurface.DRAW2D_DEBUG) {
            if (this.delegate2DNode != null && this.delegate2DNode.getParent() != null) {
                this.delegate2DNode.removeFromParent();
            }
            this.directPaintables.clear();
            this.directPaintables.add((Ice2DDirectPaintable)this.c1);
            this.directPaintables.add((Ice2DDirectPaintable)this.c2);
            this.directPaintables.add((Ice2DDirectPaintable)this.c3);
            this.directPaintables.add((Ice2DDirectPaintable)this.c4);
            this.directPaintables.add((Ice2DDirectPaintable)this);
            ice2DContainer.add((Ice2DNode)(this.delegate2DNode = new Ice2DDelegateNode((String)null, (TransformableEntity)this, this.getEntWorldSpaceMatrix(), (ArrayList)this.directPaintables)));
        }
        super.draw2D(n, ice2DContainer, solutionSetting);
    }
    
    @Override
    public void paint(final Graphics2D graphics2D, final Transform3D transform3D, final Ice2DPaintableNode ice2DPaintableNode) {
        super.paint(graphics2D, transform3D, ice2DPaintableNode);
    }
    
    public String getShapeTag() {
        return "WVVFM";
    }
    
    @Override
    public float getWidthMin() {
        final float minimumValueFromAttribute = this.getMinimumValueFromAttribute("ICD_Parametric_Width_Min");
        this.clampAttributeValue("ICD_Parametric_Depth", this.getDepthMin(), this.getDepthMax());
        final float n = this.getAttributeValueAsFloat("ICD_Parametric_Depth") / 2.0f;
        return Math.max(minimumValueFromAttribute, (float)Math.ceil(2.0f * (float)Math.sqrt(10.0f * n - 25.0f) + 2.0f * n));
    }
    
    public void setupGripPainters() {
        if (this.gripSet == null) {
            this.gripSet = new ICD1Width2Way1Depth2WayGripSet(this);
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
        final Point3f basePoint = new Point3f(0.0f, (attributeValueAsFloat - n) / 2.0f, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
    }
    
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
        final Point3f basePoint = new Point3f(0.0f, -(attributeValueAsFloat - n) / 2.0f, 0.0f);
        this.getEntWorldSpaceMatrix().transform(basePoint);
        this.setBasePoint(basePoint);
    }
    
    public TransformableEntity getEntity() {
        return (TransformableEntity)this;
    }
    
    @Override
    protected Point3f getCadOutputInsertionPoint(final String s) {
        final Point3f point3f2;
        final Point3f point3f = point3f2 = (Point3f)this.getGeometricCenterPointLocal().clone();
        point3f2.x -= s.length() * 2.0f / 2.0f;
        return this.convertPointToWorldSpace(point3f);
    }
}
