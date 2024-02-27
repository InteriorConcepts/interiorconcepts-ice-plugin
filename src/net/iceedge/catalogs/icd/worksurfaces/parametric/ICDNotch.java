package net.iceedge.catalogs.icd.worksurfaces.parametric;

import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.Notch;

public class ICDNotch extends Notch implements ICDManufacturingReportable
{
    public ICDNotch(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
    }
    
    public ICDNotch(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDNotch(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDNotch buildClone(final ICDNotch icdNotch) {
        super.buildClone((Notch)icdNotch);
        return icdNotch;
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
    
    public void cutFromTree2D() {
        if (this.shapeNode != null) {
            this.shapeNode.removeFromParent();
        }
        super.cutFromTree2D();
    }
}
