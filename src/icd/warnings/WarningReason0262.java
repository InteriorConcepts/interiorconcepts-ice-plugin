// 
// Decompiled by Procyon v0.5.36
// 

package icd.warnings;

import java.util.Iterator;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;
import javax.vecmath.Point3f;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDWireDip;
import net.dirtt.icelib.warnings.WarningLevel;
import net.dirtt.icelib.warnings.WarningReason;

public class WarningReason0262 extends WarningReason
{
    private static final int WARNING_ID = 262;
    
    private WarningReason0262() {
        super("Wire Dips too close to each other", "Wire Dips too close to each other");
    }
    
    public WarningLevel getWarningLevel() {
        return WarningReason.UNDEFINED;
    }
    
    public int getWarningNumber() {
        return 262;
    }
    
    protected String getWarningName() {
        return "Wire Dips too close to each other";
    }
    
    public static void addRequiredWarning(final ICDWireDip icdWireDip) {
        startTimer(262);
        final ICDParametricWorksurface parentWorksurface = icdWireDip.getParentWorksurface();
        if (parentWorksurface != null) {
            for (final ICDWireDip icdWireDip2 : parentWorksurface.getWireDips()) {
                if (icdWireDip2 != icdWireDip) {
                    final Point3f point3f = new Point3f(icdWireDip2.getBasePointWorldSpace());
                    final Point3f point3f2 = new Point3f(icdWireDip.getBasePointWorldSpace());
                    point3f.z = 0.0f;
                    point3f2.z = 0.0f;
                    if (point3f.distance(point3f2) >= 10.0f) {
                        continue;
                    }
                    icdWireDip.addWarning((WarningReason)new WarningReason0262());
                }
            }
        }
        stopTimer(262);
    }
}
