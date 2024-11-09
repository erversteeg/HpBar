package com.ericversteeg.frosthprun;

import com.ericversteeg.frosthprun.config.BarInfo;
import com.ericversteeg.frosthprun.config.BarStyle;
import com.ericversteeg.frosthprun.config.TextBrightness;
import com.ericversteeg.frosthprun.config.BarType;
import com.ericversteeg.frosthprun.view.*;
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

public class FrostHpRunOverlay extends RSViewOverlay {

	private final Client client;
	private final ItemManager itemManager;
	private final FrostHpRunPlugin plugin;
	private final FrostHpRunConfig config;

	private Font primaryFont;
	private Font secondaryFont;
	private Font smallFont;

	List<BarType> types;

	private RSColumn hpColumn;
	private RSColumn runColumn;

	private RSBar primaryBar;
	private RSTextView primaryTextView;

	private RSBar secondaryBar;
	private RSTextView secondaryTextView;

	private RSBar tertiaryBar;
	private RSTextView tertiaryTextView;

	private RSBar quaternaryBar;
	private RSTextView quaternaryTextView;

	private RSBar runBar;
	private RSTextView runTextView;

	private String hpViewName = "hp_view";
	private String runViewName = "run_view";

	@Inject
	private FrostHpRunOverlay(
			Client client,
			ItemManager itemManager,
			FrostHpRunPlugin plugin,
			FrostHpRunConfig config)
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

