/*
 *  This file is part of ChunkGenLimiter, licensed under the MIT License (MIT).
 *
 *  Copyright (c) 2019 Barteks2x
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package io.github.barteks2x.chunkgenlimiter;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = ChunkGenLimitMod.MODID, name = ChunkGenLimitMod.NAME, version = ChunkGenLimitMod.VERSION)
public class ChunkGenLimitMod
{
    public static final String MODID = "chunkgenlimit";
    public static final String NAME = "Chunk Generation Limiter";
    public static final String VERSION = "1.0";

    @Config(modid = MODID)
    public static class ChunkGenConfig {
        @Config.Comment("Max chunks to generate per tick per dimension")
        public static int chunksPerTick = 2;

        @Config.Comment("Max time to spend generating chunks per tick per dimension")
        public static int maxChunkGenMillis = 5;
    }
}
