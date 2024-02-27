package net.iceedge.catalogs.icd.elevation.isometric;

import java.util.Iterator;
import net.dirtt.icelib.main.ElevationEntity;
import net.iceedge.icecore.icecad.ice.IceCadIceApp;
import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import javax.swing.tree.MutableTreeNode;
import net.dirtt.icelib.main.Solution;
import net.iceedge.icecore.icecad.ice.tree.IceCadBlock;
import net.iceedge.icecore.icecad.ice.tree.IceCadSymbolTableRecord;
import net.iceedge.icecore.icecad.ice.tree.IceCadBlockRef3DNode;
import net.iceedge.icecore.icecad.ice.tree.IceCadBlockRefNode;
import net.iceedge.icecore.icecad.ice.tree.IceCadLayer;
import net.iceedge.icecore.icecad.ice.tree.IceCadCompositeBlock;
import net.iceedge.icecore.icecad.ice.tree.IceCadNodeContainer;
import net.dirtt.icecad.cadtree.ICadTreeNode;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.IceDimensionInterface;
import java.util.Collection;
import javax.vecmath.Matrix4f;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import javax.vecmath.Vector3f;
import net.dirtt.icelib.main.OptionObject;
import com.iceedge.icebox.icecore.entity.IceEntity;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icelib.main.ElevationClusterEntity;

public class ICDIsometricAssemblyElevationEntity extends ElevationClusterEntity implements ICDManufacturingReportable
{
    public ICDIsometricAssemblyElevationEntity(final String s, final TypeObject typeObject, final IceEntity iceEntity, final boolean b) {
        super(s, typeObject, iceEntity, b);
    }
    
    public ICDIsometricAssemblyElevationEntity(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public Vector3f getCADElevation3DRotation() {
        return new Vector3f(-(float)Math.toRadians(35.0), (float)Math.toRadians(45.0), (float)Math.toRadians(45.0));
    }
    
    protected void calculateDimensions() {
        super.calculateDimensions();
        this.setXDimension(48.0f);
        this.setYDimension(48.0f);
        this.setZDimension(1.0f);
    }
    
    public void drawDimensions(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting, final Matrix4f matrix4f) {
    }
    
    public void drawDimensions(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting, final Collection<IceDimensionInterface> collection, final EntityObject entityObject) {
    }
    
    public void updateDimensions() {
    }
    
    public void createElevationMarker(final Ice2DContainer ice2DContainer, final TransformableEntity transformableEntity) {
    }
    
    public float getClusterPlaneWorldRotation() {
        return 0.0f;
    }
    
    protected String getMarkerLabelText() {
        return "";
    }
    
    protected void addCadElevationMarker(final ICadTreeNode cadTreeNode) {
    }
    
    public void drawElevationBounds() {
    }
    
    public boolean shouldDraw2D() {
        return false;
    }
    
    protected IceCadBlockRefNode createIceCadEleationBlockRefNode(final IceCadNodeContainer iceCadNodeContainer, final IceCadCompositeBlock iceCadCompositeBlock, final IceCadLayer iceCadLayer) {
        return (IceCadBlockRefNode)new IceCadBlockRef3DNode(iceCadNodeContainer, (IceCadSymbolTableRecord)iceCadLayer, (IceCadBlock)iceCadCompositeBlock, this.getBasePoint3f(), 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    protected boolean shouldDrawElevationBounds() {
        return false;
    }
    
    protected void drawPlanLabel(final IceCadNodeContainer iceCadNodeContainer, final IceCadLayer iceCadLayer) {
    }
    
    public boolean shouldConvertPointsToElevationParentSpace() {
        return false;
    }
    
    public void addToSolution(final Solution solution) {
        super.addToSolution(solution);
        if (solution.isMainSolution) {
            solution.getElevationClusterGroup(true).add((MutableTreeNode)this);
        }
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
    
    public void drawIceCadDotNet(final int n, final IceCadNodeContainer iceCadNodeContainer, final IceCadIceApp iceCadIceApp) {
        super.drawIceCadDotNet(n, iceCadNodeContainer, iceCadIceApp);
        this.getIceCadElevationBlockRef().setBlockRotation3D(this.getCADElevation3DRotation());
        for (final IceEntity iceEntity : this.getReferencedParents()) {
            if (iceEntity instanceof EntityObject) {
                ((EntityObject)iceEntity).drawIceCadElevationDotNet((ElevationEntity)this, iceCadIceApp, iceCadNodeContainer, (IceCadBlock)this.getIceCadElevationBlock(), n);
            }
        }
    }
}