	void setupHpBar(boolean animate)
	{
		clearHpViewInfo();

		Map<BarType, BarInfo> barInfo = plugin.barInfo();

		int width = Math.min(800, Math.max(config.width(), 40));
		int height = Math.min(150, Math.max(config.height(), 20));

		setFonts(height);

		boolean hasSecondaryText = config.hasSecondaryText();

		hpColumn = new RSColumn(0,0, width, RSView.WRAP_CONTENT);

		types = types();

		Collections.reverse(types);

		for (int i = 0; i < types.size(); i++)
		{
			BarInfo info = barInfo.get(types.get(i));

			if (i < types.size() - 1)
			{
				RSBox container = new RSBox(0, 0, RSView.MATCH_PARENT, height / 3);
				if (i == types.size() - 2 && config.isLargeSecondary())
				{
					container = new RSBox(0, 0, RSView.MATCH_PARENT, height / 2);
				}

                RSBar bar = new RSBar(RSView.MATCH_PARENT, RSView.MATCH_PARENT, info.maxValue, config.barStyle(), i, types.size());
				bar.setHue(info.hue);
				bar.setValue(info.value);

				RSTextView text = new RSTextView(0, 0, RSView.WRAP_CONTENT, RSView.WRAP_CONTENT, smallFont);

				if (i == types.size() - 2 && config.isLargeSecondary())
				{
					text = new RSTextView(0, 0, RSView.WRAP_CONTENT, RSView.WRAP_CONTENT, secondaryFont);

					switch (config.secondaryFontAlignment()) {
						case LEFT:
							text.setLayoutGravity(RSViewGroup.Gravity.START);
							break;
						case CENTER:
							text.setLayoutGravity(RSViewGroup.Gravity.CENTER);
							break;
						case RIGHT:
							text.setLayoutGravity(RSViewGroup.Gravity.END);
							break;
					}

					int configOffsetX = config.secondaryTextOffsetX();
					if (config.isNegativeSecondaryTextOffsetX())
					{
						configOffsetX = -configOffsetX;
					}

					text.setOffsetX(configOffsetX);

					int configOffsetY = config.secondaryTextOffsetY();
					if (config.isNegativeSecondaryTextOffsetY())
					{
						configOffsetY = -configOffsetY;
					}

					text.setOffsetY(-height / 4 + configOffsetY);

					Color textColor = config.secondaryTextColor().getColor();
					if (config.barTextStyle() == TextBrightness.DARKENED)
					{
						textColor = darkenColor(textColor);
					}
					else if (config.barTextStyle() == TextBrightness.NORMAL)
					{
						textColor = slightlyDarkenColor(textColor);
					}
					text.setTextColor(textColor);
				}
				else {
					text.setMarginStart(1);
					//text.setMarginTop(-1);

					int configOffsetX = config.smallTextOffsetX();
					if (config.isNegativeSmallTextOffsetX())
					{
						configOffsetX = -configOffsetX;
					}

					text.setOffsetX(configOffsetX);

					int configOffsetY = config.smallTextOffsetY();
					if (config.isNegativeSmallTextOffsetY())
					{
						configOffsetY = -configOffsetY;
					}

					text.setOffsetY(-height / 3 + configOffsetY + 4);

					switch (config.smallFontAlignment()) {
						case LEFT:
							text.setLayoutGravity(RSViewGroup.Gravity.START);
							break;
						case CENTER:
							text.setLayoutGravity(RSViewGroup.Gravity.CENTER);
							break;
						case RIGHT:
							text.setLayoutGravity(RSViewGroup.Gravity.END);
							break;
					}

					Color textColor = config.smallTextColor().getColor();
					if (config.barTextStyle() == TextBrightness.DARKENED)
					{
						textColor = darkenColor(textColor);
					}
					else if (config.barTextStyle() == TextBrightness.NORMAL)
					{
						textColor = slightlyDarkenColor(textColor);
					}
					text.setTextColor(textColor);
				}

				text.setText(String.valueOf(bar.getValue()));

				container.addView(bar);

				if (hasSecondaryText)
				{
					container.addView(text);
				}

				hpColumn.addView(container);

				if (i == types.size() - 2)
				{
					secondaryBar = bar;
					secondaryTextView = text;
				}
				else if (i == types.size() - 3)
				{
					tertiaryBar = bar;
					tertiaryTextView = text;
				}
				else if (i == types.size() - 4)
				{
					quaternaryBar = bar;
					quaternaryTextView = text;
				}
			}
			else
			{
				RSBox container = new RSBox(0, 0, RSView.MATCH_PARENT, height);

				RSBar bar = new RSBar(RSView.MATCH_PARENT, height, info.maxValue, config.barStyle(), i, types.size());
				RSTextView text = new RSTextView(0, 0, RSView.WRAP_CONTENT,
						RSView.WRAP_CONTENT, primaryFont);

				bar.setValue(info.value);
				bar.setHue(info.hue);

				text.setText(String.format("%d", bar.getValue()));
				text.setLayoutGravity(RSViewGroup.Gravity.CENTER);

				int configOffsetX = config.primaryTextOffsetX();
				if (config.isNegativePrimaryTextOffsetX())
				{
					configOffsetX = -configOffsetX;
				}

				text.setOffsetX(configOffsetX);

				int configOffsetY = config.primaryTextOffsetY();
				if (config.isNegativePrimaryTextOffsetY())
				{
					configOffsetY = -configOffsetY;
				}

				int textOffsetY = -height / 4 + configOffsetY;
				if (config.barStyle() != BarStyle.ROUND)
				{
					textOffsetY += height / 16;
				}
				text.setOffsetY(textOffsetY);

				switch (config.primaryFontAlignment()) {
					case LEFT:
						text.setLayoutGravity(RSViewGroup.Gravity.START);
						break;
					case CENTER:
						text.setLayoutGravity(RSViewGroup.Gravity.CENTER);
						break;
					case RIGHT:
						text.setLayoutGravity(RSViewGroup.Gravity.END);
						break;
				}

				Color textColor = config.primaryTextColor().getColor();
				if (config.barTextStyle() == TextBrightness.DARKENED)
				{
					textColor = darkenColor(textColor);
				}
				else if (config.barTextStyle() == TextBrightness.NORMAL)
				{
					textColor = slightlyDarkenColor(textColor);
				}
				text.setTextColor(textColor);

				container.addView(bar);
				if (config.hasPrimaryText())
				{
					container.addView(text);
				}

				hpColumn.addView(container);

				primaryBar = bar;
				primaryTextView = text;
			}
		}

		hpColumn.setRenderReverse(false);

		hpColumn.setOpacity(Math.max(20, Math.min(100,
				config.barOpacity())) / 100f);

		if (animate)
		{
			hpColumn.animate()
					.duration(0.1f)
					.fadeIn()
					.start();
		}

		ViewInfo hpViewInfo = new ViewInfo(client, hpColumn, config.anchorType(), config.anchorX(), config.anchorY());

		addViewInfo(hpViewName, hpViewInfo);
	}

	private void updateHpBar()
	{
		Map<BarType, BarInfo> barInfo = plugin.barInfo();

		for (int i = 0; i < types.size(); i++)
		{
			BarInfo info = barInfo.get(types.get(i));

			if (i < types.size() - 1)
			{
				if (i == types.size() - 2)
				{
					int value = info.value;
					if (config.extendBars())
					{
						secondaryBar.setValue(value);
					}
					else
					{
						secondaryBar.setValue(Math.min(info.maxValue, value));
					}
					secondaryBar.setHue(info.hue);
					secondaryTextView.setText(String.valueOf(value));
				}
				else if (i == types.size() - 3)
				{
					int value = info.value;
					if (config.extendBars())
					{
						tertiaryBar.setValue(value);
					}
					else
					{
						tertiaryBar.setValue(Math.min(info.maxValue, value));
					}
					tertiaryBar.setHue(info.hue);
					tertiaryTextView.setText(String.valueOf(value));
				}
				else if (i == types.size() - 4)
				{
					int value = info.value;
					if (config.extendBars())
					{
						quaternaryBar.setValue(value);
					}
					else
					{
						quaternaryBar.setValue(Math.min(info.maxValue, value));
					}
					quaternaryBar.setHue(info.hue);
					quaternaryTextView.setText(String.valueOf(value));
				}
			}
			else
			{
				int value = info.value;
				if (config.extendBars())
				{
					primaryBar.setValue(value);
				}
				else
				{
					primaryBar.setValue(Math.min(info.maxValue, value));
				}
				primaryBar.setHue(info.hue);
				primaryTextView.setText(String.valueOf(value));
			}
		}
	}

