// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWVCAWorksurface extends ICDBasicWorksurface
{
    public ICDWVCAWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWVCAWorksurface icdwvcaWorksurface) {
        super.buildClone(icdwvcaWorksurface);
        return (TransformableEntity)icdwvcaWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWVCAWorksurface icdwvcaWorksurface) {
        super.buildClone2(icdwvcaWorksurface);
        return (TransformableEntity)icdwvcaWorksurface;
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVCAWorksurface(this.getId(), this.currentType, this.currentOption));
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
        return 25.0f;
    }
    
    @Override
    public String getShapeTag() {
        if (this.isRightHanded()) {
            return "WVCAR";
        }
        return "WVCAL";
    }
}
