// 
// Decompiled by Procyon v0.5.36
// 

package net.dirtt.icelib.report.generalquote.icd;

import com.iceedge.icd.reporting.ICDManufacturingUtils;
import net.iceedge.icebox.custom.CustomizableEntity;
import net.dirtt.icelib.main.TypeableEntity;
import net.dirtt.icelib.report.generalquote.GeneralQuoteFirstLevelBucket;
import net.dirtt.icelib.report.generalquote.GeneralQuoteXMLGetter;

public class ICDQuoteXMLGetter extends GeneralQuoteXMLGetter
{
    public ICDQuoteXMLGetter(final GeneralQuoteFirstLevelBucket generalQuoteFirstLevelBucket) {
        super(generalQuoteFirstLevelBucket);
    }
    
    protected void writeXMLForExtendedWarnings(final StringBuffer sb, final TypeableEntity typeableEntity) {
        this.writeWorksurfaceWarnings(sb, typeableEntity);
    }
    
    protected void writeXMLForManufacturer(final StringBuffer sb, final TypeableEntity typeableEntity) {
        String s = null;
        if (typeableEntity instanceof CustomizableEntity) {
            s = ((CustomizableEntity)typeableEntity).getCustomProductForQuote();
        }
        if (s == null) {
            s = ICDManufacturingUtils.getProductType(typeableEntity);
        }
        if (s != null) {
            this.writeTag(sb, "manufacturer", s);
        }
    }
    
    protected void writeXMLForPricing(final StringBuffer sb, final TypeableEntity typeableEntity) {
        this.writeTag(sb, "unitPrice", this.bucket.getUnitPriceForXML(typeableEntity));
        this.writeTag(sb, "sellPrice", this.bucket.getSellPriceForXML());
        this.writeTag(sb, "extSell", this.bucket.getExtendedSellPriceForXML());
    }
    
    protected void writeXMLForTagName(final StringBuffer sb, final TypeableEntity typeableEntity) {
        this.writeTag(sb, "Tag_Name", typeableEntity.getUserTagNameAttribute("TagName1"));
    }
    
    protected boolean shouldFakeToSecondLevelBucket(final GeneralQuoteFirstLevelBucket generalQuoteFirstLevelBucket, final TypeableEntity typeableEntity) {
        return typeableEntity.getAttributeValueAsFloat("P1") > 0.0f && generalQuoteFirstLevelBucket.getSecondLevelBuckets().size() > 0;
    }
}
