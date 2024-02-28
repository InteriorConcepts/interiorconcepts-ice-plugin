package net.iceedge.catalogs.icd.electrical;

import net.dirtt.icelib.main.Solution;
import net.iceedge.catalogs.icd.panel.ICDPanel;
import java.util.List;
import java.util.Iterator;
import net.iceedge.catalogs.icd.ICDSegment;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import net.iceedge.catalogs.icd.intersection.ICDIntersection;
import net.iceedge.icecore.basemodule.baseclasses.electrical.BasicElectricalCable;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDEndfeedPowerSource extends ICDElectricalReceptacle
{
    public ICDEndfeedPowerSource(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.setupNamedPoints();
    }
    
    public TransformableEntity buildClone(final ICDEndfeedPowerSource icdEndfeedPowerSource) {
        super.buildClone(icdEndfeedPowerSource);
        return (TransformableEntity)icdEndfeedPowerSource;
    }
    
    public TransformableEntity buildClone2(final ICDEndfeedPowerSource icdEndfeedPowerSource) {
        super.buildClone2(icdEndfeedPowerSource);
        return (TransformableEntity)icdEndfeedPowerSource;
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDEndfeedPowerSource(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDEndfeedPowerSource(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDEndfeedPowerSource icdEndfeedPowerSource, final EntityObject entityObject) {
        super.buildFrameClone(icdEndfeedPowerSource, entityObject);
        return (EntityObject)icdEndfeedPowerSource;
    }
    
    public BasicElectricalCable getElectricCable() {
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity != null && parentEntity instanceof ICDIntersection) {
            for (final Segment segment : ((ICDIntersection)parentEntity).getSegmentsFromArms()) {
                if (segment instanceof ICDSegment && segmentHasChase((ICDSegment)segment)) {
                    final List<BasicElectricalCable> childrenByClass = segment.getChildrenByClass(BasicElectricalCable.class, true, true);
                    if (!childrenByClass.isEmpty()) {
                        childrenByClass.get(0).setHasToBeHarness(true);
                        return childrenByClass.get(0);
                    }
                    continue;
                }
            }
        }
        return null;
    }
    
    private static boolean segmentHasChase(final ICDSegment icdSegment) {
        boolean hasChase = false;
        final ICDPanel icdPanel = icdSegment.getICDPanel();
        if (icdPanel != null) {
            hasChase = icdPanel.hasChase();
        }
        return hasChase;
    }
    
    @Override
    public String getRequiredCableSolver() {
        return "ICD_Electrical_Cable_Solver_Type";
    }
    
    public void setSelected(final boolean b, final Solution solution) {
        super.setSelected(b, solution);
        if (this.getPowerIntent() != null) {
            this.getPowerIntent().setSelected(b, solution);
        }
    }
    
    public String getDefaultLayerName() {
        return "Power Feeds";
    }
}
