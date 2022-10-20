package mods.cybercat.gigeresque.common.entity.ai.pathing;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;

public class CrawlerNavigation extends GroundPathNavigation {
	private BlockPos targetPos;

	public CrawlerNavigation(Mob mobEntity, Level world) {
		super(mobEntity, world);
	}

	public Path findPathTo(BlockPos target, int distance) {
		this.targetPos = target;
		return super.createPath(target, distance);
	}

	public Path findPathTo(Entity entity, int distance) {
		this.targetPos = entity.blockPosition();
		return super.createPath(entity, distance);
	}

	public boolean startMovingTo(Entity entity, double speed) {
		Path path = this.findPathTo((Entity) entity, 0);
		if (path != null) {
			return this.moveTo(path, speed);
		} else {
			this.targetPos = entity.blockPosition();
			this.speedModifier = speed;
			return true;
		}
	}

	public void tick() {
		if (!this.isDone()) {
			super.tick();
		} else {
			if (this.targetPos != null) {
				if (!this.targetPos.closerToCenterThan(this.mob.position(),
						Math.max((double) this.mob.getBbWidth(), 1.0D))
						&& (!(this.mob.getY() > (double) this.targetPos.getY())
								|| !(new BlockPos((double) this.targetPos.getX(), this.mob.getY(),
										(double) this.targetPos.getZ())).closerToCenterThan(this.mob.position(),
												Math.max((double) this.mob.getBbWidth(), 1.0D)))) {
					this.mob.getMoveControl().setWantedPosition((double) this.targetPos.getX(), (double) this.targetPos.getY(),
							(double) this.targetPos.getZ(), this.speedModifier);
				} else {
					this.targetPos = null;
				}
			}

		}
	}

}
