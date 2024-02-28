package net.iceedge.catalogs.icd.panel;

import net.iceedge.icecore.basemodule.interfaces.panels.PanelInterface;
import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.icecore.basemodule.interfaces.SubILineInterface;
import java.util.Vector;
import java.util.Iterator;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicPanel;
import net.iceedge.catalogs.icd.ICDSegment;
import net.iceedge.icecore.basemodule.interfaces.IntersectionArmInterface;
import net.iceedge.icecore.basemodule.baseclasses.BasicIntersection;
import net.iceedge.icecore.basemodule.interfaces.GeneralIntersectionInterface;
import net.iceedge.catalogs.icd.ICDSubILine;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import com.iceedge.icd.entities.extrusions.ICDExtrusionInterface;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicRaceway;

public class ICDRaceway extends BasicRaceway implements ICDExtrusionInterface, ICDManufacturingReportable
{
    public static final String CABLE_POSITION_NAMED_POINT = "CABLE_POS";
    public static final String CABLE_ROTATION_NAMED_POINT = "CABLE_ROT";
    
    public ICDRaceway(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDRaceway(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDRaceway buildClone(final ICDRaceway icdRaceway) {
        super.buildClone((BasicRaceway)icdRaceway);
        return icdRaceway;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDRaceway(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDRaceway buildFrameClone(final ICDRaceway icdRaceway, final EntityObject entityObject) {
        super.buildFrameClone((BasicRaceway)icdRaceway, entityObject);
        return icdRaceway;
    }
    
    public void setupNamedPoints() {
        super.setupNamedPoints();
        this.addNamedPoint("CABLE_POS", new Point3f());
        this.addNamedRotation("CABLE_ROT", new Vector3f());
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        final BasicPanel parentPanel = this.getParentPanel();
        if (parentPanel != null && parentPanel instanceof ICDPanel) {
            final float splitLocation = ((ICDPanel)parentPanel).getSplitLocation();
            this.getNamedPointLocal("Area_A").set(0.0f, splitLocation, this.getZDimension());
            this.getNamedPointLocal("Area_B").set(0.0f, splitLocation, 0.0f);
        }
        float zDimension = this.getZDimension();
        float n = 3.1415927f;
        if (((PanelInterface)parentPanel).getSubFrame(0) == null && ((PanelInterface)parentPanel).getSubFrame(1) != null) {
            zDimension = 0.0f;
            n = 0.0f;
        }
        ICDSubILine icdSubILine = null;
        final Segment parentSegment = this.getParentSegment();
        if (parentSegment != null) {
            icdSubILine = (ICDSubILine)parentSegment.getMyParentSubILine();
        }
        boolean b = false;
        float chaseOffset = 6.0f;
        if (icdSubILine != null) {
            for (final GeneralIntersectionInterface generalIntersectionInterface : icdSubILine.getIntersections()) {
                if (generalIntersectionInterface != null && "ICD_TwoWay90Intersection".equals(((EntityObject)generalIntersectionInterface).getCurrentOption().getId())) {
                    final Vector<IntersectionArmInterface> armsOrderedByIndex = ((BasicIntersection)generalIntersectionInterface).getArmsOrderedByIndex();
                    if (armsOrderedByIndex.size() != 2 || armsOrderedByIndex.get(1).getSegment() != parentSegment) {
                        continue;
                    }
                    b = true;
                    final ICDSegment icdSegment = (ICDSegment)armsOrderedByIndex.get(0).getSegment();
                    final SubILineInterface subWall = armsOrderedByIndex.get(0).getSubWall();
                    if (icdSegment != null && subWall != null) {
                        final ICDPanel icdPanel = icdSegment.getICDPanel();
                        if (icdPanel != null) {
                            chaseOffset = icdPanel.getChaseOffset((subWall.getStartIntersection() != generalIntersectionInterface) ? 1 : 0);
                        }
                        break;
                    }
                    break;
                }
            }
        }
        if (b) {
            if (zDimension == 0.0f) {
                zDimension = chaseOffset;
            }
            else {
                zDimension -= chaseOffset;
            }
        }
        this.getNamedPointLocal("CABLE_POS").set(0.0f, 0.0f, zDimension);
        this.getNamedRotationLocal("CABLE_ROT").set(0.0f, n, 0.0f);
    }
    
    public void draw2DElevation(final int n, final Ice2DContainer ice2DContainer, final boolean b, final SolutionSetting solutionSetting) {
    }
    
    public boolean stripTheChildren() {
        return true;
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addDimensionsToManufacturingTreeMap(treeMap, (TransformableEntity)this);
    }
    
    public String getManufacturingReportMaterialOptID() {
        return "Option0";
    }
    
    public void addManufacturingInfoToTreeMap(final TreeMap<String, String> treeMap) {
        this.getManufacturingInfo(treeMap);
    }
    
    public String getDescriptionForManufacturingReport() {
        return this.getDescription() + ",Length:" + Math.round(this.getLength()) + " " + this.getReportDescription();
    }
    
    public void getManufacturingInfo(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addManufacturingInfoToTreeMapForExtrusion(treeMap, (ManufacturingReportable)this, this.getLength());
    }
    
    public String getReportDescription() {
        return ICDManufacturingUtils.getReportDescriptionForExtrusion((EntityObject)this);
    }
}
