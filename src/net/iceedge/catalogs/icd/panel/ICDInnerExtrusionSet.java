// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import java.util.Iterator;
import com.iceedge.icebox.icecore.aspects.IceNodeAspect;
import net.dirtt.utilities.MathUtilities;
import net.iceedge.icecore.basemodule.interfaces.panels.FrameInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.InnerExtrusionSetInterface;
import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.catalogs.icd.ICDUtilities;
import net.dirtt.icelib.report.Report;
import javax.vecmath.Point3f;
import net.iceedge.catalogs.icd.ICDSegment;
import net.iceedge.catalogs.icd.intersection.ICDIntersection;
import net.iceedge.icecore.basemodule.interfaces.GeneralIntersectionInterface;
import net.iceedge.catalogs.icd.intersection.ICDPost;
import net.iceedge.catalogs.icd.ICDSubILine;
import java.util.BitSet;
import net.dirtt.icelib.main.IceDimensionUpdater;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import java.util.Vector;
import net.dirtt.icelib.main.Solution;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelInterface;
import net.dirtt.icelib.main.AdjustmentValue;
import net.dirtt.icelib.main.EntityObject;
import java.util.ArrayList;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.utilities.Pair;
import java.util.List;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import com.iceedge.icd.entities.extrusions.ICDExtrusionInterface;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicInnerExtrusionSet;

public class ICDInnerExtrusionSet extends BasicInnerExtrusionSet implements ICDHorizontalBreakableExtrusion, ICDExtrusionInterface, ICDManufacturingReportable
{
    private List<Pair<Float, Integer>> breakLocations;
    
    public ICDInnerExtrusionSet(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.breakLocations = new ArrayList<Pair<Float, Integer>>();
        this.setFixedLocation(false);
        this.setupNamedPoints();
    }
    
