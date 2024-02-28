package com.iceedge.icd.actions;

import net.dirtt.icebox.IceBoxApp;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icelib.main.Solution;
import com.iceedge.icd.commands.ICDCommandModule;
import net.dirtt.appviews.AppView;
import org.apache.log4j.Logger;
import net.dirtt.icecomponents.SolutionSettingIceCheckBox;

public class ShowPreassembledTagAction extends SolutionSettingIceCheckBox
{
    private static Logger logger;
    private static final String MENU_TEXT = "Preassembled report tags";
    
    public ShowPreassembledTagAction() {
        super("Preassembled report tags", 16);
    }
    
    public void actionPerformed(final AppView appView) {
        ShowPreassembledTagAction.logger.info((Object)("Executing action: " + this.getActionInfo()));
        final Solution solution = appView.getSolution();
        if (this.showICDPreAssembledReportTag(solution)) {
            ICDCommandModule.getInstance(solution).executeSetSolutionDirtyAndFireModelEvents();
        }
    }
    
    public boolean getInitialCheckBoxState(final AppView appView) {
        super.getInitialCheckBoxState(appView);
        return true;
    }
    
    public void refreshSettings(final SolutionSetting solutionSetting) {
        final Solution currentSolution = IceBoxApp.getCurrentSolution();
        final boolean checked = this.isChecked(currentSolution);
        if (checked && currentSolution.getReport(51) == null) {
            currentSolution.buildUnCompiledReport(51);
        }
        solutionSetting.setShowICDPreassembledTag(checked);
    }
    
    private boolean showICDPreAssembledReportTag(final Solution solution) {
        if (solution.getSolutionSetting().isShowICDPreassembledTag()) {
            solution.getSolutionSetting().setShowICDPreassembledTag(false);
            return true;
        }
        solution.getSolutionSetting().setShowICDPreassembledTag(true);
        if (solution.getReport(51) == null) {
            solution.buildUnCompiledReport(51);
        }
        return true;
    }
    
    static {
        ShowPreassembledTagAction.logger = Logger.getLogger(ShowPreassembledTagAction.class);
    }
}
