// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd;

import net.dirtt.utilities.TypeFilter;
import javax.vecmath.Vector3f;
import javax.vecmath.Matrix4f;
import net.dirtt.utilities.EntitySpaceCompareNode;
import net.iceedge.icecore.basemodule.interfaces.AssembleParent;
import net.dirtt.icelib.report.compare.CompareNode;
import net.dirtt.icelib.report.Report;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportNode;
import net.dirtt.IceApp;
import net.iceedge.icecore.basemodule.baseclasses.BasicSegment;
import net.iceedge.catalogs.icd.panel.ICDInnerExtrusionSet;
import net.iceedge.catalogs.icd.panel.ICDTopExtrusion;
import net.iceedge.catalogs.icd.panel.ICDBottomExtrusion;
import java.util.Iterator;
import net.dirtt.icelib.main.EntityObject;
import java.util.ArrayList;
import net.dirtt.utilities.Pair;
import java.util.List;
import net.iceedge.catalogs.icd.panel.ICDHorizontalBreakableExtrusion;
import net.dirtt.utilities.MathUtilities;
import net.iceedge.icecore.icecad.ice.tree.IceCadCompositeBlock;
import net.iceedge.icecore.icecad.ice.tree.IceCadSymbolTableRecord;
import net.iceedge.icecore.icecad.ice.tree.CadLayer;
import net.iceedge.icecore.icecad.ice.tree.IceCadMTextNode;
import net.iceedge.icecore.icecad.ice.tree.IceCadNodeContainer;
import net.iceedge.icecore.basemodule.interfaces.lightweight.CADPaintable;
import javax.vecmath.Point3f;
import net.dirtt.icecad.cadtree.ICadTreeNode;
import net.dirtt.icecad.cadtree.ICadTextNode;
import net.dirtt.icelib.main.TransformableEntity;
import org.apache.log4j.Logger;

public class ICDUtilities
{
    public static Logger logger;
    public static final float FONT_SIZE = 2.0f;
    
    public static ICadTextNode drawCadText(final TransformableEntity transformableEntity, ICadTextNode cadTextNode, final ICadTreeNode cadTreeNode, final String text, final Point3f insertionPoint, final int n, final float rotation) {
        if (text != null) {
            if (cadTextNode == null) {
                cadTextNode = new ICadTextNode((CADPaintable)transformableEntity, text, insertionPoint, 2.0f, rotation, n);
                cadTextNode.setScheduledAction(0);
                if (cadTreeNode != null) {
                    cadTreeNode.addChild((ICadTreeNode)cadTextNode);
                }
            }
            else {
                cadTextNode.setText(text);
                cadTextNode.setInsertionPoint(insertionPoint);
                cadTextNode.setRotation(rotation);
                cadTextNode.setScheduledAction(2);
            }
        }
        return cadTextNode;
    }
    
    public static ICadTextNode drawCadText(final TransformableEntity transformableEntity, final ICadTextNode cadTextNode, final ICadTreeNode cadTreeNode, final String s, final Point3f point3f, final int n) {
        return drawCadText(transformableEntity, cadTextNode, cadTreeNode, s, point3f, n, transformableEntity.getRotationWorldSpace());
    }
    
    public static IceCadMTextNode drawIceCadTextDotNet(final IceCadNodeContainer iceCadNodeContainer, IceCadMTextNode iceCadMTextNode, final String text, final Point3f position, final float rotation) {
        if (text != null) {
            if (iceCadMTextNode == null) {
                iceCadMTextNode = new IceCadMTextNode(iceCadNodeContainer, (IceCadSymbolTableRecord)CadLayer.LAYER_TAG.getLayer(), text, 2.0f, rotation, position, 0.0f);
            }
            else {
                iceCadMTextNode.setText(text);
                iceCadMTextNode.setPosition(position);
                iceCadMTextNode.setRotation(rotation);
            }
        }
        return iceCadMTextNode;
    }
    
