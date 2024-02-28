package net.iceedge.catalogs.icd.elevation.assembly;

import java.util.ArrayList;
import net.iceedge.catalogs.icd.panel.ICDVerticalChaseGroup;
import net.dirtt.icelib.main.MiscItemsBucket;
import net.iceedge.catalogs.icd.panel.ICDVerticalChase;
import net.dirtt.icelib.main.Solution;
import net.iceedge.catalogs.icd.elevation.isometric.ICDIsometricAssemblyElevationEntity;
import net.dirtt.icelib.main.ElevationEntity;
import net.dirtt.icebox.canvas2d.Ice2DTextNode;
import net.dirtt.icelib.main.attributes.Attribute;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.utils.IceGVTObject;
import net.dirtt.icebox.canvas2d.Ice2DGVTElevationNode;
import javax.vecmath.Vector3f;
import net.iceedge.catalogs.icd.ICDILine;
import java.util.List;
import net.iceedge.icecore.basemodule.interfaces.SegmentBase;
import net.iceedge.catalogs.icd.panel.ICDPanel;
import net.dirtt.icelib.main.EntityObject;
import net.iceedge.catalogs.icd.ICDSubILine;
import net.iceedge.catalogs.icd.intersection.ICDChaseMidConnectorContainer;
import net.iceedge.catalogs.icd.panel.ICDTopJoint;
import net.iceedge.catalogs.icd.panel.ICDPanelToPanelConnectionHW;
import java.util.Collection;
import net.iceedge.catalogs.icd.panel.JointIntersectable;
import net.iceedge.catalogs.icd.panel.ICDJointDirections;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import net.iceedge.catalogs.icd.ICDSegment;
import javax.vecmath.Matrix4f;
import net.iceedge.catalogs.icd.panel.ICDJoint;
import java.util.Iterator;
import java.util.Vector;
import java.awt.Shape;
import net.iceedge.icecore.basemodule.baseclasses.BasicIntersectionArm;
import net.iceedge.icecore.basemodule.interfaces.ILineInterface;
import net.iceedge.icecore.basemodule.interfaces.IntersectionArmInterface;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import net.iceedge.catalogs.icd.intersection.ICDIntersection;
import net.dirtt.utilities.MathUtilities;
import net.dirtt.icelib.main.TransformableEntity;
import javax.vecmath.Point3f;

public class ICDAssemblyElevationUtilities
{
    public static int INWARDS;
    public static int OUTWARDS;
    public static int LEFT;
    public static int RIGHT;
    public static final float ELEVATION_SKEW_DEGREES = 45.0f;
    public static final int CAD_ASSEMBLY_ELEV_FONT_SIZE = 2;
    
    public static int getVerticalDirectionRelativeToPoint(final Point3f point3f, final Point3f point3f2, final TransformableEntity transformableEntity, final TransformableEntity transformableEntity2) {
        final Point3f convertPointToWorldSpace = transformableEntity.convertPointToWorldSpace(point3f2);
        final Point3f convertSpaces = MathUtilities.convertSpaces(point3f, transformableEntity2.getEntWorldSpaceMatrix());
        final Point3f convertSpaces2 = MathUtilities.convertSpaces(convertPointToWorldSpace, transformableEntity2.getEntWorldSpaceMatrix());
        final float y = convertSpaces.y;
        if (convertSpaces2.y > 0.0f) {
            if (y < 0.0f) {
                return ICDAssemblyElevationUtilities.OUTWARDS;
            }
            return ICDAssemblyElevationUtilities.INWARDS;
        }
        else {
            if (y > 0.0f) {
                return ICDAssemblyElevationUtilities.OUTWARDS;
            }
            return ICDAssemblyElevationUtilities.INWARDS;
        }
    }
    
    public static int getVerticalDirectionRelativeToPoint(final Point3f point3f, final TransformableEntity transformableEntity, final TransformableEntity transformableEntity2) {
        final Point3f point3f3;
        final Point3f point3f2 = point3f3 = new Point3f(transformableEntity.getBasePoint3f());
        point3f3.x += transformableEntity.getXDimension();
        return getVerticalDirectionRelativeToPoint(point3f, point3f2, transformableEntity, transformableEntity2);
    }
    
