package dev.sygii.advancedarmorbar.mixin;

import dev.sygii.advancedarmorbar.EntityAttributeInstanceAccessor;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntityAttributeInstance.class)
public abstract class EntityAttributeInstanceMixin implements EntityAttributeInstanceAccessor {
	@Unique
	private double unclampedValue;

	@Shadow
	public abstract double getValue();

	@Inject(method="computeValue", at=@At(value="INVOKE", target="net/minecraft/entity/attribute/EntityAttribute.clamp(D)D"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void onComputeValue(CallbackInfoReturnable<Double> info, double d, double value) {
		this.unclampedValue = value;
	}

	public double getUnclampedValue() {
		this.getValue();
		return this.unclampedValue;
	}
}
