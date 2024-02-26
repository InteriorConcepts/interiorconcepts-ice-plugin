// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icelib.report.Report;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReport;
import java.util.TreeMap;
import net.iceedge.catalogs.icd.elevation.isometric.ICDIsometricAssemblyElevationEntity;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationEntity;
import net.dirtt.icelib.main.ElevationEntity;
import java.util.Enumeration;
import net.iceedge.icecore.basemodule.baseclasses.material.BasicMaterialEntity;
import java.util.Vector;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicTile;
import net.iceedge.catalogs.icd.constants.ICDTypeNameConstants;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelInterface;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.catalogs.icd.interfaces.ICDInstallTagDrawable;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicSubTile;

public class ICDSubTile extends BasicSubTile implements ICDInstallTagDrawable, ICDManufacturingReportable
{
    public ICDSubTile(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public Object clone() {
        return this.buildClone(new BasicSubTile(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDSubTile buildClone(final ICDSubTile icdSubTile) {
        super.buildClone((BasicSubTile)icdSubTile);
        return icdSubTile;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDSubTile(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDSubTile buildFrameClone(final ICDSubTile icdSubTile, final EntityObject entityObject) {
        super.buildFrameClone((BasicSubTile)icdSubTile, entityObject);
        return icdSubTile;
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
    
    public void setSelected(final boolean b) {
        super.setSelected(b, this.getSolution());
    }
    
    protected boolean shouldUseNewFinishSystem() {
        return true;
    }
    
    protected void calculateNamedScales() {
        super.calculateNamedScales();
        this.getNamedScaleLocal("ASE_scale").set(this.getXDimension(), this.getYDimension(), 0.9f);
    }
    
    protected void setupNamedPoints() {
        super.setupNamedPoints();
        this.addNamedPoint("ASE_pos", new Point3f(0.0f, 1.0f, 0.0f));
        this.addNamedPoint("ASE_pos_RH", new Point3f(0.5f, 0.5f, 0.0f));
        this.addNamedPoint("ASE_DOUBLE_VAL_DOOR", new Point3f(0.0f, 1.0f, 0.0f));
    }
    
    public boolean isStackedTile() {
        final PanelInterface parentPanel = this.getParentPanel();
        return parentPanel != null && !parentPanel.isCorePanel();
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        final float n = 0.5f + this.getWidth() / 100.0f;
        if (this.isStackedTile()) {
            this.getNamedPointLocal("ASE_pos").set(n, 0.0f, 0.0f);
        }
        else if (this.getCurrentType().getId().equals(ICDTypeNameConstants.ICD_Tile_Sliding_Door_RH)) {
            this.getNamedPointLocal("ASE_pos").set(-0.5f, 0.625f, 0.0f);
        }
        else {
            this.getNamedPointLocal("ASE_pos").set(0.0f, 0.625f, 0.0f);
        }
        this.getNamedPointLocal("ASE_DOUBLE_VAL_DOOR").set(0.05f + this.getWidth() / 100.0f, 0.25f, 0.0f);
    }
    
    protected void calculateDimensions() {
        super.calculateDimensions();
        final BasicTile parentTile = this.getParentTile();
        if (parentTile instanceof ICDTile && (((ICDTile)parentTile).isChaseMasterTile() || ((ICDTile)parentTile).isSlidingDoor())) {
            this.setXDimension(this.getXDimension() + 0.5f);
        }
    }
    
    public Vector<BasicMaterialEntity> getAllBasicMaterialFinishes() {
        final Vector<BasicMaterialEntity> vector = new Vector<BasicMaterialEntity>();
        final Enumeration breadthFirstEnumeration = this.breadthFirstEnumeration();
        while (breadthFirstEnumeration.hasMoreElements()) {
            final EntityObject entityObject = breadthFirstEnumeration.nextElement();
            if (entityObject instanceof BasicMaterialEntity && entityObject instanceof BasicMaterialEntity) {
                vector.add((BasicMaterialEntity)entityObject);
            }
        }
        return vector;
    }
    
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        final Vector<String> vector = new Vector<String>();
        if (elevationEntity instanceof ICDAssemblyElevationEntity || elevationEntity instanceof ICDIsometricAssemblyElevationEntity) {
            this.addMultiTagToScript((Vector)vector);
        }
        return vector;
    }
    
    public boolean isPrintable() {
        return false;
    }
    
    public void getManufacturingInfo(final TreeMap<String, String> treeMap) {
        super.getManufacturingInfo((TreeMap)treeMap);
        treeMap.put("TileIndicator", this.getInstallTag());
        if (this.getAttributeValueAsString("Valet_Door_Hand_Indicator") != null) {
            treeMap.put("Description", this.getDescription() + " " + this.getAttributeValueAsString("Valet_Door_Hand_Indicator"));
        }
    }
    
    public String getInstallTag() {
        String installTag = "";
        final Solution solution = this.getSolution();
        if (solution != null) {
            final Report report = solution.getReport(51);
            if (report != null) {
                installTag = ((ICDManufacturingReport)report).getInstallTag((EntityObject)this);
            }
        }
        return installTag;
    }
    
    public String getMultiTags(final SolutionSetting solutionSetting) {
        String str = super.getMultiTags(solutionSetting);
        if (solutionSetting.isShowICDTileInstallationTag()) {
            str = str + "\n" + this.getInstallTag();
        }
        return str;
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
