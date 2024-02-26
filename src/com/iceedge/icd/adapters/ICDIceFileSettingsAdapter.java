// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.adapters;

import net.dirtt.icelib.main.Solution;
import com.iceedge.icebox.icecore.system.settings.plugin.IceSettingsCategoryDefinition;
import com.iceedge.icebox.icecore.system.settings.plugin.DefaultIceFileSettingsAdapter;

public class ICDIceFileSettingsAdapter extends DefaultIceFileSettingsAdapter
{
    private final String CATEGORY_NAME = "Interior Concepts";
    
    public IceSettingsCategoryDefinition getCategoryDefinitions() {
        final IceSettingsCategoryDefinition iceSettingsCategoryDefinition = new IceSettingsCategoryDefinition();
        this.addPropertyDefinition(iceSettingsCategoryDefinition, "ICD_Rev_Number", true, true, "Interior Concepts", "Interior Concepts");
        this.addPropertyDefinition(iceSettingsCategoryDefinition, "ICD_Record_Id", true, true, "Interior Concepts", "Interior Concepts");
        return iceSettingsCategoryDefinition;
    }
    
    private void addPropertyDefinition(final IceSettingsCategoryDefinition iceSettingsCategoryDefinition, final String s, final boolean b, final boolean b2, final String s2, final String s3) {
        if (s != null && Solution.attributeProxyByName(s) != null) {
            iceSettingsCategoryDefinition.addPropertyDefintion(s, b, b2, s2, s2);
        }
    }
}
