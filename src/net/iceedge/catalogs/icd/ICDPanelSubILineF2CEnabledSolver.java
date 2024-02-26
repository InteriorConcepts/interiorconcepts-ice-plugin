// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd;

import net.iceedge.icecore.basemodule.baseclasses.panels.PanelSegmentLensRecord;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelSubILineInterface;
import net.iceedge.icecore.basemodule.baseclasses.panels.GeneralPanelSubILineF2CEnabledSolver;

public class ICDPanelSubILineF2CEnabledSolver extends GeneralPanelSubILineF2CEnabledSolver
{
    public ICDPanelSubILineF2CEnabledSolver(final PanelSubILineInterface panelSubILineInterface) {
        super(panelSubILineInterface);
    }
    
    protected PanelSegmentLensRecord generateSegmentLenghtArray(final float n) {
        final PanelSubILineInterface panelSubILineInterface = (PanelSubILineInterface)this.subILine;
        return new ICDPanelSegmentLengthsF2CEnabledGenerator((PanelSubILineInterface)this.subILine, panelSubILineInterface.getPanelSegmentLensRecordValidator(), panelSubILineInterface.getPanelSegmentLensRecordComparator(), panelSubILineInterface.getMaxStackNumber(), panelSubILineInterface.getMaxStackHeight()).getSegmentLengths(n);
    }
}
