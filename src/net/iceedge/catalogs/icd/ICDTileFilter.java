package net.iceedge.catalogs.icd;

import net.iceedge.catalogs.icd.panel.ICDTile;
import net.dirtt.utilities.TypeFilter;

public class ICDTileFilter implements TypeFilter<ICDTile>
{
    public ICDTile get(final Object o) {
        return (ICDTile)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDTile;
    }
}
