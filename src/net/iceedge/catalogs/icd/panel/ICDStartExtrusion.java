// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import icd.warnings.WarningReason0282;
import net.iceedge.icecore.basemodule.interfaces.lightweight.Paintable;
import net.dirtt.icebox.iceoutput.core.IceOutputTextNode;
import net.dirtt.icebox.iceoutput.core.IceOutputShapeNode;
import javax.vecmath.Tuple3f;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationUtilities;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import java.util.List;
import net.dirtt.icebox.canvas2d.Ice2DTextNode;
import net.dirtt.utilities.MathUtilities;
import javax.vecmath.Vector3f;
import java.awt.Color;
import java.awt.Shape;
import net.dirtt.icebox.canvas2d.Ice2DShapeNode;
import java.awt.geom.Rectangle2D;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import javax.vecmath.Matrix4f;
import net.dirtt.icelib.main.TransformableEntity;
import javax.vecmath.Point3f;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelInterface;
import net.dirtt.icelib.report.Report;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicSurroundingStickExtrusion;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import java.util.Vector;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import com.iceedge.icd.entities.extrusions.ICDExtrusionInterface;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicStartExtrusion;

public class ICDStartExtrusion extends BasicStartExtrusion implements ICDVerticalExtrusion, JointIntersectable, ICDExtrusionInterface, ICDManufacturingReportable
{
    private Vector<ICDSubInternalExtrusion> tubings;
    private Vector<ICDMiddleJoint> joints;
    
    public ICDStartExtrusion(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.tubings = new Vector<ICDSubInternalExtrusion>();
        this.joints = new Vector<ICDMiddleJoint>();
    }
    
