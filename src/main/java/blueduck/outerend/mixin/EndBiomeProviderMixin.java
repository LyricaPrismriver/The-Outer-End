package blueduck.outerend.mixin;

import blueduck.outerend.mixin_code.MixinHelpers;
import blueduck.outerend.registry.BiomeRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.EndBiomeProvider;
import net.minecraft.world.gen.SimplexNoiseGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EndBiomeProvider.class)
public abstract class EndBiomeProviderMixin {
	@Shadow @Final private Biome theEndBiome;
	
	@Shadow @Final private SimplexNoiseGenerator generator;
	@Shadow @Final private Biome endHighlandsBiome;
	@Shadow @Final private Biome endMidlandsBiome;
	@Shadow @Final private Biome smallEndIslandsBiome;
	@Shadow @Final private Biome endBarrensBiome;
	
	@Shadow @Final private Registry<Biome> lookupRegistry;
	
	/**
	 * @author OutEnd Team
	 */
	@Overwrite
	//TODO
	public Biome getNoiseBiome(int x, int y, int z) {
		int i = x >> 2;
		int j = z >> 2;
		if ((long) i * (long) i + (long) j * (long) j <= 4096L) {
			return this.theEndBiome;
		} else {
			float f = EndBiomeProvider.getRandomNoise(this.generator, i * 2 + 1, j * 2 + 1);
			if (f > 40.0F) {
				return MixinHelpers.getBiome(i,j,this.endHighlandsBiome,lookupRegistry);
			} else if (f >= 0.0F) {
				return MixinHelpers.getBiome(i,j,this.endMidlandsBiome,lookupRegistry);
			} else {
				return f < -20.0F ? this.smallEndIslandsBiome : this.endBarrensBiome;
			}
		}
	}
}