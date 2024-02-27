package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import java.util.HashSet;
import net.iceedge.icecore.basemodule.interfaces.panels.SideGroupInterface;
import net.iceedge.icecore.basemodule.interfaces.SegmentBase;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelSegmentInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.TileGroupInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.TopExtrusionInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.FrameInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelInterface;
import java.util.Iterator;
import net.iceedge.icecore.basemodule.interfaces.panels.TileInterface;
import java.util.Vector;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicTileGroup;

public class ICDTileGroup extends BasicTileGroup implements ICDManufacturingReportable
{
    public ICDTileGroup(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDTileGroup(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDTileGroup buildClone(final ICDTileGroup icdTileGroup) {
        super.buildClone((BasicTileGroup)icdTileGroup);
        return icdTileGroup;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDTileGroup(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDTileGroup buildFrameClone(final ICDTileGroup icdTileGroup, final EntityObject entityObject) {
        super.buildFrameClone((BasicTileGroup)icdTileGroup, entityObject);
        return icdTileGroup;
    }
    
    public void solve() {
        super.solve();
        final Vector allTiles = this.getAllTiles();
        if (allTiles != null && allTiles.size() > 0) {
            this.removeBreakTopExtrusion();
            this.breakTopExtrusion();
        }
    }
    
    private void breakTopExtrusion() {
        for (final TileInterface tileInterface : this.getAllTiles()) {
            if (tileInterface instanceof ICDTile) {
                ((ICDTile)tileInterface).breakTopExtrusion();
                break;
            }
        }
    }
    
    public void removeBreakTopExtrusion() {
        final PanelInterface parentPanel = this.getParentPanel();
        if (parentPanel != null && !parentPanel.isCorePanel()) {
            final FrameInterface physicalFrame = parentPanel.getPhysicalFrame();
            if (physicalFrame != null) {
                final TopExtrusionInterface topExtrusion = physicalFrame.getTopExtrusion();
                if (topExtrusion instanceof ICDTopExtrusion) {
                    ((ICDTopExtrusion)topExtrusion).breakHorizontalExtrusion(0.0f, true);
                }
            }
        }
        if (parentPanel != null && parentPanel.isCorePanel()) {
            final TileGroupInterface stackingTileGroup = this.getStackingTileGroup(parentPanel);
            if (stackingTileGroup instanceof ICDTileGroup || stackingTileGroup == null) {
                final FrameInterface physicalFrame2 = parentPanel.getPhysicalFrame();
                if (physicalFrame2 != null) {
                    final TopExtrusionInterface topExtrusion2 = physicalFrame2.getTopExtrusion();
                    if (topExtrusion2 instanceof ICDTopExtrusion) {
                        ((ICDTopExtrusion)topExtrusion2).breakHorizontalExtrusion(0.0f, true);
                    }
                }
            }
        }
    }
    
    private TileGroupInterface getStackingTileGroup(final PanelInterface panelInterface) {
        final PanelSegmentInterface parentPanelSegment = panelInterface.getParentPanelSegment();
        if (parentPanelSegment != null) {
            final SegmentBase segmentAfter = parentPanelSegment.getSegmentAfter();
            if (segmentAfter instanceof PanelSegmentInterface) {
                final PanelInterface childPanel = ((PanelSegmentInterface)segmentAfter).getChildPanel();
                if (childPanel != null) {
                    final SideGroupInterface sideGroup = childPanel.getSideGroup(this.getSide());
                    if (sideGroup != null) {
                        return sideGroup.getTileGroup();
                    }
                }
            }
        }
        return null;
    }
    
    private boolean hasTileWithoutFrame() {
        final PanelInterface parentPanel = this.getParentPanel();
        if (parentPanel != null && !parentPanel.isCorePanel()) {
            final Iterator<TileInterface> iterator = this.getAllTiles().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().withoutFrame()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean usePlotGVT() {
        return false;
    }
    
    public void collectExtraIndirectAssemblyParts(final boolean b, final HashSet<EntityObject> set, final boolean b2, final Class<EntityObject>[] array) {
        for (final TileInterface tileInterface : this.getAllTiles()) {
            if (tileInterface instanceof ICDTile) {
                ((ICDTile)tileInterface).collectExtraIndirectAssemblyParts(b, set, b2, array);
            }
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
