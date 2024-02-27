package icd.warnings;

import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricCutout;
import net.dirtt.icelib.warnings.WarningLevel;
import net.dirtt.icelib.warnings.WarningReason;

public class WarningReason0260 extends WarningReason
{
    private static final int WARNING_ID = 260;
    
    private WarningReason0260() {
        super("Cutout too close to edge", "Cutout too close to edge");
    }
    
    public WarningLevel getWarningLevel() {
        return WarningReason.UNDEFINED;
    }
    
    public int getWarningNumber() {
        return 260;
    }
    
    protected String getWarningName() {
        return "Cutout too close to edge";
    }
    
    public static void addRequiredWarning(final ICDParametricCutout icdParametricCutout) {
        startTimer(260);
        icdParametricCutout.addWarning((WarningReason)new WarningReason0260());
        stopTimer(260);
    }
}
