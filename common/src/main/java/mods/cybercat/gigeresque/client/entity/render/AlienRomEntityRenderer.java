package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.AlienRomEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.rom.RomAlienEntity;

public class AlienRomEntityRenderer extends GeoEntityRenderer<RomAlienEntity> {

    public AlienRomEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new AlienRomEntityModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(
        RomAlienEntity entity,
        float entityYaw,
        float partialTick,
        PoseStack stack,
        @NotNull MultiBufferSource bufferSource,
        int packedLightIn
    ) {
        var scaleFactor = 0.8f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entity, entityYaw, partialTick, stack, bufferSource, packedLightIn);
    }

    @Override
    protected float getDeathMaxRotation(RomAlienEntity entityLivingBaseIn) {
        return 0;
    }

    @Override
    public float getMotionAnimThreshold(RomAlienEntity animatable) {
        return !animatable.isExecuting() && animatable.isVehicle() ? 0.000f : animatable.isPassedOut() ? 0.5f : 0.005f;
    }
}
