package com.ericversteeg.view;

import java.util.Map;

public class RSColumn extends RSViewGroup
{
    public RSColumn(int x, int y, int w, int h)
    {
        super(x, y, w, h);
    }

    @Override
    protected Map<RSLayoutGuide, Integer> layoutSubviews(Map<RSLayoutGuide, Integer> guides)
    {
        w = measureWidth(guides);

        int topGuide = paddingTop;
        for (RSView view: subviews)
        {
            if (view.getWeightParams() != null
                    && view.getWeightParams().getWeight() != null)
            {
                view.setW(0);
            }

            topGuide += view.getMarginTop();
            view.applyPosition(
                    new RSLayoutGuide.Builder()
                            .maxWidth(w)
                            .top(topGuide)
                            .start(paddingStart + view.getMarginStart())
                            .end(paddingEnd + view.getMarginEnd())
                            .build()
            );
            topGuide += view.getH();
            topGuide += view.getMarginBottom();
        }

        int bottomGuide = topGuide + paddingBottom;

        return new RSLayoutGuide.Builder().bottom(bottomGuide).build();
    }
}
