package com.iceedge.icd.actions;

import net.dirtt.icelib.main.Solution;
import com.iceedge.icd.commands.ICDCommandModule;
import net.dirtt.appviews.AppView;
import org.apache.log4j.Logger;
import net.dirtt.icecomponents.SolutionSettingIceCheckBox;

public class ShowFabricCuttingReportTagsAction extends SolutionSettingIceCheckBox
{
    private static Logger logger;
    private static final String MENU_TEXT = "Fabric cutting report tags";
    
    public ShowFabricCuttingReportTagsAction() {
        super("Fabric cutting report tags", 17);
    }
    
    public void actionPerformed(final AppView appView) {
        ShowFabricCuttingReportTagsAction.logger.info((Object)("Executing action: " + this.getActionInfo()));
        final Solution solution = appView.getSolution();
        if (this.showICDFabricCutReportTag(solution)) {
            ICDCommandModule.getInstance(solution).executeSetSolutionDirtyAndFireModelEvents();
        }
    }
    
    private boolean showICDFabricCutReportTag(final Solution solution) {
        if (solution.getSolutionSetting().isShowICDFabricCutTag()) {
            solution.getSolutionSetting().setShowICDFabricCutTag(false);
            return true;
        }
        solution.getSolutionSetting().setShowICDFabricCutTag(true);
        if (solution.getReport(75) == null) {
            solution.buildUnCompiledReport(75);
        }
        return true;
    }
    
    static {
        ShowFabricCuttingReportTagsAction.logger = Logger.getLogger((Class)ShowFabricCuttingReportTagsAction.class);
    }
}
