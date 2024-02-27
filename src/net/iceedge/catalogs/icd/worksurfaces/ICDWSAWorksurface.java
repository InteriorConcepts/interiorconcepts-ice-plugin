package net.iceedge.catalogs.icd.worksurfaces;

import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWSAWorksurface extends ICDBasicWorksurface
{
    public ICDWSAWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWSAWorksurface icdwsaWorksurface) {
        super.buildClone(icdwsaWorksurface);
        return (TransformableEntity)icdwsaWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWSAWorksurface icdwsaWorksurface) {
        super.buildClone2(icdwsaWorksurface);
        return (TransformableEntity)icdwsaWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWSAWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWSAWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWSAWorksurface icdwsaWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdwsaWorksurface, entityObject);
        return (EntityObject)icdwsaWorksurface;
    }
    
    @Override
    public float getMaxXDimension() {
        if (this.getYDimensionFromData() > 60.0f) {
            return 60.0f;
        }
        return 144.0f;
    }
    
    @Override
    public float getMaxYDimension() {
        if (this.getXDimensionFromData() > 60.0f) {
            return 60.0f;
        }
        return 144.0f;
    }
    
    @Override
    public float getMinXDimension() {
        return 48.0f;
    }
    
    @Override
    public float getMinYDimension() {
        return 42.0f;
    }
    
    @Override
    public String getShapeTag() {
        return "WSA";
    }
}
