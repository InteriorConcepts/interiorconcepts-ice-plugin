// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.main.Solution;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicInnerExtrusionSet;
import com.iceedge.icebox.icecore.aspects.IceNodeAspect;
import net.iceedge.icecore.basemodule.interfaces.panels.FrameInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.InnerExtrusionSetInterface;
import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationUtilities;
import net.dirtt.icelib.main.ElevationEntity;
import icd.warnings.WarningReason0282;
import java.util.Iterator;
import net.iceedge.icecore.basemodule.baseclasses.panels.ExtrusionPoint;
import net.dirtt.icelib.report.Report;
import net.iceedge.icecore.basemodule.interfaces.lightweight.Paintable;
import net.dirtt.icebox.iceoutput.core.IceOutputTextNode;
import net.dirtt.icebox.iceoutput.core.IceOutputShapeNode;
import javax.vecmath.Tuple3f;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import java.util.List;
import java.io.Serializable;
import net.dirtt.icebox.canvas2d.Ice2DTextNode;
import javax.vecmath.Vector3f;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import java.util.Vector;
import javax.vecmath.Matrix4f;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import org.xith3d.scenegraph.Group;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelInterface;
import net.iceedge.catalogs.icd.ICDSegment;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import net.iceedge.catalogs.icd.intersection.ICDIntersection;
import net.dirtt.icelib.main.TransformableEntity;
import net.iceedge.catalogs.icd.ICDUtilities;
import net.dirtt.utilities.MathUtilities;
import javax.vecmath.Point3f;
import net.iceedge.icecore.basemodule.baseclasses.BasicSegment;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icebox.canvas2d.Ice2DShapeNode;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import com.iceedge.icd.entities.extrusions.ICDExtrusionInterface;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintable;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicInternalExtrusion;

public class ICDInternalExtrusion extends BasicInternalExtrusion implements AssemblyPaintable, JointIntersectable, ICDExtrusionInterface, ICDManufacturingReportable
{
    private Ice2DShapeNode assemblyNode;
    
    public ICDInternalExtrusion(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.setupNamedPoints();
    }
    
