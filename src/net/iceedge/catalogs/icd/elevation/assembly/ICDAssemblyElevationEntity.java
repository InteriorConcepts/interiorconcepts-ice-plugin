// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.elevation.assembly;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.icecore.icecad.ice.tree.IceCadBlock;
import net.iceedge.icecore.icecad.ice.tree.IceCadSymbolTableRecord;
import net.iceedge.icecore.icecad.ice.tree.IceCadBlockRef3DNode;
import net.iceedge.icecore.icecad.ice.tree.IceCadBlockRefNode;
import net.iceedge.icecore.icecad.ice.tree.IceCadLayer;
import net.iceedge.icecore.icecad.ice.tree.IceCadCompositeBlock;
import net.iceedge.icecore.icecad.ice.tree.IceCadNodeContainer;
import javax.vecmath.Vector3f;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import net.dirtt.icelib.report.Report;
import net.dirtt.icelib.main.Solution;
import java.awt.Font;
import net.dirtt.icebox.canvas2d.Ice2DMultipleTextNode;
import java.awt.Color;
import javax.vecmath.Matrix3f;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportNode;
import javax.vecmath.Point3f;
import net.dirtt.IceApp;
import net.iceedge.catalogs.icd.ICDILine;
import net.iceedge.catalogs.icd.panel.ICDVerticalChase;
import java.util.ArrayList;
import net.dirtt.utilities.TypeFilter;
import java.util.Iterator;
import net.dirtt.icebox.canvas2d.Ice2DNode;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import java.util.Collection;
import net.dirtt.icelib.main.TransformableEntity;
import java.util.Vector;
import java.util.List;
import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix4f;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.dirtt.icebox.canvas2d.Render2D;
import net.dirtt.icelib.main.OptionObject;
import com.iceedge.icebox.icecore.entity.IceEntity;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icebox.canvas2d.Ice2DTextNode;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icelib.main.CustomElevationEntity;

public class ICDAssemblyElevationEntity extends CustomElevationEntity implements ICDManufacturingReportable
{
    private static final long serialVersionUID = 6968963799252465703L;
    private static final float TAG_FONT_SIZE_SCALE = 0.1875f;
    protected Ice2DTextNode elevationReportTag;
    
    public ICDAssemblyElevationEntity(final String s, final TypeObject typeObject, final IceEntity iceEntity, final boolean drawMarker) {
        this(s, typeObject, typeObject.getDefaultOption());
        this.drawMarker = drawMarker;
    }
    
    public ICDAssemblyElevationEntity(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.setupNamedPoints();
        this.setChildrenPainted(false);
        this.elevationCount = Render2D.elevationCount;
    }
    
    public ICDAssemblyElevationEntity buildClone(final ICDAssemblyElevationEntity icdAssemblyElevationEntity) {
        super.buildClone((CustomElevationEntity)icdAssemblyElevationEntity);
        icdAssemblyElevationEntity.setSide(this.getSide());
        icdAssemblyElevationEntity.drawMarker = this.drawMarker;
        icdAssemblyElevationEntity.elevationCount = this.elevationCount;
        return icdAssemblyElevationEntity;
    }
    
    public Object clone() {
        return this.buildClone(new ICDAssemblyElevationEntity(this.getId(), this.currentType, this.currentOption));
    }
    
    protected String getMarkerLabelText() {
        return "Assembly Elevation " + this.calcMarkerText();
    }
    
    public void drawDimensions(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting, final Matrix4f matrix4f) {
    }
    
    public boolean use2DLOD() {
        return false;
    }
    
    public boolean isTooSmall(final Transform3D transform3D) {
        return false;
    }
    
    public void startDrawingElevation(final int n, final SolutionSetting solutionSetting) {
        if (this.parentEntity instanceof AssemblyPaintableRoot && ((AssemblyPaintableRoot)this.parentEntity).checkAssembledForAssemblyElevation()) {
            this.gatherNodes((AssemblyPaintableRoot)this.parentEntity);
        }
    }
    
    private List<AssemblyPaintable> gatherNodes(final AssemblyPaintableRoot assemblyPaintableRoot) {
        final Vector<AssemblyPaintable> vector = new Vector<AssemblyPaintable>();
        assemblyPaintableRoot.addAdditonalPaintableEntities(vector);
        vector.addAll((Collection<?>)((TransformableEntity)assemblyPaintableRoot).getChildrenByClass((Class)AssemblyPaintable.class, true, true));
        for (int i = 0; i < vector.size(); ++i) {
            final AssemblyPaintable assemblyPaintable = vector.get(i);
            if (assemblyPaintable.shouldDrawAssembly() && assemblyPaintable.isAssembled()) {
                final Vector<Ice2DPaintableNode> assemblyIcons = assemblyPaintable.getAssemblyIcons(this.getSide(), this.getSideRefPoint(), this.parentEntity, this.getEntWorldSpaceMatrix());
                if (assemblyIcons != null) {
                    for (final Ice2DPaintableNode ice2DPaintableNode : assemblyIcons) {
                        if (ice2DPaintableNode != null) {
                            this.elevationNode.add((Ice2DNode)ice2DPaintableNode);
                        }
                    }
                }
            }
        }
        this.buildElevationPaintableReportTag(assemblyPaintableRoot);
        return vector;
    }
    
