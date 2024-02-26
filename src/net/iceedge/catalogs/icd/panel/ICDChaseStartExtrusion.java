// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.main.TransformableEntity;
import icd.warnings.WarningReason0282;
import javax.vecmath.Tuple3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDChaseStartExtrusion extends ICDStartExtrusion
{
    public ICDChaseStartExtrusion(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDChaseStartExtrusion(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDChaseStartExtrusion buildClone(final ICDChaseStartExtrusion icdChaseStartExtrusion) {
        super.buildClone(icdChaseStartExtrusion);
        return icdChaseStartExtrusion;
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDChaseStartExtrusion(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDChaseStartExtrusion buildFrameClone(final ICDChaseStartExtrusion icdChaseStartExtrusion, final EntityObject entityObject) {
        super.buildFrameClone(icdChaseStartExtrusion, entityObject);
        return icdChaseStartExtrusion;
    }
    
    protected void calculateNamedScales() {
        super.calculateNamedScales();
        final Vector3f namedScaleLocal = this.getNamedScaleLocal("ASE_length_scale");
        namedScaleLocal.x = 1.0f;
        namedScaleLocal.y = 1.0f;
        namedScaleLocal.z = this.getZDimension();
    }
    
    @Override
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("extStartPoint").set((Tuple3f)new Point3f());
        this.getNamedPointLocal("extEndPoint").set((Tuple3f)new Point3f(0.0f, 0.0f, this.getZDimension()));
        this.getNamedPointLocal("extStart").set((Tuple3f)new Point3f(0.0f, this.getYDimension(), 0.0f));
        this.getNamedPointLocal("extEnd").set((Tuple3f)new Point3f(0.0f, this.getYDimension(), this.getZDimension()));
    }
    
    @Override
    public boolean doesParticipateInJointIntersection() {
        return true;
    }
    
    @Override
    public void handleWarnings() {
        WarningReason0282.addRequiredWarning((TransformableEntity)this);
    }
}
