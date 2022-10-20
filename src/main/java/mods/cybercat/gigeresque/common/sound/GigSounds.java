package mods.cybercat.gigeresque.common.sound;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class GigSounds implements GigeresqueInitializer {
	private GigSounds() {
	}

	private static GigSounds instance;

	synchronized public static GigSounds getInstance() {
		if (instance == null) {
			instance = new GigSounds();
		}
		return instance;
	}

	public static final SoundEvent ALIEN_HISS = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "alien_hiss"));
	public static final SoundEvent ALIEN_AMBIENT = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "alien_ambient"));
	public static final SoundEvent ALIEN_HURT = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "alien_hurt"));
	public static final SoundEvent ALIEN_DEATH = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "alien_death"));
	public static final SoundEvent ALIEN_DEATH_THUD = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "alien_death_thud"));
	public static final SoundEvent ALIEN_CLAW = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "alien_claw"));
	public static final SoundEvent ALIEN_TAIL = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "alien_tail"));
	public static final SoundEvent ALIEN_HEADBITE = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "alien_headbite"));
	public static final SoundEvent ALIEN_CRUNCH = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "alien_crunch"));
	public static final SoundEvent ALIEN_FOOTSTEP = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "alien_footstep"));
	public static final SoundEvent ALIEN_HANDSTEP = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "alien_handstep"));

	public static final SoundEvent EGG_NOTICE = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "egg_notice"));
	public static final SoundEvent EGG_OPEN = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "egg_open"));

	public static final SoundEvent AQUA_LANDMOVE = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "aqua_landmove"));
	public static final SoundEvent AQUA_LANDCLAW = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "aqua_landclaw"));

	public static final SoundEvent BURSTER_CRAWL = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "burster_crawl"));

	public static final SoundEvent HUGGER_AMBIENT = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "hugger_ambient"));
	public static final SoundEvent HUGGER_DEATH = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "hugger_death"));
	public static final SoundEvent HUGGER_HURT = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "hugger_hurt"));
	public static final SoundEvent HUGGER_IMPLANT = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "hugger_implant"));

	public static final SoundEvent CHESTBURSTING = new SoundEvent(new ResourceLocation(Gigeresque.MOD_ID, "chestbursting"));

	@Override
	public void initialize() {
		register(ALIEN_HISS);
		register(ALIEN_AMBIENT);
		register(ALIEN_HURT);
		register(ALIEN_DEATH);
		register(ALIEN_DEATH_THUD);
		register(ALIEN_CLAW);
		register(ALIEN_TAIL);
		register(ALIEN_HEADBITE);
		register(ALIEN_CRUNCH);
		register(ALIEN_FOOTSTEP);
		register(ALIEN_HANDSTEP);

		register(EGG_NOTICE);
		register(EGG_OPEN);

		register(AQUA_LANDMOVE);
		register(AQUA_LANDCLAW);

		register(BURSTER_CRAWL);

		register(HUGGER_AMBIENT);
		register(HUGGER_DEATH);
		register(HUGGER_HURT);
		register(HUGGER_IMPLANT);
	}

	private void register(SoundEvent soundEvent) {
		Registry.register(Registry.SOUND_EVENT, soundEvent.getLocation(), soundEvent);
	}
}
