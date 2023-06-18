package com.ericversteeg.view;

import java.awt.*;
import java.util.Map;

public class RSView implements RSRenderable
{
    class DimensionParams
    {
        private int w;
        private int h;
        public DimensionParams(int w, int h)
        {
            this.w = w;
            this.h = h;
        }

        public int getW()
        {
            return w;
        }

        public void setW(int w)
        {
            this.w = w;
        }

        public int getH()
        {
            return h;
        }

        public void setH(int h)
        {
            this.h = h;
        }
    }

    class WeightParams
    {
        private Float weight;

        public WeightParams(Float weight)
        {
            this.weight = weight;
        }

        public Float getWeight()
        {
            return weight;
        }

        public void setWeight(Float weight)
        {
            this.weight = weight;
        }
    }

    protected DimensionParams dimensionParams;

    public static int MATCH_PARENT = -1;
    public static int WRAP_CONTENT = -2;

    protected RSViewGroup.Gravity layoutGravity = null;

    protected int x;
    protected int y;
    protected int w;
    protected int h;

    private Float weight = null;
    private WeightParams weightParams = null;

    protected int marginStart = 0;
    protected int marginTop = 0;
    protected int marginEnd = 0;
    protected int marginBottom = 0;

    protected int paddingStart = 0;
    protected int paddingTop = 0;
    protected int paddingEnd = 0;
    protected int paddingBottom = 0;

    protected Color bgColor = new Color(0, 0, 0, 0);
    protected RSViewBorder border;

    protected RSViewGroup parent = null;

    public RSView(int x, int y, int w, int h)
    {
        this.dimensionParams = new DimensionParams(w, h);
        this.x = x;
        this.y = y;
    }

    public void addBorder(Color color)
    {
        border = new RSViewBorder(this, color);
    }

    public void addBorder(Color innerColor, Color outerColor)
    {
        border = new RSViewBorder(this, innerColor, outerColor);
    }

    public void setLayoutGravity(RSViewGroup.Gravity layoutGravity)
    {
        this.layoutGravity = layoutGravity;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getX()
    {
        return x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getY()
    {
        return y;
    }

    public int getW()
    {
        return w;
    }

    public void setW(int w)
    {
        this.w = w;
    }

    public int getH()
    {
        return h;
    }

    public void setH(int h)
    {
        this.h = h;
    }

    public Float getWeight()
    {
        return weight;
    }

    public void setWeight(Float weight)
    {
        this.weight = weight;
        weightParams = new WeightParams(weight);
    }

    public WeightParams getWeightParams() { return weightParams; }

    public int getMarginStart()
    {
        return marginStart;
    }

    public int getMarginTop()
    {
        return marginTop;
    }

    public int getMarginEnd()
    {
        return marginEnd;
    }

    public int getMarginBottom()
    {
        return marginBottom;
    }

    public void setParent(RSViewGroup parent)
    {
        this.parent = parent;
    }

    public void setBgColor(Color bgColor)
    {
        this.bgColor = bgColor;
    }

    public void setMarginStart(int marginStart)
    {
        this.marginStart = marginStart;
    }

    public void setMarginTop(int marginTop)
    {
        this.marginTop = marginTop;
    }

    public void setMarginEnd(int marginEnd)
    {
        this.marginEnd = marginEnd;
    }

    public void setMarginBottom(int marginBottom)
    {
        this.marginBottom = marginBottom;
    }

    public RSView getParent()
    {
        return parent;
    }

    protected Dimension applyDimension(Map<RSLayoutGuide, Integer> guides)
    {
        w = measureWidth(guides);
        h = measureHeight(guides);

        if (guides.containsKey(RSLayoutGuide.MAX_WIDTH))
        {
            int wGuide = guides.get(RSLayoutGuide.MAX_WIDTH);
            if (dimensionParams.getW() == MATCH_PARENT)
            {
                w = wGuide;
            }
        }

        if (guides.containsKey(RSLayoutGuide.MAX_HEIGHT))
        {
            int hGuide = guides.get(RSLayoutGuide.MAX_HEIGHT);
            if (dimensionParams.getH() == MATCH_PARENT)
            {
                h = hGuide;
            }
        }

        return new Dimension(w, h);
    }

    public int measureWidth(Map<RSLayoutGuide, Integer> guides)
    {
        return dimensionParams.getW();
    }

    public int measureHeight(Map<RSLayoutGuide, Integer> guides)
    {
        return dimensionParams.getH();
    }

    protected void applyPosition(Map<RSLayoutGuide, Integer> guides)
    {
        // x
        switch (getLayoutGravity())
        {
            case START:
            case TOP_START:
            case BOTTOM_START:
                x = guideOrThrow(RSLayoutGuide.START, guides);
                break;
            case CENTER:
            case TOP:
            case BOTTOM:
                int wGuide =  guideOrThrow(RSLayoutGuide.MAX_WIDTH, guides);
                x = (wGuide - w) / 2;
                break;
            case END:
            case TOP_END:
            case BOTTOM_END:
                x = guideOrThrow(RSLayoutGuide.END, guides) - w;
                break;
        }

        // y
        switch (getLayoutGravity())
        {
            case TOP_START:
            case TOP:
            case TOP_END:
                y = guideOrThrow(RSLayoutGuide.TOP, guides);
                break;
            case START:
            case CENTER:
            case END:
                int hGuide =  guideOrThrow(RSLayoutGuide.MAX_HEIGHT, guides);
                y = (hGuide - h) / 2;
                break;
            case BOTTOM_START:
            case BOTTOM:
            case BOTTOM_END:
                y = guideOrThrow(RSLayoutGuide.BOTTOM, guides) - h;
                break;
        }
    }

    public int guideOrThrow(RSLayoutGuide guide, Map<RSLayoutGuide, Integer> guides)
    {
        Integer val = guides.get(guide);
        if (val == null) throw new IllegalStateException("Must include a " + guide.toString() + " guide.");
        return val;
    }

    public RSViewGroup.Gravity getLayoutGravity()
    {
        if (layoutGravity == null)
        {
            return parent.getGravity();
        }
        return layoutGravity;
    }

    @Override
    public void render(Graphics2D graphics, Point origin)
    {
        graphics.setColor(bgColor);
        graphics.fillRect(origin.x + x, origin.y + y, w, h);

        if (border != null)
        {
            border.render(graphics, origin);
        }
    }
}