package icd.commands;

import net.dirtt.appviews.PlanInteractor;
import net.dirtt.icelib.main.commandmodules.CommandModuleWork;
import net.dirtt.icelib.main.CustomElevationEntity;
import net.dirtt.icelib.main.TransformableEntity;
import java.util.Vector;
import net.dirtt.appviews.AppView;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.commandmodules.Command;

public class AddMultipleCustomElevationCommand extends Command
{
    private int side;
    private Point3f location;
    private AppView appView;
    private Class<?> parentEntityClass;
    private Vector<TransformableEntity> entities;
    private Class<? extends CustomElevationEntity> elevationClass;
    public static String version;
    
    public AddMultipleCustomElevationCommand(final AppView appView, final Vector<TransformableEntity> entities, final Class<? extends CustomElevationEntity> elevationClass, final Class<?> parentEntityClass) {
        super(appView.getCommandIteractor(), true);
        this.side = 0;
        this.location = null;
        this.appView = null;
        this.parentEntityClass = null;
        this.entities = null;
        this.elevationClass = null;
        this.elevationClass = elevationClass;
        this.appView = appView;
        this.parentEntityClass = parentEntityClass;
        this.entities = entities;
    }
    
    protected boolean doAction() {
        final PlanInteractor planInteractor = this.appView.getPlanInteractor();
        final Point3f point = planInteractor.getPoint("Select Location");
        if (point == null) {
            return false;
        }
        final Point3f point2 = planInteractor.getPoint("Select Return Point");
        if (point2 == null) {
            return false;
        }
        final boolean addMultipleCustomElevationDoCommand = CommandModuleWork.addMultipleCustomElevationDoCommand((Vector)this.entities, this.solution, this.side, point, point, point2, this.parentEntityClass, this.elevationClass);
        this.solution.solve();
        this.solution.fireModelEvents();
        return addMultipleCustomElevationDoCommand;
    }
    
    static {
        AddMultipleCustomElevationCommand.version = "$Revision: 14481 $";
    }
}
