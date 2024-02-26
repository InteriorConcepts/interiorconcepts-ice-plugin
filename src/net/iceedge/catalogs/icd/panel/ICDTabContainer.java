// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.panel;

import net.iceedge.icecore.basemodule.finalclasses.GeneralSnapSet;
import net.dirtt.icebox.IceBoxApp;
import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.icelib.ui.attribute.explorer.ui.entity.PossibleValue;
import java.util.TreeSet;
import java.util.Queue;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.LightWeightTypeObject;
import java.util.List;
import net.iceedge.icecore.basemodule.baseclasses.panels.BasicExtrusion;
import java.util.Deque;
import java.util.Collection;
import java.util.LinkedList;
import net.dirtt.icelib.main.ChildSolver;
import net.dirtt.icelib.main.attributes.Attribute;
import java.util.Iterator;
import net.dirtt.icelib.main.TypeableEntity;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import java.util.SortedSet;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.TransformableTriggerUser;

public class ICDTabContainer extends TransformableTriggerUser implements ICDManufacturingReportable
{
    private static final long serialVersionUID = 1L;
    private static final String SPECIAL_EXTENSION = "-SP";
    public static final float INVERTED_ORIGIN_OFFSET = 0.5f;
    private SortedSet<ICDTabQuantity> quantitySet;
    
    public ICDTabContainer(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.setupNamedPoints();
    }
    
