// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import javax.vecmath.Tuple3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDCurvedEndExtrusion extends ICDEndExtrusion
{
    public ICDCurvedEndExtrusion(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDCurvedEndExtrusion(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDCurvedEndExtrusion buildClone(final ICDCurvedEndExtrusion icdCurvedEndExtrusion) {
        super.buildClone(icdCurvedEndExtrusion);
        return icdCurvedEndExtrusion;
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDCurvedEndExtrusion(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDCurvedEndExtrusion buildFrameClone(final ICDCurvedEndExtrusion icdCurvedEndExtrusion, final EntityObject entityObject) {
        super.buildFrameClone(icdCurvedEndExtrusion, entityObject);
        return icdCurvedEndExtrusion;
    }
    
    public boolean isFakePart() {
        return this.getCurrentOption().getId().toLowerCase().indexOf("special") > -1;
    }
    
    @Override
    protected void calculateGeometricCenter() {
        super.calculateGeometricCenter();
        final float n = this.getZDimension() * 0.5f;
        final Point3f namedPointLocal = this.getNamedPointLocal("extGeometricCenter");
        Point3f geometricCenterPointLocal;
        if (namedPointLocal == null) {
            geometricCenterPointLocal = new Point3f(0.0f, 0.0f, 0.0f);
        }
        else {
            geometricCenterPointLocal = new Point3f(namedPointLocal);
        }
        geometricCenterPointLocal.add((Tuple3f)new Point3f(0.0f, 0.0f, n));
        this.setGeometricCenterPointLocal(geometricCenterPointLocal);
    }
    
    @Override
    public boolean doesParticipateInJointIntersection() {
        return true;
    }
}
