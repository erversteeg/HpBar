package com.ericversteeg.view;

import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.Overlay;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.time.Instant;

public class RSViewOverlay extends Overlay
{
    private List<ViewInfo> viewInfo = new ArrayList<>();

    @Override
    public Dimension render(Graphics2D graphics)
    {
        long start = Instant.now().toEpochMilli();

        for (ViewInfo info: viewInfo)
        {
            Client client = info.getClient();
            RSViewGroup view = info.getView();
            RSAnchorType anchorType = info.getAnchorType();
            int anchorX = info.getAnchorX();
            int anchorY = info.getAnchorY();

            applyFonts(view, graphics);

            view.layout();

            if (anchorType != null)
            {
                Widget viewportWidget = getViewportWidget(client);

                view.setX(anchorX);
                view.setY(anchorY);

                if (anchorType == RSAnchorType.TOP_RIGHT || anchorType == RSAnchorType.BOTTOM_RIGHT)
                {
                    view.setX(viewportWidget.getCanvasLocation().getX() + viewportWidget.getWidth() + 28 - anchorX - view.getW());
                }

                if (anchorType == RSAnchorType.BOTTOM_LEFT || anchorType == RSAnchorType.BOTTOM_RIGHT)
                {
                    view.setY(viewportWidget.getCanvasLocation().getY() + viewportWidget.getHeight() + 41 - anchorY - view.getH());
                }

                if (anchorType == RSAnchorType.TOP_CENTER)
                {
                    view.setX((viewportWidget.getCanvasLocation().getX() + viewportWidget.getWidth() + 28 - view.getW()) / 2 + anchorX);
                }

                if (anchorType == RSAnchorType.LEFT_CENTER)
                {
                    view.setY((viewportWidget.getCanvasLocation().getY() + viewportWidget.getHeight() + 41 - view.getH()) / 2 + anchorY);
                }
            }

            view.render(graphics, new Point(0, 0));
        }

        //System.out.println("Render in " + (Instant.now().toEpochMilli() - start) + "ms");

        return new Dimension(0, 0);
    }

    public void addViewInfo(ViewInfo viewInfo) {
        this.viewInfo.add(viewInfo);
    }

    public void clearViewInfo()
    {
        viewInfo.clear();
    }

    private void applyFonts(RSView view, Graphics2D graphics)
    {
        if (view instanceof RSViewGroup)
        {
            for (RSView sView: ((RSViewGroup) view).subviews)
            {
                applyFonts(sView, graphics);
            }
        }

        if (view instanceof RSTextView)
        {
            ((RSTextView) view).setFontMetrics(graphics);
        }
    }

    private Widget getViewportWidget(Client client)
    {
        Widget widget;

        widget = client.getWidget(WidgetInfo.RESIZABLE_VIEWPORT_INTERFACE_CONTAINER);
        if (widget != null) return widget;

        widget = client.getWidget(WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_INTERFACE_CONTAINER);
        if (widget != null) return widget;

        widget = client.getWidget(WidgetInfo.FIXED_VIEWPORT_INTERFACE_CONTAINER);
        if (widget != null) return widget;

        return client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
    }

    public static class ViewInfo {

        private Client client;
        private RSViewGroup view;
        private RSAnchorType anchorType;
        private int anchorX;
        private int anchorY;

        public ViewInfo(Client client, RSViewGroup view, RSAnchorType anchorType, int anchorX, int anchorY)
        {
            this.client = client;
            this.view = view;
            this.anchorType = anchorType;
            this.anchorX = anchorX;
            this.anchorY = anchorY;
        }

        public Client getClient()
        {
            return client;
        }

        public RSViewGroup getView()
        {
            return view;
        }

        public RSAnchorType getAnchorType()
        {
            return anchorType;
        }

        public int getAnchorX()
        {
            return anchorX;
        }

        public int getAnchorY()
        {
            return anchorY;
        }
    }
}
