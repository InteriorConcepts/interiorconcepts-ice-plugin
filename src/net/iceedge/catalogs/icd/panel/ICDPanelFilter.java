package net.iceedge.catalogs.icd.panel;

import net.dirtt.utilities.TypeFilter;

public class ICDPanelFilter implements TypeFilter<ICDPanel>
{
    public ICDPanel get(final Object o) {
        return (ICDPanel)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDPanel;
    }
}
