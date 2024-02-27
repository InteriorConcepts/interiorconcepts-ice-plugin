package com.iceedge.icd.actions;

import com.iceedge.icd.commands.ICDCommandModule;
import net.dirtt.appviews.AppView;
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;
import net.dirtt.icecomponents.IceAction;

public class ShowICDManufacturingReportAction extends IceAction
{
    private static Logger logger;
    
    public ShowICDManufacturingReportAction() {
        super("Manufacturing Report", (KeyStroke)null);
    }
    
    public void actionPerformed(final AppView appView) {
        ShowICDManufacturingReportAction.logger.info((Object)("Executing action: " + this.getActionInfo()));
        ICDCommandModule.getInstance(appView.getSolution()).executeShowManufacturingReport();
    }
    
    static {
        ShowICDManufacturingReportAction.logger = Logger.getLogger((Class)ShowICDManufacturingReportAction.class);
    }
}
