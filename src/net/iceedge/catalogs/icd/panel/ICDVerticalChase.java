// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icecad.cadtree.ICadBlockNode;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import java.util.LinkedList;
import net.dirtt.utilities.EntitySpaceCompareNodeWrapper;
import java.util.Collection;
import java.util.HashSet;
import net.dirtt.icebox.iceoutput.core.IceOutputTextNode;
import java.util.ArrayList;
import net.dirtt.icelib.main.SolutionSetting;
import net.iceedge.icecore.icecad.ice.tree.IceCadCompositeBlock;
import net.iceedge.icecore.icecad.ice.IceCadIceApp;
import net.iceedge.icecore.icecad.ice.tree.IceCadNodeContainer;
import net.iceedge.catalogs.icd.ICDUtilities;
import net.dirtt.icecad.cadtree.ICadTreeNode;
import net.dirtt.icelib.report.compare.CompareNode;
import net.dirtt.icelib.main.ElevationEntity;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicPanelSubILine;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelSubILineInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelSegmentInterface;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import net.dirtt.icebox.canvas2d.Ice2DTextNode;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationUtilities;
import javax.vecmath.Vector3f;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicExtrusion;
import net.iceedge.icecore.basemodule.interfaces.GeneralIntersectionInterface;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.swing.tree.DefaultMutableTreeNode;
import net.iceedge.icebox.utilities.Node;
import net.dirtt.utilities.PersistentFileManager;
import net.dirtt.xmlFiles.XMLWriter;
import net.dirtt.icelib.main.RequiredChildTypeContainer;
import java.util.HashMap;
import net.dirtt.icelib.main.attributes.proxy.AttributeProxy;
import net.dirtt.icelib.main.attributes.Attribute;
import java.io.IOException;
import net.dirtt.icelib.main.Catalog;
import net.dirtt.icelib.main.TypeableEntity;
import net.dirtt.icelib.ui.OptionProxyValue;
import net.dirtt.icelib.main.attributes.proxy.OptionAttributeProxy;
import net.dirtt.icelib.main.Solution;
import net.iceedge.icecore.basemodule.baseclasses.BasicILine;
import java.util.List;
import net.iceedge.catalogs.icd.ICDSegment;
import java.util.Iterator;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.icecad.ice.tree.IceCadMTextNode;
import net.dirtt.icecad.cadtree.ICadTextNode;
import net.iceedge.catalogs.icd.ICDILine;
import java.util.Vector;
import net.dirtt.icelib.undo.iceobjects.AttributeHashMapWithUndo;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icebox.iceoutput.core.PlotPaintable;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintable;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintableRoot;
import net.iceedge.icecore.basemodule.interfaces.AssembleParent;
import net.dirtt.icelib.main.TransformableEntity;

public class ICDVerticalChase extends TransformableEntity implements AssembleParent, AssemblyPaintableRoot, AssemblyPaintable, PlotPaintable, ICDManufacturingReportable
{
    static final String FABRIC = "F";
    static final String LAMINATE = "L";
    static final String MELAMIME = "M";
    protected static ICDVerticalChaseSKUGenerator skuGenerator;
    public static final String MANUFACTURING_REPORT_DESC = "Assembled vertical chase";
    protected OptionObject currentCatalogOption;
    private AttributeHashMapWithUndo catalogAttributeHashMap;
    protected String newSku;
    private Vector<Long> chaseILineIDs;
    private Vector<ICDILine> chaseILines;
    private ICadTextNode textNode;
    private IceCadMTextNode iceTextNode;
    private IceCadMTextNode textNodeNetForProxyEntity;
    
    public ICDVerticalChase(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.currentCatalogOption = null;
        this.catalogAttributeHashMap = new AttributeHashMapWithUndo((EntityObject)this);
        this.newSku = "";
        this.chaseILineIDs = new Vector<Long>();
        this.chaseILines = new Vector<ICDILine>();
        if (ICDVerticalChase.skuGenerator == null) {
            ICDVerticalChase.skuGenerator = new ICDVerticalChaseSKUGenerator();
        }
        this.setupNamedPoints();
    }
    