    public static void appendInOrOut(final Point3f point3f, final ICDIntersection icdIntersection, final TransformableEntity obj, final Path2D.Float float1, final Line2D.Float float2, final Line2D.Float float3) {
        final Vector<IntersectionArmInterface> armVector = icdIntersection.getArmVector();
        for (final IntersectionArmInterface intersectionArmInterface : armVector) {
            if (intersectionArmInterface.getSegment().equals(obj)) {
                for (final IntersectionArmInterface intersectionArmInterface2 : armVector) {
                    final BasicIntersectionArm basicIntersectionArm = (BasicIntersectionArm)intersectionArmInterface;
                    final BasicIntersectionArm basicIntersectionArm2 = (BasicIntersectionArm)intersectionArmInterface2;
                    if (MathUtilities.isSameFloat((float)Math.toDegrees(icdIntersection.getRotationBetweenArms(basicIntersectionArm, basicIntersectionArm2)), 90.0f, 0.01f)) {
                        final int verticalDirectionRelativeToPoint = getVerticalDirectionRelativeToPoint(point3f, (TransformableEntity)basicIntersectionArm2.getSegment(), obj);
                        System.err.println("DIRECTION: " + ((verticalDirectionRelativeToPoint == ICDAssemblyElevationUtilities.INWARDS) ? "Inwards" : "Outwards"));
                        float1.append((verticalDirectionRelativeToPoint == ICDAssemblyElevationUtilities.INWARDS) ? float2 : float3, false);
                    }
                }
            }
        }
    }
    
    public static void appendInOrOut(final ICDJoint icdJoint, final ICDIntersection icdIntersection, final TransformableEntity transformableEntity, final Path2D.Float float1, final Line2D.Float s, final Line2D.Float s2) {
        final Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        matrix4f.rotZ(-icdIntersection.getRotationWorldSpace() + transformableEntity.getRotationWorldSpace());
        final Point3f point3f = new Point3f(0.0f, -2.0f, 0.0f);
        matrix4f.transform(point3f);
        icdIntersection.getEntWorldSpaceMatrix().transform(point3f);
        final Point3f point3f2 = new Point3f(0.0f, 2.0f, 0.0f);
        matrix4f.transform(point3f2);
        icdIntersection.getEntWorldSpaceMatrix().transform(point3f2);
        final Iterator<IntersectionArmInterface> iterator = icdIntersection.getArmVector().iterator();
        while (iterator.hasNext()) {
            final Segment segment = iterator.next().getSegment();
            if (segment instanceof ICDSegment) {
                if (segment.contains(point3f)) {
                    float1.append(s, false);
                }
                if (!segment.contains(point3f2)) {
                    continue;
                }
                float1.append(s2, false);
            }
        }
    }
    
    public static ICDJointDirections appendJointDirections(final ICDJoint icdJoint, final TransformableEntity transformableEntity) {
        ICDJointDirections icdJointDirections = null;
        if (icdJoint != null) {
            icdJointDirections = new ICDJointDirections(icdJoint, transformableEntity);
            final Point3f point3f = new Point3f(0.0f, -2.0f, 0.0f);
            final Point3f point3f2 = new Point3f(0.0f, 2.0f, 0.0f);
            final Point3f point3f3 = new Point3f(-2.0f, 0.0f, 0.0f);
            final Point3f point3f4 = new Point3f(2.0f, 0.0f, 0.0f);
            final Point3f point3f5 = new Point3f(0.0f, 0.0f, 2.0f);
            final Point3f point3f6 = new Point3f(0.0f, 0.0f, -2.0f);
            icdJoint.getEntWorldSpaceMatrix().transform(point3f);
            icdJoint.getEntWorldSpaceMatrix().transform(point3f2);
            icdJoint.getEntWorldSpaceMatrix().transform(point3f3);
            icdJoint.getEntWorldSpaceMatrix().transform(point3f4);
            icdJoint.getEntWorldSpaceMatrix().transform(point3f5);
            icdJoint.getEntWorldSpaceMatrix().transform(point3f6);
            for (final JointIntersectable jointIntersectable : getAllIntersectables(icdJoint)) {
                if (jointIntersectable != null) {
                    if (jointIntersectable.contains(point3f)) {
                        icdJointDirections.isIn = true;
                    }
                    if (jointIntersectable.contains(point3f2)) {
                        icdJointDirections.isOut = true;
                    }
                    if (jointIntersectable.contains(point3f3)) {
                        icdJointDirections.isLeft = true;
                    }
                    if (jointIntersectable.contains(point3f4)) {
                        icdJointDirections.isRight = true;
                    }
                    if (jointIntersectable.contains(point3f5)) {
                        icdJointDirections.isUp = true;
                    }
                    if (!jointIntersectable.contains(point3f6)) {
                        continue;
                    }
                    icdJointDirections.isDown = true;
                }
            }
        }
        return icdJointDirections;
    }
    
