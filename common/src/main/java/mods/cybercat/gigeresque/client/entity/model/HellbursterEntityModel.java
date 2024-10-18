package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.common.api.client.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.HellbursterEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class HellbursterEntityModel extends DefaultedEntityGeoModel<HellbursterEntity> {

    public HellbursterEntityModel() {
        super(Constants.modResource("hell_burster/hell_burster"), false);
    }

    @Override
    public RenderType getRenderType(HellbursterEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
