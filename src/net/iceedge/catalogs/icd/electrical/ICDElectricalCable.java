// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.electrical;

import net.iceedge.catalogs.icd.panel.ICDRaceway;
import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import icd.warnings.WarningReason0363;
import net.iceedge.catalogs.icd.elevation.isometric.ICDIsometricAssemblyElevationEntity;
import java.util.Vector;
import net.dirtt.icelib.main.ElevationEntity;
import net.dirtt.icelib.warnings.WarningReason;
import net.iceedge.icecore.basemodule.interfaces.GeneralIntersectionInterface;
import net.iceedge.icecore.basemodule.baseclasses.electrical.intent.BasicEndfeedPowerSourceIntent;
import net.dirtt.utilities.MathUtilities;
import net.iceedge.catalogs.icd.panel.ICDSubFrameSideContainer;
import net.iceedge.icecore.basemodule.interfaces.panels.RacewayInterface;
import net.iceedge.icecore.basemodule.baseclasses.electrical.path.util.ElectricalGraphUtilities;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import net.iceedge.icecore.basemodule.baseclasses.electrical.path.ElectricalGraphConnectorEdge;
import java.util.Collection;
import net.iceedge.icecore.basemodule.baseclasses.electrical.BasicElectricalyRunable;
import net.iceedge.icecore.basemodule.baseclasses.electrical.path.ElectricalGraphEdge;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.attributes.Attribute;
import java.util.Iterator;
import java.util.Collections;
import net.iceedge.catalogs.icd.panel.ICDPanel;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import java.util.ArrayList;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import java.util.List;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.interfaces.OptionOverrideEntity;
import net.iceedge.icecore.basemodule.baseclasses.electrical.BasicElectricalCable;

public class ICDElectricalCable extends BasicElectricalCable implements OptionOverrideEntity, ICDManufacturingReportable
{
    private static final long serialVersionUID = -7170014662173112540L;
    private static final int MIN_JOINTER_WIDTH = 4;
    private static final int CHASE_TUBE_WIDTH = 1;
    private static final int RECEPTACLE_EDGE_GAP = 7;
    private final List<Integer> jumperWidths;
    private final List<Integer> harnessWidths;
    private int extTubeWidth;
    private boolean isHarnessValid;
    private static final String CAD_POS = "CAD_POS";
    private static final String CAD_ROT = "CAD_ROT";
    
    public ICDElectricalCable(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.jumperWidths = new ArrayList<Integer>();
        this.harnessWidths = new ArrayList<Integer>();
        this.extTubeWidth = 0;
        this.setupNamedPoints();
        this.setupNamedRotations();
        this.initPossibleWidths();
    }
    
    public void setupNamedPoints() {
        super.setupNamedPoints();
        this.addNamedPoint("CAD_POS", new Point3f());
        this.addNamedPoint("CAD_ASSY_TX", new Point3f());
    }
    
