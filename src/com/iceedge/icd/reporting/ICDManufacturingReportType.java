// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.reporting;

import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReport;
import net.dirtt.icelib.report.ReportType;

public class ICDManufacturingReportType extends ReportType
{
    public ICDManufacturingReportType() {
        super(51, "Manufacturing Report", (Class)ICDManufacturingReport.class);
    }
    
    public String getShortString() {
        return "Manufacturing Report";
    }
    
    public String getXmlElementString() {
        return "icdmanreport";
    }
    
    public boolean shouldReadWriteXML() {
        return true;
    }
    
    public boolean shouldResetReportOnLoad() {
        return false;
    }
}
