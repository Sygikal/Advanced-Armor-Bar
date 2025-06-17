package dev.sygii.advancedarmorbar.data;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.List;

public class ArmorSegment {
    private final ArmorIcon icon;
    private final int points;
    private final int dyeColor;
    private final boolean enchanted;

    public ArmorSegment(ArmorIcon icon, int points, int dyeColor, boolean enchanted) {
        this.icon = icon;
        this.points = points;
        this.dyeColor = dyeColor;
        this.enchanted = enchanted;
    }

    public ArmorIcon getIcon() {
        return this.icon;
    }

    public int getPoints() {
        return points;
    }

    public int getDyeColor() {
        return this.dyeColor;
    }

    public boolean isEnchanted() {
        return this.enchanted;
    }
}
