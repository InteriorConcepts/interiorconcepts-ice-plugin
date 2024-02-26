// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.actions;

import net.iceedge.icecore.icecad.ice.IceCadIceApp;
import net.dirtt.icecad.cadtree.ICadIceDocumentListener;
import javax.vecmath.Point3f;
import com.iceedge.icd.commands.ICDCommandModule;
import net.dirtt.appviews.AppView;
import org.apache.log4j.Logger;
import net.dirtt.icecomponents.cad.IceCadAction;

public class IsometricAssemblyElevation extends IceCadAction
{
    private static Logger logger;
    private static final String MENU_TEXT = "Create Isometric Assembly Elevation on Selected Assembled Entities";
    
    public IsometricAssemblyElevation() {
        super("Create Isometric Assembly Elevation on Selected Assembled Entities", "ICEADDISOASSEMBLYELEVATION");
    }
    
    public void actionPerformed(final AppView appView) {
        IsometricAssemblyElevation.logger.info((Object)("Executing action: " + this.getActionInfo()));
        final Point3f point = appView.getPlanInteractor().getPoint("Select Insertion Point");
        if (point != null) {
            ICDCommandModule.getInstance(appView.getSolution()).executeAddIsometricAssemblyElevationCommand(appView, point);
        }
    }
    
    public void cadActionPerformed(final ICadIceDocumentListener cadIceDocumentListener) {
        this.actionPerformed((AppView)cadIceDocumentListener);
    }
    
    public void cadDotNetActionPerformed(final IceCadIceApp iceCadIceApp) {
        this.actionPerformed((AppView)iceCadIceApp.getIceCadAppView());
    }
    
    static {
        IsometricAssemblyElevation.logger = Logger.getLogger((Class)IsometricAssemblyElevation.class);
    }
}
