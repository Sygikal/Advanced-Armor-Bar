package dev.sygii.advancedarmorbar.elements;

import com.google.common.collect.Multimap;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.sygii.advancedarmorbar.AdvancedArmorBarMain;
import dev.sygii.advancedarmorbar.EntityAttributeInstanceAccessor;
import dev.sygii.advancedarmorbar.data.ArmorIcon;
import dev.sygii.advancedarmorbar.data.ArmorSegment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import dev.sygii.hotbarapi.elements.StatusBarRenderer;
import dev.sygii.hotbarapi.elements.StatusBarLogic;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

import java.util.*;

public class AdvancedArmorBar {
    public static class AdvancedArmorBarRenderer extends StatusBarRenderer {
        private static final Identifier TOUGHESS_UNDERLAY = AdvancedArmorBarMain.sprite("toughness_underlay");
        private static final Identifier EMPTY = AdvancedArmorBarMain.sprite("armor_empty");
        private static final Identifier ID = AdvancedArmorBarMain.of("advanced_armor_renderer");

        public AdvancedArmorBarRenderer() {
            super(ID, TOUGHESS_UNDERLAY, StatusBarRenderer.Position.LEFT, StatusBarRenderer.Direction.L2R);
        }

        @Override
        public void render(MinecraftClient client, DrawContext context, PlayerEntity playerEntity, int x, int y, StatusBarLogic logic) {
            float toughness = (float) playerEntity.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
            float armor = (float) playerEntity.getAttributeValue(EntityAttributes.GENERIC_ARMOR);

            float maxArmor = (float) (Math.ceil(armor/20.0) * 20);
            float maxTough = (float) (Math.ceil(toughness/20.0) * 20);

            int realArmor = MathHelper.ceil(armor);
            int realTough = MathHelper.ceil(toughness);

            float combinedMax = Math.max(maxArmor, maxTough);
            float realMax = Math.max(realArmor, realTough);
            int scale = 10;

            float f2 = Math.max((float)combinedMax, (float) Math.max((float)MathHelper.ceil(armor),  (float)MathHelper.ceil(toughness)));
            //int p2 = MathHelper.ceil(playerEntity.getAbsorptionAmount());
            int q2 = MathHelper.ceil((f2 /*+ (float)p2*/) / 2.0F / 10.0F);
            int lines = Math.max(10 - (q2 - 2), 3);

            //System.out.println(armor);
            int mmmaxxx = MathHelper.ceil((double)combinedMax / (double)2.0F);

            int sexscale = (int) (combinedMax / 2);
            float apparition = combinedMax / (float)(sexscale);

            LinkedHashMap pointsMap = new LinkedHashMap();
            Map<Identifier, Integer> pointsMap2 = new LinkedHashMap();
            List<ArmorSegment> segments = new ArrayList<>();
            int totalPoints = this.getArmorPoints(playerEntity, (Map<EquipmentSlot, Integer>)pointsMap, segments);
            Collections.sort(segments, new Comparator<ArmorSegment>(){
                public int compare(ArmorSegment o1, ArmorSegment o2){
                    if(o1.getPoints() == o2.getPoints())
                        return 0;
                    return o1.getPoints() < o2.getPoints() ? -1 : 1;
                }
            });
            Collections.reverse(segments);
            //System.out.println(pointsMap);
           /* if (totalPoints <= 0) {
                return;
            }*/

            for(int m = mmmaxxx - 1; m >= 0; --m) {
                int n = m / scale;
                int o = m % scale;
                int p = x + (getDirection().equals(Direction.L2R) ? (getPosition().equals(Position.RIGHT) ? -72 : 0) + o * 8 : (getPosition().equals(Position.LEFT) ? 72 : 0) + -(o * 8));
                int q = y - n * lines;


                float prevasd = (float)m * apparition;
                float asd = (float)(m + 1) * apparition;
                float sex = asd - apparition / 2.0F;

                if (realTough > sex) {
                    boolean bl = realTough == asd;
                    boolean bl2 = (m+1) % 10 == 0;
                    context.drawTexture(TOUGHESS_UNDERLAY, p - 1, q - 1, 0.0F, 0.0F, (bl || bl2) ? 11 : 10, 11, 44, 11);
                }

                if (realTough > prevasd && realTough <= sex) {
                    context.drawTexture(TOUGHESS_UNDERLAY, p - 1, q - 1, 0.0F, 0.0F, 6, 11, 44, 11);
                }

                context.drawTexture(EMPTY, p, q, 0.0F, 0.0F, 9, 9, 9, 9);
            }


            int donePoints = 0;
            int asdd = 0;
            for (ArmorSegment segment : segments) {
                for (int i = segment.getPoints() - 1; i >= 0; --i) {
                    int n = (donePoints + i) / (scale * 2);
                    int o = (donePoints + i)  % (scale * 2);
                    int p = x + (getDirection().equals(Direction.L2R) ? (getPosition().equals(Position.RIGHT) ? -72 : 0) + o * 4 : (getPosition().equals(Position.LEFT) ? 72 : 0) + -(o * 4));
                    int q = y - n * lines;
                    boolean firstHalf = (donePoints + i) % 2 == 0;
                    boolean lastHalf = (donePoints + i) % 2 == 1;
                    if (segment.isEnchanted()) {
                        context.getMatrices().translate(0, 0, 2);
                    }
                    if (firstHalf) {
                        /*context.getMatrices().translate(0, 0, -2);
                        context.drawTexture(EMPTY, p, q,0, 0.0F, 9, 9, 9, 9);
                        context.getMatrices().translate(0, 0, 2);*/
                    }
                    //context.drawTexture(EMPTY, p, q, lastHalf ? 4.0F : 0, 0.0F, lastHalf || firstHalf ? 5 : 9, 9, 9, 9);
                    if (segment.getDyeColor() != -1) {
                        int hex = segment.getDyeColor();
                        RenderSystem.setShaderColor(ColorHelper.Argb.getRed(hex) / 255.0F, ColorHelper.Argb.getGreen(hex) / 255.0F, ColorHelper.Argb.getBlue(hex) / 255.0F, 1.0f);
                    }
                    context.drawTexture(segment.getIcon().getTexture(), p, q, lastHalf ? 4.0F : 0, 0.0F, lastHalf || firstHalf ? 5 : 9, 9, 9, 9);
                    //RenderSystem.setShaderColor(ColorHelper.Argb.getRed(-1) / 255.0F, ColorHelper.Argb.getGreen(-1) / 255.0F, ColorHelper.Argb.getBlue(-1) / 255.0F, ColorHelper.Argb.getAlpha(-1) / 255.0F);
                    if (segment.isEnchanted()) {
                        //this.drawTexturedGlintRect(context, client, );
                        this.drawTexturedGlintRect(context, client, p, q,  lastHalf ? 4 : 0, 0, lastHalf || firstHalf ? 5 : 9, 9);
                        context.getMatrices().translate(0, 0, -2);
                    }
                    RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                }
                donePoints += segment.getPoints();
                asdd++;
            }

            Map<Integer, ArmorMaterial> valueMap = new LinkedHashMap<>();

            /*int sss = 0;
            int totalArmor = 0;
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = playerEntity.getEquippedStack(slot);
                Multimap<EntityAttribute, EntityAttributeModifier> attributeMap = stack.getAttributeModifiers(slot);
                if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem) {
                    ArmorMaterial material = ((ArmorItem) stack.getItem()).getMaterial();
                    ArmorItem.Type type = ((ArmorItem) stack.getItem()).getType();

                    //type.getName()
                    context.drawText(client.inGameHud.getTextRenderer(), Text.literal(""+material.getName() + " | " + material.getProtection(type)), 100,  100 + (sss * 9), -1, true);
                    int prot = material.getProtection(type);
                    totalArmor += prot;
                    for (int i = totalArmor; i>= totalArmor - prot; --i) {
                        valueMap.put(i, material);
                    }
                    /*int i = material.getProtection(type);
                    int j = (int) material.getToughness();
                    s = "§a+" + i;
                    s1 = (j > 0 ? "§b+" : "§b") + j;//

                }
               // double value = ((EntityAttributeModifier)attributeMap.asMap().get(EntityAttributes.GENERIC_ARMOR)).getValue();
                //context.drawText(client.inGameHud.getTextRenderer(), Text.literal(""+(value)), 100, sss * 9, -1, true);
                //Multimap asd = stack.getAttributeModifiers(slot);
                //asd.get(EntityAttributes.GENERIC_ARMOR);
                sss++;
            }*/

            /*int asasd = 0;
            for (Map.Entry<Integer, ArmorMaterial> enty : valueMap.entrySet()) {
                context.drawText(client.inGameHud.getTextRenderer(), Text.literal(""+(enty.getKey() + " | " + enty.getValue())), 200, 100 + (asasd*9), -1, true);
                asasd++;
            }*/

            /*for(int m = mmmaxxx - 1; m >= 0; --m) {
                int n = m / scale;
                int o = m % scale;
                int p = x + (getDirection().equals(Direction.L2R) ? (getPosition().equals(Position.RIGHT) ? -72 : 0) + o * 8 : (getPosition().equals(Position.LEFT) ? 72 : 0) + -(o * 8));
                int pText = x + (getDirection().equals(Direction.L2R) ? (getPosition().equals(Position.RIGHT) ? -72 : 0) + o * 27 : (getPosition().equals(Position.LEFT) ? 72 : 0) + -(o * 27));
                int q = y - n * lines;



                float prevasd = (float)m * apparition;
                float asd = (float)(m + 1) * apparition;
                float sex = asd - apparition / 2.0F;

                if (realTough > sex) {
                    boolean bl = realTough == asd;
                    boolean bl2 = (m+1) % 10 == 0;
                    context.drawTexture(TOUGHESS_UNDERLAY, p - 1, q - 1, 0.0F, 0.0F, (bl || bl2) ? 11 : 10, 11, 44, 11);
                }

                if (realTough > prevasd && realTough <= sex) {
                    context.drawTexture(TOUGHESS_UNDERLAY, p - 1, q - 1, 0.0F, 0.0F, 6, 11, 44, 11);
                }

                context.drawTexture(EMPTY, p, q, 0.0F, 0.0F, 9, 9, 9, 9);
                context.drawText(client.inGameHud.getTextRenderer(), Text.literal(""+(realArmor)), pText, q - 20, -1, true);

                ArmorMaterial se = valueMap.get((m + 1) * 2);
                if (se != null) {
                    if (realArmor > sex) {
                        context.drawTexture(AdvancedArmorBarMain.sprite("materials/" + se.getName().toLowerCase()), p, q, 0.0F, 0.0F, 9, 9, 9, 9);
                    }

                    if (realArmor > prevasd && realArmor <= sex) {
                        context.drawTexture(AdvancedArmorBarMain.sprite("materials/" + se.getName().toLowerCase()), p, q, 0.0F, 0.0F, 5, 9, 9, 9);
                    }
                }
            }*/
        }


