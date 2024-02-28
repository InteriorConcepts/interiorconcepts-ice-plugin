package net.iceedge.catalogs.icd.panel;

import net.iceedge.catalogs.icd.intersection.ICDPost;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import java.util.Iterator;
import net.iceedge.icecore.basemodule.baseclasses.material.BasicMaterialEntity;
import java.util.ArrayList;
import java.util.List;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDCurvedTile extends ICDTile
{
    public ICDCurvedTile(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDCurvedTile(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDCurvedTile buildClone(final ICDCurvedTile icdCurvedTile) {
        super.buildClone(icdCurvedTile);
        return icdCurvedTile;
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDCurvedTile(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDCurvedTile buildFrameClone(final ICDCurvedTile icdCurvedTile, final EntityObject entityObject) {
        super.buildFrameClone(icdCurvedTile, entityObject);
        return icdCurvedTile;
    }
    
    @Override
    protected void calculateNamedScales() {
        super.calculateNamedScales();
        float n;
        float n2;
        float n3;
        if (!this.isPanelWithHorisontalInnerExtrusion()) {
            n = 1.025f;
            n2 = 1.025f;
            n3 = (float)((this.getYDimension() + 1.0f) / 46.0f * 0.972);
        }
        else if ("ICD_Curved_Tile_Bottom".equals(this.getCurrentOption().getId())) {
            n = 1.03f;
            n2 = 1.025f;
            n3 = (float)((this.getYDimension() + 1.0f) / 46.0f * 0.94);
        }
        else {
            n = 1.025f;
            n2 = 1.025f;
            n3 = (float)((this.getYDimension() + 1.0f) / 46.0f * 0.95);
        }
        this.getNamedScaleLocal("ASE_scale").set(n, n3, n2);
    }
    
    protected boolean isInCorePanel() {
        return true;
    }
    
    public boolean isPrintable() {
        return false;
    }
    
    @Override
    public List<String> getCodeList() {
        final ArrayList<String> list = new ArrayList<String>();
        for (final BasicMaterialEntity basicMaterialEntity : this.getChildrenByClass(BasicMaterialEntity.class, false)) {
            for (int i = 1; i <= 2; ++i) {
                final String attributeValueAsString = basicMaterialEntity.getAttributeValueAsString("CURVE_TILE_FINISH_SIDE_" + i + "_POSTION_" + this.getVerticalLocation());
                if (attributeValueAsString != null && attributeValueAsString.length() > 0) {
                    list.add(attributeValueAsString);
                }
            }
        }
        return list;
    }
    
    public void draw2D(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
    }
    
    @Override
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        final float n = 0.5f + this.getWidth() / 100.0f;
        if (this.isStackedTile()) {
            this.getNamedPointLocal("ASE_pos").set(n, 0.2f, 0.0f);
        }
        else {
            this.getNamedPointLocal("ASE_pos").set(n, 1.0f, 0.0f);
        }
        final Point3f namedPointLocal = this.getNamedPointLocal("geometricCenterPoint");
        if (namedPointLocal != null) {
            this.getNamedPointLocal("frontStartTopCornerLocal").set(namedPointLocal.x - this.getXDimension() / 2.0f, namedPointLocal.y + this.getYDimension() / 2.0f, -this.getXDimension() + 1.35f);
            this.getNamedPointLocal("frontEndTopCornerLocal").set(namedPointLocal.x + this.getXDimension() / 2.0f, namedPointLocal.y + this.getYDimension() / 2.0f, namedPointLocal.z + this.getZDimension() / 2.0f);
        }
    }
    
    public void handleCadSelection() {
        super.handleCadSelection();
        final ICDPost icdPost = (ICDPost)this.getParent(ICDPost.class);
        if (icdPost != null) {
            icdPost.setSelected(true);
        }
    }
}
