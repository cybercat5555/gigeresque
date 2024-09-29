package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.AquaEggEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.misc.AquaEggEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AquaEggEntityRender extends GeoEntityRenderer<AquaEggEntity> {

    public AquaEggEntityRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AquaEggEntityModel());
    }

}