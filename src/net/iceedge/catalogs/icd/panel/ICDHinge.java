package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.icecore.icecad.ice.tree.IceCadBlock;
import net.iceedge.icecore.icecad.ice.tree.IceCadNodeContainer;
import net.iceedge.icecore.icecad.ice.IceCadIceApp;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icecad.cadtree.ICadBlockNode;
import net.dirtt.icelib.main.ElevationEntity;
import net.iceedge.catalogs.icd.ICDUtilities;
import java.util.Collection;
import net.iceedge.catalogs.icd.constants.ICDTypeNameConstants;
import javax.vecmath.Point3f;
import net.dirtt.utilities.TypeFilter;
import net.iceedge.catalogs.icd.ICDTileFilter;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;

public class ICDHinge extends TransformableEntity implements ICDManufacturingReportable
{
    private static final float HingeHeightDifference = 2.0f;
    private static final float hingleLength = 23.0f;
    private static final float hingeMaxLength = 72.0f;
    private static final float hingeMinLength = 4.0f;
    private static final String NAMED_SCALE = "namedScale";
    
    public ICDHinge(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDHinge(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDHinge buildClone(final ICDHinge icdHinge) {
        super.buildClone((TransformableEntity)icdHinge);
        return icdHinge;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDHinge(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDHinge buildFrameClone(final ICDHinge icdHinge, final EntityObject entityObject) {
        super.buildFrameClone((TransformableEntity)icdHinge, entityObject);
        return icdHinge;
    }
    
    protected void calculateNamedPoints() {
        final ICDTile icdTile = (ICDTile)this.getParent((TypeFilter)new ICDTileFilter());
        if (icdTile != null) {
            this.setBasePoint(this.getHingePosition(icdTile));
        }
        super.calculateNamedPoints();
    }
    
    private Point3f getHingePosition(final ICDTile icdTile) {
        final Point3f point3f = new Point3f();
        if (this.getParentEntity() instanceof ICDSubTile) {
            if (((ICDSubTile)this.getParentEntity()).getLwTypeCreatedFrom().getId().equals(ICDTypeNameConstants.ICD_VALET_DOOR_LH_TYPE)) {
                point3f.set(0.0f, icdTile.getYDimension() / 2.0f + 1.0f, 0.0f);
            }
            else {
                point3f.set(icdTile.getXDimension() / 2.0f, icdTile.getYDimension() / 2.0f + 1.0f, 0.0f);
            }
        }
        else {
            point3f.set(icdTile.isRightHanded() ? (icdTile.getXDimension() + 1.0f) : 0.0f, icdTile.getYDimension() / 2.0f + 1.0f, 0.0f);
        }
        return point3f;
    }
    
    public float getLength() {
        return this.getHeight();
    }
    
    public float getHeight() {
        float n = 0.0f;
        if (this.getParent() != null && this.getParent() instanceof ICDTile) {
            n = ((ICDTile)this.getParent()).getHeight() - 2.0f;
        }
        else if (this.getParent() != null && this.getParent() instanceof ICDSubTile) {
            n = ((ICDSubTile)this.getParent()).getHeight() - 2.0f;
        }
        if (n > 72.0f) {
            n = 72.0f;
        }
        if (n < 4.0f) {
            n = 4.0f;
        }
        return n;
    }
    
    public boolean draw2D() {
        return false;
    }
    
    public boolean drawCAD() {
        return false;
    }
    
    public void afterSolve(final Collection<EntityObject> collection) {
        super.afterSolve((Collection)collection);
        ICDUtilities.validateShowInManufacturingReport(this);
    }
    
    public void drawCadElevation(final ElevationEntity elevationEntity, final ICadBlockNode cadBlockNode, final int n, final SolutionSetting solutionSetting) {
    }
    
    public void drawIceCadElevationDotNet(final ElevationEntity elevationEntity, final IceCadIceApp iceCadIceApp, final IceCadNodeContainer iceCadNodeContainer, final IceCadBlock iceCadBlock, final int n) {
        this.setNamedScale("namedScale", 0.0f, this.getHeight() / 23.0f - 1.0f, 0.0f);
        super.drawIceCadElevationDotNet(elevationEntity, iceCadIceApp, iceCadNodeContainer, iceCadBlock, n);
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addDimensionsToManufacturingTreeMap(treeMap, this);
    }
    
    public String getManufacturingReportMaterialOptID() {
        return "Option0";
    }
    
    public void addManufacturingInfoToTreeMap(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addManufacturingInfoToTreeMap(treeMap, (ManufacturingReportable)this);
    }
    
    public String getDescriptionForManufacturingReport() {
        return this.getDescription();
    }
    
    protected boolean validateEntityParent() {
        boolean validateEntityParent = super.validateEntityParent();
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity == null || parentEntity instanceof Solution) {
            validateEntityParent = false;
        }
        return validateEntityParent;
    }
}
