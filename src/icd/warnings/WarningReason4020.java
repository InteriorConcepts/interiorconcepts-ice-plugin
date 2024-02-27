package icd.warnings;

import net.iceedge.catalogs.icd.panel.ICDPanel;
import net.dirtt.icelib.warnings.WarningLevel;
import net.dirtt.icelib.warnings.WarningReason;

public class WarningReason4020 extends WarningReason
{
    private static final int WARNING_ID = 4020;
    
    private WarningReason4020(final String s) {
        super(s, s);
    }
    
    public WarningLevel getWarningLevel() {
        return WarningReason.SOFTWARN;
    }
    
    public int getWarningNumber() {
        return 4020;
    }
    
    protected String getWarningName() {
        return "More than 2 tile splits in stack.  Only two splits are supported.";
    }
    
    public static void addRequiredWarning(final ICDPanel icdPanel) {
        icdPanel.addWarning((WarningReason)new WarningReason4020("More than 2 tiles in stack.  Only one split is supported."));
    }
}
