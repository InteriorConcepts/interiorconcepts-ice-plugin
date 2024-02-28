package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import java.util.Iterator;
import net.dirtt.icelib.main.ElevationEntity;
import icd.warnings.WarningReason0282;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationUtilities;
import com.iceedge.icd.utilities.ICDExtrusionUtilities;
import net.dirtt.icelib.report.Report;
import net.iceedge.icecore.basemodule.interfaces.lightweight.Paintable;
import net.dirtt.icebox.iceoutput.core.IceOutputTextNode;
import net.dirtt.icebox.iceoutput.core.IceOutputShapeNode;
import net.iceedge.catalogs.icd.ICDILine;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import net.dirtt.icebox.canvas2d.Ice2DTextNode;
import javax.vecmath.Vector3f;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import java.util.Vector;
import javax.vecmath.Matrix4f;
import net.dirtt.icelib.main.TransformableEntity;
import org.xith3d.scenegraph.Group;
import net.iceedge.icecore.basemodule.interfaces.panels.TileInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelInterface;
import net.dirtt.icelib.main.TypeableEntity;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelSegmentInterface;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicPanelSubILine;
import net.iceedge.catalogs.icd.ICDSegment;
import net.dirtt.utilities.MathUtilities;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import net.iceedge.catalogs.icd.intersection.ICDIntersection;
import net.iceedge.catalogs.icd.ICDUtilities;
import net.iceedge.icecore.basemodule.baseclasses.BasicSegment;
import net.dirtt.icelib.main.AdjustmentValue;
import javax.vecmath.Tuple3f;
import javax.vecmath.Point3f;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicSurroundingStickExtrusion;
import net.dirtt.icelib.main.EntityObject;
import java.util.ArrayList;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.utilities.Pair;
import java.util.List;
import net.dirtt.icebox.canvas2d.Ice2DShapeNode;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import com.iceedge.icd.entities.extrusions.ICDExtrusionInterface;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintable;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicTopExtrusion;

public class ICDTopExtrusion extends BasicTopExtrusion implements AssemblyPaintable, JointIntersectable, ICDHorizontalBreakableExtrusion, ICDExtrusionInterface, ICDManufacturingReportable
{
    private static final String ICD_EXTRUSION_TYPE = "ICD_Extrusion_Type";
    public static final String SPACE = " ";
    private Ice2DShapeNode assemblyNode;
    private List<Pair<Float, Integer>> breakLocations;
    boolean splitTwice;
    
    public ICDTopExtrusion(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.breakLocations = new ArrayList<Pair<Float, Integer>>();
        this.splitTwice = false;
    }
    
