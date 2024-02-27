package net.iceedge.catalogs.icd.worksurfaces.parametric;

import net.dirtt.utilities.EntitySpaceCompareNodeWrapper;
import java.util.LinkedList;
import net.dirtt.utilities.EntitySpaceCompareNode;
import net.dirtt.icelib.report.compare.CompareNode;
import net.iceedge.catalogs.icd.ICDUtilities;
import java.text.DecimalFormat;
import net.dirtt.icecad.cadtree.ICadBlockRefNode;
import net.dirtt.icecad.cadtree.ICadRootNode;
import net.dirtt.icecad.cadtree.ICadLayerTreeNode;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import javax.media.j3d.Transform3D;
import java.awt.Graphics2D;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.NotchEdge;
import net.dirtt.icelib.main.BoundingCube;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.Notch;
import net.dirtt.icelib.main.attributes.FloatAttribute;
import net.dirtt.icelib.main.snapping.simple.SimpleSnapTargetCollection;
import net.dirtt.icelib.main.snapping.simple.SimpleSnapper;
import net.dirtt.icelib.ui.attribute.explorer.ui.entity.PossibleValue;
import net.iceedge.icecore.basemodule.util.Mirror;
import javax.vecmath.Point3d;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.RayParameter;
import net.dirtt.utilities.IvQuat;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.CircleParameter;
import net.dirtt.icebox.canvas2d.Ice2DNode;
import java.awt.Color;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.dirtt.utilities.MathUtilities;
import net.dirtt.icelib.main.SolutionSetting;
import net.iceedge.icecore.icecad.ice.tree.IceCadLayer;
import net.dirtt.icelib.main.EntityObject;
import net.iceedge.icecore.icecad.ice.tree.listener.CopyListener;
import net.iceedge.icecore.icecad.ice.tree.IceCadEntity;
import net.iceedge.icecore.basemodule.interfaces.lightweight.CADPaintable;
import net.iceedge.icecore.icecad.ice.tree.listener.CadSelectionListener;
import net.iceedge.icecore.icecad.ice.tree.listener.IceCadListener;
import net.dirtt.icelib.main.TransformableEntity;
import net.iceedge.icecore.icecad.ice.tree.listener.BlockRefCadListener;
import net.iceedge.icecore.icecad.ice.tree.IceCadBlock;
import net.iceedge.icecore.icecad.ice.tree.IceCadSymbolTableRecord;
import net.iceedge.icecore.icecad.ice.tree.CadLayer;
import net.iceedge.icecore.icecad.ice.IceCadIceApp;
import net.iceedge.icecore.icecad.ice.tree.IceCadNodeContainer;
import net.iceedge.icecore.basemodule.interfaces.lightweight.Paintable;
import net.dirtt.icebox.iceoutput.core.IceOutputTextNode;
import net.dirtt.icecad.cadtree.ICadTreeNode;
import java.util.List;
import java.awt.geom.Line2D;
import java.awt.geom.GeneralPath;
import java.awt.Shape;
import java.util.Iterator;
import net.iceedge.icecore.basemodule.interfaces.lightweight.Paintable3D;
import net.iceedge.icebox.utilities.AssetCache;
import net.iceedge.catalogs.icd.worksurfaces.ICDParametricDeckOrShelf;
import org.xith3d.scenegraph.Geometry;
import org.xith3d.scenegraph.PolygonAttributes;
import org.xith3d.scenegraph.Appearance;
import net.dirtt.icebox.ice3dviewer.Ice3dAppearance;
import org.xith3d.scenegraph.TextureUnit;
import org.openmali.vecmath2.TexCoordf;
import javax.vecmath.Tuple3f;
import net.iceedge.vecmath.MathLibraryConversions;
import org.xith3d.scenegraph.TriangleArray;
import javax.vecmath.Matrix4f;
import net.dirtt.utilities.Utility3D;
import java.util.Collection;
import org.xith3d.scenegraph.Shape3D;
import java.io.IOException;
import org.xith3d.scenegraph.Node;
import net.dirtt.icebox.ice3dviewer.TransparencyPolygon;
import net.iceedge.icebox.utilities.Path;
import net.iceedge.icebox.utilities.ImagePool;
import net.iceedge.xith.TransformGroup;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.icecad.ice.tree.IceCadBlockRefNode;
import net.iceedge.icecore.icecad.ice.tree.IceCadMTextNode;
import net.iceedge.icecore.icecad.ice.tree.IceCadCompositeBlock;
import net.dirtt.icecad.cadtree.ICadTextNode;
import net.dirtt.icecad.cadtree.ICadBlockNode;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.brep.BRepObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.Parameter2D;
import javax.vecmath.Vector3f;
import java.util.HashMap;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.dirtt.icebox.canvas2d.Ice2DShapeNode;
import net.dirtt.icebox.canvas2d.Ice2DDelegateNode;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import org.xith3d.scenegraph.Group;
import javax.vecmath.Point3f;
import java.util.ArrayList;
import net.iceedge.icecore.basemodule.interfaces.NotchableWorksurface;
import net.dirtt.icebox.canvas2d.Ice2DDirectPaintable;
import net.iceedge.icecore.basemodule.baseclasses.material.BasicMaterialChangeListener;
import net.iceedge.icecore.basemodule.util.Mirrorable;
import net.dirtt.icebox.iceoutput.core.PlotPaintable;
import net.iceedge.catalogs.icd.worksurfaces.ICDBasicWorksurface;

public abstract class ICDParametricWorksurface extends ICDBasicWorksurface implements PlotPaintable, Mirrorable, BasicMaterialChangeListener, Ice2DDirectPaintable, NotchableWorksurface
{
    protected static final String BOTTOM_LEFT_SNAP_CORNER = "Bottom_Left_Snap_Corner";
    protected static final String BOTTOM_RIGHT_SNAP_CORNER = "Bottom_Right_Snap_Corner";
    protected static final String TOP_RIGHT_SNAP_CORNER = "Top_Right_Snap_Corner";
    protected static final String TOP_LEFT_SNAP_CORNER = "Top_Left_Snap_Corner";
    protected static final String TOP_LEFT_SNAP_CORNER_2 = "Top_Left_Snap_Corner_2";
    protected static final String TOP_RIGHT_SNAP_CORNER_2 = "Top_Right_Snap_Corner_2";
    public static final float ICD_PARAMETRIC_WORKSURFACE_THICKNESS = 1.25f;
    public static final float ICD_PARAMETRIC_WORKSURFACE_WIREDIP_INSET = 1.5f;
    public static final float ICD_PARAMETRIC_WORKSURFACE_WIREDIP_RADIUS = 3.63f;
    public static final float ICD_PARAMETRIC_WORKSURFACE_CORNER_WIREDIP_RADIUS = 2.88f;
    public static final float ICD_PARAMETRIC_WORKSURFACE_WIREDIP_SEGMENT_LENGTH;
    public static final float ICD_PARAMETRIC_WORKSURFACE_WIREDIP_CORNER_DISTANCE = 5.0f;
    public static final float ICD_PARAMETRIC_WORKSURFACE_WIREDIP_MIN_DISTANCE = 10.0f;
    public static final float ICD_PARAMETRIC_WORKSURFACE_SNAP_POINT_RANGE = 10.0f;
    public static final float ICD_PARAMETRIC_WORKSURFACE_CUTOUT_SNAP_POINT_RANGE = 5.0f;
    public static final float ICD_PARAMETRIC_WORKSURFACE_TOP_TEXTURE_PATCH_SIZE = 36.0f;
    public static final float ICD_PARAMETRIC_WORKSURFACE_EDGE_TEXTURE_PATCH_SIZE = 10.0f;
    public static final float ICD_PARAMETRIC_WORKSURFACE_FILLET_RADIUS = 1.125f;
    public static final float ICD_PARAMETRIC_WORKSURFACE_EDGE_OFFSET = -0.125f;
    public static final float ICD_PARAMETRIC_WORKSURFACE_RETURN_ARC_OFFSET = 3.8228f;
    public static final float ICD_PARAMETRIC_DeckOrShelf_THICKNESS = 0.75f;
    public static boolean DRAW2D_DEBUG;
    public static int DEBUG_POINT_RADIUS;
    protected ArrayList<Point3f> shape;
    protected boolean shapeChanged;
    private Group cachedModel;
    private float[] cachedShape;
    protected ArrayList<IceOutputNode> plotNodes;
    protected ArrayList<IceOutputNode> plotOutlineNodes;
    protected Ice2DDelegateNode delegate2DNode;
    protected Ice2DShapeNode shapeNode;
    protected ArrayList<Point3f> shapePoints;
    protected ArrayList<Ice2DDirectPaintable> directPaintables;
    protected ArrayList<LineParameter> sideLineParams;
    protected ArrayList<LineParameter> lineParams;
    protected HashMap<LineParameter, ArrayList<ICDWireDip>> wireDipMap;
    protected HashMap<ICDWireDip, LineParameter> wireDipParamLookupMap;
    protected ArrayList<Point3f> wireDipSnapPoints;
    protected HashMap<Point3f, Integer> snapPointIndexMap;
    protected ArrayList<Point3f> wireDipRefPoints;
    protected ArrayList<Point3f> cutoutSnapPoints;
    protected HashMap<Point3f, Integer> cutoutSnapPointIndexMap;
    protected Point3f cutoutRefPoint;
    protected Vector3f cutoutXDirection;
    protected Vector3f cutoutYDirection;
    protected Point3f widthAnchor;
    protected Point3f depthAnchor;
    protected Point3f width1Anchor;
    protected Point3f width2Anchor;
    protected Point3f depth1Anchor;
    protected Point3f depth2Anchor;
    protected Vector3f widthDirection;
    protected Vector3f depthDirection;
    protected Vector3f width1Direction;
    protected Vector3f width2Direction;
    protected Vector3f depth1Direction;
    protected Vector3f depth2Direction;
    protected ArrayList<Parameter2D> allParameters;
    protected ArrayList<BRepObject> boundary;
    protected ArrayList<BRepObject> cutoffBoundary;
    protected ICadBlockNode blockNode;
    private ICadTextNode textNode;
    private IceCadCompositeBlock blockNodeNet;
    private IceCadMTextNode textNodeNet;
    private IceCadBlockRefNode blockRefNodeNet;
    private IceCadMTextNode textNodeNetForProxyEntity;
    
