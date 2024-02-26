// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWSFWorksurface extends ICDBasicWorksurface
{
    public ICDWSFWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWSFWorksurface icdwsfWorksurface) {
        super.buildClone(icdwsfWorksurface);
        return (TransformableEntity)icdwsfWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWSFWorksurface icdwsfWorksurface) {
        super.buildClone2(icdwsfWorksurface);
        return (TransformableEntity)icdwsfWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWSFWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWSFWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWSFWorksurface icdwsfWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdwsfWorksurface, entityObject);
        return (EntityObject)icdwsfWorksurface;
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
        return 48.0f;
    }
    
    @Override
    public float getMinYDimension() {
        return 48.0f;
    }
    
    @Override
    public String getShapeTag() {
        return "WSF";
    }
}
