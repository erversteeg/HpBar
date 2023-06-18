package com.ericversteeg.view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class RSImageView extends RSView
{
    private BufferedImage image;
    private int offset;
    private boolean offsetNegative = false;

    public RSImageView(int w, int h) {
        super(0, 0, w, h);
    }

    public void setImage(BufferedImage image)
    {
        this.image = image;
    }

    public void setOffset(int offset, boolean negative)
    {
        this.offset = offset;
        this.offsetNegative = negative;
    }

    @Override
    public int measureWidth(Map<RSLayoutGuide, Integer> guides)
    {
        if (image == null) return 0;

        return paddingStart + image.getWidth() + paddingEnd;
    }

    @Override
    public int measureHeight(Map<RSLayoutGuide, Integer> guides)
    {
        if (image == null) return 0;

        return paddingTop + image.getHeight() + paddingBottom;
    }

    @Override
    public void render(Graphics2D graphics, Point origin)
    {
        super.render(graphics, origin);

        int signedOffset = offset;
        if (offsetNegative)
        {
            signedOffset *= -1;
        }

        graphics.drawImage(image, origin.x + x + paddingStart,
                origin.y + y + paddingTop + signedOffset, null);
    }
}
