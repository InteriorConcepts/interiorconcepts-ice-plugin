// 
// Decompiled by Procyon v0.5.36
// 

package icd.warnings;

import net.dirtt.icelib.main.attributes.Attribute;
import java.util.Iterator;
import javax.vecmath.Point3f;
import java.util.List;
import net.dirtt.icelib.warnings.AutoFixableWarning;
import net.iceedge.catalogs.icd.panel.ICDPanel;
import net.iceedge.catalogs.icd.overheads.ICDBasicOverhead;
import net.dirtt.icelib.warnings.WarningLevel;
import net.dirtt.icelib.warnings.WarningReason;

public class WarningReason0264 extends WarningReason
{
    private static final int WARNING_ID = 264;
    private static String shortDesc;
    private static String longDescStart;
    private static String longDesc;
    private static String fixLongOH;
    private static String fixLongPNL;
    
    private WarningReason0264(final String s, final String s2) {
        super(s2, s);
    }
    
    public WarningLevel getWarningLevel() {
        return WarningReason.UNDEFINED;
    }
    
    public int getWarningNumber() {
        return 264;
    }
    
    protected String getWarningName() {
        return "Invalid Overhead Height";
    }
    
    public static void addRequiredWarning(final ICDBasicOverhead icdBasicOverhead) {
        startTimer(264);
        final List<ICDPanel> childrenByClass = icdBasicOverhead.getSolution().getChildrenByClass(ICDPanel.class, true, true);
        final Point3f basePointWorldSpace = icdBasicOverhead.getBasePointWorldSpace();
        ICDPanel icdPanel = null;
        if (childrenByClass.size() > 0) {
            for (final ICDPanel icdPanel2 : childrenByClass) {
                if (icdPanel == null) {
                    icdPanel = icdPanel2;
                }
                else if (icdPanel2.convertPointToWorldSpace(icdPanel2.getBasePoint3f()).distance(basePointWorldSpace) < icdPanel.convertPointToWorldSpace(icdPanel.getBasePoint3f()).distance(basePointWorldSpace)) {
                    icdPanel = icdPanel2;
                }
                final Point3f basePointWorldSpace2 = icdPanel.getBasePointWorldSpace();
                if (Math.abs(basePointWorldSpace.x - basePointWorldSpace2.x) < 2.0f && Math.abs(basePointWorldSpace.y - basePointWorldSpace2.y) < 2.0f) {
                    final float f = (icdPanel == null) ? 0.0f : icdPanel.getHeight();
                    final Attribute attributeObject = icdBasicOverhead.getAttributeObject("ICD_Height_From_Floor");
                    final float f2 = Float.parseFloat(attributeObject.getValueAsString()) + icdBasicOverhead.getHeight();
                    if (f2 <= f) {
                        continue;
                    }
                    final WarningReason0264 warningReason0264 = new WarningReason0264(WarningReason0264.shortDesc, WarningReason0264.longDescStart + f2 + " [Height From Floor: " + attributeObject.getValueAsString() + " + Overhead Height: " + icdBasicOverhead.getHeight() + "]" + WarningReason0264.longDesc + f + WarningReason0264.fixLongOH + (f - icdBasicOverhead.getHeight()) + WarningReason0264.fixLongPNL + (f + icdBasicOverhead.getHeight()));
                    warningReason0264.addFix((AutoFixableWarning)new FixOverheadHeight(icdBasicOverhead, f - icdBasicOverhead.getHeight()));
                    icdBasicOverhead.addWarning((WarningReason)warningReason0264);
                }
            }
        }
        stopTimer(264);
    }
    
    static {
        WarningReason0264.shortDesc = "Invalid Overhead Height";
        WarningReason0264.longDescStart = "The overhead height of ";
        WarningReason0264.longDesc = " is invalid for this configuration. \nPlease ensure overhead does not exceed panel height of ";
        WarningReason0264.fixLongOH = "\n\nTo fix lower overhead height below ";
        WarningReason0264.fixLongPNL = " or raise panel height above ";
    }
    
    public static class FixOverheadHeight implements AutoFixableWarning
    {
        private ICDBasicOverhead entity;
        private float height;
        private static String AUTO_FIX;
        
        public FixOverheadHeight(final ICDBasicOverhead entity, final float height) {
            this.entity = entity;
            this.height = height;
            FixOverheadHeight.AUTO_FIX = "Adjust overhead height to " + this.height;
        }
        
        public int autoFix() {
            this.entity.applyChangesForAttribute("ICD_Height_From_Floor", "" + this.height);
            return 1;
        }
        
        public String getLongFixDescription() {
            return FixOverheadHeight.AUTO_FIX;
        }
        
        public String getShortFixDescription() {
            return FixOverheadHeight.AUTO_FIX;
        }
        
        static {
            FixOverheadHeight.AUTO_FIX = "";
        }
    }
}
