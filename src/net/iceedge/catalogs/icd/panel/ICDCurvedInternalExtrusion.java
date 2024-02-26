// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.main.BoundingCube;
import javax.vecmath.Matrix4f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDCurvedInternalExtrusion extends ICDInternalExtrusion
{
    public ICDCurvedInternalExtrusion(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDCurvedInternalExtrusion(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDCurvedInternalExtrusion buildClone(final ICDCurvedInternalExtrusion icdCurvedInternalExtrusion) {
        super.buildClone(icdCurvedInternalExtrusion);
        return icdCurvedInternalExtrusion;
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDCurvedInternalExtrusion(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDCurvedInternalExtrusion buildFrameClone(final ICDCurvedInternalExtrusion icdCurvedInternalExtrusion, final EntityObject entityObject) {
        super.buildFrameClone(icdCurvedInternalExtrusion, entityObject);
        return icdCurvedInternalExtrusion;
    }
    
    @Override
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("CAD_P1").set(this.getZDimension(), 0.0f, this.getYDimension() / 2.0f);
        final Point3f namedPointLocal = this.getNamedPointLocal("CAD_P1");
        final Point3f namedPointLocal2 = this.getNamedPointLocal("CAD_P2");
        this.getNamedPointLocal("CADELP8").set((Tuple3f)new Point3f((namedPointLocal.x + namedPointLocal2.x) / 2.0f, (namedPointLocal.y + namedPointLocal2.y) / 2.0f, (namedPointLocal.z + namedPointLocal2.z) / 2.0f));
    }
    
    public boolean contains(final Point3f point3f) {
        final float n = 100.0f;
        final Matrix4f matrix4f = (Matrix4f)this.getEntWorldSpaceMatrix().clone();
        matrix4f.invert();
        final Point3f point3f2 = new Point3f(point3f);
        matrix4f.transform(point3f2);
        point3f2.x = Math.round(point3f2.x * n) / n;
        point3f2.y = Math.round(point3f2.y * n) / n;
        point3f2.z = Math.round(point3f2.z * n) / n;
        final boolean intersect = this.getBoundingCube().intersect(point3f2);
        if (intersect) {
            return intersect;
        }
        final BoundingCube boundingCube = new BoundingCube();
        final float n2 = this.getXDimension() / 2.0f;
        final float n3 = this.getYDimension() / 2.0f;
        final float n4 = this.getZDimension() / 2.0f;
        final Point3f point3f3 = new Point3f(n4 + 0.25f, 0.5f, -0.5f);
        final float n5 = n2;
        final float n6 = n4;
        final float n7 = n5;
        boundingCube.set(point3f3.x - n6, point3f3.y - n3, point3f3.z - n7, point3f3.x + n6, point3f3.y + n3, point3f3.z + n7);
        return boundingCube.intersect(point3f2);
    }
}
