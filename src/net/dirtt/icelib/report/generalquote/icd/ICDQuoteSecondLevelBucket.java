package net.dirtt.icelib.report.generalquote.icd;

import net.dirtt.icelib.main.CatalogOptionObject;
import net.dirtt.icelib.main.TypeableEntity;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.report.Reportable;
import net.dirtt.icelib.report.ReportNode;
import net.dirtt.icelib.report.generalquote.GeneralQuoteSecondLevelBucket;

public class ICDQuoteSecondLevelBucket extends GeneralQuoteSecondLevelBucket
{
    private static boolean showQuotePrice;
    
    public ICDQuoteSecondLevelBucket() {
    }
    
    public ICDQuoteSecondLevelBucket(final ReportNode reportNode, final Reportable reportable) {
        super(reportNode, reportable);
    }
    
    public boolean addChildrenToReport(final EntityObject entityObject) {
        return false;
    }
    
    public void getXML(final StringBuffer sb, final boolean showQuotePrice) {
        ICDQuoteSecondLevelBucket.showQuotePrice = showQuotePrice;
        this.getXML(sb);
    }
    
    public void getXML(final StringBuffer sb) {
        final EntityObject originalBucketEntity = this.getOriginalBucketEntity();
        if (originalBucketEntity != null && originalBucketEntity instanceof TypeableEntity) {
            final TypeableEntity typeableEntity = (TypeableEntity)originalBucketEntity;
            getXML(sb, this.getDescriptionForQuote(), this.getTotalBucketQuantity(), this.getUnitSellPrice(), typeableEntity.getXMLImage(false), typeableEntity.getLargeQuoteImageFilename(), typeableEntity.getSKU(), typeableEntity.getTagNameAttribute(), ICDQuoteSecondLevelBucket.showQuotePrice);
        }
    }
    
    public static void getXML(final StringBuffer sb, final String s, final int i, final float n, final String str, final String str2, final String str3, final String str4, final boolean showQuotePrice) {
        ICDQuoteSecondLevelBucket.showQuotePrice = showQuotePrice;
        sb.append("<detail-item>");
        sb.append("<det-desc>");
        sb.append(replaceBadXMLCharacters(s));
        sb.append("</det-desc>");
        sb.append("<count>");
        sb.append(i);
        sb.append("</count>");
        if (ICDQuoteSecondLevelBucket.showQuotePrice) {
            sb.append("<det-show-price>");
            sb.append(ICDQuoteSecondLevelBucket.showQuotePrice ? "1" : "0");
            sb.append("</det-show-price>");
            sb.append("<det-price>");
            sb.append(ICDQuoteSecondLevelBucket.format.format(n));
            sb.append("</det-price>");
        }
        else {
            sb.append("<det-show-price>");
            sb.append(ICDQuoteSecondLevelBucket.showQuotePrice ? "1" : "0");
            sb.append("</det-show-price>");
        }
        sb.append("<image>");
        sb.append(str);
        sb.append("</image>");
        sb.append("<Tag_Name>");
        sb.append(str4);
        sb.append("</Tag_Name>");
        sb.append("<det-image>");
        sb.append(str2);
        sb.append("</det-image>");
        sb.append("<sku>");
        sb.append(str3);
        sb.append("</sku>");
        sb.append("</detail-item>");
    }
    
    public int getReportLevel() {
        return 3;
    }
    
    public String getDescriptionForQuote() {
        final EntityObject originalBucketEntity = this.getOriginalBucketEntity();
        if (originalBucketEntity == null || !(originalBucketEntity instanceof TypeableEntity)) {
            return super.getDescriptionForQuote();
        }
        final TypeableEntity typeableEntity = (TypeableEntity)originalBucketEntity;
        if (originalBucketEntity.getCatalogType() == CatalogOptionObject.Type.grade || originalBucketEntity.getCatalogType() == CatalogOptionObject.Type.type) {
            return originalBucketEntity.getAttributeValueAsString("ON") + " " + originalBucketEntity.getAttributeValueAsString("OD");
        }
        return typeableEntity.getQuoteDescription();
    }
    
    static {
        ICDQuoteSecondLevelBucket.showQuotePrice = true;
    }
}
