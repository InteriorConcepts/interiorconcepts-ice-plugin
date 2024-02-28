package com.iceedge.icd.entities.worksurfaces;

import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.dirtt.utilities.EnumerationIterator;
import java.util.Vector;
import java.util.Collection;
import net.iceedge.catalogs.icd.panel.ICDChaseExtrusion;
import java.util.Iterator;
import java.awt.geom.Point2D;
import net.iceedge.catalogs.icd.intersection.ICDIntersection;
import javax.vecmath.Point3f;
import java.util.List;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.BasicParametricWorksurface;

public class ICDParametricWorksurfaceLegacy extends BasicParametricWorksurface implements ICDManufacturingReportable
{
    public ICDParametricWorksurfaceLegacy(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public ICDParametricWorksurfaceLegacy buildClone(final ICDParametricWorksurfaceLegacy icdParametricWorksurfaceLegacy) {
        super.buildClone((BasicParametricWorksurface)icdParametricWorksurfaceLegacy);
        return icdParametricWorksurfaceLegacy;
    }
    
    public Object clone() {
        return this.buildClone(new ICDParametricWorksurfaceLegacy(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDParametricWorksurfaceLegacy(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public EntityObject buildFrameClone(final ICDParametricWorksurfaceLegacy icdParametricWorksurfaceLegacy, final EntityObject entityObject) {
        return super.buildFrameClone((BasicParametricWorksurface)icdParametricWorksurfaceLegacy, entityObject);
    }
    
    protected void getOverlappingIntersections(final Solution solution, final List<Point3f> list) {
        List<ICDIntersection> intersections = solution.getChildrenByClass(ICDIntersection.class, true, true);
        for (final ICDIntersection icdIntersection : intersections) {
            if (this.getRealBounds(-1.0f).contains(new Point2D.Float(icdIntersection.getBasePointWorldSpace().x, icdIntersection.getBasePointWorldSpace().y))) {
                list.add(this.convertPointToLocal(icdIntersection.getBasePointWorldSpace()));
            }
        }
    }
    
    public void deletedByUser() {
        super.deletedByUser();
        final Iterator<ICDChaseExtrusion> iterator = this.getChaseExtrusions().iterator();
        while (iterator.hasNext()) {
            iterator.next().removeAllBreaks();
        }
        this.handlePreDelete();
        this.setDeletedByUser(true);
        this.destroy();
    }
    
    public Collection<ICDChaseExtrusion> getChaseExtrusions() {
        final Vector<ICDChaseExtrusion> vector = new Vector<ICDChaseExtrusion>();
        final EnumerationIterator<Object> enumerationIterator = new EnumerationIterator(this.breadthFirstEnumeration());
        while (enumerationIterator.hasNext()) {
            final Object next = enumerationIterator.next();
            if (next instanceof ICDChaseExtrusion) {
                vector.add((ICDChaseExtrusion) next);
            }
        }
        return vector;
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
}
