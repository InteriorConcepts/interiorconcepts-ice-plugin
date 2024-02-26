// 
// Decompiled by Procyon v0.5.36
// 

package net.dirtt.icelib.report.generalquote.icd;

import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.report.Reportable;
import net.dirtt.icelib.report.ReportNode;
import java.text.DecimalFormat;
import net.dirtt.icelib.report.generalquote.GeneralQuoteMaterialBucket;

public class ICDQuoteMaterialBucket extends GeneralQuoteMaterialBucket
{
    private static DecimalFormat format;
    
    public ICDQuoteMaterialBucket() {
    }
    
    public ICDQuoteMaterialBucket(final ReportNode reportNode, final Reportable reportable) {
        super(reportNode, reportable);
    }
    
    public boolean addChildrenToReport(final EntityObject entityObject) {
        return false;
    }
    
    public int getReportLevel() {
        return 3;
    }
    
    static {
        ICDQuoteMaterialBucket.format = new DecimalFormat("#.00");
    }
}
