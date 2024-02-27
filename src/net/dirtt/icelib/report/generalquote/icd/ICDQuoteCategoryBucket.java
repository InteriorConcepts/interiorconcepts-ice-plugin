package net.dirtt.icelib.report.generalquote.icd;

import net.dirtt.icelib.main.EntityObject;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import net.iceedge.icebox.custom.CustomizableEntity;
import net.dirtt.icelib.main.TypeableEntity;
import net.dirtt.icelib.report.Reportable;
import net.dirtt.icelib.report.ReportNode;
import net.dirtt.icelib.report.generalquote.GeneralQuoteCategoryBucket;

public class ICDQuoteCategoryBucket extends GeneralQuoteCategoryBucket
{
    public ICDQuoteCategoryBucket() {
    }
    
    public ICDQuoteCategoryBucket(final ReportNode reportNode, final Reportable reportable) {
        super(reportNode, reportable);
        final String productType = this.getProductType(reportable);
        String reportMainHeading;
        if (productType != null) {
            reportMainHeading = productType;
        }
        else {
            reportMainHeading = ((TypeableEntity)reportable).getReportMainHeading();
        }
        if (reportMainHeading != null) {
            this.category = reportMainHeading;
        }
        else {
            this.category = "Other";
        }
        this.setDescription(this.category);
    }
    
    private String getProductType(final Reportable reportable) {
        String customMainHeading = null;
        if (reportable instanceof CustomizableEntity) {
            customMainHeading = ((CustomizableEntity)reportable).getCustomMainHeading();
        }
        return (customMainHeading != null) ? customMainHeading : ICDManufacturingUtils.getProductType((TypeableEntity)reportable);
    }
    
    public boolean doesEntityFit(final EntityObject entityObject) {
        final boolean b = true;
        final String productType = this.getProductType((Reportable)entityObject);
        String reportMainHeadingFromQuoteTables;
        if (productType != null) {
            reportMainHeadingFromQuoteTables = productType;
        }
        else {
            reportMainHeadingFromQuoteTables = ((TypeableEntity)entityObject).getReportMainHeadingFromQuoteTables();
        }
        boolean b2;
        if (reportMainHeadingFromQuoteTables != null) {
            b2 = (b && this.category.equals(reportMainHeadingFromQuoteTables));
        }
        else {
            b2 = (this.category.equals("Other") || super.doesEntityFit(entityObject));
        }
        return b2;
    }
    
    public ReportNode getBucketForEntity(final EntityObject entityObject) {
        Object matchingBucket = this.findMatchingBucket(entityObject);
        if (matchingBucket == null) {
            matchingBucket = new ICDQuoteSubCategoryBucket((ReportNode)this, (Reportable)entityObject);
        }
        return (ReportNode)matchingBucket;
    }
}
