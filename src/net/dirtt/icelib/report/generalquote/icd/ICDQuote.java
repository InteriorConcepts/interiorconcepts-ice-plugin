package net.dirtt.icelib.report.generalquote.icd;

import net.dirtt.icelib.report.Reportable;
import net.dirtt.icelib.report.ReportNode;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.report.generalquote.GeneralQuoteReportNode;
import net.dirtt.utilities.IceFileUtilities;
import net.iceedge.test.icelib.quote.TestQuote.furniture.GeneralQuoteTestCase;
import net.dirtt.icebox.canvas2d.ICEEventQueue;
import net.dirtt.icelib.report.viewers.IEViewer;
import com.iceedge.icd.commands.ICDCommandModule;
import net.dirtt.icebox.canvas2d.IceQueueExecutable;
import net.dirtt.IceApp;
import java.io.File;
import net.dirtt.icelib.report.IgnoreSkuQuoteReportable;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.report.generalquote.GeneralQuote;

public class ICDQuote extends GeneralQuote
{
    public ICDQuote() {
    }
    
    public ICDQuote(final String s, final Solution solution) {
        super(s, solution);
    }
    
    public Class getRequiredInterfaceForQuote() {
        return IgnoreSkuQuoteReportable.class;
    }
    
    public String getQuoteManufacturer() {
        return "ICI";
    }
    
    public String getReportXSLFileName() {
        return "ICDQuote.xsl";
    }
    
    public static void exportICD(final GeneralQuote generalQuote, final File file, final String s, final String s2) {
        if (generalQuote != null) {
            new ICDQuoteExport(generalQuote.getRoot().getReportReference(), IceApp.getCurrentSolution()).writeToFile(file, s, s2);
        }
    }
    
    public boolean shouldAllowExport() {
        return true;
    }
    
    public void handleExport() {
        ICEEventQueue.scheduleEvent((IceQueueExecutable)new IceQueueExecutable("Export ICD") {
            public void executeTask() {
                ICDCommandModule.getInstance(ICDQuote.this.solution).executeExportInteriorConcepts(null, ICDQuote.this);
            }
        });
    }
    
    public String getExportMenuCatalogName() {
        return "Interior Concepts";
    }
    
    public void writeCSVToFile(final File file, final int n, final char c) {
        final String csvContents = this.getCSVContents(GeneralQuoteTestCase.getHeadings());
        if (csvContents != null) {
            IceFileUtilities.writeTextFile(file, csvContents);
        }
    }
    
    public void exportReport(final File file, final boolean b) {
        this.writeCSVToFile(file, 0, 'f');
    }
    
    public ReportNode getFirstLevelBucket(final GeneralQuoteReportNode generalQuoteReportNode, final EntityObject entityObject) {
        return (ReportNode)new ICDQuoteFirstLevelBucket((ReportNode)generalQuoteReportNode, (Reportable)entityObject);
    }
    
    public ReportNode getCategoryBucket(final GeneralQuoteReportNode generalQuoteReportNode, final EntityObject entityObject) {
        return (ReportNode)new ICDQuoteCategoryBucket((ReportNode)generalQuoteReportNode, (Reportable)entityObject);
    }
    
    public String getLogoName() {
        return "ICD_logo.jpg";
    }
}
