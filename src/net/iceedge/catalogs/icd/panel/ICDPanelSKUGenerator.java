package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.main.CatalogManager;
import net.dirtt.icelib.main.EntityObject;
import net.iceedge.icebox.ui.catalogChooser.Manufacturer;
import net.dirtt.icelib.main.OptionObject;
import java.util.Vector;
import net.iceedge.icecore.basemodule.interfaces.panels.TileInterface;
import net.dirtt.icelib.main.TypeableEntity;
import net.iceedge.icebox.dataimport.sif.Part;
import java.util.Iterator;
import net.iceedge.icebox.dataimport.sif.CatalogBroker;
import java.io.IOException;
import java.util.TreeSet;
import net.dirtt.icelib.main.TableOfContents;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.Catalog;
import java.util.SortedSet;
import java.util.HashMap;
import net.iceedge.icecore.basemodule.baseclasses.SkuGeneratable;

public class ICDPanelSKUGenerator implements SkuGeneratable
{
    static HashMap<String, SortedSet<Integer>> panelTypeHeight;
    static HashMap<String, SortedSet<Integer>> panelTypeWidth;
    
    public ICDPanelSKUGenerator() {
        this.initializeCatalog();
    }
    
    private void initializeCatalog() {
        if (ICDPanelSKUGenerator.panelTypeHeight == null || ICDPanelSKUGenerator.panelTypeWidth == null) {
            final Catalog catalog = Solution.getCatalogs().get("ICI_ICD");
            if (catalog != null) {
                ICDPanelSKUGenerator.panelTypeHeight = new HashMap<String, SortedSet<Integer>>();
                ICDPanelSKUGenerator.panelTypeWidth = new HashMap<String, SortedSet<Integer>>();
                try {
                    final CatalogBroker broker = catalog.getBroker();
                    TableOfContents tableOfContents = null;
                    for (final TableOfContents tableOfContents2 : catalog.getTableOfContents()) {
                        if ("Pnl".equals(tableOfContents2.getDescription())) {
                            tableOfContents = tableOfContents2;
                            break;
                        }
                    }
                    if (tableOfContents == null) {
                        return;
                    }
                    for (final TableOfContents tableOfContents3 : tableOfContents.getChildren()) {
                        final String description = tableOfContents3.getDescription();
                        SortedSet<Integer> value = ICDPanelSKUGenerator.panelTypeHeight.get(description);
                        SortedSet<Integer> value2 = ICDPanelSKUGenerator.panelTypeWidth.get(description);
                        if (value == null) {
                            value = new TreeSet<Integer>();
                            ICDPanelSKUGenerator.panelTypeHeight.put(description, value);
                        }
                        if (value2 == null) {
                            value2 = new TreeSet<Integer>();
                            ICDPanelSKUGenerator.panelTypeWidth.put(description, value2);
                        }
                        final Iterator<TableOfContents.PartItem> iterator3 = tableOfContents3.getPartItems().iterator();
                        while (iterator3.hasNext()) {
                            final Part part = broker.getPart(iterator3.next().getPartName());
                            final String attributeValue = part.getAttributeValue("Height", (String)null);
                            final String attributeValue2 = part.getAttributeValue("Width", (String)null);
                            if (attributeValue != null) {
                                value.add((int)Float.parseFloat(attributeValue));
                            }
                            if (attributeValue2 != null) {
                                value2.add((int)Float.parseFloat(attributeValue2));
                            }
                        }
                    }
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public String generateSKU(final TypeableEntity typeableEntity) {
        return this.generateSKU(typeableEntity, false);
    }
    
    public String generateSKU(final TypeableEntity typeableEntity, final boolean b) {
        String s = "";
        if (typeableEntity instanceof ICDPanel) {
            final ICDPanel.PANEL_TYPE panelType = ((ICDPanel)typeableEntity).getPanelType();
            switch (panelType) {
                case CHASE:
                case CHASE_POWERED:
                case FULL:
                case SPLIT:
                case SUPPORT_FRAME: {
                    s = this.calculateSKUForRegularPanels((ICDPanel)typeableEntity, panelType.getPanelName(), b);
                    break;
                }
                case CURVED_PANEL: {
                    s = this.calculateSKUForCurvedPanels((ICDPanel)typeableEntity, panelType.getPanelName(), b);
                    break;
                }
                case ANGLED: {
                    s = this.calculateSKUForAngledPanels((ICDPanel)typeableEntity, panelType.getPanelName(), b);
                    break;
                }
                case STACK_PANEL: {
                    s = this.calculateSKUForStepReturnPanels((ICDPanel)typeableEntity, panelType.getPanelName(), b);
                    break;
                }
                case DOOR: {
                    s = this.calculateSKUForDoorPanels((ICDPanel)typeableEntity, panelType.getPanelName(), b);
                    break;
                }
                case VALET_DOOR: {
                    s = this.calculateSKUForValetDoorTiles((ICDPanel)typeableEntity, panelType.getPanelName(), b);
                    break;
                }
                case SLIDING_DOOR:
                case HD_SLIDING_DOOR: {
                    s = this.calculateSKUForSlidingDoor((ICDPanel)typeableEntity, panelType.getPanelName(), b);
                    break;
                }
                default: {
                    System.err.println("Unknown panel type");
                    break;
                }
            }
        }
        this.relinkCatalogPart(s, typeableEntity);
        return s;
    }
    
    private String calculateSKUForCurvedPanels(final ICDPanel icdPanel, final String s, final boolean b) {
        final String regularPanelFinishCode = this.getRegularPanelFinishCode(icdPanel);
        String str = "CP";
        if (icdPanel.withHorizontalInnerExtrusion()) {
            str = "SCP";
        }
        return regularPanelFinishCode + str + this.getHeightForRegularPanels(icdPanel, s, b) + (b ? "x" : "") + this.getWidthForRegularPanels(icdPanel, s, b);
    }
    
    private String calculateSKUForAngledPanels(final ICDPanel icdPanel, final String s, final boolean b) {
        return "A" + this.getRegularPanelFinishCode(icdPanel) + this.getHeightForRegularPanels(icdPanel, s, b) + (b ? "x" : "") + this.getWidthForRegularPanels(icdPanel, s, b);
    }
    
    private String calculateSKUForSlidingDoor(final ICDPanel icdPanel, final String s, final boolean b) {
        final StringBuffer sb = new StringBuffer();
        final TileInterface aTile = icdPanel.getATile();
        if (aTile instanceof ICDTile) {
            if (((ICDTile)aTile).isHeavyDutySlidingDoor()) {
                sb.append("HS");
            }
            else {
                sb.append("SD");
            }
        }
        sb.append(this.getHeightForRegularPanels(icdPanel, s, b) + (b ? "x" : "") + this.getWidthForRegularPanels(icdPanel, s, b));
        return sb.toString();
    }
    
    private String calculateSKUForValetDoorTiles(final ICDPanel icdPanel, final String s, final boolean b) {
        final StringBuffer sb = new StringBuffer();
        sb.append("V");
        final TileInterface aTile = icdPanel.getATile();
        if (aTile instanceof ICDTile) {
            if (((ICDTile)aTile).isSingleValetDoor()) {
                sb.append("S");
            }
            else {
                sb.append("D");
            }
        }
        sb.append(this.getHeightForRegularPanels(icdPanel, s, b) + (b ? "x" : "") + this.getWidthForRegularPanels(icdPanel, s, b));
        sb.append("L");
        return sb.toString();
    }
    
    private String calculateSKUForStepReturnPanels(final ICDPanel icdPanel, final String s, final boolean b) {
        return "SP" + this.getHeightForRegularPanels(icdPanel, s, b) + (b ? "x" : "") + this.getWidthForStepReturnPanels(icdPanel, s, b);
    }
    
    private String calculateSKUForRegularPanels(final ICDPanel icdPanel, final String s, final boolean b) {
        return this.getRegularPanelFinishCode(icdPanel) + this.getRegulaPanelSplitType(icdPanel) + this.getChaseStyle(icdPanel) + this.getHeightForRegularPanels(icdPanel, s, b) + (b ? "x" : "") + this.getWidthForRegularPanels(icdPanel, s, b) + this.getRegularPanelChaseStyle(icdPanel) + this.getElectricalCode(icdPanel);
    }
    
    private String calculateSKUForDoorPanels(final ICDPanel icdPanel, final String s, final boolean b) {
        final String str = "D";
        String str2;
        if (!b) {
            final int round = Math.round(icdPanel.getHeight());
            str2 = round - ((round <= 79) ? 1 : 0) + "";
        }
        else {
            str2 = icdPanel.getHeight() + "";
        }
        String str3;
        if (b) {
            int round2 = Math.round(icdPanel.getWidth());
            round2 -= 5;
            if (round2 < 0) {
                round2 = 0;
            }
            str3 = round2 + "";
        }
        else {
            str3 = icdPanel.getWidth() + "";
        }
        return str + str3 + (b ? "x" : "") + str2;
    }
    
    public String getRegularPanelFinishCode(final ICDPanel icdPanel) {
        String str = "";
        if (icdPanel != null) {
            if (icdPanel.withHorizontalInnerExtrusion()) {
                if (icdPanel.hasChase()) {
                    if (icdPanel.hasChase() && icdPanel.isUnderChase() && icdPanel.isOpenBottom()) {
                        return "";
                    }
                    final TileInterface topTile = icdPanel.getTopTile();
                    if (topTile instanceof ICDTile) {
                        str = ((ICDTile)topTile).getFinishCodeForSkuGeneration();
                    }
                }
                else {
                    final TileInterface topTile2 = icdPanel.getTopTile();
                    if (topTile2 instanceof ICDTile) {
                        str = ((ICDTile)topTile2).getFinishCodeForSkuGeneration();
                    }
                    final TileInterface bottomTile = icdPanel.getBottomTile();
                    if (bottomTile instanceof ICDTile) {
                        str += ((ICDTile)bottomTile).getFinishCodeForSkuGeneration();
                    }
                }
            }
            else {
                if (icdPanel.hasChase() && icdPanel.isUnderChase() && icdPanel.isOpenBottom()) {
                    return "";
                }
                final TileInterface aTile = icdPanel.getATile();
                if (aTile instanceof ICDTile) {
                    str = ((ICDTile)aTile).getFinishCodeForSkuGeneration();
                }
                if (!icdPanel.isCorePanel()) {
                    final Vector<TileInterface> allTiles = icdPanel.getAllTiles();
                    if (allTiles != null) {
                        for (final TileInterface tileInterface : allTiles) {
                            if (tileInterface instanceof ICDTile && !((ICDTile)tileInterface).isNoFrameTile()) {
                                str = ((ICDTile)tileInterface).getFinishCodeForSkuGeneration();
                            }
                        }
                    }
                }
            }
        }
        return str;
    }
    
    private String getChaseStyle(final ICDPanel icdPanel) {
        String s = "";
        if (icdPanel != null && icdPanel.hasChase()) {
            s = "C";
            if (icdPanel.isUnderChase()) {
                s += "U";
            }
            if (icdPanel.isSuspendedChase()) {
                s += "S";
            }
        }
        return s;
    }
    
    private String getRegularPanelChaseStyle(final ICDPanel icdPanel) {
        String s = "";
        if (icdPanel != null && icdPanel.hasChase()) {
            if (icdPanel.isSingleChase()) {
                s = "S";
                if (icdPanel.isUnderChase()) {
                    if (icdPanel.isSuspendedChaseHorizontalSplit() && icdPanel.isSuspendedChase()) {
                        s = "T";
                        if (icdPanel.hasOpenTiles()) {
                            s = "O";
                        }
                    }
                }
                else if (icdPanel.withHorizontalInnerExtrusion()) {
                    if (icdPanel.isSuspendedChaseHorizontalSplit()) {
                        final TileInterface middleTile = icdPanel.getMiddleTile();
                        if (middleTile != null && middleTile.isNoTile()) {
                            return "O";
                        }
                    }
                    else if (icdPanel.isOpenBottom()) {
                        return "O";
                    }
                    s = "T";
                }
            }
            else if (icdPanel.isDoubleChase()) {
                s = "D";
            }
        }
        return s;
    }
    
    private String getRegulaPanelSplitType(final ICDPanel icdPanel) {
        String s = "";
        if (icdPanel != null && !icdPanel.hasChase()) {
            if (icdPanel.withHorizontalInnerExtrusion()) {
                s = "SP";
            }
            else {
                final TileInterface bottomTile = icdPanel.getBottomTile();
                if (bottomTile != null && bottomTile.isNoTileWithFrame()) {
                    s = "F";
                }
                else {
                    s = "P";
                }
            }
        }
        return s;
    }
    
    private String getElectricalCode(final ICDPanel icdPanel) {
        String s = "";
        if (icdPanel != null && icdPanel.panelHasElectrical()) {
            s = "-P";
        }
        return s;
    }
    
    private String getHeightForRegularPanels(final ICDPanel icdPanel, final String key, final boolean b) {
        if (b) {
            return Math.round(icdPanel.getHeight()) + "";
        }
        String str = "";
        final SortedSet<Integer> set = ICDPanelSKUGenerator.panelTypeHeight.get(key);
        if (icdPanel != null && set != null) {
            final int round = Math.round(icdPanel.getHeight());
            for (final int intValue : set) {
                if (round <= intValue) {
                    str = intValue + "";
                    if (str.length() == 1) {
                        str = "0" + str;
                        break;
                    }
                    break;
                }
            }
        }
        return str;
    }
    
    private String getWidthForRegularPanels(final ICDPanel icdPanel, final String key, final boolean b) {
        if (b) {
            return Math.round(icdPanel.getWidth()) + "";
        }
        String str = "";
        final SortedSet<Integer> set = ICDPanelSKUGenerator.panelTypeWidth.get(key);
        if (icdPanel != null && set != null) {
            final int round = Math.round(icdPanel.getWidth());
            for (final int intValue : set) {
                if (round <= intValue) {
                    str = intValue + "";
                    if (str.length() == 1) {
                        str = "0" + str;
                        break;
                    }
                    break;
                }
            }
        }
        return str;
    }
    
    private String getWidthForStepReturnPanels(final ICDPanel icdPanel, final String key, final boolean b) {
        if (b) {
            return Math.round(icdPanel.getWidthExceptNoTileWithoutFrame()) + "";
        }
        String str = "";
        final SortedSet<Integer> set = ICDPanelSKUGenerator.panelTypeWidth.get(key);
        if (icdPanel != null && set != null) {
            final int round = Math.round(icdPanel.getWidthExceptNoTileWithoutFrame());
            for (final int intValue : set) {
                if (round <= intValue) {
                    str = intValue + "";
                    if (str.length() == 1) {
                        str = "0" + str;
                        break;
                    }
                    break;
                }
            }
        }
        return str;
    }
    
    public boolean shouldRelinkCatalogPart() {
        return false;
    }
    
    public void relinkCatalogPart(final String s, final TypeableEntity typeableEntity) {
        if (this.shouldRelinkCatalogPart()) {
            final OptionObject currentOption = typeableEntity.getCurrentOption();
            currentOption.resetCatalogPart();
            currentOption.linkToCatalogPart(true, s);
        }
    }
    
    private Catalog getCatalog() {
        final Catalog catalog = null;
        final Manufacturer manufacturer = CatalogManager.getCatalogs().first();
        return catalog;
    }
    
    static {
        ICDPanelSKUGenerator.panelTypeHeight = null;
        ICDPanelSKUGenerator.panelTypeWidth = null;
    }
}
