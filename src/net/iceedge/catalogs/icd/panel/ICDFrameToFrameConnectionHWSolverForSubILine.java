package net.iceedge.catalogs.icd.panel;

import net.iceedge.icecore.basemodule.interfaces.Segment;
import net.iceedge.icecore.basemodule.baseclasses.BasicFrameToFrameConnectionHW;
import net.iceedge.icecore.basemodule.util.FrameToFrameConnectionHWSolverForSubILine;

public class ICDFrameToFrameConnectionHWSolverForSubILine extends FrameToFrameConnectionHWSolverForSubILine
{
    protected void setProductSpecificAttribute(final BasicFrameToFrameConnectionHW basicFrameToFrameConnectionHW, final Segment segment, final Segment segment2) {
        if (basicFrameToFrameConnectionHW instanceof ICDPanelToPanelConnectionHW) {
            ((ICDPanelToPanelConnectionHW)basicFrameToFrameConnectionHW).setLinkedSegments(segment, segment2);
        }
    }
}
