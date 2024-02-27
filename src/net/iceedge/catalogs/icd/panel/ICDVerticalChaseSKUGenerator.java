package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.main.OptionObject;
import net.iceedge.icecore.basemodule.interfaces.panels.TileInterface;
import java.util.Vector;
import net.dirtt.icelib.ui.OptionProxyValue;
import net.dirtt.icelib.main.attributes.proxy.OptionAttributeProxy;
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

public class ICDVerticalChaseSKUGenerator implements SkuGeneratable
{
    static HashMap<String, SortedSet<Integer>> panelTypeHeight;
    static HashMap<String, SortedSet<Integer>> panelTypeWidth;
    
    public ICDVerticalChaseSKUGenerator() {
        this.initializeCatalog();
    }
    
    private void initializeCatalog() {
        if (ICDVerticalChaseSKUGenerator.panelTypeHeight == null || ICDVerticalChaseSKUGenerator.panelTypeWidth == null) {
            final Catalog catalog = Solution.getCatalogs().get("ICI_ICD");
            if (catalog != null) {
                ICDVerticalChaseSKUGenerator.panelTypeHeight = new HashMap<String, SortedSet<Integer>>();
                ICDVerticalChaseSKUGenerator.panelTypeWidth = new HashMap<String, SortedSet<Integer>>();
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
                        SortedSet<Integer> value = ICDVerticalChaseSKUGenerator.panelTypeHeight.get(description);
                        SortedSet<Integer> value2 = ICDVerticalChaseSKUGenerator.panelTypeWidth.get(description);
                        if (value == null) {
                            value = new TreeSet<Integer>();
                            ICDVerticalChaseSKUGenerator.panelTypeHeight.put(description, value);
                        }
                        if (value2 == null) {
                            value2 = new TreeSet<Integer>();
                            ICDVerticalChaseSKUGenerator.panelTypeWidth.put(description, value2);
                        }
                        final Iterator iterator3 = tableOfContents3.getPartItems().iterator();
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
        String calculateSKUForVerticalChase = "";
        if (typeableEntity instanceof ICDVerticalChase) {
            final ICDVerticalChase.PANEL_TYPE panelType = ((ICDVerticalChase)typeableEntity).getPanelType();
            switch (panelType) {
                case VERTICAL_CHASE: {
                    calculateSKUForVerticalChase = this.calculateSKUForVerticalChase((ICDVerticalChase)typeableEntity, panelType.getPanelName());
                    break;
                }
                default: {
                    System.err.println("Unknown panel type");
                    break;
                }
            }
        }
        this.relinkCatalogPart(calculateSKUForVerticalChase, typeableEntity);
        return calculateSKUForVerticalChase;
    }
    
    private String calculateSKUForVerticalChase(final ICDVerticalChase icdVerticalChase, final String s) {
        String string = "";
        if (icdVerticalChase != null) {
            final String str = "VC";
            String str2 = this.getRegularPanelFinishCode(icdVerticalChase.getAPanel());
            if (str2.length() > 0) {
                str2 = str2.substring(0, 1);
            }
            final int parametricVerticalChaseHeight = this.getParametricVerticalChaseHeight(icdVerticalChase);
            int round = Math.round(icdVerticalChase.getAttributeValueAsFloat("ICD_Vertical_Chase_Depth", 0.0f));
            int round2 = Math.round(icdVerticalChase.getAttributeValueAsFloat("ICD_Vertical_Chase_Width", 0.0f));
            ++round;
            ++round2;
            final String string2 = round + "";
            final String string3 = round2 + "";
            String str3 = "L";
            if (string2.equals(string3)) {
                str3 = "S";
            }
            string = str + str2 + str3 + parametricVerticalChaseHeight + string2;
        }
        return string;
    }
    
    private int getParametricVerticalChaseHeight(final ICDVerticalChase icdVerticalChase) {
        final float n = (float)Math.round(icdVerticalChase.getAttributeValueAsFloat("ICD_Vertical_Chase_Height", 0.0f));
        final Vector possibleValues = Solution.getWorldAttributeProxy().get("ICD_Vertical_Chase_Height").getPossibleValues();
        float float1 = Float.parseFloat(possibleValues.get(possibleValues.size() - 1).getValue());
        final Iterator<OptionProxyValue> iterator = possibleValues.iterator();
        while (iterator.hasNext()) {
            final float float2 = Float.parseFloat(iterator.next().getValue());
            if (Math.abs(float2 - n) < Math.abs(float1 - n) && float2 - n >= 0.0f) {
                float1 = float2;
            }
        }
        return (int)float1;
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
                    final Vector allTiles = icdPanel.getAllTiles();
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
    
    static {
        ICDVerticalChaseSKUGenerator.panelTypeHeight = null;
        ICDVerticalChaseSKUGenerator.panelTypeWidth = null;
    }
}
