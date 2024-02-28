package net.iceedge.catalogs.icd.worksurfaces.parametric;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import icd.warnings.WarningReason0261;
import icd.warnings.WarningReason0262;
import net.dirtt.icelib.main.attributes.FloatAttribute;
import java.util.Collection;
import net.dirtt.icelib.ui.attribute.explorer.ui.entity.PossibleValue;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.CircleParameter;
import java.awt.geom.Line2D;
import java.awt.geom.GeneralPath;
import java.awt.Shape;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import javax.media.j3d.Transform3D;
import java.awt.Graphics2D;
import java.awt.Color;
import net.dirtt.icebox.canvas2d.Ice2DNode;
import java.util.ArrayList;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import java.util.Iterator;
import java.util.List;
import net.dirtt.utilities.MathUtilities;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icebox.canvas2d.Ice2DShapeNode;
import net.dirtt.icebox.canvas2d.Ice2DDelegateNode;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icebox.canvas2d.Ice2DDirectPaintable;
import net.dirtt.icelib.main.TransformableEntity;

public class ICDWireDip extends TransformableEntity implements Ice2DDirectPaintable, ICDManufacturingReportable
{
    public static float ICD_WIRE_DIP_WORKSURFACE_DISTANCE_THRESHOLD;
    protected Ice2DDelegateNode delegate2DNode;
    protected Ice2DShapeNode shapeNode;
    protected int snapPointIndex;
    protected Point3f refPoint;
    protected boolean distanceModified;
    protected Vector3f direction;
    protected Point3f circleParameterCenter;
    
    public ICDWireDip(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.delegate2DNode = null;
        this.shapeNode = null;
        this.snapPointIndex = -1;
        this.refPoint = null;
        this.distanceModified = false;
        this.direction = null;
        this.circleParameterCenter = new Point3f();
    }
    
    public ICDWireDip(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.delegate2DNode = null;
        this.shapeNode = null;
        this.snapPointIndex = -1;
        this.refPoint = null;
        this.distanceModified = false;
        this.direction = null;
        this.circleParameterCenter = new Point3f();
    }
    
