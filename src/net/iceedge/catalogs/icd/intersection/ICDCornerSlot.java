// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.intersection;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.utilities.TypeFilter;
import net.iceedge.icecore.basemodule.baseclasses.BasicIntersectionFilter;
import net.iceedge.icecore.basemodule.baseclasses.BasicIntersection;
import javax.vecmath.Tuple3f;
import net.dirtt.icecad.cadtree.ICadTreeNode;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationUtilities;
import net.dirtt.menus.Menus;
import net.dirtt.icelib.main.ElevationEntity;
import javax.vecmath.Vector3f;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import javax.vecmath.Matrix4f;
import net.dirtt.icelib.main.attributes.Attribute;
import net.dirtt.icelib.main.LightWeightTypeObject;
import net.dirtt.utilities.MathUtilities;
import javax.vecmath.Point3f;
import net.iceedge.catalogs.icd.panel.ICDSubInternalExtrusion;
import net.dirtt.icebox.iceoutput.core.IceOutputShapeNode;
import java.util.ArrayList;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import java.util.List;
import net.dirtt.icelib.main.Solution;
import java.util.Vector;
import java.util.Collection;
import net.dirtt.icebox.canvas2d.Ice2DNode;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icebox.canvas2d.Ice2DArrowNode;
import java.awt.Color;
import org.apache.log4j.Logger;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintable;
import net.dirtt.icebox.iceoutput.core.PlotPaintable;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;

public class ICDCornerSlot extends TransformableTriggerUser implements PlotPaintable, AssemblyPaintable, ICDManufacturingReportable
{
    private static final long serialVersionUID = -1333295942540150140L;
    private static final Logger logger;
    private static float ARROW_STROKE_WIDTH;
    private static Color ARROW_COLOR;
    private static float ARROW_LENGTH;
    private Ice2DArrowNode arrowNode;
    
    public ICDCornerSlot(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.setupNamedPoints();
        this.setupNamedRotations();
    }
    
