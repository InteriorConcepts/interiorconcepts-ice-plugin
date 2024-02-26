// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDCurvedFrame extends ICDFrame
{
    public ICDCurvedFrame(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDCurvedFrame(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDCurvedFrame buildClone(final ICDCurvedFrame icdCurvedFrame) {
        super.buildClone(icdCurvedFrame);
        return icdCurvedFrame;
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDCurvedFrame(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDCurvedFrame buildFrameClone(final ICDCurvedFrame icdCurvedFrame, final EntityObject entityObject) {
        super.buildFrameClone(icdCurvedFrame, entityObject);
        return icdCurvedFrame;
    }
}
