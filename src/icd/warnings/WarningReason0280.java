// 
// Decompiled by Procyon v0.5.36
// 

package icd.warnings;

import net.iceedge.catalogs.icd.worksurfaces.ICDParametricShelf;
import net.dirtt.icelib.warnings.WarningLevel;
import net.dirtt.icelib.warnings.WarningReason;

public class WarningReason0280 extends WarningReason
{
    private static final int WARNING_ID = 280;
    
    private WarningReason0280() {
        super("wire dip is not allowed on Shelf", "wire dip is not allowed on Shelf");
    }
    
    public WarningLevel getWarningLevel() {
        return WarningReason.UNDEFINED;
    }
    
    public int getWarningNumber() {
        return 280;
    }
    
    protected String getWarningName() {
        return "wire dip is not allowed on Shelf";
    }
    
    public static void addRequiredWarning(final ICDParametricShelf icdParametricShelf) {
        startTimer(280);
        if (icdParametricShelf.getWireDips().size() > 0) {
            icdParametricShelf.addWarning((WarningReason)new WarningReason0280());
        }
        stopTimer(280);
    }
}