    public static IceCadMTextNode drawIceCadTextForProxyEntityDotNet(final IceCadNodeContainer iceCadNodeContainer, IceCadMTextNode iceCadMTextNode, final TransformableEntity transformableEntity, final IceCadCompositeBlock iceCadCompositeBlock, final String text, final Point3f position, final float rotation) {
        if (text != null) {
            MathUtilities.point3fSubtract(position, transformableEntity.getBasePoint3f());
            if (iceCadMTextNode == null) {
                iceCadMTextNode = new IceCadMTextNode(iceCadNodeContainer, (IceCadSymbolTableRecord)iceCadCompositeBlock, text, 2.0f, rotation, position, 0.0f);
            }
            else {
                iceCadMTextNode.setText(text);
                iceCadMTextNode.setPosition(position);
                iceCadMTextNode.setRotation(rotation);
            }
        }
        return iceCadMTextNode;
    }
    
    public static void validateBeamBreakLocationForExtrusion(final ICDHorizontalBreakableExtrusion icdHorizontalBreakableExtrusion) {
        final List<Pair<Float, Integer>> calculateBeamBreakLocations = calculateBeamBreakLocations(icdHorizontalBreakableExtrusion);
        final List<Float> calculateRemovedLocations = calculateRemovedLocations(icdHorizontalBreakableExtrusion.getBreakLocationsForBeam(), calculateBeamBreakLocations);
        if (calculateBeamBreakLocations.size() > 0 || calculateRemovedLocations.size() > 0) {
            processBreakLocationAttributeForExtrusion(icdHorizontalBreakableExtrusion, calculateCurrentBreakLocations(gatherExistBreakLocations(icdHorizontalBreakableExtrusion), calculateBeamBreakLocations, calculateRemovedLocations));
        }
        icdHorizontalBreakableExtrusion.SetBreakLocationForBeam(calculateBeamBreakLocations);
    }
    
    public static void applyBeamBreakLocationForExtrusion(final ICDHorizontalBreakableExtrusion icdHorizontalBreakableExtrusion) {
        final List<Pair<Float, Integer>> breakLocationsForBeam = icdHorizontalBreakableExtrusion.getBreakLocationsForBeam();
        if (breakLocationsForBeam.size() > 0) {
            processBreakLocationAttributeForExtrusion(icdHorizontalBreakableExtrusion, calculateCurrentBreakLocations(gatherExistBreakLocations(icdHorizontalBreakableExtrusion), breakLocationsForBeam, new ArrayList<Float>()));
        }
    }
    
    public static List<Pair<Float, Integer>> calculateBeamBreakLocations(final ICDHorizontalBreakableExtrusion icdHorizontalBreakableExtrusion) {
        final TransformableEntity transformableEntity = (TransformableEntity)icdHorizontalBreakableExtrusion;
        final ICDSegment icdSegment = (ICDSegment)transformableEntity.getParent(ICDSegment.class);
        final ArrayList<Pair<Float, Integer>> list = new ArrayList<Pair<Float, Integer>>();
        if (icdSegment != null) {
            final Point3f convertSpaces = MathUtilities.convertSpaces(new Point3f(0.0f, 0.0f, 0.0f), (EntityObject)transformableEntity, (EntityObject)icdSegment);
            for (final Pair<Point3f, Integer> pair : icdSegment.getJointLocationForBeam()) {
                if (MathUtilities.isSameFloat(convertSpaces.z, ((Point3f)pair.first).z, 1.4f)) {
                    list.add(new Pair((Object)(((Point3f)pair.first).x - transformableEntity.getBasePoint3f().x), pair.second));
                }
            }
        }
        return (List<Pair<Float, Integer>>)list;
    }
    
