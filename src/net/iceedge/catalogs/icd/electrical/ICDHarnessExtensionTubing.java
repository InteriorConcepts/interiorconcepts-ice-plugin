package net.iceedge.catalogs.icd.electrical;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.text.DecimalFormat;
import java.util.TreeMap;
import net.dirtt.icelib.report.compare.CompareNode;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;

public class ICDHarnessExtensionTubing extends TransformableEntity implements ICDManufacturingReportable
{
    public ICDHarnessExtensionTubing(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public ICDHarnessExtensionTubing buildClone(final ICDHarnessExtensionTubing icdHarnessExtensionTubing) {
        super.buildClone((TransformableEntity)icdHarnessExtensionTubing);
        return icdHarnessExtensionTubing;
    }
    
    public ICDHarnessExtensionTubing buildClone2(final ICDHarnessExtensionTubing icdHarnessExtensionTubing) {
        super.buildClone2((TransformableEntity)icdHarnessExtensionTubing);
        return icdHarnessExtensionTubing;
    }
    
    public Object clone() {
        return this.buildClone(new ICDHarnessExtensionTubing(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDHarnessExtensionTubing(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public EntityObject buildFrameClone(final ICDHarnessExtensionTubing icdHarnessExtensionTubing, final EntityObject entityObject) {
        super.buildFrameClone((TransformableEntity)icdHarnessExtensionTubing, entityObject);
        return (EntityObject)icdHarnessExtensionTubing;
    }
    
    public void solve() {
        if (this.isModified()) {
            this.modifyCurrentOption();
            this.validateChildTypes();
        }
        super.solve();
    }
    
    public void modifyCurrentOption() {
        this.validateIndicators();
        super.modifyCurrentOption();
    }
    
    protected void validateIndicators() {
        String s = "No";
        final ICDElectricalCable parentCable = this.getParentCable();
        if (parentCable != null && parentCable.requiresExtensionTubing()) {
            s = "Yes";
        }
        this.createNewAttribute("Generic_Has_Electrical", s);
    }
    
    protected void calculateDimensions() {
        super.calculateDimensions();
        final ICDElectricalCable parentCable = this.getParentCable();
        if (parentCable != null && parentCable.requiresExtensionTubing()) {
            this.setXDimension(parentCable.getExtTubeWidth());
        }
    }
    
    private ICDElectricalCable getParentCable() {
        return (ICDElectricalCable)this.getParentByClassRecursive(ICDElectricalCable.class);
    }
    
    public boolean isQuoteable(final String s) {
        return false;
    }
    
    public void populateCompareNode(final Class clazz, final CompareNode compareNode) {
        compareNode.addCompareValue("length", (Object)this.getLength());
    }
    
    public boolean shouldICDMakePreAssembledReport() {
        return !"true".equals(this.getAttributeValueAsString("isAssembled"));
    }
    
    public boolean usePlotGVT() {
        return false;
    }
    
    public boolean draw2D() {
        return false;
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
        final DecimalFormat decimalFormat = new DecimalFormat("#.##");
        treeMap.put("Depth", "");
        treeMap.put("Width", Double.valueOf(decimalFormat.format(this.getXDimension())) + "");
        treeMap.put("Height", "");
    }
    
    public String getManufacturingReportMaterialOptID() {
        return "Option0";
    }
    
    public void addManufacturingInfoToTreeMap(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addManufacturingInfoToTreeMap(treeMap, (ManufacturingReportable)this);
    }
    
    public String getDescriptionForManufacturingReport() {
        return this.getDescription() + ",Length:" + Double.valueOf(new DecimalFormat("#.##").format(this.getXDimension())) + " ";
    }
}
