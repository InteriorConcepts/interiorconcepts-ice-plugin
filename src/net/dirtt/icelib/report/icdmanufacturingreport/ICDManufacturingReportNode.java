package net.dirtt.icelib.report.icdmanufacturingreport;

import net.dirtt.icelib.report.Report;
import net.dirtt.icelib.report.ReportRootInterface;
import net.dirtt.xmlFiles.XmlFileUtilities;
import net.iceedge.icebox.utilities.Node;
import java.io.IOException;
import net.dirtt.xmlFiles.XMLWriter;
import net.dirtt.icelib.report.ManufacturingReportable;
import java.util.Iterator;
import com.iceedge.icebox.icecore.entity.IceEntity;
import net.dirtt.icelib.main.TypeableEntity;
import net.dirtt.icelib.report.Reportable;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.report.ReportNode;

public class ICDManufacturingReportNode extends ReportNode
{
    private String materialDescription;
    private String customMaterialDescription;
    private String materialType;
    private boolean horizontalVeneer;
    private boolean paintExtrusions;
    private boolean paintTiles;
    private String tag;
    
    public ICDManufacturingReportNode() {
        this.materialDescription = null;
        this.customMaterialDescription = null;
        this.materialType = null;
        this.horizontalVeneer = false;
        this.paintExtrusions = true;
        this.paintTiles = true;
        this.tag = "";
    }
    
    public ICDManufacturingReportNode(final ReportNode reportNode, final EntityObject entityObject) {
        super(reportNode, (Reportable)entityObject);
        this.materialDescription = null;
        this.customMaterialDescription = null;
        this.materialType = null;
        this.horizontalVeneer = false;
        this.paintExtrusions = true;
        this.paintTiles = true;
        this.tag = "";
        this.registerTag(entityObject);
    }
    
    public ReportNode getBucketForEntity(final EntityObject entityObject) {
        ReportNode matchingBucket = this.findMatchingBucket(entityObject);
        if (matchingBucket == null) {
            matchingBucket = new ICDManufacturingReportNode((ReportNode)this.getRoot(), entityObject);
        }
        return matchingBucket;
    }
    
    public boolean addChildrenToReport(final EntityObject entityObject) {
        boolean b = false;
        final Iterator children = entityObject.getChildren();
        while (children.hasNext()) {
            final EntityObject entityObject2 = children.next();
            if (entityObject2 instanceof TypeableEntity && this.matchesMethodsAndAttributes((IceEntity)entityObject2)) {
                this.getBucketAndAddToIt((IceEntity)entityObject2, true);
                b = true;
            }
            b = (this.addChildrenToReport(entityObject2) || b);
        }
        return b;
    }
    
    public ReportNode findMatchingBucket(final EntityObject entityObject) {
        return ((ReportNode)this.getRoot()).findMatchingBucket(entityObject);
    }
    
    public void addItem(final EntityObject entityObject) {
        if (entityObject instanceof ManufacturingReportable) {
            super.addItem(entityObject);
        }
        else {
            System.out.println("Attempting " + entityObject.getOptionId() + " to material report. It is not a MaterialReportable.");
        }
    }
    
    protected void writeXMLFields(final XMLWriter xmlWriter) throws IOException {
        super.writeXMLFields(xmlWriter);
        xmlWriter.writeTextElement("ICDTag", this.tag);
    }
    
    public void setFieldInfoFromXML(final Node fieldInfoFromXML) {
        super.setFieldInfoFromXML(fieldInfoFromXML);
        this.tag = XmlFileUtilities.getStringValueFromXML("ICDTag", fieldInfoFromXML, this.tag);
    }
    
    public void registerTag(final EntityObject entityObject) {
        final ReportRootInterface root = this.getRoot();
        if (root != null) {
            final Report reportReference = root.getReportReference();
            if (reportReference != null) {
                this.tag = reportReference.registerTag(entityObject);
            }
        }
    }
    
    public String getTag() {
        return this.tag;
    }
    
    public int getTotalBucketQuantity() {
        int n = 0;
        if (this.items != null) {
            final Iterator<EntityObject> iterator = this.items.iterator();
            while (iterator.hasNext()) {
                n += iterator.next().getReferencedCopyExtendedCount();
            }
        }
        return super.getTotalBucketQuantity() + n;
    }
}
