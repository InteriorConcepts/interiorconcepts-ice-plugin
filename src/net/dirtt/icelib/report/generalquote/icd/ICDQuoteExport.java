package net.dirtt.icelib.report.generalquote.icd;

import java.util.regex.Pattern;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import net.dirtt.icelib.main.EntityObject;
import net.iceedge.catalogs.icd.panel.ICDTile;
import net.dirtt.icelib.custom.CustomItem;
import net.dirtt.icelib.main.TypeableEntity;
import net.iceedge.catalogs.icd.panel.ICDPanel;
import java.util.Collection;
import net.dirtt.icelib.report.generalquote.GeneralQuoteReportNode;
import java.util.Vector;
import java.io.IOException;
import java.util.Iterator;
import java.util.Enumeration;
import net.dirtt.icelib.report.generalquote.GeneralQuoteSectionBucket;
import net.dirtt.IceApp;
import net.dirtt.utilities.IceFileUtilities;
import java.io.File;
import java.io.FileWriter;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.report.Report;
import org.apache.log4j.Logger;
import net.dirtt.icelib.report.ReportNode;

public class ICDQuoteExport extends ReportNode
{
    private static Logger logger;
    private Report report;
    private Solution solution;
    public FileWriter fWriter;
    public File file;
    private String reportNumber;
    private String revisionNumber;
    int sequenceCounter;
    
    public ICDQuoteExport(final Report report, final Solution solution) {
        this.reportNumber = "";
        this.revisionNumber = "";
        this.sequenceCounter = 1;
        this.report = report;
        this.solution = solution;
    }
    
