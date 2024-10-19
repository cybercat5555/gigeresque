package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.common.api.client.model.DefaultedEntityGeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.misc.HologramEntity;

public class HologramEntityModel extends DefaultedEntityGeoModel<HologramEntity> {

    public HologramEntityModel() {
        super(Constants.modResource("engineer_hologram/engineer_hologram"), false);
    }

    @Override
    public RenderType getRenderType(HologramEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
