package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.common.api.client.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.misc.AquaEggEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class AquaEggEntityModel extends DefaultedEntityGeoModel<AquaEggEntity> {

    public AquaEggEntityModel() {
        super(Constants.modResource("egg/egg"), false);
    }

    @Override
    public ResourceLocation getTextureResource(AquaEggEntity animatable) {
        return Constants.modResource("textures/entity/egg/egg_aqua.png");
    }

    @Override
    public RenderType getRenderType(AquaEggEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}