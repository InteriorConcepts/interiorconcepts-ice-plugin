// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.overheads;

import net.iceedge.icecore.basemodule.baseclasses.material.BasicMaterialEntity;
import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.report.compare.CompareNode;
import net.dirtt.icelib.main.BoundingCube;
import net.dirtt.icelib.main.snapping.simple.SimpleSnapper;
import javax.vecmath.Vector3f;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import java.util.LinkedList;
import java.util.List;
import net.dirtt.icelib.main.Solution;
import java.util.Iterator;
import net.dirtt.icelib.ui.attribute.explorer.ui.entity.PossibleValue;
import java.util.Vector;
import java.util.TreeSet;
import java.util.SortedSet;
import net.iceedge.icecore.basemodule.baseclasses.grips.AttributeGripPoint;
import net.dirtt.icelib.main.attributes.Attribute;
import net.iceedge.icecore.basemodule.baseclasses.grips.GripListener;
import net.iceedge.icecore.basemodule.baseclasses.grips.RelativeAttributeGrip;
import javax.vecmath.Point3f;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrip;
import net.iceedge.icecore.basemodule.baseclasses.grips.BasicAttributeGrippable;
import net.iceedge.catalogs.icd.worksurfaces.ICDBasicWorksurface;

public class ICDBasicOverhead extends ICDBasicWorksurface implements BasicAttributeGrippable
{
    private static final long serialVersionUID = -5697690205063176937L;
    private BasicAttributeGrip leftWidthGrip;
    
