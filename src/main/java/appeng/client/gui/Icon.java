/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2021, TeamAppliedEnergistics, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.client.gui;

import appeng.client.gui.style.Blitter;

public enum Icon {

    // ROW 0
    REDSTONE_LOW(0, 0),
    REDSTONE_HIGH(16, 0),
    REDSTONE_PULSE(32, 0),
    REDSTONE_IGNORE(48, 0),
    UNUSED_00_04(64, 0),
    UNUSED_00_05(80, 0),
    // CLEAR, STASH
    CLEAR(96, 0),
    ENTER(112, 0),
    // ENCODE
    WHITE_ARROW_DOWN(128, 0),
    UNUSED_00_09(144, 0),
    UNUSED_00_10(160, 0),
    UNUSED_00_11(176, 0),
    UNUSED_00_12(192, 0),
    UNUSED_00_13(208, 0),
    BACKGROUND_PRIMARY_OUTPUT(224, 0),
    BACKGROUND_STORAGE_CELL(240, 0),

    // ROW 1
    VIEW_MODE_STORED(0, 16),
    UNUSED_01_01(16, 16),
    VIEW_MODE_ALL(32, 16),
    VIEW_MODE_CRAFTING(48, 16),
    BLOCKING_MODE_NO(64, 16),
    BLOCKING_MODE_YES(80, 16),
    TRANSPARENT_FACADES_OFF(96, 16),
    TRANSPARENT_FACADES_ON(112, 16),
    TYPE_FILTER_ITEMS(128, 16),
    TYPE_FILTER_FLUIDS(144, 16),
    TYPE_FILTER_ALL(160, 16),
    UNUSED_01_11(176, 16),
    UNUSED_01_12(192, 16),
    UNUSED_01_13(208, 16),
    UNUSED_01_14(224, 16),
    BACKGROUND_ORE(240, 16),

    // ROW 2
    UNUSED_02_00(0, 32),
    UNUSED_02_01(16, 32),
    UNUSED_02_02(32, 32),
    SEARCH_AUTO_FOCUS(48, 32),
    SEARCH_DEFAULT(64, 32),
    SEARCH_JEI(80, 32),
    SEARCH_REI(96, 32),
    SEARCH_AUTO_FOCUS_REMEMBER(112, 32),
    SEARCH_REMEMBER(128, 32),
    UNUSED_02_09(144, 32),
    UNUSED_02_10(160, 32),
    UNUSED_02_11(176, 32),
    UNUSED_02_12(192, 32),
    UNUSED_02_13(208, 32),
    BACKGROUND_PLATE(224, 32),
    BACKGROUND_DUST(240, 32),

    // ROW 3
    ARROW_UP(0, 48),
    ARROW_DOWN(16, 48),
    ARROW_RIGHT(32, 48),
    ARROW_LEFT(48, 48),
    SUBSTITUTION_ENABLED(64, 48),
    STORAGE_FILTER_EXTRACTABLE_ONLY(80, 48),
    STORAGE_FILTER_EXTRACTABLE_NONE(96, 48),
    SUBSTITUTION_DISABLED(112, 48),
    FLUID_SUBSTITUTION_ENABLED(128, 48),
    FLUID_SUBSTITUTION_DISABLED(144, 48),
    FILTER_ON_EXTRACT_ENABLED(160, 48),
    FILTER_ON_EXTRACT_DISABLED(176, 48),
    UNUSED_03_12(192, 48),
    UNUSED_03_13(208, 48),
    BACKGROUND_INGOT(224, 48),
    BACKGROUND_STORAGE_COMPONENT(240, 48),

    // ROW 4
    SORT_BY_NAME(0, 64),
    SORT_BY_AMOUNT(16, 64),
    // WRENCH | PARTITION STORAGE
    WRENCH(32, 64),
    LEVEL_ITEM(48, 64),
    SORT_BY_INVENTORY_TWEAKS(64, 64),
    SORT_BY_MOD(80, 64),
    UNUSED_04_06(96, 64),
    UNUSED_04_07(112, 64),
    UNUSED_04_08(128, 64),
    UNUSED_04_09(144, 64),
    UNUSED_04_10(160, 64),
    UNUSED_04_11(176, 64),
    UNUSED_04_12(192, 64),
    UNUSED_04_13(208, 64),
    BACKGROUND_VIEW_CELL(224, 64),
    BACKGROUND_WIRELESS_TERM(240, 64),

