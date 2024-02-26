// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWVCPMWorksurface extends ICDBasicWorksurface
{
    public ICDWVCPMWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWVCPMWorksurface icdwvcpmWorksurface) {
        super.buildClone(icdwvcpmWorksurface);
        return (TransformableEntity)icdwvcpmWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWVCPMWorksurface icdwvcpmWorksurface) {
        super.buildClone2(icdwvcpmWorksurface);
        return (TransformableEntity)icdwvcpmWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWVCPMWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVCPMWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWVCPMWorksurface icdwvcpmWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdwvcpmWorksurface, entityObject);
        return (EntityObject)icdwvcpmWorksurface;
    }
    
    @Override
    public float getMaxXDimension() {
        return 36.0f;
    }
    
    @Override
    public float getMaxYDimension() {
        return 144.0f;
    }
    
    @Override
    public float getMinXDimension() {
        return 20.0f;
    }
    
    @Override
    public float getMinYDimension() {
        return 48.0f;
    }
    
    @Override
    public String getShapeTag() {
        return "WVCPM";
    }
}
