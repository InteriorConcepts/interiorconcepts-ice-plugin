package com.iceedge.icd.actions;

import net.dirtt.icelib.main.Solution;
import com.iceedge.icd.commands.ICDCommandModule;
import net.dirtt.icebox.IceBoxApp;
import net.dirtt.appviews.AppView;
import org.apache.log4j.Logger;
import net.dirtt.icecomponents.SolutionSettingIceCheckBox;

public class ShowDXFExportAction extends SolutionSettingIceCheckBox
{
    private static Logger logger;
    private static final String MENU_TEXT = "Show DXF in Export";
    
    public ShowDXFExportAction() {
        super("Show DXF in Export", 20);
    }
    
    public void actionPerformed(final AppView appView) {
        ShowDXFExportAction.logger.info((Object)("Executing action: " + this.getActionInfo()));
        super.actionPerformed(appView);
        final Solution solution = appView.getSolution();
        IceBoxApp.ENABLE_DXF_OUTPUT_ON_EXPORT = this.isChecked(solution);
        ICDCommandModule.getInstance(solution).executeSetSolutionDirtyAndFireModelEvents();
    }
    
    static {
        ShowDXFExportAction.logger = Logger.getLogger(ShowDXFExportAction.class);
    }
}
