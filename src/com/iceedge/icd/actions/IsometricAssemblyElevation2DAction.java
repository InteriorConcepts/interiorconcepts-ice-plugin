// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.actions;

import javax.vecmath.Point3f;
import com.iceedge.icd.commands.ICDCommandModule;
import net.dirtt.appviews.AppView;
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;
import net.dirtt.icecomponents.IceAction;

public class IsometricAssemblyElevation2DAction extends IceAction
{
    private static Logger logger;
    private static final String MENU_TEXT = "Create Isometric Assembly Elevation on Selected Assembled Entities";
    
    public IsometricAssemblyElevation2DAction() {
        super("Create Isometric Assembly Elevation on Selected Assembled Entities", (KeyStroke)null);
    }
    
    public void actionPerformed(final AppView appView) {
        IsometricAssemblyElevation2DAction.logger.info((Object)("Executing action: " + this.getActionInfo()));
        final Point3f point = appView.getPlanInteractor().getPoint("Select Insertion Point");
        if (point != null) {
            ICDCommandModule.getInstance(appView.getSolution()).executeAddIsometricAssemblyElevationCommand(appView, point);
        }
    }
    
    static {
        IsometricAssemblyElevation2DAction.logger = Logger.getLogger((Class)IsometricAssemblyElevation2DAction.class);
    }
}