    public ICDBasicOverhead(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public TransformableEntity buildClone(final ICDBasicOverhead icdBasicOverhead) {
        super.buildClone(icdBasicOverhead);
        return (TransformableEntity)icdBasicOverhead;
    }
    
    public TransformableEntity buildClone2(final ICDBasicOverhead icdBasicOverhead) {
        super.buildClone2(icdBasicOverhead);
        return (TransformableEntity)icdBasicOverhead;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDBasicOverhead(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDBasicOverhead(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDBasicOverhead icdBasicOverhead, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdBasicOverhead, entityObject);
        return (EntityObject)icdBasicOverhead;
    }
    
    protected void calculateGeometricCenter() {
        super.calculateGeometricCenter();
        this.setGeometricCenterPointLocal(new Point3f(this.getXDimension() * 0.5f, -this.getYDimension() * 0.5f, this.getZDimension() * 0.5f));
    }
    
    @Override
    public void setupGripPainters() {
        this.leftWidthGrip = (BasicAttributeGrip)new RelativeAttributeGrip((TransformableEntity)this, 0);
        this.rightWidthGrip = (BasicAttributeGrip)new RelativeAttributeGrip((TransformableEntity)this, 0);
        this.leftWidthGrip.addListener((GripListener)new GripListener() {
            public boolean gripChanged(final BasicAttributeGrip basicAttributeGrip, final String s, final String s2) {
                final Point3f basePoint = new Point3f(basicAttributeGrip.getAbsolutePoint().x, 0.0f, 0.0f);
                ICDBasicOverhead.this.getEntWorldSpaceMatrix().transform(basePoint);
                ICDBasicOverhead.this.setBasePoint(basePoint);
                return true;
            }
        });
        this.leftWidthGrip.setCanLink(false);
        this.rightWidthGrip.setCanLink(false);
    }
    
    @Override
    public void updateGrips(final BasicAttributeGrip basicAttributeGrip) {
        if (this.getGeometricCenterPointWorld() != null) {
            final float oldWidth = this.oldWidth;
            final float oldDepth = this.oldDepth;
            if (basicAttributeGrip == null || basicAttributeGrip.equals(this.leftWidthGrip)) {
                final Attribute attributeObject = this.getAttributeObject("ICD_Overhead_Width");
                if (attributeObject != null) {
                    final AttributeGripPoint attributeGripPoint = new AttributeGripPoint(new Attribute[] { attributeObject });
                    this.leftWidthGrip.updateGrip(attributeGripPoint);
                    this.leftWidthGrip.setLocation(new Point3f(0.0f, -this.getYDimension() / 2.0f, this.getGeometricCenterPointWorld().z));
                    this.leftWidthGrip.setAnchorLocation(new Point3f(this.getWidth(), -this.getYDimension() / 2.0f, this.getGeometricCenterPointWorld().z));
                    this.leftWidthGrip.setChosenAttribute(attributeGripPoint);
                    this.leftWidthGrip.setActiveGripPoint(attributeGripPoint);
                }
            }
            if (basicAttributeGrip == null || basicAttributeGrip.equals(this.rightWidthGrip)) {
                final Attribute attributeObject2 = this.getAttributeObject("ICD_Overhead_Width");
                if (attributeObject2 != null) {
                    final AttributeGripPoint attributeGripPoint2 = new AttributeGripPoint(new Attribute[] { attributeObject2 });
                    this.rightWidthGrip.updateGrip(attributeGripPoint2);
                    this.rightWidthGrip.setLocation(new Point3f(this.getWidth(), -this.getYDimension() / 2.0f, this.getGeometricCenterPointWorld().z));
                    this.rightWidthGrip.setAnchorLocation(new Point3f(0.0f, -this.getYDimension() / 2.0f, this.getGeometricCenterPointWorld().z));
                    this.rightWidthGrip.setChosenAttribute(attributeGripPoint2);
                    this.rightWidthGrip.setActiveGripPoint(attributeGripPoint2);
                }
            }
        }
    }
    
    @Override
    public SortedSet<AttributeGripPoint> getAttributeMap(final BasicAttributeGrip basicAttributeGrip) {
        final TreeSet<AttributeGripPoint> set = new TreeSet<AttributeGripPoint>();
        if (this.getGeometricCenterPointWorld() != null) {
            if (basicAttributeGrip.equals(this.rightWidthGrip)) {
                final Attribute attributeObject = this.getAttributeObject("ICD_Overhead_Width");
                if (attributeObject != null) {
                    this.getCurrentType().getPossibleOptionVector();
                    final Vector vector = new Vector();
                    final float z = this.getGeometricCenterPointWorld().z;
                    final Iterator<PossibleValue> iterator = this.getAttributePossibleValues("ICD_Overhead_Width", false, this.getCurrentType()).iterator();
                    while (iterator.hasNext()) {
                        final String value = iterator.next().getValue();
                        final Attribute attribute = (Attribute)attributeObject.clone();
                        attribute.setCurrentValueAsString(value);
                        final int n = (int)Float.parseFloat(value);
                        set.add(new AttributeGripPoint(new Point3f((float)n, -this.getYDimension() / 2.0f, z), n, new Attribute[] { attribute }));
                    }
                }
            }
            else if (basicAttributeGrip.equals(this.leftWidthGrip)) {
                final Attribute attributeObject2 = this.getAttributeObject("ICD_Overhead_Width");
                if (attributeObject2 != null) {
                    this.getCurrentType().getPossibleOptionVector();
                    final Vector vector2 = new Vector();
                    final float z2 = this.getGeometricCenterPointWorld().z;
                    final Iterator<PossibleValue> iterator2 = this.getAttributePossibleValues("ICD_Overhead_Width", false, this.getCurrentType()).iterator();
                    while (iterator2.hasNext()) {
                        final String value2 = iterator2.next().getValue();
                        final Attribute attribute2 = (Attribute)attributeObject2.clone();
                        attribute2.setCurrentValueAsString(value2);
                        final int n2 = -(int)Float.parseFloat(value2);
                        set.add(new AttributeGripPoint(new Point3f(n2 + this.getWidth(), -this.getYDimension() / 2.0f, z2), n2, new Attribute[] { attribute2 }));
                    }
                }
            }
        }
        return set;
    }
    
    @Override
    protected void deselectGrips() {
        super.deselectGrips();
        if (this.rightWidthGrip != null && this.leftWidthGrip != null) {
            this.rightWidthGrip.setSelected(false);
            this.leftWidthGrip.setSelected(false);
        }
    }
    
    @Override
    protected void destroyGrips() {
        super.destroyGrips();
        if (this.rightWidthGrip != null && this.leftWidthGrip != null) {
            this.rightWidthGrip.destroy();
            this.leftWidthGrip.destroy();
        }
    }
    
    @Override
    public void setSelected(final boolean b, final Solution solution) {
        super.setSelected(b, solution);
        if (this.rightWidthGrip != null) {
            this.rightWidthGrip.setSelected(b);
        }
        if (this.leftWidthGrip != null) {
            this.leftWidthGrip.setSelected(b);
        }
    }
    
    @Override
    public List<BasicAttributeGrip> getGrips() {
        final LinkedList<BasicAttributeGrip> list = new LinkedList<BasicAttributeGrip>();
        list.add(this.rightWidthGrip);
        list.add(this.leftWidthGrip);
        return list;
    }
    
    @Override
    public void draw2D(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        super.draw2D(n, ice2DContainer, solutionSetting);
        if (this.rightWidthGrip != null) {
            this.rightWidthGrip.draw2D(n, ice2DContainer, solutionSetting);
        }
        if (this.leftWidthGrip != null) {
            this.leftWidthGrip.draw2D(n, ice2DContainer, solutionSetting);
        }
    }
    
    @Override
    public void destroy() {
        super.destroy();
        if (this.rightWidthGrip != null) {
            this.rightWidthGrip.destroy();
        }
        if (this.leftWidthGrip != null) {
            this.leftWidthGrip.destroy();
        }
    }
    
    @Override
    public void solve() {
        final String anObject = (this.currentCatalogOption == null) ? "NULL" : this.currentCatalogOption.getId();
        if (this.isModified()) {
            this.modifyCurrentOption();
            this.validateChildTypes();
        }
        super.solve();
        this.handleSKUGeneration();
        if (!((this.currentCatalogOption == null) ? "NULL" : this.currentCatalogOption.getId()).equals(anObject)) {
            super.solve();
        }
        this.updateGrips(null);
    }
    
    @Override
    public void handleScaleChanges(final float n, final float n2, final float n3, final float n4) {
    }
    
    public void handleWarnings() {
    }
    
    @Override
    protected boolean shouldModify() {
        return false;
    }
    
    @Override
    public boolean isFixed() {
        return true;
    }
    
    public String getDefaultLayerName() {
        return "Overheads";
    }
    
    @Override
    public void calculateBoundingCube() {
        this.boundingCube.setUpper(this.getXDimension(), 0.0f, this.getZDimension());
        this.boundingCube.setLower(0.0f, -this.getYDimension(), 0.0f);
    }
    
    @Override
    protected void setupNamedPoints() {
        super.setupNamedPoints();
        this.addNamedPoint("ASE_POS", new Point3f());
        this.addNamedRotation("ASE_ROT", new Vector3f());
        this.addNamedPoint("Top_Right_Back", new Point3f());
        this.addNamedPoint("Bottom_Right_Back", new Point3f());
        this.addNamedPoint("Top_Left_Back", new Point3f());
        this.addNamedPoint("Bottom_Left_Back", new Point3f());
        this.addNamedPoint("Center_Point", new Point3f());
    }
    
    @Override
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("ASE_POS").set(0.0f, 0.0f, this.getZDimension());
        this.getNamedPointLocal("Bottom_Right_Back").set(this.getXDimension(), 0.0f, 0.0f);
        this.getNamedPointLocal("Top_Right_Back").set(this.getXDimension(), 0.0f, this.getZDimension());
        this.getNamedPointLocal("Bottom_Left_Back").set(0.0f, 0.0f, 0.0f);
        this.getNamedPointLocal("Top_Left_Back").set(0.0f, 0.0f, this.getZDimension());
        this.getNamedPointLocal("Center_Point").set(this.getXDimension() / 2.0f, -this.getYDimension(), this.getZDimension() / 2.0f);
    }
    
    @Override
    protected void calculateNamedRotations() {
        super.calculateNamedRotations();
        this.getNamedRotationLocal("ASE_ROT").set((float)Math.toRadians(-90.0), (float)Math.toRadians(-270.0), (float)Math.toRadians(-180.0));
    }
    
    public float getDistanceFromGround() {
        return this.getHeight();
    }
    
    @Override
    public String getHeightAttributeKey() {
        return "ICD_Overhead_Height";
    }
    
    protected SimpleSnapper addSimpleSnappablesThatDoNotFilterOccupiedTargets(final boolean b, SimpleSnapper addSimpleSnappablesThatDoNotFilterOccupiedTargets) {
        addSimpleSnappablesThatDoNotFilterOccupiedTargets = super.addSimpleSnappablesThatDoNotFilterOccupiedTargets(true, addSimpleSnappablesThatDoNotFilterOccupiedTargets);
        final Point3f namedPointLocal = this.getNamedPointLocal("Top_Left_Snap_Corner");
        final Point3f namedPointLocal2 = this.getNamedPointLocal("Top_Right_Snap_Corner");
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("BRL", namedPointLocal2, Float.valueOf(this.getSnapRotation()), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TLR", namedPointLocal2, Float.valueOf((float)Math.toRadians(180.0) + this.getSnapRotation()), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("BLR", namedPointLocal, Float.valueOf(this.getSnapRotation()), 20.0f, true);
        addSimpleSnappablesThatDoNotFilterOccupiedTargets.addPointToPointSnapRule("TRL", namedPointLocal, Float.valueOf((float)Math.toRadians(180.0) + this.getSnapRotation()), 20.0f, true);
        return addSimpleSnappablesThatDoNotFilterOccupiedTargets;
    }
    
    @Override
    public float getHeightModifier() {
        return 0.0f;
    }
    
    @Override
    public BoundingCube getCubeForTriggers() {
        return null;
    }
    
    @Override
    public void populateCompareNode(final Class clazz, final CompareNode compareNode) {
        super.populateCompareNode(clazz, compareNode);
        if (ManufacturingReportable.class.isAssignableFrom(clazz)) {
            this.populateCompareNodeForICD(clazz, compareNode);
        }
    }
    
    @Override
    public void populateCompareNodeForICD(final Class clazz, final CompareNode compareNode) {
        compareNode.addCompareValue("finish", (Object)((BasicMaterialEntity)this.getChildByClass((Class)BasicMaterialEntity.class)).getDescription());
        compareNode.addCompareValue("usertag", (Object)this.getUserTagNameAttribute("TagName1"));
    }
    
    @Override
    public boolean isCustomizable() {
        return false;
    }
    
    public void setModified(final boolean b) {
        if (b) {
            this.setChildrenModified(b);
        }
        super.setModified(b);
    }
}
