package net.iceedge.catalogs.icd;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.main.ElevationClusterEntity;
import net.dirtt.icelib.main.SolutionSetting;
import net.iceedge.icecore.icecad.ice.tree.IceCadCompositeBlock;
import net.dirtt.icelib.main.TransformableEntity;
import net.iceedge.icecore.icecad.ice.IceCadIceApp;
import net.iceedge.icecore.icecad.ice.tree.IceCadNodeContainer;
import net.dirtt.icecad.cadtree.ICadTreeNode;
import net.dirtt.icelib.main.TypeableEntity;
import net.dirtt.icelib.report.compare.CompareNode;
import java.util.LinkedList;
import net.dirtt.utilities.EntitySpaceCompareNodeWrapper;
import net.iceedge.icecore.basemodule.finalclasses.SnapSetHandler;
import java.util.HashMap;
import java.util.HashSet;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintable;
import javax.vecmath.Vector3f;
import net.dirtt.icelib.main.ElevationEntity;
import javax.swing.tree.DefaultMutableTreeNode;
import net.iceedge.icebox.utilities.Node;
import java.io.IOException;
import net.dirtt.utilities.PersistentFileManager;
import net.dirtt.xmlFiles.XMLWriter;
import net.dirtt.icelib.main.MiscItemsBucket;
import java.util.ArrayList;
import net.iceedge.icecore.basemodule.finalclasses.GeneralSnapSet;
import net.dirtt.utilities.VectorUtilities;
import net.iceedge.catalogs.icd.worksurfaces.ICDBasicWorksurface;
import net.iceedge.catalogs.icd.panel.ICDPanel;
import net.iceedge.catalogs.icd.panel.ICDVerticalChaseGroup;
import net.dirtt.icelib.ui.attribute.explorer.ui.entity.PossibleValue;
import net.iceedge.icecore.basemodule.interfaces.IntersectionFactoryInterface;
import net.iceedge.catalogs.icd.intersection.ICDIntersection;
import net.dirtt.icelib.main.attributes.Attribute;
import java.util.Collection;
import java.util.List;
import net.dirtt.icelib.main.attributes.proxy.OptionAttributeProxy;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.utilities.MathUtilities;
import java.util.Vector;
import net.iceedge.catalogs.icd.panel.ICDSubFrameSideContainer;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import net.iceedge.icecore.basemodule.interfaces.GeneralIntersectionInterface;
import java.util.StringTokenizer;
import javax.vecmath.Point3f;
import java.util.Iterator;
import net.iceedge.icecore.basemodule.interfaces.SubILineInterface;
import net.iceedge.catalogs.icd.intersection.ICDIntersectionFactory;
import net.dirtt.icelib.undo.iceobjects.Point3fWithUndo;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.catalogs.icd.icecad.ICDCadTagDelegate;
import net.iceedge.catalogs.icd.panel.ICDVerticalChase;
import org.apache.log4j.Logger;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.catalogs.icd.icecad.ICDCadTagPaintable;
import net.iceedge.icebox.utilities.MultiTagAppender;
import net.iceedge.icecore.basemodule.interfaces.AssembleParent;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintableRoot;
import net.iceedge.icecore.basemodule.baseclasses.BasicILine;

public class ICDILine extends BasicILine implements AssemblyPaintableRoot, AssembleParent, MultiTagAppender, ICDCadTagPaintable, ICDManufacturingReportable
{
    private static final long serialVersionUID = 4167822373729867680L;
    private static Logger logger;
    public static final String MANUFACTURING_REPORT_DESCRIPTION = "Assembled partition frame";
    private String isVerticalChase;
    private ICDVerticalChase verticalChase;
    private ICDCadTagDelegate cadTagDelegate;
    public static final String IS_VERTICAL_CHASE_YES = "Yes";
    public static final String IS_VERTICAL_CHASE_NO = "No";
    
    public ICDILine(final String s, final TypeObject typeObject) {
        super(s, typeObject);
        this.isVerticalChase = null;
    }
    
