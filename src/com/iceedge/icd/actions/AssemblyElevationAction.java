package com.iceedge.icd.actions;

import net.iceedge.icecore.icecad.ice.IceCadIceApp;
import net.dirtt.icecad.cadtree.ICadIceDocumentListener;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintableRoot;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationEntity;
import net.dirtt.icelib.main.commandmodules.CommandModule;
import net.dirtt.appviews.AppView;
import org.apache.log4j.Logger;
import net.dirtt.icecomponents.cad.IceCadAction;

public class AssemblyElevationAction extends IceCadAction
{
    private static Logger logger;
    private static final String MENU_TEXT = "Create Assembly Elevation on Selected Assembled Entity";
    
    public AssemblyElevationAction() {
        super("Create Assembly Elevation on Selected Assembled Entity", "ICEADDASSEMBLYELEVATION");
    }
    
    public void actionPerformed(final AppView appView) {
        AssemblyElevationAction.logger.info((Object)("Executing action: " + this.getActionInfo()));
        CommandModule.getInstance(appView.getSolution()).executeAddCustomElevationCommand(appView, ICDAssemblyElevationEntity.class, AssemblyPaintableRoot.class, false);
    }
    
    public void cadActionPerformed(final ICadIceDocumentListener cadIceDocumentListener) {
        this.actionPerformed((AppView)cadIceDocumentListener);
    }
    
    public void cadDotNetActionPerformed(final IceCadIceApp iceCadIceApp) {
        this.actionPerformed((AppView)iceCadIceApp.getIceCadAppView());
    }
    
    static {
        AssemblyElevationAction.logger = Logger.getLogger(AssemblyElevationAction.class);
    }
}
