package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDBottomJoint extends ICDJoint
{
    public ICDBottomJoint(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDBottomJoint(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDBottomJoint buildClone(final ICDBottomJoint icdBottomJoint) {
        super.buildClone(icdBottomJoint);
        return icdBottomJoint;
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDBottomJoint(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDBottomJoint buildFrameClone(final ICDBottomJoint icdBottomJoint, final EntityObject entityObject) {
        super.buildFrameClone(icdBottomJoint, entityObject);
        return icdBottomJoint;
    }
    
    @Override
    public void validateOption() {
    }
    
    @Override
    public void modifyCurrentOption() {
        super.modifyCurrentOption();
    }
    
    public void validateIndicator() {
    }
}
