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

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lenni0451.commons.httpclient.executor.extra.ReactorNettyExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import reactor.netty.http.client.HttpClient;

/// Microsoft login responses can carry more than Netty's default 8 KiB of headers (mostly
/// Set-Cookie), which fails cookie imports with a TooLongHttpHeaderException.
/// Raise the response header limit for the HTTP client used by MinecraftAuth.
@Mixin(ReactorNettyExecutor.class)
public class MixinReactorNettyExecutor {
  @Unique
  private static final int MAX_RESPONSE_HEADER_SIZE = 256 * 1024;

  @ModifyReturnValue(method = "buildClient", at = @At("RETURN"))
  private HttpClient allowLargeResponseHeaders(HttpClient original) {
    return original.httpResponseDecoder(spec -> spec.maxHeaderSize(MAX_RESPONSE_HEADER_SIZE));
  }
}
