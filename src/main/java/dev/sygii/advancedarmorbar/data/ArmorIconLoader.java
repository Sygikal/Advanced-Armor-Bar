package dev.sygii.advancedarmorbar.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.sygii.advancedarmorbar.AdvancedArmorBarMain;
import dev.sygii.hotbarapi.HotbarAPI;
import dev.sygii.hotbarapi.elements.StatusBarRenderer;
import dev.sygii.hotbarapi.network.StatusBarS2CPacket;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;

public class ArmorIconLoader implements SimpleSynchronousResourceReloadListener {

    @Override
    public Identifier getFabricId() {
        return HotbarAPI.identifierOf("server_armor_icon_loader");
    }

    @Override
    public void reload(ResourceManager manager) {
        AdvancedArmorBarMain.armorIcons.clear();
        manager.findResources("armor_icon", id -> id.getPath().endsWith(".json")).forEach((id, resourceRef) -> {
            try {
                InputStream stream = null;
                stream = resourceRef.getInputStream();
                JsonObject data = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();
                String fileName = getBaseName(id.getPath());

                Identifier armorIconId = Identifier.of(id.getNamespace(), fileName);
                Identifier texture = Identifier.tryParse(data.get("texture").getAsString());

                List<Item> items = new ArrayList<>();
                for (JsonElement elem : data.getAsJsonArray("items")) {
                    Identifier itemId = Identifier.tryParse(elem.getAsString());
                    items.add(Registries.ITEM.get(itemId));
                    //System.out.println(Registries.ITEM.get(itemId));
                }

                AdvancedArmorBarMain.armorIcons.add(new ArmorIcon(armorIconId, texture, items));
                //HotbarAPI.statusBarPacketQueue.add(new StatusBarS2CPacket(barId, beforeIds, afterIds, replaceId, texture, direction, position, logicId, rendererId, gameModes));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static String getBaseName(String filename) {
        if (filename == null)
            return null;

        String name = new File(filename).getName();
        int extPos = name.lastIndexOf('.');

        if (extPos < 0)
            return name;

        return name.substring(0, extPos);
    }
}

