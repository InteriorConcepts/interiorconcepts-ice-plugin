package net.iceedge.catalogs.icd.electrical;

import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.undo.iceobjects.Point3fWithUndo;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.electrical.intent.BasicEndfeedPowerSourceIntent;

public class ICDEndfeedPowerSourceIntent extends BasicEndfeedPowerSourceIntent implements ICDManufacturingReportable
{
    public ICDEndfeedPowerSourceIntent(final String s, final TypeObject typeObject) {
        super(s, typeObject);
    }
    
    public ICDEndfeedPowerSourceIntent(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public ICDEndfeedPowerSourceIntent(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
    }
    
    public ICDEndfeedPowerSourceIntent(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject, final Point3fWithUndo point3fWithUndo) {
        super(s, s2, typeObject, optionObject, point3fWithUndo);
    }
    
    public ICDEndfeedPowerSourceIntent buildClone(final ICDEndfeedPowerSourceIntent icdEndfeedPowerSourceIntent) {
        super.buildClone((BasicEndfeedPowerSourceIntent)icdEndfeedPowerSourceIntent);
        return icdEndfeedPowerSourceIntent;
    }
    
    public ICDEndfeedPowerSourceIntent buildClone2(final ICDEndfeedPowerSourceIntent icdEndfeedPowerSourceIntent) {
        super.buildClone2((BasicEndfeedPowerSourceIntent)icdEndfeedPowerSourceIntent);
        return icdEndfeedPowerSourceIntent;
    }
    
    public Object clone() {
        return this.buildClone(new ICDEndfeedPowerSourceIntent(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDEndfeedPowerSourceIntent icdEndfeedPowerSourceIntent, final EntityObject entityObject) {
        super.buildFrameClone((BasicEndfeedPowerSourceIntent)icdEndfeedPowerSourceIntent, entityObject);
        return (EntityObject)icdEndfeedPowerSourceIntent;
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
