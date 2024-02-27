package net.iceedge.catalogs.icd.electrical;

import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TypeValidatorEntity;
import net.dirtt.icelib.report.compare.CompareNode;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import java.util.Iterator;
import net.iceedge.icecore.basemodule.baseclasses.electrical.intent.PowerIntentinator;
import net.iceedge.icecore.basemodule.baseclasses.electrical.intent.BasicEndfeedPowerSourceIntent;
import net.iceedge.catalogs.icd.intersection.ICDIntersection;
import net.iceedge.icecore.basemodule.baseclasses.electrical.intent.BasicPowerIntent;
import net.dirtt.icelib.main.TypeableEntity;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.electrical.BasicReceptacleSolver;

public class ICDElectricalIntersectionReceptacleSolver extends BasicReceptacleSolver implements ICDManufacturingReportable
{
    public ICDElectricalIntersectionReceptacleSolver(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDElectricalIntersectionReceptacleSolver(this.getId(), this.getCurrentType(), this.getCurrentOption()), entityObject);
    }
    
    public EntityObject buildFrameClone(final ICDElectricalIntersectionReceptacleSolver icdElectricalIntersectionReceptacleSolver, final EntityObject entityObject) {
        super.buildFrameClone((BasicReceptacleSolver)icdElectricalIntersectionReceptacleSolver, entityObject);
        return (EntityObject)icdElectricalIntersectionReceptacleSolver;
    }
    
    public Object clone() {
        return this.buildClone(new ICDElectricalIntersectionReceptacleSolver(this.getId(), this.currentType, this.currentOption));
    }
    
    public TypeableEntity buildClone(final ICDElectricalIntersectionReceptacleSolver icdElectricalIntersectionReceptacleSolver) {
        super.buildClone((BasicReceptacleSolver)icdElectricalIntersectionReceptacleSolver);
        return (TypeableEntity)icdElectricalIntersectionReceptacleSolver;
    }
    
    protected void solveReceptacles() {
        for (final BasicPowerIntent powerIntent : this.getPowerIntents()) {
            if (powerIntent != null && !powerIntent.hasRealEntity()) {
                final ICDIntersection icdIntersection = (ICDIntersection)this.getParentEntity();
                if (icdIntersection == null || !(powerIntent instanceof BasicEndfeedPowerSourceIntent)) {
                    continue;
                }
                final ICDEndfeedPowerSource buildPowerEndfeed = icdIntersection.buildPowerEndfeed(true, powerIntent);
                if (buildPowerEndfeed == null) {
                    continue;
                }
                powerIntent.setRealEntity((PowerIntentinator)buildPowerEndfeed);
                buildPowerEndfeed.setPowerIntent(powerIntent);
            }
        }
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
