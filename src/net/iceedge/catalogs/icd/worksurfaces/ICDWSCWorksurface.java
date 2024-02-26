// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;
import net.iceedge.icecore.basemodule.baseclasses.grips.GripListener;
import net.iceedge.icecore.basemodule.baseclasses.grips.RelativeAttributeGrip;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWSCWorksurface extends ICDBasicWorksurface
{
    public ICDWSCWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDWSCWorksurface icdwscWorksurface) {
        super.buildClone(icdwscWorksurface);
        return (TransformableEntity)icdwscWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDWSCWorksurface icdwscWorksurface) {
        super.buildClone2(icdwscWorksurface);
        return (TransformableEntity)icdwscWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWSCWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWSCWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWSCWorksurface icdwscWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdwscWorksurface, entityObject);
        return (EntityObject)icdwscWorksurface;
    }
    
    @Override
    public void setupGripPainters() {
        (this.rightWidthGrip = (BasicAttributeGrip)new RelativeAttributeGrip((TransformableEntity)this, 0)).setLinkID((byte)99);
        (this.bottomDepthGrip = (BasicAttributeGrip)new RelativeAttributeGrip((TransformableEntity)this, 2)).setLinkID((byte)99);
        this.rightWidthGrip.addListener((GripListener)this.widthGripListener);
        this.bottomDepthGrip.addListener((GripListener)this.depthGripListener);
    }
    
    @Override
    public float getMaxXDimension() {
        return 144.0f;
    }
    
    @Override
    public float getMaxYDimension() {
        return 144.0f;
    }
    
    @Override
    public float getMinXDimension() {
        return 40.0f;
    }
    
    @Override
    public float getMinYDimension() {
        return 30.0f;
    }
    
    @Override
    public String getShapeTag() {
        return "WSC";
    }
}