    public void writeToFile(final File file, final String reportNumber, final String revisionNumber) {
        this.file = new File("tempICDOut.sif");
        this.reportNumber = reportNumber;
        this.revisionNumber = revisionNumber;
        try {
            this.openFileForWriting(this.file);
            this.writeQuoteBuckets();
            this.closeFile();
            IceFileUtilities.copyFile(this.file, file);
        }
        catch (Exception ex) {
            System.out.println("Automatically Generated Exception Log(ICDQuoteExport.java,84)[" + ex.getClass() + "]: " + ex.getMessage());
            ex.printStackTrace();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    IceApp.showLogError("A problem occurred while writing out ICD Export. File could not be written.");
                }
            }).start();
        }
        finally {
            this.file.delete();
        }
    }
    
    public void writeQuoteBuckets() throws IOException {
        final Enumeration<ReportNode> breadthFirstEnumeration = this.report.getBreadthFirstEnumeration();
        while (breadthFirstEnumeration.hasMoreElements()) {
            final ReportNode reportNode = breadthFirstEnumeration.nextElement();
            if (reportNode instanceof GeneralQuoteSectionBucket) {
                this.sequenceCounter = 1;
                final Iterator<ICDQuoteFirstLevelBucket> iterator = this.getFirstLevelBuckets(reportNode).iterator();
                while (iterator.hasNext()) {
                    this.writeFirstLevelBucketEntity(iterator.next());
                }
            }
        }
    }
    
    public Vector<ICDQuoteFirstLevelBucket> getFirstLevelBuckets(final ReportNode reportNode) {
        final Vector<ICDQuoteFirstLevelBucket> vector = new Vector<ICDQuoteFirstLevelBucket>();
        for (int i = 0; i < reportNode.getChildCount(); ++i) {
            final GeneralQuoteReportNode generalQuoteReportNode = (GeneralQuoteReportNode)reportNode.getChildAt(i);
            if (generalQuoteReportNode instanceof ICDQuoteFirstLevelBucket) {
                vector.add((ICDQuoteFirstLevelBucket)generalQuoteReportNode);
            }
            else {
                vector.addAll(this.getFirstLevelBuckets((ReportNode)generalQuoteReportNode));
            }
        }
        return vector;
    }
    
    public Vector<ICDQuoteSecondLevelBucket> getSecondLevelBuckets(final ReportNode reportNode) {
        final Vector<ICDQuoteSecondLevelBucket> vector = new Vector<ICDQuoteSecondLevelBucket>();
        for (int i = 0; i < reportNode.getChildCount(); ++i) {
            final GeneralQuoteReportNode generalQuoteReportNode = (GeneralQuoteReportNode)reportNode.getChildAt(i);
            if (generalQuoteReportNode instanceof ICDQuoteSecondLevelBucket) {
                vector.add((ICDQuoteSecondLevelBucket)generalQuoteReportNode);
            }
            else {
                vector.addAll(this.getSecondLevelBuckets((ReportNode)generalQuoteReportNode));
            }
        }
        return vector;
    }
    
    public void writeFirstLevelBucketEntity(final ICDQuoteFirstLevelBucket icdQuoteFirstLevelBucket) throws IOException {
        if (icdQuoteFirstLevelBucket != null) {
            final EntityObject aEntity = icdQuoteFirstLevelBucket.getAEntity();
            final boolean b = aEntity instanceof ICDPanel && ((ICDPanel)aEntity).isStackPanel();
            if (aEntity instanceof TypeableEntity && !b) {
                if (((TypeableEntity)aEntity).getAttributeValueAsString("Product_Type") != null || aEntity instanceof CustomItem) {
                    this.writeNode(icdQuoteFirstLevelBucket, aEntity, b);
                    ++this.sequenceCounter;
                }
                else {
                    System.err.println("Skipping " + ((TypeableEntity)aEntity).getSKUForXML());
                }
            }
            else if (b) {
                final Iterator iterator = ((ICDPanel)aEntity).getChildrenByClass(ICDTile.class, true, true).iterator();
                while (iterator.hasNext()) {
                    this.writeNode(icdQuoteFirstLevelBucket, (EntityObject)iterator.next(), b);
                    ++this.sequenceCounter;
                }
            }
        }
    }
    
    private void writeNode(final ICDQuoteFirstLevelBucket icdQuoteFirstLevelBucket, final EntityObject entityObject, final boolean b) throws IOException {
        this.fWriter.write(this.reportNumber);
        this.fWriter.write("|");
        this.fWriter.write(((TypeableEntity)entityObject).getUserTagNameAttribute("TagName1"));
        this.fWriter.write("|");
        this.fWriter.write(this.revisionNumber);
        this.fWriter.write("|");
        this.fWriter.write(this.sequenceCounter + "");
        this.fWriter.write("|");
        if (entityObject instanceof CustomItem) {
            this.fWriter.write("Custom Item");
        }
        else {
            this.fWriter.write(ICDManufacturingUtils.getProductType((TypeableEntity)entityObject));
        }
        this.fWriter.write("|");
        this.fWriter.write(((TypeableEntity)entityObject).getSKUForXML());
        this.fWriter.write("|");
        this.fWriter.write(((TypeableEntity)entityObject).getQuoteDescription());
        this.fWriter.write("|");
        this.fWriter.write(icdQuoteFirstLevelBucket.getQuantityForXML((TypeableEntity)entityObject));
        this.fWriter.write("|");
        this.fWriter.write("0");
        this.fWriter.write("|");
        if (b) {
            this.fWriter.write((((TypeableEntity)entityObject).getAttributeValueAsString("Base_Price") != null) ? ((TypeableEntity)entityObject).getAttributeValueAsString("Base_Price") : "");
        }
        else {
            this.fWriter.write(icdQuoteFirstLevelBucket.getExtendedSellPriceForXML());
        }
        this.fWriter.write("|");
        this.fWriter.write("000|\r\n");
    }
    
    static String replaceAllWords(String replaceAll, final String regex, final String replacement) {
        replaceAll = Pattern.compile(regex).matcher(replaceAll).replaceAll(replacement);
        return replaceAll;
    }
    
    public void openFileForWriting(final File file) throws IOException {
        this.fWriter = new FileWriter(file);
    }
    
    public void closeFile() {
        try {
            this.fWriter.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    static {
        ICDQuoteExport.logger = Logger.getLogger(ICDQuoteExport.class);
    }
}
