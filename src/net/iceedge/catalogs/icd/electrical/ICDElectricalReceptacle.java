// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.electrical;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import javax.vecmath.Point3f;
import net.iceedge.catalogs.icd.panel.ICDPanel;
import net.iceedge.catalogs.icd.elevation.isometric.ICDIsometricAssemblyElevationEntity;
import java.util.Vector;
import net.dirtt.icelib.main.ElevationEntity;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.electrical.BasicElectricalReceptacle;

public class ICDElectricalReceptacle extends BasicElectricalReceptacle implements ICDManufacturingReportable
{
    public ICDElectricalReceptacle(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.setupNamedPoints();
    }
    
    public TransformableEntity buildClone(final ICDElectricalReceptacle icdElectricalReceptacle) {
        super.buildClone((BasicElectricalReceptacle)icdElectricalReceptacle);
        return (TransformableEntity)icdElectricalReceptacle;
    }
    
    public TransformableEntity buildClone2(final ICDElectricalReceptacle icdElectricalReceptacle) {
        super.buildClone2((BasicElectricalReceptacle)icdElectricalReceptacle);
        return (TransformableEntity)icdElectricalReceptacle;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDElectricalReceptacle(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDElectricalReceptacle(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDElectricalReceptacle icdElectricalReceptacle, final EntityObject entityObject) {
        super.buildFrameClone((BasicElectricalReceptacle)icdElectricalReceptacle, entityObject);
        return (EntityObject)icdElectricalReceptacle;
    }
    
    public String getRequiredCableSolver() {
        return "ICD_Electrical_Cable_Solver_Type";
    }
    
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        final Vector<String> vector = new Vector<String>();
        if (elevationEntity instanceof ICDIsometricAssemblyElevationEntity && this.isRealReceptacle()) {
            final float n = 2.0f;
            final float n2 = -4.0f;
            vector.add("LI:SS(6:CAD_POS(" + n2 + ",0,0):CAD_POS(" + n2 + ",0," + n + "))");
            vector.add("LI:SS(6:CAD_POS(" + (n2 + n) + ",0,0):CAD_POS(" + (n2 + n) + ",0," + n + "))");
            vector.add("LI:SS(6:CAD_POS(" + n2 + "," + n + ",0):CAD_POS(" + n2 + "," + n + "," + n + "))");
            vector.add("LI:SS(6:CAD_POS(" + (n2 + n) + "," + n + ",0):CAD_POS(" + (n2 + n) + "," + n + "," + n + "))");
            vector.add("LI:SS(6:CAD_POS(" + n2 + ",0,0):CAD_POS(" + (n2 + n) + ",0,0))");
            vector.add("LI:SS(6:CAD_POS(" + n2 + ",0,0):CAD_POS(" + n2 + "," + n + ",0))");
            vector.add("LI:SS(6:CAD_POS(" + (n2 + n) + ",0,0):CAD_POS(" + (n2 + n) + "," + n + ",0))");
            vector.add("LI:SS(6:CAD_POS(" + n2 + "," + n + ",0):CAD_POS(" + (n2 + n) + "," + n + ",0))");
            vector.add("LI:SS(6:CAD_POS(" + n2 + ",0," + n + "):CAD_POS(" + (n2 + n) + ",0," + n + "))");
            vector.add("LI:SS(6:CAD_POS(" + n2 + ",0," + n + "):CAD_POS(" + n2 + "," + n + "," + n + "))");
            vector.add("LI:SS(6:CAD_POS(" + (n2 + n) + ",0," + n + "):CAD_POS(" + (n2 + n) + "," + n + "," + n + "))");
            vector.add("LI:SS(6:CAD_POS(" + n2 + "," + n + "," + n + "):CAD_POS(" + (n2 + n) + "," + n + "," + n + "))");
        }
        return vector;
    }
    
    private boolean isRealReceptacle() {
        return this.getAttributeValueAsBoolean("ICD_Is_Real_Receptacle", false);
    }
    
    protected boolean shouldDrawCadElevation(final ElevationEntity elevationEntity) {
        return elevationEntity instanceof ICDIsometricAssemblyElevationEntity;
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        final ICDPanel icdPanel = (ICDPanel)this.getParentByClassRecursive((Class)ICDPanel.class);
        if (icdPanel != null) {
            float n;
            if (icdPanel.hasChase(0)) {
                n = icdPanel.getChaseOffset(0);
            }
            else {
                n = icdPanel.getChaseOffset(1);
            }
            this.getNamedPointLocal("CAD_POS").set(n, 0.0f, 0.0f);
        }
    }
    
    protected void setupNamedPoints() {
        super.setupNamedPoints();
        this.addNamedPoint("CAD_POS", new Point3f(0.0f, 0.0f, 0.0f));
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addDimensionsToManufacturingTreeMap(treeMap, (TransformableEntity)this);
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
