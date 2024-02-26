// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWSEWorksurface extends ICDBasicWorksurface
{
    public ICDWSEWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWSEWorksurface icdwseWorksurface) {
        super.buildClone(icdwseWorksurface);
        return (TransformableEntity)icdwseWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWSEWorksurface icdwseWorksurface) {
        super.buildClone2(icdwseWorksurface);
        return (TransformableEntity)icdwseWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWSEWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWSEWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWSEWorksurface icdwseWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdwseWorksurface, entityObject);
        return (EntityObject)icdwseWorksurface;
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
        return 24.0f;
    }
    
    @Override
    public float getMinYDimension() {
        return 18.0f;
    }
    
    @Override
    public String getShapeTag() {
        return "WSE";
    }
}
