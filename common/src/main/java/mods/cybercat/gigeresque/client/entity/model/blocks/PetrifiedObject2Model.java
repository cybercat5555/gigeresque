package mods.cybercat.gigeresque.client.entity.model.blocks;

import mod.azure.azurelib.common.api.client.model.GeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect2Entity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class PetrifiedObject2Model extends GeoModel<PetrifiedOjbect2Entity> {

    @Override
    public ResourceLocation getModelResource(PetrifiedOjbect2Entity petrifiedOjbectEntity) {
        return Constants.modResource("geo/entity/chestburster/chestburster.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PetrifiedOjbect2Entity petrifiedOjbectEntity) {
        return EntityTextures.CHESTBURSTER_PETRIFIED;
    }

    @Override
    public ResourceLocation getAnimationResource(PetrifiedOjbect2Entity petrifiedOjbectEntity) {
        return Constants.modResource("animations/entity/chestburster/chestburster.animation.json");
    }

    @Override
    public RenderType getRenderType(PetrifiedOjbect2Entity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
