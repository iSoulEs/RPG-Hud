package net.spellcraftgaming.rpghud.gui.hud.element.simple;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.LivingEntity;
import net.spellcraftgaming.rpghud.gui.hud.element.HudElement;
import net.spellcraftgaming.rpghud.gui.hud.element.HudElementType;
import net.spellcraftgaming.rpghud.settings.Settings;

public class HudElementHealthMountSimple extends HudElement {

	public HudElementHealthMountSimple() {
		super(HudElementType.HEALTH_MOUNT, 0, 0, 0, 0, true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean checkConditions() {
		return this.mc.player.getRidingEntity() instanceof LivingEntity && this.mc.playerController.shouldDrawHUD();
	}
	
	@Override
	public void drawElement(AbstractGui gui, MatrixStack ms, float zLevel, float partialTicks, int scaledWidth, int scaledHeight) {
		LivingEntity mount = (LivingEntity) this.mc.player.getRidingEntity();
		int health = (int) Math.ceil(mount.getHealth());
		int healthMax = (int) Math.ceil(mount.getMaxHealth());
		if(health > healthMax) health = healthMax;
		
		int width = 84;
		
		int posX = ((scaledWidth) / 2) + 7 + this.settings.getPositionValue(Settings.mount_health_position)[0];
		int posY = scaledHeight - 32 - 17 + this.settings.getPositionValue(Settings.mount_health_position)[1];
		
		drawCustomBar(posX, posY, width, 8, (double) health / (double) healthMax * 100.0D, 0xA0000000, 0xA0000000, this.settings.getIntValue(Settings.color_health), offsetColorPercent(this.settings.getIntValue(Settings.color_health), OFFSET_PERCENT), 0xA0000000);
		
		String stringHealth = this.settings.getBoolValue(Settings.mount_health_percentage) ? (int) Math.floor((double) health / (double) healthMax * 100) + "%" : health + "/" + healthMax;

		if (this.settings.getBoolValue(Settings.show_numbers_health)) {
			ms.scale(0.5f, 0.5f, 0.5f);
			AbstractGui.drawCenteredString(ms,this.mc.fontRenderer, stringHealth, posX * 2 + 88, posY * 2 + 4, -1);
			ms.scale(2.0f, 2.0f, 2.0f);
		}
	}
}
