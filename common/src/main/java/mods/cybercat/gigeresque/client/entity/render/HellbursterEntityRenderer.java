package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mod.azure.azurelib.common.internal.common.cache.object.BakedGeoModel;
import mods.cybercat.gigeresque.client.entity.model.HellbursterEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.HellbursterEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class HellbursterEntityRenderer extends GeoEntityRenderer<HellbursterEntity> {
    public HellbursterEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new HellbursterEntityModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public void preRender(PoseStack poseStack, HellbursterEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
        float scaleFactor = 1.0f + (animatable.getGrowth() / animatable.getMaxGrowth());
        poseStack.pushPose();
        poseStack.scale(scaleFactor, scaleFactor, scaleFactor);
        poseStack.popPose();
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight,
                packedOverlay, color);
    }

    @Override
    protected float getDeathMaxRotation(HellbursterEntity entityLivingBaseIn) {
        return 0;
    }
}
