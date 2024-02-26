// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWVSEMWorksurface extends ICDBasicWorksurface
{
    public ICDWVSEMWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWVSEMWorksurface icdwvsemWorksurface) {
        super.buildClone(icdwvsemWorksurface);
        return (TransformableEntity)icdwvsemWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWVSEMWorksurface icdwvsemWorksurface) {
        super.buildClone2(icdwvsemWorksurface);
        return (TransformableEntity)icdwvsemWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWVSEMWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVSEMWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWVSEMWorksurface icdwvsemWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdwvsemWorksurface, entityObject);
        return (EntityObject)icdwvsemWorksurface;
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
        return 18.0f;
    }
    
    @Override
    public float getMinYDimension() {
        return 18.0f;
    }
    
    @Override
    public String getShapeTag() {
        return "WVSEM";
    }
}
