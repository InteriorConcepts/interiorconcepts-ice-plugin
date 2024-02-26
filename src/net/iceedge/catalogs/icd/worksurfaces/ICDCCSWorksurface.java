// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.BoundingCube;
import net.dirtt.icelib.main.snapping.simple.SimpleSnapTargetCollection;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.snapping.simple.SimpleSnapper;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDCCSWorksurface extends ICDBasicWorksurface
{
    public ICDCCSWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDCCSWorksurface icdccsWorksurface) {
        super.buildClone(icdccsWorksurface);
        return (TransformableEntity)icdccsWorksurface;
    }
    
    public TransformableEntity buildClone2(final ICDCCSWorksurface icdccsWorksurface) {
        super.buildClone2(icdccsWorksurface);
        return (TransformableEntity)icdccsWorksurface;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDCCSWorksurface(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDCCSWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDCCSWorksurface icdccsWorksurface, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdccsWorksurface, entityObject);
        return (EntityObject)icdccsWorksurface;
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
        return 12.0f;
    }
    
    @Override
    public float getMinXDimension() {
        return 18.0f;
    }
    
    @Override
    public float getMinYDimension() {
        return 12.0f;
    }
    
    @Override
    public String getShapeTag() {
        return "CCS";
    }
    
    protected SimpleSnapper addSimpleSnappablesThatDoNotFilterOccupiedTargets(final boolean b, SimpleSnapper addSimpleSnappablesThatDoNotFilterOccupiedTargets) {
        addSimpleSnappablesThatDoNotFilterOccupiedTargets = super.addSimpleSnappablesThatDoNotFilterOccupiedTargets(true, addSimpleSnappablesThatDoNotFilterOccupiedTargets);
        final Point3f namedPointLocal = this.getNamedPointLocal("Top_Left_Snap_Corner");
        final Point3f namedPointLocal2 = this.getNamedPointLocal("Top_Right_Snap_Corner");
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("BRC", namedPointLocal2, Float.valueOf(this.getSnapRotation()), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("BRR", namedPointLocal2, Float.valueOf(this.getSnapRotation()), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TLC", namedPointLocal2, Float.valueOf((float)Math.toRadians(180.0) + this.getSnapRotation()), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TLL", namedPointLocal2, Float.valueOf((float)Math.toRadians(180.0) + this.getSnapRotation()), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("BLC", namedPointLocal, Float.valueOf(this.getSnapRotation()), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("BLL", namedPointLocal, Float.valueOf(this.getSnapRotation()), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TRC", namedPointLocal, Float.valueOf((float)Math.toRadians(180.0) + this.getSnapRotation()), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TRR", namedPointLocal, Float.valueOf((float)Math.toRadians(180.0) + this.getSnapRotation()), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("BRL", namedPointLocal2, Float.valueOf(this.getSnapRotation()), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TLR", namedPointLocal2, Float.valueOf((float)Math.toRadians(180.0) + this.getSnapRotation()), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("BLR", namedPointLocal, Float.valueOf(this.getSnapRotation()), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TRL", namedPointLocal, Float.valueOf((float)Math.toRadians(180.0) + this.getSnapRotation()), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TLWS", namedPointLocal2, Float.valueOf(this.getSnapRotation()), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TRWS", namedPointLocal, Float.valueOf(this.getSnapRotation()), 20.0f, true);
        return addSimpleSnappablesThatDoNotFilterOccupiedTargets;
    }
    
    protected void addSimpleSnapTargets(final SimpleSnapTargetCollection collection) {
        super.addSimpleSnapTargets(collection);
        collection.addWorldSpaceSnapPoint("TLWS", this.getNamedPointWorld("Top_Left_Snap_Corner"), Float.valueOf(this.getRotationWorldSpace() + this.getSnapRotation()));
        collection.addWorldSpaceSnapPoint("TRWS", this.getNamedPointWorld("Top_Right_Snap_Corner"), Float.valueOf(this.getRotationWorldSpace() + this.getSnapRotation()));
    }
    
    @Override
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("Top_Left_Snap_Corner").set(0.0f, 0.0f, 0.0f);
        this.getNamedPointLocal("Top_Right_Snap_Corner").set(this.getXDimension(), 0.0f, 0.0f);
    }
    
    @Override
    public BoundingCube getCubeForTriggers() {
        return null;
    }
}
