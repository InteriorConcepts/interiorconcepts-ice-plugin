package net.iceedge.catalogs.icd.panel;

import com.iceedge.icd.reporting.ICDManufacturingUtils;
import net.dirtt.icelib.main.Solution;
import java.util.Iterator;
import java.util.Collection;
import net.iceedge.catalogs.icd.intersection.ICDCornerSlot;
import java.util.TreeMap;
import net.dirtt.icelib.main.ElevationEntity;
import icd.warnings.WarningReason0282;
import net.iceedge.icecore.basemodule.baseclasses.material.BasicMaterialEntity;
import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.report.compare.CompareNode;
import net.dirtt.icebox.iceoutput.core.IceOutputLayerNode;
import net.iceedge.icecore.basemodule.interfaces.lightweight.Paintable;
import net.dirtt.icebox.iceoutput.core.IceOutputTextNode;
import net.dirtt.icebox.iceoutput.core.IceOutputShapeNode;
import javax.vecmath.Tuple3f;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import net.iceedge.catalogs.icd.ICDILine;
import net.dirtt.icebox.canvas2d.Ice2DTextNode;
import net.dirtt.utilities.MathUtilities;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationUtilities;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import java.util.Vector;
import net.dirtt.icelib.report.Report;
import net.dirtt.icelib.main.TypeableEntity;
import java.util.List;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import net.dirtt.icebox.canvas2d.Ice2DNode;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import javax.vecmath.Matrix4f;
import java.awt.Shape;
import net.dirtt.icebox.canvas2d.Ice2DShapeNode;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icebox.iceoutput.core.PlotPaintable;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintable;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;

public class ICDChaseConnectorExtrusion extends TransformableTriggerUser implements AssemblyPaintable, PlotPaintable, JointIntersectable, ICDManufacturingReportable
{
    private Ice2DShapeNode extShapeNode;
    private Shape shape;
    private Matrix4f shapeMatrix;
    private float myLength;
    private boolean childOfIntersection;
    private boolean flipped;
    
    public ICDChaseConnectorExtrusion(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.shapeMatrix = new Matrix4f();
        this.myLength = 6.0f;
        this.childOfIntersection = false;
        this.flipped = false;
        this.setupNamedPoints();
        this.setupNamedScales();
    }
    
