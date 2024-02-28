package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.catalogs.icd.ICDUtilities;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationUtilities;
import net.dirtt.icelib.main.ElevationEntity;
import icd.warnings.WarningReason0282;
import java.util.Iterator;
import java.util.Collection;
import net.dirtt.icelib.report.Report;
import net.iceedge.icecore.basemodule.interfaces.lightweight.Paintable;
import net.dirtt.icebox.iceoutput.core.IceOutputTextNode;
import net.dirtt.icebox.iceoutput.core.IceOutputShapeNode;
import javax.vecmath.Tuple3f;
import net.dirtt.utilities.MathUtilities;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import java.util.List;
import java.io.Serializable;
import net.dirtt.icebox.canvas2d.Ice2DTextNode;
import net.iceedge.catalogs.icd.intersection.ICDPost;
import java.awt.geom.QuadCurve2D;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import java.util.Vector;
import net.iceedge.catalogs.icd.intersection.ICDIntersection;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.iceedge.icecore.basemodule.baseclasses.BasicPost;
import net.dirtt.icelib.main.SolutionSetting;
import java.awt.Color;
import java.util.StringTokenizer;
import net.dirtt.icebox.canvas2d.Ice2DNode;
import java.awt.Shape;
import net.dirtt.icelib.main.TransformableEntity;
import java.awt.geom.Rectangle2D;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import javax.vecmath.Matrix4f;
import net.dirtt.icebox.canvas2d.Ice2DShapeNode;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import com.iceedge.icd.entities.extrusions.ICDExtrusionInterface;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintable;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicExtrusion;

public class ICDSubInternalExtrusion extends BasicExtrusion implements AssemblyPaintable, JointIntersectable, ICDExtrusionInterface, ICDManufacturingReportable
{
    private Ice2DShapeNode assemblyNode;
    private Ice2DShapeNode shapeNode;
    private Matrix4f shapeMatrix;
    
    public ICDSubInternalExtrusion(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.shapeMatrix = new Matrix4f();
    }
    
