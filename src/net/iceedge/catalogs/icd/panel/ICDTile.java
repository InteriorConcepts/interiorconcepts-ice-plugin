package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.HashSet;
import net.iceedge.catalogs.icd.ICDUtilities;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReport;
import java.util.TreeMap;
import net.iceedge.icecore.basemodule.interfaces.panels.BottomExtrusionInterface;
import java.io.File;
import net.dirtt.icelib.ui.attribute.explorer.ui.entity.PossibleValue;
import java.util.Collection;
import net.iceedge.catalogs.icd.elevation.isometric.ICDIsometricAssemblyElevationEntity;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationEntity;
import net.dirtt.icelib.main.ElevationEntity;
import net.iceedge.icecore.basemodule.interfaces.lightweight.Paintable;
import net.dirtt.icebox.iceoutput.core.IceOutputTextNode;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import net.dirtt.icebox.canvas2d.Ice2DTextNode;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import javax.vecmath.Matrix4f;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.attributes.Attribute;
import com.iceedge.icd.utilities.ICDPathConstants;
import net.iceedge.icebox.utilities.ImagePool;
import java.io.IOException;
import net.dirtt.icelib.main.Catalog;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.ice3dviewer.IceMaterial;
import net.dirtt.utilities.GuiUtilities;
import net.dirtt.icelib.main.MaterialEntity;
import net.dirtt.icelib.main.TypeableEntity;
import net.dirtt.icelib.main.UV3Map;
import net.dirtt.icelib.main.CatalogMapping;
import org.xith3d.scenegraph.Shape3D;
import org.xith3d.scenegraph.Node;
import net.dirtt.utilities.Utility3D;
import java.util.Vector;
import java.util.LinkedList;
import java.awt.image.BufferedImage;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.dirtt.icelib.report.Report;
import net.iceedge.catalogs.icd.ICDILine;
import java.util.ArrayList;
import java.util.List;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelSubILineInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelSegmentInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.TopExtrusionInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.FrameInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelInterface;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.AdjustmentValue;
import net.iceedge.icecore.basemodule.interfaces.panels.StickLineInterface;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import java.util.Iterator;
import net.iceedge.icecore.basemodule.baseclasses.material.BasicMaterialEntity;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.undo.iceobjects.AttributeHashMapWithUndo;
import net.dirtt.icelib.main.OptionObject;
import org.apache.log4j.Logger;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.catalogs.icd.interfaces.ICDInstallTagDrawable;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintable;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicTile;

public class ICDTile extends BasicTile implements AssemblyPaintable, ICDInstallTagDrawable, ICDManufacturingReportable
{
    private static final String BOTTOM = "Bottom";
    private static final String ICD_SOLID_TILE_POSITION = "ICD_Solid_Tile_Position";
    private static final String ICD_TILE_BOTTOM = "ICD_Tile_Bottom";
    private static final long serialVersionUID = 8249844715933510297L;
    public static final String STACK_PANEL = "Stack Panel";
    public static final String PNL = "Pnl";
    public static int TOTAL_TUBE_WIDTH;
    private static Logger logger;
    protected static ICDTileSKUGenerator skuGenerator;
    protected String newSku;
    public static String PARENT_TYPE;
    public static final char[] priorityArray;
    private static final char FABRIC_FINISH = 'F';
    private static final Object ICD_ERROR_OPTION;
    private OptionObject currentCatalogOption;
    private AttributeHashMapWithUndo catalogAttributeHashMap;
    
    public ICDTile(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.newSku = "";
        this.currentCatalogOption = null;
        this.catalogAttributeHashMap = new AttributeHashMapWithUndo((EntityObject)this);
        this.setupNamedPoints();
        if (ICDTile.skuGenerator == null) {
            ICDTile.skuGenerator = new ICDTileSKUGenerator();
        }
    }
    
