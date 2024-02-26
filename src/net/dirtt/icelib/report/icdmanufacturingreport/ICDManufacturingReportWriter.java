// 
// Decompiled by Procyon v0.5.36
// 

package net.dirtt.icelib.report.icdmanufacturingreport;

import java.util.Enumeration;
import java.util.HashSet;
import net.iceedge.catalogs.icd.intersection.ICDPost;
import net.iceedge.catalogs.icd.ICDILine;
import java.util.HashMap;
import net.dirtt.icelib.main.EntityObject;
import net.iceedge.icecore.basemodule.baseclasses.BasicCatalogPart;
import net.dirtt.icelib.custom.CustomItem;
import net.iceedge.icecore.basemodule.interfaces.AssembleParent;
import java.util.TreeMap;
import net.dirtt.icelib.main.TypeableEntity;
import net.dirtt.icelib.report.ReportNode;
import java.util.Vector;
import java.io.IOException;
import java.util.Iterator;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.report.Report;
import net.dirtt.icelib.report.ReportWriter;

public class ICDManufacturingReportWriter extends ReportWriter
{
    public static final String DEPTH = "Depth";
    public static final String WIDTH = "Width";
    
    public ICDManufacturingReportWriter(final Report report, final Solution solution) {
        super(report, solution);
    }
    
    public void writeReportHeader() throws IOException {
        final Iterator<String> iterator = this.report.getCSVReportHeaders().iterator();
        while (iterator.hasNext()) {
            this.fWriter.write(iterator.next());
            this.fWriter.write("|");
        }
        this.fWriter.write("\r\n");
    }
    
    public void writeQuoteBuckets(final Vector<ReportNode> vector) throws IOException {
        for (final ReportNode reportNode : vector) {
            final EntityObject aEntity = reportNode.getAEntity();
            if (aEntity instanceof TypeableEntity) {
                final TreeMap<String, String> treeMap = new TreeMap<String, String>();
                final TypeableEntity typeableEntity = (TypeableEntity)aEntity;
                final boolean b = typeableEntity instanceof AssembleParent;
                if (!b && typeableEntity.getAttributeValueAsBoolean("isAssembled", false)) {
                    continue;
                }
                treeMap.put("PartNo", typeableEntity.getSKU());
                if (typeableEntity instanceof CustomItem) {
                    treeMap.put("Qty", reportNode.getItems().size() + "");
                }
                else {
                    treeMap.put("Qty", reportNode.getTotalBucketQuantity() + "");
                }
                typeableEntity.getManufacturingInfo((TreeMap)treeMap);
                if (typeableEntity instanceof BasicCatalogPart) {
                    treeMap.put("Depth", typeableEntity.getAttributeValueAsString("Depth"));
                    treeMap.put("Width", typeableEntity.getAttributeValueAsString("Width"));
                    treeMap.put("Height", typeableEntity.getAttributeValueAsString("Height"));
                }
                if (b) {
                    treeMap.put("PartNo", ((ICDManufacturingReportNode)reportNode).getTag());
                    treeMap.put("Description", ((AssembleParent)typeableEntity).getDescriptionForManufacturingReport());
                }
                this.writeMapValues(treeMap);
                this.fWriter.write("\r\n");
                if (!b) {
                    continue;
                }
                this.writeAssembledChildren((AssembleParent)aEntity, ((ICDManufacturingReportNode)reportNode).getTag(), reportNode.getTotalBucketQuantity());
            }
        }
    }
    
    private void writeAssembledChildren(final AssembleParent assembleParent, final String value, final int n) throws IOException {
        final HashSet assembledChildrenForManReport = assembleParent.getAssembledChildrenForManReport();
        if (assembledChildrenForManReport.size() > 0) {
            final HashMap<Object, Integer> hashMap = new HashMap<Object, Integer>();
            for (final TypeableEntity typeableEntity : assembledChildrenForManReport) {
                final TreeMap<String, String> key = new TreeMap<String, String>();
                typeableEntity.getManufacturingInfo((TreeMap)key);
                key.put("PartNo", typeableEntity.getSKU());
                int intValue = 1;
                if (hashMap.containsKey(key)) {
                    intValue = hashMap.get(key);
                    ++intValue;
                    if (assembleParent instanceof ICDILine && ((ICDILine)assembleParent).isVerticalChase() && typeableEntity.getParent((Class)ICDPost.class) != null) {
                        intValue /= 2;
                    }
                }
                hashMap.put(key, intValue);
            }
            for (final TreeMap<String, String> key2 : hashMap.keySet()) {
                key2.put("Qty", hashMap.get(key2) * n + "");
                key2.put("Assembled", value);
                this.writeMapValues(key2);
                this.fWriter.write("\r\n");
            }
        }
    }
    
    private void writeMapValues(final TreeMap<String, String> treeMap) throws IOException {
        this.checkAndWriteValue(treeMap, "PartNo");
        this.fWriter.write("|");
        this.checkAndWriteValue(treeMap, "Qty");
        this.fWriter.write("|");
        this.checkAndWriteValue(treeMap, "Type");
        this.fWriter.write("|");
        this.checkAndWriteValue(treeMap, "SubType");
        this.fWriter.write("|");
        this.checkAndWriteValue(treeMap, "Description");
        this.fWriter.write("|");
        this.checkAndWriteValue(treeMap, "Depth");
        this.fWriter.write("|");
        this.checkAndWriteValue(treeMap, "Width");
        this.fWriter.write("|");
        this.checkAndWriteValue(treeMap, "Height");
        this.fWriter.write("|");
        this.checkAndWriteValue(treeMap, "UserTag");
        this.fWriter.write("|");
        for (int i = 1; i < 10; ++i) {
            this.checkAndWriteValue(treeMap, "Option0" + i);
            this.fWriter.write("|");
        }
        this.checkAndWriteValue(treeMap, "Assembled");
        this.fWriter.write("|");
        this.checkAndWriteValue(treeMap, "TileIndicator");
    }
    
    private void checkAndWriteValue(final TreeMap<String, String> treeMap, final String s) throws IOException {
        if (treeMap.containsKey(s)) {
            this.fWriter.write(treeMap.get(s));
        }
    }
    
    public void matchAndWriteInfo() {
    }
    
    public Vector<ReportNode> getReportNodes() {
        final Enumeration breadthFirstEnumeration = this.report.getBreadthFirstEnumeration();
        final Vector<ReportNode> vector = new Vector<ReportNode>();
        while (breadthFirstEnumeration.hasMoreElements()) {
            final ReportNode e = breadthFirstEnumeration.nextElement();
            if (e instanceof ICDManufacturingReportNode) {
                if (e instanceof ICDManufacturingReportRoot) {
                    continue;
                }
                vector.add(e);
            }
        }
        return vector;
    }
}