    // ROW 5
    FULLNESS_EMPTY(0, 80),
    FULLNESS_HALF(16, 80),
    FULLNESS_FULL(32, 80),
    LEVEL_ENERGY(48, 80),
    PATTERN_ACCESS_SHOW(64, 80),
    PATTERN_ACCESS_HIDE(80, 80),
    EYE_WHITE(96, 80),
    EYE_BLUE_CENTER(112, 80),
    UNUSED_05_08(128, 80),
    UNUSED_05_09(144, 80),
    UNUSED_05_10(160, 80),
    UNUSED_05_11(176, 80),
    UNUSED_05_12(192, 80),
    UNUSED_05_13(208, 80),
    UNUSED_05_14(224, 80),
    BACKGROUND_TRASH(240, 80),

    // ROW 6
    FUZZY_PERCENT_25(0, 96),
    FUZZY_PERCENT_50(16, 96),
    FUZZY_PERCENT_75(32, 96),
    FUZZY_PERCENT_99(48, 96),
    FUZZY_IGNORE(64, 96),
    FIND_CONTAINED_FLUID(80, 96),
    UNUSED_06_06(96, 96),
    UNUSED_06_07(112, 96),
    UNUSED_06_08(128, 96),
    UNUSED_06_09(144, 96),
    UNUSED_06_10(160, 96),
    UNUSED_06_11(176, 96),
    UNUSED_06_12(192, 96),
    UNUSED_06_13(208, 96),
    UNUSED_06_14(224, 96),
    BACKGROUND_WIRELESS_BOOSTER(240, 96),

    // ROW 7
    CONDENSER_OUTPUT_TRASH(0, 112),
    CONDENSER_OUTPUT_MATTER_BALL(16, 112),
    CONDENSER_OUTPUT_SINGULARITY(32, 112),
    UNUSED_07_03(48, 112),
    UNUSED_07_04(64, 112),
    UNUSED_07_05(80, 112),
    UNUSED_07_06(96, 112),
    UNUSED_07_07(112, 112),
    UNUSED_07_08(128, 112),
    UNUSED_07_09(144, 112),
    UNUSED_07_10(160, 112),
    UNUSED_07_11(176, 112),
    UNUSED_07_12(192, 112),
    UNUSED_07_13(208, 112),
    UNUSED_07_14(224, 112),
    BACKGROUND_ENCODED_PATTERN(240, 112),

    // ROW 8
    INVALID(0, 128),
    VALID(16, 128),
    WHITELIST(32, 128),
    BLACKLIST(48, 128),
    UNUSED_08_04(64, 128),
    UNUSED_08_05(80, 128),
    UNUSED_08_06(96, 128),
    UNUSED_08_07(112, 128),
    UNUSED_08_08(128, 128),
    UNUSED_08_09(144, 128),
    UNUSED_08_10(160, 128),
    UNUSED_08_11(176, 128),
    UNUSED_08_12(192, 128),
    UNUSED_08_13(208, 128),
    UNUSED_08_14(224, 128),
    BACKGROUND_BLANK_PATTERN(240, 128),

    // ROW 9
    ACCESS_WRITE(0, 144),
    ACCESS_READ(16, 144),
    ACCESS_READ_WRITE(32, 144),
    UNUSED_09_03(48, 144),
    UNUSED_09_04(64, 144),
    UNUSED_09_05(80, 144),
    UNUSED_09_06(96, 144),
    UNUSED_09_07(112, 144),
    UNUSED_09_08(128, 144),
    UNUSED_09_09(144, 144),
    UNUSED_09_10(160, 144),
    UNUSED_09_11(176, 144),
    UNUSED_09_12(192, 144),
    UNUSED_09_13(208, 144),
    UNUSED_09_14(224, 144),
    BACKGROUND_CHARGABLE(240, 144),

