package net.iceedge.catalogs.icd.intersection;

import net.iceedge.icecore.basemodule.baseclasses.electrical.BasicElectricalReceptacle;
import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import java.util.HashSet;
import net.iceedge.catalogs.icd.ICDUtilities;
import net.dirtt.icelib.main.attributes.Attribute;
import net.iceedge.catalogs.icd.ICDBeamILine;
import net.iceedge.catalogs.icd.ICDBeamSegment;
import net.iceedge.catalogs.icd.panel.JointIntersectable;
import net.dirtt.icelib.report.Report;
import net.dirtt.icebox.canvas2d.Ice2DNode;
import java.awt.Font;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icebox.canvas2d.Ice2DMultipleTextNode;
import java.awt.Color;
import javax.vecmath.Vector3f;
import javax.vecmath.Matrix4f;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportNode;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelSegmentInterface;
import net.iceedge.icecore.basemodule.baseclasses.BasicSegment;
import net.dirtt.icelib.main.snapping.simple.SimpleSnapTargetCollection;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelInterface;
import net.iceedge.catalogs.icd.panel.ICDPanel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.iceedge.catalogs.icd.electrical.ICDEndfeedPowerSource;
import net.dirtt.icelib.main.RequiredChildTypeContainer;
import net.iceedge.icecore.basemodule.baseclasses.electrical.intent.BasicEndfeedPowerSourceIntent;
import net.iceedge.icecore.basemodule.baseclasses.electrical.intent.BasicPowerIntent;
import java.awt.geom.Line2D;
import net.iceedge.icecore.basemodule.finalclasses.GeneralSnapSet;
import net.dirtt.icelib.main.EntityObject;
import net.iceedge.icecore.basemodule.interfaces.ILineInterface;
import java.awt.geom.Point2D;
import net.iceedge.catalogs.icd.panel.ICDChaseConnectorExtrusion;
import net.iceedge.icecore.basemodule.interfaces.IntersectionArmInterface;
import net.iceedge.catalogs.icd.panel.ICDSubFrameSideContainer;
import net.dirtt.utilities.MathUtilities;
import java.util.Vector;
import net.iceedge.catalogs.icd.panel.ICDJoint;
import java.util.Iterator;
import net.iceedge.catalogs.icd.ICDSegment;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import net.dirtt.icelib.main.Solution;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icebox.canvas2d.Ice2DTextNode;
import net.iceedge.catalogs.icd.panel.ICDVerticalChase;
import net.iceedge.industry.electrical.PowerIntentHostable;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icebox.attribute.AllEditorOnTheFlyAttributeInterface;
import net.iceedge.icecore.basemodule.baseclasses.BasicIntersection;

public class ICDIntersection extends BasicIntersection implements ICDPostHostInterface, AllEditorOnTheFlyAttributeInterface, ICDManufacturingReportable, PowerIntentHostable
{
    private ICDVerticalChase verticalChase;
    private static String[] ic;
    private static String[][] indicatorMatch;
    protected Ice2DTextNode elevationReportTag;
    
