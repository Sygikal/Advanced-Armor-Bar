package dev.sygii.advancedarmorbar.elements;

import com.google.common.collect.Multimap;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.sygii.advancedarmorbar.AdvancedArmorBarMain;
import dev.sygii.advancedarmorbar.EntityAttributeInstanceAccessor;
import dev.sygii.advancedarmorbar.data.ArmorIcon;
import dev.sygii.advancedarmorbar.data.ArmorSegment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;

//? if >=1.21.5 {
/*import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.client.gl.RenderPipelines;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.pipeline.RenderPipeline;
*///?}
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.player.PlayerEntity;
//? if =1.20.1 {
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.DyeableArmorItem;
//?}
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import dev.sygii.hotbarapi.elements.StatusBarRenderer;
import dev.sygii.hotbarapi.elements.StatusBarLogic;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;

public class AdvancedArmorBar {
    public static class AdvancedArmorBarRenderer extends StatusBarRenderer {
        private static final Identifier TOUGHESS_UNDERLAY = AdvancedArmorBarMain.sprite("toughness_underlay");
        private static final Identifier EMPTY = AdvancedArmorBarMain.sprite("armor_empty");
        private static final Identifier ID = AdvancedArmorBarMain.of("advanced_armor_renderer");
        public float height = 10;

        //? if >=1.21.6 {
        /*RenderPipeline LAYER = RenderPipelines.GUI_TEXTURED;
        *///?} else if =1.21.5 {
        /*Function<Identifier, RenderLayer> LAYER = RenderLayer::getGuiTextured;
        *///?}

        public AdvancedArmorBarRenderer() {
            super(ID, TOUGHESS_UNDERLAY, StatusBarRenderer.Position.LEFT, StatusBarRenderer.Direction.L2R);
        }

        @Override
        public float getHeight(MinecraftClient client, PlayerEntity playerEntity) {
            return 10 + height;
        }

