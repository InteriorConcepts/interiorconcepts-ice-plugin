// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.electrical;

import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TypeValidatorEntity;
import net.dirtt.icelib.report.compare.CompareNode;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.main.TypeableEntity;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.electrical.BasicReceptacleSolver;

public class ICDReceptacleSolver extends BasicReceptacleSolver implements ICDManufacturingReportable
{
    public ICDReceptacleSolver(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDReceptacleSolver(this.getId(), this.getCurrentType(), this.getCurrentOption()), entityObject);
    }
    
    public EntityObject buildFrameClone(final ICDReceptacleSolver icdReceptacleSolver, final EntityObject entityObject) {
        super.buildFrameClone((BasicReceptacleSolver)icdReceptacleSolver, entityObject);
        return (EntityObject)icdReceptacleSolver;
    }
    
    public Object clone() {
        return this.buildClone(new ICDReceptacleSolver(this.getId(), this.currentType, this.currentOption));
    }
    
    public TypeableEntity buildClone(final ICDReceptacleSolver icdReceptacleSolver) {
        super.buildClone((BasicReceptacleSolver)icdReceptacleSolver);
        return (TypeableEntity)icdReceptacleSolver;
    }
    
    public void getManufacturingInfo(final TreeMap<String, String> treeMap) {
        super.getManufacturingInfo((TreeMap)treeMap);
        ICDManufacturingUtils.addAssemblyInfoToManufacturingTreeMap(treeMap, (EntityObject)this);
    }
    
    public void populateCompareNodeForManufacturing(final Class clazz, final CompareNode compareNode) {
        ICDManufacturingUtils.populateCompareNodeForManufacturing(clazz, compareNode, (TypeValidatorEntity)this);
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
