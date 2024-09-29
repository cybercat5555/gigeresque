package mods.cybercat.gigeresque.client.entity.model.blocks;

import mod.azure.azurelib.common.api.client.model.GeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect3Entity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class PetrifiedObject3Model extends GeoModel<PetrifiedOjbect3Entity> {

    @Override
    public ResourceLocation getModelResource(PetrifiedOjbect3Entity petrifiedOjbectEntity) {
        return Constants.modResource("geo/entity/neoburster/neoburster.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PetrifiedOjbect3Entity petrifiedOjbectEntity) {
        return EntityTextures.NEOBURSTER_PETRIFIED;
    }

    @Override
    public ResourceLocation getAnimationResource(PetrifiedOjbect3Entity petrifiedOjbectEntity) {
        return Constants.modResource("animations/entity/neoburster/neoburster.animation.json");
    }

    @Override
    public RenderType getRenderType(PetrifiedOjbect3Entity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
