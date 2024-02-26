// 
// Decompiled by Procyon v0.5.36
// 

package icd.warnings;

import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricCutout;
import net.dirtt.icelib.warnings.WarningLevel;
import net.dirtt.icelib.warnings.WarningReason;

public class WarningReason0257 extends WarningReason
{
    private static final int WARNING_ID = 257;
    
    private WarningReason0257() {
        super("Cutouts are too close to each other", "Cutouts are too close to each other");
    }
    
    public WarningLevel getWarningLevel() {
        return WarningReason.UNDEFINED;
    }
    
    public int getWarningNumber() {
        return 257;
    }
    
    protected String getWarningName() {
        return "Cutouts too close";
    }
    
    public static void addRequiredWarning(final ICDParametricCutout icdParametricCutout) {
        startTimer(257);
        icdParametricCutout.addWarning((WarningReason)new WarningReason0257());
        stopTimer(257);
    }
}
