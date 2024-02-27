package com.iceedge.icd.actions;

import net.dirtt.icebox.IceBoxMainPanel;
import net.dirtt.icelib.report.generalquote.icd.ICDQuote;
import net.dirtt.icebox.IceBoxApp;
import net.dirtt.appviews.AppView;
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;
import net.dirtt.icecomponents.IceAction;

public class ShowICDQuoteAction extends IceAction
{
    private static Logger logger;
    private static final int MENU_LOCATION = 2;
    private static final String PARENT_MENU_STRING = "File";
    
    public ShowICDQuoteAction() {
        super("ICD Quote", (KeyStroke)null);
    }
    
    public void actionPerformed(final AppView appView) {
        ShowICDQuoteAction.logger.info((Object)("Executing action: " + this.getActionInfo()));
        if (IceBoxApp.getMainPanel() != null) {
            final IceBoxMainPanel mainPanel = IceBoxApp.getMainPanel();
            if (!this.getBoxText().equals(mainPanel.getCurrentQuote())) {
                mainPanel.setCurrentQuote(this.getIceBoxMainPanelAction(mainPanel));
                ShowICDQuoteAction.logger.trace((Object)(this.getBoxText() + " set as default"));
            }
            mainPanel.changeQuoteOption(this.getBoxText());
            ICDQuote.showQuote(appView.getSolution(), 33, mainPanel.getQuoteWindowListener());
        }
        else {
            ICDQuote.showQuote(appView.getSolution(), 33);
        }
    }
    
    static {
        ShowICDQuoteAction.logger = Logger.getLogger((Class)ShowICDQuoteAction.class);
    }
}
