// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.interfaces;

import net.dirtt.icelib.main.SolutionSetting;
import java.util.TreeMap;

public interface ICDInstallTagDrawable
{
    String getInstallTag();
    
    void getManufacturingInfo(final TreeMap<String, String> p0);
    
    String getMultiTags(final SolutionSetting p0);
}
