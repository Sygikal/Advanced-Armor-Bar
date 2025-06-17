package dev.sygii.advancedarmorbar.mixin;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityAttributes.class)
public class EntityAttributesMixin {
	@ModifyArg(
			method = "<clinit>",
			slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=generic.armor")),
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/attribute/ClampedEntityAttribute;<init>(Ljava/lang/String;DDD)V", ordinal = 0),
			index = 3
	)
	private static double modifyMax(double original) {
		return 80.0;
	}

	@ModifyArg(
			method = "<clinit>",
			slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=generic.armor_toughness")),
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/attribute/ClampedEntityAttribute;<init>(Ljava/lang/String;DDD)V", ordinal = 0),
			index = 3
	)
	private static double modifyMax2(double original) {
		return 80.0;
	}
}
