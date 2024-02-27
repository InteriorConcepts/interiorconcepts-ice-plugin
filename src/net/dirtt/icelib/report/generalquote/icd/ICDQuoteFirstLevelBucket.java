package net.dirtt.icelib.report.generalquote.icd;

import net.iceedge.icebox.custom.CustomizableEntity;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TypeableEntity;
import net.dirtt.icelib.report.generalquote.GeneralQuoteXMLGetter;
import net.dirtt.icelib.report.Reportable;
import net.dirtt.icelib.report.ReportNode;
import net.dirtt.icelib.report.generalquote.GeneralQuoteFirstLevelBucket;

public class ICDQuoteFirstLevelBucket extends GeneralQuoteFirstLevelBucket
{
    public ICDQuoteFirstLevelBucket() {
    }
    
    public ICDQuoteFirstLevelBucket(final ReportNode reportNode, final Reportable reportable) {
        super(reportNode, reportable);
    }
    
    protected GeneralQuoteXMLGetter createMyXMLGetter() {
        return new ICDQuoteXMLGetter(this);
    }
    
    public void fakeSelfToSecondLevelBucketXML(final StringBuffer sb) {
        final EntityObject originalBucketEntity = this.getOriginalBucketEntity();
        if (originalBucketEntity != null && originalBucketEntity instanceof TypeableEntity) {
            final TypeableEntity typeableEntity = (TypeableEntity)originalBucketEntity;
            ICDQuoteSecondLevelBucket.getXML(sb, originalBucketEntity.getDescription(), 1, this.getUnitSellPrice(), typeableEntity.getXMLImage(false), typeableEntity.getLargeQuoteImageFilename(), typeableEntity.getSKU(), typeableEntity.getTagNameAttribute(), true);
        }
    }
    
    public ReportNode getBucketForEntity(final EntityObject entityObject) {
        Object matchingBucket = this.findMatchingBucket(entityObject);
        if (matchingBucket == null) {
            if (this.isMaterial(entityObject)) {
                matchingBucket = new ICDQuoteMaterialBucket((ReportNode)this, (Reportable)entityObject);
            }
            else {
                matchingBucket = new ICDQuoteSecondLevelBucket((ReportNode)this, (Reportable)entityObject);
            }
        }
        return (ReportNode)matchingBucket;
    }
    
    protected String getQuoteImageForXML(final TypeableEntity typeableEntity) {
        if (typeableEntity instanceof CustomizableEntity) {
            return typeableEntity.getXMLImage(true);
        }
        return super.getQuoteImageForXML(typeableEntity);
    }
}
