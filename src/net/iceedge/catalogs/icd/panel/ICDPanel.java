// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.icecore.basemodule.interfaces.panels.SideGroupInterface;
import java.util.HashSet;
import net.iceedge.catalogs.icd.intersection.ICDPost;
import net.iceedge.catalogs.icd.intersection.ICDChaseMidConnectorContainer;
import net.dirtt.icelib.main.CatalogOptionObject;
import net.iceedge.catalogs.icd.elevation.isometric.ICDIsometricAssemblyElevationEntity;
import net.dirtt.icelib.main.ElevationEntity;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelSubILineInterface;
import net.dirtt.icebox.iceoutput.core.IceOutputLayerNode;
import net.iceedge.icecore.basemodule.interfaces.lightweight.Paintable;
import net.dirtt.icebox.iceoutput.core.IceOutputTextNode;
import net.dirtt.icelib.main.SolutionSetting;
import net.iceedge.icecore.icecad.ice.tree.IceCadCompositeBlock;
import net.iceedge.icecore.icecad.ice.IceCadIceApp;
import net.iceedge.icecore.icecad.ice.tree.IceCadNodeContainer;
import net.iceedge.catalogs.icd.ICDUtilities;
import net.dirtt.icecad.cadtree.ICadTreeNode;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import net.dirtt.icebox.canvas2d.Ice2DTextNode;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationUtilities;
import javax.vecmath.Vector3f;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import javax.vecmath.Matrix4f;
import net.dirtt.icelib.main.TransformableEntity;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelInterface;
import net.dirtt.icelib.report.compare.CompareNode;
import net.dirtt.icelib.report.Report;
import net.iceedge.icecore.basemodule.baseclasses.utilities.IndicatorUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import net.iceedge.icebox.utilities.Node;
import net.dirtt.utilities.PersistentFileManager;
import net.dirtt.xmlFiles.XMLWriter;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelSegmentInterface;
import java.util.ArrayList;
import java.util.List;
import net.dirtt.icelib.main.RequiredChildTypeContainer;
import java.util.HashMap;
import net.dirtt.icelib.main.attributes.proxy.AttributeProxy;
import net.dirtt.icelib.main.attributes.Attribute;
import javax.vecmath.Tuple3f;
import net.iceedge.icecore.basemodule.interfaces.GeneralIntersectionInterface;
import net.iceedge.icecore.basemodule.interfaces.IntersectionArmInterface;
import net.iceedge.catalogs.icd.ICDSegmentFilter;
import net.iceedge.catalogs.icd.ICDSegment;
import net.iceedge.icecore.basemodule.interfaces.ILineInterfaceFilter;
import java.awt.geom.Line2D;
import net.iceedge.icecore.basemodule.interfaces.ILineInterface;
import java.awt.geom.Point2D;
import icd.warnings.WarningReason4020;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicTile;
import net.iceedge.catalogs.icd.electrical.ICDElectricalCable;
import java.util.Vector;
import net.dirtt.icelib.main.LightWeightTypeObject;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicFrame;
import net.iceedge.catalogs.icd.ICDILineFilter;
import net.iceedge.catalogs.icd.ICDILine;
import net.iceedge.icecore.basemodule.finalclasses.GeneralSnapSet;
import net.iceedge.catalogs.icd.worksurfaces.ICDBasicWorksurface;
import net.dirtt.utilities.TypeIterator;
import net.iceedge.icecore.basemodule.interfaces.AssembleParent;
import net.dirtt.utilities.TypeFilter;
import net.dirtt.icelib.main.snapping.SnapSetEntityFilter;
import net.dirtt.icelib.main.snapping.SnapSetEntity;
import java.util.Collection;
import net.dirtt.icelib.ui.attribute.explorer.ui.entity.PossibleValue;
import net.iceedge.icecore.basemodule.interfaces.panels.BottomExtrusionInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.TopExtrusionInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.TileInterface;
import javax.vecmath.Point3f;
import net.iceedge.icecore.basemodule.interfaces.panels.InnerExtrusionSetInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.ExtrusionGroupInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.FrameInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.StickLineInterface;
import net.iceedge.icecore.basemodule.baseclasses.panels.ExtrusionPoint;
import java.io.IOException;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.Catalog;
import net.dirtt.icelib.main.TypeableEntity;
import java.util.Iterator;
import net.dirtt.utilities.MathUtilities;
import net.dirtt.icelib.main.attributes.proxy.OptionAttributeProxy;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icecad.cadtree.ICadTextNode;
import net.iceedge.icecore.icecad.ice.tree.IceCadMTextNode;
import net.dirtt.icelib.undo.iceobjects.AttributeHashMapWithUndo;
import net.dirtt.icelib.main.OptionObject;
import org.apache.log4j.Logger;
import net.dirtt.icelib.report.CustomReportableEntity;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icebox.iceoutput.core.PlotPaintable;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintable;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintableRoot;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicPanel;

public class ICDPanel extends BasicPanel implements AssemblyPaintableRoot, AssemblyPaintable, PlotPaintable, ICDManufacturingReportable, CustomReportableEntity
{
    private static final long serialVersionUID = -8007293082232524603L;
    private static Logger logger;
    protected static ICDPanelSKUGenerator skuGenerator;
    protected float currentSplitLocationFromStart;
    protected float currentSplitLocationFromEnd;
    protected OptionObject currentCatalogOption;
    private AttributeHashMapWithUndo catalogAttributeHashMap;
    protected String newSku;
    private IceCadMTextNode iceTextNode;
    private IceCadMTextNode textNodeNetForProxyEntity;
    public static final float heightFromFloor = 0.75f;
    public static final float splitHeight = 1.0f;
    public static float SLOPED_STACK_ANGLEDX_42;
    public static float SLOPED_STACK_SHORTY_42;
    public static float SLOPED_STACK_ANGLEDX_24;
    public static float SLOPED_STACK_SHORTY_24;
    private ICadTextNode textNode;
    
    public ICDPanel(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.catalogAttributeHashMap = new AttributeHashMapWithUndo((EntityObject)this);
        this.newSku = "";
        if (ICDPanel.skuGenerator == null) {
            ICDPanel.skuGenerator = new ICDPanelSKUGenerator();
        }
    }
    
    public ICDPanel buildClone(final ICDPanel icdPanel, final EntityObject entityObject) {
        super.buildClone((BasicPanel)icdPanel, entityObject);
        return icdPanel;
    }
    
