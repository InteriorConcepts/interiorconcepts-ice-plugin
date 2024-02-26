// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces.parametric;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import org.xith3d.scenegraph.Node;
import org.xith3d.scenegraph.Geometry;
import net.dirtt.icebox.ice3dviewer.Ice3dAppearance;
import org.xith3d.scenegraph.PolygonAttributes;
import org.xith3d.scenegraph.Appearance;
import javax.vecmath.Tuple3f;
import net.iceedge.vecmath.MathLibraryConversions;
import org.xith3d.scenegraph.TriangleArray;
import net.dirtt.utilities.Utility3D;
import org.xith3d.scenegraph.Shape3D;
import javax.media.j3d.Transform3D;
import net.iceedge.xith.TransformGroup;
import org.xith3d.scenegraph.Group;
import javax.vecmath.Vector3f;
import java.util.Collection;
import net.dirtt.icelib.ui.attribute.explorer.ui.entity.PossibleValue;
import javax.vecmath.Matrix4f;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import icd.warnings.WarningReason0260;
import icd.warnings.WarningReason0257;
import net.dirtt.icebox.IceBoxMainPanel;
import java.util.Iterator;
import net.dirtt.icelib.main.attributes.FloatAttribute;
import java.awt.geom.Line2D;
import java.awt.geom.GeneralPath;
import java.awt.Shape;
import net.dirtt.icebox.canvas2d.Ice2DNode;
import java.awt.Color;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.dirtt.icelib.main.EntityObject;
import java.util.List;
import net.dirtt.utilities.MathUtilities;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import org.apache.log4j.Logger;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import net.dirtt.icebox.canvas2d.Ice2DShapeNode;
import javax.vecmath.Point3f;
import java.util.ArrayList;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icebox.iceoutput.core.PlotPaintable;
import net.dirtt.icelib.main.TransformableEntity;

public abstract class ICDParametricCutout extends TransformableEntity implements PlotPaintable, ICDManufacturingReportable
{
    private static final long serialVersionUID = -1871237202709529879L;
    public static float ICD_PARAMETRIC_CUTOUT_3D_OFFSET;
    public static float ICD_CIRCULAR_CUTOUT_TO_CUTOUT_OFFSET;
    public static float ICD_RECTANGULAR_CUTOUT_TO_CUTOUT_OFFSET;
    public static float ICD_CIRCULAR_CUTOUT_TO_EDGE_OFFSET;
    public static final float ICD_PARAMETRIC_CUTOUT_CURVE_SUBDIVISION_TRESHOLD = 100.0f;
    protected ArrayList<Point3f> shape;
    protected Ice2DShapeNode shapeNode;
    protected int cutoutSnapPointIndex;
    protected boolean distanceModified;
    protected ArrayList<IceOutputNode> plotNodes;
    public static Logger logger;
    
    public ICDParametricCutout(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.shape = new ArrayList<Point3f>();
        this.shapeNode = null;
        this.cutoutSnapPointIndex = -1;
        this.distanceModified = false;
        this.plotNodes = new ArrayList<IceOutputNode>();
    }
    
    public ICDParametricCutout(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.shape = new ArrayList<Point3f>();
        this.shapeNode = null;
        this.cutoutSnapPointIndex = -1;
        this.distanceModified = false;
        this.plotNodes = new ArrayList<IceOutputNode>();
    }
    
    public ICDParametricCutout buildClone(final ICDParametricCutout icdParametricCutout) {
        super.buildClone((TransformableEntity)icdParametricCutout);
        return icdParametricCutout;
    }
    
    public void solve() {
        if (this.getParentEntity() instanceof Solution && this.getSolution().isMainSolution()) {
            this.destroy();
        }
        if (this.isModified()) {
            this.modifyCurrentOption();
            this.validateChildTypes();
        }
        this.calculateParameters();
        super.solve();
        final ICDParametricWorksurface parentWorksurface = this.getParentWorksurface();
        if (parentWorksurface != null && !MathUtilities.shapeContainsPoint((List)parentWorksurface.getShapeWS(), this.getBasePointWorldSpace())) {
            this.destroy();
        }
        if (this.distanceModified) {
            this.updateDistanceAttributes();
        }
    }
    
    protected abstract void calculateParameters();
    
    public void cutFromTree() {
        final ICDParametricWorksurface parentWorksurface = this.getParentWorksurface();
        if (parentWorksurface != null) {
            parentWorksurface.clearCachedModel();
            parentWorksurface.setModified(true);
        }
        super.cutFromTree();
    }
    
    public void cutFromTree2D() {
        if (this.shapeNode != null) {
            this.shapeNode.removeFromParent();
        }
        super.cutFromTree2D();
    }
    
