package mods.cybercat.gigeresque.mixins.client.entity.render;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mods.cybercat.gigeresque.client.entity.render.feature.EggmorphFeatureRenderer;

/**
 * @author Aelpecyem
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Shadow
    protected abstract boolean addLayer(RenderLayer<T, M> feature);

    // @Shadow
    // protected abstract float getFlipDegrees(T animatable);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererProvider.Context ctx, M model, float shadowRadius, CallbackInfo ci) {
        this.addLayer(new EggmorphFeatureRenderer<>((RenderLayerParent<T, M>) this));
    }

    /*
     * TODO: Finish via renders
     */
    // @Inject(method = "setupRotations", at = @At("TAIL"))
    // private void gigeresque$vanillaLayingMixin(
    // T entity,
    // PoseStack poseStack,
    // float bob,
    // float yBodyRot,
    // float partialTick,
    // float scale,
    // CallbackInfo ci
    // ) {
    // if (
    // getFlipDegrees(entity) == 90 &&
    // entity.hasPassenger(
    // FacehuggerEntity.class::isInstance
    // )
    // ) {
    // poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yBodyRot));
    // poseStack.mulPose(Axis.YP.rotationDegrees(yBodyRot));
    // poseStack.mulPose(Axis.ZP.rotationDegrees(this.getFlipDegrees(entity)));
    // poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
    // }
    // }
}
