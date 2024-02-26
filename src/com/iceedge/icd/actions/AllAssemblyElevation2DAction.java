// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.actions;

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
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;
import net.dirtt.icecomponents.IceAction;

public class AllAssemblyElevation2DAction extends IceAction
{
    private static Logger logger;
    private static final String MENU_TEXT = "Assembly elevation on all assembled entities";
    
    public AllAssemblyElevation2DAction() {
        super("Assembly elevation on all assembled entities", (KeyStroke)null);
    }
    
    public void actionPerformed(final AppView appView) {
        AllAssemblyElevation2DAction.logger.info((Object)("Executing action: " + this.getActionInfo()));
        final Solution solution = appView.getSolution();
        final Vector<TransformableEntity> vector = new Vector<TransformableEntity>();
        final Iterator breadthFirstEnumerationIterator = solution.getBreadthFirstEnumerationIterator((Class)AssembleParent.class);
        while (breadthFirstEnumerationIterator.hasNext()) {
            final AssembleParent assembleParent = breadthFirstEnumerationIterator.next();
            if (((EntityObject)assembleParent).getAttributeValueAsBoolean("shouldAssemble", false)) {
                vector.add((TransformableEntity)assembleParent);
            }
        }
        ICDCommandModule.getInstance(appView.getSolution()).executeAddMultipleCustomElevationCommand(vector, appView, ICDAssemblyElevationEntity.class, AssemblyPaintableRoot.class);
    }
    
    static {
        AllAssemblyElevation2DAction.logger = Logger.getLogger((Class)AllAssemblyElevation2DAction.class);
    }
}
