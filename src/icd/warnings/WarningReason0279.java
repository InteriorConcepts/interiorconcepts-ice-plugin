package icd.warnings;

import net.iceedge.catalogs.icd.worksurfaces.ICDParametricDeck;
import net.dirtt.icelib.warnings.WarningLevel;
import net.dirtt.icelib.warnings.WarningReason;

public class WarningReason0279 extends WarningReason
{
    private static final int WARNING_ID = 279;
    
    private WarningReason0279() {
        super("wire dip is not allowed on Deck", "wire dip is not allowed on Deck");
    }
    
    public WarningLevel getWarningLevel() {
        return WarningReason.UNDEFINED;
    }
    
    public int getWarningNumber() {
        return 279;
    }
    
    protected String getWarningName() {
        return "wire dip is not allowed on Deck";
    }
    
    public static void addRequiredWarning(final ICDParametricDeck icdParametricDeck) {
        startTimer(279);
        if (icdParametricDeck.getWireDips().size() > 0) {
            icdParametricDeck.addWarning((WarningReason)new WarningReason0279());
        }
        stopTimer(279);
    }
}
