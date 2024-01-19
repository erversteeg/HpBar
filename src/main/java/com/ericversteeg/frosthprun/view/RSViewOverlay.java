package com.ericversteeg.frosthprun.view;

import net.runelite.api.Client;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;

import java.awt.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RSViewOverlay extends Overlay
{
    private Map<String, ViewInfo> viewInfo = new HashMap<>();
    private Set<String> pendingRemoves = new HashSet<>();

    @Override
    public Dimension render(Graphics2D graphics)
    {
        removePending();

        long start = Instant.now().toEpochMilli();

        int numInfo = 0;
        for (String name: viewInfo.keySet())
        {
            ViewInfo info = viewInfo.get(name);

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

                if (anchorType == RSAnchorType.BOTTOM_CENTER)
                {
                    view.setX((viewportWidget.getCanvasLocation().getX() + viewportWidget.getWidth() + 28 - view.getW()) / 2 + anchorX);
                    view.setY(viewportWidget.getCanvasLocation().getY() + viewportWidget.getHeight() + 41 - anchorY - view.getH());
                }
            }

            view.render(graphics, new Point(0, 0));

            numInfo += 1 ;
        }

        //System.out.println(numInfo);

        //System.out.println("Render in " + (Instant.now().toEpochMilli() - start) + "ms");

        return new Dimension(0, 0);
    }

    public Map<String, ViewInfo> getViewInfo()
    {
        return viewInfo;
    }

    public void addViewInfo(String name, ViewInfo viewInfo) {
        this.viewInfo.put(name, viewInfo);
    }

    public void removeViewInfo(String name, boolean immediate)
    {
        if (immediate)
        {
            viewInfo.remove(name);
        }
        else
        {
            pendingRemoves.add(name);
        }
    }

    private void removePending()
    {
        for (String name: pendingRemoves)
        {
            viewInfo.remove(name);
        }
        pendingRemoves.clear();
    }

    public boolean containsViewInfo(String name)
    {
        return viewInfo.containsKey(name) && !pendingRemoves.contains(name);
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

        widget = client.getWidget(ComponentID.RESIZABLE_VIEWPORT_INTERFACE_CONTAINER);
        if (widget != null) return widget;

        widget = client.getWidget(ComponentID.RESIZABLE_VIEWPORT_BOTTOM_LINE_INTERFACE_CONTAINER);
        if (widget != null) return widget;

        widget = client.getWidget(ComponentID.FIXED_VIEWPORT_INTERFACE_CONTAINER);
        if (widget != null) return widget;

        return client.getWidget(ComponentID.BANK_INVENTORY_ITEM_CONTAINER);
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
