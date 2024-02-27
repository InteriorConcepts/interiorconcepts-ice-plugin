package com.iceedge.icd.entities.extrusions;

import java.util.TreeMap;

public interface ICDExtrusionInterface
{
    String getReportDescription();
    
    void getManufacturingInfo(final TreeMap<String, String> p0);
    
    float getLength();
}
