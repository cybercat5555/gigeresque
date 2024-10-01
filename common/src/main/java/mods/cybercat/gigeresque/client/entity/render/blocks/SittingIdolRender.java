package mods.cybercat.gigeresque.client.entity.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.azure.azurelib.common.api.client.renderer.GeoBlockRenderer;
import mod.azure.azurelib.common.api.client.renderer.layer.BlockAndItemGeoLayer;
import mod.azure.azurelib.common.internal.common.cache.object.GeoBone;
import mods.cybercat.gigeresque.client.entity.model.blocks.SittingIdolModel;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SittingIdolRender extends GeoBlockRenderer<IdolStorageEntity> {

    public SittingIdolRender() {
        super(new SittingIdolModel());
        this.addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, IdolStorageEntity animatable) {
                return bone.getName().equalsIgnoreCase("heldItem") ? new ItemStack(Items.NETHERITE_SCRAP) : null;
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, IdolStorageEntity animatable) {
                return ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
            }

            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, IdolStorageEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                poseStack.mulPose(Axis.XP.rotationDegrees(0));
                poseStack.mulPose(Axis.YP.rotationDegrees(0));
                poseStack.mulPose(Axis.ZP.rotationDegrees(0));
                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight,
                        packedOverlay);
            }
        });
    }

    @Override
    public void render(IdolStorageEntity animatable, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockPos entityPos = animatable.getBlockPos();
        int searchRadius = 2;

        BlockPos targetBlockPos = null;
        for (int x = -searchRadius; x <= searchRadius; x++) {
            for (int y = -searchRadius; y <= searchRadius; y++) {
                for (int z = -searchRadius; z <= searchRadius; z++) {
                    BlockPos checkPos = entityPos.offset(x, y, z);
                    var blockState = animatable.getLevel().getBlockState(checkPos);
                    if (blockState.is(GigBlocks.ALIEN_STORAGE_BLOCK_INVIS2.get())) {
                        targetBlockPos = checkPos;
                        break;
                    }
                }
                if (targetBlockPos != null) break;
            }
            if (targetBlockPos != null) break;
        }
        if (targetBlockPos != null) {
            double dx = targetBlockPos.getX() - entityPos.getX();
            double dz = targetBlockPos.getZ() - entityPos.getZ();
            float yaw = (float) (Math.atan2(dz, dx) * (180 / Math.PI)) - 90.0F;
            poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        }
        super.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }
}