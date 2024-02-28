package com.iceedge.icd.actions;

import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icelib.main.Solution;
import com.iceedge.icd.commands.ICDCommandModule;
import net.dirtt.appviews.AppView;
import org.apache.log4j.Logger;
import net.dirtt.icecomponents.SolutionSettingIceCheckBox;

public class ShowTileInstallationTagsAction extends SolutionSettingIceCheckBox
{
    private static Logger logger;
    private static final String MENU_TEXT = "Tile installation tags";
    
    public ShowTileInstallationTagsAction() {
        super("Tile installation tags", 23);
    }
    
    public void actionPerformed(final AppView appView) {
        ShowTileInstallationTagsAction.logger.info((Object)("Executing action: " + this.getActionInfo()));
        final Solution solution = appView.getSolution();
        if (this.showICDTileInstallationTag(solution)) {
            ICDCommandModule.getInstance(solution).executeSetSolutionDirtyAndFireModelEvents();
        }
    }
    
    private boolean showICDTileInstallationTag(final Solution solution) {
        final SolutionSetting solutionSetting = solution.getSolutionSetting();
        if (solutionSetting.isShowICDTileInstallationTag()) {
            solutionSetting.setShowICDTileInstallationTag(false);
        }
        else {
            solutionSetting.setShowICDTileInstallationTag(true);
            if (solution.getReport(51) == null) {
                solution.buildUnCompiledReport(51);
            }
        }
        return true;
    }
    
    static {
        ShowTileInstallationTagsAction.logger = Logger.getLogger(ShowTileInstallationTagsAction.class);
    }
}
