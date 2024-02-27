package icd.warnings;

import net.iceedge.catalogs.icd.panel.ICDTile;
import net.dirtt.icelib.warnings.WarningLevel;
import net.dirtt.icelib.warnings.WarningReason;

public class WarningReason0246 extends WarningReason
{
    private static final int WARNING_ID = 246;
    
    private WarningReason0246(final String s) {
        super(s, s);
    }
    
    public WarningLevel getWarningLevel() {
        return WarningReason.CANT_EXIST;
    }
    
    public int getWarningNumber() {
        return 246;
    }
    
    protected String getWarningName() {
        return "Invalid Valet Door Size";
    }
    
    public static void addRequiredWarning(final ICDTile icdTile) {
        startTimer(246);
        stopTimer(246);
    }
}
