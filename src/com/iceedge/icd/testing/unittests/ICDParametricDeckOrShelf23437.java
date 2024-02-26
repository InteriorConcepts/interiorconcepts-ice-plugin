// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.testing.unittests;

import java.util.Iterator;
import net.dirtt.icelib.main.EntityObject;
import net.iceedge.icecore.basemodule.baseclasses.material.BasicMaterialEntity;
import java.util.HashMap;
import net.iceedge.catalogs.icd.worksurfaces.ICDParametricDeckOrShelf;
import net.dirtt.icelib.main.Solution;
import java.io.File;
import Quality.testCases.FileLoaderFailTestCase;

public class ICDParametricDeckOrShelf23437 extends FileLoaderFailTestCase
{
    protected static String path;
    private static final File fileToLoad;
    
    public ICDParametricDeckOrShelf23437(final Solution solution) {
        super(ICDParametricDeckOrShelf23437.fileToLoad, "testGetFinishCodeForDeckOrShelf", solution);
    }
    
    public void testGetFinishCodeForDeckOrShelf() throws Exception {
        try {
            final ICDParametricDeckOrShelf icdParametricDeckOrShelf = (ICDParametricDeckOrShelf)this.findEntityByClassAndUserTag(this.solution, (Class)ICDParametricDeckOrShelf.class, "testdeck");
            assertNotNull("Before - found more than one entity (or none) with specified user tag: testdeck", (Object)icdParametricDeckOrShelf);
            this.checkFinishesForEntity(icdParametricDeckOrShelf);
            final ICDParametricDeckOrShelf icdParametricDeckOrShelf2 = (ICDParametricDeckOrShelf)this.findEntityByClassAndUserTag(this.solution, (Class)ICDParametricDeckOrShelf.class, "testshelf");
            assertNotNull("Before - found more than one entity (or none) with specified user tag: testshelf", (Object)icdParametricDeckOrShelf2);
            this.checkFinishesForEntity(icdParametricDeckOrShelf2);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            assertTrue("Test should not throw exception", false);
        }
    }
    
    private void checkFinishesForEntity(final ICDParametricDeckOrShelf icdParametricDeckOrShelf) {
        assertEquals("Finish code for Laminate is not L", "L", icdParametricDeckOrShelf.getFinishCodeForDeckOrShelf());
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Melamine3", "M");
        hashMap.put("Melamine2", "M");
        hashMap.put("Melamine1", "M");
        hashMap.put("Melamine3a", "M");
        hashMap.put("Laminate1", "L");
        hashMap.put("Laminate2", "L");
        hashMap.put("Laminate3", "L");
        hashMap.put("Laminate3a", "L");
        final BasicMaterialEntity basicMaterialEntity = (BasicMaterialEntity)icdParametricDeckOrShelf.getChild((Class)BasicMaterialEntity.class, true);
        String s = "Laminate2";
        for (final String key : hashMap.keySet()) {
            this.changeAttribute((EntityObject)basicMaterialEntity, "FINISH_TYPE", "Parametric Worksurface", s, key);
            assertEquals("Finish code for " + key + " is not " + hashMap.get(key), (String)hashMap.get(key), icdParametricDeckOrShelf.getFinishCodeForDeckOrShelf());
            s = key;
        }
    }
    
    static {
        ICDParametricDeckOrShelf23437.path = "tests" + File.separator + "Quality" + File.separator + "ICDTestCases" + File.separator;
        fileToLoad = new File(ICDParametricDeckOrShelf23437.path, "deck_finish_code.ice");
    }
}