        /*@Override
        public void render(MinecraftClient client, DrawContext context, PlayerEntity playerEntity, int x, int y, StatusBarLogic logic) {
            float toughness = (float) playerEntity.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
            float armor = (float) playerEntity.getAttributeValue(EntityAttributes.GENERIC_ARMOR);

            int max =
            int scale = 10;
            float apparition = max / (float)scale;

            //addZOffset(context, 2);
            for(int w = 0; w < scale; ++w) {
                int xPosition = x + (this.getDirection().equals(Direction.L2R) ? (this.getPosition().equals(Position.RIGHT) ? -72 : 0) + w * 8 : (this.getPosition().equals(Position.LEFT) ? 72 : 0) + -(w * 8));
                //context.drawTexture(this.getTexture(), xPosition, y, 0.0F, 0.0F, 9, 9, 27, 9);
                float prevasd = (float)w * apparition;
                float asd = (float)(w + 1) * apparition;
                float sex = asd - apparition / 2.0F;
                //addZOffset(context, -2);
                //drawTexturedGlintRect(context, client,x, y, x+9, y+9, 256, 9);
                //addZOffset(context, 2);s
                /*if (w == 0) {
                    context.drawTexture(this.getTexture(), xPosition -1, y -1, 0.0F, 0.0F, 11, 11, 44, 11);
                }/

                if (current > sex) {
                    context.drawTexture(this.getTexture(), xPosition -1, y -1, 0.0F, 0.0F, 11, 11, 44, 11);
                }

                if (current > prevasd && current <= sex) {
                    context.drawTexture(this.getTexture(), xPosition- 1, y -1, 11.0F, 0.0F, 11, 11, 44, 11);
                }
                //drawTexturedGlintRect(context, client,x, y, x+9, y+9, 256, 256);
                //addZOffset(context, -2);
            }
            //drawTexturedGlintRect(context, client,x, y, x+90, y+9, 256, 256);
            //drawTexturedGlintRect(context, client, x, y, x+90, 0, 0, 9);
            //addZOffset(context, -2);
        }*/

