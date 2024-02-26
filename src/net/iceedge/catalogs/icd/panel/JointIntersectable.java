// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import javax.vecmath.Point3f;

public interface JointIntersectable
{
    boolean contains(final Point3f p0);
    
    boolean doesParticipateInJointIntersection();
}
