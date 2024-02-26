// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.icecore.basemodule.interfaces.ILineInterfaceFilter;
import net.iceedge.icecore.basemodule.interfaces.ILineInterface;
import net.dirtt.utilities.EnumerationIterator;
import net.dirtt.utilities.MathUtilities;
import net.iceedge.catalogs.icd.ICDSegment;
import net.iceedge.icecore.basemodule.interfaces.GeneralIntersectionInterface;
import net.iceedge.icecore.basemodule.finalclasses.GeneralIntersectionContainer;
import net.iceedge.icecore.basemodule.interfaces.IntersectionArmInterface;
import net.dirtt.utilities.TypeFilter;
import net.iceedge.icecore.basemodule.baseclasses.GeneralSnapSetFilter;
import net.iceedge.icecore.basemodule.finalclasses.GeneralSnapSet;
import net.iceedge.catalogs.icd.intersection.ICDChaseMidConnectorContainer;
import net.iceedge.catalogs.icd.intersection.ICDPost;
import java.util.Iterator;
import net.iceedge.catalogs.icd.intersection.ICDIntersection;
import net.iceedge.catalogs.icd.ICDBeamSegment;
import java.util.Collection;
import javax.vecmath.Point3f;
import java.util.Vector;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.catalogs.icd.intersection.ICDPostHostInterface;
import net.iceedge.icecore.basemodule.baseclasses.BasicFrameToFrameConnectionHW;

public class ICDPanelToPanelConnectionHW extends BasicFrameToFrameConnectionHW implements ICDPostHostInterface, ICDManufacturingReportable
{
    private Segment seg1;
    private Segment seg2;
    
    public ICDPanelToPanelConnectionHW(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.seg1 = null;
        this.seg2 = null;
    }
    
    public ICDPanelToPanelConnectionHW buildClone(final ICDPanelToPanelConnectionHW icdPanelToPanelConnectionHW) {
        super.buildClone((BasicFrameToFrameConnectionHW)icdPanelToPanelConnectionHW);
        return icdPanelToPanelConnectionHW;
    }
    
    public ICDPanelToPanelConnectionHW buildClone2(final ICDPanelToPanelConnectionHW icdPanelToPanelConnectionHW) {
        super.buildClone2((BasicFrameToFrameConnectionHW)icdPanelToPanelConnectionHW);
        return icdPanelToPanelConnectionHW;
    }
    