        /*private void drawTexturedGlintRect(DrawContext context, MinecraftClient client, int x, int y, int u, int v, int width, int height) {
            float intensity = client.options.getGlintStrength().getValue().floatValue()
                    * 1.0f;
            int color = getWhite(MathHelper.clamp(intensity, 0, 1));
            //context.drawTexture(id -> RenderLayer.getGlint(), ITEM_ENCHANTMENT_GLINT, x, y, u, v, width, height, TEXTURE_SIZE, TEXTURE_SIZE, color);
            setColor(context, color);
            context.drawTexture(ITEM_ENCHANTMENT_GLINT, x, y, u, v, width, height);
            setColor(context, -1);
        }*/

        public void drawTexturedGlintRect(DrawContext context, MinecraftClient client, int x, int y, int u, int v, int width, int height) {
            RenderSystem.depthFunc((int)514);
            RenderSystem.blendFuncSeparate((int)768, (int)1, (int)1, (int)0);
            float intensity = ((Double)client.options.getGlintStrength().getValue()).floatValue() * 1.5f;
            RenderSystem.setShaderColor((float)intensity, (float)intensity, (float)intensity, (float)1.0f);
            double glintSpeed = (Double)client.options.getGlintSpeed().getValue();
            long time = (long)((double) Util.getMeasuringTimeMs() * glintSpeed * 8.0);
            u = (int)((long)u + (-(time % 110000L) * 256L / 110000L + (long)x));
            v = (int)((long)v + (time % 30000L * 256L / 30000L + (long)y));
            context.drawTexture(ItemRenderer.ITEM_ENCHANTMENT_GLINT, x, y, (float)u, (float)v, width, height, 256, 256);
            RenderSystem.depthFunc((int)515);
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }

