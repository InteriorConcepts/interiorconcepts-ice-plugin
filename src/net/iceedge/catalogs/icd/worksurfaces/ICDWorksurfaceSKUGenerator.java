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
        if (ICDWorksurfaceSKUGenerator.worksurfaceTypeDepth != null && ICDWorksurfaceSKUGenerator.worksurfaceTypeWidth != null) {
            return;
        }
        final Catalog catalog = Solution.getCatalogs().get("ICI_ICD");
        if (catalog == null) {
            System.err.println("Could not find catalog ICI_ICD. Unable to initalize ICDWorksurfaceSKUGenerator");
            Thread.dumpStack();
            return;
        }
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
                SortedSet<Integer> depth = ICDWorksurfaceSKUGenerator.worksurfaceTypeDepth.get(description);
                SortedSet<Integer> width = ICDWorksurfaceSKUGenerator.worksurfaceTypeWidth.get(description);
                if (depth == null) {
                    depth = new TreeSet<Integer>();
                    ICDWorksurfaceSKUGenerator.worksurfaceTypeDepth.put(description, depth);
                }
                if (width == null) {
                    width = new TreeSet<Integer>();
                    ICDWorksurfaceSKUGenerator.worksurfaceTypeWidth.put(description, width);
                }
                final Iterator<TableOfContents.PartItem> iterator3 = tableOfContents3.getPartItems().iterator();
                while (iterator3.hasNext()) {
                    final Part part = broker.getPart(iterator3.next().getPartName());
                    final String attributeValue = part.getAttributeValue("Depth", (String)null);
                    final String attributeValue2 = part.getAttributeValue("Width", (String)null);
                    if (attributeValue != null) {
                        depth.add((int)Float.parseFloat(attributeValue));
                    }
                    if (attributeValue2 != null) {
                        width.add((int)Float.parseFloat(attributeValue2));
                    }
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public String generateSKU(final TypeableEntity typeableEntity) {
        String s = "";
        String shapeTag = "";
        float xDim = 0.0f,
              yDim = 0.0f;

        // Size for Normal Decks/Shelves/Wks
        if (typeableEntity instanceof ICDParametricDeckOrShelf || typeableEntity instanceof ICDWSCParametricWorksurface || typeableEntity instanceof ICDBasicWorksurface) {
            shapeTag = ((ICDBasicWorksurface) typeableEntity).getShapeTag();
            xDim = ((ICDBasicWorksurface) typeableEntity).getXDimensionForReport();
            yDim = ((ICDBasicWorksurface) typeableEntity).getYDimensionForReport();
        }
        // Size for Suspended Chase Decks
        if (typeableEntity instanceof ICDDeck) {
            shapeTag = ((ICDDeck) typeableEntity).getShapeTag();
            xDim = ((ICDDeck) typeableEntity).getXDimensionForReport();
            yDim = ((ICDDeck) typeableEntity).getYDimensionForReport();
        }
        if (shapeTag.isEmpty()) {
            return "";
        }
        
        String validDepth = "",
               validWidth = "";
        validDepth = this.getValidDimString(xDim, shapeTag, WidthDepth.Depth);
        validWidth = this.getValidDimString(yDim, shapeTag, WidthDepth.Width);

        String finishCodeForDeckOrShelf = "";

        // Finish Code for Decks and Shelves (Lam or Mel), Blank for Wks
        if (typeableEntity instanceof ICDParametricDeckOrShelf) {
            finishCodeForDeckOrShelf = ((ICDParametricDeckOrShelf) typeableEntity).getFinishCodeForDeckOrShelf();
        }
        if (typeableEntity instanceof ICDDeck) {
            finishCodeForDeckOrShelf = ((ICDDeck) typeableEntity).getFinishCodeForManufacturingReport();
        }
        
        // Letter for Decks and Shelves (instead of using full ShapeTag)
        if (typeableEntity instanceof ICDParametricDeckOrShelf) {
            shapeTag = (typeableEntity instanceof ICDParametricDeck ? "D" : "S");
        }

        // Create SKU
        if (shapeTag != null && validDepth != null && validWidth != null) {
            s = shapeTag + finishCodeForDeckOrShelf + validDepth + validWidth;
        }

        this.relinkCatalogPart(s, typeableEntity);
        return s;



        /*
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
        */
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
    

    public enum WidthDepth {
        Width,
        Depth
    }


    /*
     * Pads single digit numbers with a zero
     */
    private String getPaddedNumber(final int n) {
        if (n <= 9) {
            return ("0" + n);
        } else {
            return ("" + n);
        }
    }

    /*
     * Searches Wks catalog items for the closest matching size, without going over.
     */
    private String getValidDimString(final float n, final String key, WidthDepth type) {
        String string = "";
        if (key == null) {
            return string;
        }
        final SortedSet<Integer> set;
        if (type == WidthDepth.Depth) {
            set = ICDWorksurfaceSKUGenerator.worksurfaceTypeDepth.get(key);
        } else {
            set = ICDWorksurfaceSKUGenerator.worksurfaceTypeWidth.get(key);
        }
        for (final int intValue : set) {
            if (!((int)n <= intValue)) {
                continue;
            }
            string = getPaddedNumber(intValue);
            break;
        }
        return string;
    }
    
    static {
        ICDWorksurfaceSKUGenerator.worksurfaceTypeDepth = null;
        ICDWorksurfaceSKUGenerator.worksurfaceTypeWidth = null;
    }
}
