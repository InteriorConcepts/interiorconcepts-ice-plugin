// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.CatalogManager;
import net.iceedge.icebox.ui.catalogChooser.Manufacturer;
import net.dirtt.icelib.main.OptionObject;
import net.iceedge.catalogs.icd.panel.ICDDeck;
import net.dirtt.icelib.main.TypeableEntity;
import net.iceedge.icebox.dataimport.sif.Part;
import java.util.Iterator;
import net.iceedge.icebox.dataimport.sif.CatalogBroker;
import java.io.IOException;
import java.util.TreeSet;
import net.dirtt.icelib.main.TableOfContents;
import net.dirtt.icelib.main.TableOfContents.PartItem;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.Catalog;
import java.util.SortedSet;
import java.util.HashMap;
import net.iceedge.icecore.basemodule.baseclasses.SkuGeneratable;

public class ICDWorksurfaceSKUGenerator implements SkuGeneratable
{
    static HashMap<String, SortedSet<Integer>> worksurfaceTypeDepth;
    static HashMap<String, SortedSet<Integer>> worksurfaceTypeWidth;
    
    public ICDWorksurfaceSKUGenerator() {
        this.initializeCatalog();
    }
    
    private void initializeCatalog() {
        if (ICDWorksurfaceSKUGenerator.worksurfaceTypeDepth == null || ICDWorksurfaceSKUGenerator.worksurfaceTypeWidth == null) {
            final Catalog catalog = Solution.getCatalogs().get("ICI_ICD");
            if (catalog != null) {
                ICDWorksurfaceSKUGenerator.worksurfaceTypeDepth = new HashMap<String, SortedSet<Integer>>();
                ICDWorksurfaceSKUGenerator.worksurfaceTypeWidth = new HashMap<String, SortedSet<Integer>>();
                try {
                    final CatalogBroker broker = catalog.getBroker();
                    TableOfContents tableOfContents = null;
                    for (final TableOfContents tableOfContents2 : catalog.getTableOfContents()) {
                        if ("Wks".equals(tableOfContents2.getDescription())) {
                            tableOfContents = tableOfContents2;
                            break;
                        }
                    }
                    if (tableOfContents == null) {
                        return;
                    }
                    for (final TableOfContents tableOfContents3 : tableOfContents.getChildren()) {
                        final String description = tableOfContents3.getDescription();
                        SortedSet<Integer> value = ICDWorksurfaceSKUGenerator.worksurfaceTypeDepth.get(description);
                        SortedSet<Integer> value2 = ICDWorksurfaceSKUGenerator.worksurfaceTypeWidth.get(description);
                        if (value == null) {
                            value = new TreeSet<Integer>();
                            ICDWorksurfaceSKUGenerator.worksurfaceTypeDepth.put(description, value);
                        }
                        if (value2 == null) {
                            value2 = new TreeSet<Integer>();
                            ICDWorksurfaceSKUGenerator.worksurfaceTypeWidth.put(description, value2);
                        }
                        final Iterator<PartItem> iterator3 = tableOfContents3.getPartItems().iterator();
                        while (iterator3.hasNext()) {
                            final Part part = broker.getPart(iterator3.next().getPartName());
                            final String attributeValue = part.getAttributeValue("Depth", (String)null);
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
            else {
                System.err.println("Could not find catalog ICI_ICD. Unable to initalize ICDWorksurfaceSKUGenerator");
                Thread.dumpStack();
            }
        }
    }
    
    public String generateSKU(final TypeableEntity typeableEntity) {
        String s = "";
        if (typeableEntity instanceof ICDParametricShelf) {
            final ICDParametricShelf icdParametricShelf = (ICDParametricShelf)typeableEntity;
            final String shapeTag = icdParametricShelf.getShapeTag();
            icdParametricShelf.getYDimensionForReport();
            final String validDepth = this.getValidDepth(icdParametricShelf.getYDimensionForReport(), shapeTag);
            icdParametricShelf.getXDimensionForReport();
            final String validWidth = this.getValidWidth(icdParametricShelf.getXDimensionForReport(), shapeTag);
            final String finishCodeForDeckOrShelf = icdParametricShelf.getFinishCodeForDeckOrShelf();
            if (shapeTag != null && validDepth != null && validWidth != null) {
                s = "S" + finishCodeForDeckOrShelf + validDepth + validWidth;
            }
        }
        else if (typeableEntity instanceof ICDParametricDeck) {
            final ICDParametricDeck icdParametricDeck = (ICDParametricDeck)typeableEntity;
            final String shapeTag2 = icdParametricDeck.getShapeTag();
            icdParametricDeck.getYDimensionForReport();
            final String validDepth2 = this.getValidDepth(icdParametricDeck.getYDimensionForReport(), shapeTag2);
            icdParametricDeck.getXDimensionForReport();
            final String validWidth2 = this.getValidWidth(icdParametricDeck.getXDimensionForReport(), shapeTag2);
            final String finishCodeForDeckOrShelf2 = icdParametricDeck.getFinishCodeForDeckOrShelf();
            if (shapeTag2 != null && validDepth2 != null && validWidth2 != null) {
                s = "D" + finishCodeForDeckOrShelf2 + validDepth2 + validWidth2;
            }
        }
        else if (typeableEntity instanceof ICDWSCParametricWorksurface) {
            final ICDWSCParametricWorksurface icdwscParametricWorksurface = (ICDWSCParametricWorksurface)typeableEntity;
            final String shapeTag3 = icdwscParametricWorksurface.getShapeTag();
            final String validDepth3 = this.getValidDepth(icdwscParametricWorksurface.getYDimensionForReport(), shapeTag3);
            if (shapeTag3 != null && validDepth3 != null) {
                s = shapeTag3 + validDepth3;
            }
        }
        else if (typeableEntity instanceof ICDBasicWorksurface) {
            final ICDBasicWorksurface icdBasicWorksurface = (ICDBasicWorksurface)typeableEntity;
            final String shapeTag4 = icdBasicWorksurface.getShapeTag();
            final String validDepth4 = this.getValidDepth(icdBasicWorksurface.getYDimensionForReport(), shapeTag4);
            final String validWidth3 = this.getValidWidth(icdBasicWorksurface.getXDimensionForReport(), shapeTag4);
            if (shapeTag4 != null && validDepth4 != null && validWidth3 != null) {
                s = shapeTag4 + validDepth4 + validWidth3;
            }
        }
        else if (typeableEntity instanceof ICDDeck) {
            final ICDDeck icdDeck = (ICDDeck)typeableEntity;
            final String shapeTag5 = icdDeck.getShapeTag();
            final String validDepth5 = this.getValidDepth(icdDeck.getYDimensionForReport(), shapeTag5);
            final String validWidth4 = this.getValidWidth(icdDeck.getXDimensionForReport(), shapeTag5);
            final String finishCodeForManufacturingReport = icdDeck.getFinishCodeForManufacturingReport();
            if (shapeTag5 != null && validDepth5 != null && validWidth4 != null) {
                s = "D" + finishCodeForManufacturingReport + ((validDepth5.length() == 1) ? "0" : "") + validDepth5 + ((validWidth4.length() == 1) ? "0" : "") + validWidth4;
            }
        }
        this.relinkCatalogPart(s, typeableEntity);
        return s;
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
   // Decks Less Than 10 inch fix adding leading 0 start
    private String getValidDepth(final float n, final String key) {
    String formattedString = "";
    if (key != null) {
        final SortedSet<Integer> set = ICDWorksurfaceSKUGenerator.worksurfaceTypeDepth.get(key);
        if (set != null) {
            for (final int intValue : set) {
                if ((int)n <= intValue) {
                    formattedString = String.format("%02d", intValue); // Formats the number with leading zero if less than 10
                    break;
                }
            }
        }
    }
    return formattedString;
}

	private String getValidWidth(final float n, final String key) {
    String formattedString = "";
    if (key != null) {
        final SortedSet<Integer> set = ICDWorksurfaceSKUGenerator.worksurfaceTypeWidth.get(key);
        if (set != null) {
            for (final int intValue : set) {
                if ((int)n <= intValue) {
                    formattedString = String.format("%02d", intValue); // Formats the number with leading zero if less than 10
                    break;
                }
            }
        }
    }
    return formattedString;
}
   // Decks Less Than 10 inch fix adding leading 0 end   
    static {
        ICDWorksurfaceSKUGenerator.worksurfaceTypeDepth = null;
        ICDWorksurfaceSKUGenerator.worksurfaceTypeWidth = null;
    }
}
