package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.StalkerEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;

public class StalkerEntityRenderer extends GeoEntityRenderer<StalkerEntity> {

    public StalkerEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new StalkerEntityModel());
    }

    @Override
    protected float getShadowRadius(@NotNull StalkerEntity entity) {
        return animatable.walkAnimation.speedOld < 0.35F && !animatable.swinging ? 0.0f : 1.0f;
    }

    @Override
    protected float getDeathMaxRotation(StalkerEntity entityLivingBaseIn) {
        return 0.0F;
    }

    @Override
    public float getMotionAnimThreshold(StalkerEntity animatable) {
        return 0.005f;
    }
}
