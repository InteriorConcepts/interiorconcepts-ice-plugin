package net.iceedge.catalogs.icd.worksurfaces;

import com.iceedge.icd.reporting.ICDManufacturingUtils;
import net.iceedge.icecore.icecad.ice.tree.IceCadCompositeBlock;
import net.dirtt.icebox.canvas2d.Ice2DDXFLWNode;
import org.xith3d.scenegraph.Shape3D;
import net.iceedge.icecore.basemodule.interfaces.lightweight.Paintable3D;
import net.iceedge.icebox.utilities.AssetCache;
import org.xith3d.scenegraph.Group;
import net.iceedge.icecore.basemodule.baseclasses.material.BasicMaterialEntity;
import java.util.TreeMap;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricCutout;
import java.util.ArrayList;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDWireDip;
import net.dirtt.utilities.EntitySpaceCompareNodeWrapper;
import net.dirtt.utilities.EntitySpaceCompareNode;
import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.report.compare.CompareNode;
import net.iceedge.catalogs.icd.panel.ICDPanel;
import net.iceedge.icecore.basemodule.triggersystem.TriggerUser;
import net.iceedge.icecore.basemodule.triggersystem.TriggerCube;
import net.iceedge.icecore.basemodule.triggersystem.TriggerInterface;
import net.dirtt.icelib.main.BoundingCube;
import java.util.Collection;
import javax.swing.tree.DefaultMutableTreeNode;
import net.iceedge.icebox.utilities.Node;
import java.io.IOException;
import net.iceedge.icebox.custom.Utilities.CustomObjectUtilities;
import net.dirtt.utilities.PersistentFileManager;
import net.dirtt.xmlFiles.XMLWriter;
import net.dirtt.icelib.main.RequiredChildTypeContainer;
import java.util.HashMap;
import net.dirtt.icelib.main.attributes.proxy.AttributeProxy;
import net.dirtt.icelib.main.Catalog;
import net.iceedge.icecore.icecad.ice.IceCadIceApp;
import net.iceedge.icecore.icecad.ice.tree.IceCadNodeContainer;
import net.dirtt.icecad.cadtree.ICadTreeNode;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.dirtt.icelib.main.Solution;
import net.iceedge.icecore.basemodule.baseclasses.grips.GripListener;
import net.iceedge.icecore.basemodule.baseclasses.grips.RelativeAttributeGrip;
import java.util.LinkedList;
import java.util.List;
import net.dirtt.icelib.main.attributes.Attribute;
import net.dirtt.icelib.ui.attribute.explorer.ui.entity.PossibleValue;
import java.util.TreeSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.AttributeGripPoint;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import net.dirtt.icelib.main.attributes.FloatAttribute;
import net.iceedge.icecore.basemodule.finalclasses.GeneralSnapSet;
import javax.vecmath.Matrix4f;
import net.dirtt.utilities.MathUtilities;
import javax.vecmath.Tuple3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import net.iceedge.icebox.custom.CustomProperties;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.TypeableEntity;
import net.iceedge.catalogs.icd.ICDILine;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.utilities.EntityBestPointToILineSnapper;
import net.iceedge.icebox.custom.CustomContainer;
import org.apache.log4j.Logger;
import net.dirtt.icelib.undo.iceobjects.AttributeHashMapWithUndo;
import net.dirtt.icelib.main.CatalogOptionObject;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.interfaces.StorageSnappable;
import net.iceedge.icebox.custom.CustomizableEntity;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrippable;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;

public class ICDBasicWorksurface extends TransformableTriggerUser implements BasicAttributeGrippable, CustomizableEntity, StorageSnappable, ICDManufacturingReportable
{
    protected BasicAttributeGrip rightWidthGrip;
    protected BasicAttributeGrip bottomDepthGrip;
    protected float oldWidth;
    protected float oldDepth;
    protected static ICDWorksurfaceSKUGenerator skuGenerator;
    protected CatalogOptionObject currentCatalogOption;
    protected AttributeHashMapWithUndo catalogAttributeHashMap;
    protected static Logger logger;
    protected WidthGripListener widthGripListener;
    protected DepthGripListener depthGripListener;
    protected boolean gripsMoved;
    protected float grippedNewDepth;
    protected float grippedNewWidth;
    protected float chaseDepth;
    private CustomContainer customContainer;
    private boolean getCustomIfAvailable;
    protected EntityBestPointToILineSnapper iLinesnapper;
    
