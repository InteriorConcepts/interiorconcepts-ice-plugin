package net.iceedge.catalogs.icd.intersection;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.EntityObject;
import net.iceedge.icecore.basemodule.interfaces.GeneralIntersectionInterface;
import net.iceedge.icecore.basemodule.baseclasses.utilities.ILineIntersection;
import net.dirtt.icelib.undo.iceobjects.Point3fWithUndo;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.iceedge.icecore.basemodule.baseclasses.BasicIntersectionFactory;

public class ICDIntersectionFactory extends BasicIntersectionFactory implements ICDManufacturingReportable
{
    public ICDIntersectionFactory(final String s, final TypeObject typeObject) {
        this(s, typeObject, typeObject.getDefaultOption());
    }
    
    public ICDIntersectionFactory(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        this(s, "ICD Intersection Factory", typeObject, optionObject, new Point3fWithUndo());
    }
    
    public ICDIntersectionFactory(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject, final Point3fWithUndo point3fWithUndo) {
        super(s, s2, typeObject, optionObject, point3fWithUndo);
    }
    
    public ICDIntersectionFactory buildClone(final ICDIntersectionFactory icdIntersectionFactory) {
        super.buildClone((BasicIntersectionFactory)icdIntersectionFactory);
        return icdIntersectionFactory;
    }
    
    public Object clone() {
        return this.buildClone(new ICDIntersectionFactory(this.getId(), this.currentType, this.currentOption));
    }
    
    public GeneralIntersectionInterface createNewIntersection(final ILineIntersection lineIntersection) {
        final TypeObject generateTypeObjectByIntersectionType = this.generateTypeObjectByIntersectionType(lineIntersection.getArms().size());
        final ICDIntersection icdIntersection = new ICDIntersection("ICDIntersection", generateTypeObjectByIntersectionType, generateTypeObjectByIntersectionType.getDefaultOption());
        icdIntersection.setBasePoint(lineIntersection.getIntersectionPoint());
        this.addToTree((EntityObject)icdIntersection);
        return (GeneralIntersectionInterface)icdIntersection;
    }
    
    public TypeObject generateTypeObjectByIntersectionType(int n) {
        TypeObject typeObject = null;
        if (n > 4) {
            n = 4;
        }
        switch (n) {
            case 1: {
                typeObject = Solution.typeObjectByName("ICD_OneWayIntersectionType");
                break;
            }
            case 2: {
                typeObject = Solution.typeObjectByName("ICD_TwoWayIntersectionType");
                break;
            }
            case 3: {
                typeObject = Solution.typeObjectByName("ICD_ThreeWayIntersectionType");
                break;
            }
            case 4: {
                typeObject = Solution.typeObjectByName("ICD_FourWayIntersectionType");
                break;
            }
        }
        return typeObject;
    }
    
    protected GeneralIntersectionInterface createNewIntersection(final Point3f point3f) {
        return (GeneralIntersectionInterface)new ICDIntersection(point3f, Solution.typeObjectByName("ICD_OneWayIntersectionType"), 1);
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