    public Object clone() {
        return this.buildClone(new ICDPanelToPanelConnectionHW(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDPanelToPanelConnectionHW icdPanelToPanelConnectionHW, final EntityObject entityObject) {
        super.buildFrameClone((BasicFrameToFrameConnectionHW)icdPanelToPanelConnectionHW, entityObject);
        return (EntityObject)icdPanelToPanelConnectionHW;
    }
    
    public void solve() {
        super.solve();
    }
    
    public void setConnectionHeight(final float zDimension) {
        this.setZDimension(zDimension);
    }
    
    protected void setLinkedSegments(final Segment seg1, final Segment seg2) {
        this.seg1 = seg1;
        this.seg2 = seg2;
    }
    
    public Vector<Segment> getLinkedSegments() {
        final Vector<Segment> vector = new Vector<Segment>();
        if (this.seg1 != null) {
            vector.add(this.seg1);
        }
        if (this.seg2 != null) {
            vector.add(this.seg2);
        }
        return vector;
    }
    
    public String getJointTypeAtLocation(final Point3f point3f) {
        final int numberOfSegmentsAtLocation = this.getNumberOfSegmentsAtLocation(point3f);
        if (numberOfSegmentsAtLocation >= 0 && numberOfSegmentsAtLocation < ICDJoint.JOINT_TYPE.length) {
            return ICDJoint.JOINT_TYPE[numberOfSegmentsAtLocation];
        }
        return ICDJoint.JOINT_TYPE[0];
    }
    
    private int getNumberOfSegmentsAtLocation(final Point3f point3f) {
        int n = 0;
        final Vector<Segment> vector = new Vector<Segment>();
        if (this.seg1 != null && this.seg2 != null) {
            vector.add(this.seg1);
            vector.add(this.seg2);
        }
        final Vector<ICDBeamSegment> connectedBeamSegment = this.getConnectedBeamSegment();
        if (connectedBeamSegment != null) {}
        vector.addAll((Collection<? extends Segment>)connectedBeamSegment);
        int n2 = 0;
        for (final Segment segment : vector) {
            if (segment.getHeight() > point3f.z - 1.0f && segment.hasExtrusionAtLocation(point3f)) {
                ++n;
                if (!(segment instanceof ICDBeamSegment)) {
                    continue;
                }
                ++n2;
            }
        }
        final Iterator<ICDSubFrameSideContainer> iterator2 = ICDIntersection.getIntersectedSubPanels(this).iterator();
        while (iterator2.hasNext()) {
            if (iterator2.next().hasExtrusionAtLocation(point3f)) {
                ++n;
            }
        }
        if (n == 2) {
            if (n2 == 1) {
                n = 2;
            }
            else {
                n = 0;
            }
        }
        return n;
    }
    
    public Vector<Float> getSplitLocations() {
        final Vector<Float> vector = new Vector<Float>();
        if (this.seg1 != null && this.seg2 != null) {
            ICDIntersection.addLocation(vector, this.seg1.getSplitLocation(false));
            ICDIntersection.addLocation(vector, this.seg2.getSplitLocation(true));
        }
        final ICDPost icdPost = (ICDPost)this.getChildByClass(ICDPost.class);
        if (icdPost != null) {
            final ICDChaseMidConnectorContainer icdChaseMidConnectorContainer = (ICDChaseMidConnectorContainer)icdPost.getChildByClass(ICDChaseMidConnectorContainer.class);
            if (icdChaseMidConnectorContainer != null && icdChaseMidConnectorContainer.isSuspendedContainer()) {
                final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion = (ICDChaseConnectorExtrusion)icdChaseMidConnectorContainer.getChildByLWType("ICD_Chase_Mid_Bottom_Connector_Type");
                if (icdChaseConnectorExtrusion != null) {
                    final Vector<Float> vector2 = new Vector<Float>();
                    vector2.add(icdChaseConnectorExtrusion.getBasePoint3f().z);
                    ICDIntersection.addLocation(vector, vector2);
                }
            }
        }
        final Vector<Float> splitFromBeam = this.getSplitFromBeam();
        if (splitFromBeam != null) {
            ICDIntersection.addLocation(vector, splitFromBeam);
        }
        final Iterator<ICDSubFrameSideContainer> iterator = ICDIntersection.getIntersectedSubPanels(this).iterator();
        while (iterator.hasNext()) {
            ICDIntersection.addLocation(vector, iterator.next().getAllSplitLocation(true));
        }
        return vector;
    }
    
    private Vector<ICDBeamSegment> getConnectedBeamSegment() {
        final Vector<ICDBeamSegment> vector = new Vector<ICDBeamSegment>();
        final GeneralSnapSet set = (GeneralSnapSet)this.getParent((TypeFilter)new GeneralSnapSetFilter());
        if (set != null) {
            final GeneralIntersectionContainer generalIntersectionContainer = set.getGeneralIntersectionContainer();
            if (generalIntersectionContainer != null) {
                final GeneralIntersectionInterface intersection = generalIntersectionContainer.getIntersectionAt(this.getBasePointWorldSpace());
                if (intersection != null && intersection.isNonBreakingIntersection()) {
                    final Iterator<IntersectionArmInterface> iterator = intersection.getArmVector().iterator();
                    while (iterator.hasNext()) {
                        final Segment segment = iterator.next().getSegment();
                        if (segment != null && segment instanceof ICDBeamSegment) {
                            vector.add((ICDBeamSegment)segment);
                        }
                    }
                }
            }
        }
        return vector;
    }
    
    private Vector<ICDSegment> getConnectedSegments() {
        final Vector<ICDSegment> vector = new Vector<ICDSegment>();
        final GeneralSnapSet set = (GeneralSnapSet)this.getParent((TypeFilter)new GeneralSnapSetFilter());
        if (set != null) {
            final GeneralIntersectionContainer generalIntersectionContainer = set.getGeneralIntersectionContainer();
            if (generalIntersectionContainer != null) {
                final GeneralIntersectionInterface intersection = generalIntersectionContainer.getIntersectionAt(this.getBasePointWorldSpace());
                if (intersection != null && intersection.isNonBreakingIntersection()) {
                    final Iterator<IntersectionArmInterface> iterator = intersection.getArmVector().iterator();
                    while (iterator.hasNext()) {
                        final Segment segment = iterator.next().getSegment();
                        if (segment != null && segment instanceof ICDSegment) {
                            vector.add((ICDSegment)segment);
                        }
                    }
                }
            }
        }
        return vector;
    }
    
    private Vector<Float> getSplitFromBeam() {
        Vector<Float> vector = null;
        final Vector<ICDBeamSegment> connectedBeamSegment = this.getConnectedBeamSegment();
        if (connectedBeamSegment != null && connectedBeamSegment.size() > 0) {
            final Iterator<ICDBeamSegment> iterator = connectedBeamSegment.iterator();
            while (iterator.hasNext()) {
                final Vector<Float> tubeLocations = iterator.next().getTubeLocations();
                if (vector == null) {
                    vector = tubeLocations;
                }
                else {
                    vector.addAll(tubeLocations);
                }
            }
        }
        return vector;
    }
    
    public float getTallestWallHeight() {
        if (this.seg1 == null || this.seg2 == null) {
            return 0.0f;
        }
        final float height = this.seg1.getHeight();
        final float height2 = this.seg2.getHeight();
        if (height > height2) {
            return height;
        }
        return height2;
    }
    
    public String getBottomJointType() {
        final Vector<Float> splitFromBeam = this.getSplitFromBeam();
        if (splitFromBeam != null) {
            int n = 0;
            final Iterator<Float> iterator = splitFromBeam.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() < 1.001f) {
                    ++n;
                }
            }
            if (n == 1) {
                return "3 Way";
            }
            if (n == 2) {
                return "4 Way";
            }
        }
        int n2 = 0;
        final Iterator<ICDSubFrameSideContainer> iterator2 = ICDIntersection.getIntersectedSubPanels(this).iterator();
        while (iterator2.hasNext()) {
            if (!iterator2.next().getParentPanel().isSuspendedChase()) {
                ++n2;
            }
        }
        if (n2 != 0) {
            n2 += 2;
            if (n2 < ICDJoint.JOINT_TYPE.length) {
                return ICDJoint.JOINT_TYPE[n2];
            }
        }
        return "2 Way (180)";
    }
    
