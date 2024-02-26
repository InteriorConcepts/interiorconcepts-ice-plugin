// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.electrical;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.icecore.basemodule.baseclasses.electrical.path.ElectricalGraphInternalEdge;
import net.iceedge.icecore.basemodule.baseclasses.electrical.path.ElectricalNode;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import net.iceedge.icecore.basemodule.interfaces.panels.RacewayInterface;
import net.iceedge.catalogs.icd.ICDSegment;
import net.iceedge.icecore.basemodule.baseclasses.electrical.BasicElectricalCable;
import net.iceedge.icecore.basemodule.baseclasses.electrical.path.util.ElectricalGraphUtilities;
import net.dirtt.utilities.MathUtilities;
import javax.vecmath.Point3f;
import java.util.Iterator;
import net.iceedge.catalogs.icd.panel.ICDPanel;
import net.iceedge.catalogs.icd.intersection.ICDIntersection;
import java.util.Vector;
import net.iceedge.icecore.basemodule.baseclasses.electrical.path.ElectricalGraphConnectorEdge;
import java.util.Collection;
import net.iceedge.icecore.basemodule.baseclasses.electrical.BasicElectricalCableSolver;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.electrical.path.ElectricalGraphContributor;
import net.iceedge.icecore.basemodule.baseclasses.electrical.BasicElectricalIntersectionArea;

public class ICDElectricalIntersectionArea extends BasicElectricalIntersectionArea implements ElectricalGraphContributor, ICDManufacturingReportable
{
    public ICDElectricalIntersectionArea(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.setupNamedPoints();
    }
    
