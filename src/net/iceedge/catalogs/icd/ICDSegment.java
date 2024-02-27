package net.iceedge.catalogs.icd;

import net.iceedge.icecore.basemodule.interfaces.SubILineBaseInterface;
import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import net.dirtt.icelib.main.attributes.BooleanAttribute;
import net.iceedge.icecore.basemodule.interfaces.panels.FrameInterface;
import net.iceedge.catalogs.icd.panel.ICDSubInternalExtrusion;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelInterface;
import net.iceedge.catalogs.icd.panel.ICDValetHandle;
import net.iceedge.catalogs.icd.panel.ICDMagneticCatch;
import net.iceedge.catalogs.icd.panel.ICDLock;
import net.iceedge.catalogs.icd.panel.ICDDoorstop;
import net.iceedge.catalogs.icd.panel.ICDHinge;
import java.util.TreeMap;
import net.dirtt.icelib.report.compare.CompareNode;
import net.iceedge.catalogs.icd.panel.ICDTile;
import net.iceedge.icecore.basemodule.finalclasses.GeneralSnapSet;
import net.dirtt.utilities.MathUtilities;
import net.iceedge.icecore.basemodule.interfaces.IntersectionArmInterface;
import net.iceedge.catalogs.icd.intersection.ICDIntersection;
import net.iceedge.catalogs.icd.intersection.ICDIntersectionFactory;
import net.iceedge.icecore.basemodule.interfaces.IntersectionFactoryInterface;
import net.iceedge.icecore.icecad.ice.tree.IceCadCompositeBlock;
import net.dirtt.icecad.cadtree.ICadTreeNode;
import net.iceedge.icecore.icecad.ice.tree.IceCadBlock;
import net.iceedge.icecore.icecad.ice.tree.IceCadNodeContainer;
import net.iceedge.icecore.icecad.ice.IceCadIceApp;
import net.dirtt.icecad.cadtree.ICadBlockNode;
import net.dirtt.icelib.main.ElevationEntity;
import net.dirtt.utilities.EnumerationIterator;
import net.iceedge.icecore.basemodule.interfaces.SubILineInterface;
import net.dirtt.utilities.NoDuplicateVector;
import net.iceedge.catalogs.icd.panel.JointIntersectable;
import net.iceedge.catalogs.icd.panel.ICDVerticalChase;
import net.dirtt.icebox.canvas2d.Ice2DNode;
import java.awt.Font;
import net.dirtt.icebox.canvas2d.Ice2DMultipleTextNode;
import java.awt.Color;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintable;
import net.iceedge.catalogs.icd.panel.ICDChaseConnectorExtrusion;
import com.iceedge.icd.entities.ICDTypeValidatorEntity;
import net.iceedge.catalogs.icd.panel.ICDMiddleJoint;
import net.iceedge.catalogs.icd.panel.ICDTabContainer;
import net.iceedge.catalogs.icd.intersection.ICDCornerSlot;
import net.iceedge.catalogs.icd.panel.ICDTab;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicExtrusion;
import net.dirtt.icelib.main.TypeableEntity;
import com.iceedge.icd.utilities.EntitySpaceCompareUtility;
import net.dirtt.icelib.main.TransformableEntity;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationUtilities;
import net.iceedge.catalogs.icd.panel.ICDJoint;
import java.util.LinkedList;
import net.dirtt.utilities.EntitySpaceCompareNodeWrapper;
import net.iceedge.catalogs.icd.panel.ICDPanel;
import icd.warnings.WarningReason0267;
import net.iceedge.catalogs.icd.intersection.ICDPost;
import net.iceedge.catalogs.icd.panel.ICDPanelToPanelConnectionHW;
import java.util.Collection;
import net.iceedge.catalogs.icd.interfaces.ICDInstallTagDrawable;
import java.util.HashSet;
import java.util.Vector;
import net.iceedge.icecore.basemodule.interfaces.ILineInterface;
import net.iceedge.catalogs.icd.panel.ICDSubFrameSideContainer;
import net.iceedge.icecore.basemodule.baseclasses.BasicILine;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import net.iceedge.icecore.basemodule.interfaces.GeneralIntersectionInterface;
import net.dirtt.icelib.main.attributes.Attribute;
import java.util.Iterator;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.EntityObject;
import net.iceedge.icecore.basemodule.baseclasses.BasicParametricWorksurface;
import net.dirtt.icelib.main.snapping.simple.SimpleSnapTargetCollection;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icebox.canvas2d.Ice2DTextNode;
import javax.vecmath.Point3f;
import net.dirtt.utilities.Pair;
import java.util.List;
import net.iceedge.catalogs.icd.icecad.ICDCadTagDelegate;
import org.apache.log4j.Logger;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.catalogs.icd.icecad.ICDCadTagPaintable;
import net.iceedge.icebox.utilities.MultiTagAppender;
import net.iceedge.icecore.basemodule.interfaces.AssembleParent;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintableRoot;
import net.iceedge.icecore.basemodule.baseclasses.BasicSegment;

public class ICDSegment extends BasicSegment implements AssemblyPaintableRoot, AssembleParent, MultiTagAppender, ICDCadTagPaintable, ICDManufacturingReportable
{
    private static final long serialVersionUID = 1353267409820566251L;
    private static Logger logger;
    public static final String MANUFACTURING_REPORT_DESCRIPTION = "Assembled partition frame";
    private boolean inOffModuleIntersection;
    private ICDCadTagDelegate cadTagDelegate;
    private List<Pair<Point3f, Integer>> jointLocationForBeam;
    private Pair<Float, Float> extraHorizontalExtrusionSearchScope;
    protected Ice2DTextNode elevationReportTag;
    
    public ICDSegment(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.inOffModuleIntersection = false;
        this.jointLocationForBeam = new ArrayList<Pair<Point3f, Integer>>();
        this.extraHorizontalExtrusionSearchScope = (Pair<Float, Float>)new Pair((Object)0.0f, (Object)0.0f);
    }
    