	void setupRunBar(boolean animate)
	{
		clearRunViewInfo();

		Map<BarType, BarInfo> barInfo = plugin.barInfo();

		int width = Math.min(800, Math.max(config.runWidth(), 40));
		int height = Math.min(150, Math.max(config.runHeight(), 20));

		setFonts(height);

		runColumn = new RSColumn(0,0, width, RSView.WRAP_CONTENT);

		BarInfo info = barInfo.get(BarType.RUN_ENERGY);

		RSBox container = new RSBox(0, 0, RSView.MATCH_PARENT, height);

		RSBar bar = new RSBar(RSView.MATCH_PARENT, height, info.maxValue, config.barStyle(), 0, 1);
		RSTextView text = new RSTextView(0, 0, RSView.WRAP_CONTENT,
				RSView.WRAP_CONTENT, primaryFont);

		bar.setValue(info.value);
		bar.setHue(info.hue);

		text.setText(String.format("%d", bar.getValue()));
		text.setLayoutGravity(RSViewGroup.Gravity.CENTER);

		int configOffsetX = config.primaryTextOffsetX();
		if (config.isNegativePrimaryTextOffsetX())
		{
			configOffsetX = -configOffsetX;
		}

		int runOffsetX = config.runBarTextOffsetX();
		if (config.isNegativeRunTextOffsetX())
		{
			runOffsetX = -runOffsetX;
		}

		configOffsetX += runOffsetX;

		text.setOffsetX(configOffsetX);

		int configOffsetY = config.primaryTextOffsetY();
		if (config.isNegativePrimaryTextOffsetY())
		{
			configOffsetY = -configOffsetY;
		}

		int runOffsetY = config.runBarTextOffsetY();
		if (config.isNegativeRunTextOffsetY())
		{
			runOffsetY = -runOffsetY;
		}

		configOffsetY += runOffsetY;

		text.setOffsetY(-height / 4 + configOffsetY + 1);

		switch (config.primaryFontAlignment()) {
			case LEFT:
				text.setLayoutGravity(RSViewGroup.Gravity.START);
				break;
			case CENTER:
				text.setLayoutGravity(RSViewGroup.Gravity.CENTER);
				break;
			case RIGHT:
				text.setLayoutGravity(RSViewGroup.Gravity.END);
				break;
		}

		Color textColor = config.runTextColor().getColor();
		if (config.barTextStyle() == TextBrightness.DARKENED)
		{
			textColor = darkenColor(textColor);
		}
		else if (config.barTextStyle() == TextBrightness.NORMAL)
		{
			textColor = slightlyDarkenColor(textColor);
		}
		text.setTextColor(textColor);

		container.addView(bar);

		if (config.hasRunPrimaryText())
		{
			container.addView(text);
		}

		runColumn.addView(container);

		runBar = bar;
		runTextView = text;

		runColumn.setOpacity(Math.max(20, Math.min(100,
				config.runBarOpacity())) / 100f);

		if (animate)
		{
			runColumn.animate()
					.duration(0.1f)
					.fadeIn()
					.start();
		}

		ViewInfo runViewInfo = new ViewInfo(client, runColumn, config.runAnchorType(),
				config.runAnchorX(), config.runAnchorY());

		addViewInfo(runViewName, runViewInfo);
	}

	private void updateRunBar()
	{
		Map<BarType, BarInfo> barInfo = plugin.barInfo();
		BarInfo info = barInfo.get(BarType.RUN_ENERGY);

		int value = info.value;
		runBar.setValue(Math.min(info.maxValue, value));
		runBar.setHue(info.hue);
		runTextView.setText(String.valueOf(value));
	}

