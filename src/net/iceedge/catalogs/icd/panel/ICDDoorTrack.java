package net.iceedge.catalogs.icd.panel;

import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.report.Report;
import net.iceedge.icecore.basemodule.baseclasses.material.BasicMaterialEntity;
import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.report.compare.CompareNode;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;

public class ICDDoorTrack extends TransformableEntity implements ICDManufacturingReportable
{
    public ICDDoorTrack(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.setupNamedScales();
        this.setupNamedPoint();
    }
    
    public Object clone() {
        return this.buildClone(new ICDDoorTrack(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDDoorTrack buildClone(final ICDDoorTrack icdDoorTrack) {
        super.buildClone((TransformableEntity)icdDoorTrack);
        return icdDoorTrack;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDDoorTrack(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDDoorTrack buildFrameClone(final ICDDoorTrack icdDoorTrack, final EntityObject entityObject) {
        super.buildFrameClone((TransformableEntity)icdDoorTrack, entityObject);
        return icdDoorTrack;
    }
    
    public ICDTile getParentTile() {
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity instanceof ICDTile) {
            return (ICDTile)parentEntity;
        }
        return null;
    }
    
    protected void calculateDimensions() {
        super.calculateDimensions();
        final ICDTile parentTile = this.getParentTile();
        if (parentTile != null) {
            if (parentTile.isHeavyDutySlidingDoor()) {
                this.setXDimension(parentTile.getXDimension() - 0.25f);
            }
            else {
                this.setXDimension(parentTile.getXDimension());
            }
        }
    }
    
    public boolean isPrintable() {
        return false;
    }
    
    public boolean isQuotable() {
        return false;
    }
    
    public int getQuoteReportLevel() {
        return -1;
    }
    
    public void populateCompareNode(final Class clazz, final CompareNode compareNode) {
        super.populateCompareNode(clazz, compareNode);
        if (ManufacturingReportable.class.isAssignableFrom(clazz)) {
            this.populateCompareNodeForICD(clazz, compareNode);
        }
    }
    
    public void populateCompareNodeForICD(final Class clazz, final CompareNode compareNode) {
        compareNode.addCompareValue("length", (Object)Math.round(this.getWidth()));
        String description = "";
        if (((BasicMaterialEntity)this.getChildByClass((Class)BasicMaterialEntity.class)).getDescription() != null) {
            description = ((BasicMaterialEntity)this.getChildByClass((Class)BasicMaterialEntity.class)).getDescription();
        }
        compareNode.addCompareValue("finish", (Object)description);
        compareNode.addCompareValue("description", (Object)this.getDescription());
        compareNode.addCompareValue("usertag", (Object)this.getUserTagNameAttribute("TagName1"));
    }
    
    public void solve() {
        if (this.isModified()) {
            this.modifyCurrentOption();
            this.validateChildTypes();
        }
        super.solve();
    }
    
    public boolean shouldAddChildrenToReport(final Report report) {
        return false;
    }
    
    protected void calculateGeometricCenter() {
        super.calculateGeometricCenter();
        this.setGeometricCenterPointLocal(new Point3f(this.getXDimension() * 0.5f, this.getYDimension() * 0.5f, this.getZDimension() * 0.5f));
    }
    
    protected void calculateNamedScales() {
        super.calculateNamedScales();
        this.getNamedScaleLocal("ASE_SCL").set(this.getXDimension(), 1.0f, 1.0f);
    }
    
    protected void setupNamedScales() {
        this.addNamedScale("ASE_SCL", new Vector3f(1.0f, 1.0f, 1.0f));
    }
    
    public void setupNamedPoint() {
        this.addNamedPoint("ASE_POS", new Point3f());
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("ASE_POS").set(this.getXDimension() * 0.5f, 0.0f, 1.005f);
    }
    
    public boolean draw2D() {
        return super.draw2D();
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
}
