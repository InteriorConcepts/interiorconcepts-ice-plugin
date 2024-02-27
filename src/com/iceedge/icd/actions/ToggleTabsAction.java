package com.iceedge.icd.actions;

import net.dirtt.appviews.AppView;
import net.dirtt.icebox.IceBoxApp;
import net.dirtt.menus.Menus;
import net.dirtt.icebox.IceBoxMainPanel;
import net.dirtt.icecomponents.box.IceBoxCheckBox;

public class ToggleTabsAction extends IceBoxCheckBox
{
    public ToggleTabsAction() {
        super("Toggle Tabs");
    }
    
    public void boxActionPerformed(final IceBoxMainPanel iceBoxMainPanel) {
        Menus.logger.info((Object)("Executing action: " + this.getActionInfo()));
        IceBoxApp.SHOW_TABS = !IceBoxApp.SHOW_TABS;
        iceBoxMainPanel.panel2d.getMainView2D().showBoxDebug();
        Menus.getCommandModule((AppView)iceBoxMainPanel).executeResolveSolution((AppView)iceBoxMainPanel);
        this.setChecked(IceBoxApp.SHOW_TABS);
    }
    
    public boolean getInitialCheckBoxState(final AppView appView) {
        return IceBoxApp.SHOW_TABS;
    }
}
