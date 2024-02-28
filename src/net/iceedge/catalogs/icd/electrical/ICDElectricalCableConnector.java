package net.iceedge.catalogs.icd.electrical;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.warnings.WarningReason;
import net.iceedge.icecore.basemodule.interfaces.SegmentBase;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import java.util.List;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.interfaces.OptionOverrideEntity;
import net.iceedge.icecore.basemodule.baseclasses.electrical.BasicElectricalCableConnector;

public class ICDElectricalCableConnector extends BasicElectricalCableConnector implements OptionOverrideEntity, ICDManufacturingReportable
{
    private final List<Integer> widths;
    
    public ICDElectricalCableConnector(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.widths = new ArrayList<Integer>();
        this.initPossibleWidths();
    }
    
    private void initPossibleWidths() {
        for (final OptionObject optionObject : this.getCurrentType().getPossibleOptionVector()) {
            try {
                this.widths.add(Integer.parseInt(optionObject.getAttributeValueAsString("Generic_Cable_Width")));
            }
            catch (NumberFormatException ex) {}
        }
        Collections.sort(this.widths);
    }
    
    public TransformableEntity buildClone(final ICDElectricalCableConnector icdElectricalCableConnector) {
        super.buildClone((BasicElectricalCableConnector)icdElectricalCableConnector);
        return (TransformableEntity)icdElectricalCableConnector;
    }
    
    public TransformableEntity buildClone2(final ICDElectricalCableConnector icdElectricalCableConnector) {
        super.buildClone2((BasicElectricalCableConnector)icdElectricalCableConnector);
        return (TransformableEntity)icdElectricalCableConnector;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDElectricalCableConnector(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDElectricalCableConnector(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDElectricalCableConnector icdElectricalCableConnector, final EntityObject entityObject) {
        super.buildFrameClone((BasicElectricalCableConnector)icdElectricalCableConnector, entityObject);
        return (EntityObject)icdElectricalCableConnector;
    }
    
    protected void validateIndicators() {
        this.createNewAttribute("Generic_Cable_Width", this.calculateConnectorWidth());
    }
    
    protected String calculateConnectorWidth() {
        float a = 0.0f;
        final ICDElectricalCable icdElectricalCable = (ICDElectricalCable)this.getParent(ICDElectricalCable.class);
        if (icdElectricalCable != null) {
            a += icdElectricalCable.getConnectorExtensionWidth();
            SegmentBase segmentBase;
            if (this.isLeftConnector()) {
                segmentBase = icdElectricalCable.getSegment().getSegmentBefore();
            }
            else {
                segmentBase = icdElectricalCable.getSegment().getSegmentAfter();
            }
            if (segmentBase != null) {
                final List<ICDElectricalCable> childrenByClass = segmentBase.getChildrenByClass(ICDElectricalCable.class, true, true);
                if (childrenByClass != null && childrenByClass.size() != 0) {
                    final ICDElectricalCable icdElectricalCable2 = childrenByClass.get(0);
                    if (icdElectricalCable2 != null) {
                        a += icdElectricalCable2.getConnectorExtensionWidth();
                    }
                }
            }
        }
        return String.valueOf(this.getRealCableWidth(Math.round(a)));
    }
    
    public int getRealCableWidth(final int n) {
        int n2 = 0;
        for (final int intValue : this.widths) {
            if (intValue >= n) {
                n2 = intValue;
                break;
            }
        }
        return n2;
    }
    
    public float getLongestJumperSize() {
        return this.widths.get(this.widths.size() - 1);
    }
    
    protected void applyWarningOnParentTransformable(final WarningReason warningReason) {
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
