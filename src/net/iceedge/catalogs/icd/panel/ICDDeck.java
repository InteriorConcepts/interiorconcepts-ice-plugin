package net.iceedge.catalogs.icd.panel;

import net.dirtt.utilities.MathUtilities;
import net.dirtt.icelib.main.Catalog;
import net.dirtt.icelib.main.CatalogOptionObject;
import java.util.Iterator;
import net.iceedge.icecore.basemodule.triggersystem.TriggerUser;
import net.iceedge.icecore.basemodule.triggersystem.TriggerCube;
import net.dirtt.icelib.main.BoundingCube;
import java.util.Vector;
import net.iceedge.icecore.basemodule.triggersystem.TriggerInterface;
import java.util.Collection;
import net.dirtt.utilities.TypeFilter;
import net.dirtt.utilities.FloatCompare;
import net.dirtt.icelib.report.compare.CompareNode;
import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.text.DecimalFormat;
import java.util.TreeMap;
import org.openmali.vecmath2.TexCoord2f;
import java.util.ArrayList;
import org.xith3d.scenegraph.Node;
import org.xith3d.scenegraph.Geometry;
import org.xith3d.scenegraph.PolygonAttributes;
import org.xith3d.scenegraph.Appearance;
import net.dirtt.icebox.ice3dviewer.Ice3dAppearance;
import org.xith3d.scenegraph.TextureUnit;
import org.openmali.vecmath2.TexCoordf;
import org.xith3d.scenegraph.TriangleArray;
import org.xith3d.scenegraph.Shape3D;
import net.dirtt.utilities.Utility3D;
import org.openmali.vecmath2.Tuple3f;
import net.iceedge.vecmath.MathLibraryConversions;
import javax.vecmath.Matrix4f;
import javax.media.j3d.Transform3D;
import net.iceedge.xith.TransformGroup;
import org.xith3d.scenegraph.Group;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.report.Report;
import net.dirtt.icelib.main.TypeableEntity;
import net.iceedge.icecore.basemodule.util.ChildPool;
import net.iceedge.catalogs.icd.worksurfaces.supports.ICDSupport;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.undo.iceobjects.AttributeHashMapWithUndo;
import net.iceedge.catalogs.icd.worksurfaces.ICDWorksurfaceSKUGenerator;
import org.apache.log4j.Logger;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;

public class ICDDeck extends TransformableTriggerUser implements ICDManufacturingReportable
{
    private static Logger logger;
    private static final int LengthDifferenceFromPanel = 2;
    private static final int LengthDifferenceFromUnderchasePanel = 5;
    private static final int depthDifferenceFfromUnderchasePanel = 1;
    protected static ICDWorksurfaceSKUGenerator skuGenerator;
    protected AttributeHashMapWithUndo catalogAttributeHashMap;
    
    public ICDDeck(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.catalogAttributeHashMap = new AttributeHashMapWithUndo((EntityObject)this);
        if (ICDDeck.skuGenerator == null) {
            ICDDeck.skuGenerator = new ICDWorksurfaceSKUGenerator();
        }
    }
    
