package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDAngledFrame extends ICDFrame
{
    public ICDAngledFrame(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDAngledFrame(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDAngledFrame buildClone(final ICDAngledFrame icdAngledFrame) {
        super.buildClone(icdAngledFrame);
        return icdAngledFrame;
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDAngledFrame(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDAngledFrame buildFrameClone(final ICDAngledFrame icdAngledFrame, final EntityObject entityObject) {
        super.buildFrameClone(icdAngledFrame, entityObject);
        return icdAngledFrame;
    }
}
