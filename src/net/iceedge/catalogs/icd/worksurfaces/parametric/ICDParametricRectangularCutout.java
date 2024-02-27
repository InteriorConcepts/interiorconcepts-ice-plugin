package net.iceedge.catalogs.icd.worksurfaces.parametric;

import net.dirtt.utilities.MathUtilities;
import java.util.Iterator;
import javax.vecmath.Matrix4f;
import net.dirtt.icebox.iceoutput.core.IceOutputLayerNode;
import net.dirtt.icebox.iceoutput.core.IceOutputNode;
import java.util.Collection;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.CircleParameter;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.attributes.FloatAttribute;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;

public class ICDParametricRectangularCutout extends ICDParametricCutout
{
    public static final float ICD_CORNER_CIRCLE_PARAM_OFFSET = 0.12f;
    public static final float ICD_CORNER_CIRCLE_RADIUS = 0.1875f;
    public static final String LAYER_NAME_FOR_DXF_SP_Plw_PSG = "SP_Plw_PSG";
    public static final String LAYER_NAME_FOR_DXF_SP_Plw_560 = "SP_Plw_560";
    public static final String LAYER_NAME_FOR_DXF_SP_Plw_565 = "SP_Plw_565";
    
    public ICDParametricRectangularCutout(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
    }
    
    public ICDParametricRectangularCutout(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
    }
    
