package net.iceedge.catalogs.icd.panel;

import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.catalogs.icd.ICDUtilities;
import net.dirtt.menus.Menus;
import net.dirtt.icelib.main.ElevationEntity;
import com.iceedge.icd.typefilters.ICDFrameTypeFilter;
import com.iceedge.icd.typefilters.ICDPostTypeFilter;
import net.dirtt.icelib.ui.attribute.explorer.ui.entity.PossibleValue;
import net.iceedge.icecore.basemodule.baseclasses.material.BasicMaterialEntity;
import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.report.compare.CompareNode;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelInterfaceFilter;
import net.dirtt.utilities.GuiUtilities;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.Font;
import java.awt.Graphics2D;
import net.dirtt.icebox.canvas2d.Ice2DNode;
import net.dirtt.icebox.canvas2d.Ice2DImageNode;
import net.dirtt.icelib.report.Report;
import net.dirtt.icebox.iceoutput.core.IceOutputShapeNode;
import javax.vecmath.Tuple3f;
import net.iceedge.catalogs.icd.ICDILineFilter;
import net.iceedge.catalogs.icd.ICDILine;
import java.awt.geom.Line2D;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import javax.vecmath.Vector3f;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import com.iceedge.icd.rendering.ICD2DJointDirectionNode;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationUtilities;
import java.awt.geom.Path2D;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import javax.vecmath.Matrix4f;
import com.iceedge.icd.typefilters.ICDPanelToPanelConnectionHWTypeFilter;
import net.dirtt.icelib.main.attributes.Attribute;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelInterface;
import com.iceedge.icd.typefilters.ICDPanelSegmentTypeFilter;
import java.util.List;
import net.dirtt.utilities.Pair;
import com.iceedge.icd.typefilters.ICDHorizontalBreakableExtrusionTypeFilter;
import com.iceedge.icd.typefilters.ICDChaseMidConnectorContainerTypeFilter;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.BoundingCube;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.TypeableEntity;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import java.util.Vector;
import net.dirtt.utilities.MathUtilities;
import net.iceedge.catalogs.icd.intersection.ICDIntersectionFilter;
import net.iceedge.catalogs.icd.intersection.ICDChaseMidConnectorContainer;
import javax.vecmath.Point3f;
import net.iceedge.catalogs.icd.ICDSegmentFilter;
import net.iceedge.catalogs.icd.ICDSegment;
import com.iceedge.icd.typefilters.ICDInternalExtrusionTypeFilter;
import com.iceedge.icd.typefilters.ICDBottomExtrusionTypeFilter;
import com.iceedge.icd.typefilters.ICDTopExtrusionTypeFilter;
import net.iceedge.catalogs.icd.intersection.ICDPostForSubPanel;
import net.iceedge.catalogs.icd.intersection.ICDPost;
import net.iceedge.catalogs.icd.intersection.ICDIntersection;
import com.iceedge.icd.typefilters.ICDChaseConnectorExtrusionTypeFilter;
import net.dirtt.utilities.TypeFilter;
import com.iceedge.icd.typefilters.ICDPostHostInterfaceTypeFilter;
import net.iceedge.catalogs.icd.intersection.ICDPostHostInterface;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icebox.canvas2d.Ice2DShapeNode;
import java.awt.image.BufferedImage;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.catalogs.icd.constants.ICDAttributeNameConstants;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintable;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;

public class ICDJoint extends TransformableTriggerUser implements AssemblyPaintable, ICDAttributeNameConstants, ICDManufacturingReportable
{
    private static final int ICD_JOINT_NORMAL = -1;
    private static final int ICD_JOINT_TOP_STEP_RETURN = 1;
    private static final int ICD_JOINT_BOTTOM_STEP_RETURN = 2;
    private BufferedImage elevationImage;
    private Ice2DShapeNode assemblyNode;
    public static final String[] JOINT_TYPE;
    
    public ICDJoint(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.setupNamedPoints();
    }
    