    public Object clone() {
        return this.buildClone(new ICDCornerSlot(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDCornerSlot buildClone(final ICDCornerSlot icdCornerSlot) {
        super.buildClone((TransformableTriggerUser)icdCornerSlot);
        return icdCornerSlot;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDCornerSlot(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDCornerSlot buildFrameClone(final ICDCornerSlot icdCornerSlot, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdCornerSlot, entityObject);
        return icdCornerSlot;
    }
    
    public void draw2D(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        if (this.isDirty(n)) {
            if (this.draw2D()) {
                final Color arrow_COLOR = ICDCornerSlot.ARROW_COLOR;
                if (this.arrowNode == null) {
                    this.arrowNode = new Ice2DArrowNode(this.getLayerName(), (TransformableEntity)this, this.getEntWorldSpaceMatrix(), ICDCornerSlot.ARROW_LENGTH, arrow_COLOR, ICDCornerSlot.ARROW_STROKE_WIDTH);
                }
                else {
                    this.arrowNode.setArrowLength(ICDCornerSlot.ARROW_LENGTH);
                    this.arrowNode.setMatrix(this.getEntWorldSpaceMatrix());
                    this.arrowNode.setColor(arrow_COLOR);
                    this.arrowNode.setStrokeWidth(ICDCornerSlot.ARROW_STROKE_WIDTH);
                }
                ice2DContainer.add((Ice2DNode)this.arrowNode);
            }
            else {
                this.destroy2D();
            }
            this.drawBoundingCubeNode(ice2DContainer);
            this.drawTriggerIndicatorsDebug(ice2DContainer, (Collection)null);
            this.drawTriggerSpaceDebug(ice2DContainer, (Collection)null);
        }
    }
    
    public void addToCutSolution(final Vector vector, final Vector vector2, final Solution solution, final boolean b) {
    }
    
    public void cutFromTree2D() {
        super.cutFromTree2D();
        if (this.arrowNode != null) {
            this.arrowNode.removeFromParent();
        }
    }
    
    public void destroy2DAsset() {
        super.destroy2DAsset();
        this.arrowNode = null;
    }
    
    public boolean draw2D() {
        return super.draw2D() && this.isSlotted();
    }
    
    public boolean isSlotted() {
        return this.getAttributeValueAsBoolean("isCornerSlotSet", false) || this.isSlotedByUser();
    }
    
    public List<IceOutputNode> getPlotOutputNodes() {
        final ArrayList<IceOutputShapeNode> list = (ArrayList<IceOutputShapeNode>)new ArrayList<IceOutputNode>();
        if (this.arrowNode != null) {
            list.add(new IceOutputShapeNode(this.arrowNode.getArrowShape(), this.getEntWorldSpaceMatrix()));
        }
        return (List<IceOutputNode>)list;
    }
    
    public ICDSubInternalExtrusion getParentTube() {
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity instanceof ICDSubInternalExtrusion) {
            return (ICDSubInternalExtrusion)parentEntity;
        }
        return null;
    }
    
    protected void calculateDimensions() {
        super.calculateDimensions();
        final ICDSubInternalExtrusion parentTube = this.getParentTube();
        if (parentTube != null) {
            this.setZDimension(parentTube.getZDimension());
        }
    }
    
    protected void calculateGeometricCenter() {
        super.calculateGeometricCenter();
        this.setGeometricCenterPointLocal(new Point3f(0.0f, 0.0f, this.getZDimension() / 2.0f));
    }
    
    public void setupNamedPoints() {
        this.addNamedPoint("SlotStart", new Point3f());
        this.addNamedPoint("SlotEnd", new Point3f());
        this.addNamedPoint("TubeMiddle", new Point3f());
        this.addNamedPoint("SP1", new Point3f());
        this.addNamedPoint("SP2", new Point3f());
        this.addNamedPoint("SP3", new Point3f());
        this.addNamedPoint("SP4", new Point3f());
        this.addNamedPoint("SP5", new Point3f());
        this.addNamedPoint("Start_Space_Compare", new Point3f());
        this.addNamedPoint("End_Space_Compare", new Point3f());
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("SlotStart").set(0.0f, 0.0f, 0.0f);
        this.getNamedPointLocal("SlotEnd").set(0.0f, 0.0f, this.getZDimension());
        final ICDSubInternalExtrusion parentTube = this.getParentTube();
        if (parentTube != null) {
            final Point3f convertSpaces = MathUtilities.convertSpaces(parentTube.getBasePoint3f(), (EntityObject)parentTube, (EntityObject)this);
            this.getNamedPointLocal("TubeMiddle").set(convertSpaces.x, convertSpaces.y, convertSpaces.z);
        }
        this.getNamedPointLocal("SP1").set(-0.77f, 0.0f, this.getZDimension() / 2.0f);
        this.getNamedPointLocal("SP2").set(1.75f, 0.0f, this.getZDimension() / 2.0f);
        this.getNamedPointLocal("SP3").set(1.75f, 0.5f, this.getZDimension() / 2.0f);
        this.getNamedPointLocal("SP4").set(1.75f, -0.5f, this.getZDimension() / 2.0f);
        this.getNamedPointLocal("SP5").set(3.0f, 0.0f, this.getZDimension() / 2.0f);
        this.getNamedPointLocal("Start_Space_Compare").set(0.0f, 0.0f, 0.0f);
        this.getNamedPointLocal("End_Space_Compare").set(1.0f, 0.0f, 0.0f);
    }
    
    public void modifyCurrentOption() {
        this.validateIndicators();
        super.modifyCurrentOption();
    }
    
    protected void validateIndicators() {
        this.createNewAttribute("ICD_Slott_Type", this.calculateSlotType());
    }
    
    public String calculateSlotType() {
        if (this.isSlot2()) {
            return "2";
        }
        if (this.isSlot3()) {
            return "3";
        }
        if (this.isSlot4()) {
            return "4";
        }
        return "1";
    }
    
    public boolean isSlot1() {
        final LightWeightTypeObject lwTypeCreated = this.getLwTypeCreatedFrom();
        return lwTypeCreated != null && "ICD_Corner1Slotting_Type".equals(lwTypeCreated.getId());
    }
    
    public boolean isSlot2() {
        final LightWeightTypeObject lwTypeCreated = this.getLwTypeCreatedFrom();
        return lwTypeCreated != null && "ICD_Corner2Slotting_Type".equals(lwTypeCreated.getId());
    }
    
    public boolean isSlot3() {
        final LightWeightTypeObject lwTypeCreated = this.getLwTypeCreatedFrom();
        return lwTypeCreated != null && "ICD_Corner3Slotting_Type".equals(lwTypeCreated.getId());
    }
    
    public boolean isSlot4() {
        final LightWeightTypeObject lwTypeCreated = this.getLwTypeCreatedFrom();
        return lwTypeCreated != null && "ICD_Corner4Slotting_Type".equals(lwTypeCreated.getId());
    }
    
    public void addOnTheFlyAttribute() {
        if (this.isSlot1()) {
            this.createNewAttribute("ICD_SLOT_1", this.getCurrentUserControlledValue(), false, false);
        }
        if (this.isSlot2()) {
            this.createNewAttribute("ICD_SLOT_2", this.getCurrentUserControlledValue(), false, false);
        }
        if (this.isSlot3()) {
            this.createNewAttribute("ICD_SLOT_3", this.getCurrentUserControlledValue(), false, false);
        }
        if (this.isSlot4()) {
            this.createNewAttribute("ICD_SLOT_4", this.getCurrentUserControlledValue(), false, false);
        }
    }
    
    public void removeOnTheFlyAttribute() {
        this.removeAttribute("ICD_SLOT_1");
        this.removeAttribute("ICD_SLOT_2");
        this.removeAttribute("ICD_SLOT_3");
        this.removeAttribute("ICD_SLOT_4");
    }
    
    public void getOnTheFlyAttributes(final Collection<Attribute> collection) {
        if (this.isSlot1()) {
            collection.add(this.getAttributeObject("ICD_SLOT_1"));
        }
        if (this.isSlot2()) {
            collection.add(this.getAttributeObject("ICD_SLOT_2"));
        }
        if (this.isSlot3()) {
            collection.add(this.getAttributeObject("ICD_SLOT_3"));
        }
        if (this.isSlot4()) {
            collection.add(this.getAttributeObject("ICD_SLOT_4"));
        }
    }
    
    public void setLocalVariables(final String s) {
        String s2 = "";
        if ("ICD_SLOT_1".equals(s)) {
            s2 = this.getAttributeValueAsString(s);
        }
        if ("ICD_SLOT_2".equals(s)) {
            s2 = this.getAttributeValueAsString(s);
        }
        if ("ICD_SLOT_3".equals(s)) {
            s2 = this.getAttributeValueAsString(s);
        }
        if ("ICD_SLOT_4".equals(s)) {
            s2 = this.getAttributeValueAsString(s);
        }
        this.applyChangesForAttribute("ICD_Slot_Type_UserControllable", s2);
    }
    
    private String getCurrentUserControlledValue() {
        return this.getAttributeValueAsString("ICD_Slot_Type_UserControllable");
    }
    
    public boolean isSlotedByUser() {
        return "Slotted".equals(this.getCurrentUserControlledValue());
    }
    
    public boolean isTopSlot() {
        final ICDSubInternalExtrusion parentTube = this.getParentTube();
        return parentTube != null && parentTube.isTopVerticalTube();
    }
    
    public Vector<Ice2DPaintableNode> getAssemblyIcons(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        final Matrix4f matrix4f2 = new Matrix4f();
        matrix4f2.setIdentity();
        matrix4f2.mul(this.getEntWorldSpaceMatrix());
        final Matrix4f matrix4f3 = new Matrix4f();
        matrix4f3.setIdentity();
        matrix4f2.mul(matrix4f3);
        final Matrix4f matrix4f4 = new Matrix4f();
        matrix4f4.setIdentity();
        matrix4f4.setTranslation(new Vector3f(0.0f, 0.0f, this.getZDimension() / 2.0f));
        matrix4f2.mul(matrix4f4);
        final Ice2DArrowNode e = new Ice2DArrowNode(this.getDefaultLayerName(), (TransformableEntity)this, matrix4f2, 5.0f, Color.GREEN, 2.0f);
        final Vector<Ice2DPaintableNode> vector = new Vector<Ice2DPaintableNode>();
        vector.add((Ice2DPaintableNode)e);
        return vector;
    }
    
    public List<IceOutputNode> getPlotOutputNodes(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        return null;
    }
    
    public boolean shouldDrawAssembly() {
        return this.isSlotted();
    }
    
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        final Solution solution = this.getSolution();
        if (solution != null && Menus.getInstance().referenceLineMode.isChecked(solution)) {
            return new Vector<String>();
        }
        if (ICDAssemblyElevationUtilities.shouldDrawElevation(elevationEntity, this)) {
            Vector<String> cadElevationScript = (Vector<String>)super.getCadElevationScript(elevationEntity);
            if (cadElevationScript == null) {
                cadElevationScript = new Vector<String>();
            }
            cadElevationScript.add("LI:SS(SP1:SP2)");
            cadElevationScript.add("LI:SS(SP3:SP5)");
            cadElevationScript.add("LI:SS(SP4:SP5)");
            cadElevationScript.add("LI:SS(SP4:SP3)");
            return cadElevationScript;
        }
        return new Vector<String>();
    }
    
    public boolean isAssembled() {
        return true;
    }
    
    public boolean drawCAD() {
        return super.drawCAD() && this.isSlotted();
    }
    
    public void drawCad(final ICadTreeNode cadTreeNode, final int n) {
        super.drawCad(cadTreeNode, n);
    }
    
    protected void calculateNamedRotations() {
        super.calculateNamedRotations();
        this.getNamedRotationLocal("SVG_ROT").set((Tuple3f)new Vector3f(0.0f, 0.7853982f, 0.0f));
    }
    
    private void setupNamedRotations() {
        this.addNamedRotation("SVG_ROT", new Vector3f());
    }
    
    protected boolean validateEntityParent() {
        boolean validateEntityParent = super.validateEntityParent();
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity == null || parentEntity instanceof Solution) {
            validateEntityParent = false;
        }
        return validateEntityParent;
    }
    
    public void setModified(final boolean modified) {
        final BasicIntersection basicIntersection = (BasicIntersection)this.getParent((TypeFilter)new BasicIntersectionFilter());
        if (basicIntersection != null && modified) {
            basicIntersection.setEntitiesModified();
        }
        super.setModified(modified);
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addDimensionsToManufacturingTreeMap(treeMap, (TransformableEntity)this);
    }
    
    public String getManufacturingReportMaterialOptID() {
        return "Option0";
    }
    
    public void addManufacturingInfoToTreeMap(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addManufacturingInfoToTreeMap(treeMap, (ManufacturingReportable)this);
    }
    
    public String getDescriptionForManufacturingReport() {
        return this.getDescription();
    }
    
    static {
        logger = Logger.getLogger((Class)ICDCornerSlot.class);
        ICDCornerSlot.ARROW_STROKE_WIDTH = 5.0f;
        ICDCornerSlot.ARROW_COLOR = Color.GREEN;
        ICDCornerSlot.ARROW_LENGTH = 2.0f;
    }
}
