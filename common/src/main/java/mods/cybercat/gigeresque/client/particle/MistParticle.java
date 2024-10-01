package mods.cybercat.gigeresque.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class MistParticle extends TextureSheetParticle {
    private final SpriteSet spriteProvider;
    private boolean reachedGround;

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
    }

    @Override
    public float getQuadSize(float tickDelta) {
        return quadSize * Mth.clamp(((age) + tickDelta) / (lifetime) * 32.0f, 0.0f, 1.0f);
    }

}
