// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces.parametric;

import java.util.Iterator;
import net.dirtt.icebox.iceoutput.core.IceOutputLayerNode;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import java.util.Collection;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.CircleParameter;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.attributes.FloatAttribute;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDParametricCircularCutout extends ICDParametricCutout
{
    public static final String LAYER_NAME_FOR_DXF = "DIM";
    
    public ICDParametricCircularCutout(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
    }
    
    public ICDParametricCircularCutout(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDParametricCircularCutout(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDParametricCircularCutout buildClone(final ICDParametricCircularCutout icdParametricCircularCutout) {
        super.buildClone(icdParametricCircularCutout);
        return icdParametricCircularCutout;
    }
    
    @Override
    protected void calculateParameters() {
        float currentValue = 1.0f;
        if (this.getAttributeObject("XDimension") != null) {
            currentValue = ((FloatAttribute)this.getAttributeObject("XDimension")).getCurrentValue();
        }
        final CircleParameter circleParameter = new CircleParameter(new Point3f(), currentValue / 2.0f);
        this.shape.clear();
        this.shape.addAll(circleParameter.getPath((Point3f)null, (Point3f)null, false, false, 25.0f));
        this.plotNodes.clear();
        this.plotNodes.add((IceOutputNode)circleParameter.getArc(new Point3f(-currentValue / 2.0f, 0.0f, 0.0f), new Point3f(currentValue / 2.0f, 0.0f, 0.0f), this.getEntWorldSpaceMatrix()));
        this.plotNodes.add((IceOutputNode)circleParameter.getArc(new Point3f(currentValue / 2.0f, 0.0f, 0.0f), new Point3f(-currentValue / 2.0f, 0.0f, 0.0f), this.getEntWorldSpaceMatrix()));
        final IceOutputLayerNode parent = new IceOutputLayerNode("DIM");
        final Iterator<IceOutputNode> iterator = this.plotNodes.iterator();
        while (iterator.hasNext()) {
            iterator.next().setParent((IceOutputNode)parent);
        }
    }
}
