package icd.warnings;

import net.iceedge.catalogs.icd.electrical.ICDElectricalCable;
import net.dirtt.icelib.warnings.WarningLevel;
import net.dirtt.icelib.warnings.WarningReason;

public class WarningReason0363 extends WarningReason
{
    private static final int WARNING_NUMBER = 363;
    
    private WarningReason0363(final String s, final String s2) {
        super(s, s2);
    }
    
    public WarningLevel getWarningLevel() {
        return WarningReason.CANT_EXIST;
    }
    
    public int getWarningNumber() {
        return 363;
    }
    
    protected String getWarningName() {
        return "Invalid Beam Assembly Width";
    }
    
    public static void addRequiredWarning(final ICDElectricalCable icdElectricalCable) {
        icdElectricalCable.addWarning((WarningReason)new WarningReason0363("Electrical beam is not valid for this panel length.", "Electrical beam is not valid for this panel length."));
    }
}
