package net.spellcraftgaming.rpghud.gui.hud.element.texture;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.tags.FluidTags;
import net.spellcraftgaming.rpghud.gui.hud.element.HudElement;
import net.spellcraftgaming.rpghud.gui.hud.element.HudElementType;
import net.spellcraftgaming.rpghud.settings.Settings;

public class HudElementAirTexture extends HudElement {

	public HudElementAirTexture() {
		super(HudElementType.AIR, 0, 0, 0, 0, true);
	}

	@Override
	public boolean checkConditions() {
		return (this.mc.player.areEyesInFluid(FluidTags.WATER) || this.mc.player.getAir() < this.mc.player.getMaxAir()) && this.mc.playerController.shouldDrawHUD();
	}

	@Override
	public void drawElement(AbstractGui gui, MatrixStack ms, float zLevel, float partialTicks, int scaledWidth, int scaledHeight) {
		bind(INTERFACE);
		RenderSystem.color3f(1f, 1f, 1f);
		int height = scaledHeight + this.settings.getPositionValue(Settings.air_position)[1];
		int adjustedWidth = (scaledWidth / 2) + this.settings.getPositionValue(Settings.air_position)[0];
		int airAmount = this.mc.player.getAir();
		double maxAir = this.mc.player.getMaxAir();
		gui.blit(ms, adjustedWidth - 70, height - 80, 0, 160, 141, 10);
		gui.blit(ms, adjustedWidth - 70, height - 80, 0, 140, (int) (141.0D * (airAmount / maxAir)), 10);
		RenderSystem.color3f(1f, 1f, 1f);
		this.mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
	}

}
