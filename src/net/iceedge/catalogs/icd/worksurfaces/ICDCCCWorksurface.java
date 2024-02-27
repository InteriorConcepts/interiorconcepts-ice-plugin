package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.BoundingCube;
import javax.vecmath.Point3f;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDCCCWorksurface extends ICDBasicWorksurface
{
    public ICDCCCWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDCCCWorksurface icdcccWorksurface) {
        super.buildClone(icdcccWorksurface);
        return (TransformableEntity)icdcccWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDCCCWorksurface icdcccWorksurface) {
        super.buildClone2(icdcccWorksurface);
        return (TransformableEntity)icdcccWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDCCCWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDCCCWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDCCCWorksurface icdcccWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdcccWorksurface, entityObject);
        return (EntityObject)icdcccWorksurface;
    }
    
    @Override
    public float getMaxXDimension() {
        if (this.getYDimension() > 60.0f) {
            return 60.0f;
        }
        return 144.0f;
    }
    
    @Override
    public float getMaxYDimension() {
        if (this.getXDimension() > 60.0f) {
            return 60.0f;
        }
        return 144.0f;
    }
    
    @Override
    public float getMinXDimension() {
        return 36.0f;
    }
    
    @Override
    public float getMinYDimension() {
        return 36.0f;
    }
    
    @Override
    public String getShapeTag() {
        return "CCC";
    }
    
    @Override
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        final Point3f namedPointLocal = this.getNamedPointLocal("Top_Left_Snap_Corner");
        final Point3f namedPointLocal2 = this.getNamedPointLocal("Top_Right_Snap_Corner");
        this.getNamedPointLocal("Top_Left_Snap_Corner").set(6.0f, -6.0f, namedPointLocal.z);
        this.getNamedPointLocal("Top_Right_Snap_Corner").set(this.getXDimension() - 6.0f, -6.0f, namedPointLocal2.z);
    }
    
    @Override
    public BoundingCube getCubeForTriggers() {
        return null;
    }
}
