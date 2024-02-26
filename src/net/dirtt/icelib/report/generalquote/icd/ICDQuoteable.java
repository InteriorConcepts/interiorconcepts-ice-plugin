// 
// Decompiled by Procyon v0.5.36
// 

package net.dirtt.icelib.report.generalquote.icd;

import net.dirtt.icelib.report.Reportable;

public interface ICDQuoteable extends Reportable
{
    public static final boolean allowToAddExtraValues = true;
    public static final boolean allowToAddAllAttributeValues = true;
    public static final boolean checkIndicatorWhenAddingToTree = false;
    public static final boolean populateJustSKU = false;
    public static final String[] requiredAttributes = new String[0];
    public static final String[] requiredAttributeValues = new String[0];
    public static final String levelAttribute = "";
    public static final String[] requiredMethods = new String[0];
    public static final String[] requiredMethodValues = new String[0];
    
    boolean isQuoteable(final String p0);
}
