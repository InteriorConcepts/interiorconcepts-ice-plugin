package com.iceedge.icd.reporting;

import net.dirtt.IceApp;
import java.io.File;
import net.dirtt.icelib.report.CuttingListFabric.CuttingListFabric;

public class ICDCuttingListFabricReport extends CuttingListFabric
{
    public void exportReport(final File file, final boolean writeReportHeader) {
        final ICDCuttingListReportWriter icdCuttingListReportWriter = new ICDCuttingListReportWriter(this.getRoot().getReportReference(), IceApp.getCurrentSolution());
        icdCuttingListReportWriter.setWriteReportHeader(writeReportHeader);
        icdCuttingListReportWriter.writeToFile(file);
    }
}
