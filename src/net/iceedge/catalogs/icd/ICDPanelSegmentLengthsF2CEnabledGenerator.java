// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd;

import net.dirtt.utilities.MathUtilities;
import net.iceedge.icecore.basemodule.baseclasses.panels.PanelSegmentLensRecord;
import net.iceedge.icecore.basemodule.baseclasses.panels.PanelSegmentLensRecordComparatorInterface;
import net.iceedge.icecore.basemodule.baseclasses.panels.PanelSegmentLensRecordValidatorInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelSubILineInterface;
import net.iceedge.icecore.basemodule.baseclasses.panels.GeneralPanelSegmentLengthsF2CEnabledGenerator;

public class ICDPanelSegmentLengthsF2CEnabledGenerator extends GeneralPanelSegmentLengthsF2CEnabledGenerator
{
    public ICDPanelSegmentLengthsF2CEnabledGenerator(final PanelSubILineInterface panelSubILineInterface, final PanelSegmentLensRecordValidatorInterface panelSegmentLensRecordValidatorInterface, final PanelSegmentLensRecordComparatorInterface panelSegmentLensRecordComparatorInterface, final int n, final float n2) {
        super(panelSubILineInterface, panelSegmentLensRecordValidatorInterface, panelSegmentLensRecordComparatorInterface, n, n2);
    }
    
    protected PanelSegmentLensRecord calculateSegmentLenghtArray(int n, int n2) {
        final PanelSegmentLensRecord expectedPanelSegmentLensRecord = this.subILine.getExpectedPanelSegmentLensRecord();
        if (MathUtilities.isSameFloat(expectedPanelSegmentLensRecord.getTotalHeight(), this.lengthOfSubWall, 0.001f)) {
            return expectedPanelSegmentLensRecord;
        }
        if (!this.subILine.getSegmentLensModificationRecord().baseSegmenModified) {
            final float n3 = this.lengthOfSubWall - expectedPanelSegmentLensRecord.getTotalHeight();
            final PanelSegmentLensRecord panelSegmentLensRecord = expectedPanelSegmentLensRecord;
            panelSegmentLensRecord.baseSegmentLen += n3;
            if (this.baseSizeArray.length > 0 && expectedPanelSegmentLensRecord.baseSegmentLen <= this.baseSizeArray[this.baseSizeArray.length - 1] && expectedPanelSegmentLensRecord.baseSegmentLen >= this.baseSizeArray[0]) {
                return expectedPanelSegmentLensRecord;
            }
        }
        else {
            final float baseSegmentLen = expectedPanelSegmentLensRecord.baseSegmentLen;
            int i = 0;
            while (i <= this.baseSizeArray.length - 1) {
                if (MathUtilities.isSameFloat(this.baseSizeArray[i], baseSegmentLen, 0.001f)) {
                    n = i;
                    if (i + 6 <= this.baseSizeArray.length) {
                        n2 = i + 6;
                        break;
                    }
                    break;
                }
                else {
                    ++i;
                }
            }
        }
        return super.calculateSegmentLenghtArray(n, n2);
    }
}
