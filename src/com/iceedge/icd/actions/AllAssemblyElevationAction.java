package com.iceedge.icd.actions;

import net.iceedge.icecore.icecad.ice.IceCadIceApp;
import net.dirtt.icecad.cadtree.ICadIceDocumentListener;
import java.util.Iterator;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.CustomElevationEntity;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintableRoot;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationEntity;
import com.iceedge.icd.commands.ICDCommandModule;
import net.dirtt.icelib.main.EntityObject;
import net.iceedge.icecore.basemodule.interfaces.AssembleParent;
import net.dirtt.icelib.main.TransformableEntity;
import java.util.Vector;
import net.dirtt.appviews.AppView;
import org.apache.log4j.Logger;
import net.dirtt.icecomponents.cad.IceCadAction;

public class AllAssemblyElevationAction extends IceCadAction
{
    private static Logger logger;
    private static final String MENU_TEXT = "Create Assembly Elevation on All Assembled Entities";
    
    public AllAssemblyElevationAction() {
        super("Create Assembly Elevation on All Assembled Entities", "ICEADDALLASSEMBLYELEVATION");
    }
    
    public void actionPerformed(final AppView appView) {
        AllAssemblyElevationAction.logger.info((Object)("Executing action: " + this.getActionInfo()));
        final Solution solution = appView.getSolution();
        final Vector<TransformableEntity> vector = new Vector<TransformableEntity>();
        final Iterator breadthFirstEnumerationIterator = solution.getBreadthFirstEnumerationIterator(AssembleParent.class);
        while (breadthFirstEnumerationIterator.hasNext()) {
            final EntityObject assembleParent = (EntityObject) breadthFirstEnumerationIterator.next();
            if ((assembleParent).getAttributeValueAsBoolean("shouldAssemble", false)) {
                vector.add((TransformableEntity)assembleParent);
            }
        }
        ICDCommandModule.getInstance(solution).executeAddMultipleCustomElevationCommand(vector, appView, ICDAssemblyElevationEntity.class, AssemblyPaintableRoot.class);
    }
    
    public void cadActionPerformed(final ICadIceDocumentListener cadIceDocumentListener) {
        this.actionPerformed((AppView)cadIceDocumentListener);
    }
    
    public void cadDotNetActionPerformed(final IceCadIceApp iceCadIceApp) {
        this.actionPerformed((AppView)iceCadIceApp.getIceCadAppView());
    }
    
    static {
        AllAssemblyElevationAction.logger = Logger.getLogger(AllAssemblyElevationAction.class);
    }
}
