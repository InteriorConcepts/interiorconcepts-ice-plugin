// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icecad.cadtree.ICadBlockNode;
import net.dirtt.icelib.main.ElevationEntity;
import net.iceedge.catalogs.icd.ICDUtilities;
import net.dirtt.icelib.report.compare.CompareNode;
import javax.vecmath.Point3f;
import java.awt.Color;
import net.dirtt.icebox.canvas2d.Ice2DCubeNode;
import net.dirtt.icebox.canvas2d.Ice2DNode;
import net.iceedge.industry.walls.frames.CoreFrameStructureInterfaceTypeFilter;
import net.dirtt.icelib.ui.CoreFrameStructureInterface;
import net.iceedge.industry.walls.AbstractFrameFilter;
import net.iceedge.industry.walls.AbstractFrame;
import javax.vecmath.Vector3f;
import javax.vecmath.Matrix4f;
import net.dirtt.utilities.TypeFilter;
import net.iceedge.industry.walls.FrameStructureHostInterfaceFilter;
import net.iceedge.industry.walls.FrameStructureHostInterface;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.main.BoundingCube;
import net.dirtt.icebox.canvas2d.Ice2DImageNode;
import java.awt.image.BufferedImage;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;

public class ICDValetHandle extends TransformableEntity implements ICDManufacturingReportable
{
    private static final long serialVersionUID = -7723878149237776803L;
    protected BufferedImage elevationImage;
    protected Ice2DImageNode imageNode;
    protected Ice2DImageNode sideBImageNode;
    protected BoundingCube elevationCube;
    
    public ICDValetHandle(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.elevationCube = new BoundingCube();
        this.setUpNamedPoints();
    }
    
    public ICDValetHandle buildClone(final ICDValetHandle icdValetHandle) {
        super.buildClone((TransformableEntity)icdValetHandle);
        return icdValetHandle;
    }
    
    public ICDValetHandle buildClone2(final ICDValetHandle icdValetHandle) {
        super.buildClone2((TransformableEntity)icdValetHandle);
        return icdValetHandle;
    }
    