    public void setModified(final boolean b) {
        super.setModified(b);
        if (b) {
            final ICDPost icdPost = (ICDPost)this.getChildByClass(ICDPost.class);
            if (icdPost != null) {
                icdPost.setModified(b);
            }
        }
    }
    
    public Vector<ICDPanel> getCorePanelsVector() {
        final Vector<ICDPanel> vector = new Vector<ICDPanel>();
        if (this.seg1 != null) {
            for (final ICDPanel e : this.seg1.getChildrenByClass(ICDPanel.class, true, true)) {
                if (e.isCorePanel()) {
                    vector.add(e);
                }
            }
        }
        if (this.seg2 != null) {
            for (final ICDPanel e2 : this.seg2.getChildrenByClass(ICDPanel.class, true, true)) {
                if (e2.isCorePanel()) {
                    vector.add(e2);
                }
            }
        }
        return vector;
    }
    
    public int getNumberOfChaseOnPointSide(final Point3f point3f) {
        int n = 0;
        for (final ICDPanel icdPanel : this.getCorePanelsVector()) {
            if (icdPanel.hasChaseOnPointSide(MathUtilities.convertPointToOtherLocalSpace((EntityObject)icdPanel, point3f))) {
                ++n;
            }
        }
        return n;
    }
    
    public Collection<JointIntersectable> getAllIntersectables() {
        final Vector<JointIntersectable> vector = new Vector<JointIntersectable>();
        final EnumerationIterator enumerationIterator = new EnumerationIterator(this.breadthFirstEnumeration());
        while (((Iterator)enumerationIterator).hasNext()) {
            final JointIntersectable next = ((Iterator<JointIntersectable>)enumerationIterator).next();
            if (next instanceof JointIntersectable && next.doesParticipateInJointIntersection()) {
                vector.add(next);
            }
        }
        if (this.seg1 instanceof ICDSegment) {
            vector.addAll(((ICDSegment)this.seg1).getAllIntersectables());
        }
        if (this.seg2 instanceof ICDSegment) {
            vector.addAll(((ICDSegment)this.seg2).getAllIntersectables());
        }
        return vector;
    }
    
    public Vector<ILineInterface> getConnectedILines() {
        final Vector<ILineInterface> vector = new Vector<ILineInterface>();
        final ILineInterface e = (ILineInterface)this.getParent((TypeFilter)new ILineInterfaceFilter());
        if (e != null) {
            vector.add(e);
        }
        return vector;
    }
    
    public int getNumberOfNoFrameBottomTiles() {
        int n = 0;
        if (this.seg1 != null && ((ICDSegment)this.seg1).isBottomTileNoFrame()) {
            ++n;
        }
        if (this.seg2 != null && ((ICDSegment)this.seg2).isBottomTileNoFrame()) {
            ++n;
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