    public Object clone() {
        return this.buildClone(new ICDTopExtrusion(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDTopExtrusion buildClone(final ICDTopExtrusion icdTopExtrusion) {
        super.buildClone((BasicTopExtrusion)icdTopExtrusion);
        return icdTopExtrusion;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDTopExtrusion(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDTopExtrusion buildFrameClone(final ICDTopExtrusion icdTopExtrusion, final EntityObject entityObject) {
        super.buildFrameClone((BasicSurroundingStickExtrusion)icdTopExtrusion, entityObject);
        return icdTopExtrusion;
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("EXT_POS").set(0.0f, -1.0f, 0.0f);
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("breakLocation");
        float attributeValueAsFloat2 = 0.0f;
        final int attributeValueAsInt = this.getAttributeValueAsInt("NumberOfJoints");
        if (attributeValueAsInt == 2) {
            attributeValueAsFloat2 = this.getAttributeValueAsFloat("breakLocation2");
        }
        this.getNamedPointLocal("ext1BP").set(0.0f, -0.5f, 0.0f);
        this.getNamedPointLocal("ext2BP").set(0.0f, -0.5f, attributeValueAsFloat + 0.5f);
        this.getNamedPointLocal("jointBP").set(0.0f, -0.5f, attributeValueAsFloat);
        if (attributeValueAsInt == 2) {
            this.getNamedPointLocal("ext3BP").set(0.0f, -0.5f, attributeValueAsFloat2 + 0.5f);
            this.getNamedPointLocal("joint2BP").set(0.0f, -0.5f, attributeValueAsFloat2);
        }
        this.getNamedPointLocal("CAD_P1").set(0.0f, -this.getYDimension() / 2.0f, 0.0f);
        this.getNamedPointLocal("CAD_P2").set(0.0f, -this.getYDimension() / 2.0f, this.getZDimension());
        this.getNamedPointLocal("TAB_CONT_LOCATION_A").set(0.0f, 0.0f, 0.0f);
        final Point3f namedPointLocal = this.getNamedPointLocal("extEndPoint");
        this.getNamedPointLocal("TAB_CONT_LOCATION_B").set(namedPointLocal.x, namedPointLocal.y, namedPointLocal.z);
        if (this.isChaseTopExtrusion()) {
            final ICDSubFrameSideContainer icdSubFrameSideContainer = (ICDSubFrameSideContainer)this.getParent(ICDSubFrameSideContainer.class);
            final float chaseOffset = ((ICDPanel)icdSubFrameSideContainer.getParent(ICDPanel.class)).getChaseOffset(icdSubFrameSideContainer.getSide());
            final Point3f point3f = (Point3f)this.getNamedPointLocal("P2").clone();
            final Point3f point3f2 = (Point3f)this.getNamedPointLocal("P4").clone();
            final Point3f point3f3 = point3f;
            point3f3.x += chaseOffset;
            final Point3f point3f4 = point3f2;
            point3f4.x += chaseOffset;
            this.getNamedPointLocal("PANEL_P2").set((Tuple3f)point3f);
            this.getNamedPointLocal("PANEL_P4").set((Tuple3f)point3f2);
        }
        else {
            this.getNamedPointLocal("PANEL_P2").set((Tuple3f)this.getNamedPointLocal("P2"));
            this.getNamedPointLocal("PANEL_P4").set((Tuple3f)this.getNamedPointLocal("P4"));
        }
    }
    
    protected void calculateGeometricCenter() {
        super.calculateGeometricCenter();
        this.setGeometricCenterPointLocal(new Point3f(0.0f, -this.getYDimension() / 2.0f, this.getZDimension() / 2.0f));
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
        this.addNamedPoint("PANEL_P2", new Point3f());
        this.addNamedPoint("PANEL_P4", new Point3f());
    }
    
    public float getAttributeValueAsFloat(final String anObject) {
        if ("P1".equals(anObject)) {
            return this.getLength() * super.getAttributeValueAsFloat(anObject);
        }
        return super.getAttributeValueAsFloat(anObject);
    }
    
    public AdjustmentValue getTopExtrusionAdjValue(final String s) {
        return super.getTopExtrusionAdjValue(s);
    }
    
    public AdjustmentValue getAdjustmentAttributeCurrentValue(final String s, final String anObject) {
        if (this.isCurvedExtrusion()) {
            return new AdjustmentValue((OptionObject)null, -31.75f);
        }
        final ICDPanel icdPanel = (ICDPanel)this.getParent(ICDPanel.class);
        if (icdPanel != null && icdPanel.isUnderChase() && icdPanel.isCorePanel() && "ICD_InnerExtrusionSet".equals(anObject)) {
            return new AdjustmentValue((OptionObject)null, -38.1f);
        }
        final AdjustmentValue adjustmentAttributeCurrentValue = super.getAdjustmentAttributeCurrentValue(s, anObject);
        if (adjustmentAttributeCurrentValue.getOption() != null) {
            return adjustmentAttributeCurrentValue;
        }
        return new AdjustmentValue((OptionObject)null, -31.75f);
    }
    
    public void breakHorizontalExtrusion(float n, final boolean b) {
        if (!b) {
            n = this.getZDimension() - n;
        }
        final ICDPanel icdPanel = (ICDPanel)this.getParentPanel();
        final BasicSegment basicSegment = (BasicSegment)this.getParentSegment();
        if (icdPanel == null || basicSegment == null) {
            return;
        }
        if (icdPanel.isCorePanel()) {
            this.validateBreaksForTopExtrusionInCorePanel(basicSegment, icdPanel, n);
        }
        else if (!icdPanel.isCorePanel() && !icdPanel.isTopPositionPanel()) {
            this.validateBreaksForTopExtrusionInMIddlePanel(basicSegment, icdPanel);
        }
        else if (!icdPanel.isCorePanel() && icdPanel.isTopPositionPanel()) {
            this.validateBreaksForTopExtrusionInTopPanel(n);
        }
        ICDUtilities.applyBeamBreakLocationForExtrusion(this);
    }
    
    private void validateBreaksForTopExtrusionInCorePanel(final BasicSegment basicSegment, final ICDPanel icdPanel, final float n) {
        final String s = (this.getStackingPanel(basicSegment) != null) ? "step" : "regular";
        String extrusionType = s + " " + s + " " + s;
        float[] array = { 0.0f, 0.0f };
        float noFrameTileBreakLocation = 0.0f;
        final Point3f convertPointToWorldSpace = icdPanel.convertPointToWorldSpace(new Point3f(5.0f, 0.0f, 0.0f));
        final ICDIntersection icdIntersection = (ICDIntersection)basicSegment.getIntersectionForSegment(true);
        if (icdIntersection != null) {
            array = icdIntersection.getChaseOffsetAndHeightOnPointSide((Segment)basicSegment, convertPointToWorldSpace);
        }
        if (MathUtilities.isSameFloat(array[0], 0.0f, 1.5f)) {
            final ICDIntersection icdIntersection2 = (ICDIntersection)basicSegment.getIntersectionForSegment(false);
            if (icdIntersection2 != null) {
                array = icdIntersection2.getChaseOffsetAndHeightOnPointSide((Segment)basicSegment, convertPointToWorldSpace);
                array[0] = this.getZDimension() - array[0];
            }
        }
        if (!MathUtilities.isSameFloat(array[0], 0.0f, 1.5f)) {
            float n2;
            if (basicSegment.isFlipped()) {
                n2 = this.getZDimension() - array[0];
                extrusionType = s + " " + "chase" + " " + s;
            }
            else {
                n2 = array[0];
                extrusionType = "chase " + s + " " + s;
            }
            if (((ICDSegment)basicSegment).getNumberOfChaseAtPoint(this.convertPointToWorldSpace(new Point3f(0.0f, 0.0f, n2))) == 0) {
                array[0] = 0.0f;
                extrusionType = s + " " + s + " " + s;
            }
        }
        final ICDPanel stackingPanel = this.getStackingPanel(basicSegment);
        if (stackingPanel != null) {
            noFrameTileBreakLocation = stackingPanel.getNoFrameTileBreakLocation();
        }
        if (!MathUtilities.isSameFloat(n, 0.0f, 1.5f) && !MathUtilities.isSameFloat(n, this.getZDimension(), 1.5f)) {
            this.applyChangesForAttribute("specialInternalExtrusion", "Yes");
            this.createNewAttribute("breakLocation", n + "");
            this.createNewAttribute("NumberOfJoints", "1");
            if (MathUtilities.isSameFloat(array[0], 0.0f, 1.5f) || !MathUtilities.isSameFloat(array[1], icdPanel.getHeight() + 2.0f, 0.5f)) {
                this.setExtrusionType("step regular regular");
                return;
            }
            if (MathUtilities.isSameFloat(noFrameTileBreakLocation, 0.0f, 1.5f)) {
                this.setExtrusionType((stackingPanel != null) ? ("step " + s + " " + s) : ("chase " + s + " " + s));
                return;
            }
            if (array[0] < noFrameTileBreakLocation) {
                this.createNewAttribute("breakLocation", array[0] - 0.5f + "");
                this.createNewAttribute("breakLocation2", noFrameTileBreakLocation - 0.0f + "");
                extrusionType = "chase  " + s + " " + "step";
            }
            else {
                this.createNewAttribute("breakLocation", noFrameTileBreakLocation + "");
                this.createNewAttribute("breakLocation2", array[0] + 0.5f + "");
                extrusionType = "step " + s + " " + "chase";
            }
            this.createNewAttribute("NumberOfJoints", "2");
        }
        else {
            final boolean b = !MathUtilities.isSameFloat(noFrameTileBreakLocation, 0.0f, 1.5f);
            final boolean b2 = !MathUtilities.isSameFloat(array[0], 0.0f, 1.5f) && MathUtilities.isSameFloat(array[1], icdPanel.getHeight() + 2.0f, 0.5f);
            if (!b && !b2) {
                this.applyChangesForAttribute("specialInternalExtrusion", "No");
                this.createNewAttribute("breakLocation", n + "");
            }
        }
        this.setExtrusionType(extrusionType);
    }
    
    private ICDPanel getStackingPanel(final BasicSegment basicSegment) {
        ICDPanel icdPanel = null;
        final BasicPanelSubILine basicPanelSubILine = (BasicPanelSubILine)basicSegment.getPanelSubILine();
        if (basicPanelSubILine != null) {
            final PanelSegmentInterface stackingSegment = basicPanelSubILine.getStackingSegment();
            if (stackingSegment != null) {
                icdPanel = (ICDPanel)stackingSegment.getChildPanel();
            }
        }
        return icdPanel;
    }
    
    private void setExtrusionType(final String currentValueAsString) {
        if (this.getAttributeObject("ICD_SubExtrusionTypesForBreaks") != null) {
            this.getAttributeObject("ICD_SubExtrusionTypesForBreaks").setCurrentValueAsString(currentValueAsString);
        }
    }
    
    private void validateBreaksForTopExtrusionInMIddlePanel(final BasicSegment basicSegment, final ICDPanel icdPanel) {
        this.splitTwice = false;
        float noFrameTileBreakLocation = 0.0f;
        final ICDPanel abovePanel = icdPanel.getAbovePanel();
        if (abovePanel != null) {
            noFrameTileBreakLocation = abovePanel.getNoFrameTileBreakLocation();
        }
        final float noFrameTileBreakLocation2 = icdPanel.getNoFrameTileBreakLocation();
        if (!MathUtilities.isSameFloat(noFrameTileBreakLocation, 0.0f, 1.5f) && !MathUtilities.isSameFloat(noFrameTileBreakLocation2, 0.0f, 1.5f)) {
            this.splitTwice = true;
            final EntityObject childByLWType = this.getChildByLWType("ICD_Sub1_InnerExtrusion_Type");
            final EntityObject childByLWType2 = this.getChildByLWType("ICD_Sub2_InnerExtrusion_Type");
            final EntityObject childByLWType3 = this.getChildByLWType("ICD_Sub3_InnerExtrusion_Type");
            if (noFrameTileBreakLocation2 < noFrameTileBreakLocation) {
                this.createNewAttribute("breakLocation", noFrameTileBreakLocation2 + "");
                this.createNewAttribute("breakLocation2", noFrameTileBreakLocation + "");
                this.createNewAttribute("NumberOfJoints", "2");
                if (childByLWType != null) {
                    ((ICDSubInternalExtrusion)childByLWType).setZDimension(noFrameTileBreakLocation2 - 0.5f);
                    childByLWType.applyChangesForAttribute("ICD_Extrusion_Type", "none");
                }
                if (childByLWType2 != null) {
                    ((ICDSubInternalExtrusion)childByLWType2).setZDimension(noFrameTileBreakLocation - noFrameTileBreakLocation2 - 1.0f);
                    childByLWType2.applyChangesForAttribute("ICD_Extrusion_Type", "regular");
                }
                if (childByLWType3 != null) {
                    ((ICDSubInternalExtrusion)childByLWType3).setZDimension(this.getZDimension() - noFrameTileBreakLocation - 0.5f);
                    childByLWType3.applyChangesForAttribute("ICD_Extrusion_Type", "regular");
                }
            }
            else if (noFrameTileBreakLocation2 > noFrameTileBreakLocation) {
                this.createNewAttribute("breakLocation", noFrameTileBreakLocation + "");
                this.createNewAttribute("breakLocation2", noFrameTileBreakLocation2 + "");
                this.createNewAttribute("NumberOfJoints", "2");
                if (childByLWType != null) {
                    ((ICDSubInternalExtrusion)childByLWType).setZDimension(noFrameTileBreakLocation - 0.5f);
                    childByLWType.applyChangesForAttribute("ICD_Extrusion_Type", "regular");
                }
                if (childByLWType2 != null) {
                    ((ICDSubInternalExtrusion)childByLWType2).setZDimension(noFrameTileBreakLocation2 - noFrameTileBreakLocation - 1.0f);
                    childByLWType2.applyChangesForAttribute("ICD_Extrusion_Type", "regular");
                }
                if (childByLWType3 != null) {
                    ((ICDSubInternalExtrusion)childByLWType3).setZDimension(this.getZDimension() - noFrameTileBreakLocation2);
                    childByLWType3.applyChangesForAttribute("ICD_Extrusion_Type", "none");
                }
            }
        }
    }
    
    private void validateBreaksForTopExtrusionInTopPanel(final float n) {
        if (MathUtilities.isSameFloat(n, 0.0f, 1.5f) || MathUtilities.isSameFloat(n, this.getZDimension(), 1.5f)) {
            this.applyChangesForAttribute("specialInternalExtrusion", "No");
            this.createNewAttribute("breakLocation", n + "");
            this.setExtrusionType("regular regular regular");
        }
        else {
            this.applyChangesForAttribute("specialInternalExtrusion", "Yes");
            this.createNewAttribute("breakLocation", n + "");
            this.createNewAttribute("NumberOfJoints", "1");
            this.setExtrusionType("step regular regular");
        }
    }
    
    public void solve() {
        ICDUtilities.validateBeamBreakLocationForExtrusion(this);
        super.solve();
        this.setSubExtrusionDimension();
    }
    
    private void setSubExtrusionDimension() {
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("breakLocation");
        String attributeValueAsString = "regular regular regular";
        if (this.getAttributeObject("ICD_SubExtrusionTypesForBreaks") != null && this.getAttributeValueAsString("ICD_SubExtrusionTypesForBreaks").length() > 0) {
            attributeValueAsString = this.getAttributeValueAsString("ICD_SubExtrusionTypesForBreaks");
        }
        final String[] split = attributeValueAsString.split(" ");
        final PanelInterface parentPanel = this.getParentPanel();
        final boolean topPositionPanel = ((ICDPanel)parentPanel).isTopPositionPanel();
        final boolean corePanel = parentPanel.isCorePanel();
        if (parentPanel != null && !parentPanel.isCorePanel() && !topPositionPanel && this.splitTwice) {
            return;
        }
        final EntityObject childByLWType = this.getChildByLWType("ICD_Sub1_InnerExtrusion_Type");
        if (childByLWType instanceof ICDSubInternalExtrusion) {
            ((ICDSubInternalExtrusion)childByLWType).setZDimension(attributeValueAsFloat - 0.5f);
            String s = split[0];
            if (this.isExtrusionOverNoTileNoFrame((ICDPanel)parentPanel, (TypeableEntity)childByLWType)) {
                s = "none";
            }
            childByLWType.applyChangesForAttribute("ICD_Extrusion_Type", s);
            if (parentPanel != null && (!topPositionPanel || corePanel)) {
                childByLWType.applyChangesForAttribute("ICD_Extrusion_Type", split[0]);
            }
        }
        if ("1".equals(this.getAttributeValueAsString("NumberOfJoints"))) {
            final EntityObject childByLWType2 = this.getChildByLWType("ICD_Sub2_InnerExtrusion_Type");
            if (childByLWType2 instanceof ICDSubInternalExtrusion) {
                ((ICDSubInternalExtrusion)childByLWType2).setZDimension(this.getZDimension() - attributeValueAsFloat - 0.5f);
                String s2 = split[0];
                if (this.isExtrusionOverNoTileNoFrame((ICDPanel)parentPanel, (TypeableEntity)childByLWType2)) {
                    s2 = "none";
                }
                childByLWType2.applyChangesForAttribute("ICD_Extrusion_Type", s2);
                if (parentPanel != null && (!topPositionPanel || corePanel)) {
                    childByLWType2.applyChangesForAttribute("ICD_Extrusion_Type", "regular");
                }
            }
        }
        else if ("2".equals(this.getAttributeValueAsString("NumberOfJoints"))) {
            final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("breakLocation2");
            final EntityObject childByLWType3 = this.getChildByLWType("ICD_Sub2_InnerExtrusion_Type");
            if (childByLWType3 instanceof ICDSubInternalExtrusion) {
                ((ICDSubInternalExtrusion)childByLWType3).setZDimension(attributeValueAsFloat2 - attributeValueAsFloat - 1.0f);
                String s3 = split[1];
                if (!this.isFakeExt1()) {
                    s3 = "none";
                }
                childByLWType3.applyChangesForAttribute("ICD_Extrusion_Type", s3);
                if (parentPanel != null && (!topPositionPanel || corePanel)) {
                    childByLWType3.applyChangesForAttribute("ICD_Extrusion_Type", (this.getStackingPanel((BasicSegment)this.getParentSegment()) != null) ? "step" : "regular");
                }
            }
            final EntityObject childByLWType4 = this.getChildByLWType("ICD_Sub3_InnerExtrusion_Type");
            if (childByLWType4 instanceof ICDSubInternalExtrusion) {
                ((ICDSubInternalExtrusion)childByLWType4).setZDimension(this.getZDimension() - attributeValueAsFloat2 - 0.5f);
                String s4 = split[2];
                if (!this.isFakeExt1()) {
                    s4 = "none";
                }
                childByLWType4.applyChangesForAttribute("ICD_Extrusion_Type", s4);
                if (parentPanel != null && (!topPositionPanel || corePanel)) {
                    childByLWType4.applyChangesForAttribute("ICD_Extrusion_Type", ((ICDPanel)parentPanel).isStackPanel() ? "step" : "regular");
                }
            }
        }
    }
    
    private boolean isExtrusionOverNoTileNoFrame(final ICDPanel icdPanel, final TypeableEntity typeableEntity) {
        final TileInterface noTileWithoutFrame = icdPanel.getNoTileWithoutFrame();
        if (noTileWithoutFrame != null) {
            float n;
            float n2;
            if (noTileWithoutFrame.getRotationWorldSpace() == 0.0 || Math.abs(noTileWithoutFrame.getRotationWorldSpace() - 3.141592653589793) < 1.0) {
                n = noTileWithoutFrame.getBasePointWorldSpace().x;
                n2 = typeableEntity.getBasePointWorldSpace().x;
            }
            else {
                n = noTileWithoutFrame.getBasePointWorldSpace().y;
                n2 = typeableEntity.getBasePointWorldSpace().y;
            }
            if (Math.abs(n - n2) < 1.0f) {
                return true;
            }
        }
        return false;
    }
    
    public void setFakeExtrusion(final boolean b) {
        this.applyChangesForAttribute("FakeExt1", b + "");
    }
    
    private boolean isFakeExt1() {
        return this.getAttributeValueAsBoolean("FakeExt1", false);
    }
    
    public void draw3D(final Group group, final int n) {
        super.draw3D(group, n);
    }
    
    protected boolean shouldUseNewFinishSystem() {
        return true;
    }
    
    protected void calculateNamedScales() {
        super.calculateNamedScales();
        this.getNamedScaleLocal("SVG_elevation_scale").set(1.0f, 1.0f, this.getZDimension());
    }
    
    protected void calcSvgElevationIP() {
        super.calcSvgElevationIP();
        this.svgElevationIP = new Point3f(0.0f, -1.0f, 0.0f);
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
        final Rectangle2D.Float float1 = new Rectangle2D.Float(0.0f, -yDimension, this.getZDimension(), yDimension);
        final Matrix4f matrix4f2 = new Matrix4f();
        matrix4f2.setIdentity();
        matrix4f2.mul(this.getEntWorldSpaceMatrix());
        final Matrix4f matrix4f3 = new Matrix4f();
        matrix4f3.setIdentity();
        matrix4f3.rotY(-1.5707964f);
        matrix4f2.mul(matrix4f3);
        Object e = null;
        if (!this.isCurvedExtrusion()) {
            e = new Ice2DShapeNode(this.getLayerName(), (TransformableEntity)this, matrix4f2, (Shape)float1);
            ((Ice2DShapeNode)e).setFillColor(Color.lightGray);
        }
        final Matrix4f matrix4f4 = new Matrix4f();
        matrix4f4.setIdentity();
        matrix4f4.mul(this.getEntWorldSpaceMatrix());
        matrix4f4.mul(matrix4f3);
        final Matrix4f matrix4f5 = new Matrix4f();
        matrix4f5.setIdentity();
        matrix4f5.setTranslation(new Vector3f(this.getZDimension() / 2.0f, 0.0f, 0.0f));
        matrix4f4.mul(matrix4f5);
        final Ice2DTextNode e2 = new Ice2DTextNode(this.getLayerName(), (TransformableEntity)this, matrix4f4, Math.round(this.getZDimension()) + "", 3);
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
        final Rectangle2D.Float float1 = new Rectangle2D.Float(0.0f, -this.getYDimension(), this.getZDimension(), this.getYDimension());
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
    
    public boolean addTabbing(final int n) {
        if (ICDExtrusionUtilities.isPartOfChase((EntityObject)this)) {
            final ICDSubFrameSideContainer icdSubFrameSideContainer = (ICDSubFrameSideContainer)this.getParent(ICDSubFrameSideContainer.class);
            if (icdSubFrameSideContainer != null) {
                return ((ICDPanel)icdSubFrameSideContainer.getParent(ICDPanel.class)).hasChase(n) && icdSubFrameSideContainer.getSide() == n;
            }
        }
        return super.validateTabs();
    }
    
    public boolean isChaseTopExtrusion() {
        boolean b = false;
        if (this.getParentPanel() instanceof ICDSubFrameSideContainer) {
            b = true;
        }
        return b;
    }
    
    public boolean panelHasElectrical() {
        final PanelInterface parentPanel = this.getParentPanel();
        if (parentPanel instanceof ICDSubFrameSideContainer) {
            final ICDPanel parentPanel2 = ((ICDSubFrameSideContainer)parentPanel).getParentPanel();
            if (parentPanel2 instanceof ICDPanel) {
                return parentPanel2.panelHasElectrical();
            }
        }
        return false;
    }
    
    public boolean isElectrifiedSideAChase() {
        boolean b = false;
        if (this.isChaseTopExtrusion() && this.panelHasElectrical()) {
            final PanelInterface parentPanel = this.getParentPanel();
            if (parentPanel instanceof ICDSubFrameSideContainer) {
                b = (((ICDSubFrameSideContainer)parentPanel).getSide() == 0);
            }
        }
        return b;
    }
    
    public boolean isManufacturerReportable() {
        return super.isManufacturerReportable() && !this.isElectrifiedSideAChase();
    }
    
    public boolean isSpecialInternalExtrusion() {
        return "Yes".equals(this.getAttributeValueAsString("specialInternalExtrusion"));
    }
    
    public void modifyCurrentOption() {
        this.validateIndicators();
        super.modifyCurrentOption();
        this.setSubExtrusionDimension();
    }
    
    private void validateIndicators() {
        final PanelInterface parentPanel = this.getParentPanel();
        String s = "none";
        if (parentPanel instanceof ICDPanel && ((ICDPanel)parentPanel).isDoorPanelWithTopStack()) {
            s = "regular";
        }
        this.createNewAttribute("ICD_Extrusion_Type", s);
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("breakLocation");
        if (attributeValueAsFloat < 2.0f || attributeValueAsFloat > this.getZDimension() - 2.0f) {
            this.createNewAttribute("specialInternalExtrusion", "No");
        }
    }
    
    public boolean isCurvedExtrusion() {
        final String id = this.getCurrentOption().getId();
        return id != null && (id.indexOf("Curved") >= 0 || id.indexOf("curved") >= 0);
    }
    
    public boolean shouldDrawAssembly() {
        final String sku = this.getSKU();
        if (sku == null || sku.equals("")) {
            return false;
        }
        final ICDPanel icdPanel = (ICDPanel)this.getParent(ICDPanel.class);
        return (icdPanel == null || !icdPanel.isSlopedPanel()) && !this.getCurrentOption().getId().contains("None") && !this.isSpecialInternalExtrusion() && !ICDAssemblyElevationUtilities.isExtrusionOnChase((TransformableEntity)this);
    }
    
    public boolean isAssembled() {
        return "true".equals(this.getAttributeValueAsString("isAssembled"));
    }
    
    public boolean doesParticipateInJointIntersection() {
        return !this.isSpecialInternalExtrusion();
    }
    
    public void handleWarnings() {
        WarningReason0282.addRequiredWarning((TransformableEntity)this);
    }
    
    public boolean isAngledExtrusion() {
        final String id = this.getCurrentOption().getId();
        return id != null && (id.indexOf("Angled") >= 0 || id.indexOf("angled") >= 0);
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
    
    public boolean drawCAD() {
        return false;
    }
    
    public ICDSubInternalExtrusion getSubInternalExtrusion(final float n) {
        for (final ICDSubInternalExtrusion icdSubInternalExtrusion : this.getChildrenByClass(ICDSubInternalExtrusion.class, false)) {
            final float z = icdSubInternalExtrusion.getBasePoint3f().z;
            if (n >= z && n < z + icdSubInternalExtrusion.getZDimension()) {
                return icdSubInternalExtrusion;
            }
        }
        return null;
    }
    
    public List<Pair<Float, Integer>> getBreakLocationsForBeam() {
        return this.breakLocations;
    }
    
    public void SetBreakLocationForBeam(final List<Pair<Float, Integer>> breakLocations) {
        this.breakLocations = breakLocations;
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
