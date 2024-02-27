package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.icecore.basemodule.interfaces.panels.TileGroupInterface;
import java.util.HashSet;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicSideGroup;

public class ICDSideGroup extends BasicSideGroup implements ICDManufacturingReportable
{
    public ICDSideGroup(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public ICDSideGroup buildClone(final ICDSideGroup icdSideGroup) {
        super.buildClone((BasicSideGroup)icdSideGroup);
        return icdSideGroup;
    }
    
    public Object clone() {
        return this.buildClone(new ICDSideGroup(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDSideGroup(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public EntityObject buildFrameClone(final BasicSideGroup basicSideGroup, final EntityObject entityObject) {
        super.buildFrameClone(basicSideGroup, entityObject);
        return (EntityObject)basicSideGroup;
    }
    
    public void collectExtraIndirectAssemblyParts(final boolean b, final HashSet<EntityObject> set, final boolean b2, final Class<EntityObject>[] array) {
        final TileGroupInterface tileGroup = this.getTileGroup();
        if (tileGroup instanceof ICDTileGroup) {
            ((ICDTileGroup)tileGroup).collectExtraIndirectAssemblyParts(b, set, b2, array);
        }
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
