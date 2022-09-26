/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 TeamAppliedEnergistics
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package appeng.api.ids;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/**
 * Contains {@link Block} ids for various blocks defined by Ae2.
 */
public final class AEBlockIds {

    ///
    /// WORLDGEN/MISC
    ///
    public static final ResourceLocation QUARTZ_ORE = id("quartz_ore");
    public static final ResourceLocation DEEPSLATE_QUARTZ_ORE = id("deepslate_quartz_ore");
    public static final ResourceLocation SKY_STONE_CHEST = id("sky_stone_chest");
    public static final ResourceLocation SMOOTH_SKY_STONE_CHEST = id("smooth_sky_stone_chest");
    public static final ResourceLocation SKY_COMPASS = id("sky_compass");

    ///
    /// ME NETWORK
    ///
    public static final ResourceLocation INSCRIBER = id("inscriber");
    public static final ResourceLocation SECURITY_STATION = id("security_station");
    public static final ResourceLocation CONTROLLER = id("controller");
    public static final ResourceLocation DRIVE = id("drive");
    public static final ResourceLocation CHEST = id("chest");
    public static final ResourceLocation INTERFACE = id("interface");
    public static final ResourceLocation CELL_WORKBENCH = id("cell_workbench");
    public static final ResourceLocation IO_PORT = id("io_port");
    public static final ResourceLocation QUARTZ_GROWTH_ACCELERATOR = id("quartz_growth_accelerator");
    public static final ResourceLocation CABLE_BUS = id("cable_bus");

    ///
    /// AUTO CRAFTING
    ///
    public static final ResourceLocation CRAFTING_UNIT = id("crafting_unit");
    public static final ResourceLocation CRAFTING_ACCELERATOR = id("crafting_accelerator");
    public static final ResourceLocation CRAFTING_STORAGE_1K = id("1k_crafting_storage");
    public static final ResourceLocation CRAFTING_STORAGE_4K = id("4k_crafting_storage");
    public static final ResourceLocation CRAFTING_STORAGE_16K = id("16k_crafting_storage");
    public static final ResourceLocation CRAFTING_STORAGE_64K = id("64k_crafting_storage");
    public static final ResourceLocation CRAFTING_STORAGE_256K = id("256k_crafting_storage");
    public static final ResourceLocation CRAFTING_MONITOR = id("crafting_monitor");
    public static final ResourceLocation PATTERN_PROVIDER = id("pattern_provider");
    public static final ResourceLocation MOLECULAR_ASSEMBLER = id("molecular_assembler");

    ///
    /// DECORATIVE BLOCKS
    ///
    public static final ResourceLocation QUARTZ_BLOCK = id("quartz_block");
    public static final ResourceLocation QUARTZ_PILLAR = id("quartz_pillar");
    public static final ResourceLocation CHISELED_QUARTZ_BLOCK = id("chiseled_quartz_block");
    public static final ResourceLocation FLUIX_BLOCK = id("fluix_block");
    public static final ResourceLocation SKY_STONE_BLOCK = id("sky_stone_block");
    public static final ResourceLocation SMOOTH_SKY_STONE_BLOCK = id("smooth_sky_stone_block");
    public static final ResourceLocation SKY_STONE_BRICK = id("sky_stone_brick");
    public static final ResourceLocation SKY_STONE_SMALL_BRICK = id("sky_stone_small_brick");
    public static final ResourceLocation QUARTZ_GLASS = id("quartz_glass");
    public static final ResourceLocation QUARTZ_VIBRANT_GLASS = id("quartz_vibrant_glass");

    ///
    /// STAIRS
    ///
    public static final ResourceLocation SKY_STONE_STAIRS = id("sky_stone_stairs");
    public static final ResourceLocation SMOOTH_SKY_STONE_STAIRS = id("smooth_sky_stone_stairs");
    public static final ResourceLocation SKY_STONE_BRICK_STAIRS = id("sky_stone_brick_stairs");
    public static final ResourceLocation SKY_STONE_SMALL_BRICK_STAIRS = id("sky_stone_small_brick_stairs");
    public static final ResourceLocation FLUIX_STAIRS = id("fluix_stairs");
    public static final ResourceLocation QUARTZ_STAIRS = id("quartz_stairs");
    public static final ResourceLocation CHISELED_QUARTZ_STAIRS = id("chiseled_quartz_stairs");
    public static final ResourceLocation QUARTZ_PILLAR_STAIRS = id("quartz_pillar_stairs");

    ///
    /// WALLS
    ///
    public static final ResourceLocation SKY_STONE_WALL = id("sky_stone_wall");
    public static final ResourceLocation SMOOTH_SKY_STONE_WALL = id("smooth_sky_stone_wall");
    public static final ResourceLocation SKY_STONE_BRICK_WALL = id("sky_stone_brick_wall");
    public static final ResourceLocation SKY_STONE_SMALL_BRICK_WALL = id("sky_stone_small_brick_wall");
    public static final ResourceLocation FLUIX_WALL = id("fluix_wall");
    public static final ResourceLocation QUARTZ_WALL = id("quartz_wall");
    public static final ResourceLocation CHISELED_QUARTZ_WALL = id("chiseled_quartz_wall");
    public static final ResourceLocation QUARTZ_PILLAR_WALL = id("quartz_pillar_wall");

    ///
    /// SLABS
    ///
    public static final ResourceLocation SKY_STONE_SLAB = id("sky_stone_slab");
    public static final ResourceLocation SMOOTH_SKY_STONE_SLAB = id("smooth_sky_stone_slab");
    public static final ResourceLocation SKY_STONE_BRICK_SLAB = id("sky_stone_brick_slab");
    public static final ResourceLocation SKY_STONE_SMALL_BRICK_SLAB = id("sky_stone_small_brick_slab");
    public static final ResourceLocation FLUIX_SLAB = id("fluix_slab");
    public static final ResourceLocation QUARTZ_SLAB = id("quartz_slab");
    public static final ResourceLocation CHISELED_QUARTZ_SLAB = id("chiseled_quartz_slab");
    public static final ResourceLocation QUARTZ_PILLAR_SLAB = id("quartz_pillar_slab");

    private static ResourceLocation id(String id) {
        return new ResourceLocation(AEConstants.MOD_ID, id);
    }
}
