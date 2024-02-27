package net.iceedge.catalogs.icd.panel;

import java.util.Vector;
import net.dirtt.icelib.main.ElevationEntity;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDAngledStartExtrusion extends ICDStartExtrusion
{
    public ICDAngledStartExtrusion(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDAngledStartExtrusion(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDAngledStartExtrusion buildClone(final ICDAngledStartExtrusion icdAngledStartExtrusion) {
        super.buildClone(icdAngledStartExtrusion);
        return icdAngledStartExtrusion;
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDAngledStartExtrusion(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDAngledStartExtrusion buildFrameClone(final ICDAngledStartExtrusion icdAngledStartExtrusion, final EntityObject entityObject) {
        super.buildFrameClone(icdAngledStartExtrusion, entityObject);
        return icdAngledStartExtrusion;
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
