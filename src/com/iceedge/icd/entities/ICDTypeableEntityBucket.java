// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.entities;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.undo.iceobjects.Point3fWithUndo;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icelib.main.TypeableEntity;

@Deprecated
public class ICDTypeableEntityBucket extends TypeableEntity implements ICDManufacturingReportable
{
    private static final long serialVersionUID = 1514271399777455644L;
    
    public ICDTypeableEntityBucket(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject, final Point3fWithUndo point3fWithUndo) {
        super(s, s2, typeObject, optionObject, point3fWithUndo);
    }
    
    public ICDTypeableEntityBucket(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public ICDTypeableEntityBucket(final TypeObject typeObject) {
        super(typeObject);
    }
    
    public EntityObject buildClone(final ICDTypeableEntityBucket icdTypeableEntityBucket) {
        super.buildClone((TypeableEntity)icdTypeableEntityBucket);
        return (EntityObject)icdTypeableEntityBucket;
    }
    
    public EntityObject buildClone2(final ICDTypeableEntityBucket icdTypeableEntityBucket) {
        super.buildClone2((TypeableEntity)icdTypeableEntityBucket);
        return (EntityObject)icdTypeableEntityBucket;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDTypeableEntityBucket(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDTypeableEntityBucket(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDTypeableEntityBucket icdTypeableEntityBucket, final EntityObject entityObject) {
        super.buildFrameClone((TypeableEntity)icdTypeableEntityBucket, entityObject);
        return (EntityObject)icdTypeableEntityBucket;
    }
    
    public void solve() {
        final Solution mainSolution = this.getMainSolution();
        if (mainSolution != null && mainSolution.isMainSolution()) {
            final String attributeValueAsString = this.getAttributeValueAsString("ICD_Record_Id", "");
            final String attributeValueAsString2 = this.getAttributeValueAsString("ICD_Rev_Number", "");
            if (!attributeValueAsString.equals("")) {
                mainSolution.applyChangesForAttribute("ICD_Record_Id", attributeValueAsString);
                mainSolution.applyChangesForAttribute("ICD_Rev_Number", attributeValueAsString2);
            }
            this.destroy();
        }
        super.solve();
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
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
