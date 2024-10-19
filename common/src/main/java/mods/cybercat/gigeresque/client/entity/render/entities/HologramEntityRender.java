package mods.cybercat.gigeresque.client.entity.render.entities;

import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mod.azure.azurelib.common.api.client.renderer.layer.AutoGlowingGeoLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.HologramEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.misc.HologramEntity;

public class HologramEntityRender extends GeoEntityRenderer<HologramEntity> {

    public HologramEntityRender(EntityRendererProvider.Context context) {
        super(context, new HologramEntityModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
