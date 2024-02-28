package com.iceedge.icd.actions;

import com.iceedge.icd.commands.ICDCommandModule;
import net.dirtt.appviews.AppView;
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;
import net.dirtt.icecomponents.IceAction;

public class ShowICDSheetAction extends IceAction
{
    private static Logger logger;
    
    public ShowICDSheetAction() {
        super("ICD ICEsheet", (KeyStroke)null);
    }
    
    public void actionPerformed(final AppView appView) {
        ShowICDSheetAction.logger.info((Object)("Executing action: " + this.getActionInfo()));
        ICDCommandModule.getInstance(appView.getSolution()).executeShowSheet();
    }
    
    static {
        ShowICDSheetAction.logger = Logger.getLogger(ShowICDSheetAction.class);
    }
}
