package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.common.api.client.renderer.GeoBlockRenderer;

import mods.cybercat.gigeresque.client.entity.model.blocks.PetrifiedObject5Model;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect5Entity;

public class PetrifiedObject5Render extends GeoBlockRenderer<PetrifiedOjbect5Entity> {

    public PetrifiedObject5Render() {
        super(new PetrifiedObject5Model());
    }
}
