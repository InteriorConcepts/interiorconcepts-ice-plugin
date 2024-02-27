package com.iceedge.icd.utilities;

import net.iceedge.catalogs.icd.panel.ICDJoint;
import java.util.Collection;
import net.dirtt.icelib.main.TransformableEntity;
import javax.vecmath.Point3f;
import java.util.Vector;
import net.dirtt.utilities.EntitySpaceCompareNodeWrapper;
import net.iceedge.catalogs.icd.panel.ICDJointDirections;

public class EntitySpaceCompareUtility
{
    public static EntitySpaceCompareNodeWrapper convertJointWrapper(final ICDJointDirections icdJointDirections) {
        EntitySpaceCompareNodeWrapper entitySpaceCompareNodeWrapper = null;
        if (icdJointDirections != null && icdJointDirections.getJoint() != null) {
            final ICDJoint joint = icdJointDirections.getJoint();
            final Vector<Point3f> vector = new Vector<Point3f>();
            if (icdJointDirections.isIn) {
                vector.add(icdJointDirections.in);
            }
            if (icdJointDirections.isDown) {
                vector.add(icdJointDirections.down);
            }
            if (icdJointDirections.isLeft) {
                vector.add(icdJointDirections.left);
            }
            if (icdJointDirections.isRight) {
                vector.add(icdJointDirections.right);
            }
            if (icdJointDirections.isOut) {
                vector.add(icdJointDirections.out);
            }
            if (icdJointDirections.isUp) {
                vector.add(icdJointDirections.up);
            }
            entitySpaceCompareNodeWrapper = new EntitySpaceCompareNodeWrapper((TransformableEntity)joint, (Collection)vector);
        }
        return entitySpaceCompareNodeWrapper;
    }
}
