package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import java.util.Collection;
import net.dirtt.icelib.main.ElevationEntity;
import java.util.ArrayList;
import net.dirtt.icebox.iceoutput.core.IceOutputShapeNode;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import java.util.List;
import net.dirtt.icebox.canvas2d.Ice2DTextNode;
import net.dirtt.icebox.canvas2d.Ice2DShapeNode;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import java.util.Vector;
import javax.vecmath.Matrix4f;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicExtrusion;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;
import net.iceedge.icecore.basemodule.interfaces.panels.TopExtrusionInterface;
import net.dirtt.icebox.canvas2d.Ice2DNode;
import java.awt.Shape;
import net.dirtt.icelib.main.TransformableEntity;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import java.util.Iterator;
import net.dirtt.icelib.main.attributes.Attribute;
import net.dirtt.utilities.MathUtilities;
import javax.vecmath.Point3f;
import net.iceedge.icecore.basemodule.interfaces.ILineInterface;
import net.iceedge.icecore.basemodule.interfaces.GeneralIntersectionInterface;
import net.iceedge.catalogs.icd.ICDILine;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icebox.canvas2d.Ice2DLightShapeNode;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icebox.iceoutput.core.PlotPaintable;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintable;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;

public class ICDTab extends TransformableTriggerUser implements AssemblyPaintable, PlotPaintable, ICDManufacturingReportable
{
    private float offset;
    private Ice2DLightShapeNode boxNode;
    
    public ICDTab(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.setupNamedPoints();
        this.setupNamedRotations();
    }
    
