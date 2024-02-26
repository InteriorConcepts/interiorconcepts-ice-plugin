// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import java.util.Iterator;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.TypeValidatorEntity;
import net.dirtt.icebox.iceoutput.core.IceOutputShapeNode;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;
import net.dirtt.utilities.MathUtilities;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import java.util.List;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import com.iceedge.icd.rendering.ICD2DJointDirectionNode;
import net.iceedge.catalogs.icd.elevation.assembly.ICDAssemblyElevationUtilities;
import java.awt.geom.Path2D;
import net.iceedge.catalogs.icd.intersection.ICDIntersection;
import net.dirtt.icebox.canvas2d.Ice2DPaintableNode;
import java.util.Vector;
import javax.vecmath.Matrix4f;
import net.dirtt.icelib.main.TransformableEntity;
import javax.vecmath.Point3f;
import net.iceedge.catalogs.icd.intersection.ICDPost;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import org.apache.log4j.Logger;
import net.dirtt.icebox.canvas2d.Ice2DShapeNode;

public class ICDMiddleJoint extends ICDJoint
{
    private Ice2DShapeNode assemblyNode;
    private static Logger logger;
    
    public ICDMiddleJoint(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDMiddleJoint(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDMiddleJoint buildClone(final ICDMiddleJoint icdMiddleJoint) {
        super.buildClone(icdMiddleJoint);
        return icdMiddleJoint;
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDMiddleJoint(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDMiddleJoint buildFrameClone(final ICDMiddleJoint icdMiddleJoint, final EntityObject entityObject) {
        super.buildFrameClone(icdMiddleJoint, entityObject);
        return icdMiddleJoint;
    }
    
    @Override
    public void modifyCurrentOption() {
        this.validateIndicator();
        super.modifyCurrentOption();
    }
    
    @Override
    protected void validateIndicator() {
        final ICDPost icdPost = (ICDPost)this.getParent(ICDPost.class);
        if (icdPost != null) {
            final String id = icdPost.getCurrentType().getId();
            String s;
            if ("ICD_TwoWayCurvedPostType".equals(id) || "ICD_TwoWayAngledPostType".equals(id)) {
                s = this.getAttributeValueAsString("Joint_Type");
                if (s == null || s.trim().isEmpty()) {
                    s = ICDJoint.JOINT_TYPE[1];
                }
            }
            else {
                s = icdPost.getJointTypeAtLocation(this.getBasePointWorldSpace());
            }
            this.setBottomAndMiddleJointTypeForPost(icdPost, s);
        }
    }
    
    @Override
    public Vector<Ice2DPaintableNode> getAssemblyIcons(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        final ICDIntersection icdIntersection = (ICDIntersection)this.getParent(ICDIntersection.class);
        if (icdIntersection != null && icdIntersection.getVerticalChase() != null) {
            return null;
        }
        final ICDPost icdPost = (ICDPost)this.getParent(ICDPost.class);
        if (icdPost != null && icdPost.isCurvedPost()) {
            return null;
        }
        if (this.getCurrentOption().getId().contains("None")) {
            return null;
        }
        final Path2D.Float float1 = new Path2D.Float();
        final float stroke = 3.0f;
        final ICD2DJointDirectionNode e = new ICD2DJointDirectionNode(this.getLayerName(), (TransformableEntity)this, this.getEntWorldSpaceMatrix(), ICDAssemblyElevationUtilities.appendJointDirections(this, transformableEntity));
        if (this.getCurrentOption().getId().equals("Bolt_On_Joint")) {
            float1.append(new Arc2D.Float(-this.getXDimension(), -this.getYDimension(), this.getXDimension() * 2.0f, this.getYDimension() * 2.0f, 0.0f, 360.0f, 0), false);
        }
        final Ice2DShapeNode e2 = new Ice2DShapeNode(this.getLayerName(), (TransformableEntity)this, this.getAssemblyElevationMatrix(transformableEntity, false), (Shape)float1);
        e2.setStroke(stroke);
        final Vector<Ice2DPaintableNode> vector = new Vector<Ice2DPaintableNode>();
        vector.add(e2);
        vector.add(e);
        return (Vector<Ice2DPaintableNode>)vector;
    }
    
    @Override
    public List<IceOutputNode> getPlotOutputNodes(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        if (this.getCurrentOption().getId().contains("None")) {
            return null;
        }
        final Path2D.Float float1 = new Path2D.Float();
        float1.append(new Rectangle2D.Float(-this.getXDimension() / 2.0f, -this.getYDimension() / 2.0f, this.getXDimension(), this.getYDimension()), false);
        final Line2D.Float s = new Line2D.Float(0.0f, 0.0f, 0.0f, this.getYDimension() * 2.0f);
        final Line2D.Float s2 = new Line2D.Float(0.0f, 0.0f, 0.0f, -this.getYDimension() * 2.0f);
        final Line2D.Float s3 = new Line2D.Float(0.0f, 0.0f, this.getXDimension() * 2.0f, 0.0f);
        final Line2D.Float s4 = new Line2D.Float(0.0f, 0.0f, -this.getXDimension() * 2.0f, 0.0f);
        final Line2D.Float float2 = new Line2D.Float(0.0f, 0.0f, -this.getXDimension() * 2.0f, this.getYDimension() * 2.0f);
        final Line2D.Float float3 = new Line2D.Float(0.0f, 0.0f, this.getXDimension() * 2.0f, -this.getYDimension() * 2.0f);
        float1.append(s, false);
        float1.append(s2, false);
        if (this.isUnderIntersection()) {
            ICDAssemblyElevationUtilities.appendInOrOut(point3f, this.getIntersection(), transformableEntity, float1, float2, float3);
            ICDAssemblyElevationUtilities.appendLeftOrRight((TransformableEntity)this, transformableEntity, this.getIntersection(), float1, s3, s4);
        }
        else if (this.getCurrentOption().getId().equals("ICD_J104F")) {
            float1.append(s3, false);
            float1.append(s4, false);
        }
        if (this.getCurrentOption().getId().equals("Bolt_On_Joint")) {
            float1.append(new Arc2D.Float(-this.getXDimension(), -this.getYDimension(), this.getXDimension() * 2.0f, this.getYDimension() * 2.0f, 0.0f, 360.0f, 0), false);
        }
        final Point3f convertSpaces = MathUtilities.convertSpaces(this.getBasePointWorldSpace(), transformableEntity.getEntWorldSpaceMatrix());
        final Matrix4f matrix4f2 = new Matrix4f();
        matrix4f2.setIdentity();
        matrix4f2.mul(matrix4f);
        if (n == 1) {
            final Matrix4f matrix4f3 = new Matrix4f();
            matrix4f3.setIdentity();
            matrix4f3.rotY(3.1415927f);
            matrix4f2.mul(matrix4f3);
        }
        final Matrix4f matrix4f4 = new Matrix4f();
        matrix4f4.setIdentity();
        matrix4f4.rotX(-1.5707964f);
        matrix4f2.mul(matrix4f4);
        final Matrix4f matrix4f5 = new Matrix4f();
        matrix4f5.setIdentity();
        matrix4f5.setTranslation(new Vector3f((Tuple3f)convertSpaces));
        matrix4f2.mul(matrix4f5);
        final float n2 = -1.5707964f;
        final Matrix4f matrix4f6 = new Matrix4f();
        matrix4f6.setIdentity();
        matrix4f6.rotX(n2);
        matrix4f2.mul(matrix4f6);
        final Matrix4f matrix4f7 = new Matrix4f();
        matrix4f7.setIdentity();
        matrix4f7.rotY(3.1415927f);
        matrix4f2.mul(matrix4f7);
        final IceOutputShapeNode e = new IceOutputShapeNode((Shape)float1, matrix4f2);
        final Vector<IceOutputNode> vector = new Vector<IceOutputNode>();
        vector.add((IceOutputNode)e);
        return vector;
    }
    
    public boolean isQuoteable(final String s) {
        return false;
    }
    
    public boolean isBoltOnJoint() {
        return "Yes".equals(this.getAttributeValueAsString("Joint_BoltOn"));
    }
    
    @Override
    public boolean shouldDrawAssembly() {
        return this.isBoltOnJoint() || super.shouldDrawAssembly();
    }
    
    @Override
    public void solve() {
        if (this.isBoltOnJoint()) {
            this.validateBoltOnSolver();
        }
        super.solve();
    }
    
    private void validateBoltOnSolver() {
        final ICDJointDirections appendJointDirections = ICDAssemblyElevationUtilities.appendJointDirections(this, null);
        int n = 0;
        if (appendJointDirections.isIn) {
            ++n;
        }
        if (appendJointDirections.isOut) {
            ++n;
        }
        if (appendJointDirections.isLeft) {
            ++n;
        }
        if (appendJointDirections.isRight) {
            ++n;
        }
        final Vector<TypeValidatorEntity> vector = new Vector<TypeValidatorEntity>();
        final Iterator<TypeValidatorEntity> clonedChildren = this.getClonedChildren();
        while (clonedChildren.hasNext()) {
            final TypeValidatorEntity next = clonedChildren.next();
            if (next instanceof TypeValidatorEntity) {
                vector.add(next);
            }
        }
        if (vector.size() < n) {
            for (int n2 = n - vector.size(), i = 0; i < n2; ++i) {
                final TypeObject typeObjectByName = Solution.typeObjectByName("ICD_BoltOn_Container_Type");
                if (typeObjectByName != null) {
                    this.addToTree((EntityObject)new TypeValidatorEntity(typeObjectByName.getId(), typeObjectByName, typeObjectByName.getDefaultOption()));
                }
            }
        }
        else {
            for (int n3 = vector.size() - n, j = 0; j < n3; ++j) {
                vector.get(j).destroy();
            }
        }
    }
    
    static {
        ICDMiddleJoint.logger = Logger.getLogger(ICDMiddleJoint.class);
    }
}
