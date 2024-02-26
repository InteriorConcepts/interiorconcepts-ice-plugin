// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.main.Solution;
import java.util.Vector;
import net.iceedge.catalogs.icd.ICDILine;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.List;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icelib.main.TypeableEntity;

public class ICDVerticalChaseGroup extends TypeableEntity implements ICDManufacturingReportable
{
    public ICDVerticalChaseGroup(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDVerticalChaseGroup(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDVerticalChaseGroup buildClone(final ICDVerticalChaseGroup icdVerticalChaseGroup) {
        super.buildClone((TypeableEntity)icdVerticalChaseGroup);
        return icdVerticalChaseGroup;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDVerticalChaseGroup(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDVerticalChaseGroup buildFrameClone(final ICDVerticalChaseGroup icdVerticalChaseGroup, final EntityObject entityObject) {
        super.buildFrameClone((TypeableEntity)icdVerticalChaseGroup, entityObject);
        return icdVerticalChaseGroup;
    }
    
    public void solve() {
        this.destroyTheseChases(this.getVerticalChasesToDestroy(this.getChildrenByClass((Class)ICDVerticalChase.class, false).iterator()));
        super.solve();
    }
    
    private void destroyTheseChases(final List<ICDVerticalChase> list) {
        for (int i = 0; i < list.size(); ++i) {
            final ICDVerticalChase icdVerticalChase = list.get(i);
            if (icdVerticalChase != null) {
                icdVerticalChase.destroy();
            }
        }
    }
    
    private List<ICDVerticalChase> getVerticalChasesToDestroy(final Iterator<ICDVerticalChase> iterator) {
        final LinkedList<ICDVerticalChase> list = new LinkedList<ICDVerticalChase>();
        while (iterator.hasNext()) {
            final ICDVerticalChase icdVerticalChase = iterator.next();
            final Vector<ICDILine> chaseILines = icdVerticalChase.getChaseILines();
            final Iterator<ICDILine> iterator2 = chaseILines.iterator();
            while (iterator2.hasNext()) {
                final ICDVerticalChase verticalChase = iterator2.next().getVerticalChase();
                if (chaseILines.size() != 4 || verticalChase != icdVerticalChase) {
                    list.add(icdVerticalChase);
                }
            }
        }
        return list;
    }
    
    public ICDVerticalChase getAndValidateVerticalChase(final Vector<ICDILine> vector, final ICDILine icdiLine, final boolean b) {
        ICDVerticalChase verticalChaseFromILines = null;
        if (vector == null || vector.size() != 4) {
            this.searchAndDestroyChaseWithILine(icdiLine);
        }
        else {
            verticalChaseFromILines = this.getVerticalChaseFromILines(vector, icdiLine);
            if (!b) {
                verticalChaseFromILines.destroy();
                verticalChaseFromILines = null;
            }
        }
        return verticalChaseFromILines;
    }
    
    private void searchAndDestroyChaseWithILine(final ICDILine icdiLine) {
        final ICDVerticalChase verticalChase = icdiLine.getVerticalChase();
        if (verticalChase == null) {
            this.getChaseWithILine(icdiLine);
        }
        if (verticalChase != null) {
            verticalChase.destroy();
        }
        icdiLine.setVerticalChase(null);
    }
    
    private ICDVerticalChase getVerticalChaseFromILines(final Vector<ICDILine> chaseILines, final ICDILine icdiLine) {
        Object chaseWithILine = null;
        final Iterator<ICDILine> iterator = chaseILines.iterator();
        while (iterator.hasNext()) {
            chaseWithILine = this.getChaseWithILine(iterator.next());
            if (chaseWithILine == null) {
                final TypeObject typeObjectByName = Solution.typeObjectByName("ICD_Vertical_Chase_Type");
                chaseWithILine = EntityObject.createNewEntity(typeObjectByName.getId(), typeObjectByName, typeObjectByName.getDefaultOption());
                ((ICDVerticalChase)chaseWithILine).setChaseILines(chaseILines);
                ((ICDVerticalChase)chaseWithILine).applyChangesForAttribute("ICD_Vertical_Chase_Depth", Float.toString(ICDVerticalChase.getDepthForILines(chaseILines)));
                ((ICDVerticalChase)chaseWithILine).applyChangesForAttribute("ICD_Vertical_Chase_Width", Float.toString(ICDVerticalChase.getWidthForILines(chaseILines)));
                ((ICDVerticalChase)chaseWithILine).applyChangesForAttribute("ICD_Vertical_Chase_Height", Float.toString(icdiLine.getHeight()));
                ((ICDVerticalChase)chaseWithILine).modifyCurrentOption();
                this.addToTree((EntityObject)chaseWithILine);
            }
        }
        return (ICDVerticalChase)chaseWithILine;
    }
    
    private ICDVerticalChase getChaseWithILine(final ICDILine o) {
        for (final ICDVerticalChase icdVerticalChase : this.getChildrenByClass((Class)ICDVerticalChase.class, false)) {
            if (icdVerticalChase.getChaseILines().contains(o)) {
                return icdVerticalChase;
            }
        }
        return null;
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
    }
    
    public String getManufacturingReportMaterialOptID() {
        return "Option0";
    }
    
    public void addManufacturingInfoToTreeMap(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addManufacturingInfoToTreeMap(treeMap, (ManufacturingReportable)this);
    }
    
    public String getDescriptionForManufacturingReport() {
        return this.getDescription();
    }
}
