package net.iceedge.catalogs.icd;

import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;
import net.iceedge.icecore.basemodule.baseclasses.grips.RelativeAttributeGrip;
import net.dirtt.icelib.undo.iceobjects.Point3fWithUndo;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import org.apache.log4j.Logger;

public class ICDBeamILine extends ICDILine
{
    private static Logger logger;
    
    public ICDBeamILine(final String s, final TypeObject typeObject) {
        super(s, typeObject);
    }
    
    public ICDBeamILine(final TypeObject typeObject) {
        super(typeObject);
    }
    
    public ICDBeamILine(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public ICDBeamILine(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject, final Point3fWithUndo point3fWithUndo) {
        super(s, s2, typeObject, optionObject, point3fWithUndo);
    }
    
    public ICDBeamILine buildClone(final ICDBeamILine icdBeamILine) {
        super.buildClone(icdBeamILine);
        return icdBeamILine;
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDBeamILine(this.getId(), this.currentType, this.currentOption));
    }
    
    public void handle3DSelect(final boolean b) {
    }
    
    public void setupJoinedHeightGrip(final RelativeAttributeGrip relativeAttributeGrip, final RelativeAttributeGrip relativeAttributeGrip2) {
    }
    
    public void updateGrips(final BasicAttributeGrip basicAttributeGrip) {
    }
    
    public void setupGripPainters() {
    }
    
    static {
        ICDBeamILine.logger = Logger.getLogger((Class)ICDBeamILine.class);
    }
}
