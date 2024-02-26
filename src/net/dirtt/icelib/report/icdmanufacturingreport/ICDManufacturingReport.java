// 
// Decompiled by Procyon v0.5.36
// 

package net.dirtt.icelib.report.icdmanufacturingreport;

import net.dirtt.icelib.report.ReportRootInterface;
import java.util.List;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.report.TagRegister;
import net.iceedge.catalogs.icd.interfaces.ICDInstallTagDrawable;
import com.iceedge.icebox.icecore.entity.IceEntity;
import net.dirtt.IceApp;
import java.io.File;
import java.util.Vector;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.report.Report;

public class ICDManufacturingReport extends Report
{
    public static final String ICD_MAN_REPORT_PARTNO = "PartNo";
    public static final String ICD_MAN_REPORT_QTY = "Qty";
    public static final String ICD_MAN_REPORT_TYPE = "Type";
    public static final String ICD_MAN_REPORT_SUBTYPE = "SubType";
    public static final String ICD_MAN_REPORT_DESC = "Description";
    public static final String ICD_MAN_REPORT_DEPTH = "Depth";
    public static final String ICD_MAN_REPORT_WIDTH = "Width";
    public static final String ICD_MAN_REPORT_HEIGHT = "Height";
    public static final String ICD_MAN_REPORT_USERTAG = "UserTag";
    public static final String ICD_MAN_REPORT_OPT = "Option0";
    public static final String ICD_MAN_REPORT_ASSM = "Assembled";
    public static final String ICD_MAN_REPORT_TILE_INDICATOR = "TileIndicator";
    String lastTileTag;
    
    public ICDManufacturingReport() {
        this.lastTileTag = null;
        this.requiredInterface = ICDManufacturingReportable.class;
        this.setRequiredAttributesAndValues();
    }
    
    public ICDManufacturingReport(final String s, final Solution solution) {
        super(s, solution);
        this.lastTileTag = null;
        this.initReport();
        this.requiredInterface = ICDManufacturingReportable.class;
        this.setRequiredAttributesAndValues();
    }
    
    public void compile() {
        super.compile();
    }
    
    protected void initReport() {
        (this.root = (ReportRootInterface)new ICDManufacturingReportRoot()).setReportReference((Report)this);
    }
    
    public Vector<String> getCSVReportHeaders() {
        final Vector<String> vector = new Vector<String>();
        vector.add("PartNo");
        vector.add("Qty");
        vector.add("Type");
        vector.add("SubType");
        vector.add("Description");
        vector.add("Depth");
        vector.add("Width");
        vector.add("Height");
        vector.add("UserTag");
        for (int i = 1; i < 10; ++i) {
            vector.add("Option0" + i);
        }
        vector.add("Assembled");
        vector.add("TileIndicator");
        return vector;
    }
    
    public void handlePossibleChange() {
        super.handlePossibleChange();
    }
    
    public String getReportXMLFileName() {
        return "";
    }
    
    public void getExtraXMLInformation(final StringBuffer sb) {
    }
    
    public String getReportXSLFileName() {
        return "";
    }
    
    public String getConfigurationFile() {
        return null;
    }
    
    public void getExtraCSVInformation(final int n, final StringBuffer sb) {
    }
    
    public void getExtraCSVHeaders(final StringBuffer sb) {
    }
    
    public int getQuoteXMLType() {
        return 0;
    }
    
    public int getCompilationPriority() {
        return 2;
    }
    
    public void setSuspended(final boolean suspended) {
        if (this.isSuspended() != suspended) {
            this.solution.setDirtyForSolutionRedraw();
        }
        super.setSuspended(suspended);
    }
    
    public String getName() {
        return "ICD Manufacturing Report";
    }
    
    public boolean isSuspendable() {
        return true;
    }
    
    public void exportReport(final File file, final boolean writeReportHeader) {
        final ICDManufacturingReportWriter icdManufacturingReportWriter = new ICDManufacturingReportWriter(this.getRoot().getReportReference(), IceApp.getCurrentSolution());
        icdManufacturingReportWriter.setWriteReportHeader(writeReportHeader);
        icdManufacturingReportWriter.writeToFile(file);
    }
    
    public void writeCSVToFile(final File file, final int n, final char c) {
        this.exportReport(file, false);
    }
    
    public String registerTagGeneral(final IceEntity iceEntity) {
        String lastTileTag = null;
        if (this.solution != null) {
            final TagRegister tagRegister = this.solution.getTagRegister();
            if (tagRegister != null) {
                if (iceEntity instanceof ICDInstallTagDrawable) {
                    lastTileTag = tagRegister.registerTileTag(this.lastTileTag);
                    this.lastTileTag = lastTileTag;
                }
                else {
                    lastTileTag = super.registerTagGeneral(iceEntity);
                }
            }
        }
        return lastTileTag;
    }
    
    public String getInstallTag(final EntityObject entityObject) {
        String tag = "";
        final ICDManufacturingReportNode icdManufacturingReportNode = (ICDManufacturingReportNode)entityObject.getBucketInReport((Report)this);
        if (icdManufacturingReportNode != null) {
            tag = icdManufacturingReportNode.getTag();
        }
        return tag;
    }
    
    public boolean shouldSaveFurnitureReport() {
        return false;
    }
}
