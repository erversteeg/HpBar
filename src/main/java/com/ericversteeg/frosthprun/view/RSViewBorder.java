package com.ericversteeg.frosthprun.view;

import java.awt.*;

public class RSViewBorder implements RSRenderable
{
    private RSView view;
    private Color innerColor;
    private Color outerColor;
    private float opacity = 1f;

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

    public float getOpacity()
    {
        return opacity;
    }

    public void setOpacity(float opacity)
    {
        this.opacity = opacity;
    }

    @Override
    public void render(Graphics2D graphics, Point origin)
    {
        if (outerColor != null)
        {
            graphics.setColor(colorWithOpacity(outerColor));
            graphics.drawRect(origin.x + view.getX(), origin.y + view.getY(), view.getW(), view.getH());

            graphics.setColor(colorWithOpacity(outerColor));
            graphics.drawRect(origin.x + view.getX() - 1, origin.y + view.getY() - 1, view.getW() + 2, view.getH() + 2);
        }

        if (innerColor != null)
        {
            graphics.setColor(colorWithOpacity(innerColor));
            graphics.drawRect(origin.x + view.getX() + 1, origin.y + view.getY() + 1, view.getW() - 2, view.getH() - 2);
        }
    }

    protected Color colorWithOpacity(Color color)
    {
        return new Color(color.getRed() / 255f, color.getGreen() / 255f,
                color.getBlue() / 255f, (opacity * color.getAlpha()) / 255f);
    }
}
