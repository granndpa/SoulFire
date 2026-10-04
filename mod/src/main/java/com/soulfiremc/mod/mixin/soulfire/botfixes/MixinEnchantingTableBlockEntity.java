/*
 * SoulFire
 * Copyright (C) 2026  AlexProgrammerDE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.soulfiremc.mod.mixin.soulfire.botfixes;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/// The enchanting table book animation draws from one static [RandomSource] shared by every table.
/// Bots tick their levels on separate threads, so two bots near enchanting tables can use it at
/// the same time, which trips the random's threading check and ends the bot's tick loop
/// ("Accessing LegacyRandomSource from multiple threads").
/// Use the random of the level that is ticking instead, which only its own bot touches.
@Mixin(EnchantingTableBlockEntity.class)
public class MixinEnchantingTableBlockEntity {
  @ModifyExpressionValue(method = "bookAnimationTick",
    at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/entity/EnchantingTableBlockEntity;RANDOM:Lnet/minecraft/util/RandomSource;"))
  private static RandomSource useLevelRandom(RandomSource original, @Local(argsOnly = true) Level level) {
    return level.getRandom();
  }
}
