package com.iceedge.icd.reporting;

import net.iceedge.catalogs.icd.panel.ICDTile;
import net.iceedge.catalogs.icd.intersection.ICDCornerSlot;
import java.util.Vector;
import com.iceedge.icd.utilities.ICDExtrusionUtilities;
import java.text.DecimalFormat;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.utilities.TypeFilter;
import com.iceedge.icd.typefilters.ICDJointTypeFilter;
import net.iceedge.catalogs.icd.panel.ICDJoint;
import net.iceedge.icecore.basemodule.baseclasses.material.BasicMaterialEntity;
import net.dirtt.icelib.main.TypeValidatorEntity;
import net.dirtt.icelib.report.compare.CompareNode;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TypeableEntity;
import net.dirtt.icelib.report.ManufacturingReportable;
import java.util.TreeMap;

public class ICDManufacturingUtils
{
    public static void addManufacturingInfoToTreeMap(final TreeMap<String, String> treeMap, final ManufacturingReportable manufacturingReportable) {
        addBasicManufacturingInfoToTreeMap(treeMap, manufacturingReportable);
        treeMap.put("UserTag", manufacturingReportable.getUserTagNameAttribute("TagName1"));
    }
    
    public static void addBasicManufacturingInfoToTreeMap(final TreeMap<String, String> treeMap, final ManufacturingReportable manufacturingReportable) {
        if (manufacturingReportable.containsAttributeKey("Product_Type")) {
            treeMap.put("Type", manufacturingReportable.getAttributeValueAsString("Product_Type"));
        }
        if (manufacturingReportable.containsAttributeKey("SubType")) {
            treeMap.put("SubType", manufacturingReportable.getSubType());
        }
        treeMap.put("Description", manufacturingReportable.getDescriptionForManufacturingReport());
    }
    
    public static void addManufacturingInfoToTreeMapForExtrusion(final TreeMap<String, String> treeMap, final ManufacturingReportable manufacturingReportable, final float a) {
        addBasicManufacturingInfoToTreeMap(treeMap, manufacturingReportable);
        treeMap.put("Width", Math.round(a) + "");
        if (manufacturingReportable instanceof TypeableEntity) {
            final TypeableEntity typeableEntity = (TypeableEntity)manufacturingReportable;
            typeableEntity.handleManufacturingReportMaterials((TreeMap)treeMap);
            addAssembledAttributeIfNecessary(treeMap, (EntityObject)typeableEntity);
            treeMap.put("UserTag", typeableEntity.getUserTagNameAttribute("TagName1"));
        }
    }
    
    public static void populateCompareNodeForManufacturing(final Class clazz, final CompareNode compareNode, final TypeValidatorEntity typeValidatorEntity) {
        compareNode.addCompareValue("length", (Object)Math.round(typeValidatorEntity.getLength()));
        if (typeValidatorEntity.getChildByClass(BasicMaterialEntity.class) != null) {
            compareNode.addCompareValue("finish", (Object)((BasicMaterialEntity)typeValidatorEntity.getChildByClass(BasicMaterialEntity.class)).getDescription());
        }
        compareNode.addCompareValue("description", (Object)typeValidatorEntity.getDescription());
        final ICDJoint icdJoint = (ICDJoint)typeValidatorEntity.getParent((TypeFilter)new ICDJointTypeFilter());
        if (icdJoint != null) {
            if (icdJoint.containsAttributeKey("isAssembled")) {
                compareNode.addCompareValue("assembled", (Object)icdJoint.getAttributeValueAsString("isAssembled"));
            }
        }
        else if (typeValidatorEntity.containsAttributeKey("isAssembled")) {
            compareNode.addCompareValue("assembled", (Object)typeValidatorEntity.getAttributeValueAsString("isAssembled"));
        }
        compareNode.addCompareValue("usertag", (Object)typeValidatorEntity.getUserTagNameAttribute("TagName1"));
    }
    
    public static void addAssemblyInfoToManufacturingTreeMap(final TreeMap<String, String> treeMap, final EntityObject entityObject) {
        if (entityObject.getParentEntity() instanceof ICDJoint) {
            if (entityObject.getParentEntity().containsAttributeKey("isAssembled")) {
                addAssembledAttributeIfNecessary(treeMap, entityObject);
            }
        }
        else if (entityObject.containsAttributeKey("isAssembled")) {
            addAssembledAttributeIfNecessary(treeMap, entityObject);
        }
    }
    
    private static void addAssembledAttributeIfNecessary(final TreeMap<String, String> treeMap, final EntityObject entityObject) {
        if ("true".equals(entityObject.getAttributeValueAsString("isAssembled"))) {
            treeMap.put("Assembled", "yes");
        }
    }
    
    public static void addDimensionsToManufacturingTreeMap(final TreeMap<String, String> treeMap, final TransformableEntity transformableEntity) {
        treeMap.put("Depth", transformableEntity.getDepth() + "");
        final DecimalFormat decimalFormat = new DecimalFormat("#.##");
        treeMap.put("Width", Double.valueOf(decimalFormat.format(transformableEntity.getWidth())) + "");
        treeMap.put("Height", Double.valueOf(decimalFormat.format(transformableEntity.getHeight())) + "");
    }
    
    public static String getReportDescriptionForExtrusion(final EntityObject entityObject) {
        final Vector<ICDCornerSlot> allSlottedSlots = ICDExtrusionUtilities.getAllSlottedSlots(entityObject);
        if (allSlottedSlots != null && allSlottedSlots.size() > 0) {
            return " CS" + allSlottedSlots.size() + " ";
        }
        final String tabCode = ICDExtrusionUtilities.getTabCode(entityObject);
        if (tabCode.length() > 0) {
            return tabCode;
        }
        return "";
    }
    
    public static String getProductType(final TypeableEntity typeableEntity) {
        if (typeableEntity.getAttributeValueAsString("Product_Type") != null) {
            String s = typeableEntity.getAttributeValueAsString("Product_Type");
            if (typeableEntity instanceof ICDTile) {
                final ICDTile icdTile = (ICDTile)typeableEntity;
                if (icdTile.isStackedTile()) {
                    s = icdTile.getTypeForQuote();
                }
            }
            return s;
        }
        return "";
    }
    
    public static String getProductSubType(final TypeableEntity typeableEntity) {
        String s = typeableEntity.getSubType();
        if (typeableEntity instanceof ICDTile) {
            final ICDTile icdTile = (ICDTile)typeableEntity;
            if (icdTile.isStackedTile()) {
                s = icdTile.getSubTypeForQuote();
            }
        }
        return s;
    }
}
