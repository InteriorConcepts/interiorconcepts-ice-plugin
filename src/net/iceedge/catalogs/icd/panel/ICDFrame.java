// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.main.TypeableEntity;
import net.iceedge.catalogs.icd.ICDSegmentFilter;
import net.iceedge.catalogs.icd.ICDSegment;
import net.dirtt.utilities.TypeFilter;
import net.dirtt.utilities.SortedCollection;
import net.iceedge.icecore.basemodule.interfaces.panels.InnerExtrusionSetInterface;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicInternalExtrusion;
import java.util.Vector;
import net.iceedge.catalogs.icd.ICDUtilities;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicExtrusion;
import net.dirtt.utilities.Pair;
import java.util.Iterator;
import net.iceedge.icecore.basemodule.interfaces.panels.ExtrusionGroupInterface;
import net.iceedge.icecore.basemodule.interfaces.panels.ExtrusionInterface;
import java.util.HashSet;
import java.util.ArrayList;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.main.EntityObject;
import java.util.List;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicFrame;

public class ICDFrame extends BasicFrame implements ICDManufacturingReportable
{
    private List<EntityObject> existingPartList;
    
    public ICDFrame(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.existingPartList = new ArrayList<EntityObject>();
    }
    
    public Object clone() {
        return this.buildClone(new ICDFrame(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDFrame buildClone(final ICDFrame icdFrame) {
        super.buildClone((BasicFrame)icdFrame);
        return icdFrame;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDFrame(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDFrame buildFrameClone(final ICDFrame icdFrame, final EntityObject entityObject) {
        super.buildFrameClone((BasicFrame)icdFrame, entityObject);
        return icdFrame;
    }
    
    public boolean isSizeValid() {
        return true;
    }
    
    public void collectExtraIndirectAssemblyParts(final boolean b, final HashSet<EntityObject> set, final boolean b2, final boolean b3, final boolean b4, final Class<EntityObject>... array) {
        final ExtrusionGroupInterface extrusionGroup = this.getExtrusionGroup();
        if (extrusionGroup != null) {
            this.existingPartList.clear();
            final Iterator<EntityObject> iterator = set.iterator();
            while (iterator.hasNext()) {
                this.existingPartList.add(iterator.next());
            }
            final Pair<Boolean, Boolean> collectPartsForHorizontalExtrusion = this.collectPartsForHorizontalExtrusion(b, set, extrusionGroup, b3, b4, array);
            (boolean)collectPartsForHorizontalExtrusion.first;
            if (b4) {
                this.collectPartsForVerticalExtrusion(b, (boolean)collectPartsForHorizontalExtrusion.second, set, extrusionGroup, b2, b3, b4, array);
            }
        }
        if (this.getBottomExtrusion() != null) {
            this.collectExtraIndirectAssemblyParts(b, false, false, false, set, (ExtrusionInterface)this.getBottomExtrusion(), b3, b4, array);
        }
        if (this.getTopExtrusion() != null) {
            this.collectExtraIndirectAssemblyParts(b, false, false, false, set, (ExtrusionInterface)this.getTopExtrusion(), b3, b4, array);
        }
        if (extrusionGroup != null && b3) {
            this.collectVerticalExtrusionForChase(b, extrusionGroup, set, array);
        }
    }
    
    private void collectVerticalExtrusionForChase(final boolean b, final ExtrusionGroupInterface extrusionGroupInterface, final HashSet<EntityObject> set, final Class<EntityObject>... array) {
        float x = -1.0f;
        for (final EntityObject entityObject : set) {
            if (!this.existingPartList.contains(entityObject)) {
                if (entityObject instanceof ICDInternalExtrusion) {
                    if (((ICDInternalExtrusion)entityObject).isVertical()) {
                        continue;
                    }
                    if (b) {
                        final float n = entityObject.getBasePoint3f().x + ((ICDInternalExtrusion)entityObject).getZDimension();
                        if (x >= 0.0f && x >= n) {
                            continue;
                        }
                        x = n;
                    }
                    else {
                        if (x >= 0.0f && x <= entityObject.getBasePoint3f().x) {
                            continue;
                        }
                        x = entityObject.getBasePoint3f().x;
                    }
                }
                else {
                    if (!(entityObject instanceof ICDSubInternalExtrusion) || !(entityObject.getParentEntity() instanceof BasicExtrusion)) {
                        continue;
                    }
                    final BasicExtrusion basicExtrusion = (BasicExtrusion)entityObject.getParentEntity();
                    final ICDSubInternalExtrusion icdSubInternalExtrusion = (ICDSubInternalExtrusion)entityObject;
                    if (basicExtrusion.isVertical()) {
                        continue;
                    }
                    if (b) {
                        final float n2 = basicExtrusion.getBasePoint3f().x + icdSubInternalExtrusion.getBasePoint3f().z + icdSubInternalExtrusion.getZDimension();
                        if (x >= 0.0f && x >= n2) {
                            continue;
                        }
                        x = n2;
                    }
                    else {
                        final float n3 = basicExtrusion.getBasePoint3f().x + icdSubInternalExtrusion.getBasePoint3f().z;
                        if (x >= 0.0f && x <= n3) {
                            continue;
                        }
                        x = n3;
                    }
                }
            }
        }
        if (x > -1.0f) {
            this.addVerticalExtrusionForChase(b, x, extrusionGroupInterface, set, array);
        }
        this.existingPartList.clear();
    }
    
    private void addVerticalExtrusionForChase(final boolean b, final float n, final ExtrusionGroupInterface extrusionGroupInterface, final HashSet<EntityObject> set, final Class<EntityObject>... array) {
        final Vector allVerticalExtrusions = extrusionGroupInterface.getAllVerticalExtrusions();
        final boolean doesTheArrayContain = ICDUtilities.doesTheArrayContain(array, BasicExtrusion.class);
        final boolean doesTheArrayContain2 = ICDUtilities.doesTheArrayContain(array, ICDJoint.class);
        if (doesTheArrayContain) {
            for (final ExtrusionInterface extrusionInterface : allVerticalExtrusions) {
                if (extrusionInterface instanceof ICDInnerExtrusionSet) {
                    final BasicInternalExtrusion internalExtrusion = ((ICDInnerExtrusionSet)extrusionInterface).getInternalExtrusion(0);
                    boolean b2 = false;
                    if (internalExtrusion != null && internalExtrusion.containsAttributeKey("isAssembled")) {
                        if (b) {
                            if (n + 2.0f > extrusionInterface.getBasePoint3f().x) {
                                set.add((EntityObject)internalExtrusion);
                            }
                            b2 = true;
                        }
                        else {
                            if (n - 2.0f < extrusionInterface.getBasePoint3f().x) {
                                set.add((EntityObject)internalExtrusion);
                            }
                            b2 = true;
                        }
                    }
                    if (!b2) {
                        continue;
                    }
                    final Iterator children = internalExtrusion.getChildren();
                    while (children.hasNext()) {
                        final ICDJoint next = children.next();
                        if (next instanceof ICDJoint && doesTheArrayContain2 && next.containsAttributeKey("isAssembled")) {
                            set.add((EntityObject)next);
                        }
                        if (next instanceof ICDSubInternalExtrusion && ((ICDSubInternalExtrusion)next).containsAttributeKey("isAssembled")) {
                            set.add((EntityObject)next);
                        }
                    }
                }
            }
        }
    }
    
    private Pair<Boolean, Boolean> collectPartsForHorizontalExtrusion(final boolean b, final HashSet<EntityObject> set, final ExtrusionGroupInterface extrusionGroupInterface, final boolean b2, final boolean b3, final Class<EntityObject>... array) {
        final Pair pair = new Pair((Object)true, (Object)true);
        final Vector allHorizontalExtrusions = extrusionGroupInterface.getAllHorizontalExtrusions(0);
        Object o2;
        if (b) {
            final Object o = this.getStartExtrusion();
            o2 = this.getEndExtrusion();
        }
        else {
            o2 = this.getStartExtrusion();
            final Object o = this.getEndExtrusion();
        }
        for (final ExtrusionInterface extrusionInterface : allHorizontalExtrusions) {
            final Object o;
            if (extrusionInterface instanceof InnerExtrusionSetInterface && ((InnerExtrusionSetInterface)extrusionInterface).isReferenceTo(b, (ExtrusionInterface)o)) {
                if (!((InnerExtrusionSetInterface)extrusionInterface).isReferenceTo(!b, (ExtrusionInterface)o2)) {
                    pair.second = false;
                    this.collectExtraIndirectAssemblyParts(b, true, true, true, set, extrusionInterface, b2, b3, array);
                }
                else {
                    pair.first = false;
                    this.collectExtraIndirectAssemblyParts(b, true, false, false, set, extrusionInterface, b2, b3, array);
                }
            }
        }
        return (Pair<Boolean, Boolean>)pair;
    }
    
    private void collectPartsForVerticalExtrusion(final boolean b, final boolean b2, final HashSet<EntityObject> set, final ExtrusionGroupInterface extrusionGroupInterface, final boolean b3, final boolean b4, final boolean b5, final Class<EntityObject>... array) {
        final SortedCollection collection = new SortedCollection();
        for (final ExtrusionInterface extrusionInterface : extrusionGroupInterface.getAllVerticalExtrusions()) {
            collection.add((Object)extrusionInterface.getBasePoint3f().x, (Object)extrusionInterface);
        }
        ExtrusionInterface extrusionInterface2;
        if (b) {
            extrusionInterface2 = (ExtrusionInterface)collection.getFirstValue();
        }
        else {
            extrusionInterface2 = (ExtrusionInterface)collection.getLastValue();
        }
        if (extrusionInterface2 != null && ((ICDInnerExtrusionSet)extrusionInterface2).getParent((TypeFilter)new ICDPanelFilter()) != null && this.collectExtraIndirectAssemblyParts(b, true, true, b2 || b3, set, extrusionInterface2, b4, b5, array)) {
            final ICDSegment icdSegment = (ICDSegment)((EntityObject)extrusionInterface2).getParent((TypeFilter)new ICDSegmentFilter());
            if (icdSegment != null) {
                float n = 0.0f;
                float n2 = extrusionInterface2.getBasePoint3f().x;
                if (!b) {
                    n = n2;
                    n2 = this.getXDimension();
                }
                icdSegment.setExtraHorizontalExtrusionSearchScope(n, n2);
            }
        }
    }
    
    private boolean collectExtraIndirectAssemblyParts(final boolean b, final boolean b2, final boolean b3, final boolean b4, final HashSet<EntityObject> set, ExtrusionInterface internalExtrusion, final boolean b5, final boolean b6, final Class<EntityObject>... array) {
        boolean b7 = false;
        if (internalExtrusion instanceof InnerExtrusionSetInterface && b2) {
            internalExtrusion = (ExtrusionInterface)((InnerExtrusionSetInterface)internalExtrusion).getInternalExtrusion(0);
        }
        if (b3) {
            for (final EntityObject e : internalExtrusion.getChildrenByClass((Class)EntityObject.class, false, true)) {
                if (e.containsAttributeKey("isAssembled") && ICDUtilities.doesTheArrayContain(array, e.getClass())) {
                    set.add(e);
                }
            }
            if (b4 && internalExtrusion.containsAttributeKey("isAssembled") && ICDUtilities.doesTheArrayContain(array, internalExtrusion.getClass())) {
                set.add((EntityObject)internalExtrusion);
                b7 = true;
            }
        }
        else if (b) {
            this.addAssemblyTubeAndJoint(set, internalExtrusion, "ICD_Sub1_InnerExtrusion_Type", "ICD_Special_Joint_Type", b5, b6, array);
            if (((EntityObject)internalExtrusion).getAttributeValueAsInt("NumberOfJoints", 1) == 2) {
                this.addAssemblyTubeAndJoint(set, internalExtrusion, "ICD_Sub2_InnerExtrusion_Type", "ICD_Special_Joint2_Type", b5, b6, array);
            }
        }
        else if (((EntityObject)internalExtrusion).getAttributeValueAsInt("NumberOfJoints", 1) == 2) {
            this.addAssemblyTubeAndJoint(set, internalExtrusion, "ICD_Sub3_InnerExtrusion_Type", "ICD_Special_Joint2_Type", b5, b6, array);
        }
        else {
            this.addAssemblyTubeAndJoint(set, internalExtrusion, "ICD_Sub2_InnerExtrusion_Type", "ICD_Special_Joint_Type", b5, b6, array);
        }
        return b7;
    }
    
    private void addAssemblyTubeAndJoint(final HashSet<EntityObject> set, final ExtrusionInterface extrusionInterface, final String s, final String s2, final boolean b, final boolean b2, final Class<EntityObject>... array) {
        if (ICDUtilities.doesTheArrayContain(array, BasicExtrusion.class)) {
            this.addAssemblyPart(set, extrusionInterface, b, b2, s);
        }
        if (ICDUtilities.doesTheArrayContain(array, ICDJoint.class)) {
            this.addAssemblyPart(set, extrusionInterface, b, b2, s2);
        }
    }
    
    private void addAssemblyPart(final HashSet<EntityObject> set, final ExtrusionInterface extrusionInterface, final boolean b, final boolean b2, final String... array) {
        if (extrusionInterface != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                final EntityObject childByLWType = ((EntityObject)extrusionInterface).getChildByLWType(array[i]);
                if (childByLWType != null && childByLWType.containsAttributeKey("isAssembled")) {
                    if (childByLWType instanceof ICDSubInternalExtrusion) {
                        if (b && childByLWType.getCurrentOption().getId().equals("ICD_Sub_Inner_Chase_Extrusion")) {
                            set.add(childByLWType);
                        }
                        else if (b2 && childByLWType.getCurrentOption().getId().equals("ICD_Sub_Innner_StepReturn_Extrusion")) {
                            set.add(childByLWType);
                        }
                    }
                    else if (childByLWType instanceof ICDJoint) {
                        final List childrenByClass = childByLWType.getParentEntity().getChildrenByClass((Class)ICDSubInternalExtrusion.class, false, false);
                        final ICDJoint icdJoint = (ICDJoint)childByLWType;
                        for (final TypeableEntity o : childrenByClass) {
                            if (set.contains(o) && icdJoint.extrusionConnectsToJoint(o)) {
                                set.add(childByLWType);
                                break;
                            }
                        }
                    }
                    else {
                        set.add(childByLWType);
                    }
                }
            }
        }
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addDimensionsToManufacturingTreeMap(treeMap, (TransformableEntity)this);
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
