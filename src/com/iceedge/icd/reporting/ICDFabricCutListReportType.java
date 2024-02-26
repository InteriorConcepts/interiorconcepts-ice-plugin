// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.reporting;

import net.dirtt.icelib.report.ReportType;

public class ICDFabricCutListReportType extends ReportType
{
    public ICDFabricCutListReportType() {
        super(75, "Fabric Cut List Report", (Class)ICDCuttingListFabricReport.class);
    }
    
    public boolean shouldReadWriteXML() {
        return false;
    }
}