    public Object clone() {
        return this.buildClone(new ICDParametricRectangularCutout(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDParametricRectangularCutout buildClone(final ICDParametricRectangularCutout icdParametricRectangularCutout) {
        super.buildClone(icdParametricRectangularCutout);
        return icdParametricRectangularCutout;
    }
    
    public void calculateParameters() {
        float currentValue = 1.0f;
        float currentValue2 = 1.0f;
        if (this.getAttributeObject("XDimension") != null) {
            currentValue = ((FloatAttribute)this.getAttributeObject("XDimension")).getCurrentValue();
        }
        if (this.getAttributeObject("YDimension") != null) {
            currentValue2 = ((FloatAttribute)this.getAttributeObject("YDimension")).getCurrentValue();
        }
        final Point3f point3f = new Point3f(-currentValue / 2.0f, -currentValue2 / 2.0f, 0.0f);
        final Point3f point3f2 = new Point3f(-currentValue / 2.0f, currentValue2 / 2.0f, 0.0f);
        final Point3f point3f3 = new Point3f(currentValue / 2.0f, currentValue2 / 2.0f, 0.0f);
        final Point3f point3f4 = new Point3f(currentValue / 2.0f, -currentValue2 / 2.0f, 0.0f);
        final Point3f point = ICDParametricWorksurface.pointAt(ICDParametricWorksurface.pointAt(point3f, new Vector3f(1.0f, 0.0f, 0.0f), 0.12f), new Vector3f(0.0f, 1.0f, 0.0f), 0.12f);
        final Point3f point2 = ICDParametricWorksurface.pointAt(ICDParametricWorksurface.pointAt(point3f2, new Vector3f(1.0f, 0.0f, 0.0f), 0.12f), new Vector3f(0.0f, -1.0f, 0.0f), 0.12f);
        final Point3f point3 = ICDParametricWorksurface.pointAt(ICDParametricWorksurface.pointAt(point3f3, new Vector3f(-1.0f, 0.0f, 0.0f), 0.12f), new Vector3f(0.0f, -1.0f, 0.0f), 0.12f);
        final Point3f point4 = ICDParametricWorksurface.pointAt(ICDParametricWorksurface.pointAt(point3f4, new Vector3f(-1.0f, 0.0f, 0.0f), 0.12f), new Vector3f(0.0f, 1.0f, 0.0f), 0.12f);
        final CircleParameter circleParameter = new CircleParameter(point, 0.1875f);
        final CircleParameter circleParameter2 = new CircleParameter(point2, 0.1875f);
        final CircleParameter circleParameter3 = new CircleParameter(point3, 0.1875f);
        final CircleParameter circleParameter4 = new CircleParameter(point4, 0.1875f);
        final LineParameter lineParameter = new LineParameter(point3f, point3f2);
        final LineParameter lineParameter2 = new LineParameter(point3f2, point3f3);
        final LineParameter lineParameter3 = new LineParameter(point3f3, point3f4);
        final LineParameter lineParameter4 = new LineParameter(point3f4, point3f);
        final Point3f point3f5 = circleParameter.getRayIntersections(point3f4, new Vector3f(-1.0f, 0.0f, 0.0f), CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
        final Point3f point3f6 = circleParameter.getRayIntersections(point3f2, new Vector3f(0.0f, -1.0f, 0.0f), CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
        final Point3f point3f7 = circleParameter2.getRayIntersections(point3f, new Vector3f(0.0f, 1.0f, 0.0f), CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
        final Point3f point3f8 = circleParameter2.getRayIntersections(point3f3, new Vector3f(-1.0f, 0.0f, 0.0f), CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
        final Point3f point3f9 = circleParameter3.getRayIntersections(point3f2, new Vector3f(1.0f, 0.0f, 0.0f), CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
        final Point3f point3f10 = circleParameter3.getRayIntersections(point3f4, new Vector3f(0.0f, 1.0f, 0.0f), CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
        final Point3f point3f11 = circleParameter4.getRayIntersections(point3f3, new Vector3f(0.0f, -1.0f, 0.0f), CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
        final Point3f point3f12 = circleParameter4.getRayIntersections(point3f, new Vector3f(1.0f, 0.0f, 0.0f), CircleParameter.RAY_INTERSECTON_SOLUTION_TYPE.CLOSER_TO_ORIGIN).get(0);
        this.shape.clear();
        this.shape.addAll(circleParameter.getPath(point3f5, point3f6, false, true, 100.0f));
        this.shape.add(point3f6);
        this.shape.add(point3f7);
        this.shape.addAll(circleParameter2.getPath(point3f7, point3f8, false, true, 100.0f));
        this.shape.add(point3f8);
        this.shape.add(point3f9);
        this.shape.addAll(circleParameter3.getPath(point3f9, point3f10, false, true, 100.0f));
        this.shape.add(point3f10);
        this.shape.add(point3f11);
        this.shape.addAll(circleParameter4.getPath(point3f11, point3f12, false, true, 100.0f));
        this.shape.add(point3f12);
        this.shape.add(point3f5);
        lineParameter.setStartPoint(point3f6);
        lineParameter.setEndPoint(point3f7);
        lineParameter2.setStartPoint(point3f8);
        lineParameter2.setEndPoint(point3f9);
        lineParameter3.setStartPoint(point3f10);
        lineParameter3.setEndPoint(point3f11);
        lineParameter4.setStartPoint(point3f12);
        lineParameter4.setEndPoint(point3f5);
        this.plotNodes.clear();
        final Matrix4f entWorldSpaceMatrix = this.getEntWorldSpaceMatrix();
        this.plotNodes.add((IceOutputNode)lineParameter.getOutputLine(entWorldSpaceMatrix));
        this.plotNodes.add((IceOutputNode)lineParameter2.getOutputLine(entWorldSpaceMatrix));
        this.plotNodes.add((IceOutputNode)lineParameter3.getOutputLine(entWorldSpaceMatrix));
        this.plotNodes.add((IceOutputNode)lineParameter4.getOutputLine(entWorldSpaceMatrix));
        this.plotNodes.add((IceOutputNode)circleParameter.getArc(point3f6, point3f5, entWorldSpaceMatrix));
        this.plotNodes.add((IceOutputNode)circleParameter2.getArc(point3f8, point3f7, entWorldSpaceMatrix));
        this.plotNodes.add((IceOutputNode)circleParameter3.getArc(point3f10, point3f9, entWorldSpaceMatrix));
        this.plotNodes.add((IceOutputNode)circleParameter4.getArc(point3f12, point3f11, entWorldSpaceMatrix));
        String s = "SP_Plw_PSG";
        final String attributeValueAsString = this.getAttributeValueAsString("ICD_Rectangle_Cutout_Indicator");
        if (attributeValueAsString != null && attributeValueAsString.startsWith("STD")) {
            if ("-".equals(attributeValueAsString.substring(6, 7))) {
                s = "SP_Plw_565";
            }
            else {
                s = "SP_Plw_560";
            }
        }
        final IceOutputLayerNode parent = new IceOutputLayerNode(s);
        final Iterator<IceOutputNode> iterator = this.plotNodes.iterator();
        while (iterator.hasNext()) {
            iterator.next().setParent((IceOutputNode)parent);
        }
    }
    
    @Override
    public boolean interferesWithCutouts(final ICDParametricWorksurface icdParametricWorksurface) {
        final float icd_RECTANGULAR_CUTOUT_TO_CUTOUT_OFFSET = ICDParametricRectangularCutout.ICD_RECTANGULAR_CUTOUT_TO_CUTOUT_OFFSET;
        for (final ICDParametricCutout icdParametricCutout : icdParametricWorksurface.getCutouts()) {
            if (icdParametricCutout != this && ICDParametricCutout.getShapeDistance(this.getEntWorldSpaceMatrix(), this.getShape(), icdParametricCutout.getEntWorldSpaceMatrix(), icdParametricCutout.getShape()) < icd_RECTANGULAR_CUTOUT_TO_CUTOUT_OFFSET) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean interferesWithBackEdges(final ICDParametricWorksurface icdParametricWorksurface) {
        final float currentValue = ((FloatAttribute)this.getAttributeObject("ICD_Cutout_Back_Edge_Offset")).getCurrentValue();
        for (final LineParameter lineParameter : icdParametricWorksurface.getLineParameters()) {
            if (MathUtilities.getDistanceFromLine(lineParameter.getStartPoint(), lineParameter.getEndPoint(), this.getBasePoint3f(), true) < currentValue) {
                return true;
            }
        }
        return false;
    }
}
