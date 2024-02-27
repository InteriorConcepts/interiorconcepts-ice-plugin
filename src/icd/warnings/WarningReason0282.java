package icd.warnings;

import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.warnings.WarningLevel;
import net.dirtt.icelib.warnings.WarningReason;

public class WarningReason0282 extends WarningReason
{
    private static final int WARNING_ID = 282;
    private static float MIN_EXTRUSION_LENGTH;
    
    public WarningReason0282(final String s) {
        super(s, s);
    }
    
    public WarningLevel getWarningLevel() {
        return WarningReason.SOFTWARN;
    }
    
    public int getWarningNumber() {
        return 282;
    }
    
    protected String getWarningName() {
        return "Extrusion is too small";
    }
    
    public static void addRequiredWarning(final TransformableEntity transformableEntity) {
        startTimer(282);
        float n = 0.0f;
        if (transformableEntity.getXDimension() > n) {
            n = transformableEntity.getXDimension();
        }
        if (transformableEntity.getYDimension() > n) {
            n = transformableEntity.getYDimension();
        }
        if (transformableEntity.getZDimension() > n) {
            n = transformableEntity.getZDimension();
        }
        if (n <= WarningReason0282.MIN_EXTRUSION_LENGTH) {
            transformableEntity.addWarning((WarningReason)new WarningReason0282("Tubing is shorter than " + WarningReason0282.MIN_EXTRUSION_LENGTH + "\""));
        }
        stopTimer(282);
    }
    
    static {
        WarningReason0282.MIN_EXTRUSION_LENGTH = 3.0f;
    }
}
