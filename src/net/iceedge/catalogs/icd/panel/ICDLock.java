// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icecad.cadtree.ICadBlockNode;
import net.dirtt.icelib.main.ElevationEntity;
import net.iceedge.catalogs.icd.ICDUtilities;
import javax.vecmath.Tuple3f;
import javax.vecmath.Point3f;
import net.dirtt.utilities.TypeFilter;
import net.iceedge.catalogs.icd.ICDTileFilter;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;

public class ICDLock extends TransformableEntity implements ICDManufacturingReportable
{
    private static final float singleDoorHandleOffset = 3.0f;
    private static final float doubleDoorHandleOffset = 2.0f;
    private static final float tileEdgeOffset = 1.44f;
    
    public ICDLock(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDLock(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDLock buildClone(final ICDLock icdLock) {
        super.buildClone((TransformableEntity)icdLock);
        return icdLock;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDLock(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDLock buildFrameClone(final ICDLock icdLock, final EntityObject entityObject) {
        super.buildFrameClone((TransformableEntity)icdLock, entityObject);
        return icdLock;
    }
    
    protected void calculateNamedPoints() {
        final ICDTile icdTile = (ICDTile)this.getParent((TypeFilter)new ICDTileFilter());
        if (icdTile != null) {
            this.setBasePoint(this.getLockPosition(icdTile));
        }
        super.calculateNamedPoints();
    }
    
    private Point3f getLockPosition(final ICDTile icdTile) {
        final boolean b = !icdTile.isSingleValetDoor();
        final float n = b ? 3.0f : 2.0f;
        final boolean rightHanded = icdTile.isRightHanded();
        final ICDValetHandle icdValetHandle = (ICDValetHandle)icdTile.getChildByClass((Class)ICDValetHandle.class);
        final float n2 = icdValetHandle.getBasePoint().getY() + icdValetHandle.getZDimension() / 2.0f;
        final Point3f point3f = new Point3f();
        if (b) {
            point3f.set((Tuple3f)icdTile.getNamedPointLocal("VHDR"));
            point3f.y = n2 + n;
            point3f.x = 1.44f;
        }
        else {
            point3f.set(rightHanded ? (icdValetHandle.getXDimension() + 1.44f) : (icdTile.getXDimension() - 1.44f), n2 + n + 1.44f, 0.0f);
        }
        return point3f;
    }
    
    public boolean drawCAD() {
        return false;
    }
    
    public void solve() {
        super.solve();
        ICDUtilities.validateShowInManufacturingReport(this);
    }
    
    public void drawCadElevation(final ElevationEntity elevationEntity, final ICadBlockNode cadBlockNode, final int n, final SolutionSetting solutionSetting) {
    }
    
    protected boolean validateEntityParent() {
        boolean validateEntityParent = super.validateEntityParent();
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity == null || parentEntity instanceof Solution) {
            validateEntityParent = false;
        }
        return validateEntityParent;
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
}
