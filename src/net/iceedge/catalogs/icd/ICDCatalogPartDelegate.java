// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd;

import net.iceedge.icecore.icecad.ice.tree.CadLayer;
import net.iceedge.icecore.icecad.ice.tree.IceCadLayer;
import net.iceedge.icecore.icecad.ice.tree.IceCadNodeContainer;
import net.dirtt.icecad.cadtree.ICadTreeNode;
import net.dirtt.icelib.main.attributes.Attribute;
import net.iceedge.icecore.basemodule.baseclasses.BasicCatalogPart;
import org.apache.log4j.Logger;
import net.iceedge.icecore.basemodule.baseclasses.BasicCatalogPartDelegate;

public class ICDCatalogPartDelegate extends BasicCatalogPartDelegate
{
    private static Logger logger;
    
    public ICDCatalogPartDelegate(final BasicCatalogPart basicCatalogPart) {
        super(basicCatalogPart);
    }
    
    public String getLayerName() {
        String valueAsString = null;
        if (this.catalogPart != null) {
            final Attribute attributeObject = this.catalogPart.getAttributeObject("ICD_LayerName");
            if (attributeObject != null) {
                valueAsString = attributeObject.getValueAsString();
                if (valueAsString != null && valueAsString.trim().isEmpty()) {
                    valueAsString = null;
                }
            }
        }
        return valueAsString;
    }
    
    public int getMyCadLayerNode(final ICadTreeNode cadTreeNode) {
        if ("Worksurfaces".equals(this.getLayerName())) {
            return 2;
        }
        return -1;
    }
    
    public IceCadLayer getCadLayer(final IceCadNodeContainer iceCadNodeContainer) {
        if ("Worksurfaces".equals(this.getLayerName())) {
            return CadLayer.LAYER_2.getLayer();
        }
        return null;
    }
    
    static {
        ICDCatalogPartDelegate.logger = Logger.getLogger((Class)ICDCatalogPartDelegate.class);
    }
}
