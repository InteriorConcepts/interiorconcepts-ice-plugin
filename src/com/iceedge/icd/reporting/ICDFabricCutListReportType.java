package com.iceedge.icd.reporting;

import net.dirtt.icelib.report.ReportType;

public class ICDFabricCutListReportType extends ReportType
{
    public ICDFabricCutListReportType() {
        super(75, "Fabric Cut List Report", ICDCuttingListFabricReport.class);
    }
    
    public boolean shouldReadWriteXML() {
        return false;
    }
}
