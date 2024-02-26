// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces.supports;

import net.dirtt.icelib.report.ManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import net.iceedge.catalogs.icd.worksurfaces.parametric.ICDCornerParametricWorksurface;
import net.dirtt.icelib.main.LightWeightTypeObject;
import net.iceedge.catalogs.icd.worksurfaces.ICDBasicWorksurface;
import javax.vecmath.Tuple3f;
import net.dirtt.utilities.MathUtilities;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.interfaces.OptionOverrideEntity;
import net.iceedge.icecore.basemodule.baseclasses.BasicSimpleSupport;

public class ICDSupport extends BasicSimpleSupport implements OptionOverrideEntity, ICDManufacturingReportable
{
    public ICDSupport(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.setupNamedPoints();
    }
    
    public ICDSupport buildClone(final ICDSupport icdSupport) {
        super.buildClone((BasicSimpleSupport)icdSupport);
        return icdSupport;
    }
    
    public ICDSupport buildClone2(final ICDSupport icdSupport) {
        super.buildClone2((BasicSimpleSupport)icdSupport);
        return icdSupport;
    }
    
    public Object clone() {
        return this.buildClone(new ICDSupport(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDSupport icdSupport, final EntityObject entityObject) {
        super.buildFrameClone((BasicSimpleSupport)icdSupport, entityObject);
        return (EntityObject)icdSupport;
    }
    
    protected void setupNamedPoints() {
        this.addNamedPoint("WSBL", new Point3f());
        this.addNamedPoint("WSBR", new Point3f());
        this.addNamedPoint("WSFL", new Point3f());
        this.addNamedPoint("WSFR", new Point3f());
        this.addNamedPoint("Support_Front", new Point3f());
        this.addNamedPoint("Support_Front_Inside", new Point3f());
        this.addNamedPoint("RIGHT_SUPPORTING_SPAN_POINT", new Point3f());
        this.addNamedPoint("LEFT_SUPPORTING_SPAN_POINT", new Point3f());
        this.addNamedPoint("ASE_POS", new Point3f());
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        final ICDBasicWorksurface parentWorksurface = this.getParentWorksurface();
        if (parentWorksurface != null) {
            this.getNamedPointLocal("WSBL").set((Tuple3f)MathUtilities.convertSpaces(parentWorksurface.getNamedPointLocal("WSBL"), (EntityObject)parentWorksurface, (EntityObject)this));
            this.getNamedPointLocal("WSBR").set((Tuple3f)MathUtilities.convertSpaces(parentWorksurface.getNamedPointLocal("WSBR"), (EntityObject)parentWorksurface, (EntityObject)this));
            this.getNamedPointLocal("WSFL").set((Tuple3f)MathUtilities.convertSpaces(parentWorksurface.getNamedPointLocal("WSFL"), (EntityObject)parentWorksurface, (EntityObject)this));
            this.getNamedPointLocal("WSFR").set((Tuple3f)MathUtilities.convertSpaces(parentWorksurface.getNamedPointLocal("WSFR"), (EntityObject)parentWorksurface, (EntityObject)this));
            this.getNamedPointLocal("Support_Front").set(this.getXDimension(), 0.0f, 0.0f);
            this.getNamedPointLocal("RIGHT_SUPPORTING_SPAN_POINT").set(0.0f, 29.3f, 0.0f);
            this.getNamedPointLocal("LEFT_SUPPORTING_SPAN_POINT").set(0.0f, -29.3f, 0.0f);
            if (this.isLeftSupport()) {
                this.getNamedPointLocal("Support_Front_Inside").set(this.getXDimension(), this.getYDimension(), 0.0f);
            }
            else if (this.isColumnSupport()) {
                this.getNamedPointLocal("ASE_POS").set(0.0f, 0.0f, 0.0f);
            }
            else {
                this.getNamedPointLocal("Support_Front_Inside").set(this.getXDimension(), -this.getYDimension(), 0.0f);
            }
        }
    }
    
    public ICDBasicWorksurface getParentWorksurface() {
        for (EntityObject entityObject = this.getParentEntity(); entityObject != null; entityObject = entityObject.getParentEntity()) {
            if (entityObject instanceof ICDBasicWorksurface) {
                return (ICDBasicWorksurface)entityObject;
            }
        }
        return null;
    }
    
    protected ICDBasicWorksurface getWorksurface() {
        return this.getParentWorksurface();
    }
    
    public boolean isColumnSupport() {
        final LightWeightTypeObject lwTypeCreated = this.getLwTypeCreatedFrom();
        if (lwTypeCreated != null) {
            final String namedPoint = lwTypeCreated.getNamedPoint();
            if (namedPoint != null) {
                return "P11".equals(namedPoint) || namedPoint.contains("Column");
            }
        }
        return false;
    }
    
    protected void calculateGeometricCenter() {
        if (this.isLeftSupport()) {
            this.setGeometricCenterPointLocal(new Point3f(this.getXDimension() / 2.0f, this.getYDimension() / 2.0f, -this.getZDimension() / 2.0f));
        }
        else if (this.isColumnSupport()) {
            this.setGeometricCenterPointLocal(new Point3f(0.0f, 0.0f, -this.getZDimension() / 2.0f));
        }
        else {
            this.setGeometricCenterPointLocal(new Point3f(this.getXDimension() / 2.0f, -this.getYDimension() / 2.0f, -this.getZDimension() / 2.0f));
        }
    }
    
    public boolean isLeftSupport() {
        final LightWeightTypeObject lwTypeCreated = this.getLwTypeCreatedFrom();
        return lwTypeCreated != null && ("WSBL".equals(lwTypeCreated.getNamedPoint()) || "WSFL".equals(lwTypeCreated.getNamedPoint()) || "LH_Back_Corner".equals(lwTypeCreated.getNamedPoint()));
    }
    
    public void solve() {
        this.validateIndicators();
        super.solve();
    }
    
    protected void validateIndicators() {
        this.applyChangesForAttribute("Support_Depth", String.valueOf(this.getWorksurfaceDepthInMyLocation()));
    }
    
    protected float getWorksurfaceDepthInMyLocation() {
        final ICDBasicWorksurface parentWorksurface = this.getParentWorksurface();
        float n = 0.0f;
        if (parentWorksurface != null) {
            if (parentWorksurface instanceof ICDCornerParametricWorksurface) {
                if (this.isLeftSupport()) {
                    n = parentWorksurface.getLeftDepth() - parentWorksurface.getLeftChaseDepthForWorksurfaceSupport();
                }
                else {
                    n = parentWorksurface.getRightDepth() - parentWorksurface.getRightChaseDepthForWorksurfaceSupport();
                }
            }
            else {
                final float chaseDepthForWorksurfaceSupports = parentWorksurface.getChaseDepthForWorksurfaceSupports();
                if (this.isLeftSupport()) {
                    n = parentWorksurface.getLeftDepth() - chaseDepthForWorksurfaceSupports;
                }
                else {
                    n = parentWorksurface.getRightDepth() - chaseDepthForWorksurfaceSupports;
                }
            }
        }
        return n;
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