    public ICDBasicWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.oldWidth = this.getXDimensionFromData();
        this.oldDepth = this.getYDimensionFromData();
        this.currentCatalogOption = null;
        this.catalogAttributeHashMap = new AttributeHashMapWithUndo((EntityObject)this);
        this.widthGripListener = new WidthGripListener();
        this.depthGripListener = new DepthGripListener();
        this.gripsMoved = false;
        this.grippedNewDepth = this.getYDimensionFromData();
        this.grippedNewWidth = this.getXDimensionFromData();
        this.getCustomIfAvailable = true;
        this.iLinesnapper = new EntityBestPointToILineSnapper((TypeableEntity)this, new Class[] { ICDILine.class });
        this.setupGripPainters();
        this.setupNamedPoints();
        this.setupNamedRotations();
        this.customContainer = new CustomContainer();
        if (ICDBasicWorksurface.skuGenerator == null) {
            ICDBasicWorksurface.skuGenerator = new ICDWorksurfaceSKUGenerator();
        }
    }
    
    public TransformableEntity buildClone(final ICDBasicWorksurface icdBasicWorksurface) {
        super.buildClone((TransformableTriggerUser)icdBasicWorksurface);
        if (!this.isFixed()) {
            icdBasicWorksurface.setGrippedNewDepth(this.grippedNewDepth);
            icdBasicWorksurface.setGrippedNewWidth(this.grippedNewWidth);
            icdBasicWorksurface.refreshNamedPoints();
        }
        if (this.isCustomized()) {
            icdBasicWorksurface.customContainer.setProperties(new CustomProperties(this.getCustomProperties()), false);
        }
        return (TransformableEntity)icdBasicWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDBasicWorksurface icdBasicWorksurface) {
        super.buildClone2((TransformableTriggerUser)icdBasicWorksurface);
        return (TransformableEntity)icdBasicWorksurface;
    }
    
    public Object clone() {
        return this.buildClone(new ICDBasicWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    protected void refreshNamedPoints() {
        for (int i = this.getChildrenByClass((Class)ICDChildModel.class, false, true).size(); i > 0; --i) {
            if (this.getNamedScaleLocal("SCL" + i) == null) {
                this.addNamedScale("SCL" + i, new Vector3f(1.0f, 1.0f, 1.0f));
            }
            if (this.getNamedPointLocal("POS" + i) == null) {
                this.addNamedPoint("POS" + i, new Point3f(0.0f, 0.0f, 0.0f));
            }
        }
    }
    
    protected void setupNamedPoints() {
        this.addNamedPoint("Top_Left_Snap_Corner", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("Top_Right_Snap_Corner", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("Top_Left_3D", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("Top_Right_3D", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("Bottom_Left_3D", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("Bottom_Right_3D", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("WSBL", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("WSBR", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("WSBM", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("WSFL", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("WSFR", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("RH_Back_Corner", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("LH_Back_Corner", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("D_Column", new Point3f());
    }
    
    public float getSnapRotation() {
        return 0.0f;
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        final Point3f point3f = new Point3f(this.getGeometricCenterPointLocal());
        point3f.x -= this.getXDimension() / 2.0f;
        point3f.y += this.getYDimension() / 2.0f;
        final Point3f point3f2 = new Point3f(this.getGeometricCenterPointLocal());
        point3f2.x += this.getXDimension() / 2.0f;
        point3f2.y += this.getYDimension() / 2.0f;
        final Point3f point3f3 = new Point3f(this.getGeometricCenterPointLocal());
        point3f3.x -= this.getXDimension() / 2.0f;
        point3f3.y -= this.getYDimension() / 2.0f;
        final Point3f point3f4 = new Point3f(this.getGeometricCenterPointLocal());
        point3f4.x += this.getXDimension() / 2.0f;
        point3f4.y -= this.getYDimension() / 2.0f;
        this.getNamedPointLocal("Top_Left_Snap_Corner").set((Tuple3f)point3f);
        this.getNamedPointLocal("Top_Right_Snap_Corner").set((Tuple3f)point3f2);
        this.getNamedPointLocal("Top_Left_3D").set(point3f.x, point3f.y, this.getHeightModifier());
        this.getNamedPointLocal("Top_Right_3D").set(point3f2.x, point3f2.y, this.getHeightModifier());
        this.getNamedPointLocal("Bottom_Left_3D").set(point3f3.x, point3f3.y, this.getHeightModifier());
        this.getNamedPointLocal("Bottom_Right_3D").set(point3f4.x, point3f4.y, this.getHeightModifier());
        final Point3f geometricCenterPointLocal = this.getGeometricCenterPointLocal();
        this.chaseDepth = this.getChaseDepthForWorksurfaceSupports();
        final float n = this.getXDimension() / 2.0f;
        final float n2 = this.getYDimension() / 2.0f;
        final float n3 = this.getZDimension() / 2.0f;
        this.getNamedPointLocal("WSBL").set(geometricCenterPointLocal.x - n, geometricCenterPointLocal.y + n2 - this.chaseDepth, geometricCenterPointLocal.z - n3);
        this.getNamedPointLocal("WSBR").set(geometricCenterPointLocal.x + n, geometricCenterPointLocal.y + n2 - this.chaseDepth, geometricCenterPointLocal.z - n3);
        this.getNamedPointLocal("WSBM").set(geometricCenterPointLocal.x, geometricCenterPointLocal.y + n2 - this.chaseDepth, geometricCenterPointLocal.z - n3);
        float n4 = this.getLeftDepth();
        float n5 = this.getRightDepth();
        if (n4 < 0.001f) {
            n4 = this.getYDimension();
        }
        if (n5 < 0.001f) {
            n5 = this.getYDimension();
        }
        this.getNamedPointLocal("WSFL").set(geometricCenterPointLocal.x - n, geometricCenterPointLocal.y + n2 - n4, geometricCenterPointLocal.z - n3);
        this.getNamedPointLocal("WSFR").set(geometricCenterPointLocal.x + n, geometricCenterPointLocal.y + n2 - n5, geometricCenterPointLocal.z - n3);
    }
    
    protected void setupNamedRotations() {
        this.addNamedRotation("Left_Support_Angle", new Vector3f());
        this.addNamedRotation("Right_Support_Angle", new Vector3f());
    }
    
    protected void calculateNamedRotations() {
        super.calculateNamedRotations();
        this.getNamedRotationLocal("Left_Support_Angle").set(0.0f, 0.0f, MathUtilities.ANGLE_270);
        this.getNamedRotationLocal("Right_Support_Angle").set(0.0f, 0.0f, MathUtilities.ANGLE_270);
    }
    
    protected void calculateDimensionsCenterPoint() {
        super.calculateDimensionsCenterPoint();
        this.setGeometricCenterPointLocal(new Point3f(this.getXDimension() / 2.0f, -(this.getYDimension() / 2.0f), 0.0f));
    }
    
    public void calculateASETransformMatrix() {
        if (this.customContainer.hasCustom3DAsset()) {
            if (this.entityASETransformMatrix != null) {
                this.entityASETransformMatrix.setIdentity();
                final Matrix4f user3DTransform = this.getUser3DTransform();
                if (user3DTransform != null) {
                    this.entityASETransformMatrix.mul(user3DTransform);
                }
            }
        }
        else {
            super.calculateASETransformMatrix();
        }
    }
    
    public String getWorksurfaceType() {
        return this.getAttributeValueAsString("ICD_Worksurface_Type");
    }
    
    public void solve() {
        final boolean b = this.isModified() && this.shouldModify();
        this.validateHeight();
        super.solve();
        this.handleScaleChanges(this.oldWidth, this.grippedNewWidth, this.oldDepth, this.grippedNewDepth);
        if (b) {
            this.refreshNamedPoints();
        }
        this.handleSKUGeneration();
        if (b) {
            super.solve();
        }
        this.gripsMoved = false;
    }
    
    public void handleSnap() {
        final float xDimension = this.getXDimension();
        float yDimension = this.getYDimension();
        if (yDimension < xDimension) {
            yDimension = xDimension;
        }
        final EntityObject bestSnapHost = this.iLinesnapper.findBestSnapHost(yDimension / 2.0f + 0.51f);
        if (bestSnapHost != null && bestSnapHost.getParent() != null && !bestSnapHost.getParent().equals(this.getParent())) {
            final GeneralSnapSet set = (GeneralSnapSet)bestSnapHost.getParent((Class)GeneralSnapSet.class);
            this.cutTriggers();
            if (set != null) {
                set.addToTree((EntityObject)this);
            }
            this.setModified(true);
        }
    }
    
    protected boolean shouldModify() {
        return true;
    }
    
    protected void validateHeight() {
        final FloatAttribute floatAttribute = (FloatAttribute)this.getAttributeObject(this.getHeightAttributeKey());
        if (floatAttribute != null) {
            final float currentValue = floatAttribute.getCurrentValue();
            final Point3f basePoint3f = this.getBasePoint3f();
            this.setBasePoint(basePoint3f.x, basePoint3f.y, currentValue - this.getHeightModifier());
        }
    }
    
    public float getHeightModifier() {
        return 1.25f;
    }
    
    public String getHeightAttributeKey() {
        return "ICD_Height_From_Floor";
    }
    
    public void handleScaleChanges(float xDimensionFromData, float oldWidth, float yDimensionFromData, float oldDepth) {
        if (!this.isFixed()) {
            if (xDimensionFromData == -1.0f) {
                oldWidth = (xDimensionFromData = this.getXDimensionFromData());
            }
            if (yDimensionFromData == -1.0f) {
                oldDepth = (yDimensionFromData = this.getYDimensionFromData());
            }
            this.oldWidth = oldWidth;
            this.oldDepth = oldDepth;
            final String attributeValueAsString = this.getAttributeValueAsString("ICD_Scale_Rules");
            if (attributeValueAsString != null) {
                final Vector<String> vector = new Vector<String>();
                final StringTokenizer stringTokenizer = new StringTokenizer(attributeValueAsString, ",");
                String e = stringTokenizer.nextToken();
                while (e != null) {
                    vector.add(e);
                    if (stringTokenizer.hasMoreTokens()) {
                        e = stringTokenizer.nextToken();
                    }
                    else {
                        e = null;
                    }
                }
                for (final String s : vector) {
                    final Vector3f namedScaleLocal = this.getNamedScaleLocal("SCL" + s);
                    final Point3f namedPointLocal = this.getNamedPointLocal("POS" + s);
                    if (namedScaleLocal != null && namedPointLocal != null) {
                        for (final ICDChildModel icdChildModel : this.getChildrenByClass((Class)ICDChildModel.class, false, true)) {
                            if (s.equals(icdChildModel.getModelNumber())) {
                                icdChildModel.scaleModels(namedScaleLocal, oldWidth, oldDepth);
                            }
                        }
                    }
                }
            }
            this.applyDimensionChanges(oldWidth, oldDepth);
            this.setChildrenModified(true);
            this.setModified(true);
            this.calculate();
            this.updateGrips(this.rightWidthGrip);
            this.updateGrips(this.bottomDepthGrip);
        }
    }
    
    protected void applyDimensionChanges(final float n, final float n2) {
        this.setXDimension(n);
        this.setYDimension(n2);
        this.applyChangesForAttribute("XDimension", n + "");
        this.applyChangesForAttribute("YDimension", n2 + "");
    }
    
    public SortedSet<AttributeGripPoint> getAttributeMap(final BasicAttributeGrip basicAttributeGrip) {
        final TreeSet<AttributeGripPoint> set = new TreeSet<AttributeGripPoint>();
        if (this.getGeometricCenterPointWorld() != null) {
            if (basicAttributeGrip.equals(this.rightWidthGrip)) {
                final Attribute attributeObject = this.getAttributeObject("ICD_Scalable_Worksurface_Width");
                if (attributeObject != null) {
                    final float z = this.getGeometricCenterPointWorld().z;
                    final Iterator<PossibleValue> iterator = this.getAttributePossibleValues("ICD_Scalable_Worksurface_Width", false, this.getCurrentType()).iterator();
                    while (iterator.hasNext()) {
                        final String value = iterator.next().getValue();
                        final Attribute attribute = (Attribute)attributeObject.clone();
                        attribute.setCurrentValueAsString(value);
                        final int n = (int)Float.parseFloat(value);
                        if (n >= this.getMinXDimension()) {
                            if (n > this.getMaxXDimension()) {
                                continue;
                            }
                            set.add(new AttributeGripPoint(new Point3f((float)n, -this.getYDimensionFromData() / 2.0f, z), n, new Attribute[] { attribute }));
                        }
                    }
                }
            }
            else if (basicAttributeGrip.equals(this.bottomDepthGrip)) {
                final Attribute attributeObject2 = this.getAttributeObject("ICD_Scalable_Worksurface_Depth");
                if (attributeObject2 != null) {
                    final float z2 = this.getGeometricCenterPointWorld().z;
                    final Iterator<PossibleValue> iterator2 = this.getAttributePossibleValues("ICD_Scalable_Worksurface_Depth", false, this.getCurrentType()).iterator();
                    while (iterator2.hasNext()) {
                        final String value2 = iterator2.next().getValue();
                        final Attribute attribute2 = (Attribute)attributeObject2.clone();
                        attribute2.setCurrentValueAsString(value2);
                        final int n2 = (int)Float.parseFloat(value2);
                        if (n2 >= this.getMinYDimension()) {
                            if (n2 > this.getMaxYDimension()) {
                                continue;
                            }
                            set.add(new AttributeGripPoint(new Point3f(this.getXDimensionFromData() / 2.0f, (float)(-n2), z2), n2, new Attribute[] { attribute2 }));
                        }
                    }
                }
            }
        }
        return set;
    }
    
    public List<BasicAttributeGrip> getGrips() {
        final LinkedList<BasicAttributeGrip> list = new LinkedList<BasicAttributeGrip>();
        list.add(this.rightWidthGrip);
        list.add(this.bottomDepthGrip);
        return list;
    }
    
    public void setupGripPainters() {
        if (!this.isFixed()) {
            (this.rightWidthGrip = (BasicAttributeGrip)new RelativeAttributeGrip((TransformableEntity)this, 0)).setLinkID((byte)99);
            (this.bottomDepthGrip = (BasicAttributeGrip)new RelativeAttributeGrip((TransformableEntity)this, 2)).setLinkID((byte)98);
            this.rightWidthGrip.addListener((GripListener)this.widthGripListener);
            this.bottomDepthGrip.addListener((GripListener)this.depthGripListener);
        }
    }
    
    public void updateGrips(final BasicAttributeGrip basicAttributeGrip) {
        if (this.getGeometricCenterPointWorld() != null) {
            AttributeGripPoint attributeGripPoint = null;
            AttributeGripPoint attributeGripPoint2 = null;
            if (basicAttributeGrip.equals(this.rightWidthGrip)) {
                attributeGripPoint = new AttributeGripPoint(new Attribute[] { this.getAttributeObject("ICD_Scalable_Worksurface_Width") });
            }
            else if (basicAttributeGrip.equals(this.bottomDepthGrip)) {
                attributeGripPoint2 = new AttributeGripPoint(new Attribute[] { this.getAttributeObject("ICD_Scalable_Worksurface_Depth") });
            }
            this.updateGripPositions(attributeGripPoint2, attributeGripPoint);
        }
    }
    
    protected void updateGripPositions(final AttributeGripPoint attributeGripPoint, final AttributeGripPoint attributeGripPoint2) {
        if (attributeGripPoint != null) {
            this.bottomDepthGrip.updateGrip(attributeGripPoint);
            this.bottomDepthGrip.setLocation(new Point3f(this.getXDimensionFromData() / 2.0f, -this.getYDimensionFromData(), this.getGeometricCenterPointWorld().z));
            this.bottomDepthGrip.setAnchorLocation(new Point3f(this.getXDimensionFromData() / 2.0f, 0.0f, this.getGeometricCenterPointWorld().z));
        }
        if (attributeGripPoint2 != null) {
            this.rightWidthGrip.updateGrip(attributeGripPoint2);
            this.rightWidthGrip.setLocation(new Point3f(this.getXDimensionFromData(), -this.getYDimensionFromData() / 2.0f, this.getGeometricCenterPointWorld().z));
            this.rightWidthGrip.setAnchorLocation(new Point3f(0.0f, -this.getYDimensionFromData() / 2.0f, this.getGeometricCenterPointWorld().z));
        }
    }
    
    public void setSelected(final boolean b, final Solution solution) {
        super.setSelected(b, solution);
        this.selectGrips(b);
    }
    
    public void draw2D(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        if (this.currentCatalogOption != null && !this.currentCatalogOption.hasSVGObject() && !this.isFixed()) {
            this.drawWarning(ice2DContainer);
            this.drawChildren2D(n, ice2DContainer, solutionSetting);
        }
        else {
            super.draw2D(n, ice2DContainer, solutionSetting);
        }
        this.drawGrips(n, ice2DContainer, solutionSetting);
    }
    
    public void drawCad(final ICadTreeNode cadTreeNode, final int n) {
        if (this.currentCatalogOption != null && !this.currentCatalogOption.hasSvgGvtObject() && !this.isFixed()) {
            this.drawCadWarning(cadTreeNode);
            this.drawChildrenCad(cadTreeNode, n);
        }
        else {
            super.drawCad(cadTreeNode, n);
        }
    }
    
    public void drawIceCadDotNet(final int n, final IceCadNodeContainer iceCadNodeContainer, final IceCadIceApp iceCadIceApp) {
        if (this.currentCatalogOption != null && !this.currentCatalogOption.hasSvgGvtObject() && !this.isFixed()) {
            this.drawIceCadWarningDotNet(iceCadNodeContainer);
            this.drawChildrenIceCadDotNet(n, iceCadNodeContainer, iceCadIceApp);
        }
        else {
            super.drawIceCadDotNet(n, iceCadNodeContainer, iceCadIceApp);
        }
    }
    
    protected void drawBottomDepthGrip(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        if (this.bottomDepthGrip != null) {
            this.bottomDepthGrip.draw2D(n, ice2DContainer, solutionSetting);
        }
    }
    
    protected void drawRightWidthGrip(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        if (this.rightWidthGrip != null) {
            this.rightWidthGrip.draw2D(n, ice2DContainer, solutionSetting);
        }
    }
    
    public void destroy() {
        super.destroy();
        if (this.customContainer != null) {
            this.customContainer.destroy();
        }
        this.destroyGrips();
    }
    
    protected boolean forceDraw3DSelection() {
        return true;
    }
    
    public void calculateBoundingCube() {
        this.boundingCube.setLower(0.0f, -this.getYDimensionFromData() - 0.25f, 0.0f);
        this.boundingCube.setUpper(this.getXDimensionFromData(), 0.25f, 1.5f);
    }
    
    protected float getXLockedValue() {
        return this.getAttributeValueAsFloat("ICD_Locked_X_Percentage");
    }
    
    protected float getYLockedValue() {
        return this.getAttributeValueAsFloat("ICD_Locked_Y_Percentage");
    }
    
    public void moveChildren(final char c, final float n) {
        for (final ICDChildModel icdChildModel : this.getChildrenByClass((Class)ICDChildModel.class, false, true)) {
            final Point3f namedPointLocal = icdChildModel.getNamedPointLocal("SVG_POS");
            final Point3f namedPointLocal2 = icdChildModel.getNamedPointLocal("ASE_POS");
            if (namedPointLocal != null && namedPointLocal2 != null) {
                if (icdChildModel.movesOnX() && c == 'X') {
                    namedPointLocal.set(n, namedPointLocal.y, namedPointLocal.z);
                    namedPointLocal2.set(n, namedPointLocal2.y, namedPointLocal2.z);
                }
                else if (icdChildModel.movesOnY() && c == 'Y') {
                    namedPointLocal.set(namedPointLocal.x, -n, namedPointLocal.z);
                    namedPointLocal2.set(namedPointLocal2.x, -n, namedPointLocal2.z);
                }
                icdChildModel.calculate();
            }
        }
    }
    
    public float getMaxXDimension() {
        return 144.0f;
    }
    
    public float getMaxYDimension() {
        return 144.0f;
    }
    
    public float getMinXDimension() {
        return 18.0f;
    }
    
    public float getMinYDimension() {
        return 18.0f;
    }
    
    public String getShapeTag() {
        return "";
    }
    
    protected void handleSKUGeneration() {
        String attributeValueAsString = null;
        String anObject;
        if (!this.isFixed()) {
            anObject = ICDBasicWorksurface.skuGenerator.generateSKU((TypeableEntity)this);
        }
        else {
            anObject = this.getSKU();
        }
        if (this.currentCatalogOption != null) {
            attributeValueAsString = this.currentCatalogOption.getAttributeValueAsString("PN");
        }
        if (this.currentCatalogOption == null || attributeValueAsString == null || !attributeValueAsString.equals(anObject)) {
            final Catalog catalog = Solution.getCatalogs().get("ICI_ICD");
            if (catalog != null) {
                final OptionObject currentOption = this.currentOption;
                this.currentCatalogOption = OptionObject.loadCatalogOption(catalog, anObject, true);
                this.handleDynamicAttributes();
                this.createNewAttribute("Base_SKU", anObject);
            }
        }
    }
    
    public void handleDynamicAttributes() {
        final HashMap attributeList = this.currentCatalogOption.getAttributeList();
        if (attributeList != null) {
            for (final String str : attributeList.keySet()) {
                final Attribute attribute = (Attribute)attributeList.get(str);
                final AttributeProxy attributeProxy = Solution.getWorldAttributeProxy().get(str);
                if (attributeProxy != null && attribute != null) {
                    final Attribute attributeObject = Attribute.createAttributeObject(attributeProxy.getAttributeClass(), str, attribute.getValueAsString());
                    if (attributeObject == null) {
                        continue;
                    }
                    this.catalogAttributeHashMap.put(str, attributeObject);
                }
                else {
                    ICDBasicWorksurface.logger.warn((Object)("Error loading possible attribute into " + this.getCurrentOption().getId() + ". No Attribute Proxy for [" + str + "]"));
                }
            }
        }
    }
    
    public String getManufacturerForQuote() {
        if (this.currentCatalogOption != null) {
            return this.currentCatalogOption.getCatalog().getProduceCode();
        }
        return super.getProductForQuote();
    }
    
    public String getProductForQuote() {
        return this.getManufacturerForQuote();
    }
    
    public boolean isCatalogPartPresent() {
        return this.currentCatalogOption != null;
    }
    
    public boolean containsAttributeKey(final String s) {
        return super.containsAttributeKey(s) || this.catalogAttributeHashMap.containsKey((Object)s);
    }
    
    public Attribute getAttributeObject(final String s) {
        Attribute attribute = this.getAttributeHashMap().get((Object)s);
        if (attribute == null && this.getCurrentOption() != null) {
            attribute = this.getCurrentOption().getAttributeObject(s);
        }
        if (attribute == null) {
            attribute = this.catalogAttributeHashMap.get((Object)s);
        }
        if (attribute == null || attribute.isNULL()) {
            final AttributeProxy attributeProxyByKey = AttributeProxy.getAttributeProxyByKey(s);
            if (attributeProxyByKey != null && attributeProxyByKey.isAutoInherited()) {
                final EntityObject parentEntity = this.getParentEntity();
                if (parentEntity != null) {
                    return parentEntity.getAttributeObject(s);
                }
            }
        }
        return attribute;
    }
    
    public void getRequiredCatalogChildren(final Vector<RequiredChildTypeContainer> vector) {
        if (this.isCatalogPartPresent()) {
            final Vector<RequiredChildTypeContainer> vector2 = new Vector<RequiredChildTypeContainer>();
            for (final RequiredChildTypeContainer e : vector) {
                try {
                    if (!"CAT".equals(e.getType().getId().substring(0, 3))) {
                        continue;
                    }
                    vector2.add(e);
                }
                catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
            final Iterator<RequiredChildTypeContainer> iterator2 = vector2.iterator();
            while (iterator2.hasNext()) {
                vector.remove(iterator2.next());
            }
            for (final RequiredChildTypeContainer requiredChildTypeContainer : this.currentCatalogOption.getTypeKeyList()) {
                if (!vector.contains(requiredChildTypeContainer)) {
                    vector.add(requiredChildTypeContainer);
                }
            }
        }
        else {
            super.getRequiredCatalogChildren((Vector)vector);
        }
    }
    
    public String getDescription() {
        String s = null;
        if (this.isCustomized()) {
            s = this.getCustomProperties().getDescription();
        }
        if (s == null && !this.isFixed() && this.currentCatalogOption != null) {
            s = this.currentCatalogOption.getDescription() + " - Real Size: " + MathUtilities.round(this.getYDimension(), 1) + "dX" + MathUtilities.round(this.getXDimension(), 1) + "w";
        }
        return (s != null) ? s : super.getDescription();
    }
    
    public String getQuoteDescription() {
        return this.getDescription();
    }
    
    public float getXDimensionFromData() {
        return this.getAttributeValueAsFloat("XDimension");
    }
    
    public float getYDimensionFromData() {
        return this.getAttributeValueAsFloat("YDimension");
    }
    
    public boolean isRightHanded() {
        return "right".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Worksurface_Hand_Style"));
    }
    
    public void addToCutSolution(final Vector vector, final Vector vector2, final Solution solution, final boolean b) {
        this.deselectGrips();
        super.addToCutSolution(vector, vector2, solution, b);
    }
    
    public void setGrippedNewDepth(final float grippedNewDepth) {
        this.grippedNewDepth = grippedNewDepth;
    }
    
    public void setGrippedNewWidth(final float grippedNewWidth) {
        this.grippedNewWidth = grippedNewWidth;
    }
    
    public boolean isFixed() {
        return "fixed".equals(this.getWorksurfaceType());
    }
    
    protected void writeXMLFields(final XMLWriter xmlWriter, final PersistentFileManager.FileWriter fileWriter) throws IOException {
        super.writeXMLFields(xmlWriter, fileWriter);
        xmlWriter.writeTextElement("grippedDepth", this.grippedNewDepth + "");
        xmlWriter.writeTextElement("grippedWidth", this.grippedNewWidth + "");
        xmlWriter.writeTextElement("oldDepth", this.oldDepth + "");
        xmlWriter.writeTextElement("oldWidth", this.oldWidth + "");
        CustomObjectUtilities.writeXMLFields((CustomizableEntity)this, xmlWriter, fileWriter);
    }
    
    protected void setFieldInfoFromXML(final Node node, final DefaultMutableTreeNode defaultMutableTreeNode, final PersistentFileManager.FileReader fileReader) {
        super.setFieldInfoFromXML(node, defaultMutableTreeNode, fileReader);
        final String childElementValue = this.getChildElementValue("oldDepth", node);
        final String childElementValue2 = this.getChildElementValue("oldWidth", node);
        final String childElementValue3 = this.getChildElementValue("grippedDepth", node);
        final String childElementValue4 = this.getChildElementValue("grippedWidth", node);
        if (childElementValue != null) {
            this.oldDepth = Float.parseFloat(childElementValue);
        }
        if (childElementValue2 != null) {
            this.oldWidth = Float.parseFloat(childElementValue2);
        }
        if (childElementValue3 != null) {
            this.grippedNewDepth = Float.parseFloat(childElementValue3);
        }
        if (childElementValue4 != null) {
            this.grippedNewWidth = Float.parseFloat(childElementValue4);
        }
        CustomObjectUtilities.setFieldInfoFromXML((CustomizableEntity)this, node, defaultMutableTreeNode, fileReader);
    }
    
    public float getXDimensionForReport() {
        return this.getXDimensionFromData();
    }
    
    public float getYDimensionForReport() {
        return this.getYDimensionFromData();
    }
    
    public void applyChangesFromEditor(final String s, final PossibleValue possibleValue, final Collection<PossibleValue> collection, final Collection<String> collection2, final String s2) {
        try {
            if ("ICD_Scalable_Worksurface_Depth".equals(s)) {
                final float float1 = Float.parseFloat(possibleValue.getValue());
                if (float1 < this.getMinYDimension() || float1 > this.getMaxYDimension()) {
                    return;
                }
                this.grippedNewDepth = float1;
            }
            if ("ICD_Scalable_Worksurface_Width".equals(s)) {
                final float float2 = Float.parseFloat(possibleValue.getValue());
                if (float2 < this.getMinXDimension() || float2 > this.getMaxXDimension()) {
                    return;
                }
                this.grippedNewWidth = float2;
            }
        }
        catch (Exception ex) {
            System.out.println("Automatically Generated Exception Log(ICDBasicWorksurface.java,897)[" + ex.getClass() + "]: " + ex.getMessage());
            return;
        }
        super.applyChangesFromEditor(s, possibleValue, (Collection)collection, (Collection)collection2, s2);
    }
    
    public List<TypeableEntity> getFinishRoots() {
        final LinkedList<ICDBasicWorksurface> list = new LinkedList<ICDBasicWorksurface>();
        final TypeableEntity typeableEntity = (TypeableEntity)this.getParent((Class)TypeableEntity.class);
        if (typeableEntity != null) {
            list.addAll((Collection<?>)typeableEntity.getFinishRoots());
        }
        list.add(this);
        return (List<TypeableEntity>)list;
    }
    
    protected boolean shouldUseNewFinishSystem() {
        return true;
    }
    
    protected void selectGrips(final boolean b) {
        if (this.rightWidthGrip != null && this.bottomDepthGrip != null) {
            this.rightWidthGrip.setSelected(b);
            this.bottomDepthGrip.setSelected(b);
        }
    }
    
    protected void deselectGrips() {
        if (this.rightWidthGrip != null && this.bottomDepthGrip != null) {
            this.rightWidthGrip.setSelected(false);
            this.bottomDepthGrip.setSelected(false);
        }
    }
    
    protected void destroyGrips() {
        if (this.rightWidthGrip != null && this.bottomDepthGrip != null) {
            this.rightWidthGrip.destroy();
            this.bottomDepthGrip.destroy();
        }
    }
    
    protected void drawGrips(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        this.drawRightWidthGrip(n, ice2DContainer, solutionSetting);
        this.drawBottomDepthGrip(n, ice2DContainer, solutionSetting);
    }
    
    public BoundingCube getCubeForTriggers() {
        final float thickness = this.getThickness();
        final Point3f namedPointLocal = this.getNamedPointLocal("Top_Right_3D");
        final Point3f namedPointLocal2 = this.getNamedPointLocal("Bottom_Left_3D");
        namedPointLocal2.z -= thickness;
        final BoundingCube boundingCube = new BoundingCube(namedPointLocal2, namedPointLocal);
        boundingCube.setTransform(this.getEntWorldSpaceMatrix());
        return boundingCube;
    }
    
    protected Collection<TriggerInterface> getHACKTriggers() {
        final BoundingCube cubeForTriggers = this.getCubeForTriggers();
        if (cubeForTriggers != null) {
            Collection<TriggerInterface> hackTriggers = (Collection<TriggerInterface>)super.getHACKTriggers();
            if (hackTriggers == null) {
                hackTriggers = new Vector<TriggerInterface>();
            }
            hackTriggers.add((TriggerInterface)new TriggerCube("Tab_Location", (Float)null, cubeForTriggers, (TriggerUser)this));
            return hackTriggers;
        }
        return (Collection<TriggerInterface>)super.getHACKTriggers();
    }
    
    public boolean isPartAvaliable() {
        return this.currentCatalogOption != null && this.currentCatalogOption.isAvailable() && this.currentCatalogOption.isPartAvailable();
    }
    
    public float getLeftDepth() {
        return this.getAttributeValueAsFloat("ICD_Parametric_Depth1");
    }
    
    public float getRightDepth() {
        return this.getAttributeValueAsFloat("ICD_Parametric_Depth2");
    }
    
    public float getChaseDepthForWorksurfaceSupports() {
        final GeneralSnapSet generalSnapSet = this.getGeneralSnapSet();
        if (generalSnapSet != null) {
            final BoundingCube worldBoundingCube = this.getWorldBoundingCube();
            for (final ICDPanel icdPanel : generalSnapSet.getChildrenByClass((Class)ICDPanel.class, true)) {
                if (worldBoundingCube.intersect(icdPanel.getWorldBoundingCube())) {
                    final Point3f convertPointToOtherLocalSpace = MathUtilities.convertPointToOtherLocalSpace((EntityObject)icdPanel, this.getBasePointWorldSpace());
                    if (!icdPanel.hasChaseOnPointSide(convertPointToOtherLocalSpace)) {
                        continue;
                    }
                    if (icdPanel.isPointOnSideA(convertPointToOtherLocalSpace)) {
                        return icdPanel.getChaseSideAOffset();
                    }
                    return icdPanel.getChaseSideBOffset();
                }
            }
        }
        return 0.0f;
    }
    
    public float getLeftChaseDepthForWorksurfaceSupport() {
        return this.chaseDepth;
    }
    
    public float getRightChaseDepthForWorksurfaceSupport() {
        return this.chaseDepth;
    }
    
    public void populateCompareNode(final Class clazz, final CompareNode compareNode) {
        super.populateCompareNode(clazz, compareNode);
        if (ManufacturingReportable.class.isAssignableFrom(clazz)) {
            this.populateCompareNodeForICD(clazz, compareNode);
        }
    }
    
    public void populateCompareNodeForICD(final Class clazz, final CompareNode compareNode) {
        compareNode.addCompareValue("usertag", (Object)this.getUserTagNameAttribute("TagName1"));
        final Vector<String> gatherFinishes = this.gatherFinishes();
        for (int i = 0; i < gatherFinishes.size(); ++i) {
            compareNode.addCompareValue("finish" + i, (Object)gatherFinishes.get(i));
        }
        compareNode.addCompareValue("cutoutdipspacecompare", (Object)this.createCutoutAndDipSpaceCompareNodes());
    }
    
    protected void populateCompareNodeWithExtraValues(final Class clazz, final CompareNode compareNode) {
        compareNode.addCompareValue("description", (Object)this.getDescription());
        compareNode.addCompareValue("sku", (Object)this.getSKU());
        compareNode.addCompareValue("price", (Object)this.getUnitPrice());
        compareNode.addCompareValue("subType", (Object)this.getSubType());
        CustomObjectUtilities.populateCompareNode((CustomizableEntity)this, compareNode);
        super.populateCompareNodeWithExtraValues(clazz, compareNode);
    }
    
    private EntitySpaceCompareNode createCutoutAndDipSpaceCompareNodes() {
        final LinkedList list = new LinkedList();
        list.addAll(this.getCutOutEntitySpaceCompare());
        list.addAll(this.getDipEntitySpaceCompare());
        return new EntitySpaceCompareNode((Collection)list, this.getEntWorldSpaceMatrix(), (Matrix4f)null);
    }
    
    private Collection<? extends EntitySpaceCompareNodeWrapper> getDipEntitySpaceCompare() {
        final List childrenByClass = this.getChildrenByClass((Class)ICDWireDip.class, false, true);
        if (childrenByClass != null) {
            final LinkedList<EntitySpaceCompareNodeWrapper> list = new LinkedList<EntitySpaceCompareNodeWrapper>();
            for (final ICDWireDip icdWireDip : childrenByClass) {
                final ArrayList<Point3f> list2 = new ArrayList<Point3f>();
                final Point3f point3f = new Point3f(icdWireDip.getAttributeValueAsFloat("ICD_WireDip_Distance"), 0.0f, 0.0f);
                if (point3f != null) {
                    list2.add(point3f);
                }
                list.add(new EntitySpaceCompareNodeWrapper((TransformableEntity)icdWireDip, (Collection)list2));
            }
            return list;
        }
        return null;
    }
    
    private Collection<? extends EntitySpaceCompareNodeWrapper> getCutOutEntitySpaceCompare() {
        final List childrenByClass = this.getChildrenByClass((Class)ICDParametricCutout.class, false, true);
        if (childrenByClass != null) {
            final LinkedList<EntitySpaceCompareNodeWrapper> list = new LinkedList<EntitySpaceCompareNodeWrapper>();
            for (final ICDParametricCutout icdParametricCutout : childrenByClass) {
                final ArrayList<Point3f> list2 = new ArrayList<Point3f>();
                final Point3f point3f = new Point3f(icdParametricCutout.getAttributeValueAsFloat("ICD_Parametric_Cutout_Distance_X"), icdParametricCutout.getAttributeValueAsFloat("ICD_Parametric_Cutout_Distance_Y"), 0.0f);
                if (point3f != null) {
                    list2.add(point3f);
                }
                list.add(new EntitySpaceCompareNodeWrapper((TransformableEntity)icdParametricCutout, (Collection)list2));
            }
            return list;
        }
        return null;
    }
    
    public void getManufacturingInfo(final TreeMap<String, String> treeMap) {
        super.getManufacturingInfo((TreeMap)treeMap);
        if (this.containsAttributeKey("Product_Type")) {
            treeMap.put("Type", this.getAttributeValueAsString("Product_Type"));
        }
        if (this.containsAttributeKey("SubType")) {
            treeMap.put("SubType", this.getSubType());
        }
        treeMap.put("Description", this.getDescription());
        treeMap.put("Description", this.getDescription());
        treeMap.put("Depth", this.getYDimension() + "");
        treeMap.put("Width", this.getWidth() + "");
        treeMap.put("Height", this.getHeight() + "");
        final Vector<String> gatherFinishes = this.gatherFinishes();
        for (int i = 0; i < gatherFinishes.size(); ++i) {
            treeMap.put("Option0" + (i + 1), gatherFinishes.get(i));
        }
        treeMap.put("UserTag", this.getUserTagNameAttribute("TagName1"));
    }
    
    private Vector<String> gatherFinishes() {
        final Vector<String> vector = new Vector<String>();
        final List childrenByClass = this.getChildrenByClass((Class)BasicMaterialEntity.class, false);
        if (childrenByClass != null) {
            for (int i = 0; i < childrenByClass.size(); ++i) {
                final BasicMaterialEntity basicMaterialEntity = childrenByClass.get(i);
                String e = "";
                if (basicMaterialEntity.getChildCount() > 0) {
                    if (basicMaterialEntity.getChildAt(0) instanceof BasicMaterialEntity) {
                        final BasicMaterialEntity basicMaterialEntity2 = (BasicMaterialEntity)basicMaterialEntity.getChildAt(0);
                        if (basicMaterialEntity2.getAttributeValueAsString("Material_ID") != null) {
                            e = basicMaterialEntity2.getAttributeValueAsString("Material_ID");
                        }
                    }
                }
                else if (basicMaterialEntity.getAttributeValueAsString("Material_ID") != null) {
                    e = basicMaterialEntity.getAttributeValueAsString("Material_ID");
                }
                vector.add(e);
            }
        }
        return vector;
    }
    
    public void parentSnapSetDestroyed() {
        final Solution solution = this.getSolution();
        this.removeFromParent();
        solution.addToTree((EntityObject)this);
    }
    
    public boolean isCustomizable() {
        return true;
    }
    
    public CustomProperties getCustomProperties() {
        return this.customContainer.getProperties();
    }
    
    public void setCustomProperties(final CustomProperties customProperties, final boolean b) {
        this.customContainer.setProperties(customProperties, b);
    }
    
    public boolean isCustomized() {
        return this.customContainer.isInitalized();
    }
    
    public void makeCustomizable(final boolean b) {
        if (b) {
            this.customContainer.initialize((TransformableEntity)this);
        }
        else {
            this.customContainer.destroy();
        }
    }
    
    public Matrix4f getUser3DTransform() {
        if (this.isCustomized()) {
            return this.getCustomProperties().get3DTransform();
        }
        return null;
    }
    
    public Group getCustom3DModel() {
        Group get3DAsset = null;
        if (this.isCustomized()) {
            get3DAsset = this.customContainer.get3DAsset();
            if (get3DAsset != null) {
                Matrix4f user3DTransform = this.getUser3DTransform();
                if (user3DTransform == null) {
                    user3DTransform = new Matrix4f();
                    user3DTransform.setIdentity();
                }
                AssetCache.calculate3DBoundingCube((Paintable3D)this, get3DAsset, user3DTransform, false);
            }
        }
        return get3DAsset;
    }
    
    public Group getOriginal3DModel() {
        return super.get3DModel();
    }
    
    public Group get3DModel() {
        Group group = this.getCustom3DModel();
        if (group == null) {
            group = super.get3DModel();
        }
        return group;
    }
    
    public Vector<Shape3D> getMappableShapes(Vector<Shape3D> mappableShapes, final Group group) {
        if (!this.customContainer.addCustomShapes(mappableShapes, group)) {
            mappableShapes = super.getMappableShapes(mappableShapes, group);
        }
        return (Vector<Shape3D>)mappableShapes;
    }
    
    public String getMappableShapeName(final Shape3D shape3D) {
        final String customMapping = this.customContainer.getCustomMapping(shape3D);
        return (customMapping == null) ? super.getMappableShapeName(shape3D) : customMapping;
    }
    
    public void destroyCad() {
        super.destroyCad();
        this.customContainer.destroyCAD();
    }
    
    public void destroy2DAsset() {
        this.customContainer.destory2D();
        super.destroy2DAsset();
    }
    
    public void cutFromTree2D() {
        super.cutFromTree2D();
    }
    
    public void setPseudoTranslateVectorOn2DNodes(final Vector3f pseudoTranslateVectorOn2DNodes) {
        super.setPseudoTranslateVectorOn2DNodes(pseudoTranslateVectorOn2DNodes);
        if (this.customContainer.get2DNode() != null) {
            this.customContainer.get2DNode().pseudoTranslate(pseudoTranslateVectorOn2DNodes);
        }
    }
    
    public void setPseudoRotateVectorOn2DNodes(final Vector3f pseudoRotateVectorOn2DNodes) {
        super.setPseudoRotateVectorOn2DNodes(pseudoRotateVectorOn2DNodes);
        if (this.customContainer.get2DNode() != null) {
            this.customContainer.get2DNode().pseudoRotate(pseudoRotateVectorOn2DNodes);
        }
    }
    
    public CatalogOptionObject findCatalogPart() {
        return this.currentCatalogOption;
    }
    
    protected boolean getCustomIfAvailable() {
        return this.getCustomIfAvailable;
    }
    
    public float getUnitPrice() {
        float n = super.getUnitPrice();
        if (this.isCustomized()) {
            final String price = this.getCustomProperties().getPrice();
            if (price != null) {
                n = Float.parseFloat(price);
            }
        }
        return n;
    }
    
    public float getOriginalUnitPrice() {
        return super.getUnitPrice();
    }
    
    public float getOwnAmount(final int n, final boolean b) {
        if (this.isCustomized()) {
            final String price = this.getCustomProperties().getPrice();
            if (price != null) {
                return Float.parseFloat(price);
            }
        }
        return super.getOwnAmount(n, b);
    }
    
    public String getPartNumber() {
        String s = null;
        if (!this.isFixed() && this.currentCatalogOption != null) {
            final String attributeValueAsString = this.getAttributeValueAsString("PN");
            if (attributeValueAsString != null) {
                s = attributeValueAsString;
            }
        }
        return (s != null) ? s : super.getSKU();
    }
    
    public String getSKU() {
        String s = null;
        if (this.isCustomized()) {
            s = this.getCustomProperties().getProductNumber();
        }
        if (s == null) {
            s = this.getPartNumber();
        }
        return s;
    }
    
    public String getSubType() {
        String subType = null;
        final CustomProperties customProperties = this.getCustomProperties();
        if (customProperties != null) {
            final String productName = customProperties.getProductName();
            if (productName != null) {
                subType = productName;
            }
        }
        if (subType == null) {
            subType = super.getSubType();
        }
        return subType;
    }
    
    public boolean isQuoteable(final String s) {
        return this.customContainer.isQuoteable(s);
    }
    
    public String getXMLImage(final boolean b) {
        String s = null;
        if (this.isCustomized()) {
            s = this.customContainer.getCustomImage(false);
        }
        if (s == null) {
            s = super.getXMLImage(b);
        }
        return s;
    }
    
    public String getLargeQuoteImageFilename() {
        String s = null;
        if (this.isCustomized()) {
            s = this.customContainer.getCustomImage(true);
        }
        if (s == null) {
            s = super.getLargeQuoteImageFilename();
        }
        return s;
    }
    
    public Ice2DDXFLWNode getCustomIce2DNode() {
        return this.customContainer.get2DNode();
    }
    
    public String getReportMainHeading() {
        final String customMainHeading = this.getCustomMainHeading();
        return (customMainHeading != null) ? customMainHeading : super.getReportMainHeading();
    }
    
    public String getReportSubHeading() {
        final String customSubHeading = this.getCustomSubHeading();
        return (customSubHeading != null) ? customSubHeading : super.getReportSubHeading();
    }
    
    public String getCustomMainHeading() {
        String quoteHeading = null;
        if (this.isCustomized()) {
            quoteHeading = this.getCustomProperties().getQuoteHeading();
        }
        return quoteHeading;
    }
    
    public String getCustomSubHeading() {
        String quoteSubHeading = null;
        if (this.isCustomized()) {
            quoteSubHeading = this.getCustomProperties().getQuoteSubHeading();
        }
        return quoteSubHeading;
    }
    
    public String getCustomProductForQuote() {
        String catalogName = null;
        if (this.isCustomized()) {
            catalogName = this.getCustomProperties().getCatalogName();
        }
        return catalogName;
    }
    
    public boolean drawDXF(final int n, final Ice2DContainer ice2DContainer) {
        return this.customContainer.draw2D((TransformableEntity)this, n, ice2DContainer);
    }
    
    public boolean drawDXFCad(final ICadTreeNode cadTreeNode, final int n) {
        return this.customContainer.drawCad((TransformableEntity)this, n, cadTreeNode);
    }
    
    public boolean drawCustomCadDotNet(final int n, final IceCadNodeContainer iceCadNodeContainer, final IceCadIceApp iceCadIceApp) {
        return this.customContainer.drawCadDotNet((TransformableEntity)this, n, iceCadNodeContainer, iceCadIceApp);
    }
    
    public boolean drawCustomCadForProxyEntityDotNet(final int n, final IceCadNodeContainer iceCadNodeContainer, final IceCadCompositeBlock iceCadCompositeBlock, final Point3f point3f, final float n2, final Point3f point3f2) {
        return this.customContainer.drawCadForProxyEntityDotNet((TransformableEntity)this, n, iceCadNodeContainer, iceCadCompositeBlock, point3f, n2, point3f2);
    }
    
    public void draw3DMarker(final int n) {
        if (this.iceBranch != null) {
            this.customContainer.draw3DMarker((TransformableEntity)this, this.iceBranch, n);
        }
    }
    
    public void draw2DMarker(final int n, final Ice2DContainer ice2DContainer) {
        this.customContainer.drawPaintMarker((TransformableEntity)this, n, ice2DContainer);
    }
    
    public void toggleEntityMarkers(final boolean paintMarkers) {
        this.customContainer.setPaintMarkers(paintMarkers);
    }
    
    public boolean hasCustomAsset() {
        return this.customContainer.hasCustom2DAsset();
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
        ICDBasicWorksurface.skuGenerator = null;
        ICDBasicWorksurface.logger = Logger.getLogger((Class)ICDBasicWorksurface.class);
    }
    
    private class WidthGripListener implements GripListener
    {
        public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
            ICDBasicWorksurface.this.gripsMoved = true;
            ICDBasicWorksurface.this.grippedNewWidth = Float.parseFloat(basicAttributeGrip.getDisplayValue());
            ICDBasicWorksurface.this.handleScaleChanges(ICDBasicWorksurface.this.oldWidth, ICDBasicWorksurface.this.grippedNewWidth, ICDBasicWorksurface.this.oldDepth, ICDBasicWorksurface.this.grippedNewDepth);
            return true;
        }
    }
    
    private class DepthGripListener implements GripListener
    {
        public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
            ICDBasicWorksurface.this.gripsMoved = true;
            ICDBasicWorksurface.this.grippedNewDepth = Float.parseFloat(basicAttributeGrip.getDisplayValue());
            ICDBasicWorksurface.this.handleScaleChanges(ICDBasicWorksurface.this.oldWidth, ICDBasicWorksurface.this.grippedNewWidth, ICDBasicWorksurface.this.oldDepth, ICDBasicWorksurface.this.grippedNewDepth);
            return true;
        }
    }
}
