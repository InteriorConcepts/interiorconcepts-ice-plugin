package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import icd.warnings.WarningReason0282;
import net.dirtt.icelib.report.compare.CompareNode;
import net.dirtt.icelib.main.SolutionSetting;
import java.awt.Color;
import java.util.StringTokenizer;
import net.dirtt.icebox.canvas2d.Ice2DNode;
import java.awt.Shape;
import net.dirtt.icelib.main.TransformableEntity;
import java.awt.geom.Rectangle2D;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.dirtt.icelib.main.Solution;
import net.dirtt.utilities.EnumerationIterator;
import java.util.Vector;
import java.util.Iterator;
import java.util.Collection;
import net.dirtt.utilities.MathUtilities;
import net.dirtt.icelib.main.attributes.Attribute;
import net.iceedge.icecore.basemodule.baseclasses.BasicParametricWorksurface;
import net.dirtt.icelib.main.LightWeightTypeObject;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import javax.vecmath.Matrix4f;
import net.dirtt.icebox.canvas2d.Ice2DShapeNode;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;

public class ICDChaseExtrusion extends TransformableTriggerUser implements ICDManufacturingReportable
{
    private Ice2DShapeNode shapeNode;
    private Matrix4f shapeMatrix;
    
    public ICDChaseExtrusion(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.shapeMatrix = new Matrix4f();
        this.setupNamedPoints();
        this.setupNamedRotations();
        this.setupNamedScales();
    }
    
