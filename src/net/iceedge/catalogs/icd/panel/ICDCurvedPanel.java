// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintable;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationUtilities;
import net.dirtt.icelib.main.ElevationEntity;
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

public class ICDCurvedPanel extends ICDPanel
{
    private Vector<ICDSubInternalExtrusion> tubings;
    private Vector<ICDMiddleJoint> joints;
    
    public ICDCurvedPanel(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDCurvedPanel(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDCurvedPanel buildClone(final ICDCurvedPanel icdCurvedPanel) {
        super.buildClone((TransformableTriggerUser)icdCurvedPanel);
        return icdCurvedPanel;
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDCurvedPanel(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDCurvedPanel buildFrameClone(final ICDCurvedPanel icdCurvedPanel, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdCurvedPanel, entityObject);
        return icdCurvedPanel;
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
        return PANEL_TYPE.CURVED_PANEL;
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
        boolean b = false;
        int n = 1;
        if (icdVerticalExtrusion instanceof ICDStartExtrusion) {
            n = 0;
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
                int equalsIgnoreCase = 0;
                final float floatValue2 = vector2.get(i);
                basePoint2.z = floatValue2 + 0.5f;
                if (childType2 != null && i < vector2.size() - 1) {
                    ICDMiddleJoint firstMiddleJoint = this.getFirstMiddleJoint(childType2, icdVerticalExtrusion);
                    if (firstMiddleJoint == null) {
                        childType2.getDefaultOption();
                        firstMiddleJoint = (ICDMiddleJoint)getTypeableEntityInstance("", childType2, childType2.getDefaultOption());
                        if (firstMiddleJoint != null) {
                            icdVerticalExtrusion.addToTree((EntityObject)firstMiddleJoint);
                        }
                    }
                    if (firstMiddleJoint != null) {
                        if (firstMiddleJoint.containsAttributeKey("Joint_BoltOn")) {
                            equalsIgnoreCase = (firstMiddleJoint.getAttributeValueAsString("Joint_BoltOn").equalsIgnoreCase("Yes") ? 1 : 0);
                        }
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
                    if (equalsIgnoreCase == 0) {
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
            }
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
    
    protected ICDMiddleJoint getFirstMiddleJoint(final TypeObject typeObject, final ICDVerticalExtrusion icdVerticalExtrusion) {
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
    
    @Override
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        if (ICDAssemblyElevationUtilities.shouldDrawElevation(elevationEntity, this)) {
            return new Vector<String>();
        }
        return super.getCadElevationScript(elevationEntity);
    }
}
