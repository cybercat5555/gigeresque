package mods.cybercat.gigeresque.client.entity.model.blocks;

import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect4Entity;

public class PetrifiedObject4Model extends GeoModel<PetrifiedOjbect4Entity> {

    @Override
    public ResourceLocation getModelResource(PetrifiedOjbect4Entity petrifiedOjbectEntity) {
        return Constants.modResource("geo/entity/runnerburster/runnerburster.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PetrifiedOjbect4Entity petrifiedOjbectEntity) {
        return EntityTextures.RUNNERBURSTER_PETRIFIED;
    }

    @Override
    public ResourceLocation getAnimationResource(PetrifiedOjbect4Entity petrifiedOjbectEntity) {
        return Constants.modResource("animations/entity/runnerburster/runnerburster.animation.json");
    }

    @Override
    public RenderType getRenderType(PetrifiedOjbect4Entity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