    // ROW 10
    POWER_UNIT_AE(0, 160),
    POWER_UNIT_EU(16, 160),
    POWER_UNIT_J(32, 160),
    POWER_UNIT_W(48, 160),
    POWER_UNIT_RF(64, 160),
    POWER_UNIT_TR(80, 160),
    UNUSED_10_06(96, 160),
    UNUSED_10_07(112, 160),
    UNUSED_10_08(128, 160),
    UNUSED_10_09(144, 160),
    UNUSED_10_10(160, 160),
    UNUSED_10_11(176, 160),
    UNUSED_10_12(192, 160),
    UNUSED_10_13(208, 160),
    UNUSED_10_14(224, 160),
    BACKGROUND_SINGULARITY(240, 160),

    // ROW 11
    PERMISSION_INJECT(0, 176),
    PERMISSION_EXTRACT(16, 176),
    PERMISSION_CRAFT(32, 176),
    PERMISSION_BUILD(48, 176),
    PERMISSION_SECURITY(64, 176),
    COPY_MODE_ON(80, 176),
    UNUSED_11_06(96, 176),
    UNUSED_11_07(112, 176),
    UNUSED_11_08(128, 176),
    UNUSED_11_09(144, 176),
    UNUSED_11_10(160, 176),
    UNUSED_11_11(176, 176),
    UNUSED_11_12(192, 176),
    UNUSED_11_13(208, 176),
    UNUSED_11_14(224, 176),
    BACKGROUND_SPATIAL_CELL(240, 176),

    // ROW 12
    PERMISSION_INJECT_DISABLED(0, 192),
    PERMISSION_EXTRACT_DISABLED(16, 192),
    PERMISSION_CRAFT_DISABLED(32, 192),
    PERMISSION_BUILD_DISABLED(48, 192),
    PERMISSION_SECURITY_DISABLED(64, 192),
    COPY_MODE_OFF(80, 192),
    UNUSED_12_06(96, 192),
    UNUSED_12_07(112, 192),
    TAB_BUTTON_BACKGROUND_BORDERLESS(128, 192, 25, 22),
    TAB_BUTTON_BACKGROUND(160, 192, 22, 22),
    SLOT_BACKGROUND(192, 192, 18, 18),
    UNUSED_12_14(224, 192),
    BACKGROUND_FUEL(240, 192),

    // ROW 13
    TERMINAL_STYLE_TALL(0, 208),
    TERMINAL_STYLE_SMALL(16, 208),
    TERMINAL_STYLE_FULL(32, 208),
    UNUSED_13_03(48, 208),
    UNUSED_13_04(64, 208),
    UNUSED_13_05(80, 208),
    UNUSED_13_06(96, 208),
    UNUSED_13_07(112, 208),
    UNUSED_13_14(224, 208),
    BACKGROUND_UPGRADE(240, 208),

    // ROW 14
    PLACEMENT_BLOCK(0, 224),
    PLACEMENT_ITEM(16, 224),
    UNUSED_14_02(32, 224),
    UNUSED_14_03(48, 224),
    UNUSED_14_04(64, 224),
    UNUSED_14_05(80, 224),
    UNUSED_14_06(96, 224),
    UNUSED_14_07(112, 224),
    TAB_BUTTON_BACKGROUND_BORDERLESS_FOCUS(128, 224, 25, 22),
    TAB_BUTTON_BACKGROUND_FOCUS(160, 224, 22, 22),
    UNUSED_14_12(192, 224),
    UNUSED_14_13(208, 224),
    UNUSED_14_14(224, 224),
    BACKGROUND_BIOMETRIC_CARD(240, 224),

    SCHEDULING_DEFAULT(0, 240),
    SCHEDULING_ROUND_ROBIN(16, 240),
    SCHEDULING_RANDOM(32, 240),
    OVERLAY_OFF(48, 240),
    OVERLAY_ON(64, 240),
    UNUSED_15_05(80, 240),
    UNUSED_15_06(96, 240),
    UNUSED_15_07(112, 240),
    UNUSED_15_12(192, 240),
    UNUSED_15_13(208, 240),
    UNUSED_15_14(224, 240),
    TOOLBAR_BUTTON_BACKGROUND(240, 240);

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    Icon(int x, int y) {
        this(x, y, 16, 16);
    }

    Icon(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Blitter getBlitter() {
        return Blitter.texture("guis/states.png", 256, 256)
                .src(x, y, width, height);
    }

}