    public Object clone() {
        return this.buildClone(new ICDStartExtrusion(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDStartExtrusion buildClone(final ICDStartExtrusion icdStartExtrusion) {
        super.buildClone((BasicStartExtrusion)icdStartExtrusion);
        return icdStartExtrusion;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDStartExtrusion(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDStartExtrusion buildFrameClone(final ICDStartExtrusion icdStartExtrusion, final EntityObject entityObject) {
        super.buildFrameClone((BasicSurroundingStickExtrusion)icdStartExtrusion, entityObject);
        return icdStartExtrusion;
    }
    
    public boolean usePlotGVT() {
        return false;
    }
    
    public boolean shouldAddChildrenToReport(final Report report) {
        return false;
    }
    
    public boolean useTransformablesDraw3D() {
        return true;
    }
    
    public boolean isQuotable() {
        return false;
    }
    
    public int getQuoteReportLevel() {
        return -1;
    }
    
    public void solve() {
        if (this.isModified()) {
            final PanelInterface parentPanel = this.getParentPanel();
            if (parentPanel instanceof ICDCurvedPanel) {
                ((ICDCurvedPanel)parentPanel).calculateChildTubingAndJoints(this);
            }
            else if (parentPanel instanceof ICDAngledPanel) {
                ((ICDAngledPanel)parentPanel).calculateChildTubingAndJoints(this);
            }
        }
        super.solve();
    }
    
    public void setupNamedPoints() {
        super.setupNamedPoints();
        this.addNamedPoint("topJointBP", new Point3f());
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        if (this.getParentCurvedPanel() != null) {
            final float n = 0.0f;
            final float n2 = this.getZDimension() + 0.5f;
            final Point3f namedPointLocal = this.getNamedPointLocal("topJointBP");
            if (namedPointLocal != null) {
                namedPointLocal.set(n, 0.0f, n2);
            }
        }
    }
    
    public PanelInterface getParentPanel() {
        for (EntityObject entityObject = this.getParentEntity(); entityObject != null; entityObject = entityObject.getParentEntity()) {
            if (entityObject instanceof PanelInterface) {
                return (PanelInterface)entityObject;
            }
        }
        return null;
    }
    
    public ICDCurvedPanel getParentCurvedPanel() {
        final PanelInterface parentPanel = this.getParentPanel();
        if (parentPanel != null && parentPanel instanceof ICDCurvedPanel) {
            return (ICDCurvedPanel)parentPanel;
        }
        return null;
    }
    
    public Vector<ICDSubInternalExtrusion> getTubings() {
        return this.tubings;
    }
    
    public Vector<ICDMiddleJoint> getJoints() {
        return this.joints;
    }
    
    public Vector<Ice2DPaintableNode> getAssemblyIcons(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        final Rectangle2D.Float float1 = new Rectangle2D.Float(0.0f, 0.0f, this.getZDimension(), this.getYDimension());
        final Matrix4f matrix4f2 = new Matrix4f();
        matrix4f2.setIdentity();
        matrix4f2.mul(this.getEntWorldSpaceMatrix());
        final Matrix4f matrix4f3 = new Matrix4f();
        matrix4f3.setIdentity();
        matrix4f3.rotY(-1.5707964f);
        matrix4f2.mul(matrix4f3);
        final Ice2DShapeNode e = new Ice2DShapeNode(this.getLayerName(), (TransformableEntity)this, matrix4f2, (Shape)float1);
        e.setFillColor(Color.lightGray);
        final Matrix4f matrix4f4 = new Matrix4f();
        matrix4f4.setIdentity();
        matrix4f4.mul(this.getEntWorldSpaceMatrix());
        final Matrix4f matrix4f5 = new Matrix4f();
        matrix4f5.setIdentity();
        matrix4f5.rotY(-1.5707964f);
        matrix4f4.mul(matrix4f5);
        final Matrix4f matrix4f6 = new Matrix4f();
        matrix4f6.setIdentity();
        matrix4f6.setTranslation(new Vector3f(this.getZDimension() / 2.0f, 0.0f, 0.0f));
        matrix4f4.mul(matrix4f6);
        final Ice2DTextNode e2 = new Ice2DTextNode(this.getLayerName(), (TransformableEntity)this, matrix4f4, MathUtilities.round(this.getZDimension(), 2) + "", 3);
        final Vector<Ice2DPaintableNode> vector = new Vector<Ice2DPaintableNode>();
        vector.add((Ice2DPaintableNode)e);
        vector.add((Ice2DPaintableNode)e2);
        return vector;
    }
    
    public List<IceOutputNode> getPlotOutputNodes(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        if (!ICDAssemblyElevationUtilities.shouldDrawAssemblyShapeNodes((TransformableEntity)this)) {
            return null;
        }
        final Rectangle2D.Float float1 = new Rectangle2D.Float(0.0f, 0.0f, this.getZDimension(), this.getYDimension());
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
        final IceOutputShapeNode e = new IceOutputShapeNode((Shape)float1, matrix4f2);
        final Point3f point3f2 = new Point3f(this.getZDimension() / 2.0f, 0.0f, 0.0f);
        if (n == 1) {
            final Matrix4f matrix4f7 = new Matrix4f();
            matrix4f7.setIdentity();
            matrix4f7.rotX(3.1415927f);
            matrix4f2.mul(matrix4f7);
            final Matrix4f matrix4f8 = new Matrix4f();
            matrix4f8.setIdentity();
            matrix4f8.rotZ(3.1415927f);
            matrix4f2.mul(matrix4f8);
            point3f2.x = -this.getZDimension() / 2.0f;
        }
        final IceOutputTextNode e2 = new IceOutputTextNode((Paintable)null, MathUtilities.round(this.getZDimension(), 2) + "", 3.0f, 1.0f, 0.0f, point3f2, matrix4f2);
        final Vector<IceOutputNode> vector = new Vector<IceOutputNode>();
        vector.add((IceOutputNode)e);
        vector.add((IceOutputNode)e2);
        return vector;
    }
    
    public void setBasePoint(final Point3f basePoint) {
        if (this.getParentCurvedPanel() != null) {
            basePoint.z -= 30.0f;
        }
        super.setBasePoint(basePoint);
    }
    
    public boolean doesParticipateInJointIntersection() {
        return this.isSlopePanel();
    }
    
    public boolean isSlopePanel() {
        final ICDPanel icdPanel = (ICDPanel)this.getParent((Class)ICDPanel.class);
        return icdPanel != null && icdPanel.isSlopedPanel();
    }
    
    protected void calculateGeometricCenter() {
        super.calculateGeometricCenter();
        if (this.isSlopePanel()) {
            this.setGeometricCenterPointLocal(new Point3f(0.0f, -this.getYDimension() / 2.0f, this.getZDimension() / 2.0f));
        }
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
