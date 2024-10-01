package mods.cybercat.gigeresque.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class MistParticle extends TextureSheetParticle {
    private final SpriteSet spriteProvider;
    private boolean reachedGround;
    ParticleRenderType PARTICLE_SHEET_TRANSLUCENT_LIT = new ParticleRenderType() {
        @Override
        public BufferBuilder begin(Tesselator tesselator, @NotNull TextureManager textureManager) {
            RenderSystem.depthMask(true);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            RenderSystem.depthMask(false);

            return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public String toString() {
            return "PARTICLE_SHEET_TRANSLUCENT_LIT";
        }
    };

    public MistParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i, SpriteSet spriteProvider) {
        super(clientWorld, d, e, f);
        xd = g;
        yd = h;
        zd = i;
        setColor(0.3f, 0.4f, 0.8f);
        setAlpha(0.2f);
        quadSize *= 1.5f;
        lifetime = (int) (100.0 / ((random.nextFloat()) * 0.5 + 0.5));
        reachedGround = false;
        hasPhysics = false;
        this.spriteProvider = spriteProvider;
        setSpriteFromAge(spriteProvider);
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;

        if (age++ >= lifetime) {
            remove();
        } else {
            setSpriteFromAge(spriteProvider);
            if (onGround) {
                yd = 0.0;
                reachedGround = true;
            }
            if (reachedGround) {
                yd += 0.001;
            } else {
                yd *= 0.95;
            }
            xd *= 0.8;
            zd *= 0.8;
            this.move(xd, yd, zd);
            if (y == yo) {
                xd *= 0.5;
                zd *= 0.5;
            }
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        //return PARTICLE_SHEET_TRANSLUCENT_LIT;
    }

    @Override
    public float getQuadSize(float tickDelta) {
        return quadSize * Mth.clamp(((age) + tickDelta) / (lifetime) * 32.0f, 0.0f, 1.0f);
    }

}
