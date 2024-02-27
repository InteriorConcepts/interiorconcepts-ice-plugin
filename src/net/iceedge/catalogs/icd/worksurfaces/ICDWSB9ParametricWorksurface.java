package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWSB9ParametricWorksurface extends ICDWSBParametricWorksurface implements ICD2Widths2DepthsGrippable
{
    private static final float bottomArcRadius = 9.0f;
    
    public ICDWSB9ParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWSB9ParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWSB9ParametricWorksurface buildClone(final ICDWSB9ParametricWorksurface icdwsb9ParametricWorksurface) {
        super.buildClone(icdwsb9ParametricWorksurface);
        icdwsb9ParametricWorksurface.calculateParameters();
        return icdwsb9ParametricWorksurface;
    }
    
    @Override
    protected float getBottomArcRadius() {
        return 9.0f;
    }
    
    @Override
    public String getShapeTag() {
        return "WSB9";
    }
}
