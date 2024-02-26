// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWCLWorksurface extends ICDBasicWorksurface
{
    public ICDWCLWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWCLWorksurface icdwclWorksurface) {
        super.buildClone(icdwclWorksurface);
        return (TransformableEntity)icdwclWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWCLWorksurface icdwclWorksurface) {
        super.buildClone2(icdwclWorksurface);
        return (TransformableEntity)icdwclWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWCLWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWCLWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWCLWorksurface icdwclWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdwclWorksurface, entityObject);
        return (EntityObject)icdwclWorksurface;
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
        return 42.0f;
    }
    
    @Override
    public float getMinYDimension() {
        return 42.0f;
    }
    
    @Override
    public String getShapeTag() {
        return "WCL";
    }
}
