package net.iceedge.catalogs.icd.worksurfaces;

import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDWSB12ParametricWorksurface extends ICDWSBParametricWorksurface implements ICD2Widths2DepthsGrippable
{
    private static final float bottomArcRadius = 12.0f;
    
    public ICDWSB12ParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDWSB12ParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDWSB12ParametricWorksurface buildClone(final ICDWSB12ParametricWorksurface icdwsb12ParametricWorksurface) {
        super.buildClone(icdwsb12ParametricWorksurface);
        icdwsb12ParametricWorksurface.calculateParameters();
        return icdwsb12ParametricWorksurface;
    }
    
    @Override
    protected float getBottomArcRadius() {
        return 12.0f;
    }
    
    @Override
    public String getShapeTag() {
        return "WSB12";
    }
}