    protected void setupNamedRotations() {
        this.addNamedRotation("CAD_ROT", new Vector3f());
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
            this.getNamedPointLocal("CAD_POS").set(n, 0.0f, this.getCalculatedCableWidth());
            this.getNamedPointLocal("CAD_ASSY_TX").set(n - 3.0f, icdPanel.getSplitLocation() + 1.0f, icdPanel.getWidth() / 2.0f);
        }
    }
    
    protected void calculateNamedRotations() {
        super.calculateNamedRotations();
        this.getNamedRotationLocal("CAD_ROT").set(0.0f, (float)Math.toRadians(90.0), 0.0f);
    }
    
    private void initPossibleWidths() {
        this.jumperWidths.add(0);
        this.harnessWidths.add(0);
        for (final OptionObject optionObject : this.getCurrentType().getPossibleOptionVector()) {
            final Attribute attributeObject = optionObject.getAttributeObject("Generic_Cable_Width");
            if (attributeObject != null) {
                final String attributeValueAsString = optionObject.getAttributeValueAsString("Generic_Cable_Type");
                final int int1 = Integer.parseInt(attributeObject.getValueAsString());
                if ("PassThru".equals(attributeValueAsString)) {
                    this.jumperWidths.add(int1);
                }
                else {
                    if (!"Harness".equals(attributeValueAsString)) {
                        continue;
                    }
                    this.harnessWidths.add(int1);
                }
            }
        }
        Collections.sort(this.jumperWidths);
        Collections.sort(this.harnessWidths);
    }
    
    public TransformableEntity buildClone(final ICDElectricalCable icdElectricalCable) {
        super.buildClone((BasicElectricalCable)icdElectricalCable);
        return (TransformableEntity)icdElectricalCable;
    }
    
    public TransformableEntity buildClone2(final ICDElectricalCable icdElectricalCable) {
        super.buildClone2((BasicElectricalCable)icdElectricalCable);
        return (TransformableEntity)icdElectricalCable;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDElectricalCable(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDElectricalCable(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDElectricalCable icdElectricalCable, final EntityObject entityObject) {
        super.buildFrameClone((BasicElectricalCable)icdElectricalCable, entityObject);
        return (EntityObject)icdElectricalCable;
    }
    
    public String getRequiredCableSolver() {
        return "ICD_Electrical_Cable_Solver_Type";
    }
    
    public ElectricalGraphEdge getEdge(final TransformableEntity transformableEntity) {
        final BasicElectricalyRunable electricalRun = this.getElectricalRun();
        if (electricalRun != null && electricalRun.getReceptacles().contains(transformableEntity)) {
            for (final ElectricalGraphEdge electricalGraphEdge : this.getOwnEdges()) {
                if (electricalGraphEdge != null) {
                    return electricalGraphEdge;
                }
            }
        }
        if (transformableEntity instanceof ICDEndfeedPowerSource) {
            for (final ElectricalGraphEdge electricalGraphEdge2 : this.getOwnEdges()) {
                if (electricalGraphEdge2 != null) {
                    return electricalGraphEdge2;
                }
            }
        }
        return null;
    }
    
    protected void buildRaceWayToRacewayEdges(final Collection<ElectricalGraphConnectorEdge> collection, final Segment segment, final boolean b) {
        if (segment != null) {
            final RacewayInterface raceWay = segment.getRaceWay();
            if (raceWay != null) {
                final BasicElectricalCable electricCable = raceWay.getElectricCable();
                if (electricCable != null && this.hasConnectingChase(electricCable) && this.isChildOfGraphContributingIntersection() == null) {
                    final ElectricalGraphConnectorEdge connectorEdge = ElectricalGraphUtilities.createConnectorEdge(this.getSolution(), this.getLeftSideNode(), this.getRightSideNode(), electricCable.getLeftSideNode(), electricCable.getRightSideNode());
                    if (connectorEdge != null) {
                        connectorEdge.setIntersectionConnector(!b);
                        collection.add(connectorEdge);
                    }
                }
            }
        }
    }
    
    private boolean hasConnectingChase(final BasicElectricalCable basicElectricalCable) {
        if (basicElectricalCable != null) {
            final ICDPanel icdPanel = (ICDPanel)basicElectricalCable.getParent((Class)ICDPanel.class);
            if (icdPanel != null) {
                final ICDPanel icdPanel2 = (ICDPanel)this.getParent((Class)ICDPanel.class);
                for (int i = 0; i <= 1; ++i) {
                    final ICDSubFrameSideContainer subFrameSideContainer = icdPanel2.getSubFrameSideContainer(i);
                    if (subFrameSideContainer != null) {
                        for (int j = 0; j <= 1; ++j) {
                            final ICDSubFrameSideContainer subFrameSideContainer2 = icdPanel.getSubFrameSideContainer(j);
                            if (subFrameSideContainer2 != null && subFramesIntersect(subFrameSideContainer, subFrameSideContainer2)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private static boolean subFramesIntersect(final ICDSubFrameSideContainer icdSubFrameSideContainer, final ICDSubFrameSideContainer icdSubFrameSideContainer2) {
        if (icdSubFrameSideContainer == null || icdSubFrameSideContainer2 == null) {
            return false;
        }
        final Point3f convertPointToWorldSpace = icdSubFrameSideContainer.convertPointToWorldSpace(new Point3f());
        final Point3f convertPointToWorldSpace2 = icdSubFrameSideContainer.convertPointToWorldSpace(new Point3f(icdSubFrameSideContainer.getXDimension(), 0.0f, 0.0f));
        final Point3f convertPointToWorldSpace3 = icdSubFrameSideContainer2.convertPointToWorldSpace(new Point3f());
        final Point3f convertPointToWorldSpace4 = icdSubFrameSideContainer2.convertPointToWorldSpace(new Point3f(icdSubFrameSideContainer2.getXDimension(), 0.0f, 0.0f));
        convertPointToWorldSpace4.z = 0.0f;
        convertPointToWorldSpace3.z = 0.0f;
        convertPointToWorldSpace2.z = 0.0f;
        convertPointToWorldSpace.z = 0.0f;
        return MathUtilities.getIntersectionPoint(convertPointToWorldSpace, convertPointToWorldSpace2, convertPointToWorldSpace3, convertPointToWorldSpace4, false) != null;
    }
    
    public boolean isJumperToJumperConnectionAllowed() {
        return false;
    }
    
    public void setCableWidth(int cableWidth) {
        while (!this.isPossibleCableLength(cableWidth)) {
            if (cableWidth > this.getLongestJumperSize()) {
                super.setCableWidth((int)this.getLongestJumperSize());
                return;
            }
            ++cableWidth;
        }
        super.setCableWidth(cableWidth);
    }
    
    private boolean isPossibleCableLength(final int i) {
        return this.jumperWidths.contains(i);
    }
    
    public float getLongestJumperSize() {
        return 144.0f;
    }
    
    public float getExtTubeWidth() {
        return (float)this.extTubeWidth;
    }
    
    public float getConnectorExtensionWidth() {
        if (this.isHarness()) {
            return (float)(7 + this.extTubeWidth / 2);
        }
        return 0.0f;
    }
    
    public String getSegmentWidthIndicator() {
        int calculateExtraTubeWidth = 0;
        this.isHarnessValid = true;
        String s = "0";
        if (this.isHarness()) {
            final ICDPanel icdPanel = (ICDPanel)this.getParent((Class)ICDPanel.class);
            ICDSubFrameSideContainer icdSubFrameSideContainer = icdPanel.getSubFrameSideContainer(0);
            if (icdSubFrameSideContainer == null) {
                icdSubFrameSideContainer = icdPanel.getSubFrameSideContainer(1);
            }
            if (icdSubFrameSideContainer != null) {
                final int n = (int)MathUtilities.round(icdSubFrameSideContainer.getLength(), 0);
                final int n2 = n - 4 - 1 - 1;
                int i = 0;
                for (final int intValue : this.harnessWidths) {
                    if (intValue != n - 1 && intValue > n2) {
                        break;
                    }
                    i = intValue;
                }
                calculateExtraTubeWidth = this.calculateExtraTubeWidth(n - i - 1, i);
                s = String.valueOf(i);
                this.isHarnessValid = (i != 0);
            }
        }
        else {
            s = super.getSegmentWidthIndicator();
        }
        if (calculateExtraTubeWidth != this.extTubeWidth) {
            this.extTubeWidth = calculateExtraTubeWidth;
            final Iterator<ICDHarnessExtensionTubing> iterator2 = (Iterator<ICDHarnessExtensionTubing>)this.getChildrenByClass((Class)ICDHarnessExtensionTubing.class, false).iterator();
            while (iterator2.hasNext()) {
                iterator2.next().setModified(true);
            }
        }
        return s;
    }
    
    private int calculateExtraTubeWidth(final int n, final int n2) {
        int n3 = 0;
        if (n > 0) {
            if (n2 > 0) {
                n3 = n - 1;
            }
            else {
                n3 = n;
            }
        }
        return n3;
    }
    
    public boolean requiresExtensionTubing() {
        return this.isHarness() && this.extTubeWidth > 0;
    }
    
    public boolean isQuoteable(final String s) {
        return !this.isHarness() && super.isQuoteable(s);
    }
    
    public void modifyCurrentOption() {
        final boolean harness = this.isHarness();
        super.modifyCurrentOption();
        if (harness != this.isHarness()) {
            final ICDPanel icdPanel = (ICDPanel)this.getParent((Class)ICDPanel.class);
            if (icdPanel != null) {
                icdPanel.setModified(true);
                icdPanel.solve();
            }
        }
    }
    
    public boolean hasInfeed() {
        boolean hasInfeed = super.hasInfeed();
        if (!hasInfeed && this.getSegment() != null) {
            final GeneralIntersectionInterface intersectionForSegment = this.getSegment().getIntersectionForSegment(true);
            final GeneralIntersectionInterface intersectionForSegment2 = this.getSegment().getIntersectionForSegment(false);
            final boolean b = intersectionForSegment != null && intersectionForSegment.getChildrenByClass((Class)BasicEndfeedPowerSourceIntent.class, false, true).size() > 0;
            final boolean b2 = intersectionForSegment2 != null && intersectionForSegment2.getChildrenByClass((Class)BasicEndfeedPowerSourceIntent.class, false, true).size() > 0;
            hasInfeed = (b || b2);
        }
        return hasInfeed;
    }
    
    protected void applyWarningOnParentTransformable(final WarningReason warningReason) {
    }
    
    public boolean hasWireableReceptacles() {
        return super.hasWireableReceptacles() || this.hasInfeed();
    }
    
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        final Vector<String> vector = new Vector<String>();
        if (elevationEntity instanceof ICDIsometricAssemblyElevationEntity && this.hasElectrical()) {
            final float attributeValueAsFloat = this.getAttributeValueAsFloat("Width", 0.0f);
            float n = 0.0f;
            float width = -1.0f;
            float n2 = 0.0f;
            final ICDPanel icdPanel = (ICDPanel)this.getParent((Class)ICDPanel.class);
            if (icdPanel != null) {
                width = icdPanel.getWidth();
                n = icdPanel.getSplitLocation() + 1.0f;
            }
            if (width != -1.0f) {
                n2 = (width - attributeValueAsFloat) / 2.0f;
            }
            final String assemblyElevationText = this.getAssemblyElevationText();
            vector.add("LI:SS(6:CAD_POS(-3," + n + "," + -n2 + "):CAD_POS(-3," + n + "," + -(attributeValueAsFloat + n2) + "))");
            vector.add("TX:SS(" + assemblyElevationText + ":" + 6 + ":0:" + 2 + ":CAD_ASSY_TX)");
        }
        return vector;
    }
    
    private String getAssemblyElevationText() {
        return "EB" + (int)this.getAttributeValueAsFloat("Width", 0.0f) + "ASSY";
    }
    
    public void handleWarnings() {
        super.handleWarnings();
        if (!this.isHarnessValid) {
            WarningReason0363.addRequiredWarning(this);
        }
    }
    
    public String getDefaultLayerName() {
        return "Electrical Harness";
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
    
    protected void calculateDimensions() {
        super.calculateDimensions();
        final BasicElectricalyRunable electricalRun = this.getElectricalRun();
        if (electricalRun != null && electricalRun instanceof ICDRaceway) {
            final Point3f namedPointLocal = ((ICDRaceway)electricalRun).getNamedPointLocal("CABLE_POS");
            if (namedPointLocal != null && !MathUtilities.isSameFloat(namedPointLocal.z, 0.0f, 0.1f) && !MathUtilities.isSameFloat(namedPointLocal.z, electricalRun.getZDimension(), 0.1f)) {
                final Vector3f namedRotationLocal = ((ICDRaceway)electricalRun).getNamedRotationLocal("CABLE_ROT");
                if (namedRotationLocal != null && MathUtilities.isSameFloat(namedRotationLocal.y, 0.0f, 0.1f)) {
                    this.setZDimension(Math.abs(electricalRun.getZDimension() - namedPointLocal.z));
                }
                else if (namedRotationLocal != null && MathUtilities.isSameFloat(namedRotationLocal.y, 3.1415927f, 0.1f)) {
                    this.setZDimension(Math.abs(namedPointLocal.z));
                }
            }
        }
    }
}