    public Object clone() {
        return this.buildClone(new ICDChaseExtrusion(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDChaseExtrusion buildClone(final ICDChaseExtrusion icdChaseExtrusion) {
        super.buildClone((TransformableTriggerUser)icdChaseExtrusion);
        return icdChaseExtrusion;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDChaseExtrusion(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDChaseExtrusion buildFrameClone(final ICDChaseExtrusion icdChaseExtrusion, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdChaseExtrusion, entityObject);
        return icdChaseExtrusion;
    }
    
    public void setupNamedPoints() {
        this.addNamedPoint("EXT_POS", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("TAB_CONT_LOCATION_A", new Point3f());
        this.addNamedPoint("TAB_CONT_LOCATION_B", new Point3f());
    }
    
    public void setupNamedRotations() {
        this.addNamedRotation("EXT_ROT", new Vector3f(0.0f, 0.0f, 0.0f));
    }
    
    public void setupNamedScales() {
        this.addNamedScale("ASE_length_scale", new Vector3f(1.0f, 1.0f, 1.0f));
    }
    
    protected void calculateNamedScales() {
        super.calculateNamedScales();
        this.getNamedScaleLocal("ASE_length_scale").set(1.0f, 1.0f, this.getZDimension());
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("TAB_CONT_LOCATION_A").set(0.0f, 1.0f, 0.0f);
        this.getNamedPointLocal("TAB_CONT_LOCATION_B").set(this.getNamedPointLocal("extEndPoint").x, this.getNamedPointLocal("extEndPoint").y + 1.0f, this.getNamedPointLocal("extEndPoint").z);
    }
    
    protected void calculateNamedRotations() {
        super.calculateNamedRotations();
        this.getNamedRotationLocal("EXT_ROT").set((float)new Float(Math.toRadians(0.0)), (float)new Float(Math.toRadians(0.0)), (float)new Float(Math.toRadians(90.0)));
    }
    
    public boolean isTopChase() {
        final LightWeightTypeObject lwTypeCreated = this.getLwTypeCreatedFrom();
        return lwTypeCreated != null && "ICD_Top_Chase_Extrusion_LW_Type".equals(lwTypeCreated.getId());
    }
    
    public boolean isBottomChase() {
        final LightWeightTypeObject lwTypeCreated = this.getLwTypeCreatedFrom();
        return lwTypeCreated != null && "ICD_Bottom_Chase_Extrusion_LW_Type".equals(lwTypeCreated.getId());
    }
    
    public BasicParametricWorksurface getParentWorksurface() {
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity instanceof BasicParametricWorksurface) {
            return (BasicParametricWorksurface)parentEntity;
        }
        return null;
    }
    
    public void solve() {
        super.solve();
        this.validatePanelExtrusionBreaks();
    }
    
    protected void calculateDimensions() {
        super.calculateDimensions();
        this.setXDimension(1.0f);
        this.setYDimension(1.0f);
        final BasicParametricWorksurface parentWorksurface = this.getParentWorksurface();
        if (parentWorksurface != null) {
            this.setZDimension(parentWorksurface.getXDimension());
        }
        else {
            System.err.println("Could not set length of chase extrusion. Worksurface is null");
            this.setZDimension(1.0f);
        }
    }
    
    protected void calculateGeometricCenter() {
        this.setGeometricCenterPointLocal(new Point3f(this.getXDimension() * 0.5f, this.getYDimension() * 0.5f, this.getZDimension() * 0.5f));
    }
    
    public boolean isRealChaseExtrusion() {
        final Attribute attributeObject = this.getAttributeObject("is_ICD_Chase_Extrusion");
        return attributeObject != null && "true".equals(attributeObject.getValueAsString());
    }
    
    private void validatePanelExtrusionBreaks() {
        if (this.isRealChaseExtrusion()) {
            this.createAllBreaks();
        }
        else {
            this.removeAllBreaks();
        }
    }
    
    public void createAllBreaks() {
        final ICDPanel panel = this.getPanel(true);
        if (panel != null) {
            this.createPanelExtrusionBreaks(panel, true);
            this.createPanelExtrusionBreaks(panel, false);
        }
        final ICDPanel panel2 = this.getPanel(false);
        if (panel2 != null) {
            this.createPanelExtrusionBreaks(panel2, true);
            this.createPanelExtrusionBreaks(panel2, false);
        }
    }
    
    public void removeAllBreaks() {
        final ICDPanel panel = this.getPanel(true);
        if (panel != null) {
            this.removePanelExtrusionBreaks(panel, true);
            this.removePanelExtrusionBreaks(panel, false);
        }
        final ICDPanel panel2 = this.getPanel(false);
        if (panel2 != null) {
            this.removePanelExtrusionBreaks(panel2, true);
            this.removePanelExtrusionBreaks(panel2, false);
        }
    }
    
    private void createPanelExtrusionBreaks(final ICDPanel icdPanel, final boolean b) {
        if (icdPanel != null) {
            Point3f point3f = new Point3f(0.0f, 0.0f, 0.0f);
            if (!b) {
                point3f = new Point3f(0.0f, 0.0f, this.getZDimension());
            }
            final Point3f convertSpaces = MathUtilities.convertSpaces(point3f, (EntityObject)this, (EntityObject)icdPanel);
            icdPanel.breakHorizontalExtrusion((float)(int)convertSpaces.x, true, true, true);
            icdPanel.breakTileByChaseVertical((float)(int)convertSpaces.x, true);
        }
    }
    
    private void removePanelExtrusionBreaks(final ICDPanel icdPanel, final boolean b) {
        icdPanel.breakHorizontalExtrusion(0.0f, true, true, true);
        icdPanel.breakTileByChaseVertical(0.0f, true);
    }
    
    private ICDPanel getPanel(final boolean b) {
        final Collection<ICDPanel> allPanels = this.getAllPanels();
        if (allPanels != null && allPanels.size() > 0) {
            Point3f point3f = new Point3f(0.0f, 0.0f, 0.0f);
            if (!b) {
                point3f = new Point3f(0.0f, 0.0f, this.getZDimension());
            }
            for (final ICDPanel icdPanel : allPanels) {
                if (icdPanel != null) {
                    final Point3f convertSpaces = MathUtilities.convertSpaces(point3f, (EntityObject)this, (EntityObject)icdPanel);
                    if (Math.abs(convertSpaces.z) < 1.0f && convertSpaces.x > 0.0f && convertSpaces.x < icdPanel.getXDimension()) {
                        return icdPanel;
                    }
                    continue;
                }
            }
        }
        return null;
    }
    
    public void deletedByUser() {
        this.createNewAttribute("is_ICD_Chase_Extrusion", "false");
    }
    
    public Collection<ICDPanel> getAllPanels() {
        final Vector<ICDPanel> vector = new Vector<ICDPanel>();
        final Solution mainSolution = this.getMainSolution();
        if (mainSolution != null) {
            final EnumerationIterator enumerationIterator = new EnumerationIterator(mainSolution.breadthFirstEnumeration());
            while (((Iterator)enumerationIterator).hasNext()) {
                final ICDPanel next = ((Iterator<ICDPanel>)enumerationIterator).next();
                if (next instanceof ICDPanel) {
                    vector.add(next);
                }
            }
        }
        return vector;
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
                System.out.println("Automatically Generated Exception Log(ICDChaseExtrusion.java,365)[" + ex.getClass() + "]: " + ex.getMessage());
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
    }
    
    public void cutFromTree2D() {
        super.cutFromTree2D();
        if (this.shapeNode != null) {
            this.shapeNode.removeFromParent();
        }
    }
    
    public void draw2D(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        if (this.draw2D() && this.isRealChaseExtrusion()) {
            if (this.isDirty(n)) {
                this.draw2DShapeNode(n, ice2DContainer);
            }
        }
        else {
            this.destroy2D();
        }
        super.draw2D(n, ice2DContainer, solutionSetting);
    }
    
    public boolean draw2D() {
        return true;
    }
    
    public void populateCompareNode(final Class clazz, final CompareNode compareNode) {
        compareNode.addCompareValue("length", (Object)this.getLength());
    }
    
    public String getDescription() {
        return super.getDescription() + " " + this.getLength() + "\"";
    }
    
    public float getAttributeValueAsFloat(final String anObject) {
        if ("P1".equals(anObject)) {
            return this.getLength() * super.getAttributeValueAsFloat(anObject);
        }
        return super.getAttributeValueAsFloat(anObject);
    }
    
    public boolean isQuoteable(final String s) {
        return false;
    }
    
    public boolean shouldICDMakePreAssembledReport() {
        return !"true".equals(this.getAttributeValueAsString("isAssembled"));
    }
    
    public void handleWarnings() {
        WarningReason0282.addRequiredWarning((TransformableEntity)this);
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
