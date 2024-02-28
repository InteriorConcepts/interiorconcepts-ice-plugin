package net.iceedge.catalogs.icd.panel;

import java.util.TreeMap;
import java.util.Collection;
import javax.vecmath.Point3f;
import java.util.Iterator;
import net.dirtt.utilities.MathUtilities;
import java.util.List;
import java.util.Collections;
import net.iceedge.icecore.basemodule.interfaces.IntersectionArmInterface;
import net.iceedge.icecore.basemodule.interfaces.Segment;
import net.iceedge.catalogs.icd.intersection.ICDIntersection;
import net.iceedge.catalogs.icd.intersection.ICDPost;
import net.dirtt.icelib.main.EntityObject;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import java.util.Vector;

public class ICDAngledPanel extends ICDPanel
{
    private Vector<ICDSubInternalExtrusion> tubings;
    private Vector<ICDMiddleJoint> joints;
    
    public ICDAngledPanel(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDAngledPanel(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDAngledPanel buildClone(final ICDAngledPanel icdAngledPanel) {
        super.buildClone((TransformableTriggerUser)icdAngledPanel);
        return icdAngledPanel;
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDAngledPanel(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDAngledPanel buildFrameClone(final ICDAngledPanel icdAngledPanel, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdAngledPanel, entityObject);
        return icdAngledPanel;
    }
    
    public void calculateDimensions() {
        final ICDPost post = this.getPost();
        if (post != null) {
            this.setXDimension(post.getXDimension());
            this.setYDimension(post.getZDimension());
            this.setZDimension(post.getYDimension());
        }
        else {
            super.calculateDimensions();
        }
    }
    
    @Override
    public PANEL_TYPE getPanelType() {
        return PANEL_TYPE.ANGLED;
    }
    
    @Override
    public String getDescription() {
        String s = "";
        if (this.currentCatalogOption != null) {
            s = this.currentCatalogOption.getDescription();
            if ("ICD_Panel_Error_Option".equalsIgnoreCase(this.currentCatalogOption.getId())) {
                s = s + "-" + this.newSku;
            }
        }
        return s + " (Actual Size " + Math.round(this.getHeight()) + "h 54w)";
    }
    
    public boolean isCorePanel() {
        return true;
    }
    
    public ICDIntersection getParentIntersection() {
        for (EntityObject entityObject = this.getParentEntity(); entityObject != null; entityObject = entityObject.getParentEntity()) {
            if (entityObject instanceof ICDIntersection) {
                return (ICDIntersection)entityObject;
            }
        }
        return null;
    }
    
    public Segment getAdjacentSegment(final int index) {
        Segment segment = null;
        final ICDIntersection parentIntersection = this.getParentIntersection();
        if (parentIntersection != null) {
            final Vector<IntersectionArmInterface> armsOrderedByIndex_WithoutCloning = parentIntersection.getArmsOrderedByIndex_WithoutCloning();
            if (armsOrderedByIndex_WithoutCloning != null && armsOrderedByIndex_WithoutCloning.size() > index) {
                final IntersectionArmInterface intersectionArmInterface = armsOrderedByIndex_WithoutCloning.get(index);
                if (intersectionArmInterface != null) {
                    segment = intersectionArmInterface.getSegment();
                }
            }
        }
        return segment;
    }
    
    public void calculateChildTubingAndJoints(final ICDVerticalExtrusion icdVerticalExtrusion) {
        if (icdVerticalExtrusion instanceof ICDStartExtrusion) {
            if ("ICD_Angled_2_Start_Extrusion_Type".equals(((ICDStartExtrusion)icdVerticalExtrusion).getCurrentType().getId())) {
                this.calculateChildTubingForStartExtrusion(icdVerticalExtrusion);
            }
            return;
        }
        boolean b = false;
        int n = 0;
        if ("ICD_Core_Angled_Panel_Type".equals(this.getCurrentType().getId())) {
            n = 1;
        }
        this.tubings = icdVerticalExtrusion.getTubings();
        this.joints = icdVerticalExtrusion.getJoints();
        final float height = this.getHeight();
        Vector<Float> splitLocation = null;
        float floatValue = 0.0f;
        final Segment adjacentSegment = this.getAdjacentSegment(n);
        if (adjacentSegment != null) {
            final float height2 = adjacentSegment.getHeight();
            splitLocation = adjacentSegment.getSplitLocation(false);
            if (splitLocation.size() > 2 || height2 < height - 1.0f) {
                b = true;
            }
            Collections.sort(splitLocation);
            if (splitLocation.size() > 0) {
                floatValue = splitLocation.lastElement();
            }
        }
        String s = "No";
        final boolean withHorizontalInnerExtrusion = this.withHorizontalInnerExtrusion();
        if (withHorizontalInnerExtrusion || b) {
            s = "Yes";
        }
        if (!s.equals(icdVerticalExtrusion.getAttributeValueAsString("specialInternalExtrusion"))) {
            icdVerticalExtrusion.createNewAttribute("specialInternalExtrusion", s);
            icdVerticalExtrusion.modifyCurrentOption();
            icdVerticalExtrusion.validateChildTypes();
        }
        boolean b2 = false;
        float value = 0.0f;
        if (withHorizontalInnerExtrusion) {
            value = this.getSplitLocation() + 0.75f;
            final Iterator<Float> iterator = splitLocation.iterator();
            while (iterator.hasNext()) {
                if (MathUtilities.isSameFloat((float)iterator.next(), value, 1.0f)) {
                    b2 = true;
                    break;
                }
            }
            if (!b2) {
                splitLocation.add(new Float(value));
            }
        }
        Collections.sort(splitLocation);
        final float value2 = this.getHeight() - 0.25f;
        if (value2 > floatValue + 0.5) {
            splitLocation.add(new Float(value2));
        }
        this.collectTubingAndJoints(icdVerticalExtrusion);
        this.validateJointsTubings((Vector<Float>)splitLocation, icdVerticalExtrusion, b2, value);
        this.destroyUnusedTubingAndJoints(icdVerticalExtrusion);
    }
    
    private void collectTubingAndJoints(final ICDVerticalExtrusion icdVerticalExtrusion) {
        final Vector<ICDSubInternalExtrusion> tubings = icdVerticalExtrusion.getTubings();
        final Vector<ICDMiddleJoint> joints = icdVerticalExtrusion.getJoints();
        tubings.clear();
        joints.clear();
        final Iterator<EntityObject> children = icdVerticalExtrusion.getChildren();
        while (children.hasNext()) {
            final EntityObject entityObject = children.next();
            if (entityObject instanceof ICDMiddleJoint) {
                joints.add((ICDMiddleJoint)entityObject);
            }
            else {
                if (!(entityObject instanceof ICDSubInternalExtrusion)) {
                    continue;
                }
                tubings.add((ICDSubInternalExtrusion)entityObject);
            }
        }
    }
    
    private void validateJointsTubings(final Vector<Float> vector, final ICDVerticalExtrusion icdVerticalExtrusion, final boolean b, float n) {
        if (vector != null && vector.size() >= 2) {
            final Vector<Float> vector2 = new Vector<Float>();
            final Iterator<Float> iterator = vector.iterator();
            while (iterator.hasNext()) {
                vector2.add(new Float(iterator.next() - 0.75f - 1.0f));
            }
            n = n - 0.75f - 1.0f;
            final TypeObject childType = icdVerticalExtrusion.getChildTypeFor(ICDSubInternalExtrusion.class);
            final TypeObject childType2 = icdVerticalExtrusion.getChildTypeFor(ICDMiddleJoint.class);
            final Point3f point3f = new Point3f(0.0f, 0.0f, 0.0f);
            final Point3f point3f2 = new Point3f(0.0f, 0.0f, 0.0f);
            final Point3f basePoint = new Point3f(0.0f, -0.5f, 0.0f);
            final Point3f basePoint2 = new Point3f(0.0f, 0.0f, 0.0f);
            float floatValue = vector2.get(0);
            basePoint.z = floatValue + 1.0f;
            for (int i = 1; i < vector2.size(); ++i) {
                final float floatValue2 = vector2.get(i);
                basePoint2.z = floatValue2 + 0.5f;
                if (childType2 != null && i < vector2.size() - 1) {
                    ICDMiddleJoint firstMiddleJoint = this.getFirstMiddleJoint(icdVerticalExtrusion);
                    if (firstMiddleJoint == null) {
                        childType2.getDefaultOption();
                        firstMiddleJoint = (ICDMiddleJoint)getTypeableEntityInstance("", childType2, childType2.getDefaultOption());
                        if (firstMiddleJoint != null) {
                            icdVerticalExtrusion.addToTree((EntityObject)firstMiddleJoint);
                        }
                    }
                    if (firstMiddleJoint != null) {
                        String s = ICDJoint.JOINT_TYPE[1];
                        firstMiddleJoint.createNewAttribute("Joint_Type", s);
                        if (b && MathUtilities.isSameFloat(floatValue2, n, 1.0f)) {
                            s = ICDJoint.JOINT_TYPE[0];
                            firstMiddleJoint.modifyAttributeValue("Joint_Type", s);
                        }
                        firstMiddleJoint.setBasePoint(basePoint2);
                        this.validateJointType(this.getTypeOfChaseMidConnectorContainerAtLocation(firstMiddleJoint.getBasePointWorldSpace()), s, firstMiddleJoint);
                        firstMiddleJoint.modifyCurrentOption();
                        firstMiddleJoint.validateChildTypes();
                        firstMiddleJoint.solve();
                    }
                }
                if (childType != null) {
                    ICDSubInternalExtrusion firstTubing = this.getFirstTubing(childType, icdVerticalExtrusion);
                    if (firstTubing == null) {
                        firstTubing = (ICDSubInternalExtrusion)getTypeableEntityInstance("", childType, childType.getDefaultOption());
                        if (firstTubing != null) {
                            icdVerticalExtrusion.addToTree((EntityObject)firstTubing);
                        }
                    }
                    if (firstTubing != null) {
                        firstTubing.setBasePoint(basePoint);
                        firstTubing.setZDimension(floatValue2 - floatValue - 1.0f);
                        firstTubing.solve();
                    }
                    floatValue = floatValue2;
                    basePoint.z = floatValue + 1.0f;
                }
            }
            this.validateOnPresenceOfBoltOnJoints(icdVerticalExtrusion);
        }
    }
    
    private void destroyUnusedTubingAndJoints(final ICDVerticalExtrusion icdVerticalExtrusion) {
        final Vector<ICDSubInternalExtrusion> tubings = icdVerticalExtrusion.getTubings();
        final Vector<ICDMiddleJoint> joints = icdVerticalExtrusion.getJoints();
        final Iterator<ICDSubInternalExtrusion> iterator = tubings.iterator();
        while (iterator.hasNext()) {
            iterator.next().destroy();
        }
        tubings.clear();
        final Iterator<ICDMiddleJoint> iterator2 = joints.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().destroy();
        }
        joints.clear();
    }
    
    protected ICDSubInternalExtrusion getFirstTubing(final TypeObject typeObject, final ICDVerticalExtrusion icdVerticalExtrusion) {
        final Vector<ICDSubInternalExtrusion> tubings = icdVerticalExtrusion.getTubings();
        Object o = null;
        if (tubings.size() > 0) {
            o = tubings.firstElement();
            tubings.remove(o);
        }
        return (ICDSubInternalExtrusion)o;
    }
    
    protected ICDMiddleJoint getFirstMiddleJoint(final ICDVerticalExtrusion icdVerticalExtrusion) {
        Object o = null;
        final Vector<ICDMiddleJoint> joints = icdVerticalExtrusion.getJoints();
        if (joints.size() > 0) {
            o = joints.firstElement();
            joints.remove(o);
        }
        return (ICDMiddleJoint)o;
    }
    
    @Override
    public boolean isCurvedPanel() {
        return true;
    }
    
    public void calculateChildTubingForStartExtrusion(final ICDVerticalExtrusion icdVerticalExtrusion) {
        this.tubings = icdVerticalExtrusion.getTubings();
        this.joints = icdVerticalExtrusion.getJoints();
        final Vector<Float> vector = new Vector<Float>();
        vector.add(new Float(0.75f));
        String s = "No";
        final boolean withHorizontalInnerExtrusion = this.withHorizontalInnerExtrusion();
        if (withHorizontalInnerExtrusion) {
            s = "Yes";
        }
        if (!s.equals(icdVerticalExtrusion.getAttributeValueAsString("specialInternalExtrusion"))) {
            icdVerticalExtrusion.createNewAttribute("specialInternalExtrusion", s);
            icdVerticalExtrusion.modifyCurrentOption();
            icdVerticalExtrusion.validateChildTypes();
        }
        if (withHorizontalInnerExtrusion) {
            vector.add(new Float(this.getSplitLocation() + 0.75f));
        }
        vector.add(new Float(this.getHeight() - 0.25f));
        this.collectTubingAndJoints(icdVerticalExtrusion);
        this.validateTubingsForPlasticPost(vector, icdVerticalExtrusion);
        this.destroyUnusedTubingAndJoints(icdVerticalExtrusion);
    }
    
    private void validateTubingsForPlasticPost(final Vector<Float> vector, final ICDVerticalExtrusion icdVerticalExtrusion) {
        if (vector != null && vector.size() >= 2) {
            final float n = 0.5f;
            final Vector<Float> vector2 = new Vector<Float>();
            final Iterator<Float> iterator = vector.iterator();
            while (iterator.hasNext()) {
                vector2.add(new Float(iterator.next() - 0.75f - 1.0f));
            }
            final TypeObject childType = icdVerticalExtrusion.getChildTypeFor(ICDSubInternalExtrusion.class);
            final Point3f basePoint = new Point3f(0.0f, -0.5f, 0.0f);
            float floatValue = vector2.get(0);
            basePoint.z = floatValue + 1.0f + n;
            for (int i = 1; i < vector2.size(); ++i) {
                final float floatValue2 = vector2.get(i);
                if (childType != null) {
                    ICDSubInternalExtrusion firstTubing = this.getFirstTubing(childType, icdVerticalExtrusion);
                    if (firstTubing == null) {
                        firstTubing = (ICDSubInternalExtrusion)getTypeableEntityInstance("", childType, childType.getDefaultOption());
                        if (firstTubing != null) {
                            icdVerticalExtrusion.addToTree((EntityObject)firstTubing);
                        }
                    }
                    if (firstTubing != null) {
                        firstTubing.setBasePoint(basePoint);
                        firstTubing.setZDimension(floatValue2 - floatValue - 1.0f - 2.0f * n);
                        firstTubing.solve();
                    }
                    floatValue = floatValue2;
                    basePoint.z = floatValue + 1.0f + n;
                }
            }
        }
    }
    
    private Collection<ICDSubInternalExtrusion> getSortedSubInternalExtrusions(final ICDVerticalExtrusion icdVerticalExtrusion) {
        final TreeMap<Float, ICDSubInternalExtrusion> treeMap = new TreeMap<Float, ICDSubInternalExtrusion>();
        if (icdVerticalExtrusion instanceof ICDAngledEndExtrusion) {
            for (final ICDSubInternalExtrusion value : ((ICDAngledEndExtrusion)icdVerticalExtrusion).getChildrenByClass(ICDSubInternalExtrusion.class, false, true)) {
                treeMap.put(new Float(value.getBasePointWorldSpace().z), value);
            }
        }
        return treeMap.values();
    }
    
    private Collection<ICDMiddleJoint> getSortedMiddleJoints(final ICDVerticalExtrusion icdVerticalExtrusion) {
        final TreeMap<Float, ICDMiddleJoint> treeMap = new TreeMap<Float, ICDMiddleJoint>();
        if (icdVerticalExtrusion instanceof ICDAngledEndExtrusion) {
            for (final ICDMiddleJoint value : ((ICDAngledEndExtrusion)icdVerticalExtrusion).getChildrenByClass(ICDMiddleJoint.class, false, true)) {
                treeMap.put(new Float(value.getBasePointWorldSpace().z), value);
            }
        }
        return treeMap.values();
    }
    
    private void validateOnPresenceOfBoltOnJoints(final ICDVerticalExtrusion icdVerticalExtrusion) {
        final Collection<ICDMiddleJoint> sortedMiddleJoints = this.getSortedMiddleJoints(icdVerticalExtrusion);
        if (sortedMiddleJoints.size() == 0) {
            return;
        }
        final Collection<ICDSubInternalExtrusion> sortedSubInternalExtrusions = this.getSortedSubInternalExtrusions(icdVerticalExtrusion);
        for (final ICDMiddleJoint icdMiddleJoint : sortedMiddleJoints) {
            if (icdMiddleJoint.isBoltOnJoint()) {
                final ICDSubInternalExtrusion[] adjacentExtrusions = this.getAdjacentExtrusions(icdMiddleJoint, sortedSubInternalExtrusions);
                final ICDSubInternalExtrusion icdSubInternalExtrusion = adjacentExtrusions[0];
                final ICDSubInternalExtrusion icdSubInternalExtrusion2 = adjacentExtrusions[1];
                if (icdSubInternalExtrusion == null || icdSubInternalExtrusion2 == null) {
                    continue;
                }
                final Point3f point3f = new Point3f();
                final Point3f basePoint3f = icdSubInternalExtrusion.getBasePoint3f();
                icdSubInternalExtrusion2.setZDimension(icdSubInternalExtrusion.getZDimension() + icdSubInternalExtrusion2.getZDimension() + 1.0f);
                sortedSubInternalExtrusions.remove(icdSubInternalExtrusion);
                icdSubInternalExtrusion.destroy();
                icdSubInternalExtrusion2.setBasePoint(basePoint3f);
                icdSubInternalExtrusion2.solve();
            }
        }
    }
    
    private ICDSubInternalExtrusion[] getAdjacentExtrusions(final ICDMiddleJoint icdMiddleJoint, final Collection<ICDSubInternalExtrusion> collection) {
        final Object[] array = collection.toArray();
        final float z = icdMiddleJoint.getBasePointWorldSpace().z;
        ICDSubInternalExtrusion icdSubInternalExtrusion = null;
        ICDSubInternalExtrusion icdSubInternalExtrusion2 = (ICDSubInternalExtrusion)array[0];
        for (int i = 1; i < array.length; ++i) {
            icdSubInternalExtrusion = (ICDSubInternalExtrusion)array[i];
            if (icdSubInternalExtrusion.getBasePointWorldSpace().z > z) {
                break;
            }
            icdSubInternalExtrusion2 = icdSubInternalExtrusion;
        }
        return new ICDSubInternalExtrusion[] { icdSubInternalExtrusion2, icdSubInternalExtrusion };
    }
}
