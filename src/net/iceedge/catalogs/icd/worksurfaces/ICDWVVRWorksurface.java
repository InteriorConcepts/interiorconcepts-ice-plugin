// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWVVRWorksurface extends ICDBasicWorksurface
{
    public ICDWVVRWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWVVRWorksurface icdwvvrWorksurface) {
        super.buildClone(icdwvvrWorksurface);
        return (TransformableEntity)icdwvvrWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWVVRWorksurface icdwvvrWorksurface) {
        super.buildClone2(icdwvvrWorksurface);
        return (TransformableEntity)icdwvvrWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWVVRWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVVRWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWVVRWorksurface icdwvvrWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdwvvrWorksurface, entityObject);
        return (EntityObject)icdwvvrWorksurface;
    }
    
    @Override
    public float getMaxXDimension() {
        if (this.getYDimensionFromData() > 60.0f) {
            return 60.0f;
        }
        return 96.0f;
    }
    
    @Override
    public float getMaxYDimension() {
        if (this.getXDimensionFromData() > 60.0f) {
            return 60.0f;
        }
        return 96.0f;
    }
    
    @Override
    public float getMinXDimension() {
        return 15.0f;
    }
    
    @Override
    public float getMinYDimension() {
        return 25.0f;
    }
    
    @Override
    public String getShapeTag() {
        if (this.isRightHanded()) {
            return "WVVRR";
        }
        return "WVVRL";
    }
}