    public static Collection<JointIntersectable> getAllIntersectables(final ICDJoint icdJoint) {
        final Vector<JointIntersectable> vector = new Vector<JointIntersectable>();
        if (icdJoint != null) {
            if (icdJoint.isUnderIntersection()) {
                final ICDIntersection intersection = icdJoint.getIntersection();
                if (intersection != null) {
                    vector.addAll(intersection.getAllIntersectables());
                }
            }
            else if (icdJoint.isUnderPanelToPanelConnectionHW()) {
                final ICDPanelToPanelConnectionHW panelToPanelConnectionHW = icdJoint.getPanelToPanelConnectionHW();
                if (panelToPanelConnectionHW != null) {
                    vector.addAll(panelToPanelConnectionHW.getAllIntersectables());
                }
            }
            else {
                final ICDSegment icdSegment = icdJoint.getParent(ICDSegment.class);
                if (icdSegment != null) {
                    vector.addAll(icdSegment.getAllIntersectables());
                }
            }
        }
        return (Collection<JointIntersectable>)vector;
    }
    
    public static int getHorizontalDirectionRelativeToPoint(final Point3f point3f, final TransformableEntity transformableEntity, final TransformableEntity transformableEntity2) {
        final Point3f point3f3;
        final Point3f point3f2 = point3f3 = new Point3f(transformableEntity.getBasePoint3f());
        point3f3.x += transformableEntity.getXDimension();
        final Point3f convertPointToWorldSpace = transformableEntity.convertPointToWorldSpace(point3f2);
        final Point3f convertSpaces = MathUtilities.convertSpaces(point3f, transformableEntity2.getEntWorldSpaceMatrix());
        final Point3f convertSpaces2 = MathUtilities.convertSpaces(convertPointToWorldSpace, transformableEntity2.getEntWorldSpaceMatrix());
        final float y = convertSpaces.y;
        if (convertSpaces2.y > 0.0f) {
            if (y < 0.0f) {
                return ICDAssemblyElevationUtilities.OUTWARDS;
            }
            return ICDAssemblyElevationUtilities.INWARDS;
        }
        else {
            if (y > 0.0f) {
                return ICDAssemblyElevationUtilities.OUTWARDS;
            }
            return ICDAssemblyElevationUtilities.INWARDS;
        }
    }
    
    public static void appendLeftOrRight(final TransformableEntity transformableEntity, final TransformableEntity transformableEntity2, final ICDIntersection icdIntersection, final Path2D.Float float1, final Line2D.Float s, final Line2D.Float s2) {
        float n = 0.0f;
        if (transformableEntity instanceof ICDTopJoint) {
            n = -1.0f;
        }
        final Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        matrix4f.rotZ(-icdIntersection.getRotationWorldSpace() + transformableEntity2.getRotationWorldSpace());
        final Point3f point3f = new Point3f(-2.0f, 0.0f, n);
        matrix4f.transform(point3f);
        transformableEntity.getEntWorldSpaceMatrix().transform(point3f);
        final Point3f point3f2 = new Point3f(2.0f, 0.0f, n);
        matrix4f.transform(point3f2);
        transformableEntity.getEntWorldSpaceMatrix().transform(point3f2);
        final Iterator<IntersectionArmInterface> iterator = icdIntersection.getArmVector().iterator();
        while (iterator.hasNext()) {
            final Segment segment = iterator.next().getSegment();
            if (segment instanceof ICDSegment) {
                if (segment.contains(point3f)) {
                    float1.append(s, false);
                }
                if (!segment.contains(point3f2)) {
                    continue;
                }
                float1.append(s2, false);
            }
        }
    }
    
