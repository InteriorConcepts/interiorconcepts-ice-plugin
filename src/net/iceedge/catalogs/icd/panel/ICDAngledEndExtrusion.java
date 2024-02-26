// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import java.util.Vector;
import net.dirtt.icelib.main.ElevationEntity;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDAngledEndExtrusion extends ICDEndExtrusion
{
    public ICDAngledEndExtrusion(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDAngledEndExtrusion(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDAngledEndExtrusion buildClone(final ICDAngledEndExtrusion icdAngledEndExtrusion) {
        super.buildClone(icdAngledEndExtrusion);
        return icdAngledEndExtrusion;
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDAngledEndExtrusion(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDAngledEndExtrusion buildFrameClone(final ICDAngledEndExtrusion icdAngledEndExtrusion, final EntityObject entityObject) {
        super.buildFrameClone(icdAngledEndExtrusion, entityObject);
        return icdAngledEndExtrusion;
    }
    
    @Override
    protected void calculateGeometricCenter() {
        super.calculateGeometricCenter();
        this.setGeometricCenterPointLocal(new Point3f(0.0f, 0.0f, this.getZDimension() / 2.0f));
    }
    
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        return new Vector<String>();
    }
    
    public boolean isFakePart() {
        return this.getCurrentOption().getId().toLowerCase().indexOf("special") > -1;
    }
}
