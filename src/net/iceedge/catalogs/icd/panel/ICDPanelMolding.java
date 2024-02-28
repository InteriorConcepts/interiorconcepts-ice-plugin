package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.icecore.basemodule.interfaces.panels.TileInterface;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;

public class ICDPanelMolding extends TransformableTriggerUser implements ICDManufacturingReportable
{
    public ICDPanelMolding(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDPanelMolding(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDPanelMolding buildClone(final ICDPanelMolding icdPanelMolding) {
        super.buildClone((TransformableTriggerUser)icdPanelMolding);
        return icdPanelMolding;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDPanelMolding(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDPanelMolding buildFrameClone(final ICDPanelMolding icdPanelMolding, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdPanelMolding, entityObject);
        return icdPanelMolding;
    }
    
    public void modifyCurrentOption() {
        super.modifyCurrentOption();
    }
    
    private boolean isHorizontal() {
        return this.getLwTypeCreatedFrom().equals(Solution.lwTypeObjectByName("ICD_BottomPanelMolding_Type")) || this.getLwTypeCreatedFrom().equals(Solution.lwTypeObjectByName("ICD_TopPanelMolding_Type"));
    }
    
    private boolean isVertical() {
        return !this.isHorizontal();
    }
    
    protected void calculateDimensions() {
        super.calculateDimensions();
        final TileInterface tileInterface = (TileInterface)this.getParent(TileInterface.class);
        if (tileInterface != null) {
            if (this.isHorizontal()) {
                this.setZDimension(tileInterface.getXDimension());
            }
            else {
                this.setZDimension(tileInterface.getYDimension());
            }
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
}
