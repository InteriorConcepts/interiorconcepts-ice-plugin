// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces.parametric;

import java.util.Iterator;
import net.dirtt.icelib.main.BoundingCube;
import net.iceedge.icecore.basemodule.finalclasses.GeneralSnapSet;
import net.iceedge.catalogs.icd.panel.ICDPanel;
import net.dirtt.utilities.MathUtilities;
import javax.vecmath.Point3f;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.iceedge.catalogs.icd.worksurfaces.ICDBasicWorksurface;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public abstract class ICDCornerParametricWorksurface extends ICDParametricWorksurface
{
    protected float chaseDepthLeft;
    protected float chaseDepthRight;
    
    public ICDCornerParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, "", typeObject, optionObject);
        this.chaseDepthLeft = 0.0f;
        this.chaseDepthRight = 0.0f;
    }
    
    public ICDCornerParametricWorksurface buildClone(final ICDCornerParametricWorksurface icdCornerParametricWorksurface) {
        super.buildClone(icdCornerParametricWorksurface);
        return icdCornerParametricWorksurface;
    }
    
    public ICDCornerParametricWorksurface buildClone2(final ICDCornerParametricWorksurface icdCornerParametricWorksurface) {
        super.buildClone2(icdCornerParametricWorksurface);
        return icdCornerParametricWorksurface;
    }
    
    public EntityObject buildFrameClone(final ICDCornerParametricWorksurface icdCornerParametricWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdCornerParametricWorksurface, entityObject);
        return (EntityObject)icdCornerParametricWorksurface;
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
        final float leftDepth = this.getLeftDepth();
        final float rightDepth = this.getRightDepth();
        this.calculateChaseDepthForCornerWorksurfaceSupports();
        this.getNamedPointLocal("WSBL").set(geometricCenterPointLocal.x - n + this.chaseDepthLeft, geometricCenterPointLocal.y - n2, geometricCenterPointLocal.z - n3);
        this.getNamedPointLocal("WSBR").set(geometricCenterPointLocal.x + n, geometricCenterPointLocal.y + n2 - this.chaseDepthRight, geometricCenterPointLocal.z - n3);
        this.getNamedPointLocal("WSBM_ARM1").set(geometricCenterPointLocal.x - n + this.chaseDepthLeft, geometricCenterPointLocal.y, geometricCenterPointLocal.z - n3);
        this.getNamedPointLocal("WSBM").set(geometricCenterPointLocal.x, geometricCenterPointLocal.y + n2 - this.chaseDepthRight, geometricCenterPointLocal.z - n3);
        this.getNamedPointLocal("RH_Back_Corner").set(geometricCenterPointLocal.x - n, geometricCenterPointLocal.y + n2, geometricCenterPointLocal.z - n3);
        this.getNamedPointLocal("LH_Back_Corner").set(geometricCenterPointLocal.x - n, geometricCenterPointLocal.y + n2, geometricCenterPointLocal.z - n3);
        this.getNamedPointLocal("WSFL").set(geometricCenterPointLocal.x - n + leftDepth, geometricCenterPointLocal.y - n2, geometricCenterPointLocal.z - n3);
        this.getNamedPointLocal("WSFR").set(geometricCenterPointLocal.x + n, geometricCenterPointLocal.y + n2 - rightDepth, geometricCenterPointLocal.z - n3);
        if (this.shouldMirrorNamedPoints()) {
            this.getNamedPointLocal("WSBL").set(geometricCenterPointLocal.x - n, geometricCenterPointLocal.y + n2 - this.chaseDepthLeft, geometricCenterPointLocal.z - n3);
            this.getNamedPointLocal("WSBR").set(geometricCenterPointLocal.x + n - this.chaseDepthRight, geometricCenterPointLocal.y - n2, geometricCenterPointLocal.z - n3);
            this.getNamedPointLocal("WSBM_ARM1").set(geometricCenterPointLocal.x - this.chaseDepthLeft, geometricCenterPointLocal.y + n2, geometricCenterPointLocal.z - n3);
            this.getNamedPointLocal("WSBM").set(geometricCenterPointLocal.x + n, geometricCenterPointLocal.y - this.chaseDepthRight, geometricCenterPointLocal.z - n3);
            this.getNamedPointLocal("RH_Back_Corner").set(geometricCenterPointLocal.x + n, geometricCenterPointLocal.y + n2, geometricCenterPointLocal.z - n3);
            this.getNamedPointLocal("LH_Back_Corner").set(geometricCenterPointLocal.x + n, geometricCenterPointLocal.y + n2, geometricCenterPointLocal.z - n3);
            this.getNamedPointLocal("WSFL").set(geometricCenterPointLocal.x - n, geometricCenterPointLocal.y + n2 - leftDepth, geometricCenterPointLocal.z - n3);
            this.getNamedPointLocal("WSFR").set(geometricCenterPointLocal.x + n - rightDepth, geometricCenterPointLocal.y - n2, geometricCenterPointLocal.z - n3);
        }
    }
    
    @Override
    protected void calculateNamedRotations() {
        super.calculateNamedRotations();
        this.getNamedRotationLocal("Left_Support_Angle").set(0.0f, 0.0f, 0.0f);
        this.getNamedRotationLocal("Right_Support_Angle").set(0.0f, 0.0f, MathUtilities.ANGLE_270);
        if (this.shouldMirrorNamedPoints()) {
            this.getNamedRotationLocal("Left_Support_Angle").set(0.0f, 0.0f, MathUtilities.ANGLE_270);
            this.getNamedRotationLocal("Right_Support_Angle").set(0.0f, 0.0f, -3.1415927f);
        }
    }
    
    @Override
    public float getLeftChaseDepthForWorksurfaceSupport() {
        return this.chaseDepthLeft;
    }
    
    @Override
    public float getRightChaseDepthForWorksurfaceSupport() {
        return this.chaseDepthRight;
    }
    
    private void calculateChaseDepthForCornerWorksurfaceSupports() {
        this.chaseDepthRight = 0.0f;
        this.chaseDepthLeft = 0.0f;
        final GeneralSnapSet generalSnapSet = this.getGeneralSnapSet();
        if (generalSnapSet != null) {
            final BoundingCube worldBoundingCube = this.getWorldBoundingCube();
            for (final ICDPanel icdPanel : generalSnapSet.getChildrenByClass((Class)ICDPanel.class, true)) {
                if (worldBoundingCube.intersect(icdPanel.getWorldBoundingCube())) {
                    final Point3f convertPointToOtherLocalSpace = MathUtilities.convertPointToOtherLocalSpace((EntityObject)icdPanel, this.getBasePointWorldSpace());
                    if (!icdPanel.hasChaseOnPointSide(convertPointToOtherLocalSpace)) {
                        continue;
                    }
                    final Point3f convertSpaces = MathUtilities.convertSpaces(new Point3f(this.getLength(), 0.0f, 0.0f), (EntityObject)icdPanel, (EntityObject)this);
                    final float calculateLength = MathUtilities.calculateLength(this.getNamedPointLocal("WSBL"), convertSpaces);
                    final float calculateLength2 = MathUtilities.calculateLength(this.getNamedPointLocal("WSBR"), convertSpaces);
                    boolean b = true;
                    if (calculateLength > calculateLength2) {
                        b = false;
                    }
                    if (icdPanel.isPointOnSideA(convertPointToOtherLocalSpace)) {
                        if (b) {
                            this.chaseDepthLeft = icdPanel.getChaseSideAOffset();
                        }
                        else {
                            this.chaseDepthRight = icdPanel.getChaseSideAOffset();
                        }
                    }
                    else if (b) {
                        this.chaseDepthLeft = icdPanel.getChaseSideBOffset();
                    }
                    else {
                        this.chaseDepthRight = icdPanel.getChaseSideBOffset();
                    }
                }
            }
        }
    }
}
