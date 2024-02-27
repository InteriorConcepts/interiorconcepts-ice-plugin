package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.main.TransformableEntity;
import javax.vecmath.Point3f;

public class ICDJointDirections
{
    float d;
    public Point3f in;
    public Point3f out;
    public Point3f left;
    public Point3f right;
    public Point3f up;
    public Point3f down;
    public Point3f center;
    ICDJoint joint;
    TransformableEntity parent;
    public boolean isIn;
    public boolean isOut;
    public boolean isLeft;
    public boolean isRight;
    public boolean isUp;
    public boolean isDown;
    
    public ICDJointDirections(final ICDJoint joint, final TransformableEntity parent) {
        this.d = 2.0f;
        this.in = new Point3f(0.0f, -this.d, 0.0f);
        this.out = new Point3f(0.0f, this.d, 0.0f);
        this.left = new Point3f(-this.d, 0.0f, 0.0f);
        this.right = new Point3f(this.d, 0.0f, 0.0f);
        this.up = new Point3f(0.0f, 0.0f, this.d);
        this.down = new Point3f(0.0f, 0.0f, -this.d);
        this.center = new Point3f(0.0f, 0.0f, 0.0f);
        this.isIn = false;
        this.isOut = false;
        this.isLeft = false;
        this.isRight = false;
        this.isUp = false;
        this.isDown = false;
        this.joint = joint;
        this.parent = parent;
    }
    
    public ICDJoint getJoint() {
        return this.joint;
    }
    
    public void setJoint(final ICDJoint joint) {
        this.joint = joint;
    }
}