    public Object clone() {
        return this.buildClone(new ICDJoint(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDJoint buildClone(final ICDJoint icdJoint) {
        super.buildClone((TransformableTriggerUser)icdJoint);
        return icdJoint;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDJoint(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDJoint buildFrameClone(final ICDJoint icdJoint, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdJoint, entityObject);
        return icdJoint;
    }
    
    public void modifyCurrentOption() {
        this.validateIndicator();
        super.modifyCurrentOption();
    }
    
    protected void validateIndicator() {
        final ICDPostHostInterface icdPostHostInterface = (ICDPostHostInterface)this.getParent((TypeFilter)new ICDPostHostInterfaceTypeFilter());
        if (icdPostHostInterface != null) {
            final String bottomJointType = icdPostHostInterface.getBottomJointType();
            if (this.getParent((TypeFilter)new ICDChaseConnectorExtrusionTypeFilter()) != null) {
                this.setJointTypeForVerticalConnector(bottomJointType);
                return;
            }
            ICDPost icdPost;
            if (icdPostHostInterface instanceof ICDIntersection) {
                icdPost = (ICDPost)((ICDIntersection)icdPostHostInterface).getChildByClass((Class)ICDPost.class);
            }
            else if (icdPostHostInterface instanceof ICDPanelToPanelConnectionHW) {
                icdPost = (ICDPost)((ICDPanelToPanelConnectionHW)icdPostHostInterface).getChildByClass((Class)ICDPost.class);
            }
            else {
                if (icdPostHostInterface instanceof ICDPostForSubPanel) {
                    this.createNewAttribute("Joint_Type", bottomJointType);
                    return;
                }
                System.err.println("&&&&&&&&&&&&&&&&&&&&&&: Wrong type");
                return;
            }
            this.setBottomAndMiddleJointTypeForPost(icdPost, bottomJointType);
        }
        else {
            final int numberOfBeamForJoint = this.getNumberOfBeamForJoint();
            final ICDTopExtrusion icdTopExtrusion = (ICDTopExtrusion)this.getParent((TypeFilter)new ICDTopExtrusionTypeFilter());
            if (icdTopExtrusion != null) {
                this.setJointTypeForTopExtrusion(icdTopExtrusion, numberOfBeamForJoint);
                return;
            }
            if (this.getParent((TypeFilter)new ICDBottomExtrusionTypeFilter()) != null) {
                this.setJointTypeForBottomAndInternalExtrusion(numberOfBeamForJoint);
                return;
            }
            if (this.getParent((TypeFilter)new ICDInternalExtrusionTypeFilter()) != null) {
                this.setJointTypeForBottomAndInternalExtrusion(numberOfBeamForJoint);
            }
        }
    }
    
    private void setJointTypeForTopExtrusion(final ICDTopExtrusion icdTopExtrusion, final int n) {
        if ("Yes".equalsIgnoreCase(icdTopExtrusion.getAttributeValueAsString("specialInternalExtrusion"))) {
            final ICDSegment icdSegment = (ICDSegment)this.getParent((TypeFilter)new ICDSegmentFilter());
            final ICDPanel icdPanel = (ICDPanel)this.getParent((TypeFilter)new ICDPanelFilter());
            if (icdSegment != null && icdPanel != null) {
                final Point3f point3f2;
                final Point3f point3f = point3f2 = new Point3f();
                point3f2.z += 0.5f;
                final int numberOfChaseAtPoint = icdSegment.getNumberOfChaseAtPoint(this.convertPointToWorldSpace(point3f));
                final float z = this.getBasePoint3f().z;
                final ICDSubInternalExtrusion subInternalExtrusion = icdTopExtrusion.getSubInternalExtrusion(z - 1.0f);
                final ICDSubInternalExtrusion subInternalExtrusion2 = icdTopExtrusion.getSubInternalExtrusion(z + 1.0f);
                if (subInternalExtrusion != null && subInternalExtrusion2 != null) {
                    final String attributeValueAsString = subInternalExtrusion.getAttributeValueAsString("ICD_Extrusion_Type");
                    final String attributeValueAsString2 = subInternalExtrusion2.getAttributeValueAsString("ICD_Extrusion_Type");
                    final boolean b = !"none".equalsIgnoreCase(attributeValueAsString) && !"none".equalsIgnoreCase(attributeValueAsString2);
                    final boolean shouldBreakChaseVertically = icdPanel.shouldBreakChaseVertically();
                    if (numberOfChaseAtPoint > 0) {
                        if (shouldBreakChaseVertically) {
                            switch (numberOfChaseAtPoint) {
                                case 1: {
                                    if (n <= 0) {
                                        this.createNewAttribute("Joint_Type", "4 Way (3D)(90)");
                                        break;
                                    }
                                    this.createNewAttribute("Joint_Type", "5 Way (3D)");
                                    break;
                                }
                                case 2: {
                                    this.createNewAttribute("Joint_Type", "5 Way (3D)");
                                    break;
                                }
                                default: {
                                    System.err.println("ICDJoint.setJointTypeForTopExtrusion() something wrong with this condition.");
                                    break;
                                }
                            }
                        }
                        else {
                            switch (numberOfChaseAtPoint) {
                                case 1: {
                                    if (n <= 0) {
                                        this.createNewAttribute("Joint_Type", "3 Way (3D)");
                                        break;
                                    }
                                    this.createNewAttribute("Joint_Type", "4 Way (3D)(180)");
                                    break;
                                }
                                case 2: {
                                    this.createNewAttribute("Joint_Type", "4 Way (3D)(180)");
                                    break;
                                }
                                default: {
                                    System.err.println("ICDJoint.setJointTypeForTopExtrusion() something wrong with this condition.");
                                    break;
                                }
                            }
                        }
                    }
                    else if (b) {
                        if (this.connectWithVerticalInternalExtrusionFromPanelAbove()) {
                            if (n <= 0) {
                                this.createNewAttribute("Joint_Type", "3 Way (3D)");
                            }
                            else if (n == 1) {
                                this.createNewAttribute("Joint_Type", "4 Way (3D)(90)");
                            }
                            else if (n == 2) {
                                this.createNewAttribute("Joint_Type", "5 Way (3D)");
                            }
                        }
                        else if (n <= 0) {
                            this.createNewAttribute("Joint_Type", "2 Way (180)");
                        }
                        else if (n == 1) {
                            this.createNewAttribute("Joint_Type", "3 Way (3D)");
                        }
                        else if (n == 2) {
                            this.createNewAttribute("Joint_Type", "4 Way (3D)(180)");
                        }
                    }
                    else {
                        this.createNewAttribute("Joint_Type", "2 Way (3D)(90)");
                    }
                }
            }
        }
    }
    
    protected void setBottomAndMiddleJointTypeForPost(final ICDPost icdPost, String s) {
        if (icdPost != null) {
            final ICDChaseMidConnectorContainer icdChaseMidConnectorContainer = (ICDChaseMidConnectorContainer)icdPost.getChildByClass((Class)ICDChaseMidConnectorContainer.class);
            if (icdChaseMidConnectorContainer == null) {
                this.setJoint(s);
            }
            else {
                final String attributeValueAsString = icdChaseMidConnectorContainer.getAttributeValueAsString("ICD_Chase_Connector_Container_Size");
                if (!(this instanceof ICDMiddleJoint) && (attributeValueAsString == null || attributeValueAsString.equalsIgnoreCase("None"))) {
                    this.setJoint(s);
                }
                else {
                    final ICDIntersection icdIntersection = (ICDIntersection)this.getParent((TypeFilter)new ICDIntersectionFilter());
                    final ICDPanelToPanelConnectionHW icdPanelToPanelConnectionHW = (ICDPanelToPanelConnectionHW)this.getParent((TypeFilter)new ICDPanelToPanelConnectionHWFilter());
                    final boolean suspendedContainer = icdChaseMidConnectorContainer.isSuspendedContainer();
                    final Point3f basePointWorldSpace = this.getBasePointWorldSpace();
                    final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion = (ICDChaseConnectorExtrusion)icdChaseMidConnectorContainer.getChildByLWType("ICD_Chase_Mid_Top_Connector_Type");
                    final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion2 = (ICDChaseConnectorExtrusion)icdChaseMidConnectorContainer.getChildByLWType("ICD_Chase_Mid_Bottom_Connector_Type");
                    Point3f basePointWorldSpace2 = new Point3f();
                    if (icdChaseConnectorExtrusion != null) {
                        basePointWorldSpace2 = icdChaseConnectorExtrusion.getBasePointWorldSpace();
                    }
                    boolean b = false;
                    if (icdChaseConnectorExtrusion2 != null && MathUtilities.isSameFloat(basePointWorldSpace.z, icdChaseConnectorExtrusion2.getBasePointWorldSpace().z + 0.5f, 0.1f) && this instanceof ICDMiddleJoint) {
                        b = true;
                    }
                    if (this instanceof ICDMiddleJoint && !MathUtilities.isSameFloat(basePointWorldSpace.z, basePointWorldSpace2.z + 0.5f, 0.1f) && !b) {
                        this.createNewAttribute("Joint_Type", s);
                    }
                    else {
                        if (s.equals("1 Way")) {
                            if (attributeValueAsString.equalsIgnoreCase("Single")) {
                                s = "2 Way (90)";
                                if (icdIntersection != null && icdIntersection.getSegmentsWithNoFrameBottomTile() > 0) {
                                    s = "1 Way";
                                }
                                else if (icdPanelToPanelConnectionHW != null && !(this instanceof ICDMiddleJoint)) {
                                    if (icdPanelToPanelConnectionHW.getNumberOfNoFrameBottomTiles() == 1) {
                                        s = "2 Way (90)";
                                    }
                                    if (icdPanelToPanelConnectionHW.getNumberOfNoFrameBottomTiles() == 2) {
                                        s = "1 Way";
                                    }
                                }
                                if (suspendedContainer && b) {
                                    s = "2 Way (90)";
                                }
                            }
                            else if (attributeValueAsString.equalsIgnoreCase("Double")) {
                                s = "3 Way";
                                if (icdIntersection != null && icdIntersection.getSegmentsWithNoFrameBottomTile() > 0) {
                                    s = "2 Way (180)";
                                }
                                else if (icdPanelToPanelConnectionHW != null && !(this instanceof ICDMiddleJoint)) {
                                    if (icdPanelToPanelConnectionHW.getNumberOfNoFrameBottomTiles() == 1) {
                                        s = "3 Way";
                                    }
                                    if (icdPanelToPanelConnectionHW.getNumberOfNoFrameBottomTiles() == 2) {
                                        s = "2 Way (180)";
                                    }
                                }
                                if (suspendedContainer && b) {
                                    s = "2 Way (180)";
                                }
                            }
                        }
                        else if (s.equals("2 Way (90)")) {
                            s = "3 Way";
                            if (icdPanelToPanelConnectionHW != null && !(this instanceof ICDMiddleJoint)) {
                                if (icdPanelToPanelConnectionHW.getNumberOfNoFrameBottomTiles() == 1) {
                                    s = "2 Way (90)";
                                }
                                if (icdPanelToPanelConnectionHW.getNumberOfNoFrameBottomTiles() == 2) {
                                    s = "1 Way";
                                }
                            }
                            if (suspendedContainer && b) {
                                s = "1 Way";
                            }
                        }
                        else if (s.equals("2 Way (180)")) {
                            if (attributeValueAsString.equalsIgnoreCase("Single")) {
                                s = "3 Way";
                                if (icdPanelToPanelConnectionHW != null && !(this instanceof ICDMiddleJoint)) {
                                    if (icdPanelToPanelConnectionHW.getNumberOfNoFrameBottomTiles() == 1) {
                                        s = "2 Way (90)";
                                    }
                                    if (icdPanelToPanelConnectionHW.getNumberOfNoFrameBottomTiles() == 2) {
                                        s = "1 Way";
                                    }
                                }
                                if (suspendedContainer && b) {
                                    s = "1 Way";
                                }
                            }
                            else if (attributeValueAsString.equalsIgnoreCase("Double")) {
                                s = "4 Way";
                                if (icdPanelToPanelConnectionHW != null && !(this instanceof ICDMiddleJoint)) {
                                    if (icdPanelToPanelConnectionHW.getNumberOfNoFrameBottomTiles() == 1) {
                                        s = "3 Way";
                                    }
                                    if (icdPanelToPanelConnectionHW.getNumberOfNoFrameBottomTiles() == 2) {
                                        s = "2 Way (180)";
                                    }
                                }
                                if (suspendedContainer && b) {
                                    s = "2 Way (180)";
                                }
                            }
                        }
                        else if (s.equals("3 Way")) {
                            s = "4 Way";
                            if (suspendedContainer && b) {
                                s = "1 Way";
                            }
                        }
                        else if (s.equals("4 Way")) {}
                        this.createNewAttribute("Joint_Type", s);
                    }
                }
            }
            return;
        }
        System.err.println("&&&&&&&&&&&&&&&&&&&&&&: Null Pointer for post");
    }
    
    private void setJoint(String jointTypeForNumberOfConnectingSegments) {
        final ICDIntersection icdIntersection = (ICDIntersection)this.getParent((TypeFilter)new ICDIntersectionFilter());
        final ICDPanelToPanelConnectionHW icdPanelToPanelConnectionHW = (ICDPanelToPanelConnectionHW)this.getParent((TypeFilter)new ICDPanelToPanelConnectionHWFilter());
        if (icdIntersection != null) {
            final Vector segmentsFromArms = icdIntersection.getSegmentsFromArms();
            final int numberOfConnectingSegments = this.getNumberOfConnectingSegments(segmentsFromArms, icdIntersection);
            final int connectedChaseCountToSegments = this.getConnectedChaseCountToSegments(segmentsFromArms);
            final int n = numberOfConnectingSegments + connectedChaseCountToSegments;
            final ArrayList<Float> list = new ArrayList<Float>();
            for (final Segment segment : segmentsFromArms) {
                if ((segment instanceof ICDSegment && this instanceof ICDMiddleJoint) || !((ICDSegment)segment).isBottomTileNoFrame()) {
                    list.add(segment.getRotationWorldSpace());
                }
            }
            jointTypeForNumberOfConnectingSegments = getJointTypeForNumberOfConnectingSegments(jointTypeForNumberOfConnectingSegments, n, connectedChaseCountToSegments, list);
        }
        else if (icdPanelToPanelConnectionHW != null) {
            final int connectedExtrusionCount = this.getConnectedExtrusionCount(icdPanelToPanelConnectionHW.getLinkedSegments());
            if (connectedExtrusionCount == 3) {
                jointTypeForNumberOfConnectingSegments = "2 Way (180)";
            }
            if (connectedExtrusionCount == 2) {
                jointTypeForNumberOfConnectingSegments = "1 Way";
            }
        }
        this.createNewAttribute("Joint_Type", jointTypeForNumberOfConnectingSegments);
    }
    
    public static String getJointTypeForNumberOfConnectingSegments(String s, final int n, final int n2, final ArrayList<Float> list) {
        if (n == 4) {
            s = "4 Way";
        }
        else if (n == 3) {
            s = "3 Way";
        }
        else if (n == 2) {
            s = "2 Way (180)";
            if ((list.size() == 2 && !MathUtilities.isSameFloat((float)list.get(0), (float)list.get(1), 0.001f)) || (list.size() == 1 && n2 == 1)) {
                s = "2 Way (90)";
            }
        }
        else if (n <= 1) {
            s = "1 Way";
        }
        return s;
    }
    
    private int getConnectedChaseCountToSegments(final Vector<Segment> vector) {
        final Point3f basePointWorldSpace = this.getBasePointWorldSpace();
        int n = 0;
        for (final Segment segment : vector) {
            if (segment instanceof ICDSegment) {
                n += ((ICDSegment)segment).getNumberOfChaseAtPoint(basePointWorldSpace);
            }
        }
        return n;
    }
    
    private int getNumberOfConnectingSegments(final Vector<Segment> vector, final ICDIntersection icdIntersection) {
        int connectedExtrusionCount;
        if (this instanceof ICDMiddleJoint) {
            connectedExtrusionCount = this.getConnectedExtrusionCount(vector);
            connectedExtrusionCount -= 2;
        }
        else {
            connectedExtrusionCount = vector.size() - icdIntersection.getSegmentsWithNoFrameBottomTile();
        }
        return connectedExtrusionCount;
    }
    
    private int getConnectedExtrusionCount(final Vector<? extends Segment> vector) {
        int n = 0;
        final HashSet<Object> set = new HashSet<Object>();
        final Iterator<? extends Segment> iterator = vector.iterator();
        while (iterator.hasNext()) {
            set.addAll(((ICDSegment)iterator.next()).getAllBasicExtrusions(false));
        }
        for (final TypeableEntity typeableEntity : set) {
            final BoundingCube worldBoundingCube = this.getWorldBoundingCube();
            worldBoundingCube.set(worldBoundingCube.getLowerX() * 2.0f, worldBoundingCube.getLowerY() * 2.0f, worldBoundingCube.getLowerZ() * 2.0f, worldBoundingCube.getUpperX() * 2.0f, worldBoundingCube.getUpperY() * 2.0f, worldBoundingCube.getUpperZ() * 2.0f);
            if (typeableEntity instanceof TransformableEntity && worldBoundingCube.intersect(((TransformableEntity)typeableEntity).getWorldBoundingCube())) {
                ++n;
            }
        }
        return n;
    }
    
    public boolean extrusionConnectsToJoint(final TypeableEntity typeableEntity) {
        final BoundingCube worldBoundingCube = this.getWorldBoundingCube();
        worldBoundingCube.set(worldBoundingCube.getLowerX() * 2.0f, worldBoundingCube.getLowerY() * 2.0f, worldBoundingCube.getLowerZ() * 2.0f, worldBoundingCube.getUpperX() * 2.0f, worldBoundingCube.getUpperY() * 2.0f, worldBoundingCube.getUpperZ() * 2.0f);
        return typeableEntity instanceof TransformableEntity && worldBoundingCube.intersect(((TransformableEntity)typeableEntity).getWorldBoundingCube());
    }
    
    private void setJointTypeForBottomAndInternalExtrusion(final int n) {
        final ICDSegment icdSegment = (ICDSegment)this.getParent((TypeFilter)new ICDSegmentFilter());
        final ICDPanel icdPanel = (ICDPanel)this.getParent((TypeFilter)new ICDPanelFilter());
        if (icdSegment == null || icdPanel == null) {
            this.createNewAttribute("Joint_Type", "2 Way (3D)");
            return;
        }
        final int numberOfChaseAtPoint = icdSegment.getNumberOfChaseAtPoint(this.getBasePointWorldSpace());
        final boolean equalsIgnoreCase = "Yes".equalsIgnoreCase(icdPanel.getAttributeValueAsString("ICD_Vertically_Split_Chase"));
        final EntityObject entityObject = (EntityObject)this.getParent();
        if (entityObject == null) {
            this.createNewAttribute("Joint_Type", "2 Way (3D)");
            return;
        }
        if (!(entityObject instanceof ICDInternalExtrusion)) {
            if (entityObject instanceof ICDBottomExtrusion) {
                this.validateJointTypeForBottomExtrusion(numberOfChaseAtPoint, equalsIgnoreCase, n);
            }
            return;
        }
        if (((ICDInternalExtrusion)entityObject).isVertical()) {
            this.validateJointTypeForVerticalInternalExtrusion(numberOfChaseAtPoint);
            return;
        }
        this.validateJointTypeForHorizontalInternalExtrusion(numberOfChaseAtPoint, equalsIgnoreCase, n);
    }
    
    private void validateJointTypeForBottomExtrusion(final int n, final boolean b, final int n2) {
        if (b) {
            switch (n) {
                case 0: {
                    if (n2 <= 0) {
                        this.createNewAttribute("Joint_Type", "3 Way (3D) Leveller");
                        break;
                    }
                    if (n2 == 1) {
                        this.createNewAttribute("Joint_Type", "4 Way (3D)(90) Leveller");
                        break;
                    }
                    if (n2 == 2) {
                        this.createNewAttribute("Joint_Type", "5 Way (3D) Leveller");
                        break;
                    }
                    break;
                }
                case 1: {
                    if (n2 <= 0) {
                        this.createNewAttribute("Joint_Type", "4 Way (3D)(90) Leveller");
                        break;
                    }
                    this.createNewAttribute("Joint_Type", "5 Way (3D) Leveller");
                    break;
                }
                case 2: {
                    this.createNewAttribute("Joint_Type", "5 Way (3D) Leveller");
                    break;
                }
                default: {
                    this.createNewAttribute("Joint_Type", "2 Way (3D)(180)");
                    break;
                }
            }
        }
        else {
            switch (n) {
                case 1: {
                    if (n2 <= 0) {
                        this.createNewAttribute("Joint_Type", "3 Way (3D)");
                        break;
                    }
                    this.createNewAttribute("Joint_Type", "4 Way (3D)(180)");
                    break;
                }
                case 2: {
                    this.createNewAttribute("Joint_Type", "4 Way (3D)(180)");
                    break;
                }
                default: {
                    if (n2 <= 0) {
                        this.createNewAttribute("Joint_Type", "2 Way (3D)(180)");
                        break;
                    }
                    if (n2 == 1) {
                        this.createNewAttribute("Joint_Type", "3 Way (3D)");
                        break;
                    }
                    if (n2 == 2) {
                        this.createNewAttribute("Joint_Type", "4 Way (3D)(180)");
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private void validateJointTypeForHorizontalInternalExtrusion(final int n, final boolean b, final int n2) {
        if (b) {
            switch (n) {
                case 1: {
                    if (n2 <= 0) {
                        this.createNewAttribute("Joint_Type", "4 Way (3D)(90)");
                        break;
                    }
                    this.createNewAttribute("Joint_Type", "5 Way (3D)");
                    break;
                }
                case 2: {
                    this.createNewAttribute("Joint_Type", "5 Way (3D)");
                    break;
                }
                default: {
                    if (n2 <= 0) {
                        this.createNewAttribute("Joint_Type", "3 Way (3D)");
                        break;
                    }
                    if (n2 == 1) {
                        this.createNewAttribute("Joint_Type", "4 Way (3D)(90)");
                        break;
                    }
                    if (n2 == 2) {
                        this.createNewAttribute("Joint_Type", "5 Way (3D)");
                        break;
                    }
                    break;
                }
            }
        }
        else {
            switch (n) {
                case 1: {
                    if (n2 <= 0) {
                        this.createNewAttribute("Joint_Type", "3 Way (3D)");
                        break;
                    }
                    this.createNewAttribute("Joint_Type", "4 Way (3D)(180)");
                    break;
                }
                case 2: {
                    this.createNewAttribute("Joint_Type", "4 Way (3D)(180)");
                    break;
                }
                default: {
                    if (n2 <= 0) {
                        this.createNewAttribute("Joint_Type", "2 Way (3D)(180)");
                        break;
                    }
                    if (n2 == 1) {
                        this.createNewAttribute("Joint_Type", "3 Way (3D)");
                        break;
                    }
                    if (n2 == 2) {
                        this.createNewAttribute("Joint_Type", "4 Way (3D)(180)");
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private void validateJointTypeForVerticalInternalExtrusion(final int n) {
        switch (n) {
            case 1: {
                this.createNewAttribute("Joint_Type", "3 Way (3D)(90)");
                break;
            }
            case 2: {
                String s = "4 Way (3D)(180)";
                final ICDPanel icdPanel = (ICDPanel)this.getParent((TypeFilter)new ICDPanelFilter());
                if (icdPanel != null && icdPanel.shouldBreakChaseVertically()) {
                    s = "5 Way (3D)";
                }
                this.createNewAttribute("Joint_Type", s);
                break;
            }
            default: {
                this.createNewAttribute("Joint_Type", "2 Way (3D)");
                break;
            }
        }
    }
    
    private boolean isBottomJointForSuspendedChase() {
        return Solution.lwTypeObjectByName("ICD_BottomJoint_For_SuspendedChase_Type") == this.getLwTypeCreatedFrom();
    }
    
    protected void setJointTypeForVerticalConnector(final String s) {
        final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion = (ICDChaseConnectorExtrusion)this.getParent((TypeFilter)new ICDChaseConnectorExtrusionTypeFilter());
        if (icdChaseConnectorExtrusion != null && icdChaseConnectorExtrusion.isSuspendedeChaseSupport()) {
            this.createNewAttribute("Joint_Type", "1 Way");
            this.createNewAttribute("Use_Bottom_Joint_Indicator", "true");
            return;
        }
        final ICDChaseMidConnectorContainer icdChaseMidConnectorContainer = (ICDChaseMidConnectorContainer)this.getParent((TypeFilter)new ICDChaseMidConnectorContainerTypeFilter());
        if (icdChaseMidConnectorContainer == null) {
            this.createNewAttribute("Joint_Type", "2 Way (90)");
            return;
        }
        if (icdChaseMidConnectorContainer.isSuspendedContainer()) {
            this.createNewAttribute("Use_Bottom_Joint_Indicator", "false");
            this.createNewAttribute("Use_Top_Joint_Indicator", "true");
        }
        else {
            this.createNewAttribute("Use_Bottom_Joint_Indicator", "true");
            if ("ICD_BottomJoint_For_SuspendedChase_Type".equals(this.getLwTypeCreatedFrom().getId())) {
                this.createNewAttribute("Use_Top_Joint_Indicator", "false");
            }
            else {
                this.createNewAttribute("Use_Top_Joint_Indicator", "true");
            }
        }
        if (s.equals("1 Way")) {
            if (this.isBottomJointForSuspendedChase()) {
                this.createNewAttribute("Joint_Type", "3 Way");
            }
            else {
                this.createNewAttribute("Joint_Type", "2 Way (90)");
            }
        }
        else if (s.equals("2 Way (90)")) {
            if (this.isBottomJointForSuspendedChase()) {
                this.createNewAttribute("Joint_Type", "3 Way");
            }
            else {
                this.createNewAttribute("Joint_Type", "2 Way (90)");
            }
        }
        else if (s.equals("2 Way (180)")) {
            final ICDPostHostInterface icdPostHostInterface = (ICDPostHostInterface)this.getParent((TypeFilter)new ICDPostHostInterfaceTypeFilter());
            int n = 0;
            if (icdPostHostInterface instanceof ICDIntersection) {
                n = ((ICDIntersection)icdPostHostInterface).getNumberOfChaseOnPointSideFor2And3WayPost(this.getBasePointWorldSpace());
            }
            else if (icdPostHostInterface instanceof ICDPanelToPanelConnectionHW) {
                n = ((ICDPanelToPanelConnectionHW)icdPostHostInterface).getNumberOfChaseOnPointSide(this.getBasePointWorldSpace());
            }
            if (n == 1) {
                if (this.isBottomJointForSuspendedChase()) {
                    this.createNewAttribute("Joint_Type", "3 Way");
                }
                else {
                    this.createNewAttribute("Joint_Type", "2 Way (90)");
                }
            }
            else if (n == 2) {
                if (this.isBottomJointForSuspendedChase()) {
                    this.createNewAttribute("Joint_Type", "4 Way");
                }
                else {
                    this.createNewAttribute("Joint_Type", "3 Way");
                }
            }
        }
        else if (s.equals("3 Way")) {
            final ICDPostHostInterface icdPostHostInterface2 = (ICDPostHostInterface)this.getParent((TypeFilter)new ICDPostHostInterfaceTypeFilter());
            if (icdPostHostInterface2 instanceof ICDIntersection) {
                final int numberOfChaseOnPointSideFor2And3WayPost = ((ICDIntersection)icdPostHostInterface2).getNumberOfChaseOnPointSideFor2And3WayPost(this.getBasePointWorldSpace());
                if (numberOfChaseOnPointSideFor2And3WayPost == 1) {
                    if (this.isBottomJointForSuspendedChase()) {
                        this.createNewAttribute("Joint_Type", "3 Way");
                    }
                    else {
                        this.createNewAttribute("Joint_Type", "2 Way (90)");
                    }
                }
                else if (numberOfChaseOnPointSideFor2And3WayPost == 2) {
                    if (this.isBottomJointForSuspendedChase()) {
                        this.createNewAttribute("Joint_Type", "4 Way");
                    }
                    else {
                        this.createNewAttribute("Joint_Type", "3 Way");
                    }
                }
            }
        }
        else if (this.isBottomJointForSuspendedChase()) {
            this.createNewAttribute("Joint_Type", "3 Way");
        }
        else {
            this.createNewAttribute("Joint_Type", "2 Way (90)");
        }
    }
    
    public int getNumberOfBeamForJoint() {
        final ICDHorizontalBreakableExtrusion icdHorizontalBreakableExtrusion = (ICDHorizontalBreakableExtrusion)this.getParent((TypeFilter)new ICDHorizontalBreakableExtrusionTypeFilter());
        if (icdHorizontalBreakableExtrusion == null) {
            return -1;
        }
        final List<Pair<Float, Integer>> breakLocationsForBeam = icdHorizontalBreakableExtrusion.getBreakLocationsForBeam();
        if (breakLocationsForBeam == null || breakLocationsForBeam.size() < 1) {
            return 0;
        }
        int intValue = 0;
        for (final Pair<Float, Integer> pair : breakLocationsForBeam) {
            if (MathUtilities.isSameFloat((float)pair.first, this.getBasePoint3f().z, 1.0f)) {
                intValue = (int)pair.second;
                break;
            }
        }
        return intValue;
    }
    
    public boolean connectWithVerticalInternalExtrusionFromPanelAbove() {
        final ICDPanel icdPanel = (ICDPanel)this.getParent((TypeFilter)new ICDPanelFilter());
        if (icdPanel == null) {
            return false;
        }
        final ICDPanelSegment icdPanelSegment = (ICDPanelSegment)icdPanel.getParent((TypeFilter)new ICDPanelSegmentTypeFilter());
        if (icdPanelSegment == null) {
            return false;
        }
        final ICDSegment icdSegment = (ICDSegment)icdPanel.getParent((TypeFilter)new ICDSegmentFilter());
        if (icdSegment == null) {
            return false;
        }
        final PanelInterface childPanel = icdSegment.getChildPanelAt(icdPanelSegment.getBasePoint3f().x + icdPanelSegment.getXDimension() + 1.0f);
        if (childPanel == null || icdPanel.equals(childPanel)) {
            return false;
        }
        final Point3f convertSpaces = MathUtilities.convertSpaces(new Point3f(0.0f, 0.0f, 0.0f), (EntityObject)this, (EntityObject)childPanel);
        return childPanel instanceof ICDPanel && ((ICDPanel)childPanel).getVerticalExtrusion(convertSpaces.x, true) != null;
    }
    
    public void destroy2DAsset() {
        super.destroy2DAsset();
        this.shapeNode = null;
        this.assemblyNode = null;
    }
    
    public void cutFromTree2D() {
        super.cutFromTree2D();
        if (this.shapeNode != null) {
            this.shapeNode.removeFromParent();
        }
        if (this.assemblyNode != null) {
            this.assemblyNode.removeFromParent();
        }
    }
    
    public void draw2D(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        if (this.getParent((TypeFilter)new ICDChaseConnectorExtrusionTypeFilter()) == null) {
            super.draw2D(n, ice2DContainer, solutionSetting);
        }
    }
    
    public String getDefaultLayerName() {
        return "Connectors";
    }
    
    protected boolean shouldUseNewFinishSystem() {
        return true;
    }
    
    public void solve() {
        this.validateOption();
        super.solve();
    }
    
    public void validateOption() {
        if (!(this instanceof ICDTopJoint)) {
            final ICDChaseMidConnectorContainer icdChaseMidConnectorContainer = (ICDChaseMidConnectorContainer)this.getParent((TypeFilter)new ICDChaseMidConnectorContainerTypeFilter());
            final Attribute attributeObject = this.getAttributeObject("Use_Bottom_Joint_Indicator");
            if (icdChaseMidConnectorContainer != null && attributeObject != null) {
                attributeObject.setCurrentValueAsString(String.valueOf(!icdChaseMidConnectorContainer.isSuspendedContainer()));
            }
        }
    }
    
    public boolean usePlotGVT() {
        return false;
    }
    
    public boolean isUnderIntersection() {
        return this.getIntersection() != null;
    }
    
    public ICDIntersection getIntersection() {
        return (ICDIntersection)this.getParent((TypeFilter)new ICDIntersectionFilter());
    }
    
    public boolean isUnderPanelToPanelConnectionHW() {
        return this.getPanelToPanelConnectionHW() != null;
    }
    
    public ICDPanelToPanelConnectionHW getPanelToPanelConnectionHW() {
        return (ICDPanelToPanelConnectionHW)this.getParent((TypeFilter)new ICDPanelToPanelConnectionHWTypeFilter());
    }
    
    public Vector<Ice2DPaintableNode> getAssemblyIcons(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        final ICDIntersection icdIntersection = (ICDIntersection)this.getParent((TypeFilter)new ICDIntersectionFilter());
        if (icdIntersection != null && icdIntersection.getVerticalChase() != null) {
            return null;
        }
        final Path2D.Float float1 = new Path2D.Float();
        final ICD2DJointDirectionNode e = new ICD2DJointDirectionNode(this.getLayerName(), (TransformableEntity)this, this.getEntWorldSpaceMatrix(), ICDAssemblyElevationUtilities.appendJointDirections(this, transformableEntity));
        if (this.getCurrentType().getId().equals("ICD_BottomJoint_Type")) {
            float1.append(new Rectangle2D.Float(-1.5f, this.getYDimension() - 1.75f, 3.0f, this.getYDimension()), false);
        }
        final float stroke = 3.0f;
        final Ice2DShapeNode e2 = new Ice2DShapeNode(this.getLayerName(), (TransformableEntity)this, this.getAssemblyElevationMatrix(transformableEntity, false), (Shape)float1);
        e2.setFillColor(Color.lightGray);
        e2.setStroke(stroke);
        final Vector<ICD2DJointDirectionNode> vector = new Vector<ICD2DJointDirectionNode>();
        vector.add((ICD2DJointDirectionNode)e2);
        vector.add(e);
        return (Vector<Ice2DPaintableNode>)vector;
    }
    
    protected Matrix4f getAssemblyElevationMatrix(final TransformableEntity transformableEntity, final boolean b) {
        final Matrix4f matrix4f = new Matrix4f(this.getEntWorldSpaceMatrix());
        final Matrix4f matrix4f2 = new Matrix4f();
        final ICDIntersection intersection = this.getIntersection();
        if (intersection != null) {
            matrix4f2.setIdentity();
            matrix4f2.rotZ(intersection.getRotationWorldSpace() - transformableEntity.getRotationWorldSpace());
            matrix4f.mul(matrix4f2);
        }
        if (b) {
            matrix4f2.setIdentity();
            matrix4f2.rotX(1.5707964f);
            matrix4f.mul(matrix4f2);
        }
        if (this.isStepReturnJoint() == 1) {
            matrix4f2.setIdentity();
            matrix4f2.rotZ(-1.5707964f);
            matrix4f.mul(matrix4f2);
            matrix4f2.setIdentity();
            matrix4f2.rotY(-3.1415927f);
            matrix4f.mul(matrix4f2);
            matrix4f2.setIdentity();
            matrix4f2.setTranslation(new Vector3f(0.0f, 0.0f, -1.0f));
            matrix4f.mul(matrix4f2);
        }
        else if (this.isStepReturnJoint() == 2) {
            matrix4f2.setIdentity();
            matrix4f2.rotZ(-1.5707964f);
            matrix4f.mul(matrix4f2);
            matrix4f2.setIdentity();
            matrix4f2.setTranslation(new Vector3f(0.0f, 0.0f, -1.5f));
            matrix4f.mul(matrix4f2);
        }
        matrix4f2.setIdentity();
        matrix4f2.rotX(-1.5707964f);
        matrix4f.mul(matrix4f2);
        return matrix4f;
    }
    
    public List<IceOutputNode> getPlotOutputNodes(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        if (this.getCurrentOption().getId().contains("None")) {
            return null;
        }
        final boolean jointOnChase = ICDAssemblyElevationUtilities.isJointOnChase(this);
        final Path2D.Float float1 = new Path2D.Float();
        float1.append(new Rectangle2D.Float(-this.getXDimension() / 2.0f, -this.getYDimension() * 1.75f, this.getXDimension(), this.getYDimension()), false);
        final Line2D.Float s = new Line2D.Float(0.0f, -1.25f, 0.0f, -this.getYDimension() * 2.0f - 1.25f);
        final Line2D.Float s2 = new Line2D.Float(0.0f, -1.25f, this.getXDimension() * 2.0f, -1.25f);
        final Line2D.Float s3 = new Line2D.Float(0.0f, -1.25f, -this.getXDimension() * 2.0f, -1.25f);
        final Line2D.Float float2 = new Line2D.Float(0.0f, -1.25f, -this.getXDimension() * 2.0f, this.getYDimension() * 2.0f - 1.25f);
        final Line2D.Float float3 = new Line2D.Float(0.0f, -1.25f, this.getXDimension() * 2.0f, -this.getYDimension() * 2.0f - 1.25f);
        float1.append(s, false);
        if (this.isUnderIntersection()) {
            ICDAssemblyElevationUtilities.appendInOrOut(point3f, this.getIntersection(), transformableEntity, float1, float2, float3);
            ICDAssemblyElevationUtilities.appendLeftOrRight((TransformableEntity)this, transformableEntity, this.getIntersection(), float1, s2, s3);
        }
        else if (this.getCurrentOption().getId().equals("ICD_J104CDT")) {
            ICDAssemblyElevationUtilities.appendChaseInOrOut((TransformableEntity)this, float1, float2, float3);
            float1.append(s3, false);
            float1.append(s2, false);
        }
        else if (this.getCurrentOption().getId().equals("ICD_J105CDT")) {
            float1.append(float2, false);
            float1.append(float3, false);
            float1.append(s3, false);
            float1.append(s2, false);
        }
        else if (this.getCurrentOption().getId().equals("ICD_J103CDT")) {
            if (MathUtilities.convertSpaces(new Point3f(this.getBasePointWorldSpace()), ((ICDILine)this.getParent((TypeFilter)new ICDILineFilter())).getEntWorldSpaceMatrix()).y > 0.0f) {
                float1.append(float2, false);
            }
            else {
                float1.append(float3, false);
            }
            ICDAssemblyElevationUtilities.appendChaseLeftOrRight((TransformableEntity)this, transformableEntity, float1, s3, s2);
        }
        else {
            float1.append(s3, false);
            float1.append(s2, false);
        }
        if (this.getCurrentType().getId().equals("ICD_BottomJoint_Type")) {
            float1.append(new Rectangle2D.Float(-1.5f, this.getYDimension() - 1.75f, 3.0f, this.getYDimension()), false);
        }
        final Point3f convertSpaces = MathUtilities.convertSpaces(this.getBasePointWorldSpace(), transformableEntity.getEntWorldSpaceMatrix());
        final Matrix4f matrix4f2 = new Matrix4f();
        matrix4f2.setIdentity();
        matrix4f2.mul(matrix4f);
        if (n == 1) {
            final Matrix4f matrix4f3 = new Matrix4f();
            matrix4f3.setIdentity();
            matrix4f3.rotY(3.1415927f);
            matrix4f2.mul(matrix4f3);
        }
        final Matrix4f matrix4f4 = new Matrix4f();
        matrix4f4.setIdentity();
        matrix4f4.rotX(-1.5707964f);
        matrix4f2.mul(matrix4f4);
        final Matrix4f matrix4f5 = new Matrix4f();
        matrix4f5.setIdentity();
        matrix4f5.setTranslation(new Vector3f((Tuple3f)convertSpaces));
        matrix4f2.mul(matrix4f5);
        if (jointOnChase) {
            final Matrix4f matrix4f6 = new Matrix4f();
            matrix4f6.setIdentity();
            matrix4f6.rotZ(3.1415927f);
            matrix4f2.mul(matrix4f6);
            final int n2 = -1;
            final int n3 = 1;
            final Point3f convertSpaces2 = MathUtilities.convertSpaces(new Point3f(this.getBasePointWorldSpace()), ((ICDILine)this.getParent((TypeFilter)new ICDILineFilter())).getEntWorldSpaceMatrix());
            int n4 = n2;
            if (convertSpaces2.y < 0.0f) {
                n4 = n3;
            }
            boolean b = false;
            final ICDChaseMidConnectorContainer icdChaseMidConnectorContainer = (ICDChaseMidConnectorContainer)this.getParent((TypeFilter)new ICDChaseMidConnectorContainerTypeFilter());
            if (icdChaseMidConnectorContainer != null && icdChaseMidConnectorContainer.getRotationVector3f().z == 0.0f) {
                final Matrix4f matrix4f7 = new Matrix4f();
                matrix4f7.setIdentity();
                matrix4f7.rotZ(3.1415927f);
                matrix4f2.mul(matrix4f7);
                b = true;
            }
            final Matrix4f matrix4f8 = new Matrix4f();
            matrix4f8.setIdentity();
            final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion = (ICDChaseConnectorExtrusion)this.getParent((TypeFilter)new ICDChaseConnectorExtrusionTypeFilter());
            if (icdChaseConnectorExtrusion != null) {
                final float verticalSkewOffset = ICDAssemblyElevationUtilities.getVerticalSkewOffset(icdChaseConnectorExtrusion.getBasePoint3f().y);
                if (n4 == n2) {
                    matrix4f8.setTranslation(new Vector3f((b ? -1 : 1) * icdChaseConnectorExtrusion.getBasePoint3f().y, 0.0f, verticalSkewOffset));
                }
                else {
                    matrix4f8.setTranslation(new Vector3f((b ? -1 : 1) * icdChaseConnectorExtrusion.getBasePoint3f().y, 0.0f, -verticalSkewOffset));
                }
            }
            if (n4 == n2 || b) {
                final Matrix4f matrix4f9 = new Matrix4f();
                matrix4f9.setIdentity();
                matrix4f9.rotZ(3.1415927f);
                matrix4f2.mul(matrix4f9);
            }
            matrix4f2.mul(matrix4f8);
        }
        final float n5 = -1.5707964f;
        final Matrix4f matrix4f10 = new Matrix4f();
        matrix4f10.setIdentity();
        matrix4f10.rotX(n5);
        matrix4f2.mul(matrix4f10);
        final Matrix4f matrix4f11 = new Matrix4f();
        matrix4f11.setIdentity();
        matrix4f11.rotY(3.1415927f);
        matrix4f2.mul(matrix4f11);
        final IceOutputShapeNode e = new IceOutputShapeNode((Shape)float1, matrix4f2);
        final Vector<IceOutputNode> vector = new Vector<IceOutputNode>();
        vector.add((IceOutputNode)e);
        return vector;
    }
    
    public boolean shouldAddChildrenToReport(final Report report) {
        return false;
    }
    
    public void draw2DElevation(final int n, final Ice2DContainer ice2DContainer, final boolean b, final SolutionSetting solutionSetting) {
        super.draw2DElevation(n, ice2DContainer, b, solutionSetting);
        this.draw2DImageNode(ice2DContainer);
    }
    
    public void draw2DImageNode(final Ice2DContainer ice2DContainer) {
        this.elevationImage = this.getElevationImage(ice2DContainer);
        if (this.elevationImage == null) {
            this.elevationImage = this.createElevationImage();
        }
        if (this.elevationImage != null) {
            final int side = ice2DContainer.getSide();
            switch (side) {
                case -1: {
                    this.draw2DElevationImageNode(ice2DContainer, this.elevationImage, this.getEntWorldSpaceMatrix());
                    if (ice2DContainer.isShowFrameMode()) {
                        this.drawSideB(ice2DContainer);
                        break;
                    }
                    break;
                }
                case 0: {
                    this.draw2DElevationImageNode(ice2DContainer, this.elevationImage, this.getEntWorldSpaceMatrix());
                    break;
                }
                case 1: {
                    this.drawSideB(ice2DContainer);
                    break;
                }
                default: {
                    System.out.println("Extrusion.draw2DImageNode(), Wrong rootDrawSide " + side + " for: " + this);
                    break;
                }
            }
        }
    }
    
    protected void draw2DElevationImageNode(final Ice2DContainer ice2DContainer, final BufferedImage bufferedImage, final Matrix4f matrix4f) {
        ice2DContainer.add((Ice2DNode)new Ice2DImageNode(this.getElevationImageLayerName(), (TransformableEntity)this, matrix4f, bufferedImage));
    }
    
    protected String getElevationImageLayerName() {
        return "Elevation Extrusions";
    }
    
    public BufferedImage createElevationImage() {
        final BufferedImage src = new BufferedImage(150, 150, 2);
        final BufferedImage dst = new BufferedImage(150, 150, 2);
        final Graphics2D graphics2D = (Graphics2D)src.getGraphics();
        graphics2D.setFont(new Font("Verdana", 0, 8));
        graphics2D.setColor(Color.BLACK);
        final float[] data = new float[400];
        for (int i = 0; i < 100; ++i) {
            data[i] = 0.05f;
        }
        new ConvolveOp(new Kernel(5, 5, data)).filter(src, dst);
        graphics2D.setColor(new Color(192, 192, 192));
        graphics2D.fillRect(0, 0, src.getWidth(), src.getHeight());
        graphics2D.drawImage(dst, 0, 0, null);
        return src;
    }
    
    protected BufferedImage getElevationImage(final Ice2DContainer ice2DContainer) {
        BufferedImage bufferedImage;
        if (this.myIceMaterial != null) {
            final String textureMapName = this.myIceMaterial.getTextureMapName();
            bufferedImage = ice2DContainer.getBufferedImage(textureMapName);
            if (bufferedImage == null) {
                bufferedImage = GuiUtilities.createNewElevationImage(ice2DContainer, textureMapName, 1.0f, 1.0f, 0.0f);
            }
        }
        else {
            bufferedImage = null;
        }
        if (bufferedImage == null) {
            final String elevationBufferedImageFileName = this.getElevationBufferedImageFileName();
            if (elevationBufferedImageFileName != null) {
                bufferedImage = ice2DContainer.getBufferedImage(elevationBufferedImageFileName);
            }
            if (bufferedImage == null && elevationBufferedImageFileName != null) {
                bufferedImage = GuiUtilities.createNewElevationImage(ice2DContainer, elevationBufferedImageFileName, 1.0f, 1.0f, 0.0f);
            }
        }
        return bufferedImage;
    }
    
    protected void drawSideB(final Ice2DContainer ice2DContainer) {
        final Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        float n = 0.0f;
        final PanelInterface panelInterface = (PanelInterface)this.getParent((TypeFilter)new PanelInterfaceFilter());
        if (ice2DContainer.getSide() == -1) {
            final Matrix4f matrix4f2 = new Matrix4f();
            matrix4f2.setIdentity();
            matrix4f2.rotZ(3.1415927f);
            matrix4f.mul(matrix4f2);
            if (panelInterface != null) {
                n = 2.0f * panelInterface.getXDimension() + 12.0f;
            }
        }
        matrix4f.setTranslation(new Vector3f(n, 0.0f, 0.0f));
        matrix4f.mul(this.getEntWorldSpaceMatrix());
        ice2DContainer.add((Ice2DNode)new Ice2DImageNode(this.getElevationImageLayerName(), (TransformableEntity)this, matrix4f, this.elevationImage));
    }
    
    public void populateCompareNode(final Class clazz, final CompareNode compareNode) {
        super.populateCompareNode(clazz, compareNode);
        if (ManufacturingReportable.class.isAssignableFrom(clazz)) {
            this.populateCompareNodeForICD(compareNode);
        }
    }
    
    public void populateCompareNodeForICD(final CompareNode compareNode) {
        compareNode.addCompareValue("length", (Object)this.getLength());
        final EntityObject childByClass = this.getChildByClass((Class)BasicMaterialEntity.class);
        if (childByClass != null) {
            compareNode.addCompareValue("finish", (Object)childByClass.getDescription());
        }
        compareNode.addCompareValue("description", (Object)this.getDescription());
        compareNode.addCompareValue("assembled", (Object)this.getAttributeValueAsString("isAssembled"));
        compareNode.addCompareValue("usertag", (Object)this.getUserTagNameAttribute("TagName1"));
    }
    
    public void applyChangesFromEditor(final String s, final PossibleValue possibleValue, final Collection<PossibleValue> collection, final Collection<String> collection2, final String s2) {
        super.applyChangesFromEditor(s, possibleValue, (Collection)collection, (Collection)collection2, s2);
        if (s.equals("Joint_BoltOn")) {
            this.setParentPostModified();
        }
    }
    
    private void setParentPostModified() {
        final ICDPost icdPost = (ICDPost)this.getParent((TypeFilter)new ICDPostTypeFilter());
        if (icdPost != null) {
            icdPost.setModified(true);
        }
    }
    
    public int isStepReturnJoint() {
        final ICDTopExtrusion icdTopExtrusion = (ICDTopExtrusion)this.getParent((TypeFilter)new ICDTopExtrusionTypeFilter());
        if (icdTopExtrusion != null && "Yes".equals(icdTopExtrusion.getAttributeValueAsString("specialInternalExtrusion"))) {
            final ICDFrame icdFrame = (ICDFrame)icdTopExtrusion.getParent((TypeFilter)new ICDFrameTypeFilter());
            if (icdFrame != null) {
                if ("ICD_StackingFrame".equals(icdFrame.getCurrentOption().getId())) {
                    return 1;
                }
                return 2;
            }
        }
        return -1;
    }
    
    public boolean shouldDrawAssembly() {
        return !"".equals(this.getSKU()) && !this.getCurrentOption().getId().contains("None");
    }
    
    public boolean isAssembled() {
        return "true".equals(this.getAttributeValueAsString("isAssembled"));
    }
    
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        final Solution solution = this.getSolution();
        if (solution != null && Menus.getInstance().referenceLineMode.isChecked(solution)) {
            return new Vector<String>();
        }
        if (ICDAssemblyElevationUtilities.shouldDrawElevation(elevationEntity, this)) {
            Vector<String> cadElevationScript = (Vector<String>)super.getCadElevationScript(elevationEntity);
            if (cadElevationScript == null) {
                cadElevationScript = new Vector<String>();
            }
            final ICDJointDirections appendJointDirections = ICDAssemblyElevationUtilities.appendJointDirections(this, null);
            if (appendJointDirections != null) {
                if (appendJointDirections.isLeft) {
                    cadElevationScript.add("LI:SS(3:CENTER:LEFT)");
                }
                if (appendJointDirections.isRight) {
                    cadElevationScript.add("LI:SS(3:CENTER:RIGHT)");
                }
                if (appendJointDirections.isUp) {
                    cadElevationScript.add("LI:SS(3:CENTER:UP)");
                }
                if (appendJointDirections.isDown) {
                    cadElevationScript.add("LI:SS(3:CENTER:DOWN)");
                }
                if (appendJointDirections.isIn) {
                    cadElevationScript.add("LI:SS(3:CENTER:IN)");
                }
                if (appendJointDirections.isOut) {
                    cadElevationScript.add("LI:SS(3:CENTER:OUT)");
                }
            }
            if (this.getCurrentOption().getId().equals("ICD_J102R_StepReturn")) {
                this.paintRadiusJointR(cadElevationScript);
            }
            return cadElevationScript;
        }
        return new Vector<String>();
    }
    
    void paintRadiusJointR(final Vector<String> vector) {
        vector.add(ICDAssemblyElevationUtilities.getAssemblyLineScript(new Point3f(0.0f, -0.5f, 0.0f), "extStart", "extEnd"));
        vector.add("TX:SS(" + "R" + ":" + 5 + ":" + 0.0f + ":" + 2 + ":CADELP8)");
    }
    
    protected void setupNamedPoints() {
        this.addNamedPoint("ASE_IP", new Point3f());
        final float n = 2.0f;
        this.addNamedPoint("IN", new Point3f(0.0f, -n, 0.0f));
        this.addNamedPoint("OUT", new Point3f(0.0f, n, 0.0f));
        this.addNamedPoint("LEFT", new Point3f(-n, 0.0f, 0.0f));
        this.addNamedPoint("RIGHT", new Point3f(n, 0.0f, 0.0f));
        this.addNamedPoint("UP", new Point3f(0.0f, 0.0f, n));
        this.addNamedPoint("DOWN", new Point3f(0.0f, 0.0f, -n));
        this.addNamedPoint("CENTER", new Point3f());
    }
    
    public boolean isNonOption() {
        return this.getCurrentOption().getId().contains("No_Option");
    }
    
    public void handleAttributeChange(final String s, final String s2) {
        ICDUtilities.handleAttributeChange((EntityObject)this, s, s2);
    }
    
    public void getManufacturingInfo(final TreeMap<String, String> treeMap) {
        if (this.containsAttributeKey("Product_Type")) {
            treeMap.put("Type", this.getAttributeValueAsString("Product_Type"));
        }
        if (this.containsAttributeKey("SubType")) {
            treeMap.put("SubType", this.getSubType());
        }
        treeMap.put("Description", this.getDescription());
        final List childrenByClass = this.getChildrenByClass((Class)BasicMaterialEntity.class, false);
        if (childrenByClass != null) {
            for (int i = 0; i < childrenByClass.size(); ++i) {
                final BasicMaterialEntity basicMaterialEntity = childrenByClass.get(i);
                String value = "";
                if (basicMaterialEntity.getChildCount() > 0) {
                    if (basicMaterialEntity.getChildAt(0) instanceof BasicMaterialEntity) {
                        final BasicMaterialEntity basicMaterialEntity2 = (BasicMaterialEntity)basicMaterialEntity.getChildAt(0);
                        if (basicMaterialEntity2.getAttributeValueAsString("Material_ID") != null) {
                            value = basicMaterialEntity2.getAttributeValueAsString("Material_ID");
                        }
                    }
                }
                else if (basicMaterialEntity.getAttributeValueAsString("Material_ID") != null) {
                    value = basicMaterialEntity.getAttributeValueAsString("Material_ID");
                }
                treeMap.put("Option0" + (i + 1), value);
            }
        }
        treeMap.put("UserTag", this.getUserTagNameAttribute("TagName1"));
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
    
    static {
        JOINT_TYPE = new String[] { "2 Way (180)", "1 Way", "2 Way (90)", "3 Way", "4 Way" };
    }
}
