// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.main.warnings;

import net.iceedge.catalogs.icd.intersection.ICDPost;
import net.dirtt.icelib.warnings.WarningLevel;
import net.dirtt.icelib.warnings.WarningReason;

public class ICDWarningReason1000 extends WarningReason
{
    private static final int WARNING_ID = 1000;
    private static String shortDesc;
    private static String longDesc;
    
    private ICDWarningReason1000() {
        super(ICDWarningReason1000.longDesc, ICDWarningReason1000.shortDesc);
    }
    
    public WarningLevel getWarningLevel() {
        return WarningReason.UNDEFINED;
    }
    
    public int getWarningNumber() {
        return 1000;
    }
    
    protected String getWarningName() {
        return ICDWarningReason1000.shortDesc;
    }
    
    public static void addRequiredWarning(final ICDPost icdPost) {
    }
    
    static {
        ICDWarningReason1000.shortDesc = "Too many overheads!";
        ICDWarningReason1000.longDesc = "Can not place more than 3 overheads on the same post!";
    }
}
