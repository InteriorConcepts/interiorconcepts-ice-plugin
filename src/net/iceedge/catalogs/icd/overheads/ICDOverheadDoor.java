package net.iceedge.catalogs.icd.overheads;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;

public class ICDOverheadDoor extends TransformableTriggerUser implements ICDManufacturingReportable
{
    public ICDOverheadDoor(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDOverheadDoor icdOverheadDoor) {
        super.buildClone((TransformableTriggerUser)icdOverheadDoor);
        return (TransformableEntity)icdOverheadDoor;
    }
    
    public TransformableEntity buildClone2(final ICDOverheadDoor icdOverheadDoor) {
        super.buildClone2((TransformableTriggerUser)icdOverheadDoor);
        return (TransformableEntity)icdOverheadDoor;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDOverheadDoor(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDOverheadDoor(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDOverheadDoor icdOverheadDoor, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdOverheadDoor, entityObject);
        return (EntityObject)icdOverheadDoor;
    }
    
    public void modifyCurrentOption() {
        this.validateIndicators();
        super.modifyCurrentOption();
    }
    
    protected void validateIndicators() {
        if (this.getParentEntity() != null) {
            final String attributeValueAsString = this.getParentEntity().getAttributeValueAsString("ICD_Overhead_Width");
            if (attributeValueAsString != null) {
                this.applyChangesForAttribute("ICD_Overhead_Width", attributeValueAsString);
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
