package net.iceedge.catalogs.icd.electrical;

import java.util.Iterator;
import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.icecore.basemodule.baseclasses.electrical.path.ElectricalGraphEdge;
import net.iceedge.icecore.basemodule.baseclasses.electrical.path.ElectricalGraphNode;
import net.iceedge.icecore.basemodule.baseclasses.electrical.path.ElectricalGraphInternalEdge;
import net.iceedge.icecore.basemodule.baseclasses.electrical.path.ElectricalWeightedGraph;
import net.iceedge.icecore.basemodule.finalclasses.GeneralSnapSet;
import net.dirtt.utilities.EnumerationIterator;
import java.util.Vector;
import net.iceedge.icecore.basemodule.baseclasses.electrical.intent.BasicPowerIntent;
import java.util.Collection;
import net.dirtt.icelib.main.TypeableEntity;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.electrical.BasicElectricalCableSolver;

public class ICDElectricalCableSolver extends BasicElectricalCableSolver implements ICDManufacturingReportable
{
    public ICDElectricalCableSolver(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDElectricalCableSolver(this.getId(), this.getCurrentType(), this.getCurrentOption()), entityObject);
    }
    
    public EntityObject buildFrameClone(final ICDElectricalCableSolver icdElectricalCableSolver, final EntityObject entityObject) {
        super.buildFrameClone((BasicElectricalCableSolver)icdElectricalCableSolver, entityObject);
        return (EntityObject)icdElectricalCableSolver;
    }
    
    public Object clone() {
        return this.buildClone(new ICDElectricalCableSolver(this.getId(), this.currentType, this.currentOption));
    }
    
    public TypeableEntity buildClone(final ICDElectricalCableSolver icdElectricalCableSolver) {
        super.buildClone((BasicElectricalCableSolver)icdElectricalCableSolver);
        return (TypeableEntity)icdElectricalCableSolver;
    }
    
    public Collection<BasicPowerIntent> getPowerIntentsWithinSnapSetWithRealPower() {
        final Vector<BasicPowerIntent> vector = new Vector<BasicPowerIntent>();
        final GeneralSnapSet generalSnapSet = this.getGeneralSnapSet();
        if (generalSnapSet != null) {
            final EnumerationIterator enumerationIterator = new EnumerationIterator(((EntityObject)generalSnapSet).breadthFirstEnumeration());
            while (((Iterator)enumerationIterator).hasNext()) {
                final BasicPowerIntent next = ((Iterator<BasicPowerIntent>)enumerationIterator).next();
                if (next instanceof BasicPowerIntent && next.getRealEntity() != null) {
                    vector.add(next);
                }
            }
        }
        return vector;
    }
    
    protected float getAdditionalLength(final ElectricalWeightedGraph electricalWeightedGraph, final ElectricalGraphInternalEdge electricalGraphInternalEdge, final boolean b) {
        float n = 0.0f;
        ElectricalGraphEdge electricalGraphEdge;
        if (b) {
            electricalGraphEdge = electricalWeightedGraph.getEdgeThatEndsWith((ElectricalGraphNode)electricalGraphInternalEdge.getStartNode(), false);
        }
        else {
            electricalGraphEdge = electricalWeightedGraph.getEdgeThatStartsWith((ElectricalGraphNode)electricalGraphInternalEdge.getDestinationNode(), false);
        }
        if (electricalGraphEdge != null) {
            ElectricalGraphNode electricalGraphNode;
            if (b) {
                electricalGraphNode = (ElectricalGraphNode)electricalGraphEdge.getStartNode();
            }
            else {
                electricalGraphNode = (ElectricalGraphNode)electricalGraphEdge.getDestinationNode();
            }
            if (electricalGraphNode != null && electricalGraphNode.getEntity() instanceof ICDElectricalCable) {
                n += ((ICDElectricalCable)electricalGraphNode.getEntity()).getConnectorExtensionWidth();
            }
        }
        return n;
    }
    
    protected float getAdditionalLength(final ElectricalGraphInternalEdge electricalGraphInternalEdge) {
        float n = 0.0f;
        if (((ElectricalGraphNode)electricalGraphInternalEdge.getStartNode()).getEntity() instanceof ICDElectricalCable) {
            n += ((ICDElectricalCable)((ElectricalGraphNode)electricalGraphInternalEdge.getStartNode()).getEntity()).getConnectorExtensionWidth();
        }
        return n;
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
