package com.iceedge.icd.actions;

import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.Solution;
import java.util.Iterator;
import net.iceedge.catalogs.icd.panel.ICDTabContainer;
import net.dirtt.appviews.AppView;
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;
import net.dirtt.icecomponents.IceAction;

public class PrintTabConfigurationsAction extends IceAction
{
    private static Logger logger;
    private static final String MENU_TEXT = "Print Tab Configurations";
    
    public PrintTabConfigurationsAction() {
        super("Print Tab Configurations", (KeyStroke)null);
    }
    
    public void actionPerformed(final AppView appView) {
        PrintTabConfigurationsAction.logger.info((Object)("Executing action: " + this.getActionInfo()));
        if (!appView.getSolution().getChildrenByClass((Class)ICDTabContainer.class, true, true).isEmpty()) {
            PrintTabConfigurationsAction.logger.trace((Object)"===================Printing Tab Configurations===============================");
            boolean b = false;
            for (final ICDTabContainer icdTabContainer : appView.getSolution().getChildrenByClass((Class)ICDTabContainer.class, true, true)) {
                final String reportIndex = icdTabContainer.getReportIndex();
                if (reportIndex.trim().isEmpty()) {
                    continue;
                }
                b = true;
                PrintTabConfigurationsAction.logger.trace((Object)("Tab Container: " + this.getTabPath(icdTabContainer, appView.getSolution()) + "(" + icdTabContainer.hashCode() + ") Report String: " + reportIndex));
            }
            if (!b) {
                PrintTabConfigurationsAction.logger.trace((Object)"Tab Containers have no Report Data (No Tabs)");
            }
        }
    }
    
    private String getTabPath(final ICDTabContainer icdTabContainer, final Solution solution) {
        String string = "";
        boolean b = false;
        Object obj = icdTabContainer;
        EntityObject parentEntity = icdTabContainer.getParentEntity();
        while (parentEntity != null) {
            final String replace = ((ICDTabContainer)obj).getClass().getCanonicalName().replace(".class", "");
            String s = replace.substring(replace.lastIndexOf(".") + 1);
            if (string.length() > 0) {
                s += "/";
            }
            string = s + string;
            final EntityObject parentEntity2 = parentEntity.getParentEntity();
            obj = parentEntity;
            parentEntity = parentEntity2;
            if (solution.equals(obj)) {
                b = true;
                break;
            }
        }
        return b ? string : null;
    }
    
    static {
        PrintTabConfigurationsAction.logger = Logger.getLogger((Class)PrintTabConfigurationsAction.class);
    }
}
