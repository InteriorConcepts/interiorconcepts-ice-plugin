// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.adapters;

import net.dirtt.icelib.report.generalquote.icd.ICDQuote;
import net.dirtt.icelib.main.Solution;
import com.iceedge.icd.actions.ShowICDQuoteAction;
import net.dirtt.icecomponents.IceAction;
import net.dirtt.icebox.ice2dViewer.IconConstants;
import javax.swing.Icon;
import net.dirtt.icelib.report.ReportType;
import com.iceedge.icd.reporting.ICDQuoteReportType;
import net.iceedge.icecore.plugin.systemInterfaces.IceQuoteAdapter;

public class ICDQuoteAdapter implements IceQuoteAdapter
{
    private ICDQuoteReportType reportType;
    private ReportType[] allReports;
    
    public ICDQuoteAdapter(final ReportType[] allReports) {
        this.reportType = new ICDQuoteReportType();
        this.allReports = allReports;
    }
    
    public ReportType[] getAllReportTypes() {
        return this.allReports;
    }
    
    public String getQuoteMenuName() {
        return "ICD Quote";
    }
    
    public Icon getQuoteIcon() {
        return IconConstants.LONG_QUOTE_ICON;
    }
    
    public IceAction getQuoteAction() {
        return new ShowICDQuoteAction();
    }
    
    public boolean shouldShowQuoteFromOpenCommand(final String anObject) {
        return "ICI".equals(anObject);
    }
    
    public void showQuoteForOpenCommand(final Solution solution) {
        ICDQuote.showQuote(solution, this.reportType.getId());
    }
    
    public String getManufacturerQuoteShortName() {
        return "ICI";
    }
    
    public String getManufacturerQuoteLongName() {
        return "Interior Concepts";
    }
    
    public String getQuoteTypeNameForNonVisibleItems() {
        return this.getManufacturerQuoteShortName();
    }
}