    public ICDILine(final TypeObject typeObject) {
        super(typeObject);
        this.isVerticalChase = null;
    }
    
    public ICDILine(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.isVerticalChase = null;
    }
    
    public ICDILine(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject, final Point3fWithUndo point3fWithUndo) {
        super(s, s2, typeObject, optionObject, point3fWithUndo);
        this.isVerticalChase = null;
    }
    
    public ICDILine buildClone(final ICDILine icdiLine) {
        super.buildClone((BasicILine)icdiLine);
        icdiLine.setVerticalChase(null);
        icdiLine.createNewAttribute("ICD_is_Vertical_Chase", this.isVerticalChase);
        icdiLine.setIsVerticalChase(this.isVerticalChase);
        return icdiLine;
    }
    
    public Object clone() {
        return this.buildClone(new ICDILine(this.getId(), this.currentType, this.currentOption));
    }
    
    public float getRealPanelDepthForPanels() {
        return 1.0f;
    }
    
    public Class getIntersectionFactoryClassName() {
        return ICDIntersectionFactory.class;
    }
    
    public String getIntersectionFactoryTypeName() {
        return "ICD_IntersectionFactoryType";
    }
    
    public String getILineType() {
        return "ICD";
    }
    
    public void setModified(final boolean b) {
        super.setModified(b);
        if (b) {
            for (final SubILineInterface subILineInterface : this.getSubWallILines()) {
                if (subILineInterface instanceof ICDSubILine) {
                    ((ICDSubILine)subILineInterface).setPanelToPanelHWModified(b);
                }
            }
        }
    }
    
    public double[] getSnapAngleConstraints(final BasicILine basicILine, final Point3f point3f) {
        final StringTokenizer stringTokenizer = new StringTokenizer("0,30,54,60,72,90,108,120,126,150,180,240,270,300,360", ",");
        final double[] array = new double[stringTokenizer.countTokens()];
        int n = 0;
        while (stringTokenizer.hasMoreTokens()) {
            array[n++] = Math.toRadians(Double.parseDouble(stringTokenizer.nextToken()));
        }
        return super.getSnapAngleConstraints(basicILine, point3f);
    }
    