    public void draw2D(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        final AssemblyPaintableRoot assemblyPaintableRoot = (AssemblyPaintableRoot)this.getParent((TypeFilter)new AssemblyPaintableRootFilter());
        if (assemblyPaintableRoot != null && assemblyPaintableRoot.shouldPaintAssemblyInIce2D()) {
            super.draw2D(n, ice2DContainer, solutionSetting);
        }
    }
    
    public boolean isPlottable() {
        return this.shouldDraw2D();
    }
    
    public boolean shouldDraw2D() {
        final AssemblyPaintableRoot assemblyPaintableRoot = (AssemblyPaintableRoot)this.getParent((TypeFilter)new AssemblyPaintableRootFilter());
        return assemblyPaintableRoot != null && assemblyPaintableRoot.shouldPaintAssemblyInIce2D();
    }
    
    public List<IceEntity> getReferencedParents() {
        final ArrayList<ICDILine> list = (ArrayList<ICDILine>)new ArrayList<IceEntity>();
        if (this.parentEntity instanceof ICDVerticalChase) {
            list.add((IceEntity)((ICDVerticalChase)this.parentEntity).getChaseILines().get(0));
            return (List<IceEntity>)list;
        }
        return (List<IceEntity>)super.getReferencedParents();
    }
    
    protected void buildElevationPaintableReportTag(final AssemblyPaintableRoot assemblyPaintableRoot) {
        if (!IceApp.getCurrentSolution().getSolutionSetting().isShowICDPreassembledTag() || !assemblyPaintableRoot.shouldICDMakePreAssembledReport()) {
            return;
        }
        final Point3f point3f = new Point3f(this.getLength() / 2.0f, 0.0f, 0.0f);
        if (point3f != null) {
            String tag = "";
            final Solution solution = this.getSolution();
            if (solution != null) {
                final Report report = solution.getReport(51);
                if (report != null) {
                    final ICDManufacturingReportNode icdManufacturingReportNode = (ICDManufacturingReportNode)((EntityObject)assemblyPaintableRoot).getBucketInReport(report);
                    if (icdManufacturingReportNode != null) {
                        tag = icdManufacturingReportNode.getTag();
                    }
                }
            }
            if (tag != null) {
                final Point3f point3f2 = new Point3f(point3f.x, this.getHeight(), point3f.z);
                final Matrix4f matrix4f = (Matrix4f)((EntityObject)assemblyPaintableRoot).getEntWorldSpaceMatrix().clone();
                matrix4f.getRotationScale(new Matrix3f());
                final Matrix4f matrix4f2 = new Matrix4f();
                matrix4f2.setIdentity();
                matrix4f2.rotX(1.5707964f);
                matrix4f.mul(matrix4f2);
                final int tagFontSize = this.getSolution().getTagFontSize();
                final int length = tag.split("\n").length;
                (this.elevationReportTag = (Ice2DTextNode)new Ice2DMultipleTextNode("Dimensions", (TransformableEntity)this, matrix4f, tag, Color.blue, new Point3f(this.getXDimension() / 2.0f, this.getHeight(), 0.0f))).showWithWhiteBackground(true);
                this.elevationReportTag.setFont(new Font("Arial", 0, tagFontSize));
                this.elevationReportTag.setCentered(true);
                if (this.elevationReportTag.getParent() == null) {
                    this.elevationNode.add((Ice2DNode)this.elevationReportTag);
                }
            }
        }
    }
    
    public List<IceOutputNode> getPlotOutputNodes() {
        return new ArrayList<IceOutputNode>();
    }
    
    public Collection<Matrix4f> getElevationAdjustmentMatrixes() {
        final Collection elevationAdjustmentMatrixes = super.getElevationAdjustmentMatrixes();
        final Transform3D transform3D = new Transform3D();
        transform3D.rotZ((double)(EntityObject.HALF_PI / 2.0f));
        final Matrix4f matrix4f = new Matrix4f();
        transform3D.get(matrix4f);
        final Transform3D transform3D2 = new Transform3D();
        transform3D2.rotY((double)(EntityObject.HALF_PI / 4.0f));
        final Matrix4f matrix4f2 = new Matrix4f();
        transform3D2.get(matrix4f2);
        elevationAdjustmentMatrixes.add(matrix4f);
        elevationAdjustmentMatrixes.add(matrix4f2);
        return (Collection<Matrix4f>)elevationAdjustmentMatrixes;
    }
    
    public boolean shouldDrawCAD() {
        return true;
    }
    
    public Vector3f getCADElevation3DRotation() {
        return new Vector3f(-(float)Math.toRadians(35.0), (float)Math.toRadians(45.0), (float)Math.toRadians(45.0));
    }
    
    protected boolean shouldDrawElevationBounds() {
        return false;
    }
    
    protected boolean drawElevationDims() {
        return false;
    }
    
    public float getCADFontSizeScale() {
        return 0.1875f;
    }
    
    protected IceCadBlockRefNode createIceCadEleationBlockRefNode(final IceCadNodeContainer iceCadNodeContainer, final IceCadCompositeBlock iceCadCompositeBlock, final IceCadLayer iceCadLayer) {
        return (IceCadBlockRefNode)new IceCadBlockRef3DNode(iceCadNodeContainer, (IceCadSymbolTableRecord)iceCadLayer, (IceCadBlock)iceCadCompositeBlock, this.getBasePoint3f(), 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public boolean shouldConvertPointsToElevationParentSpace() {
        return false;
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
    
    public boolean shouldSubtractOffsetOfElevationParent() {
        return true;
    }
}
