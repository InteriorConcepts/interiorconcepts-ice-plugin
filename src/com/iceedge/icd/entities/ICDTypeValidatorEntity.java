package com.iceedge.icd.entities;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icelib.main.TypeValidatorEntity;

public class ICDTypeValidatorEntity extends TypeValidatorEntity implements ICDManufacturingReportable
{
    private static final long serialVersionUID = 1L;
    
    public ICDTypeValidatorEntity(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public ICDTypeValidatorEntity buildClone(final ICDTypeValidatorEntity icdTypeValidatorEntity) {
        super.buildClone((TypeValidatorEntity)icdTypeValidatorEntity);
        return icdTypeValidatorEntity;
    }
    
    public Object clone() {
        return this.buildClone(new ICDTypeValidatorEntity(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDTypeValidatorEntity(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public EntityObject buildFrameClone(final ICDTypeValidatorEntity icdTypeValidatorEntity, final EntityObject entityObject) {
        return super.buildFrameClone((TypeValidatorEntity)icdTypeValidatorEntity, entityObject);
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
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