        @Override
        public void render(MinecraftClient client, DrawContext context, PlayerEntity playerEntity, int x, int y, StatusBarLogic logic) {
            //? if =1.20.1 {
            float toughness = (float) playerEntity.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
            //?} else {
            /*float toughness = (float) playerEntity.getAttributeValue(EntityAttributes.ARMOR_TOUGHNESS);
            *///?}

            //float armor = (float) playerEntity.getAttributeValue(EntityAttributes.GENERIC_ARMOR);

            //float maxArmor = (float) (Math.ceil(armor/20.0) * 20);
            float maxTough = (float) (Math.ceil(toughness/20.0) * 20);

           // int realArmor = MathHelper.ceil(armor);
            int realTough = MathHelper.ceil(toughness);

            //float combinedMax = Math.max(maxArmor, maxTough);
            //float realMax = Math.max(realArmor, realTough);
            int scale = 10;

            /*float f2 = Math.max((float)combinedMax, (float) Math.max((float)MathHelper.ceil(armor),  (float)MathHelper.ceil(toughness)));
            //int p2 = MathHelper.ceil(playerEntity.getAbsorptionAmount());
            int q2 = MathHelper.ceil((f2 /*+ (float)p2//) / 2.0F / 10.0F);
            int lines = Math.max(10 - (q2 - 2), 3);*/

            /*int mmmaxxx = MathHelper.ceil((double)combinedMax / (double)2.0F);

            int sexscale = (int) (combinedMax / 2);
            float apparition = combinedMax / (float)(sexscale);*/

            List<ArmorSegment> segments = new ArrayList<>();
            int totalPoints = this.getArmorPoints(playerEntity, segments);

            //Sorting
            Collections.sort(segments, new Comparator<ArmorSegment>(){
                public int compare(ArmorSegment o1, ArmorSegment o2){
                    if(o1.getPoints() == o2.getPoints())
                        return 0;
                    return o1.getPoints() < o2.getPoints() ? -1 : 1;
                }
            });
            Collections.reverse(segments);

            float maxArmor2 = (float) (Math.ceil(totalPoints/20.0) * 20);
            float combinedMax2 = Math.max(maxArmor2, maxTough);
            float f22 = Math.max((float)combinedMax2, (float) Math.max((float)MathHelper.ceil(totalPoints),  (float)MathHelper.ceil(toughness)));
            int q22 = MathHelper.ceil((f22) / 2.0F / 10.0F);
            int lines2 = Math.max(10 - (q22 - 2), 3);

            //Switch maxArmor2 with totalPoints to cull after 1st row
            int max2 = MathHelper.ceil((double)combinedMax2 / (double)2.0F);

            int sexscale2 = (int) (combinedMax2 / 2);
            float apparition2 = combinedMax2 / (float)(sexscale2);

            for(int m = max2 - 1; m >= 0; --m) {
                int n = m / scale;
                int o = m % scale;
                int p = x + (getDirection().equals(Direction.L2R) ? (getPosition().equals(Position.RIGHT) ? -72 : 0) + o * 8 : (getPosition().equals(Position.LEFT) ? 72 : 0) + -(o * 8));
                int q = y - n * lines2;


                float prevasd = (float)m * apparition2;
                float asd = (float)(m + 1) * apparition2;
                float sex = asd - apparition2 / 2.0F;

                float level = n+1;
                float armorLevel = level + 0.5f;

                //?if <=1.21.5
                context.getMatrices().translate(0, 0, -armorLevel);
                if (realTough > sex) {
                    boolean bl = realTough == asd;
                    boolean bl2 = (m+1) % 10 == 0;
                    //? if =1.20.1 {
                    context.drawTexture(TOUGHESS_UNDERLAY, p - 1, q - 1, 0.0F, 0.0F, (bl || bl2) ? 11 : 10, 11, 44, 11);
                    //?} else {
                    /*context.drawTexture(LAYER, TOUGHESS_UNDERLAY, p - 1, q - 1, 0.0F, 0.0F, (bl || bl2) ? 11 : 10, 11, 44, 11);
                     *///?}
                }

                if (realTough > prevasd && realTough <= sex) {
                    //? if =1.20.1 {
                    context.drawTexture(TOUGHESS_UNDERLAY, p - 1, q - 1, 0.0F, 0.0F, 6, 11, 44, 11);
                    //?} else {
                    /*context.drawTexture(LAYER, TOUGHESS_UNDERLAY, p - 1, q - 1, 0.0F, 0.0F, 6, 11, 44, 11);
                     *///?}
                }

                //? if =1.20.1 {
                context.drawTexture(EMPTY, p, q, 0.0F, 0.0F, 9, 9, 9, 9);
                //?} else {
                /*context.drawTexture(LAYER, EMPTY, p, q, 0.0F, 0.0F, 9, 9, 9, 9);
                 *///?}
                //?if <=1.21.5
                context.getMatrices().translate(0, 0, armorLevel);
            }

            int donePoints = 0;
            for (ArmorSegment segment : segments) {
                for (int i = segment.getPoints() - 1; i >= 0; --i) {
                    int n = (donePoints + i) / (scale * 2);
                    int o = (donePoints + i)  % (scale * 2);
                    int p = x + (getDirection().equals(Direction.L2R) ? (getPosition().equals(Position.RIGHT) ? -72 : 0) + o * 4 : (getPosition().equals(Position.LEFT) ? 72 : 0) + -(o * 4));
                    int q = y - n * lines2;
                    boolean firstHalf = (donePoints + i) % 2 == 0;
                    boolean lastHalf = (donePoints + i) % 2 == 1;
                    float level = n+1;
                    float enchantLevel = level + 0.6f;
                    float armorLevel = level + 0.5f;

                    //For only render armor under used
                    /*if (firstHalf) {
                        context.getMatrices().translate(0, 0, -armorLevel);
                        context.drawTexture(EMPTY, p, q,0, 0.0F, 9, 9, 9, 9);
                        context.getMatrices().translate(0, 0, armorLevel);
                    }*/

                    if (segment.getDyeColor() != -1) {
                        int hex = segment.getDyeColor();
                        //RenderSystem.setShaderColor(ColorHelper.Argb.getRed(hex) / 255.0F, ColorHelper.Argb.getGreen(hex) / 255.0F, ColorHelper.Argb.getBlue(hex) / 255.0F, 1.0f);
                    }

                    //? if <=1.21.5
                    context.getMatrices().translate(0, 0, -level);

                    //? if =1.20.1 {
                    context.drawTexture(segment.getIcon().getTexture(), p, q, lastHalf ? 4.0F : 0, 0.0F, lastHalf || firstHalf ? 5 : 9, 9, 9, 9);
                    //?} else {
                    /*context.drawTexture(LAYER, segment.getIcon().getTexture(), p, q, lastHalf ? 4.0F : 0, 0.0F, lastHalf || firstHalf ? 5 : 9, 9, 9, 9);
                    *///?}

                    if (segment.isEnchanted()) {
                        //? if <=1.21.5 {
                        this.drawTexturedGlintRect(context, client, p, q, lastHalf ? 4 : 0, 0, lastHalf || firstHalf ? 5 : 9, 9);
                        //?}
                    }
                    //? if <=1.21.5
                    context.getMatrices().translate(0, 0, level);

                    //RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                }
                donePoints += segment.getPoints();
            }

            this.height = (q22 - 1) * lines2;
        }

        /*private void drawTexturedGlintRect(DrawContext context, MinecraftClient client, int x, int y, int u, int v, int width, int height) {
            float intensity = client.options.getGlintStrength().getValue().floatValue()
                    * 1.0f;
            int color = getWhite(MathHelper.clamp(intensity, 0, 1));
            //context.drawTexture(id -> RenderLayer.getGlint(), ITEM_ENCHANTMENT_GLINT, x, y, u, v, width, height, TEXTURE_SIZE, TEXTURE_SIZE, color);
            setColor(context, color);
            context.drawTexture(ITEM_ENCHANTMENT_GLINT, x, y, u, v, width, height);
            setColor(context, -1);
        }*/

        //? if =1.20.1 {
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
        //?} else if =1.21.6 {
        /*/^public void drawTexturedGlintRect(DrawContext context, MinecraftClient client, int x, int y, int u, int v, int width, int height) {
            GlStateManager._depthFunc(514);
            GlStateManager._blendFuncSeparate((int)768, (int)1, (int)1, (int)0);
            float intensity = ((Double)client.options.getGlintStrength().getValue()).floatValue() * 1.5f;
            //RenderSystem.setShaderColor((float)intensity, (float)intensity, (float)intensity, (float)1.0f);
            double glintSpeed = (Double)client.options.getGlintSpeed().getValue();
            long time = (long)((double) Util.getMeasuringTimeMs() * glintSpeed * 8.0);
            u = (int)((long)u + (-(time % 110000L) * 256L / 110000L + (long)x));
            v = (int)((long)v + (time % 30000L * 256L / 30000L + (long)y));
            int color = ColorHelper.getWhite(MathHelper.clamp(intensity, 0, 1));
            context.drawTexture(RenderPipelines.GUI_TEXTURED, ItemRenderer.ITEM_ENCHANTMENT_GLINT, x, y, (float)u, (float)v, width, height, 256, 256, color);
            context.drawTexture(RenderPipelines.GUI_TEXTURED, AdvancedArmorBarMain.sprite("materials/iron"), x, y, u, v, width, height, 9, 9);
            GlStateManager._depthFunc(515);
            GlStateManager._blendFuncSeparate(770, 771, 1, 0);
        }^/
        private void drawTexturedGlintRect(DrawContext context, MinecraftClient client, int x, int y, float u, float v, int width, int height) {
            float intensity = ((Double)client.options.getGlintStrength().getValue()).floatValue() * 1.5f;
            int color = ColorHelper.getWhite(MathHelper.clamp(intensity, 0, 1));
            context.drawTexture(RenderPipelines.GLINT, ItemRenderer.ITEM_ENCHANTMENT_GLINT, x, y, u, v, width, height, 256, 256, color);
        }
        *///?} else {
        /*private void drawTexturedGlintRect(DrawContext context, MinecraftClient client, int x, int y, float u, float v, int width, int height) {
            float intensity = ((Double)client.options.getGlintStrength().getValue()).floatValue() * 1.5f;
            int color = ColorHelper.getWhite(MathHelper.clamp(intensity, 0, 1));
            context.drawTexture(id -> RenderLayer.getGlint(), ItemRenderer.ITEM_ENCHANTMENT_GLINT, x, y, u, v, width, height, 256, 256, color);
        }
        *///?}

        public static void setColor(DrawContext context, int hex) {
            //? if =1.20.1 {
            RenderSystem.setShaderColor(ColorHelper.Argb.getRed(hex) / 255.0F, ColorHelper.Argb.getGreen(hex) / 255.0F, ColorHelper.Argb.getBlue(hex) / 255.0F, ColorHelper.Argb.getAlpha(hex) / 255.0F);
            //?} else {
            /*int i = 0;
            *///?}
            //RenderSystem.setShaderColor(ColorHelper.getRed(hex) / 255.0F, ColorHelper.getGreen(hex) / 255.0F, ColorHelper.getBlue(hex) / 255.0F, ColorHelper.getAlpha(hex) / 255.0F);
            //context.setShaderColor(ColorHelper.Argb.getRed(hex) / 255.0F, ColorHelper.Argb.getGreen(hex) / 255.0F, ColorHelper.Argb.getBlue(hex) / 255.0F, ColorHelper.Argb.getAlpha(hex) / 255.0F);
        }

        //? if =1.20.1 {
        private static final DefaultAttributeContainer FALLBACK_ARMOR = DefaultAttributeContainer.builder().add(EntityAttributes.GENERIC_ARMOR).build();
        private static final DefaultAttributeContainer FALLBACK_TOUGHNESS = DefaultAttributeContainer.builder().add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).build();
        //?} else {
        /*private static final DefaultAttributeContainer FALLBACK_ARMOR = DefaultAttributeContainer.builder().add(EntityAttributes.ARMOR).build();
        *///?}
        private static final int ARMOR_CAP = 200;

