// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWCAWorksurface extends ICDBasicWorksurface
{
    public ICDWCAWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWCAWorksurface icdwcaWorksurface) {
        super.buildClone(icdwcaWorksurface);
        return (TransformableEntity)icdwcaWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWCAWorksurface icdwcaWorksurface) {
        super.buildClone2(icdwcaWorksurface);
        return (TransformableEntity)icdwcaWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWCAWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWCAWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWCAWorksurface icdwcaWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdwcaWorksurface, entityObject);
        return (EntityObject)icdwcaWorksurface;
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
        return 42.0f;
    }
    
    @Override
    public float getMinYDimension() {
        return 42.0f;
    }
    
    @Override
    public String getShapeTag() {
        return "WCA";
    }
}
