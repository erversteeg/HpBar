package com.ericversteeg;

import com.ericversteeg.config.BarInfo;
import com.ericversteeg.config.BarType;
import com.ericversteeg.view.*;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;
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

	void updateViews() {
		clearViewInfo();

		Map<BarType, BarInfo> barInfo = barInfo();

		int width = Math.min(800, Math.max(config.width(), 40));
		int height = Math.min(150, Math.max(config.height(), 20));

		setFonts(height);

		boolean hasSecondaryText = config.hasSecondaryText();

		RSColumn column = new RSColumn(0,0, width, RSView.WRAP_CONTENT);

		List<BarType> types = types();
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
			}
		}

//		RSBox attackBarContainer = new RSBox(0, 0, RSView.MATCH_PARENT, height / 4);
//
//		RSBar attackBar = new RSBar(RSView.MATCH_PARENT, RSView.MATCH_PARENT, 100);
//		attackBar.setHue(180);
//		attackBar.setValue(client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT) / 10);
//
//		RSTextView attackBarText = new RSTextView(0, 0, RSView.WRAP_CONTENT, RSView.WRAP_CONTENT, font);
//		attackBarText.setText(String.valueOf(attackBar.getValue()));
//		attackBarText.setMarginStart(1);
//		attackBarText.setMarginTop(-1);
//
//		attackBarContainer.addView(attackBar);
//
//		if (hasSecondaryText)
//		{
//			attackBarContainer.addView(attackBarText);
//		}
//
//		RSBox runBarContainer = new RSBox(0, 0, RSView.MATCH_PARENT, height / 4);
//
//		RSBar runBar = new RSBar(RSView.MATCH_PARENT, RSView.MATCH_PARENT, 100);
//		runBar.setHue(50);
//		runBar.setValue(Math.min(client.getEnergy() / 100, 100));
//
//		RSTextView runBarText = new RSTextView(0, 0, RSView.WRAP_CONTENT, RSView.WRAP_CONTENT, font);
//		runBarText.setText(String.valueOf(runBar.getValue()));
//		runBarText.setMarginStart(1);
//		runBarText.setMarginTop(-1);
//
//		runBarContainer.addView(runBar);
//
//		if (hasSecondaryText)
//		{
//			runBarContainer.addView(runBarText);
//		}
//
//		RSBox prayerBarContainer = new RSBox(0, 0, RSView.MATCH_PARENT, height / 4);
//
//		RSBar prayerBar = new RSBar(RSView.MATCH_PARENT, RSView.MATCH_PARENT, client.getRealSkillLevel(Skill.PRAYER));
//		prayerBar.setHue(155);
//		prayerBar.setValue(client.getBoostedSkillLevel(Skill.PRAYER));
//
//		RSTextView prayerBarText = new RSTextView(0, 0, RSView.WRAP_CONTENT, RSView.WRAP_CONTENT, font);
//		prayerBarText.setText(String.valueOf(prayerBar.getValue()));
//		prayerBarText.setMarginStart(1);
//		prayerBarText.setMarginTop(-1);
//
//		prayerBarContainer.addView(prayerBar);
//
//		if (hasSecondaryText)
//		{
//			prayerBarContainer.addView(prayerBarText);
//		}
//
//		RSBox hpBarContainer = new RSBox(0, 0, RSView.MATCH_PARENT, height);
//
//		RSBar hpBar = new RSBar(RSView.MATCH_PARENT, height, client.getRealSkillLevel(Skill.HITPOINTS));
//		RSTextView hpText = new RSTextView(0, 0, RSView.WRAP_CONTENT, RSView.WRAP_CONTENT, FontManager.getRunescapeBoldFont());
//
//		hpBar.setValue(client.getBoostedSkillLevel(Skill.HITPOINTS));
//		hpBar.setHue(0f);
//
//		hpText.setText(String.format("%d", hpBar.getValue()));
//		hpText.setLayoutGravity(RSViewGroup.Gravity.CENTER);
//
//		hpBarContainer.addView(hpBar);
//		hpBarContainer.addView(hpText);
//
//		column.addView(runBarContainer);
//		column.addView(attackBarContainer);
//		column.addView(prayerBarContainer);
//		column.addView(hpBarContainer);

		column.setRenderReverse(true);

		addViewInfo(new ViewInfo(client, column, config.anchorType(), config.anchorX(), config.anchorY()));
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

	@Override
	public Dimension render(Graphics2D graphics)
	{
		updateViews();
		return super.render(graphics);
	}

	private Map<BarType, BarInfo> barInfo()
	{
		BarInfo hitpoints = new BarInfo(
				client.getBoostedSkillLevel(Skill.HITPOINTS),
				client.getRealSkillLevel(Skill.HITPOINTS),
				0
		);

		BarInfo prayer = new BarInfo(
				client.getBoostedSkillLevel(Skill.PRAYER),
				client.getRealSkillLevel(Skill.PRAYER),
				155
		);

		BarInfo attack = new BarInfo(
				client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT) / 10,
				100,
				180
		);

		BarInfo run = new BarInfo(
				client.getEnergy() / 100,
				100,
				50
		);

		Map<BarType, BarInfo> barInfo = new HashMap<>();

		barInfo.put(BarType.HITPOINTS, hitpoints);
		barInfo.put(BarType.PRAYER, prayer);
		barInfo.put(BarType.SPECIAL_ATTACK, attack);
		barInfo.put(BarType.RUN_ENERGY, run);

		return barInfo;
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
