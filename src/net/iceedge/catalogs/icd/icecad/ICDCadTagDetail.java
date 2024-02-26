// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.icecad;

import net.iceedge.icecore.basemodule.util.ElevationUtility;
import javax.vecmath.Point3f;

public class ICDCadTagDetail
{
    private final String tagInfo;
    private final float fontHeight;
    private final float rotation;
    private final Point3f tagInsertionPoint;
    
    public ICDCadTagDetail(final ICDCadTagPaintable icdCadTagPaintable, final String tagInfo) {
        this.tagInfo = tagInfo;
        this.fontHeight = icdCadTagPaintable.getSolution().getTagFontSize() / 2.0f;
        this.rotation = ElevationUtility.normalizeTextRotation(icdCadTagPaintable.getRotationWorldSpace());
        (this.tagInsertionPoint = new Point3f(icdCadTagPaintable.getGeometricCenterPointLocal())).set(this.tagInsertionPoint.x, this.tagInsertionPoint.y + 5.0f, this.tagInsertionPoint.z);
        icdCadTagPaintable.getEntWorldSpaceMatrix().transform(this.tagInsertionPoint);
    }
    
    public boolean isValid() {
        return this.tagInfo != null && this.tagInfo.length() > 0;
    }
    
    public String getTagInfo() {
        return this.tagInfo;
    }
    
    public float getFontHeight() {
        return this.fontHeight;
    }
    
    public float getRotation() {
        return this.rotation;
    }
    
    public Point3f getTagInsertionPoint() {
        return this.tagInsertionPoint;
    }
}
