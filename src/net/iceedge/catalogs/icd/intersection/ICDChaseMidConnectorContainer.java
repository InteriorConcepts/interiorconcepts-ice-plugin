package net.iceedge.catalogs.icd.intersection;

import net.dirtt.icelib.report.ManufacturingReportable;
import com.iceedge.icd.reporting.ICDManufacturingUtils;
import java.util.TreeMap;
import java.util.List;
import net.iceedge.catalogs.icd.panel.ICDAngledPanel;
import java.util.ArrayList;
import net.iceedge.catalogs.icd.panel.ICDCurvedPanel;
import net.dirtt.utilities.ValidationUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import net.iceedge.icebox.utilities.Node;
import java.io.IOException;
import net.dirtt.utilities.PersistentFileManager;
import net.dirtt.xmlFiles.XMLWriter;
import net.dirtt.icelib.main.Solution;
import java.util.Vector;
import net.dirtt.icelib.main.LightWeightTypeObject;
import java.util.Iterator;
import net.iceedge.catalogs.icd.panel.ICDChaseConnectorExtrusion;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.dirtt.icelib.report.icdmanufacturingreport.ICDManufacturingReportable;
import net.dirtt.icelib.main.TransformableEntity;

public class ICDChaseMidConnectorContainer extends TransformableEntity implements ICDManufacturingReportable
{
    private static final long serialVersionUID = 5338356668746975247L;
    private boolean underChaseContainer;
    private boolean isSuspendedContainer;
    
    public ICDChaseMidConnectorContainer(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
        this.underChaseContainer = false;
        this.isSuspendedContainer = false;
        this.setupNamedPoints();
    }
    
