package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.common.api.client.model.DefaultedEntityGeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.rom.RomAlienEntity;

public class AlienRomEntityModel extends DefaultedEntityGeoModel<RomAlienEntity> {

    public AlienRomEntityModel() {
        super(Constants.modResource("rom_alien/rom_alien"), false);
    }

    @Override
    public ResourceLocation getTextureResource(RomAlienEntity object) {
        return object.isPassedOut() ? EntityTextures.ROM_ALIEN_STATIS : EntityTextures.ALIEN;
    }

    @Override
    public RenderType getRenderType(RomAlienEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

}
