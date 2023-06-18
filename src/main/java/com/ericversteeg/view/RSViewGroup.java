package com.ericversteeg.view;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RSViewGroup extends RSView
{
    public enum Gravity
    {
        START,
        TOP_START,
        TOP,
        TOP_END,
        END,
        BOTTOM_END,
        BOTTOM,
        BOTTOM_START,
        CENTER
    }

    protected Gravity gravity = Gravity.TOP_START;

    protected List<RSView> subviews = new ArrayList<>();

    public RSViewGroup(int x, int y, int w, int h)
    {
        super(x, y, w, h);
    }

    public void setGravity(Gravity gravity)
    {
        this.gravity = gravity;
    }

    public void addView(RSView view)
    {
        subviews.add(view);
        view.setParent(this);
    }

    @Override
    public void render(Graphics2D graphics, Point origin)
    {
        super.render(graphics, origin);

        for (RSView view: subviews)
        {
            view.render(graphics, new Point(origin.x + x, origin.y + y));
        }
    }

    public Gravity getGravity()
    {
        return gravity;
    }

    public int getPaddingStart()
    {
        return paddingStart;
    }

    public int getPaddingTop()
    {
        return paddingTop;
    }

    public int getPaddingEnd()
    {
        return paddingEnd;
    }

    public int getPaddingBottom()
    {
        return paddingBottom;
    }

    public void setPadding(int padding)
    {
        paddingStart = padding;
        paddingTop = padding;
        paddingEnd = padding;
        paddingBottom = padding;
    }

    public Dimension layout()
    {
        return applyDimension(new RSLayoutGuide.Builder()
                .maxWidth(w)
                .build()
        );
    }

    abstract protected Map<RSLayoutGuide, Integer> layoutSubviews(Map<RSLayoutGuide, Integer> guides);

    @Override
    protected Dimension applyDimension(Map<RSLayoutGuide, Integer> guides) {
        // calculate width and height for all subviews before layout
        for (RSView view: subviews)
        {
            view.applyDimension(
                    subviewMaxDimensionGuides(guides, view)
            );
        }

        // layout subviews (x, y)
        Map<RSLayoutGuide, Integer> layoutGuides = layoutSubviewsAndResize(guides);

        boolean appliedWeight = false;

        // apply width weights
        if (dimensionParams.getW() != WRAP_CONTENT)
        {
            Integer endGuide = layoutGuides.get(RSLayoutGuide.END);
            if (endGuide != null)
            {
                float totalWeight = 0;
                int totalAddedWidth = 0;
                int weightWidth = measureWidth(guides) - endGuide;

                for (RSView view: subviews)
                {
                    if (view.getWeight() != null)
                    {
                        totalWeight += view.getWeight();
                    }
                }

                if (totalWeight > 0)
                {
                    for (RSView view: subviews)
                    {
                        view.setX(view.getX() + totalAddedWidth);

                        Float weight = view.getWeight();
                        if (weight != null)
                        {
                            int width = (int) (weight / totalWeight * weightWidth);
                            view.setW(width);

                            view.dimensionParams.setW(width);
                            view.applyDimension(subviewMaxDimensionGuides(guides, view));
                            view.getWeightParams().setWeight(null);

                            appliedWeight = true;

                            totalAddedWidth += width;
                        }
                    }

                    int truncatedWidth = weightWidth - totalAddedWidth;
                    boolean foundView = false;

                    for (RSView view: subviews)
                    {
                        if (getWeight() != null && !foundView)
                        {
                            view.setW(view.getW()
                                    + truncatedWidth);

                            view.dimensionParams.setW(view.getW());
                            view.applyDimension(subviewMaxDimensionGuides(guides, view));

                            foundView = true;
                        }

                        if (foundView)
                        {
                            view.setX(view.getX() + truncatedWidth);
                        }
                    }
                }
            }
        }

        // apply height weights
        if (dimensionParams.getH() != WRAP_CONTENT)
        {
            Integer bottomGuide = layoutGuides.get(RSLayoutGuide.BOTTOM);
            if (bottomGuide != null)
            {
                float totalWeight = 0;
                int totalAddedHeight = 0;
                int weightHeight = measureHeight(guides) - bottomGuide;

                for (RSView view: subviews)
                {
                    if (view.getWeight() != null)
                    {
                        totalWeight += view.getWeight();
                    }
                }

                if (totalWeight > 0)
                {
                    for (RSView view: subviews)
                    {
                        view.setY(view.getY() + totalAddedHeight);

                        Float weight = view.getWeight();
                        if (weight != null)
                        {
                            int height = (int) (weight / totalWeight * weightHeight);
                            view.setH(height);

                            view.dimensionParams.setH(view.getH());
                            view.applyDimension(subviewMaxDimensionGuides(guides, view));
                            view.getWeightParams().setWeight(null);

                            appliedWeight = true;

                            totalAddedHeight += height;
                        }
                    }

                    int truncatedHeight = weightHeight - totalAddedHeight;
                    boolean foundView = false;

                    for (RSView view: subviews)
                    {
                        if (getWeight() != null && !foundView)
                        {
                            view.setH(view.getH()
                                    + truncatedHeight);

                            view.dimensionParams.setH(view.getH());
                            view.applyDimension(subviewMaxDimensionGuides(guides, view));

                            foundView = true;
                        }

                        if (foundView)
                        {
                            view.setY(view.getY() + truncatedHeight);
                        }
                    }
                }
            }
        }

        // if weight was applied
        // layout subviews again
        if (appliedWeight)
        {
            layoutSubviewsAndResize(guides);
        }

        return new Dimension(w, h);
    }

    private Map<RSLayoutGuide, Integer> layoutSubviewsAndResize(Map<RSLayoutGuide, Integer> guides)
    {
        Map<RSLayoutGuide, Integer> layoutGuides = layoutSubviews(guides);

        // measure (w, h)
        if (dimensionParams.getW() == WRAP_CONTENT
                && layoutGuides.containsKey(RSLayoutGuide.END))
        {
            w = layoutGuides.get(RSLayoutGuide.END);
        }
        else
        {
            w = measureWidth(guides);
        }

        if (dimensionParams.getH() == WRAP_CONTENT
                && layoutGuides.containsKey(RSLayoutGuide.BOTTOM))
        {
            h = layoutGuides.get(RSLayoutGuide.BOTTOM);
        }
        else
        {
            h = measureHeight(guides);
        }

        return layoutGuides;
    }

    @Override
    public int measureWidth(Map<RSLayoutGuide, Integer> guides)
    {
        if (dimensionParams.getW() == MATCH_PARENT)
        {
            return guides.get(RSLayoutGuide.MAX_WIDTH);
        }
        else if (dimensionParams.getW() == WRAP_CONTENT)
        {
            return getMaxSubviewWidth();
        }
        else
        {
            return dimensionParams.getW();
        }
    }

    protected int getMaxSubviewWidth()
    {
        int max = 0;
        for (RSView view: subviews)
        {
            int sw = view.getW() + view.getMarginStart()
                    + view.getMarginEnd() + paddingStart
                    + paddingEnd;
            if (sw > max)
            {
                max = sw;
            }
        }
        return max;
    }

    @Override
    public int measureHeight(Map<RSLayoutGuide, Integer> guides)
    {
        if (dimensionParams.getH() == MATCH_PARENT)
        {
            return guides.get(RSLayoutGuide.MAX_HEIGHT);
        }
        else if (dimensionParams.getH() == WRAP_CONTENT)
        {
            return getMaxSubviewHeight();
        }
        else
        {
            return dimensionParams.getH();
        }
    }

    protected int getMaxSubviewHeight()
    {
        int max = 0;
        for (RSView view: subviews)
        {
            int sw = view.getH() + view.getMarginTop()
                    + view.getMarginBottom() + paddingTop
                    + paddingBottom;
            if (sw > max)
            {
                max = sw;
            }
        }
        return max;
    }

    protected Map<RSLayoutGuide, Integer> subviewMaxDimensionGuides(
            Map<RSLayoutGuide, Integer> guides, RSView view)
    {
        Map<RSLayoutGuide, Integer> subviewGuides = new HashMap<>();

        if (dimensionParams.getW() != WRAP_CONTENT)
        {
            int wGuide = dimensionParams.getW();
            if (dimensionParams.getW() == MATCH_PARENT
                    && guides.containsKey(RSLayoutGuide.MAX_WIDTH))
            {
                wGuide = guides.get(RSLayoutGuide.MAX_WIDTH);
            }

            subviewGuides.put(RSLayoutGuide.MAX_WIDTH, wGuide
                    - view.getMarginStart() - view.getMarginEnd()
                    - getPaddingStart() - getPaddingEnd());
        }

        if (dimensionParams.getH() != WRAP_CONTENT)
        {
            int hGuide = dimensionParams.getH();
            if (dimensionParams.getH() == MATCH_PARENT
                    && guides.containsKey(RSLayoutGuide.MAX_HEIGHT))
            {
                hGuide = guides.get(RSLayoutGuide.MAX_HEIGHT);
            }

            subviewGuides.put(RSLayoutGuide.MAX_HEIGHT, hGuide
                    - view.getMarginTop() - view.getMarginBottom()
                    - getPaddingTop() - getPaddingBottom());
        }

        return subviewGuides;
    }
}