    public Object clone() {
        return this.buildClone(new ICDValetHandle(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDValetHandle icdValetHandle, final EntityObject entityObject) {
        super.buildFrameClone((TransformableEntity)icdValetHandle, entityObject);
        return (EntityObject)icdValetHandle;
    }
    
    public void draw2DElevation(final int n, final Ice2DContainer ice2DContainer, final boolean b, final SolutionSetting solutionSetting) {
        super.draw2DElevation(n, ice2DContainer, b, solutionSetting);
        this.draw2DImageNode(ice2DContainer);
    }
    
    public void draw2DImageNode(final Ice2DContainer ice2DContainer) {
        this.elevationImage = this.getElevationImage(ice2DContainer);
        if (this.elevationImage != null) {
            final int side = this.getSide();
            final int side2 = ice2DContainer.getSide();
            if (side == 0 || side == -1) {
                switch (side2) {
                    case -1: {
                        this.draw2DElevationImageNode(ice2DContainer, this.elevationImage, this.getEntWorldSpaceMatrix(), 1.0f, 1.0f);
                        if (ice2DContainer.isShowFrameMode() && !this.getFrame().hasTwoSides()) {
                            if (((FrameStructureHostInterface)this.getParent((TypeFilter)new FrameStructureHostInterfaceFilter())).isFrameSolver()) {
                                this.drawOtherSide(ice2DContainer, 1.0f, 1.0f);
                            }
                            break;
                        }
                        break;
                    }
                    case 0: {
                        this.draw2DElevationImageNode(ice2DContainer, this.elevationImage, this.getEntWorldSpaceMatrix(), 1.0f, 1.0f);
                        break;
                    }
                    case 1: {
                        this.drawOtherSide(ice2DContainer, 1.0f, 1.0f);
                        break;
                    }
                    default: {
                        System.out.println("DoorHardware.draw2DImageNode(), Wrong rootDrawSide " + side2 + " for: " + this);
                        break;
                    }
                }
            }
            else if (side == 1) {
                final Matrix4f matrix4f = new Matrix4f();
                matrix4f.setIdentity();
                float n = 0.0f;
                if (ice2DContainer.getSide() == -1) {
                    final Matrix4f matrix4f2 = new Matrix4f();
                    matrix4f2.setIdentity();
                    matrix4f2.rotZ(3.1415927f);
                    matrix4f.mul(matrix4f2);
                    if (this.getFrame() != null) {
                        n = 2.0f * this.getFrame().getXDimension() + 12.0f;
                        if (this.getFrameSet() != null) {
                            n = 2.0f * this.getFrameSet().getXDimension() + 12.0f;
                        }
                    }
                }
                matrix4f.setTranslation(new Vector3f(n, 0.0f, 0.0f));
                matrix4f.mul(this.getEntWorldSpaceMatrix());
                switch (side2) {
                    case -1: {
                        this.draw2DElevationImageNode(ice2DContainer, this.elevationImage, matrix4f, 1.0f, 1.0f);
                        break;
                    }
                    case 0: {
                        this.drawOtherSide(ice2DContainer, 1.0f, 1.0f);
                        break;
                    }
                    case 1: {
                        this.draw2DElevationImageNode(ice2DContainer, this.elevationImage, matrix4f, 1.0f, 1.0f);
                        break;
                    }
                    default: {
                        System.out.println("DoorHardware.draw2DImageNode(), Wrong rootDrawSide " + side2 + " for: " + this);
                        break;
                    }
                }
            }
            else {
                System.out.println("DoorHardware.draw2DImageNode(), Wrong side " + side + " for: " + this);
            }
        }
    }
    
    public int getSide() {
        return -1;
    }
    
    private AbstractFrame getFrame() {
        return (AbstractFrame)this.getParent((TypeFilter)new AbstractFrameFilter());
    }
    
    private CoreFrameStructureInterface getFrameSet() {
        return (CoreFrameStructureInterface)this.getParent((TypeFilter)new CoreFrameStructureInterfaceTypeFilter());
    }
    
    protected void draw2DElevationImageNode(final Ice2DContainer ice2DContainer, final BufferedImage bufferedImage, final Matrix4f matrix4f, final float n, final float n2) {
        ice2DContainer.add((Ice2DNode)(this.imageNode = new Ice2DImageNode(this.getElevationImageLayerName(), (TransformableEntity)this, matrix4f, bufferedImage)));
        if (this.getElevationBufferedImageFileName() != null) {
            this.drawSelectionCubeNode(ice2DContainer, "Elevation Selection");
        }
    }
    
    protected String getElevationImageLayerName() {
        return "Elevation Duplexes";
    }
    
    protected void drawOtherSide(final Ice2DContainer ice2DContainer, final float n, final float n2) {
        ice2DContainer.add((Ice2DNode)(this.sideBImageNode = new Ice2DImageNode(this.getElevationImageLayerName(), (TransformableEntity)this, this.calculateSideBMatrix(ice2DContainer), this.elevationImage)));
        this.drawSideBSelectionCube(ice2DContainer);
    }
    
    private void drawSideBSelectionCube(final Ice2DContainer ice2DContainer) {
        this.elevationCube = new BoundingCube(this.getBoundingCube());
        final Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        float n = 0.0f;
        if (ice2DContainer.getSide() == -1) {
            final Matrix4f matrix4f2 = new Matrix4f();
            matrix4f2.setIdentity();
            matrix4f2.rotZ(3.1415927f);
            matrix4f.mul(matrix4f2);
            if (this.getFrame() != null) {
                n = 2.0f * this.getFrame().getXDimension() + 12.0f;
                if (this.getFrameSet() != null) {
                    n = 2.0f * this.getFrameSet().getXDimension() + 12.0f;
                }
            }
        }
        matrix4f.setTranslation(new Vector3f(n, 0.0f, 0.0f));
        matrix4f.mul(this.getEntWorldSpaceMatrix());
        ice2DContainer.add((Ice2DNode)(this.sideBSelectionCubeNode = new Ice2DCubeNode("Elevation Selection", (TransformableEntity)this, matrix4f)));
        this.sideBSelectionCubeNode.setSize(this.elevationCube);
        this.sideBSelectionCubeNode.setColor(Color.RED);
    }
    
    private Matrix4f calculateSideBMatrix(final Ice2DContainer ice2DContainer) {
        final Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        float n = 0.0f;
        if (ice2DContainer.getSide() == -1) {
            final Matrix4f matrix4f2 = new Matrix4f();
            matrix4f2.setIdentity();
            matrix4f2.rotZ(3.1415927f);
            matrix4f.mul(matrix4f2);
            if (this.getFrame() != null) {
                n = 2.0f * this.getFrame().getXDimension() + 12.0f;
                if (this.getFrameSet() != null) {
                    n = 2.0f * this.getFrameSet().getXDimension() + 12.0f;
                }
            }
        }
        matrix4f.setTranslation(new Vector3f(n, 0.0f, 0.0f));
        matrix4f.mul(this.getEntWorldSpaceMatrix());
        return matrix4f;
    }
    
    private void setUpNamedPoints() {
        this.addNamedPoint("SVG_POS", new Point3f(0.0f, 0.0f, 0.0f));
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedRotations();
        final Point3f namedPointLocal = this.getNamedPointLocal("SVG_POS");
        if (namedPointLocal != null) {
            namedPointLocal.x = 0.5f;
            final String id = this.getLwTypeCreatedFrom().getId();
            if (id != null && id.indexOf("Single") > -1) {
                namedPointLocal.x = -0.5f;
            }
        }
    }
    
    public void populateCompareNode(final Class clazz, final CompareNode compareNode) {
        super.populateCompareNode(clazz, compareNode);
        compareNode.addCompareValue("usertag", (Object)this.getUserTagNameAttribute("TagName1"));
    }
    
    public boolean draw2D() {
        return false;
    }
    
    public boolean drawCAD() {
        return false;
    }
    
    public void solve() {
        super.solve();
        ICDUtilities.validateShowInManufacturingReport(this);
    }
    
    public void drawCadElevation(final ElevationEntity elevationEntity, final ICadBlockNode cadBlockNode, final int n, final SolutionSetting solutionSetting) {
    }
    
    protected boolean validateEntityParent() {
        boolean validateEntityParent = super.validateEntityParent();
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity == null || parentEntity instanceof Solution) {
            validateEntityParent = false;
        }
        return validateEntityParent;
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addDimensionsToManufacturingTreeMap(treeMap, this);
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
}
