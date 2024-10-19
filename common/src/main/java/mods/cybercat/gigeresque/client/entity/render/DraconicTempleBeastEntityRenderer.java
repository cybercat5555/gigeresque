package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mod.azure.azurelib.common.internal.common.cache.object.BakedGeoModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.DraconicTempleBeastEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.DraconicTempleBeastEntity;

public class DraconicTempleBeastEntityRenderer extends GeoEntityRenderer<DraconicTempleBeastEntity> {

    public DraconicTempleBeastEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new DraconicTempleBeastEntityModel());
        this.shadowRadius = 1.0f;
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        DraconicTempleBeastEntity animatable,
        BakedGeoModel model,
        MultiBufferSource bufferSource,
        VertexConsumer buffer,
        boolean isReRender,
        float partialTick,
        int packedLight,
        int packedOverlay,
        int colour
    ) {
        super.preRender(
            poseStack,
            animatable,
            model,
            bufferSource,
            buffer,
            isReRender,
            partialTick,
            packedLight,
            packedOverlay,
            colour
        );
        poseStack.scale(1.23F, 1.23F, 1.23F);
    }

    @Override
    protected float getDeathMaxRotation(DraconicTempleBeastEntity entityLivingBaseIn) {
        return 0.0F;
    }

    @Override
    public float getMotionAnimThreshold(DraconicTempleBeastEntity animatable) {
        return 0.005f;
    }
}