    public Object clone() {
        return this.buildClone(new ICDWireDip(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWireDip buildClone(final ICDWireDip icdWireDip) {
        super.buildClone((TransformableEntity)icdWireDip);
        icdWireDip.circleParameterCenter = new Point3f(this.circleParameterCenter);
        return icdWireDip;
    }
    
    public void addToSolution(final Solution solution) {
        this.setDistanceModified();
        boolean b = true;
        this.setSnapPointIndex(-1);
        final Iterator breadthFirstEnumerationIterator = solution.getBreadthFirstEnumerationIterator();
        while (breadthFirstEnumerationIterator.hasNext()) {
            final Object entityObject = breadthFirstEnumerationIterator.next();
            if (entityObject instanceof ICDParametricWorksurface) {
                final ICDParametricWorksurface icdParametricWorksurface = (ICDParametricWorksurface)entityObject;
                if (icdParametricWorksurface.getShapeWS() != null && icdParametricWorksurface.getShapeWS().size() > 0 && MathUtilities.shapeContainsPoint((List)icdParametricWorksurface.getShapeWS(), this.getBasePointWorldSpace())) {
                    icdParametricWorksurface.addWireDip(this);
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
    
    public void cutFromTree() {
        final ICDParametricWorksurface parentWorksurface = this.getParentWorksurface();
        if (parentWorksurface != null) {
            parentWorksurface.removeWireDip(this);
        }
        super.cutFromTree();
    }
    
    public void cutFromTree2D() {
        if (this.shapeNode != null) {
            this.shapeNode.removeFromParent();
        }
        if (this.delegate2DNode != null) {
            this.delegate2DNode.removeFromParent();
        }
        super.cutFromTree2D();
    }
    
    public void draw2D(final int notDirty, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        if (this.isDirty(notDirty)) {
            if (this.delegate2DNode != null && this.delegate2DNode.getParent() != null) {
                this.delegate2DNode.removeFromParent();
            }
            final ArrayList<ICDWireDip> list = new ArrayList<ICDWireDip>();
            list.add(this);
            ice2DContainer.add((Ice2DNode)(this.delegate2DNode = new Ice2DDelegateNode((String)null, (TransformableEntity)this, this.getEntWorldSpaceMatrix(), (ArrayList)list)));
            this.drawChildren2D(notDirty, ice2DContainer, solutionSetting);
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
    
    public void destroy2D() {
        if (this.delegate2DNode != null) {
            this.delegate2DNode.removeFromParent();
        }
        if (this.shapeNode != null) {
            this.shapeNode.removeFromParent();
        }
        super.destroy2D();
    }
    
    public void paint(final Graphics2D graphics2D, final Transform3D transform3D, final Ice2DPaintableNode ice2DPaintableNode) {
        final Point3f point3f = new Point3f(this.circleParameterCenter);
        this.getEntWorldSpaceMatrix().transform(point3f);
        transform3D.transform(point3f);
        graphics2D.fillOval((int)point3f.x - 2, (int)point3f.y - 2, 4, 4);
    }
    
    public Shape getShape2D() {
        final Point3f point3f = new Point3f(-1.5f, 0.0f, 0.0f);
        final Point3f point3f2 = new Point3f(1.5f, 0.0f, 0.0f);
        final Point3f point3f3 = new Point3f(0.0f, 1.5f, 0.0f);
        final Point3f point3f4 = new Point3f(0.0f, -1.5f, 0.0f);
        final GeneralPath generalPath = new GeneralPath();
        generalPath.append(new Line2D.Float(point3f.x, point3f.y, point3f2.x, point3f2.y), false);
        generalPath.append(new Line2D.Float(point3f3.x, point3f3.y, point3f4.x, point3f4.y), false);
        return generalPath;
    }
    
    public Ice2DNode getIce2DNode() {
        return (Ice2DNode)this.shapeNode;
    }
    
    public void setCircleParameterLocation(final Point3f circleParameterCenter) {
        this.circleParameterCenter = circleParameterCenter;
    }
    
    public Point3f getCircleParameterCenterWS() {
        final Point3f point3f = new Point3f(this.circleParameterCenter);
        this.getEntWorldSpaceMatrix().transform(point3f);
        return point3f;
    }
    
    public Point3f getCircleParameterCenterPS() {
        return MathUtilities.convertSpaces(new Point3f(this.circleParameterCenter), (EntityObject)this, this.getParentEntity());
    }
    
    public CircleParameter getCircleParameter() {
        return new CircleParameter(this.getCircleParameterCenterPS(), 3.63f);
    }
    
    public int getSnapPointIndex() {
        return this.snapPointIndex;
    }
    
    public void setSnapPointIndex(final int snapPointIndex) {
        this.snapPointIndex = snapPointIndex;
    }
    
    public void applyChangesFromEditor(final String s, final PossibleValue possibleValue, final Collection<PossibleValue> collection, final Collection<String> collection2, final String s2) {
        if (s.equalsIgnoreCase("ICD_WireDip_Distance")) {
            final float float1 = Float.parseFloat(possibleValue.getValue());
            final ICDParametricWorksurface parentWorksurface = this.getParentWorksurface();
            if (parentWorksurface != null) {
                parentWorksurface.updateDipLocation(this, float1);
            }
        }
        super.applyChangesFromEditor(s, possibleValue, (Collection)collection, (Collection)collection2, s2);
    }
    
    public ICDParametricWorksurface getParentWorksurface() {
        if (this.getParentEntity() != null && this.getParentEntity() instanceof ICDParametricWorksurface) {
            return (ICDParametricWorksurface)this.getParentEntity();
        }
        return null;
    }
    
    public Point3f getRefPoint() {
        return this.refPoint;
    }
    
    public void setRefPoint(final Point3f refPoint) {
        this.refPoint = refPoint;
    }
    
    public void solve() {
        if (this.refPoint != null && this.distanceModified) {
            ((FloatAttribute)this.getAttributeObject("ICD_WireDip_Distance")).setCurrentValue(MathUtilities.calculate2DLength(this.refPoint, this.getBasePoint3f()));
        }
        super.solve();
    }
    
    public void setDistanceModified() {
        this.distanceModified = true;
    }
    
    public void handleWarnings() {
        WarningReason0262.addRequiredWarning(this);
        WarningReason0261.addRequiredWarning(this);
        super.handleWarnings();
    }
    
    public String getAttributeExplorerCategory() {
        return "Parametric Wire Dip";
    }
    
    public Vector3f getDirection() {
        return this.direction;
    }
    
    public void setDirection(final Vector3f direction) {
        (this.direction = direction).normalize();
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
        ICDWireDip.ICD_WIRE_DIP_WORKSURFACE_DISTANCE_THRESHOLD = 10.0f;
    }
}
