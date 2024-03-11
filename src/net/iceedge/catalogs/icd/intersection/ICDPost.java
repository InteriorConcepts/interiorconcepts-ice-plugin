package net.iceedge.catalogs.icd.intersection;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import net.iceedge.catalogs.icd.interfaces.ICDInstallTagDrawable;
import net.iceedge.icecore.icecad.ice.tree.IceCadBlock;
import net.dirtt.icecad.cadtree.ICadBlockNode;
import net.iceedge.catalogs.icd.ICDILine;
import net.iceedge.icecore.icecad.ice.IceCadIceApp;
import net.iceedge.icecore.icecad.ice.tree.IceCadNodeContainer;
import net.dirtt.icecad.cadtree.ICadTreeNode;
import java.util.TreeMap;
import java.util.ArrayList;
import com.iceedge.icd.utilities.EntitySpaceCompareUtility;
import java.util.LinkedList;
import net.dirtt.utilities.EntitySpaceCompareNodeWrapper;
import net.dirtt.icelib.main.ElevationEntity;
import net.iceedge.catalogs.icd.panel.ICDAngledPanel;
import net.dirtt.icelib.main.attributes.Attribute;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import net.dirtt.icebox.canvas2d.Ice2DGVTElevationNode;
import net.dirtt.icebox.canvas2d.Ice2DTextNode;
import net.dirtt.icelib.main.LightWeightTypeObject;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationUtilities;
import net.iceedge.catalogs.icd.panel.ICDChaseConnectorExtrusion;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import java.util.List;
import net.dirtt.icebox.canvas2d.Ice2DNode;
import java.awt.Font;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icebox.canvas2d.Ice2DMultipleTextNode;
import java.awt.Color;
import javax.vecmath.Matrix4f;
import org.xith3d.scenegraph.Group;
import net.iceedge.catalogs.icd.ICDUtilities;
import net.dirtt.icelib.report.compare.CompareNode;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicExtrusion;
import com.iceedge.icd.entities.ICDTypeValidatorEntity;
import java.util.HashSet;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import javax.swing.tree.DefaultMutableTreeNode;
import net.iceedge.icebox.utilities.Node;
import java.io.IOException;
import net.dirtt.utilities.PersistentFileManager;
import net.dirtt.xmlFiles.XMLWriter;
import javax.vecmath.Tuple3f;
import net.dirtt.utilities.MathUtilities;
import java.util.Collection;
import net.iceedge.catalogs.icd.panel.ICDPanelToPanelConnectionHW;
import net.iceedge.catalogs.icd.ICDSubILine;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import net.iceedge.catalogs.icd.panel.ICDPanel;
import net.iceedge.catalogs.icd.panel.ICDJoint;
import net.dirtt.icelib.main.RequiredChildTypeContainer;
import net.dirtt.icelib.main.TypeableEntity;
import java.util.Iterator;
import net.iceedge.icecore.basemodule.finalclasses.GeneralSnapSet;
import net.iceedge.icecore.basemodule.interfaces.GeneralIntersectionInterface;
import net.iceedge.icecore.basemodule.interfaces.IntersectionArmInterface;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.catalogs.icd.icecad.ICDCadTagDelegate;
import net.iceedge.catalogs.icd.panel.ICDMiddleJoint;
import net.iceedge.catalogs.icd.panel.ICDSubInternalExtrusion;
import java.util.Vector;
import org.apache.log4j.Logger;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.catalogs.icd.icecad.ICDCadTagPaintable;
import net.iceedge.icebox.attribute.OnTheFlyAttributeInterface;
import net.iceedge.icebox.utilities.MultiTagAppender;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintable;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintableRoot;
import net.iceedge.icecore.basemodule.interfaces.AssembleParent;
import net.iceedge.icecore.basemodule.baseclasses.BasicPost;

public class ICDPost extends BasicPost implements AssembleParent, AssemblyPaintableRoot, AssemblyPaintable, MultiTagAppender, OnTheFlyAttributeInterface, ICDCadTagPaintable, ICDManufacturingReportable
{
    private static final long serialVersionUID = 5553576903732527878L;
    private static Logger logger;
    public static final String MANUFACTURING_REPORT_DESCRIPTION = "Assembled partition frame";
    private Vector<ICDSubInternalExtrusion> tubings;
    private Vector<ICDMiddleJoint> joints;
    private String oldContainer;
    public static float BIG_NEGATIVE;
    private float chaseTopHeight;
    private float chaseBottomHeight;
    private static int MAX_ALLOWED_OVERHEAD_NUMBER;
    private int oldCornerSlotCount;
    private boolean assemblyIncludeExtraTubes;
    private boolean assemblyIncludeExtraStepReturnTubes;
    private ICDCadTagDelegate cadTagDelegate;
    
    public ICDPost(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.tubings = new Vector<ICDSubInternalExtrusion>();
        this.joints = new Vector<ICDMiddleJoint>();
        this.oldContainer = "null";
        this.chaseTopHeight = 29.0f;
        this.chaseBottomHeight = 1.0f;
        this.oldCornerSlotCount = 0;
        this.assemblyIncludeExtraTubes = false;
        this.assemblyIncludeExtraStepReturnTubes = false;
        this.setupNamedPoints();
    }
    
    public ICDPost buildClone(final ICDPost icdPost) {
        super.buildClone((BasicPost)icdPost);
        icdPost.assemblyIncludeExtraTubes = this.assemblyIncludeExtraTubes;
        icdPost.assemblyIncludeExtraStepReturnTubes = this.assemblyIncludeExtraStepReturnTubes;
        return icdPost;
    }
    
