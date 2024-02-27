package net.iceedge.catalogs.icd.icecad;

import javax.vecmath.Point3f;
import net.iceedge.icecore.icecad.ice.tree.IceCadCompositeBlock;
import net.dirtt.icelib.main.TransformableEntity;
import net.iceedge.icecore.icecad.ice.tree.IceCadSymbolTableRecord;
import net.iceedge.icecore.icecad.ice.tree.CadLayer;
import net.iceedge.icecore.icecad.ice.tree.IceCadNodeContainer;
import net.iceedge.icecore.basemodule.interfaces.lightweight.CADPaintable;
import net.dirtt.icecad.cadtree.ICadTreeNode;
import net.iceedge.icecore.icecad.ice.tree.IceCadMTextNode;
import net.dirtt.icecad.cadtree.ICadMTextNode;

public class ICDCadTagDelegate
{
    private static final float LINE_SPACING = 0.75f;
    private static final int ATTACHMENT_POINT = 5;
    private final ICDCadTagPaintable tagPaintable;
    private ICadMTextNode cadPlanTagNode;
    private IceCadMTextNode iceCadPlanTagNode;
    private IceCadMTextNode iceCadPlanTagNodeForProxyEntity;
    
    public ICDCadTagDelegate(final ICDCadTagPaintable tagPaintable) {
        this.tagPaintable = tagPaintable;
    }
    
    public void drawCad(final ICadTreeNode cadTreeNode, final String s) {
        final ICDCadTagDetail icdCadTagDetail = new ICDCadTagDetail(this.tagPaintable, s);
        if (icdCadTagDetail.isValid()) {
            if (this.cadPlanTagNode == null) {
                cadTreeNode.addChild((ICadTreeNode)(this.cadPlanTagNode = new ICadMTextNode((CADPaintable)this.tagPaintable, icdCadTagDetail.getTagInfo(), icdCadTagDetail.getTagInsertionPoint(), 0.75f, 5, icdCadTagDetail.getFontHeight(), this.tagPaintable.getXDimension(), icdCadTagDetail.getRotation())));
                this.cadPlanTagNode.setScheduledAction(0);
            }
            else {
                this.cadPlanTagNode.setText(icdCadTagDetail.getTagInfo());
                this.cadPlanTagNode.setIP(icdCadTagDetail.getTagInsertionPoint());
                this.cadPlanTagNode.setFontHeight(icdCadTagDetail.getFontHeight());
                this.cadPlanTagNode.setRotation(icdCadTagDetail.getRotation());
                this.cadPlanTagNode.setScheduledAction(2);
            }
        }
        else if (this.cadPlanTagNode != null) {
            this.destroyCad();
        }
    }
    
    public void drawIceCadDotNet(final IceCadNodeContainer iceCadNodeContainer, final String s) {
        final ICDCadTagDetail icdCadTagDetail = new ICDCadTagDetail(this.tagPaintable, s);
        if (icdCadTagDetail.isValid()) {
            if (this.iceCadPlanTagNode == null) {
                this.iceCadPlanTagNode = new IceCadMTextNode(iceCadNodeContainer, (IceCadSymbolTableRecord)CadLayer.LAYER_TAG.getLayer(), icdCadTagDetail.getTagInfo(), icdCadTagDetail.getFontHeight(), icdCadTagDetail.getRotation(), icdCadTagDetail.getTagInsertionPoint(), this.tagPaintable.getXDimension());
            }
            else {
                this.iceCadPlanTagNode.setText(icdCadTagDetail.getTagInfo());
                this.iceCadPlanTagNode.setPosition(icdCadTagDetail.getTagInsertionPoint());
                this.iceCadPlanTagNode.setTextHeight(icdCadTagDetail.getFontHeight());
                this.iceCadPlanTagNode.setRotation(icdCadTagDetail.getRotation());
            }
        }
        else if (this.iceCadPlanTagNode != null) {
            this.destroyCad();
        }
    }
    
    public void drawIceCadForProxyEntityDotNet(final IceCadNodeContainer iceCadNodeContainer, final String s, final TransformableEntity transformableEntity, final IceCadCompositeBlock iceCadCompositeBlock) {
        final ICDCadTagDetail icdCadTagDetail = new ICDCadTagDetail(this.tagPaintable, s);
        final Point3f tagInsertionPoint = icdCadTagDetail.getTagInsertionPoint();
        if (icdCadTagDetail.isValid()) {
            if (this.iceCadPlanTagNodeForProxyEntity == null) {
                this.iceCadPlanTagNodeForProxyEntity = new IceCadMTextNode(iceCadNodeContainer, (IceCadSymbolTableRecord)iceCadCompositeBlock, icdCadTagDetail.getTagInfo(), icdCadTagDetail.getFontHeight(), icdCadTagDetail.getRotation(), tagInsertionPoint, this.tagPaintable.getXDimension());
            }
            else {
                this.iceCadPlanTagNodeForProxyEntity.setText(icdCadTagDetail.getTagInfo());
                this.iceCadPlanTagNodeForProxyEntity.setPosition(tagInsertionPoint);
                this.iceCadPlanTagNodeForProxyEntity.setTextHeight(icdCadTagDetail.getFontHeight());
                this.iceCadPlanTagNodeForProxyEntity.setRotation(icdCadTagDetail.getRotation());
            }
        }
        else if (this.iceCadPlanTagNodeForProxyEntity != null) {
            this.destroyCad();
        }
    }
    
    public void destroyCad() {
        if (this.cadPlanTagNode != null) {
            this.cadPlanTagNode.setScheduledAction(1);
            this.cadPlanTagNode = null;
        }
        if (this.iceCadPlanTagNode != null) {
            this.iceCadPlanTagNode.erase();
        }
        if (this.iceCadPlanTagNodeForProxyEntity != null) {
            this.iceCadPlanTagNodeForProxyEntity.erase();
        }
    }
    
    public void finalDestroyCad() {
        if (this.iceCadPlanTagNode != null) {
            this.iceCadPlanTagNode.destroy();
            this.iceCadPlanTagNode = null;
        }
        if (this.iceCadPlanTagNodeForProxyEntity != null) {
            this.iceCadPlanTagNodeForProxyEntity.destroy();
            this.iceCadPlanTagNodeForProxyEntity = null;
        }
    }
}
