package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.common.api.client.renderer.GeoBlockRenderer;

import mods.cybercat.gigeresque.client.entity.model.blocks.PetrifiedObject1Model;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect1Entity;

public class PetrifiedObject1Render extends GeoBlockRenderer<PetrifiedOjbect1Entity> {

    public PetrifiedObject1Render() {
        super(new PetrifiedObject1Model());
    }
}