    public Object clone() {
        return this.buildClone(new ICDTile(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDTile buildClone(final ICDTile icdTile) {
        super.buildClone((TransformableTriggerUser)icdTile);
        return icdTile;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDTile(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDTile buildFrameClone(final ICDTile icdTile, final EntityObject entityObject) {
        super.buildFrameClone((BasicTile)icdTile, entityObject);
        return icdTile;
    }
    
    public boolean isNoTile() {
        final Iterator<BasicMaterialEntity> iterator = this.getChildrenByClass((Class)BasicMaterialEntity.class, false, true).iterator();
        while (iterator.hasNext()) {
            if ("noTile".equals(iterator.next().getAttributeValueAsString("Option_Indicator"))) {
                return true;
            }
        }
        return this.getAttributeValueAsBoolean("ICD_NoTileSKU", false);
    }
    
    protected float getMinTileHeight() {
        return 5.0f;
    }
    
    public boolean isHorizontallySplitable() {
        return false;
    }
    
    public void horizontalSplit(final int n, final Point3f point3f, final Point3f point3f2, final TypeObject typeObject, final boolean b, final boolean b2) {
        super.horizontalSplit(n, point3f, point3f2, typeObject, b, b2);
    }
    
    protected void setupNamedScales() {
        super.setupNamedScales();
    }
    
    protected void calculateNamedScales() {
        super.calculateNamedScales();
        this.getNamedScaleLocal("ASE_scale").set(this.getXDimension(), this.getYDimension(), 0.8f);
    }
    
    protected void setupNamedPoints() {
        super.setupNamedPoints();
        this.addNamedPoint("ASE_pos", new Point3f(0.0f, 1.0f, 0.0f));
        this.addNamedPoint("VHS", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("VHDL", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("VHDR", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("frontStartBottomCornerLocal", new Point3f());
        this.addNamedPoint("frontEndBottomCornerLocal", new Point3f());
        this.addNamedPoint("frontStartTopCornerLocal", new Point3f());
        this.addNamedPoint("frontEndTopCornerLocal", new Point3f());
        this.addNamedPoint("LH_SUBTILE", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("RH_SUBTILE", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("ASE_VAL_DOOR", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("NT_NamedPoint", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedRotation("VHS_ROT", new Vector3f(0.0f, 0.0f, 0.0f));
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        final float n = 0.5f + this.getWidth() / 100.0f;
        if (this.isStackedTile()) {
            this.getNamedPointLocal("ASE_pos").set(n, 0.2f, 0.0f);
        }
        else {
            this.getNamedPointLocal("ASE_pos").set(n, 1.25f, 0.0f);
        }
        if (this.isValetDoorTile()) {
            this.calculateValetDoorASE(n);
            this.calculateValetDoorHandlePosition();
        }
        final Point3f namedPointLocal = this.getNamedPointLocal("geometricCenterPoint");
        if (namedPointLocal != null) {
            this.getNamedPointLocal("frontStartBottomCornerLocal").set(namedPointLocal.x - this.getXDimension() / 2.0f, namedPointLocal.y - this.getYDimension() / 2.0f, namedPointLocal.z + this.getZDimension() / 2.0f);
            this.getNamedPointLocal("frontEndBottomCornerLocal").set(namedPointLocal.x + this.getXDimension() / 2.0f, namedPointLocal.y - this.getYDimension() / 2.0f, namedPointLocal.z + this.getZDimension() / 2.0f);
            this.getNamedPointLocal("frontStartTopCornerLocal").set(namedPointLocal.x - this.getXDimension() / 2.0f, namedPointLocal.y + this.getYDimension() / 2.0f, namedPointLocal.z + this.getZDimension() / 2.0f);
            this.getNamedPointLocal("frontEndTopCornerLocal").set(namedPointLocal.x + this.getXDimension() / 2.0f, namedPointLocal.y + this.getYDimension() / 2.0f, namedPointLocal.z + this.getZDimension() / 2.0f);
            this.getNamedPointLocal("NT_NamedPoint").set(namedPointLocal.x - 2.0f, namedPointLocal.y + 2.0f, namedPointLocal.z);
            if (this.isValetDoorTile()) {
                this.calculateDoubleValetDoorSubTilePosition();
            }
            else {
                this.getNamedPointLocal("RH_SUBTILE").set(this.getXDimension() / 2.0f, 0.0f, 0.0f);
            }
        }
    }
    
    private void calculateValetDoorASE(final float n) {
        if (this.isStackedTile()) {
            this.getNamedPointLocal("ASE_VAL_DOOR").set(n, 0.0f, 0.0f);
        }
        else {
            this.getNamedPointLocal("ASE_VAL_DOOR").set(n, 1.0f, 0.0f);
        }
    }
    
    private void calculateDoubleValetDoorSubTilePosition() {
        if (this.isStackedTile()) {
            this.getNamedPointLocal("LH_SUBTILE").set(0.5f, -0.25f, 0.0f);
            this.getNamedPointLocal("RH_SUBTILE").set(this.getXDimension() / 2.0f + 0.5f, -0.25f, 0.0f);
        }
        else {
            this.getNamedPointLocal("LH_SUBTILE").set(0.5f, 0.75f, 0.0f);
            this.getNamedPointLocal("RH_SUBTILE").set(this.getXDimension() / 2.0f + 0.5f, 0.75f, 0.0f);
        }
    }
    
    private void calculateValetDoorHandlePosition() {
        if (this.isSingleValetDoor()) {
            float n = 3.0f;
            final float n2 = this.getHeight() / 2.0f;
            this.getNamedRotationLocal("VHS_ROT").set(-1.5707964f, 0.0f, 1.5707964f);
            if (!this.isRightHanded()) {
                n = this.getWidth() - n + 1.0f;
            }
            this.getNamedPointLocal("VHS").set(n, n2, 0.0f);
        }
        else {
            this.getNamedPointLocal("VHDL").set(this.getWidth() / 2.0f - 2.0f, this.getHeight() / 2.0f, 0.0f);
            this.getNamedPointLocal("VHDR").set(this.getWidth() / 2.0f + 3.0f, this.getHeight() / 2.0f, 0.0f);
        }
    }
    
    public boolean isRightHanded() {
        return "Right Hand".equals(this.getAttributeValueAsString("Valet_Door_Hand_Indicator"));
    }
    
    protected boolean shouldUseNewFinishSystem() {
        return true;
    }
    
    protected boolean allowSplitVertically() {
        return this.getXDimension() > this.getTileMinWidth() * 2.0f + 2.0f && !this.isInCorePanel();
    }
    
    protected float getTileMinWidth() {
        return 4.0f;
    }
    
    public AdjustmentValue getAdjustmentValue(final StickLineInterface stickLineInterface, final int n) {
        if (!this.isValetDoorTile()) {
            return super.getAdjustmentValue(stickLineInterface, n);
        }
        return this.getValetDoorTileAdjustmentValue(stickLineInterface, n);
    }
    
    private AdjustmentValue getValetDoorTileAdjustmentValue(final StickLineInterface stickLineInterface, final int n) {
        AdjustmentValue adjustmentValue = new AdjustmentValue((OptionObject)null, 0.0f);
        if (stickLineInterface != null) {
            if (n == 2) {
                if (this.isInCorePanel()) {
                    adjustmentValue = new AdjustmentValue((OptionObject)null, -19.05f);
                }
                else {
                    adjustmentValue = new AdjustmentValue((OptionObject)null, 6.35f);
                }
            }
            else {
                adjustmentValue = super.getAdjustmentValue(stickLineInterface, n);
            }
        }
        return adjustmentValue;
    }
    
    protected Float getVerticalSplitLocation() {
        return super.getVerticalSplitLocation() - 1.75f;
    }
    
    public boolean withoutFrame() {
        return this.getCurrentOption() == Solution.optionObjectByName("ICD_No_Tile_WithoutFrame");
    }
    
    public void breakTopExtrusion() {
        if (!this.isInCorePanel() && this.getStartEdge() != null) {
            final PanelInterface parentPanel = this.getParentPanel();
            if (parentPanel != null) {
                final FrameInterface physicalFrame = parentPanel.getPhysicalFrame();
                final boolean frameExtrusion = this.getStartEdge().isFrameExtrusion();
                final float n = 0.25f;
                final float n2 = -0.5f;
                final float n3 = this.isNoTile() ? n2 : n;
                float n4;
                if (frameExtrusion) {
                    n4 = this.getEndBottomCornerInTileGroupSpace().x + n3;
                }
                else {
                    n4 = this.getStartBottomCornerInTileGroupSpace().x - n3;
                }
                if (physicalFrame != null) {
                    final TopExtrusionInterface topExtrusion = physicalFrame.getTopExtrusion();
                    if (topExtrusion instanceof ICDTopExtrusion) {
                        ((ICDTopExtrusion)topExtrusion).breakHorizontalExtrusion(n4, true);
                        ((ICDTopExtrusion)topExtrusion).setFakeExtrusion(frameExtrusion);
                        ((ICDTopExtrusion)topExtrusion).setChildrenModified(true);
                    }
                }
                final PanelSegmentInterface parentPanelSegment = parentPanel.getParentPanelSegment();
                if (parentPanelSegment != null) {
                    final PanelSubILineInterface parentPanelSubILine = parentPanelSegment.getParentPanelSubILine();
                    if (parentPanelSubILine != null) {
                        final PanelInterface childPanel = parentPanelSubILine.getChildPanelAt(parentPanelSegment.getBasePoint3f().x - 3.0f);
                        if (childPanel != null) {
                            final FrameInterface physicalFrame2 = childPanel.getPhysicalFrame();
                            if (physicalFrame2 != null) {
                                final TopExtrusionInterface topExtrusion2 = physicalFrame2.getTopExtrusion();
                                if (topExtrusion2 instanceof ICDTopExtrusion) {
                                    ((ICDTopExtrusion)topExtrusion2).breakHorizontalExtrusion(n4, true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void removeBreakTopExtrusion() {
        if (!this.isInCorePanel()) {
            final ICDTileGroup icdTileGroup = (ICDTileGroup)this.getParent((Class)ICDTileGroup.class);
            if (icdTileGroup != null) {
                icdTileGroup.removeBreakTopExtrusion();
            }
        }
    }
    
    public boolean isNoFrameTile() {
        return "No Tile, Without Frame".equals(this.getAttributeValueAsString("Tile_Indicator"));
    }
    
    public boolean isRealGlassTile() {
        return "Glass Tile".equals(this.getAttributeValueAsString("Tile_Indicator"));
    }
    
    protected void validateIndicators() {
        super.validateIndicators();
        this.applyChangesForAttribute("ICD_Solid_Tile_Position", this.isBottomTileInBasePanel() ? "Bottom" : "Top");
        this.applyChangesForAttribute("ICD_Tile_Chase_Indicator", this.getTileChaseValue());
    }
    
    private String getTileChaseValue() {
        if (((EntityObject)this.getParent((Class)ICDPanel.class)) instanceof ICDSubFrameSideContainer) {
            return "Chase";
        }
        return "Regular";
    }
    
    public int getVerticalLocation() {
        int n;
        if (this.isBottomTileInBasePanel()) {
            n = 2;
        }
        else {
            n = 1;
        }
        return n;
    }
    
    public char[] getTileFinishCode() {
        final List<String> codeList = this.getCodeList();
        final char[] array = new char[codeList.size()];
        for (int i = 0; i < codeList.size(); ++i) {
            array[i] = codeList.get(i).charAt(0);
        }
        return array;
    }
    
    public List<String> getCodeList() {
        final ArrayList<String> list = new ArrayList<String>();
        for (final BasicMaterialEntity basicMaterialEntity : this.getChildrenByClass((Class)BasicMaterialEntity.class, false)) {
            for (int i = 1; i <= 2; ++i) {
                final String attributeValueAsString = basicMaterialEntity.getAttributeValueAsString("TILE_FINISH_SIDE_" + i + "_POSITION_" + this.getVerticalLocation());
                if (attributeValueAsString != null && attributeValueAsString.length() > 0) {
                    list.add(attributeValueAsString);
                }
            }
        }
        return list;
    }
    
    public String getSKUForXML() {
        String s = super.getSKUForXML();
        if (this.isStackedTile()) {
            s = this.newSku;
        }
        return s;
    }
    
    public String getSkuForQuote() {
        String s = super.getSKU();
        if (this.isStackedTile()) {
            s = this.newSku;
        }
        return s;
    }
    
    public String getFinishCodeForSkuGeneration() {
        String highestPriorityCode = "?";
        final String attributeValueAsString = this.getAttributeValueAsString("Tile_Indicator");
        if ("Hardboard Tile".equalsIgnoreCase(attributeValueAsString)) {
            highestPriorityCode = getHighestPriorityCode(this.getTileFinishCode());
        }
        else if (this.isNoTile() || this.isValetDoorTile()) {
            highestPriorityCode = "O";
        }
        else if ("Glass Tile".equalsIgnoreCase(attributeValueAsString)) {
            final EntityObject childByClass = this.getChildByClass((Class)BasicMaterialEntity.class);
            if (childByClass != null) {
                final String attributeValueAsString2 = childByClass.getAttributeValueAsString("GLASS_TILE_FINISH_POSITION_1");
                if (attributeValueAsString2 == null) {
                    childByClass.getAttributeValueAsString("GLASS_TILE_FINISH_POSITION_2");
                }
                if (attributeValueAsString2 != null) {
                    final char char1 = attributeValueAsString2.charAt(0);
                    if (char1 == 'A') {
                        highestPriorityCode = "G";
                    }
                    else if (char1 == 'G') {
                        highestPriorityCode = "Z";
                    }
                }
            }
        }
        else if ("Acoustic Tile".equalsIgnoreCase(attributeValueAsString)) {
            highestPriorityCode = "A";
        }
        else if ("Tackboard Tile".equalsIgnoreCase(attributeValueAsString)) {
            highestPriorityCode = "T";
        }
        return highestPriorityCode;
    }
    
    public static String getHighestPriorityCode(final char... array) {
        int length = ICDTile.priorityArray.length;
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < ICDTile.priorityArray.length; ++j) {
                if (ICDTile.priorityArray[j] == array[i] && j < length) {
                    length = j;
                    break;
                }
            }
        }
        return (length != ICDTile.priorityArray.length) ? String.valueOf(ICDTile.priorityArray[length]) : "?";
    }
    
    public boolean isQuotable() {
        final ICDILine icdiLine = (ICDILine)this.getParentILine();
        if (icdiLine == null) {
            return false;
        }
        if (icdiLine.isVerticalChase()) {
            return false;
        }
        if (!this.isNoFrameTile()) {
            return this.isStackedTile();
        }
        return this.isStackedTile() && !this.isNoTile();
    }
    
    public int getQuoteReportLevel() {
        final ICDILine icdiLine = (ICDILine)this.getParentILine();
        if (icdiLine == null) {
            return -1;
        }
        if (icdiLine.isVerticalChase()) {
            return -1;
        }
        if (!this.isNoFrameTile()) {
            return this.isStackedTile() ? 1 : -1;
        }
        return (this.isStackedTile() && !this.isNoTile()) ? 1 : -1;
    }
    
    public String getSubType() {
        return super.getSubType();
    }
    
    public String getTypeForQuote() {
        return this.isStackedTile() ? "Pnl" : this.getAttributeValueAsString("Product_Type");
    }
    
    public String getSubTypeForQuote() {
        return this.isStackedTile() ? "Stack Panel" : super.getSubType();
    }
    
    protected boolean shouldUsePolygon() {
        return false;
    }
    
    public boolean shouldAddChildrenToReport(final Report report) {
        return super.shouldAddChildrenToReport(report) && !this.isStackedTile();
    }
    
    public BufferedImage createNewElevationImage(final Ice2DContainer ice2DContainer) {
        BufferedImage newElevationImage = null;
        IceMaterial iceMaterial = null;
        final List finishRoots = this.getFinishRoots();
        final LinkedList list = new LinkedList();
        final Vector vector = new Vector();
        this.model3D = this.get3DModel();
        final Vector shapesBySubstring = Utility3D.findShapesBySubstring((Node)this.model3D, "_UV3", vector);
        String s;
        if (this.getSide() == 0) {
            s = shapesBySubstring.get(0).getName();
        }
        else {
            s = shapesBySubstring.get(1).getName();
        }
        final String uv3FinishSignatureKeyFromShapeName = Utility3D.getUV3FinishSignatureKeyFromShapeName(s);
        if (iceMaterial == null) {
            if (uv3FinishSignatureKeyFromShapeName != null) {
                iceMaterial = this.findMaterial(uv3FinishSignatureKeyFromShapeName, true);
            }
            if (iceMaterial == null) {
                iceMaterial = this.findMaterial(uv3FinishSignatureKeyFromShapeName);
            }
        }
        Label_0295: {
            if (iceMaterial == null) {
                for (final UV3Map uv3Map : CatalogMapping.getUV3Map("GLASS-FINISH")) {
                    final Iterator<TypeableEntity> iterator2 = finishRoots.iterator();
                    while (iterator2.hasNext()) {
                        for (final EntityObject entityObject : iterator2.next().getChildrenVector()) {
                            if (list.contains(entityObject)) {
                                continue;
                            }
                            if (!(entityObject instanceof MaterialEntity)) {
                                continue;
                            }
                            iceMaterial = this.findFinish((TypeableEntity)entityObject, uv3Map, false, 0, true);
                            if (iceMaterial != null) {
                                break Label_0295;
                            }
                        }
                    }
                }
            }
        }
        if (iceMaterial != null) {
            newElevationImage = GuiUtilities.createNewElevationImage(ice2DContainer, iceMaterial.getTextureMapName(), iceMaterial.getUScale(), iceMaterial.getVScale(), iceMaterial.getUvRotation());
        }
        return newElevationImage;
    }
    
    public float getUnitPrice() {
        return Float.valueOf(this.currentCatalogOption.getAttributeValueAsString("P1"));
    }
    
    public BufferedImage createElevationImage(final Ice2DContainer ice2DContainer, final int n) {
        BufferedImage elevationImage = null;
        IceMaterial iceMaterial = null;
        final List finishRoots = this.getFinishRoots();
        final LinkedList list = new LinkedList();
        final Vector vector = new Vector();
        this.model3D = this.get3DModel();
        final Vector shapesBySubstring = Utility3D.findShapesBySubstring((Node)this.model3D, "_UV3", vector);
        if (!shapesBySubstring.isEmpty()) {
            String s;
            if (n == 1) {
                s = shapesBySubstring.get(0).getName();
            }
            else {
                s = shapesBySubstring.get(1).getName();
            }
            final String uv3FinishSignatureKeyFromShapeName = Utility3D.getUV3FinishSignatureKeyFromShapeName(s);
            if (iceMaterial == null) {
                if (uv3FinishSignatureKeyFromShapeName != null) {
                    iceMaterial = this.findMaterial(uv3FinishSignatureKeyFromShapeName, true);
                }
                if (iceMaterial == null) {
                    iceMaterial = this.findMaterial(uv3FinishSignatureKeyFromShapeName);
                }
            }
        }
        Label_0302: {
            if (iceMaterial == null) {
                for (final UV3Map uv3Map : CatalogMapping.getUV3Map("GLASS-FINISH")) {
                    final Iterator<TypeableEntity> iterator2 = finishRoots.iterator();
                    while (iterator2.hasNext()) {
                        for (final EntityObject entityObject : iterator2.next().getChildrenVector()) {
                            if (list.contains(entityObject)) {
                                continue;
                            }
                            if (!(entityObject instanceof MaterialEntity)) {
                                continue;
                            }
                            iceMaterial = this.findFinish((TypeableEntity)entityObject, uv3Map, false, 0, true);
                            if (iceMaterial != null) {
                                break Label_0302;
                            }
                        }
                    }
                }
            }
        }
        if (iceMaterial != null) {
            elevationImage = GuiUtilities.getElevationImage(ice2DContainer, iceMaterial);
        }
        return elevationImage;
    }
    
    public void draw2DElevation(final int n, final Ice2DContainer ice2DContainer, final boolean b, final SolutionSetting solutionSetting) {
        super.draw2DElevation(n, ice2DContainer, b, solutionSetting);
        this.drawHandleElevation(n, ice2DContainer, b, solutionSetting);
    }
    
    public void drawHandleElevation(final int n, final Ice2DContainer ice2DContainer, final boolean b, final SolutionSetting solutionSetting) {
    }
    
    public boolean isStackedTile() {
        final PanelInterface parentPanel = this.getParentPanel();
        return parentPanel != null && !parentPanel.isCorePanel();
    }
    
    public boolean isBottomTile() {
        final String attributeValueAsString = this.getAttributeValueAsString("ICD_Solid_Tile_Position");
        return attributeValueAsString != null && attributeValueAsString.equals("Bottom");
    }
    
    public boolean isSlopedTile() {
        return this.getAttributeValueAsBoolean("IS_ICD_Slope_Tile", false);
    }
    
    public void solve() {
        super.solve();
        this.handleSKUGeneration();
    }
    
    protected boolean shouldGenerateSku() {
        return this.isStackedTile();
    }
    
    protected void handleSKUGeneration() {
        if (this.shouldGenerateSku()) {
            this.newSku = ICDTile.skuGenerator.generateSKU((TypeableEntity)this);
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
                        this.createNewAttribute("Base_Price", this.currentCatalogOption.getAttributeValueAsString("P1"));
                        this.createNewAttribute("Base_SKU", this.newSku);
                        if (this.isStackedTile()) {
                            this.createNewAttribute("SubType", ((ICDPanel)this.getParent((Class)ICDPanel.class)).getSubType());
                        }
                    }
                    else {
                        this.currentCatalogOption = Solution.getWorldOptions().get("ICD_Tile_Error_Option");
                        this.createNewAttribute("Base_SKU", "NA");
                    }
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public String getQuoteDescription() {
        String s = this.getDescription();
        if (this.isStackedTile()) {
            String s2;
            if (this.currentCatalogOption != null && this.shouldGenerateSku()) {
                s2 = this.currentCatalogOption.getDescription();
                if ("ICD_Tile_Error_Option".equalsIgnoreCase(this.currentCatalogOption.getId())) {
                    s2 = s2 + "-" + this.newSku;
                }
            }
            else {
                s2 = this.getDescription();
            }
            s = s2 + " (Actual Size " + (Math.round(this.getHeight()) + ICDTile.TOTAL_TUBE_WIDTH) + "h " + (Math.round(this.getWidth()) + ICDTile.TOTAL_TUBE_WIDTH) + "w)";
        }
        return s;
    }
    
    public boolean isValetDoorTile() {
        return ICDTile.PARENT_TYPE.equals(this.getCurrentOption().getAttributeValueAsString("Tile_Indicator"));
    }
    
    public boolean isSingleValetDoor() {
        return "Single".equals(this.getCurrentOption().getAttributeValueAsString("ICD_Valet_Door_Type"));
    }
    
    public boolean isHeavyDutySlidingDoor() {
        return "Heavy Duty Sliding Door".equals(this.getCurrentOption().getAttributeValueAsString("Tile_Indicator"));
    }
    
    public boolean isRegularSlidingDoor() {
        return "Sliding Door".equals(this.getCurrentOption().getAttributeValueAsString("Tile_Indicator"));
    }
    
    public boolean isSlidingDoor() {
        return this.isHeavyDutySlidingDoor() || this.isRegularSlidingDoor();
    }
    
    public boolean isGlassTile() {
        return super.isGlassTile();
    }
    
    public boolean usePlotGVT() {
        return false;
    }
    
    public BufferedImage createNoElevationImage() {
        if (this.isNoTile() || this.isNoTileWithFrame()) {
            BufferedImage image = null;
            try {
                image = ImagePool.getImagePool().getImage(ICDPathConstants.ICD_TEXTURES + "clear.png", true);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            return image;
        }
        return super.createNoElevationImage();
    }
    
    public float getCutLength() {
        final float height = super.getHeight();
        if (this.isAcusticTile()) {
            return height - 1.0f;
        }
        return height - this.getFabricPullback();
    }
    
    public float getCutWidth() {
        final float width = super.getWidth();
        if (this.isAcusticTile()) {
            return width - 1.0f;
        }
        return width - this.getFabricPullback();
    }
    
    public float getFabricWidth() {
        return super.getWidth() + this.getAttributeValueAsFloat("fabric_Cover_Oversize");
    }
    
    public float getFabricLength() {
        return super.getLength() + this.getAttributeValueAsFloat("fabric_Cover_Oversize");
    }
    
    public float getFabricPullback() {
        float attributeValueAsFloat = 0.0f;
        if (this.isFabricFinish()) {
            attributeValueAsFloat = this.getAttributeValueAsFloat("fabricTilePullback");
        }
        return attributeValueAsFloat;
    }
    
    public boolean isFabricFinish() {
        final String attributeValueAsString = this.getAttributeValueAsString("Tile_Indicator");
        if ("Glass Tile".equalsIgnoreCase(attributeValueAsString) || "Sliding Door".equalsIgnoreCase(attributeValueAsString)) {
            return false;
        }
        final char[] tileFinishCode = this.getTileFinishCode();
        for (int length = tileFinishCode.length, i = 0; i < length; ++i) {
            if (tileFinishCode[i] == 'F') {
                return true;
            }
        }
        return false;
    }
    
    public boolean isAcusticTile() {
        return "Acoustic Tile".equalsIgnoreCase(this.getAttributeValueAsString("Tile_Indicator"));
    }
    
    public boolean useTransformablesDraw3D() {
        return true;
    }
    
    public boolean isChaseMasterTile() {
        boolean equals = false;
        final Attribute attributeObject = this.getAttributeObject("ICD_Tile_Chase_Indicator");
        if (attributeObject != null) {
            equals = "Chase".equals(attributeObject.getValueAsString());
        }
        return equals;
    }
    
    public boolean shouldDraw() {
        return !this.isSubTiled();
    }
    
    public boolean shouldDraw2DFabricCuttingElevationTags() {
        return true;
    }
    
    public Vector<Ice2DPaintableNode> getAssemblyIcons(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        final Vector<Ice2DTextNode> vector = (Vector<Ice2DTextNode>)new Vector<Ice2DPaintableNode>();
        if (this.getCurrentOption().getId().contains("No_Tile")) {
            final Matrix4f matrix4f2 = new Matrix4f();
            matrix4f2.setIdentity();
            matrix4f2.mul(this.getEntWorldSpaceMatrix());
            final Matrix4f matrix4f3 = new Matrix4f();
            matrix4f3.setIdentity();
            matrix4f3.setTranslation(new Vector3f(this.getXDimension() / 2.0f, this.getYDimension() / 2.0f, 0.0f));
            matrix4f2.mul(matrix4f3);
            final Ice2DTextNode e = new Ice2DTextNode(this.getLayerName(), (TransformableEntity)this, matrix4f2, "No Tile", 3);
            e.setCentered(true);
            vector.add((Ice2DPaintableNode)e);
        }
        return (Vector<Ice2DPaintableNode>)vector;
    }
    
    public List<IceOutputNode> getPlotOutputNodes(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        final Vector<IceOutputTextNode> vector = (Vector<IceOutputTextNode>)new Vector<IceOutputNode>();
        if (this.getCurrentOption().getId().contains("No_Tile")) {
            final Matrix4f matrix4f2 = new Matrix4f();
            matrix4f2.setIdentity();
            matrix4f2.mul(this.getEntWorldSpaceMatrix());
            final Matrix4f matrix4f3 = new Matrix4f();
            matrix4f3.setIdentity();
            matrix4f3.setTranslation(new Vector3f(this.getXDimension() / 2.0f, this.getYDimension() / 2.0f, 0.0f));
            matrix4f2.mul(matrix4f3);
            vector.add(new IceOutputTextNode((Paintable)null, "No Tile", 3.0f, 1.0f, 0.0f, new Point3f(this.getZDimension() / 2.0f, 0.0f, 0.0f), matrix4f2));
        }
        return (List<IceOutputNode>)vector;
    }
    
    public boolean shouldDrawAssembly() {
        return true;
    }
    
    public boolean isAssembled() {
        return true;
    }
    
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        if (elevationEntity instanceof ICDAssemblyElevationEntity || elevationEntity instanceof ICDIsometricAssemblyElevationEntity) {
            final Vector<String> vector = new Vector<String>();
            if (this.isNoTileWithFrame()) {
                vector.add("TX:SS(" + "NT" + ":" + 5 + ":" + 0.0f + ":" + 2 + ":NT_NamedPoint)");
            }
            else {
                this.addMultiTagToScript((Vector)vector);
            }
            return vector;
        }
        if (!this.isNoTile() && !this.isNoFrameTile()) {
            return (Vector<String>)super.getCadElevationScript(elevationEntity);
        }
        return new Vector<String>();
    }
    
    public boolean isPanelWithHorisontalInnerExtrusion() {
        boolean withHorizontalInnerExtrusion = false;
        final ICDPanel icdPanel = (ICDPanel)this.getParent((Class)ICDPanel.class);
        if (icdPanel != null) {
            withHorizontalInnerExtrusion = icdPanel.withHorizontalInnerExtrusion();
        }
        return withHorizontalInnerExtrusion;
    }
    
    public Collection<PossibleValue> getAttributePossibleValues(final String s, final boolean b, final TypeObject typeObject) {
        final Collection attributePossibleValues = super.getAttributePossibleValues(s, b, typeObject);
        if (s.equals("Tile_Indicator") && !this.allowNoTileWithoutFrame()) {
            attributePossibleValues.remove(new PossibleValue("No Tile, Without Frame", "No Tile, Without Frame", (File)null));
        }
        return (Collection<PossibleValue>)attributePossibleValues;
    }
    
    private boolean allowNoTileWithoutFrame() {
        final PanelInterface parentPanel = this.getParentPanel();
        final ICDTileGroup icdTileGroup = (ICDTileGroup)this.getParent((Class)ICDTileGroup.class);
        return this.isBottomTileInBasePanel() || (parentPanel != null && !parentPanel.isCorePanel() && icdTileGroup.getChildrenByClass((Class)ICDTile.class, true, true).size() > 1);
    }
    
    public void applyChangesFromEditor(final String s, final PossibleValue possibleValue, final Collection<PossibleValue> collection, final Collection<String> collection2, final String s2) {
        super.applyChangesFromEditor(s, possibleValue, (Collection)collection, (Collection)collection2, s2);
        if (this.isBottomTileInBasePanel() && s.equals("Tile_Indicator")) {
            final PanelInterface parentPanel = this.getParentPanel();
            if (parentPanel != null) {
                final FrameInterface physicalFrame = parentPanel.getPhysicalFrame();
                if (physicalFrame != null) {
                    final BottomExtrusionInterface bottomExtrusion = physicalFrame.getBottomExtrusion();
                    if (bottomExtrusion != null) {
                        this.modifyCurrentOption();
                        bottomExtrusion.setModified(true);
                    }
                }
            }
        }
        if (s.equals("Valet_Door_Hand_Indicator")) {
            this.setChildrenModified(true);
        }
    }
    
    public void getManufacturingInfo(final TreeMap<String, String> treeMap) {
        super.getManufacturingInfo((TreeMap)treeMap);
        treeMap.put("TileIndicator", this.getInstallTag());
        if (this.getAttributeValueAsString("Valet_Door_Hand_Indicator") != null) {
            treeMap.put("Description", this.getDescription() + ", " + this.getAttributeValueAsString("Valet_Door_Hand_Indicator"));
        }
    }
    
    public String getInstallTag() {
        String installTag = "";
        final Solution solution = this.getSolution();
        if (solution != null) {
            final Report report = solution.getReport(51);
            if (report != null) {
                installTag = ((ICDManufacturingReport)report).getInstallTag((EntityObject)this);
            }
        }
        return installTag;
    }
    
    public String getMultiTags(final SolutionSetting solutionSetting) {
        String str = super.getMultiTags(solutionSetting);
        if (solutionSetting.isShowICDTileInstallationTag()) {
            str = str + "\n" + this.getInstallTag();
        }
        return str;
    }
    
    public void handleAttributeChange(final String s, final String s2) {
        ICDUtilities.handleAttributeChange((EntityObject)this, s, s2);
    }
    
    public void collectExtraIndirectAssemblyParts(final boolean b, final HashSet<EntityObject> set, final boolean b2, final Class<EntityObject>[] array) {
        set.addAll((Collection<?>)this.getChildrenByClass((Class)ICDInnerExtrusion.class, true, true));
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
        ICDTile.TOTAL_TUBE_WIDTH = 1;
        ICDTile.logger = Logger.getLogger((Class)ICDTile.class);
        ICDTile.PARENT_TYPE = "Valet Door";
        priorityArray = new char[] { 'L', 'F', 'M' };
        ICD_ERROR_OPTION = "ICD_Panel_Error_Option";
    }
}
