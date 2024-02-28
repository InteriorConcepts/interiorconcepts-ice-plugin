package net.iceedge.catalogs.icd;

import java.util.Iterator;
import net.iceedge.catalogs.icd.worksurfaces.ICDBasicWorksurface;
import net.iceedge.icecore.basemodule.finalclasses.GeneralSnapSet;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.icecore.basemodule.interfaces.SolutionPostSolveListener;
import net.iceedge.icecore.basemodule.interfaces.SolutionPreSolveListener;
import java.util.Collection;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icelib.main.TypeableEntity;

public class ICDWorksurfaceSolver extends TypeableEntity implements ICDManufacturingReportable
{
    private WorksurfaceSnapper worksurfaceSnapper;
    
    public ICDWorksurfaceSolver(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.worksurfaceSnapper = new WorksurfaceSnapper();
    }
    
    public EntityObject buildClone(final ICDWorksurfaceSolver icdWorksurfaceSolver) {
        super.buildClone((TypeableEntity)icdWorksurfaceSolver);
        return (EntityObject)icdWorksurfaceSolver;
    }
    
    public EntityObject buildClone2(final ICDWorksurfaceSolver icdWorksurfaceSolver) {
        super.buildClone2((TypeableEntity)icdWorksurfaceSolver);
        return (EntityObject)icdWorksurfaceSolver;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDWorksurfaceSolver(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDWorksurfaceSolver(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDWorksurfaceSolver icdWorksurfaceSolver, final EntityObject entityObject) {
        super.buildFrameClone((TypeableEntity)icdWorksurfaceSolver, entityObject);
        return (EntityObject)icdWorksurfaceSolver;
    }
    
    public void collectSolutionSolveListeners(final Collection<SolutionPreSolveListener> collection, final Collection<SolutionPostSolveListener> collection2) {
        collection.add((SolutionPreSolveListener)this.worksurfaceSnapper);
        super.collectSolutionSolveListeners((Collection)collection, (Collection)collection2);
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
    }
    
    public String getManufacturingReportMaterialOptID() {
        return "Option0";
    }
    
    public void addManufacturingInfoToTreeMap(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addManufacturingInfoToTreeMap(treeMap, (ManufacturingReportable)this);
    }
    
    public String getDescriptionForManufacturingReport() {
        return this.getDescription();
    }
    
    class WorksurfaceSnapper implements SolutionPreSolveListener
    {
        public void preSolve(final Solution solution) {
            if (solution == null) {
                return;
            }
            for (final GeneralSnapSet set : solution.getChildrenByClass(GeneralSnapSet.class, false, true)) {
                if (!(set.isModified())) {
                    continue;
                }
                final Iterator<ICDBasicWorksurface> iterator2 = set.getChildrenByClass(ICDBasicWorksurface.class, false, true).iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().handleSnap();
                }
            }
            final Iterator<ICDBasicWorksurface> iterator3 = solution.getChildrenByClass(ICDBasicWorksurface.class, false, true).iterator();
            while (iterator3.hasNext()) {
                iterator3.next().handleSnap();
            }
        }
    }
}
