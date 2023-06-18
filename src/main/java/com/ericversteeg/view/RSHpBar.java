package com.ericversteeg.view;

import java.awt.*;

public class RSHpBar extends RSView {

    private Color topColorOuter = Color.decode("#0d8f0f");
    private Color topColorInner = Color.decode("#14d512");
    private Color darkColor = Color.decode("#0B780D");
    private Color bottomColor = Color.decode("#0F7311");
    private float [] stops = new float [] {0f, 0.325f, 0.65f, 0.7f, 1.0f};
    private Color [] colors = new Color [] {topColorOuter, topColorInner, topColorOuter, darkColor, bottomColor};

    private Color overlayStart = new Color(0, 0, 0, 0);
    private Color  overlayEnd = new Color(247, 247, 143, 60);
    private float [] overlayStops = new float [] {0f, 1f};
    private Color [] overlayColors = new Color [] {overlayStart, overlayEnd};

    public RSHpBar(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    public void render(Graphics2D graphics, Point origin) {
        super.render(graphics, origin);

        LinearGradientPaint gradientPaint = new LinearGradientPaint(origin.x, origin.y, origin.x, origin.y + h, stops, colors);
        graphics.setPaint(gradientPaint);
        graphics.fillRect(origin.x, origin.y, w, h);

        gradientPaint = new LinearGradientPaint(origin.x, h, origin.x + w, h, overlayStops, overlayColors);
        graphics.setPaint(gradientPaint);
        graphics.fillRect(origin.x, origin.y, w, h);
    }
}
