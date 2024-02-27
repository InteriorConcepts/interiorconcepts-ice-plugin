package net.iceedge.catalogs.icd.worksurfaces;

import icd.warnings.WarningReason0280;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDWireDip;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDParametricShelf extends ICDParametricDeckOrShelf
{
    public ICDParametricShelf(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
    }
    
    public ICDParametricShelf(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDParametricShelf(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDParametricShelf buildClone(final ICDParametricShelf icdParametricShelf) {
        super.buildClone(icdParametricShelf);
        return icdParametricShelf;
    }
    
    @Override
    public String getShapeTag() {
        return "Shelf";
    }
    
    @Override
    protected void addWireDip(final ICDWireDip icdWireDip) {
        if (this.getWireDips().size() == 0) {
            super.addWireDip(icdWireDip);
        }
    }
    
    public void handleWarnings() {
        super.handleWarnings();
        WarningReason0280.addRequiredWarning(this);
    }
}
