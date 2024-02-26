// 
// Decompiled by Procyon v0.5.36
// 

package net.dirtt.icelib.report.icdmanufacturingreport;

import net.dirtt.icelib.report.GenericCompareable;
import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.report.Reportable;

public interface ICDManufacturingReportable extends Reportable, ManufacturingReportable, GenericCompareable
{
    public static final boolean allowToAddExtraValues = true;
    public static final boolean allowToAddAllAttributeValues = false;
    public static final boolean populateJustSKU = true;
    public static final boolean populateCompareNodeWithExtraValues = true;
    public static final boolean populateNodesForPreassembled = true;
    public static final String[] requiredAttributes = { "ShowInManufacturingReport" };
    public static final String[] requiredAttributeValues = { "1.0" };
    public static final String levelAttribute = "";
    public static final String[] requiredMethods = { "isManufacturerReportable" };
    public static final String[] requiredMethodValues = { "true" };
    public static final String[] desiredCompareableAttributes = { "TagName1" };
}