    public static List<Float> calculateRemovedLocations(final List<Pair<Float, Integer>> list, final List<Pair<Float, Integer>> list2) {
        final ArrayList<Float> list3 = new ArrayList<Float>();
        for (final Pair<Float, Integer> pair : list) {
            boolean b = false;
            final Iterator<Pair<Float, Integer>> iterator2 = list2.iterator();
            while (iterator2.hasNext()) {
                if (MathUtilities.isSameFloat((float)iterator2.next().first, (float)pair.first, 0.1f)) {
                    b = true;
                }
            }
            if (!b) {
                list3.add((Float)pair.first);
            }
        }
        return list3;
    }
    
    public static List<Float> gatherExistBreakLocations(final ICDHorizontalBreakableExtrusion icdHorizontalBreakableExtrusion) {
        final TransformableEntity transformableEntity = (TransformableEntity)icdHorizontalBreakableExtrusion;
        final ArrayList<Float> list = new ArrayList<Float>();
        Object internalExtrusion = null;
        if (icdHorizontalBreakableExtrusion instanceof ICDBottomExtrusion || icdHorizontalBreakableExtrusion instanceof ICDTopExtrusion) {
            internalExtrusion = transformableEntity;
        }
        else if (icdHorizontalBreakableExtrusion instanceof ICDInnerExtrusionSet) {
            internalExtrusion = ((ICDInnerExtrusionSet)icdHorizontalBreakableExtrusion).getInternalExtrusion(0);
        }
        if (internalExtrusion != null) {
            if ("No".equals(((TransformableEntity)internalExtrusion).getCurrentOption().getAttributeValueAsString("specialInternalExtrusion"))) {
                return list;
            }
            final int attributeValueAsInt = ((TransformableEntity)internalExtrusion).getAttributeValueAsInt("NumberOfJoints");
            if (attributeValueAsInt == 1) {
                final float attributeValueAsFloat = ((TransformableEntity)internalExtrusion).getAttributeValueAsFloat("breakLocation");
                if (attributeValueAsFloat > 0.01f) {
                    list.add(attributeValueAsFloat);
                }
            }
            else if (attributeValueAsInt == 2) {
                final float attributeValueAsFloat2 = ((TransformableEntity)internalExtrusion).getAttributeValueAsFloat("breakLocation");
                if (attributeValueAsFloat2 > 0.01f) {
                    list.add(attributeValueAsFloat2);
                }
                final float attributeValueAsFloat3 = ((TransformableEntity)internalExtrusion).getAttributeValueAsFloat("breakLocation2");
                if (attributeValueAsFloat3 > 0.01f) {
                    list.add(attributeValueAsFloat3);
                }
            }
        }
        return list;
    }
    
    public static List<Float> calculateCurrentBreakLocations(final List<Float> list, final List<Pair<Float, Integer>> list2, final List<Float> list3) {
        for (final Pair<Float, Integer> pair : list2) {
            boolean b = false;
            final Iterator<Float> iterator2 = list.iterator();
            while (iterator2.hasNext()) {
                if (MathUtilities.isSameFloat((float)pair.first, (float)iterator2.next(), 0.1f)) {
                    b = true;
                }
            }
            if (!b) {
                list.add((Float)pair.first);
            }
        }
        for (final Float n : list3) {
            int n2 = -1;
            for (int i = 0; i < list.size(); ++i) {
                if (MathUtilities.isSameFloat((float)n, (float)list.get(i), 0.1f)) {
                    n2 = i;
                }
            }
            if (n2 > -1) {
                list.remove(n2);
            }
        }
        return list;
    }
    
