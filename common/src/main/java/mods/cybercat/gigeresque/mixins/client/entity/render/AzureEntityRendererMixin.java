package mods.cybercat.gigeresque.mixins.client.entity.render;

import mod.azure.azurelib.common.api.client.model.GeoModel;
import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mod.azure.azurelib.common.api.client.renderer.layer.GeoRenderLayer;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.common.internal.client.renderer.GeoRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mods.cybercat.gigeresque.client.entity.render.feature.EggmorphGeoFeatureRenderer;

/**
 * @author Aelpecyem
 */
@Mixin(value = GeoEntityRenderer.class)
public abstract class AzureEntityRendererMixin<T extends Entity & GeoEntity> {

    @Shadow
    public abstract T getAnimatable();

    @Shadow
    public abstract GeoEntityRenderer<T> addRenderLayer(GeoRenderLayer<T> layer);

    // @Shadow
    // protected abstract float getDeathMaxRotation(T animatable);

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(EntityRendererProvider.Context ctx, GeoModel<T> modelProvider, CallbackInfo ci) {
        if (this.getAnimatable() instanceof Mob)
            this.addRenderLayer(new EggmorphGeoFeatureRenderer<>((GeoRenderer<T>) this));
    }

    /*
     * TODO: Finish via renders
     */
    // @Inject(
    // method = "applyRotations(Lnet/minecraft/world/entity/Entity;Lcom/mojang/blaze3d/vertex/PoseStack;FFFF)V", at =
    // @At("TAIL"),
    // remap = false
    // )
    // private void gigeresque$LayingMixin(
    // T animatable,
    // PoseStack poseStack,
    // float ageInTicks,
    // float rotationYaw,
    // float partialTick,
    // float nativeScale,
    // CallbackInfo ci
    // ) {
    // if (
    // getDeathMaxRotation(animatable) == 90 &&
    // animatable.hasPassenger(
    // FacehuggerEntity.class::isInstance
    // )
    // ) {
    // poseStack.mulPose(Axis.YP.rotationDegrees(180f - rotationYaw));
    // poseStack.mulPose(Axis.YP.rotationDegrees(rotationYaw));
    // poseStack.mulPose(Axis.ZP.rotationDegrees(getDeathMaxRotation(animatable)));
    // poseStack.mulPose(Axis.YP.rotationDegrees(270f));
    // }
    // }
}
