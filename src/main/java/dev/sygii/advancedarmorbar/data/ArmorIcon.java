package dev.sygii.advancedarmorbar.data;

import dev.sygii.hotbarapi.elements.StatusBarLogic;
import dev.sygii.hotbarapi.elements.StatusBarOverlay;
import dev.sygii.hotbarapi.elements.StatusBarRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ArmorIcon {
    private final Identifier id;
    private final Identifier texture;
    private final List<Item> items;

    public ArmorIcon(Identifier id, Identifier texture, List<Item> items) {
        this.id = id;
        this.texture = texture;
        this.items = items;
    }

    public Identifier getId() {
        return id;
    }

    public Identifier getTexture() {
        return texture;
    }

    public List<Item> getItems() {
        return items;
    }
}
