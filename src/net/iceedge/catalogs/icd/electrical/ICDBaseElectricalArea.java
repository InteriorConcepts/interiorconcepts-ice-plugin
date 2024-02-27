package net.iceedge.catalogs.icd.electrical;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.electrical.BasicElectricalBaseArea;

public class ICDBaseElectricalArea extends BasicElectricalBaseArea implements ICDManufacturingReportable
{
    public ICDBaseElectricalArea(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDBaseElectricalArea icdBaseElectricalArea) {
        super.buildClone((BasicElectricalBaseArea)icdBaseElectricalArea);
        return (TransformableEntity)icdBaseElectricalArea;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDBaseElectricalArea(this.getId(), this.getCurrentType(), this.getCurrentOption()), entityObject);
    }
    
    public TransformableEntity buildClone2(final ICDBaseElectricalArea icdBaseElectricalArea) {
        super.buildClone2((BasicElectricalBaseArea)icdBaseElectricalArea);
        return (TransformableEntity)icdBaseElectricalArea;
    }
    
    public Object clone() {
        return this.buildClone(new ICDBaseElectricalArea(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDBaseElectricalArea icdBaseElectricalArea, final EntityObject entityObject) {
        super.buildFrameClone((BasicElectricalBaseArea)icdBaseElectricalArea, entityObject);
        return (EntityObject)icdBaseElectricalArea;
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
