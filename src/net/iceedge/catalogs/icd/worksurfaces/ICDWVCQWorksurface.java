package net.iceedge.catalogs.icd.worksurfaces;

import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWVCQWorksurface extends ICDBasicWorksurface
{
    public ICDWVCQWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWVCQWorksurface icdwvcqWorksurface) {
        super.buildClone(icdwvcqWorksurface);
        return (TransformableEntity)icdwvcqWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWVCQWorksurface icdwvcqWorksurface) {
        super.buildClone2(icdwvcqWorksurface);
        return (TransformableEntity)icdwvcqWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWVCQWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVCQWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWVCQWorksurface icdwvcqWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdwvcqWorksurface, entityObject);
        return (EntityObject)icdwvcqWorksurface;
    }
    
    @Override
    public float getMaxXDimension() {
        return 144.0f;
    }
    
    @Override
    public float getMaxYDimension() {
        return 39.0f;
    }
    
    @Override
    public float getMinXDimension() {
        return 48.0f;
    }
    
    @Override
    public float getMinYDimension() {
        return 39.0f;
    }
    
    @Override
    public String getShapeTag() {
        if (this.isRightHanded()) {
            return "WVCQR";
        }
        return "WVCQL";
    }
}
