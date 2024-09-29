package mods.cybercat.gigeresque.client.entity.model.blocks;

import mod.azure.azurelib.common.api.client.model.GeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect5Entity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class PetrifiedObject5Model extends GeoModel<PetrifiedOjbect5Entity> {

    @Override
    public ResourceLocation getModelResource(PetrifiedOjbect5Entity petrifiedOjbectEntity) {
        return Constants.modResource("geo/block/neomorph_spore_pods/neomorph_spore_pods.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PetrifiedOjbect5Entity petrifiedOjbectEntity) {
        return EntityTextures.SPORE_PETRIFIED;
    }

    @Override
    public ResourceLocation getAnimationResource(PetrifiedOjbect5Entity petrifiedOjbectEntity) {
        return Constants.modResource("animations/block/neomorph_spore_pods/neomorph_spore_pods.animation.json");
    }

    @Override
    public RenderType getRenderType(PetrifiedOjbect5Entity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
