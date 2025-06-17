package dev.sygii.advancedarmorbar;

import dev.sygii.advancedarmorbar.data.ArmorIcon;
import dev.sygii.advancedarmorbar.data.ArmorIconLoader;
import dev.sygii.advancedarmorbar.elements.AdvancedArmorBar;
import dev.sygii.hotbarapi.HotbarAPI;
import dev.sygii.hotbarapi.data.server.ServerStatusBarLoader;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AdvancedArmorBarMain implements ModInitializer {
	public static final String MOD_ID = "advancedarmorbar";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final List<ArmorIcon> armorIcons = new ArrayList<>();

	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ArmorIconLoader());

		HotbarAPI.registerStatusBarRenderer(new AdvancedArmorBar.AdvancedArmorBarRenderer());
		HotbarAPI.registerStatusBarLogic(new AdvancedArmorBar.AdvancedArmorBarLogic());
	}

	public static Identifier of(String text) {
		return Identifier.of(MOD_ID, text);
	}

	public static Identifier sprite(String text) {
		return Identifier.of(MOD_ID, "textures/gui/"+text+".png");
	}
}
