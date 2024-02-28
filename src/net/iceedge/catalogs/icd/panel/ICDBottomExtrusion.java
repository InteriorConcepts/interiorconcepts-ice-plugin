package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import java.util.Iterator;
import net.dirtt.utilities.TypeFilter;
import com.iceedge.icd.typefilters.ICDSubFrameFilter;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationUtilities;
import net.dirtt.icelib.main.ElevationEntity;
import icd.warnings.WarningReason0282;
import net.dirtt.icelib.report.Report;
import net.iceedge.icecore.basemodule.interfaces.lightweight.Paintable;
import net.dirtt.icebox.iceoutput.core.IceOutputTextNode;
import net.dirtt.icebox.iceoutput.core.IceOutputShapeNode;
import javax.vecmath.Tuple3f;
import net.iceedge.catalogs.icd.ICDILine;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import net.dirtt.icebox.canvas2d.Ice2DTextNode;
import javax.vecmath.Vector3f;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import java.util.Vector;
import net.iceedge.icecore.basemodule.interfaces.panels.TileInterface;
import net.dirtt.icelib.main.AdjustmentValue;
import net.dirtt.icelib.main.snapping.simple.SimpleSnapTargetCollection;
import net.dirtt.icelib.main.SolutionSetting;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelInterface;
import java.awt.Color;
import java.util.StringTokenizer;
import net.dirtt.icebox.canvas2d.Ice2DNode;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.catalogs.icd.ICDSegment;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import net.iceedge.catalogs.icd.intersection.ICDIntersection;
import net.dirtt.icelib.main.TransformableEntity;
import javax.vecmath.Point3f;
import net.iceedge.catalogs.icd.ICDUtilities;
import net.dirtt.utilities.MathUtilities;
import net.iceedge.icecore.basemodule.baseclasses.BasicSegment;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicSurroundingStickExtrusion;
import net.dirtt.icelib.main.EntityObject;
import java.util.ArrayList;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import javax.vecmath.Matrix4f;
import net.dirtt.utilities.Pair;
import java.util.List;
import net.dirtt.icebox.canvas2d.Ice2DShapeNode;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import com.iceedge.icd.entities.extrusions.ICDExtrusionInterface;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintable;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicBottomExtrusion;

public class ICDBottomExtrusion extends BasicBottomExtrusion implements AssemblyPaintable, JointIntersectable, ICDHorizontalBreakableExtrusion, ICDExtrusionInterface, ICDManufacturingReportable
{
    public static final String SPACE = " ";
    private Ice2DShapeNode assemblyNode;
    private List<Pair<Float, Integer>> breakLocations;
    private Ice2DShapeNode shapeNode;
    private Matrix4f shapeMatrix;
    
    public ICDBottomExtrusion(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.breakLocations = new ArrayList<Pair<Float, Integer>>();
        this.shapeMatrix = new Matrix4f();
    }
    
