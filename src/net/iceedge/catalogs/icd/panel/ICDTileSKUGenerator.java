package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.utilities.TypeFilter;
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

public class ICDTileSKUGenerator implements SkuGeneratable
{
    static HashMap<String, SortedSet<Integer>> panelTypeHeight;
    static HashMap<String, SortedSet<Integer>> panelTypeWidth;
    
    public ICDTileSKUGenerator() {
        this.initializeCatalog();
    }
    
    private void initializeCatalog() {
        if (ICDTileSKUGenerator.panelTypeHeight == null || ICDTileSKUGenerator.panelTypeWidth == null) {
            final Catalog catalog = Solution.getCatalogs().get("ICI_ICD");
            if (catalog != null) {
                ICDTileSKUGenerator.panelTypeHeight = new HashMap<String, SortedSet<Integer>>();
                ICDTileSKUGenerator.panelTypeWidth = new HashMap<String, SortedSet<Integer>>();
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
                        SortedSet<Integer> value = ICDTileSKUGenerator.panelTypeHeight.get(description);
                        SortedSet<Integer> value2 = ICDTileSKUGenerator.panelTypeWidth.get(description);
                        if (value == null) {
                            value = new TreeSet<Integer>();
                            ICDTileSKUGenerator.panelTypeHeight.put(description, value);
                        }
                        if (value2 == null) {
                            value2 = new TreeSet<Integer>();
                            ICDTileSKUGenerator.panelTypeWidth.put(description, value2);
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
        return this.generateSKU(typeableEntity, false);
    }
    
    public String generateSKU(final TypeableEntity typeableEntity, final boolean b) {
        String calculateSKUForStepReturnTiles = "";
        if (typeableEntity instanceof ICDTile) {
            final ICDPanel.PANEL_TYPE panelType = ((ICDPanel)typeableEntity.getParent((TypeFilter)new ICDPanelFilter())).getPanelType();
            if (panelType.equals(ICDPanel.PANEL_TYPE.STACK_PANEL)) {
                calculateSKUForStepReturnTiles = this.calculateSKUForStepReturnTiles((ICDTile)typeableEntity, panelType.getPanelName(), b);
            }
        }
        this.relinkCatalogPart(calculateSKUForStepReturnTiles, typeableEntity);
        return calculateSKUForStepReturnTiles;
    }
    
    private String calculateSKUForStepReturnTiles(final ICDTile icdTile, final String s, final boolean b) {
        return icdTile.getFinishCodeForSkuGeneration() + "SP" + this.getHeightForTiles((TransformableEntity)icdTile, s, b) + (b ? "x" : "") + this.getWidthForTiles(icdTile, s, b);
    }
    
    private String getHeightForTiles(final TransformableEntity transformableEntity, final String key, final boolean b) {
        if (b) {
            return Math.round(transformableEntity.getHeight()) + "";
        }
        String str = "";
        final SortedSet<Integer> set = ICDTileSKUGenerator.panelTypeHeight.get(key);
        if (transformableEntity != null && set != null) {
            final int round = Math.round(transformableEntity.getHeight());
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
    
    private String getWidthForTiles(final ICDTile icdTile, final String key, final boolean b) {
        if (b) {
            return Math.round(icdTile.getWidth()) + "";
        }
        String str = "";
        final SortedSet<Integer> set = ICDTileSKUGenerator.panelTypeWidth.get(key);
        if (icdTile != null && set != null) {
            final int round = Math.round(icdTile.getWidth());
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
    
    static {
        ICDTileSKUGenerator.panelTypeHeight = null;
        ICDTileSKUGenerator.panelTypeWidth = null;
    }
}