    public static boolean isJointOnChase(final ICDJoint icdJoint) {
        return icdJoint.getParent(ICDChaseMidConnectorContainer.class) != null;
    }
    
    public static boolean isExtrusionOnChase(final TransformableEntity transformableEntity) {
        return transformableEntity.getParent(ICDChaseMidConnectorContainer.class) != null;
    }
    
    public static void appendChaseInOrOut(final TransformableEntity transformableEntity, final Path2D.Float float1, final Line2D.Float float2, final Line2D.Float float3) {
        final ICDSubILine icdSubILine = (ICDSubILine)transformableEntity.getParent(ICDSubILine.class);
        if (icdSubILine != null) {
            final Point3f convertSpaces = MathUtilities.convertSpaces(new Point3f(transformableEntity.getBasePoint3f()), (EntityObject)transformableEntity, (EntityObject)icdSubILine);
            ICDSegment icdSegment = null;
            for (final Segment segment : icdSubILine.getSegments()) {
                if (MathUtilities.isSameFloat(convertSpaces.x, segment.getBasePoint3f().x, 0.1f)) {
                    icdSegment = (ICDSegment)segment;
                }
            }
            if (icdSegment != null) {
                final ICDPanel icdPanel = icdSegment.getChildrenByClass(ICDPanel.class, true).get(0);
                if (icdPanel != null) {
                    if (icdPanel.getCurrentOption().getId().equals("ICD_Panel_With_Chase_Side_A")) {
                        float1.append(float2, false);
                    }
                    else if (icdPanel.getCurrentOption().getId().equals("ICD_Panel_With_Chase_Side_B")) {
                        float1.append(float3, false);
                    }
                    else if (icdPanel.getCurrentOption().getId().equals("ICD_Panel_With_Chase_Both_Sides")) {
                        float1.append(float2, false);
                        float1.append(float3, false);
                    }
                }
            }
        }
    }
    
    public static float getVerticalSkewOffset(final float a) {
        return (float)(Math.abs(a) * Math.tan(Math.toRadians(45.0)));
    }
    
    public static float getAngularSkewOffset(final float a) {
        return (float)(Math.abs(a) / Math.cos(Math.toRadians(45.0)));
    }
    
    public static void hackRotationOutOfEntityWorldSpaceMatrixToFixAssemblyElevationRotation(final Matrix4f matrix4f) {
        matrix4f.m00 = -1.0f;
        matrix4f.m11 = -1.0f;
    }
    