    public Object clone() {
        return this.buildClone(new ICDSubInternalExtrusion(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDSubInternalExtrusion buildClone(final ICDSubInternalExtrusion icdSubInternalExtrusion) {
        super.buildClone((BasicExtrusion)icdSubInternalExtrusion);
        return icdSubInternalExtrusion;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDSubInternalExtrusion(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDSubInternalExtrusion buildFrameClone(final ICDSubInternalExtrusion icdSubInternalExtrusion, final EntityObject entityObject) {
        super.buildFrameClone((BasicExtrusion)icdSubInternalExtrusion, entityObject);
        return icdSubInternalExtrusion;
    }
    
    public float getLength() {
        return super.getLength();
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
                System.out.println("Automatically Generated Exception Log(ICDSubInternalExtrusion.java,132)[" + ex.getClass() + "]: " + ex.getMessage());
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
    
    public void draw2D(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        if (this.draw2D() && this.isDirty(n)) {
            this.draw2DShapeNode(n, ice2DContainer);
        }
        super.draw2D(n, ice2DContainer, solutionSetting);
    }
    
    public boolean draw2D() {
        return super.draw2D() && this.getParent(ICDBottomExtrusion.class) != null;
    }
    
    protected void calculateNamedRotations() {
        super.calculateNamedRotations();
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity != null && parentEntity instanceof BasicPost) {
            this.getNamedRotationLocal("EXT_ROT").set(0.0f, 0.0f, 1.5707964f);
            this.getNamedRotationLocal("SVG_Elevation_Rotation").set(0.0f, 0.0f, -1.5707964f);
        }
        else {
            this.getNamedRotationLocal("EXT_ROT").set(0.0f, 0.0f, 0.0f);
            this.getNamedRotationLocal("SVG_Elevation_Rotation").set(0.0f, 0.0f, -3.1415927f);
        }
    }
    
    protected void calculateNamedScales() {
        super.calculateNamedScales();
        this.getNamedScaleLocal("SVG_elevation_scale").set(1.0f, 1.0f, this.getZDimension());
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity != null && parentEntity instanceof BasicPost) {
            this.getNamedPointLocal("EXT_POS").set(0.5f, 0.0f, 0.0f);
            this.getNamedPointLocal("svgElevationIP").set(-0.5f, 0.0f, 0.0f);
        }
        else if (parentEntity != null && parentEntity instanceof ICDBottomExtrusion) {
            this.getNamedPointLocal("EXT_POS").set(0.0f, 0.25f, 0.0f);
            this.getNamedPointLocal("svgElevationIP").set(0.0f, 1.0f, 0.0f);
        }
        else if (parentEntity != null && parentEntity instanceof ICDPanelToPanelConnectionHW) {
            this.getNamedPointLocal("EXT_POS").set(0.0f, -0.5f, 0.0f);
            this.getNamedPointLocal("svgElevationIP").set(-0.5f, 0.0f, 0.0f);
        }
        else if (parentEntity != null && parentEntity instanceof ICDTopExtrusion) {
            this.getNamedPointLocal("EXT_POS").set(0.0f, -0.5f, 0.0f);
        }
        else {
            this.getNamedPointLocal("EXT_POS").set(0.0f, 0.0f, 0.0f);
            this.getNamedPointLocal("svgElevationIP").set(0.0f, 1.0f, 0.0f);
        }
        this.getNamedPointLocal("TAB_CONT_LOCATION_A").set(0.0f, 1.0f, 0.0f);
        this.getNamedPointLocal("TAB_CONT_LOCATION_B").set(this.getNamedPointLocal("extEndPoint").x, this.getNamedPointLocal("extEndPoint").y + 1.0f, this.getNamedPointLocal("extEndPoint").z);
    }
    
    public void setupNamedPoints() {
        super.setupNamedPoints();
        this.addNamedPoint("EXT_POS", new Point3f());
        this.addNamedPoint("TAB_CONT_LOCATION_A", new Point3f());
        this.addNamedPoint("TAB_CONT_LOCATION_B", new Point3f());
    }
    
    protected void setupNamedRotations() {
        super.setupNamedRotations();
        this.addNamedRotation("EXT_ROT", new Vector3f());
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
    
    private boolean isUnderIntersection() {
        return this.getIntersection() != null || this.getPanelConnectingHW() != null;
    }
    
    private ICDIntersection getIntersection() {
        return (ICDIntersection)this.getParent(ICDIntersection.class);
    }
    
    private ICDPanelToPanelConnectionHW getPanelConnectingHW() {
        return (ICDPanelToPanelConnectionHW)this.getParent(ICDPanelToPanelConnectionHW.class);
    }
    
    private void hackRotationOutOfEntityWorldSpaceMatrixToFixAssemblyElevationRotation(final Matrix4f matrix4f) {
        matrix4f.m00 = -1.0f;
        matrix4f.m10 = 0.0f;
        matrix4f.m11 = -1.0f;
        matrix4f.m01 = 0.0f;
    }
    
    public Vector<Ice2DPaintableNode> getAssemblyIcons(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        this.getYDimension();
        final float zDimension = this.getZDimension();
        Serializable s = new Rectangle2D.Float(0.0f, 0.0f, zDimension, 1.0f);
        if (this.isCurvedExtrusion()) {
            s = new QuadCurve2D.Float(0.0f, 0.0f, zDimension / 2.0f + 1.0f, 4.0f, zDimension, 0.0f);
        }
        final Matrix4f matrix4f2 = (Matrix4f)this.getEntWorldSpaceMatrix().clone();
        final float rotationWorldSpace = transformableEntity.getRotationWorldSpace();
        final float rotationWorldSpace2 = this.getRotationWorldSpace();
        final Matrix4f matrix4f3 = new Matrix4f();
        matrix4f3.setIdentity();
        matrix4f3.mul(matrix4f2);
        final Matrix4f matrix4f4 = new Matrix4f();
        matrix4f4.setIdentity();
        matrix4f4.rotY(-1.5707964f);
        final Matrix4f matrix4f5 = new Matrix4f();
        matrix4f5.setIdentity();
        matrix4f5.rotX(-1.5707964f);
        final Matrix4f matrix4f6 = new Matrix4f();
        matrix4f6.setIdentity();
        matrix4f6.setTranslation(new Vector3f(0.0f, -this.getXDimension() / 2.0f, 0.0f));
        final Matrix4f matrix4f7 = new Matrix4f();
        matrix4f7.setIdentity();
        matrix4f7.rotX(rotationWorldSpace - rotationWorldSpace2);
        matrix4f3.mul(matrix4f4);
        matrix4f3.mul(matrix4f5);
        matrix4f3.mul(matrix4f7);
        matrix4f3.mul(matrix4f6);
        Object e = null;
        int n2 = 1;
        final ICDPost icdPost = (ICDPost)this.getParent(ICDPost.class);
        if (icdPost != null) {
            n2 = (icdPost.isCurvedPost() ? 0 : 1);
        }
        if (n2 != 0) {
            e = new Ice2DShapeNode(this.getLayerName(), (TransformableEntity)this, matrix4f3, (Shape)s);
            if (!this.isCurvedExtrusion()) {
                ((Ice2DShapeNode)e).setFillColor(Color.lightGray);
            }
        }
        final Matrix4f matrix4f8 = new Matrix4f();
        matrix4f8.setIdentity();
        matrix4f8.mul(matrix4f2);
        final Matrix4f matrix4f9 = new Matrix4f();
        matrix4f9.setIdentity();
        matrix4f9.rotY(-1.5707964f);
        final Matrix4f matrix4f10 = new Matrix4f();
        matrix4f10.setIdentity();
        matrix4f10.rotX(1.5707964f);
        final Matrix4f matrix4f11 = new Matrix4f();
        matrix4f11.setIdentity();
        matrix4f11.setTranslation(new Vector3f(this.getZDimension() / 2.0f, 0.0f, 0.0f));
        matrix4f8.mul(matrix4f9);
        matrix4f8.mul(matrix4f10);
        matrix4f8.mul(matrix4f7);
        matrix4f8.mul(matrix4f11);
        final Ice2DTextNode e2 = new Ice2DTextNode(this.getLayerName(), (TransformableEntity)this, matrix4f8, Math.round(this.getZDimension()) + "", 3);
        final ICDIntersection icdIntersection = (ICDIntersection)this.getParent(ICDIntersection.class);
        if (icdIntersection != null && icdIntersection.getVerticalChase() != null) {
            e = null;
        }
        final Vector<Ice2DPaintableNode> vector = new Vector<Ice2DPaintableNode>();
        vector.add((Ice2DPaintableNode)e);
        vector.add((Ice2DPaintableNode)e2);
        return vector;
    }
    
    public List<IceOutputNode> getPlotOutputNodes(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        if (this.getCurrentOption().getId().contains("None") || this.isFakePart()) {
            return null;
        }
        final Rectangle2D.Float float1 = new Rectangle2D.Float(0.0f, -this.getXDimension() / 2.0f, this.getZDimension(), 1.0f);
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
        final Matrix4f matrix4f7 = new Matrix4f();
        matrix4f7.setIdentity();
        matrix4f7.rotZ(1.5707964f);
        matrix4f2.mul(matrix4f7);
        final IceOutputShapeNode e = new IceOutputShapeNode((Shape)float1, matrix4f2);
        if (n == 1) {
            final Matrix4f matrix4f8 = new Matrix4f();
            matrix4f8.setIdentity();
            matrix4f8.rotX(3.1415927f);
            matrix4f2.mul(matrix4f8);
        }
        final IceOutputTextNode e2 = new IceOutputTextNode((Paintable)null, MathUtilities.round(this.getZDimension(), 2) + "", 3.0f, 1.0f, 0.0f, new Point3f(this.getZDimension() / 2.0f, 0.0f, 0.0f), matrix4f2);
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
        return super.validateTabs();
    }
    
    public boolean isCurvedExtrusion() {
        final String id = this.getCurrentOption().getId();
        return id != null && (id.indexOf("Curved") >= 0 || id.indexOf("curved") >= 0);
    }
    
    public boolean isVertical() {
        final Point3f namedPointWorld = this.getNamedPointWorld("extStartPoint");
        final Point3f namedPointWorld2 = this.getNamedPointWorld("extEndPoint");
        return namedPointWorld != null && namedPointWorld2 != null && MathUtilities.isSameFloat(namedPointWorld.x, namedPointWorld2.x, 0.01f);
    }
    
    public void modifyCurrentOption() {
        this.validateIndicators();
        super.modifyCurrentOption();
    }
    
    protected void validateIndicators() {
        this.createNewAttribute("ICD_Vertical_Slottable_Extrusion", this.calculateExtrusionType());
    }
    
    protected String calculateExtrusionType() {
        if (this.isVertical() && this.isUnderIntersection()) {
            return "Vertical";
        }
        return "Horizontal";
    }
    
    public ICDPost getPost() {
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity instanceof ICDPost) {
            return (ICDPost)parentEntity;
        }
        return null;
    }
    
    public boolean isTopVerticalTube() {
        boolean b = false;
        final ICDPost post = this.getPost();
        if (post != null) {
            final Collection<ICDSubInternalExtrusion> allSubInternalTubes = post.getAllSubInternalTubes();
            if (allSubInternalTubes != null && allSubInternalTubes.size() == 1 && this.isVertical()) {
                return true;
            }
            for (final ICDSubInternalExtrusion icdSubInternalExtrusion : allSubInternalTubes) {
                if (icdSubInternalExtrusion != this && icdSubInternalExtrusion.getBasePoint3f().z <= this.getBasePoint3f().z) {
                    b = true;
                    break;
                }
            }
        }
        return b;
    }
    
    public boolean shouldDrawAssembly() {
        final String sku = this.getSKU();
        return sku != null && !sku.equals("") && !this.getCurrentOption().getId().contains("None") && !this.isFakePart();
    }
    
    public boolean isAssembled() {
        return "true".equals(this.getAttributeValueAsString("isAssembled"));
    }
    
    public boolean isManufacturerReportable() {
        return super.isManufacturerReportable() && !this.isFakePart();
    }
    
    public boolean isFakePart() {
        final ICDInternalExtrusion icdInternalExtrusion = (ICDInternalExtrusion)this.getParent(ICDInternalExtrusion.class);
        if (icdInternalExtrusion != null) {
            return icdInternalExtrusion.isFakePart();
        }
        return super.isFakePart();
    }
    
    public boolean doesParticipateInJointIntersection() {
        return !this.isNoneExtrusion() && !this.getCurrentOption().getId().contains("None");
    }
    
    public boolean isNoneExtrusion() {
        return "None".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Extrusion_Type"));
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
            return this.getAssemblyLineScript();
        }
        return (Vector<String>)super.getCadElevationScript(elevationEntity);
    }
    
    private Vector<String> getAssemblyLineScript() {
        final Vector<String> vector = new Vector<String>();
        Point3f point3f;
        if (this.getParentEntity() instanceof ICDBottomExtrusion) {
            point3f = new Point3f(0.0f, 0.75f, 0.0f);
        }
        else if (this.getParentEntity() instanceof ICDInternalExtrusion) {
            point3f = new Point3f(0.0f, 0.0f, 0.0f);
        }
        else {
            point3f = new Point3f(0.0f, -0.5f, 0.0f);
        }
        vector.add(ICDAssemblyElevationUtilities.getAssemblyLineScript(point3f, "extStart", "extEnd"));
        if (!this.isAssembled()) {
            vector.add(ICDAssemblyElevationUtilities.getAssemblyLineScript(new Point3f(point3f.x, point3f.y - 0.5f, point3f.z), "extStart", "extEnd"));
        }
        vector.add("TX:SS(" + (Math.round(this.getZDimension()) + "") + ":" + 5 + ":" + 0.0f + ":" + 2 + ":CADELP8)");
        return vector;
    }
    
    protected void calculateGeometricCenter() {
        super.calculateGeometricCenter();
        this.setGeometricCenterPointLocal(new Point3f(0.0f, 0.5f, this.getZDimension() / 2.0f));
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity instanceof ICDAngledStartExtrusion || parentEntity instanceof ICDAngledEndExtrusion) {
            final String id = ((BasicExtrusion)parentEntity).getCurrentOption().getId();
            if (id != null && (id.indexOf("Special") >= 0 || id.indexOf("special") >= 0)) {
                this.setGeometricCenterPointLocal(new Point3f(0.0f, this.getYDimension() / 2.0f, this.getZDimension() / 2.0f));
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
}
