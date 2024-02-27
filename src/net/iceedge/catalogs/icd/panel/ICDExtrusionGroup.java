package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicExtrusionGroup;

public class ICDExtrusionGroup extends BasicExtrusionGroup implements ICDManufacturingReportable
{
    public ICDExtrusionGroup(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDExtrusionGroup(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDExtrusionGroup buildClone(final ICDExtrusionGroup icdExtrusionGroup) {
        super.buildClone((BasicExtrusionGroup)icdExtrusionGroup);
        return icdExtrusionGroup;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDExtrusionGroup(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDExtrusionGroup buildFrameClone(final ICDExtrusionGroup icdExtrusionGroup, final EntityObject entityObject) {
        super.buildFrameClone((BasicExtrusionGroup)icdExtrusionGroup, entityObject);
        return icdExtrusionGroup;
    }
    
    public float getExtrusionXTolerance() {
        return 0.0f;
    }
    
    public float getExtrusionYTolerance() {
        return 0.0f;
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