    public Object clone() {
        return this.buildClone(new ICDTab(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDTab buildClone(final ICDTab icdTab) {
        super.buildClone((TransformableTriggerUser)icdTab);
        return icdTab;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDTab(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDTab buildFrameClone(final ICDTab icdTab, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdTab, entityObject);
        return icdTab;
    }
    
    public void solve() {
        boolean validateParent = false;
        if (this.isModified()) {
            validateParent = this.validateParent();
        }
        if (!validateParent) {
            super.solve();
        }
    }
    
    private boolean validateParent() {
        final EntityObject parentEntity = this.getParentEntity();
        boolean b = false;
        if (parentEntity instanceof Solution) {
            this.destroy();
            b = true;
        }
        return b;
    }
    
    public void modifyCurrentOption() {
        this.validateIndicators();
        super.modifyCurrentOption();
    }
    
    protected void validateIndicators() {
        this.applyChangesForAttribute("ICD_Tab_Style_Type_NonVis", this.getContainer().getTabStyle());
        this.validateBehindChaseIndicator();
    }
    
    private void validateBehindChaseIndicator() {
        final Attribute attributeObject = this.getAttributeObject("ICD_Tab_Behind_Chase_Indicator");
        if (attributeObject != null) {
            boolean b = false;
            final ICDILine icdiLine = (ICDILine)this.getParent(ICDILine.class);
            final ICDTopExtrusion icdTopExtrusion = (ICDTopExtrusion)this.getParent(ICDTopExtrusion.class);
            Label_0223: {
                if (icdTopExtrusion == null || !icdTopExtrusion.isChaseTopExtrusion()) {
                    final Iterator<GeneralIntersectionInterface> iterator = icdiLine.getIntersections().iterator();
                    while (iterator.hasNext()) {
                        final Iterator<ILineInterface> iterator2 = iterator.next().getWallSetsFromArms().iterator();
                        while (iterator2.hasNext()) {
                            for (final ICDTopExtrusion icdTopExtrusion2 : iterator2.next().getChildrenByClass(ICDTopExtrusion.class, true, true)) {
                                if (icdTopExtrusion2.isChaseTopExtrusion() && MathUtilities.isPointWithinCube(this.convertPointToLocal(icdTopExtrusion2.getNamedPointWorld("P2")), this.convertPointToLocal(icdTopExtrusion2.getNamedPointWorld("PANEL_P4")), new Point3f(), 0.1f)) {
                                    b = true;
                                    break Label_0223;
                                }
                            }
                        }
                    }
                }
            }
            attributeObject.setCurrentValueAsString(b + "");
        }
    }
    
    public void draw2D(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        final ICDTabContainer container = this.getContainer();
        if (this.isDirty(n)) {
            if (this.draw2D() && container != null) {
                final Rectangle2D.Float shape = new Rectangle2D.Float(-this.getXDimension() / 2.0f, -this.getYDimension() / 2.0f, this.getXDimension(), this.getYDimension());
                if (this.boxNode == null) {
                    (this.boxNode = new Ice2DLightShapeNode(this.getLayerName(), (TransformableEntity)this, (Shape)shape, Color.black)).setFill(true);
                }
                else {
                    this.boxNode.setShape((Shape)shape);
                }
                ice2DContainer.add((Ice2DNode)this.boxNode);
            }
            else {
                this.destroy2D();
            }
        }
        this.drawBoundingCubeNode(ice2DContainer);
    }
    
    public void destroy2DAsset() {
        this.boxNode = null;
        super.destroy2DAsset();
    }
    
    public void cutFromTree2D() {
        if (this.boxNode != null) {
            this.boxNode.removeFromParent();
        }
        super.cutFromTree2D();
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("Tab_Start").set(0.0f, -1.0f, -0.01f);
        this.getNamedPointLocal("Tab_End").set(0.25f, -1.0f, -0.01f);
        final ICDTabContainer container = this.getContainer();
        if (container != null) {
            if (container.isFlippedContainer()) {
                this.getNamedPointLocal("CAD_POS").set(0.0f, 0.0f, 0.0f);
            }
            else {
                this.getNamedPointLocal("CAD_POS").set(0.0f, -2.0f, 0.0f);
            }
        }
        this.getNamedPointLocal("Start_Space_Compare").set(0.0f, 0.5f, 0.0f);
        this.getNamedPointLocal("End_Space_Compare").set(0.0f, -0.5f, 0.0f);
        final float n = this.getXDimension() * 2.0f;
        final float n2 = this.getYDimension() / 2.0f;
        float n3 = 0.5f;
        if (this.getExtrusion() instanceof TopExtrusionInterface) {
            n3 = 1.5f;
        }
        if (this.isInverted()) {
            --n3;
        }
        this.getNamedPointLocal("SP1").set(-n, n2, n3);
        this.getNamedPointLocal("SP2").set(n, n2, n3);
        this.getNamedPointLocal("SP3").set(n, -n2 * 3.0f, n3);
        this.getNamedPointLocal("SP4").set(-n, -n2 * 3.0f, n3);
        final float n4 = 0.5f;
        this.getNamedPointLocal("SP11").set(-n4 / 2.0f, n2, n3);
        this.getNamedPointLocal("SP12").set(-n4 / 2.0f, n2 - n4, n3);
        this.getNamedPointLocal("SP13").set(-n4 / 2.0f, n2 - n4 * 2.0f, n3);
        this.getNamedPointLocal("SP14").set(n4 / 2.0f, n2 - n4 * 2.0f, n3);
        this.getNamedPointLocal("SP15").set(n4 * 5.0f / 6.0f, n2 - n4 * 5.0f / 3.0f, n3);
        this.getNamedPointLocal("SP16").set(n4 * 5.0f / 6.0f, n2 - n4 * 4.0f / 3.0f, n3);
        this.getNamedPointLocal("SP17").set(n4 / 2.0f, n2 - n4, n3);
        this.getNamedPointLocal("SP18").set(-n4 / 6.0f, n2 - n4, n3);
        this.getNamedPointLocal("SP19").set(n4 * 5.0f / 6.0f, n2, n3);
    }
    
    protected void setupNamedPoints() {
        this.addNamedPoint("Tab_Start", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("Tab_End", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("CAD_POS", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("SP1", new Point3f());
        this.addNamedPoint("SP2", new Point3f());
        this.addNamedPoint("SP3", new Point3f());
        this.addNamedPoint("SP4", new Point3f());
        this.addNamedPoint("SP11", new Point3f());
        this.addNamedPoint("SP12", new Point3f());
        this.addNamedPoint("SP13", new Point3f());
        this.addNamedPoint("SP14", new Point3f());
        this.addNamedPoint("SP15", new Point3f());
        this.addNamedPoint("SP16", new Point3f());
        this.addNamedPoint("SP17", new Point3f());
        this.addNamedPoint("SP18", new Point3f());
        this.addNamedPoint("SP19", new Point3f());
        this.addNamedPoint("Start_Space_Compare", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("End_Space_Compare", new Point3f(0.0f, 0.0f, 0.0f));
    }
    
    protected void setupNamedRotations() {
        this.addNamedRotation("CAD_ROT", new Vector3f());
    }
    
    protected void calculateNamedRotations() {
        super.calculateNamedRotations();
        this.getNamedRotationLocal("CAD_ROT").set((Tuple3f)new Vector3f(0.0f, 0.0f, this.getRotationWorldSpace()));
    }
    
    public float getPositionOffset() {
        return this.offset;
    }
    
    public void setPositionOffset(final float offset) {
        this.offset = offset;
    }
    
    public int getSide() {
        final ICDTabContainer container = this.getContainer();
        return (container == null) ? -1 : container.getSide();
    }
    
    public ICDTabContainer getContainer() {
        return (ICDTabContainer)this.getParent(ICDTabContainer.class);
    }
    
    public BasicExtrusion getExtrusion() {
        return (BasicExtrusion)this.getParentByClassRecursive(BasicExtrusion.class);
    }
    
    public Vector<Ice2DPaintableNode> getAssemblyIcons(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        final float n2 = 0.0f;
        final Matrix4f matrix4f2 = new Matrix4f();
        matrix4f2.setIdentity();
        matrix4f2.mul(this.getEntWorldSpaceMatrix());
        final Matrix4f matrix4f3 = new Matrix4f();
        matrix4f3.setIdentity();
        matrix4f3.rotX(-1.5707964f);
        matrix4f2.mul(matrix4f3);
        final Ice2DShapeNode e = new Ice2DShapeNode(this.getLayerName(), (TransformableEntity)this, matrix4f2, (Shape)new Rectangle2D.Float(this.getBasePoint().getX(), this.getBasePoint().getY() - n2, 2.0f, 3.0f));
        e.setFillColor(Color.black);
        final Vector<Ice2DPaintableNode> vector = new Vector<Ice2DPaintableNode>();
        vector.add(e);
        if (this.isInverted()) {
            final Matrix4f matrix4f4 = new Matrix4f();
            matrix4f4.setIdentity();
            matrix4f4.mul(this.getEntWorldSpaceMatrix());
            matrix4f4.mul(matrix4f3);
            final Matrix4f matrix4f5 = new Matrix4f();
            matrix4f5.setIdentity();
            matrix4f5.setTranslation(new Vector3f(0.0f, 3.0f, 0.0f));
            matrix4f4.mul(matrix4f5);
            vector.add((Ice2DPaintableNode)new Ice2DTextNode(this.getLayerName(), (TransformableEntity)this, matrix4f4, "R", 3));
        }
        return (Vector<Ice2DPaintableNode>)vector;
    }
    
    public List<IceOutputNode> getPlotOutputNodes(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        if (this.isNoneTab() || this.isFakePart()) {
            return null;
        }
        final float n2 = 0.0f;
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
        final float n3 = -1.5707964f;
        final Matrix4f matrix4f6 = new Matrix4f();
        matrix4f6.setIdentity();
        matrix4f6.rotX(n3);
        matrix4f2.mul(matrix4f6);
        final Matrix4f matrix4f7 = new Matrix4f();
        matrix4f7.setIdentity();
        matrix4f7.rotY(3.1415927f);
        matrix4f2.mul(matrix4f7);
        final IceOutputShapeNode e = new IceOutputShapeNode((Shape)new Rectangle2D.Float(this.getBasePoint().getX(), this.getBasePoint().getY() - n2, 2.0f, 3.0f), matrix4f2);
        final Vector<IceOutputNode> vector = new Vector<IceOutputNode>();
        vector.add((IceOutputNode)e);
        return vector;
    }
    
    public void calculateLocalSpace() {
        super.calculateLocalSpace();
        this.setBasePoint(this.getBasePoint3f().x, this.getBasePoint3f().y, this.getPositionOffset());
    }
    
    public List<IceOutputNode> getPlotOutputNodes() {
        final ArrayList<IceOutputNode> list = new ArrayList<IceOutputNode>();
        if (this.boxNode != null) {
            list.add(new IceOutputShapeNode(this.boxNode.getShape(), this.getEntWorldSpaceMatrix()));
        }
        return (List<IceOutputNode>)list;
    }
    
    protected void calculateDimensions() {
        super.calculateDimensions();
        this.setXDimension(0.25f);
        this.setZDimension(0.1f);
        this.setYDimension(1.0f);
    }
    
    public boolean isPrintable() {
        return false;
    }
    
    public boolean shouldDrawAssembly() {
        return !this.isNoneTab() && !this.isFakePart();
    }
    
    public boolean isNoneTab() {
        return this.getAttributeValueAsBoolean("ICD_Is_None_Tab", false);
    }
    
    public boolean isAssembled() {
        return false;
    }
    
    public boolean isInverted() {
        return "Inverted".equals(this.getAttributeValueAsString("ICD_Tab_Style_Type_NonVis"));
    }
    
    public boolean isTabbed() {
        return !this.isNoneTab();
    }
    
    public boolean isOnChase() {
        return this.getCurrentOption().getAttributeValueAsBoolean("isChaseTab", false);
    }
    
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        if (this.shouldDrawAssembly()) {
            return this.getAssemblyScripts();
        }
        return new Vector<String>();
    }
    
    private Vector<String> getAssemblyScripts() {
        final Vector<String> vector = new Vector<String>();
        vector.add("LI:SS(5:SP1:SP2)");
        vector.add("LI:SS(5:SP2:SP3)");
        vector.add("LI:SS(5:SP3:SP4)");
        vector.add("LI:SS(5:SP4:SP1)");
        if (this.isInverted()) {
            vector.addAll(this.getAssemblyTextScripts());
        }
        return vector;
    }
    
    private Collection<? extends String> getAssemblyTextScripts() {
        final Vector<String> vector = new Vector<String>();
        vector.add("LI:SS(5:SP11:SP13)");
        vector.add("LI:SS(5:SP13:SP14)");
        vector.add("LI:SS(5:SP14:SP15)");
        vector.add("LI:SS(5:SP15:SP16)");
        vector.add("LI:SS(5:SP16:SP17)");
        vector.add("LI:SS(5:SP17:SP12)");
        vector.add("LI:SS(5:SP18:SP19)");
        return vector;
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
