package com.bvanseg.gigeresque.mixins.client.render;

import com.bvanseg.gigeresque.ConstantsJava;
import com.bvanseg.gigeresque.common.GigeresqueJava;
import com.bvanseg.gigeresque.common.block.BlocksJava;
import com.bvanseg.gigeresque.interfacing.Eggmorphable;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Boston Vanseghi
 */
@Environment(EnvType.CLIENT)
@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    private static final Identifier BLACK_FLUID_TEXTURE = new Identifier(GigeresqueJava.MOD_ID, "textures/misc/black_fluid_overlay.png");
    private static final Identifier EGGMORPH_OVERLAY_TEXTURE = new Identifier(GigeresqueJava.MOD_ID, "textures/misc/eggmorph_overlay.png");

    @Inject(method = {"renderOverlays"}, at = {@At("RETURN")})
    private static void renderOverlays(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (!client.player.isSpectator()) {
            double d = client.player.getEyeY() - 0.1111111119389534D;
            BlockPos blockPos = new BlockPos(client.player.getX(), d, client.player.getZ());
            FluidState fluidState = client.player.world.getFluidState(blockPos);

            if (fluidState.getBlockState().getBlock() == BlocksJava.BLACK_FLUID) {
                renderBlackFluidOverlay(client, matrices);
            }

            if (!client.player.isCreative() && client.player instanceof Eggmorphable eggmorphable && eggmorphable.isEggmorphing()) {
                float eggmorphingProgress = (ConstantsJava.EGGMORPH_DURATION - eggmorphable.getTicksUntilEggmorphed()) / ConstantsJava.EGGMORPH_DURATION;
                renderEggmorphOverlay(client, matrices, eggmorphingProgress);
            }
        }
    }

    private static void renderBlackFluidOverlay(MinecraftClient client, MatrixStack matrices) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableTexture();
        RenderSystem.setShaderTexture(0, BLACK_FLUID_TEXTURE);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        float f = client.player.getBrightnessAtEyes();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(f, f, f, 0.95F);
        float m = -client.player.getYaw() / 64.0F;
        float n = client.player.getPitch() / 64.0F;
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).texture(4.0F + m, 4.0F + n).next();
        bufferBuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).texture(0.0F + m, 4.0F + n).next();
        bufferBuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).texture(0.0F + m, 0.0F + n).next();
        bufferBuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).texture(4.0F + m, 0.0F + n).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.disableBlend();
    }

    private static void renderEggmorphOverlay(MinecraftClient client, MatrixStack matrices, float progress) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableTexture();
        RenderSystem.setShaderTexture(0, EGGMORPH_OVERLAY_TEXTURE);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        float f = client.player.getBrightnessAtEyes();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(f, f, f, progress);
        float m = -client.player.getYaw() / 64.0F;
        float n = client.player.getPitch() / 64.0F;
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).texture(4.0F + m, 4.0F + n).next();
        bufferBuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).texture(0.0F + m, 4.0F + n).next();
        bufferBuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).texture(0.0F + m, 0.0F + n).next();
        bufferBuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).texture(4.0F + m, 0.0F + n).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.disableBlend();
    }
}