    public Object clone() {
        return this.buildClone(new ICDVerticalChase(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDVerticalChase buildClone(final ICDVerticalChase icdVerticalChase) {
        super.buildClone((TransformableEntity)icdVerticalChase);
        return icdVerticalChase;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDVerticalChase(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDVerticalChase buildFrameClone(final ICDVerticalChase icdVerticalChase, final EntityObject entityObject) {
        super.buildFrameClone((TransformableEntity)icdVerticalChase, entityObject);
        return icdVerticalChase;
    }
    
    protected void calculateDimensions() {
        super.calculateDimensions();
        if (this.chaseILines.size() > 0) {
            final ICDILine icdiLine = this.chaseILines.get(0);
            this.setXDimension(icdiLine.getXDimension());
            this.setYDimension(icdiLine.getYDimension());
            this.setZDimension(icdiLine.getZDimension());
        }
    }
    
    public Vector<ICDILine> getChaseILines() {
        return this.chaseILines;
    }
    
    public void setChaseILines(final Vector<ICDILine> chaseILines) {
        this.chaseILines = chaseILines;
        if (chaseILines != null) {
            for (final ICDILine icdiLine : chaseILines) {
                if (icdiLine != null) {
                    this.chaseILineIDs.add(icdiLine.getUniqueId());
                }
            }
        }
        else {
            this.chaseILineIDs.clear();
        }
    }
    
    public static float getDepthForILines(final Vector<ICDILine> vector) {
        float min = Float.MAX_VALUE;
        final Iterator<ICDILine> iterator = vector.iterator();
        while (iterator.hasNext()) {
            min = Math.min(min, iterator.next().getLength());
        }
        return min;
    }
    
    public static float getWidthForILines(final Vector<ICDILine> vector) {
        float max = Float.MIN_VALUE;
        final Iterator<ICDILine> iterator = vector.iterator();
        while (iterator.hasNext()) {
            max = Math.max(max, iterator.next().getLength());
        }
        return max;
    }
    
    public void destroy() {
        for (final ICDILine icdiLine : this.chaseILines) {
            icdiLine.setVerticalChase(null);
            icdiLine.applyChangesForAttribute("ICD_is_Vertical_Chase", "No");
        }
        this.chaseILines.clear();
        this.chaseILineIDs.clear();
        super.destroy();
    }
    
    public void solve() {
        this.validateSelfOnILines();
        if (this.isModified()) {
            this.modifyCurrentOption();
            this.validateChildTypes();
        }
        super.solve();
        this.validateDimensions();
        this.validateShowInManufacturingReport();
        this.handleSKUGeneration();
    }
    
    private void validateShowInManufacturingReport() {
        if (this.shouldAssemble()) {
            this.applyChangesForAttribute("ShowInManufacturingReport", "1.0");
        }
        else {
            this.applyChangesForAttribute("ShowInManufacturingReport", "0");
        }
    }
    
    private void validateDimensions() {
        if (this.chaseILines.size() > 0) {
            this.applyChangesForAttribute("ICD_Vertical_Chase_Depth", Float.toString(getDepthForILines(this.chaseILines)));
            this.applyChangesForAttribute("ICD_Vertical_Chase_Width", Float.toString(getWidthForILines(this.chaseILines)));
            this.applyChangesForAttribute("ICD_Vertical_Chase_Height", Float.toString(this.chaseILines.get(0).getHeight()));
            for (final ICDILine icdiLine : this.chaseILines) {
                final Iterator iterator2 = icdiLine.getChildrenByClass((Class)ICDSegment.class, true).iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().applyChangesForAttribute("ICD_Segment_Height", (int)icdiLine.getHeight() + "");
                }
            }
        }
    }
    
    private void validateSelfOnILines() {
        final Solution mainSolution = this.getMainSolution();
        if (mainSolution != null && !mainSolution.isLoading() && mainSolution.isMainSolution()) {
            boolean b = true;
            for (final ICDILine icdiLine : this.chaseILines) {
                if (icdiLine.getChaseILines(this.chaseILines).size() == 4) {
                    icdiLine.setVerticalChase(this);
                    b = false;
                }
                else {
                    icdiLine.setVerticalChase(null);
                }
            }
            if (b) {
                this.destroy();
            }
        }
        if (mainSolution != null && mainSolution.isLoading() && this.chaseILineIDs.size() != 0) {
            for (final EntityObject o : this.getChildrenByClass((Class)BasicILine.class, true, true)) {
                if (o != null) {
                    final Iterator<Long> iterator3 = this.chaseILineIDs.iterator();
                    while (iterator3.hasNext()) {
                        if (o.getUniqueId().compareTo(iterator3.next()) == 0 && !this.chaseILines.contains(o)) {
                            this.chaseILines.add((ICDILine)o);
                            ((ICDILine)o).setVerticalChase(this);
                        }
                    }
                }
            }
        }
    }
    
    private void validateFinish() {
        String s = "M";
        String string = "";
        final String attributeValueAsString = this.getAttributeValueAsString("ICD_Vertical_Chase_Finish_Type");
        final Iterator<ICDILine> iterator = this.chaseILines.iterator();
        while (iterator.hasNext()) {
            string += ICDVerticalChase.skuGenerator.getRegularPanelFinishCode((ICDPanel)iterator.next().getFirstChildByClass((Class)ICDPanel.class, false));
        }
        if (string.contains("F")) {
            s = "F";
        }
        else if (string.contains("L")) {
            s = "L";
        }
        this.applyChangesForAttribute("ICD_Vertical_Chase_Finish_Type", s);
        if (!attributeValueAsString.equals(attributeValueAsString)) {
            this.setModified(true);
            this.modifyCurrentOption();
        }
    }
    
    public void addToCutSolution(final Vector vector, final Vector vector2, final Solution solution, final boolean b) {
        if (b) {
            final Iterator<ICDILine> iterator = this.chaseILines.iterator();
            while (iterator.hasNext()) {
                iterator.next().setVerticalChase(null);
            }
            this.chaseILines.clear();
            this.chaseILineIDs.clear();
        }
    }
    
    public static Vector<Float> getPossibleDimensionsForVerticalChase() {
        final Vector<Float> vector = new Vector<Float>();
        populatePossibleDimensionsForAttribute(vector, "ICD_Vertical_Chase_Width", true);
        populatePossibleDimensionsForAttribute(vector, "ICD_Vertical_Chase_Depth", true);
        return vector;
    }
    
    private static void populatePossibleDimensionsForAttribute(final Vector<Float> vector, final String key, final boolean b) {
        final Iterator<OptionProxyValue> iterator = Solution.getWorldAttributeProxy().get(key).getPossibleValues().iterator();
        while (iterator.hasNext()) {
            final float float1 = Float.parseFloat(iterator.next().getValue());
            vector.add(float1);
            if (b) {
                vector.add(float1);
            }
        }
    }
    
    public void setVerticalChaseILinesSelected(final boolean b, final Solution solution) {
        for (final ICDILine icdiLine : this.chaseILines) {
            if (b != icdiLine.isSelected()) {
                icdiLine.setSelected(b, solution);
            }
        }
        this.setSelected(b, solution);
    }
    
    public void setVerticalChaseSegmentsSelected(final boolean b, final Solution solution) {
        final Iterator<ICDILine> iterator = this.getChaseILines().iterator();
        while (iterator.hasNext()) {
            for (final ICDSegment icdSegment : iterator.next().getChildrenByClass((Class)ICDSegment.class, true)) {
                if (b != icdSegment.isSelected()) {
                    icdSegment.setSelected(b, solution);
                }
            }
        }
        this.setSelected(b, solution);
    }
    
    public void setVerticalChasePanelSegmentsSelected(final ICDPanelSegment icdPanelSegment, final boolean b, final Solution solution) {
        if (icdPanelSegment != null) {
            final Iterator<ICDILine> iterator = this.getChaseILines().iterator();
            while (iterator.hasNext()) {
                for (final ICDPanelSegment icdPanelSegment2 : iterator.next().getChildrenByClass((Class)ICDPanelSegment.class, true)) {
                    if (b != icdPanelSegment2.isSelected() && icdPanelSegment.isBaseSegment() == icdPanelSegment2.isBaseSegment()) {
                        icdPanelSegment2.setSelected(b, solution);
                    }
                }
            }
        }
        this.setSelected(b, solution);
    }
    
    public PANEL_TYPE getPanelType() {
        return PANEL_TYPE.VERTICAL_CHASE;
    }
    
    public ICDPanel getAPanel() {
        ICDPanel icdPanel = null;
        final Iterator<ICDILine> iterator = this.chaseILines.iterator();
        while (iterator.hasNext()) {
            icdPanel = (ICDPanel)iterator.next().getFirstChildByClass((Class)ICDPanel.class, false);
            if (icdPanel != null) {
                break;
            }
        }
        return icdPanel;
    }
    
    protected void handleSKUGeneration() {
        if (this.shouldGenerateSku()) {
            this.newSku = ICDVerticalChase.skuGenerator.generateSKU((TypeableEntity)this);
            String attributeValueAsString = null;
            if (this.currentCatalogOption != null) {
                attributeValueAsString = this.currentCatalogOption.getAttributeValueAsString("PN");
            }
            if (this.currentCatalogOption == null || attributeValueAsString == null || !attributeValueAsString.equals(this.newSku)) {
                final Catalog catalog = Solution.getCatalogs().get("ICI_ICD");
                try {
                    if (catalog != null && catalog.getBroker().isAvailable(this.newSku)) {
                        final OptionObject currentOption = this.currentOption;
                        this.currentCatalogOption = (OptionObject)OptionObject.loadCatalogOption(catalog, this.newSku, true);
                        if (this.currentCatalogOption == null) {
                            System.err.println("No ICD part found for SKU: " + this.newSku);
                        }
                        this.handleDynamicAttributes();
                        this.createNewAttribute("Base_SKU", this.newSku);
                    }
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    protected boolean shouldGenerateSku() {
        return true;
    }
    
    public String getSKU() {
        final String sku = super.getSKU();
        if (this.currentCatalogOption != null) {
            return this.getAttributeValueAsString("PN");
        }
        return sku;
    }
    
    public void handleDynamicAttributes() {
        final HashMap attributeList = this.currentCatalogOption.getAttributeList();
        if (attributeList != null) {
            for (final String s : attributeList.keySet()) {
                final Attribute attribute = (Attribute)attributeList.get(s);
                final AttributeProxy attributeProxy = Solution.getWorldAttributeProxy().get(s);
                if (attributeProxy != null && attribute != null) {
                    final Attribute attributeObject = Attribute.createAttributeObject(attributeProxy.getAttributeClass(), s, attribute.getValueAsString());
                    if (attributeObject == null) {
                        continue;
                    }
                    this.catalogAttributeHashMap.put(s, attributeObject);
                }
            }
        }
    }
    
    public boolean containsAttributeKey(final String s) {
        return super.containsAttributeKey(s) || this.catalogAttributeHashMap.containsKey((Object)s);
    }
    
    public Attribute getAttributeObject(final String s) {
        Attribute attribute = this.getAttributeHashMap().get((Object)s);
        if (attribute == null && this.getCurrentOption() != null) {
            attribute = this.getCurrentOption().getAttributeObject(s);
        }
        if (attribute == null) {
            attribute = this.catalogAttributeHashMap.get((Object)s);
        }
        if (attribute == null || attribute.isNULL()) {
            final AttributeProxy attributeProxyByKey = AttributeProxy.getAttributeProxyByKey(s);
            if (attributeProxyByKey != null && attributeProxyByKey.isAutoInherited()) {
                final EntityObject parentEntity = this.getParentEntity();
                if (parentEntity != null) {
                    return parentEntity.getAttributeObject(s);
                }
            }
        }
        return attribute;
    }
    
    public String getDescription() {
        String s;
        if (this.currentCatalogOption != null) {
            s = this.currentCatalogOption.getDescription();
            if ("ICD_Vertical_Chase_Error_Option".equalsIgnoreCase(this.currentCatalogOption.getId())) {
                s = s + "-" + this.newSku;
            }
        }
        else {
            s = super.getDescription();
        }
        return s + " (Actual Size " + Math.round(this.getHeight()) + "h " + (Math.round(this.getWidth()) + 1) + "w)";
    }
    
    public String getQuoteDescription() {
        return this.getDescription();
    }
    
    public void getRequiredCatalogChildren(final Vector<RequiredChildTypeContainer> vector) {
        if (this.isCatalogPartPresent()) {
            final Vector<RequiredChildTypeContainer> vector2 = new Vector<RequiredChildTypeContainer>();
            for (final RequiredChildTypeContainer e : vector) {
                if (e.getType() != null && "CAT".equals(e.getType().getId().substring(0, 3))) {
                    vector2.add(e);
                }
            }
            final Iterator<RequiredChildTypeContainer> iterator2 = vector2.iterator();
            while (iterator2.hasNext()) {
                vector.remove(iterator2.next());
            }
            for (final RequiredChildTypeContainer requiredChildTypeContainer : this.currentCatalogOption.getTypeKeyList()) {
                if (!vector.contains(requiredChildTypeContainer)) {
                    vector.add(requiredChildTypeContainer);
                }
            }
        }
        else {
            super.getRequiredCatalogChildren((Vector)vector);
        }
    }
    
    public void addAdditonalPaintableEntities(final List<AssemblyPaintable> list) {
        list.add(this);
    }
    
    public boolean shouldICDMakePreAssembledReport() {
        return false;
    }
    
    public boolean checkAssembledForAssemblyElevation() {
        boolean b = false;
        final Iterator<ICDILine> iterator = this.getChaseILines().iterator();
        while (iterator.hasNext()) {
            final Iterator iterator2 = iterator.next().getChildrenByClass((Class)AssembleParent.class, true, true).iterator();
            while (iterator2.hasNext()) {
                b |= ((EntityObject)iterator2.next()).getAttributeValueAsBoolean("isAssembled", false);
            }
        }
        return b;
    }
    
    protected void writeXMLFields(final XMLWriter xmlWriter, final PersistentFileManager.FileWriter fileWriter) throws IOException {
        super.writeXMLFields(xmlWriter, fileWriter);
        xmlWriter.writeTextElement("ChaseILineSize", this.chaseILineIDs.size() + "");
        for (int i = 0; i < this.chaseILineIDs.size(); ++i) {
            xmlWriter.writeTextElement("chase" + i, this.chaseILineIDs.get(i) + "");
        }
    }
    
    protected void setFieldInfoFromXML(final Node node, final DefaultMutableTreeNode defaultMutableTreeNode, final PersistentFileManager.FileReader fileReader) {
        super.setFieldInfoFromXML(node, defaultMutableTreeNode, fileReader);
        for (int int1 = Integer.parseInt(this.getChildElementValue("ChaseILineSize", node)), i = 0; i < int1; ++i) {
            this.chaseILineIDs.add(Long.parseLong(this.getChildElementValue("chase" + i, node)));
        }
    }
    
    private ICDILine getChaseReferenceILine() {
        ICDILine icdiLine = null;
        if (this.chaseILines.size() > 0) {
            icdiLine = this.chaseILines.get(0);
            for (final ICDILine icdiLine2 : this.chaseILines) {
                if (icdiLine2.getWidth() > icdiLine.getWidth()) {
                    icdiLine = icdiLine2;
                }
            }
        }
        return icdiLine;
    }
    
    public Vector<Ice2DPaintableNode> getAssemblyIcons(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        final Vector<Ice2DPaintableNode> vector = new Vector<Ice2DPaintableNode>();
        final ICDILine chaseReferenceILine = this.getChaseReferenceILine();
        for (final BasicExtrusion basicExtrusion : ((EntityObject)chaseReferenceILine.getIntersections().get(0)).getChildrenByClass((Class)BasicExtrusion.class, true, true)) {
            vector.add((Ice2DPaintableNode)ICDAssemblyElevationUtilities.createAssemblyDimensionTextNode(this, new Vector3f(0.0f, 0.0f, basicExtrusion.getBasePointWorldSpace().z + basicExtrusion.getZDimension() / 2.0f), Math.round(basicExtrusion.getZDimension()) + "", new Vector3f(-1.5707964f, 3.1415927f, -1.5707964f)));
        }
        final Ice2DTextNode assemblyDimensionTextNode = ICDAssemblyElevationUtilities.createAssemblyDimensionTextNode(this, new Vector3f(chaseReferenceILine.getWidth() / 2.0f, 0.0f, chaseReferenceILine.getHeight()), Math.round(chaseReferenceILine.getWidth() - 1.0f) + "", new Vector3f(-1.5707964f, 3.1415927f, 3.1415927f));
        vector.add((Ice2DPaintableNode)ICDAssemblyElevationUtilities.createAssemblyElevationImageNode(this, new Vector3f(), new Vector3f(-1.5707964f, 0.0f, 0.0f)));
        vector.add((Ice2DPaintableNode)assemblyDimensionTextNode);
        return vector;
    }
    
    public List<IceOutputNode> getPlotOutputNodes(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        return null;
    }
    
    public boolean shouldDrawAssembly() {
        return true;
    }
    
    public boolean isAssembled() {
        return true;
    }
    
    public void modifyCurrentOption() {
        this.validateIndicators();
        super.modifyCurrentOption();
    }
    
    protected void validateIndicators() {
        this.createNewAttribute("ICD_Vertical_Chase_Is_Stacked", this.isStacked() + "");
        this.createNewAttribute("ICD_Vertical_Chase_Is_Split", this.isSplit() + "");
        this.createNewAttribute("ICD_Vertical_Chase_Dimensions", this.getChaseDimension());
    }
    
    public String getChaseDimension() {
        int round = Integer.MAX_VALUE;
        int round2 = Integer.MIN_VALUE;
        for (final ICDILine icdiLine : this.chaseILines) {
            if (icdiLine.getLength() < round) {
                round = Math.round(icdiLine.getLength());
            }
            if (icdiLine.getLength() > round2) {
                round2 = Math.round(icdiLine.getLength());
            }
        }
        return round2 + "x" + round;
    }
    
    public boolean isStacked() {
        return this.getStackingSegment() != null;
    }
    
    private PanelSegmentInterface getStackingSegment() {
        final ICDILine chaseReferenceILine = this.getChaseReferenceILine();
        if (chaseReferenceILine != null) {
            final Iterator iterator = chaseReferenceILine.getSegments().iterator();
            while (iterator.hasNext()) {
                final PanelSubILineInterface panelSubILineInterface = (PanelSubILineInterface)((EntityObject)iterator.next()).getChildByInterface((Class)PanelSubILineInterface.class);
                if (panelSubILineInterface instanceof BasicPanelSubILine && ((BasicPanelSubILine)panelSubILineInterface).getStackingSegment() != null) {
                    return ((BasicPanelSubILine)panelSubILineInterface).getStackingSegment();
                }
            }
        }
        return null;
    }
    
    public boolean isSplit() {
        return this.getPanelWithSplit() != null;
    }
    
    private ICDPanel getPanelWithSplit() {
        final ICDILine chaseReferenceILine = this.getChaseReferenceILine();
        if (chaseReferenceILine != null) {
            for (final ICDPanel icdPanel : chaseReferenceILine.getChildrenByClass((Class)ICDPanel.class, true, true)) {
                if (icdPanel.withHorizontalInnerExtrusion()) {
                    return icdPanel;
                }
            }
        }
        return null;
    }
    
    public void setupNamedPoints() {
        this.addNamedPoint("CADELEXP1", new Point3f());
        this.addNamedPoint("CADELEXP2", new Point3f());
        this.addNamedPoint("CADELEXP3", new Point3f());
        this.addNamedPoint("CAD_TAG", new Point3f(0.0f, 0.0f, 0.0f));
    }
    
    public void calculateNamedPoints() {
        final int n = 5;
        if (this.isSplit()) {
            this.getNamedPointLocal("CADELEXP1").set(0.0f, (float)n, 15.0f);
            this.getNamedPointLocal("CADELEXP2").set(0.0f, (float)n, 50.0f);
        }
        else {
            this.getNamedPointLocal("CADELEXP1").set(0.0f, (float)n, 35.0f);
            this.getNamedPointLocal("CADELEXP2").set(0.0f, (float)n, 0.0f);
        }
        this.getNamedPointLocal("CADELEXP3").set(0.0f, (float)n, 95.0f);
        this.getNamedPointLocal("CAD_TAG").set(this.getXDimension() + 2.0f, -1.0f, this.getZDimension() + 6.0f);
    }
    
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        final Vector<String> vector = new Vector<String>();
        final String tags = this.getTags();
        if (tags != null) {
            vector.add("TX:SS(" + tags + ":" + 3 + ":" + 0.0 + ":" + 2 + ":CAD_TAG)");
        }
        return vector;
    }
    
    public void populateCompareNode(final Class clazz, final CompareNode compareNode) {
        super.populateCompareNode(clazz, compareNode);
        compareNode.addCompareValue("height", (Object)this.getHeight());
    }
    
    public void drawCad(final ICadTreeNode cadTreeNode, final int n) {
        this.drawChildrenCad(cadTreeNode, n);
        this.textNode = this.getCadOutputTextNode(cadTreeNode);
    }
    
    private ICadTextNode getCadOutputTextNode(final ICadTreeNode cadTreeNode) {
        final String attributeValueAsString = this.getAttributeValueAsString("Base_SKU");
        if (attributeValueAsString != null) {
            ICDILine chaseReferenceILine = this.getChaseReferenceILine();
            for (final ICDILine icdiLine : this.getChaseILines()) {
                if (icdiLine.getBasePointWorldSpace().y < chaseReferenceILine.getBasePointWorldSpace().y) {
                    chaseReferenceILine = icdiLine;
                }
            }
            final Point3f point3f2;
            final Point3f point3f = point3f2 = (Point3f)chaseReferenceILine.getBasePointWorldSpace().clone();
            point3f2.x -= attributeValueAsString.length() / 2.0f * 2.0f;
            final Point3f point3f3 = point3f;
            point3f3.y -= 2.0f + chaseReferenceILine.getYDimension() / 2.0f;
            return ICDUtilities.drawCadText(this, this.textNode, cadTreeNode, attributeValueAsString, point3f, 1, 0.0f);
        }
        return null;
    }
    
    public void drawIceCadDotNet(final int n, final IceCadNodeContainer iceCadNodeContainer, final IceCadIceApp iceCadIceApp) {
        this.drawChildrenIceCadDotNet(n, iceCadNodeContainer, iceCadIceApp);
        this.iceTextNode = this.getIceCadOutputTextNode(iceCadNodeContainer);
    }
    
    private IceCadMTextNode getIceCadOutputTextNode(final IceCadNodeContainer iceCadNodeContainer) {
        final String attributeValueAsString = this.getAttributeValueAsString("Base_SKU");
        if (attributeValueAsString != null) {
            ICDILine chaseReferenceILine = this.getChaseReferenceILine();
            for (final ICDILine icdiLine : this.getChaseILines()) {
                if (icdiLine.getBasePointWorldSpace().y < chaseReferenceILine.getBasePointWorldSpace().y) {
                    chaseReferenceILine = icdiLine;
                }
            }
            final Point3f point3f2;
            final Point3f point3f = point3f2 = (Point3f)chaseReferenceILine.getBasePointWorldSpace().clone();
            point3f2.x -= attributeValueAsString.length() / 2.0f * 2.0f;
            final Point3f point3f3 = point3f;
            point3f3.y -= 2.0f + chaseReferenceILine.getYDimension() / 2.0f;
            return ICDUtilities.drawIceCadTextDotNet(iceCadNodeContainer, this.iceTextNode, attributeValueAsString, point3f, 0.0f);
        }
        return null;
    }
    
    private IceCadMTextNode getCadOutputTextNodeForProxyEntityDotNet(final IceCadNodeContainer iceCadNodeContainer, final IceCadMTextNode iceCadMTextNode, final TransformableEntity transformableEntity, final IceCadCompositeBlock iceCadCompositeBlock) {
        final String attributeValueAsString = this.getAttributeValueAsString("Base_SKU");
        if (attributeValueAsString != null) {
            ICDILine chaseReferenceILine = this.getChaseReferenceILine();
            for (final ICDILine icdiLine : this.getChaseILines()) {
                if (icdiLine.getBasePointWorldSpace().y < chaseReferenceILine.getBasePointWorldSpace().y) {
                    chaseReferenceILine = icdiLine;
                }
            }
            final Point3f point3f2;
            final Point3f point3f = point3f2 = (Point3f)chaseReferenceILine.getBasePointWorldSpace().clone();
            point3f2.x -= attributeValueAsString.length() / 2.0f * 2.0f;
            final Point3f point3f3 = point3f;
            point3f3.y -= 2.0f + chaseReferenceILine.getYDimension() / 2.0f;
            return ICDUtilities.drawIceCadTextForProxyEntityDotNet(iceCadNodeContainer, iceCadMTextNode, transformableEntity, iceCadCompositeBlock, attributeValueAsString, point3f, 0.0f);
        }
        return null;
    }
    
    public void drawIceCadForProxyEntityDotNet(final IceCadNodeContainer iceCadNodeContainer, final TransformableEntity transformableEntity, final IceCadCompositeBlock iceCadCompositeBlock, final int n, final SolutionSetting solutionSetting) {
        this.drawChildrenIceCadForProxyEntityDotNet(iceCadNodeContainer, transformableEntity, iceCadCompositeBlock, n, solutionSetting);
        this.textNodeNetForProxyEntity = this.getCadOutputTextNodeForProxyEntityDotNet(iceCadNodeContainer, this.textNodeNetForProxyEntity, transformableEntity, iceCadCompositeBlock);
    }
    
    public void destroyCad() {
        if (this.textNode != null) {
            this.textNode.setScheduledAction(1);
            this.textNode = null;
        }
        if (this.iceTextNode != null) {
            this.iceTextNode.erase();
        }
        if (this.textNodeNetForProxyEntity != null) {
            this.textNodeNetForProxyEntity.erase();
        }
        super.destroyCad();
    }
    
    public void finalDestroyCad() {
        if (this.iceTextNode != null) {
            this.iceTextNode.destroy();
            this.iceTextNode = null;
        }
        if (this.textNodeNetForProxyEntity != null) {
            this.textNodeNetForProxyEntity.destroy();
            this.textNodeNetForProxyEntity = null;
        }
        super.finalDestroyCad();
    }
    
    public void calculate() {
        super.calculate();
        final ICDILine chaseReferenceILine = this.getChaseReferenceILine();
        if (chaseReferenceILine != null) {
            this.setBasePoint((Point3f)chaseReferenceILine.getBasePointWorldSpace().clone());
            this.setRotation(chaseReferenceILine.getRotationWorldSpace());
        }
    }
    
    public List<IceOutputNode> getPlotOutputNodes() {
        final ArrayList<IceOutputTextNode> list = (ArrayList<IceOutputTextNode>)new ArrayList<IceOutputNode>();
        final ICadTextNode cadOutputTextNode = this.getCadOutputTextNode(null);
        if (cadOutputTextNode != null) {
            final Matrix4f matrix4f = new Matrix4f();
            matrix4f.setIdentity();
            list.add(new IceOutputTextNode(cadOutputTextNode, matrix4f));
        }
        return (List<IceOutputNode>)list;
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
    
    public boolean shouldIncludeExtraIndirectAssemblyParts() {
        return false;
    }
    
    public HashSet<AssembleParent> getExternalAssemblyParts() {
        final HashSet<Object> set = (HashSet<Object>)new HashSet<AssembleParent>();
        set.addAll(this.chaseILines);
        return (HashSet<AssembleParent>)set;
    }
    
    public Collection<EntitySpaceCompareNodeWrapper> getSpaceCompareNodeWrappers() {
        final LinkedList<Object> list = (LinkedList<Object>)new LinkedList<EntitySpaceCompareNodeWrapper>();
        final Iterator<AssembleParent> iterator = this.getExternalAssemblyParts().iterator();
        while (iterator.hasNext()) {
            list.addAll(iterator.next().getSpaceCompareNodeWrappers());
        }
        return (Collection<EntitySpaceCompareNodeWrapper>)list;
    }
    
    public HashSet<TypeableEntity> getAssembledChildrenForManReport() {
        final HashSet<Object> set = (HashSet<Object>)new HashSet<TypeableEntity>();
        final Iterator<AssembleParent> iterator = this.getExternalAssemblyParts().iterator();
        while (iterator.hasNext()) {
            set.addAll(iterator.next().getAssembledChildrenForManReport());
        }
        return (HashSet<TypeableEntity>)set;
    }
    
    public boolean shouldAssemble() {
        boolean b = false;
        final Iterator<ICDILine> iterator = this.chaseILines.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().getAttributeValueAsBoolean("shouldAssemble", false)) {
                return false;
            }
            b = true;
        }
        return b;
    }
    
    protected String getTags() {
        return ICDUtilities.getTags((EntityObject)this);
    }
    
    public void draw2D(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        super.draw2D(n, ice2DContainer, solutionSetting);
        this.refreshTagNode(n, ice2DContainer, solutionSetting);
    }
    
    public void drawChildrenCadElevation(final ElevationEntity elevationEntity, final ICadBlockNode cadBlockNode, final int n, final SolutionSetting solutionSetting) {
    }
    
    protected boolean shouldDrawInCadElevationCluster() {
        return true;
    }
    
    public String getManufacturingReportMaterialOptID() {
        return "Option0";
    }
    
    public void addManufacturingInfoToTreeMap(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addManufacturingInfoToTreeMap(treeMap, (ManufacturingReportable)this);
    }
    
    public String getDescriptionForManufacturingReport() {
        return "Assembled vertical chase";
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
        treeMap.put("Depth", this.getAttributeValueAsString("ICD_Vertical_Chase_Depth", getDepthForILines(this.chaseILines) + ""));
        treeMap.put("Width", this.getAttributeValueAsString("ICD_Vertical_Chase_Width", getWidthForILines(this.chaseILines) + ""));
        treeMap.put("Height", this.getHeight() + "");
    }
    
    static {
        ICDVerticalChase.skuGenerator = null;
    }
    
    enum PANEL_TYPE
    {
        VERTICAL_CHASE {
            @Override
            public String getPanelName() {
                return "Vertical Chase Panel";
            }
        };
        
        public abstract String getPanelName();
    }
}