    public void flip() {
        final Vector breakingIntersections = this.getBreakingIntersections();
        for (int i = 0; i < breakingIntersections.size(); ++i) {
            final Iterator iterator = breakingIntersections.get(i).getSegmentsFromArms().iterator();
            while (iterator.hasNext()) {
                final Iterator iterator2 = ((BasicILine)iterator.next().getMyParentILine()).getChildrenByClass((Class)ICDSubFrameSideContainer.class, true, true).iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().removeAllBreaks();
                }
            }
        }
        super.flip();
    }
    
    public Vector getDimensionPoints() {
        final Vector dimensionPoints = super.getDimensionPoints();
        final Solution solution = this.getSolution();
        if (solution != null) {
            final Point3f point3f = new Point3f(this.getStartPoint3f());
            final Point3f point3f2 = new Point3f(this.getEndPoint3f());
            final Point3f convertSpaces = MathUtilities.convertSpaces(point3f, (EntityObject)solution, (EntityObject)this);
            final Point3f convertSpaces2 = MathUtilities.convertSpaces(point3f2, (EntityObject)solution, (EntityObject)this);
            final Point3f point3f3 = new Point3f(convertSpaces.x - 0.5f, convertSpaces.y, convertSpaces.z);
            final Point3f point3f4 = new Point3f(convertSpaces2.x + 0.5f, convertSpaces.y, convertSpaces.z);
            final Point3f convertSpaces3 = MathUtilities.convertSpaces(point3f3, (EntityObject)this, (EntityObject)solution);
            final Point3f convertSpaces4 = MathUtilities.convertSpaces(point3f4, (EntityObject)this, (EntityObject)solution);
            dimensionPoints.add(new Point3f(convertSpaces3));
            dimensionPoints.add(new Point3f(convertSpaces4));
        }
        return dimensionPoints;
    }
    
    public String getDefaultLayerName() {
        return "ICD I-Line";
    }
    
    public boolean allowOtherAngleSnapping() {
        return true;
    }
    
    public void addOnTheFlyAttribute() {
        super.addOnTheFlyAttribute();
        this.addIsVerticalChaseAttOnTheFly();
    }
    
    private void addIsVerticalChaseAttOnTheFly() {
        final List selectedEntitiesByClass = this.getSolution().getSelectedEntitiesByClass((Class)ICDILine.class, true);
        if (selectedEntitiesByClass != null) {
            final OptionAttributeProxy optionAttributeProxy = Solution.getWorldAttributeProxy().get("ICD_is_Vertical_Chase");
            optionAttributeProxy.getPossibleValues().clear();
            optionAttributeProxy.addPossibleValue("No");
            if (this.isValidSelectionForChase(selectedEntitiesByClass)) {
                optionAttributeProxy.addPossibleValue("Yes");
            }
            this.createNewAttribute("ICD_is_Vertical_Chase", this.isVerticalChase = (this.isVerticalChase() ? "Yes" : "No"));
        }
    }
    
    private boolean isValidSelectionForChase(final List<ICDILine> c) {
        final Vector vector = new Vector();
        vector.addAll(c);
        vector.removeAll(this.getChaseILines(c));
        return vector.size() == 0;
    }
    
    public Vector<ICDILine> getChaseILines(final List<ICDILine> list) {
        final Vector<ICDILine> vector = new Vector<ICDILine>();
        Vector<Float> vector2 = ICDVerticalChase.getPossibleDimensionsForVerticalChase();
        vector.addAll(this.getChaseILinesUsingRotationModifier(list, vector2, this, this.getEndPoint3f(), 1.5707964f));
        if (vector.size() != 4 || !this.areRemainingPossibleDimensionsValid(vector2) || !this.areHeightsForChaseLinesValid(vector)) {
            vector.clear();
            vector2 = ICDVerticalChase.getPossibleDimensionsForVerticalChase();
            vector.addAll(this.getChaseILinesUsingRotationModifier(list, vector2, this, this.getEndPoint3f(), -1.5707964f));
        }
        if (vector.size() != 4 || !this.areRemainingPossibleDimensionsValid(vector2) || !this.areHeightsForChaseLinesValid(vector)) {
            vector.clear();
        }
        return vector;
    }
    
    private boolean areRemainingPossibleDimensionsValid(final Vector<Float> vector) {
        final Vector<Object> c = new Vector<Object>();
        for (int i = 0; i < vector.size(); ++i) {
            final float floatValue = vector.get(i);
            for (int j = 0; j < vector.size(); ++j) {
                final float floatValue2 = vector.get(j);
                if (i != j && MathUtilities.isSameFloat(floatValue, floatValue2, 0.01f)) {
                    c.add(floatValue);
                    c.add(floatValue2);
                }
            }
        }
        vector.removeAll(c);
        return vector.size() == 0;
    }
    
    private boolean areHeightsForChaseLinesValid(final Vector<ICDILine> vector) {
        final float height = this.getHeight();
        final Iterator<ICDILine> iterator = vector.iterator();
        while (iterator.hasNext()) {
            if (!MathUtilities.isSameFloat(height, iterator.next().getHeight(), 0.1f)) {
                return false;
            }
        }
        return true;
    }
    
    private Vector<ICDILine> getChaseILinesUsingRotationModifier(final List<ICDILine> list, final Vector<Float> vector, final ICDILine e, final Point3f point3f, final float n) {
        final Vector<ICDILine> vector2 = new Vector<ICDILine>();
        vector2.add(e);
        final float normalizeRotation = MathUtilities.normalizeRotation(MathUtilities.normalizeRotation(MathUtilities.calculateRotation(e.getStartPoint3f(), e.getEndPoint3f())) + n);
        for (final ICDILine icdiLine : list) {
            final Point3f startPoint3f = icdiLine.getStartPoint3f();
            final Point3f endPoint3f = icdiLine.getEndPoint3f();
            if (this.isPossibleDimensionForVerticalChase(vector, MathUtilities.calculate2DLength(startPoint3f, endPoint3f), 0.01f) && icdiLine.getPanelSegments().size() < 2) {
                float n2 = 0.0f;
                Point3f point3f2 = null;
                if (MathUtilities.isSamePoint(point3f, startPoint3f, 0.01f)) {
                    n2 = MathUtilities.normalizeRotation(MathUtilities.calculateRotation(startPoint3f, endPoint3f));
                    point3f2 = endPoint3f;
                }
                else if (MathUtilities.isSamePoint(point3f, endPoint3f, 0.01f)) {
                    n2 = MathUtilities.normalizeRotation(MathUtilities.calculateRotation(endPoint3f, startPoint3f));
                    point3f2 = startPoint3f;
                }
                if (!MathUtilities.isSameFloat(normalizeRotation, n2, 0.01f) || point3f2 == null) {
                    continue;
                }
                this.removeFloatElementFromList(vector, icdiLine.getLength(), 0.01f);
                if (icdiLine == this) {
                    break;
                }
                vector2.addAll(this.getChaseILinesUsingRotationModifier(list, vector, icdiLine, point3f2, n));
                break;
            }
        }
        return vector2;
    }
    
    private void removeFloatElementFromList(final Vector<Float> vector, final float n, final float n2) {
        int index = 0;
        boolean b = false;
        final Iterator<Float> iterator = vector.iterator();
        while (iterator.hasNext()) {
            if (MathUtilities.isSameFloat((float)iterator.next(), n, n2)) {
                b = true;
                break;
            }
            ++index;
        }
        if (b) {
            vector.removeElementAt(index);
        }
    }
    
    private boolean isPossibleDimensionForVerticalChase(final Vector<Float> vector, final float n, final float n2) {
        final Iterator<Float> iterator = vector.iterator();
        while (iterator.hasNext()) {
            if (MathUtilities.isSameFloat((float)iterator.next(), n, n2)) {
                return true;
            }
        }
        return false;
    }
    
    public void removeOnTheFlyAttribute() {
        if (this.verticalChase == null) {
            this.getAttributeHashMap().remove((Object)"ICD_is_Vertical_Chase");
        }
    }
    
    public void setLocalVariables(final String s) {
    }
    
    public void getOnTheFlyAttributes(final Collection<Attribute> collection) {
        super.getOnTheFlyAttributes((Collection)collection);
        collection.add(this.getAttributeObject("ICD_is_Vertical_Chase"));
    }
    
    public ICDVerticalChase getVerticalChase() {
        return this.verticalChase;
    }
    
    public void setVerticalChase(final ICDVerticalChase icdVerticalChase) {
        final IntersectionFactoryInterface intersectionFactory = this.getIntersectionFactory();
        if (intersectionFactory != null) {
            for (final ICDIntersection icdIntersection : ((EntityObject)intersectionFactory).getChildrenByClass((Class)ICDIntersection.class, true, true)) {
                final Vector segmentsFromArms = icdIntersection.getSegmentsFromArms();
                if (segmentsFromArms.size() == 2) {
                    boolean b = true;
                    final Iterator<Segment> iterator2 = segmentsFromArms.iterator();
                    while (iterator2.hasNext()) {
                        final ICDILine icdiLine = (ICDILine)iterator2.next().getParent((Class)ICDILine.class);
                        if (icdiLine != null && !icdiLine.isVerticalChase()) {
                            b = false;
                            break;
                        }
                    }
                    if (!b) {
                        continue;
                    }
                    icdIntersection.setVerticalChase(icdVerticalChase);
                }
            }
        }
        this.verticalChase = icdVerticalChase;
    }
    
    public void applyChangesFromEditor(final String s, final PossibleValue possibleValue, final Collection<PossibleValue> collection, final Collection<String> collection2, final String s2) {
        super.applyChangesFromEditor(s, possibleValue, (Collection)collection, (Collection)collection2, s2);
        this.applyChangesForIsVerticalChaseFromEditor(s, possibleValue);
    }
    
    private void applyChangesForIsVerticalChaseFromEditor(final String s, final PossibleValue possibleValue) {
        if (s.equals("ICD_is_Vertical_Chase")) {
            this.isVerticalChase = possibleValue.getValue();
            final ICDVerticalChase andValidateVerticalChase = ((ICDVerticalChaseGroup)this.getSolution().getMiscItemsBucket().getChildByClass((Class)ICDVerticalChaseGroup.class)).getAndValidateVerticalChase(this.getChaseILines(this.getSolution().getSelectedEntitiesByClass((Class)ICDILine.class, true)), this, this.isVerticalChase());
            this.setVerticalChase(andValidateVerticalChase);
            if (this.isVerticalChase()) {
                final Iterator<ICDILine> iterator = andValidateVerticalChase.getChaseILines().iterator();
                while (iterator.hasNext()) {
                    final Iterator iterator2 = iterator.next().getChildrenFirstAppearance((Class)ICDPanel.class, true).iterator();
                    while (iterator2.hasNext()) {
                        iterator2.next().destroyCad();
                    }
                }
            }
        }
    }
    
    public void setSelected(final boolean b, final Solution solution) {
        super.setSelected(b, solution);
        if (this.verticalChase != null) {
            this.verticalChase.setVerticalChaseILinesSelected(b, solution);
        }
    }
    
    public boolean shouldSelectIntersectionsOnSelect() {
        return false;
    }
    
    public boolean shouldCreateElevation() {
        return this.verticalChase == null;
    }
    
    public boolean elevationDimensionIncludePost() {
        return false;
    }
    
    public void addToCutSolution(final Vector vector, final Vector vector2, final Solution solution, final boolean b) {
        final GeneralSnapSet generalSnapSet = this.getGeneralSnapSet();
        if (generalSnapSet != null && generalSnapSet.getSnappableILines().size() == 1) {
            for (final EntityObject e : generalSnapSet.getChildrenVector()) {
                if (e instanceof ICDBasicWorksurface && !VectorUtilities.vectorContains(vector, (Object)e)) {
                    vector.add(e);
                }
            }
        }
        if (this.verticalChase != null && !VectorUtilities.vectorContains(vector, (Object)this.verticalChase) && b) {
            vector.add(this.verticalChase);
        }
        super.addToCutSolution(vector, vector2, solution, b);
    }
    
    public void cutFromTree() {
        final List<ChaseAndPanel> chaseAndPanelList = this.createChaseAndPanelList();
        super.cutFromTree();
        this.removeAllPanelBreaks(chaseAndPanelList);
    }
    
    private List<ChaseAndPanel> createChaseAndPanelList() {
        final ArrayList<ChaseAndPanel> list = new ArrayList<ChaseAndPanel>();
        for (final ICDSubFrameSideContainer icdSubFrameSideContainer : this.getChildrenByClass((Class)ICDSubFrameSideContainer.class, true)) {
            list.add(new ChaseAndPanel(icdSubFrameSideContainer, icdSubFrameSideContainer.getPanel(true), icdSubFrameSideContainer.getPanel(false)));
        }
        return list;
    }
    
    private void removeAllPanelBreaks(final List<ChaseAndPanel> list) {
        for (final ChaseAndPanel chaseAndPanel : list) {
            final ICDSubFrameSideContainer chase = chaseAndPanel.chase;
            final ICDPanel atTheStart = chaseAndPanel.atTheStart;
            final ICDPanel atTheEnd = chaseAndPanel.atTheEnd;
            this.removePanelBreaks(chase, atTheStart);
            this.removePanelBreaks(chase, atTheEnd);
        }
    }
    
    private void removePanelBreaks(final ICDSubFrameSideContainer icdSubFrameSideContainer, final ICDPanel icdPanel) {
        if (icdPanel != null) {
            icdSubFrameSideContainer.removePanelExtrusionBreaks(icdPanel, true);
            icdSubFrameSideContainer.removePanelExtrusionBreaks(icdPanel, false);
        }
    }
    
    public void solve() {
        this.validateVerticalChase();
        super.solve();
    }
    
    private void validateVerticalChase() {
        final boolean verticalChase = this.isVerticalChase();
        if (verticalChase && this.verticalChase == null) {
            final MiscItemsBucket miscItemsBucket = this.getSolution().getMiscItemsBucket();
            if (miscItemsBucket != null) {
                final ICDVerticalChaseGroup icdVerticalChaseGroup = (ICDVerticalChaseGroup)miscItemsBucket.getChildByClass((Class)ICDVerticalChaseGroup.class);
                if (icdVerticalChaseGroup != null) {
                    this.verticalChase = icdVerticalChaseGroup.getAndValidateVerticalChase(this.getChaseILines(((GeneralSnapSet)this.getParent((Class)GeneralSnapSet.class)).getChildrenByClass((Class)ICDILine.class, false)), this, verticalChase);
                }
            }
        }
    }
    
    public void destroy() {
        if (this.verticalChase != null) {
            this.verticalChase.destroy();
        }
        super.destroy();
    }
    
    protected void writeXMLFields(final XMLWriter xmlWriter, final PersistentFileManager.FileWriter fileWriter) throws IOException {
        super.writeXMLFields(xmlWriter, fileWriter);
        xmlWriter.writeTextElement("ICD_is_Vertical_Chase", this.isVerticalChase);
    }
    
    protected void setFieldInfoFromXML(final Node node, final DefaultMutableTreeNode defaultMutableTreeNode, final PersistentFileManager.FileReader fileReader) {
        super.setFieldInfoFromXML(node, defaultMutableTreeNode, fileReader);
        this.isVerticalChase = this.getChildElementValue("ICD_is_Vertical_Chase", node);
    }
    
    public String getIsVerticalChase() {
        return this.isVerticalChase;
    }
    
    public boolean isVerticalChase() {
        return "Yes".equals(this.getIsVerticalChase());
    }
    
    public void setIsVerticalChase(final String isVerticalChase) {
        this.isVerticalChase = isVerticalChase;
    }
    
    public boolean checkSplit() {
        return false;
    }
    
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        final Vector<String> vector = new Vector<String>();
        if (this.getTags() != null) {
            vector.add("MTG:SS(CAD_TAG:CADELWIDTH)");
        }
        return vector;
    }
    
    public String appendElevationInfoToMultiTags(final String s, final int n) {
        return this.getTags();
    }
    
    protected void calculateNamedScales() {
        super.calculateNamedScales();
        this.addNamedScale("CADELWIDTH", new Vector3f(this.getXDimension(), 1.0f, 1.0f));
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.addNamedPoint("CAD_TAG", new Point3f(this.getXDimension() / 2.0f, this.getYDimension() / 2.0f, this.getZDimension() + 5.0f));
    }
    
    public boolean isValidForClusterElevation() {
        return true;
    }
    
    public void addAdditonalPaintableEntities(final List<AssemblyPaintable> list) {
    }
    
    public boolean checkAssembledForAssemblyElevation() {
        return true;
    }
    
    public boolean shouldICDMakePreAssembledReport() {
        return this.getAttributeValueAsString("isAssembled").equals("true");
    }
    
    public boolean shouldPaintAssemblyInIce2D() {
        return false;
    }
    
    public HashSet<EntityObject> getDirectAssemblyParts() {
        return new HashSet<EntityObject>();
    }
    
    public HashSet<EntityObject> getIndirectAssemblyParts() {
        return new HashSet<EntityObject>();
    }
    
    public HashSet<AssembleParent> getExternalAssemblyParts() {
        final HashSet<ICDSegment> set = (HashSet<ICDSegment>)new HashSet<AssembleParent>();
        for (final ICDSegment e : this.getChildrenFirstAppearance((Class)ICDSegment.class, true)) {
            if (!e.isSegmentExcludedFromIlineAssemly()) {
                set.add((AssembleParent)e);
            }
        }
        return (HashSet<AssembleParent>)set;
    }
    
    public boolean shouldIncludeExtraIndirectAssemblyParts() {
        return false;
    }
    
    public void collectSnapSetHandlers(final HashMap<Class<? extends SnapSetHandler>, SnapSetHandler> hashMap) {
        super.collectSnapSetHandlers((HashMap)hashMap);
        if (hashMap.get(ICDAssemblyHandler.class) == null) {
            hashMap.put((Class<? extends SnapSetHandler>)ICDAssemblyHandler.class, (SnapSetHandler)new ICDAssemblyHandler());
        }
    }
    
    public boolean shouldAssemble() {
        final boolean attributeValueAsBoolean = this.getAttributeValueAsBoolean("shouldAssemble", false);
        final boolean b = this.verticalChase != null && this.verticalChase.shouldAssemble();
        return attributeValueAsBoolean && !b;
    }
    
    public boolean shouldILineAssemble() {
        final boolean attributeValueAsBoolean = this.getAttributeValueAsBoolean("shouldAssemble", false);
        final boolean b = this.verticalChase != null && this.verticalChase.shouldAssemble();
        return attributeValueAsBoolean || b;
    }
    
    public boolean isManufacturerReportable() {
        return this.shouldAssemble();
    }
    
    public Collection<EntitySpaceCompareNodeWrapper> getSpaceCompareNodeWrappers() {
        final LinkedList<Object> list = (LinkedList<Object>)new LinkedList<EntitySpaceCompareNodeWrapper>();
        final Iterator<AssembleParent> iterator = this.getExternalAssemblyParts().iterator();
        while (iterator.hasNext()) {
            list.addAll(iterator.next().getSpaceCompareNodeWrappers());
        }
        return (Collection<EntitySpaceCompareNodeWrapper>)list;
    }
    
    public void populateCompareNode(final Class clazz, final CompareNode compareNode) {
        if (!ICDUtilities.populateCompareNode(clazz, compareNode, (AssembleParent)this)) {
            super.populateCompareNode(clazz, compareNode);
        }
    }
    
    public HashSet<TypeableEntity> getAssembledChildrenForManReport() {
        final HashSet<Object> set = (HashSet<Object>)new HashSet<TypeableEntity>();
        final Iterator<AssembleParent> iterator = this.getExternalAssemblyParts().iterator();
        while (iterator.hasNext()) {
            set.addAll(iterator.next().getAssembledChildrenForManReport());
        }
        return (HashSet<TypeableEntity>)set;
    }
    
    protected String getTags() {
        return ICDUtilities.getTags((EntityObject)this);
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
    
    public void handleAttributeChange(final String s, final String s2) {
        ICDUtilities.handleAttributeChange((EntityObject)this, s, s2);
    }
    
    public boolean shouldDrawInClusterElevation(final ElevationClusterEntity elevationClusterEntity) {
        return false;
    }
    
    public boolean shouldCheckSKUSetting() {
        return false;
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
        ICDILine.logger = Logger.getLogger((Class)ICDILine.class);
    }
    
    private class ChaseAndPanel
    {
        ICDSubFrameSideContainer chase;
        ICDPanel atTheStart;
        ICDPanel atTheEnd;
        
        ChaseAndPanel(final ICDSubFrameSideContainer chase, final ICDPanel atTheStart, final ICDPanel atTheEnd) {
            this.chase = chase;
            this.atTheStart = atTheStart;
            this.atTheEnd = atTheEnd;
        }
    }
}
