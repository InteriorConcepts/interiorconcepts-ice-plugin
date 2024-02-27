package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWVSRLParametricWorksurface extends ICDWVCRRParametricWorksurface
{
    public ICDWVSRLParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public ICDWVSRLParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWVSRLParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWVSRLParametricWorksurface buildClone(final ICDWVSRLParametricWorksurface icdwvsrlParametricWorksurface) {
        super.buildClone(icdwvsrlParametricWorksurface);
        icdwvsrlParametricWorksurface.calculateParameters();
        return icdwvsrlParametricWorksurface;
    }
    
    @Override
    public String getShapeTag() {
        return this.getAttributeValueAsString("ShapeTag", "WVSRL");
    }
}
