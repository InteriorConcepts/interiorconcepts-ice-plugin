// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWCFWorksurface extends ICDBasicWorksurface
{
    public ICDWCFWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWCFWorksurface icdwcfWorksurface) {
        super.buildClone(icdwcfWorksurface);
        return (TransformableEntity)icdwcfWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWCFWorksurface icdwcfWorksurface) {
        super.buildClone2(icdwcfWorksurface);
        return (TransformableEntity)icdwcfWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWCFWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWCFWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWCFWorksurface icdwcfWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdwcfWorksurface, entityObject);
        return (EntityObject)icdwcfWorksurface;
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
        return "WCF";
    }
}