    public TransformableEntity buildClone(final ICDElectricalIntersectionArea icdElectricalIntersectionArea) {
        super.buildClone((BasicElectricalIntersectionArea)icdElectricalIntersectionArea);
        return (TransformableEntity)icdElectricalIntersectionArea;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDElectricalIntersectionArea(this.getId(), this.getCurrentType(), this.getCurrentOption()), entityObject);
    }
    
    public TransformableEntity buildClone2(final ICDElectricalIntersectionArea icdElectricalIntersectionArea) {
        super.buildClone2((BasicElectricalIntersectionArea)icdElectricalIntersectionArea);
        return (TransformableEntity)icdElectricalIntersectionArea;
    }
    
    public Object clone() {
        return this.buildClone(new ICDElectricalIntersectionArea(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDElectricalIntersectionArea icdElectricalIntersectionArea, final EntityObject entityObject) {
        super.buildFrameClone((BasicElectricalIntersectionArea)icdElectricalIntersectionArea, entityObject);
        return (EntityObject)icdElectricalIntersectionArea;
    }
    
    public boolean applicableToEuroUnionSolveType(final String anObject) {
        return "SnapSet".equals(anObject);
    }
    
    public void calculateGraphNodes() {
    }
    
    public boolean canSpawnEdgesAndNodes() {
        return true;
    }
    
    public Collection<ElectricalGraphConnectorEdge> getEdges(final BasicElectricalCableSolver basicElectricalCableSolver) {
        final Vector<ElectricalGraphConnectorEdge> vector = new Vector<ElectricalGraphConnectorEdge>();
        final Vector<ICDPanel> panelsFromArms = this.getPanelsFromArms((ICDIntersection)this.getParent((Class)ICDIntersection.class));
        for (final ICDPanel icdPanel : panelsFromArms) {
            if (icdPanel.hasChase()) {
                final int indexNumberUsingPanel = this.getIndexNumberUsingPanel(icdPanel);
                if (indexNumberUsingPanel == -1) {
                    continue;
                }
                int n;
                int n2;
                int n3;
                if (indexNumberUsingPanel == 1) {
                    n = 4;
                    n2 = 2;
                    n3 = 3;
                }
                else if (indexNumberUsingPanel == 2) {
                    n = 1;
                    n2 = 3;
                    n3 = 4;
                }
                else if (indexNumberUsingPanel == 3) {
                    n = 2;
                    n2 = 4;
                    n3 = 1;
                }
                else {
                    n = 3;
                    n2 = 1;
                    n3 = 2;
                }
                final ICDPanel panelArmFromIndex = this.getPanelArmFromIndex(n, panelsFromArms);
                final ICDPanel panelArmFromIndex2 = this.getPanelArmFromIndex(n2, panelsFromArms);
                final ICDPanel panelArmFromIndex3 = this.getPanelArmFromIndex(n3, panelsFromArms);
                boolean addEdgeForPanels = false;
                boolean addEdgeForPanels2 = false;
                if (panelArmFromIndex != null && this.checkArmOnSameSideAsRequestingChase(icdPanel, panelArmFromIndex, this.getNamedPointLocal("Arm" + n), this.getNamedPointLocal("Arm" + indexNumberUsingPanel))) {
                    addEdgeForPanels = this.addEdgeForPanels(vector, icdPanel, panelArmFromIndex);
                }
                if (panelArmFromIndex2 != null && this.checkArmOnSameSideAsRequestingChase(icdPanel, panelArmFromIndex2, this.getNamedPointLocal("Arm" + n2), this.getNamedPointLocal("Arm" + indexNumberUsingPanel))) {
                    addEdgeForPanels2 = this.addEdgeForPanels(vector, icdPanel, panelArmFromIndex2);
                }
                if (panelArmFromIndex3 == null || !panelArmFromIndex3.hasChase() || (!this.sameSideChaseOnTwoArms(icdPanel, panelArmFromIndex3) && (!addEdgeForPanels || !this.panelHasChaseOnSameSideAsArm(panelArmFromIndex3, panelArmFromIndex, this.getNamedPointLocal("Arm" + indexNumberUsingPanel), this.getNamedPointLocal("Arm" + n))) && (!addEdgeForPanels2 || !this.panelHasChaseOnSameSideAsArm(panelArmFromIndex3, panelArmFromIndex2, this.getNamedPointLocal("Arm" + indexNumberUsingPanel), this.getNamedPointLocal("Arm" + n2))))) {
                    continue;
                }
                this.addEdgeForPanels(vector, icdPanel, panelArmFromIndex3);
            }
        }
        return vector;
    }
    
    private boolean checkArmOnSameSideAsRequestingChase(final ICDPanel icdPanel, final ICDPanel icdPanel2, final Point3f point3f, final Point3f point3f2) {
        return icdPanel2.hasChaseOnPointSide(MathUtilities.convertSpaces(point3f2, (EntityObject)this, (EntityObject)icdPanel2)) || icdPanel.hasChaseOnPointSide(MathUtilities.convertSpaces(point3f, (EntityObject)this, (EntityObject)icdPanel));
    }
    
    private boolean panelHasChaseOnSameSideAsArm(final ICDPanel icdPanel, final ICDPanel icdPanel2, final Point3f point3f, final Point3f point3f2) {
        return icdPanel2.hasChaseOnPointSide(MathUtilities.convertSpaces(point3f, (EntityObject)this, (EntityObject)icdPanel2)) && icdPanel.hasChaseOnPointSide(MathUtilities.convertSpaces(point3f2, (EntityObject)this, (EntityObject)icdPanel));
    }
    
    private boolean sameSideChaseOnTwoArms(final ICDPanel icdPanel, final ICDPanel icdPanel2) {
        return (icdPanel.isChaseSideA() && icdPanel2.hasChaseOnPointSide(MathUtilities.convertSpaces(new Point3f(2.0f, 0.0f, 2.0f), (EntityObject)icdPanel, (EntityObject)icdPanel2))) || (icdPanel.isChaseSideB() && icdPanel2.hasChaseOnPointSide(MathUtilities.convertSpaces(new Point3f(2.0f, 0.0f, -2.0f), (EntityObject)icdPanel, (EntityObject)icdPanel2)));
    }
    
    private boolean addEdgeForPanels(final Vector<ElectricalGraphConnectorEdge> vector, final ICDPanel icdPanel, final ICDPanel icdPanel2) {
        final ElectricalGraphConnectorEdge buildConnectorEdgeForPanels = this.buildConnectorEdgeForPanels(icdPanel, icdPanel2);
        if (buildConnectorEdgeForPanels != null) {
            vector.add(buildConnectorEdgeForPanels);
            return true;
        }
        return false;
    }
    
    private ElectricalGraphConnectorEdge buildConnectorEdgeForPanels(final ICDPanel icdPanel, final ICDPanel icdPanel2) {
        final BasicElectricalCable cableFromPanel = this.getCableFromPanel(icdPanel2);
        final BasicElectricalCable cableFromPanel2 = this.getCableFromPanel(icdPanel);
        if (cableFromPanel != null && cableFromPanel2 != null) {
            return ElectricalGraphUtilities.createConnectorEdge(this.getSolution(), cableFromPanel.getLeftSideNode(), cableFromPanel.getRightSideNode(), cableFromPanel2.getLeftSideNode(), cableFromPanel2.getRightSideNode());
        }
        return null;
    }
    
    private BasicElectricalCable getCableFromPanel(final ICDPanel icdPanel) {
        BasicElectricalCable electricCable = null;
        final ICDSegment icdSegment = (ICDSegment)icdPanel.getParent((Class)ICDSegment.class);
        if (icdSegment != null) {
            final RacewayInterface raceWay = icdSegment.getRaceWay();
            if (raceWay != null) {
                electricCable = raceWay.getElectricCable();
            }
        }
        return electricCable;
    }
    
    private Vector<ICDPanel> getPanelsFromArms(final ICDIntersection icdIntersection) {
        final Vector<ICDPanel> vector = new Vector<ICDPanel>();
        if (icdIntersection != null) {
            final Iterator<Segment> iterator = icdIntersection.getSegmentsFromArms().iterator();
            while (iterator.hasNext()) {
                for (final ICDPanel e : iterator.next().getChildrenByClass((Class)ICDPanel.class, true, true)) {
                    if (e.isCorePanel()) {
                        vector.add(e);
                    }
                }
            }
        }
        return vector;
    }
    
    private int getIndexNumberUsingPanel(final ICDPanel icdPanel) {
        final int n = -1;
        for (int i = 1; i < 5; ++i) {
            final Point3f convertSpaces = MathUtilities.convertSpaces(this.getNamedPointLocal("Arm" + i), (EntityObject)this, (EntityObject)icdPanel);
            convertSpaces.y = 0.0f;
            if (icdPanel.getBoundingCube().contains(convertSpaces.x, convertSpaces.y, convertSpaces.z)) {
                return i;
            }
        }
        return n;
    }
    
    private ICDPanel getPanelArmFromIndex(final int i, final Vector<ICDPanel> vector) {
        for (final ICDPanel icdPanel : vector) {
            final Point3f convertSpaces = MathUtilities.convertSpaces(this.getNamedPointLocal("Arm" + i), (EntityObject)this, (EntityObject)icdPanel);
            convertSpaces.y = 0.0f;
            if (icdPanel.getBoundingCube().contains(convertSpaces.x, convertSpaces.y, convertSpaces.z)) {
                return icdPanel;
            }
        }
        return null;
    }
    
    public ElectricalNode getLeftSideNode() {
        return null;
    }
    
    public Collection<ElectricalNode> getNodes() {
        return new Vector<ElectricalNode>();
    }
    
    public Collection<ElectricalGraphInternalEdge> getOwnEdges() {
        return new Vector<ElectricalGraphInternalEdge>();
    }
    
    public String getRequiredCableSolver() {
        return "ICD_Electrical_Cable_Solver_Type";
    }
    
    public ElectricalNode getRightSideNode() {
        return null;
    }
    
    public void validateBasedOnGraph() {
    }
    
    protected void setupNamedPoints() {
        super.setupNamedPoints();
        this.addNamedPoint("Arm1", new Point3f(0.0f, 1.5f, 0.0f));
        this.addNamedPoint("Arm2", new Point3f(1.5f, 0.0f, 0.0f));
        this.addNamedPoint("Arm3", new Point3f(0.0f, -1.5f, 0.0f));
        this.addNamedPoint("Arm4", new Point3f(-1.5f, 0.0f, 0.0f));
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
