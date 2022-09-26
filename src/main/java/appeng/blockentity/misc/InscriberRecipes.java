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

package appeng.blockentity.misc;

import java.util.Collection;

import javax.annotation.Nullable;

import com.google.common.collect.Iterables;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import appeng.core.AppEng;
import appeng.recipes.handlers.InscriberRecipe;

/**
 * This class indexes all inscriber recipes to find valid inputs for the top and bottom optional slots. This speeds up
 * checks whether inputs for those two slots are valid.
 */
public final class InscriberRecipes {

    public static final ResourceLocation NAMEPLATE_RECIPE_ID = new ResourceLocation(AppEng.MOD_ID, "nameplate");

    private InscriberRecipes() {
    }

    /**
     * Returns an unmodifiable view of all registered inscriber recipes.
     */
    public static Iterable<InscriberRecipe> getRecipes(Level level) {
        Collection<Recipe<Container>> unfilteredRecipes = level.getRecipeManager().byType(InscriberRecipe.TYPE)
                .values();
        return Iterables.filter(unfilteredRecipes, InscriberRecipe.class);
    }

    @Nullable
    public static InscriberRecipe findRecipe(Level level, ItemStack input, ItemStack plateA, ItemStack plateB,
            boolean supportNamePress) {
        for (InscriberRecipe recipe : getRecipes(level)) {
            // The recipe can be flipped at will
            final boolean matchA = recipe.getTopOptional().test(plateA) && recipe.getBottomOptional().test(plateB);
            final boolean matchB = recipe.getTopOptional().test(plateB) && recipe.getBottomOptional().test(plateA);

            if ((matchA || matchB) && recipe.getMiddleInput().test(input)) {
                return recipe;
            }
        }

        return null;
    }

    /**
     * Checks if there is an inscriber recipe that supports the given combination of top/bottom presses. Both the given
     * combination and the reverse will be searched.
     */
    public static boolean isValidOptionalIngredientCombination(Level level, ItemStack pressA, ItemStack pressB) {
        for (InscriberRecipe recipe : getRecipes(level)) {
            if (recipe.getTopOptional().test(pressA) && recipe.getBottomOptional().test(pressB)
                    || recipe.getTopOptional().test(pressB) && recipe.getBottomOptional().test(pressA)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if there is an inscriber recipe that would use the given item stack as an optional ingredient. Bottom and
     * top can be used interchangeably here, because the inscriber will flip the recipe if needed.
     */
    public static boolean isValidOptionalIngredient(Level level, ItemStack is) {
        for (InscriberRecipe recipe : getRecipes(level)) {
            if (recipe.getTopOptional().test(is) || recipe.getBottomOptional().test(is)) {
                return true;
            }
        }

        return false;
    }

}