    public Object clone() {
        return this.buildClone(new ICDInternalExtrusion(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDInternalExtrusion buildClone(final ICDInternalExtrusion icdInternalExtrusion) {
        super.buildClone((BasicInternalExtrusion)icdInternalExtrusion);
        return icdInternalExtrusion;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDInternalExtrusion(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDInternalExtrusion buildFrameClone(final ICDInternalExtrusion icdInternalExtrusion, final EntityObject entityObject) {
        super.buildFrameClone((BasicInternalExtrusion)icdInternalExtrusion, entityObject);
        return icdInternalExtrusion;
    }
    
    protected boolean autoValidateBeltLineIndicator() {
        return false;
    }
    
    public boolean isFakePart() {
        return super.isFakePart() || this.getSide() == 1;
    }
    
    public void breakHorizontalExtrusion(float n, final boolean b) {
        String currentValueAsString = "regular regular regular";
        if (!b) {
            n = this.getZDimension() - n;
        }
        if (this.isFakePart()) {
            n = this.getZDimension() - n - 1.0f;
        }
        final BasicSegment basicSegment = (BasicSegment)this.getParentSegment();
        final ICDPanel icdPanel = (ICDPanel)this.getParentPanel();
        if (basicSegment == null || icdPanel == null) {
            return;
        }
        final Point3f convertPointToWorldSpace = icdPanel.convertPointToWorldSpace(new Point3f(5.0f, 0.0f, 0.0f));
        final float chaseOffset = this.getChaseOffset(basicSegment, icdPanel, convertPointToWorldSpace, true);
        final float chaseOffset2 = this.getChaseOffset(basicSegment, icdPanel, convertPointToWorldSpace, false);
        if (MathUtilities.isSameFloat(n, 0.0f, 1.5f) || MathUtilities.isSameFloat(n, this.getZDimension(), 1.5f)) {
            if (MathUtilities.isSameFloat(chaseOffset, 0.0f, 1.5f) && MathUtilities.isSameFloat(chaseOffset2, 0.0f, 1.5f)) {
                this.createNewAttribute("specialInternalExtrusion", "No");
                this.createNewAttribute("breakLocation", n + "");
                currentValueAsString = "regular regular regular";
            }
        }
        else {
            this.createNewAttribute("specialInternalExtrusion", "Yes");
            this.createNewAttribute("breakLocation", n + "");
            if (MathUtilities.isSameFloat(chaseOffset, 0.0f, 1.5f) ^ MathUtilities.isSameFloat(chaseOffset2, 0.0f, 1.5f)) {
                this.createNewAttribute("NumberOfJoints", "1");
                currentValueAsString = ICDUtilities.calculateExtrusionTypesForSingleChase((TransformableEntity)this, n, chaseOffset, chaseOffset2, basicSegment);
            }
            else if (!MathUtilities.isSameFloat(chaseOffset, 0.0f, 1.5f) && !MathUtilities.isSameFloat(chaseOffset2, 0.0f, 1.5f)) {
                this.createNewAttribute("NumberOfJoints", "2");
                if (!this.isFakePart()) {
                    currentValueAsString = ICDUtilities.calculateExtrusionTypesForDoubleChase((TransformableEntity)this, chaseOffset, chaseOffset2);
                }
            }
        }
        if (this.getAttributeObject("ICD_SubExtrusionTypesForBreaks") != null) {
            this.getAttributeObject("ICD_SubExtrusionTypesForBreaks").setCurrentValueAsString(currentValueAsString);
        }
    }
    
    private float getChaseOffset(final BasicSegment basicSegment, final ICDPanel icdPanel, final Point3f point3f, final boolean b) {
        final ICDIntersection icdIntersection = (ICDIntersection)basicSegment.getIntersectionForSegment(b);
        float chaseOffsetOnPointSide;
        if (icdIntersection != null) {
            chaseOffsetOnPointSide = icdIntersection.getChaseOffsetOnPointSide((Segment)basicSegment, point3f, false, icdPanel.shouldBreakChaseVertically());
            if (!MathUtilities.isSameFloat(chaseOffsetOnPointSide, 0.0f, 1.5f)) {
                int n;
                if (b) {
                    if (basicSegment.isFlipped()) {
                        n = ((ICDSegment)basicSegment).getNumberOfChaseAtPoint(this.convertPointToWorldSpace(new Point3f(0.0f, 0.0f, this.getZDimension() - chaseOffsetOnPointSide)));
                    }
                    else {
                        n = ((ICDSegment)basicSegment).getNumberOfChaseAtPoint(this.convertPointToWorldSpace(new Point3f(0.0f, 0.0f, chaseOffsetOnPointSide)));
                    }
                }
                else if (basicSegment.isFlipped()) {
                    n = ((ICDSegment)basicSegment).getNumberOfChaseAtPoint(this.convertPointToWorldSpace(new Point3f(0.0f, 0.0f, chaseOffsetOnPointSide)));
                }
                else {
                    n = ((ICDSegment)basicSegment).getNumberOfChaseAtPoint(this.convertPointToWorldSpace(new Point3f(0.0f, 0.0f, this.getZDimension() - chaseOffsetOnPointSide)));
                }
                if (n == 0) {
                    chaseOffsetOnPointSide = 0.0f;
                }
            }
        }
        else {
            chaseOffsetOnPointSide = 0.0f;
        }
        return chaseOffsetOnPointSide;
    }
    
    public void breakVerticalExtrusion(float n, final boolean b) {
        if (!b) {
            n = this.getZDimension() - n;
        }
        if (this.isFakePart()) {
            n = this.getZDimension() - n - 1.0f;
        }
        if (MathUtilities.isSameFloat(n, 0.0f, 1.5f) || MathUtilities.isSameFloat(n, this.getZDimension(), 1.5f)) {
            this.createNewAttribute("specialInternalExtrusion", "No");
            this.createNewAttribute("breakLocation", n + "");
        }
        else {
            this.createNewAttribute("specialInternalExtrusion", "Yes");
            this.createNewAttribute("breakLocation", n + "");
        }
    }
    
    public void setupNamedPoints() {
        super.setupNamedPoints();
        this.addNamedPoint("ext1BP", new Point3f());
        this.addNamedPoint("ext2BP", new Point3f());
        this.addNamedPoint("ext3BP", new Point3f());
        this.addNamedPoint("jointBP", new Point3f());
        this.addNamedPoint("joint2BP", new Point3f());
        this.addNamedPoint("EXT_POS", new Point3f());
        this.addNamedPoint("CAD_P1", new Point3f());
        this.addNamedPoint("CAD_P2", new Point3f());
        this.addNamedPoint("TAB_CONT_LOCATION_A", new Point3f());
        this.addNamedPoint("TAB_CONT_LOCATION_B", new Point3f());
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("breakLocation");
        float attributeValueAsFloat2 = 0.0f;
        final int attributeValueAsInt = this.getAttributeValueAsInt("NumberOfJoints");
        if (attributeValueAsInt == 2) {
            attributeValueAsFloat2 = this.getAttributeValueAsFloat("breakLocation2");
        }
        this.getNamedPointLocal("ext1BP").set(0.0f, 0.0f, 0.0f);
        this.getNamedPointLocal("ext2BP").set(0.0f, 0.0f, attributeValueAsFloat + 1.0f);
        this.getNamedPointLocal("jointBP").set(0.0f, 0.5f, attributeValueAsFloat + 0.5f);
        if (attributeValueAsInt == 2) {
            this.getNamedPointLocal("ext3BP").set(0.0f, 0.0f, attributeValueAsFloat2 + 1.0f);
            this.getNamedPointLocal("joint2BP").set(0.0f, 0.5f, attributeValueAsFloat2 + 0.5f);
        }
        this.getNamedPointLocal("EXT_POS").set(0.0f, 0.0f, 0.0f);
        this.getNamedPointLocal("svgElevationIP").set(0.0f, 0.0f, 0.0f);
        final PanelInterface parentPanel = this.getParentPanel();
        if (this.isVertical() && parentPanel != null) {
            if (parentPanel.isCorePanel()) {
                this.getNamedPointLocal("EXT_POS").set(0.0f, -0.5f, 1.0f);
                if (this.isFakePart()) {
                    this.getNamedPointLocal("svgElevationIP").set(0.0f, -0.5f, -0.5f);
                }
                else {
                    this.getNamedPointLocal("svgElevationIP").set(0.0f, -0.5f, 0.5f);
                }
                this.getNamedPointLocal("ext1BP").set(0.0f, -0.5f, 0.5f);
                this.getNamedPointLocal("ext2BP").set(0.0f, -0.5f, attributeValueAsFloat + 1.5f);
                this.getNamedPointLocal("jointBP").set(0.0f, 0.0f, attributeValueAsFloat + 1.0f);
            }
            else {
                this.getNamedPointLocal("EXT_POS").set(0.0f, -0.5f, 0.5f);
                this.getNamedPointLocal("svgElevationIP").set(0.0f, -0.5f, 0.0f);
            }
        }
        float n = this.getYDimension() / 2.0f;
        if (this.isVertical()) {
            n -= 0.5f;
        }
        this.getNamedPointLocal("CAD_P1").set(0.0f, n, 0.0f);
        this.getNamedPointLocal("CAD_P2").set(0.0f, n, this.getZDimension());
        this.getNamedPointLocal("TAB_CONT_LOCATION_A").set(0.0f, 1.0f, 0.0f);
        this.getNamedPointLocal("TAB_CONT_LOCATION_B").set(this.getNamedPointLocal("extEndPoint").x, this.getNamedPointLocal("extEndPoint").y + 1.0f, this.getNamedPointLocal("extEndPoint").z);
    }
    
    public void solve() {
        super.solve();
        this.setSubExtrusionDimension();
    }
    
    public void modifyCurrentOption() {
        this.validateIndicators();
        super.modifyCurrentOption();
    }
    
    protected void validateIndicators() {
        this.createNewAttribute("ICD_Vertical_Slottable_Extrusion", this.calculateExtrusionType());
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("breakLocation");
        if (attributeValueAsFloat < 2.0f || attributeValueAsFloat > this.getZDimension() - 2.0f) {
            this.createNewAttribute("specialInternalExtrusion", "No");
        }
    }
    
    protected String calculateExtrusionType() {
        if (this.isVertical()) {
            return "Vertical";
        }
        return "Horizontal";
    }
    
    private void setSubExtrusionDimension() {
        String attributeValueAsString = "regular regular regular";
        if (this.getAttributeObject("ICD_SubExtrusionTypesForBreaks") != null && this.getAttributeValueAsString("ICD_SubExtrusionTypesForBreaks").length() > 0) {
            attributeValueAsString = this.getAttributeValueAsString("ICD_SubExtrusionTypesForBreaks");
        }
        final String[] split = attributeValueAsString.split(" ");
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("breakLocation");
        final EntityObject childByLWType = this.getChildByLWType("ICD_Sub1_InnerExtrusion_Type");
        if (childByLWType instanceof ICDSubInternalExtrusion) {
            ((ICDSubInternalExtrusion)childByLWType).setZDimension(attributeValueAsFloat);
            ((ICDSubInternalExtrusion)childByLWType).getAttributeObject("ICD_Extrusion_Type").setCurrentValueAsString(split[0]);
        }
        if ("1".equals(this.getAttributeValueAsString("NumberOfJoints"))) {
            final EntityObject childByLWType2 = this.getChildByLWType("ICD_Sub2_InnerExtrusion_Type");
            if (childByLWType2 instanceof ICDSubInternalExtrusion) {
                ((ICDSubInternalExtrusion)childByLWType2).setZDimension(this.getZDimension() - attributeValueAsFloat - 1.0f);
                ((ICDSubInternalExtrusion)childByLWType2).getAttributeObject("ICD_Extrusion_Type").setCurrentValueAsString(split[1]);
            }
        }
        else if ("2".equals(this.getAttributeValueAsString("NumberOfJoints"))) {
            final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("breakLocation2");
            final EntityObject childByLWType3 = this.getChildByLWType("ICD_Sub2_InnerExtrusion_Type");
            if (childByLWType3 instanceof ICDSubInternalExtrusion) {
                ((ICDSubInternalExtrusion)childByLWType3).setZDimension(attributeValueAsFloat2 - attributeValueAsFloat - 1.0f);
                ((ICDSubInternalExtrusion)childByLWType3).getAttributeObject("ICD_Extrusion_Type").setCurrentValueAsString(split[1]);
            }
            final EntityObject childByLWType4 = this.getChildByLWType("ICD_Sub3_InnerExtrusion_Type");
            if (childByLWType4 instanceof ICDSubInternalExtrusion) {
                ((ICDSubInternalExtrusion)childByLWType4).setZDimension(this.getZDimension() - attributeValueAsFloat2 - 1.0f);
                ((ICDSubInternalExtrusion)childByLWType4).getAttributeObject("ICD_Extrusion_Type").setCurrentValueAsString(split[2]);
            }
        }
    }
    
    public void draw3D(final Group group, final int n) {
        if (!this.isFakePart()) {
            super.draw3D(group, n);
        }
    }
    
    public float getAttributeValueAsFloat(final String anObject) {
        if ("P1".equals(anObject)) {
            return this.getLength() * super.getAttributeValueAsFloat(anObject);
        }
        return super.getAttributeValueAsFloat(anObject);
    }
    
    protected boolean shouldUseNewFinishSystem() {
        return true;
    }
    
    protected void calculateNamedScales() {
        super.calculateNamedScales();
        this.getNamedScaleLocal("SVG_elevation_scale").set(1.0f, 1.0f, this.getZDimension());
    }
    
    public int getQuoteReportLevel() {
        return -1;
    }
    
    public boolean isQuoteable(final String s) {
        return false;
    }
    
    public boolean usePlotGVT() {
        return false;
    }
    
    public boolean shouldICDMakePreAssembledReport() {
        return !this.isFakePart() && !"true".equals(this.getAttributeValueAsString("isAssembled"));
    }
    
    public void draw2D(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        super.draw2D(n, ice2DContainer, solutionSetting);
        this.drawChildren2D(n, ice2DContainer, solutionSetting);
        this.getSolution();
    }
    
    public void destroy2DAsset() {
        super.destroy2DAsset();
        this.shapeNode = null;
        this.assemblyNode = null;
    }
    
    public void cutFromTree2D() {
        super.cutFromTree2D();
        if (this.shapeNode != null) {
            this.shapeNode.removeFromParent();
        }
        if (this.assemblyNode != null) {
            this.assemblyNode.removeFromParent();
        }
    }
    
    public Vector<Ice2DPaintableNode> getAssemblyIcons(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        final float yDimension = this.getYDimension();
        final float zDimension = this.getZDimension();
        Serializable s = new Rectangle2D.Float(0.0f, 0.0f, zDimension, yDimension);
        final boolean curvedExtrusion = this.isCurvedExtrusion();
        if (curvedExtrusion) {
            s = new QuadCurve2D.Float(0.0f, 0.0f, zDimension / 2.0f + 1.0f, 4.0f, zDimension, 0.0f);
        }
        final Matrix4f matrix4f2 = new Matrix4f();
        matrix4f2.setIdentity();
        matrix4f2.mul(this.getEntWorldSpaceMatrix());
        final Matrix4f matrix4f3 = new Matrix4f();
        matrix4f3.setIdentity();
        matrix4f3.rotY(-1.5707964f);
        matrix4f2.mul(matrix4f3);
        final Ice2DShapeNode e = new Ice2DShapeNode(this.getLayerName(), (TransformableEntity)this, matrix4f2, (Shape)s);
        if (!curvedExtrusion) {
            e.setFillColor(Color.lightGray);
        }
        final Matrix4f matrix4f4 = new Matrix4f();
        matrix4f4.setIdentity();
        matrix4f4.mul(this.getEntWorldSpaceMatrix());
        final Matrix4f matrix4f5 = new Matrix4f();
        matrix4f5.setIdentity();
        matrix4f5.rotY(-1.5707964f);
        matrix4f4.mul(matrix4f5);
        final Matrix4f matrix4f6 = new Matrix4f();
        matrix4f6.setIdentity();
        matrix4f6.setTranslation(new Vector3f(this.getZDimension() / 2.0f, 0.0f, 0.0f));
        matrix4f4.mul(matrix4f6);
        final Ice2DTextNode e2 = new Ice2DTextNode(this.getLayerName(), (TransformableEntity)this, matrix4f4, Math.round(this.getZDimension()) + "", 3);
        final Vector<Ice2DPaintableNode> vector = new Vector<Ice2DPaintableNode>();
        vector.add((Ice2DPaintableNode)e);
        vector.add((Ice2DPaintableNode)e2);
        return vector;
    }
    
    public List<IceOutputNode> getPlotOutputNodes(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        if (this.getCurrentOption().getId().contains("None") || this.isFakePart()) {
            return null;
        }
        final Rectangle2D.Float float1 = new Rectangle2D.Float(0.0f, 0.0f, this.getZDimension(), this.getYDimension());
        final Point3f convertSpaces = MathUtilities.convertSpaces(this.getBasePointWorldSpace(), transformableEntity.getEntWorldSpaceMatrix());
        final Matrix4f matrix4f2 = new Matrix4f();
        matrix4f2.setIdentity();
        matrix4f2.mul(matrix4f);
        if (n == 1) {
            final Matrix4f matrix4f3 = new Matrix4f();
            matrix4f3.setIdentity();
            matrix4f3.rotY(3.1415927f);
            matrix4f2.mul(matrix4f3);
        }
        final Matrix4f matrix4f4 = new Matrix4f();
        matrix4f4.setIdentity();
        matrix4f4.rotX(-1.5707964f);
        matrix4f2.mul(matrix4f4);
        final Matrix4f matrix4f5 = new Matrix4f();
        matrix4f5.setIdentity();
        matrix4f5.setTranslation(new Vector3f((Tuple3f)convertSpaces));
        matrix4f2.mul(matrix4f5);
        final float n2 = 1.5707964f;
        final Matrix4f matrix4f6 = new Matrix4f();
        matrix4f6.setIdentity();
        matrix4f6.rotX(n2);
        matrix4f2.mul(matrix4f6);
        final IceOutputShapeNode e = new IceOutputShapeNode((Shape)float1, matrix4f2);
        final Point3f point3f2 = new Point3f(this.getZDimension() / 2.0f, 0.0f, 0.0f);
        if (n == 1) {
            final Matrix4f matrix4f7 = new Matrix4f();
            matrix4f7.setIdentity();
            matrix4f7.rotX(3.1415927f);
            matrix4f2.mul(matrix4f7);
            final Matrix4f matrix4f8 = new Matrix4f();
            matrix4f8.setIdentity();
            matrix4f8.rotZ(3.1415927f);
            matrix4f2.mul(matrix4f8);
            point3f2.x = -this.getZDimension() / 2.0f;
        }
        final IceOutputTextNode e2 = new IceOutputTextNode((Paintable)null, MathUtilities.round(this.getZDimension(), 2) + "", 3.0f, 1.0f, 0.0f, point3f2, matrix4f2);
        final Vector<IceOutputNode> vector = new Vector<IceOutputNode>();
        vector.add((IceOutputNode)e);
        vector.add((IceOutputNode)e2);
        return vector;
    }
    
    public boolean shouldAddChildrenToReport(final Report report) {
        return false;
    }
    
    public boolean useTransformablesDraw3D() {
        return true;
    }
    
    public boolean hasSubExtrusion() {
        return this.getChildByClass(ICDSubInternalExtrusion.class) != null;
    }
    
    public boolean addTabbing(final int n) {
        final ICDPanel icdPanel = (ICDPanel)this.getParent(ICDPanel.class);
        if (icdPanel != null && icdPanel.isSuspendedChase()) {
            final ExtrusionPoint startPoint = this.getStartPoint();
            final ExtrusionPoint endPoint = this.getEndPoint();
            final float suspendedChaseLocation = icdPanel.getSuspendedChaseLocation();
            if (startPoint != null && endPoint != null && MathUtilities.isSameFloat(startPoint.getValue(), suspendedChaseLocation, 0.2f) && MathUtilities.isSameFloat(endPoint.getValue(), suspendedChaseLocation, 0.2f)) {
                return icdPanel.hasChase(n);
            }
        }
        if (icdPanel != null && icdPanel.hasChase(n)) {
            return false;
        }
        boolean hasSubExtrusion = this.hasSubExtrusion();
        if (!hasSubExtrusion) {
            final ICDInternalExtrusion sibling = this.getSibling();
            if (sibling != null && sibling.hasSubExtrusion()) {
                hasSubExtrusion = (n != this.getSide());
            }
        }
        return super.validateTabs() && !hasSubExtrusion;
    }
    
    private ICDInternalExtrusion getSibling() {
        ICDInternalExtrusion icdInternalExtrusion = null;
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity != null) {
            for (final ICDInternalExtrusion icdInternalExtrusion2 : parentEntity.getChildrenByClass(ICDInternalExtrusion.class, false, true)) {
                if (!icdInternalExtrusion2.equals(this)) {
                    icdInternalExtrusion = icdInternalExtrusion2;
                    break;
                }
            }
        }
        return icdInternalExtrusion;
    }
    
    public boolean isManufacturerReportable() {
        return !this.isSpecialInternalExtrusion() && !this.isFakePart();
    }
    
    public boolean isSpecialInternalExtrusion() {
        return "Yes".equals(this.getAttributeValueAsString("specialInternalExtrusion"));
    }
    
    public boolean isCurvedExtrusion() {
        final String id = this.getCurrentOption().getId();
        return id != null && (id.indexOf("Curved") >= 0 || id.indexOf("curved") >= 0);
    }
    
    public boolean shouldDrawAssembly() {
        final String sku = this.getSKU();
        return sku != null && !sku.equals("") && !this.getCurrentOption().getId().contains("None") && !this.isFakePart();
    }
    
    public boolean isAssembled() {
        return "true".equals(this.getAttributeValueAsString("isAssembled"));
    }
    
    public boolean doesParticipateInJointIntersection() {
        return !this.isFakePart();
    }
    
    public void handleWarnings() {
        WarningReason0282.addRequiredWarning((TransformableEntity)this);
    }
    
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        if (ICDAssemblyElevationUtilities.shouldDrawElevation(elevationEntity, this)) {
            return this.getAssemblyLineScripts();
        }
        return (Vector<String>)super.getCadElevationScript(elevationEntity);
    }
    
    private Vector<String> getAssemblyLineScripts() {
        final Vector<String> vector = new Vector<String>();
        vector.add(ICDAssemblyElevationUtilities.getAssemblyLineScript(new Point3f(0.0f, 0.0f, 0.0f), "CAD_P1", "CAD_P2"));
        if (!this.isAssembled()) {
            vector.add(ICDAssemblyElevationUtilities.getAssemblyLineScript(new Point3f(0.0f, -0.5f, 0.0f), "CAD_P1", "CAD_P2"));
        }
        String string = Math.round(this.getZDimension()) + "";
        if (this.isCurvedExtrusion()) {
            string = "24-Curved";
        }
        vector.add("TX:SS(" + string + ":" + 5 + ":" + 0 + ":" + 2 + ":CADELP8)");
        return vector;
    }
    
    public void validateChildTypes() {
        if (this.isFakePart()) {
            this.destroyChildren();
        }
        else {
            super.validateChildTypes();
        }
    }
    
    protected boolean drawnForBothSides() {
        return true;
    }
    
    public void draw2DElevation(final int n, final Ice2DContainer ice2DContainer, final boolean b, final SolutionSetting solutionSetting) {
        if (solutionSetting.isShownHiddenExtrusion() || this.isVisible()) {
            if (ice2DContainer.isShowFrameMode() || this.getSide() != 1) {
                if ((this.getRealSide() == ice2DContainer.getSide() || ice2DContainer.getSide() == -1 || this.drawnForBothSides()) && !this.isFakePart()) {
                    super.draw2DElevation(n, ice2DContainer, b, solutionSetting);
                    this.draw2DImageNode(ice2DContainer, solutionSetting);
                    this.draw2DElevationPreviewLine(n, ice2DContainer, b, solutionSetting);
                }
            }
        }
    }
    
    public void handleAttributeChange(final String s, final String s2) {
        ICDUtilities.handleAttributeChange((EntityObject)this, s, s2);
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addDimensionsToManufacturingTreeMap(treeMap, (TransformableEntity)this);
    }
    
    public String getManufacturingReportMaterialOptID() {
        return "Option0";
    }
    
    public void addManufacturingInfoToTreeMap(final TreeMap<String, String> treeMap) {
        this.getManufacturingInfo(treeMap);
    }
    
    public String getDescriptionForManufacturingReport() {
        return this.getDescription() + ",Length:" + Math.round(this.getLength()) + " " + this.getReportDescription();
    }
    
    public void getManufacturingInfo(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addManufacturingInfoToTreeMapForExtrusion(treeMap, (ManufacturingReportable)this, this.getLength());
    }
    
    public String getReportDescription() {
        return ICDManufacturingUtils.getReportDescriptionForExtrusion((EntityObject)this);
    }
    
    protected float getMaxBoundary() {
        final Vector<InnerExtrusionSetInterface> innerNeighbourExtrusions = this.getInnerNeighbourExtrusions(true);
        final FrameInterface physicalFrame = this.getParentPanel().getPhysicalFrame();
        float n;
        if (this.isVertical()) {
            n = physicalFrame.getEndExtrusion().getStartPoint().getPoint().x;
            for (int i = 0; i < innerNeighbourExtrusions.size(); ++i) {
                final float x = innerNeighbourExtrusions.elementAt(i).getStartPoint().getPoint().x;
                if (x > this.getStartPoint().getPoint().x && x < n) {
                    n = x;
                }
            }
        }
        else {
            n = physicalFrame.getTopExtrusion().getStartPoint().getPoint().y;
            for (int j = 0; j < innerNeighbourExtrusions.size(); ++j) {
                final float y = innerNeighbourExtrusions.elementAt(j).getStartPoint().getPoint().y;
                if (y > this.getStartPoint().getPoint().y && y < n) {
                    n = y;
                }
            }
        }
        return n;
    }
    
    protected float getMinBoundary() {
        final Vector<InnerExtrusionSetInterface> innerNeighbourExtrusions = this.getInnerNeighbourExtrusions(true);
        final FrameInterface physicalFrame = this.getParentPanel().getPhysicalFrame();
        float n;
        if (this.isVertical()) {
            n = physicalFrame.getStartExtrusion().getStartPoint().getPoint().x;
            for (int i = 0; i < innerNeighbourExtrusions.size(); ++i) {
                final float x = innerNeighbourExtrusions.elementAt(i).getStartPoint().getPoint().x;
                if (x < this.getStartPoint().getPoint().x && x > n) {
                    n = x;
                }
            }
        }
        else {
            n = physicalFrame.getBottomExtrusion().getStartPoint().getPoint().y;
            for (int j = 0; j < innerNeighbourExtrusions.size(); ++j) {
                final float y = innerNeighbourExtrusions.elementAt(j).getStartPoint().getPoint().y;
                if (y < this.getStartPoint().getPoint().y && y > n) {
                    n = y;
                }
            }
        }
        return n;
    }
    
    private Vector<InnerExtrusionSetInterface> getInnerNeighbourExtrusions(final boolean b) {
        final Vector<InnerExtrusionSetInterface> vector = new Vector<InnerExtrusionSetInterface>();
        final Point3f calculateMidpoint = MathUtilities.calculateMidpoint(this.getStartPoint().getPoint(), this.getEndPoint().getPoint());
        final EntityObject entityObject = (EntityObject)((IceNodeAspect)this.getAspect(IceNodeAspect.class)).getParent();
        if (entityObject != null) {
            for (int i = 0; i < entityObject.getChildCount(); ++i) {
                final EntityObject childEntity = entityObject.getChildEntityAt(i);
                if (childEntity instanceof InnerExtrusionSetInterface && childEntity != this) {
                    final InnerExtrusionSetInterface obj = (InnerExtrusionSetInterface)childEntity;
                    if (!(obj.isVertical() ^ this.isVertical()) && obj.projectionInsideExtrusion(calculateMidpoint) && (!b || ((BasicInnerExtrusionSet)obj).isVisible())) {
                        vector.addElement(obj);
                    }
                }
            }
        }
        return vector;
    }
    
    public void addToCutSolution(final Vector vector, final Vector vector2, final Solution solution, final boolean b) {
        final Iterator<EntityObject> children = this.getChildren();
        while (children.hasNext()) {
            final EntityObject entityObject = children.next();
            if (vector.contains(entityObject)) {
                vector.remove(entityObject);
            }
        }
    }
    
    public Point3f pseudoMove(final Point3f point3f, final Point3f point3f2) {
        final float currentLocation = this.getCurrentLocation();
        final Point3f pseudoMove = super.pseudoMove(point3f, point3f2);
        if (this.isVertical()) {
            this.moveableMove(this.getCurrentLocation() - currentLocation, 0.0f, true);
        }
        else {
            this.moveableMove(0.0f, this.getCurrentLocation() - currentLocation, true);
        }
        return pseudoMove;
    }
}
