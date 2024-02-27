package com.iceedge.icd.typefilters;

import net.iceedge.catalogs.icd.intersection.ICDChaseMidConnectorContainer;
import net.dirtt.utilities.TypeFilter;

public class ICDChaseMidConnectorContainerTypeFilter implements TypeFilter<ICDChaseMidConnectorContainer>
{
    public ICDChaseMidConnectorContainer get(final Object o) {
        return (ICDChaseMidConnectorContainer)o;
    }
    
    public boolean matches(final Object o) {
        return o instanceof ICDChaseMidConnectorContainer;
    }
}
