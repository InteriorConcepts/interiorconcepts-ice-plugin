package com.iceedge.icd.reporting;

import net.dirtt.icelib.report.generalquote.icd.ICDQuote;
import net.dirtt.icelib.report.ReportType;

public class ICDQuoteReportType extends ReportType
{
    public ICDQuoteReportType() {
        super(33, "ICD Quote", ICDQuote.class);
    }
    
    public String getShortString() {
        return "ICD Quote";
    }
}
