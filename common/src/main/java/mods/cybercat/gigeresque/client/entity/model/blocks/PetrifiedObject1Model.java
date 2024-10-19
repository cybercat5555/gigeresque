package mods.cybercat.gigeresque.client.entity.model.blocks;

import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect1Entity;

public class PetrifiedObject1Model extends GeoModel<PetrifiedOjbect1Entity> {

    @Override
    public ResourceLocation getModelResource(PetrifiedOjbect1Entity petrifiedOjbectEntity) {
        return Constants.modResource("geo/entity/aquatic_chestburster/aquatic_chestburster.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PetrifiedOjbect1Entity petrifiedOjbectEntity) {
        return EntityTextures.AQUATIC_CHESTBURSTER_PETRIFIED;
    }

    @Override
    public ResourceLocation getAnimationResource(PetrifiedOjbect1Entity petrifiedOjbectEntity) {
        return Constants.modResource("animations/entity/aquatic_chestburster/aquatic_chestburster.animation.json");
    }

    @Override
    public RenderType getRenderType(PetrifiedOjbect1Entity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
