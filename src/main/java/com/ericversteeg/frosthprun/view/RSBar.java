package com.ericversteeg.frosthprun.view;

import com.ericversteeg.frosthprun.config.BarStyle;

import java.awt.*;

public class RSBar extends RSView {

    // prayer 155
    // hp 0
    // run 50
    // special attack 180

    private Color topColorOuter = Color.decode("#0d8f0f");
    //private Color topColorInner = Color.decode("#14d512");
    private Color topColorInner = Color.decode("#11D614");
    private Color darkColor = Color.decode("#0B780D");
    private Color bottomColor = Color.decode("#0F7311");
    //private float [] stops = new float [] {0f, 0.325f, 0.65f, 0.7f, 1.0f};
    private float [] stops = new float [] {0f, 0.275f, 0.55f, 0.65f, 1.0f};
    private Color [] colors = new Color [] {topColorOuter, topColorInner, topColorOuter, darkColor, bottomColor};

    private Color overlayStart = new Color(150, 150, 150, 0);
    private Color  overlayEnd = new Color(247, 247, 143, 30);
    private float [] overlayStops = new float [] {0f, 1f};
    private Color [] overlayColors = new Color [] {overlayStart, overlayEnd};

    private Color outerBorderColor = new Color(57, 41, 13, 200);
    private Color innerBorderColor = new Color(147, 141, 130, 120);

    private float maxValue;
    private int value;

    private BarStyle barStyle;

    public RSBar(int w, int h, int maxValue, BarStyle barStyle) {
        super(0, 0, w, h);

        this.maxValue = maxValue;
        this.barStyle = barStyle;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        int oldValue = this.value;
        this.value = value;

        if (oldValue != value)
        {
            //System.out.println("Starting animation");
            float pixelsPerValue = w / maxValue;

            float pixels = Math.abs((oldValue - value) * pixelsPerValue);

            if (pixels >= 5)
            {
                this.animate()
                        .interpolate(oldValue, value)
                        .duration(0.2f)
                        .easeOut()
                        .start();
            }
        }
    }

    public int getMaxValue()
    {
        return (int) maxValue;
    }

    public void setHue(float hue)
    {
        topColorOuter = hueColor(topColorOuter, hue);
        topColorInner = hueColor(topColorInner, hue);
        darkColor = hueColor(darkColor, hue);
        bottomColor = hueColor(bottomColor, hue);
    }

    @Override
    public void render(Graphics2D graphics, Point origin) {
        super.render(graphics, origin);

        if (maxValue == 0) return;

        colors = new Color [] {
                colorWithOpacity(topColorOuter),
                colorWithOpacity(topColorInner),
                colorWithOpacity(topColorOuter),
                colorWithOpacity(darkColor),
                colorWithOpacity(bottomColor)
        };

        graphics.setPaint(colorWithOpacity(new Color(0, 0, 0, 156)));

        graphics.fillRect(origin.x + x, origin.y + y, w, h);

        if (barStyle == BarStyle.ROUND)
        {
            LinearGradientPaint gradientPaint = new LinearGradientPaint(origin.x + x, origin.y + y,
                    origin.x + x, origin.y + y + h, stops, colors);
            graphics.setPaint(gradientPaint);
        }
        else if (barStyle == BarStyle.FLAT)
        {
            graphics.setColor(topColorInner);
        }
        else
        {
            graphics.setColor(darkColor);
        }

        int barSize = (int) (w * (value / maxValue));
        RSAnimation animation = animations.get(RSAnimation.Type.INTERPOLATE);
        if (animation != null)
        {
            //float aValue = animation.getValue();
            //System.out.println("Animation value is " + aValue);
            barSize = (int) (w * (animation.getValue() / maxValue));
            //System.out.println("Bar size is " + barSize);
        }

        graphics.fillRect(origin.x + x, origin.y + y, barSize, h);

        LinearGradientPaint gradientPaint = new LinearGradientPaint(origin.x + x, h, origin.x + x + w, h, overlayStops, overlayColors);
        graphics.setPaint(gradientPaint);
        graphics.fillRect(origin.x + x, origin.y + y, barSize, h);

        graphics.setColor(outerBorderColor);
        graphics.drawRect(origin.x + x, origin.y + y, barSize, h);

        graphics.setColor(outerBorderColor);
        graphics.drawRect(origin.x + x - 1, origin.y - 1, barSize + 2, h + 2);

        graphics.setColor(innerBorderColor);
        graphics.drawRect(origin.x + x + 1, origin.y + 1, barSize - 2, h - 2);
    }

    private Color hueColor(Color color, float hue)
    {
        float [] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        return Color.getHSBColor(hue / 360f, hsb[1], hsb[2]);
    }
}
