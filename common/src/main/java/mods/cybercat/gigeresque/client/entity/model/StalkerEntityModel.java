package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.common.api.client.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class StalkerEntityModel extends DefaultedEntityGeoModel<StalkerEntity> {

    public StalkerEntityModel() {
        super(Constants.modResource("stalker/stalker"), false);
    }

    @Override
    public ResourceLocation getTextureResource(StalkerEntity animatable) {
        return animatable.walkAnimation.speedOld < 0.35F && !animatable.swinging ? Constants.modResource("textures/entity/stalker/stalker_transparent.png") :super.getTextureResource(animatable);
    }

    @Override
    public RenderType getRenderType(StalkerEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
