package net.iceedge.catalogs.icd;

import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import java.util.Iterator;
import net.iceedge.catalogs.icd.panel.ICDPanelToPanelConnectionHW;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.ChildSolver;
import net.iceedge.catalogs.icd.panel.ICDFrameToFrameConnectionHWSolverForSubILine;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.util.FrameToFrameConnectionHWSolverForSubILine;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.BasicSubILine;

public class ICDSubILine extends BasicSubILine implements ICDManufacturingReportable
{
    private FrameToFrameConnectionHWSolverForSubILine frameToFrameConnectionHWSolver;
    
    public ICDSubILine(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.frameToFrameConnectionHWSolver = new ICDFrameToFrameConnectionHWSolverForSubILine();
    }
    
    public ICDSubILine buildClone(final ICDSubILine icdSubILine) {
        super.buildClone((BasicSubILine)icdSubILine);
        return icdSubILine;
    }
    
    public Object clone() {
        return this.buildClone(new ICDSubILine(this.getId(), this.currentType, this.currentOption));
    }
    
    public boolean usingTallestSegmentToCreateConnectionHW() {
        return true;
    }
    
    protected ChildSolver createChildSolver() {
        return new ChildSolver(this) {
            public void processChildren() {
                if (ICDSubILine.this.isModified()) {
                    ICDSubILine.this.frameToFrameConnectionHWSolver.solveFrameToFrameConnectionHWs((BasicSubILine)ICDSubILine.this);
                }
                super.processChildren();
            }
        };
    }
    
    public void setPanelToPanelHWModified(final boolean modified) {
        if (modified) {
            final Iterator<ICDPanelToPanelConnectionHW> iterator = this.getChildrenByClass(ICDPanelToPanelConnectionHW.class, false).iterator();
            while (iterator.hasNext()) {
                iterator.next().setModified(modified);
            }
        }
    }
    
    public void handleAttributeChange(final String s, final String s2) {
        ICDUtilities.handleAttributeChange((EntityObject)this, s, s2);
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