	void setFonts(int height)
	{
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			InputStream inputStream = FrostHpRunOverlay.class.getResourceAsStream(
					"NotoSansJP-" + config.primaryFontWeight().getName()
							.replace(" ", "") + ".ttf"
			);
			Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream)
					.deriveFont(Font.PLAIN,  (int) Math.ceil((double) height * 0.55f) * (config.primaryFontScaleFactor() / 100f));
			ge.registerFont(font);
			primaryFont = font;
		}
		catch (Exception e)
		{
			primaryFont = FontManager.getRunescapeBoldFont();
		}

		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			InputStream inputStream = FrostHpRunOverlay.class.getResourceAsStream(
					"NotoSansJP-" + config.secondaryFontWeight().getName()
							.replace(" ", "") + ".ttf"
			);
			Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream)
					.deriveFont(Font.PLAIN,  (int) Math.ceil((double) height / 2) * (config.secondaryFontScaleFactor() / 100f));
			ge.registerFont(font);
			secondaryFont = font;
		}
		catch (Exception e)
		{
			secondaryFont = FontManager.getRunescapeSmallFont();
		}

		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			InputStream inputStream = FrostHpRunOverlay.class.getResourceAsStream(
					"NotoSansJP-" + config.smallFontWeight().getName()
							.replace(" ", "") + ".ttf"
			);
			Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream)
					.deriveFont(Font.PLAIN,  (int) Math.ceil((double) height / 3) * (config.smallFontScaleFactor() / 100f));
			ge.registerFont(font);
			smallFont = font;
		}
		catch (Exception e)
		{
			smallFont = FontManager.getRunescapeSmallFont();
		}
	}

	private boolean isFadeOut = false;
	private boolean isFadeOutRun = false;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.isSmartHide() && config.showHpBar() && !containsViewInfo(hpViewName))
		{
			//System.out.println("Always visible, view info not there, setup hp bar, fade in false");
			setupHpBar(false);
		}
		else if (!config.isSmartHide() && config.showHpBar())
		{
			//System.out.println("Always visible, update hp bar");
			updateHpBar();
		}
		else if (config.showHpBar())
		{
			if (plugin.isActive() && !containsViewInfo(hpViewName))
			{
				//System.out.println("Sometimes visible, view info not there, setup hp bar, fade in true");
				setupHpBar(true);
			}
			else if (plugin.isActive())
			{
				//System.out.println("Sometimes visible, update hp bar");
				updateHpBar();
			}
			else if (containsViewInfo(hpViewName))
			{
				//System.out.println("Hp is fade out is " + isFadeOut);
				if (!isFadeOut)
				{
					//System.out.println("Fade out hp bar start");
					hpColumn.animate()
							.fadeOut()
							.duration(0.10f)
							.onComplete(() -> {
								removeViewInfo(hpViewName, false);
								isFadeOut = false;
								//System.out.println("fade out hp bar done");
							})
							.start();

					isFadeOut = true;
				}
			}
		}

		if (!config.isRunSmartHide() && config.showRunBar() && !containsViewInfo(runViewName))
		{
			//System.out.println("Always visible, view info not there, setup run bar, fade in false");
			setupRunBar(false);
		}
		else if (!config.isRunSmartHide() && config.showRunBar())
		{
			//System.out.println("Always visible, update run bar");
			updateRunBar();
		}
		else if (config.showRunBar())
		{
			if (plugin.isRun() && !containsViewInfo(runViewName))
			{
				//System.out.println("Sometimes visible, view info not there, setup run bar, fade in true");
				setupRunBar(true);
			}
			else if (plugin.isRun())
			{
				//System.out.println("Sometimes visible, update run bar");
				updateRunBar();
			}
			else if (containsViewInfo(runViewName))
			{
				//System.out.println("Run is fade out is " + isFadeOut);
				if (!isFadeOutRun)
				{
					//System.out.println("Fade out run bar start");
					runColumn.animate()
							.fadeOut()
							.duration(0.10f)
							.onComplete(() -> {
								removeViewInfo(runViewName, false);
								isFadeOutRun = false;
								//System.out.println("fade out run bar done");
							})
							.start();

					isFadeOutRun = true;
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

	boolean hasBarType(BarType barType) {
		for (BarType type: types()) {
			if (barType == type) {
				return true;
			}
		}
		return false;
	}

	public void clearHpViewInfo()
	{
		removeViewInfo(hpViewName, true);
		isFadeOut = false;
	}

	public void clearRunViewInfo()
	{
		removeViewInfo(runViewName, true);
		isFadeOutRun = false;
	}

	private Color slightlyDarkenColor(Color color)
	{
		float [] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		return Color.getHSBColor(hsb[0], hsb[1], hsb[2] * 0.8f);
	}

	private Color darkenColor(Color color)
	{
		float [] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		return Color.getHSBColor(hsb[0], hsb[1], hsb[2] * 0.6f);
	}
}
