package net.iceedge.catalogs.icd.panel;

import net.dirtt.utilities.TypeFilter;

public class ICDPanelSubILineFilter implements TypeFilter<ICDPanelSubILine>
{
    public ICDPanelSubILine get(final Object o) {
        return (ICDPanelSubILine)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDPanelSubILine;
    }
}
