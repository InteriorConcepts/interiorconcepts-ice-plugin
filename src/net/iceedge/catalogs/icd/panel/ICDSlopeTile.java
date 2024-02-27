package net.iceedge.catalogs.icd.panel;

import java.awt.Color;
import java.awt.geom.Path2D;
import net.dirtt.icebox.canvas2d.Ice2DNode;
import java.awt.Shape;
import net.dirtt.icelib.main.TransformableEntity;
import net.dirtt.icebox.canvas2d.ClippedImageNode;
import java.awt.geom.AffineTransform;
import javax.vecmath.Matrix4f;
import java.awt.image.BufferedImage;
import net.dirtt.icelib.main.SolutionSetting;
import net.dirtt.icebox.canvas2d.Ice2DContainer;
import net.iceedge.icecore.basemodule.interfaces.panels.PanelInterface;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDSlopeTile extends ICDTile
{
    public static final float[] ICD_SLOPE_TILE_42_MELAMINE_CUTT_SIZE;
    public static final float[] ICD_SLOPE_TILE_42_FABRIC_CUTT_SIZE;
    public static final float[] ICD_SLOPE_TILE_24_MELAMINE_CUTT_SIZE;
    public static final float[] ICD_SLOPE_TILE_24_FABRIC_CUTT_SIZE;
    
    public ICDSlopeTile(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDSlopeTile(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDSlopeTile buildClone(final ICDSlopeTile icdSlopeTile) {
        super.buildClone(icdSlopeTile);
        return icdSlopeTile;
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDSlopeTile(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDSlopeTile buildFrameClone(final ICDSlopeTile icdSlopeTile, final EntityObject entityObject) {
        super.buildFrameClone(icdSlopeTile, entityObject);
        return icdSlopeTile;
    }
    
    @Override
    protected void validateIndicators() {
        super.validateIndicators();
        final PanelInterface parentPanel = this.getParentPanel();
        if (parentPanel instanceof ICDPanel) {
            if (((ICDPanel)parentPanel).getWidth() > 30.0f) {
                this.applyChangesForAttribute("ICD_Slope_Tile_Width", "42");
            }
            else {
                this.applyChangesForAttribute("ICD_Slope_Tile_Width", "24");
            }
        }
    }
    
    public void draw2DImageNode(final int n, final Ice2DContainer ice2DContainer, final SolutionSetting solutionSetting) {
        final int side = ice2DContainer.getSide();
        BufferedImage bufferedImage = this.createElevationImage(ice2DContainer, side);
        if (bufferedImage == null) {
            bufferedImage = this.createElevationImage();
        }
        if (bufferedImage != null) {
            this.draw2DElevationImageNode(ice2DContainer, bufferedImage, this.getEntWorldSpaceMatrix());
            this.drawTileDivider(ice2DContainer, side);
            this.drawSelectionCubeNode(ice2DContainer, "Elevation Selection", this.getEntWorldSpaceMatrix());
            switch (side) {
                case 0: {
                    this.drawSelectionLabelNode(ice2DContainer, false);
                    this.refreshTextNodes(n, ice2DContainer, solutionSetting);
                    break;
                }
                case 1: {
                    this.drawSelectionLabelNode(ice2DContainer, true);
                    break;
                }
            }
        }
    }
    
    private void draw2DElevationImageNode(final Ice2DContainer ice2DContainer, final BufferedImage bufferedImage, final Matrix4f matrix4f) {
        final ClippedImageNode clippedImageNode = new ClippedImageNode(this.getElevationLayerName(), (TransformableEntity)this, matrix4f, bufferedImage, (Shape)this.getPanelShape(), 1.0f, 1.0f, new AffineTransform());
        if (this.is24TileWidth()) {
            clippedImageNode.setOutlineStrokeWidth(24.0f);
        }
        ice2DContainer.add((Ice2DNode)clippedImageNode);
        if (this.hasVisibleWarning()) {
            this.drawAngryElevation(ice2DContainer, matrix4f);
        }
    }
    
    private Path2D.Float getPanelShape() {
        final Path2D.Float float1 = new Path2D.Float();
        if (this.is24TileWidth()) {
            float1.moveTo(-14.0f, 270.0f);
            float1.lineTo(-14.0f, -17.0f);
            float1.lineTo(270.0f, 120.0f);
            float1.lineTo(270.0f, 275.0f);
            float1.lineTo(-14.0f, 275.0f);
        }
        else {
            float1.moveTo(-7.5, 270.0);
            float1.lineTo(-7.5, -17.0);
            float1.lineTo(264.0f, 180.0f);
            float1.lineTo(264.0f, 270.0f);
            float1.lineTo(-7.5, 270.0);
        }
        return float1;
    }
    
    public boolean is24TileWidth() {
        return this.getAttributeValueAsFloat("ICD_Slope_Tile_Width") == 24.0f;
    }
    
    public Color getClippedImageColor() {
        return Color.lightGray;
    }
    
    static {
        ICD_SLOPE_TILE_42_MELAMINE_CUTT_SIZE = new float[] { 40.56f, 4.94f, 43.61f, 20.97f };
        ICD_SLOPE_TILE_42_FABRIC_CUTT_SIZE = new float[] { 40.43f, 4.81f, 43.49f, 20.84f };
        ICD_SLOPE_TILE_24_MELAMINE_CUTT_SIZE = new float[] { 22.56f, 12.08f, 24.25f, 20.97f };
        ICD_SLOPE_TILE_24_FABRIC_CUTT_SIZE = new float[] { 22.43f, 11.95f, 24.13f, 20.84f };
    }
}
