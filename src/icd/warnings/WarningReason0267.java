package icd.warnings;

import net.dirtt.icelib.warnings.AutoFixableWarning;
import net.iceedge.icecore.basemodule.interfaces.SegmentBase;
import net.dirtt.icelib.warnings.WarningLevel;
import net.iceedge.catalogs.icd.ICDSegment;
import net.dirtt.icelib.warnings.WarningReason;

public class WarningReason0267 extends WarningReason
{
    private static final int WARNING_ID = 267;
    private static final String WARNING_MESSAGE_NAME = "Invalid Assembly";
    private static final String WARNING_MESSAGE_LONG_DESC = "Cannot create an assembly of segment beside already existing assembly.";
    ICDSegment segment;
    
    private WarningReason0267() {
    }
    
    private WarningReason0267(final String s, final ICDSegment segment) {
        super(s, s);
        this.segment = segment;
        this.initializeAutoFixes();
    }
    
    public WarningLevel getWarningLevel() {
        return WarningReason.CANT_EXIST;
    }
    
    public int getWarningNumber() {
        return 267;
    }
    
    protected String getWarningName() {
        return "Invalid Assembly";
    }
    
    public static void addRequiredWarning(final ICDSegment icdSegment) {
        startTimer(267);
        if (icdSegment.getAttributeValueAsBoolean("shouldAssemble", false)) {
            final SegmentBase segmentBefore = icdSegment.getSegmentBefore();
            final SegmentBase segmentAfter = icdSegment.getSegmentAfter();
            if (false || (segmentBefore != null && segmentBefore.getAttributeValueAsBoolean("shouldAssemble", false)) || (segmentAfter != null && segmentAfter.getAttributeValueAsBoolean("shouldAssemble", false))) {
                icdSegment.addWarning((WarningReason)new WarningReason0267("Cannot create an assembly of segment beside already existing assembly.", icdSegment));
            }
        }
        stopTimer(267);
    }
    
    private void initializeAutoFixes() {
        this.addFix((AutoFixableWarning)new UnassemblePanelFix());
    }
    
    class UnassemblePanelFix implements AutoFixableWarning
    {
        public String getShortFixDescription() {
            return "Unassemble Panel";
        }
        
        public String getLongFixDescription() {
            return "Unassemble Panel";
        }
        
        public int autoFix() {
            if (WarningReason0267.this.segment != null) {
                WarningReason0267.this.segment.createNewAttribute("shouldAssemble", "false");
                return 1;
            }
            return 2;
        }
    }
}
