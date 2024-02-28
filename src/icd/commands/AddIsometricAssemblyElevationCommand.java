package icd.commands;

import net.dirtt.icelib.main.ElevationEntity;
import net.iceedge.catalogs.icd.panel.ICDVerticalChase;
import java.util.Iterator;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.ElevationClusterEntity;
import com.iceedge.icebox.icecore.entity.IceEntity;
import net.iceedge.catalogs.icd.elevation.isometric.ICDIsometricAssemblyElevationEntity;
import net.dirtt.icebox.canvas2d.Render2D;
import java.util.Collection;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationUtilities;
import net.iceedge.catalogs.icd.ICDILine;
import java.util.ArrayList;
import net.iceedge.icecore.basemodule.util.ElevationUtility;
import net.dirtt.icelib.main.commandmodules.exception.CommandException;
import net.dirtt.icelib.main.CustomElevationEntity;
import net.dirtt.icelib.main.TransformableEntity;
import java.util.Vector;
import net.dirtt.appviews.AppView;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.commandmodules.Command;

public class AddIsometricAssemblyElevationCommand extends Command
{
    private int side;
    private Point3f location;
    private AppView appView;
    private Class<?> parentEntityClass;
    private Vector<TransformableEntity> entities;
    private Class<? extends CustomElevationEntity> elevationClass;
    
    public AddIsometricAssemblyElevationCommand(final AppView appView, final Point3f location) {
        super(appView.getCommandIteractor(), true);
        this.side = 0;
        this.location = null;
        this.appView = null;
        this.parentEntityClass = null;
        this.entities = null;
        this.elevationClass = null;
        this.appView = appView;
        this.location = location;
    }
    
    protected boolean doAction() throws CommandException {
        return this.addIsometricAssemblyElevationDoCommand(this.location);
    }
    
    public boolean addIsometricAssemblyElevationDoCommand(final Point3f point3f) {
        if (ElevationUtility.isSelectionSetValidForElevation(this.solution)) {
            final ArrayList<ICDILine> list = new ArrayList<ICDILine>();
            final ArrayList<TransformableEntity> list2 = new ArrayList<TransformableEntity>();
            for (final TransformableEntity e : this.solution.getSelectedEntitiesByClass(TransformableEntity.class, true)) {
                if (e.shouldDrawSingleElevation()) {
                    list2.add(e);
                    if (!(e instanceof ICDILine)) {
                        continue;
                    }
                    list.add((ICDILine)e);
                }
            }
            if (!list2.isEmpty()) {
                if (!list.isEmpty()) {
                    final ArrayList<ICDVerticalChase> verticalChasesCorrespondingToILines = ICDAssemblyElevationUtilities.getVerticalChasesCorrespondingToILines(ICDAssemblyElevationUtilities.getVerticalChases(this.solution), list);
                    if (verticalChasesCorrespondingToILines != null && !verticalChasesCorrespondingToILines.isEmpty()) {
                        list2.addAll(verticalChasesCorrespondingToILines);
                    }
                }
                final ElevationEntity elevation = ElevationUtility.getElevation(-1, Render2D.elevationCount + 1, point3f, (IceEntity)this.solution.getElevationClusterGroup(true), true, ICDIsometricAssemblyElevationEntity.class);
                if (elevation != null && elevation instanceof ICDIsometricAssemblyElevationEntity) {
                    ((ElevationClusterEntity)elevation).setUserClickedClusterPoint(new Point3f(0.0f, 1.0f, 0.0f));
                    ((ElevationClusterEntity)elevation).setUserClickedPlaneStart(new Point3f(0.0f, 0.0f, 0.0f));
                    ((ElevationClusterEntity)elevation).setUserClickedPlaneEnd(new Point3f(1.0f, 0.0f, 0.0f));
                    this.solution.getElevationClusterGroup(true).addToTree((EntityObject)elevation);
                    final Iterator<TransformableEntity> iterator2 = list2.iterator();
                    while (iterator2.hasNext()) {
                        iterator2.next().getElevationList().add(elevation.getUniqueId());
                    }
                    elevation.solve();
                    this.solution.fireModelEvents();
                    return true;
                }
            }
        }
        return false;
    }
}
