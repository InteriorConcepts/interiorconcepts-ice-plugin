// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDSubFrame extends ICDFrame
{
    public ICDSubFrame(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public ICDSubFrame buildClone(final ICDSubFrame icdSubFrame) {
        super.buildClone(icdSubFrame);
        return icdSubFrame;
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDSubFrame(this.getId(), this.currentType, this.currentOption));
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDSubFrame(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public EntityObject buildFrameClone(final ICDSubFrame icdSubFrame, final EntityObject entityObject) {
        return (EntityObject)super.buildFrameClone(icdSubFrame, entityObject);
    }
}
