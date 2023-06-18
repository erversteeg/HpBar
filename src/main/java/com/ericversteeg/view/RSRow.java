package com.ericversteeg.view;

import java.util.Map;

public class RSRow extends RSViewGroup
{
    public RSRow(int x, int y, int w, int h)
    {
        super(x, y, w, h);
    }

    @Override
    protected Map<RSLayoutGuide, Integer> layoutSubviews(Map<RSLayoutGuide, Integer> guides)
    {
        h = measureHeight(guides);

        int startGuide = paddingTop;
        for (RSView view: subviews)
        {
            if (view.getWeightParams() != null
                    && view.getWeightParams().getWeight() != null)
            {
                view.setW(0);
            }

            startGuide += view.getMarginStart();
            view.applyPosition(
                    new RSLayoutGuide.Builder()
                            .maxHeight(h)
                            .start(startGuide)
                            .top(paddingTop + view.getMarginTop())
                            .bottom(paddingBottom + view.getMarginBottom())
                            .build()
            );
            startGuide += view.getW();
            startGuide += view.getMarginEnd();
        }

        int endGuide = startGuide + paddingEnd;

        return new RSLayoutGuide.Builder().end(endGuide).build();
    }
}
