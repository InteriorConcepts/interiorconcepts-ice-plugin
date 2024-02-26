// 
// Decompiled by Procyon v0.5.36
// 

package net.iceedge.catalogs.icd.worksurfaces;

import javax.vecmath.Vector3f;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.Parameter2D;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.FilletParameter;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.LineParameter;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.OptionObject;
import net.dirtt.icelib.main.TypeObject;
import net.iceedge.icecore.basemodule.baseclasses.worksurfaces.parametric.buildingblock.CircleParameter;

public class ICDCCSCParametricWorksurface extends ICDCCSParametricWorksurface
{
    private CircleParameter topCircle;
    private CircleParameter bottomCircle;
    
    public ICDCCSCParametricWorksurface(final String s, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s, typeObject, optionObject);
        this.topCircle = new CircleParameter();
        this.bottomCircle = new CircleParameter();
    }
    
    public ICDCCSCParametricWorksurface(final String s, final String s2, final TypeObject typeObject, final OptionObject optionObject) {
        super(s, s2, typeObject, optionObject);
        this.topCircle = new CircleParameter();
        this.bottomCircle = new CircleParameter();
    }
    
    @Override
    public Object clone() {
        return this.buildClone(new ICDCCSCParametricWorksurface(this.getId(), this.currentType, this.currentOption));
    }
    
    public ICDCCSCParametricWorksurface buildClone(final ICDCCSCParametricWorksurface icdccscParametricWorksurface) {
        super.buildClone(icdccscParametricWorksurface);
        icdccscParametricWorksurface.calculateParameters();
        return icdccscParametricWorksurface;
    }
    
    @Override
    protected void calculateParameters() {
        this.getAttributeValueAsFloat("ICD_Curve_Subdivision_Factor");
        this.shape.clear();
        this.validateDimensionAttributes();
        final float n = this.getAttributeValueAsFloat("ICD_Parametric_Width") / 2.0f;
        final float n2 = this.getAttributeValueAsFloat("ICD_Parametric_Depth") / 2.0f;
        final float n3 = 1.0f;
        final float n4 = ((1.125f - n3) * (1.125f - n3) + n * (n - 2.25f)) / 2.0f / n3;
        final float n5 = n4 - n3 - (float)Math.sqrt(n4 * n4 - n * n) + n2;
        final Point3f point3f = new Point3f(-n, n5, 0.0f);
        final Point3f point3f2 = new Point3f(n, n5, 0.0f);
        final Point3f point3f3 = new Point3f(-n, -n5, 0.0f);
        final Point3f point3f4 = new Point3f(n, -n5, 0.0f);
        this.topCircle = new CircleParameter(new Point3f(0.0f, n4 + n2 - n3, 0.0f), n4);
        this.bottomCircle = new CircleParameter(new Point3f(0.0f, -(n4 + n2 - n3), 0.0f), n4);
        this.rightEdge = new LineParameter(point3f2, point3f4);
        this.leftEdge = new LineParameter(point3f3, point3f);
        this.topLeftFillet = new FilletParameter(this.leftEdge, this.topCircle, 1.125f, point3f, true, false, true);
        this.topRightFillet = new FilletParameter(this.rightEdge, this.topCircle, 1.125f, point3f2, true, false, true);
        this.bottomRightFillet = new FilletParameter(this.rightEdge, this.bottomCircle, 1.125f, point3f4, true, false, true);
        this.bottomLeftFillet = new FilletParameter(this.leftEdge, this.bottomCircle, 1.125f, point3f3, true, false, true);
        this.topLeftFillet.setClockwisePath(true);
        this.topRightFillet.setClockwisePath(true);
        this.bottomRightFillet.setClockwisePath(true);
        this.bottomLeftFillet.setClockwisePath(true);
        this.topCircle.calculate();
        this.bottomCircle.calculate();
        this.allParameters.clear();
        this.plotNodes.clear();
        this.allParameters.add((Parameter2D)this.topCircle);
        this.allParameters.add((Parameter2D)this.topRightFillet);
        this.allParameters.add((Parameter2D)this.rightEdge);
        this.allParameters.add((Parameter2D)this.bottomRightFillet);
        this.allParameters.add((Parameter2D)this.bottomCircle);
        this.allParameters.add((Parameter2D)this.bottomLeftFillet);
        this.allParameters.add((Parameter2D)this.leftEdge);
        this.allParameters.add((Parameter2D)this.topLeftFillet);
        this.updateBRep(true, true);
        this.width1Anchor = new Point3f(-n, 0.0f, 0.0f);
        this.width2Anchor = new Point3f(n, 0.0f, 0.0f);
        this.width1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.width2Direction = new Vector3f(-1.0f, 0.0f, 0.0f);
        this.depth1Anchor = new Point3f(point3f3);
        this.depth1Direction = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutRefPoint = new Point3f(point3f);
        this.cutoutXDirection = new Vector3f(1.0f, 0.0f, 0.0f);
        this.cutoutYDirection = new Vector3f(0.0f, 1.0f, 0.0f);
        this.wireDipRefPoints.clear();
        this.wireDipRefPoints.add(new Point3f(point3f));
    }
    
    @Override
    public String getShapeTag() {
        return "CCSC";
    }
}
