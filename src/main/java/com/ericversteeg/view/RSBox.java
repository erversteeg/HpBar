package com.ericversteeg.view;

import java.util.Map;

public class RSBox extends RSViewGroup
{
    public RSBox(int x, int y, int w, int h)
    {
        super(x, y, w, h);
    }

    @Override
    protected Map<RSLayoutGuide, Integer> layoutSubviews(Map<RSLayoutGuide, Integer> guides)
    {
        w = measureWidth(guides);
        h = measureHeight(guides);

        for (RSView view: subviews)
        {
            view.applyPosition(
                    new RSLayoutGuide.Builder()
                            .maxWidth(w)
                            .maxHeight(h)
                            .start(paddingStart + view.getMarginStart())
                            .top(paddingTop + view.getMarginTop())
                            .end(w - paddingEnd - view.getMarginEnd())
                            .bottom(h - paddingBottom - view.getMarginBottom())
                            .build()
            );
        }

        return new RSLayoutGuide.Builder().build();
    }
}
