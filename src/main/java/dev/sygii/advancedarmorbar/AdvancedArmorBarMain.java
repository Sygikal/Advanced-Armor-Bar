package dev.sygii.advancedarmorbar;

import dev.sygii.advancedarmorbar.data.ArmorIcon;
import dev.sygii.advancedarmorbar.data.ArmorIconLoader;
import dev.sygii.advancedarmorbar.elements.AdvancedArmorBar;
import dev.sygii.hotbarapi.HotbarAPI;
import dev.sygii.hotbarapi.data.server.HotbarHighlightLoader;
import dev.sygii.hotbarapi.data.server.ServerStatusBarLoader;
import dev.sygii.hotbarapi.data.server.ServerStatusBarOverlayLoader;
import dev.sygii.hotbarapi.elements.StatusBar;
import dev.sygii.hotbarapi.elements.StatusBarOverlay;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

//? if >=1.21.9 {
/*import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
*///?}

public class AdvancedArmorBarMain implements ModInitializer {
	public static final String MOD_ID = "advancedarmorbar";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final List<ArmorIcon> armorIcons = new ArrayList<>();

	@Override
	public void onInitialize() {
		//? if >=1.21.9 {
		/*ResourceLoader.get(ResourceType.CLIENT_RESOURCES).registerReloader(ArmorIconLoader.ID, new ArmorIconLoader());
		*///?} else {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ArmorIconLoader());
		//?}

		HotbarAPI.registerStatusBarRenderer(new AdvancedArmorBar.AdvancedArmorBarRenderer());
		HotbarAPI.registerStatusBarLogic(new AdvancedArmorBar.AdvancedArmorBarLogic());

		Identifier targetId = Identifier.of("minecraft", "armor");
		HotbarAPI.registerStatusBarOverlay(targetId, new StatusBarOverlay(of("advanced_armor_bar"), targetId, sprite("armor_empty"), HotbarAPI.getRenderer(AdvancedArmorBar.AdvancedArmorBarRenderer.ID), HotbarAPI.getLogic(AdvancedArmorBar.AdvancedArmorBarLogic.ID), false));
	}

	public static Identifier of(String text) {
		return Identifier.of(MOD_ID, text);
	}

	public static Identifier sprite(String text) {
		return Identifier.of(MOD_ID, "textures/gui/"+text+".png");
	}
}
