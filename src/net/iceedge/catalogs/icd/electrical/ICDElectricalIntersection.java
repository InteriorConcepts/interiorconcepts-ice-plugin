package net.iceedge.catalogs.icd.electrical;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.icecore.basemodule.baseclasses.electrical.BasicElectricalCable;
import net.iceedge.icecore.basemodule.interfaces.panels.RacewayInterface;
import java.util.Iterator;
import net.dirtt.utilities.MathUtilities;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicRaceway;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import java.util.ArrayList;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.electrical.BasicElectricalRacewayIntersection;

public class ICDElectricalIntersection extends BasicElectricalRacewayIntersection implements ICDManufacturingReportable
{
    public ICDElectricalIntersection(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDElectricalIntersection icdElectricalIntersection) {
        super.buildClone((BasicElectricalRacewayIntersection)icdElectricalIntersection);
        return (TransformableEntity)icdElectricalIntersection;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDElectricalIntersection(this.getId(), this.getCurrentType(), this.getCurrentOption()), entityObject);
    }
    
    public TransformableEntity buildClone2(final ICDElectricalIntersection icdElectricalIntersection) {
        super.buildClone2((BasicElectricalRacewayIntersection)icdElectricalIntersection);
        return (TransformableEntity)icdElectricalIntersection;
    }
    
    public Object clone() {
        return this.buildClone(new ICDElectricalIntersection(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDElectricalIntersection icdElectricalIntersection, final EntityObject entityObject) {
        super.buildFrameClone((BasicElectricalRacewayIntersection)icdElectricalIntersection, entityObject);
        return (EntityObject)icdElectricalIntersection;
    }
    
    protected void validateIndicators() {
        this.setNeeded(this.isElectricalPresent());
        final ArrayList<Segment> list = new ArrayList<Segment>();
        for (final Segment segment : this.getSegments()) {
            final RacewayInterface raceWay = segment.getRaceWay();
            if (raceWay instanceof BasicRaceway) {
                final BasicElectricalCable electricCable = ((BasicRaceway)raceWay).getElectricCable();
                if (electricCable == null || !"Harness".equals(electricCable.getUserDesiredCableType())) {
                    continue;
                }
                list.add(segment);
            }
        }
        String s = String.valueOf(list.size());
        if (list.size() == 2) {
            final float convertRadiansIntoDegrees = MathUtilities.convertRadiansIntoDegrees(list.get(0).getRotationWorldSpace() - list.get(1).getRotationWorldSpace());
            if (convertRadiansIntoDegrees == 90.0f || convertRadiansIntoDegrees == -90.0f || convertRadiansIntoDegrees == 270.0f || convertRadiansIntoDegrees == -270.0f) {
                s += "_90";
            }
        }
        this.setElectricalIntersectionType(s);
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