    public Object clone() {
        return this.buildClone(new ICDSegment(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDSegment buildClone(final ICDSegment icdSegment) {
        super.buildClone((BasicSegment)icdSegment);
        return icdSegment;
    }
    
    public void setupNamedPoints() {
        super.setupNamedPoints();
        this.addNamedPoint("BLC", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("BLL", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("BLR", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("BRC", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("BRR", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("BRL", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("TLC", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("TLL", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("TLR", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("TRC", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("TRR", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("TRL", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("CAD_TAG", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedScale("CADELWIDTH", new Vector3f(1.0f, 1.0f, 1.0f));
    }
    
    protected void calculateNamedScales() {
        super.calculateNamedScales();
        this.getNamedScaleLocal("CADELWIDTH").set(this.getXDimension(), 1.0f, 1.0f);
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("BLC").set(0.0f, -this.getYDimension() / 2.0f, 0.0f);
        this.getNamedPointLocal("BLL").set(-0.5f, -this.getYDimension() / 2.0f, 0.0f);
        this.getNamedPointLocal("BLR").set(0.5f, -this.getYDimension() / 2.0f, 0.0f);
        this.getNamedPointLocal("BRC").set(this.getXDimension(), -this.getYDimension() / 2.0f, 0.0f);
        this.getNamedPointLocal("BRR").set(this.getXDimension() + 0.5f, -this.getYDimension() / 2.0f, 0.0f);
        this.getNamedPointLocal("BRL").set(this.getXDimension() - 0.5f, -this.getYDimension() / 2.0f, 0.0f);
        this.getNamedPointLocal("TLC").set(0.0f, this.getYDimension() / 2.0f, 0.0f);
        this.getNamedPointLocal("TLL").set(-0.5f, this.getYDimension() / 2.0f, 0.0f);
        this.getNamedPointLocal("TLR").set(0.5f, this.getYDimension() / 2.0f, 0.0f);
        this.getNamedPointLocal("TRC").set(this.getXDimension(), this.getYDimension() / 2.0f, 0.0f);
        this.getNamedPointLocal("TRR").set(this.getXDimension() + 0.5f, this.getYDimension() / 2.0f, 0.0f);
        this.getNamedPointLocal("TRL").set(this.getXDimension() - 0.5f, this.getYDimension() / 2.0f, 0.0f);
        this.getNamedPointLocal("CAD_TAG").set(this.getXDimension() / 2.0f, this.getYDimension() / 2.0f, this.getZDimension() + 5.0f);
    }
    
    protected void addSimpleSnapTargets(final SimpleSnapTargetCollection collection) {
        super.addSimpleSnapTargets(collection);
        collection.addWorldSpaceSnapPoint("BLC", this.getNamedPointWorld("BLC"), Float.valueOf(this.getRotationWorldSpace()));
        collection.addWorldSpaceSnapPoint("BLL", this.getNamedPointWorld("BLL"), Float.valueOf(this.getRotationWorldSpace()));
        collection.addWorldSpaceSnapPoint("BLR", this.getNamedPointWorld("BLR"), Float.valueOf(this.getRotationWorldSpace()));
        collection.addWorldSpaceSnapPoint("BRC", this.getNamedPointWorld("BRC"), Float.valueOf(this.getRotationWorldSpace()));
        collection.addWorldSpaceSnapPoint("BRR", this.getNamedPointWorld("BRR"), Float.valueOf(this.getRotationWorldSpace()));
        collection.addWorldSpaceSnapPoint("BRL", this.getNamedPointWorld("BRL"), Float.valueOf(this.getRotationWorldSpace()));
        collection.addWorldSpaceSnapPoint("TLC", this.getNamedPointWorld("TLC"), Float.valueOf(this.getRotationWorldSpace()));
        collection.addWorldSpaceSnapPoint("TLL", this.getNamedPointWorld("TLL"), Float.valueOf(this.getRotationWorldSpace()));
        collection.addWorldSpaceSnapPoint("TLR", this.getNamedPointWorld("TLR"), Float.valueOf(this.getRotationWorldSpace()));
        collection.addWorldSpaceSnapPoint("TRC", this.getNamedPointWorld("TRC"), Float.valueOf(this.getRotationWorldSpace()));
        collection.addWorldSpaceSnapPoint("TRR", this.getNamedPointWorld("TRR"), Float.valueOf(this.getRotationWorldSpace()));
        collection.addWorldSpaceSnapPoint("TRL", this.getNamedPointWorld("TRL"), Float.valueOf(this.getRotationWorldSpace()));
    }
    
    public boolean showDimensions() {
        return false;
    }
    
    public boolean isHeightGripEnabled() {
        return true;
    }
    
    public String getSegmentHeighKeytAttributeName() {
        if (this.isUnderChase()) {
            return "ICD_Under_Chase_Segment_Height";
        }
        if (this.isVerticalChase()) {
            return "ICD_Vertical_Chase_Segment_Height";
        }
        return "ICD_Segment_Height";
    }
    
    private boolean isVerticalChase() {
        final ICDILine icdiLine = (ICDILine)this.getParent((Class)ICDILine.class);
        return icdiLine != null && icdiLine.isVerticalChase();
    }
    
    public boolean hasWorksurfaceOnBothSide() {
        final Solution solution = this.getSolution();
        int n = 0;
        final Iterator iterator = solution.getEntityInSolutionByClass((Class)BasicParametricWorksurface.class).iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getRealBounds(0.5f).intersects(this.getRealBounds(0.5f))) {
                ++n;
            }
        }
        return n >= 2;
    }
    
    public boolean isLengthHardcoded() {
        if (super.isLengthHardcoded()) {
            final Attribute desiredSegmentLengthAttribute = this.getDesiredSegmentLengthAttribute();
            if (desiredSegmentLengthAttribute != null && desiredSegmentLengthAttribute.isEditedByUser()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean likeToSystemHardCodeLength() {
        return false;
    }
    
    protected void handleHardcodingAndModularityForValidSegmentLength(final float n) {
    }
    
    public boolean isUnderChase() {
        return "true".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Is_Under_Chase"));
    }
    
    public void flip() {
        final ILineInterface myParentILine = this.getMyParentILine();
        if (myParentILine != null) {
            final Vector breakingIntersections = myParentILine.getBreakingIntersections();
            for (int i = 0; i < breakingIntersections.size(); ++i) {
                for (final Segment segment : breakingIntersections.get(i).getSegmentsFromArms()) {
                    if (!segment.equals(this)) {
                        final Iterator iterator2 = ((BasicILine)segment.getMyParentILine()).getChildrenByClass((Class)ICDSubFrameSideContainer.class, true, true).iterator();
                        while (iterator2.hasNext()) {
                            iterator2.next().removeAllBreaks();
                        }
                    }
                }
            }
        }
        super.flip();
    }
    
    public HashSet<EntityObject> getDirectAssemblyParts() {
        final HashSet<EntityObject> set = new HashSet<EntityObject>();
        final Iterator breadthFirstEnumerationIterator = this.getBreadthFirstEnumerationIterator();
        while (breadthFirstEnumerationIterator.hasNext()) {
            final EntityObject e = breadthFirstEnumerationIterator.next();
            if (e.containsAttributeKey("isAssembled") && e != this && e.getParent((Class)ICDSubFrameSideContainer.class) == null) {
                if (e instanceof ICDInstallTagDrawable) {
                    e.modifyAttributeValue("isAssembled", "false");
                }
                else {
                    set.add(e);
                }
            }
        }
        set.add((EntityObject)this);
        return set;
    }
    
    public HashSet<EntityObject> getIndirectAssemblyParts() {
        return new HashSet<EntityObject>();
    }
    
    public boolean shouldIncludeExtraIndirectAssemblyParts() {
        return false;
    }
    
    public HashSet<AssembleParent> getExternalAssemblyParts() {
        final HashSet<Object> set = (HashSet<Object>)new HashSet<AssembleParent>();
        set.addAll(this.getAssembledChildrenOnIntersection());
        set.addAll(this.getAssembledChildrenOnSubILine());
        return (HashSet<AssembleParent>)set;
    }
    
    private Vector<AssembleParent> getAssembledChildrenOnSubILine() {
        final Vector<AssembleParent> vector = new Vector<AssembleParent>();
        vector.addAll(this.getAssembledChildrenOnSubILine(true));
        vector.addAll(this.getAssembledChildrenOnSubILine(false));
        return vector;
    }
    
    private Vector<AssembleParent> getAssembledChildrenOnSubILine(final boolean b) {
        final Vector<AssembleParent> vector = new Vector<AssembleParent>();
        final ICDSubILine icdSubILine = (ICDSubILine)this.getParent((Class)ICDSubILine.class);
        if (icdSubILine != null) {
            final Iterator iterator = icdSubILine.getChildrenByClass((Class)ICDPanelToPanelConnectionHW.class, false).iterator();
            while (iterator.hasNext()) {
                final ICDPost e = (ICDPost)iterator.next().getFirstChildByClass((Class)ICDPost.class, false);
                final Point3f point3f = new Point3f(0.0f, 0.0f, 0.0f);
                final Point3f point3f2 = new Point3f(this.getXDimension(), 0.0f, 0.0f);
                final Point3f convertPointToWorldSpace = this.convertPointToWorldSpace(point3f);
                final Point3f convertPointToWorldSpace2 = this.convertPointToWorldSpace(point3f2);
                if (e != null && !vector.contains(e)) {
                    if (b && e.getBasePointWorldSpace().distance(convertPointToWorldSpace) < 0.1f) {
                        vector.add((AssembleParent)e);
                    }
                    else {
                        if (e.getBasePointWorldSpace().distance(convertPointToWorldSpace2) >= 0.1f) {
                            continue;
                        }
                        vector.add((AssembleParent)e);
                    }
                }
            }
        }
        return vector;
    }
    
    private Vector<AssembleParent> getAssembledChildrenOnIntersection() {
        final Vector<AssembleParent> vector = new Vector<AssembleParent>();
        vector.addAll(this.getAssembledChildrenOnIntersection(true));
        vector.addAll(this.getAssembledChildrenOnIntersection(false));
        return vector;
    }
    
    private Vector<AssembleParent> getAssembledChildrenOnIntersection(final boolean b) {
        final Vector<ICDPost> vector = (Vector<ICDPost>)new Vector<AssembleParent>();
        final GeneralIntersectionInterface intersectionForSegment = this.getIntersectionForSegment(b);
        if (intersectionForSegment != null) {
            final ICDPost icdPost = (ICDPost)intersectionForSegment.getFirstChildByClass((Class)ICDPost.class, false);
            if (icdPost != null && !vector.contains(icdPost)) {
                vector.add((AssembleParent)icdPost);
            }
        }
        return (Vector<AssembleParent>)vector;
    }
    
    public void handleWarnings() {
        super.handleWarnings();
        if (this.shouldAssemble()) {
            WarningReason0267.addRequiredWarning(this);
        }
    }
    
    public boolean shouldICDMakePreAssembledReport() {
        return this.shouldAssemble();
    }
    
    public ICDPanel getICDPanel() {
        final List childrenByClass = this.getChildrenByClass((Class)ICDPanel.class, true);
        if (childrenByClass != null && childrenByClass.size() > 0) {
            return childrenByClass.get(0);
        }
        return null;
    }
    
    private Collection<EntitySpaceCompareNodeWrapper> getJointsEntitySpaceCompare() {
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
    
    private Collection<EntitySpaceCompareNodeWrapper> getTubesEntitySpaceCompare(final boolean b) {
        final Vector<TypeableEntity> vector = new Vector<TypeableEntity>();
        this.getExtrusionForPreAssembleReport(vector, (TypeableEntity)this, b);
        final Iterator<AssembleParent> iterator = this.getExternalAssemblyParts().iterator();
        while (iterator.hasNext()) {
            this.getExtrusionForPreAssembleReport(vector, (TypeableEntity)iterator.next(), b);
        }
        final ArrayList<EntitySpaceCompareNodeWrapper> list = new ArrayList<EntitySpaceCompareNodeWrapper>();
        for (int i = 0; i < vector.size(); ++i) {
            final TypeableEntity typeableEntity = vector.get(i);
            if (typeableEntity instanceof BasicExtrusion) {
                final BasicExtrusion basicExtrusion = (BasicExtrusion)typeableEntity;
                if (basicExtrusion != null) {
                    final ArrayList<Point3f> list2 = new ArrayList<Point3f>();
                    final Point3f namedPointLocal = basicExtrusion.getNamedPointLocal("extStartPoint");
                    final Point3f namedPointLocal2 = basicExtrusion.getNamedPointLocal("extEndPoint");
                    if (namedPointLocal != null) {
                        list2.add(namedPointLocal);
                    }
                    if (namedPointLocal2 != null) {
                        list2.add(namedPointLocal2);
                    }
                    list.add(new EntitySpaceCompareNodeWrapper((TransformableEntity)basicExtrusion, (Collection)list2));
                }
            }
        }
        return list;
    }
    
    private Collection<? extends EntitySpaceCompareNodeWrapper> getTabsEntitySpaceCompare() {
        final Vector<ICDTab> tabs = this.getTabs();
        if (tabs != null) {
            final LinkedList<EntitySpaceCompareNodeWrapper> list = new LinkedList<EntitySpaceCompareNodeWrapper>();
            for (final ICDTab icdTab : tabs) {
                final ArrayList<Point3f> list2 = new ArrayList<Point3f>();
                final Point3f namedPointLocal = icdTab.getNamedPointLocal("Start_Space_Compare");
                final Point3f namedPointLocal2 = icdTab.getNamedPointLocal("End_Space_Compare");
                if (namedPointLocal != null) {
                    list2.add(namedPointLocal);
                }
                if (namedPointLocal2 != null) {
                    list2.add(namedPointLocal2);
                }
                list.add(new EntitySpaceCompareNodeWrapper((TransformableEntity)icdTab, (Collection)list2));
            }
            return list;
        }
        return null;
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
    
    private Vector<ICDTab> getTabs() {
        final Vector<ICDTab> vector = new Vector<ICDTab>();
        final List childrenByClass = this.getChildrenByClass((Class)ICDTabContainer.class, true);
        for (int i = 0; i < childrenByClass.size(); ++i) {
            final ICDTabContainer icdTabContainer = childrenByClass.get(i);
            for (int j = 0; j < icdTabContainer.getChildCount(); ++j) {
                final ICDTab e = (ICDTab)icdTabContainer.getChildAt(j);
                if (e.isTabbed() && !e.isOnChase()) {
                    vector.add(e);
                }
            }
        }
        return vector;
    }
    
    private Vector<ICDJoint> getJoints() {
        final Vector<ICDJoint> vector = new Vector<ICDJoint>();
        final Vector<AssembleParent> vector2 = new Vector<AssembleParent>();
        vector2.addAll(this.getAssembledChildrenOnSubILine(true));
        vector2.addAll(this.getAssembledChildrenOnIntersection(true));
        vector2.addAll(this.getAssembledChildrenOnSubILine(false));
        vector2.addAll(this.getAssembledChildrenOnIntersection(false));
        final Iterator<AssembleParent> iterator = vector2.iterator();
        while (iterator.hasNext()) {
            for (final EntityObject entityObject : iterator.next().getDirectAssemblyParts()) {
                if (entityObject instanceof ICDJoint) {
                    final ICDJoint icdJoint = (ICDJoint)entityObject;
                    if (icdJoint.isNonOption() || vector.contains(icdJoint)) {
                        continue;
                    }
                    vector.add(icdJoint);
                }
            }
        }
        vector.addAll(this.getJointsOnSegment());
        return vector;
    }
    
    private Vector<TypeableEntity> getJointsAndChildrenForManufacturingReport() {
        final Vector<ICDMiddleJoint> vector = (Vector<ICDMiddleJoint>)new Vector<TypeableEntity>();
        for (final ICDJoint e : this.getJoints()) {
            if (e instanceof ICDMiddleJoint && ((ICDMiddleJoint)e).isBoltOnJoint()) {
                for (final ICDTypeValidatorEntity e2 : e.getChildrenByClass((Class)ICDTypeValidatorEntity.class, true, true)) {
                    if (e2.containsAttributeKey("isAssembled")) {
                        vector.add((TypeableEntity)e2);
                    }
                }
            }
            else {
                vector.add((TypeableEntity)e);
            }
        }
        return (Vector<TypeableEntity>)vector;
    }
    
    private Vector<ICDJoint> getJointsOnSegment() {
        final Vector<ICDJoint> vector = new Vector<ICDJoint>();
        for (final ICDJoint e : this.getChildrenByClass((Class)ICDJoint.class, true, true)) {
            if (!e.isNonOption()) {
                vector.add(e);
            }
        }
        return vector;
    }
    
    public Vector<ICDCornerSlot> getSlots() {
        final Vector<ICDCornerSlot> vector = new Vector<ICDCornerSlot>();
        final Vector<AssembleParent> vector2 = new Vector<AssembleParent>();
        vector2.addAll(this.getAssembledChildrenOnSubILine(true));
        vector2.addAll(this.getAssembledChildrenOnIntersection(true));
        vector2.addAll(this.getAssembledChildrenOnSubILine(false));
        vector2.addAll(this.getAssembledChildrenOnIntersection(false));
        final Iterator<AssembleParent> iterator = vector2.iterator();
        while (iterator.hasNext()) {
            for (final EntityObject entityObject : iterator.next().getDirectAssemblyParts()) {
                if (entityObject instanceof ICDCornerSlot) {
                    final ICDCornerSlot e = (ICDCornerSlot)entityObject;
                    if (!e.isSlotted()) {
                        continue;
                    }
                    vector.add(e);
                }
            }
        }
        return vector;
    }
    
    private Vector<TypeableEntity> getExtrusionForPreAssembleReport(final Vector<TypeableEntity> vector, final TypeableEntity typeableEntity, final boolean b) {
        for (int i = 0; i < typeableEntity.getChildCount(); ++i) {
            final TypeableEntity e = (TypeableEntity)typeableEntity.getChildAt(i);
            if (e instanceof BasicExtrusion && !((BasicExtrusion)e).isFakePart()) {
                if (b) {
                    if (!((BasicExtrusion)e).isStickExtrusion()) {
                        vector.add(e);
                    }
                }
                else if (((BasicExtrusion)e).isStickExtrusion()) {
                    vector.add(e);
                }
            }
            if (e instanceof ICDChaseConnectorExtrusion) {
                vector.add(e);
            }
            this.getExtrusionForPreAssembleReport(vector, e, b);
        }
        return vector;
    }
    
    public void addAdditonalPaintableEntities(final List<AssemblyPaintable> list) {
        final Iterator<ICDPanel> iterator = this.getChildrenByClass((Class)ICDPanel.class, true, true).iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isDoorPanel()) {
                return;
            }
        }
        final ICDSubILine icdSubILine = (ICDSubILine)this.getParent((Class)ICDSubILine.class);
        if (icdSubILine != null) {
            final List childrenByClass = icdSubILine.getChildrenByClass((Class)ICDPost.class, true, true);
            final ICDILine icdiLine = (ICDILine)this.getParent((Class)ICDILine.class);
            if (icdiLine != null) {
                final Iterator iterator2 = icdiLine.getIntersections().iterator();
                while (iterator2.hasNext()) {
                    childrenByClass.addAll(iterator2.next().getChildrenByClass((Class)ICDPost.class, true, true));
                }
            }
            final Point3f point3f = new Point3f(0.0f, 0.0f, 0.0f);
            final Point3f point3f2 = new Point3f(this.getXDimension(), 0.0f, 0.0f);
            final Point3f convertPointToWorldSpace = this.convertPointToWorldSpace(point3f);
            final Point3f convertPointToWorldSpace2 = this.convertPointToWorldSpace(point3f2);
            for (final ICDPost icdPost : childrenByClass) {
                if (icdPost.getBasePointWorldSpace().distance(convertPointToWorldSpace) < 0.1f || icdPost.getBasePointWorldSpace().distance(convertPointToWorldSpace2) < 0.1f) {
                    for (final AssemblyPaintable assemblyPaintable : icdPost.getChildrenByClass((Class)AssemblyPaintable.class, true, true)) {
                        if (!list.contains(assemblyPaintable)) {
                            list.add(assemblyPaintable);
                        }
                    }
                }
            }
        }
    }
    
    protected String getTags() {
        return ICDUtilities.getTags((EntityObject)this);
    }
    
    public void draw2DElevation(final int n, final Ice2DContainer ice2DContainer, final boolean b, final SolutionSetting solutionSetting) {
        super.draw2DElevation(n, ice2DContainer, b, solutionSetting);
        final Solution solution = this.getSolution();
        if (solution != null && solution.isMainSolution()) {
            this.buildElevationPaintableReportTag(n, ice2DContainer, solutionSetting);
        }
    }
    
    protected void buildElevationPaintableReportTag(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        if (!solutionSetting.isShowICDPreassembledTag() || !this.shouldICDMakePreAssembledReport()) {
            return;
        }
        final Point3f point3f = new Point3f(this.getLength() / 2.0f, 0.0f, 0.0f);
        if (point3f != null) {
            final String tags = this.getTags();
            if (tags != null) {
                final Point3f point3f2 = new Point3f(point3f.x, this.getHeight(), point3f.z);
                final Matrix4f matrix4f = (Matrix4f)this.getEntWorldSpaceMatrix().clone();
                matrix4f.getRotationScale(new Matrix3f());
                final Matrix4f matrix4f2 = new Matrix4f();
                matrix4f2.setIdentity();
                matrix4f2.rotX(1.5707964f);
                matrix4f.mul(matrix4f2);
                final int tagFontSize = this.getSolution().getTagFontSize();
                if (ice2DContainer.getSide() == 1) {
                    final Matrix4f matrix4f3 = new Matrix4f();
                    matrix4f3.setIdentity();
                    matrix4f3.rotY((float)Math.toRadians(180.0));
                    final Matrix4f matrix4f4 = new Matrix4f();
                    matrix4f4.setIdentity();
                    matrix4f4.setTranslation(new Vector3f(-this.getLength() + tags.length() / 2, 0.0f, 0.0f));
                    matrix4f.mul(matrix4f3);
                    matrix4f.mul(matrix4f4);
                }
                final int length = tags.split("\n").length;
                (this.elevationReportTag = (Ice2DTextNode)new Ice2DMultipleTextNode("Dimensions", (TransformableEntity)this, matrix4f, tags, Color.blue, new Point3f(this.getXDimension() / 2.0f, this.getHeight(), 0.0f))).showWithWhiteBackground(true);
                this.elevationReportTag.setFont(new Font("Arial", 0, tagFontSize));
                this.elevationReportTag.setCentered(true);
                if (this.elevationReportTag.getParent() == null) {
                    ice2DContainer.add((Ice2DNode)this.elevationReportTag);
                }
            }
        }
    }
    
    public void setSelected(final boolean b, final Solution solution) {
        super.setSelected(b, solution);
        if (this.getParent((Class)ICDILine.class) != null) {
            final ICDVerticalChase verticalChase = ((ICDILine)this.getParent((Class)ICDILine.class)).getVerticalChase();
            if (verticalChase != null) {
                verticalChase.setVerticalChaseSegmentsSelected(b, solution);
            }
        }
    }
    
    public boolean checkAssembledForAssemblyElevation() {
        return true;
    }
    
    public Collection<JointIntersectable> getAllIntersectables() {
        final Vector<Object> vector = (Vector<Object>)new Vector<JointIntersectable>();
        vector.addAll(this.getOwnIntersectables());
        final SubILineInterface myParentSubILine = this.getMyParentSubILine();
        final NoDuplicateVector noDuplicateVector = new NoDuplicateVector();
        if (myParentSubILine != null) {
            if (this.isStartSegment()) {
                ((Vector<Segment>)noDuplicateVector).addAll(this.getSegmentsFromIntersection(((SubILineBaseInterface)myParentSubILine).getStartIntersection()));
            }
            if (this.isEndSegment()) {
                ((Vector<Segment>)noDuplicateVector).addAll(this.getSegmentsFromIntersection(((SubILineBaseInterface)myParentSubILine).getEndIntersection()));
            }
        }
        for (final Segment segment : noDuplicateVector) {
            if (segment instanceof ICDSegment && segment != this) {
                vector.addAll(((ICDSegment)segment).getOwnIntersectables());
            }
        }
        return (Collection<JointIntersectable>)vector;
    }
    
    private Collection<JointIntersectable> getOwnIntersectables() {
        final Vector<JointIntersectable> vector = new Vector<JointIntersectable>();
        final EnumerationIterator enumerationIterator = new EnumerationIterator(this.breadthFirstEnumeration());
        while (((Iterator)enumerationIterator).hasNext()) {
            final JointIntersectable next = ((Iterator<JointIntersectable>)enumerationIterator).next();
            if (next instanceof JointIntersectable && next.doesParticipateInJointIntersection()) {
                vector.add(next);
            }
        }
        return vector;
    }
    
    private Vector<Segment> getSegmentsFromIntersection(final GeneralIntersectionInterface generalIntersectionInterface) {
        Vector<Segment> segmentsFromArms = new Vector<Segment>();
        if (generalIntersectionInterface != null) {
            segmentsFromArms = (Vector<Segment>)generalIntersectionInterface.getSegmentsFromArms();
        }
        return segmentsFromArms;
    }
    
    public void drawCadElevation(final ElevationEntity elevationEntity, final ICadBlockNode cadBlockNode, final int n, final SolutionSetting solutionSetting) {
        super.drawCadElevation(elevationEntity, cadBlockNode, n, solutionSetting);
        for (final AssembleParent assembleParent : this.getExternalAssemblyParts()) {
            if (assembleParent instanceof TransformableEntity) {
                ((TransformableEntity)assembleParent).drawCadElevation(elevationEntity, cadBlockNode, n, solutionSetting);
            }
        }
    }
    
    public void drawIceCadElevationDotNet(final ElevationEntity elevationEntity, final IceCadIceApp iceCadIceApp, final IceCadNodeContainer iceCadNodeContainer, final IceCadBlock iceCadBlock, final int n) {
        super.drawIceCadElevationDotNet(elevationEntity, iceCadIceApp, iceCadNodeContainer, iceCadBlock, n);
        for (final AssembleParent assembleParent : this.getExternalAssemblyParts()) {
            if (assembleParent instanceof TransformableEntity) {
                ((TransformableEntity)assembleParent).drawIceCadElevationDotNet(elevationEntity, iceCadIceApp, iceCadNodeContainer, iceCadBlock, n);
            }
        }
    }
    
    public boolean shouldCreateElevation() {
        final ICDILine icdiLine = (ICDILine)this.getParent((Class)ICDILine.class);
        return icdiLine == null || icdiLine.getVerticalChase() == null;
    }
    
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        Vector<String> cadElevationScript = (Vector<String>)super.getCadElevationScript(elevationEntity);
        if (cadElevationScript == null) {
            cadElevationScript = new Vector<String>();
        }
        if (this.getTags() != null) {
            cadElevationScript.add("MTG:SS(CAD_TAG:CADELWIDTH)");
        }
        return cadElevationScript;
    }
    
    public String appendElevationInfoToMultiTags(final String s, final int n) {
        return this.getTags();
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
    
    public void drawIceCadForProxyEntityDotNet(final IceCadNodeContainer iceCadNodeContainer, final TransformableEntity transformableEntity, final IceCadCompositeBlock iceCadCompositeBlock, final int n, final SolutionSetting solutionSetting) {
        if (this.isDirty(n)) {
            if (this.cadTagDelegate == null) {
                this.cadTagDelegate = new ICDCadTagDelegate(this);
            }
            this.cadTagDelegate.drawIceCadForProxyEntityDotNet(iceCadNodeContainer, this.getTags(), transformableEntity, iceCadCompositeBlock);
        }
        super.drawIceCadForProxyEntityDotNet(iceCadNodeContainer, transformableEntity, iceCadCompositeBlock, n, solutionSetting);
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
    
    public boolean isManufacturerReportable() {
        return this.shouldAssemble();
    }
    
    public void handleCadSelection() {
        super.handleCadSelection();
        this.setSelected(true);
    }
    
    public void solve() {
        this.validateJointLocationFromOffModuleIntersection();
        super.solve();
    }
    
    public void validateJointLocationFromOffModuleIntersection() {
        this.jointLocationForBeam.clear();
        if (!this.inOffModuleIntersection) {
            return;
        }
        final GeneralSnapSet generalSnapSet = this.getGeneralSnapSet();
        if (generalSnapSet != null) {
            final Vector<ICDBeamSegment> vector = new Vector<ICDBeamSegment>();
            for (final IntersectionFactoryInterface intersectionFactoryInterface : generalSnapSet.getGeneralIntersectionFactoryInterfaces()) {
                if (intersectionFactoryInterface instanceof ICDIntersectionFactory) {
                    final Iterator children = ((ICDIntersectionFactory)intersectionFactoryInterface).getChildren();
                    while (children.hasNext()) {
                        final EntityObject entityObject = children.next();
                        if (entityObject instanceof ICDIntersection && ((ICDIntersection)entityObject).isNonBreakingIntersection()) {
                            final float n = 0.5f;
                            final ICDIntersection icdIntersection = (ICDIntersection)entityObject;
                            boolean b = false;
                            Point3f point3f = null;
                            vector.clear();
                            final Iterator iterator2 = icdIntersection.getArmVector().iterator();
                            while (iterator2.hasNext()) {
                                final Segment segment = iterator2.next().getSegment();
                                if (this.equals(segment)) {
                                    final Point3f convertSpaces = MathUtilities.convertSpaces(new Point3f(0.0f, 0.0f, 0.0f), (EntityObject)icdIntersection, (EntityObject)this);
                                    if (convertSpaces.x <= n || convertSpaces.x >= this.getXDimension() - n) {
                                        continue;
                                    }
                                    b = true;
                                    point3f = convertSpaces;
                                }
                                else {
                                    if (segment == null || !(segment instanceof ICDBeamSegment) || vector.contains(segment)) {
                                        continue;
                                    }
                                    vector.add((ICDBeamSegment)segment);
                                }
                            }
                            if (!b) {
                                continue;
                            }
                            final Iterator<ICDBeamSegment> iterator3 = vector.iterator();
                            while (iterator3.hasNext()) {
                                final float tubeLocation = iterator3.next().getTubeLocation();
                                if (tubeLocation >= 0.0f) {
                                    this.addJointLocationForBeam(new Point3f(point3f.x, 0.0f, tubeLocation));
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.jointLocationForBeam.size() == 0) {
            this.inOffModuleIntersection = false;
        }
    }
    
    public Collection<EntitySpaceCompareNodeWrapper> getSpaceCompareNodeWrappers() {
        final LinkedList<EntitySpaceCompareNodeWrapper> list = new LinkedList<EntitySpaceCompareNodeWrapper>();
        list.addAll((Collection<?>)this.getJointsEntitySpaceCompare());
        list.addAll((Collection<?>)this.getTubesEntitySpaceCompare(false));
        list.addAll((Collection<?>)this.getTubesEntitySpaceCompare(true));
        list.addAll((Collection<?>)this.getTabsEntitySpaceCompare());
        list.addAll((Collection<?>)this.getSlotsEntitySpaceCompare());
        final List childrenByClass = this.getChildrenByClass((Class)ICDTile.class, true, true);
        if (childrenByClass.size() > 0) {
            list.add(new EntitySpaceCompareNodeWrapper((TransformableEntity)childrenByClass.get(0), (Collection)new ArrayList()));
        }
        return list;
    }
    
    public void populateCompareNode(final Class clazz, final CompareNode compareNode) {
        if (!ICDUtilities.populateCompareNode(clazz, compareNode, (AssembleParent)this)) {
            super.populateCompareNode(clazz, compareNode);
        }
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
        final HashSet<Object> set = (HashSet<Object>)new HashSet<TypeableEntity>();
        set.addAll(this.getAllBasicExtrusions(false));
        set.addAll(this.getAllBasicExtrusions(true));
        set.addAll(this.getChildrenByClass((Class)ICDHinge.class, true));
        set.addAll(this.getChildrenByClass((Class)ICDDoorstop.class, true));
        set.addAll(this.getChildrenByClass((Class)ICDLock.class, true));
        set.addAll(this.getChildrenByClass((Class)ICDMagneticCatch.class, true));
        set.addAll(this.getChildrenByClass((Class)ICDValetHandle.class, true));
        set.addAll(this.getJointsAndChildrenForManufacturingReport());
        return (HashSet<TypeableEntity>)set;
    }
    
    public Vector<TypeableEntity> getAllBasicExtrusions(final boolean b) {
        final Vector<TypeableEntity> vector = new Vector<TypeableEntity>();
        this.getExtrusionForPreAssembleReport(vector, (TypeableEntity)this, b);
        final Iterator<AssembleParent> iterator = this.getExternalAssemblyParts().iterator();
        while (iterator.hasNext()) {
            this.getExtrusionForPreAssembleReport(vector, (TypeableEntity)iterator.next(), b);
        }
        final Vector<TypeableEntity> vector2 = new Vector<TypeableEntity>();
        for (int i = 0; i < vector.size(); ++i) {
            final TypeableEntity typeableEntity = vector.get(i);
            if (typeableEntity instanceof BasicExtrusion) {
                final BasicExtrusion e = (BasicExtrusion)typeableEntity;
                if (e != null && e.containsAttributeKey("ShowInManufacturingReport") && e.getAttributeValueAsFloat("ShowInManufacturingReport") == 1.0) {
                    vector2.add((TypeableEntity)e);
                }
            }
        }
        return vector2;
    }
    
    public int getNumberOfChaseAtPoint(final Point3f point3f) {
        int n = 0;
        for (final GeneralIntersectionInterface generalIntersectionInterface : this.getIntersectionsForSegment()) {
            if (generalIntersectionInterface instanceof ICDIntersection) {
                n += ((ICDIntersection)generalIntersectionInterface).getNumberOfChaseAtPoint((Segment)this, point3f);
            }
        }
        return n;
    }
    
    public void SetWithOffModuleIntersection(final boolean inOffModuleIntersection) {
        if (!(this.inOffModuleIntersection = inOffModuleIntersection)) {
            this.jointLocationForBeam.clear();
        }
    }
    
    public void clearJointLocationForBeam() {
        this.jointLocationForBeam.clear();
    }
    
    public void addJointLocationForBeam(final Point3f point3f) {
        if (point3f.x > 0.5f && point3f.x < this.getXDimension() - 0.5f) {
            boolean b = false;
            for (final Pair<Point3f, Integer> pair : this.jointLocationForBeam) {
                if (Math.abs(((Point3f)pair.first).x - point3f.x) < 1.5f && Math.abs(((Point3f)pair.first).z - point3f.z) < 1.5f) {
                    b = true;
                    pair.second = 2;
                    break;
                }
            }
            if (!b) {
                this.jointLocationForBeam.add((Pair<Point3f, Integer>)new Pair((Object)point3f, (Object)1));
            }
        }
    }
    
    public List<Pair<Point3f, Integer>> getJointLocationForBeam() {
        return this.jointLocationForBeam;
    }
    
    public boolean shouldAssemble() {
        return this.getAttributeValueAsBoolean("shouldAssemble", false) && !((ICDILine)this.getParent((Class)ICDILine.class)).shouldILineAssemble();
    }
    
    public void handleAttributeChange(final String s, final String s2) {
        ICDUtilities.handleAttributeChange((EntityObject)this, s, s2);
    }
    
    public boolean hasDoubleChase() {
        boolean doubleChase = false;
        final PanelInterface basePanel = this.getBasePanel();
        if (basePanel instanceof ICDPanel) {
            doubleChase = ((ICDPanel)basePanel).isDoubleChase();
        }
        return doubleChase;
    }
    
    public void collectExtraIndirectAssemblyParts(final ICDIntersection icdIntersection, final HashSet<EntityObject> set, final boolean b, final boolean b2, final boolean b3, final Class<EntityObject>... array) {
        this.extraHorizontalExtrusionSearchScope.first = -1.0f;
        this.extraHorizontalExtrusionSearchScope.second = -1.0f;
        final PanelInterface basePanel = this.getBasePanel();
        if (basePanel instanceof ICDPanel) {
            ((ICDPanel)basePanel).collectExtraIndirectAssemblyParts(icdIntersection == this.getIntersectionForSegment(!this.isFlipped()), set, b, b2, b3, array);
        }
        final PanelInterface stackingPanel = this.getStackingPanel();
        if (stackingPanel != null && stackingPanel instanceof ICDPanel) {
            ((ICDPanel)stackingPanel).collectExtraIndirectAssemblyParts(icdIntersection == this.getIntersectionForSegment(!this.isFlipped()), set, b, b2, b3, array);
        }
        if ((float)this.extraHorizontalExtrusionSearchScope.first > -1.0f && (float)this.extraHorizontalExtrusionSearchScope.second > -1.0f) {
            this.collectTubeAndJointWithinScope(basePanel, set, array);
        }
    }
    
    private void collectTubeAndJointWithinScope(final PanelInterface panelInterface, final HashSet<EntityObject> set, final Class<EntityObject>... array) {
        final FrameInterface physicalFrame = panelInterface.getPhysicalFrame();
        if (physicalFrame != null) {
            final EntityObject entityObject = (EntityObject)physicalFrame.getTopExtrusion();
            if (entityObject != null) {
                final boolean doesTheArrayContain = ICDUtilities.doesTheArrayContain(array, BasicExtrusion.class);
                final boolean doesTheArrayContain2 = ICDUtilities.doesTheArrayContain(array, ICDJoint.class);
                final Iterator children = entityObject.getChildren();
                while (children.hasNext()) {
                    final ICDSubInternalExtrusion next = children.next();
                    if (next instanceof ICDSubInternalExtrusion && doesTheArrayContain && next.containsAttributeKey("isAssembled")) {
                        final ICDSubInternalExtrusion e = next;
                        final float n = e.getBasePoint3f().z + e.getZDimension() / 2.0f;
                        if ((float)this.extraHorizontalExtrusionSearchScope.first >= n || n >= (float)this.extraHorizontalExtrusionSearchScope.second) {
                            continue;
                        }
                        set.add((EntityObject)e);
                    }
                    else {
                        if (!(next instanceof ICDJoint) || !doesTheArrayContain2 || !((ICDJoint)next).containsAttributeKey("isAssembled")) {
                            continue;
                        }
                        final float z = ((ICDJoint)next).getBasePoint3f().z;
                        if ((float)this.extraHorizontalExtrusionSearchScope.first - 1.0f >= z || z >= (float)this.extraHorizontalExtrusionSearchScope.second + 1.0f) {
                            continue;
                        }
                        set.add((EntityObject)next);
                    }
                }
            }
        }
    }
    
    public boolean isSegmentExcludedFromIlineAssemly() {
        boolean currentValue = false;
        final Attribute attributeObject = this.getAttributeObject("ICD_ExcludeFromIlineAssembly");
        if (attributeObject != null) {
            currentValue = ((BooleanAttribute)attributeObject).getCurrentValue();
        }
        return currentValue;
    }
    
    public boolean brokenByAnotherSegment(final ICDSegment icdSegment) {
        final PanelInterface basePanel = this.getBasePanel();
        if (icdSegment != null) {
            final PanelInterface basePanel2 = icdSegment.getBasePanel();
            if (basePanel instanceof ICDPanel && basePanel2 instanceof ICDPanel) {
                return ((ICDPanel)basePanel).brokenByAnotherPanel((ICDPanel)basePanel2);
            }
        }
        return false;
    }
    
    public boolean isBottomTileNoFrame() {
        boolean b = false;
        for (final ICDTile icdTile : this.getChildrenByClass((Class)ICDTile.class, true)) {
            if (icdTile.isBottomTileInBasePanel() && icdTile.isNoFrameTile()) {
                b = true;
            }
        }
        return b;
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
    
    public void setExtraHorizontalExtrusionSearchScope(final float f, final float f2) {
        this.extraHorizontalExtrusionSearchScope.first = f;
        this.extraHorizontalExtrusionSearchScope.second = f2;
    }
    
    static {
        ICDSegment.logger = Logger.getLogger((Class)ICDSegment.class);
    }
}
