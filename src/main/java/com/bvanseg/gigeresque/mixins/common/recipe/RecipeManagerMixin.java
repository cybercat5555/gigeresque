package com.bvanseg.gigeresque.mixins.common.recipe;

import com.bvanseg.gigeresque.common.GigeresqueJava;
import com.google.gson.JsonElement;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

/**
 * Derived from: https://fabricmc.net/wiki/tutorial:dynamic_recipe_generation
 *
 * @author Boston Vanseghi
 */
@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Inject(method = "apply", at = @At("HEAD"))
    public void interceptApply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        if (GigeresqueJava.config.getFeatures().getSurgeryKit()) {
            map.put(new Identifier(GigeresqueJava.MOD_ID, "surgery_kit"), RecipesJava.SURGERY_KIT_RECIPE);
        }
    }
}