    public static void appendChaseLeftOrRight(final TransformableEntity transformableEntity, final TransformableEntity transformableEntity2, final Path2D.Float float1, final Line2D.Float float2, final Line2D.Float float3) {
        final ICDSubILine icdSubILine = (ICDSubILine)transformableEntity2.getParent(ICDSubILine.class);
        if (icdSubILine != null) {
            final Point3f point3f = new Point3f(transformableEntity.getBasePoint3f());
            final Point3f convertSpaces = MathUtilities.convertSpaces(new Point3f(), (EntityObject)transformableEntity, (EntityObject)icdSubILine);
            for (final Segment segment : icdSubILine.getSegments()) {
                if (MathUtilities.isSameFloat(segment.getBasePoint3f().x, convertSpaces.x, 0.01f)) {
                    final ICDSegment icdSegment = (ICDSegment)segment;
                    appendChaseDirection(icdSegment, transformableEntity, float1, float3);
                    final ICDSegment icdSegment2 = (ICDSegment)icdSubILine.getSegmentBefore((SegmentBase)icdSegment);
                    if (icdSegment2 != null) {
                        appendChaseDirection(icdSegment2, transformableEntity, float1, float2);
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private static void appendChaseDirection(final ICDSegment icdSegment, final TransformableEntity transformableEntity, final Path2D.Float float1, final Line2D.Float s) {
        final List<ICDPanel> childrenByClass = icdSegment.getChildrenByClass(ICDPanel.class, true);
        if (childrenByClass.size() > 0) {
            final Point3f point3f = new Point3f(transformableEntity.getBasePoint3f());
            final ICDPanel icdPanel = childrenByClass.get(0);
            if (icdPanel.hasChaseOnPointSide(MathUtilities.convertSpaces(new Point3f(), (EntityObject)transformableEntity, (EntityObject)icdPanel))) {
                float1.append(s, false);
            }
        }
    }
    
    public static ICDILine getILineForEntity(final EntityObject entityObject) {
        ICDILine icdiLine = (ICDILine)entityObject.getParent(ICDILine.class);
        if (icdiLine == null) {
            final Vector<ILineInterface> wallSetsFromArms = ((ICDIntersection)entityObject.getParent(ICDIntersection.class)).getWallSetsFromArms();
            if (wallSetsFromArms.size() > 0) {
                icdiLine = (ICDILine) wallSetsFromArms.firstElement();
            }
        }
        return icdiLine;
    }
    
    public static boolean shouldDrawAssemblyShapeNodes(final TransformableEntity transformableEntity) {
        if (transformableEntity.getCurrentOption().getId().contains("None") || transformableEntity.isFakePart()) {
            return false;
        }
        if ("Yes".equals(transformableEntity.getAttributeValueAsString("specialInternalExtrusion"))) {
            return false;
        }
        final ICDPanel icdPanel = (ICDPanel)transformableEntity.getParent(ICDPanel.class);
        return icdPanel == null || !icdPanel.isSlopedPanel();
    }
    
    public static Ice2DGVTElevationNode createAssemblyElevationImageNode(final TransformableEntity transformableEntity, final Vector3f translation, final Vector3f vector3f) {
        Ice2DGVTElevationNode ice2DGVTElevationNode = null;
        final IceGVTObject assemblyElevationGVTObject = getAssemblyElevationGVTObject(transformableEntity);
        if (assemblyElevationGVTObject != null) {
            final Matrix4f matrix4f = new Matrix4f();
            matrix4f.setIdentity();
            matrix4f.rotX(vector3f.x);
            final Matrix4f matrix4f2 = new Matrix4f();
            matrix4f2.setIdentity();
            matrix4f2.rotY(vector3f.y);
            final Matrix4f matrix4f3 = new Matrix4f();
            matrix4f3.setIdentity();
            matrix4f3.rotZ(vector3f.z);
            final Matrix4f matrix4f4 = new Matrix4f();
            matrix4f4.setIdentity();
            matrix4f4.mul(transformableEntity.getEntWorldSpaceMatrix());
            final Matrix4f matrix4f5 = new Matrix4f();
            matrix4f5.setIdentity();
            matrix4f5.setTranslation(translation);
            matrix4f4.mul(matrix4f5);
            matrix4f4.mul(matrix4f);
            matrix4f4.mul(matrix4f2);
            matrix4f4.mul(matrix4f3);
            ice2DGVTElevationNode = new Ice2DGVTElevationNode(transformableEntity.getDefaultLayerName(), transformableEntity, matrix4f4, assemblyElevationGVTObject);
        }
        return ice2DGVTElevationNode;
    }
    
    public static IceGVTObject getAssemblyElevationGVTObject(final TransformableEntity transformableEntity) {
        IceGVTObject icePlanSvgGvtObject = null;
        final OptionObject currentOption = transformableEntity.getCurrentOption();
        if (currentOption != null) {
            final Attribute attributeObject = transformableEntity.getAttributeObject("Assembly_Elevation_SVG_File");
            final Attribute attributeObject2 = transformableEntity.getAttributeObject("Assembly_Elevation_SVG_Path");
            if (attributeObject != null && attributeObject2 != null) {
                icePlanSvgGvtObject = currentOption.getIcePlanSvgGvtObject(attributeObject2.getKey(), attributeObject.getKey());
            }
        }
        return icePlanSvgGvtObject;
    }
    
    public static Ice2DTextNode createAssemblyDimensionTextNode(final TransformableEntity transformableEntity, final Vector3f translation, final String s, final Vector3f vector3f) {
        final Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        matrix4f.rotX(vector3f.x);
        final Matrix4f matrix4f2 = new Matrix4f();
        matrix4f2.setIdentity();
        matrix4f2.rotY(vector3f.y);
        final Matrix4f matrix4f3 = new Matrix4f();
        matrix4f3.setIdentity();
        matrix4f3.rotZ(vector3f.z);
        final Matrix4f matrix4f4 = new Matrix4f();
        matrix4f4.setIdentity();
        matrix4f4.mul(transformableEntity.getEntWorldSpaceMatrix());
        final Matrix4f matrix4f5 = new Matrix4f();
        matrix4f5.setIdentity();
        matrix4f5.setTranslation(translation);
        matrix4f4.mul(matrix4f5);
        matrix4f4.mul(matrix4f);
        matrix4f4.mul(matrix4f2);
        matrix4f4.mul(matrix4f3);
        return new Ice2DTextNode(transformableEntity.getLayerName(), transformableEntity, matrix4f4, s, 3);
    }
    
    public static boolean shouldDrawElevation(final ElevationEntity elevationEntity, final AssemblyPaintable assemblyPaintable) {
        boolean b = false;
        if (assemblyPaintable.shouldDrawAssembly()) {
            if (elevationEntity instanceof ICDIsometricAssemblyElevationEntity) {
                b = true;
            }
            if (elevationEntity instanceof ICDAssemblyElevationEntity && assemblyPaintable.isAssembled()) {
                b = true;
            }
        }
        return b;
    }
    
    public static List<ICDVerticalChase> getVerticalChases(final Solution solution) {
        final List<ICDVerticalChase> list = null;
        final MiscItemsBucket miscItemsBucket = (MiscItemsBucket)solution.getChild(MiscItemsBucket.class, true);
        if (miscItemsBucket == null) {
            return list;
        }
        final ICDVerticalChaseGroup icdVerticalChaseGroup = (ICDVerticalChaseGroup)miscItemsBucket.getChild(ICDVerticalChaseGroup.class, true);
        if (icdVerticalChaseGroup == null) {
            return list;
        }
        return (List<ICDVerticalChase>)icdVerticalChaseGroup.getChildrenByClass(ICDVerticalChase.class, false, true);
    }
    
    public static ArrayList<ICDVerticalChase> getVerticalChasesCorrespondingToILines(final List<ICDVerticalChase> list, final ArrayList<ICDILine> list2) {
        if (list2 == null || list2.isEmpty() || list == null || list.isEmpty()) {
            return null;
        }
        final ArrayList<ICDVerticalChase> list3 = new ArrayList<ICDVerticalChase>();
        for (final ICDVerticalChase e : list) {
            final Vector<ICDILine> chaseILines = e.getChaseILines();
            if (chaseILines != null && list2.containsAll(chaseILines)) {
                list3.add(e);
            }
        }
        return list3;
    }
    
    public static String getAssemblyLineScript(final Point3f point3f, final String str, final String str2) {
        return "LI:SS(1:" + str + "(" + point3f.x + "," + point3f.y + "," + point3f.z + "):" + str2 + "(" + point3f.x + "," + point3f.y + "," + point3f.z + "))";
    }
    
    static {
        ICDAssemblyElevationUtilities.INWARDS = 0;
        ICDAssemblyElevationUtilities.OUTWARDS = 1;
        ICDAssemblyElevationUtilities.LEFT = 0;
        ICDAssemblyElevationUtilities.RIGHT = 1;
    }
}
