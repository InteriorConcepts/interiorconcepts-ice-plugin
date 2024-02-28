package com.iceedge.icd.actions;

import net.dirtt.icelib.report.Report;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.commandmodules.CommandModule;
//import Quality.testCases.CSVCompareTestCase;
import net.iceedge.test.TestType;
import java.io.File;
//import com.iceedge.icd.testing.ICDReportTestSuite;
import net.iceedge.common.ui.IceFileChooser;
import net.dirtt.appviews.AppView;
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;
import net.dirtt.icecomponents.IceAction;

public class ICDReportTestAction extends IceAction
{
    private static Logger logger;
    private static final String MENU_TEXT = "Create ICD Report Test";
    
    public ICDReportTestAction() {
        super("Create ICD Report Test", (KeyStroke)null);
    }
    
    public void actionPerformed(final AppView appView) {
        ICDReportTestAction.logger.info((Object)("Executing action: " + this.getActionInfo()));
        final IceFileChooser iceFileChooser = new IceFileChooser();
        //iceFileChooser.setCurrentDirectory(new File(ICDReportTestSuite.ICDTestFolder));
        iceFileChooser.showSaveDialog(appView.getMainWindow());
        final File selectedFile = iceFileChooser.getSelectedFile();
        if (selectedFile != null) {
            final String replace = selectedFile.getAbsolutePath().replace(".ice", "").replace(".csv", "").replace(".sif", "");
            final Solution solution = appView.getSolution();
            final File file = new File(replace + ".ice");
            Report report = solution.getReport(51);
            if (report == null) {
                report = solution.buildUnCompiledReport(51);
            }
            report.compile();
            //report.exportReport(new File(file.getParent() + File.separator + CSVCompareTestCase.getCSVFileName(file.getName(), TestType.REPORT, 51, "ManufacturingReport") + ".csv"), false);
            CommandModule.getInstance(appView.getSolution()).executeSaveCommand(appView.getCommandIteractor(), appView.getMainWindow(), file, false);
        }
    }
    
    static {
        ICDReportTestAction.logger = Logger.getLogger(ICDReportTestAction.class);
    }
}