    public Object clone() {
        return this.buildClone(new ICDBottomExtrusion(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDBottomExtrusion buildClone(final ICDBottomExtrusion icdBottomExtrusion) {
        super.buildClone((BasicBottomExtrusion)icdBottomExtrusion);
        return icdBottomExtrusion;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDBottomExtrusion(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDBottomExtrusion buildFrameClone(final ICDBottomExtrusion icdBottomExtrusion, final EntityObject entityObject) {
        super.buildFrameClone((BasicSurroundingStickExtrusion)icdBottomExtrusion, entityObject);
        return icdBottomExtrusion;
    }
    
    public void breakHorizontalExtrusion(float f, final boolean b) {
        String extrusionType = "regular regular regular";
        final BasicSegment basicSegment = (BasicSegment)this.getParentSegment();
        final ICDPanel icdPanel = (ICDPanel)this.getParentPanel();
        if (basicSegment == null || icdPanel == null) {
            return;
        }
        final String attributeValueAsString = this.getCurrentOption().getAttributeValueAsString("specialInternalExtrusion");
        if (!b) {
            f = this.getZDimension() - f;
        }
        if (!MathUtilities.isSameFloat(f, 0.0f, 1.5f) && !MathUtilities.isSameFloat(f, this.getZDimension(), 1.5f) && "No".equals(attributeValueAsString)) {
            this.createNewAttribute("specialInternalExtrusion", "Yes");
            this.createNewAttribute("NumberOfJoints", "1");
            this.createNewAttribute("breakLocation", f + "");
            ICDUtilities.applyBeamBreakLocationForExtrusion(this);
            this.setExtrusionType("regular regular regular");
            return;
        }
        final float[] offsetOnBothEnds = this.getOffsetOnBothEnds(basicSegment, icdPanel, icdPanel.convertPointToWorldSpace(new Point3f(5.0f, 0.0f, 0.0f)));
        final float n = offsetOnBothEnds[0];
        final float n2 = offsetOnBothEnds[1];
        if (MathUtilities.isSameFloat(n, 0.0f, 1.5f) && MathUtilities.isSameFloat(n2, 0.0f, 1.5f)) {
            this.createNewAttribute("specialInternalExtrusion", "No");
            ICDUtilities.applyBeamBreakLocationForExtrusion(this);
            this.setExtrusionType("regular regular regular");
            return;
        }
        this.createNewAttribute("specialInternalExtrusion", "Yes");
        if (MathUtilities.isSameFloat(n, 0.0f, 1.5f) ^ MathUtilities.isSameFloat(n2, 0.0f, 1.5f)) {
            this.createNewAttribute("NumberOfJoints", "1");
            extrusionType = ICDUtilities.calculateExtrusionTypesForSingleChase((TransformableEntity)this, f, n, n2, basicSegment);
        }
        else if (!MathUtilities.isSameFloat(n, 0.0f, 1.5f) && !MathUtilities.isSameFloat(n2, 0.0f, 1.5f)) {
            this.createNewAttribute("NumberOfJoints", "2");
            extrusionType = ICDUtilities.calculateExtrusionTypesForDoubleChase((TransformableEntity)this, n, n2);
        }
        this.setExtrusionType(extrusionType);
        ICDUtilities.applyBeamBreakLocationForExtrusion(this);
        this.validateIndicators();
    }
    
    private float[] getOffsetOnBothEnds(final BasicSegment basicSegment, final ICDPanel icdPanel, final Point3f point3f) {
        return new float[] { this.getChaseOffset(basicSegment, icdPanel, point3f, true), this.getChaseOffset(basicSegment, icdPanel, point3f, false) };
    }
    
    private float getChaseOffset(final BasicSegment basicSegment, final ICDPanel icdPanel, final Point3f point3f, final boolean b) {
        final ICDIntersection icdIntersection = (ICDIntersection)basicSegment.getIntersectionForSegment(b);
        float chaseOffsetOnPointSide;
        if (icdIntersection != null) {
            chaseOffsetOnPointSide = icdIntersection.getChaseOffsetOnPointSide((Segment)basicSegment, point3f, true, icdPanel.shouldBreakChaseVertically());
            if (chaseOffsetOnPointSide > 0.0f) {
                boolean b2 = false;
                if (icdIntersection.getNumberOfSuspendedChaseOnPointSide((Segment)basicSegment, point3f) > 0 && icdPanel.shouldBreakChaseVertically()) {
                    b2 = true;
                }
                int n;
                if (b) {
                    if (basicSegment.isFlipped()) {
                        n = ((ICDSegment)basicSegment).getNumberOfChaseAtPoint(icdPanel.convertPointToWorldSpace(new Point3f(icdPanel.getXDimension() - chaseOffsetOnPointSide, 0.75f, 0.0f)));
                    }
                    else {
                        n = ((ICDSegment)basicSegment).getNumberOfChaseAtPoint(icdPanel.convertPointToWorldSpace(new Point3f(chaseOffsetOnPointSide, 0.75f, 0.0f)));
                    }
                }
                else if (basicSegment.isFlipped()) {
                    n = ((ICDSegment)basicSegment).getNumberOfChaseAtPoint(icdPanel.convertPointToWorldSpace(new Point3f(chaseOffsetOnPointSide, 0.75f, 0.0f)));
                }
                else {
                    n = ((ICDSegment)basicSegment).getNumberOfChaseAtPoint(icdPanel.convertPointToWorldSpace(new Point3f(icdPanel.getXDimension() - chaseOffsetOnPointSide, 0.75f, 0.0f)));
                }
                if (n == 0 && !b2) {
                    chaseOffsetOnPointSide = 0.0f;
                }
            }
        }
        else {
            chaseOffsetOnPointSide = 0.0f;
        }
        return chaseOffsetOnPointSide;
    }
    
    public void setupNamedPoints() {
        super.setupNamedPoints();
        this.addNamedPoint("ext1BP", new Point3f());
        this.addNamedPoint("ext2BP", new Point3f());
        this.addNamedPoint("ext3BP", new Point3f());
        this.addNamedPoint("jointBP", new Point3f());
        this.addNamedPoint("joint2BP", new Point3f());
        this.addNamedPoint("EXT_POS", new Point3f());
        this.addNamedPoint("PP1", new Point3f());
        this.addNamedPoint("PP2", new Point3f());
        this.addNamedPoint("PP3", new Point3f());
        this.addNamedPoint("PP4", new Point3f());
        this.addNamedPoint("CAD_P1", new Point3f());
        this.addNamedPoint("CAD_P2", new Point3f());
        this.addNamedPoint("CAD_POS", new Point3f());
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
        final float n = 0.0f;
        final float n2 = 0.75f;
        this.getNamedPointLocal("ext1BP").set(0.0f, n, 0.0f);
        this.getNamedPointLocal("ext2BP").set(0.0f, n, attributeValueAsFloat + 1.0f);
        this.getNamedPointLocal("jointBP").set(0.0f, n2, attributeValueAsFloat + 0.5f);
        if (attributeValueAsInt == 2) {
            this.getNamedPointLocal("ext3BP").set(0.0f, n, attributeValueAsFloat2 + 1.0f);
            this.getNamedPointLocal("joint2BP").set(0.0f, n2, attributeValueAsFloat2 + 0.5f);
        }
        this.getNamedPointLocal("EXT_POS").set(0.0f, 0.25f, 0.0f);
        final ICDSubFrameSideContainer icdSubFrameSideContainer = (ICDSubFrameSideContainer)this.getParent(ICDSubFrameSideContainer.class);
        if (icdSubFrameSideContainer != null) {
            final ICDPanel icdPanel = (ICDPanel)icdSubFrameSideContainer.getParent(ICDPanel.class);
            if (icdPanel != null && icdPanel.isSuspendedChase()) {
                this.getNamedPointLocal("EXT_POS").set(0.0f, 0.0f, 0.0f);
            }
        }
        this.getNamedPointLocal("PP1").set(this.getNamedPointLocal("P1").x, this.getNamedPointLocal("P1").y, this.getNamedPointLocal("P1").z);
        this.getNamedPointLocal("PP2").set(this.getNamedPointLocal("P2").x, this.getNamedPointLocal("P2").y, this.getNamedPointLocal("P2").z);
        this.getNamedPointLocal("PP3").set(this.getNamedPointLocal("P3").x, this.getNamedPointLocal("P3").y, this.getNamedPointLocal("P3").z);
        this.getNamedPointLocal("PP4").set(this.getNamedPointLocal("P4").x, this.getNamedPointLocal("P4").y, this.getNamedPointLocal("P4").z);
        this.getNamedPointLocal("CAD_P1").set(0.0f, this.getYDimension() / 2.0f, 0.0f);
        this.getNamedPointLocal("CAD_P2").set(0.0f, this.getYDimension() / 2.0f, this.getZDimension());
        this.getNamedPointLocal("CAD_POS").set(0.0f, 0.0f, this.getZDimension() / 2.0f);
        this.getNamedPointLocal("TAB_CONT_LOCATION_A").set(0.0f, 1.0f, 0.0f);
        this.getNamedPointLocal("TAB_CONT_LOCATION_B").set(this.getNamedPointLocal("extEndPoint").x, this.getNamedPointLocal("extEndPoint").y + 1.0f, this.getNamedPointLocal("extEndPoint").z);
    }
    
    public void solve() {
        ICDUtilities.validateBeamBreakLocationForExtrusion(this);
        this.setSubExtrusionDimension();
        super.solve();
    }
    
    private void setSubExtrusionDimension() {
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("breakLocation");
        String attributeValueAsString = "regular regular regular";
        if (this.getAttributeObject("ICD_SubExtrusionTypesForBreaks") != null && this.getAttributeValueAsString("ICD_SubExtrusionTypesForBreaks").length() > 0) {
            attributeValueAsString = this.getAttributeValueAsString("ICD_SubExtrusionTypesForBreaks");
        }
        final String[] split = attributeValueAsString.split(" ");
        final EntityObject childByLWType = this.getChildByLWType("ICD_Sub1_InnerExtrusion_Type");
        if (childByLWType instanceof ICDSubInternalExtrusion) {
            ((ICDSubInternalExtrusion)childByLWType).setZDimension(attributeValueAsFloat);
        }
        if ("1".equals(this.getAttributeValueAsString("NumberOfJoints"))) {
            final EntityObject childByLWType2 = this.getChildByLWType("ICD_Sub2_InnerExtrusion_Type");
            if (childByLWType2 instanceof ICDSubInternalExtrusion) {
                ((ICDSubInternalExtrusion)childByLWType2).setZDimension(this.getZDimension() - attributeValueAsFloat - 1.0f);
                childByLWType2.applyChangesForAttribute("ICD_Extrusion_Type", split[1]);
            }
            final EntityObject childByLWType3 = this.getChildByLWType("ICD_Sub1_InnerExtrusion_Type");
            if (childByLWType3 != null) {
                childByLWType3.applyChangesForAttribute("ICD_Extrusion_Type", split[0]);
            }
        }
        else if ("2".equals(this.getAttributeValueAsString("NumberOfJoints"))) {
            final EntityObject childByLWType4 = this.getChildByLWType("ICD_Sub1_InnerExtrusion_Type");
            if (childByLWType4 != null) {
                childByLWType4.applyChangesForAttribute("ICD_Extrusion_Type", split[0]);
            }
            final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("breakLocation2");
            final EntityObject childByLWType5 = this.getChildByLWType("ICD_Sub2_InnerExtrusion_Type");
            if (childByLWType5 instanceof ICDSubInternalExtrusion) {
                ((ICDSubInternalExtrusion)childByLWType5).setZDimension(attributeValueAsFloat2 - attributeValueAsFloat - 1.0f);
                childByLWType5.applyChangesForAttribute("ICD_Extrusion_Type", split[1]);
            }
            final EntityObject childByLWType6 = this.getChildByLWType("ICD_Sub3_InnerExtrusion_Type");
            if (childByLWType6 instanceof ICDSubInternalExtrusion) {
                ((ICDSubInternalExtrusion)childByLWType6).setZDimension(this.getZDimension() - attributeValueAsFloat2 - 1.0f);
                childByLWType6.applyChangesForAttribute("ICD_Extrusion_Type", split[2]);
            }
        }
    }
    
    private void setExtrusionType(final String currentValueAsString) {
        if (this.getAttributeObject("ICD_SubExtrusionTypesForBreaks") != null) {
            this.getAttributeObject("ICD_SubExtrusionTypesForBreaks").setCurrentValueAsString(currentValueAsString);
        }
    }
    
    protected void draw2DShapeNode(final int n, final Ice2DContainer ice2DContainer) {
        final Rectangle2D.Float shape = new Rectangle2D.Float(0.0f, 0.0f, this.getXDimension(), this.getZDimension());
        if (this.shapeNode == null) {
            ice2DContainer.add((Ice2DNode)(this.shapeNode = new Ice2DShapeNode(this.getLayerName(), (TransformableEntity)this, this.calculate2DShapeMatrix(), (Shape)shape)));
        }
        else {
            this.shapeNode.setShape((Shape)shape);
            this.shapeNode.setWorldSpaceMatrix(this.calculate2DShapeMatrix());
            if (!ice2DContainer.containsNode((Ice2DNode)this.shapeNode)) {
                ice2DContainer.add((Ice2DNode)this.shapeNode);
            }
        }
        this.shapeNode.setSelectable(false);
        final String attributeValueAsString = this.getAttributeValueAsString("Tile2DColor");
        Color fillColor;
        if (attributeValueAsString != null) {
            try {
                final StringTokenizer stringTokenizer = new StringTokenizer(attributeValueAsString.replace("(", "").replace(")", ""), ",");
                fillColor = new Color(Integer.parseInt(stringTokenizer.nextToken().trim()), Integer.parseInt(stringTokenizer.nextToken().trim()), Integer.parseInt(stringTokenizer.nextToken().trim()));
            }
            catch (Exception ex) {
                System.out.println("Automatically Generated Exception Log(ICDBottomExtrusion.java,226)[" + ex.getClass() + "]: " + ex.getMessage());
                ex.printStackTrace();
                fillColor = new Color(75, 64, 53);
            }
        }
        else {
            fillColor = new Color(75, 64, 53);
        }
        this.shapeNode.setFillColor(fillColor);
    }
    
    private Matrix4f calculate2DShapeMatrix() {
        this.shapeMatrix.setIdentity();
        this.shapeMatrix.mul(this.getEntWorldSpaceMatrix());
        this.shapeMatrix.mul(this.getEntitySVGPlanTransformMatrix());
        return this.shapeMatrix;
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
    
    public boolean draw2D() {
        final PanelInterface parentPanel = this.getParentPanel();
        return super.draw2D() && parentPanel != null && parentPanel.isCorePanel() && this.isChaseTube();
    }
    
    public void draw2D(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        if (this.draw2D() && this.isDirty(n)) {
            this.draw2DShapeNode(n, ice2DContainer);
        }
        super.draw2D(n, ice2DContainer, solutionSetting);
    }
    
    protected void addSimpleSnapTargets(final SimpleSnapTargetCollection collection) {
        super.addSimpleSnapTargets(collection);
        if (this.shouldBuildSnappingRules()) {
            collection.addWorldSpaceSnapPoint("WALL1", this.getNamedPointWorld("PP1"), Float.valueOf(this.getRotationWorldSpace() + (float)Math.toRadians(180.0)));
            collection.addWorldSpaceSnapPoint("WALL2", this.getNamedPointWorld("PP3"), Float.valueOf(this.getRotationWorldSpace() + (float)Math.toRadians(180.0)));
            collection.addWorldSpaceSnapPoint("WALL3", this.getNamedPointWorld("PP4"), Float.valueOf(this.getRotationWorldSpace()));
            collection.addWorldSpaceSnapPoint("WALL4", this.getNamedPointWorld("PP2"), Float.valueOf(this.getRotationWorldSpace()));
        }
    }
    
    private boolean shouldBuildSnappingRules() {
        return this.getParentByClassRecursive(ICDSubFrameSideContainer.class) == null;
    }
    
    public float getAttributeValueAsFloat(final String anObject) {
        if ("P1".equals(anObject)) {
            return this.getLength() * super.getAttributeValueAsFloat(anObject);
        }
        return super.getAttributeValueAsFloat(anObject);
    }
    
    public AdjustmentValue getBottomExtrusionAdjValue(final String s) {
        final PanelInterface parentPanel = this.getParentPanel();
        if (parentPanel != null && !parentPanel.isCorePanel()) {
            return new AdjustmentValue((OptionObject)null, -6.35f);
        }
        return new AdjustmentValue((OptionObject)null, -31.75f);
    }
    
    public void modifyCurrentOption() {
        this.validateIndicators();
        super.modifyCurrentOption();
    }
    
    private void validateIndicators() {
        final PanelInterface parentPanel = this.getParentPanel();
        String attributeValueAsString = "regular regular regular";
        if (this.getAttributeObject("ICD_SubExtrusionTypesForBreaks") != null && this.getAttributeValueAsString("ICD_SubExtrusionTypesForBreaks").length() > 0) {
            attributeValueAsString = this.getAttributeValueAsString("ICD_SubExtrusionTypesForBreaks");
        }
        final String[] split = attributeValueAsString.split(" ");
        String s = "none";
        if (parentPanel != null && parentPanel.isCorePanel()) {
            s = split[0];
            if (parentPanel instanceof ICDPanel) {
                final TileInterface bottomTile = ((ICDPanel)parentPanel).getBottomTile();
                if (bottomTile != null && ((ICDTile)bottomTile).withoutFrame()) {
                    s = "none";
                }
            }
        }
        this.createNewAttribute("ICD_Extrusion_Type", s);
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("breakLocation");
        if (attributeValueAsFloat < 2.0f || attributeValueAsFloat > this.getZDimension() - 2.0f) {
            this.createNewAttribute("specialInternalExtrusion", "No");
        }
    }
    
    protected boolean shouldUseNewFinishSystem() {
        return true;
    }
    
    protected void calculateNamedScales() {
        super.calculateNamedScales();
        this.getNamedScaleLocal("SVG_elevation_scale").set(1.0f, 1.0f, this.getZDimension());
        this.getNamedScaleLocal("CAD_SCL").set(1.0f, this.getZDimension(), 1.0f);
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
        return !"true".equals(this.getAttributeValueAsString("isAssembled"));
    }
    
    public Vector<Ice2DPaintableNode> getAssemblyIcons(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        final Rectangle2D.Float float1 = new Rectangle2D.Float(0.0f, 0.0f, this.getZDimension(), this.getYDimension());
        final Matrix4f matrix4f2 = new Matrix4f();
        matrix4f2.setIdentity();
        matrix4f2.mul(this.getEntWorldSpaceMatrix());
        final Matrix4f matrix4f3 = new Matrix4f();
        matrix4f3.setIdentity();
        matrix4f3.rotY(-1.5707964f);
        matrix4f2.mul(matrix4f3);
        final Matrix4f matrix4f4 = new Matrix4f();
        matrix4f4.setIdentity();
        matrix4f4.setTranslation(new Vector3f(0.0f, 1.25f, 0.0f));
        matrix4f2.mul(matrix4f4);
        Object e = null;
        if (!this.isCurvedExtrusion()) {
            e = new Ice2DShapeNode(this.getLayerName(), (TransformableEntity)this, matrix4f2, (Shape)float1);
            ((Ice2DShapeNode)e).setFillColor(Color.lightGray);
        }
        final Matrix4f matrix4f5 = new Matrix4f();
        matrix4f5.setIdentity();
        matrix4f5.mul(this.getEntWorldSpaceMatrix());
        matrix4f5.mul(matrix4f3);
        final Matrix4f matrix4f6 = new Matrix4f();
        matrix4f6.setIdentity();
        matrix4f6.setTranslation(new Vector3f(this.getZDimension() / 2.0f, 0.0f, 0.0f));
        matrix4f5.mul(matrix4f6);
        final Ice2DTextNode e2 = new Ice2DTextNode(this.getLayerName(), (TransformableEntity)this, matrix4f5, Math.round(this.getZDimension()) + "", 3);
        final Vector<Ice2DPaintableNode> vector = new Vector<Ice2DPaintableNode>();
        vector.add((Ice2DPaintableNode)e);
        vector.add((Ice2DPaintableNode)e2);
        return vector;
    }
    
    public List<IceOutputNode> getPlotOutputNodes(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        if (this.getCurrentOption().getId().contains("None") || this.isSpecialInternalExtrusion()) {
            return null;
        }
        if (!MathUtilities.isSameFloat(MathUtilities.convertSpaces(new Point3f(), (EntityObject)this, (EntityObject)this.getParent(ICDILine.class)).y, 0.0f, 0.001f)) {
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
    
    public boolean isSpecialInternalExtrusion() {
        return "Yes".equals(this.getAttributeValueAsString("specialInternalExtrusion"));
    }
    
    public boolean isCurvedExtrusion() {
        final String id = this.getCurrentOption().getId();
        return id != null && (id.indexOf("Curved") >= 0 || id.indexOf("curved") >= 0);
    }
    
    public boolean isAngledExtrusion() {
        final String id = this.getCurrentOption().getId();
        return id != null && (id.indexOf("Angled") >= 0 || id.indexOf("angled") >= 0);
    }
    
    public boolean shouldDrawAssembly() {
        final String sku = this.getSKU();
        if (sku == null || sku.equals("")) {
            return false;
        }
        final ICDPanel icdPanel = (ICDPanel)this.getParent(ICDPanel.class);
        return (icdPanel == null || !icdPanel.isSlopedPanel()) && !this.isFakePart();
    }
    
    public boolean isAssembled() {
        return "true".equals(this.getAttributeValueAsString("isAssembled"));
    }
    
    public boolean doesParticipateInJointIntersection() {
        return !this.isNoneExtrusion();
    }
    
    public boolean isNoneExtrusion() {
        return "None".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Extrusion_Type"));
    }
    
    public boolean isManufacturerReportable() {
        return !this.isSpecialInternalExtrusion();
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
        vector.add(this.getAssemblyLineScript(new Point3f(0.0f, 0.0f, 0.0f)));
        if (!this.isAssembled()) {
            vector.add(this.getAssemblyLineScript(new Point3f(0.0f, 0.5f, 0.0f)));
        }
        String string = Math.round(this.getZDimension()) + "";
        if (this.isCurvedExtrusion()) {
            string = "24-Curved";
        }
        vector.add("TX:SS(" + string + ":" + 5 + ":" + 0 + ":" + 2 + ":CADELP8)");
        return vector;
    }
    
    private String getAssemblyLineScript(final Point3f point3f) {
        return "LI:SS(1:CAD_P1(" + point3f.x + "," + point3f.y + "," + point3f.z + "):CAD_P2(" + point3f.x + "," + point3f.y + "," + point3f.z + "))";
    }
    
    public void setupNamedScales() {
        super.setupNamedScales();
        this.addNamedScale("CAD_SCL", new Vector3f());
    }
    
    public boolean drawCAD() {
        return this.isChaseTube() && super.drawCAD();
    }
    
    public boolean isChaseTube() {
        return this.getParent(ICDSubFrame.class) != null;
    }
    
    public boolean hasSubExtrusion() {
        return this.getChildByClass(ICDSubInternalExtrusion.class) != null;
    }
    
    public boolean addTabbing(final int n) {
        final ICDSubFrame icdSubFrame = (ICDSubFrame)this.getParent((TypeFilter)new ICDSubFrameFilter());
        if (icdSubFrame != null) {
            final EntityObject parentEntity = icdSubFrame.getParentEntity();
            if (parentEntity != null) {
                final ICDPanel icdPanel = (ICDPanel)parentEntity.getParent((TypeFilter)new ICDPanelFilter());
                if (icdPanel != null && icdPanel.isSuspendedChase()) {
                    return 1 == n;
                }
            }
        }
        return icdSubFrame == null || !icdSubFrame.getOptionId().contains("Chase");
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
    
    public void SetBreakLocationForBeam(final List<Pair<Float, Integer>> breakLocations) {
        this.breakLocations = breakLocations;
    }
    
    public List<Pair<Float, Integer>> getBreakLocationsForBeam() {
        return this.breakLocations;
    }
    
    public void handleAttributeChange(final String s, final String s2) {
        ICDUtilities.handleAttributeChange((EntityObject)this, s, s2);
    }
    
    public boolean isFakePart() {
        return this.getCurrentOption().getId().toLowerCase().indexOf("special") > -1;
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
}