    public ICDIntersection(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public ICDIntersection(final String s, final TypeObject typeObject, final OptionObject optionObject, final Point3f point3f) {
        super(s, typeObject, optionObject, point3f);
    }
    
    public ICDIntersection(final Point3f point3f, final TypeObject typeObject, final OptionObject optionObject, final int n) {
        super(point3f, typeObject, optionObject, n);
    }
    
    public ICDIntersection(final Point3f point3f, final TypeObject typeObject, final int n) {
        super(point3f, typeObject, n);
    }
    
    public ICDIntersection buildClone(final ICDIntersection icdIntersection) {
        super.buildClone((BasicIntersection)icdIntersection);
        return icdIntersection;
    }
    
    public Object clone() {
        return this.buildClone(new ICDIntersection(this.getId(), this.currentType, this.currentOption));
    }
    
    public void calculateDimensions() {
        this.setXDimension(1.0f);
        this.setYDimension(1.0f);
    }
    
    protected TypeObject generateTypeObjectByIntersectionType(int n) {
        TypeObject typeObject = null;
        if (n > 4) {
            n = 4;
        }
        switch (n) {
            case 1: {
                typeObject = Solution.typeObjectByName("ICD_OneWayIntersectionType");
                break;
            }
            case 2: {
                typeObject = Solution.typeObjectByName("ICD_TwoWayIntersectionType");
                break;
            }
            case 3: {
                typeObject = Solution.typeObjectByName("ICD_ThreeWayIntersectionType");
                break;
            }
            case 4: {
                typeObject = Solution.typeObjectByName("ICD_FourWayIntersectionType");
                break;
            }
        }
        return typeObject;
    }
    
    public int getSegmentsWithNoFrameBottomTile() {
        int n = 0;
        for (final Segment segment : this.getSegmentsFromArms()) {
            if (segment instanceof ICDSegment && ((ICDSegment)segment).isBottomTileNoFrame()) {
                ++n;
            }
        }
        return n;
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
        final Vector sortedSegmentByHeight = this.getSortedSegmentByHeight();
        final Vector<Segment> vector = new Vector<Segment>();
        for (final Segment e : sortedSegmentByHeight) {
            if (e.getHeight() > point3f.z - 1.0f && e.hasExtrusionAtLocation(point3f)) {
                ++n;
                vector.add(e);
            }
        }
        if (n == 2) {
            final Segment segment = vector.lastElement();
            final Segment segment2 = vector.firstElement();
            final IntersectionArmInterface armForSegment = this.getArmForSegment(segment);
            final IntersectionArmInterface armForSegment2 = this.getArmForSegment(segment2);
            if (armForSegment != null && armForSegment2 != null) {
                final float differenceBetweenAngles = MathUtilities.findDifferenceBetweenAngles(MathUtilities.normalizeRotation(armForSegment.getWorldRotation()), MathUtilities.normalizeRotation(armForSegment2.getWorldRotation()));
                if (MathUtilities.isSameFloat(differenceBetweenAngles, 0.0f, 0.001f) || MathUtilities.isSameFloat(differenceBetweenAngles, 3.1415927f, 0.001f)) {
                    n = 0;
                }
            }
        }
        final Iterator<ICDSubFrameSideContainer> iterator2 = getIntersectedSubPanels(this).iterator();
        while (iterator2.hasNext()) {
            if (iterator2.next().hasExtrusionAtLocation(point3f)) {
                ++n;
            }
        }
        return n;
    }
    
    public Vector<Float> getSplitLocations() {
        final Vector<Float> vector = new Vector<Float>();
        final Iterator<Segment> iterator = this.getSegmentsFromArms().iterator();
        while (iterator.hasNext()) {
            addLocation(vector, iterator.next().getSplitLocation((BasicIntersection)this));
        }
        final ICDPost icdPost = (ICDPost)this.getChildByClass((Class)ICDPost.class);
        if (icdPost != null) {
            final ICDChaseMidConnectorContainer icdChaseMidConnectorContainer = (ICDChaseMidConnectorContainer)icdPost.getChildByClass((Class)ICDChaseMidConnectorContainer.class);
            if (icdChaseMidConnectorContainer != null && icdChaseMidConnectorContainer.isSuspendedContainer()) {
                final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion = (ICDChaseConnectorExtrusion)icdChaseMidConnectorContainer.getChildByLWType("ICD_Chase_Mid_Bottom_Connector_Type");
                if (icdChaseConnectorExtrusion != null) {
                    final Vector<Float> vector2 = new Vector<Float>();
                    vector2.add(icdChaseConnectorExtrusion.getBasePoint3f().z);
                    addLocation(vector, vector2);
                }
            }
        }
        final Iterator<ICDSubFrameSideContainer> iterator2 = getIntersectedSubPanels(this).iterator();
        while (iterator2.hasNext()) {
            addLocation(vector, iterator2.next().getAllSplitLocation(true));
        }
        return vector;
    }
    
    public static Vector<ICDSubFrameSideContainer> getIntersectedSubPanels(final ICDPostHostInterface icdPostHostInterface) {
        final Vector<ICDSubFrameSideContainer> vector = new Vector<ICDSubFrameSideContainer>();
        final Point2D.Float float1 = new Point2D.Float(icdPostHostInterface.getBasePointWorldSpace().x, icdPostHostInterface.getBasePointWorldSpace().y);
        final GeneralSnapSet generalSnapSet = icdPostHostInterface.getGeneralSnapSet();
        if (generalSnapSet != null) {
            final Vector<ILineInterface> connectedILines = icdPostHostInterface.getConnectedILines();
            for (final ILineInterface o : generalSnapSet.getAllILines()) {
                if (connectedILines == null || !connectedILines.contains(o)) {
                    for (final ICDSubFrameSideContainer e : ((EntityObject)o).getChildrenByClass((Class)ICDSubFrameSideContainer.class, true)) {
                        final Line2D.Float line = e.getLine();
                        if (line != null && (MathUtilities.isSamePoint(line.getP1(), (Point2D)float1, 0.001f) || MathUtilities.isSamePoint(line.getP2(), (Point2D)float1, 0.001f))) {
                            vector.add(e);
                        }
                    }
                }
            }
        }
        return vector;
    }
    
    public String getBottomJointType() {
        final Vector<Float> splitFromBeam = this.getSplitFromBeam();
        String attributeValueAsString = this.getAttributeValueAsString("IntersectionConnector");
        if (splitFromBeam != null && splitFromBeam.size() > 0) {
            int n = 0;
            final Iterator<Float> iterator = splitFromBeam.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() < 1.001f) {
                    ++n;
                }
            }
            final int n2 = splitFromBeam.size() - n;
            if (n2 == 1 || n2 == 2) {
                int n3 = -1;
                for (int i = 0; i < ICDIntersection.ic.length; ++i) {
                    if (ICDIntersection.ic[i].equals(attributeValueAsString)) {
                        n3 = i;
                        break;
                    }
                }
                if (n3 != -1 && ICDIntersection.indicatorMatch[n2 - 1][n3] != null) {
                    attributeValueAsString = ICDIntersection.indicatorMatch[n2 - 1][n3];
                }
            }
        }
        int n4 = 0;
        final Iterator<ICDSubFrameSideContainer> iterator2 = getIntersectedSubPanels(this).iterator();
        while (iterator2.hasNext()) {
            if (!iterator2.next().getParentPanel().isSuspendedChase()) {
                ++n4;
            }
        }
        if (n4 != 0) {
            final int n5 = n4 + this.getIntersectionType();
            if (n5 < ICDJoint.JOINT_TYPE.length) {
                attributeValueAsString = ICDJoint.JOINT_TYPE[n5];
            }
        }
        return attributeValueAsString;
    }
    
    public static void addLocation(final Vector<Float> vector, final Vector<Float> vector2) {
        for (final Float obj : vector2) {
            final int index = getIndex(obj, vector);
            if (index >= 0 && index <= vector.size()) {
                vector.insertElementAt(obj, index);
            }
        }
    }
    
    private static int getIndex(final Float n, final Vector<Float> vector) {
        if (vector.size() == 0) {
            return 0;
        }
        if (vector.size() != 1) {
            for (int i = 0; i < vector.size() - 1; ++i) {
                final float floatValue = vector.get(i);
                final float floatValue2 = vector.get(i + 1);
                if (MathUtilities.isSameFloat(floatValue, (float)n, 0.5f) || MathUtilities.isSameFloat(floatValue2, (float)n, 0.5f)) {
                    return -1;
                }
                if (floatValue > n) {
                    return i;
                }
                if (floatValue < n && floatValue2 > n) {
                    return i + 1;
                }
            }
            return vector.size();
        }
        final float floatValue3 = vector.firstElement();
        if (MathUtilities.isSameFloat(floatValue3, (float)n, 0.5f)) {
            return -1;
        }
        if (floatValue3 > n) {
            return 0;
        }
        return 1;
    }
    
    public boolean canAcceptPowerIntent(final BasicPowerIntent basicPowerIntent) {
        return basicPowerIntent instanceof BasicEndfeedPowerSourceIntent && this.getChildrenByClass((Class)BasicEndfeedPowerSourceIntent.class, true, true).size() < 1 && this.getVerticalChase() == null;
    }
    
    protected void deleteUnUsedConnectorKit() {
        for (final RequiredChildTypeContainer requiredChildTypeContainer : this.getCurrentOption().getTypeKeyList()) {
            if (requiredChildTypeContainer.getMinimum() == 0) {
                final Iterator allChildrenByType = this.getAllChildrenByType(requiredChildTypeContainer.getType());
                while (allChildrenByType.hasNext()) {
                    final EntityObject entityObject = allChildrenByType.next();
                    if (!(entityObject instanceof BasicEndfeedPowerSourceIntent) && !(entityObject instanceof ICDPostForSubPanel)) {
                        entityObject.destroy();
                    }
                }
            }
        }
    }
    
    public ICDEndfeedPowerSource buildPowerEndfeed(final boolean b, final BasicPowerIntent basicPowerIntent) {
        final List childrenByClass = this.getChildrenByClass((Class)ICDEndfeedPowerSource.class, true);
        if (b) {
            final Iterator<ICDEndfeedPowerSource> iterator = childrenByClass.iterator();
            if (iterator.hasNext()) {
                final ICDEndfeedPowerSource icdEndfeedPowerSource = iterator.next();
                icdEndfeedPowerSource.modifyAttributeValue("Generic_Hole_Type", "BasePowerFeed");
                icdEndfeedPowerSource.modifyCurrentOption();
                icdEndfeedPowerSource.setModified(true);
                icdEndfeedPowerSource.solve();
                icdEndfeedPowerSource.getParentEntity().setModified(true);
                return icdEndfeedPowerSource;
            }
        }
        return null;
    }
    
    public void afterSolve(final Collection<EntityObject> collection) {
        super.afterSolve((Collection)collection);
        if (this.needToValidatePostForSubPanels() && this.getSolution().isMainSolution()) {
            this.calculateChildPost4SubPanel();
        }
    }
    
    public void validateChildPost() {
        super.validateChildPost();
        this.markOffModuleIntersectionSegment();
    }
    
    private void calculateChildPost4SubPanel() {
        final List childrenByClass = this.getChildrenByClass((Class)ICDPostForSubPanel.class, false, true);
        final List<SubPostInfo> allSubPostInfos = this.getAllSubPostInfos();
        this.validatePostsAndInfo(childrenByClass, allSubPostInfos);
        this.validateChildPostsForSubPanel(childrenByClass, allSubPostInfos);
        this.destroyUnusedChildPostsForSubPanel(childrenByClass);
    }
    
    private void validateChildPostsForSubPanel(final List<ICDPostForSubPanel> list, final List<SubPostInfo> list2) {
        final TypeObject childType = this.getChildTypeFor((Class)ICDPostForSubPanel.class);
        if (childType != null) {
            for (int i = 0; i < list2.size(); ++i) {
                final SubPostInfo subPostInfo = list2.get(i);
                ICDPostForSubPanel icdPostForSubPanel;
                if (!list.isEmpty()) {
                    icdPostForSubPanel = list.remove(0);
                }
                else {
                    icdPostForSubPanel = null;
                }
                if (icdPostForSubPanel == null) {
                    icdPostForSubPanel = (ICDPostForSubPanel)getTypeableEntityInstance("", childType, childType.getDefaultOption());
                    if (icdPostForSubPanel != null) {
                        this.addToTree((EntityObject)icdPostForSubPanel);
                    }
                }
                if (icdPostForSubPanel != null) {
                    final Point3f basePoint = (Point3f)subPostInfo.getBasePoint().clone();
                    basePoint.z += 0.75f;
                    icdPostForSubPanel.setBasePoint(basePoint);
                    icdPostForSubPanel.setZDimension(subPostInfo.getHeight());
                    icdPostForSubPanel.solve();
                }
            }
        }
    }
    
    private void validatePostsAndInfo(final List<ICDPostForSubPanel> c, final List<SubPostInfo> c2) {
        final ArrayList<ICDPostForSubPanel> list = new ArrayList<ICDPostForSubPanel>();
        list.addAll(c);
        final ArrayList<SubPostInfo> list2 = new ArrayList<SubPostInfo>();
        list2.addAll(c2);
        for (final SubPostInfo subPostInfo : list2) {
            for (final ICDPostForSubPanel icdPostForSubPanel : list) {
                if (subPostInfo.getBasePoint().equals((Object)icdPostForSubPanel.getHeight())) {
                    c2.remove(subPostInfo);
                    c.remove(icdPostForSubPanel);
                }
            }
        }
    }
    
    private void destroyUnusedChildPostsForSubPanel(final List<ICDPostForSubPanel> list) {
        final Iterator<ICDPostForSubPanel> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next().destroy();
        }
    }
    
    private List<SubPostInfo> getAllSubPostInfos() {
        final Vector<SubPostInfo> vector = new Vector<SubPostInfo>();
        final Vector<ICDSubFrameSideContainer> allSubPanels = this.getAllSubPanels();
        for (int i = 0; i < allSubPanels.size(); ++i) {
            final ICDSubFrameSideContainer icdSubFrameSideContainer = allSubPanels.get(i);
            for (int j = 0; j < allSubPanels.size(); ++j) {
                if (i != j) {
                    final Point3f intersectionPointWith = icdSubFrameSideContainer.getIntersectionPointWith(allSubPanels.get(j));
                    if (intersectionPointWith != null) {
                        final SubPostInfo e = new SubPostInfo(this.convertPointToLocal(intersectionPointWith), icdSubFrameSideContainer.getHeight());
                        if (!e.isContainedBy(vector)) {
                            vector.add(e);
                        }
                    }
                }
            }
        }
        return vector;
    }
    
    private Vector<ICDSubFrameSideContainer> getAllSubPanels() {
        final Vector<ICDSubFrameSideContainer> vector = new Vector<ICDSubFrameSideContainer>();
        final Iterator<Segment> iterator = this.getSegmentsFromArms().iterator();
        while (iterator.hasNext()) {
            final PanelInterface childPanel = iterator.next().getChildPanelAt(0.0f);
            if (childPanel instanceof ICDPanel) {
                final ICDSubFrameSideContainer subFrameSideContainer = ((ICDPanel)childPanel).getSubFrameSideContainer(0);
                if (subFrameSideContainer != null) {
                    vector.add(subFrameSideContainer);
                }
                final ICDSubFrameSideContainer subFrameSideContainer2 = ((ICDPanel)childPanel).getSubFrameSideContainer(1);
                if (subFrameSideContainer2 == null) {
                    continue;
                }
                vector.add(subFrameSideContainer2);
            }
        }
        return vector;
    }
    
    private boolean needToValidatePostForSubPanels() {
        return this.getChildTypeFor((Class)ICDPostForSubPanel.class) != null;
    }
    
    protected void addSimpleSnapTargets(final SimpleSnapTargetCollection collection) {
        super.addSimpleSnapTargets(collection);
        if (this.shouldBuildSnappingRules()) {
            final Point3f namedPointWorld = this.getNamedPointWorld("Top_Center");
            final Point3f point3f = new Point3f(namedPointWorld.x, namedPointWorld.y, namedPointWorld.z);
            collection.addWorldSpaceSnapPoint("IntersectionSnap", point3f, Float.valueOf(this.getRotationWorldSpace() + (float)Math.toRadians(180.0)));
            collection.addWorldSpaceSnapPoint("WSurfIntersectionSnap", MathUtilities.calculateNextPoint(this.getRotationWorldSpace() + 0.7853982f, 1.0f, point3f), Float.valueOf(this.getRotationWorldSpace() + (float)Math.toRadians(180.0)));
        }
    }
    
    private boolean shouldBuildSnappingRules() {
        return "true".equals(this.getAttributeValueAsString("ICD_Curved_Intersection"));
    }
    
    private Vector<Segment> getSegmentsAt90Or270Degree(final Segment segment) {
        final Vector<Segment> vector = new Vector<Segment>();
        final IntersectionArmInterface armForSegment = this.getArmForSegment(segment);
        if (armForSegment == null) {
            return vector;
        }
        for (final IntersectionArmInterface intersectionArmInterface : this.getArmVector()) {
            if (intersectionArmInterface != null && intersectionArmInterface != armForSegment && (MathUtilities.isSameFloat(MathUtilities.normalizeRotation(armForSegment.getWorldRotation() + 1.5707964f), intersectionArmInterface.getWorldRotation(), 0.1f) || MathUtilities.isSameFloat(MathUtilities.normalizeRotation(armForSegment.getWorldRotation() + 4.712389f), intersectionArmInterface.getWorldRotation(), 0.1f))) {
                vector.add(intersectionArmInterface.getSegment());
            }
        }
        return vector;
    }
    
    public int getNumberOfChaseOnPointSide(final Segment segment, final Point3f point3f) {
        int n = 0;
        final Iterator<Segment> iterator = this.getSegmentsAt90Or270Degree(segment).iterator();
        while (iterator.hasNext()) {
            final PanelSegmentInterface baseSegment = ((BasicSegment)iterator.next()).getBaseSegment();
            if (baseSegment != null) {
                final ICDPanel icdPanel = (ICDPanel)baseSegment.getChildPanel();
                if (icdPanel == null || !icdPanel.hasChaseOnPointSide(MathUtilities.convertPointToOtherLocalSpace((EntityObject)icdPanel, point3f)) || icdPanel.isSuspendedChase()) {
                    continue;
                }
                ++n;
            }
        }
        return n;
    }
    
    public float getChaseOffsetOnPointSide(final Segment segment, final Point3f point3f, final boolean b, final boolean b2) {
        final Iterator<Segment> iterator = this.getSegmentsAt90Or270Degree(segment).iterator();
        while (iterator.hasNext()) {
            final PanelSegmentInterface baseSegment = ((BasicSegment)iterator.next()).getBaseSegment();
            if (baseSegment != null) {
                final ICDPanel icdPanel = (ICDPanel)baseSegment.getChildPanel();
                if (icdPanel == null) {
                    continue;
                }
                final float chaseOffsetOnPointSide = icdPanel.getChaseOffsetOnPointSide(MathUtilities.convertPointToOtherLocalSpace((EntityObject)icdPanel, point3f), b, b2);
                if (!MathUtilities.isSameFloat(chaseOffsetOnPointSide, 0.0f, 2.0f)) {
                    return chaseOffsetOnPointSide;
                }
                continue;
            }
        }
        return 0.0f;
    }
    
    public float[] getChaseOffsetAndHeightOnPointSide(final Segment segment, final Point3f point3f) {
        final Iterator<Segment> iterator = this.getSegmentsAt90Or270Degree(segment).iterator();
        while (iterator.hasNext()) {
            final PanelSegmentInterface baseSegment = ((BasicSegment)iterator.next()).getBaseSegment();
            if (baseSegment != null) {
                final ICDPanel icdPanel = (ICDPanel)baseSegment.getChildPanel();
                if (icdPanel != null) {
                    return icdPanel.getChaseOffsetAndHeightOnPointSide(MathUtilities.convertPointToOtherLocalSpace((EntityObject)icdPanel, point3f));
                }
                continue;
            }
        }
        return new float[] { 0.0f, 0.0f };
    }
    
    public int getNumberOfSuspendedChaseOnPointSide(final Segment segment, final Point3f point3f) {
        int n = 0;
        final Iterator<Segment> iterator = this.getSegmentsAt90Or270Degree(segment).iterator();
        while (iterator.hasNext()) {
            final PanelSegmentInterface baseSegment = ((BasicSegment)iterator.next()).getBaseSegment();
            if (baseSegment != null) {
                final ICDPanel icdPanel = (ICDPanel)baseSegment.getChildPanel();
                if (icdPanel == null || !icdPanel.hasSuspendedChaseOnPointSide(MathUtilities.convertPointToOtherLocalSpace((EntityObject)icdPanel, point3f))) {
                    continue;
                }
                ++n;
            }
        }
        return n;
    }
    
    public int getNumberOfChaseOnPointSideFor2And3WayPost(final Point3f point3f) {
        int n = 0;
        IntersectionArmInterface oppositeArm = null;
        IntersectionArmInterface intersectionArmInterface = null;
        final Vector<Segment> vector = new Vector<Segment>();
        for (final IntersectionArmInterface intersectionArmInterface2 : this.getArmVector()) {
            oppositeArm = this.getOppositeArm(intersectionArmInterface2);
            if (oppositeArm != null) {
                intersectionArmInterface = intersectionArmInterface2;
                break;
            }
        }
        if (intersectionArmInterface != null && oppositeArm != null) {
            vector.add(intersectionArmInterface.getSegment());
            vector.add(oppositeArm.getSegment());
        }
        final Iterator<Segment> iterator2 = vector.iterator();
        while (iterator2.hasNext()) {
            final PanelSegmentInterface baseSegment = ((BasicSegment)iterator2.next()).getBaseSegment();
            if (baseSegment != null) {
                final ICDPanel icdPanel = (ICDPanel)baseSegment.getChildPanel();
                if (icdPanel == null || !icdPanel.hasChaseOnPointSide(MathUtilities.convertPointToOtherLocalSpace((EntityObject)icdPanel, point3f))) {
                    continue;
                }
                ++n;
            }
        }
        return n;
    }
    
    public float getIntersectionGripExtendAmount() {
        return 1.0f;
    }
    
    public boolean hasBoltOnJointOnHeight(final float n) {
        boolean hasBoltOnJointOnHeight = false;
        final EntityObject childByClass = this.getChildByClass((Class)ICDPost.class);
        if (childByClass != null) {
            hasBoltOnJointOnHeight = ((ICDPost)childByClass).hasBoltOnJointOnHeight(n);
        }
        return hasBoltOnJointOnHeight;
    }
    
    public float getBoltOnJointHeight(final float n) {
        float n2 = ICDPost.BIG_NEGATIVE;
        final EntityObject childByClass = this.getChildByClass((Class)ICDPost.class);
        if (childByClass != null) {
            n2 = ((ICDPost)childByClass).getBoltOnJointHeight(n);
        }
        return n2;
    }
    
    public void draw2DElevation(final int n, final Ice2DContainer ice2DContainer, final boolean b, final SolutionSetting solutionSetting) {
        super.draw2DElevation(n, ice2DContainer, b, solutionSetting);
    }
    
    protected void buildElevationPaintableReportTag(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        final ICDPost icdPost = (ICDPost)this.getChildByClass((Class)ICDPost.class);
        if (!solutionSetting.isShowICDPreassembledTag() || !icdPost.shouldICDMakePreAssembledReport()) {
            return;
        }
        final Point3f point3f = new Point3f(this.getLength() / 2.0f, 0.0f, 0.0f);
        if (point3f != null) {
            String string = "";
            final Solution solution = this.getSolution();
            if (solution != null) {
                final Report report = solution.getReport(51);
                if (report != null) {
                    final ICDManufacturingReportNode icdManufacturingReportNode = (ICDManufacturingReportNode)icdPost.getBucketInReport(report);
                    if (icdManufacturingReportNode != null) {
                        string = "SUB INTERNAL" + icdManufacturingReportNode.getTag();
                    }
                }
            }
            if (string != null) {
                final Point3f point3f2 = new Point3f(point3f.x, icdPost.getHeight(), point3f.z);
                final Matrix4f obj = (Matrix4f)this.getEntWorldSpaceMatrix().clone();
                System.out.println("--- Whats m?= " + obj);
                final Matrix4f matrix4f = new Matrix4f();
                matrix4f.setIdentity();
                matrix4f.rotX(1.57f);
                obj.mul(matrix4f);
                final Matrix4f matrix4f2 = new Matrix4f();
                System.out.println("--M03= " + obj.m03);
                System.out.println("--M13= " + obj.m13);
                System.out.println("--M23= " + obj.m23);
                System.out.println("--M33= " + obj.m33);
                if (ice2DContainer.getSide() == 1) {
                    final Matrix4f matrix4f3 = new Matrix4f();
                    matrix4f3.setIdentity();
                    matrix4f3.rotY((float)Math.toRadians(180.0));
                    final Matrix4f matrix4f4 = new Matrix4f();
                    matrix4f4.setIdentity();
                    matrix4f4.setTranslation(new Vector3f(-30.0f, 0.0f, 0.0f));
                    obj.mul(matrix4f3);
                    obj.mul(matrix4f4);
                }
                this.elevationReportTag = (Ice2DTextNode)new Ice2DMultipleTextNode("Dimensions", (TransformableEntity)this, obj, string, Color.blue, new Point3f(this.getXDimension() / 2.0f, icdPost.getHeight(), 0.0f));
                final int tagFontSize = this.getSolution().getTagFontSize();
                this.elevationReportTag.showWithWhiteBackground(true);
                this.elevationReportTag.setFont(new Font("Arial", 0, tagFontSize));
                this.elevationReportTag.setCentered(true);
                if (this.elevationReportTag.getParent() == null) {
                    ice2DContainer.add((Ice2DNode)this.elevationReportTag);
                }
            }
        }
    }
    
    public boolean isCurvedIntersection() {
        if (this.getIntersectionType() == 2) {
            final String id = this.getCurrentOption().getId();
            if (id != null && (id.indexOf("Curved") >= 0 || id.indexOf("curved") >= 0)) {
                return true;
            }
        }
        return false;
    }
    
    public void setVerticalChase(final ICDVerticalChase verticalChase) {
        this.verticalChase = verticalChase;
    }
    
    public ICDVerticalChase getVerticalChase() {
        return this.verticalChase;
    }
    
    public Collection<JointIntersectable> getAllIntersectables() {
        final Vector<JointIntersectable> vector = new Vector<JointIntersectable>();
        for (final JointIntersectable jointIntersectable : this.getChildrenByClass((Class)JointIntersectable.class, true, true)) {
            if (jointIntersectable.doesParticipateInJointIntersection()) {
                vector.add(jointIntersectable);
            }
        }
        for (final Segment segment : this.getSegmentsFromArms()) {
            if (segment instanceof ICDSegment) {
                for (final JointIntersectable jointIntersectable2 : ((ICDSegment)segment).getAllIntersectables()) {
                    if (!vector.contains(jointIntersectable2)) {
                        vector.add(jointIntersectable2);
                    }
                }
            }
        }
        return vector;
    }
    
    public boolean canBuildOwnGraphEdges(final Segment segment) {
        return true;
    }
    
    private Vector<Float> getSplitFromBeam() {
        Vector<Float> vector = null;
        final Iterator<IntersectionArmInterface> iterator = this.getArmVector().iterator();
        while (iterator.hasNext()) {
            final Segment segment = iterator.next().getSegment();
            if (segment != null && segment instanceof ICDBeamSegment) {
                final Vector<Float> tubeLocations = ((ICDBeamSegment)segment).getTubeLocations();
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
    
    private boolean validForNonBreakIntersection() {
        if (this.getIntersectionType() > 2) {
            final Vector armsOrderedByIndex = this.getArmsOrderedByIndex();
            final Vector<Boolean> vector = new Vector<Boolean>();
            final Iterator<IntersectionArmInterface> iterator = armsOrderedByIndex.iterator();
            while (iterator.hasNext()) {
                final ILineInterface wallSet = iterator.next().getWallSet();
                vector.add(wallSet != null && wallSet instanceof ICDBeamILine);
            }
            if (armsOrderedByIndex.size() == 3 && !vector.get(0) && !vector.get(1) && vector.get(2)) {
                return true;
            }
            if (armsOrderedByIndex.size() == 4) {
                if (vector.get(0) && !vector.get(1) && vector.get(2) && !vector.get(3)) {
                    return true;
                }
                if (!vector.get(0) && vector.get(1) && !vector.get(2) && vector.get(3)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean BeamWantBreakPanel() {
        return "Yes".equals(this.getAttributeValueAsString("ICD_WantBreakIntersection"));
    }
    
    protected OptionObject getBestOptionByConnector() {
        if (this.getIntersectionType() < 3) {
            this.removeAttribute("ICD_WantBreakIntersection");
        }
        if (this.qualifiedOptions.size() > 1) {
            final boolean validForNonBreakIntersection = this.validForNonBreakIntersection();
            final boolean beamWantBreakPanel = this.BeamWantBreakPanel();
            if (!validForNonBreakIntersection || beamWantBreakPanel) {
                this.removeFromQualifiedOptions(false);
            }
            else if (validForNonBreakIntersection && !beamWantBreakPanel) {
                this.removeFromQualifiedOptions(true);
            }
        }
        return super.getBestOptionByConnector();
    }
    
    private void removeFromQualifiedOptions(final boolean b) {
        int i = 0;
        final boolean b2 = this.getIntersectionType() == 4;
        boolean b3 = false;
        if (b && b2) {
            final Vector armsOrderedByIndex = this.getArmsOrderedByIndex();
            if (armsOrderedByIndex.size() > 0) {
                final Segment segment = armsOrderedByIndex.get(0).getSegment();
                if (segment != null && segment instanceof ICDBeamSegment) {
                    b3 = true;
                }
            }
        }
        if (this.qualifiedOptions.size() > 0) {
            while (i < this.qualifiedOptions.size()) {
                final OptionObject optionObject = this.qualifiedOptions.get(i);
                final String attributeValueAsString = optionObject.getAttributeValueAsString("NonBreakingArms");
                final boolean b4 = attributeValueAsString != null && attributeValueAsString.length() > 0;
                if ((!b && b4) || (b && !b4)) {
                    this.qualifiedOptions.remove(optionObject);
                }
                else if (b && b2 && b4) {
                    if (b3 ^ attributeValueAsString.indexOf("1") != -1) {
                        this.qualifiedOptions.remove(optionObject);
                    }
                    else {
                        ++i;
                    }
                }
                else {
                    ++i;
                }
            }
        }
    }
    
    public void addOnTheFlyAttribute() {
        super.addOnTheFlyAttribute();
        if (this.isNonBreakingIntersection()) {
            this.createNewAttribute("ICD_BeamBreakPanel", "No", false, false);
        }
    }
    
    public void removeOnTheFlyAttribute() {
        this.removeAttribute("ICD_BeamBreakPanel");
    }
    
    public void getOnTheFlyAttributes(final Collection<Attribute> collection) {
        super.getOnTheFlyAttributes((Collection)collection);
        collection.add(this.getAttributeObject("ICD_BeamBreakPanel"));
    }
    
    public void setLocalVariables(final String anObject) {
        if ("ICD_BeamBreakPanel".equals(anObject)) {
            if ("Yes".equals(this.getAttributeValueAsString("ICD_BeamBreakPanel"))) {
                this.createNewAttribute("ICD_WantBreakIntersection", "Yes", true, false);
            }
            else {
                this.removeAttribute("ICD_WantBreakIntersection");
            }
        }
    }
    
    private void markOffModuleIntersectionSegment() {
        if (this.getIntersectionType() < 3) {
            return;
        }
        final float n = 0.5f;
        boolean b = false;
        ICDSegment icdSegment = null;
        final Iterator<IntersectionArmInterface> iterator = (Iterator<IntersectionArmInterface>)this.getArmVector().iterator();
        while (iterator.hasNext()) {
            final Segment segment = iterator.next().getSegment();
            if (segment != null && !(segment instanceof ICDBeamSegment) && segment instanceof ICDSegment && icdSegment == null) {
                final Point3f convertSpaces = MathUtilities.convertSpaces(new Point3f(0.0f, 0.0f, 0.0f), (EntityObject)this, (EntityObject)segment);
                if (convertSpaces.x <= n || convertSpaces.x >= segment.getXDimension() - n) {
                    continue;
                }
                icdSegment = (ICDSegment)segment;
            }
            else {
                if (segment == null || !(segment instanceof ICDBeamSegment)) {
                    continue;
                }
                b = true;
            }
        }
        if (icdSegment != null && b) {
            icdSegment.SetWithOffModuleIntersection(true);
        }
    }
    
    public int getNumberOfChaseAtPoint(final Segment segment, final Point3f point3f) {
        int n = 0;
        final Iterator<Segment> iterator = this.getSegmentsAt90Or270Degree(segment).iterator();
        while (iterator.hasNext()) {
            final PanelSegmentInterface baseSegment = ((BasicSegment)iterator.next()).getBaseSegment();
            if (baseSegment != null) {
                final ICDPanel icdPanel = (ICDPanel)baseSegment.getChildPanel();
                if (icdPanel == null || !icdPanel.hasChaseAtPoint(MathUtilities.convertPointToOtherLocalSpace((EntityObject)icdPanel, point3f))) {
                    continue;
                }
                ++n;
            }
        }
        return n;
    }
    
    public void handleAttributeChange(final String s, final String s2) {
        ICDUtilities.handleAttributeChange((EntityObject)this, s, s2);
    }
    
    public Vector<ILineInterface> getConnectedILines() {
        return (Vector<ILineInterface>)this.getArmWallSets();
    }
    
    public boolean needExtraIndirectAssemblyParts(final ICDPost icdPost) {
        return (this.getIntersectionType() == 4 && this.isSpecial4WayIntersection()) || ((this.getIntersectionType() == 2 || this.getIntersectionType() == 3) && ((icdPost.isAssemblyIncludeExtraTubes() && this.allowUserControlPostAssembly()) || icdPost.isAssemblyIncludeExtraStepReturnTubes()));
    }
    
    private boolean isSpecial4WayIntersection() {
        final Segment[] armSegments = this.getArmSegments();
        final boolean segmentHasDoubleChase = this.segmentHasDoubleChase(armSegments[0]);
        final boolean segmentHasDoubleChase2 = this.segmentHasDoubleChase(armSegments[1]);
        final boolean segmentHasDoubleChase3 = this.segmentHasDoubleChase(armSegments[2]);
        final boolean segmentHasDoubleChase4 = this.segmentHasDoubleChase(armSegments[3]);
        return segmentHasDoubleChase || segmentHasDoubleChase3 || segmentHasDoubleChase2 || segmentHasDoubleChase4;
    }
    
    public boolean allowUserControlPostAssembly() {
        return this.getSegmentsBrokenByOthers().size() != 0;
    }
    
    public boolean allowUserControlStackPanelAssembly() {
        boolean b = false;
        for (final Segment segment : this.getArmSegments()) {
            b |= (segment != null && segment.getStackingPanel() != null);
        }
        return b;
    }
    
    private Collection<Segment> getSegmentsBrokenByOthers() {
        final Vector<Segment> vector = new Vector<Segment>();
        if (this.getIntersectionType() == 2 && this.is90DegreeIntersection()) {
            final Segment[] armSegments = this.getArmSegments();
            if (((ICDSegment)armSegments[0]).brokenByAnotherSegment((ICDSegment)armSegments[1])) {
                vector.add(armSegments[0]);
            }
            if (((ICDSegment)armSegments[1]).brokenByAnotherSegment((ICDSegment)armSegments[0])) {
                vector.add(armSegments[1]);
            }
        }
        else if (this.getIntersectionType() == 3) {
            final Segment[] armSegments2 = this.getArmSegments();
            if (((ICDSegment)armSegments2[0]).brokenByAnotherSegment((ICDSegment)armSegments2[2])) {
                vector.add(armSegments2[0]);
            }
            if (((ICDSegment)armSegments2[1]).brokenByAnotherSegment((ICDSegment)armSegments2[2])) {
                vector.add(armSegments2[1]);
            }
            if (((ICDSegment)armSegments2[2]).brokenByAnotherSegment((ICDSegment)armSegments2[0]) || ((ICDSegment)armSegments2[2]).brokenByAnotherSegment((ICDSegment)armSegments2[1])) {
                vector.add(armSegments2[2]);
            }
        }
        else if (this.getIntersectionType() == 4) {
            final Segment[] armSegments3 = this.getArmSegments();
            if (((ICDSegment)armSegments3[0]).brokenByAnotherSegment((ICDSegment)armSegments3[2])) {
                vector.add(armSegments3[0]);
            }
            if (((ICDSegment)armSegments3[1]).brokenByAnotherSegment((ICDSegment)armSegments3[2])) {
                vector.add(armSegments3[1]);
            }
            if (((ICDSegment)armSegments3[2]).brokenByAnotherSegment((ICDSegment)armSegments3[3])) {
                vector.add(armSegments3[2]);
            }
            if (((ICDSegment)armSegments3[2]).brokenByAnotherSegment((ICDSegment)armSegments3[0]) || ((ICDSegment)armSegments3[2]).brokenByAnotherSegment((ICDSegment)armSegments3[1])) {
                vector.add(armSegments3[2]);
            }
            if (((ICDSegment)armSegments3[3]).brokenByAnotherSegment((ICDSegment)armSegments3[0]) || ((ICDSegment)armSegments3[3]).brokenByAnotherSegment((ICDSegment)armSegments3[2]) || ((ICDSegment)armSegments3[3]).brokenByAnotherSegment((ICDSegment)armSegments3[1])) {
                vector.add(armSegments3[3]);
            }
        }
        return vector;
    }
    
    private Segment[] getArmSegments() {
        final Segment[] array = new Segment[4];
        final Vector armsOrderedByIndex_WithoutCloning = this.getArmsOrderedByIndex_WithoutCloning();
        for (int i = 0; i < armsOrderedByIndex_WithoutCloning.size(); ++i) {
            final IntersectionArmInterface intersectionArmInterface = armsOrderedByIndex_WithoutCloning.get(i);
            if (intersectionArmInterface != null) {
                array[i] = intersectionArmInterface.getSegment();
            }
        }
        return array;
    }
    
    private boolean segmentHasDoubleChase(final Segment segment) {
        boolean hasDoubleChase = false;
        if (segment instanceof ICDSegment) {
            hasDoubleChase = ((ICDSegment)segment).hasDoubleChase();
        }
        return hasDoubleChase;
    }
    
    public void collectExtraIndirectAssemblyParts(final HashSet<EntityObject> set, final boolean b, final boolean b2, final boolean b3, final Class<EntityObject>... array) {
        if (this.getIntersectionType() == 4) {
            this.collectExtraIndirectAssemblyPartsForSpecial4WayIntersection(set, b, b2, b3, array);
        }
        else if (this.getIntersectionType() == 3 || this.getIntersectionType() == 2) {
            this.collectExtraIndirectAssemblyPartsFor2WayAnd3WayIntersections(set, b, b2, b3, array);
        }
    }
    
    private void collectExtraIndirectAssemblyPartsFor2WayAnd3WayIntersections(final HashSet<EntityObject> set, final boolean b, final boolean b2, final boolean b3, final Class<EntityObject>[] array) {
        for (final Segment segment : this.getArmSegments()) {
            if (segment != null) {
                ((ICDSegment)segment).collectExtraIndirectAssemblyParts(this, set, b, b2, b3, array);
            }
        }
    }
    
    private void collectExtraIndirectAssemblyPartsForSpecial4WayIntersection(final HashSet<EntityObject> set, final boolean b, final boolean b2, final boolean b3, final Class<EntityObject>... array) {
        final Segment[] armSegments = this.getArmSegments();
        final boolean segmentHasDoubleChase = this.segmentHasDoubleChase(armSegments[0]);
        final boolean segmentHasDoubleChase2 = this.segmentHasDoubleChase(armSegments[1]);
        final boolean segmentHasDoubleChase3 = this.segmentHasDoubleChase(armSegments[2]);
        final boolean segmentHasDoubleChase4 = this.segmentHasDoubleChase(armSegments[3]);
        if (segmentHasDoubleChase && segmentHasDoubleChase3 && !segmentHasDoubleChase2 && !segmentHasDoubleChase4) {
            ((ICDSegment)armSegments[1]).collectExtraIndirectAssemblyParts(this, set, b, b2, b3, array);
            ((ICDSegment)armSegments[3]).collectExtraIndirectAssemblyParts(this, set, b, b2, b3, array);
        }
        else if (!segmentHasDoubleChase && !segmentHasDoubleChase3 && segmentHasDoubleChase2 && segmentHasDoubleChase4) {
            ((ICDSegment)armSegments[0]).collectExtraIndirectAssemblyParts(this, set, b, b2, b3, array);
            ((ICDSegment)armSegments[2]).collectExtraIndirectAssemblyParts(this, set, b, b2, b3, array);
        }
        else {
            ((ICDSegment)armSegments[0]).collectExtraIndirectAssemblyParts(this, set, b, b2, b3, array);
            ((ICDSegment)armSegments[2]).collectExtraIndirectAssemblyParts(this, set, b, b2, b3, array);
            ((ICDSegment)armSegments[1]).collectExtraIndirectAssemblyParts(this, set, b, b2, b3, array);
            ((ICDSegment)armSegments[3]).collectExtraIndirectAssemblyParts(this, set, b, b2, b3, array);
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
    
    static {
        ICDIntersection.ic = new String[] { "1 Way", "2 Way (180)", "2 Way (90)", "3 Way", "4 Way" };
        ICDIntersection.indicatorMatch = new String[][] { { null, ICDIntersection.ic[0], ICDIntersection.ic[0], ICDIntersection.ic[1], ICDIntersection.ic[3] }, { null, null, null, ICDIntersection.ic[0], ICDIntersection.ic[1] } };
    }
    
    private class SubPostInfo
    {
        private float height;
        private Point3f basePoint;
        
        public SubPostInfo(final Point3f basePoint, final float height) {
            this.height = 0.0f;
            this.basePoint = new Point3f();
            this.basePoint = basePoint;
            this.height = height;
        }
        
        public boolean isContainedBy(final Vector<SubPostInfo> vector) {
            final Iterator<SubPostInfo> iterator = vector.iterator();
            while (iterator.hasNext()) {
                if (MathUtilities.isSamePoint(this.basePoint, iterator.next().getBasePoint(), 0.001f)) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public String toString() {
            return "Height: " + this.getHeight() + " @ " + this.getBasePoint() + " " + this.hashCode();
        }
        
        public Point3f getBasePoint() {
            return this.basePoint;
        }
        
        public void setBasePoint(final Point3f basePoint) {
            this.basePoint = basePoint;
        }
        
        public float getHeight() {
            return this.height;
        }
        
        public void setHeight(final float height) {
            this.height = height;
        }
    }
}