        private int getArmorPoints(PlayerEntity player, List<ArmorSegment> segments) {
            AttributeContainer attributes = new AttributeContainer(FALLBACK_ARMOR);
            //? if =1.20.1 {
            EntityAttributeInstance armor = attributes.getCustomInstance(EntityAttributes.GENERIC_ARMOR);
            //?} else {
            /*EntityAttributeInstance armor = attributes.getCustomInstance(EntityAttributes.ARMOR);
            *///?}
            if (armor == null) {
                return 0;
            }
            int attrLast = (int)((EntityAttributeInstanceAccessor)armor).getUnclampedValue();
            int totalPoints = 0;
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = player.getEquippedStack(slot);
                //if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem) {
                    //ArmorMaterial material = ((ArmorItem) stack.getItem()).getMaterial();
                    //ArmorItem.Type type = ((ArmorItem) stack.getItem()).getType();

                //? if =1.20.1 {
                attributes.addTemporaryModifiers(stack.getAttributeModifiers(slot));
                int attrNext = Math.min((int) ARMOR_CAP, (int) ((int) ((EntityAttributeInstanceAccessor) armor).getUnclampedValue()));
                int points = attrNext - attrLast;
                attrLast = attrNext;
                //?} else {
                //int points = getDefense(stack, slot);
                //?}
                if (points <= 0) continue;
                //? if =1.20.1 {
                int dyeColor = -1;
                if (stack.getItem() instanceof DyeableArmorItem dye) {
                    dyeColor = dye.getColor(stack);
                }
                //?} else {
                /*int dyeColor = DyedColorComponent.getColor(stack, -1);
                *///?}

                totalPoints += points;
                for (ArmorIcon icon : AdvancedArmorBarMain.armorIcons) {
                    if (icon.getItems().contains(stack.getItem())) {
                        segments.add(new ArmorSegment(icon, points, dyeColor, stack.hasGlint()));
                    }
                }
            }
            /*if (totalPoints != (int) player.getAttributeValue(EntityAttributes.GENERIC_ARMOR)) {
                int remaining = (int) (player.getAttributeValue(EntityAttributes.GENERIC_ARMOR)) - totalPoints;
                segments.add(new ArmorSegment(new ArmorIcon(Identifier.of("aab", "default"), AdvancedArmorBarMain.sprite("materials/iron"), null), remaining, -1, false));
           }*/
            //return attrLast;
            return totalPoints;
        }

        //if >=1.21.5 {
        /*private static int getDefense(ItemStack itemStack, EquipmentSlot slot) {
            AttributeModifiersComponent modifier = itemStack.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);

            for (AttributeModifiersComponent.Entry entry : modifier.modifiers()) {
                if (entry.slot().matches(slot) && entry.attribute().equals(EntityAttributes.ARMOR)) {
                    return (int) entry.modifier().value();
                }
            }
            return 0;
        }
        *///?}
    }

    public static class AdvancedArmorBarLogic extends StatusBarLogic {

        private static final Identifier ID = AdvancedArmorBarMain.of("advanced_armor_logic");

        public AdvancedArmorBarLogic() {
            super(ID, (ent) -> 0, (ent) -> 0);
        }

        @Override
        public boolean isVisible(MinecraftClient client, PlayerEntity playerEntity) {
            //? if =1.20.1 {
            return playerEntity.getArmor() > 0 || MathHelper.ceil(playerEntity.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)) > 0;
            //?} else {
            /*return playerEntity.getArmor() > 0 || MathHelper.ceil(playerEntity.getAttributeValue(EntityAttributes.ARMOR_TOUGHNESS)) > 0;
            *///?}
        }
    }
}
