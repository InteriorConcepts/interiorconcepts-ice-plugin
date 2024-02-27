package net.dirtt.icelib.report.generalquote.icd;

import com.iceedge.icd.reporting.ICDManufacturingUtils;
import net.dirtt.icelib.main.TypeableEntity;
import net.iceedge.icebox.custom.CustomizableEntity;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.report.Reportable;
import net.dirtt.icelib.report.ReportNode;
import net.dirtt.icelib.report.generalquote.GeneralQuoteSubCategoryBucket;

public class ICDQuoteSubCategoryBucket extends GeneralQuoteSubCategoryBucket
{
    public ICDQuoteSubCategoryBucket() {
    }
    
    public ICDQuoteSubCategoryBucket(final ReportNode reportNode, final Reportable reportable) {
        super(reportNode, reportable);
        final String heading = this.getHeading(reportable, true);
        if (heading != null) {
            this.subcategory = heading;
        }
        else {
            this.subcategory = "Other";
        }
        this.setDescription(this.subcategory);
    }
    
    public boolean doesEntityFit(final EntityObject entityObject) {
        final String heading = this.getHeading((Reportable)entityObject, false);
        boolean b;
        if (heading != null) {
            b = this.subcategory.equals(heading);
        }
        else {
            b = super.doesEntityFit(entityObject);
        }
        return b;
    }
    
    private String getHeading(final Reportable reportable, final boolean b) {
        String s = null;
        if (reportable instanceof CustomizableEntity) {
            s = ((CustomizableEntity)reportable).getCustomSubHeading();
        }
        if (s == null) {
            s = ICDManufacturingUtils.getProductSubType((TypeableEntity)reportable);
        }
        String s2;
        if (s != null) {
            s2 = s;
        }
        else {
            final String attributeValueAsString = ((TypeableEntity)reportable).getAttributeValueAsString("Product_Type");
            if (attributeValueAsString != null) {
                s2 = attributeValueAsString;
            }
            else if (b) {
                s2 = ((TypeableEntity)reportable).getReportMainHeading();
            }
            else {
                s2 = ((TypeableEntity)reportable).getReportSubHeading();
            }
        }
        return s2;
    }
    
    public ReportNode getBucketForEntity(final EntityObject entityObject) {
        Object matchingBucket = this.findMatchingBucket(entityObject);
        if (matchingBucket == null) {
            matchingBucket = new ICDQuoteFirstLevelBucket((ReportNode)this, (Reportable)entityObject);
        }
        return (ReportNode)matchingBucket;
    }
}