        public static void setColor(DrawContext context, int hex) {
            //? if =1.20.1 {
            RenderSystem.setShaderColor(ColorHelper.Argb.getRed(hex) / 255.0F, ColorHelper.Argb.getGreen(hex) / 255.0F, ColorHelper.Argb.getBlue(hex) / 255.0F, ColorHelper.Argb.getAlpha(hex) / 255.0F);
            //?} else {
            /*RenderSystem.setShaderColor(ColorHelper.getRed(hex) / 255.0F, ColorHelper.getGreen(hex) / 255.0F, ColorHelper.getBlue(hex) / 255.0F, ColorHelper.getAlpha(hex) / 255.0F);
             *///?}
            //context.setShaderColor(ColorHelper.Argb.getRed(hex) / 255.0F, ColorHelper.Argb.getGreen(hex) / 255.0F, ColorHelper.Argb.getBlue(hex) / 255.0F, ColorHelper.Argb.getAlpha(hex) / 255.0F);
        }

        private static final DefaultAttributeContainer FALLBACK_ARMOR = DefaultAttributeContainer.builder().add(EntityAttributes.GENERIC_ARMOR).build();
        private static final DefaultAttributeContainer FALLBACK_TOUGHNESS = DefaultAttributeContainer.builder().add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).build();

