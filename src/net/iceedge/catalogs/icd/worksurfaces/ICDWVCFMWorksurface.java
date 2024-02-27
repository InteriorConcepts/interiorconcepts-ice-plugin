package net.iceedge.catalogs.icd.worksurfaces;

import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWVCFMWorksurface extends ICDBasicWorksurface
{
    public ICDWVCFMWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWVCFMWorksurface icdwvcfmWorksurface) {
        super.buildClone(icdwvcfmWorksurface);
        return (TransformableEntity)icdwvcfmWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWVCFMWorksurface icdwvcfmWorksurface) {
        super.buildClone2(icdwvcfmWorksurface);
        return (TransformableEntity)icdwvcfmWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWVCFMWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVCFMWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWVCFMWorksurface icdwvcfmWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdwvcfmWorksurface, entityObject);
        return (EntityObject)icdwvcfmWorksurface;
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
        return "WVCFM";
    }
}
