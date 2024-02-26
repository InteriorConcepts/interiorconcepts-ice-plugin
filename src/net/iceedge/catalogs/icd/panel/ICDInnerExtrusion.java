// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.catalogs.icd.ICDUtilities;
import net.dirtt.icelib.main.TransformableEntity;
import icd.warnings.WarningReason0282;
import net.dirtt.icelib.report.Report;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import com.iceedge.icd.entities.extrusions.ICDExtrusionInterface;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicExtrusion;

public class ICDInnerExtrusion extends BasicExtrusion implements JointIntersectable, ICDExtrusionInterface, ICDManufacturingReportable
{
    private static String END_POINT;
    
    public ICDInnerExtrusion(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDInnerExtrusion(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDInnerExtrusion buildClone(final ICDInnerExtrusion icdInnerExtrusion) {
        super.buildClone((BasicExtrusion)icdInnerExtrusion);
        return icdInnerExtrusion;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDInnerExtrusion(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDInnerExtrusion buildFrameClone(final ICDInnerExtrusion icdInnerExtrusion, final EntityObject entityObject) {
        super.buildFrameClone((BasicExtrusion)icdInnerExtrusion, entityObject);
        return icdInnerExtrusion;
    }
    
    public void setupNamedPoints() {
        super.setupNamedPoints();
        this.addNamedPoint(ICDInnerExtrusion.END_POINT, new Point3f());
        this.addNamedPoint("TopMolding_POS", new Point3f());
        this.addNamedPoint("BottomMolding_POS", new Point3f());
        this.addNamedPoint("EndMolding_POS", new Point3f());
        this.addNamedPoint("StartMolding_POS", new Point3f());
        this.addNamedScale("TopMolding_SCL", new Vector3f(1.0f, 1.0f, 1.0f));
        this.addNamedScale("BottomMolding_SCL", new Vector3f(1.0f, 1.0f, 1.0f));
        this.addNamedScale("EndMolding_SCL", new Vector3f(1.0f, 1.0f, 1.0f));
        this.addNamedScale("StartMolding_SCL", new Vector3f(1.0f, 1.0f, 1.0f));
        this.addNamedRotation("TopMolding_ROT", new Vector3f(0.0f, 0.0f, 0.0f));
        this.addNamedRotation("BottomMolding_ROT", new Vector3f(0.0f, 0.0f, 0.0f));
        this.addNamedRotation("EndMolding_ROT", new Vector3f(0.0f, 0.0f, 0.0f));
        this.addNamedRotation("StartMolding_ROT", new Vector3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("StartMolding_ElevationIP", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("EndMolding_ElevationIP", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("TopMolding_ElevationIP", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("BottomMolding_ElevationIP", new Point3f(0.0f, 0.0f, 0.0f));
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.calculateOffset(ICDInnerExtrusion.END_POINT, this.getAttributeValueAsString("innerExtrusionEndPoint"), false);
        final float attributeValueAsFloat = this.getAttributeValueAsFloat("X_Position_Offset");
        final float attributeValueAsFloat2 = this.getAttributeValueAsFloat("Y_Position_Offset");
        final float n = this.getLength() / 2.0f + this.getAttributeValueAsFloat("Z_Position_Offset");
        this.getNamedPointLocal("TopMolding_POS").set((Tuple3f)new Point3f(attributeValueAsFloat + 0.11f, attributeValueAsFloat2, n));
        this.getNamedPointLocal("BottomMolding_POS").set((Tuple3f)new Point3f(attributeValueAsFloat, attributeValueAsFloat2, n));
        this.getNamedPointLocal("EndMolding_POS").set((Tuple3f)new Point3f(attributeValueAsFloat, attributeValueAsFloat2, n));
        this.getNamedPointLocal("StartMolding_POS").set((Tuple3f)new Point3f(attributeValueAsFloat, attributeValueAsFloat2, n));
        this.getNamedPointLocal("StartMolding_ElevationIP").set(-0.75f, 0.0f, -0.25f);
        this.getNamedPointLocal("EndMolding_ElevationIP").set(0.0f, 0.0f, -0.25f);
        this.getNamedPointLocal("TopMolding_ElevationIP").set(0.0f, 0.0f, -1.0f);
        this.getNamedPointLocal("BottomMolding_ElevationIP").set(-0.75f, 0.0f, -0.25f);
        final ICDTile icdTile = (ICDTile)this.getParent((Class)ICDTile.class);
        if (icdTile != null && icdTile.isBottomTile()) {
            this.getNamedPointLocal("TopMolding_POS").set((Tuple3f)new Point3f(0.85f, attributeValueAsFloat2, this.getLength() / 2.0f - 1.0f));
            this.getNamedPointLocal("BottomMolding_POS").set((Tuple3f)new Point3f(-0.4f, attributeValueAsFloat2, n));
        }
        if (icdTile != null && icdTile instanceof ICDCurvedTile) {
            this.getNamedPointLocal("EndMolding_POS").set((Tuple3f)new Point3f(attributeValueAsFloat - 0.75f, attributeValueAsFloat2, n));
            this.getNamedPointLocal("StartMolding_POS").set((Tuple3f)new Point3f(attributeValueAsFloat + 0.25f, attributeValueAsFloat2 - 0.25f, n));
            this.getNamedPointLocal("TopMolding_POS").set((Tuple3f)new Point3f(0.85f, attributeValueAsFloat2, this.getLength() / 2.0f - 1.0f - 2.05f));
            this.getNamedPointLocal("BottomMolding_POS").set((Tuple3f)new Point3f(-0.4f, attributeValueAsFloat2, n - 2.1f));
        }
    }
    
    private void calculateOffset(final String s, final String s2, final boolean b) {
        if (s2 != null) {
            final ICDTile icdTile = (ICDTile)this.getParent((Class)ICDTile.class);
            if (icdTile != null) {
                final Point3f namedPointWorld = icdTile.getNamedPointWorld(s2);
                if (namedPointWorld != null) {
                    final Point3f convertPointToLocal = this.convertPointToLocal(namedPointWorld);
                    float attributeValueAsFloat = this.getAttributeValueAsFloat("innerExtrusionHorizontalOffset");
                    if (b) {
                        attributeValueAsFloat *= -1.0f;
                    }
                    this.getNamedPointLocal(s).set(convertPointToLocal.x, convertPointToLocal.y, convertPointToLocal.z + attributeValueAsFloat);
                }
            }
        }
    }
    
    protected void calculateNamedScales() {
        super.calculateNamedScales();
        this.getNamedScaleLocal("TopMolding_SCL").set((Tuple3f)new Vector3f(this.getLength(), 1.0f, 1.0f));
        this.getNamedScaleLocal("BottomMolding_SCL").set((Tuple3f)new Vector3f(this.getLength(), 1.0f, 1.0f));
        this.getNamedScaleLocal("EndMolding_SCL").set((Tuple3f)new Vector3f(this.getLength(), 1.0f, 1.0f));
        this.getNamedScaleLocal("StartMolding_SCL").set((Tuple3f)new Vector3f(this.getLength(), 1.0f, 1.0f));
        final ICDTile icdTile = (ICDTile)this.getParent((Class)ICDTile.class);
        if (icdTile != null && icdTile.isSlopedTile()) {
            this.getNamedScaleLocal("StartMolding_SCL").set((Tuple3f)new Vector3f(this.getLength() * 1.02f, 1.0f, 1.0f));
            this.getNamedScaleLocal("EndMolding_SCL").set((Tuple3f)new Vector3f(this.getLength() * 1.1f, 1.0f, 1.0f));
            if (icdTile.getAttributeValueAsFloat("ICD_Slope_Tile_Width") == 24.0f) {
                this.getNamedScaleLocal("EndMolding_SCL").set((Tuple3f)new Vector3f(this.getLength() * 1.04f, 1.0f, 1.0f));
            }
        }
    }
    
    protected void calculateNamedRotations() {
        super.calculateNamedScales();
        this.getNamedRotationLocal("TopMolding_ROT").set((Tuple3f)new Vector3f(1.57f, 0.0f, 1.57f));
        this.getNamedRotationLocal("BottomMolding_ROT").set((Tuple3f)new Vector3f(1.57f, 0.0f, -1.57f));
        this.getNamedRotationLocal("EndMolding_ROT").set((Tuple3f)new Vector3f(1.57f, 0.0f, 1.57f));
        this.getNamedRotationLocal("StartMolding_ROT").set((Tuple3f)new Vector3f(1.57f, 0.0f, -1.57f));
        if (this.isSlopedTileMoulding()) {
            this.getNamedRotationLocal("TopMolding_ROT").set((Tuple3f)new Vector3f(1.57f, 0.003f, 1.947f));
        }
        final ICDTile icdTile = (ICDTile)this.getParent((Class)ICDTile.class);
        if (icdTile != null && icdTile instanceof ICDCurvedTile) {
            this.getNamedRotationLocal("StartMolding_ROT").set((Tuple3f)new Vector3f(1.57f, 1.57f, -1.57f));
        }
    }
    
    public float getLength() {
        if (!this.isStickExtrusion()) {
            float zDimension = this.getZDimension();
            final String id = this.getCurrentOption().getId();
            if ("ICDTileMoldTopCurved".equals(id) || "ICDTileMoldBottomCurved".equals(id)) {
                zDimension = 52.0f;
            }
            return zDimension;
        }
        final ICDTile icdTile = (ICDTile)this.getParent((Class)ICDTile.class);
        if (this.isVertical()) {
            return icdTile.getCutLength();
        }
        return icdTile.getWidth() - 1.0f;
    }
    
    protected void calculateDimensions() {
        super.calculateDimensions();
        this.setZDimension((float)Math.round(Math.abs(this.getBasePoint().getZ() + this.getNamedPointLocal(ICDInnerExtrusion.END_POINT).z)));
        final ICDTile icdTile = (ICDTile)this.getParent((Class)ICDTile.class);
        if (icdTile != null) {
            this.setYDimension(icdTile.getThickness());
            if (this.isStickExtrusion()) {
                this.setYDimension(0.475f);
                if (this.isVertical()) {
                    this.setZDimension(icdTile.getLength() - icdTile.getFabricPullback());
                }
                else {
                    this.setZDimension(icdTile.getWidth() - 1.0f);
                }
            }
            else if (icdTile.isSlopedTile()) {
                this.setZDimension(this.getAttributeValueAsFloat("ICD_Slope_Tile_Molding_Length"));
            }
            else if (this.isVertical()) {
                this.setZDimension(icdTile.getLength() + 0.5f);
            }
            else {
                this.setZDimension(icdTile.getWidth() + 0.5f);
            }
        }
    }
    
    public boolean isVertical() {
        return this.getAttributeValueAsBoolean("isVerticalExtrusion", false);
    }
    
    public boolean shouldICDMakePreAssembledReport() {
        return !"true".equals(this.getAttributeValueAsString("isAssembled"));
    }
    
    public boolean draw2D() {
        return false;
    }
    
    public void draw2D(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        super.draw2D(n, ice2DContainer, solutionSetting);
        this.getSolution();
    }
    
    public boolean usePlotGVT() {
        return false;
    }
    
    public boolean isQuoteable(final String s) {
        return false;
    }
    
    public void destroy2D() {
        super.destroy2D();
    }
    
    public boolean isStickExtrusion() {
        return this.getAttributeValueAsBoolean("isTileStick", false);
    }
    
    public void cutFromTree2D() {
        super.cutFromTree2D();
    }
    
    public boolean shouldAddChildrenToReport(final Report report) {
        return false;
    }
    
    public boolean useTransformablesDraw3D() {
        return true;
    }
    
    private boolean isSlopedTileMoulding() {
        final ICDTile icdTile = (ICDTile)this.getParent((Class)ICDTile.class);
        return icdTile != null && icdTile.isSlopedTile();
    }
    
    public boolean isMolding() {
        return this.getParent((Class)ICDTile.class) != null;
    }
    
    public boolean doesParticipateInJointIntersection() {
        return !this.isMolding();
    }
    
    public void handleWarnings() {
        WarningReason0282.addRequiredWarning((TransformableEntity)this);
    }
    
    public void handleAttributeChange(final String s, final String s2) {
        ICDUtilities.handleAttributeChange((EntityObject)this, s, s2);
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addDimensionsToManufacturingTreeMap(treeMap, (TransformableEntity)this);
    }
    
    public String getManufacturingReportMaterialOptID() {
        return "Option0";
    }
    
    public void addManufacturingInfoToTreeMap(final TreeMap<String, String> treeMap) {
        this.getManufacturingInfo(treeMap);
    }
    
    public String getDescriptionForManufacturingReport() {
        return this.getDescription() + ",Length:" + Math.round(this.getLength()) + " " + this.getReportDescription();
    }
    
    public void getManufacturingInfo(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addManufacturingInfoToTreeMapForExtrusion(treeMap, (ManufacturingReportable)this, this.getLength());
    }
    
    public String getReportDescription() {
        return ICDManufacturingUtils.getReportDescriptionForExtrusion((EntityObject)this);
    }
    
    static {
        ICDInnerExtrusion.END_POINT = "InnerExtrusionEnd";
    }
}
