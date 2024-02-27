package icd.warnings;

import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricCutout;
import net.dirtt.icelib.warnings.WarningLevel;
import net.dirtt.icelib.warnings.WarningReason;

public class WarningReason0258 extends WarningReason
{
    private static final int WARNING_ID = 258;
    
    private WarningReason0258() {
        super("Cutout too close to back edge", "Cutout too close to back edge");
    }
    
    public WarningLevel getWarningLevel() {
        return WarningReason.UNDEFINED;
    }
    
    public int getWarningNumber() {
        return 258;
    }
    
    protected String getWarningName() {
        return "Cutout too close to edge";
    }
    
    public static void addRequiredWarning(final ICDParametricCutout icdParametricCutout) {
        startTimer(258);
        icdParametricCutout.addWarning((WarningReason)new WarningReason0258());
        stopTimer(258);
    }
}