    public ICDChaseConnectorExtrusion buildClone(final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion) {
        super.buildClone((TransformableTriggerUser)icdChaseConnectorExtrusion);
        return icdChaseConnectorExtrusion;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDChaseConnectorExtrusion(this.getId(), this.getCurrentType(), this.getCurrentOption()), entityObject);
    }
    
    public TransformableEntity buildClone2(final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion) {
        super.buildClone2((TransformableTriggerUser)icdChaseConnectorExtrusion);
        return (TransformableEntity)icdChaseConnectorExtrusion;
    }
    
    public Object clone() {
        return this.buildClone(new ICDChaseConnectorExtrusion(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdChaseConnectorExtrusion, entityObject);
        return (EntityObject)icdChaseConnectorExtrusion;
    }
    
    public void draw2D(final int notDirty, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        this.drawChildren2D(notDirty, ice2DContainer, solutionSetting);
        if (this.isDirty(notDirty) && this.draw2D()) {
            this.draw2DShapeNode(notDirty, ice2DContainer);
            this.drawWarning(ice2DContainer);
            this.setNotDirty(notDirty);
        }
    }
    
    private void draw2DShapeNode(final int n, final Ice2DContainer ice2DContainer) {
        if (this.getAttributeObject("SVG_Transform") != null) {
            if (this.extShapeNode == null) {
                ice2DContainer.add((Ice2DNode)(this.extShapeNode = new Ice2DShapeNode(this.getLayerName(), (TransformableEntity)this, this.calculate2DShapeMatrix(), this.calculate2DShape())));
            }
            else {
                this.extShapeNode.setShape(this.calculate2DShape());
                this.extShapeNode.setWorldSpaceMatrix(this.calculate2DShapeMatrix());
                if (this.extShapeNode.getParent() == null || !ice2DContainer.containsNode((Ice2DNode)this.gvtPlanNode)) {
                    ice2DContainer.add((Ice2DNode)this.extShapeNode);
                }
            }
            this.extShapeNode.setFillColor(new Color(200, 200, 200));
        }
    }
    
    public Shape calculate2DShape() {
        return this.shape = new Rectangle2D.Float(this.getBoundingCube().getLower().getZ() - 0.5f, this.getBoundingCube().getLower().getX() + 0.5f, 1.0f, this.myLength);
    }
    
    protected void calculateDimensions() {
        super.calculateDimensions();
        this.setYDimension(this.getLength());
    }
    
    public void calculateBoundingCube() {
        super.calculateBoundingCube();
    }
    
    protected void calculateGeometricCenter() {
        this.setGeometricCenterPointLocal(new Point3f(0.0f, this.getYDimension() / 2.0f, this.getZDimension() / 2.0f));
    }
    
    private Matrix4f calculate2DShapeMatrix() {
        this.shapeMatrix.setIdentity();
        this.shapeMatrix.mul(this.getEntWorldSpaceMatrix());
        this.shapeMatrix.mul(this.getEntitySVGPlanTransformMatrix());
        return this.shapeMatrix;
    }
    
    public ICDPanel getParentPanel() {
        if (this.getParent() != null && this.getParent() instanceof ICDPanel) {
            return (ICDPanel)this.getParent();
        }
        return null;
    }
    
    public void destroy2DAsset() {
        super.destroy2DAsset();
        this.extShapeNode = null;
    }
    
    public void cutFromTree2D() {
        super.cutFromTree2D();
        if (this.extShapeNode != null) {
            this.extShapeNode.removeFromParent();
        }
    }
    
    public boolean isSideA() {
        return "Yes".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Chase_Mid_Connector_Side_A"));
    }
    
    public boolean isVertical() {
        return "Yes".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Chase_Mid_Connector_Vertical"));
    }
    
    private float getMyLength() {
        return this.myLength;
    }
    
    public float getLength() {
        return this.myLength;
    }
    
    public void setMyLength(final float myLength) {
        this.myLength = myLength;
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("bottomJointBP").set(0.0f, this.myLength + 0.5f, 0.5f);
        this.getNamedPointLocal("middleJointBP").set(0.0f, this.myLength, 0.5f);
        this.getNamedPointLocal("topJointBP").set(0.0f, -0.5f, 0.5f);
        this.getNamedPointLocal("extStartPoint").set(0.0f, 0.0f, this.getZDimension() / 2.0f);
        this.getNamedPointLocal("extEndPoint").set(0.0f, this.getYDimension(), this.getZDimension() / 2.0f);
        this.getNamedPointLocal("CADELP8").set(this.getXDimension() / 2.0f, this.getYDimension() / 2.0f, this.getZDimension() / 2.0f);
        this.getNamedPointLocal("CAD_POS").set(0.0f, this.getYDimension() / 2.0f, 0.0f);
    }
    
    protected void calculateNamedRotations() {
        super.calculateNamedRotations();
        this.getNamedRotationLocal("chaseRot").set(1.5707964f, 0.0f, 0.0f);
    }
    
    protected void calculateNamedScales() {
        super.calculateNamedScales();
        this.getNamedScaleLocal("ASE_SCL").set(1.0f, this.myLength + 0.5f, 1.0f);
        this.getNamedScaleLocal("CAD_SCL").set(1.0f, this.myLength, 1.0f);
    }
    
    public void setupNamedPoints() {
        this.addNamedPoint("bottomJointBP", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedRotation("chaseRot", new Vector3f(0.0f, 0.0f, 0.0f));
        this.addNamedScale("ASE_SCL", new Vector3f(1.0f, 1.0f, 1.0f));
        this.addNamedPoint("middleJointBP", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("topJointBP", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("extStartPoint", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("extEndPoint", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("CADELP8", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("CAD_POS", new Point3f(0.0f, 0.0f, 0.0f));
    }
    
    public void setupNamedScales() {
        this.addNamedScale("CAD_SCL", new Vector3f());
    }
    
    public boolean isChildOfIntersection() {
        return this.childOfIntersection;
    }
    
    public void setChildOfIntersection(final boolean childOfIntersection) {
        this.childOfIntersection = childOfIntersection;
    }
    
    public void setFlipped(final boolean flipped) {
        this.flipped = flipped;
    }
    
    public List<TypeableEntity> getFinishRoots() {
        return (List<TypeableEntity>)super.getFinishRoots();
    }
    
    protected boolean shouldUseNewFinishSystem() {
        return true;
    }
    
    public void solve() {
        if (this.isModified()) {
            this.modifyCurrentOption();
            this.validateChildTypes();
        }
        super.solve();
        this.validateChaseConnectorExtrusion();
    }
    
    public int getQuoteReportLevel() {
        return -1;
    }
    
    public boolean isQuoteable(final String s) {
        return false;
    }
    
    public boolean shouldICDMakePreAssembledReport() {
        return !"true".equals(this.getAttributeValueAsString("isAssembled"));
    }
    
    public boolean shouldAddChildrenToReport(final Report report) {
        return false;
    }
    
    public Vector<Ice2DPaintableNode> getAssemblyIcons(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        final Matrix4f matrix4f2 = new Matrix4f();
        matrix4f2.setIdentity();
        matrix4f2.mul((Matrix4f)this.getEntWorldSpaceMatrix().clone());
        final ICDILine iLineForEntity = ICDAssemblyElevationUtilities.getILineForEntity((EntityObject)this);
        final Point3f point3f2 = new Point3f();
        if (!this.isVertical()) {
            point3f2.y += this.getMyLength();
        }
        if (MathUtilities.convertSpaces(point3f2, (EntityObject)this, (EntityObject)iLineForEntity).y < 0.0f) {}
        Rectangle2D.Float float1;
        if (this.isVertical()) {
            float1 = new Rectangle2D.Float(0.5f, -0.5f, this.myLength, 1.0f);
            new Matrix4f().setIdentity();
            final Matrix4f matrix4f3 = new Matrix4f();
            matrix4f3.setIdentity();
            matrix4f3.rotZ((float)Math.toRadians(90.0));
            matrix4f2.mul(matrix4f3);
        }
        else {
            float1 = new Rectangle2D.Float(0.5f, -0.5f, this.myLength, 1.0f);
            final Matrix4f matrix4f4 = new Matrix4f();
            matrix4f4.setIdentity();
            final Matrix4f matrix4f5 = new Matrix4f();
            matrix4f5.setIdentity();
            matrix4f5.rotZ(1.5707964f);
            matrix4f2.mul(matrix4f5);
            matrix4f2.mul(matrix4f4);
        }
        final Ice2DShapeNode e = new Ice2DShapeNode(this.getLayerName(), (TransformableEntity)this, matrix4f2, (Shape)float1);
        e.setFillColor(Color.lightGray);
        final Matrix4f matrix4f6 = new Matrix4f();
        matrix4f6.setIdentity();
        matrix4f6.mul(this.getEntWorldSpaceMatrix());
        final Matrix4f matrix4f7 = new Matrix4f();
        matrix4f7.setIdentity();
        if (this.isVertical()) {
            matrix4f7.setTranslation(new Vector3f(0.0f, this.getLength() / 2.0f, 0.0f));
            matrix4f6.mul(matrix4f7);
            matrix4f7.setIdentity();
            matrix4f7.rotZ((float)Math.toRadians(90.0));
            matrix4f6.mul(matrix4f7);
        }
        else {
            matrix4f7.setTranslation(new Vector3f(0.0f, this.getLength(), 0.0f));
            matrix4f7.rotZ((float)Math.toRadians(90.0));
            matrix4f7.rotY((float)Math.toRadians(45.0));
            matrix4f6.mul(matrix4f7);
        }
        matrix4f6.mul(matrix4f7);
        final Ice2DTextNode e2 = new Ice2DTextNode(this.getLayerName(), (TransformableEntity)this, matrix4f6, Math.round(this.getLength()) + "", 3);
        final Vector<Ice2DPaintableNode> vector = new Vector<Ice2DPaintableNode>();
        vector.add((Ice2DPaintableNode)e);
        vector.add((Ice2DPaintableNode)e2);
        return vector;
    }
    
    public List<IceOutputNode> getPlotOutputNodes(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        if (!this.shouldDrawAssembly()) {
            return new Vector<IceOutputNode>();
        }
        if (this.getCurrentOption().getId().contains("None")) {
            return null;
        }
        final int n2 = -1;
        final int n3 = 1;
        final ICDILine icdiLine = (ICDILine)this.getParent(ICDILine.class);
        final Point3f point3f2 = new Point3f();
        if (!this.isVertical()) {
            point3f2.y += this.getMyLength();
        }
        final Point3f convertSpaces = MathUtilities.convertSpaces(point3f2, (EntityObject)this, (EntityObject)icdiLine);
        int n4 = n2;
        if (convertSpaces.y < 0.0f) {
            n4 = n3;
        }
        final Point3f convertSpaces2 = MathUtilities.convertSpaces(this.getBasePointWorldSpace(), transformableEntity.getEntWorldSpaceMatrix());
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
        matrix4f5.setTranslation(new Vector3f((Tuple3f)convertSpaces2));
        matrix4f2.mul(matrix4f5);
        Rectangle2D.Float float1;
        if (this.isVertical()) {
            float1 = new Rectangle2D.Float(0.5f, -0.5f, this.myLength, 1.0f);
            final Matrix4f matrix4f6 = new Matrix4f();
            matrix4f6.setIdentity();
            final float abs = Math.abs(this.getBasePoint3f().y);
            final float verticalSkewOffset = ICDAssemblyElevationUtilities.getVerticalSkewOffset(abs);
            if (n4 == n2) {
                matrix4f6.setTranslation(new Vector3f(verticalSkewOffset, 0.0f, abs));
            }
            else {
                matrix4f6.setTranslation(new Vector3f(-verticalSkewOffset, 0.0f, -abs));
            }
            matrix4f2.mul(matrix4f6);
            final Matrix4f matrix4f7 = new Matrix4f();
            matrix4f7.setIdentity();
            matrix4f7.rotY((float)Math.toRadians(90.0));
            matrix4f2.mul(matrix4f7);
        }
        else {
            float1 = new Rectangle2D.Float(0.5f, -0.5f, ICDAssemblyElevationUtilities.getAngularSkewOffset(this.myLength), 1.0f);
            final Matrix4f matrix4f8 = new Matrix4f();
            matrix4f8.setIdentity();
            matrix4f8.rotY((float)Math.toRadians((n4 == n2) ? -45.0 : 135.0));
            matrix4f2.mul(matrix4f8);
        }
        final float n5 = 1.5707964f;
        final Matrix4f matrix4f9 = new Matrix4f();
        matrix4f9.setIdentity();
        matrix4f9.rotX(n5);
        matrix4f2.mul(matrix4f9);
        final IceOutputShapeNode e = new IceOutputShapeNode((Shape)float1, matrix4f2);
        final Point3f point3f3 = new Point3f(this.getZDimension() / 2.0f, 0.0f, 0.0f);
        if (n == 1) {
            final Matrix4f matrix4f10 = new Matrix4f();
            matrix4f10.setIdentity();
            matrix4f10.rotX(3.1415927f);
            matrix4f2.mul(matrix4f10);
            final Matrix4f matrix4f11 = new Matrix4f();
            matrix4f11.setIdentity();
            matrix4f11.rotZ(3.1415927f);
            matrix4f2.mul(matrix4f11);
            point3f3.x = -this.getZDimension() / 2.0f;
        }
        final IceOutputTextNode iceOutputTextNode = new IceOutputTextNode((Paintable)null, MathUtilities.round(this.getZDimension(), 2) + "", 3.0f, 1.0f, 0.0f, point3f3, matrix4f2);
        final Vector<IceOutputNode> vector = new Vector<IceOutputNode>();
        vector.add((IceOutputNode)e);
        return vector;
    }
    
    public boolean usePlotGVT() {
        return false;
    }
    
    public List<IceOutputNode> getPlotOutputNodes() {
        final IceOutputShapeNode e = new IceOutputShapeNode(this.shape, this.getEntWorldSpaceMatrix());
        final Vector<IceOutputNode> vector = new Vector<IceOutputNode>();
        vector.add(e);
        e.setParent((IceOutputNode)new IceOutputLayerNode("Panels"));
        return (List<IceOutputNode>)vector;
    }
    
    public void populateCompareNode(final Class clazz, final CompareNode compareNode) {
        super.populateCompareNode(clazz, compareNode);
        if (ManufacturingReportable.class.isAssignableFrom(clazz)) {
            this.populateCompareNodeForICD(clazz, compareNode);
        }
    }
    
    public void populateCompareNodeForICD(final Class clazz, final CompareNode compareNode) {
        compareNode.addCompareValue("length", (Object)Math.round(this.getMyLength()));
        String description = "";
        if (((BasicMaterialEntity)this.getChildByClass(BasicMaterialEntity.class)).getDescription() != null) {
            description = ((BasicMaterialEntity)this.getChildByClass(BasicMaterialEntity.class)).getDescription();
        }
        compareNode.addCompareValue("finish", (Object)description);
        compareNode.addCompareValue("description", (Object)this.getDescription());
        compareNode.addCompareValue("assembled", (Object)this.getAttributeValueAsString("isAssembled"));
        compareNode.addCompareValue("tabsandslots", (Object)this.getReportDescription());
        compareNode.addCompareValue("usertag", (Object)this.getUserTagNameAttribute("TagName1"));
    }
    
    public boolean isUserDeletable() {
        return false;
    }
    
    public boolean shouldDrawAssembly() {
        return ICDAssemblyElevationUtilities.shouldDrawAssemblyShapeNodes((TransformableEntity)this);
    }
    
    public boolean isAssembled() {
        return "true".equals(this.getAttributeValueAsString("isAssembled"));
    }
    
    public String getDefaultLayerName() {
        return "Panels";
    }
    
    public boolean contains(final Point3f point3f) {
        final Matrix4f matrix4f = (Matrix4f)this.getEntWorldSpaceMatrix().clone();
        matrix4f.invert();
        final Point3f point3f2 = new Point3f(point3f);
        matrix4f.transform(point3f2);
        return this.getBoundingCube().intersect(point3f2);
    }
    
    public boolean doesParticipateInJointIntersection() {
        return true;
    }
    
    public void handleWarnings() {
        WarningReason0282.addRequiredWarning((TransformableEntity)this);
    }
    
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        if (ICDAssemblyElevationUtilities.shouldDrawElevation(elevationEntity, this)) {
            return this.getAssemblyLineScript();
        }
        return (Vector<String>)super.getCadElevationScript(elevationEntity);
    }
    
    private Vector<String> getAssemblyLineScript() {
        final Vector<String> vector = new Vector<String>();
        vector.add(ICDAssemblyElevationUtilities.getAssemblyLineScript(new Point3f(0.0f, 0.0f, 0.0f), "extStartPoint", "extEndPoint"));
        if (!this.isAssembled()) {
            vector.add(ICDAssemblyElevationUtilities.getAssemblyLineScript(new Point3f(0.5f, 0.0f, 0.0f), "extStartPoint", "extEndPoint"));
        }
        vector.add("TX:SS(" + (Math.round(this.getYDimension()) + "") + ":" + 5 + ":" + 0.0f + ":" + 2 + ":CADELP8)");
        return vector;
    }
    
    public void getManufacturingInfo(final TreeMap<String, String> treeMap) {
        if (this.containsAttributeKey("Product_Type")) {
            treeMap.put("Type", this.getAttributeValueAsString("Product_Type"));
        }
        if (this.containsAttributeKey("SubType")) {
            treeMap.put("SubType", this.getSubType());
        }
        treeMap.put("Description", this.getDescription() + ",Length:" + Math.round(this.getLength()) + " " + this.getReportDescription());
        treeMap.put("Width", Math.round(this.getLength()) + "");
        final List<BasicMaterialEntity> childrenByClass = this.getChildrenByClass(BasicMaterialEntity.class, false);
        if (childrenByClass != null) {
            for (int i = 0; i < childrenByClass.size(); ++i) {
                final BasicMaterialEntity basicMaterialEntity = childrenByClass.get(i);
                String value = "";
                if (basicMaterialEntity.getChildCount() > 0) {
                    if (basicMaterialEntity.getChildAt(0) instanceof BasicMaterialEntity) {
                        final BasicMaterialEntity basicMaterialEntity2 = (BasicMaterialEntity)basicMaterialEntity.getChildAt(0);
                        if (basicMaterialEntity2.getAttributeValueAsString("Material_ID") != null) {
                            value = basicMaterialEntity2.getAttributeValueAsString("Material_ID");
                        }
                    }
                }
                else if (basicMaterialEntity.getAttributeValueAsString("Material_ID") != null) {
                    value = basicMaterialEntity.getAttributeValueAsString("Material_ID");
                }
                treeMap.put("Option0" + (i + 1), value);
            }
        }
        treeMap.put("UserTag", this.getUserTagNameAttribute("TagName1"));
    }
    
    public String getReportDescription() {
        final Vector<ICDCornerSlot> allSlottedSlots = this.getAllSlottedSlots();
        if (allSlottedSlots != null && allSlottedSlots.size() > 0) {
            return " CS" + allSlottedSlots.size() + " ";
        }
        final String tabCode = this.getTabCode();
        if (tabCode.length() > 0) {
            return tabCode;
        }
        return "";
    }
    
    public Collection<ICDCornerSlot> getAllSlots() {
        final Vector<ICDCornerSlot> vector = new Vector<ICDCornerSlot>();
        final Iterator<EntityObject> children = this.getChildren();
        while (children.hasNext()) {
            final EntityObject entityObject = children.next();
            if (entityObject instanceof ICDCornerSlot) {
                vector.add((ICDCornerSlot)entityObject);
            }
        }
        return vector;
    }
    
    public Vector<ICDCornerSlot> getAllSlottedSlots() {
        final Vector<ICDCornerSlot> vector = new Vector<ICDCornerSlot>();
        for (final ICDCornerSlot e : this.getAllSlots()) {
            if (e != null && e.isSlotted()) {
                vector.add(e);
            }
        }
        return vector;
    }
    
    public String getTabCode() {
        String string = "";
        final List<ICDTabContainer> tabContainers = this.getTabContainers();
        boolean b = false;
        boolean b2 = false;
        boolean b3 = false;
        for (int i = 0; i < tabContainers.size(); ++i) {
            final int side = tabContainers.get(i).getSide();
            final List<ICDTab> tabs = tabContainers.get(i).getTabs(true);
            final List<ICDTab> tabs2 = tabContainers.get(i).getTabs(false);
            if (tabs != null && tabs.size() > 0) {
                if (tabs2 != null && tabs2.size() != tabs.size()) {
                    b3 = true;
                }
                if (side == 0) {
                    b = true;
                }
                if (side == 1) {
                    b2 = true;
                }
            }
        }
        if (b && b2) {
            string = "S2";
        }
        else if (b || b2) {
            string = "S1";
        }
        if (b3) {
            string += "-spec";
        }
        return string;
    }
    
    private List<ICDTabContainer> getTabContainers() {
        return (List<ICDTabContainer>)this.getChildrenByClass(ICDTabContainer.class, false);
    }
    
    public boolean isSuspendedeChaseSupport() {
        final String id = this.getCurrentType().getId();
        return "ICD_Chase_Mid_Suspended_Chase_Vertical_Support_Type".equals(id) || "ICD_Chase_Mid_Suspended_Chase_Vertical_Support_B_Type".equals(id);
    }
    
    public void destroy() {
        super.destroy();
    }
    
    public void validateChaseConnectorExtrusion() {
        if (this.getParentEntity() instanceof Solution) {
            this.destroy();
        }
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
}
