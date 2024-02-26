// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.main.Solution;
import net.iceedge.catalogs.icd.ICDUtilities;
import java.util.Collection;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;

public class ICDMagneticCatch extends TransformableEntity implements ICDManufacturingReportable
{
    public ICDMagneticCatch(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDMagneticCatch(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDMagneticCatch buildClone(final ICDMagneticCatch icdMagneticCatch) {
        super.buildClone((TransformableEntity)icdMagneticCatch);
        return icdMagneticCatch;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDMagneticCatch(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDMagneticCatch buildFrameClone(final ICDMagneticCatch icdMagneticCatch, final EntityObject entityObject) {
        super.buildFrameClone((TransformableEntity)icdMagneticCatch, entityObject);
        return icdMagneticCatch;
    }
    
    public void afterSolve(final Collection<EntityObject> collection) {
        super.afterSolve((Collection)collection);
        ICDUtilities.validateShowInManufacturingReport(this);
    }
    
    protected boolean validateEntityParent() {
        boolean validateEntityParent = super.validateEntityParent();
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity == null || parentEntity instanceof Solution) {
            validateEntityParent = false;
        }
        return validateEntityParent;
    }
    
    public boolean draw2D() {
        return false;
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
