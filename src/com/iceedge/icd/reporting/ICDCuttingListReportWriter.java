// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.reporting;

import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.dirtt.icelib.report.Report;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.report.ReportWriter;

public class ICDCuttingListReportWriter extends ReportWriter
{
    private static final long serialVersionUID = 1L;
    private Solution solution;
    
    public ICDCuttingListReportWriter(final Report report, final Solution solution) {
        super(report, solution);
        this.solution = null;
        this.solution = solution;
    }
    
    public void writeReportHeader() throws IOException {
        if (this.writeReportHeader) {
            final String attributeValueAsString = this.solution.getAttributeValueAsString("ICD_Report_Company_Name");
            final String attributeValueAsString2 = this.solution.getAttributeValueAsString("ICD_Report_Symix_Number");
            final String attributeValueAsString3 = this.solution.getAttributeValueAsString("ICD_Report_Order_Number");
            final String attributeValueAsString4 = this.solution.getAttributeValueAsString("ICD_Report_Type");
            final String attributeValueAsString5 = this.solution.getAttributeValueAsString("ICD_Report_E_Number");
            final String attributeValueAsString6 = this.solution.getAttributeValueAsString("ICD_Init");
            this.fWriter.write(this.report.getName());
            this.fWriter.write("\r\n");
            this.fWriter.write("CUSTOMER: " + attributeValueAsString);
            this.fWriter.write("\r\n");
            this.fWriter.write("Symix Job Number: " + attributeValueAsString2);
            this.fWriter.write("|");
            this.fWriter.write("Customer Order Number: " + attributeValueAsString3);
            this.fWriter.write("|");
            this.fWriter.write("TYPE: " + attributeValueAsString4);
            this.fWriter.write("\r\n");
            this.fWriter.write("E NUMBER-SH: " + attributeValueAsString5);
            this.fWriter.write("\r\n");
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
            final Date date = new Date();
            simpleDateFormat.format(date);
            this.fWriter.write("DATE: " + simpleDateFormat.format(date));
            this.fWriter.write("\r\n");
            this.fWriter.write("INIT: " + attributeValueAsString6);
            this.fWriter.write("\r\n");
        }
    }
}
