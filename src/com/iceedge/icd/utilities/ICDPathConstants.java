// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.utilities;

import net.iceedge.icebox.utilities.Path;

public class ICDPathConstants
{
    public static final String PRODUCT_PARTS_ICD;
    public static final String ICD_TEXTURES;
    
    static {
        PRODUCT_PARTS_ICD = Path.PRODUCT_PARTS + "InteriorConcepts" + Path.SLASH;
        ICD_TEXTURES = ICDPathConstants.PRODUCT_PARTS_ICD + "Textures" + Path.SLASH;
    }
}
