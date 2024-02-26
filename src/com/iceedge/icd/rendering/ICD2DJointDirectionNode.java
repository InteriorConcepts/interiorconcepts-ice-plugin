// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.rendering;

import net.dirtt.icebox.canvas2d.Ice2DUtilities;
import javax.vecmath.Point3f;
import java.awt.Color;
import javax.media.j3d.Transform3D;
import java.awt.Graphics2D;
import javax.vecmath.Matrix4f;
import net.dirtt.icelib.main.TransformableEntity;
import net.iceedge.catalogs.icd.panel.ICDJointDirections;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;

public class ICD2DJointDirectionNode extends Ice2DPaintableNode
{
    ICDJointDirections jointDirections;
    public static int JOINT_DIRECTION_THICKNESS;
    
    public ICD2DJointDirectionNode(final String s, final TransformableEntity transformableEntity, final Matrix4f matrix4f, final ICDJointDirections jointDirections) {
        super(s, transformableEntity, matrix4f);
        this.jointDirections = null;
        this.jointDirections = jointDirections;
        this.visible = true;
    }
    
    public void paint(final Graphics2D graphics2D, final Transform3D transform3D, final Matrix4f matrix4f, final byte b) {
        try {
            transform3D.get(this.paintMatrix);
            this.paintMatrix.mul(matrix4f);
            this.paintMatrix.mul(this.worldSpaceMatrix);
            final Color black = Color.black;
            if (this.jointDirections.isDown) {
                Ice2DUtilities.drawLine(graphics2D, this.paintMatrix, new Point3f(0.0f, 0.0f, 0.0f), new Point3f(this.jointDirections.down), black, ICD2DJointDirectionNode.JOINT_DIRECTION_THICKNESS);
            }
            if (this.jointDirections.isUp) {
                Ice2DUtilities.drawLine(graphics2D, this.paintMatrix, new Point3f(0.0f, 0.0f, 0.0f), new Point3f(this.jointDirections.up), black, ICD2DJointDirectionNode.JOINT_DIRECTION_THICKNESS);
            }
            if (this.jointDirections.isLeft) {
                Ice2DUtilities.drawLine(graphics2D, this.paintMatrix, new Point3f(0.0f, 0.0f, 0.0f), new Point3f(this.jointDirections.left), black, ICD2DJointDirectionNode.JOINT_DIRECTION_THICKNESS);
            }
            if (this.jointDirections.isRight) {
                Ice2DUtilities.drawLine(graphics2D, this.paintMatrix, new Point3f(0.0f, 0.0f, 0.0f), new Point3f(this.jointDirections.right), black, ICD2DJointDirectionNode.JOINT_DIRECTION_THICKNESS);
            }
            if (this.jointDirections.isIn) {
                Ice2DUtilities.drawLine(graphics2D, this.paintMatrix, new Point3f(0.0f, 0.0f, 0.0f), new Point3f(this.jointDirections.in), black, ICD2DJointDirectionNode.JOINT_DIRECTION_THICKNESS);
            }
            if (this.jointDirections.isOut) {
                Ice2DUtilities.drawLine(graphics2D, this.paintMatrix, new Point3f(0.0f, 0.0f, 0.0f), new Point3f(this.jointDirections.out), black, ICD2DJointDirectionNode.JOINT_DIRECTION_THICKNESS);
            }
        }
        catch (Exception ex) {
            System.out.println("Automatically Generated Exception Log(ICD2DJointDirectionNode,61)[" + ex.getClass() + "]: " + ex.getMessage());
            System.err.println("Somethings broken going to try and remove from parent");
            this.removeFromParent();
            ex.printStackTrace();
        }
    }
    
    public String toString() {
        return "Joint Direction Node... " + super.toString();
    }
    
    static {
        ICD2DJointDirectionNode.JOINT_DIRECTION_THICKNESS = 3;
    }
}
