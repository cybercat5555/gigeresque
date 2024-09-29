package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.common.api.client.renderer.GeoBlockRenderer;
import mods.cybercat.gigeresque.client.entity.model.blocks.PetrifiedObject4Model;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect4Entity;

public class PetrifiedObject4Render extends GeoBlockRenderer<PetrifiedOjbect4Entity> {
    public PetrifiedObject4Render() {
        super(new PetrifiedObject4Model());
    }
}