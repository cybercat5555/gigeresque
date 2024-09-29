package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.common.api.client.renderer.GeoBlockRenderer;
import mods.cybercat.gigeresque.client.entity.model.blocks.PetrifiedObject3Model;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect3Entity;

public class PetrifiedObject3Render extends GeoBlockRenderer<PetrifiedOjbect3Entity> {
    public PetrifiedObject3Render() {
        super(new PetrifiedObject3Model());
    }
}