    public Object clone() {
        return this.buildClone(new ICDPanel(this.getId(), this.currentType, this.currentOption), null);
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildClone(new ICDPanel(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    protected void validateIndicators() {
        super.validateIndicators();
        if (!this.isCorePanel()) {
            if (!this.isValidSize()) {
                this.applyChangesForAttribute("ICD_Sloped_Panel", "No");
                if (this.containsAttributeKey("ICD_Sloped_Visible")) {
                    final OptionAttributeProxy optionAttributeProxy = (OptionAttributeProxy)this.getAttributeObject("ICD_Sloped_Visible").getAttributeProxy();
                    optionAttributeProxy.getPossibleValues().clear();
                    optionAttributeProxy.addPossibleValue("No");
                }
            }
            else if (this.containsAttributeKey("ICD_Sloped_Visible")) {
                final OptionAttributeProxy optionAttributeProxy2 = (OptionAttributeProxy)this.getAttributeObject("ICD_Sloped_Visible").getAttributeProxy();
                optionAttributeProxy2.getPossibleValues().clear();
                optionAttributeProxy2.addPossibleValue("Yes");
                optionAttributeProxy2.addPossibleValue("No");
            }
        }
        else if (this.isValidSize()) {
            if (this.containsAttributeKey("ICD_Door_Visible")) {
                final OptionAttributeProxy optionAttributeProxy3 = (OptionAttributeProxy)this.getAttributeObject("ICD_Door_Visible").getAttributeProxy();
                optionAttributeProxy3.getPossibleValues().clear();
                optionAttributeProxy3.addPossibleValue("No");
                optionAttributeProxy3.addPossibleValue("Yes");
            }
        }
        else {
            this.applyChangesForAttribute("ICD_Door_Panel", "No");
            if (this.containsAttributeKey("ICD_Door_Visible")) {
                final OptionAttributeProxy optionAttributeProxy4 = (OptionAttributeProxy)this.getAttributeObject("ICD_Door_Visible").getAttributeProxy();
                optionAttributeProxy4.getPossibleValues().clear();
                optionAttributeProxy4.addPossibleValue("No");
            }
        }
    }
    
    public boolean withHorizontalInnerExtrusion() {
        return "Yes".equals(this.getAttributeValueAsString("With_Horizontal_InnerExtrusion")) && this.getHeight() > this.getWorksurfaceHeight() + 1.0f;
    }
    
    public void solve() {
        final float height = this.getHeight();
        this.validateIndicators();
        final String anObject = (this.currentCatalogOption == null) ? "NULL" : this.currentCatalogOption.getId();
        super.solve();
        this.validateChaseVerticalSplit();
        if (this.validateSplit(height)) {
            super.solve();
        }
        this.handleSKUGeneration();
        if (!((this.currentCatalogOption == null) ? "NULL" : this.currentCatalogOption.getId()).equals(anObject)) {
            super.solve();
        }
    }
    
    protected void validateChaseVerticalSplit() {
        if (!this.shouldBreakChaseVertically() && this.getPhysicalFrame() != null) {
            ICDInternalExtrusion icdInternalExtrusion = null;
            for (final ICDInternalExtrusion icdInternalExtrusion2 : this.getChildrenByClass(ICDInternalExtrusion.class, true)) {
                if (!icdInternalExtrusion2.isVertical() && icdInternalExtrusion2.getCurrentOption().getId().equals("ICD_Extrusion_Special_With_2Joints")) {
                    icdInternalExtrusion = icdInternalExtrusion2;
                    break;
                }
            }
            if (icdInternalExtrusion != null) {
                this.removeBreakTileByChaseVertical(icdInternalExtrusion.getAttributeValueAsFloat("breakLocation") + 1.0f, true);
                this.removeBreakTileByChaseVertical(icdInternalExtrusion.getAttributeValueAsFloat("breakLocation2") + 1.0f, true);
            }
            else {
                if (!MathUtilities.isSameFloat(this.currentSplitLocationFromStart, 0.0f, 0.2f)) {
                    this.removeBreakTileByChaseVertical(this.currentSplitLocationFromStart, true);
                }
                if (!MathUtilities.isSameFloat(this.currentSplitLocationFromEnd, 0.0f, 0.2f)) {
                    this.removeBreakTileByChaseVertical(this.currentSplitLocationFromEnd, false);
                }
            }
        }
    }
    
    public void modifyCurrentOption() {
        super.modifyCurrentOption();
    }
    
    public int getQuoteReportLevel() {
        return this.isStackPanel() ? -1 : super.getQuoteReportLevel();
    }
    
    protected void handleSKUGeneration() {
        if (this.shouldGenerateSku()) {
            this.newSku = ICDPanel.skuGenerator.generateSKU((TypeableEntity)this);
            String attributeValueAsString = null;
            if (this.currentCatalogOption != null) {
                attributeValueAsString = this.currentCatalogOption.getAttributeValueAsString("PN");
            }
            if (this.currentCatalogOption == null || attributeValueAsString == null || !attributeValueAsString.equals(this.newSku)) {
                final Catalog catalog = Solution.getCatalogs().get("ICI_ICD");
                try {
                    if (catalog != null && catalog.getBroker().isAvailable(this.newSku)) {
                        this.currentCatalogOption = (OptionObject)OptionObject.loadCatalogOption(catalog, this.newSku, true);
                        if (this.currentCatalogOption == null) {
                            System.err.println("No ICD part found for SKU: " + this.newSku);
                        }
                        this.handleDynamicAttributes();
                        this.createNewAttribute("Base_SKU", this.newSku);
                    }
                    else {
                        this.currentCatalogOption = Solution.getWorldOptions().get("ICD_Panel_Error_Option");
                        this.handleDynamicAttributes();
                        this.createNewAttribute("Base_SKU", "NA");
                    }
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    protected boolean shouldGenerateSku() {
        return !this.isSlopedPanel() && !this.isDoorPanel();
    }
    
    protected boolean validateSplit(final float n) {
        boolean b = false;
        if (!this.isUnderChase()) {
            if (this.withHorizontalInnerExtrusion() && this.isCorePanel()) {
                b = this.horizontalSplit(this.getSplitLocation());
            }
            else {
                b = this.removeSplit(this.getSplitLocation());
            }
        }
        boolean b2;
        if (this.isSuspendedChaseHorizontalSplit()) {
            if (this.isUnderChase()) {
                this.moveHorizontalExtrusion(n - 1.0f - (this.getSuspendedOffset() - 1.0f), this.getHeight() - n);
            }
            b2 = (this.horizontalSplit(this.getSuspendedChaseLocation()) || b);
        }
        else {
            b2 = (this.removeSplit(this.getSuspendedChaseLocation()) || b);
        }
        return b2;
    }
    
    private boolean removeSplit(final float n) {
        boolean b = false;
        final FrameInterface physicalFrame = this.getPhysicalFrame();
        final ExtrusionGroupInterface extrusionGroup = this.getExtrusionGroup();
        if (extrusionGroup != null && physicalFrame != null) {
            final InnerExtrusionSetInterface extrusion = extrusionGroup.findExtrusionAt(new ExtrusionPoint(1, n, 0, (StickLineInterface)physicalFrame.getStartExtrusion()), new ExtrusionPoint(1, n, 0, (StickLineInterface)physicalFrame.getEndExtrusion()));
            if (extrusion != null) {
                extrusion.deleteAnyLinkedExtrusion();
                b = true;
                extrusion.deleteForBothSides();
                this.setChildrenModified(true);
            }
        }
        return b;
    }
    
    private boolean horizontalSplit(final float n) {
        boolean b = false;
        final FrameInterface physicalFrame = this.getPhysicalFrame();
        final ExtrusionGroupInterface extrusionGroup = this.getExtrusionGroup();
        if (extrusionGroup != null && physicalFrame != null && extrusionGroup.findExtrusionAt(new ExtrusionPoint(1, n, 0, (StickLineInterface)physicalFrame.getStartExtrusion()), new ExtrusionPoint(1, n, 0, (StickLineInterface)physicalFrame.getEndExtrusion())) == null) {
            b = true;
            final TileInterface tile = this.getTile(0, 0);
            if (tile != null) {
                tile.setModified(true);
                tile.horizontalSplit(1, new Point3f(0.0f, n, 0.0f), new Point3f(this.getXDimension(), n, 0.0f), this.getHorizontalSplitExtrusionType(), false, false);
            }
            final TileInterface tile2 = this.getTile(-1, 0);
            if (tile2 != null) {
                tile2.setModified(true);
                tile2.horizontalSplit(1, new Point3f(0.0f, n, 0.0f), new Point3f(this.getXDimension(), n, 0.0f), this.getHorizontalSplitExtrusionType(), false, false);
            }
            final TileInterface tile3 = this.getTile(1, 0);
            if (tile3 != null) {
                tile3.setModified(true);
                tile3.horizontalSplit(1, new Point3f(0.0f, n, 0.0f), new Point3f(this.getXDimension(), n, 0.0f), this.getHorizontalSplitExtrusionType(), false, false);
            }
        }
        return b;
    }
    
    protected TypeObject getHorizontalSplitExtrusionType() {
        final String attributeValueAsString = this.getAttributeValueAsString("Split_InnerExtrusion_Type");
        if (attributeValueAsString != null) {
            return Solution.typeObjectByName(attributeValueAsString);
        }
        return Solution.typeObjectByName("ICD_InnerExtrusionSet_Type");
    }
    
    public float getSplitLocation() {
        if (this.isUnderChase()) {
            return this.getHeight() - 1.0f;
        }
        return this.getAttributeValueAsFloat("ICD_Height_From_Floor") - 3.0f;
    }
    
    public void breakHorizontalExtrusion(final float n, final boolean b, final boolean b2, final boolean b3) {
        final FrameInterface physicalFrame = this.getPhysicalFrame();
        final ExtrusionGroupInterface extrusionGroup = this.getExtrusionGroup();
        if (extrusionGroup != null && physicalFrame != null) {
            final InnerExtrusionSetInterface extrusion = extrusionGroup.findExtrusionAt(new ExtrusionPoint(1, this.getSplitLocation(), 0, (StickLineInterface)physicalFrame.getStartExtrusion()), new ExtrusionPoint(1, this.getSplitLocation(), 0, (StickLineInterface)physicalFrame.getEndExtrusion()));
            if (extrusion instanceof ICDInnerExtrusionSet) {
                ((ICDInnerExtrusionSet)extrusion).breakHorizontalExtrusion(n, b);
            }
            if (b2) {
                final TopExtrusionInterface topExtrusion = physicalFrame.getTopExtrusion();
                if (topExtrusion instanceof ICDTopExtrusion) {
                    ((ICDTopExtrusion)topExtrusion).breakHorizontalExtrusion(n + 0.5f, b);
                }
            }
            if (this.isUnderChase()) {
                final TopExtrusionInterface topExtrusion2 = physicalFrame.getTopExtrusion();
                if (topExtrusion2 instanceof ICDTopExtrusion) {
                    ((ICDTopExtrusion)topExtrusion2).breakHorizontalExtrusion(n + 0.5f, b);
                    topExtrusion2.solve();
                }
            }
            if (b3) {
                final BottomExtrusionInterface bottomExtrusion = physicalFrame.getBottomExtrusion();
                if (bottomExtrusion instanceof ICDBottomExtrusion) {
                    ((ICDBottomExtrusion)bottomExtrusion).breakHorizontalExtrusion(n, b);
                }
            }
        }
    }
    
    public void breakVerticalExtrusion(final float n, final float n2, final boolean b) {
        final InnerExtrusionSetInterface verticalExtrusion = this.getVerticalExtrusion(n, b);
        if (verticalExtrusion != null) {
            ((ICDInnerExtrusionSet)verticalExtrusion).breakVerticalExtrusion(n2, b);
        }
    }
    
    public void applyChangesFromEditor(final String s, final PossibleValue possibleValue, final Collection<PossibleValue> collection, final Collection<String> collection2, final String s2) {
        if (s != null) {
            if (s.equals("ICD_Chase_Type") || s.equals("Chase_Side_B_Distance") || s.equals("Chase_Side_A_Distance")) {
                this.setWorksurfacesModified();
                if (s.equals("Chase_Side_A_Distance")) {
                    final float n = Float.parseFloat(possibleValue.getValue()) - this.getAttributeValueAsFloat("Chase_Side_A_Distance");
                    final ICDSubFrameSideContainer subFrameSideContainer = this.getSubFrameSideContainer(0);
                    if (subFrameSideContainer != null) {
                        subFrameSideContainer.moveOtherVerticalExtrusion(n);
                    }
                }
                else if (s.equals("Chase_Side_B_Distance")) {
                    final float n2 = -(Float.parseFloat(possibleValue.getValue()) - this.getAttributeValueAsFloat("Chase_Side_B_Distance"));
                    final ICDSubFrameSideContainer subFrameSideContainer2 = this.getSubFrameSideContainer(1);
                    if (subFrameSideContainer2 != null) {
                        subFrameSideContainer2.moveOtherVerticalExtrusion(n2);
                    }
                }
            }
            else if (s.equals("ICD_Height_From_Floor")) {
                final float n3 = Float.parseFloat(possibleValue.getValue()) - this.getAttributeValueAsFloat("ICD_Height_From_Floor");
                this.moveHorizontalExtrusion(this.getSplitLocation(), n3);
                this.moveHorizontalExtrusion(this.getSuspendedChaseLocation(), n3);
            }
            else if (s.equals("ICD_Suspended_Chase_Height") || s.equals("ICD_Suspended_Chase")) {
                if (s.equals("ICD_Suspended_Chase_Height")) {
                    final float n4 = -Float.parseFloat(possibleValue.getValue()) + this.getAttributeValueAsFloat("ICD_Suspended_Chase_Height");
                    this.moveHorizontalExtrusion(this.getSuspendedChaseLocation(), n4);
                    final ICDSubFrameSideContainer subFrameSideContainer3 = this.getSubFrameSideContainer(0);
                    if (subFrameSideContainer3 != null) {
                        subFrameSideContainer3.moveOtherHorizontalExtrusion(this.getSuspendedChaseLocation(), n4);
                    }
                    final ICDSubFrameSideContainer subFrameSideContainer4 = this.getSubFrameSideContainer(1);
                    if (subFrameSideContainer4 != null) {
                        subFrameSideContainer4.moveOtherHorizontalExtrusion(this.getSuspendedChaseLocation(), n4);
                    }
                }
                this.removeOtherPanelChaseHorizontalSplit();
            }
            else if (s.equals("ICD_Height_From_Floor")) {
                this.horizontalSplit(this.getSplitLocation());
            }
            else if (s.equals("ICD_Vertically_Split_Chase")) {
                this.setAdjacentPanelsDirty();
            }
        }
        super.applyChangesFromEditor(s, possibleValue, (Collection)collection, (Collection)collection2, s2);
    }
    
    private void setAdjacentPanelsDirty() {
        final SnapSetEntity snapSetEntity = (SnapSetEntity)this.getParent((TypeFilter)new SnapSetEntityFilter());
        if (snapSetEntity != null) {
            final TypeIterator typeIterator = new TypeIterator(snapSetEntity.breadthFirstEnumeration(), AssembleParent.class);
            while (typeIterator.hasNext()) {
                final EntityObject entityObject = (EntityObject)typeIterator.next();
                entityObject.setDirty();
                entityObject.setModified(true);
            }
        }
    }
    
    private void moveHorizontalExtrusion(final float n, final float n2) {
        final FrameInterface physicalFrame = this.getPhysicalFrame();
        final ExtrusionGroupInterface extrusionGroup = this.getExtrusionGroup();
        if (extrusionGroup != null && physicalFrame != null) {
            final InnerExtrusionSetInterface extrusion = extrusionGroup.findExtrusionAt(new ExtrusionPoint(1, n, 0, (StickLineInterface)physicalFrame.getStartExtrusion()), new ExtrusionPoint(1, n, 0, (StickLineInterface)physicalFrame.getEndExtrusion()));
            if (extrusion != null) {
                extrusion.moveableMove(0.0f, n2, false);
            }
        }
    }
    
    private void removeOtherPanelChaseHorizontalSplit() {
        final ICDSubFrameSideContainer chase = this.getChase(0);
        if (chase != null) {
            chase.removeOtherPanelChaseHorizontalSplit();
        }
        final ICDSubFrameSideContainer chase2 = this.getChase(1);
        if (chase2 != null) {
            chase2.removeOtherPanelChaseHorizontalSplit();
        }
    }
    
    private void setWorksurfacesModified() {
        final GeneralSnapSet generalSnapSet = this.getGeneralSnapSet();
        if (generalSnapSet != null) {
            for (final EntityObject entityObject : generalSnapSet.getChildrenVector()) {
                if (entityObject instanceof ICDBasicWorksurface) {
                    entityObject.setModified(true);
                }
            }
        }
    }
    
    public void setModified(final boolean b) {
        super.setModified(b);
        if (b) {
            final ICDILine icdiLine = (ICDILine)this.getParent((TypeFilter)new ICDILineFilter());
            if (icdiLine != null) {
                icdiLine.setModified(b);
            }
        }
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.adjustChildPanelSizeForSuspendedFramePositions();
    }
    
    protected void setupNamedPoints() {
        super.setupNamedPoints();
        this.addNamedPoint("Sub_Frame_A_POS", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("Sub_Frame_B_POS", new Point3f(0.0f, 0.0f, 0.0f));
    }
    
    public BasicFrame getSubFrame(final int n) {
        for (final ICDSubFrameSideContainer icdSubFrameSideContainer : this.getChildrenByClass(ICDSubFrameSideContainer.class, true, true)) {
            final LightWeightTypeObject lwTypeCreated = icdSubFrameSideContainer.getLwTypeCreatedFrom();
            if (lwTypeCreated != null && (("ICD_Chase_Side_A_Type".equals(lwTypeCreated.getId()) && n == 0) || ("ICD_Chase_Side_B_Type".equals(lwTypeCreated.getId()) && n != 0))) {
                final EntityObject childByClass = icdSubFrameSideContainer.getChildByClass(ICDSubFrame.class);
                if (childByClass != null) {
                    return (BasicFrame)childByClass;
                }
                continue;
            }
        }
        return null;
    }
    
    public boolean hasChase() {
        return this.isChaseSideA() || this.isChaseSideB();
    }
    
    public boolean isDoubleChase() {
        return this.isChaseSideA() && this.isChaseSideB();
    }
    
    public boolean isSingleChase() {
        boolean b = false;
        if (this.hasChase() && !this.isDoubleChase()) {
            b = true;
        }
        return b;
    }
    
    public float getChaseIntersectOffset(final int n) {
        final ICDSubFrameSideContainer chase = this.getChase(n);
        float n2 = 0.0f;
        if (chase != null && chase.getLength() != this.getLength()) {
            final boolean b = this.getBasePoint3f().x != chase.getBasePoint3f().x;
            float abs = Math.abs(this.getLength() - chase.getLength());
            if (!b) {
                abs *= -1.0f;
            }
            n2 = abs;
        }
        return n2;
    }
    
    public float getChaseOffset(final int n) {
        float n2 = 0.0f;
        if (n == 0) {
            n2 = this.getChaseSideAOffset();
        }
        else if (n == 1) {
            n2 = this.getChaseSideBOffset();
        }
        return n2;
    }
    
    public boolean hasChase(final int n) {
        boolean b = false;
        if (n == 0) {
            b = this.isChaseSideA();
        }
        else if (n == 1) {
            b = this.isChaseSideB();
        }
        return b;
    }
    
    public boolean isChaseSideA() {
        return "Side A".equals(this.getAttributeValueAsString("ICD_Chase_Type")) || "Double".equals(this.getAttributeValueAsString("ICD_Chase_Type"));
    }
    
    public boolean isChaseSideB() {
        return "Side B".equals(this.getAttributeValueAsString("ICD_Chase_Type")) || "Double".equals(this.getAttributeValueAsString("ICD_Chase_Type"));
    }
    
    public boolean isSuspendedChase() {
        return "Yes".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Suspended_Chase"));
    }
    
    public TileInterface getATile() {
        final Vector<TileInterface> allTiles = this.getAllTiles();
        if (allTiles != null && allTiles.size() > 0) {
            return allTiles.firstElement();
        }
        return null;
    }
    
    public TileInterface getBottomTile() {
        final Vector<TileInterface> allTiles = this.getAllTiles();
        if (allTiles != null) {
            while (allTiles.size() > 0) {
                final TileInterface o = allTiles.lastElement();
                if (o.isBottomTileInBasePanel()) {
                    return o;
                }
                allTiles.remove(o);
            }
        }
        return null;
    }
    
    public TileInterface getTopTile() {
        final Vector<TileInterface> allTiles = this.getAllTiles();
        if (allTiles != null) {
            while (allTiles.size() > 0) {
                final TileInterface o = allTiles.lastElement();
                if (o.isTopTileInBasePanel()) {
                    return o;
                }
                allTiles.remove(o);
            }
        }
        return null;
    }
    
    public TileInterface getMiddleTile() {
        final Vector<TileInterface> allTiles = this.getAllTiles();
        if (allTiles != null) {
            allTiles.remove(this.getTopTile());
            allTiles.remove(this.getBottomTile());
            return allTiles.firstElement();
        }
        return null;
    }
    
    public String getSKU() {
        final String sku = super.getSKU();
        if (this.currentCatalogOption != null) {
            return this.getAttributeValueAsString("PN");
        }
        return sku;
    }
    
    protected boolean panelHasElectrical() {
        final Iterator<ICDElectricalCable> iterator = this.getChildrenByClass(ICDElectricalCable.class, true, true).iterator();
        while (iterator.hasNext()) {
            if (iterator.next().calculateCableType().equalsIgnoreCase("harness")) {
                return true;
            }
        }
        return false;
    }
    
    protected float getWidthExceptNoTileWithoutFrame() {
        float width = this.getWidth();
        if (!this.isCorePanel()) {
            for (final TileInterface tileInterface : this.getAllTiles()) {
                if (tileInterface.withoutFrame()) {
                    width -= tileInterface.getXDimension() - tileInterface.getAdjustmentValue(tileInterface.getStartEdge(), 0).getAdjustmentInInches() - tileInterface.getAdjustmentValue(tileInterface.getEndEdge(), 0).getAdjustmentInInches();
                }
            }
        }
        return width;
    }
    
    public TileInterface getNoTileWithoutFrame() {
        TileInterface tileInterface = null;
        if (!this.isCorePanel()) {
            for (final TileInterface tileInterface2 : this.getAllTiles()) {
                if (tileInterface2.withoutFrame()) {
                    tileInterface = tileInterface2;
                    break;
                }
            }
        }
        return tileInterface;
    }
    
    public float getNoFrameTileBreakLocation() {
        TileInterface tileInterface = null;
        if (!this.isCorePanel()) {
            for (final TileInterface tileInterface2 : this.getAllTiles()) {
                if (tileInterface2.withoutFrame()) {
                    tileInterface = tileInterface2;
                    break;
                }
            }
        }
        if (tileInterface != null) {
            float n;
            if (tileInterface.getStartEdge().isFrameExtrusion()) {
                n = tileInterface.getEndBottomCornerInTileGroupSpace().x - 0.5f;
            }
            else {
                n = tileInterface.getStartBottomCornerInTileGroupSpace().x - 0.5f;
            }
            return n;
        }
        return 0.0f;
    }
    
    private InnerExtrusionSetInterface getHorizontalExtrusion(final float n, final float n2) {
        InnerExtrusionSetInterface extrusion = null;
        final Segment segment = this.getSegment();
        if (segment != null) {
            final TileInterface tile = BasicTile.getTile(segment, n2 - 3.0f, n, -1);
            if (tile != null) {
                extrusion = this.getExtrusionGroup().findExtrusionAt(new ExtrusionPoint(1, n2, 0, tile.getStartEdge()), new ExtrusionPoint(1, n2, 0, tile.getEndEdge()));
            }
        }
        return extrusion;
    }
    
    public void handleWarnings() {
        super.handleWarnings();
        boolean b = true;
        if (this.isStackPanel()) {
            final Iterator<ICDTileGroup> iterator = (Iterator<ICDTileGroup>)this.getChildrenByClass(ICDTileGroup.class, true, true).iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getChildCountByClass(ICDTile.class) > 2) {
                    b = false;
                }
            }
        }
        if (!b) {
            WarningReason4020.addRequiredWarning(this);
        }
    }
    
    public Vector<ICDSubFrameSideContainer> getIntersectedSubPanels(final float n, final boolean b) {
        final Vector<ICDSubFrameSideContainer> vector = new Vector<ICDSubFrameSideContainer>();
        final Point3f point3f = new Point3f();
        if (b) {
            final Point3f point3f2 = point3f;
            point3f2.x += n + 2.0f;
        }
        else {
            point3f.x = n - 2.0f;
        }
        final Point3f convertPointToWorldSpace = this.convertPointToWorldSpace(point3f);
        final Point2D.Float float1 = new Point2D.Float(convertPointToWorldSpace.x, convertPointToWorldSpace.y);
        final GeneralSnapSet generalSnapSet = this.getGeneralSnapSet();
        if (generalSnapSet != null) {
            final Vector<ILineInterface> connectedILines = this.getConnectedILines();
            for (final ILineInterface o : generalSnapSet.getAllILines()) {
                if (connectedILines == null || !connectedILines.contains(o)) {
                    for (final ICDSubFrameSideContainer e : ((EntityObject)o).getChildrenByClass(ICDSubFrameSideContainer.class, true)) {
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
    
    private Vector<ILineInterface> getConnectedILines() {
        final Vector<ILineInterface> vector = new Vector<ILineInterface>();
        final ILineInterface e = (ILineInterface)this.getParent((TypeFilter)new ILineInterfaceFilter());
        if (e != null) {
            vector.add(e);
        }
        return vector;
    }
    
    public void breakTileByChaseHorizontal(final float n, final float n2, final boolean b) {
        if (this.getIntersectedSubPanels(n, b).size() == 2) {
            final Segment segment = this.getSegment();
            if (segment != null) {
                final TileInterface tile = BasicTile.getTile(segment, n2 - 3.0f, n, -1);
                if (tile != null && this.getExtrusionGroup().findExtrusionAt(new ExtrusionPoint(1, n2, 0, tile.getStartEdge()), new ExtrusionPoint(1, n2, 0, tile.getEndEdge())) == null) {
                    tile.horizontalSplit(1, new Point3f(tile.getStartEdge().getBasePoint3f().x, 0.0f, n2), new Point3f(tile.getEndEdge().getBasePoint3f().x, 0.0f, n2), (TypeObject)null, true, true);
                }
            }
        }
    }
    
    public void removeBreakTileByChaseHorizontal(final float n, final float n2) {
        final InnerExtrusionSetInterface horizontalExtrusion = this.getHorizontalExtrusion(n, n2);
        if (horizontalExtrusion != null) {
            horizontalExtrusion.deleteAnyLinkedExtrusion();
            horizontalExtrusion.deleteForBothSides();
            this.setChildrenModified(true);
            this.getChildSolver().processChildren();
        }
    }
    
    public InnerExtrusionSetInterface getVerticalExtrusion(float n, final boolean b) {
        InnerExtrusionSetInterface extrusion = null;
        final FrameInterface physicalFrame = this.getPhysicalFrame();
        final ExtrusionGroupInterface extrusionGroup = this.getExtrusionGroup();
        if (extrusionGroup != null && physicalFrame != null) {
            final float splitLocation = this.getSplitLocation();
            Object o = extrusionGroup.findExtrusionAt(new ExtrusionPoint(1, splitLocation, 0, (StickLineInterface)physicalFrame.getStartExtrusion()), new ExtrusionPoint(1, splitLocation, 0, (StickLineInterface)physicalFrame.getEndExtrusion()));
            if (this.isUnderChase() || !this.withHorizontalInnerExtrusion()) {
                o = physicalFrame.getTopExtrusion();
            }
            if (o != null) {
                if (!b) {
                    n = this.getXDimension() - n;
                }
                extrusion = extrusionGroup.findExtrusionAt(new ExtrusionPoint(1, n, 0, (StickLineInterface)physicalFrame.getBottomExtrusion()), new ExtrusionPoint(1, n, 0, (StickLineInterface)o));
            }
        }
        return extrusion;
    }
    
    public void breakTileByChaseVertical(final float n, final boolean b) {
        if (this.shouldBreakChaseVertically() && this.getPhysicalFrame() != null) {
            ICDInternalExtrusion icdInternalExtrusion = null;
            for (final ICDInternalExtrusion icdInternalExtrusion2 : this.getChildrenByClass(ICDInternalExtrusion.class, true)) {
                if (!icdInternalExtrusion2.isVertical() && icdInternalExtrusion2.getCurrentOption().getId().equals("ICD_Extrusion_Special_With_2Joints")) {
                    icdInternalExtrusion = icdInternalExtrusion2;
                    break;
                }
            }
            if (icdInternalExtrusion == null) {
                if (this.getVerticalExtrusion(n, b) == null) {
                    final TileInterface tile = this.getTile(-1, 0);
                    if (tile != null) {
                        float n2;
                        if (b) {
                            this.currentSplitLocationFromStart = n;
                            n2 = tile.getAdjustmentValue(tile.getStartEdge(), 0).getAdjustmentInInches();
                        }
                        else {
                            this.currentSplitLocationFromEnd = n;
                            n2 = tile.getAdjustmentValue(tile.getEndEdge(), 0).getAdjustmentInInches();
                        }
                        tile.verticalSplit(n + n2);
                    }
                }
            }
            else {
                final float attributeValueAsFloat = icdInternalExtrusion.getAttributeValueAsFloat("breakLocation");
                final float attributeValueAsFloat2 = icdInternalExtrusion.getAttributeValueAsFloat("breakLocation2");
                final Segment segment = this.getSegment();
                if (segment != null) {
                    if (this.getVerticalExtrusion(attributeValueAsFloat + 1.0f, true) == null) {
                        final TileInterface tile2 = BasicTile.getTile(segment, this.getWorksurfaceHeight() - 5.0f, attributeValueAsFloat, -1);
                        if (tile2 != null) {
                            tile2.verticalSplit(attributeValueAsFloat + 1.0f + tile2.getAdjustmentValue(tile2.getStartEdge(), 0).getAdjustmentInInches());
                        }
                    }
                    if (this.getVerticalExtrusion(attributeValueAsFloat2 + 1.0f, true) == null) {
                        final TileInterface tile3 = BasicTile.getTile(segment, this.getWorksurfaceHeight() - 5.0f, attributeValueAsFloat2, -1);
                        if (tile3 != null) {
                            tile3.verticalSplit(attributeValueAsFloat2 - attributeValueAsFloat + tile3.getAdjustmentValue(tile3.getStartEdge(), 0).getAdjustmentInInches());
                        }
                    }
                }
            }
        }
    }
    
    public boolean requiresBreak(final boolean b) {
        final ICDSegment icdSegment = (ICDSegment)this.getParent((TypeFilter)new ICDSegmentFilter());
        final GeneralIntersectionInterface intersectionForSegment = icdSegment.getIntersectionForSegment(b);
        if (intersectionForSegment != null) {
            final Iterator<IntersectionArmInterface> iterator = intersectionForSegment.getArmVector().iterator();
            while (iterator.hasNext()) {
                final ICDSegment obj = (ICDSegment)iterator.next().getSegment();
                if (!icdSegment.equals(obj)) {
                    for (final ICDSubFrameSideContainer icdSubFrameSideContainer : obj.getChildrenByClass(ICDSubFrameSideContainer.class, true)) {
                        if (icdSubFrameSideContainer.requiresBreak(this)) {
                            return true;
                        }
                        if (icdSubFrameSideContainer.requiresBreak(this)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public void removeBreakTileByChaseVertical(final float n, final boolean b) {
        final InnerExtrusionSetInterface verticalExtrusion = this.getVerticalExtrusion(n, b);
        if (verticalExtrusion != null) {
            verticalExtrusion.deleteAnyLinkedExtrusion();
            verticalExtrusion.deleteForBothSides();
            this.setChildrenModified(true);
            this.getChildSolver().processChildren();
        }
    }
    
    public PANEL_TYPE getPanelType() {
        if (this.isDoorPanel()) {
            return PANEL_TYPE.DOOR;
        }
        if (this.isSlopedPanel()) {
            return PANEL_TYPE.ANGLED;
        }
        if (this.isStackPanel()) {
            return PANEL_TYPE.STACK_PANEL;
        }
        if (this.hasChase()) {
            if (this.panelHasElectrical()) {
                return PANEL_TYPE.CHASE_POWERED;
            }
            return PANEL_TYPE.CHASE;
        }
        else {
            if (this.withHorizontalInnerExtrusion()) {
                return PANEL_TYPE.SPLIT;
            }
            final TileInterface aTile = this.getATile();
            if (aTile != null && aTile.isNoTileWithFrame()) {
                return PANEL_TYPE.SUPPORT_FRAME;
            }
            if (aTile instanceof ICDTile && ((ICDTile)aTile).isValetDoorTile()) {
                return PANEL_TYPE.VALET_DOOR;
            }
            if (aTile instanceof ICDTile && ((ICDTile)aTile).isSlidingDoor()) {
                if (((ICDTile)aTile).isRegularSlidingDoor()) {
                    return PANEL_TYPE.SLIDING_DOOR;
                }
                if (((ICDTile)aTile).isHeavyDutySlidingDoor()) {
                    return PANEL_TYPE.HD_SLIDING_DOOR;
                }
            }
            return PANEL_TYPE.FULL;
        }
    }
    
    protected void adjustChildPanelSizeForSuspendedFramePositions() {
        final Point3f namedPointLocal = this.getNamedPointLocal("Sub_Frame_A_POS");
        final Point3f namedPointLocal2 = this.getNamedPointLocal("Sub_Frame_B_POS");
        final Float value = this.getAttributeValueAsFloat("Chase_Side_A_Distance");
        final Float value2 = this.getAttributeValueAsFloat("Chase_Side_B_Distance");
        if (this.isSuspendedChase()) {
            float splitLocation = this.getSplitLocation();
            if (this.isUnderChase()) {
                splitLocation = this.getHeight() - 1.0f;
            }
            namedPointLocal.set(0.0f, splitLocation - (this.getSuspendedOffset() - 1.0f), (float)value);
            namedPointLocal2.set(this.getLength(), splitLocation - (this.getSuspendedOffset() - 1.0f), -value2);
        }
        else {
            namedPointLocal.set((Tuple3f)new Point3f(0.0f, 0.0f, (float)value));
            namedPointLocal2.set((Tuple3f)new Point3f(this.getLength(), 0.0f, -value2));
        }
    }
    
    public float getSuspendedOffset() {
        return this.getAttributeValueAsFloat("ICD_Suspended_Chase_Height");
    }
    
    public void handleDynamicAttributes() {
        final HashMap<String, Attribute> attributeList = this.currentCatalogOption.getAttributeList();
        if (attributeList != null) {
            for (final String str : attributeList.keySet()) {
                final Attribute attribute = (Attribute)attributeList.get(str);
                final AttributeProxy attributeProxy = Solution.getWorldAttributeProxy().get(str);
                if (attributeProxy != null && attribute != null) {
                    final Attribute attributeObject = Attribute.createAttributeObject(attributeProxy.getAttributeClass(), str, attribute.getValueAsString());
                    if (attributeObject == null) {
                        continue;
                    }
                    this.catalogAttributeHashMap.put(str, attributeObject);
                }
                else {
                    ICDPanel.logger.warn((Object)("Error loading possible attribute into " + this.getCurrentOption().getId() + ". No Attribute Proxy for [" + str + "]"));
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
        if (this.currentCatalogOption != null && this.shouldGenerateSku()) {
            s = this.currentCatalogOption.getDescription();
            if ("ICD_Panel_Error_Option".equalsIgnoreCase(this.currentCatalogOption.getId())) {
                s = s + "-" + this.newSku;
            }
        }
        else {
            s = super.getDescription();
        }
        int i = Math.round(this.getWidth());
        if (this.isStackPanel()) {
            i = Math.round(this.getWidthExceptNoTileWithoutFrame());
        }
        return s + " (Actual Size " + Math.round(this.getHeight()) + "h " + i + "w)";
    }
    
    public String getQuoteDescription() {
        return this.getDescription();
    }
    
    public void getRequiredCatalogChildren(final Vector<RequiredChildTypeContainer> vector) {
        if (this.isCatalogPartPresent() && this.shouldGenerateSku()) {
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
    
    public List<EntityObject> getChildrenForReport() {
        if (this.isStackPanel()) {
            final ArrayList<EntityObject> list = new ArrayList<EntityObject>();
            final Iterator<ICDTile> iterator = this.getChildrenByClass(ICDTile.class, true).iterator();
            while (iterator.hasNext()) {
                list.add((EntityObject)iterator.next());
            }
            this.removeFromReportBuckets();
            return (List<EntityObject>)list;
        }
        return (List<EntityObject>)this.getChildrenVectorForReport();
    }
    
    public String getManufacturerForQuote() {
        if (this.currentCatalogOption != null && this.currentCatalogOption.getCatalogPart() != null) {
            return this.currentCatalogOption.getCatalogPart().getCatalog().getProduceCode();
        }
        if (this.currentCatalogOption != null) {
            return "ICI";
        }
        return super.getManufacturerForQuote();
    }
    
    public String getProductForQuote() {
        if (this.currentCatalogOption != null && this.currentCatalogOption.getCatalogPart() != null) {
            return this.currentCatalogOption.getCatalogPart().getCatalog().getProduceCode();
        }
        if (this.currentCatalogOption != null) {
            return "ICI";
        }
        return super.getProductForQuote();
    }
    
    public boolean isCatalogPartPresent() {
        return this.currentCatalogOption != null;
    }
    
    public boolean isStackPanel() {
        final PanelSegmentInterface parentPanelSegment = this.getParentPanelSegment();
        return parentPanelSegment != null && !parentPanelSegment.isBaseSegment();
    }
    
    public boolean isUnderChase() {
        return "true".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Is_Under_Chase"));
    }
    
    public boolean isDoorPanel() {
        return "Yes".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Door_Panel"));
    }
    
    public boolean isSlopedPanel() {
        return "Yes".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Sloped_Panel"));
    }
    
    public int canCreateInnerCorner(final ICDSubFrameSideContainer icdSubFrameSideContainer) {
        int n = -1;
        final Line2D.Float line = icdSubFrameSideContainer.getLine();
        if (line != null) {
            final Line2D.Float subPanelLine = this.getSubPanelLine(0);
            if (subPanelLine != null && line.intersectsLine(subPanelLine)) {
                n = 0;
            }
            if (n == -1) {
                final Line2D.Float subPanelLine2 = this.getSubPanelLine(1);
                if (subPanelLine2 != null && line.intersectsLine(subPanelLine2)) {
                    n = 1;
                }
            }
        }
        return n;
    }
    
    public Line2D.Float getSubPanelLine(final int n) {
        Line2D.Float float1 = null;
        if (this.getSubFrameSideContainer(n) != null) {
            Point3f point3f;
            Point3f point3f2;
            if (n == 0) {
                point3f = new Point3f(this.getNamedPointLocal("Sub_Frame_A_POS"));
                point3f2 = new Point3f(point3f);
                point3f2.x += this.getLength();
            }
            else {
                point3f = new Point3f(this.getNamedPointLocal("Sub_Frame_B_POS"));
                point3f2 = new Point3f(point3f);
                point3f2.x -= this.getLength();
            }
            final Point3f convertPointToWorldSpace = this.convertPointToWorldSpace(point3f);
            final Point3f convertPointToWorldSpace2 = this.convertPointToWorldSpace(point3f2);
            float1 = new Line2D.Float(convertPointToWorldSpace.x, convertPointToWorldSpace.y, convertPointToWorldSpace2.x, convertPointToWorldSpace2.y);
        }
        return float1;
    }
    
    public ICDSubFrameSideContainer getSubFrameSideContainer(final int n) {
        String s = "ICD_Chase_Side_A_Type";
        if (n == 1) {
            s = "ICD_Chase_Side_B_Type";
        }
        return (ICDSubFrameSideContainer)this.getChildByLWType(s);
    }
    
    public float getChasePullBack(final int n) {
        float n2;
        if (n == 0) {
            n2 = this.getAttributeValueAsFloat("Chase_Side_A_Distance");
        }
        else {
            n2 = this.getAttributeValueAsFloat("Chase_Side_B_Distance");
        }
        return n2;
    }
    
    public Point3f getSubPanelPoint(final int n, final boolean b) {
        Point3f point3f;
        if (n == 0) {
            point3f = new Point3f(this.getNamedPointLocal("Sub_Frame_A_POS"));
            if (!b) {
                point3f.x += this.getXDimension();
            }
        }
        else {
            point3f = new Point3f(this.getNamedPointLocal("Sub_Frame_B_POS"));
            if (!b) {
                point3f.x -= this.getXDimension();
            }
        }
        return point3f;
    }
    
    public boolean isOpenBottom() {
        final TileInterface tile = this.getTile(-1, 0);
        return tile instanceof ICDTile && tile.isNoTile();
    }
    
    public boolean hasOpenTiles() {
        final Iterator<TileInterface> iterator = this.getAllTiles(-1).iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isNoTile()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean shouldBreakChaseVertically() {
        return "Yes".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Vertically_Split_Chase"));
    }
    
    protected void writeXMLFields(final XMLWriter xmlWriter, final PersistentFileManager.FileWriter fileWriter) throws IOException {
        super.writeXMLFields(xmlWriter, fileWriter);
        xmlWriter.writeTextElement("currentSplitLocationFromStart", this.currentSplitLocationFromStart + "");
        xmlWriter.writeTextElement("currentSplitLocationFromEnd", this.currentSplitLocationFromEnd + "");
    }
    
    protected void setFieldInfoFromXML(final Node node, final DefaultMutableTreeNode defaultMutableTreeNode, final PersistentFileManager.FileReader fileReader) {
        super.setFieldInfoFromXML(node, defaultMutableTreeNode, fileReader);
        final String stringValueFromXML = this.getStringValueFromXML("currentSplitLocationFromStart", node, "null");
        final String stringValueFromXML2 = this.getStringValueFromXML("currentSplitLocationFromEnd", node, "null");
        try {
            this.currentSplitLocationFromStart = Float.parseFloat(stringValueFromXML);
            this.currentSplitLocationFromEnd = Float.parseFloat(stringValueFromXML2);
        }
        catch (Exception ex) {
            System.out.println("Automatically Generated Exception Log(ICDPanel.java,1263)[" + ex.getClass() + "]: " + ex.getMessage());
            this.currentSplitLocationFromStart = 0.0f;
            this.currentSplitLocationFromEnd = 0.0f;
        }
    }
    
    public boolean hasChaseOnPointSide(final Point3f point3f) {
        if (!this.hasChase()) {
            return false;
        }
        if (point3f.z > 0.0f) {
            return this.isChaseSideA();
        }
        return this.isChaseSideB();
    }
    
    public float getChaseOffsetOnPointSide(final Point3f point3f, final boolean b, final boolean b2) {
        if (point3f.z > 0.0f) {
            if (this.isChaseSideA()) {
                if (!b) {
                    return this.getChaseSideAOffset();
                }
                if (!this.isSuspendedChase() || (this.isSuspendedChase() && b2)) {
                    return this.getChaseSideAOffset();
                }
            }
        }
        else if (this.isChaseSideB()) {
            if (!b) {
                return this.getChaseSideBOffset();
            }
            if (!this.isSuspendedChase() || (this.isSuspendedChase() && b2)) {
                return this.getChaseSideBOffset();
            }
        }
        return 0.0f;
    }
    
    public float[] getChaseOffsetAndHeightOnPointSide(final Point3f point3f) {
        final float[] array = { 0.0f, 0.0f };
        if (point3f.z > 0.0f) {
            if (this.isChaseSideA()) {
                array[0] = this.getChaseSideAOffset();
                array[1] = this.getWorksurfaceHeight();
            }
        }
        else if (this.isChaseSideB()) {
            array[0] = this.getChaseSideBOffset();
            array[1] = this.getWorksurfaceHeight();
        }
        return array;
    }
    
    public boolean isPointOnSideA(final Point3f point3f) {
        return point3f.z > 0.0f;
    }
    
    public boolean hasSuspendedChaseOnPointSide(final Point3f point3f) {
        if (!this.hasChase()) {
            return false;
        }
        if (point3f.z > 0.0f) {
            return this.isChaseSideA() && this.isSuspendedChase();
        }
        return this.isChaseSideB() && this.isSuspendedChase();
    }
    
    public float getDefaultWorksurfaceHeight() {
        return this.getAttributeValueAsFloat("ICD_Height_From_Floor");
    }
    
    public float getChaseSideAOffset() {
        return this.getAttributeValueAsFloat("Chase_Side_A_Distance");
    }
    
    public float getChaseSideBOffset() {
        return this.getAttributeValueAsFloat("Chase_Side_B_Distance");
    }
    
    public void addOnTheFlyAttribute() {
        super.addOnTheFlyAttribute();
        if (!this.isCorePanel()) {
            final OptionAttributeProxy optionAttributeProxy = (OptionAttributeProxy)Solution.getWorldAttributeProxy().get("ICD_Sloped_Visible");
            optionAttributeProxy.getPossibleValues().clear();
            optionAttributeProxy.addPossibleValue("No");
            if (this.isValidSize()) {
                optionAttributeProxy.addPossibleValue("Yes");
            }
            this.createNewAttribute("ICD_Sloped_Visible", this.getAttributeValueAsString("ICD_Sloped_Panel"));
        }
        else {
            final OptionAttributeProxy optionAttributeProxy2 = (OptionAttributeProxy)Solution.getWorldAttributeProxy().get("ICD_Door_Visible");
            optionAttributeProxy2.getPossibleValues().clear();
            optionAttributeProxy2.addPossibleValue("No");
            if (this.isValidSize()) {
                optionAttributeProxy2.addPossibleValue("Yes");
            }
            this.createNewAttribute("ICD_Door_Visible", this.getAttributeValueAsString("ICD_Door_Panel"));
        }
    }
    
    public void getOnTheFlyAttributes(final Collection<Attribute> collection) {
        super.getOnTheFlyAttributes((Collection)collection);
        collection.add(this.getAttributeObject("ICD_Sloped_Visible"));
        collection.add(this.getAttributeObject("ICD_Door_Visible"));
    }
    
    public void setLocalVariables(final String localVariables) {
        super.setLocalVariables(localVariables);
        if (localVariables.equals("ICD_Sloped_Visible")) {
            this.applyChangesForAttribute("ICD_Sloped_Panel", this.getAttributeObject("ICD_Sloped_Visible").getValueAsString());
        }
        else if (localVariables.equals("ICD_Door_Visible")) {
            this.applyChangesForAttribute("ICD_Door_Panel", this.getAttributeObject("ICD_Door_Visible").getValueAsString());
        }
    }
    
    public void removeOnTheFlyAttribute() {
        super.removeOnTheFlyAttribute();
        final OptionAttributeProxy optionAttributeProxy = (OptionAttributeProxy)Solution.getWorldAttributeProxy().get("ICD_Sloped_Visible");
        if (optionAttributeProxy != null) {
            optionAttributeProxy.setPossibleValues(new Vector());
        }
        this.removeAttribute("ICD_Sloped_Visible");
        final OptionAttributeProxy optionAttributeProxy2 = (OptionAttributeProxy)Solution.getWorldAttributeProxy().get("ICD_Door_Visible");
        if (optionAttributeProxy2 != null) {
            optionAttributeProxy2.setPossibleValues(new Vector());
        }
        this.removeAttribute("ICD_Door_Visible");
    }
    
    public boolean isValidSize() {
        final String attributeValueAsString = this.getAttributeValueAsString("Panel_Width_Indicator");
        final Vector possibleAttributeValues = IndicatorUtilities.getPossibleAttributeValues("Panel_Width_Indicator", this.getCurrentType().getPossibleOptionVector());
        final String attributeValueAsString2 = this.getAttributeValueAsString("Panel_Height_Indicator");
        final Vector possibleAttributeValues2 = IndicatorUtilities.getPossibleAttributeValues("Panel_Height_Indicator", this.getCurrentType().getPossibleOptionVector());
        return possibleAttributeValues.contains(attributeValueAsString) && possibleAttributeValues2.contains(attributeValueAsString2);
    }
    
    public ICDSubFrameSideContainer getChase(final int n) {
        ICDSubFrameSideContainer icdSubFrameSideContainer = null;
        for (final ICDSubFrameSideContainer icdSubFrameSideContainer2 : this.getChildrenByClass(ICDSubFrameSideContainer.class, false)) {
            if (icdSubFrameSideContainer2.getSide() == n) {
                icdSubFrameSideContainer = icdSubFrameSideContainer2;
            }
        }
        return icdSubFrameSideContainer;
    }
    
    public Vector<Float> getAllSplitLocation(final boolean b) {
        if (this.isDoorPanel()) {
            final Vector<Float> vector = new Vector<Float>();
            vector.add(1.0f);
            vector.add(this.getYDimension());
            return vector;
        }
        if (this.isSlopedPanel()) {
            return new Vector<Float>();
        }
        return (Vector<Float>)super.getAllSplitLocation(b);
    }
    
    public boolean isQuoteable(final String s) {
        return super.isQuoteable(s) && !this.isVerticalChasePanel();
    }
    
    public boolean shouldAddChildrenToReport(final Report report) {
        return (super.shouldAddChildrenToReport(report) && !this.isVerticalChasePanel()) || this.isStackPanel();
    }
    
    private boolean isVerticalChasePanel() {
        final ICDILine icdiLine = (ICDILine)this.getParent((TypeFilter)new ICDILineFilter());
        return icdiLine != null && icdiLine.getVerticalChase() != null;
    }
    
    public void populateCompareNode(final Class clazz, final CompareNode compareNode) {
        super.populateCompareNode(clazz, compareNode);
        final int round = Math.round(this.getWidth());
        final int round2 = Math.round(this.getHeight());
        compareNode.addCompareValue("ActualWidth", (Object)round);
        compareNode.addCompareValue("ActualHeight", (Object)round2);
    }
    
    public boolean isDoorPanelWithTopStack() {
        final PanelInterface topestPanel = this.getTopestPanel();
        return this.isDoorPanel() && !this.equals(topestPanel);
    }
    
    public float getWorksurfaceHeight() {
        return this.getAttributeValueAsFloat("ICD_Height_From_Floor");
    }
    
    public Vector<Ice2DPaintableNode> getAssemblyIcons(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        final Vector<Ice2DPaintableNode> vector = new Vector<Ice2DPaintableNode>();
        if (this.isSlopedPanel()) {
            final float a = (this.getXDimension() == 42.0f) ? ICDPanel.SLOPED_STACK_SHORTY_42 : ICDPanel.SLOPED_STACK_SHORTY_24;
            final float a2 = (this.getXDimension() == 42.0f) ? ICDPanel.SLOPED_STACK_ANGLEDX_42 : ICDPanel.SLOPED_STACK_ANGLEDX_24;
            final Ice2DTextNode assemblyDimensionTextNode = ICDAssemblyElevationUtilities.createAssemblyDimensionTextNode((TransformableEntity)this, new Vector3f(0.0f, this.getYDimension() / 2.0f, 0.0f), Math.round(this.getYDimension()) + "", new Vector3f(0.0f, 0.0f, 1.5707964f));
            final Ice2DTextNode assemblyDimensionTextNode2 = ICDAssemblyElevationUtilities.createAssemblyDimensionTextNode((TransformableEntity)this, new Vector3f(this.getXDimension(), a / 2.0f, 0.0f), Math.round(a) + "", new Vector3f(0.0f, 0.0f, 1.5707964f));
            final Ice2DTextNode assemblyDimensionTextNode3 = ICDAssemblyElevationUtilities.createAssemblyDimensionTextNode((TransformableEntity)this, new Vector3f(this.getXDimension() / 2.0f, this.getYDimension() - a / 2.0f, 0.0f), Math.round(a2) + "", new Vector3f(0.0f, 0.0f, 0.0f));
            vector.add((Ice2DPaintableNode)assemblyDimensionTextNode);
            vector.add((Ice2DPaintableNode)assemblyDimensionTextNode2);
            vector.add((Ice2DPaintableNode)assemblyDimensionTextNode3);
            final Vector3f vector3f = new Vector3f();
            final Vector3f vector3f2 = new Vector3f();
            if (MathUtilities.isSameFloat(this.getXDimension(), 42.0f, 0.5f)) {
                vector3f.x = 43.0f;
                vector3f2.y = 3.1415927f;
            }
            else {
                vector3f.x = -1.0f;
            }
            vector3f.y = 23.0f;
            vector.add((Ice2DPaintableNode)ICDAssemblyElevationUtilities.createAssemblyElevationImageNode((TransformableEntity)this, vector3f, vector3f2));
        }
        else {
            vector.add((Ice2DPaintableNode)ICDAssemblyElevationUtilities.createAssemblyElevationImageNode((TransformableEntity)this, new Vector3f(0.0f, this.getHeight(), 0.0f), new Vector3f()));
        }
        return vector;
    }
    
    public List<IceOutputNode> getPlotOutputNodes(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        return null;
    }
    
    public boolean shouldDrawAssembly() {
        return this.isSlopedPanel() || this.isDoorPanel() || this.isCurvedPanel();
    }
    
    public boolean isAssembled() {
        return true;
    }
    
    public void addAdditonalPaintableEntities(final List<AssemblyPaintable> list) {
    }
    
    public boolean checkAssembledForAssemblyElevation() {
        return this.getAttributeValueAsBoolean("isAssembled", false);
    }
    
    public boolean shouldICDMakePreAssembledReport() {
        return "true".equals(this.getAttributeValueAsString("isAssembled")) && "true".equals(this.getAttributeValueAsString("shouldAssemble"));
    }
    
    public boolean isSuspendedChaseHorizontalSplit() {
        return this.isSuspendedChase() && "Yes".equalsIgnoreCase(this.getAttributeValueAsString("ICD_Suspended_Chase_Horizontal_Split"));
    }
    
    float getSuspendedChaseLocation() {
        return this.getSplitLocation() - (this.getSuspendedOffset() - 1.0f);
    }
    
    public void drawCad(final ICadTreeNode cadTreeNode, final int n) {
        super.drawCad(cadTreeNode, n);
        if (ICDPanel.skuGenerator != null && this.getAttributeObject("Base_SKU") != null && !this.isVerticalChasePanel()) {
            final String generateSKU = ICDPanel.skuGenerator.generateSKU((TypeableEntity)this, true);
            final Point3f point3f = (Point3f)this.getBasePoint3f().clone();
            float rotationWorldSpace = this.getRotationWorldSpace();
            if (!this.isCurvedPanel()) {
                final Point3f point3f2 = point3f;
                point3f2.x += this.getXDimension() / 2.0f - generateSKU.length() / 2.0f * 2.0f;
                final Point3f point3f3 = point3f;
                point3f3.z -= this.getZDimension() / 2.0f + this.getStackingLevel() * 2.0f;
            }
            else {
                final Point3f point3f4 = point3f;
                point3f4.x += generateSKU.length() * 2.0f;
                rotationWorldSpace -= (float)3.141592653589793;
            }
            this.textNode = ICDUtilities.drawCadText((TransformableEntity)this, this.textNode, cadTreeNode, generateSKU, this.convertPointToWorldSpace(point3f), 1, rotationWorldSpace);
        }
    }
    
    public void drawIceCadDotNet(final int n, final IceCadNodeContainer iceCadNodeContainer, final IceCadIceApp iceCadIceApp) {
        super.drawIceCadDotNet(n, iceCadNodeContainer, iceCadIceApp);
        if (ICDPanel.skuGenerator != null && this.getAttributeObject("Base_SKU") != null && !this.isVerticalChasePanel()) {
            final String generateSKU = ICDPanel.skuGenerator.generateSKU((TypeableEntity)this, true);
            final Point3f point3f = (Point3f)this.getBasePoint3f().clone();
            float rotationWorldSpace = this.getRotationWorldSpace();
            if (!this.isCurvedPanel()) {
                final Point3f point3f2 = point3f;
                point3f2.x += this.getXDimension() / 2.0f - generateSKU.length() / 2.0f * 2.0f;
                final Point3f point3f3 = point3f;
                point3f3.z -= this.getZDimension() / 2.0f + this.getStackingLevel() * 2.0f;
            }
            else {
                final Point3f point3f4 = point3f;
                point3f4.x += generateSKU.length() * 2.0f;
                rotationWorldSpace -= (float)3.141592653589793;
            }
            this.iceTextNode = ICDUtilities.drawIceCadTextDotNet(iceCadNodeContainer, this.iceTextNode, generateSKU, this.convertPointToWorldSpace(point3f), rotationWorldSpace);
        }
    }
    
    public void drawIceCadForProxyEntityDotNet(final IceCadNodeContainer iceCadNodeContainer, final TransformableEntity transformableEntity, final IceCadCompositeBlock iceCadCompositeBlock, final int n, final SolutionSetting solutionSetting) {
        if (ICDPanel.skuGenerator != null && this.getAttributeObject("Base_SKU") != null && !this.isVerticalChasePanel()) {
            final String generateSKU = ICDPanel.skuGenerator.generateSKU((TypeableEntity)this, true);
            final Point3f point3f = (Point3f)this.getBasePoint3f().clone();
            float rotationWorldSpace = this.getRotationWorldSpace();
            if (!this.isCurvedPanel()) {
                final Point3f point3f2 = point3f;
                point3f2.x += this.getXDimension() / 2.0f - generateSKU.length() / 2.0f * 2.0f;
                final Point3f point3f3 = point3f;
                point3f3.z -= this.getZDimension() / 2.0f + this.getStackingLevel() * 2.0f;
            }
            else {
                final Point3f point3f4 = point3f;
                point3f4.x += generateSKU.length() * 2.0f;
                rotationWorldSpace -= (float)3.141592653589793;
            }
            this.textNodeNetForProxyEntity = ICDUtilities.drawIceCadTextForProxyEntityDotNet(iceCadNodeContainer, this.textNodeNetForProxyEntity, transformableEntity, iceCadCompositeBlock, generateSKU, this.convertPointToWorldSpace(point3f), rotationWorldSpace);
        }
        super.drawIceCadForProxyEntityDotNet(iceCadNodeContainer, transformableEntity, iceCadCompositeBlock, n, solutionSetting);
    }
    
    public boolean isCurvedPanel() {
        return false;
    }
    
    public int getStackingLevel() {
        int n = 0;
        final ICDPanelSubILine icdPanelSubILine = (ICDPanelSubILine)this.getParent((TypeFilter)new ICDPanelSubILineFilter());
        final float z = this.getBasePointWorldSpace().z;
        if (icdPanelSubILine != null) {
            for (final ICDPanel icdPanel : icdPanelSubILine.getChildrenFirstAppearance(ICDPanel.class, true)) {
                if (!icdPanel.equals(this) && icdPanel.getBasePointWorldSpace().z < z) {
                    ++n;
                }
            }
        }
        return n;
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
    
    public List<IceOutputNode> getPlotOutputNodes() {
        final ArrayList<IceOutputNode> list = new ArrayList<IceOutputNode>();
        final ICadTextNode cadOutputTextNode = this.getCadOutputTextNode(null);
        if (cadOutputTextNode != null) {
            final Matrix4f matrix4f = new Matrix4f();
            matrix4f.setIdentity();
            final Point3f cadOutputInsertionPoint = this.getCadOutputInsertionPoint(cadOutputTextNode.getText());
            final Matrix4f matrix4f2 = new Matrix4f();
            matrix4f2.setIdentity();
            matrix4f2.setTranslation(new Vector3f(cadOutputInsertionPoint.x, cadOutputInsertionPoint.y, cadOutputInsertionPoint.z));
            matrix4f.mul(matrix4f2);
            final Matrix4f matrix4f3 = new Matrix4f();
            matrix4f3.setIdentity();
            matrix4f3.rotZ(this.getRotationWorldSpace());
            matrix4f.mul(matrix4f3);
            final IceOutputTextNode e = new IceOutputTextNode((Paintable)null, cadOutputTextNode.getText(), 2.0f, 1.0f, 0.0f, new Point3f(), matrix4f);
            list.add(e);
            e.setParent((IceOutputNode)new IceOutputLayerNode("Panels"));
        }
        return (List<IceOutputNode>)list;
    }
    
    private ICadTextNode getCadOutputTextNode(final ICadTreeNode cadTreeNode) {
        if (ICDPanel.skuGenerator != null && this.getAttributeObject("Base_SKU") != null && !this.isVerticalChasePanel()) {
            final String generateSKU = ICDPanel.skuGenerator.generateSKU((TypeableEntity)this, true);
            final Point3f cadOutputInsertionPoint = this.getCadOutputInsertionPoint(generateSKU);
            float rotationWorldSpace = this.getRotationWorldSpace();
            if (this.isCurvedPanel()) {
                rotationWorldSpace -= (float)3.141592653589793;
            }
            return ICDUtilities.drawCadText((TransformableEntity)this, this.textNode, cadTreeNode, generateSKU, cadOutputInsertionPoint, 1, rotationWorldSpace);
        }
        return null;
    }
    
    private Point3f getCadOutputInsertionPoint(final String s) {
        final Point3f point3f = (Point3f)this.getBasePoint3f().clone();
        if (!this.isCurvedPanel()) {
            final Point3f point3f2 = point3f;
            point3f2.x += this.getXDimension() / 2.0f - s.length() / 2.0f * 2.0f;
            final Point3f point3f3 = point3f;
            point3f3.z -= this.getZDimension() / 2.0f + this.getStackingLevel() * 2.0f;
        }
        else {
            final Point3f point3f4 = point3f;
            point3f4.x += s.length() * 2.0f;
        }
        return this.convertPointToWorldSpace(point3f);
    }
    
    public void moveVerticalExtrusion(final float n, final float n2) {
        final InnerExtrusionSetInterface verticalExtrusion = this.getVerticalExtrusion(n, true);
        if (verticalExtrusion != null) {
            verticalExtrusion.moveableMove(n2, 0.0f, false);
            this.setModified(true);
        }
    }
    
    public void moveHorizontalExtrusionBySuspendedChase(final float n, final float n2, final float n3) {
        final InnerExtrusionSetInterface horizontalExtrusion = this.getHorizontalExtrusion(n, n2);
        if (horizontalExtrusion != null) {
            horizontalExtrusion.moveableMove(0.0f, n3, false);
            this.setModified(true);
        }
    }
    
    public boolean isTopPositionPanel() {
        return this == this.getTopestPanel();
    }
    
    public ICDPanel getAbovePanel() {
        final PanelSegmentInterface parentPanelSegment = this.getParentPanelSegment();
        if (parentPanelSegment != null) {
            final PanelSubILineInterface parentPanelSubILine = parentPanelSegment.getParentPanelSubILine();
            if (parentPanelSubILine != null) {
                return (ICDPanel)parentPanelSubILine.getChildPanelAt(parentPanelSegment.getBasePoint3f().x + parentPanelSegment.getHeight() + 3.0f);
            }
        }
        return null;
    }
    
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        if (elevationEntity instanceof ICDIsometricAssemblyElevationEntity) {
            return new Vector<String>();
        }
        return (Vector<String>)super.getCadElevationScript(elevationEntity);
    }
    
    public CatalogOptionObject findCatalogPart() {
        if (this.currentCatalogOption instanceof CatalogOptionObject) {
            return (CatalogOptionObject)this.currentCatalogOption;
        }
        return null;
    }
    
    public boolean shouldPaintAssemblyInIce2D() {
        return false;
    }
    
    public boolean hasChaseAtPoint(final Point3f point3f) {
        if (this.hasChase()) {
            if (point3f.z > 0.0f && this.isChaseSideA()) {
                return this.hasChaseAtPoint(point3f, this.getSubFrameSideContainer(0));
            }
            if (point3f.z <= 0.0f && this.isChaseSideB()) {
                return this.hasChaseAtPoint(point3f, this.getSubFrameSideContainer(1));
            }
        }
        return false;
    }
    
    private boolean hasChaseAtPoint(final Point3f point3f, final ICDSubFrameSideContainer icdSubFrameSideContainer) {
        if (icdSubFrameSideContainer != null) {
            final FrameInterface physicalFrame = icdSubFrameSideContainer.getPhysicalFrame();
            if (physicalFrame != null) {
                final TopExtrusionInterface topExtrusion = physicalFrame.getTopExtrusion();
                final BottomExtrusionInterface bottomExtrusion = physicalFrame.getBottomExtrusion();
                if (topExtrusion != null) {
                    final Point3f convertSpaces = MathUtilities.convertSpaces(point3f, (EntityObject)this, (EntityObject)topExtrusion);
                    if (MathUtilities.isSamePoint(convertSpaces, new Point3f(), 1.5f) || MathUtilities.isSamePoint(convertSpaces, new Point3f(0.0f, 0.0f, topExtrusion.getZDimension()), 1.5f)) {
                        return true;
                    }
                }
                if (bottomExtrusion != null) {
                    final Point3f convertSpaces2 = MathUtilities.convertSpaces(point3f, (EntityObject)this, (EntityObject)bottomExtrusion);
                    if (MathUtilities.isSamePoint(convertSpaces2, new Point3f(), 1.5f) || MathUtilities.isSamePoint(convertSpaces2, new Point3f(0.0f, 0.0f, bottomExtrusion.getZDimension()), 1.5f)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public void validateJointType(final String anObject, String s, final ICDMiddleJoint icdMiddleJoint) {
        if ("None".equals(anObject)) {
            return;
        }
        if ("Single".equals(anObject)) {
            if (s.equals(ICDJoint.JOINT_TYPE[1])) {
                s = ICDJoint.JOINT_TYPE[2];
            }
            else if (s.equals(ICDJoint.JOINT_TYPE[0])) {
                s = ICDJoint.JOINT_TYPE[3];
            }
        }
        else if ("Double".equals(anObject)) {
            if (s.equals(ICDJoint.JOINT_TYPE[1])) {
                s = ICDJoint.JOINT_TYPE[3];
            }
            else if (s.equals(ICDJoint.JOINT_TYPE[0])) {
                s = ICDJoint.JOINT_TYPE[4];
            }
        }
        icdMiddleJoint.modifyAttributeValue("Joint_Type", s);
    }
    
    public void setVerticalExtrusionsModified() {
        for (final ICDVerticalExtrusion icdVerticalExtrusion : this.getChildrenByClass(ICDVerticalExtrusion.class, true, true)) {
            if (icdVerticalExtrusion instanceof ICDStartExtrusion) {
                ((ICDStartExtrusion)icdVerticalExtrusion).setModified(true);
            }
            else {
                if (!(icdVerticalExtrusion instanceof ICDEndExtrusion)) {
                    continue;
                }
                ((ICDEndExtrusion)icdVerticalExtrusion).setModified(true);
            }
        }
    }
    
    public String getTypeOfChaseMidConnectorContainerAtLocation(final Point3f point3f) {
        String valueAsString = "None";
        final ICDChaseMidConnectorContainer chaseMidConnectorContainerAtLocation = this.getChaseMidConnectorContainerAtLocation(point3f);
        if (chaseMidConnectorContainerAtLocation != null) {
            final Attribute attributeObject = chaseMidConnectorContainerAtLocation.getAttributeObject("ICD_Chase_Connector_Container_Size");
            if (attributeObject != null) {
                valueAsString = attributeObject.getValueAsString();
            }
        }
        return valueAsString;
    }
    
    public ICDChaseMidConnectorContainer getChaseMidConnectorContainerAtLocation(final Point3f point3f) {
        ICDChaseMidConnectorContainer icdChaseMidConnectorContainer = null;
        final ICDPost fakePostAtLocation = this.getFakePostAtLocation(point3f);
        if (fakePostAtLocation != null) {
            icdChaseMidConnectorContainer = (ICDChaseMidConnectorContainer)fakePostAtLocation.getChildByClass(ICDChaseMidConnectorContainer.class);
        }
        return icdChaseMidConnectorContainer;
    }
    
    public ICDPost getFakePostAtLocation(final Point3f point3f) {
        final List<ICDPost> fakePostsUnderCurvedPost = this.getFakePostsUnderCurvedPost();
        if (fakePostsUnderCurvedPost != null) {
            for (final ICDPost icdPost : fakePostsUnderCurvedPost) {
                if (MathUtilities.isSameXY(point3f, icdPost.getBasePointWorldSpace(), 0.1f)) {
                    return icdPost;
                }
            }
        }
        return null;
    }
    
    public List<ICDPost> getFakePostsUnderCurvedPost() {
        List<ICDPost> childrenByClass = null;
        final ICDPost post = this.getPost();
        if (post != null) {
            childrenByClass = (List<ICDPost>)post.getChildrenByClass(ICDPost.class, false);
        }
        return childrenByClass;
    }
    
    public ICDPost getPost() {
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity instanceof ICDPost) {
            return (ICDPost)parentEntity;
        }
        return null;
    }
    
    public void collectExtraIndirectAssemblyParts(final boolean b, final HashSet<EntityObject> set, final boolean b2, final boolean b3, final boolean b4, final Class<EntityObject>... array) {
        final FrameInterface physicalFrame = this.getPhysicalFrame();
        if (physicalFrame instanceof ICDFrame) {
            ((ICDFrame)physicalFrame).collectExtraIndirectAssemblyParts(b, set, b2, b3, b4, array);
        }
        if (this.isStackPanel()) {
            for (final SideGroupInterface sideGroupInterface : this.getSideGroups()) {
                if (sideGroupInterface instanceof ICDSideGroup) {
                    ((ICDSideGroup)sideGroupInterface).collectExtraIndirectAssemblyParts(b, set, b2, array);
                }
            }
        }
    }
    
    public boolean brokenByAnotherPanel(final ICDPanel icdPanel) {
        return this.brokenBySide(icdPanel, 0) || this.brokenBySide(icdPanel, 1);
    }
    
    private boolean brokenBySide(final ICDPanel icdPanel, final int n) {
        boolean intersectsLine = false;
        final ICDSubFrameSideContainer subFrameSideContainer = icdPanel.getSubFrameSideContainer(n);
        if (subFrameSideContainer != null) {
            intersectsLine = subFrameSideContainer.getLine2DFloat().intersectsLine(this.getLine2DFloat());
        }
        return intersectsLine;
    }
    
    public Line2D.Float getLine2DFloat() {
        final float n = 1000.0f;
        final Point3f point3f = new Point3f(0.0f, 0.0f, 0.0f);
        final Point3f point3f2 = new Point3f(point3f);
        point3f2.x += this.getLength();
        final Point3f convertPointToWorldSpace = this.convertPointToWorldSpace(point3f);
        final Point3f convertPointToWorldSpace2 = this.convertPointToWorldSpace(point3f2);
        convertPointToWorldSpace.x = Math.round(convertPointToWorldSpace.x * n) / n;
        convertPointToWorldSpace.y = Math.round(convertPointToWorldSpace.y * n) / n;
        convertPointToWorldSpace2.x = Math.round(convertPointToWorldSpace2.x * n) / n;
        convertPointToWorldSpace2.y = Math.round(convertPointToWorldSpace2.y * n) / n;
        return new Line2D.Float(convertPointToWorldSpace.x, convertPointToWorldSpace.y, convertPointToWorldSpace2.x, convertPointToWorldSpace2.y);
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
        ICDPanel.logger = Logger.getLogger(ICDPanel.class);
        ICDPanel.SLOPED_STACK_ANGLEDX_42 = 44.05f;
        ICDPanel.SLOPED_STACK_SHORTY_42 = 4.8f;
        ICDPanel.SLOPED_STACK_ANGLEDX_24 = 24.23f;
        ICDPanel.SLOPED_STACK_SHORTY_24 = 11.95f;
    }
    
    enum PANEL_TYPE
    {
        FULL {
            @Override
            public String getPanelName() {
                return "Full Panel";
            }
        }, 
        ANGLED {
            @Override
            public String getPanelName() {
                return "Angled Panel";
            }
        }, 
        DOOR {
            @Override
            public String getPanelName() {
                return "Door Panel";
            }
        }, 
        SPLIT {
            @Override
            public String getPanelName() {
                return "Split Panel";
            }
        }, 
        STACK_PANEL {
            @Override
            public String getPanelName() {
                return "Stack Panel";
            }
        }, 
        CHASE {
            @Override
            public String getPanelName() {
                return "Chase Panel";
            }
        }, 
        CHASE_POWERED {
            @Override
            public String getPanelName() {
                return "Chase Panel Powered";
            }
        }, 
        VALET_DOOR {
            @Override
            public String getPanelName() {
                return "Valet Door";
            }
        }, 
        SLIDING_DOOR {
            @Override
            public String getPanelName() {
                return "Sliding Door";
            }
        }, 
        HD_SLIDING_DOOR {
            @Override
            public String getPanelName() {
                return "Heavy Duty Sliding Door";
            }
        }, 
        CURVED_PANEL {
            @Override
            public String getPanelName() {
                return "Curved Panel";
            }
        }, 
        SUPPORT_FRAME {
            @Override
            public String getPanelName() {
                return "Support Frame";
            }
        };
        
        public abstract String getPanelName();
    }
}
