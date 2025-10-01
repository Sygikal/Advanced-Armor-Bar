package dev.sygii.advancedarmorbar.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sygii.advancedarmorbar.AdvancedArmorBarMain;
import dev.sygii.ultralib.data.loader.SimpleDataLoader;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import java.util.*;
import java.util.List;

public class ArmorIconLoader extends SimpleDataLoader {
    public static final Identifier ID = AdvancedArmorBarMain.of("server_armor_icon_loader");

    public ArmorIconLoader() {
        super(ID, "armor_icon");
    }

    public void preReload() {
        AdvancedArmorBarMain.armorIcons.clear();
    }

    public void reloadResource(JsonObject data, Identifier entryId, String fileName) {
        Identifier texture = Identifier.tryParse(data.get("texture").getAsString());

        List<Item> items = new ArrayList<>();
        for (JsonElement elem : data.getAsJsonArray("items")) {
            Identifier itemId = Identifier.tryParse(elem.getAsString());
            items.add(Registries.ITEM.get(itemId));
            //System.out.println(Registries.ITEM.get(itemId));
        }

        AdvancedArmorBarMain.armorIcons.add(new ArmorIcon(entryId, texture, items));
    }
}

