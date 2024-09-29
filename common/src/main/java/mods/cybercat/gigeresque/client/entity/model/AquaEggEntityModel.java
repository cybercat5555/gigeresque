package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.common.api.client.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.misc.AquaEggEntity;

public class AquaEggEntityModel extends DefaultedEntityGeoModel<AquaEggEntity> {

    public AquaEggEntityModel() {
        super(Constants.modResource("aquatic_egg/aquatic_egg"), false);
    }

}