    public static void processBreakLocationAttributeForExtrusion(final ICDHorizontalBreakableExtrusion icdHorizontalBreakableExtrusion, final List<Float> list) {
        final TransformableEntity targetExtrusion = getTargetExtrusion(icdHorizontalBreakableExtrusion);
        if (targetExtrusion != null) {
            switch (list.size()) {
                case 0: {
                    targetExtrusion.createNewAttribute("specialInternalExtrusion", "No");
                    break;
                }
                case 1: {
                    targetExtrusion.createNewAttribute("specialInternalExtrusion", "Yes");
                    targetExtrusion.createNewAttribute("NumberOfJoints", "1");
                    targetExtrusion.createNewAttribute("breakLocation", list.get(0) + "");
                    break;
                }
                case 2: {
                    targetExtrusion.createNewAttribute("specialInternalExtrusion", "Yes");
                    targetExtrusion.createNewAttribute("NumberOfJoints", "2");
                    targetExtrusion.createNewAttribute("breakLocation", list.get(0) + "");
                    targetExtrusion.createNewAttribute("breakLocation2", list.get(1) + "");
                    break;
                }
            }
        }
    }
    
    public static String calculateExtrusionTypesForSingleChase(final TransformableEntity transformableEntity, final float n, final float n2, final float n3, final BasicSegment basicSegment) {
        final boolean flipped = basicSegment.isFlipped();
        String s;
        if (n2 < n3 && n > n2) {
            if (flipped) {
                transformableEntity.createNewAttribute("breakLocation", n + "");
                s = "chase regular regular";
            }
            else {
                transformableEntity.createNewAttribute("breakLocation", transformableEntity.getZDimension() - n3 + "");
                s = "regular chase regular";
            }
        }
        else if (flipped) {
            transformableEntity.createNewAttribute("breakLocation", n + "");
            s = "regular chase regular";
        }
        else {
            transformableEntity.createNewAttribute("breakLocation", n2 - 1.0f + "");
            s = "chase regular regular";
        }
        return s;
    }
    
    public static String calculateExtrusionTypesForDoubleChase(final TransformableEntity transformableEntity, final float n, final float n2) {
        transformableEntity.createNewAttribute("breakLocation", n - 1.0f + "");
        transformableEntity.createNewAttribute("breakLocation2", transformableEntity.getZDimension() - n2 + "");
        return "chase regular chase";
    }
    
    public static TransformableEntity getTargetExtrusion(final ICDHorizontalBreakableExtrusion icdHorizontalBreakableExtrusion) {
        if (icdHorizontalBreakableExtrusion instanceof ICDBottomExtrusion || icdHorizontalBreakableExtrusion instanceof ICDTopExtrusion) {
            return (TransformableEntity)icdHorizontalBreakableExtrusion;
        }
        if (icdHorizontalBreakableExtrusion instanceof ICDInnerExtrusionSet) {
            return (TransformableEntity)((ICDInnerExtrusionSet)icdHorizontalBreakableExtrusion).getInternalExtrusion(0);
        }
        return null;
    }
    
    public static String getTags(final EntityObject entityObject) {
        if (IceApp.getCurrentSolution().getSolutionSetting().isShowICDPreassembledTag()) {
            final Solution solution = entityObject.getSolution();
            if (solution != null) {
                final Report report = solution.getReport(51);
                if (report != null) {
                    final ICDManufacturingReportNode icdManufacturingReportNode = (ICDManufacturingReportNode)entityObject.getBucketInReport(report);
                    if (icdManufacturingReportNode != null) {
                        return icdManufacturingReportNode.getTag();
                    }
                }
            }
        }
        return null;
    }
    
    public static boolean populateCompareNode(final Class clazz, final CompareNode compareNode, final AssembleParent assembleParent) {
        return populateCompareNode(clazz, compareNode, assembleParent, true);
    }
    