    public Object clone() {
        return this.buildClone(new ICDDeck(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDDeck buildClone(final ICDDeck icdDeck) {
        super.buildClone((TransformableTriggerUser)icdDeck);
        return icdDeck;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDDeck(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDDeck buildFrameClone(final ICDDeck icdDeck, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdDeck, entityObject);
        return icdDeck;
    }
    
    public ICDPanel getParentPanel() {
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity instanceof ICDPanel) {
            return (ICDPanel)parentEntity;
        }
        return null;
    }
    
    protected void calculateDimensions() {
        super.calculateDimensions();
        final ICDPanel parentPanel = this.getParentPanel();
        if (parentPanel != null) {
            this.setXDimension(parentPanel.getXDimension() - this.lengthDifferenceFromPanel());
            this.setYDimension(1.0f);
            this.setZDimension(parentPanel.getChaseOffset(this.getSide()) - 1.0f);
        }
    }
    
    private float lengthDifferenceFromPanel() {
        return this.isSuspendedChaseDeck() ? 5.0f : 2.0f;
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        if (this.isSuspendedChaseDeck()) {
            final Point3f basePoint3f = this.getBasePoint3f();
            if (this.getSide() == 0) {
                basePoint3f.x += this.lengthDifferenceFromPanel() / 2.0f;
            }
            else {
                basePoint3f.x -= this.lengthDifferenceFromPanel() / 2.0f;
            }
            this.setBasePoint(basePoint3f);
        }
    }
    
    private boolean isSuspendedChaseDeck() {
        return this.getCurrentType().getId().equals("ICD_Deck_UnderSuspendedChase_Type");
    }
    
    protected void calculateGeometricCenter() {
        super.calculateGeometricCenter();
        this.setGeometricCenterPointLocal(new Point3f(this.getXDimension() / 2.0f, this.getYDimension() / 2.0f, -this.getZDimension() / 2.0f));
    }
    
    public boolean isQuotable() {
        return false;
    }
    
    public int getQuoteReportLevel() {
        return -1;
    }
    
    public void solve() {
        if (this.isModified()) {
            this.validateLBracket();
        }
        this.handleSKUGeneration();
        super.solve();
    }
    
    private void validateLBracket() {
        boolean b = true;
        final ICDPanel parentPanel = this.getParentPanel();
        if (parentPanel != null && parentPanel.isSuspendedChaseHorizontalSplit()) {
            b = false;
        }
        final ChildPool childPool = new ChildPool((TypeableEntity)this, ICDSupport.class);
        if (b) {
            childPool.next();
            childPool.next();
        }
        childPool.destroy();
    }
    
    public boolean shouldAddChildrenToReport(final Report report) {
        return false;
    }
    
    private int getSide() {
        int n = 0;
        if (this.getLwTypeCreatedFrom() == Solution.lwTypeObjectByName("ICD_Deck_UnderSuspendedChase_SideB_Type")) {
            n = 1;
        }
        return n;
    }
    
    public void draw3D(final Group group, final int n) {
        super.draw3D(group, n);
    }
    
    public Group get3DModel() {
        final Group group = new Group();
        final TransformGroup transformGroup = new TransformGroup();
        final Transform3D transform = new Transform3D();
        transform.setIdentity();
        transformGroup.setTransform(transform);
        final Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        matrix4f.setScale(1.0f);
        final ArrayList cubeTriangles = Utility3D.getCubeTriangles(MathLibraryConversions.toJavaPoint3f((Tuple3f)this.getBoundingCube().getLower()), MathLibraryConversions.toJavaPoint3f((Tuple3f)this.getBoundingCube().getUpper()));
        final Point3f[] point3fArray = Utility3D.toPoint3fArray(cubeTriangles);
        final Shape3D shape3D = new Shape3D();
        final TriangleArray geometry = new TriangleArray(point3fArray.length);
        geometry.setCoordinates(0, (Tuple3f[])MathLibraryConversions.toXithArrayPoint3f((javax.vecmath.Tuple3f[])point3fArray));
        final TexCoord2f[] cubeMapping = Utility3D.getCubeMapping(matrix4f, Utility3D.toPoint3fArray(cubeTriangles));
        geometry.setTextureCoordinates(0, 0, (TexCoordf[])cubeMapping);
        geometry.setTextureCoordinates(1, 0, (TexCoordf[])cubeMapping);
        (new TextureUnit[1])[0] = Ice3dAppearance.getNewOrExistingTextureUnit();
        final Appearance appearance = new Appearance();
        appearance.setPolygonAttributes(Ice3dAppearance.getNewOrExistingPolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_BACK, 0.0f));
        shape3D.setName("WKSLAM_UV3_WKSLAM");
        shape3D.setGeometry((Geometry)geometry);
        shape3D.setAppearance(appearance);
        transformGroup.addChild((Node)shape3D);
        group.addChild((Node)transformGroup);
        return group;
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
        treeMap.put("Depth", this.getZDimension() + "");
        final DecimalFormat decimalFormat = new DecimalFormat("#.##");
        treeMap.put("Width", Double.valueOf(decimalFormat.format(this.getXDimension())) + "");
        treeMap.put("Height", Double.valueOf(decimalFormat.format(0.75)) + "");
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
    
    public void populateCompareNode(final Class clazz, final CompareNode compareNode) {
        super.populateCompareNode(clazz, compareNode);
        compareNode.addCompareValue("length", (Object)new FloatCompare(this.getLength()));
        final ICDPanel icdPanel = (ICDPanel)this.getParent((TypeFilter)new ICDPanelFilter());
        if (icdPanel.hasChase()) {
            float n;
            if (icdPanel.isChaseSideA()) {
                n = icdPanel.getAttributeValueAsFloat("Chase_Side_A_Distance");
            }
            else {
                n = icdPanel.getAttributeValueAsFloat("Chase_Side_B_Distance");
            }
            compareNode.addCompareValue("depth", (Object)new FloatCompare(n));
        }
    }
    
    protected Collection<TriggerInterface> getHACKTriggers() {
        final BoundingCube boundingCube = this.getBoundingCube();
        if (boundingCube != null) {
            Collection<TriggerInterface> hackTriggers = (Collection<TriggerInterface>)super.getHACKTriggers();
            if (hackTriggers == null) {
                hackTriggers = new Vector<TriggerInterface>();
            }
            final float n = 0.1f;
            final float n2 = 0.1f;
            final BoundingCube boundingCube2 = new BoundingCube(new Point3f(boundingCube.getLower().getX() - n, boundingCube.getLower().getY() - n2, boundingCube.getLower().getZ() - n), new Point3f(boundingCube.getUpper().getX() + n, boundingCube.getUpper().getY() + n2, boundingCube.getUpper().getZ() + n));
            boundingCube2.setTransform(this.getEntWorldSpaceMatrix());
            hackTriggers.add((TriggerInterface)new TriggerCube("Tab_Location", (Float)null, boundingCube2, (TriggerUser)this));
            return hackTriggers;
        }
        return (Collection<TriggerInterface>)super.getHACKTriggers();
    }
    
    public String getFinishCodeForManufacturingReport() {
        String s = "M";
        final Iterator<EntityObject> iterator = (Iterator<EntityObject>)this.getChildrenVector().iterator();
        while (iterator.hasNext()) {
            final String attributeValueAsString = iterator.next().getAttributeValueAsString("Option_Indicator");
            if (attributeValueAsString != null && attributeValueAsString.toLowerCase().contains("laminate")) {
                s = "L";
                break;
            }
        }
        return s;
    }
    
    public String getShapeTag() {
        return "Deck";
    }
    
    public float getXDimensionForReport() {
        return this.getXDimension();
    }
    
    public float getYDimensionForReport() {
        return this.getZDimension();
    }
    
    protected void handleSKUGeneration() {
        final String generateSKU = ICDDeck.skuGenerator.generateSKU((TypeableEntity)this);
        final CatalogOptionObject catalogPart = this.getCurrentOption().getCatalogPart();
        if (catalogPart == null || "DECK".equalsIgnoreCase(catalogPart.getPartName())) {
            this.getCurrentOption().resetCatalogPart();
            this.getCurrentOption().linkToCatalogPart(true, generateSKU);
        }
        final String sku = this.getSKU();
        if (sku == null) {
            this.createNewAttribute("Base_SKU", generateSKU);
        }
        else if (!sku.equals(generateSKU)) {
            this.modifyAttributeValue("Base_SKU", generateSKU);
            this.setModified(true);
        }
    }
    
    public String getSKU() {
        return this.getAttributeValueAsString("Base_SKU");
    }
    
    public String getDescription() {
        final String sku = this.getSKU();
        if (sku != null) {
            final Catalog catalog = Solution.getCatalogs().get("ICI_ICD");
            if (catalog != null) {
                final CatalogOptionObject loadCatalogOption = OptionObject.loadCatalogOption(catalog, sku, true);
                if (loadCatalogOption != null) {
                    return loadCatalogOption.getDescription() + " - Real Size: " + MathUtilities.round(this.getZDimension(), 1) + "dX" + MathUtilities.round(this.getXDimension(), 1) + "w";
                }
            }
        }
        return super.getDescription();
    }
    
    static {
        ICDDeck.logger = Logger.getLogger(ICDDeck.class);
        ICDDeck.skuGenerator = null;
    }
}
