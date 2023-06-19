package com.ericversteeg;

import com.ericversteeg.config.BarInfo;
import com.ericversteeg.config.BarType;
import com.ericversteeg.view.*;
import net.runelite.api.*;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import javax.inject.Inject;
import java.util.*;
import java.awt.*;
import java.io.InputStream;
import java.util.List;

class HpBarOverlay extends RSViewOverlay {

	private final Client client;
	private final ItemManager itemManager;
	private final HpBarPlugin plugin;
	private final HpBarConfig config;

	private Font primaryFont;
	private Font secondaryFont;

	List<BarType> types;

	private RSColumn column;

	private RSBar primaryBar;
	private RSTextView primaryTextView;

	private RSBar secondaryBar;
	private RSTextView secondaryTextView;

	private RSBar tertiaryBar;
	private RSTextView tertiaryTextView;

	private RSBar quaternaryBar;
	private RSTextView quaternaryTextView;

	@Inject
	private HpBarOverlay(
			Client client,
			ItemManager itemManager,
			HpBarPlugin plugin,
			HpBarConfig config)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);

		this.client = client;
		this.itemManager = itemManager;
		this.plugin = plugin;
		this.config = config;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.UNDER_WIDGETS);
	}

	void setupViews()
	{
		clearViewInfo();

		System.out.println("Setup Views");

		Map<BarType, BarInfo> barInfo = plugin.barInfo();

		int width = Math.min(800, Math.max(config.width(), 40));
		int height = Math.min(150, Math.max(config.height(), 20));

		setFonts(height);

		boolean hasSecondaryText = config.hasSecondaryText();

		column = new RSColumn(0,0, width, RSView.WRAP_CONTENT);

		if (plugin.isRun() && !plugin.isCombat())
		{
			types = new ArrayList<>();
			types.add(BarType.RUN_ENERGY);
		}
		else
		{
			types = types();
		}

		Collections.reverse(types);

		for (int i = 0; i < types.size(); i++)
		{
			BarInfo info = barInfo.get(types.get(i));

			if (i < types.size() - 1)
			{
				RSBox container = new RSBox(0, 0, RSView.MATCH_PARENT, height / 4);

				RSBar bar = new RSBar(RSView.MATCH_PARENT, RSView.MATCH_PARENT, info.maxValue);
				bar.setHue(info.hue);
				bar.setValue(info.value);

				RSTextView text = new RSTextView(0, 0, RSView.WRAP_CONTENT, RSView.WRAP_CONTENT, secondaryFont);
				text.setText(String.valueOf(bar.getValue()));
				text.setMarginStart(1);
				text.setMarginTop(-1);

				container.addView(bar);

				if (hasSecondaryText)
				{
					container.addView(text);
				}

				column.addView(container);

				if (i == 0)
				{
					secondaryBar = bar;
					secondaryTextView = text;
				}
				else if (i == 1)
				{
					tertiaryBar = bar;
					tertiaryTextView = text;
				}
				else if (i == 2)
				{
					quaternaryBar = bar;
					quaternaryTextView = text;
				}
			}
			else
			{
				RSBox container = new RSBox(0, 0, RSView.MATCH_PARENT, height);

				RSBar bar = new RSBar(RSView.MATCH_PARENT, height, info.maxValue);
				RSTextView text = new RSTextView(0, 0, RSView.WRAP_CONTENT,
						RSView.WRAP_CONTENT, primaryFont);

				bar.setValue(info.value);
				bar.setHue(info.hue);

				text.setText(String.format("%d", bar.getValue()));
				text.setLayoutGravity(RSViewGroup.Gravity.CENTER);

				container.addView(bar);
				container.addView(text);

				column.addView(container);

				primaryBar = bar;
				primaryTextView = text;
			}
		}

		column.setRenderReverse(true);

		column.animate()
				.duration(0.15f)
				.fadeIn()
				.start();

		addViewInfo(new ViewInfo(client, column, config.anchorType(), config.anchorX(), config.anchorY()));
	}

	private void updateViews()
	{
		Map<BarType, BarInfo> barInfo = plugin.barInfo();

		for (int i = 0; i < types.size(); i++)
		{
			BarInfo info = barInfo.get(types.get(i));

			if (i < types.size() - 1)
			{
				if (i == 0)
				{
					secondaryBar.setValue(info.value);
					secondaryTextView.setText(String.valueOf(secondaryBar.getValue()));
				}
				else if (i == 1)
				{
					tertiaryBar.setValue(info.value);
					tertiaryTextView.setText(String.valueOf(tertiaryBar.getValue()));
				}
				else if (i == 2)
				{
					quaternaryBar.setValue(info.value);
					quaternaryTextView.setText(String.valueOf(quaternaryBar.getValue()));
				}
			}
			else
			{
				primaryBar.setValue(info.value);
				primaryTextView.setText(String.valueOf(primaryBar.getValue()));
			}
		}
	}

	private void setFonts(int height)
	{
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			InputStream inputStream = FontManager.class.getResourceAsStream("runescape_bold.ttf");
			Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream)
					.deriveFont(Font.PLAIN,  (int) Math.ceil((double) height / 2));
			ge.registerFont(font);
			primaryFont = font;
		}
		catch (Exception e)
		{
			primaryFont = FontManager.getRunescapeBoldFont();
		}

		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			InputStream inputStream = FontManager.class.getResourceAsStream("runescape_bold.ttf");
			Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream)
					.deriveFont(Font.PLAIN,  (int) Math.ceil((double) height / 4));
			ge.registerFont(font);
			secondaryFont = font;
		}
		catch (Exception e)
		{
			secondaryFont = FontManager.getRunescapeSmallFont();
		}
	}

	private boolean isFadeOut = false;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (getViewInfo().isEmpty())
		{
			if (config.isAlwaysVisible() || plugin.isCombat() || plugin.isRun())
			{
				setupViews();
			}
		}
		else
		{
			if (config.isAlwaysVisible() || plugin.isCombat() || plugin.isRun())
			{
				updateViews();
			}
			else
			{
				if (!isFadeOut)
				{
					column.animate()
							.fadeOut()
							.duration(0.3f)
							.onComplete(() -> {
								clearViewInfo();
								isFadeOut = false;
							})
							.start();

					isFadeOut = true;
				}
			}
		}

		return super.render(graphics);
	}

	private List<BarType> types()
	{
		BarType primary = config.primaryBarType();
		BarType secondary = config.secondaryBarType();
		BarType tertiary = config.tertiaryBarType();
		BarType quaternary = config.quaternaryBarType();

		List<BarType> barTypes = new ArrayList<>();

		if (primary != BarType.NONE)
		{
			barTypes.add(primary);
		}

		if (!barTypes.contains(secondary)
				&& secondary != BarType.NONE)
		{
			barTypes.add(secondary);
		}

		if (!barTypes.contains(tertiary)
				&& tertiary != BarType.NONE)
		{
			barTypes.add(tertiary);
		}

		if (!barTypes.contains(quaternary)
				&& quaternary != BarType.NONE)
		{
			barTypes.add(quaternary);
		}

		return barTypes;
	}
}
