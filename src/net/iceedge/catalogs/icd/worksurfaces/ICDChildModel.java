// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import org.xith3d.scenegraph.Group;
import java.util.Collection;
import java.util.LinkedList;
import net.dirtt.icelib.main.TypeableEntity;
import java.util.List;
import net.dirtt.icelib.main.BoundingCube;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;

public class ICDChildModel extends TransformableEntity implements ICDManufacturingReportable
{
    protected float oldLength;
    protected float oldDepth;
    
    public ICDChildModel(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.oldLength = this.getOriginalXDimension();
        this.oldDepth = this.getOriginalYDimension();
        this.setupNamedPoints();
    }
    
    public TransformableEntity buildClone(final ICDChildModel icdChildModel) {
        super.buildClone((TransformableEntity)icdChildModel);
        return icdChildModel;
    }
    
    public TransformableEntity buildClone2(final ICDChildModel icdChildModel) {
        super.buildClone2((TransformableEntity)icdChildModel);
        return icdChildModel;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDChildModel(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDChildModel(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDChildModel icdChildModel, final EntityObject entityObject) {
        super.buildFrameClone((TransformableEntity)icdChildModel, entityObject);
        return (EntityObject)icdChildModel;
    }
    
    public void setSelected(final boolean b, final Solution solution) {
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity != null && parentEntity instanceof TransformableEntity) {
            ((TransformableEntity)parentEntity).setSelected(b, solution);
        }
        else {
            super.setSelected(b, solution);
        }
    }
    
    public String getDescription() {
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity != null) {
            return parentEntity.getDescription();
        }
        return super.getSKU();
    }
    
    public void scaleModels(final Vector3f vector3f, final float n, final float n2) {
        if (vector3f != null) {
            if (!this.isLockedOnX()) {
                final float originalXDimension = this.getOriginalXDimension();
                float n3;
                if (this.getXScalePercentage() == 0.0f) {
                    n3 = n / originalXDimension;
                }
                else {
                    n3 = this.getMyPercentageOfX() * n / originalXDimension;
                    final float n4 = originalXDimension * n3 + (this.getLockedPieceOriginalX() * n3 - this.getLockedPieceOriginalX());
                    if (n4 != 0.0f) {
                        n3 = n4 / originalXDimension;
                    }
                }
                vector3f.set(n3, vector3f.y, vector3f.z);
                this.moveSiblings('X', this.calcXMoveFactor(n3));
            }
            if (!this.isLockedOnY()) {
                final float originalYDimension = this.getOriginalYDimension();
                float n5;
                if (this.getYScalePercentage() == 0.0f) {
                    n5 = n2 / originalYDimension;
                }
                else {
                    n5 = this.getMyPercentageOfY() * n2 / originalYDimension;
                    final float n6 = originalYDimension * n5 + (this.getLockedPieceOriginalY() * n5 - this.getLockedPieceOriginalY());
                    if (n6 != 0.0f) {
                        n5 = n6 / originalYDimension;
                    }
                }
                vector3f.set(vector3f.x, n5, vector3f.z);
                this.moveSiblings('Y', this.calcYMoveFactor(n5));
            }
        }
        this.calculate();
    }
    
    protected float calcYMoveFactor(final float n) {
        return this.getOriginalYDimension() * n - this.getOriginalYDimension();
    }
    
    protected float calcXMoveFactor(final float n) {
        return this.getOriginalXDimension() * n - this.getOriginalXDimension();
    }
    
    public String getModelNumber() {
        return this.getAttributeValueAsString("ICD_Model_Number");
    }
    
    public boolean isLockedOnY() {
        return "true".equals(this.getAttributeValueAsString("ICD_Locked_On_Y"));
    }
    
    public boolean isLockedOnX() {
        return "true".equals(this.getAttributeValueAsString("ICD_Locked_On_X"));
    }
    
    public float getXScalePercentage() {
        return this.getAttributeValueAsFloat("ICD_Locked_X_Percentage");
    }
    
    public float getYScalePercentage() {
        return this.getAttributeValueAsFloat("ICD_Locked_Y_Percentage");
    }
    
    public boolean movesOnY() {
        return "true".equals(this.getAttributeValueAsString("ICD_Moves_On_Y"));
    }
    
    public boolean movesOnX() {
        return "true".equals(this.getAttributeValueAsString("ICD_Moves_On_X"));
    }
    
    public float getOriginalXDimension() {
        return this.getAttributeValueAsFloat("ICD_Original_X_Value");
    }
    
    public float getOriginalYDimension() {
        return this.getAttributeValueAsFloat("ICD_Original_Y_Value");
    }
    
    public float getLockedPieceOriginalX() {
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity == null || !(parentEntity instanceof ICDBasicWorksurface)) {
            return 0.0f;
        }
        final float attributeValueAsFloat = parentEntity.getAttributeValueAsFloat("ICD_Locked_Piece_Original_X");
        if (attributeValueAsFloat == 0.0f) {
            return this.getAttributeValueAsFloat("ICD_Locked_Piece_Original_X");
        }
        return attributeValueAsFloat;
    }
    
    public float getLockedPieceOriginalY() {
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity == null || !(parentEntity instanceof ICDBasicWorksurface)) {
            return 0.0f;
        }
        final float attributeValueAsFloat = parentEntity.getAttributeValueAsFloat("ICD_Locked_Piece_Original_Y");
        if (attributeValueAsFloat == 0.0f) {
            return this.getAttributeValueAsFloat("ICD_Locked_Piece_Original_Y");
        }
        return attributeValueAsFloat;
    }
    
    public float getMyPercentageOfX() {
        return this.getAttributeValueAsFloat("ICD_My_Percentage_Of_Total_X");
    }
    
    public float getMyPercentageOfY() {
        return this.getAttributeValueAsFloat("ICD_My_Percentage_Of_Total_Y");
    }
    
    public void moveSiblings(final char c, final float n) {
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity != null && parentEntity instanceof ICDBasicWorksurface) {
            ((ICDBasicWorksurface)parentEntity).moveChildren(c, n);
        }
    }
    
    protected void setupNamedPoints() {
        this.addNamedPoint("SVG_POS", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("ASE_POS", new Point3f(0.0f, 0.0f, 0.0f));
    }
    
    public void calculateBoundingCube() {
        final EntityObject parentEntity = this.getParentEntity();
        if (parentEntity != null && parentEntity instanceof TransformableEntity) {
            final TransformableEntity transformableEntity = (TransformableEntity)parentEntity;
            if (((TransformableEntity)parentEntity).getBoundingCube() != null) {
                final BoundingCube boundingCube = this.getBoundingCube();
                boundingCube.setLower(-0.5f, -transformableEntity.getYDimension(), -2.0f);
                boundingCube.setUpper(transformableEntity.getXDimension() + 0.5f, transformableEntity.getYDimension() + 0.5f, 2.0f);
            }
        }
    }
    
    public List<TypeableEntity> getFinishRoots() {
        final LinkedList<ICDChildModel> list = new LinkedList<ICDChildModel>();
        list.addAll((Collection<?>)((TypeableEntity)this.getParent((Class)TypeableEntity.class)).getFinishRoots());
        list.add(this);
        return (List<TypeableEntity>)list;
    }
    
    public void draw3D(final Group group, final int n) {
        super.draw3D(group, n);
    }
    
    protected boolean shouldUseNewFinishSystem() {
        return true;
    }
    
    public String getDefaultLayerName() {
        return "Worksurfaces";
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addDimensionsToManufacturingTreeMap(treeMap, this);
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
