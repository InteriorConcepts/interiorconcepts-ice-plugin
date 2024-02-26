// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWVSRRParametricWorksurface extends ICDWVCRLParametricWorksurface
{
    public ICDWVSRRParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public ICDWVSRRParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVSRRParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWVSRRParametricWorksurface buildClone(final ICDWVSRRParametricWorksurface icdwvsrrParametricWorksurface) {
        super.buildClone(icdwvsrrParametricWorksurface);
        icdwvsrrParametricWorksurface.calculateParameters();
        return icdwvsrrParametricWorksurface;
    }
    
    @Override
    public String getShapeTag() {
        return this.getAttributeValueAsString("ShapeTag", "WVSRR");
    }
}
