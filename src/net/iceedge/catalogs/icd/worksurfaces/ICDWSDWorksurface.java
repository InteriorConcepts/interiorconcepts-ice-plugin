package net.iceedge.catalogs.icd.worksurfaces;

import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWSDWorksurface extends ICDBasicWorksurface
{
    public ICDWSDWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWSDWorksurface icdwsdWorksurface) {
        super.buildClone(icdwsdWorksurface);
        return (TransformableEntity)icdwsdWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWSDWorksurface icdwsdWorksurface) {
        super.buildClone2(icdwsdWorksurface);
        return (TransformableEntity)icdwsdWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWSDWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWSDWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWSDWorksurface icdwsdWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdwsdWorksurface, entityObject);
        return (EntityObject)icdwsdWorksurface;
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
        return 40.0f;
    }
    
    @Override
    public float getMinYDimension() {
        return 30.0f;
    }
    
    @Override
    public String getShapeTag() {
        return "WSD";
    }
}
