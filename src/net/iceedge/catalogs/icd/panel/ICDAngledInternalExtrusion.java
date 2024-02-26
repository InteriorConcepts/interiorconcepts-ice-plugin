// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import java.util.Vector;
import net.dirtt.icelib.main.ElevationEntity;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDAngledInternalExtrusion extends ICDInternalExtrusion
{
    public ICDAngledInternalExtrusion(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDAngledInternalExtrusion(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDAngledInternalExtrusion buildClone(final ICDAngledInternalExtrusion icdAngledInternalExtrusion) {
        super.buildClone(icdAngledInternalExtrusion);
        return icdAngledInternalExtrusion;
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDAngledInternalExtrusion(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDAngledInternalExtrusion buildFrameClone(final ICDAngledInternalExtrusion icdAngledInternalExtrusion, final EntityObject entityObject) {
        super.buildFrameClone(icdAngledInternalExtrusion, entityObject);
        return icdAngledInternalExtrusion;
    }
    
    @Override
    protected Vector<String> getCadElevationScript(final ElevationEntity elevationEntity) {
        return new Vector<String>();
    }
}
