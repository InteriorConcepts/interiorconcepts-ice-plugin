package net.iceedge.catalogs.icd.panel;

import java.util.Collection;
import net.iceedge.catalogs.icd.elevation.assembly.AssemblyPaintable;
import net.dirtt.icelib.main.ElevationEntity;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelInterface;
import net.dirtt.icebox.iceoutput.core.IceOutputShapeNode;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;
import net.iceedge.catalogs.icd.ICDILine;
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
import net.dirtt.utilities.MathUtilities;
import javax.vecmath.Point3f;
import net.iceedge.catalogs.icd.intersection.ICDChaseMidConnectorContainer;
import net.iceedge.catalogs.icd.intersection.ICDPostHostInterface;
import net.iceedge.catalogs.icd.intersection.ICDPost;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icebox.canvas2d.Ice2DShapeNode;

public class ICDTopJoint extends ICDJoint
{
    private Ice2DShapeNode assemblyNode;
    
    public ICDTopJoint(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDTopJoint(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDTopJoint buildClone(final ICDTopJoint icdTopJoint) {
        super.buildClone(icdTopJoint);
        return icdTopJoint;
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDTopJoint(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDTopJoint buildFrameClone(final ICDTopJoint icdTopJoint, final EntityObject entityObject) {
        super.buildFrameClone(icdTopJoint, entityObject);
        return icdTopJoint;
    }
    
    @Override
    public void modifyCurrentOption() {
        this.validateIndicator();
        super.modifyCurrentOption();
    }
    
    @Override
    protected void validateIndicator() {
        final ICDPost icdPost = (ICDPost)this.getParent((Class)ICDPost.class);
        if (icdPost != null) {
            if (icdPost.isCurvedPost()) {
                if (this.getParent((Class)ICDChaseConnectorExtrusion.class) != null) {
                    this.setJointTypeForVerticalConnector(((ICDPostHostInterface)this.getParent((Class)ICDPostHostInterface.class)).getBottomJointType());
                }
                else {
                    this.createNewAttribute("Joint_Type", ICDJoint.JOINT_TYPE[0]);
                }
            }
            else if (this.getParent((Class)ICDChaseConnectorExtrusion.class) != null) {
                this.setJointTypeForVerticalConnector(((ICDPostHostInterface)this.getParent((Class)ICDPostHostInterface.class)).getBottomJointType());
            }
            else {
                this.setTopJointTypeForPost(icdPost, icdPost.getJointTypeAtLocation(this.getBasePointWorldSpace()));
            }
        }
    }
    
    private void setTopJointTypeForPost(final ICDPost icdPost, String s) {
        if (icdPost != null) {
            final ICDChaseMidConnectorContainer icdChaseMidConnectorContainer = (ICDChaseMidConnectorContainer)icdPost.getChildByClass((Class)ICDChaseMidConnectorContainer.class);
            if (icdChaseMidConnectorContainer == null) {
                this.createNewAttribute("Joint_Type", s);
            }
            else {
                final String attributeValueAsString = icdChaseMidConnectorContainer.getAttributeValueAsString("ICD_Chase_Connector_Container_Size");
                if (attributeValueAsString == null || attributeValueAsString.equalsIgnoreCase("None")) {
                    this.createNewAttribute("Joint_Type", s);
                }
                else {
                    final Point3f basePointWorldSpace = this.getBasePointWorldSpace();
                    final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion = (ICDChaseConnectorExtrusion)icdChaseMidConnectorContainer.getChildByLWType("ICD_Chase_Mid_Top_Connector_Type");
                    Point3f basePointWorldSpace2 = new Point3f();
                    if (icdChaseConnectorExtrusion != null) {
                        basePointWorldSpace2 = icdChaseConnectorExtrusion.getBasePointWorldSpace();
                    }
                    if (!MathUtilities.isSameFloat(basePointWorldSpace.z, basePointWorldSpace2.z + 0.5f, 0.1f)) {
                        this.createNewAttribute("Joint_Type", s);
                    }
                    else {
                        if (s.equals("1 Way")) {
                            if (attributeValueAsString.equalsIgnoreCase("Single")) {
                                s = "2 Way (90)";
                            }
                            else if (attributeValueAsString.equalsIgnoreCase("Double")) {
                                s = "3 Way";
                            }
                        }
                        else if (s.equals("2 Way (90)")) {
                            s = "3 Way";
                        }
                        else if (s.equals("2 Way (180)")) {
                            if (attributeValueAsString.equalsIgnoreCase("Single")) {
                                s = "3 Way";
                            }
                            else if (attributeValueAsString.equalsIgnoreCase("Double")) {
                                s = "4 Way";
                            }
                        }
                        else if (s.equals("3 Way")) {
                            s = "4 Way";
                        }
                        else if (s.equals("4 Way")) {}
                        this.createNewAttribute("Joint_Type", s);
                    }
                }
            }
            return;
        }
        this.createNewAttribute("Joint_Type", s);
    }
    
    @Override
    public Vector<Ice2DPaintableNode> getAssemblyIcons(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        final ICDIntersection icdIntersection = (ICDIntersection)this.getParent((Class)ICDIntersection.class);
        if (icdIntersection != null && icdIntersection.getVerticalChase() != null) {
            return null;
        }
        final float stroke = 3.0f;
        final Path2D.Float float1 = new Path2D.Float();
        final ICD2DJointDirectionNode e = new ICD2DJointDirectionNode(this.getLayerName(), (TransformableEntity)this, this.getEntWorldSpaceMatrix(), ICDAssemblyElevationUtilities.appendJointDirections(this, transformableEntity));
        if (this.getCurrentOption().getId().equals("ICD_J102R")) {
            float1.append(new Arc2D.Float(-2.0f, -3.0f, 4.0f, 3.0f, 25.0f, 130.0f, 0), false);
        }
        final Ice2DShapeNode e2 = new Ice2DShapeNode(this.getLayerName(), (TransformableEntity)this, this.getAssemblyElevationMatrix(transformableEntity, false), (Shape)float1);
        e2.setStroke(stroke);
        final Vector<ICD2DJointDirectionNode> vector = new Vector<ICD2DJointDirectionNode>();
        vector.add((ICD2DJointDirectionNode)e2);
        vector.add(e);
        return (Vector<Ice2DPaintableNode>)vector;
    }
    
    @Override
    public List<IceOutputNode> getPlotOutputNodes(final int n, final Point3f point3f, final TransformableEntity transformableEntity, final Matrix4f matrix4f) {
        if (this.getCurrentOption().getId().contains("No")) {
            return null;
        }
        final Path2D.Float float1 = new Path2D.Float();
        float1.append(new Rectangle2D.Float(-this.getXDimension() / 2.0f, -this.getYDimension() / 2.0f, this.getXDimension(), this.getYDimension()), false);
        final Line2D.Float float2 = new Line2D.Float(0.0f, 0.0f, 0.0f, -this.getYDimension() * 2.0f);
        final Line2D.Float s = new Line2D.Float(0.0f, 0.0f, 0.0f, this.getYDimension() * 2.0f);
        final Line2D.Float s2 = new Line2D.Float(0.0f, 0.0f, -this.getXDimension() * 2.0f, 0.0f);
        final Line2D.Float s3 = new Line2D.Float(0.0f, 0.0f, this.getXDimension() * 2.0f, 0.0f);
        final Line2D.Float s4 = new Line2D.Float(0.0f, 0.0f, -this.getXDimension() * 2.0f, this.getYDimension() * 2.0f);
        final Line2D.Float s5 = new Line2D.Float(0.0f, 0.0f, this.getXDimension() * 2.0f, -this.getYDimension() * 2.0f);
        float1.append(s, false);
        if (this.isUnderIntersection()) {
            ICDAssemblyElevationUtilities.appendInOrOut(point3f, this.getIntersection(), transformableEntity, float1, s4, s5);
            ICDAssemblyElevationUtilities.appendLeftOrRight((TransformableEntity)this, transformableEntity, this.getIntersection(), float1, s3, s2);
        }
        else if (this.getCurrentOption().getId().equals("ICD_J103F")) {
            float1.append(s2, false);
            float1.append(s3, false);
        }
        else if (this.getCurrentOption().getId().equals("ICD_J103C")) {
            if (MathUtilities.convertSpaces(new Point3f(this.getBasePointWorldSpace()), ((ICDILine)this.getParent((Class)ICDILine.class)).getEntWorldSpaceMatrix()).y > 0.0f) {
                float1.append(s4, false);
            }
            else {
                float1.append(s5, false);
            }
            ICDAssemblyElevationUtilities.appendChaseLeftOrRight((TransformableEntity)this, transformableEntity, float1, s2, s3);
        }
        if (this.getCurrentOption().getId().equals("ICD_J102R")) {
            float1.append(new Arc2D.Float(-2.0f, -2.0f, 3.0f, 3.0f, 90.0f, 120.0f, 0), false);
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
        final Matrix4f matrix4f6 = new Matrix4f();
        matrix4f6.setIdentity();
        if (ICDAssemblyElevationUtilities.isJointOnChase(this)) {
            final Matrix4f matrix4f7 = new Matrix4f();
            matrix4f7.setIdentity();
            matrix4f7.rotZ(3.1415927f);
            matrix4f2.mul(matrix4f7);
            final int n2 = -1;
            final int n3 = 1;
            final Point3f convertSpaces2 = MathUtilities.convertSpaces(new Point3f(this.getBasePointWorldSpace()), ((ICDILine)this.getParent((Class)ICDILine.class)).getEntWorldSpaceMatrix());
            int n4 = n2;
            if (convertSpaces2.y > 0.0f) {
                n4 = n3;
            }
            boolean b = false;
            final ICDChaseMidConnectorContainer icdChaseMidConnectorContainer = (ICDChaseMidConnectorContainer)this.getParent((Class)ICDChaseMidConnectorContainer.class);
            if (icdChaseMidConnectorContainer != null && icdChaseMidConnectorContainer.getRotationVector3f().z == 0.0f) {
                final Matrix4f matrix4f8 = new Matrix4f();
                matrix4f8.setIdentity();
                matrix4f8.rotZ(3.1415927f);
                matrix4f2.mul(matrix4f8);
                b = true;
            }
            final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion = (ICDChaseConnectorExtrusion)this.getParent((Class)ICDChaseConnectorExtrusion.class);
            if (icdChaseConnectorExtrusion != null) {
                final float verticalSkewOffset = ICDAssemblyElevationUtilities.getVerticalSkewOffset(icdChaseConnectorExtrusion.getBasePoint3f().y);
                if (n4 == n2) {
                    matrix4f6.setTranslation(new Vector3f((b ? -1 : 1) * icdChaseConnectorExtrusion.getBasePoint3f().y, 0.0f, -verticalSkewOffset));
                }
                else {
                    matrix4f6.setTranslation(new Vector3f((b ? -1 : 1) * icdChaseConnectorExtrusion.getBasePoint3f().y, 0.0f, verticalSkewOffset));
                }
            }
            if (n4 == n3 || b) {
                final Matrix4f matrix4f9 = new Matrix4f();
                matrix4f9.setIdentity();
                matrix4f9.rotZ(3.1415927f);
                matrix4f2.mul(matrix4f9);
            }
            matrix4f2.mul(matrix4f6);
        }
        final float n5 = -1.5707964f;
        final Matrix4f matrix4f10 = new Matrix4f();
        matrix4f10.setIdentity();
        matrix4f10.rotX(n5);
        matrix4f2.mul(matrix4f10);
        final Matrix4f matrix4f11 = new Matrix4f();
        matrix4f11.setIdentity();
        matrix4f11.rotY(3.1415927f);
        matrix4f2.mul(matrix4f11);
        final IceOutputShapeNode e = new IceOutputShapeNode((Shape)float1, matrix4f2);
        final Vector<IceOutputNode> vector = new Vector<IceOutputNode>();
        vector.add((IceOutputNode)e);
        return vector;
    }
    
    public PanelInterface getParentPanel() {
        for (EntityObject entityObject = this.getParentEntity(); entityObject != null; entityObject = entityObject.getParentEntity()) {
            if (entityObject instanceof PanelInterface) {
                return (PanelInterface)entityObject;
            }
        }
        return null;
    }
    
    public ICDCurvedPanel getParentCurvedPanel() {
        final PanelInterface parentPanel = this.getParentPanel();
        if (parentPanel != null && parentPanel instanceof ICDCurvedPanel) {
            return (ICDCurvedPanel)parentPanel;
        }
        return null;
    }
    
    @Override
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        final Vector<String> cadElevationScript = super.getCadElevationScript(elevationEntity);
        if (ICDAssemblyElevationUtilities.shouldDrawElevation(elevationEntity, this)) {
            cadElevationScript.addAll(this.getAssemblyLineScript());
            return cadElevationScript;
        }
        return cadElevationScript;
    }
    
    private Vector<String> getAssemblyLineScript() {
        final Vector<String> vector = new Vector<String>();
        if (this.getCurrentOption().getId().equals("ICD_J102R")) {
            this.paintRadiusJointR(vector);
        }
        return vector;
    }
}
