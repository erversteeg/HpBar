package com.ericversteeg.view;

import java.awt.*;

public class RSViewBorder implements RSRenderable
{
    private RSView view;
    private Color innerColor;
    private Color outerColor;

    public RSViewBorder(RSView view, Color color)
    {
        this.view = view;

        innerColor = color;
    }

    public RSViewBorder(RSView view, Color innerColor, Color outerColor)
    {
        this.view = view;

        this.innerColor = innerColor;
        this.outerColor = outerColor;
    }

    @Override
    public void render(Graphics2D graphics, Point origin)
    {
        if (outerColor != null)
        {
            graphics.setColor(outerColor);
            graphics.drawRect(origin.x + view.getX(), origin.y + view.getY(), view.getW(), view.getH());

            graphics.setColor(outerColor);
            graphics.drawRect(origin.x + view.getX() - 1, origin.y + view.getY() - 1, view.getW() + 2, view.getH() + 2);
        }

        if (innerColor != null)
        {
            graphics.setColor(innerColor);
            graphics.drawRect(origin.x + view.getX() + 1, origin.y + view.getY() + 1, view.getW() - 2, view.getH() - 2);
        }
    }
}
