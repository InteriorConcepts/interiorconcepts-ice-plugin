package net.iceedge.catalogs.icd.intersection;

import net.dirtt.icelib.report.compare.CompareNode;
import net.iceedge.icecore.basemodule.interfaces.ILineInterface;
import javax.vecmath.Point3f;
import java.util.Vector;
import net.dirtt.icelib.main.EntityObject;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDPostForSubPanel extends ICDPost implements ICDPostHostInterface
{
    public ICDPostForSubPanel(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, typeObject, optionObject);
    }
    
    public ICDPostForSubPanel buildClone(final ICDPostForSubPanel icdPostForSubPanel) {
        super.buildClone(icdPostForSubPanel);
        return icdPostForSubPanel;
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDPostForSubPanel(this.getId(), this.currentType, this.currentOption));
    }
    
    @Override
    public EntityObject frameClone(final EntityObject entityObject) {
        return this.buildFrameClone(new ICDPostForSubPanel(this.getId(), this.currentType, this.currentOption), entityObject);
    }
    
    public EntityObject buildFrameClone(final ICDPostForSubPanel icdPostForSubPanel, final EntityObject entityObject) {
        return super.buildFrameClone(icdPostForSubPanel, entityObject);
    }
    
    @Override
    protected void calculateZDimension() {
    }
    
    @Override
    public String getBottomJointType() {
        return "2 Way (90)";
    }
    
    @Override
    public Vector<Float> getSplitLocations() {
        final Vector<Float> vector = new Vector<Float>();
        vector.add(0.0f);
        vector.add(this.getZDimension() - 1.0f);
        return vector;
    }
    
    @Override
    public float getTallestWallHeight() {
        return this.getZDimension();
    }
    
    @Override
    public String getJointTypeAtLocation(final Point3f point3f) {
        return "2 Way (90)";
    }
    
    @Override
    protected ICDPostHostInterface getPostHostInterface() {
        return this;
    }
    
    @Override
    protected void calculateNamedPoints() {
        super.calculateNamedPoints();
        this.getNamedPointLocal("bottomJointBP").set(0.0f, 0.0f, 0.5f);
    }
    
    @Override
    public Vector<ILineInterface> getConnectedILines() {
        return null;
    }
    
    @Override
    protected boolean compareSpaces() {
        return false;
    }
    
    @Override
    public void populateCompareNode(final Class clazz, final CompareNode compareNode) {
        super.populateCompareNode(clazz, compareNode);
        compareNode.addCompareValue("worksurface_height", (Object)this.getHeight());
    }
}