    public ICDChaseMidConnectorContainer buildClone(final ICDChaseMidConnectorContainer icdChaseMidConnectorContainer) {
        super.buildClone((TransformableEntity)icdChaseMidConnectorContainer);
        return icdChaseMidConnectorContainer;
    }
    
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDChaseMidConnectorContainer(this.getId(), this.getCurrentType(), this.getCurrentOption()), entityObject);
    }
    
    public TransformableEntity buildClone2(final ICDChaseMidConnectorContainer icdChaseMidConnectorContainer) {
        super.buildClone2((TransformableEntity)icdChaseMidConnectorContainer);
        return icdChaseMidConnectorContainer;
    }
    
    public Object clone() {
        return this.buildClone(new ICDChaseMidConnectorContainer(this.getId(), this.currentType, this.currentOption));
    }
    
    public EntityObject buildFrameClone(final ICDChaseMidConnectorContainer icdChaseMidConnectorContainer, final EntityObject entityObject) {
        super.buildFrameClone((TransformableEntity)icdChaseMidConnectorContainer, entityObject);
        icdChaseMidConnectorContainer.setIsSuspendedContainer(this.isSuspendedContainer());
        icdChaseMidConnectorContainer.setUnderChaseContainer(this.isUnderChaseContainer());
        return (EntityObject)icdChaseMidConnectorContainer;
    }
    
    public void solve() {
        final boolean modified = this.isModified();
        if (modified) {
            this.modifyCurrentOption();
            this.validateChildTypes();
        }
        super.solve();
        if (modified) {
            this.setVerticalExtrusionUnderSpecialPostModified(this.getSpecialPost());
        }
    }
    
    protected boolean isChaseSingle() {
        boolean b = false;
        if ("Single".equals(this.getAttributeValueAsString("ICD_Chase_Connector_Container_Size"))) {
            b = true;
        }
        return b;
    }
    
    public void setupNamedPoints() {
        this.addNamedPoint("Bottom_Connector_POS", new Point3f());
        this.addNamedPoint("Top_Connector_POS", new Point3f());
        this.addNamedPoint("Vertical_Connector_POS", new Point3f());
        this.addNamedRotation("Bottom_Connector_ROT", new Vector3f(0.0f, 0.0f, 0.0f));
        this.addNamedRotation("Top_Connector_ROT", new Vector3f(0.0f, 0.0f, 0.0f));
        this.addNamedRotation("Vertical_Connector_ROT", new Vector3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("Bottom_Connector_B_POS", new Point3f());
        this.addNamedPoint("Top_Connector_B_POS", new Point3f());
        this.addNamedPoint("Vertical_Connector_B_POS", new Point3f());
        this.addNamedRotation("Bottom_Connector_B_ROT", new Vector3f(0.0f, 0.0f, 0.0f));
        this.addNamedRotation("Top_Connector_B_ROT", new Vector3f(0.0f, 0.0f, 0.0f));
        this.addNamedRotation("Vertical_Connector_B_ROT", new Vector3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("extStartPoint", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("extEndPoint", new Point3f(0.0f, 0.0f, 0.0f));
        this.addNamedPoint("Suspened_Chase_Vertical_Support_POS", new Point3f());
        this.addNamedPoint("Suspened_Chase_Vertical_Support_B_POS", new Point3f());
        this.addNamedPoint("Suspened_Chase_Bottom_Support_POS", new Point3f());
        this.addNamedPoint("Suspened_Chase_Bottom_Support_B_POS", new Point3f());
    }
    
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("extStartPoint").set((Tuple3f)new Point3f());
        this.getNamedPointLocal("extEndPoint").set((Tuple3f)new Point3f(0.0f, 0.0f, this.getZDimension()));
    }
    
    public void resetContainer(final float n, final float n2, final boolean b, final float n3, final float n4, final float myLength) {
        if (b) {
            this.getNamedPointLocal("Vertical_Connector_POS").set((Tuple3f)new Point3f(0.0f, n3 + 0.5f, n));
            this.getNamedPointLocal("Top_Connector_POS").set((Tuple3f)new Point3f(0.0f, 0.5f, n));
            this.getNamedPointLocal("Bottom_Connector_POS").set((Tuple3f)new Point3f(0.0f, 0.5f, 0.75f));
            this.getNamedPointLocal("Suspened_Chase_Bottom_Support_POS").set((Tuple3f)new Point3f(0.0f, 0.5f, 0.75f));
            this.getNamedPointLocal("Suspened_Chase_Vertical_Support_POS").set((Tuple3f)new Point3f(0.0f, n3 + 0.5f, n - n2 + 1.0f));
            this.getNamedRotationLocal("Vertical_Connector_ROT").set(1.5707964f, 0.0f, 3.1415927f);
        }
        else {
            this.getNamedPointLocal("Vertical_Connector_POS").set((Tuple3f)new Point3f(0.0f, n3 + 0.5f, n));
            this.getNamedPointLocal("Top_Connector_POS").set((Tuple3f)new Point3f(0.0f, 0.5f, n));
            this.getNamedPointLocal("Bottom_Connector_POS").set((Tuple3f)new Point3f(0.0f, 0.5f, 0.75f));
            this.getNamedPointLocal("Suspened_Chase_Bottom_Support_POS").set((Tuple3f)new Point3f(0.0f, 0.5f, 0.75f));
            this.getNamedPointLocal("Suspened_Chase_Vertical_Support_POS").set((Tuple3f)new Point3f(0.0f, n3 + 0.5f, n - n2 + 1.0f));
            this.getNamedPointLocal("Vertical_Connector_B_POS").set((Tuple3f)new Point3f(0.0f, -n4 + 0.5f, n));
            this.getNamedPointLocal("Top_Connector_B_POS").set((Tuple3f)new Point3f(0.0f, -0.5f, n));
            this.getNamedPointLocal("Bottom_Connector_B_POS").set((Tuple3f)new Point3f(0.0f, -0.5f, 0.75f));
            this.getNamedPointLocal("Suspened_Chase_Bottom_Support_B_POS").set((Tuple3f)new Point3f(0.0f, -0.5f, 0.75f));
            this.getNamedPointLocal("Suspened_Chase_Vertical_Support_B_POS").set((Tuple3f)new Point3f(0.0f, -n4 + 0.5f, n - n2 + 1.0f));
        }
        for (final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion : this.getChildrenByClass(ICDChaseConnectorExtrusion.class, false)) {
            if (icdChaseConnectorExtrusion.isVertical()) {
                if (icdChaseConnectorExtrusion.isSuspendedeChaseSupport()) {
                    continue;
                }
                icdChaseConnectorExtrusion.setMyLength(myLength);
            }
            else if (b) {
                icdChaseConnectorExtrusion.setMyLength(n3 - 1.0f);
            }
            else {
                final LightWeightTypeObject lwTypeCreated = icdChaseConnectorExtrusion.getLwTypeCreatedFrom();
                if (lwTypeCreated == null) {
                    continue;
                }
                if (lwTypeCreated.getId().indexOf("Connector_B_Type") == -1) {
                    icdChaseConnectorExtrusion.setMyLength(n3 - 1.0f);
                    this.getNamedRotationLocal("Vertical_Connector_ROT").set(1.5707964f, 0.0f, 3.1415927f);
                }
                else {
                    icdChaseConnectorExtrusion.setMyLength(n4 - 1.0f);
                    this.getNamedRotationLocal("Vertical_Connector_B_ROT").set(1.5707964f, 0.0f, 3.1415927f);
                    this.getNamedRotationLocal("Bottom_Connector_B_ROT").set(0.0f, 0.0f, 3.1415927f);
                    this.getNamedRotationLocal("Top_Connector_B_ROT").set(0.0f, 0.0f, 3.1415927f);
                }
            }
        }
    }
    
    public Vector<ICDChaseConnectorExtrusion> getVerticalPieces() {
        final Vector<ICDChaseConnectorExtrusion> vector = new Vector<ICDChaseConnectorExtrusion>();
        for (final ICDChaseConnectorExtrusion e : this.getChildrenByClass(ICDChaseConnectorExtrusion.class, false, true)) {
            if (e.isVertical()) {
                vector.add(e);
            }
        }
        return vector;
    }
    
    public void refreshContainer(final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
        boolean b = false;
        boolean b2 = false;
        if (n != n3) {
            b = true;
        }
        if (n2 != n3) {
            b2 = true;
        }
        this.validateSuspendedChaseSupport(b, b2, n5, n6);
        float myLength = n;
        float myLength2 = n2;
        float myLength3 = 0.0f;
        float myLength4 = 0.0f;
        if (!this.isUnderChaseContainer()) {
            if (b) {
                myLength -= 2.0f;
                myLength3 = n3 - n - 0.75f;
            }
            else {
                myLength -= 1.75f;
            }
            if (b2) {
                myLength2 -= 2.0f;
                myLength4 = n3 - n2 - 0.75f;
            }
            else {
                myLength2 -= 1.75f;
            }
        }
        else {
            if (b) {
                myLength -= 2.0f;
                myLength3 = n3 - n + 1.0f;
            }
            if (b2) {
                myLength2 -= 2.0f;
                myLength4 = n3 - n2 + 1.0f;
            }
        }
        for (final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion : this.getVerticalPieces()) {
            if (icdChaseConnectorExtrusion.isSideA()) {
                if (!icdChaseConnectorExtrusion.isSuspendedeChaseSupport()) {
                    icdChaseConnectorExtrusion.setMyLength(myLength);
                }
                else {
                    icdChaseConnectorExtrusion.setMyLength(myLength3);
                }
            }
            else if (!icdChaseConnectorExtrusion.isSuspendedeChaseSupport()) {
                icdChaseConnectorExtrusion.setMyLength(myLength2);
            }
            else {
                icdChaseConnectorExtrusion.setMyLength(myLength4);
            }
        }
        final Point3f namedPointLocal = this.getNamedPointLocal("Bottom_Connector_POS");
        final Point3f namedPointLocal2 = this.getNamedPointLocal("Bottom_Connector_B_POS");
        this.getNamedPointLocal("Bottom_Connector_B_POS").set(namedPointLocal2.x, namedPointLocal2.y, n3 - n2 + 0.75f);
        this.getNamedPointLocal("Bottom_Connector_POS").set(namedPointLocal.x, namedPointLocal.y, n3 - n + 0.75f);
        if (this.isUnderChaseContainer()) {
            final Point3f namedPointLocal3 = this.getNamedPointLocal("Top_Connector_POS");
            final Point3f namedPointLocal4 = this.getNamedPointLocal("Top_Connector_B_POS");
            this.getNamedPointLocal("Top_Connector_B_POS").set(namedPointLocal4.x, namedPointLocal4.y, namedPointLocal4.z + 1.75f);
            this.getNamedPointLocal("Top_Connector_POS").set(namedPointLocal3.x, namedPointLocal3.y, namedPointLocal3.z + 1.75f);
            final Point3f namedPointLocal5 = this.getNamedPointLocal("Vertical_Connector_POS");
            final Point3f namedPointLocal6 = this.getNamedPointLocal("Vertical_Connector_B_POS");
            this.getNamedPointLocal("Vertical_Connector_B_POS").set(namedPointLocal6.x, namedPointLocal6.y, namedPointLocal6.z + 1.75f);
            this.getNamedPointLocal("Vertical_Connector_POS").set(namedPointLocal5.x, namedPointLocal5.y, namedPointLocal5.z + 1.75f);
            final Point3f namedPointLocal7 = this.getNamedPointLocal("Suspened_Chase_Vertical_Support_POS");
            final Point3f namedPointLocal8 = this.getNamedPointLocal("Suspened_Chase_Vertical_Support_B_POS");
            this.getNamedPointLocal("Suspened_Chase_Vertical_Support_POS").set(namedPointLocal7.x, namedPointLocal7.y, namedPointLocal7.z + 1.75f);
            this.getNamedPointLocal("Suspened_Chase_Vertical_Support_B_POS").set(namedPointLocal8.x, namedPointLocal8.y, namedPointLocal8.z + 1.75f);
            if (b) {
                final Point3f namedPointLocal9 = this.getNamedPointLocal("Bottom_Connector_POS");
                this.getNamedPointLocal("Bottom_Connector_POS").set(namedPointLocal9.x, namedPointLocal9.y, n3 - n + 2.75f);
            }
            if (b2) {
                final Point3f namedPointLocal10 = this.getNamedPointLocal("Bottom_Connector_B_POS");
                this.getNamedPointLocal("Bottom_Connector_B_POS").set(namedPointLocal10.x, namedPointLocal10.y, n3 - n2 + 2.75f);
            }
        }
        else {
            if (b) {
                final Point3f namedPointLocal11 = this.getNamedPointLocal("Bottom_Connector_POS");
                this.getNamedPointLocal("Bottom_Connector_POS").set(namedPointLocal11.x, namedPointLocal11.y, n3 - n + 1.0f);
            }
            if (b2) {
                final Point3f namedPointLocal12 = this.getNamedPointLocal("Bottom_Connector_B_POS");
                this.getNamedPointLocal("Bottom_Connector_B_POS").set(namedPointLocal12.x, namedPointLocal12.y, n3 - n2 + 1.0f);
            }
        }
        final Iterator<ICDChaseConnectorExtrusion> iterator2 = (Iterator<ICDChaseConnectorExtrusion>)this.getChildrenByClass(ICDChaseConnectorExtrusion.class, true, true).iterator();
        while (iterator2.hasNext()) {
            iterator2.next().calculate();
        }
    }
    
    private void validateSuspendedChaseSupport(final boolean b, final boolean b2, final float n, final float n2) {
        if (b) {
            this.validateChildByType("ICD_Chase_Mid_Suspended_Chase_Vertical_Support_Type");
            this.validateChildByType("ICD_Chase_Bottom_Suspended_Chase_Support_Type");
            final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion = (ICDChaseConnectorExtrusion)this.getChildByType("ICD_Chase_Bottom_Suspended_Chase_Support_Type");
            if (icdChaseConnectorExtrusion != null) {
                icdChaseConnectorExtrusion.setMyLength(n - 1.0f);
            }
        }
        else {
            this.deleteChildByType("ICD_Chase_Mid_Suspended_Chase_Vertical_Support_Type");
            this.deleteChildByType("ICD_Chase_Bottom_Suspended_Chase_Support_Type");
        }
        if (!this.isChaseSingle()) {
            if (b2) {
                this.validateChildByType("ICD_Chase_Mid_Suspended_Chase_Vertical_Support_B_Type");
                this.validateChildByType("ICD_Chase_Bottom_Suspended_Chase_Support_B_Type");
                final ICDChaseConnectorExtrusion icdChaseConnectorExtrusion2 = (ICDChaseConnectorExtrusion)this.getChildByType("ICD_Chase_Bottom_Suspended_Chase_Support_B_Type");
                if (icdChaseConnectorExtrusion2 != null) {
                    icdChaseConnectorExtrusion2.setMyLength(n2 - 1.0f);
                }
            }
            else {
                this.deleteChildByType("ICD_Chase_Mid_Suspended_Chase_Vertical_Support_B_Type");
                this.deleteChildByType("ICD_Chase_Bottom_Suspended_Chase_Support_B_Type");
            }
        }
        else {
            this.deleteChildByType("ICD_Chase_Mid_Suspended_Chase_Vertical_Support_B_Type");
            this.deleteChildByType("ICD_Chase_Bottom_Suspended_Chase_Support_B_Type");
        }
    }
    
    private void validateChildByType(final String s) {
        if (this.getChildByType(s) == null) {
            final TypeObject typeObjectByName = Solution.typeObjectByName(s);
            final EntityObject newEntity = EntityObject.createNewEntity("", typeObjectByName, Solution.lwTypeObjectByName(s), typeObjectByName.getDefaultOption());
            if (newEntity != null) {
                this.addToTree(newEntity);
            }
        }
    }
    
    private void deleteChildByType(final String s) {
        final EntityObject childByType = this.getChildByType(s);
        if (childByType != null) {
            childByType.destroy();
        }
    }
    
    public boolean isSuspendedContainer() {
        return this.isSuspendedContainer;
    }
    
    public void setIsSuspendedContainer(final boolean isSuspendedContainer) {
        this.isSuspendedContainer = isSuspendedContainer;
    }
    
    public boolean isUnderChaseContainer() {
        return this.underChaseContainer;
    }
    
    public void setUnderChaseContainer(final boolean underChaseContainer) {
        this.underChaseContainer = underChaseContainer;
    }
    
    protected void writeXMLFields(final XMLWriter xmlWriter, final PersistentFileManager.FileWriter fileWriter) throws IOException {
        super.writeXMLFields(xmlWriter, fileWriter);
        xmlWriter.writeTextElement("underChaseContainer", this.isUnderChaseContainer() + "");
        xmlWriter.writeTextElement("isSuspendedContainer", this.isSuspendedContainer() + "");
    }
    
    protected void setFieldInfoFromXML(final Node node, final DefaultMutableTreeNode defaultMutableTreeNode, final PersistentFileManager.FileReader fileReader) {
        super.setFieldInfoFromXML(node, defaultMutableTreeNode, fileReader);
        this.setUnderChaseContainer(this.getBooleanValueFromXML("underChaseContainer", node, false));
        this.setIsSuspendedContainer(this.getBooleanValueFromXML("isSuspendedContainer", node, false));
    }
    
    protected boolean validateEntityParent() {
        boolean b = super.validateEntityParent();
        if (b) {
            b = ValidationUtilities.validateParentIsCorrectClass((EntityObject)this, new Class[] { ICDPost.class });
        }
        return b;
    }
    
    private ICDPost getSpecialPost() {
        final ICDPost icdPost = (ICDPost)this.getParent(ICDPost.class);
        if (icdPost != null) {
            final ICDPost icdPost2 = (ICDPost)icdPost.getParent(ICDPost.class);
            if (icdPost2 != null) {
                final String id = icdPost2.getCurrentType().getId();
                if ("ICD_TwoWayCurvedPostType".equals(id) || "ICD_TwoWayAngledPostType".equals(id)) {
                    return icdPost2;
                }
            }
        }
        return null;
    }
    
    private void setVerticalExtrusionUnderSpecialPostModified(final ICDPost icdPost) {
        if (icdPost == null) {
            return;
        }
        final String id = icdPost.getCurrentType().getId();
        if ("ICD_TwoWayCurvedPostType".equals(id)) {
            final ICDCurvedPanel icdCurvedPanel = (ICDCurvedPanel)icdPost.getChild(ICDCurvedPanel.class, true);
            if (icdCurvedPanel != null) {
                icdCurvedPanel.setVerticalExtrusionsModified();
            }
        }
        else if ("ICD_TwoWayAngledPostType".equals(id)) {
            final List childrenByClass = icdPost.getChildrenByClass((List)new ArrayList(), ICDAngledPanel.class, false, true);
            if (childrenByClass != null) {
                final Iterator<ICDAngledPanel> iterator = childrenByClass.iterator();
                while (iterator.hasNext()) {
                    iterator.next().setVerticalExtrusionsModified();
                }
            }
        }
    }
    
    public void addDimensionsToManufacturingReport(final TreeMap<String, String> treeMap) {
        ICDManufacturingUtils.addDimensionsToManufacturingTreeMap(treeMap, this);
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
