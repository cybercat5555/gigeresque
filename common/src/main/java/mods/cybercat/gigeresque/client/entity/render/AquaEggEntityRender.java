package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.AquaEggEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.misc.AquaEggEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

public class AquaEggEntityRender extends GeoEntityRenderer<AquaEggEntity> {

    public AquaEggEntityRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AquaEggEntityModel());
    }

    @Override
    public void render(AquaEggEntity entity, float entityYaw, float partialTick, PoseStack stack, @NotNull MultiBufferSource bufferSource, int packedLightIn) {
        var scaleFactor = 0.2f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entity, entityYaw, partialTick, stack, bufferSource, packedLightIn);
    }

}