// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd;

import net.dirtt.icelib.main.attributes.Attribute;
import java.util.Collection;
import java.util.HashSet;
import net.iceedge.catalogs.icd.panel.ICDVerticalChase;
import java.util.Iterator;
import net.iceedge.icecore.basemodule.interfaces.AssembleParent;
import net.dirtt.icelib.main.EntityObject;
import java.util.HashMap;
import net.iceedge.icecore.basemodule.finalclasses.GeneralSnapSet;
import net.iceedge.icecore.basemodule.finalclasses.SnapSetHandler;

public class ICDAssemblyHandler implements SnapSetHandler
{
    public void solve(final GeneralSnapSet set) {
        this.validateAssemblyParts(this.getAssemblyPartMap(set));
    }
    
    private HashMap<EntityObject, Boolean> getAssemblyPartMap(final GeneralSnapSet set) {
        final HashMap<EntityObject, Boolean> hashMap = new HashMap<EntityObject, Boolean>();
        for (final AssembleParent assembleParent : set.getChildrenByClass((Class)AssembleParent.class, true, true)) {
            this.collectAssemblyParts(hashMap, assembleParent);
            if (assembleParent instanceof ICDILine) {
                final ICDVerticalChase verticalChase = ((ICDILine)assembleParent).getVerticalChase();
                if (verticalChase == null) {
                    continue;
                }
                this.collectAssemblyParts(hashMap, (AssembleParent)verticalChase);
            }
        }
        return hashMap;
    }
    
    private void collectAssemblyParts(final HashMap<EntityObject, Boolean> hashMap, final AssembleParent assembleParent) {
        this.collectAssemblyParts(hashMap, assembleParent, assembleParent.shouldAssemble());
    }
    
    private void collectAssemblyParts(final HashMap<EntityObject, Boolean> hashMap, final AssembleParent assembleParent, boolean shouldAssemble) {
        if (!shouldAssemble) {
            shouldAssemble = assembleParent.shouldAssemble();
        }
        final HashSet<EntityObject> set = new HashSet<EntityObject>();
        set.addAll((Collection<?>)assembleParent.getDirectAssemblyParts());
        if (assembleParent.shouldAssemble()) {
            set.addAll((Collection<?>)assembleParent.getIndirectAssemblyParts());
        }
        for (final EntityObject entityObject : set) {
            if (hashMap.get(entityObject) == null || shouldAssemble) {
                hashMap.put(entityObject, shouldAssemble);
            }
        }
        final Iterator<AssembleParent> iterator2 = assembleParent.getExternalAssemblyParts().iterator();
        while (iterator2.hasNext()) {
            this.collectAssemblyParts(hashMap, iterator2.next(), shouldAssemble);
        }
    }
    
    private void validateAssemblyParts(final HashMap<EntityObject, Boolean> hashMap) {
        for (final EntityObject key : hashMap.keySet()) {
            final boolean booleanValue = hashMap.get(key);
            final Attribute attributeObject = key.getAttributeObject("isAssembled");
            if (attributeObject == null) {
                key.createNewAttribute("isAssembled", booleanValue + "");
            }
            else {
                if (attributeObject.getValueAsString().equals(booleanValue + "")) {
                    continue;
                }
                attributeObject.setCurrentValueAsString(booleanValue + "");
            }
        }
    }
}
