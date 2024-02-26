// 
// Decompiled by Procyon v0.5.36
// 

package icd.warnings;

import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricCutout;
import net.dirtt.icelib.warnings.WarningLevel;
import net.dirtt.icelib.warnings.WarningReason;

public class WarningReason0259 extends WarningReason
{
    private static final int WARNING_ID = 259;
    
    private WarningReason0259() {
        super("Cutout too close to side edge", "Cutout too close to side edge");
    }
    
    public WarningLevel getWarningLevel() {
        return WarningReason.UNDEFINED;
    }
    
    public int getWarningNumber() {
        return 259;
    }
    
    protected String getWarningName() {
        return "Cutout too close to edge";
    }
    
    public static void addRequiredWarning(final ICDParametricCutout icdParametricCutout) {
        startTimer(259);
        icdParametricCutout.addWarning((WarningReason)new WarningReason0259());
        stopTimer(259);
    }
}