    public Object clone() {
        return this.buildClone(new ICDTabContainer(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDTabContainer buildClone(final ICDTabContainer icdTabContainer) {
        super.buildClone((TransformableTriggerUser)icdTabContainer);
        return icdTabContainer;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return (EntityObject)this.buildFrameClone(new ICDTabContainer(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public ICDTabContainer buildFrameClone(final ICDTabContainer icdTabContainer, final EntityObject entityObject) {
        super.buildFrameClone((TransformableTriggerUser)icdTabContainer, entityObject);
        return icdTabContainer;
    }
    
    protected void setupNamedPoints() {
        this.addNamedPoint("Tab_Start", new Point3f());
        this.addNamedPoint("Tab_End", new Point3f());
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("Tab_Start").set(1.0f, 0.5f, 1.0f);
        this.getNamedPointLocal("Tab_End").set(1.0f, 0.5f, this.getZDimension() - 1.0f);
    }
    
    public void solve() {
        super.solve();
        this.validateUserTag();
    }
    
    private void validateUserTag() {
        final String userTagNameAttribute = this.getUserTagNameAttribute("TagName1");
        if (userTagNameAttribute != null) {
            for (final TypeableEntity typeableEntity : this.getChildrenByClass((Class)TypeableEntity.class, true, true)) {
                final Attribute attributeObject = typeableEntity.getAttributeObject("TagName1");
                if (attributeObject != null && attributeObject.getValueAsString().equals("")) {
                    attributeObject.setCurrentValueAsString(userTagNameAttribute);
                }
                else {
                    typeableEntity.createNewAttribute("TagName1", userTagNameAttribute);
                }
            }
        }
    }
    
    protected ChildSolver createChildSolver() {
        return new ICDTabContainerChildSolver((EntityObject)this);
    }
    
    private void validateTabbedTubing() {
        final BasicExtrusion parentExtrusion = this.getParentExtrusion();
        if (parentExtrusion != null && parentExtrusion.addTabbing(this.getSide())) {
            final List childrenByClass = this.getChildrenByClass((Class)ICDTab.class, false);
            final boolean b = false;
            final int calculateNeededTabs = this.calculateNeededTabs(parentExtrusion.getLength());
            final LinkedList list = new LinkedList<ICDTab>();
            list.addAll(childrenByClass);
            final int n = calculateNeededTabs - list.size();
            for (int i = 0; i < n; ++i) {
                final ICDTab tab = this.createTab();
                tab.applyChangesForAttribute("TagName1", this.getAttributeValueAsString("TagName1"));
                list.add(tab);
            }
            for (int j = n; j < 0; ++j) {
                list.poll().destroy();
            }
            this.validateTabPosition(list, b);
        }
        else {
            this.destroyChildren();
        }
    }
    
    public BasicExtrusion getParentExtrusion() {
        return (BasicExtrusion)this.getParent((Class)BasicExtrusion.class);
    }
    
    public TypeObject getTabType() {
        final LightWeightTypeObject lwType = this.getCurrentOption().getLWType((Class)ICDTab.class);
        if (lwType != null) {
            return lwType.getType();
        }
        return null;
    }
    
    private ICDTab createTab() {
        ICDTab icdTab = null;
        final TypeObject tabType = this.getTabType();
        if (tabType != null) {
            icdTab = new ICDTab("", tabType, tabType.getDefaultOption());
            icdTab.setLwTypeCreatedFrom(Solution.lwTypeObjectByName("ICD_Tab"));
        }
        return icdTab;
    }
    
    private int calculateNeededTabs(final float n) {
        final Collection<ICDTabQuantity> quantitySet = this.getQuantitySet();
        int n2 = 1;
        if (quantitySet.size() > 0) {
            int tabCount = 0;
            for (final ICDTabQuantity icdTabQuantity : quantitySet) {
                if (n <= icdTabQuantity.getSize()) {
                    break;
                }
                tabCount = icdTabQuantity.getTabCount();
            }
            n2 = tabCount;
        }
        return n2;
    }
    
    private void validateTabPosition(final Deque<ICDTab> deque, final boolean b) {
        final BasicExtrusion parentExtrusion = this.getParentExtrusion();
        final LinkedList<ICDTab> list = new LinkedList<ICDTab>();
        if (deque.size() > 0 && parentExtrusion != null) {
            if (deque.size() > 1) {
                final ICDPanel panel = this.getPanel();
                final float n = (b && panel != null) ? panel.getChaseIntersectOffset(this.getSide()) : 0.0f;
                final float pullback = this.getPullback();
                final float positionOffset = (n > 0.0f) ? (n + pullback) : pullback;
                final float positionOffset2 = (n < 0.0f) ? (parentExtrusion.getLength() + n - pullback) : (parentExtrusion.getLength() - pullback);
                final ICDTab icdTab = deque.pollFirst();
                final ICDTab icdTab2 = deque.pollLast();
                if (icdTab != null) {
                    icdTab.setPositionOffset(positionOffset);
                }
                if (icdTab2 != null) {
                    icdTab2.setPositionOffset(positionOffset2);
                }
                list.add(icdTab);
                if (!deque.isEmpty()) {
                    this.addExtraTabs(deque, list, positionOffset, positionOffset2);
                }
                list.add(icdTab2);
            }
            else {
                final ICDTab icdTab3 = deque.poll();
                icdTab3.setPositionOffset(parentExtrusion.getLength() / 2.0f);
                list.add(icdTab3);
            }
            final Iterator<Object> iterator = list.iterator();
            while (iterator.hasNext()) {
                this.addToTree((EntityObject)iterator.next());
            }
        }
    }
    
    private void addExtraTabs(final Queue<ICDTab> queue, final List<ICDTab> list, final float n, final float n2) {
        final float n3 = (n2 - n) / (queue.size() + 1);
        float n4 = n;
        while (!queue.isEmpty()) {
            final ICDTab icdTab = queue.poll();
            if (icdTab != null) {
                final float positionOffset = n4 + n3;
                icdTab.setPositionOffset(positionOffset);
                n4 = positionOffset;
                list.add(icdTab);
            }
        }
    }
    
    public float getPullback() {
        return this.getAttributeValueAsFloat("Tab_Pullback");
    }
    
    private Collection<ICDTabQuantity> getQuantitySet() {
        if (this.quantitySet == null) {
            this.quantitySet = new TreeSet<ICDTabQuantity>();
            for (final String s : this.getAttributeValueAsString("Tab_Configuration").split(",")) {
                try {
                    final String[] split2 = s.split(":");
                    this.quantitySet.add(new ICDTabQuantity(Float.parseFloat(split2[0]), Integer.parseInt(split2[1])));
                }
                catch (ArrayIndexOutOfBoundsException ex) {
                    throw new ArrayIndexOutOfBoundsException("Invalid attribute format for configuration attribute: " + s);
                }
                catch (NumberFormatException ex2) {
                    throw new NumberFormatException("Invalid attribute format for configuration attribute: " + s);
                }
            }
        }
        return this.quantitySet;
    }
    
    public String getReportIndex() {
        final BasicExtrusion parentExtrusion = this.getParentExtrusion();
        String string = "";
        if (parentExtrusion != null) {
            final List childrenByClass = parentExtrusion.getChildrenByClass((Class)ICDTabContainer.class, false);
            boolean b = false;
            int i = 0;
            for (final ICDTabContainer icdTabContainer : childrenByClass) {
                final int size = icdTabContainer.getTabs().size();
                final int size2 = icdTabContainer.getTabs(true).size();
                if (size2 > 0) {
                    final boolean b2 = size != size2;
                    ++i;
                    if (!b2) {
                        continue;
                    }
                    b = true;
                }
            }
            if (i > 0) {
                string = "T" + i + (b ? "-SP" : "");
            }
        }
        return string;
    }
    
    public List<ICDTab> getTabs() {
        return this.getTabs(false);
    }
    
    public List<ICDTab> getTabs(final boolean b) {
        List<ICDTab> childrenByClass = new LinkedList<ICDTab>();
        if (b) {
            for (final ICDTab icdTab : this.getChildrenByClass((Class)ICDTab.class, false)) {
                if (icdTab.shouldDrawAssembly()) {
                    childrenByClass.add(icdTab);
                }
            }
        }
        else {
            childrenByClass = (List<ICDTab>)this.getChildrenByClass((Class)ICDTab.class, false);
        }
        return childrenByClass;
    }
    
    public ICDPanel getPanel() {
        return (ICDPanel)this.getParent((Class)ICDPanel.class);
    }
    
    public int getSide() {
        return this.getLwTypeCreatedFrom().getId().equals("ICD_TabContainer_Flip") ? 0 : 1;
    }
    
    public void setTabsModified(final boolean b) {
        final Iterator<ICDTab> iterator = this.getTabs(false).iterator();
        while (iterator.hasNext()) {
            iterator.next().setModified(true);
        }
    }
    
    protected void calculateDimensions() {
        final BasicExtrusion parentExtrusion = this.getParentExtrusion();
        if (parentExtrusion != null) {
            this.setZDimension(parentExtrusion.getLength());
            this.setXDimension(0.5f);
            this.setYDimension(1.0f);
            return;
        }
        super.calculateDimensions();
    }
    
    protected Point3f calculateLocalOriginPointFromLWtype(final LightWeightTypeObject lightWeightTypeObject, final TypeableEntity typeableEntity) {
        final Point3f calculateLocalOriginPointFromLWtype = super.calculateLocalOriginPointFromLWtype(lightWeightTypeObject, typeableEntity);
        if (this.isInverted()) {
            calculateLocalOriginPointFromLWtype.y -= 0.5f;
        }
        return calculateLocalOriginPointFromLWtype;
    }
    
    public void calculateBoundingCube() {
        Point3f geometricCenterPointLocal = this.getGeometricCenterPointLocal();
        if (geometricCenterPointLocal == null) {
            geometricCenterPointLocal = new Point3f();
        }
        this.boundingCube.setLower(geometricCenterPointLocal.x - this.getXDimension() * 0.5f, geometricCenterPointLocal.y - this.getYDimension() * 0.5f, geometricCenterPointLocal.z - this.getZDimension() * 0.5f);
        this.boundingCube.setUpper(geometricCenterPointLocal.x + this.getXDimension() * 0.5f, geometricCenterPointLocal.y + this.getYDimension() * 0.5f, geometricCenterPointLocal.z + this.getZDimension() * 0.5f);
    }
    
    protected void calculateGeometricCenter() {
        this.setGeometricCenterPointLocal(new Point3f(this.getXDimension() / 2.0f, -this.getYDimension() / 2.0f, this.getZDimension() / 2.0f));
    }
    
    public void applyChangesFromEditor(final String anObject, final PossibleValue possibleValue, final Collection<PossibleValue> collection, final Collection<String> collection2, final String s) {
        super.applyChangesFromEditor(anObject, possibleValue, (Collection)collection, (Collection)collection2, s);
        if ("ICD_Tab_Style_Type".equals(anObject)) {
            this.setChildrenModified(true);
        }
    }
    
    public String getTabStyle() {
        return this.getAttributeValueAsString("ICD_Tab_Style_Type");
    }
    
    public boolean isInverted() {
        return "Inverted".equals(this.getTabStyle());
    }
    
    public boolean isFlippedContainer() {
        final LightWeightTypeObject lwTypeCreated = this.getLwTypeCreatedFrom();
        return lwTypeCreated != null && "TAB_CONT_LOCATION_B".equals(lwTypeCreated.getNamedPoint());
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addDimensionsToManufacturingTreeMap(treeMap, (TransformableEntity)this);
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
    
    protected class ICDTabContainerChildSolver extends ChildSolver
    {
        public ICDTabContainerChildSolver(final EntityObject entityObject) {
            super(entityObject);
        }
        
        public void processChildren() {
            if (IceBoxApp.SHOW_TABS && ICDTabContainer.this.getCurrentOption().getTypeList().size() > 0) {
                final boolean modified = ICDTabContainer.this.isModified();
                final GeneralSnapSet generalSnapSet = ICDTabContainer.this.getGeneralSnapSet();
                if (generalSnapSet != null && (modified || generalSnapSet.isModified())) {
                    ICDTabContainer.this.validateTabbedTubing();
                }
            }
            else {
                ICDTabContainer.this.destroyChildren();
            }
            super.processChildren();
        }
    }
}
