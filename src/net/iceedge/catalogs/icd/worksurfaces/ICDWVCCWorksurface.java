package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWVCCWorksurface extends ICDBasicWorksurface
{
    public ICDWVCCWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWVCCWorksurface icdwvccWorksurface) {
        super.buildClone(icdwvccWorksurface);
        return (TransformableEntity)icdwvccWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWVCCWorksurface icdwvccWorksurface) {
        super.buildClone2(icdwvccWorksurface);
        return (TransformableEntity)icdwvccWorksurface;
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVCCWorksurface(this.getId(), this.currentType, this.currentOption));
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
        return 48.0f;
    }
    
    @Override
    public float getMinYDimension() {
        return 25.0f;
    }
    
    @Override
    public String getShapeTag() {
        if (this.isRightHanded()) {
            return "WVCCR";
        }
        return "WVCCL";
    }
}
