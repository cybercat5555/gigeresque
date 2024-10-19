package mods.cybercat.gigeresque.common.block;

import net.minecraft.world.level.block.Block;

/**
 * TODO: https://trello.com/c/yFqxRPYe/33-alien-vents
 */
public class DungeonVentBlock extends Block {

    public DungeonVentBlock() {
        super(Properties.of().explosionResistance(Float.MAX_VALUE).strength(Float.MAX_VALUE, Float.MAX_VALUE).noOcclusion().noLootTable());
    }
}