        private int getArmorPoints(PlayerEntity player, Map<EquipmentSlot, Integer> pointsMap, List<ArmorSegment> segments) {
            AttributeContainer attributes = new AttributeContainer(FALLBACK_ARMOR);
            EntityAttributeInstance armor = attributes.getCustomInstance(EntityAttributes.GENERIC_ARMOR);
            if (armor == null) {
                return 0;
            }
            int displayedArmorCap = /*ArmorChroma.config.getDisplayedArmorCap()*/ 100;
            int attrLast = (int)((EntityAttributeInstanceAccessor)armor).getUnclampedValue();
            /*if (ArmorChroma.config.reverse()) {
                Util.reverse((Object[])slots);
            }*/
            int totalPoints = 0;
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = player.getEquippedStack(slot);
                //if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem) {
                    //ArmorMaterial material = ((ArmorItem) stack.getItem()).getMaterial();
                    //ArmorItem.Type type = ((ArmorItem) stack.getItem()).getType();
                attributes.addTemporaryModifiers(stack.getAttributeModifiers(slot));
                int attrNext = Math.min((int) displayedArmorCap, (int) ((int) ((EntityAttributeInstanceAccessor) armor).getUnclampedValue()));
                int points = attrNext - attrLast;
                attrLast = attrNext;
                if (points <= 0) continue;
                pointsMap.put(slot, points);
                int dyeColor = -1;
                if (stack.getItem() instanceof DyeableArmorItem dye) {
                    dyeColor = dye.getColor(stack);
                }
                totalPoints += points;
                for (ArmorIcon icon : AdvancedArmorBarMain.armorIcons) {
                    if (icon.getItems().contains(stack.getItem())) {
                        segments.add(new ArmorSegment(icon, points, dyeColor, stack.hasGlint()));
                    }
                }
                    //pointsMap2.put(material, points);
                //}
            }
            /*if (totalPoints != (int) player.getAttributeValue(EntityAttributes.GENERIC_ARMOR)) {
                int remaining = (int) (player.getAttributeValue(EntityAttributes.GENERIC_ARMOR)) - totalPoints;
                segments.add(new ArmorSegment(new ArmorIcon(Identifier.of("aab", "default"), AdvancedArmorBarMain.sprite("materials/iron"), null), remaining, -1, false));
           }*/
            return attrLast;
        }

        /*private int getToughnessPoints(PlayerEntity player, Map<EquipmentSlot, Integer> pointsMap, List<ArmorSegment> segments) {
            AttributeContainer attributes = new AttributeContainer(FALLBACK_TOUGHNESS);
            EntityAttributeInstance armor = attributes.getCustomInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
            if (armor == null) {
                return 0;
            }
            int displayedArmorCap = /*ArmorChroma.config.getDisplayedArmorCap()/ 100;
            int attrLast = (int)((EntityAttributeInstanceAccessor)armor).getUnclampedValue();
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = player.getEquippedStack(slot);
                //if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem) {
                //ArmorMaterial material = ((ArmorItem) stack.getItem()).getMaterial();
                //ArmorItem.Type type = ((ArmorItem) stack.getItem()).getType();
                attributes.addTemporaryModifiers(stack.getAttributeModifiers(slot));
                int attrNext = Math.min((int) displayedArmorCap, (int) ((int) ((EntityAttributeInstanceAccessor) armor).getUnclampedValue()));
                int points = attrNext - attrLast;
                attrLast = attrNext;
                if (points <= 0) continue;
                pointsMap.put(slot, points);
                int dyeColor = -1;
                if (stack.getItem() instanceof DyeableArmorItem dye) {
                    dyeColor = dye.getColor(stack);
                }
                for (ArmorIcon icon : AdvancedArmorBarMain.armorIcons) {
                    if (icon.getItems().contains(stack.getItem())) {
                        segments.add(new ArmorSegment(icon, points, dyeColor, stack.hasGlint()));
                    }
                }
                //pointsMap2.put(material, points);
                //}
            }
            return attrLast;
        }*/
    }

    public static class AdvancedArmorBarLogic extends StatusBarLogic {

        private static final Identifier ID = AdvancedArmorBarMain.of("advanced_armor_logic");

        public AdvancedArmorBarLogic() {
            super(ID, (ent) -> 0, (ent) -> 0);
        }

        @Override
        public boolean isVisible(MinecraftClient client, PlayerEntity playerEntity) {
            return playerEntity.getArmor() > 0 || MathHelper.ceil(playerEntity.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)) > 0;
        }
    }
}
