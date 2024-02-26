// 
// Decompiled by Procyon v0.5.36
// 

package icd.warnings;

import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDParametricWorksurface;
import javax.vecmath.Point3f;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDWireDip;
import net.dirtt.icelib.warnings.WarningLevel;
import net.dirtt.icelib.warnings.WarningReason;

public class WarningReason0261 extends WarningReason
{
    private static final int WARNING_ID = 261;
    
    private WarningReason0261() {
        super("Wire Dip too close to corner", "Wire Dip too close to corner");
    }
    
    public WarningLevel getWarningLevel() {
        return WarningReason.UNDEFINED;
    }
    
    public int getWarningNumber() {
        return 261;
    }
    
    protected String getWarningName() {
        return "Wire Dip too close to corner";
    }
    
    public static void addRequiredWarning(final ICDWireDip key) {
        startTimer(261);
        final ICDParametricWorksurface parentWorksurface = key.getParentWorksurface();
        if (parentWorksurface != null) {
            final LineParameter lineParameter = parentWorksurface.getWireDipParamLookupMap().get(key);
            if (lineParameter != null) {
                lineParameter.getStartPoint();
                lineParameter.getEndPoint();
                final Point3f point3f = new Point3f(key.getBasePoint3f());
                point3f.z = 0.0f;
                if (lineParameter.getStartPoint().distance(point3f) < 5.0f || lineParameter.getEndPoint().distance(point3f) < 5.0f) {
                    key.addWarning((WarningReason)new WarningReason0261());
                }
            }
        }
        stopTimer(261);
    }
}
