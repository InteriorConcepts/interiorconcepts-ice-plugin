package com.iceedge.icd.adapters;

import net.iceedge.icecore.plugin.systemInterfaces.IceCoreFeatureAdapter;

public class ICDCoreFeatureAdapter extends IceCoreFeatureAdapter
{
    public boolean enableImportSifOptionsCommand() {
        return true;
    }
    
    public boolean enableShowCablesInElevations() {
        return true;
    }
}
