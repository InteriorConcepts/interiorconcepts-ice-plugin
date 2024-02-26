// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.catalogs.icd.ICDILine;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicPanelSegment;

public class ICDPanelSegment extends BasicPanelSegment implements ICDManufacturingReportable
{
    private static final long serialVersionUID = 8053389917854908333L;
    
    public ICDPanelSegment(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public ICDPanelSegment buildClone(final ICDPanelSegment icdPanelSegment) {
        super.buildClone((BasicPanelSegment)icdPanelSegment);
        return icdPanelSegment;
    }
    
    public Object clone() {
        return this.buildClone(new ICDPanelSegment(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDPanelSegment(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public EntityObject buildFrameClone(final ICDPanelSegment icdPanelSegment, final EntityObject entityObject) {
        return super.buildFrameClone((BasicPanelSegment)icdPanelSegment, entityObject);
    }
    
    public boolean showDimensions() {
        return false;
    }
    
    protected boolean needDrawPanelDividerInElevation() {
        return false;
    }
    
    public void setSelected(final boolean b, final Solution solution) {
        super.setSelected(b, solution);
        final ICDILine icdiLine = (ICDILine)this.getParent((Class)ICDILine.class);
        if (icdiLine != null) {
            final ICDVerticalChase verticalChase = icdiLine.getVerticalChase();
            if (verticalChase != null) {
                verticalChase.setVerticalChasePanelSegmentsSelected(this, b, solution);
            }
        }
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