    public ICDParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.shape = new ArrayList<Point3f>();
        this.shapeChanged = true;
        this.cachedModel = null;
        this.cachedShape = null;
        this.plotNodes = new ArrayList<IceOutputNode>();
        this.plotOutlineNodes = new ArrayList<IceOutputNode>();
        this.delegate2DNode = null;
        this.shapeNode = null;
        this.shapePoints = new ArrayList<Point3f>();
        this.directPaintables = new ArrayList<Ice2DDirectPaintable>();
        this.sideLineParams = new ArrayList<LineParameter>();
        this.lineParams = new ArrayList<LineParameter>();
        this.wireDipMap = new HashMap<LineParameter, ArrayList<ICDWireDip>>();
        this.wireDipParamLookupMap = new HashMap<ICDWireDip, LineParameter>();
        this.wireDipSnapPoints = new ArrayList<Point3f>();
        this.snapPointIndexMap = new HashMap<Point3f, Integer>();
        this.wireDipRefPoints = new ArrayList<Point3f>();
        this.cutoutSnapPoints = new ArrayList<Point3f>();
        this.cutoutSnapPointIndexMap = new HashMap<Point3f, Integer>();
        this.cutoutRefPoint = null;
        this.cutoutXDirection = null;
        this.cutoutYDirection = null;
        this.allParameters = new ArrayList<Parameter2D>();
        this.boundary = new ArrayList<BRepObject>();
        this.cutoffBoundary = new ArrayList<BRepObject>();
        this.blockNode = null;
        this.textNode = null;
        this.blockNodeNet = null;
        this.textNodeNet = null;
        this.blockRefNodeNet = null;
    }
    
    public void calculate() {
        this.calculateLocalSpace();
        this.calculateEntityParentSpaceMatrix();
        this.calculateDimensionsCenterPoint();
        this.calculateNamedData();
        this.calculateBoundingRect();
        this.calculateBoundingCube();
        this.calculateShape();
        this.calculateAllAssetTransforms();
    }
    
    protected abstract void calculateParameters();
    
    @Override
    public void calculateBoundingCube() {
        Point3f geometricCenterPointLocal = this.getGeometricCenterPointLocal();
        if (geometricCenterPointLocal == null) {
            geometricCenterPointLocal = new Point3f();
        }
        this.boundingCube.setLower(geometricCenterPointLocal.x - (this.getXDimension() + 0.5f) * 0.5f, geometricCenterPointLocal.y - (this.getYDimension() + 0.5f) * 0.5f, geometricCenterPointLocal.z - this.getZDimension() * 0.5f);
        this.boundingCube.setUpper(geometricCenterPointLocal.x + (this.getXDimension() + 0.5f) * 0.5f, geometricCenterPointLocal.y + (this.getYDimension() + 0.5f) * 0.5f, geometricCenterPointLocal.z + this.getZDimension() * 0.5f);
    }
    
    protected void addAutoShadow3D(final TransformGroup transformGroup) {
        if (!this.isCustomized()) {
            final Point3f basePointWorldSpace = this.getBasePointWorldSpace();
            final float xDimension = this.getXDimension();
            final Point3f point3f = new Point3f(-xDimension, this.getYDimension() - xDimension, -basePointWorldSpace.z);
            final Point3f point3f2 = new Point3f(xDimension, this.getYDimension() - xDimension, -basePointWorldSpace.z);
            final Point3f point3f3 = new Point3f(xDimension, this.getYDimension() + xDimension, -basePointWorldSpace.z);
            final Point3f point3f4 = new Point3f(-xDimension, this.getYDimension() + xDimension, -basePointWorldSpace.z);
            try {
                final TransparencyPolygon transparencyPolygon = new TransparencyPolygon(point3f4, point3f3, point3f2, point3f, ImagePool.getImagePool().getTexture(Path.THREE_D_IMAGES, "VladShadow4.tga", true), 0.5f);
                ((Shape3D)transparencyPolygon).setName("Shadow");
                transformGroup.addChild((Node)transparencyPolygon);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void cacheShape() {
        if (this.cachedShape == null || this.cachedShape.length != this.shape.size() * 3) {
            this.cachedShape = new float[this.shape.size() * 3];
        }
        for (int i = 0, index = 0; i < this.cachedShape.length; i += 3, ++index) {
            final Point3f point3f = this.shape.get(index);
            this.cachedShape[i] = point3f.x;
            this.cachedShape[i + 1] = point3f.y;
            this.cachedShape[i + 2] = point3f.z;
        }
    }
    
    private void determineIfModelHasChanged() {
        if (this.cachedShape != null && this.cachedShape.length == this.shape.size() * 3) {
            this.shapeChanged = false;
            for (int i = 0, index = 0; i < this.cachedShape.length; i += 3, ++index) {
                final Point3f point3f = this.shape.get(index);
                if (this.cachedShape[i] != point3f.x || this.cachedShape[i + 1] != point3f.y || this.cachedShape[i + 2] != point3f.z) {
                    this.shapeChanged = true;
                    break;
                }
            }
        }
        else {
            this.shapeChanged = true;
        }
    }
    
    @Override
    public Group getOriginal3DModel() {
        return this.build3DModel();
    }
    
    @Override
    public Group get3DModel() {
        if (this.getCustomIfAvailable()) {
            final Group custom3DModel = this.getCustom3DModel();
            if (custom3DModel != null) {
                return custom3DModel;
            }
        }
        return this.build3DModel();
    }
    
    public Group build3DModel() {
        this.determineIfModelHasChanged();
        Group cachedModel;
        if (this.shapeChanged || this.cachedModel == null) {
            this.cacheShape();
            cachedModel = (this.cachedModel = new Group());
            final Shape3D shape3D = new Shape3D();
            final ArrayList<ArrayList<Point3f>> list = new ArrayList<ArrayList<Point3f>>();
            final ArrayList list2 = new ArrayList();
            final ArrayList<ICDParametricCutout> cutouts = this.getCutouts();
            final int[] array = new int[cutouts.size() + 1];
            list2.addAll(this.shape);
            array[0] = this.shape.size();
            list.add(this.shape);
            int n = 1;
            for (final ICDParametricCutout icdParametricCutout : cutouts) {
                ArrayList<Point3f> shapePS = icdParametricCutout.getShapePS();
                if (this.shouldFlipCutouts3D()) {
                    final ArrayList<Point3f> list3 = new ArrayList<Point3f>();
                    for (int i = shapePS.size() - 1; i >= 0; --i) {
                        list3.add(shapePS.get(i));
                    }
                    shapePS = list3;
                }
                list.add(shapePS);
                list2.addAll(shapePS);
                array[n] = icdParametricCutout.getShape().size();
                ++n;
            }
            final Point3f[] triangulateShapeWithHoles = Utility3D.triangulateShapeWithHoles(Utility3D.toPoint3fArray(list2), array);
            final Point3f point3f = triangulateShapeWithHoles[0];
            final Point3f point3f2 = triangulateShapeWithHoles[1];
            final Point3f point3f3 = triangulateShapeWithHoles[2];
            final Vector3f vector3f = new Vector3f(point3f2.x - point3f.x, point3f2.y - point3f.y, 0.0f);
            final Vector3f vector3f2 = new Vector3f(point3f3.x - point3f.x, point3f3.y - point3f.y, 0.0f);
            vector3f.normalize();
            vector3f2.normalize();
            final Vector3f vector3f3 = new Vector3f();
            vector3f3.cross(vector3f, vector3f2);
            vector3f3.normalize();
            if (vector3f3.z < 0.0f) {
                Utility3D.flipTris(triangulateShapeWithHoles);
            }
            final Matrix4f matrix4f = new Matrix4f();
            matrix4f.setIdentity();
            matrix4f.setScale(36.0f);
            final TriangleArray geometry = new TriangleArray(triangulateShapeWithHoles.length);
            geometry.setCoordinates(0, (org.openmali.vecmath2.Tuple3f[])MathLibraryConversions.toXithArrayPoint3f((Tuple3f[])triangulateShapeWithHoles));
            geometry.setTextureCoordinates(0, 0, (TexCoordf[])Utility3D.getCubeMapping(matrix4f, triangulateShapeWithHoles));
            final TextureUnit[] textureUnits = { Ice3dAppearance.getNewOrExistingTextureUnit() };
            final Appearance appearance = new Appearance();
            appearance.setPolygonAttributes(Ice3dAppearance.getNewOrExistingPolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_BACK, 0.0f));
            appearance.setTextureUnits(textureUnits);
            shape3D.setGeometry((Geometry)geometry);
            shape3D.setAppearance(appearance);
            shape3D.setName("WKSLAM_UV3_WKSLAM");
            final Shape3D shape3D2 = new Shape3D();
            final ArrayList list4 = new ArrayList();
            for (final ArrayList<Point3f> list5 : list) {
                if (this instanceof ICDParametricDeckOrShelf) {
                    list4.addAll(Utility3D.extrudeShape((ArrayList)list5, -0.75f));
                }
                else {
                    list4.addAll(Utility3D.extrudeShape((ArrayList)list5, -1.25f));
                }
            }
            final Point3f[] point3fArray = Utility3D.toPoint3fArray(list4);
            if (this.shouldFlipEdge3D()) {
                Utility3D.flipTris(point3fArray);
            }
            final TriangleArray geometry2 = new TriangleArray(point3fArray.length);
            geometry2.setCoordinates(0, (org.openmali.vecmath2.Tuple3f[])MathLibraryConversions.toXithArrayPoint3f((Tuple3f[])point3fArray));
            matrix4f.setIdentity();
            matrix4f.setScale(10.0f);
            geometry2.setTextureCoordinates(0, 0, (TexCoordf[])Utility3D.getCubeMapping(matrix4f, point3fArray));
            final TextureUnit[] textureUnits2 = { Ice3dAppearance.getNewOrExistingTextureUnit() };
            final Appearance appearance2 = new Appearance();
            appearance2.setPolygonAttributes(Ice3dAppearance.getNewOrExistingPolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_BACK, 0.0f));
            appearance2.setTextureUnits(textureUnits2);
            shape3D2.setGeometry((Geometry)geometry2);
            shape3D2.setAppearance(appearance2);
            shape3D2.setName("T-EDGE_UV3_T-EDGE");
            cachedModel.addChild((Node)shape3D);
            cachedModel.addChild((Node)shape3D2);
            if (this.getCustomIfAvailable()) {
                Matrix4f user3DTransform = this.getUser3DTransform();
                if (user3DTransform == null) {
                    user3DTransform = new Matrix4f();
                    user3DTransform.setIdentity();
                }
                AssetCache.calculate3DBoundingCube((Paintable3D)this, cachedModel, user3DTransform, false);
            }
        }
        else {
            if (this.cachedModel.getParent() != null) {
                this.cachedModel.removeFromParentGroup();
            }
            cachedModel = this.cachedModel;
        }
        return Utility3D.shareClone((Node)cachedModel);
    }
    
    public Shape getShape2D() {
        final GeneralPath generalPath = new GeneralPath();
        if (this.shape.size() > 0) {
            for (int i = 0; i < this.shape.size() - 1; ++i) {
                final Point3f point3f = this.shape.get(i);
                final Point3f point3f2 = this.shape.get(i + 1);
                generalPath.append(new Line2D.Float(point3f.x, point3f.y, point3f2.x, point3f2.y), true);
            }
            final Point3f point3f3 = this.shape.get(this.shape.size() - 1);
            final Point3f point3f4 = this.shape.get(0);
            generalPath.append(new Line2D.Float(point3f3.x, point3f3.y, point3f4.x, point3f4.y), true);
        }
        return generalPath;
    }
    
    public String getAttributeExplorerCategory() {
        return "Parametric Worksurface";
    }
    
    public List<IceOutputNode> getPlotOutputNodes() {
        final ArrayList<IceOutputTextNode> list = (ArrayList<IceOutputTextNode>)new ArrayList<Object>();
        if (!this.hasCustomAsset()) {
            final ICadTextNode cadOutputTextNode = this.getCadOutputTextNode(null);
            if (cadOutputTextNode != null) {
                final Matrix4f matrix4f = new Matrix4f();
                matrix4f.setIdentity();
                final Point3f cadOutputInsertionPoint = this.getCadOutputInsertionPoint(cadOutputTextNode.getText());
                final Matrix4f matrix4f2 = new Matrix4f();
                matrix4f2.setIdentity();
                matrix4f2.setTranslation(new Vector3f(cadOutputInsertionPoint.x, cadOutputInsertionPoint.y, cadOutputInsertionPoint.z));
                matrix4f.mul(matrix4f2);
                final Matrix4f matrix4f3 = new Matrix4f();
                matrix4f3.setIdentity();
                matrix4f3.rotZ(this.getRotationWorldSpace());
                matrix4f.mul(matrix4f3);
                list.add(new IceOutputTextNode((Paintable)null, cadOutputTextNode.getText(), 2.0f, 1.0f, 0.0f, new Point3f(), matrix4f));
            }
        }
        list.addAll((Collection<?>)this.plotNodes);
        return (List<IceOutputNode>)list;
    }
    
    @Override
    public void drawIceCadDotNet(final int notDirty, final IceCadNodeContainer iceCadNodeContainer, final IceCadIceApp iceCadIceApp) {
        if (this.isDirty(notDirty)) {
            this.setNotDirty(notDirty);
            if (!this.drawCustomCadDotNet(notDirty, iceCadNodeContainer, iceCadIceApp)) {
                this.textNodeNet = this.getCadOutputTextNodeDotNet(iceCadNodeContainer, this.textNodeNet);
                final IceCadLayer layer = CadLayer.LAYER_2.getLayer();
                this.updateIceCadBlock(iceCadNodeContainer);
                if (this.blockRefNodeNet == null) {
                    (this.blockRefNodeNet = new IceCadBlockRefNode(iceCadNodeContainer, (IceCadSymbolTableRecord)layer, (IceCadBlock)this.blockNodeNet, this.getBasePoint3f(), this.getRotation(), 1.0f, 1.0f, 1.0f)).setCadListener((IceCadListener)new BlockRefCadListener(iceCadIceApp.getCommandWrapperContainer(), (TransformableEntity)this, this.blockRefNodeNet));
                    this.blockRefNodeNet.setSelectListener(new CadSelectionListener(iceCadIceApp.getSelectionContainer(), (CADPaintable)this, (IceCadEntity)this.blockRefNodeNet));
                    this.blockRefNodeNet.setCopyListener(new CopyListener((IceCadEntity)this.blockRefNodeNet, (EntityObject)this, iceCadIceApp));
                }
                else {
                    this.blockRefNodeNet.setPosition(this.getBasePoint3f());
                    this.blockRefNodeNet.setRotation(this.getRotation());
                }
            }
        }
        super.drawIceCadDotNet(notDirty, iceCadNodeContainer, iceCadIceApp);
    }
    
    private void updateIceCadBlock(final IceCadNodeContainer iceCadNodeContainer) {
        if (this.blockNodeNet == null) {
            this.blockNodeNet = new IceCadCompositeBlock(iceCadNodeContainer);
        }
        else {
            this.blockNodeNet.clearBlock();
        }
        final Iterator<BRepObject> iterator = this.cutoffBoundary.iterator();
        while (iterator.hasNext()) {
            iterator.next().drawIceCadDotNet(iceCadNodeContainer, (IceCadSymbolTableRecord)this.blockNodeNet);
        }
        final Iterator<BRepObject> iterator2 = this.boundary.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().drawIceCadDotNet(iceCadNodeContainer, (IceCadSymbolTableRecord)this.blockNodeNet);
        }
    }
    
    public void drawIceCadForProxyEntityDotNet(final IceCadNodeContainer iceCadNodeContainer, final TransformableEntity transformableEntity, final IceCadCompositeBlock iceCadCompositeBlock, final int n, final SolutionSetting solutionSetting) {
        if (this.drawCAD()) {
            final float n2 = this.getCadWorldRotation() - ((transformableEntity instanceof TransformableEntity) ? transformableEntity.getRotationWorldSpace() : 0.0f) + this.blockRotation.y;
            final Point3f convertPointToWorldSpace = this.convertPointToWorldSpace(this.blockInsertionPoint);
            Matrix4f entWorldSpaceMatrix;
            if (transformableEntity instanceof TransformableEntity) {
                entWorldSpaceMatrix = ((EntityObject)transformableEntity).getEntWorldSpaceMatrix();
            }
            else {
                entWorldSpaceMatrix = new Matrix4f();
                entWorldSpaceMatrix.setIdentity();
                final Point3f basePointWorldSpace = ((EntityObject)transformableEntity).getBasePointWorldSpace();
                entWorldSpaceMatrix.setTranslation(new Vector3f(basePointWorldSpace.x, basePointWorldSpace.y, basePointWorldSpace.z));
            }
            final Point3f convertSpaces = MathUtilities.convertSpaces(convertPointToWorldSpace, entWorldSpaceMatrix);
            if (this.isCadPaintable() && !this.drawCustomCadForProxyEntityDotNet(n, iceCadNodeContainer, iceCadCompositeBlock, convertSpaces, n2, this.blockScale)) {
                this.updateIceCadBlock(iceCadNodeContainer);
                new IceCadBlockRefNode(iceCadNodeContainer, (IceCadSymbolTableRecord)iceCadCompositeBlock, (IceCadBlock)this.blockNodeNet, convertSpaces, n2, this.blockScale.x, this.blockScale.y, this.blockScale.z);
            }
            this.textNodeNetForProxyEntity = this.getCadOutputTextNodeForProxyEntityDotNet(iceCadNodeContainer, this.textNodeNetForProxyEntity, transformableEntity, iceCadCompositeBlock);
        }
        super.drawIceCadForProxyEntityDotNet(iceCadNodeContainer, transformableEntity, iceCadCompositeBlock, n, solutionSetting);
    }
    
    public void draw3D(final Group group, final int n) {
        super.draw3D(group, n);
        this.draw3DMarker(n);
    }
    
    @Override
    public void draw2D(final int notDirty, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        this.drawChildren2D(notDirty, ice2DContainer, solutionSetting);
        if (this.isDirty(notDirty)) {
            this.draw2DMarker(notDirty, ice2DContainer);
            if (!this.drawDXF(notDirty, ice2DContainer) || this.displayOrigional()) {
                this.getShape2D();
                if (this.shapeNode == null) {
                    (this.shapeNode = new Ice2DShapeNode(this.getLayerName(), (TransformableEntity)this, this.getEntWorldSpaceMatrix(), this.getShape2D())).setColor(Color.GRAY);
                    this.shapeNode.setStroke(1.0f);
                    this.shapeNode.setFillOpacity(0.75f);
                    this.shapeNode.setSelectedStroke(2.0f);
                    this.shapeNode.setPseudoMoveable(true);
                }
                else {
                    this.shapeNode.setShape(this.getShape2D());
                    this.shapeNode.setMatrix(this.getEntWorldSpaceMatrix());
                    this.shapeNode.setPseudoMoveable(true);
                }
                ice2DContainer.add((Ice2DNode)this.shapeNode);
            }
            else {
                if (this.shapeNode != null) {
                    this.shapeNode.removeFromParent();
                }
                this.shapeNode = null;
            }
            this.drawBoundingCubeNode(ice2DContainer);
            this.drawNamedPointsDebug(ice2DContainer);
            this.drawWarning(ice2DContainer);
            this.setNotDirty(notDirty);
            this.drawGrips(notDirty, ice2DContainer, solutionSetting);
            this.draw2DsnapNode(ice2DContainer);
        }
    }
    
    private boolean displayOrigional() {
        return !this.getSolution().isMainSolution && (this.getSolution().isTypicalSolution() || !this.getSolution().isTempTypicalSolution());
    }
    
    public Shape getWorldBoundingShape() {
        return this.isCustomized() ? this.getShape2D() : super.getWorldBoundingShape();
    }
    
    public void destroy2D() {
        super.destroy2D();
        if (this.delegate2DNode != null) {
            this.delegate2DNode.removeFromParent();
        }
        if (this.shapeNode != null) {
            this.shapeNode.removeFromParent();
        }
    }
    
    @Override
    public void cutFromTree2D() {
        super.cutFromTree2D();
        if (this.delegate2DNode != null) {
            this.delegate2DNode.removeFromParent();
        }
        if (this.shapeNode != null) {
            this.shapeNode.removeFromParent();
        }
    }
    
    public Ice2DNode getIce2DNode() {
        return (Ice2DNode)this.shapeNode;
    }
    
    @Override
    protected void calculateDimensionsCenterPoint() {
        this.calculateParameters();
        this.calculateDimensionsOfWorksurface();
        this.setGeometricCenterPointLocal(new Point3f(0.0f, 0.0f, 0.0f));
        if (this.shape.size() > 0) {
            final Point3f[] calcAxisAlignedBoundingRectXY = MathUtilities.calcAxisAlignedBoundingRectXY((Collection)this.shape);
            final float n = calcAxisAlignedBoundingRectXY[1].x - calcAxisAlignedBoundingRectXY[0].x;
            final float n2 = calcAxisAlignedBoundingRectXY[1].y - calcAxisAlignedBoundingRectXY[0].y;
            float n3 = 1.25f;
            if (this instanceof ICDParametricDeckOrShelf) {
                n3 = 0.75f;
            }
            this.setGeometricCenterPointLocal(new Point3f(calcAxisAlignedBoundingRectXY[0].x + n / 2.0f, calcAxisAlignedBoundingRectXY[0].y + n2 / 2.0f, -n3 / 2.0f));
        }
    }
    
    public ArrayList<Point3f> getShape() {
        return this.shape;
    }
    
    public ArrayList<Point3f> getShapeWS() {
        final Matrix4f entWorldSpaceMatrix = this.getEntWorldSpaceMatrix();
        final ArrayList<Point3f> list = new ArrayList<Point3f>();
        final Iterator<Point3f> iterator = this.shape.iterator();
        while (iterator.hasNext()) {
            final Point3f e = new Point3f((Point3f)iterator.next());
            entWorldSpaceMatrix.transform(e);
            list.add(e);
        }
        return list;
    }
    
    protected void addWireDip(final ICDWireDip icdWireDip) {
        this.mapDip(icdWireDip, true, true);
        this.setModified(true);
    }
    
    protected ArrayList<Point3f> getShapeForLineParameter(final LineParameter key) {
        final Point3f startPoint = key.getStartPoint();
        final Point3f endPoint = key.getEndPoint();
        final Vector3f vector3f = new Vector3f(endPoint.x - startPoint.x, endPoint.y - startPoint.y, 0.0f);
        vector3f.normalize();
        final ArrayList<Point3f> list = new ArrayList<Point3f>();
        list.add(new Point3f(startPoint));
        final ArrayList<ICDWireDip> list2 = this.wireDipMap.get(key);
        if (list2 != null) {
            final Iterator<ICDWireDip> iterator = list2.iterator();
            while (iterator.hasNext()) {
                final CircleParameter circleParameter = iterator.next().getCircleParameter();
                final ArrayList rayIntersections = circleParameter.getRayIntersections(startPoint, vector3f, CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN);
                final FilletParameter filletParameter = new FilletParameter(key, circleParameter, 1.125f, (Point3f)rayIntersections.get(0), !this.isMirrored(), false, false);
                final FilletParameter filletParameter2 = new FilletParameter(key, circleParameter, 1.125f, (Point3f)rayIntersections.get(1), !this.isMirrored(), false, false);
                filletParameter.setClockwisePath(true);
                filletParameter2.setClockwisePath(true);
                final Point3f lineFilletTangentPoint = filletParameter.getLineFilletTangentPoint(key);
                final Point3f circleFilletTangentPoint = filletParameter.getCircleFilletTangentPoint(circleParameter);
                final Point3f circleFilletTangentPoint2 = filletParameter2.getCircleFilletTangentPoint(circleParameter);
                final Point3f lineFilletTangentPoint2 = filletParameter2.getLineFilletTangentPoint(key);
                list.addAll(filletParameter.getPath(lineFilletTangentPoint, circleFilletTangentPoint, true, !this.isMirrored(), this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor")));
                list.addAll(circleParameter.getPath(circleFilletTangentPoint, circleFilletTangentPoint2, true, this.isMirrored(), this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor")));
                list.addAll(filletParameter2.getPath(circleFilletTangentPoint2, lineFilletTangentPoint2, true, !this.isMirrored(), this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor")));
            }
        }
        list.add(new Point3f(endPoint));
        return list;
    }
    
    protected ArrayList<Parameter2D> getParametersForLine(final LineParameter lineParameter) {
        final ArrayList<Parameter2D> list = new ArrayList<Parameter2D>();
        try {
            final Point3f startPoint = lineParameter.getStartPoint();
            final Point3f endPoint = lineParameter.getEndPoint();
            final Vector3f vector3f = new Vector3f(endPoint.x - startPoint.x, endPoint.y - startPoint.y, 0.0f);
            vector3f.normalize();
            boolean b = false;
            final ArrayList<ICDWireDip> list2 = this.wireDipMap.get(lineParameter);
            if (list2 != null && list2.size() > 0) {
                b = true;
                LineParameter lineParameter2 = new LineParameter();
                lineParameter2.setStartPoint(startPoint);
                for (final ICDWireDip icdWireDip : list2) {
                    final LineParameter lineParameter3 = new LineParameter();
                    lineParameter2.setEndPoint(endPoint);
                    lineParameter3.setStartPoint(startPoint);
                    lineParameter3.setEndPoint(endPoint);
                    final CircleParameter circleParameter = icdWireDip.getCircleParameter();
                    final ArrayList rayIntersections = circleParameter.getRayIntersections(startPoint, vector3f, CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN);
                    final FilletParameter e = new FilletParameter(lineParameter2, circleParameter, 1.125f, (Point3f)rayIntersections.get(0), !this.isMirrored(), false, false);
                    final FilletParameter e2 = new FilletParameter(lineParameter3, circleParameter, 1.125f, (Point3f)rayIntersections.get(1), !this.isMirrored(), false, false);
                    e.setClockwisePath(true);
                    e2.setClockwisePath(true);
                    final Point3f lineFilletTangentPoint = e.getLineFilletTangentPoint(lineParameter2);
                    final Point3f lineFilletTangentPoint2 = e2.getLineFilletTangentPoint(lineParameter3);
                    lineParameter2.setEndPoint(lineFilletTangentPoint);
                    lineParameter3.setStartPoint(lineFilletTangentPoint2);
                    list.add((Parameter2D)lineParameter2);
                    list.add((Parameter2D)e);
                    list.add((Parameter2D)circleParameter);
                    list.add((Parameter2D)e2);
                    lineParameter2 = lineParameter3;
                }
                lineParameter2.setEndPoint(endPoint);
                list.add((Parameter2D)lineParameter2);
            }
            if (!b) {
                list.add((Parameter2D)lineParameter);
            }
        }
        catch (Exception ex) {
            System.out.println("Automatically Generated Exception Log(ICDParametricWorksurface.java,593)[" + ex.getClass() + "]: " + ex.getMessage());
            System.err.println("Could not create line parameter");
        }
        return list;
    }
    
    protected ArrayList<Parameter2D> getParametersForCornerWireDip(final LineParameter lineParameter, final LineParameter lineParameter2, final boolean b, final boolean b2, final boolean b3) {
        final ArrayList<Parameter2D> list = new ArrayList<Parameter2D>();
        final Point3f intersectionPoint2D = MathUtilities.getIntersectionPoint2D(lineParameter.getStartPoint(), lineParameter.getEndPoint(), lineParameter2.getStartPoint(), lineParameter2.getEndPoint());
        if (intersectionPoint2D != null) {
            final CircleParameter e = new CircleParameter(intersectionPoint2D, 2.88f);
            Point3f point3f = lineParameter.getStartPoint();
            Point3f point3f2 = lineParameter2.getStartPoint();
            if (MathUtilities.isSamePoint(point3f, intersectionPoint2D, 0.001f)) {
                point3f = lineParameter.getEndPoint();
            }
            if (MathUtilities.isSamePoint(point3f2, intersectionPoint2D, 0.001f)) {
                point3f2 = lineParameter2.getEndPoint();
            }
            final Vector3f vector3f = new Vector3f(intersectionPoint2D.x - point3f.x, intersectionPoint2D.y - point3f.y, 0.0f);
            final Vector3f vector3f2 = new Vector3f(intersectionPoint2D.x - point3f2.x, intersectionPoint2D.y - point3f2.y, 0.0f);
            final Point3f point3f3 = e.getRayIntersections(point3f, vector3f, CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
            final Point3f point3f4 = e.getRayIntersections(point3f2, vector3f2, CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
            final FilletParameter e2 = new FilletParameter(lineParameter, e, 1.125f, point3f3, b, false, false);
            final FilletParameter e3 = new FilletParameter(lineParameter2, e, 1.125f, point3f4, b2, false, false);
            e2.setClockwisePath(true);
            e3.setClockwisePath(true);
            e.setClockwisePath(false);
            e2.setLinePointToFilletTangentPoint(lineParameter, intersectionPoint2D);
            e3.setLinePointToFilletTangentPoint(lineParameter2, intersectionPoint2D);
            list.add((Parameter2D)e2);
            list.add((Parameter2D)e);
            list.add((Parameter2D)e3);
        }
        return list;
    }
    
    protected void mapDip(final ICDWireDip icdWireDip, final boolean b, final boolean b2) {
        if (!this.getCurrentOption().containsReferenceToHeavyWeightType(icdWireDip.getCurrentType())) {
            return;
        }
        LineParameter value = null;
        Point3f point3f = null;
        Point3f calculateNextPoint = null;
        Point3f point3f2 = null;
        Point3f point3f3 = null;
        boolean b3 = true;
        float n = Float.MAX_VALUE;
        for (final LineParameter lineParameter : this.lineParams) {
            final Point3f point3f4 = new Point3f(lineParameter.getStartPoint());
            final Point3f point3f5 = new Point3f(lineParameter.getEndPoint());
            this.getEntWorldSpaceMatrix().transform(point3f4);
            this.getEntWorldSpaceMatrix().transform(point3f5);
            point3f4.z = 0.0f;
            point3f5.z = 0.0f;
            final Point3f calculatePointNormalProjection2D = MathUtilities.calculatePointNormalProjection2D(point3f4, point3f5, icdWireDip.getBasePointWorldSpace());
            calculatePointNormalProjection2D.z = 0.0f;
            if (MathUtilities.isPointOnLine(point3f4, point3f5, calculatePointNormalProjection2D, true, 0.002f)) {
                final Point3f basePointWorldSpace = icdWireDip.getBasePointWorldSpace();
                basePointWorldSpace.z = 0.0f;
                final float distance = calculatePointNormalProjection2D.distance(basePointWorldSpace);
                if (distance >= n) {
                    continue;
                }
                point3f2 = point3f4;
                point3f3 = point3f5;
                n = distance;
                value = lineParameter;
                point3f = new Point3f(calculatePointNormalProjection2D);
                final Point3f wireDipSnapPoint = this.getWireDipSnapPoint(point3f);
                if (MathUtilities.isPointOnLine(point3f4, point3f5, wireDipSnapPoint) && this.isSnapPointValid(wireDipSnapPoint) && this.isSnapPointInRange(wireDipSnapPoint, basePointWorldSpace)) {
                    point3f = wireDipSnapPoint;
                }
                if (b2) {
                    calculateNextPoint = MathUtilities.calculateNextPoint(MathUtilities.calculateRotation(point3f4, point3f5) + (float)(this.isMirrored() ? -1.5707963267948966 : 1.5707963267948966), 2.13f, point3f);
                }
                else {
                    b3 = false;
                    calculateNextPoint = new Point3f();
                }
            }
        }
        if (point3f != null) {
            final Point3f convertSpaces = MathUtilities.convertSpaces(point3f, this.getEntWorldSpaceMatrix());
            icdWireDip.setBasePoint(convertSpaces);
            this.setDipReferencePoint(value, icdWireDip);
            final int snapPointIndex = this.getSnapPointIndex(convertSpaces);
            if (snapPointIndex != -1) {
                icdWireDip.setSnapPointIndex(snapPointIndex);
            }
            else {
                icdWireDip.setSnapPointIndex(-1);
            }
            if (b) {
                icdWireDip.cutFromTree();
                this.addToTree((EntityObject)icdWireDip);
            }
            this.calculate();
            final Point3f convertSpaces2 = MathUtilities.convertSpaces(calculateNextPoint, icdWireDip.getEntWorldSpaceMatrix());
            if (b3) {
                icdWireDip.setCircleParameterLocation(convertSpaces2);
            }
            ArrayList<ICDWireDip> value2 = this.wireDipMap.get(value);
            if (value2 == null) {
                value2 = new ArrayList<ICDWireDip>();
                this.wireDipMap.put(value, value2);
            }
            this.wireDipParamLookupMap.put(icdWireDip, value);
            if (value2.contains(icdWireDip)) {
                value2.remove(icdWireDip);
            }
            final float distance2 = point3f.distance(point3f2);
            if (point3f.distance(point3f3) > 5.0f && distance2 > 5.0f) {
                boolean b4 = false;
                int index = 0;
                final Iterator<ICDWireDip> iterator2 = value2.iterator();
                while (iterator2.hasNext()) {
                    final float calculate2DLength = MathUtilities.calculate2DLength(iterator2.next().getBasePoint3f(), value.getStartPoint());
                    if (distance2 > calculate2DLength) {
                        ++index;
                    }
                    if (Math.abs(calculate2DLength - distance2) < 10.0f) {
                        b4 = true;
                    }
                }
                if (!b4) {
                    value2.add(index, icdWireDip);
                }
            }
        }
    }
    
    protected void updateDipLocations() {
        for (final LineParameter key : this.lineParams) {
            final ArrayList<ICDWireDip> list = this.wireDipMap.get(key);
            if (list != null) {
                for (final ICDWireDip icdWireDip : list) {
                    icdWireDip.setBasePoint(MathUtilities.calculatePointNormalProjection2D(key.getStartPoint(), key.getEndPoint(), icdWireDip.getBasePoint3f()));
                }
            }
        }
    }
    
    protected void removeWireDip(final ICDWireDip icdWireDip) {
        this.removeWireDipFromMaps(icdWireDip);
        this.setModified(true);
    }
    
    private void removeWireDipFromMaps(final ICDWireDip o) {
        final LineParameter key = this.wireDipParamLookupMap.get(o);
        if (key != null) {
            this.wireDipParamLookupMap.remove(o);
            this.wireDipMap.get(key).remove(o);
        }
    }
    
    protected void printWireDipMap() {
        for (final LineParameter key : this.wireDipMap.keySet()) {
            System.err.println("CurrKey: " + key.hashCode());
            final Iterator<ICDWireDip> iterator2 = this.wireDipMap.get(key).iterator();
            while (iterator2.hasNext()) {
                System.err.println(" CurrDip: " + iterator2.next().hashCode());
            }
        }
    }
    
    @Override
    public void solve() {
        final boolean modified = this.isModified();
        super.solve();
        if (modified) {
            final ArrayList<ICDWireDip> list = new ArrayList<ICDWireDip>();
            final ArrayList<ICDParametricCutout> list2 = new ArrayList<ICDParametricCutout>();
            this.cleanUpWireDips();
            for (final EntityObject entityObject : this.getChildrenVector()) {
                if (entityObject instanceof ICDWireDip) {
                    list.add((ICDWireDip)entityObject);
                }
                if (entityObject instanceof ICDParametricCutout) {
                    list2.add((ICDParametricCutout)entityObject);
                }
            }
            final Iterator<ICDWireDip> iterator2 = list.iterator();
            while (iterator2.hasNext()) {
                this.mapDip(iterator2.next(), false, true);
            }
            for (final ICDParametricCutout icdParametricCutout : list2) {
                this.mapCutout(icdParametricCutout);
                icdParametricCutout.validateCutout();
            }
        }
    }
    
    public Point3f getWireDipSnapPoint(final Point3f point3f) {
        float n = Float.MAX_VALUE;
        Point3f point3f2 = point3f;
        for (int i = this.wireDipSnapPoints.size() - 1; i >= 0; --i) {
            final Point3f point3f3 = new Point3f((Point3f)this.wireDipSnapPoints.get(i));
            this.getEntWorldSpaceMatrix().transform(point3f3);
            final float distance = point3f.distance(point3f3);
            if (distance < n) {
                n = distance;
                point3f2 = point3f3;
                point3f2.z = 0.0f;
            }
        }
        return point3f2;
    }
    
    public void addWireDipSnapPoint(final Point3f e) {
        if (this.isSnapPointValid(e)) {
            this.wireDipSnapPoints.add(e);
        }
    }
    
    public boolean isSnapPointValid(final Point3f point3f) {
        final Iterator breadthFirstEnumerationIterator = this.getBreadthFirstEnumerationIterator();
        while (breadthFirstEnumerationIterator.hasNext()) {
            final ICDWireDip next = breadthFirstEnumerationIterator.next();
            if (next instanceof ICDWireDip && next.getBasePoint3f().distance(point3f) < 0.02f) {
                return false;
            }
        }
        return true;
    }
    
    public void updateSnappedWireDips(final Point3f basePoint, final int n) {
        final Iterator breadthFirstEnumerationIterator = this.getBreadthFirstEnumerationIterator();
        while (breadthFirstEnumerationIterator.hasNext()) {
            final ICDWireDip next = breadthFirstEnumerationIterator.next();
            if (next instanceof ICDWireDip) {
                if (n == next.getSnapPointIndex()) {
                    next.setBasePoint(basePoint);
                }
                next.setRefPoint(this.wireDipRefPoints.get(0));
            }
        }
    }
    
    public int getSnapPointIndex(final Point3f point3f) {
        final Point3f key = new Point3f(point3f);
        key.z = 0.0f;
        Integer n = this.snapPointIndexMap.get(key);
        if (n == null) {
            for (final Point3f key2 : this.snapPointIndexMap.keySet()) {
                if (MathUtilities.isSamePoint(key2, key, 0.02f)) {
                    n = this.snapPointIndexMap.get(key2);
                    break;
                }
            }
        }
        if (n != null) {
            return n;
        }
        return -1;
    }
    
    public boolean isSnapPointInRange(final Point3f point3f, final Point3f point3f2) {
        return point3f.distance(point3f2) < 10.0f;
    }
    
    public void updateDipLocation(final ICDWireDip icdWireDip, final float n) {
        if (icdWireDip.getRefPoint() != null) {
            icdWireDip.setBasePoint(pointAt(icdWireDip.getRefPoint(), icdWireDip.getDirection(), n));
            icdWireDip.setDistanceModified();
            this.mapDip(icdWireDip, false, false);
            this.setModified(true);
        }
    }
    
    public static Point3f pointAt(final Point3f point3f, final Vector3f vector3f, final float n) {
        return new Point3f(vector3f.x * n + point3f.x, vector3f.y * n + point3f.y, 0.0f);
    }
    
    public static Point3f pointAt(final Point3f point3f, final Vector3f vector3f, final float n, final float n2) {
        return new IvQuat(new Vector3f(0.0f, 0.0f, 1.0f), (float)Math.toRadians(n2), point3f).rotate(pointAt(point3f, vector3f, n));
    }
    
    public void setDipReferencePoint(final LineParameter lineParameter, final ICDWireDip icdWireDip) {
        final Point3f refPoint = this.wireDipRefPoints.get(0);
        icdWireDip.setRefPoint(refPoint);
        final Point3f basePoint3f = icdWireDip.getBasePoint3f();
        icdWireDip.setDirection(new Vector3f(basePoint3f.x - refPoint.x, basePoint3f.y - refPoint.y, 0.0f));
    }
    
    protected void validateDimensionAttributes() {
        this.clampAttributeValue("ICD_Parametric_Width", this.getWidthMin(), this.getWidthMax());
        this.clampAttributeValue("ICD_Parametric_Width1", this.getWidth1Min(), this.getWidth1Max());
        this.clampAttributeValue("ICD_Parametric_Width2", this.getWidth2Min(), this.getWidth2Max());
        this.clampAttributeValue("ICD_Parametric_Depth", this.getDepthMin(), this.getDepthMax());
        this.clampAttributeValue("ICD_Parametric_Depth1", this.getDepth1Min(), this.getDepth1Max());
        this.clampAttributeValue("ICD_Parametric_Depth2", this.getDepth2Min(), this.getDepth2Max());
    }
    
    protected void clampAttributeValue(final String s, final float f, final float f2) {
        if (this.containsAttributeKey(s)) {
            final float attributeValueAsFloat = this.getAttributeValueAsFloat(s);
            if (attributeValueAsFloat < f) {
                this.getAttributeObject(s).setCurrentValueAsString(String.valueOf(f));
            }
            else if (attributeValueAsFloat > f2) {
                this.getAttributeObject(s).setCurrentValueAsString(String.valueOf(f2));
            }
        }
    }
    
    protected float getMinimumValueFromAttribute(final String s) {
        float attributeValueAsFloat = 0.0f;
        if (this.containsAttributeKey(s)) {
            attributeValueAsFloat = this.getAttributeValueAsFloat(s);
        }
        return attributeValueAsFloat;
    }
    
    protected float getMaximumValueFromAttribute(final String s) {
        float attributeValueAsFloat = 500.0f;
        if (this.containsAttributeKey(s)) {
            attributeValueAsFloat = this.getAttributeValueAsFloat(s);
        }
        return attributeValueAsFloat;
    }
    
    public float getWidthMin() {
        return this.getMinimumValueFromAttribute("ICD_Parametric_Width_Min");
    }
    
    public float getWidthMax() {
        return this.getMaximumValueFromAttribute("ICD_Parametric_Width_Max");
    }
    
    public float getWidth1Min() {
        return this.getMinimumValueFromAttribute("ICD_Parametric_Width1_Min");
    }
    
    public float getWidth1Max() {
        return this.getMaximumValueFromAttribute("ICD_Parametric_Width1_Max");
    }
    
    public float getWidth2Min() {
        return this.getMinimumValueFromAttribute("ICD_Parametric_Width2_Min");
    }
    
    public float getWidth2Max() {
        return this.getMaximumValueFromAttribute("ICD_Parametric_Width2_Max");
    }
    
    public float getDepthMin() {
        return this.getMinimumValueFromAttribute("ICD_Parametric_Depth_Min");
    }
    
    public float getDepthMax() {
        return this.getMaximumValueFromAttribute("ICD_Parametric_Depth_Max");
    }
    
    public float getDepth1Min() {
        return this.getMinimumValueFromAttribute("ICD_Parametric_Depth1_Min");
    }
    
    public float getDepth1Max() {
        return this.getMaximumValueFromAttribute("ICD_Parametric_Depth1_Max");
    }
    
    public float getDepth2Min() {
        return this.getMinimumValueFromAttribute("ICD_Parametric_Depth2_Min");
    }
    
    public float getDepth2Max() {
        return this.getMaximumValueFromAttribute("ICD_Parametric_Depth2_Max");
    }
    
    protected void addCutout(final ICDParametricCutout icdParametricCutout) {
        this.mapCutout(icdParametricCutout);
        this.setModified(true);
    }
    
    protected void mapCutout(final ICDParametricCutout icdParametricCutout) {
        final Point3f basePointWorldSpace = icdParametricCutout.getBasePointWorldSpace();
        Point3f cutoutSnapPoint;
        if (icdParametricCutout.getAttributeValueAsBoolean("isUsePredefinedSnap", true)) {
            cutoutSnapPoint = this.getCutoutSnapPoint(basePointWorldSpace);
        }
        else {
            cutoutSnapPoint = new Point3f(basePointWorldSpace);
            cutoutSnapPoint.z = 0.0f;
        }
        final Point3f convertSpaces = MathUtilities.convertSpaces(cutoutSnapPoint, this.getEntWorldSpaceMatrix());
        icdParametricCutout.cutFromTree();
        this.addToTree((EntityObject)icdParametricCutout);
        convertSpaces.z = 0.0f;
        icdParametricCutout.setBasePoint(convertSpaces);
        this.calculate();
        icdParametricCutout.setCutoutSnapPointIndex(this.getCutoutSnapPointIndex(convertSpaces));
    }
    
    public void updateSnappedCutouts(final Point3f basePoint, final int n) {
        final Iterator breadthFirstEnumerationIterator = this.getBreadthFirstEnumerationIterator();
        while (breadthFirstEnumerationIterator.hasNext()) {
            final ICDParametricCutout next = breadthFirstEnumerationIterator.next();
            if (next instanceof ICDParametricCutout && next.getAttributeValueAsBoolean("isUsePredefinedSnap", true) && n == next.getCutoutSnapPointIndex()) {
                next.setBasePoint(basePoint);
            }
        }
    }
    
    public boolean isCutoutSnapPointValid(final Point3f point3f) {
        final Iterator breadthFirstEnumerationIterator = this.getBreadthFirstEnumerationIterator();
        while (breadthFirstEnumerationIterator.hasNext()) {
            final ICDParametricCutout next = breadthFirstEnumerationIterator.next();
            if (next instanceof ICDParametricCutout && next.getBasePoint3f().distance(point3f) < 0.02f) {
                return false;
            }
        }
        return true;
    }
    
    public void addCutoutSnapPoint(final Point3f e) {
        if (this.isCutoutSnapPointValid(e)) {
            this.cutoutSnapPoints.add(e);
        }
    }
    
    public int getCutoutSnapPointIndex(final Point3f point3f) {
        final Point3f key = new Point3f(point3f);
        key.z = 0.0f;
        Integer n = this.cutoutSnapPointIndexMap.get(key);
        if (n == null) {
            for (final Point3f key2 : this.cutoutSnapPointIndexMap.keySet()) {
                if (MathUtilities.isSamePoint(key2, key, 0.02f)) {
                    n = this.cutoutSnapPointIndexMap.get(key2);
                    break;
                }
            }
        }
        if (n != null) {
            return n;
        }
        return -1;
    }
    
    public Point3f getCutoutSnapPoint(final Point3f point3f) {
        float n = Float.MAX_VALUE;
        final Point3f point3f2 = new Point3f(point3f);
        point3f2.z = 0.0f;
        Point3f point3f3 = point3f2;
        for (int i = this.cutoutSnapPoints.size() - 1; i >= 0; --i) {
            final Point3f point3f4 = new Point3f((Point3f)this.cutoutSnapPoints.get(i));
            this.getEntWorldSpaceMatrix().transform(point3f4);
            point3f4.z = 0.0f;
            final float distance = point3f2.distance(point3f4);
            if (distance < n && distance < 5.0f) {
                n = distance;
                point3f3 = point3f4;
                point3f3.z = 0.0f;
            }
        }
        return point3f3;
    }
    
    public ArrayList<ICDParametricCutout> getCutouts() {
        final ArrayList<ICDParametricCutout> list = new ArrayList<ICDParametricCutout>();
        final Iterator breadthFirstEnumerationIterator = this.getBreadthFirstEnumerationIterator();
        while (breadthFirstEnumerationIterator.hasNext()) {
            final ICDParametricCutout next = breadthFirstEnumerationIterator.next();
            if (next instanceof ICDParametricCutout) {
                list.add(next);
            }
        }
        return list;
    }
    
    public ArrayList<ICDWireDip> getWireDips() {
        final ArrayList<ICDWireDip> list = new ArrayList<ICDWireDip>();
        for (final EntityObject entityObject : this.getChildrenVector()) {
            if (entityObject instanceof ICDWireDip) {
                list.add((ICDWireDip)entityObject);
            }
        }
        return list;
    }
    
    private void cleanUpWireDips() {
        final ArrayList<ICDWireDip> wireDips = this.getWireDips();
        final ArrayList<ICDWireDip> list = new ArrayList<ICDWireDip>();
        final Iterator<LineParameter> iterator = this.wireDipMap.keySet().iterator();
        while (iterator.hasNext()) {
            for (final ICDWireDip icdWireDip : this.wireDipMap.get(iterator.next())) {
                if (!wireDips.contains(icdWireDip)) {
                    list.add(icdWireDip);
                }
            }
        }
        final Iterator<ICDWireDip> iterator3 = list.iterator();
        while (iterator3.hasNext()) {
            this.removeWireDipFromMaps(iterator3.next());
        }
    }
    
    public ArrayList<LineParameter> getSideLineParameters() {
        return this.sideLineParams;
    }
    
    public ArrayList<LineParameter> getLineParameters() {
        return this.lineParams;
    }
    
    public HashMap<ICDWireDip, LineParameter> getWireDipParamLookupMap() {
        return this.wireDipParamLookupMap;
    }
    
    public Vector3f getCutoutXDirection() {
        return this.cutoutXDirection;
    }
    
    public Vector3f getCutoutYDirection() {
        return this.cutoutYDirection;
    }
    
    public Point3f getCutoutRefPoint() {
        return this.cutoutRefPoint;
    }
    
    protected boolean isMirrored() {
        return false;
    }
    
    protected void mirrorPoints(final RayParameter rayParameter, final Point3f... array) {
        if (array != null) {
            for (final Point3f point3f : array) {
                point3f.set((Tuple3f)MathUtilities.mirrorPoint2D(point3f, rayParameter.getOrigin(), rayParameter.getDirection()));
            }
        }
    }
    
    protected void populateShapeAndPlotNodes(final boolean b, final boolean b2) {
        this.shape.clear();
        this.plotOutlineNodes.clear();
        this.plotNodes.clear();
        for (int i = 0; i < this.boundary.size(); ++i) {
            BRepObject bRepObject;
            if (i == 0) {
                bRepObject = this.boundary.get(this.boundary.size() - 1);
            }
            else {
                bRepObject = this.boundary.get(i - 1);
            }
            BRepObject bRepObject2;
            if (i == this.boundary.size() - 1) {
                bRepObject2 = this.boundary.get(0);
            }
            else {
                bRepObject2 = this.boundary.get(i + 1);
            }
            final BRepObject bRepObject3 = this.boundary.get(i);
            if (b) {
                this.shape.addAll(bRepObject3.getPath(this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor")));
            }
            if (b2) {
                bRepObject3.populateOutputNodes((ArrayList)this.plotNodes, this.getEntWorldSpaceMatrix(), bRepObject, bRepObject2);
            }
        }
        if (b2) {
            final Iterator<IceOutputNode> iterator = this.plotNodes.iterator();
            while (iterator.hasNext()) {
                iterator.next().setDXFLayer("T EDGE");
            }
            final ArrayList<IceOutputNode> c = new ArrayList<IceOutputNode>();
            for (int j = 0; j < this.cutoffBoundary.size(); ++j) {
                BRepObject bRepObject4;
                if (j == 0) {
                    bRepObject4 = this.cutoffBoundary.get(this.cutoffBoundary.size() - 1);
                }
                else {
                    bRepObject4 = this.cutoffBoundary.get(j - 1);
                }
                BRepObject bRepObject5;
                if (j == this.cutoffBoundary.size() - 1) {
                    bRepObject5 = this.cutoffBoundary.get(0);
                }
                else {
                    bRepObject5 = this.cutoffBoundary.get(j + 1);
                }
                this.cutoffBoundary.get(j).populateOutputNodes((ArrayList)c, this.getEntWorldSpaceMatrix(), bRepObject4, bRepObject5);
            }
            final Iterator<IceOutputNode> iterator2 = c.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().setDXFLayer("RAW EDGE");
            }
            this.plotNodes.addAll(c);
        }
    }
    
    public boolean allowsNotching() {
        return "Yes".equals(this.getAttributeValueAsString("ICD_Parametric_AllowsNotching"));
    }
    
    protected void updateBRep(final boolean b, final boolean b2) {
        this.updateBRep(b, b2, this.allowsNotching());
    }
    
    protected void updateBRep(final boolean b, final boolean b2, final boolean b3) {
        final boolean mirrored = this.isMirrored();
        this.boundary.clear();
        this.cutoffBoundary.clear();
        for (int i = 0; i < this.allParameters.size(); ++i) {
            final Parameter2D parameter2D = this.allParameters.get(i);
            final Parameter2D parameter2D2 = (i < 1) ? this.allParameters.get(this.allParameters.size() - 1) : this.allParameters.get(i - 1);
            final Parameter2D parameter2D3 = (i >= this.allParameters.size() - 1) ? this.allParameters.get(0) : this.allParameters.get(i + 1);
            if (parameter2D instanceof LineParameter) {
                final LineParameter lineParameter = (LineParameter)parameter2D;
                this.boundary.add((BRepObject)lineParameter.getBLine());
                float n = -0.125f;
                if (mirrored) {
                    n = -n;
                }
                this.cutoffBoundary.add((BRepObject)lineParameter.getBLine((double)n));
            }
            else if (parameter2D instanceof CircleParameter) {
                final CircleParameter circleParameter = (CircleParameter)parameter2D;
                Point3f point3f = null;
                Point3f point3f2 = null;
                if (parameter2D2 instanceof FilletParameter) {
                    point3f = ((FilletParameter)parameter2D2).getCircleFilletTangentPoint(circleParameter);
                }
                else if (parameter2D2 instanceof CircleParameter) {
                    point3f = ((CircleParameter)parameter2D2).touch(circleParameter);
                }
                else if (parameter2D2 instanceof LineParameter) {
                    point3f = circleParameter.getLineCircleTangentPoint((LineParameter)parameter2D2);
                }
                if (parameter2D3 instanceof FilletParameter) {
                    point3f2 = ((FilletParameter)parameter2D3).getCircleFilletTangentPoint(circleParameter);
                }
                else if (parameter2D3 instanceof CircleParameter) {
                    point3f2 = ((CircleParameter)parameter2D3).touch(circleParameter);
                }
                else if (parameter2D3 instanceof LineParameter) {
                    point3f2 = circleParameter.getLineCircleTangentPoint((LineParameter)parameter2D3);
                }
                final boolean b4 = circleParameter.isClockwisePath() ^ mirrored;
                if (point3f == null || Float.isNaN(point3f.x) || Float.isNaN(point3f.y)) {
                    point3f = new Point3f(-circleParameter.getRadius(), 0.0f, 0.0f);
                }
                if (point3f2 == null || Float.isNaN(point3f2.x) || Float.isNaN(point3f2.y)) {
                    point3f2 = new Point3f(circleParameter.getRadius(), 0.0f, 0.0f);
                }
                point3f.set(MathUtilities.roundFloat(point3f.x, 5), MathUtilities.roundFloat(point3f.y, 5), MathUtilities.roundFloat(point3f.z, 5));
                point3f2.set(MathUtilities.roundFloat(point3f2.x, 5), MathUtilities.roundFloat(point3f2.y, 5), MathUtilities.roundFloat(point3f2.z, 5));
                if (this.allParameters.size() == 1) {
                    this.boundary.add((BRepObject)circleParameter.getBArc(new Point3d((double)point3f.x, (double)point3f.y, (double)point3f.z), new Point3d((double)point3f2.x, (double)point3f2.y, (double)point3f2.z), b4, circleParameter.getShorterPath()));
                    float n2 = -0.125f;
                    if (mirrored) {
                        n2 = -n2;
                    }
                    this.cutoffBoundary.add((BRepObject)circleParameter.getBArc(new Point3d((double)point3f.x, (double)point3f.y, (double)point3f.z), new Point3d((double)point3f2.x, (double)point3f2.y, (double)point3f2.z), b4, circleParameter.getShorterPath(), (double)n2));
                    this.boundary.add((BRepObject)circleParameter.getBArc(new Point3d((double)point3f2.x, (double)point3f2.y, (double)point3f2.z), new Point3d((double)point3f.x, (double)point3f.y, (double)point3f.z), b4, circleParameter.getShorterPath()));
                    this.cutoffBoundary.add((BRepObject)circleParameter.getBArc(new Point3d((double)point3f2.x, (double)point3f2.y, (double)point3f2.z), new Point3d((double)point3f.x, (double)point3f.y, (double)point3f.z), b4, circleParameter.getShorterPath(), (double)n2));
                }
                else {
                    this.boundary.add((BRepObject)circleParameter.getBArc(new Point3d((double)point3f.x, (double)point3f.y, (double)point3f.z), new Point3d((double)point3f2.x, (double)point3f2.y, (double)point3f2.z), b4, circleParameter.getShorterPath()));
                    float n3 = -0.125f;
                    if (mirrored) {
                        n3 = -n3;
                    }
                    this.cutoffBoundary.add((BRepObject)circleParameter.getBArc(new Point3d((double)point3f.x, (double)point3f.y, (double)point3f.z), new Point3d((double)point3f2.x, (double)point3f2.y, (double)point3f2.z), b4, circleParameter.getShorterPath(), (double)n3));
                }
            }
            else if (parameter2D instanceof FilletParameter) {
                final FilletParameter filletParameter = (FilletParameter)parameter2D;
                Point3f point3f3 = new Point3f();
                Point3f point3f4 = new Point3f();
                if (parameter2D2 instanceof CircleParameter) {
                    point3f3 = filletParameter.getCircleFilletTangentPoint((CircleParameter)parameter2D2);
                }
                else if (parameter2D2 instanceof LineParameter) {
                    point3f3 = ((LineParameter)parameter2D2).getEndPoint();
                }
                if (parameter2D3 instanceof CircleParameter) {
                    point3f4 = filletParameter.getCircleFilletTangentPoint((CircleParameter)parameter2D3);
                }
                else if (parameter2D3 instanceof LineParameter) {
                    point3f4 = ((LineParameter)parameter2D3).getStartPoint();
                }
                final boolean b5 = filletParameter.isClockwisePath() ^ mirrored;
                this.boundary.add((BRepObject)filletParameter.getBArc(new Point3d((double)point3f3.x, (double)point3f3.y, (double)point3f3.z), new Point3d((double)point3f4.x, (double)point3f4.y, (double)point3f4.z), b5, true));
                float n4 = -0.125f;
                if (mirrored) {
                    n4 = -n4;
                }
                this.cutoffBoundary.add((BRepObject)filletParameter.getBArc(new Point3d((double)point3f3.x, (double)point3f3.y, (double)point3f3.z), new Point3d((double)point3f4.x, (double)point3f4.y, (double)point3f4.z), b5, true, (double)n4));
            }
        }
        if (b3) {
            this.trim();
        }
        this.boundary = this.sortBoundary(this.boundary);
        this.cutoffBoundary = this.sortBoundary(this.cutoffBoundary);
        this.populateShapeAndPlotNodes(b, b2);
    }
    
    @Override
    public void handleScaleChanges(final float n, final float n2, final float n3, final float n4) {
    }
    
    public Point3f getWidthAnchor() {
        return this.widthAnchor;
    }
    
    public Point3f getWidth1Anchor() {
        return this.width1Anchor;
    }
    
    public Point3f getWidth2Anchor() {
        return this.width2Anchor;
    }
    
    public Point3f getDepthAnchor() {
        return this.depthAnchor;
    }
    
    public Point3f getDepth1Anchor() {
        return this.depth1Anchor;
    }
    
    public Point3f getDepth2Anchor() {
        return this.depth2Anchor;
    }
    
    public Vector3f getWidthDirection() {
        return this.widthDirection;
    }
    
    public Vector3f getWidth1Direction() {
        return this.width1Direction;
    }
    
    public Vector3f getWidth2Direction() {
        return this.width2Direction;
    }
    
    public Vector3f getDepthDirection() {
        return this.depthDirection;
    }
    
    public Vector3f getDepth1Direction() {
        return this.depth1Direction;
    }
    
    public Vector3f getDepth2Direction() {
        return this.depth2Direction;
    }
    
    public float getValidDepth(final float a) {
        return Math.max(Math.min(a, this.getDepthMax()), this.getDepthMin());
    }
    
    public float getValidDepth1(final float a) {
        return Math.max(Math.min(a, this.getDepth1Max()), this.getDepth1Min());
    }
    
    public float getValidDepth2(final float a) {
        return Math.max(Math.min(a, this.getDepth2Max()), this.getDepth2Min());
    }
    
    public float getValidWidth(final float a) {
        return Math.max(Math.min(a, this.getWidthMax()), this.getWidthMin());
    }
    
    public float getValidWidth1(final float a) {
        return Math.max(Math.min(a, this.getWidth1Max()), this.getWidth1Min());
    }
    
    public float getValidWidth2(final float a) {
        return Math.max(Math.min(a, this.getWidth2Max()), this.getWidth2Min());
    }
    
    public boolean usePlotGVT() {
        return false;
    }
    
    public boolean isPrintable() {
        return this.hasCustomAsset();
    }
    
    public void mirror(final Mirror mirror) {
        if (this.getAttributeObject("ICD_Worksurface_Orientation") != null) {
            boolean b;
            if (this.getAttributeValueAsString("ICD_Worksurface_Orientation").equalsIgnoreCase("Left")) {
                this.getAttributeObject("ICD_Worksurface_Orientation").setCurrentValueAsString("Right");
                b = true;
            }
            else {
                this.getAttributeObject("ICD_Worksurface_Orientation").setCurrentValueAsString("Left");
                b = true;
            }
            final Point3f basePointWorldSpace = this.getBasePointWorldSpace();
            Point3f geometricCenterPointWorld = this.getGeometricCenterPointWorld();
            if (geometricCenterPointWorld == null) {
                geometricCenterPointWorld = basePointWorldSpace;
            }
            this.setBasePoint(mirror.getMirroredPoint(new Point3f(geometricCenterPointWorld.x, geometricCenterPointWorld.y, basePointWorldSpace.z)));
            this.setRotation(mirror.getMirroredRotation(this.getRotationWorldSpace() + this.getOffsetMirrorRotation()));
            this.calculate();
            final Point3f geometricCenterPointLocal = this.getGeometricCenterPointLocal();
            this.setBasePoint(MathUtilities.convertSpaces(new Point3f(-geometricCenterPointLocal.x, -geometricCenterPointLocal.y, 0.0f), (EntityObject)this, (EntityObject)this.getSolution()));
            this.calculate();
            if (b) {
                this.updateCutoutsFromAttributes();
                this.updateWiredipsFromAttributes();
            }
        }
    }
    
    public float getOffsetMirrorRotation() {
        return 3.1415927f;
    }
    
    @Override
    public void applyChangesFromEditor(final String s, final PossibleValue possibleValue, final Collection<PossibleValue> collection, final Collection<String> collection2, final String s2) {
        super.applyChangesFromEditor(s, possibleValue, collection, collection2, s2);
        this.calculate();
        if (s.equalsIgnoreCase("ICD_Worksurface_Orientation")) {
            this.updateCutoutsFromAttributes();
            this.updateWiredipsFromAttributes();
        }
    }
    
    public void updateCutoutsFromAttributes() {
        final Iterator breadthFirstEnumerationIterator = this.getBreadthFirstEnumerationIterator();
        while (breadthFirstEnumerationIterator.hasNext()) {
            final EntityObject entityObject = breadthFirstEnumerationIterator.next();
            if (entityObject instanceof ICDParametricCutout) {
                ((ICDParametricCutout)entityObject).updateCutoutLocationFromAttributes();
            }
        }
    }
    
    public void updateWiredipsFromAttributes() {
        final Iterator breadthFirstEnumerationIterator = this.getBreadthFirstEnumerationIterator();
        while (breadthFirstEnumerationIterator.hasNext()) {
            final EntityObject entityObject = breadthFirstEnumerationIterator.next();
            if (entityObject instanceof ICDWireDip) {
                ((ICDWireDip)entityObject).getDirection().x *= -1.0f;
                this.updateDipLocation((ICDWireDip)entityObject, entityObject.getAttributeValueAsFloat("ICD_WireDip_Distance"));
            }
        }
    }
    
    public String getDefaultLayerName() {
        return "Worksurfaces";
    }
    
    protected SimpleSnapper addSimpleSnappablesThatDoNotFilterOccupiedTargets(final boolean b, SimpleSnapper addSimpleSnappablesThatDoNotFilterOccupiedTargets) {
        addSimpleSnappablesThatDoNotFilterOccupiedTargets = super.addSimpleSnappablesThatDoNotFilterOccupiedTargets(true, addSimpleSnappablesThatDoNotFilterOccupiedTargets);
        final Point3f namedPointLocal = this.getNamedPointLocal("Top_Left_Snap_Corner");
        final Point3f namedPointLocal2 = this.getNamedPointLocal("Top_Right_Snap_Corner");
        final float snapRotation = this.getSnapRotation();
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("BRC", namedPointLocal2, Float.valueOf(snapRotation), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("BRR", namedPointLocal2, Float.valueOf(snapRotation), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TLC", namedPointLocal2, Float.valueOf(3.1415927f + snapRotation), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TLL", namedPointLocal2, Float.valueOf(3.1415927f + snapRotation), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("BLC", namedPointLocal, Float.valueOf(snapRotation), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("BLL", namedPointLocal, Float.valueOf(snapRotation), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TRC", namedPointLocal, Float.valueOf(3.1415927f + snapRotation), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TRR", namedPointLocal, Float.valueOf(3.1415927f + snapRotation), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("BRL", namedPointLocal2, Float.valueOf(snapRotation), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TLR", namedPointLocal2, Float.valueOf(3.1415927f + snapRotation), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("BLR", namedPointLocal, Float.valueOf(snapRotation), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TRL", namedPointLocal, Float.valueOf(3.1415927f + snapRotation), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TLWS", namedPointLocal2, Float.valueOf(snapRotation), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TRWS", namedPointLocal, Float.valueOf(snapRotation), 20.0f, true);
        final Point3f namedPointLocal3 = this.getNamedPointLocal("Bottom_Left_Snap_Corner");
        if (namedPointLocal3 != null) {
            addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TLWS2", namedPointLocal3, Float.valueOf(snapRotation + 3.1415927f), 20.0f, true);
        }
        final Point3f namedPointLocal4 = this.getNamedPointLocal("Bottom_Right_Snap_Corner");
        if (namedPointLocal4 != null) {
            addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TRWS2", namedPointLocal4, Float.valueOf(snapRotation + 3.1415927f), 20.0f, true);
        }
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("WSurfIntersectionSnap", namedPointLocal2, Float.valueOf(snapRotation), 30.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("WSurfIntersectionSnap", namedPointLocal, Float.valueOf(-4.712389f), 30.0f, true);
        return addSimpleSnappablesThatDoNotFilterOccupiedTargets;
    }
    
    protected void addSimpleSnapTargets(final SimpleSnapTargetCollection collection) {
        super.addSimpleSnapTargets(collection);
        collection.addWorldSpaceSnapPoint("TLWS", this.getNamedPointWorld("Top_Left_Snap_Corner"), Float.valueOf(this.getRotationWorldSpace() + this.getSnapRotation()));
        collection.addWorldSpaceSnapPoint("TRWS", this.getNamedPointWorld("Top_Right_Snap_Corner"), Float.valueOf(this.getRotationWorldSpace() + this.getSnapRotation()));
        final Point3f namedPointWorld = this.getNamedPointWorld("Top_Left_Snap_Corner_2");
        if (namedPointWorld != null) {
            collection.addWorldSpaceSnapPoint("TLWS2", namedPointWorld, Float.valueOf(this.getRotationWorldSpace() + this.getSnapRotation()));
        }
        final Point3f namedPointWorld2 = this.getNamedPointWorld("Top_Right_Snap_Corner_2");
        if (namedPointWorld2 != null) {
            collection.addWorldSpaceSnapPoint("TRWS2", namedPointWorld2, Float.valueOf(this.getRotationWorldSpace() + this.getSnapRotation()));
        }
    }
    
    @Override
    public float getHeightModifier() {
        return 0.0f;
    }
    
    protected boolean shouldMirrorNamedPoints() {
        return this.isMirrored();
    }
    
    protected void calculateDimensionsOfWorksurface() {
        if (this.shape.size() > 0) {
            final Point3f[] calcAxisAlignedBoundingRectXY = MathUtilities.calcAxisAlignedBoundingRectXY((Collection)this.shape);
            final float n = calcAxisAlignedBoundingRectXY[1].x - calcAxisAlignedBoundingRectXY[0].x;
            final float n2 = calcAxisAlignedBoundingRectXY[1].y - calcAxisAlignedBoundingRectXY[0].y;
            float n3 = 1.25f;
            if (this instanceof ICDParametricDeckOrShelf) {
                n3 = 0.75f;
            }
            this.setXDimension(n);
            this.setYDimension(n2);
            this.setZDimension(n3);
            if (this.getAttributeObject("XDimension") != null) {
                ((FloatAttribute)this.getAttributeObject("XDimension")).setCurrentValue(n);
            }
            if (this.getAttributeObject("YDimension") != null) {
                ((FloatAttribute)this.getAttributeObject("YDimension")).setCurrentValue(n2);
            }
            if (this.getAttributeObject("ZDimension") != null) {
                ((FloatAttribute)this.getAttributeObject("ZDimension")).setCurrentValue(n3);
            }
        }
    }
    
    protected ArrayList<Notch> getNotches() {
        final ArrayList<Notch> list = new ArrayList<Notch>();
        if (this.getSolution() != null) {
            final Iterator breadthFirstEnumerationIterator = this.getSolution().getBreadthFirstEnumerationIterator();
            while (breadthFirstEnumerationIterator.hasNext()) {
                final EntityObject entityObject = breadthFirstEnumerationIterator.next();
                if (entityObject instanceof Notch) {
                    list.add((Notch)entityObject);
                }
            }
        }
        return list;
    }
    
    protected void trim() {
        this.shape.clear();
        this.populateShapeAndPlotNodes(true, false);
        final ArrayList<Notch> notches = this.getNotches();
        BoundingCube worldBoundingCube = null;
        if (!notches.isEmpty()) {
            worldBoundingCube = this.getWorldBoundingCube();
        }
        for (final Notch notch : notches) {
            if (notch.getWorldBoundingCube().intersect(worldBoundingCube)) {
                try {
                    this.trimNotch(notch, this.boundary);
                    this.trimNotch(notch, this.cutoffBoundary);
                }
                catch (Exception ex2) {
                    System.err.println("Could not trim the surface, will skip notch");
                    try {
                        this.updateBRep(true, true, false);
                    }
                    catch (Exception ex) {
                        System.err.println("Could not trim parametric worksurface, Irrecoverable error occured");
                        ex.printStackTrace();
                        this.destroy();
                    }
                }
            }
        }
    }
    
    protected void trimNotch(final Notch notch, final ArrayList<BRepObject> list) {
        final ArrayList<Boolean> list2 = new ArrayList<Boolean>();
        list2.add(0, false);
        final ArrayList notchEdgesLS = notch.getNotchEdgesLS(this.getEntWorldSpaceMatrix(), this.isMirrored());
        final ArrayList<BRepObject> list3 = new ArrayList<BRepObject>();
        final ArrayList<BRepObject> list4 = new ArrayList<BRepObject>();
        for (final BRepObject e : list) {
            final ArrayList trimSegments = e.getTrimSegments(notchEdgesLS, (ArrayList)list2);
            if (trimSegments == null) {
                list3.add(e);
            }
            else {
                list3.addAll(trimSegments);
            }
        }
        final Iterator<NotchEdge> iterator2 = notchEdgesLS.iterator();
        while (iterator2.hasNext()) {
            list4.addAll(iterator2.next().getTrimSegments());
        }
        final ArrayList<BRepObject> c = new ArrayList<BRepObject>();
        for (final BRepObject e2 : list3) {
            final Point3d midpoint = e2.getMidpoint();
            final Point3f point3f = new Point3f((float)midpoint.x, (float)midpoint.y, (float)midpoint.z);
            this.getEntWorldSpaceMatrix().transform(point3f);
            if (!MathUtilities.shapeContainsPoint((List)notch.getShapeWSFloats(), point3f)) {
                c.add(e2);
            }
        }
        for (final BRepObject e3 : list4) {
            final Point3d midpoint2 = e3.getMidpoint();
            if (MathUtilities.shapeContainsPoint((List)this.shape, new Point3f((float)midpoint2.x, (float)midpoint2.y, (float)midpoint2.z))) {
                c.add(e3);
            }
        }
        if (list2.get(0)) {
            list.clear();
            list.addAll(c);
        }
    }
    
    public void paint(final Graphics2D graphics2D, final Transform3D transform3D, final Ice2DPaintableNode ice2DPaintableNode) {
        final Matrix4f entWorldSpaceMatrix = this.getEntWorldSpaceMatrix();
        final Iterator<BRepObject> iterator = this.boundary.iterator();
        while (iterator.hasNext()) {
            iterator.next().paint(graphics2D, transform3D, ice2DPaintableNode, entWorldSpaceMatrix);
        }
    }
    
    public ArrayList<BRepObject> sortBoundary(final ArrayList<BRepObject> list) {
        final ArrayList<BRepObject> list2 = new ArrayList<BRepObject>();
        int n = list.size() - 1;
        while (list.size() > 0) {
            final BRepObject bRepObject = list.get(n);
            if (list2.size() == 0) {
                list2.add(bRepObject);
            }
            else {
                bRepObject.getStartPoint();
                bRepObject.getEndPoint();
                final Point3d endPoint = list2.get(list2.size() - 1).getEndPoint();
                list2.get(list2.size() - 1).getStartPoint();
                if (MathUtilities.isSamePoint(bRepObject.getStartPoint(), endPoint, 0.01) || MathUtilities.isSamePoint(bRepObject.getEndPoint(), endPoint, 0.01)) {
                    list.remove(n);
                    n = list.size() - 1;
                    list2.add(bRepObject);
                }
            }
            if (--n < 0) {
                break;
            }
        }
        return list2;
    }
    
    public boolean shouldFlipEdge3D() {
        return true;
    }
    
    public boolean shouldFlipCutouts3D() {
        return true;
    }
    
    @Override
    public void drawCad(final ICadTreeNode cadTreeNode, final int notDirty) {
        if (this.isDirty(notDirty)) {
            this.destroyCad();
            this.setNotDirty(notDirty);
            if (!this.drawDXFCad(cadTreeNode, notDirty)) {
                this.textNode = this.getCadOutputTextNode(cadTreeNode);
                final ICadLayerTreeNode cadLayerTreeNode = ((ICadRootNode)cadTreeNode).getLayers().get(2);
                if (this.blockNode == null) {
                    cadLayerTreeNode.addChild((ICadTreeNode)(this.blockNode = new ICadBlockNode((CADPaintable)this)));
                    this.blockNode.setScheduledAction(0);
                }
                if (this.blockRefNode == null) {
                    this.blockRefNode = new ICadBlockRefNode((CADPaintable)this, this.blockNode.getBlockName(), this.blockNode.getBlockName(), this.getBasePoint3f(), this.getRotation(), 1.0f, 1.0f, 1.0f);
                    this.blockNode.addChild((ICadTreeNode)this.blockRefNode);
                    this.blockRefNode.setScheduledAction(0);
                }
                else {
                    this.blockRefNode.setInsertionPoint(this.getBasePoint3f());
                    this.blockRefNode.setRotation(this.getRotation());
                    this.blockRefNode.setScheduledAction(2);
                }
                this.blockRefNode.setUpdateBRef(true);
                final Iterator<BRepObject> iterator = this.cutoffBoundary.iterator();
                while (iterator.hasNext()) {
                    iterator.next().drawCad((EntityObject)this, this.blockNode);
                }
                final Iterator<BRepObject> iterator2 = this.boundary.iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().drawCad((EntityObject)this, this.blockNode);
                }
            }
        }
        super.drawCad(cadTreeNode, notDirty);
    }
    
    @Override
    public void destroyCad() {
        if (this.blockNode != null) {
            this.blockRefNode.setScheduledAction(1);
            this.blockRefNode = null;
            this.blockNode.setScheduledAction(1);
            this.blockNode = null;
        }
        if (this.textNode != null) {
            this.textNode.setScheduledAction(1);
            this.textNode = null;
        }
        if (this.blockRefNodeNet != null) {
            this.blockRefNodeNet.erase();
        }
        if (this.textNodeNet != null) {
            this.textNodeNet.erase();
        }
        if (this.textNodeNetForProxyEntity != null) {
            this.textNodeNetForProxyEntity.erase();
        }
        super.destroyCad();
    }
    
    public void finalDestroyCad() {
        if (this.blockRefNodeNet != null) {
            this.blockRefNodeNet.destroy();
            this.blockRefNodeNet = null;
        }
        if (this.textNodeNet != null) {
            this.textNodeNet.destroy();
            this.textNodeNet = null;
        }
        if (this.blockNodeNet != null) {
            this.blockNodeNet.destroy();
            this.blockNodeNet = null;
        }
        if (this.textNodeNetForProxyEntity != null) {
            this.textNodeNetForProxyEntity.destroy();
            this.textNodeNetForProxyEntity = null;
        }
        super.finalDestroyCad();
    }
    
    private ICadTextNode getCadOutputTextNode(final ICadTreeNode cadTreeNode) {
        final DecimalFormat decimalFormat = new DecimalFormat("#.00");
        final String string = this.getShapeTag() + decimalFormat.format(this.getYDimension()) + "x" + decimalFormat.format(this.getXDimension());
        return ICDUtilities.drawCadText((TransformableEntity)this, this.textNode, cadTreeNode, string, this.getCadOutputInsertionPoint(string), 3, this.getCadOutputRotation());
    }
    
    private IceCadMTextNode getCadOutputTextNodeDotNet(final IceCadNodeContainer iceCadNodeContainer, final IceCadMTextNode iceCadMTextNode) {
        final DecimalFormat decimalFormat = new DecimalFormat("#.00");
        final String string = this.getShapeTag() + decimalFormat.format(this.getYDimension()) + "x" + decimalFormat.format(this.getXDimension());
        return ICDUtilities.drawIceCadTextDotNet(iceCadNodeContainer, iceCadMTextNode, string, this.getCadOutputInsertionPoint(string), this.getCadOutputRotation());
    }
    
    private IceCadMTextNode getCadOutputTextNodeForProxyEntityDotNet(final IceCadNodeContainer iceCadNodeContainer, final IceCadMTextNode iceCadMTextNode, final TransformableEntity transformableEntity, final IceCadCompositeBlock iceCadCompositeBlock) {
        final DecimalFormat decimalFormat = new DecimalFormat("#.00");
        final String string = this.getShapeTag() + decimalFormat.format(this.getYDimension()) + "x" + decimalFormat.format(this.getXDimension());
        return ICDUtilities.drawIceCadTextForProxyEntityDotNet(iceCadNodeContainer, iceCadMTextNode, transformableEntity, iceCadCompositeBlock, string, this.getCadOutputInsertionPoint(string), this.getCadOutputRotation());
    }
    
    protected Point3f getCadOutputInsertionPoint(final String s) {
        final Point3f point3f2;
        final Point3f point3f = point3f2 = (Point3f)this.getGeometricCenterPointLocal().clone();
        point3f2.x -= this.getXDimension() / 2.0f - 4.0f;
        final Point3f point3f3 = point3f;
        point3f3.y -= this.getYDimension() / 2.0f - 4.0f;
        return this.convertPointToWorldSpace(point3f);
    }
    
    protected float getCadOutputRotation() {
        return this.getRotationWorldSpace();
    }
    
    @Override
    public void populateCompareNode(final Class clazz, final CompareNode compareNode) {
        super.populateCompareNode(clazz, compareNode);
        final float round = MathUtilities.round(this.getWidth(), 1);
        final float round2 = MathUtilities.round(this.getYDimension(), 1);
        compareNode.addCompareValue("ActualWidth", (Object)round);
        compareNode.addCompareValue("ActualDepth", (Object)round2);
    }
    
    @Override
    public void populateCompareNodeForICD(final Class clazz, final CompareNode compareNode) {
        super.populateCompareNodeForICD(clazz, compareNode);
        compareNode.addCompareValue("notchSpaceCompare", (Object)this.createNotchSpaceCompareNodes());
    }
    
    private EntitySpaceCompareNode createNotchSpaceCompareNodes() {
        final LinkedList list = new LinkedList();
        list.addAll(this.getNotchEntitySpaceCompare());
        return new EntitySpaceCompareNode((Collection)list, this.getEntWorldSpaceMatrix(), (Matrix4f)null);
    }
    
    private Collection<? extends EntitySpaceCompareNodeWrapper> getNotchEntitySpaceCompare() {
        final ArrayList<Notch> notches = this.getNotches();
        if (notches != null) {
            final LinkedList<EntitySpaceCompareNodeWrapper> list = new LinkedList<EntitySpaceCompareNodeWrapper>();
            for (final Notch notch : notches) {
                if (notch.getWorldBoundingCube().intersect(this.getWorldBoundingCube())) {
                    final ArrayList<Point3f> list2 = new ArrayList<Point3f>();
                    final Point3f point3f = new Point3f(notch.getAttributeValueAsFloat("ICD_Notch_Width"), notch.getAttributeValueAsFloat("ICD_Notch_Depth"), 0.0f);
                    if (point3f != null) {
                        list2.add(point3f);
                    }
                    list.add(new EntitySpaceCompareNodeWrapper((TransformableEntity)notch, (Collection)list2));
                }
            }
            return list;
        }
        return null;
    }
    
    public void clearCachedModel() {
        this.cachedShape = null;
    }
    
    static {
        ICD_PARAMETRIC_WORKSURFACE_WIREDIP_SEGMENT_LENGTH = (float)Math.sqrt(8.640000343322754);
        ICDParametricWorksurface.DRAW2D_DEBUG = false;
        ICDParametricWorksurface.DEBUG_POINT_RADIUS = 4;
    }
}
