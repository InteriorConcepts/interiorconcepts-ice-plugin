package net.iceedge.catalogs.icd.worksurfaces;

import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWVVWorksurface extends ICDBasicWorksurface
{
    public ICDWVVWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWVVWorksurface icdwvvWorksurface) {
        super.buildClone(icdwvvWorksurface);
        return (TransformableEntity)icdwvvWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWVVWorksurface icdwvvWorksurface) {
        super.buildClone2(icdwvvWorksurface);
        return (TransformableEntity)icdwvvWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWVVWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVVWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWVVWorksurface icdwvvWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdwvvWorksurface, entityObject);
        return (EntityObject)icdwvvWorksurface;
    }
    
    @Override
    public String getShapeTag() {
        if (this.isRightHanded()) {
            return "WVVRL";
        }
        return "WVVRR";
    }
}