    public Object clone() {
        return this.buildClone(new ICDInnerExtrusionSet(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDInnerExtrusionSet buildClone(final ICDInnerExtrusionSet set) {
        super.buildClone((BasicInnerExtrusionSet)set);
        return set;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDInnerExtrusionSet(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDInnerExtrusionSet buildFrameClone(final ICDInnerExtrusionSet set, final EntityObject entityObject) {
        super.buildFrameClone((BasicInnerExtrusionSet)set, entityObject);
        return set;
    }
    
    public void breakHorizontalExtrusion(final float n, final boolean b) {
        final ICDInternalExtrusion icdInternalExtrusion = (ICDInternalExtrusion)this.getInternalExtrusion(0);
        if (icdInternalExtrusion != null) {
            icdInternalExtrusion.breakHorizontalExtrusion(n, b);
        }
        final ICDInternalExtrusion icdInternalExtrusion2 = (ICDInternalExtrusion)this.getInternalExtrusion(1);
        if (icdInternalExtrusion2 != null) {
            icdInternalExtrusion2.breakHorizontalExtrusion(n, b);
        }
    }
    
    public void breakVerticalExtrusion(final float n, final boolean b) {
        final ICDInternalExtrusion icdInternalExtrusion = (ICDInternalExtrusion)this.getInternalExtrusion(0);
        if (icdInternalExtrusion != null) {
            icdInternalExtrusion.breakVerticalExtrusion(n, b);
        }
        final ICDInternalExtrusion icdInternalExtrusion2 = (ICDInternalExtrusion)this.getInternalExtrusion(1);
        if (icdInternalExtrusion2 != null) {
            icdInternalExtrusion2.breakVerticalExtrusion(n, b);
        }
    }
    
    public AdjustmentValue getTopExtrusionAdjValue(final String s) {
        return new AdjustmentValue((OptionObject)null, -6.35f);
    }
    
    public AdjustmentValue getBottomExtrusionAdjValue(final String s) {
        return new AdjustmentValue((OptionObject)null, -31.75f);
    }
    
    protected boolean isOnlyAllowInchIncreament() {
        return true;
    }
    
    public void moveableMove() {
        final PanelInterface parentPanel = this.getParentPanel();
        if (this.isVertical() && parentPanel != null && !parentPanel.isCorePanel()) {
            super.moveableMove();
            final Solution solution = this.getSolution();
            if (solution != null) {
                solution.solve();
            }
        }
    }
    
    public Vector<Float> initMoveableLocation() {
        final Vector<Float> vector = new Vector<Float>();
        final PanelInterface parentPanel = this.getParentPanel();
        if (!this.isVertical() || (parentPanel != null && parentPanel.isCorePanel())) {
            return vector;
        }
        return (Vector<Float>)super.initMoveableLocation();
    }
    
    protected boolean isMoveVerticalParametrically() {
        return true;
    }
    
    public void draw2D(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        super.draw2D(n, ice2DContainer, solutionSetting);
        this.drawChildren2D(n, ice2DContainer, solutionSetting);
    }
    
    public void populateDimensionForElevation(final TransformableEntity transformableEntity, final IceDimensionUpdater iceDimensionUpdater, final int n, final BitSet set, final float n2, final float n3) {
        final ICDSubILine icdSubILine = (ICDSubILine)this.getParent((Class)ICDSubILine.class);
        if (icdSubILine == null) {
            return;
        }
        boolean b = false;
        final float z = this.getBasePointWorldSpace().z;
        float n4 = ICDPost.BIG_NEGATIVE;
        final Vector intersections = icdSubILine.getIntersections();
        for (int i = 0; i < intersections.size(); ++i) {
            final GeneralIntersectionInterface generalIntersectionInterface = intersections.get(i);
            if (generalIntersectionInterface instanceof ICDIntersection) {
                n4 = ((ICDIntersection)generalIntersectionInterface).getBoltOnJointHeight(z);
                if (n4 > ICDPost.BIG_NEGATIVE) {
                    b = true;
                    break;
                }
            }
        }
        if (!b) {
            return;
        }
        final ICDSegment icdSegment = (ICDSegment)this.getParent((Class)ICDSegment.class);
        if (icdSegment != null) {
            icdSegment.isFlipped();
            if (!this.isVertical()) {
                final float n5 = n4;
                iceDimensionUpdater.addDimension((Object)this, (TransformableEntity)icdSegment, new Point3f(), new Point3f(n5, 0.0f, 0.0f), new Point3f(0.0f, 0.0f, 0.0f), n5);
            }
        }
    }
    
    public boolean shouldAddChildrenToReport(final Report report) {
        return false;
    }
    
    public boolean useTransformablesDraw3D() {
        return true;
    }
    
    public void setBreakAttributes(final int n, final boolean b) {
        this.applyChangesForAttribute("Break_Tile_SideA", b + "");
        this.applyChangesForAttribute("Break_Tile_SideB", b + "");
    }
    
    protected boolean needToSetOppSideBreakAttributes() {
        return false;
    }
    
    public void solve() {
        ICDUtilities.validateBeamBreakLocationForExtrusion(this);
        super.solve();
    }
    
    public List<Pair<Float, Integer>> getBreakLocationsForBeam() {
        return this.breakLocations;
    }
    
    public void SetBreakLocationForBeam(final List<Pair<Float, Integer>> breakLocations) {
        this.breakLocations = breakLocations;
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
    
    protected float getMaxBoundary() {
        final Vector<InnerExtrusionSetInterface> innerNeighbourExtrusions = this.getInnerNeighbourExtrusions(true);
        final FrameInterface physicalFrame = this.getParentPanel().getPhysicalFrame();
        float n;
        if (this.isVertical()) {
            n = physicalFrame.getEndExtrusion().getStartPoint().getPoint().x;
            for (int i = 0; i < innerNeighbourExtrusions.size(); ++i) {
                final float x = innerNeighbourExtrusions.elementAt(i).getStartPoint().getPoint().x;
                if (x > this.getStartPoint().getPoint().x && x < n) {
                    n = x;
                }
            }
        }
        else {
            n = physicalFrame.getTopExtrusion().getStartPoint().getPoint().y;
            for (int j = 0; j < innerNeighbourExtrusions.size(); ++j) {
                final float y = innerNeighbourExtrusions.elementAt(j).getStartPoint().getPoint().y;
                if (y > this.getStartPoint().getPoint().y && y < n) {
                    n = y;
                }
            }
        }
        return n;
    }
    
    protected float getMinBoundary() {
        final Vector<InnerExtrusionSetInterface> innerNeighbourExtrusions = this.getInnerNeighbourExtrusions(true);
        final FrameInterface physicalFrame = this.getParentPanel().getPhysicalFrame();
        float n;
        if (this.isVertical()) {
            n = physicalFrame.getStartExtrusion().getStartPoint().getPoint().x;
            for (int i = 0; i < innerNeighbourExtrusions.size(); ++i) {
                final float x = innerNeighbourExtrusions.elementAt(i).getStartPoint().getPoint().x;
                if (x < this.getStartPoint().getPoint().x && x > n) {
                    n = x;
                }
            }
        }
        else {
            n = physicalFrame.getBottomExtrusion().getStartPoint().getPoint().y;
            for (int j = 0; j < innerNeighbourExtrusions.size(); ++j) {
                final float y = innerNeighbourExtrusions.elementAt(j).getStartPoint().getPoint().y;
                if (y < this.getStartPoint().getPoint().y && y > n) {
                    n = y;
                }
            }
        }
        return n;
    }
    
    private Vector<InnerExtrusionSetInterface> getInnerNeighbourExtrusions(final boolean b) {
        final Vector<InnerExtrusionSetInterface> vector = new Vector<InnerExtrusionSetInterface>();
        final Point3f calculateMidpoint = MathUtilities.calculateMidpoint(this.getStartPoint().getPoint(), this.getEndPoint().getPoint());
        final EntityObject entityObject = (EntityObject)((IceNodeAspect)this.getAspect((Class)IceNodeAspect.class)).getParent();
        if (entityObject != null) {
            for (int i = 0; i < entityObject.getChildCount(); ++i) {
                final EntityObject childEntity = entityObject.getChildEntityAt(i);
                if (childEntity instanceof InnerExtrusionSetInterface && childEntity != this) {
                    final InnerExtrusionSetInterface obj = (InnerExtrusionSetInterface)childEntity;
                    if (!(obj.isVertical() ^ this.isVertical()) && obj.projectionInsideExtrusion(calculateMidpoint) && (!b || ((BasicInnerExtrusionSet)obj).isVisible())) {
                        vector.addElement(obj);
                    }
                }
            }
        }
        return vector;
    }
    
    public void addToCutSolution(final Vector vector, final Vector vector2, final Solution solution, final boolean b) {
        final Iterator children = this.getChildren();
        while (children.hasNext()) {
            final EntityObject entityObject = children.next();
            if (vector.contains(entityObject)) {
                vector.remove(entityObject);
            }
        }
    }
    
    public Point3f pseudoMove(final Point3f point3f, final Point3f point3f2) {
        final float currentLocation = this.getCurrentLocation();
        final Point3f pseudoMove = super.pseudoMove(point3f, point3f2);
        if (this.isVertical()) {
            this.moveableMove(this.getCurrentLocation() - currentLocation, 0.0f, true);
        }
        else {
            this.moveableMove(0.0f, this.getCurrentLocation() - currentLocation, true);
        }
        return pseudoMove;
    }
}
