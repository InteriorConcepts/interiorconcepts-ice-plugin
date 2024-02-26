// 
// Decompiled by Procyon v0.5.36
// 

package net.dirtt.icelib.report.icdmanufacturingreport;

import javax.swing.tree.TreeNode;
import net.dirtt.icelib.main.attributes.Attribute;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import net.dirtt.icelib.main.TypeableEntity;
import java.util.Vector;
import net.dirtt.xmlFiles.XmlFileUtilities;
import net.iceedge.icebox.utilities.Node;
import java.io.IOException;
import net.dirtt.xmlFiles.XMLWriter;
import net.dirtt.icelib.report.ReportNode;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.report.Report;
import net.dirtt.icelib.report.ReportRootInterface;

public class ICDManufacturingReportRoot extends ICDManufacturingReportNode implements ReportRootInterface
{
    private Report reportReference;
    private long uidPool;
    private boolean changed;
    
    public ICDManufacturingReportRoot() {
        this.reportReference = null;
        this.uidPool = 0L;
        this.changed = false;
    }
    
    public boolean hasChanged() {
        return this.changed;
    }
    
    public void setChanged(final boolean changed) {
        this.changed = changed;
    }
    
    @Override
    public void addItem(final EntityObject obj) {
        if (obj instanceof Solution && this.items.size() == 0) {
            this.items.addElement(obj);
            obj.addReferenceToReportBucket((ReportNode)this);
            this.setRootChanged();
        }
    }
    
    @Override
    public ReportNode getBucketForEntity(final EntityObject entityObject) {
        ReportNode matchingBucket = this.findMatchingBucket(entityObject);
        if (matchingBucket == null) {
            matchingBucket = new ICDManufacturingReportNode(this, entityObject);
        }
        return matchingBucket;
    }
    
    @Override
    public ReportNode findMatchingBucket(final EntityObject entityObject) {
        for (int i = 0; i < this.getChildCount(); ++i) {
            final ReportNode child = this.getChildAt(i);
            if (child.doesEntityFit(entityObject)) {
                return child;
            }
        }
        return null;
    }
    
    protected String getFormattedAttributeValue(final EntityObject entityObject, final String s) {
        String attributeValueAsString = entityObject.getAttributeValueAsString(s);
        if (attributeValueAsString == null) {
            attributeValueAsString = "";
        }
        return attributeValueAsString;
    }
    
    public Report getReportReference() {
        return this.reportReference;
    }
    
    public void setReportReference(final Report reportReference) {
        this.reportReference = reportReference;
    }
    
    public ReportRootInterface getRoot() {
        return (ReportRootInterface)this;
    }
    
    public long getNextAvailableUID() {
        return ++this.uidPool;
    }
    
    public void registerUID(final long uidPool) {
        if (uidPool > this.uidPool) {
            this.uidPool = uidPool;
        }
    }
    
    @Override
    protected void writeXMLFields(final XMLWriter xmlWriter) throws IOException {
        super.writeXMLFields(xmlWriter);
        xmlWriter.writeTextElement("uidPool", this.uidPool + "");
    }
    
    @Override
    public void setFieldInfoFromXML(final Node fieldInfoFromXML) {
        this.uidPool = Long.parseLong(XmlFileUtilities.getStringValueFromXML("uidPool", fieldInfoFromXML, "0"));
        super.setFieldInfoFromXML(fieldInfoFromXML);
    }
    
    public String toString() {
        return super.toString() + " uidPool: " + this.uidPool;
    }
    
    public boolean isEmpty() {
        return true;
    }
    
    private Vector<String> getSortedMatchingCustomNodes(final String anObject) {
        final Vector<String> list = new Vector<String>();
        final Iterator children = this.getChildren();
        while (children.hasNext()) {
            final ICDManufacturingReportNode icdManufacturingReportNode = (ICDManufacturingReportNode)children.next();
            final TypeableEntity typeableEntity = (TypeableEntity)icdManufacturingReportNode.getAEntity();
            if (typeableEntity != null) {
                final Attribute attributeObject = typeableEntity.getCurrentOption().getAttributeObject("Material_ID");
                if (attributeObject == null || !attributeObject.getValueAsString().equals(anObject)) {
                    continue;
                }
                list.add(icdManufacturingReportNode.getUID() + "");
            }
        }
        Collections.sort(list);
        return new Vector<String>(list);
    }
    
    public String getCustomIDForNode(final String o, final String str) {
        String s = this.getSortedMatchingCustomNodes(str).indexOf(o) + 1 + "";
        if (s.length() == 1) {
            s = "0" + s;
        }
        return str + s;
    }
}
