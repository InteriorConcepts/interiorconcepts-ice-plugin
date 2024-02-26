// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import java.awt.geom.Line2D;
import net.dirtt.icelib.main.LightWeightTypeObject;
import net.dirtt.utilities.EnumerationIterator;
import net.iceedge.icecore.basemodule.baseclasses.GeneralSnapSetFilter;
import net.iceedge.icecore.basemodule.finalclasses.GeneralSnapSet;
import java.util.Vector;
import java.util.Iterator;
import java.util.Collection;
import net.iceedge.icecore.basemodule.interfaces.panels.BottomExtrusionInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.FrameInterface;
import javax.vecmath.Point3f;
import net.dirtt.utilities.MathUtilities;
import net.dirtt.utilities.TypeFilter;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelSegmentInterfaceFilter;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelSegmentInterface;
import net.dirtt.icelib.main.EntityObject;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDSubFrameSideContainer extends ICDPanel
{
    private static final long serialVersionUID = 8759016675242751901L;
    private float basePointOffsetX;
    private float xDimPullBack;
    
    public ICDSubFrameSideContainer(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.setupNamedPoints();
    }
    
    public ICDSubFrameSideContainer buildClone(final ICDSubFrameSideContainer icdSubFrameSideContainer) {
        super.buildClone((TransformableTriggerUser)icdSubFrameSideContainer);
        return icdSubFrameSideContainer;
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDSubFrameSideContainer(this.getId(), this.currentType, this.currentOption));
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDSubFrameSideContainer(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public EntityObject buildFrameClone(final ICDSubFrameSideContainer icdSubFrameSideContainer, final EntityObject entityObject) {
        return super.buildFrameClone((TransformableTriggerUser)icdSubFrameSideContainer, entityObject);
    }
    
    @Override
    public void solve() {
        super.solve();
        if (this.createAllBreaks()) {
            super.solve();
        }
    }
    
    public boolean isCorePanel() {
        return true;
    }
    
    public PanelSegmentInterface getParentPanelSegment() {
        return (PanelSegmentInterface)this.getParent((TypeFilter)new PanelSegmentInterfaceFilter());
    }
    
    public void calculateDimensions() {
        float yDimension = 0.0f;
        float panelWidth = 0.0f;
        float panelDepthForPanels = 0.0f;
        final PanelSegmentInterface parentPanelSegment = this.getParentPanelSegment();
        final ICDPanel parentPanel = this.getParentPanel();
        if (parentPanelSegment != null && parentPanel != null) {
            if (parentPanel.isSuspendedChase()) {
                yDimension = parentPanel.getSuspendedOffset();
            }
            else if (parentPanel.isUnderChase()) {
                yDimension = parentPanel.getHeight();
            }
            else {
                yDimension = parentPanel.getSplitLocation() + 1.0f;
            }
            panelWidth = parentPanelSegment.getPanelWidth();
            panelDepthForPanels = parentPanelSegment.getPanelDepthForPanels();
        }
        if (panelWidth - this.xDimPullBack > 3.0f) {
            panelWidth -= this.xDimPullBack;
        }
        this.setXDimension(panelWidth);
        this.setYDimension(yDimension);
        this.setZDimension(panelDepthForPanels);
    }
    
    public boolean createAllBreaks() {
        boolean b = false;
        this.basePointOffsetX = 0.0f;
        this.xDimPullBack = 0.0f;
        final ICDPanel panel = this.getPanel(true);
        if (panel != null) {
            this.createPanelExtrusionBreaks(panel, true);
            b = true;
        }
        final ICDPanel panel2 = this.getPanel(false);
        if (panel2 != null) {
            this.createPanelExtrusionBreaks(panel2, false);
            b = true;
        }
        this.calculateDimensions();
        return b;
    }
    
    private void removeAllBreaks(final boolean b, final boolean b2) {
        final ICDPanel panel = this.getPanel(b);
        if (panel != null) {
            this.removePanelExtrusionBreaks(panel, true, b2);
            this.removePanelExtrusionBreaks(panel, false, b2);
        }
    }
    
    private void removeAllBreaks(final boolean b) {
        this.removeAllBreaks(true, b);
        this.removeAllBreaks(false, b);
    }
    
    public void removeAllBreaks() {
        this.removeAllBreaks(false);
    }
    
    public boolean requiresBreak(final ICDPanel icdPanel) {
        return this.equals(this.getPanel(false)) && !icdPanel.hasChaseOnPointSide(this.convertPointToLocal(this.getGeometricCenterPointWorld()));
    }
    
    private void createPanelExtrusionBreaks(final ICDPanel icdPanel, final boolean b) {
        if (icdPanel != null) {
            final int canCreateInnerCorner = icdPanel.canCreateInnerCorner(this);
            final ICDPanel parentPanel = this.getParentPanel();
            if (canCreateInnerCorner != -1) {
                this.calculateInnerCorner(icdPanel, b, canCreateInnerCorner);
                if (parentPanel != null) {
                    this.breakTileAndSplitExtrusion(icdPanel, b, parentPanel);
                    if (!icdPanel.requiresBreak(true) && !icdPanel.requiresBreak(false)) {
                        this.removeAllBreaks(b, false);
                    }
                }
            }
            else if (parentPanel != null) {
                this.breakTileAndSplitExtrusion(icdPanel, b, parentPanel);
            }
        }
    }
    
    private void breakTileAndSplitExtrusion(final ICDPanel icdPanel, final boolean b, final ICDPanel icdPanel2) {
        final Point3f subPanelPoint = icdPanel2.getSubPanelPoint(this.getSide(), b);
        final Point3f convertSpaces = MathUtilities.convertSpaces(subPanelPoint, (EntityObject)icdPanel2, (EntityObject)icdPanel);
        icdPanel.breakHorizontalExtrusion((float)Math.round(convertSpaces.x - 1.0f), true, MathUtilities.isSameFloat(icdPanel2.getWorksurfaceHeight(), icdPanel.getHeight() + 2.0f, 0.5f), icdPanel.shouldBreakChaseVertically() || !icdPanel2.isSuspendedChase());
        icdPanel.breakTileByChaseVertical((float)Math.round(convertSpaces.x), true);
        this.splitExtrusionForSuspendedChase(icdPanel, b, subPanelPoint);
    }
    
    private void removeBottomExtrusionBreakInAdjacentPanel(final ICDPanel icdPanel) {
        final FrameInterface physicalFrame = icdPanel.getPhysicalFrame();
        if (physicalFrame != null) {
            final BottomExtrusionInterface bottomExtrusion = physicalFrame.getBottomExtrusion();
            if (bottomExtrusion instanceof ICDBottomExtrusion) {
                ((ICDBottomExtrusion)bottomExtrusion).breakHorizontalExtrusion(0.0f, true);
            }
        }
    }
    
    private void calculateInnerCorner(final ICDPanel icdPanel, final boolean b, final int n) {
        final float chasePullBack = icdPanel.getChasePullBack(n);
        final int side = this.getSide();
        if (b) {
            if (side == 0) {
                this.basePointOffsetX = chasePullBack;
            }
            else {
                this.basePointOffsetX = -chasePullBack;
            }
        }
        this.xDimPullBack += chasePullBack;
    }
    
    public void removePanelExtrusionBreaks(final ICDPanel icdPanel, final boolean b) {
        this.removePanelExtrusionBreaks(icdPanel, b, false);
    }
    
    private void removePanelExtrusionBreaks(final ICDPanel icdPanel, final boolean b, final boolean b2) {
        if (icdPanel != null) {
            Point3f point3f = new Point3f(0.0f, 0.0f, 0.0f);
            if (!b) {
                point3f = new Point3f(this.getXDimension(), 0.0f, 0.0f);
            }
            final Point3f convertSpaces = MathUtilities.convertSpaces(point3f, (EntityObject)this, (EntityObject)icdPanel);
            icdPanel.breakHorizontalExtrusion(0.0f, true, true, true);
            if ((this.getParentPanel() != null && this.getParentPanel().isSuspendedChase() && !this.shouldBreakChaseVertically()) || b2) {
                icdPanel.removeBreakTileByChaseVertical((float)Math.round(convertSpaces.x), true);
            }
        }
    }
    
    public ICDPanel getPanel(final boolean b) {
        final Collection<ICDPanel> allPanels = this.getAllPanels();
        if (allPanels != null && allPanels.size() > 0) {
            final ICDPanel parentPanel = this.getParentPanel();
            if (parentPanel != null) {
                final Point3f subPanelPoint = parentPanel.getSubPanelPoint(this.getSide(), b);
                for (final ICDPanel icdPanel : allPanels) {
                    if (icdPanel != null && !(icdPanel instanceof ICDSubFrameSideContainer)) {
                        final Point3f convertSpaces = MathUtilities.convertSpaces(subPanelPoint, (EntityObject)parentPanel, (EntityObject)icdPanel);
                        if (Math.abs(convertSpaces.z) < 1.0f && convertSpaces.x > 0.0f && convertSpaces.x < icdPanel.getXDimension()) {
                            return icdPanel;
                        }
                        continue;
                    }
                }
            }
        }
        return null;
    }
    
    public Collection<ICDPanel> getAllPanels() {
        final Vector<ICDPanel> vector = new Vector<ICDPanel>();
        final GeneralSnapSet set = (GeneralSnapSet)this.getParent((TypeFilter)new GeneralSnapSetFilter());
        if (set != null) {
            final EnumerationIterator enumerationIterator = new EnumerationIterator(set.breadthFirstEnumeration());
            while (((Iterator)enumerationIterator).hasNext()) {
                final ICDPanel next = ((Iterator<ICDPanel>)enumerationIterator).next();
                if (next instanceof ICDPanel) {
                    vector.add(next);
                }
            }
        }
        return vector;
    }
    
    public void destroy() {
        final ICDPanel panel = this.getPanel(true);
        final ICDPanel panel2 = this.getPanel(false);
        this.removeAllBreaks(true);
        super.destroy();
        if (panel != null) {
            this.removePanelExtrusionBreaks(panel, true, true);
            this.removePanelExtrusionBreaks(panel, false, true);
        }
        if (panel2 != null) {
            this.removePanelExtrusionBreaks(panel2, true, true);
            this.removePanelExtrusionBreaks(panel2, false, true);
        }
    }
    
    @Override
    protected boolean shouldGenerateSku() {
        return false;
    }
    
    public int getSide() {
        final LightWeightTypeObject lwTypeCreated = this.getLwTypeCreatedFrom();
        if (lwTypeCreated != null && "ICD_Chase_Side_A_Type".equalsIgnoreCase(lwTypeCreated.getId())) {
            return 0;
        }
        return 1;
    }
    
    private void splitExtrusionForSuspendedChase(final ICDPanel icdPanel, final boolean b, final Point3f point3f) {
        if (icdPanel != null) {
            final ICDPanel parentPanel = this.getParentPanel();
            if (parentPanel != null) {
                final int side = this.getSide();
                if (parentPanel.isSuspendedChase()) {
                    final float n = (float)Math.round(MathUtilities.convertSpaces(point3f, (EntityObject)parentPanel, (EntityObject)icdPanel).x);
                    final float n2 = parentPanel.getSplitLocation() - (parentPanel.getSuspendedOffset() - 1.0f);
                    final boolean shouldBreakChaseVertically = icdPanel.shouldBreakChaseVertically();
                    if (side == 0) {
                        point3f.z -= 2.0f;
                        this.validateTileByChaseHorizontal(icdPanel, b, point3f, parentPanel, n2, shouldBreakChaseVertically);
                    }
                    else {
                        point3f.z += 2.0f;
                        this.validateTileByChaseHorizontal(icdPanel, b, point3f, parentPanel, n2, shouldBreakChaseVertically);
                    }
                    if (shouldBreakChaseVertically) {
                        icdPanel.breakVerticalExtrusion(n, parentPanel.getSplitLocation() - parentPanel.getSuspendedOffset(), true);
                    }
                    else {
                        this.removeBottomExtrusionBreakInAdjacentPanel(icdPanel);
                    }
                }
            }
        }
    }
    
    private void validateTileByChaseHorizontal(final ICDPanel icdPanel, final boolean b, final Point3f point3f, final ICDPanel icdPanel2, final float n, final boolean b2) {
        final Point3f convertSpaces = MathUtilities.convertSpaces(point3f, (EntityObject)icdPanel2, (EntityObject)icdPanel);
        if (b2) {
            icdPanel.breakTileByChaseHorizontal(convertSpaces.x, n, b);
        }
        else {
            icdPanel.removeBreakTileByChaseHorizontal(convertSpaces.x, n);
        }
    }
    
    public Line2D.Float getLine() {
        final ICDPanel parentPanel = this.getParentPanel();
        if (parentPanel != null) {
            return parentPanel.getSubPanelLine(this.getSide());
        }
        return null;
    }
    
    public ICDPanel getParentPanel() {
        return (ICDPanel)this.getParent((TypeFilter)new ICDPanelFilter());
    }
    
    public void calculateLocalSpace() {
        super.calculateLocalSpace();
        final Point3f basePoint3f = this.getBasePoint3f();
        basePoint3f.x += this.basePointOffsetX;
        this.setBasePoint(basePoint3f);
    }
    
    public Point3f getIntersectionPointWith(final ICDSubFrameSideContainer icdSubFrameSideContainer) {
        final float normalizeRotation = MathUtilities.normalizeRotation(this.getRotationWorldSpace() - icdSubFrameSideContainer.getRotationWorldSpace());
        if (MathUtilities.isSameFloat(normalizeRotation, 1.5707964f, 0.001f) || MathUtilities.isSameFloat(normalizeRotation, 1.5707964f, 0.001f)) {
            return MathUtilities.getIntersectionPoint(this.convertPointToWorldSpace(new Point3f()), this.convertPointToWorldSpace(new Point3f(this.getXDimension(), 0.0f, 0.0f)), icdSubFrameSideContainer.convertPointToWorldSpace(new Point3f()), icdSubFrameSideContainer.convertPointToWorldSpace(new Point3f(icdSubFrameSideContainer.getXDimension(), 0.0f, 0.0f)), false);
        }
        return null;
    }
    
    @Override
    protected boolean validateSplit(final float n) {
        return false;
    }
    
    @Override
    protected void validateChaseVerticalSplit() {
    }
    
    @Override
    protected void adjustChildPanelSizeForSuspendedFramePositions() {
    }
    
    public void removeOtherPanelChaseHorizontalSplit() {
        final ICDPanel panel = this.getPanel(true);
        if (panel != null) {
            this.removeOtherPanelChaseHorizontalSplit(panel, true);
        }
        final ICDPanel panel2 = this.getPanel(false);
        if (panel2 != null) {
            this.removeOtherPanelChaseHorizontalSplit(panel2, false);
        }
    }
    
    private void removeOtherPanelChaseHorizontalSplit(final ICDPanel icdPanel, final boolean b) {
        if (icdPanel != null) {
            final ICDPanel parentPanel = this.getParentPanel();
            if (parentPanel != null) {
                final Point3f subPanelPoint = parentPanel.getSubPanelPoint(this.getSide(), b);
                final float n = (float)Math.round(MathUtilities.convertSpaces(subPanelPoint, (EntityObject)parentPanel, (EntityObject)icdPanel).x);
                final int side = this.getSide();
                final float n2 = parentPanel.getSplitLocation() - (parentPanel.getSuspendedOffset() - 1.0f);
                if (side == 0) {
                    subPanelPoint.z -= 2.0f;
                    icdPanel.removeBreakTileByChaseHorizontal(MathUtilities.convertSpaces(subPanelPoint, (EntityObject)parentPanel, (EntityObject)icdPanel).x, n2);
                }
                else {
                    subPanelPoint.z += 2.0f;
                    icdPanel.removeBreakTileByChaseHorizontal(MathUtilities.convertSpaces(subPanelPoint, (EntityObject)parentPanel, (EntityObject)icdPanel).x, n2);
                }
                icdPanel.breakVerticalExtrusion(n, 0.0f, true);
            }
        }
    }
    
    public void moveOtherVerticalExtrusion(final float n) {
        this.moveOtherVerticalExtrusion(n, true);
        this.moveOtherVerticalExtrusion(n, false);
    }
    
    private void moveOtherVerticalExtrusion(final float n, final boolean b) {
        final ICDPanel panel = this.getPanel(b);
        if (panel != null) {
            final ICDPanel parentPanel = this.getParentPanel();
            if (parentPanel != null) {
                final Point3f subPanelPoint = parentPanel.getSubPanelPoint(this.getSide(), b);
                final Point3f convertSpaces = MathUtilities.convertSpaces(subPanelPoint, (EntityObject)parentPanel, (EntityObject)panel);
                final Point3f point3f = subPanelPoint;
                point3f.z += n;
                panel.moveVerticalExtrusion((float)Math.round(convertSpaces.x), MathUtilities.convertSpaces(subPanelPoint, (EntityObject)parentPanel, (EntityObject)panel).x - convertSpaces.x);
            }
        }
    }
    
    public void setLwTypeCreatedFrom(final LightWeightTypeObject lwTypeCreatedFrom) {
        if (lwTypeCreatedFrom != this.getLwTypeCreatedFrom() && this.getLwTypeCreatedFrom() != null) {
            this.removeAllBreaks(true);
        }
        super.setLwTypeCreatedFrom(lwTypeCreatedFrom);
    }
    
    public void moveOtherHorizontalExtrusion(final float n, final float n2) {
        this.moveOtherHorizontalExtrusion(n, n2, true);
        this.moveOtherHorizontalExtrusion(n, n2, false);
    }
    
    private void moveOtherHorizontalExtrusion(final float n, final float n2, final boolean b) {
        final ICDPanel panel = this.getPanel(b);
        if (panel != null) {
            final ICDPanel parentPanel = this.getParentPanel();
            if (parentPanel != null) {
                final int side = this.getSide();
                final Point3f subPanelPoint = parentPanel.getSubPanelPoint(side, b);
                if (side == 0) {
                    subPanelPoint.z -= 2.0f;
                }
                else {
                    subPanelPoint.z += 2.0f;
                }
                panel.moveHorizontalExtrusionBySuspendedChase(MathUtilities.convertSpaces(subPanelPoint, (EntityObject)parentPanel, (EntityObject)panel).x, n, n2);
            }
        }
    }
}
