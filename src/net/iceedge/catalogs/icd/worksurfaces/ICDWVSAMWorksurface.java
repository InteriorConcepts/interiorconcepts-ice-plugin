package net.iceedge.catalogs.icd.worksurfaces;

import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWVSAMWorksurface extends ICDBasicWorksurface
{
    public ICDWVSAMWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWVSAMWorksurface icdwvsamWorksurface) {
        super.buildClone(icdwvsamWorksurface);
        return (TransformableEntity)icdwvsamWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWVSAMWorksurface icdwvsamWorksurface) {
        super.buildClone2(icdwvsamWorksurface);
        return (TransformableEntity)icdwvsamWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWVSAMWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVSAMWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWVSAMWorksurface icdwvsamWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdwvsamWorksurface, entityObject);
        return (EntityObject)icdwvsamWorksurface;
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
        return 18.0f;
    }
    
    @Override
    public float getMinYDimension() {
        return 18.0f;
    }
    
    @Override
    public String getShapeTag() {
        return "WVSAM";
    }
}
