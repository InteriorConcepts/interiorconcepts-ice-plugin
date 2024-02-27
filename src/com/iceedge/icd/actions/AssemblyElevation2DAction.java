package com.iceedge.icd.actions;

import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintableRoot;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationEntity;
import net.dirtt.icelib.main.commandmodules.CommandModule;
import net.dirtt.appviews.AppView;
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;
import net.dirtt.icecomponents.IceAction;

public class AssemblyElevation2DAction extends IceAction
{
    private static Logger logger;
    private static final String MENU_TEXT = "Assembly elevation on selected assembled entity";
    
    public AssemblyElevation2DAction() {
        super("Assembly elevation on selected assembled entity", (KeyStroke)null);
    }
    
    public void actionPerformed(final AppView appView) {
        AssemblyElevation2DAction.logger.info((Object)("Executing action: " + this.getActionInfo()));
        CommandModule.getInstance(appView.getSolution()).executeAddCustomElevationCommand(appView, (Class)ICDAssemblyElevationEntity.class, (Class)AssemblyPaintableRoot.class, false);
    }
    
    static {
        AssemblyElevation2DAction.logger = Logger.getLogger((Class)AllAssemblyElevation2DAction.class);
    }
}