    public static boolean populateCompareNode(final Class obj, final CompareNode compareNode, final AssembleParent assembleParent, final boolean b) {
        boolean booleanValue = false;
        try {
            booleanValue = (boolean)obj.getDeclaredField("populateNodesForPreassembled").get(obj);
        }
        catch (IllegalArgumentException ex) {
            ICDUtilities.logger.error((Object)ex.getMessage(), (Throwable)ex);
            ex.printStackTrace();
        }
        catch (SecurityException ex2) {
            ICDUtilities.logger.error((Object)ex2.getMessage(), (Throwable)ex2);
            ex2.printStackTrace();
        }
        catch (IllegalAccessException ex3) {
            ICDUtilities.logger.error((Object)ex3.getMessage(), (Throwable)ex3);
            ex3.printStackTrace();
        }
        catch (NoSuchFieldException ex4) {
            ICDUtilities.logger.error((Object)ex4.getMessage(), (Throwable)ex4);
            ex4.printStackTrace();
        }
        if (booleanValue) {
            final TransformableEntity transformableEntity = (TransformableEntity)assembleParent;
            if (b) {
                compareNode.addCompareValue("spaceCompare", (Object)new EntitySpaceCompareNode(assembleParent.getSpaceCompareNodeWrappers(), transformableEntity.getEntWorldSpaceMatrix(), getAssemblyCompareFlipMatrix(transformableEntity)));
            }
            else {
                booleanValue = false;
            }
            final String userTagNameAttribute = transformableEntity.getUserTagNameAttribute("TagName1");
            if (!"".equals(userTagNameAttribute) && userTagNameAttribute != null) {
                compareNode.addCompareValue("usertag", (Object)transformableEntity.getUserTagNameAttribute("TagName1"));
            }
        }
        return booleanValue;
    }
    
    private static Matrix4f getAssemblyCompareFlipMatrix(final TransformableEntity transformableEntity) {
        final Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        matrix4f.setTranslation(new Vector3f(transformableEntity.getXDimension(), 0.0f, 0.0f));
        final Matrix4f matrix4f2 = new Matrix4f();
        matrix4f2.setIdentity();
        matrix4f2.rotZ((float)Math.toRadians(180.0));
        matrix4f.mul(matrix4f2);
        return matrix4f;
    }
    
    public static void handleAttributeChange(final EntityObject entityObject, final String s, final String s2) {
        if (s.equalsIgnoreCase("TagName1")) {
            final Iterator<EntityObject> iterator = entityObject.getChildrenByClass(EntityObject.class, true, true).iterator();
            while (iterator.hasNext()) {
                iterator.next().applyChangesForAttribute(s, s2);
            }
        }
    }
    
    public static boolean doesTheArrayContain(final Class<EntityObject>[] array, final Class clazz) {
        boolean b = false;
        if (array != null) {
            for (int i = 0; i < array.length; ++i) {
                if (array[i].isAssignableFrom(clazz)) {
                    b = true;
                    break;
                }
            }
        }
        return b;
    }
    
    public static void validateShowInManufacturingReport(final TransformableEntity transformableEntity) {
        if (shouldAssemble(transformableEntity)) {
            transformableEntity.applyChangesForAttribute("ShowInManufacturingReport", "0");
        }
        else {
            transformableEntity.applyChangesForAttribute("ShowInManufacturingReport", "1.0");
        }
    }
    
    private static boolean shouldAssemble(final TransformableEntity transformableEntity) {
        final ICDILine icdiLine = (ICDILine)transformableEntity.getParent((TypeFilter)new ICDILineFilter());
        final ICDSegment icdSegment = (ICDSegment)transformableEntity.getParent((TypeFilter)new ICDSegmentFilter());
        final boolean attributeValueAsBoolean = icdSegment.getAttributeValueAsBoolean("isAssembled", false);
        if (icdiLine == null || icdSegment == null) {
            return false;
        }
        if (icdiLine.getAttributeValueAsBoolean("shouldAssemble", false)) {
            return !icdSegment.getAttributeValueAsBoolean("ICD_ExcludeFromIlineAssembly", false) || attributeValueAsBoolean;
        }
        return attributeValueAsBoolean;
    }
    
    static {
        ICDUtilities.logger = Logger.getLogger(ICDUtilities.class);
    }
}
