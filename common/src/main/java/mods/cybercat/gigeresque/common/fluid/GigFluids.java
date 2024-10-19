package mods.cybercat.gigeresque.common.fluid;

import java.util.function.Supplier;

import mods.cybercat.gigeresque.CommonMod;

public class GigFluids implements CommonFluidRegistryInterface {

    public static final Supplier<BlackFluid.Still> BLACK_FLUID_STILL = CommonFluidRegistryInterface.registerFluid(
        CommonMod.MOD_ID,
        "black_fluid_still",
        BlackFluid.Still::new
    );

    public static final Supplier<BlackFluid.Flowing> BLACK_FLUID_FLOWING = CommonFluidRegistryInterface.registerFluid(
        CommonMod.MOD_ID,
        "black_fluid_flowing",
        BlackFluid.Flowing::new
    );

    public static void initialize() {}
}
