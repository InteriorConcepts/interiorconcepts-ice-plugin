package net.iceedge.catalogs.icd.worksurfaces;

import icd.warnings.WarningReason0279;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDWireDip;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDParametricDeck extends ICDParametricDeckOrShelf
{
    public ICDParametricDeck(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
    }
    
    public ICDParametricDeck(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDParametricDeck(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDParametricDeck buildClone(final ICDParametricDeck icdParametricDeck) {
        super.buildClone(icdParametricDeck);
        return icdParametricDeck;
    }
    
    @Override
    public String getShapeTag() {
        return "Deck";
    }
    
    @Override
    protected void addWireDip(final ICDWireDip icdWireDip) {
        if (this.getWireDips().size() == 0) {
            super.addWireDip(icdWireDip);
        }
    }
    
    public void handleWarnings() {
        super.handleWarnings();
        WarningReason0279.addRequiredWarning(this);
    }
}
