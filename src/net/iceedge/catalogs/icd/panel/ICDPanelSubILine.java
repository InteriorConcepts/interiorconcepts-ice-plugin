// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.iceedge.icecore.basemodule.baseclasses.utilities.BasicSubIlineSolver;
import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.icecore.basemodule.interfaces.SegmentBase;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.EntityObject;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelSubILineInterface;
import net.iceedge.catalogs.icd.ICDPanelSubILineF2CEnabledSolver;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import org.apache.log4j.Logger;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicPanelSubILine;

public class ICDPanelSubILine extends BasicPanelSubILine implements ICDManufacturingReportable
{
    private static final long serialVersionUID = -1726920812169146671L;
    private static Logger logger;
    
    public ICDPanelSubILine(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.solver = (BasicSubIlineSolver)new ICDPanelSubILineF2CEnabledSolver((PanelSubILineInterface)this);
    }
    
    public ICDPanelSubILine buildClone(final ICDPanelSubILine icdPanelSubILine) {
        super.buildClone((BasicPanelSubILine)icdPanelSubILine);
        return icdPanelSubILine;
    }
    
    public Object clone() {
        return this.buildClone(new ICDPanelSubILine(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDPanelSubILine(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public EntityObject buildFrameClone(final ICDPanelSubILine icdPanelSubILine, final EntityObject entityObject) {
        return super.buildFrameClone((TransformableEntity)icdPanelSubILine, entityObject);
    }
    
    protected Class<? extends SegmentBase> getSegmentChildInterface() {
        return (Class<? extends SegmentBase>)ICDPanelSegment.class;
    }
    
    public int getMaxStackNumber() {
        return 3;
    }
    
    public float getMaxStackHeight() {
        return 144.0f;
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
    
    static {
        ICDPanelSubILine.logger = Logger.getLogger((Class)ICDPanelSubILine.class);
    }
}