    private ICDParametricWorksurface getParentWorksurface() {
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity != null && parentEntity instanceof ICDParametricWorksurface) {
            return (ICDParametricWorksurface)parentEntity;
        }
        return null;
    }
    
    public void destroy2D() {
        if (this.shapeNode != null) {
            this.shapeNode.removeFromParent();
        }
        super.destroy2D();
    }
    
    public void draw2D(final int notDirty, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        this.drawChildren2D(notDirty, ice2DContainer, solutionSetting);
        if (this.isDirty(notDirty)) {
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
            if (this.shapeNode.getParent() == null) {
                ice2DContainer.add((Ice2DNode)this.shapeNode);
            }
            this.drawBoundingCubeNode(ice2DContainer);
            this.drawNamedPointsDebug(ice2DContainer);
            this.drawWarning(ice2DContainer);
            this.setNotDirty(notDirty);
        }
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
    
    protected void calculateDimensionsCenterPoint() {
        float currentValue = 1.0f;
        float currentValue2 = 1.0f;
        float currentValue3 = 1.0f;
        if (this.getAttributeObject("XDimension") != null) {
            currentValue = ((FloatAttribute)this.getAttributeObject("XDimension")).getCurrentValue();
        }
        if (this.getAttributeObject("YDimension") != null) {
            currentValue2 = ((FloatAttribute)this.getAttributeObject("YDimension")).getCurrentValue();
        }
        if (this.getAttributeObject("YDimension") != null) {
            currentValue3 = ((FloatAttribute)this.getAttributeObject("YDimension")).getCurrentValue();
        }
        this.setXDimension(currentValue);
        this.setYDimension(currentValue2);
        this.setZDimension(currentValue3);
        this.setGeometricCenterPointLocal(new Point3f());
    }
    
    public Ice2DNode getIce2DNode() {
        return (Ice2DNode)this.shapeNode;
    }
    
    public void addToSolution(final Solution solution) {
        boolean b = true;
        this.setCutoutSnapPointIndex(-1);
        final Iterator breadthFirstEnumerationIterator = solution.getBreadthFirstEnumerationIterator();
        while (breadthFirstEnumerationIterator.hasNext()) {
            final EntityObject entityObject = (EntityObject)breadthFirstEnumerationIterator.next();
            if (entityObject instanceof ICDParametricWorksurface) {
                final ICDParametricWorksurface icdParametricWorksurface = (ICDParametricWorksurface)entityObject;
                if (icdParametricWorksurface.getShapeWS() != null && icdParametricWorksurface.getShapeWS().size() > 0 && MathUtilities.shapeContainsPoint((List)icdParametricWorksurface.getShapeWS(), this.getBasePointWorldSpace())) {
                    icdParametricWorksurface.clearCachedModel();
                    this.setDistanceModified();
                    icdParametricWorksurface.addCutout(this);
                    b = false;
                    break;
                }
                continue;
            }
        }
        if (b) {
            super.addToSolution(solution);
        }
    }
    
    public int getCutoutSnapPointIndex() {
        return this.cutoutSnapPointIndex;
    }
    
    public void setCutoutSnapPointIndex(final int cutoutSnapPointIndex) {
        this.cutoutSnapPointIndex = cutoutSnapPointIndex;
    }
    
    public void validateCutout() {
        final ICDParametricWorksurface parentWorksurface = this.getParentWorksurface();
        if (parentWorksurface != null && IceBoxMainPanel.warningUndefinedState) {
            if (this.interferesWithCutouts(parentWorksurface)) {
                WarningReason0257.addRequiredWarning(this);
            }
            if (this.interferesWithWKS(parentWorksurface)) {
                WarningReason0260.addRequiredWarning(this);
            }
        }
    }
    
    public boolean interferesWithCutouts(final ICDParametricWorksurface icdParametricWorksurface) {
        final float icd_CIRCULAR_CUTOUT_TO_CUTOUT_OFFSET = ICDParametricCutout.ICD_CIRCULAR_CUTOUT_TO_CUTOUT_OFFSET;
        for (final ICDParametricCutout icdParametricCutout : icdParametricWorksurface.getCutouts()) {
            if (icdParametricCutout != this && getShapeDistance(this.getEntWorldSpaceMatrix(), this.getShape(), icdParametricCutout.getEntWorldSpaceMatrix(), icdParametricCutout.getShape()) < icd_CIRCULAR_CUTOUT_TO_CUTOUT_OFFSET) {
                return true;
            }
        }
        return false;
    }
    
    public boolean interferesWithSideEdges(final ICDParametricWorksurface icdParametricWorksurface) {
        final float currentValue = ((FloatAttribute)this.getAttributeObject("ICD_Cutout_Side_Edge_Offset")).getCurrentValue();
        for (final LineParameter lineParameter : icdParametricWorksurface.getSideLineParameters()) {
            if (MathUtilities.getDistanceFromLine(lineParameter.getStartPoint(), lineParameter.getEndPoint(), this.getBasePoint3f(), true) < currentValue) {
                return true;
            }
        }
        return false;
    }
    
    public boolean interferesWithBackEdges(final ICDParametricWorksurface icdParametricWorksurface) {
        final float currentValue = ((FloatAttribute)this.getAttributeObject("ICD_Cutout_Back_Edge_Offset")).getCurrentValue();
        for (final LineParameter lineParameter : icdParametricWorksurface.getLineParameters()) {
            if (MathUtilities.getDistanceFromLine(lineParameter.getStartPoint(), lineParameter.getEndPoint(), this.getBasePoint3f(), true) < currentValue) {
                return true;
            }
        }
        return false;
    }
    
    public boolean interferesWithWKS(final ICDParametricWorksurface icdParametricWorksurface) {
        final float icd_CIRCULAR_CUTOUT_TO_EDGE_OFFSET = ICDParametricCutout.ICD_CIRCULAR_CUTOUT_TO_EDGE_OFFSET;
        final float shapeDistance = getShapeDistance(this.getEntWorldSpaceMatrix(), this.getShape(), icdParametricWorksurface.getEntWorldSpaceMatrix(), icdParametricWorksurface.getShape());
        if (shapeDistance < icd_CIRCULAR_CUTOUT_TO_EDGE_OFFSET) {
            ICDParametricCutout.logger.error((Object)("Interference distance " + shapeDistance));
            return true;
        }
        return false;
    }
    
    public void handleWarnings() {
        super.handleWarnings();
        this.validateCutout();
    }
    
    public ArrayList<Point3f> getShape() {
        return this.shape;
    }
    
    public static float getShapeDistance(final Matrix4f matrix4f, final ArrayList<Point3f> list, final Matrix4f matrix4f2, final ArrayList<Point3f> list2) {
        float n = Float.MAX_VALUE;
        final Iterator<Point3f> iterator = list.iterator();
        while (iterator.hasNext()) {
            final Point3f point3f = new Point3f((Point3f)iterator.next());
            matrix4f.transform(point3f);
            Point3f point3f2 = new Point3f((Point3f)list2.get(list2.size() - 1));
            Point3f point3f3 = new Point3f((Point3f)list2.get(0));
            matrix4f2.transform(point3f2);
            matrix4f2.transform(point3f3);
            for (int i = 1; i < list2.size(); ++i) {
                final float calculateDistanceFromPointToLine = MathUtilities.calculateDistanceFromPointToLine(point3f, point3f2, point3f3);
                if (calculateDistanceFromPointToLine < n) {
                    n = calculateDistanceFromPointToLine;
                }
                point3f2 = point3f3;
                point3f3 = new Point3f((Point3f)list2.get(i));
                matrix4f2.transform(point3f3);
            }
        }
        return n;
    }
    
    public void setDistanceModified() {
        this.distanceModified = true;
    }
    
    public void updateDistanceAttributes() {
        final ICDParametricWorksurface parentWorksurface = this.getParentWorksurface();
        if (parentWorksurface != null) {
            final float abs = Math.abs(parentWorksurface.getCutoutRefPoint().x - this.getBasePoint3f().x);
            final float abs2 = Math.abs(parentWorksurface.getCutoutRefPoint().y - this.getBasePoint3f().y);
            ((FloatAttribute)this.getAttributeObject("ICD_Parametric_Cutout_Distance_X")).setCurrentValue(abs);
            ((FloatAttribute)this.getAttributeObject("ICD_Parametric_Cutout_Distance_Y")).setCurrentValue(abs2);
        }
    }
    
    public void applyChangesFromEditor(final String s, final PossibleValue possibleValue, final Collection<PossibleValue> collection, final Collection<String> collection2, final String s2) {
        super.applyChangesFromEditor(s, possibleValue, (Collection)collection, (Collection)collection2, s2);
        if (s.equalsIgnoreCase("ICD_Parametric_Cutout_Distance_X") || s.equalsIgnoreCase("ICD_Parametric_Cutout_Distance_Y")) {
            this.updateCutoutLocationFromAttributes();
        }
        final ICDParametricWorksurface parentWorksurface = this.getParentWorksurface();
        if (parentWorksurface != null) {
            if (this.getAttributeValueAsBoolean("isUsePredefinedSnap", false)) {
                parentWorksurface.setModified(true);
                parentWorksurface.solve();
                this.updateDistanceAttributes();
            }
            parentWorksurface.setModified(true);
        }
    }
    
    public void updateCutoutLocationFromAttributes() {
        final ICDParametricWorksurface parentWorksurface = this.getParentWorksurface();
        if (parentWorksurface != null) {
            final Point3f cutoutRefPoint = parentWorksurface.getCutoutRefPoint();
            final Vector3f vector3f = (Vector3f)parentWorksurface.getCutoutXDirection().clone();
            final Vector3f vector3f2 = (Vector3f)parentWorksurface.getCutoutYDirection().clone();
            vector3f.normalize();
            vector3f2.normalize();
            this.setBasePoint(ICDParametricWorksurface.pointAt(ICDParametricWorksurface.pointAt(cutoutRefPoint, vector3f, ((FloatAttribute)this.getAttributeObject("ICD_Parametric_Cutout_Distance_X")).getCurrentValue()), vector3f2, ((FloatAttribute)this.getAttributeObject("ICD_Parametric_Cutout_Distance_Y")).getCurrentValue()));
            parentWorksurface.setModified(true);
        }
    }
    
    public Group get3DModel() {
        final Group group = new Group();
        final TransformGroup transformGroup = new TransformGroup();
        final Transform3D transform = new Transform3D();
        transform.setIdentity();
        transform.setTranslation(new Vector3f(0.0f, 0.0f, ICDParametricCutout.ICD_PARAMETRIC_CUTOUT_3D_OFFSET));
        transformGroup.setTransform(transform);
        final Shape3D shape3D = new Shape3D();
        final Point3f[] triangulateCoords = Utility3D.triangulateCoords(Utility3D.toPoint3fArray((ArrayList)this.shape));
        Utility3D.flipTris(triangulateCoords);
        final TriangleArray geometry = new TriangleArray(triangulateCoords.length);
        geometry.setCoordinates(0, (org.openmali.vecmath2.Tuple3f[])MathLibraryConversions.toXithArrayPoint3f((Tuple3f[])triangulateCoords));
        final Appearance appearance = new Appearance();
        appearance.setPolygonAttributes(Ice3dAppearance.getNewOrExistingPolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_BACK, 0.0f));
        shape3D.setGeometry((Geometry)geometry);
        shape3D.setAppearance(appearance);
        final Shape3D shape3D2 = new Shape3D();
        final Point3f[] point3fArray = Utility3D.toPoint3fArray(Utility3D.extrudeShape((ArrayList)this.shape, -1.25f));
        final TriangleArray geometry2 = new TriangleArray(point3fArray.length);
        geometry2.setCoordinates(0, (org.openmali.vecmath2.Tuple3f[])MathLibraryConversions.toXithArrayPoint3f((Tuple3f[])point3fArray));
        final Appearance appearance2 = new Appearance();
        appearance2.setPolygonAttributes(Ice3dAppearance.getNewOrExistingPolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_BACK, 0.0f));
        shape3D2.setGeometry((Geometry)geometry2);
        shape3D2.setAppearance(appearance2);
        transformGroup.addChild((Node)shape3D);
        transformGroup.addChild((Node)shape3D2);
        group.addChild((Node)transformGroup);
        return null;
    }
    
    public ArrayList<Point3f> getShapePS() {
        final ICDParametricWorksurface parentWorksurface = this.getParentWorksurface();
        final ArrayList<Point3f> list = new ArrayList<Point3f>();
        if (parentWorksurface != null) {
            final Iterator<Point3f> iterator = this.shape.iterator();
            while (iterator.hasNext()) {
                list.add(MathUtilities.convertSpaces(new Point3f((Point3f)iterator.next()), (EntityObject)this, (EntityObject)parentWorksurface));
            }
        }
        return list;
    }
    
    public boolean usePlotGVT() {
        return false;
    }
    
    public boolean isPrintable() {
        return false;
    }
    
    public List<IceOutputNode> getPlotOutputNodes() {
        return this.plotNodes;
    }
    
    public String getAttributeExplorerCategory() {
        return "Parametric Cutout";
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addDimensionsToManufacturingTreeMap(treeMap, this);
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
        ICDParametricCutout.ICD_PARAMETRIC_CUTOUT_3D_OFFSET = 0.1f;
        ICDParametricCutout.ICD_CIRCULAR_CUTOUT_TO_CUTOUT_OFFSET = 2.0f;
        ICDParametricCutout.ICD_RECTANGULAR_CUTOUT_TO_CUTOUT_OFFSET = 4.0f;
        ICDParametricCutout.ICD_CIRCULAR_CUTOUT_TO_EDGE_OFFSET = 0.9f;
        ICDParametricCutout.logger = Logger.getLogger(ICDParametricCutout.class);
    }
}
