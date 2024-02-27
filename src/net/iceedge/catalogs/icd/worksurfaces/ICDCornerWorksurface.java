package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.utilities.MathUtilities;
import javax.vecmath.Point3f;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDCornerWorksurface extends ICDBasicWorksurface
{
    public ICDCornerWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public ICDCornerWorksurface buildClone(final ICDCornerWorksurface icdCornerWorksurface) {
        super.buildClone(icdCornerWorksurface);
        return icdCornerWorksurface;
    }
    
    public ICDCornerWorksurface buildClone2(final ICDCornerWorksurface icdCornerWorksurface) {
        super.buildClone2(icdCornerWorksurface);
        return icdCornerWorksurface;
    }
    
    public EntityObject buildFrameClone(final ICDCornerWorksurface icdCornerWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdCornerWorksurface, entityObject);
        return (EntityObject)icdCornerWorksurface;
    }
    
    @Override
    protected void setupNamedPoints() {
        super.setupNamedPoints();
        this.addNamedPoint("WSBM_ARM1", new Point3f(0.0f, 0.0f, 0.0f));
    }
    
    @Override
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        final Point3f geometricCenterPointLocal = this.getGeometricCenterPointLocal();
        final float n = this.getXDimension() / 2.0f;
        final float n2 = this.getYDimension() / 2.0f;
        final float n3 = this.getZDimension() / 2.0f;
        this.getNamedPointLocal("WSBL").set(geometricCenterPointLocal.x - n, geometricCenterPointLocal.y - n2, geometricCenterPointLocal.z - n3);
        this.getNamedPointLocal("WSBR").set(geometricCenterPointLocal.x + n, geometricCenterPointLocal.y + n2, geometricCenterPointLocal.z - n3);
        this.getNamedPointLocal("WSBM_ARM1").set(geometricCenterPointLocal.x - n, geometricCenterPointLocal.y, geometricCenterPointLocal.z - n3);
        final float leftDepth = this.getLeftDepth();
        final float rightDepth = this.getRightDepth();
        this.getNamedPointLocal("WSFL").set(geometricCenterPointLocal.x - n + leftDepth, geometricCenterPointLocal.y - n2, geometricCenterPointLocal.z - n3);
        this.getNamedPointLocal("WSFR").set(geometricCenterPointLocal.x + n, geometricCenterPointLocal.y + n2 - rightDepth, geometricCenterPointLocal.z - n3);
    }
    
    @Override
    protected void calculateNamedRotations() {
        super.calculateNamedRotations();
        this.getNamedRotationLocal("Left_Support_Angle").set(0.0f, 0.0f, 0.0f);
        this.getNamedRotationLocal("Right_Support_Angle").set(0.0f, 0.0f, MathUtilities.ANGLE_270);
    }
    
    @Override
    public float getLeftDepth() {
        return 24.0f;
    }
    
    @Override
    public float getRightDepth() {
        return 24.0f;
    }
}
