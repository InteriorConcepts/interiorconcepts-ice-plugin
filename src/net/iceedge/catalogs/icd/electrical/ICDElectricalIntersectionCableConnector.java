package net.iceedge.catalogs.icd.electrical;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.utilities.TypeFilter;
import com.iceedge.icd.typefilters.ICDElectricalIntersectionFilter;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.interfaces.OptionOverrideEntity;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;

public class ICDElectricalIntersectionCableConnector extends TransformableTriggerUser implements OptionOverrideEntity, ICDManufacturingReportable
{
    private final String DEFUALT_INTERSECTION_CONNECTOR_LENGTH = "36";
    
    public ICDElectricalIntersectionCableConnector(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDElectricalIntersectionCableConnector icdElectricalIntersectionCableConnector) {
        super.buildClone((TransformableTriggerUser)icdElectricalIntersectionCableConnector);
        return (TransformableEntity)icdElectricalIntersectionCableConnector;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDElectricalIntersectionCableConnector(this.getId(), this.getCurrentType(), this.getCurrentOption()), entityObject);
    }
    
    public TransformableEntity buildClone2(final ICDElectricalIntersectionCableConnector icdElectricalIntersectionCableConnector) {
        super.buildClone2((TransformableTriggerUser)icdElectricalIntersectionCableConnector);
        return (TransformableEntity)icdElectricalIntersectionCableConnector;
    }
    
    public Object clone() {
        return this.buildClone(new ICDElectricalIntersectionCableConnector(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDElectricalIntersectionCableConnector icdElectricalIntersectionCableConnector, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdElectricalIntersectionCableConnector, entityObject);
        return (EntityObject)icdElectricalIntersectionCableConnector;
    }
    
    public void solve() {
        if (this.isModified()) {
            this.applyChangesForAttribute("Generic_Cable_Width", "36");
            if (this.getParent((TypeFilter)new ICDElectricalIntersectionFilter()) == null) {
                this.destroy();
            }
        }
        super.solve();
    }
    
    public String getDefaultLayerName() {
        return "Electrical Harness";
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
