package net.iceedge.catalogs.icd.electrical;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;

public class ICDHarnessExtensionTubingJointer extends TransformableEntity implements ICDManufacturingReportable
{
    public ICDHarnessExtensionTubingJointer(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public ICDHarnessExtensionTubingJointer buildClone(final ICDHarnessExtensionTubingJointer icdHarnessExtensionTubingJointer) {
        super.buildClone((TransformableEntity)icdHarnessExtensionTubingJointer);
        return icdHarnessExtensionTubingJointer;
    }
    
    public ICDHarnessExtensionTubingJointer buildClone2(final ICDHarnessExtensionTubingJointer icdHarnessExtensionTubingJointer) {
        super.buildClone2((TransformableEntity)icdHarnessExtensionTubingJointer);
        return icdHarnessExtensionTubingJointer;
    }
    
    public Object clone() {
        return this.buildClone(new ICDHarnessExtensionTubingJointer(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDHarnessExtensionTubingJointer(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public EntityObject buildFrameClone(final ICDHarnessExtensionTubingJointer icdHarnessExtensionTubingJointer, final EntityObject entityObject) {
        super.buildFrameClone((TransformableEntity)icdHarnessExtensionTubingJointer, entityObject);
        return (EntityObject)icdHarnessExtensionTubingJointer;
    }
    
    public void solve() {
        if (this.isModified()) {
            this.modifyCurrentOption();
            this.validateChildTypes();
        }
        super.solve();
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
