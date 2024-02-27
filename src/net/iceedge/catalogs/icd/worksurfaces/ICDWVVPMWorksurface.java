package net.iceedge.catalogs.icd.worksurfaces;

import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWVVPMWorksurface extends ICDBasicWorksurface
{
    public ICDWVVPMWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWVVPMWorksurface icdwvvpmWorksurface) {
        super.buildClone(icdwvvpmWorksurface);
        return (TransformableEntity)icdwvvpmWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWVVPMWorksurface icdwvvpmWorksurface) {
        super.buildClone2(icdwvvpmWorksurface);
        return (TransformableEntity)icdwvvpmWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWVVPMWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVVPMWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWVVPMWorksurface icdwvvpmWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdwvvpmWorksurface, entityObject);
        return (EntityObject)icdwvvpmWorksurface;
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
        return 12.0f;
    }
    
    @Override
    public String getShapeTag() {
        return "WVVPM";
    }
}
