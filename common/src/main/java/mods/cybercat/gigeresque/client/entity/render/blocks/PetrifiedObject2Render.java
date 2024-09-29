package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.common.api.client.renderer.GeoBlockRenderer;
import mods.cybercat.gigeresque.client.entity.model.blocks.PetrifiedObject2Model;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect2Entity;

public class PetrifiedObject2Render extends GeoBlockRenderer<PetrifiedOjbect2Entity> {
    public PetrifiedObject2Render() {
        super(new PetrifiedObject2Model());
    }
}