    public Object clone() {
        return this.buildClone(new ICDPost(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDPost(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public EntityObject buildFrameClone(final ICDPost icdPost, final EntityObject entityObject) {
        return super.buildFrameClone((TransformableTriggerUser)icdPost, entityObject);
    }
    
    protected void calculateDimensions() {
        super.calculateDimensions();
        this.calculateZDimension();
    }
    
    protected void calculateZDimension() {
        final ICDPostHostInterface postHostInterface = this.getPostHostInterface();
        if (postHostInterface != null) {
            this.setZDimension(postHostInterface.getTallestWallHeight());
        }
    }
    
    protected void setupNamedPoints() {
        super.setupNamedPoints();
        this.addNamedPoint("bottomJointBP", new Point3f());
        this.addNamedPoint("topJointBP", new Point3f());
        this.addNamedPoint("Mid_Connector_Container_POS", new Point3f());
        this.addNamedRotation("Mid_Connector_Container_ROT", new Vector3f());
        this.addNamedPoint("Spot1", new Point3f(0.0f, 1.0f, 0.0f));
        this.addNamedPoint("Spot2", new Point3f(1.0f, 0.0f, 0.0f));
        this.addNamedPoint("Spot3", new Point3f(0.0f, -1.0f, 0.0f));
        this.addNamedPoint("Spot4", new Point3f(-1.0f, 0.0f, 0.0f));
        this.addNamedRotation("chaseRot", new Vector3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("CAD_TAG", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedScale("CADELWIDTH", new Vector3f(1.0f, 1.0f, 1.0f));
        this.addNamedPoint("BL", new Point3f());
        this.addNamedPoint("BM", new Point3f());
        this.addNamedPoint("BR", new Point3f());
        this.addNamedPoint("TL", new Point3f());
        this.addNamedPoint("TM", new Point3f());
        this.addNamedPoint("TR", new Point3f());
        this.addNamedPoint("ML", new Point3f());
        this.addNamedPoint("MM", new Point3f());
        this.addNamedPoint("MR", new Point3f());
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("CAD_TAG").set(this.getXDimension() / 2.0f, this.getYDimension() / 2.0f, this.getZDimension() + 5.0f);
        this.getNamedPointLocal("bottomJointBP").set(0.0f, 0.0f, 1.25f);
        this.getNamedPointLocal("topJointBP").set(0.0f, 0.0f, this.getZDimension() - 0.5f);
        final ICDPostHostInterface postHostInterface = this.getPostHostInterface();
        if (postHostInterface != null) {
            final Vector<Float> splitLocations = postHostInterface.getSplitLocations();
            if (splitLocations != null && splitLocations.size() >= 2) {
                this.getNamedPointLocal("topJointBP").set(0.0f, 0.0f, splitLocations.lastElement() + 0.5f);
            }
        }
        final GeneralIntersectionInterface parentIntersection = this.getParentIntersection();
        if (parentIntersection != null && parentIntersection.getIntersectionType() == 2 && "ICD_TwoWayCurvedPostType".equals(this.getCurrentType().getId())) {
            final Vector<IntersectionArmInterface> armVector = parentIntersection.getArmVector();
            if (armVector != null && armVector.size() > 0) {
                final IntersectionArmInterface intersectionArmInterface = armVector.get(0);
                if (intersectionArmInterface != null) {
                    this.getNamedPointLocal("ASE_IP").set(intersectionArmInterface.getPullBack(), 0.0f, 0.0f);
                }
            }
        }
        this.getNamedPointLocal("BL").set(12.5f, 0.0f, 1.5f);
        this.getNamedPointLocal("BM").set(0.0f, 0.0f, 1.5f);
        this.getNamedPointLocal("BR").set(-8.5f, -8.5f, 1.5f);
        this.getNamedPointLocal("TL").set(12.5f, 0.0f, this.getZDimension() + 0.5f);
        this.getNamedPointLocal("TM").set(0.0f, 0.0f, this.getZDimension() + 0.5f);
        this.getNamedPointLocal("TR").set(-8.5f, -8.5f, this.getZDimension() + 0.5f);
        if (this.isSplit()) {
            final float n = this.getWorksurfaceHeight() - 1.75f;
            this.getNamedPointLocal("ML").set(12.5f, 0.0f, n);
            this.getNamedPointLocal("MM").set(0.0f, 0.0f, n);
            this.getNamedPointLocal("MR").set(-8.5f, -8.5f, n);
        }
    }
    
    public void solve() {
        final boolean curvedPost = this.isCurvedPost();
        final boolean modified = this.isModified();
        boolean b = false;
        final GeneralSnapSet generalSnapSet = this.getGeneralSnapSet();
        if (generalSnapSet != null && generalSnapSet.isAutoFixSolve()) {
            b = true;
        }
        if (modified && !curvedPost && !b) {
            this.calculateChildTubingAndJoints();
        }
        if (!this.isSubFramePost() && !b) {
            this.validateChaseMidConnectors();
        }
        super.solve();
    }
    
    public void handleWarnings() {
        super.handleWarnings();
    }
    
    private void calculateChildTubingAndJoints() {
        this.collectTubingAndJoints();
        this.validateJointsTubings();
        this.destroyUnusedTubingAndJoints();
    }
    
    private void collectTubingAndJoints() {
        this.tubings.clear();
        this.joints.clear();
        final Iterator<EntityObject> children = this.getChildren();
        while (children.hasNext()) {
            final EntityObject entityObject = children.next();
            if (entityObject instanceof ICDMiddleJoint) {
                this.joints.add((ICDMiddleJoint)entityObject);
            }
            else {
                if (!(entityObject instanceof ICDSubInternalExtrusion)) {
                    continue;
                }
                this.tubings.add((ICDSubInternalExtrusion)entityObject);
            }
        }
    }
    
    private void validateJointsTubings() {
        final ICDPostHostInterface postHostInterface = this.getPostHostInterface();
        if (postHostInterface != null) {
            final Vector<Float> splitLocations = postHostInterface.getSplitLocations();
            if (splitLocations != null && splitLocations.size() >= 2) {
                final TypeObject childType = this.getChildTypeFor(ICDSubInternalExtrusion.class);
                final TypeObject childType2 = this.getChildTypeFor(ICDMiddleJoint.class);
                final Point3f basePoint = new Point3f();
                final Point3f basePoint2 = new Point3f();
                float floatValue = splitLocations.get(0);
                basePoint.z = floatValue + 1.0f;
                Object o = null;
                Object o2 = null;
                Object firstMiddleJoint = null;
                for (int i = 1; i < splitLocations.size(); ++i) {
                    basePoint2.z = splitLocations.get(i) + 0.5f;
                    if (childType2 != null && i < splitLocations.size() - 1) {
                        firstMiddleJoint = this.getFirstMiddleJoint(childType2);
                        if (firstMiddleJoint == null) {
                            if (o2 != null) {
                                firstMiddleJoint = o2;
                            }
                            else {
                                firstMiddleJoint = getTypeableEntityInstance("", childType2, childType2.getDefaultOption());
                            }
                            if (firstMiddleJoint != null) {
                                this.addToTree((EntityObject)firstMiddleJoint);
                            }
                        }
                        if (firstMiddleJoint != null) {
                            ((ICDMiddleJoint)firstMiddleJoint).setBasePoint(basePoint2);
                            if (this.getRotationWorldSpace() != 0.0f) {
                                ((ICDMiddleJoint)firstMiddleJoint).setRotation(3.1415927f);
                            }
                            else {
                                ((ICDMiddleJoint)firstMiddleJoint).setRotation(6.2831855f);
                            }
                            ((ICDMiddleJoint)firstMiddleJoint).solve();
                            o2 = ((ICDMiddleJoint)firstMiddleJoint).clone();
                        }
                    }
                    if (i == splitLocations.size() - 1) {
                        firstMiddleJoint = null;
                    }
                    if (childType != null) {
                        if (firstMiddleJoint == null || !((ICDMiddleJoint)firstMiddleJoint).isBoltOnJoint()) {
                            Object firstTubing = this.getFirstTubing(childType);
                            final float floatValue2 = splitLocations.get(i);
                            if (firstTubing == null) {
                                if (o != null) {
                                    firstTubing = o;
                                }
                                else {
                                    firstTubing = getTypeableEntityInstance("", childType, childType.getDefaultOption());
                                }
                                if (firstTubing != null) {
                                    this.addToTree((EntityObject)firstTubing);
                                }
                            }
                            if (firstTubing != null) {
                                ((ICDSubInternalExtrusion)firstTubing).setBasePoint(basePoint);
                                ((ICDSubInternalExtrusion)firstTubing).setZDimension(floatValue2 - floatValue - 1.0f);
                                ((ICDSubInternalExtrusion)firstTubing).solve();
                                o = ((ICDSubInternalExtrusion)firstTubing).clone();
                            }
                            floatValue = floatValue2;
                            basePoint.z = floatValue + 1.0f;
                        }
                    }
                }
            }
        }
    }
    
    protected ICDSubInternalExtrusion getFirstTubing(final TypeObject typeObject) {
        Object o = null;
        if (this.tubings.size() > 0) {
            o = this.tubings.firstElement();
            this.tubings.remove(o);
        }
        return (ICDSubInternalExtrusion)o;
    }
    
    protected ICDMiddleJoint getFirstMiddleJoint(final TypeObject typeObject) {
        Object o = null;
        if (this.joints.size() > 0) {
            o = this.joints.firstElement();
            this.joints.remove(o);
        }
        return (ICDMiddleJoint)o;
    }
    
    private void destroyUnusedTubingAndJoints() {
        final Iterator<ICDSubInternalExtrusion> iterator = this.tubings.iterator();
        while (iterator.hasNext()) {
            iterator.next().destroy();
        }
        this.tubings.clear();
        final Iterator<ICDMiddleJoint> iterator2 = this.joints.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().destroy();
        }
        this.joints.clear();
    }
    
    private TypeableEntity createNewExtraPost(final int n) {
        TypeableEntity typeableEntityInstance = null;
        final RequiredChildTypeContainer requiredChildTypeByIndex = this.getRequiredChildTypeByIndex(n);
        if (requiredChildTypeByIndex != null) {
            final TypeObject type = requiredChildTypeByIndex.getType();
            typeableEntityInstance = getTypeableEntityInstance("", type, type.getDefaultOption());
        }
        return typeableEntityInstance;
    }
    
    public String getJointTypeAtLocation(final Point3f point3f) {
        final ICDPostHostInterface postHostInterface = this.getPostHostInterface();
        if (postHostInterface != null) {
            return postHostInterface.getJointTypeAtLocation(point3f);
        }
        return ICDJoint.JOINT_TYPE[0];
    }
    
    protected ICDPostHostInterface getPostHostInterface() {
        return (ICDPostHostInterface)this.getParent(ICDPostHostInterface.class);
    }
    
    public boolean isSubFramePost() {
        return this instanceof ICDPostForSubPanel;
    }
    
    public void validateChaseMidConnectors() {
        float n = this.getChaseTopHeight();
        float n2 = this.getChaseTopHeight();
        float n3 = this.getChaseTopHeight();
        float n4 = this.getChaseTopHeight();
        float n5 = 0.0f;
        final ICDChaseMidConnectorContainer icdChaseMidConnectorContainer = (ICDChaseMidConnectorContainer)this.getChildByClass(ICDChaseMidConnectorContainer.class);
        final Vector<ICDPanel> vector = new Vector<ICDPanel>();
        if (icdChaseMidConnectorContainer != null) {
            final ICDIntersection icdIntersection = (ICDIntersection)this.getParent(ICDIntersection.class);
            if (icdIntersection != null) {
                final Iterator<Segment> iterator = icdIntersection.getSegmentsFromArms().iterator();
                while (iterator.hasNext()) {
                    for (final ICDPanel e : iterator.next().getChildrenByClass(ICDPanel.class, true, true)) {
                        if (e.isCorePanel()) {
                            vector.add(e);
                        }
                    }
                }
            }
            else if (this.getParentByClassRecursive(ICDSubILine.class) != null) {
                final EntityObject parentEntity = this.getParentEntity();
                if (parentEntity != null && parentEntity instanceof ICDPanelToPanelConnectionHW) {
                    vector.addAll(((ICDPanelToPanelConnectionHW)parentEntity).getCorePanelsVector());
                }
            }
            boolean b = false;
            boolean b2 = false;
            boolean b3 = false;
            boolean b4 = false;
            int n6 = 0;
            final ICDPanel[] array = new ICDPanel[5];
            boolean underChaseContainer = false;
            for (int i = 1; i <= 4; ++i) {
                final ICDPanel panelArmFromSpot = this.getPanelArmFromSpot(i, vector);
                array[i] = panelArmFromSpot;
                ++n6;
                if (panelArmFromSpot != null) {
                    final boolean underChase = panelArmFromSpot.isUnderChase();
                    if (panelArmFromSpot.isSuspendedChase()) {
                        n5 = panelArmFromSpot.getSuspendedOffset() - 2.0f;
                    }
                    else {
                        float n7;
                        if (underChase) {
                            n7 = panelArmFromSpot.getHeight() - 2.0f;
                        }
                        else {
                            n7 = panelArmFromSpot.getWorksurfaceHeight() - 4.0f;
                        }
                        if (n7 > n5) {
                            n5 = n7;
                        }
                    }
                    underChaseContainer = (underChaseContainer || underChase);
                    icdChaseMidConnectorContainer.setUnderChaseContainer(underChaseContainer);
                    if (i == 1) {
                        b = false;
                    }
                    else if (i == 2) {
                        b2 = false;
                    }
                    else if (i == 3) {
                        b3 = false;
                    }
                    else if (i == 4) {
                        b4 = false;
                    }
                    int j = n6 - 1;
                    int k = n6 + 1;
                    if (n6 == 1) {
                        j = 4;
                    }
                    if (n6 == 4) {
                        k = 1;
                    }
                    ICDPanel panelArmFromSpot2 = null;
                    ICDPanel panelArmFromSpot3 = null;
                    if (!this.isFakePost()) {
                        panelArmFromSpot2 = this.getPanelArmFromSpot(j, vector);
                        panelArmFromSpot3 = this.getPanelArmFromSpot(k, vector);
                    }
                    if (panelArmFromSpot2 != null) {
                        if (j == 1) {
                            b = false;
                        }
                        else if (j == 2) {
                            b2 = false;
                        }
                        else if (j == 3) {
                            b3 = false;
                        }
                        else if (j == 4) {
                            b4 = false;
                        }
                    }
                    else if (panelArmFromSpot.hasChaseOnPointSide(MathUtilities.convertSpaces(this.getNamedPointLocal("Spot" + j), (EntityObject)this, (EntityObject)panelArmFromSpot))) {
                        boolean b5 = false;
                        if (panelArmFromSpot.isUnderChase()) {
                            this.setChaseTopHeight(panelArmFromSpot.getHeight() - 2.0f);
                        }
                        else {
                            this.setChaseTopHeight(panelArmFromSpot.getDefaultWorksurfaceHeight() - 2.25f);
                        }
                        if (panelArmFromSpot.isSuspendedChase()) {
                            b5 = true;
                            this.setChaseBottomHeight(panelArmFromSpot.getSuspendedOffset());
                        }
                        icdChaseMidConnectorContainer.setIsSuspendedContainer(panelArmFromSpot.isSuspendedChase());
                        if (j == 1) {
                            b = true;
                            if (b5) {
                                n = this.getChaseBottomHeight();
                            }
                        }
                        else if (j == 2) {
                            b2 = true;
                            if (b5) {
                                n2 = this.getChaseBottomHeight();
                            }
                        }
                        else if (j == 3) {
                            b3 = true;
                            if (b5) {
                                n3 = this.getChaseBottomHeight();
                            }
                        }
                        else if (j == 4) {
                            b4 = true;
                            if (b5) {
                                n4 = this.getChaseBottomHeight();
                            }
                        }
                    }
                    if (panelArmFromSpot3 != null) {
                        if (k == 1) {
                            b = false;
                        }
                        else if (k == 2) {
                            b2 = false;
                        }
                        else if (k == 3) {
                            b3 = false;
                        }
                        else if (k == 4) {
                            b4 = false;
                        }
                    }
                    else if (panelArmFromSpot.hasChaseOnPointSide(MathUtilities.convertSpaces(this.getNamedPointLocal("Spot" + k), (EntityObject)this, (EntityObject)panelArmFromSpot))) {
                        boolean b6 = false;
                        if (panelArmFromSpot.isUnderChase()) {
                            this.setChaseTopHeight(panelArmFromSpot.getHeight() - 2.0f);
                        }
                        else {
                            this.setChaseTopHeight(panelArmFromSpot.getDefaultWorksurfaceHeight() - 2.25f);
                        }
                        if (panelArmFromSpot.isSuspendedChase()) {
                            b6 = true;
                            this.setChaseBottomHeight(panelArmFromSpot.getSuspendedOffset());
                        }
                        icdChaseMidConnectorContainer.setIsSuspendedContainer(panelArmFromSpot.isSuspendedChase());
                        if (k == 1) {
                            b = true;
                            if (b6) {
                                n = this.getChaseBottomHeight();
                            }
                        }
                        else if (k == 2) {
                            b2 = true;
                            if (b6) {
                                n2 = this.getChaseBottomHeight();
                            }
                        }
                        else if (k == 3) {
                            b3 = true;
                            if (b6) {
                                n3 = this.getChaseBottomHeight();
                            }
                        }
                        else if (k == 4) {
                            b4 = true;
                            if (b6) {
                                n4 = this.getChaseBottomHeight();
                            }
                        }
                    }
                }
            }
            float n8 = 0.0f;
            final boolean chaseSingle = icdChaseMidConnectorContainer.isChaseSingle();
            float calculateOffsetA;
            if (chaseSingle) {
                calculateOffsetA = this.calculateOffsetA(array, b, b2, b3, b4);
            }
            else {
                final float[] calculateOffsetAandB = this.calculateOffsetAandB(array, b, b2, b3, b4);
                calculateOffsetA = calculateOffsetAandB[0];
                n8 = calculateOffsetAandB[1];
            }
            icdChaseMidConnectorContainer.resetContainer(this.getChaseTopHeight(), this.getChaseBottomHeight(), chaseSingle, calculateOffsetA, n8, n5);
            this.setContainerForSpots(b, b2, b3, b4, n, n2, n3, n4, icdChaseMidConnectorContainer, array, calculateOffsetA, n8);
        }
    }
    
    public void setModified(final boolean modified) {
        super.setModified(modified);
        for (final ICDPost icdPost : this.getChildrenByClass(ICDPost.class, false, false)) {
            if (icdPost.isFakePost()) {
                icdPost.setModified(true);
            }
        }
        final ICDPostHostInterface icdPostHostInterface = (ICDPostHostInterface)this.getParent(ICDPostHostInterface.class);
        if (icdPostHostInterface != null && icdPostHostInterface instanceof ICDPanelToPanelConnectionHW) {
            final Iterator<ICDJoint> iterator2 = this.getChildrenByClass(ICDJoint.class, false, true).iterator();
            while (iterator2.hasNext()) {
                iterator2.next().setModified(modified);
            }
            final Iterator<ICDCornerSlot> iterator3 = this.getChildrenByClass(ICDCornerSlot.class, true, false).iterator();
            while (iterator3.hasNext()) {
                iterator3.next().setModified(modified);
            }
        }
    }
    
    public float calculateOffsetA(final ICDPanel[] array, final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        float n = 0.0f;
        final float n2 = 1.0f;
        if (b || b3) {
            Point3f point3f;
            if (b) {
                point3f = new Point3f(0.0f, n2, 0.0f);
            }
            else {
                point3f = new Point3f(0.0f, -n2, 0.0f);
            }
            for (int i = 2; i <= 4; i += 2) {
                final ICDPanel icdPanel = array[i];
                if (icdPanel != null) {
                    final float chaseOffset = icdPanel.getChaseOffset(this.getPanelSide(icdPanel, point3f));
                    if (chaseOffset > n) {
                        n = chaseOffset;
                    }
                }
            }
        }
        else if (b2 || b4) {
            Point3f point3f2;
            if (b2) {
                point3f2 = new Point3f(n2, 0.0f, 0.0f);
            }
            else {
                point3f2 = new Point3f(-n2, 0.0f, 0.0f);
            }
            for (int j = 1; j <= 3; j += 2) {
                final ICDPanel icdPanel2 = array[j];
                if (icdPanel2 != null) {
                    final float chaseOffset2 = icdPanel2.getChaseOffset(this.getPanelSide(icdPanel2, point3f2));
                    if (chaseOffset2 > n) {
                        n = chaseOffset2;
                    }
                }
            }
        }
        return n;
    }
    
    public float[] calculateOffsetAandB(final ICDPanel[] array, final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        float n = 0.0f;
        float n2 = 0.0f;
        final float n3 = 10.0f;
        if (b && b3) {
            final Point3f point3f = new Point3f(0.0f, n3, 0.0f);
            final Point3f point3f2 = new Point3f(0.0f, -n3, 0.0f);
            for (int i = 2; i <= 4; i += 2) {
                final ICDPanel icdPanel = array[i];
                if (icdPanel != null) {
                    final float chaseOffset = icdPanel.getChaseOffset(this.getPanelSide(icdPanel, point3f));
                    if (chaseOffset > n) {
                        n = chaseOffset;
                    }
                    final float chaseOffset2 = icdPanel.getChaseOffset(this.getPanelSide(icdPanel, point3f2));
                    if (chaseOffset2 > n2) {
                        n2 = chaseOffset2;
                    }
                }
            }
        }
        else if (b2 && b4) {
            final Point3f point3f3 = new Point3f(n3, 0.0f, 0.0f);
            final Point3f point3f4 = new Point3f(-n3, 0.0f, 0.0f);
            for (int j = 1; j <= 3; j += 2) {
                final ICDPanel icdPanel2 = array[j];
                if (icdPanel2 != null) {
                    final float chaseOffset3 = icdPanel2.getChaseOffset(this.getPanelSide(icdPanel2, point3f3));
                    if (chaseOffset3 > n) {
                        n = chaseOffset3;
                    }
                    final float chaseOffset4 = icdPanel2.getChaseOffset(this.getPanelSide(icdPanel2, point3f4));
                    if (chaseOffset4 > n2) {
                        n2 = chaseOffset4;
                    }
                }
            }
        }
        return new float[] { n, n2 };
    }
    
    private int getPanelSide(final ICDPanel icdPanel, final Point3f point3f) {
        return icdPanel.isPointOnSideA(MathUtilities.convertSpaces(point3f, (EntityObject)this, (EntityObject)icdPanel)) ? 0 : 1;
    }
    
    private ICDPanel getPanelArmFromSpot(final int i, final Vector<ICDPanel> vector) {
        for (final ICDPanel icdPanel : vector) {
            final Point3f convertSpaces = MathUtilities.convertSpaces(this.getNamedPointLocal("Spot" + i), (EntityObject)this, (EntityObject)icdPanel);
            convertSpaces.y = 0.0f;
            if (icdPanel.getBoundingCube().contains(convertSpaces.x, convertSpaces.y, convertSpaces.z)) {
                return icdPanel;
            }
        }
        return null;
    }
    
    private void setContainerSize(final int n, final ICDChaseMidConnectorContainer icdChaseMidConnectorContainer) {
        if (icdChaseMidConnectorContainer != null) {
            if (n == 2) {
                icdChaseMidConnectorContainer.applyChangesForAttribute("ICD_Chase_Connector_Container_Size", "Double");
            }
            else if (n == 1) {
                icdChaseMidConnectorContainer.applyChangesForAttribute("ICD_Chase_Connector_Container_Size", "Single");
            }
            else {
                icdChaseMidConnectorContainer.applyChangesForAttribute("ICD_Chase_Connector_Container_Size", "None");
            }
            icdChaseMidConnectorContainer.setModified(true);
            icdChaseMidConnectorContainer.solve();
        }
    }
    
    private void setContainerSuspendedHeight(final float n, final float n2, final float n3, final float n4) {
        final EntityObject childByClass = this.getChildByClass(ICDChaseMidConnectorContainer.class);
        if (childByClass != null) {
            ((ICDChaseMidConnectorContainer)childByClass).refreshContainer(n, n2, this.getChaseTopHeight(), this.getChaseBottomHeight(), n3, n4);
        }
    }
    
    public void setMidChaseContainerRotation(final int n) {
        if (n == 1) {
            this.getNamedRotationLocal("Mid_Connector_Container_ROT").set((Tuple3f)new Vector3f(0.0f, 0.0f, 0.0f));
        }
        else if (n == 2) {
            this.getNamedRotationLocal("Mid_Connector_Container_ROT").set((Tuple3f)new Vector3f(0.0f, 0.0f, (float)Math.toRadians(270.0)));
        }
        else if (n == 3) {
            this.getNamedRotationLocal("Mid_Connector_Container_ROT").set((Tuple3f)new Vector3f(0.0f, 0.0f, 0.0f));
            this.getNamedRotationLocal("Mid_Connector_Container_ROT").set((Tuple3f)new Vector3f(0.0f, 0.0f, (float)Math.toRadians(180.0)));
        }
        else {
            this.getNamedRotationLocal("Mid_Connector_Container_ROT").set((Tuple3f)new Vector3f(0.0f, 0.0f, (float)Math.toRadians(90.0)));
        }
    }
    
    protected void writeXMLFields(final XMLWriter xmlWriter, final PersistentFileManager.FileWriter fileWriter) throws IOException {
        super.writeXMLFields(xmlWriter, fileWriter);
        xmlWriter.writeTextElement("oldContainer", this.oldContainer + "");
        xmlWriter.writeTextElement("assemblyIncludeExtraTubes", this.isAssemblyIncludeExtraTubes() + "");
        xmlWriter.writeTextElement("assemblyIncludeExtraStepReturnTubes", this.isAssemblyIncludeExtraStepReturnTubes() + "");
    }
    
    protected void setFieldInfoFromXML(final Node node, final DefaultMutableTreeNode defaultMutableTreeNode, final PersistentFileManager.FileReader fileReader) {
        super.setFieldInfoFromXML(node, defaultMutableTreeNode, fileReader);
        this.oldContainer = this.getStringValueFromXML("oldContainer", node, "null");
        this.setAssemblyIncludeExtraTubes(this.getBooleanValueFromXML("assemblyIncludeExtraTubes", node, false));
        this.setAssemblyIncludeExtraStepReturnTubes(this.getBooleanValueFromXML("assemblyIncludeExtraStepReturnTubes", node, false));
    }
    
    private void setContainerForSpots(final boolean b, final boolean b2, final boolean b3, final boolean b4, final float n, final float n2, final float n3, final float n4, final ICDChaseMidConnectorContainer icdChaseMidConnectorContainer, final ICDPanel[] array, final float n5, final float n6) {
        if ((b && b2) || (b2 && b3) || (b3 && b4) || (b4 && b)) {
            this.setContainerSize(0, icdChaseMidConnectorContainer);
        }
        else if ((b && b3) || (b2 && b4)) {
            if (b && b3) {
                this.setContainerUnderChase(icdChaseMidConnectorContainer, array, 2, 4);
                this.setContainerSuspendedHeight(n, n3, n5, n6);
                this.setMidChaseContainerRotation(1);
            }
            else {
                this.setContainerUnderChase(icdChaseMidConnectorContainer, array, 1, 3);
                this.setContainerSuspendedHeight(n2, n4, n5, n6);
                if (this.isFakePost()) {
                    this.setMidChaseContainerRotation(2);
                }
                else {
                    this.setMidChaseContainerRotation(1);
                }
            }
            this.setContainerSize(2, icdChaseMidConnectorContainer);
        }
        else if (b && !b2 && !b3 && !b4) {
            this.setContainerUnderChase(icdChaseMidConnectorContainer, array, 2, 4);
            this.setContainerSuspendedHeight(n, n, n5, n6);
            this.setMidChaseContainerRotation(1);
            this.setContainerSize(1, icdChaseMidConnectorContainer);
        }
        else if (!b && b2 && !b3 && !b4) {
            this.setContainerUnderChase(icdChaseMidConnectorContainer, array, 1, 3);
            this.setContainerSuspendedHeight(n2, n2, n5, n6);
            this.setMidChaseContainerRotation(2);
            this.setContainerSize(1, icdChaseMidConnectorContainer);
        }
        else if (!b && !b2 && b3 && !b4) {
            this.setContainerUnderChase(icdChaseMidConnectorContainer, array, 2, 4);
            this.setContainerSuspendedHeight(n3, n3, n5, n6);
            this.setMidChaseContainerRotation(3);
            this.setContainerSize(1, icdChaseMidConnectorContainer);
        }
        else if (!b && !b2 && !b3 && b4) {
            this.setContainerUnderChase(icdChaseMidConnectorContainer, array, 1, 3);
            this.setContainerSuspendedHeight(n4, n4, n5, n6);
            this.setMidChaseContainerRotation(4);
            this.setContainerSize(1, icdChaseMidConnectorContainer);
        }
        else if (!b && !b2 && !b3 && !b4) {
            this.setContainerSize(0, icdChaseMidConnectorContainer);
        }
    }
    
    private void setContainerUnderChase(final ICDChaseMidConnectorContainer icdChaseMidConnectorContainer, final ICDPanel[] array, final int n, final int n2) {
        if (array != null) {
            boolean underChaseContainer = false;
            if (array[n] != null) {
                underChaseContainer = array[n].isUnderChase();
            }
            if (array[n2] != null) {
                underChaseContainer = array[n2].isUnderChase();
            }
            icdChaseMidConnectorContainer.setUnderChaseContainer(underChaseContainer);
        }
    }
    
    public void draw2DElevation(final int n, final Ice2DContainer ice2DContainer, final boolean b, final SolutionSetting solutionSetting) {
        this.drawChildren2DElevation(n, ice2DContainer, b, solutionSetting);
    }
    
    public float getChaseTopHeight() {
        return this.chaseTopHeight;
    }
    
    public void setChaseTopHeight(final float chaseTopHeight) {
        this.chaseTopHeight = chaseTopHeight;
    }
    
    public float getChaseBottomHeight() {
        return this.chaseBottomHeight;
    }
    
    public void setChaseBottomHeight(final float chaseBottomHeight) {
        this.chaseBottomHeight = chaseBottomHeight;
    }
    
    public HashSet<EntityObject> getDirectAssemblyParts() {
        final HashSet<EntityObject> set = new HashSet<EntityObject>();
        final String id = this.getCurrentOption().getId();
        if ("ICD_Angled_Panel".equals(id)) {
            this.addAssemblyPartsForCurvedOrAngledPanel((HashSet<EntityObject>)set);
        }
        else if (id != null && id.startsWith("ICD_Curved_Panel")) {
            this.addAssemblyPartsForCurvedOrAngledPanel((HashSet<EntityObject>)set);
        }
        else {
            for (final EntityObject e : this.getChildrenByClass(EntityObject.class, false, true)) {
                if (e.containsAttributeKey("isAssembled")) {
                    set.add(e);
                }
            }
            this.addChildrenOfBoltOnJoint((EntityObject)this, (HashSet<EntityObject>)set);
        }
        set.add(this);
        set.addAll(this.getChildrenByClass(ICDCornerSlot.class, true, true));
        return (HashSet<EntityObject>)set;
    }
    
    private void addChildrenOfBoltOnJoint(final EntityObject entityObject, final HashSet<EntityObject> set) {
        final EntityObject childByClass = entityObject.getChildByClass(ICDMiddleJoint.class);
        if (childByClass != null && ((ICDMiddleJoint)childByClass).isBoltOnJoint()) {
            for (final ICDTypeValidatorEntity e : childByClass.getChildrenByClass(ICDTypeValidatorEntity.class, true)) {
                if (e.containsAttributeKey("isAssembled")) {
                    set.add((EntityObject)e);
                }
            }
        }
    }
    
    public HashSet<EntityObject> getIndirectAssemblyParts() {
        final HashSet<EntityObject> set = new HashSet<EntityObject>();
        if ("ICD_Angled_Panel".equals(this.getCurrentOption().getId())) {
            final Iterator<ICDPost> iterator = this.getChildrenByClass(ICDPost.class, false, true).iterator();
            while (iterator.hasNext()) {
                iterator.next().addAssemblyPartsFromChaseMidConnectorContainer(set);
            }
        }
        else {
            this.addAssemblyPartsFromChaseMidConnectorContainer(set);
        }
        return set;
    }
    
    public boolean shouldIncludeExtraIndirectAssemblyParts() {
        return true;
    }
    
    private void addAssemblyPartsFromChaseMidConnectorContainer(final HashSet<EntityObject> set) {
        final Iterator<ICDChaseMidConnectorContainer> iterator = this.getChildrenByClass(ICDChaseMidConnectorContainer.class, false, true).iterator();
        while (iterator.hasNext()) {
            for (final EntityObject e : iterator.next().getChildrenByClass(EntityObject.class, true, true)) {
                if (e.containsAttributeKey("isAssembled")) {
                    set.add(e);
                }
            }
        }
        if (this.needExtraIndirectAssemblyParts()) {
            this.collectExtraIndirectAssemblyParts(set, true, BasicExtrusion.class, ICDJoint.class);
        }
    }
    
    public HashSet<AssembleParent> getExternalAssemblyParts() {
        return new HashSet<AssembleParent>();
    }
    
    public boolean shouldICDMakePreAssembledReport() {
        return this.shouldAssemble();
    }
    
    public void populateCompareNode(final Class clazz, final CompareNode compareNode) {
        if (!ICDUtilities.populateCompareNode(clazz, compareNode, (AssembleParent)this, this.compareSpaces())) {
            super.populateCompareNode(clazz, compareNode);
        }
    }
    
    protected boolean compareSpaces() {
        return true;
    }
    
    public boolean hasMiddleJoint() {
        boolean b = false;
        final EntityObject childByClass = this.getChildByClass(ICDMiddleJoint.class);
        if (childByClass != null && "Yes".equals(((ICDMiddleJoint)childByClass).getAttributeValueAsString("Joint_BoltOn"))) {
            b = true;
        }
        return b;
    }

    public boolean hasBoltOnJointIntersectsExtrusion(final ICDSubInternalExtrusion ext) {
        return hasBoltOnJointIntersect(ext.getBasePoint3f().z, ext.getZDimension());
    }

    public boolean hasBoltOnJointIntersect(final float z, final float h) {
        final EntityObject childByClass = this.getChildByClass(ICDMiddleJoint.class);
        if (childByClass == null || !(((ICDMiddleJoint)childByClass).getAttributeValueAsString("Joint_BoltOn")).equals("Yes")) {
            return false;
        }
        final Point3f basePointWorldSpace = childByClass.getBasePointWorldSpace();
        if (basePointWorldSpace == null) {
            return false;
        }
        return (basePointWorldSpace.z > z) && (basePointWorldSpace.z < z + h);
    }
    
    public boolean hasBoltOnJointOnHeight(final float n) {
        boolean b = false;
        final EntityObject childByClass = this.getChildByClass(ICDMiddleJoint.class);
        if (childByClass != null && "Yes".equals(((ICDMiddleJoint)childByClass).getAttributeValueAsString("Joint_BoltOn"))) {
            final Point3f basePointWorldSpace = childByClass.getBasePointWorldSpace();
            if (basePointWorldSpace != null && MathUtilities.isSameFloat(n, basePointWorldSpace.z, 1.0f)) {
                b = true;
            }
        }
        return b;
    }

    public float getBoltOnJointHeight() {
        final EntityObject childByClass = this.getChildByClass(ICDMiddleJoint.class);
        if (childByClass == null || !(((ICDMiddleJoint)childByClass).getAttributeValueAsString("Joint_BoltOn")).equals("Yes")) {
            return ICDPost.BIG_NEGATIVE;
        }
        final Point3f basePointWorldSpace = childByClass.getBasePointWorldSpace();
        if (basePointWorldSpace == null) {
            return ICDPost.BIG_NEGATIVE;
        }
        final float z = basePointWorldSpace.z;
        return z;
    }
    
    public float getBoltOnJointHeight(final float n) {
        float big_NEGATIVE = ICDPost.BIG_NEGATIVE;
        final EntityObject childByClass = this.getChildByClass(ICDMiddleJoint.class);
        if (childByClass != null && "Yes".equals(((ICDMiddleJoint)childByClass).getAttributeValueAsString("Joint_BoltOn"))) {
            final Point3f basePointWorldSpace = childByClass.getBasePointWorldSpace();
            if (basePointWorldSpace != null) {
                final float z = basePointWorldSpace.z;
                if (MathUtilities.isSameFloat(n, z, 1.0f)) {
                    big_NEGATIVE = z;
                }
            }
        }
        return big_NEGATIVE;
    }
    
    public void draw3D(final Group group, final int n) {
        if (!this.isFakePost()) {
            super.draw3D(group, n);
        }
        else {
            this.drawChildren3D(group, n);
        }
    }
    
    public void draw2D(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        if (!this.isFakePost()) {
            super.draw2D(n, ice2DContainer, solutionSetting);
        }
        else {
            this.drawChildren2D(n, ice2DContainer, solutionSetting);
        }
    }
    
    protected void refreshTagNode(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        final String tags = this.getTags();
        if (tags != null) {
            final Point3f insertPoint = new Point3f(this.getXDimension() / 2.0f, 0.0f, 0.0f);
            if (tags != null) {
                final Matrix4f matrix = (Matrix4f)this.getEntWorldSpaceMatrix().clone();
                final Matrix4f matrix4f = new Matrix4f();
                matrix4f.setIdentity();
                final int tagFontSize = this.getSolution().getTagFontSize();
                matrix4f.setTranslation(new Vector3f(0.0f, 8.0f, 0.0f));
                matrix.mul(matrix4f);
                if (this.tagNode == null) {
                    (this.tagNode = new Ice2DMultipleTextNode("Dimensions", (TransformableEntity)this, matrix, tags, Color.blue, insertPoint)).showWithWhiteBackground(true);
                    this.tagNode.setFont(new Font("Arial", 0, tagFontSize));
                    this.tagNode.setCentered(true);
                }
                else {
                    this.tagNode.setMatrix(matrix);
                    this.tagNode.setText(tags);
                    this.tagNode.setInsertPoint(insertPoint);
                }
                if (this.tagNode.getParent() == null) {
                    ice2DContainer.add((Ice2DNode)this.tagNode);
                }
            }
        }
        else if (this.tagNode != null) {
            this.tagNode.removeFromParent();
            this.tagNode = null;
        }
    }
    
    public void addAdditonalPaintableEntities(final List<AssemblyPaintable> list) {
        list.add(this);
    }
    
    public void calculateNamedScales() {
        super.calculateNamedScales();
        this.getNamedScaleLocal("CADELWIDTH").set(this.getXDimension(), 1.0f, 1.0f);
        Vector3f namedScaleLocal = this.getNamedScaleLocal("ASE_SCALE");
        if (namedScaleLocal == null) {
            namedScaleLocal = new Vector3f();
            this.addNamedScale("ASE_SCALE", namedScaleLocal);
        }
        final GeneralIntersectionInterface parentIntersection = this.getParentIntersection();
        if (parentIntersection != null && parentIntersection.getIntersectionType() == 2 && "ICD_TwoWayCurvedPostType".equals(this.getCurrentType().getId())) {
            float n = 1.0f;
            final Vector<IntersectionArmInterface> armVector = parentIntersection.getArmVector();
            if (armVector != null && armVector.size() == 2) {
                for (int i = 0; i < 2; ++i) {
                    final IntersectionArmInterface intersectionArmInterface = armVector.get(i);
                    if (intersectionArmInterface != null) {
                        final Segment segment = intersectionArmInterface.getSegment();
                        if (segment != null) {
                            final float height = segment.getHeight();
                            if (i == 0) {
                                n = height;
                            }
                            else if (i == 1 && height > n) {
                                n = height;
                            }
                        }
                    }
                }
            }
            namedScaleLocal.set(1.0f, 1.0f, n / 48.0f);
        }
    }
    
    public boolean checkAssembledForAssemblyElevation() {
        return this.getAttributeValueAsBoolean("isAssembled", false);
    }
    
    public boolean isCurvedPost() {
        return "true".equals(this.getAttributeValueAsString("ICD_Is_Curved_Post"));
    }
    
    public boolean isFakePost() {
        return "true".equals(this.getAttributeValueAsString("ICD_Fake_Post"));
    }
    
    public Vector<Ice2DPaintableNode> getAssemblyIcons(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        final Vector<Ice2DPaintableNode> vector = new Vector<Ice2DPaintableNode>();
        ICDPost icdPost = null;
        ICDPost icdPost2 = null;
        for (final ICDPost icdPost3 : this.getChildrenFirstAppearance(ICDPost.class, true)) {
            final LightWeightTypeObject lwTypeCreated = icdPost3.getLwTypeCreatedFrom();
            if (lwTypeCreated != null) {
                if (lwTypeCreated.getId().equals("Curved_Fake_Post_2")) {
                    icdPost = icdPost3;
                }
                else {
                    if (!lwTypeCreated.getId().equals("Curved_Fake_Post_1")) {
                        continue;
                    }
                    icdPost2 = icdPost3;
                }
            }
        }
        if (icdPost != null && icdPost2 != null) {
            final Point3f basePointWorldSpace = icdPost.getBasePointWorldSpace();
            final Point3f basePointWorldSpace2 = icdPost2.getBasePointWorldSpace();
            final Point3f point3f2 = new Point3f((basePointWorldSpace.x + basePointWorldSpace2.x) / 2.0f, (basePointWorldSpace.y + basePointWorldSpace2.y) / 2.0f, (basePointWorldSpace.z + basePointWorldSpace2.z) / 2.0f);
            Point3f point3f3;
            Point3f point3f4;
            if (point3f2.distance(icdPost.getNamedPointWorld("Spot1")) < point3f2.distance(icdPost.getNamedPointWorld("Spot3"))) {
                point3f3 = icdPost.getNamedPointWorld("Spot1");
                point3f4 = icdPost.getNamedPointWorld("Spot3");
            }
            else {
                point3f3 = icdPost.getNamedPointWorld("Spot3");
                point3f4 = icdPost.getNamedPointWorld("Spot1");
            }
            Point3f point3f5;
            Point3f point3f6;
            if (point3f2.distance(icdPost2.getNamedPointWorld("Spot1")) < point3f2.distance(icdPost2.getNamedPointWorld("Spot3"))) {
                point3f5 = icdPost2.getNamedPointWorld("Spot1");
                point3f6 = icdPost2.getNamedPointWorld("Spot3");
            }
            else {
                point3f5 = icdPost2.getNamedPointWorld("Spot3");
                point3f6 = icdPost2.getNamedPointWorld("Spot1");
            }
            final Vector3f vector3f = new Vector3f(1.5707964f, 0.0f, 0.0f);
            float n2 = 0.0f;
            for (final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion : icdPost.getChildrenFirstAppearance(ICDChaseConnectorExtrusion.class, true)) {
                if (!icdChaseConnectorExtrusion.isVertical()) {
                    n2 = icdChaseConnectorExtrusion.getYDimension();
                }
                else {
                    if (!icdChaseConnectorExtrusion.isVertical()) {
                        continue;
                    }
                    final Point3f basePointWorldSpace3 = icdChaseConnectorExtrusion.getBasePointWorldSpace();
                    if (point3f3.distance(basePointWorldSpace3) >= point3f4.distance(basePointWorldSpace3)) {
                        continue;
                    }
                    final Ice2DTextNode assemblyDimensionTextNode = ICDAssemblyElevationUtilities.createAssemblyDimensionTextNode((TransformableEntity)this, new Vector3f(2.0f, 0.0f, 24.0f), Math.round(n2) + "", vector3f);
                    final Ice2DTextNode assemblyDimensionTextNode2 = ICDAssemblyElevationUtilities.createAssemblyDimensionTextNode((TransformableEntity)this, new Vector3f(2.0f, 0.0f, 0.0f), Math.round(n2) + "", vector3f);
                    final Ice2DTextNode assemblyDimensionTextNode3 = ICDAssemblyElevationUtilities.createAssemblyDimensionTextNode((TransformableEntity)this, new Vector3f(6.0f, 0.0f, 12.0f), Math.round(icdChaseConnectorExtrusion.getYDimension()) + "", new Vector3f(1.5707964f, 0.0f, 1.5707964f));
                    vector.add((Ice2DPaintableNode)assemblyDimensionTextNode2);
                    vector.add((Ice2DPaintableNode)assemblyDimensionTextNode);
                    vector.add((Ice2DPaintableNode)assemblyDimensionTextNode3);
                }
            }
            for (final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion2 : icdPost2.getChildrenFirstAppearance(ICDChaseConnectorExtrusion.class, true)) {
                if (!icdChaseConnectorExtrusion2.isVertical()) {
                    n2 = icdChaseConnectorExtrusion2.getYDimension();
                }
                else {
                    if (!icdChaseConnectorExtrusion2.isVertical()) {
                        continue;
                    }
                    final Point3f basePointWorldSpace4 = icdChaseConnectorExtrusion2.getBasePointWorldSpace();
                    if (point3f5.distance(basePointWorldSpace4) >= point3f6.distance(basePointWorldSpace4)) {
                        continue;
                    }
                    final Ice2DTextNode assemblyDimensionTextNode4 = ICDAssemblyElevationUtilities.createAssemblyDimensionTextNode((TransformableEntity)this, new Vector3f(40.0f, 0.0f, 24.0f), Math.round(n2) + "", vector3f);
                    final Ice2DTextNode assemblyDimensionTextNode5 = ICDAssemblyElevationUtilities.createAssemblyDimensionTextNode((TransformableEntity)this, new Vector3f(40.0f, 0.0f, 0.0f), Math.round(n2) + "", vector3f);
                    final Ice2DTextNode assemblyDimensionTextNode6 = ICDAssemblyElevationUtilities.createAssemblyDimensionTextNode((TransformableEntity)this, new Vector3f(36.0f, 0.0f, 12.0f), Math.round(icdChaseConnectorExtrusion2.getYDimension()) + "", new Vector3f(1.5707964f, 0.0f, 1.5707964f));
                    vector.add((Ice2DPaintableNode)assemblyDimensionTextNode5);
                    vector.add((Ice2DPaintableNode)assemblyDimensionTextNode4);
                    vector.add((Ice2DPaintableNode)assemblyDimensionTextNode6);
                }
            }
        }
        final Ice2DTextNode assemblyDimensionTextNode7 = ICDAssemblyElevationUtilities.createAssemblyDimensionTextNode((TransformableEntity)this, new Vector3f(0.0f, 0.0f, 12.0f), Math.round(this.getHeight()) + "", new Vector3f(1.5707964f, 0.0f, 1.5707964f));
        final Ice2DGVTElevationNode assemblyElevationImageNode = ICDAssemblyElevationUtilities.createAssemblyElevationImageNode((TransformableEntity)this, new Vector3f(0.0f, 0.0f, this.getHeight() - (38 - ("true".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Curved_Panel_Split_Indicator")) ? 13 : 0))), new Vector3f());
        vector.add((Ice2DPaintableNode)assemblyDimensionTextNode7);
        vector.add((Ice2DPaintableNode)assemblyElevationImageNode);
        return vector;
    }
    
    public List<IceOutputNode> getPlotOutputNodes(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        return null;
    }
    
    protected void validateIndicators() {
        super.validateIndicators();
        if (this.isCurvedPost()) {
            this.validateCurvedPanelStyle();
        }
    }
    
    private void validateCurvedPanelStyle() {
        ICDPost icdPost = null;
        ICDPost icdPost2 = null;
        for (final ICDPost icdPost3 : this.getChildrenFirstAppearance(ICDPost.class, true)) {
            final LightWeightTypeObject lwTypeCreated = icdPost3.getLwTypeCreatedFrom();
            if (lwTypeCreated != null) {
                if (lwTypeCreated.getId().equals("Curved_Fake_Post_2")) {
                    icdPost = icdPost3;
                }
                else {
                    if (!lwTypeCreated.getId().equals("Curved_Fake_Post_1")) {
                        continue;
                    }
                    icdPost2 = icdPost3;
                }
            }
        }
        boolean b = false;
        boolean b2 = false;
        boolean b3 = false;
        boolean b4 = false;
        if (icdPost != null && icdPost2 != null) {
            final Point3f basePointWorldSpace = icdPost.getBasePointWorldSpace();
            final Point3f basePointWorldSpace2 = icdPost2.getBasePointWorldSpace();
            final Point3f point3f = new Point3f((basePointWorldSpace.x + basePointWorldSpace2.x) / 2.0f, (basePointWorldSpace.y + basePointWorldSpace2.y) / 2.0f, (basePointWorldSpace.z + basePointWorldSpace2.z) / 2.0f);
            Point3f point3f2;
            Point3f point3f3;
            if (point3f.distance(icdPost.getNamedPointWorld("Spot1")) < point3f.distance(icdPost.getNamedPointWorld("Spot3"))) {
                point3f2 = icdPost.getNamedPointWorld("Spot1");
                point3f3 = icdPost.getNamedPointWorld("Spot3");
            }
            else {
                point3f2 = icdPost.getNamedPointWorld("Spot3");
                point3f3 = icdPost.getNamedPointWorld("Spot1");
            }
            Point3f point3f4;
            Point3f point3f5;
            if (point3f.distance(icdPost2.getNamedPointWorld("Spot1")) < point3f.distance(icdPost2.getNamedPointWorld("Spot3"))) {
                point3f4 = icdPost2.getNamedPointWorld("Spot1");
                point3f5 = icdPost2.getNamedPointWorld("Spot3");
            }
            else {
                point3f4 = icdPost2.getNamedPointWorld("Spot3");
                point3f5 = icdPost2.getNamedPointWorld("Spot1");
            }
            for (final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion : icdPost.getChildrenFirstAppearance(ICDChaseConnectorExtrusion.class, true)) {
                if (icdChaseConnectorExtrusion.isVertical()) {
                    final Point3f basePointWorldSpace3 = icdChaseConnectorExtrusion.getBasePointWorldSpace();
                    if (point3f2.distance(basePointWorldSpace3) < point3f3.distance(basePointWorldSpace3)) {
                        b = true;
                    }
                    else {
                        b2 = true;
                    }
                }
            }
            for (final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion2 : icdPost2.getChildrenFirstAppearance(ICDChaseConnectorExtrusion.class, true)) {
                if (icdChaseConnectorExtrusion2.isVertical()) {
                    final Point3f basePointWorldSpace4 = icdChaseConnectorExtrusion2.getBasePointWorldSpace();
                    if (point3f4.distance(basePointWorldSpace4) < point3f5.distance(basePointWorldSpace4)) {
                        b3 = true;
                    }
                    else {
                        b4 = true;
                    }
                }
            }
        }
        boolean equals = false;
        final Iterator<ICDPanel> iterator4 = this.getChildrenFirstAppearance(ICDPanel.class, true).iterator();
        if (iterator4.hasNext()) {
            equals = "Yes".equals(iterator4.next().getAttributeValueAsString("With_Horizontal_InnerExtrusion"));
        }
        final Attribute attributeObject = this.getAttributeObject("ICD_Curved_Panel_LeftChaseA_Indicator");
        if (attributeObject != null && b != attributeObject.getValueAsString().equals("true")) {
            attributeObject.setCurrentValueAsString(b + "");
        }
        final Attribute attributeObject2 = this.getAttributeObject("ICD_Curved_Panel_LeftChaseB_Indicator");
        if (attributeObject2 != null && b2 != attributeObject2.getValueAsString().equals("true")) {
            attributeObject2.setCurrentValueAsString(b2 + "");
        }
        final Attribute attributeObject3 = this.getAttributeObject("ICD_Curved_Panel_RightChaseA_Indicator");
        if (attributeObject3 != null && b3 != attributeObject3.getValueAsString().equals("true")) {
            attributeObject3.setCurrentValueAsString(b3 + "");
        }
        final Attribute attributeObject4 = this.getAttributeObject("ICD_Curved_Panel_RightChaseB_Indicator");
        if (attributeObject4 != null && b4 != attributeObject4.getValueAsString().equals("true")) {
            attributeObject4.setCurrentValueAsString(b4 + "");
        }
        final Attribute attributeObject5 = this.getAttributeObject("ICD_Curved_Panel_Split_Indicator");
        if (attributeObject5 != null && equals != attributeObject5.getValueAsString().equals("true")) {
            attributeObject5.setCurrentValueAsString(equals + "");
        }
    }
    
    public Collection<ICDSubInternalExtrusion> getAllSubInternalTubes() {
        final Vector<ICDSubInternalExtrusion> vector = new Vector<ICDSubInternalExtrusion>();
        final Iterator<EntityObject> children = this.getChildren();
        while (children.hasNext()) {
            final EntityObject entityObject = children.next();
            if (entityObject instanceof ICDSubInternalExtrusion) {
                vector.add((ICDSubInternalExtrusion)entityObject);
            }
        }
        return vector;
    }
    
    protected void validateBoltOnJoints() {
        boolean b = false;
        final Iterator<? extends ICDJoint> iterator = (Iterator<? extends ICDJoint>)this.getChildrenByClass(ICDMiddleJoint.class, false, true).iterator();
        while (iterator.hasNext()) {
            if ("Yes".equals(iterator.next().getAttributeValueAsString("Joint_BoltOn"))) {
                b = true;
                break;
            }
        }
        if (b) {
            final Point3f point3f = new Point3f();
            final ICDSubInternalExtrusion lowestExtrusion = this.getLowestExtrusion();
            if (lowestExtrusion != null) {
                final float zDimension = lowestExtrusion.getZDimension();
                final Point3f basePoint3f = lowestExtrusion.getBasePoint3f();
                lowestExtrusion.destroy();
                final ICDSubInternalExtrusion lowestExtrusion2 = this.getLowestExtrusion();
                if (lowestExtrusion2 != null) {
                    lowestExtrusion2.setZDimension(lowestExtrusion2.getZDimension() + zDimension + 1.0f);
                    lowestExtrusion2.setBasePoint(basePoint3f);
                    lowestExtrusion2.solve();
                }
            }
        }
    }
    
    private ICDSubInternalExtrusion getLowestExtrusion() {
        ICDSubInternalExtrusion icdSubInternalExtrusion = null;
        float z = 999.0f;
        for (final ICDSubInternalExtrusion icdSubInternalExtrusion2 : this.getChildrenByClass(ICDSubInternalExtrusion.class, false, true)) {
            if (icdSubInternalExtrusion2.getBasePointWorldSpace().z < z) {
                z = icdSubInternalExtrusion2.getBasePointWorldSpace().z;
                icdSubInternalExtrusion = icdSubInternalExtrusion2;
            }
        }
        return icdSubInternalExtrusion;
    }
    
    public String getDefaultLayerName() {
        return "Panels";
    }
    
    public boolean shouldDrawAssembly() {
        final String sku = this.getSKU();
        return sku != null && !sku.equals("") && this.isCurvedPost() && !this.isFakePost();
    }
    
    public boolean shouldCreateElevation() {
        final ICDIntersection icdIntersection = (ICDIntersection)this.getParent(ICDIntersection.class);
        return (icdIntersection == null || icdIntersection.getVerticalChase() == null) && super.shouldCreateElevation();
    }
    
    public boolean isAngledPost() {
        final String id = this.getCurrentOption().getId();
        return id != null && (id.indexOf("Angled") >= 0 || id.indexOf("angled") >= 0);
    }
    
    private boolean isSplit() {
        final Iterator<ICDAngledPanel> iterator = this.getChildrenFirstAppearance(ICDAngledPanel.class, true).iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getAttributeValueAsString("With_Horizontal_InnerExtrusion", "No").equals("Yes")) {
                return true;
            }
        }
        return false;
    }
    
    private float getWorksurfaceHeight() {
        for (final ICDAngledPanel icdAngledPanel : this.getChildrenFirstAppearance(ICDAngledPanel.class, true)) {
            if (icdAngledPanel.getAttributeValueAsString("With_Horizontal_InnerExtrusion", "No").equals("Yes")) {
                return icdAngledPanel.getAttributeValueAsFloat("ICD_Height_From_Floor", 0.0f);
            }
        }
        return 0.0f;
    }
    
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        Vector<String> cadElevationScript = (Vector<String>)super.getCadElevationScript(elevationEntity);
        if (!this.isFakePost()) {
            if (cadElevationScript == null) {
                cadElevationScript = new Vector<String>();
            }
            if (this.isAngledPost()) {
                cadElevationScript.addAll(this.getAngledCadElevationScripts(new Point3f(0.0f, 0.0f, 0.0f)));
                if (!this.isAssembled()) {
                    cadElevationScript.addAll(this.getAngledCadElevationScripts(new Point3f(0.0f, 0.5f, 0.0f)));
                    cadElevationScript.add(ICDAssemblyElevationUtilities.getAssemblyLineScript(new Point3f(0.0f, 0.0f, 0.0f), "BL", "TL"));
                    cadElevationScript.add(ICDAssemblyElevationUtilities.getAssemblyLineScript(new Point3f(0.0f, 0.0f, 0.0f), "BR", "TR"));
                }
            }
            if (this.appendElevationInfoToMultiTags(null, 0) != null) {
                cadElevationScript.add("MTG:SS(CAD_TAG:CADELWIDTH)");
            }
        }
        return cadElevationScript;
    }
    
    private Collection<String> getAngledCadElevationScripts(final Point3f point3f) {
        final Vector<String> vector = new Vector<String>();
        vector.add(ICDAssemblyElevationUtilities.getAssemblyLineScript(point3f, "BL", "BM"));
        vector.add(ICDAssemblyElevationUtilities.getAssemblyLineScript(point3f, "BM", "BR"));
        vector.add(ICDAssemblyElevationUtilities.getAssemblyLineScript(point3f, "TL", "TM"));
        vector.add(ICDAssemblyElevationUtilities.getAssemblyLineScript(point3f, "TM", "TR"));
        if (this.isSplit()) {
            vector.add(ICDAssemblyElevationUtilities.getAssemblyLineScript(point3f, "ML", "MM"));
            vector.add(ICDAssemblyElevationUtilities.getAssemblyLineScript(point3f, "MM", "MR"));
        }
        return vector;
    }
    
    public String appendElevationInfoToMultiTags(final String s, final int n) {
        return this.getTags();
    }
    
    public boolean isManufacturerReportable() {
        return this.shouldAssemble();
    }
    
    public boolean isAssembled() {
        return this.getAttributeValueAsBoolean("isAssembled", false);
    }
    
    private Collection<? extends EntitySpaceCompareNodeWrapper> getJointsEntitySpaceCompare() {
        final Vector<ICDJoint> joints = this.getJoints();
        if (joints != null) {
            final LinkedList<EntitySpaceCompareNodeWrapper> list = new LinkedList<EntitySpaceCompareNodeWrapper>();
            final Iterator<ICDJoint> iterator = joints.iterator();
            while (iterator.hasNext()) {
                list.add(EntitySpaceCompareUtility.convertJointWrapper(ICDAssemblyElevationUtilities.appendJointDirections(iterator.next(), null)));
            }
            return list;
        }
        return null;
    }
    
    private Vector<ICDJoint> getJoints() {
        final Vector<ICDJoint> vector = new Vector<ICDJoint>();
        final List<ICDJoint> childrenByClass = this.getChildrenByClass(ICDJoint.class, true, true);
        if (this.needExtraIndirectAssemblyParts()) {
            this.getExtraIndirectJoints(childrenByClass);
        }
        vector.addAll(childrenByClass);
        for (final ICDJoint e : childrenByClass) {
            if (!e.isNonOption()) {
                vector.add(e);
            }
        }
        return vector;
    }
    
    private Collection<? extends EntitySpaceCompareNodeWrapper> getTubesEntitySpaceCompare() {
        final Vector<TransformableEntity> tubes = this.getTubes();
        if (tubes != null) {
            final LinkedList<EntitySpaceCompareNodeWrapper> list = new LinkedList<EntitySpaceCompareNodeWrapper>();
            for (final TransformableEntity transformableEntity : tubes) {
                final ArrayList<Point3f> list2 = new ArrayList<Point3f>();
                final Point3f namedPointLocal = transformableEntity.getNamedPointLocal("Start_Space_Compare");
                final Point3f namedPointLocal2 = transformableEntity.getNamedPointLocal("End_Space_Compare");
                if (namedPointLocal != null) {
                    list2.add(namedPointLocal);
                }
                if (namedPointLocal2 != null) {
                    list2.add(namedPointLocal2);
                }
                int n = 0;
                final Iterator<ICDCornerSlot> iterator2 = transformableEntity.getChildrenByClass(ICDCornerSlot.class, false).iterator();
                while (iterator2.hasNext()) {
                    if (iterator2.next().isSlotted()) {
                        ++n;
                    }
                }
                if (n > 0) {
                    list2.add(new Point3f(0.0f, 1.919192E7f * n, 0.0f));
                }
                list.add(new EntitySpaceCompareNodeWrapper(transformableEntity, (Collection)list2));
            }
            return list;
        }
        return null;
    }
    
    private Vector<TransformableEntity> getTubes() {
        final Vector<TransformableEntity> vector = new Vector<TransformableEntity>();
        final List<BasicExtrusion> childrenByClass = this.getChildrenByClass(BasicExtrusion.class, true, true);
        final List<ICDChaseConnectorExtrusion> childrenByClass2 = this.getChildrenByClass(ICDChaseConnectorExtrusion.class, true, true);
        if (this.needExtraIndirectAssemblyParts()) {
            this.getExtraIndirectTubes(childrenByClass);
        }
        for (final BasicExtrusion e : childrenByClass) {
            if (!e.isFakePart()) {
                vector.add((TransformableEntity)e);
            }
        }
        if (childrenByClass2 != null) {
            vector.addAll((Collection<? extends TransformableEntity>)childrenByClass2);
        }
        return (Vector<TransformableEntity>)vector;
    }
    
    private Collection<? extends EntitySpaceCompareNodeWrapper> getSlotsEntitySpaceCompare() {
        final Vector<ICDCornerSlot> slots = this.getSlots();
        if (slots != null) {
            final LinkedList<EntitySpaceCompareNodeWrapper> list = new LinkedList<EntitySpaceCompareNodeWrapper>();
            for (final ICDCornerSlot icdCornerSlot : slots) {
                final ArrayList<Point3f> list2 = new ArrayList<Point3f>();
                final Point3f namedPointLocal = icdCornerSlot.getNamedPointLocal("Start_Space_Compare");
                final Point3f namedPointLocal2 = icdCornerSlot.getNamedPointLocal("End_Space_Compare");
                if (namedPointLocal != null) {
                    list2.add(namedPointLocal);
                }
                if (namedPointLocal2 != null) {
                    list2.add(namedPointLocal2);
                }
                list.add(new EntitySpaceCompareNodeWrapper((TransformableEntity)icdCornerSlot, (Collection)list2));
            }
            return list;
        }
        return null;
    }
    
    public Vector<ICDCornerSlot> getSlots() {
        final Vector<ICDCornerSlot> vector = new Vector<ICDCornerSlot>();
        for (final ICDCornerSlot e : this.getChildrenByClass(ICDCornerSlot.class, true)) {
            if (e.isSlotted()) {
                vector.add(e);
            }
        }
        return vector;
    }
    
    public Collection<EntitySpaceCompareNodeWrapper> getSpaceCompareNodeWrappers() {
        final LinkedList<EntitySpaceCompareNodeWrapper> list = new LinkedList<EntitySpaceCompareNodeWrapper>();
        list.addAll(this.getJointsEntitySpaceCompare());
        list.addAll(this.getTubesEntitySpaceCompare());
        list.addAll(this.getSlotsEntitySpaceCompare());
        return (Collection<EntitySpaceCompareNodeWrapper>)list;
    }
    
    public void getManufacturingInfo(final TreeMap<String, String> treeMap) {
        final String userTagNameAttribute = this.getUserTagNameAttribute("TagName1");
        if (!"".equals(userTagNameAttribute) && userTagNameAttribute != null) {
            treeMap.put("UserTag", userTagNameAttribute);
        }
    }
    
    public boolean shouldPaintAssemblyInIce2D() {
        return false;
    }
    
    public HashSet<TypeableEntity> getAssembledChildrenForManReport() {
        final HashSet<TypeableEntity> set = new HashSet<TypeableEntity>();
        final HashSet<EntityObject> set2 = new HashSet<EntityObject>();
        set2.addAll(this.getDirectAssemblyParts());
        set2.addAll(this.getIndirectAssemblyParts());
        for (final EntityObject entityObject : set2) {
            if (entityObject instanceof TypeableEntity && !entityObject.equals(this) && entityObject.getAttributeValueAsBoolean("isAssembled", false) && this.checkForJoint(entityObject)) {
                set.add((TypeableEntity)entityObject);
            }
        }
        return set;
    }
    
    private boolean checkForJoint(final EntityObject entityObject) {
        return !(entityObject instanceof ICDJoint) || !((ICDJoint)entityObject).isNonOption();
    }
    
    public void drawCad(final ICadTreeNode cadTreeNode, final int n) {
        if (this.isDirty(n)) {
            if (this.cadTagDelegate == null) {
                this.cadTagDelegate = new ICDCadTagDelegate(this);
            }
            this.cadTagDelegate.drawCad(cadTreeNode, this.getTags());
        }
        super.drawCad(cadTreeNode, n);
    }
    
    public void drawIceCadDotNet(final int n, final IceCadNodeContainer iceCadNodeContainer, final IceCadIceApp iceCadIceApp) {
        if (this.isDirty(n)) {
            if (this.cadTagDelegate == null) {
                this.cadTagDelegate = new ICDCadTagDelegate(this);
            }
            this.cadTagDelegate.drawIceCadDotNet(iceCadNodeContainer, this.getTags());
        }
        super.drawIceCadDotNet(n, iceCadNodeContainer, iceCadIceApp);
    }
    
    public void destroyCad() {
        super.destroyCad();
        if (this.cadTagDelegate != null) {
            this.cadTagDelegate.destroyCad();
        }
    }
    
    public void finalDestroyCad() {
        super.finalDestroyCad();
        if (this.cadTagDelegate != null) {
            this.cadTagDelegate.finalDestroyCad();
            this.cadTagDelegate = null;
        }
    }
    
    protected String getTags() {
        return ICDUtilities.getTags((EntityObject)this);
    }
    
    public boolean shouldAssemble() {
        return this.getAttributeValueAsBoolean("shouldAssemble", false) && !this.shouldILineAssemble();
    }
    
    private boolean shouldILineAssemble() {
        boolean b = false;
        final GeneralIntersectionInterface parentIntersection = this.getParentIntersection();
        if (parentIntersection != null) {
            final Iterator<IntersectionArmInterface> iterator = parentIntersection.getArmVector().iterator();
            while (iterator.hasNext()) {
                b = ((ICDILine)iterator.next().getWallSet()).shouldILineAssemble();
                if (b) {
                    break;
                }
            }
        }
        else {
            b = ((ICDILine)this.getParent(ICDILine.class)).shouldILineAssemble();
        }
        return b;
    }
    
    private boolean needExtraIndirectAssemblyParts() {
        final ICDIntersection icdIntersection = (ICDIntersection)this.getParent(ICDIntersection.class);
        return icdIntersection != null && icdIntersection.needExtraIndirectAssemblyParts(this);
    }
    
    private HashSet<EntityObject> collectExtraIndirectAssemblyParts(final HashSet<EntityObject> set, final boolean b, final Class... array) {
        final ICDIntersection icdIntersection = (ICDIntersection)this.getParent(ICDIntersection.class);
        if (icdIntersection != null) {
            icdIntersection.collectExtraIndirectAssemblyParts(set, b, this.assemblyIncludeExtraTubes, this.assemblyIncludeExtraStepReturnTubes, (Class<EntityObject>[])array);
        }
        return set;
    }
    
    private List<? extends ICDJoint> getExtraIndirectJoints(final List<ICDJoint> list) {
        return this.getExtraIndirectTubesOrJoints(list, ICDJoint.class);
    }
    
    private List<? extends BasicExtrusion> getExtraIndirectTubes(final List<BasicExtrusion> list) {
        return this.getExtraIndirectTubesOrJoints(list, BasicExtrusion.class);
    }
    
    private <E> List<E> getExtraIndirectTubesOrJoints(final List<E> list, final Class<E> clazz) {
        for (final EntityObject entityObject : this.collectExtraIndirectAssemblyParts(new HashSet<EntityObject>(), false, clazz)) {
            if (clazz.isAssignableFrom(entityObject.getClass())) {
                list.add((E)entityObject);
            }
        }
        return list;
    }
    
    public void drawCadElevation(final ElevationEntity elevationEntity, final ICadBlockNode cadBlockNode, final int n, final SolutionSetting solutionSetting) {
        super.drawCadElevation(elevationEntity, cadBlockNode, n, solutionSetting);
        for (final EntityObject entityObject : this.collectExtraIndirectAssemblyParts(new HashSet<EntityObject>(), true, BasicExtrusion.class, ICDJoint.class)) {
            if (entityObject instanceof TransformableEntity) {
                ((TransformableEntity)entityObject).drawCadElevation(elevationEntity, cadBlockNode, n, solutionSetting);
            }
        }
    }
    
    public void drawIceCadElevationDotNet(final ElevationEntity elevationEntity, final IceCadIceApp iceCadIceApp, final IceCadNodeContainer iceCadNodeContainer, final IceCadBlock iceCadBlock, final int n) {
        super.drawIceCadElevationDotNet(elevationEntity, iceCadIceApp, iceCadNodeContainer, iceCadBlock, n);
        if (this.needExtraIndirectAssemblyParts()) {
            for (final EntityObject entityObject : this.collectExtraIndirectAssemblyParts(new HashSet<EntityObject>(), true, BasicExtrusion.class, ICDJoint.class)) {
                if (entityObject instanceof TransformableEntity) {
                    ((TransformableEntity)entityObject).drawIceCadElevationDotNet(elevationEntity, iceCadIceApp, iceCadNodeContainer, iceCadBlock, n);
                }
            }
        }
    }
    
    private void addAssemblyPartsForCurvedOrAngledPanel(final HashSet<EntityObject> set) {
        for (final EntityObject e : this.getChildrenByClass(EntityObject.class, true, true)) {
            if (e.containsAttributeKey("isAssembled")) {
                if (e instanceof ICDInstallTagDrawable) {
                    e.modifyAttributeValue("isAssembled", "false");
                }
                else {
                    if (e instanceof BasicExtrusion && ((BasicExtrusion)e).isFakePart()) {
                        continue;
                    }
                    if (e instanceof ICDPost) {
                        continue;
                    }
                    if (e instanceof ICDMiddleJoint && ((ICDMiddleJoint)e).isBoltOnJoint()) {
                        this.addChildrenOfBoltOnJoint(e, set);
                    }
                    else {
                        set.add(e);
                    }
                }
            }
        }
    }
    
    public boolean isAssemblyIncludeExtraTubes() {
        return this.assemblyIncludeExtraTubes;
    }
    
    public void setAssemblyIncludeExtraTubes(final boolean assemblyIncludeExtraTubes) {
        this.assemblyIncludeExtraTubes = assemblyIncludeExtraTubes;
    }
    
    public boolean isAssemblyIncludeExtraStepReturnTubes() {
        return this.assemblyIncludeExtraStepReturnTubes;
    }
    
    public void setAssemblyIncludeExtraStepReturnTubes(final boolean assemblyIncludeExtraStepReturnTubes) {
        this.assemblyIncludeExtraStepReturnTubes = assemblyIncludeExtraStepReturnTubes;
    }
    
    public void addOnTheFlyAttribute() {
        super.addOnTheFlyAttribute();
        final ICDIntersection icdIntersection = (ICDIntersection)this.getParent(ICDIntersection.class);
        if (icdIntersection != null) {
            if (icdIntersection.allowUserControlPostAssembly()) {
                this.createNewAttribute("ICD_IncludeExtraChaseTubes", this.assemblyIncludeExtraTubes ? "Yes" : "No", true, false);
            }
            if (icdIntersection.allowUserControlStackPanelAssembly()) {
                this.createNewAttribute("ICD_IncludeStepReturnTubes", this.assemblyIncludeExtraStepReturnTubes ? "Yes" : "No", true, false);
            }
        }
    }
    
    public void removeOnTheFlyAttribute() {
        super.removeOnTheFlyAttribute();
        this.removeAttribute("ICD_IncludeExtraChaseTubes");
        this.removeAttribute("ICD_IncludeStepReturnTubes");
    }
    
    public void setLocalVariables(final String anObject) {
        super.setLocalVariables(anObject);
        if ("ICD_IncludeExtraChaseTubes".equals(anObject)) {
            final Attribute attributeObject = this.getAttributeObject("ICD_IncludeExtraChaseTubes");
            if (attributeObject != null) {
                this.assemblyIncludeExtraTubes = "Yes".equals(attributeObject.getValueAsString());
            }
        }
        else if ("ICD_IncludeStepReturnTubes".equals(anObject)) {
            final Attribute attributeObject2 = this.getAttributeObject("ICD_IncludeStepReturnTubes");
            if (attributeObject2 != null) {
                this.assemblyIncludeExtraStepReturnTubes = "Yes".equals(attributeObject2.getValueAsString());
            }
        }
    }
    
    public void getOnTheFlyAttributes(final Collection<Attribute> collection) {
        super.getOnTheFlyAttributes((Collection)collection);
        final Attribute attributeObject = this.getAttributeObject("ICD_IncludeExtraChaseTubes");
        if (attributeObject != null) {
            collection.add(attributeObject);
        }
        final Attribute attributeObject2 = this.getAttributeObject("ICD_IncludeStepReturnTubes");
        if (attributeObject2 != null) {
            collection.add(attributeObject2);
        }
    }
    
    private void deleteExtraTubeFromCad() {
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
        return "Assembled partition frame";
    }
    
    static {
        ICDPost.logger = Logger.getLogger(ICDPost.class);
        ICDPost.BIG_NEGATIVE = -10001.0f;
        ICDPost.MAX_ALLOWED_OVERHEAD_NUMBER = 3;
    }
}
