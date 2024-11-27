/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.datagen.recipes.machine.scrapbox

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.registry.RegistryWrapper
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

class ScrapboxRecipesProvider extends TechRebornRecipesProvider {
	static List<String> OUTPUT = [
		"minecraft:jungle_fence_gate",
		"techreborn:steel_dust",
		"minecraft:glowstone_dust",
		"techreborn:raw_silver",
		"techreborn:sapphire_dust",
		"techreborn:coal_small_dust",
		"minecraft:powered_rail",
		"minecraft:experience_bottle",
		"techreborn:zinc_nugget",
		"techreborn:diamond_nugget",
		"minecraft:player_head",
		"minecraft:oak_fence_gate",
		"techreborn:netherrack_dust",
		"minecraft:gold_nugget",
		"minecraft:spruce_fence_gate",
		"minecraft:purple_bed",
		"techreborn:bauxite_small_dust",
		"techreborn:rubber_sapling",
		"minecraft:podzol",
		"techreborn:dark_ashes_dust",
		"minecraft:cooked_chicken",
		"minecraft:apple",
		"minecraft:sand",
		"minecraft:baked_potato",
		"techreborn:diorite_small_dust",
		"minecraft:sunflower",
		"minecraft:slime_ball",
		"minecraft:hopper_minecart",
		"minecraft:acacia_boat",
		"minecraft:oak_sign",
		"techreborn:basalt_dust",
		"techreborn:calcite_small_dust",
		"techreborn:phosphorous_dust",
		"techreborn:platinum_small_dust",
		"minecraft:magenta_bed",
		"techreborn:titanium_dust",
		"minecraft:minecart",
		"techreborn:diorite_dust",
		"minecraft:fishing_rod",
		"techreborn:dark_ashes_small_dust",
		"minecraft:short_grass",
		"minecraft:light_gray_bed",
		"minecraft:spruce_fence",
		"minecraft:oak_leaves",
		"techreborn:chrome_nugget",
		"minecraft:dark_oak_fence_gate",
		"techreborn:magnesium_dust",
		"techreborn:coal_dust",
		"techreborn:yellow_garnet_small_dust",
		"minecraft:iron_nugget",
		"techreborn:charcoal_small_dust",
		"techreborn:copper_nugget",
		"minecraft:pink_bed",
		"techreborn:almandine_small_dust",
		"techreborn:yellow_garnet_gem",
		"techreborn:raw_tin",
		"minecraft:gravel",
		"minecraft:writable_book",
		"minecraft:birch_boat",
		"minecraft:gunpowder",
		"minecraft:brick",
		"techreborn:magnesium_small_dust",
		"minecraft:acacia_sapling",
		"techreborn:diamond_small_dust",
		"techreborn:olivine_small_dust",
		"minecraft:clay",
		"minecraft:cactus",
		"minecraft:chest",
		"techreborn:almandine_dust",
		"techreborn:clay_dust",
		"minecraft:lime_bed",
		"minecraft:shears",
		"techreborn:endstone_small_dust",
		"minecraft:player_head",
		"techreborn:ruby_dust",
		"techreborn:tungstensteel_nugget",
		"techreborn:lead_nugget",
		"minecraft:red_mushroom_block",
		"techreborn:steel_small_dust",
		"techreborn:raw_lead",
		"minecraft:black_bed",
		"techreborn:olivine_dust",
		"minecraft:oak_trapdoor",
		"minecraft:blaze_powder",
		"techreborn:cinnabar_small_dust",
		"techreborn:tungsten_nugget",
		"minecraft:red_bed",
		"techreborn:titanium_small_dust",
		"minecraft:name_tag",
		"minecraft:tnt",
		"techreborn:andesite_dust",
		"minecraft:pumpkin",
		"minecraft:compass",
		"techreborn:electrum_small_dust",
		"techreborn:emerald_dust",
		"minecraft:wooden_sword",
		"techreborn:sphalerite_small_dust",
		"minecraft:glass",
		"minecraft:book",
		"minecraft:oak_sapling",
		"techreborn:pyrite_dust",
		"techreborn:marble_small_dust",
		"minecraft:red_sand",
		"techreborn:grossular_small_dust",
		"minecraft:oak_button",
		"techreborn:redstone_small_dust",
		"techreborn:clay_small_dust",
		"minecraft:blaze_rod",
		"techreborn:basalt_small_dust",
		"minecraft:stick",
		"minecraft:apple",
		"minecraft:acacia_fence",
		"minecraft:dark_oak_boat",
		"minecraft:sugar_cane",
		"minecraft:sugar",
		"minecraft:wooden_hoe",
		"techreborn:ender_pearl_dust",
		"minecraft:green_bed",
		"techreborn:platinum_nugget",
		"techreborn:ender_eye_dust",
		"minecraft:paper",
		"techreborn:andradite_small_dust",
		"techreborn:phosphorous_small_dust",
		"minecraft:orange_bed",
		"minecraft:gray_bed",
		"techreborn:invar_small_dust",
		"techreborn:peridot_dust",
		"techreborn:silver_nugget",
		"techreborn:peridot_gem",
		"minecraft:rabbit_foot",
		"techreborn:red_garnet_gem",
		"techreborn:ender_pearl_small_dust",
		"minecraft:bowl",
		"minecraft:acacia_fence_gate",
		"techreborn:andradite_dust",
		"techreborn:rubber",
		"techreborn:cell",
		"minecraft:jungle_sapling",
		"minecraft:shulker_shell",
		"minecraft:acacia_leaves",
		"techreborn:red_garnet_small_dust",
		"minecraft:wooden_axe",
		"techreborn:zinc_small_dust",
		"minecraft:wooden_shovel",
		"techreborn:zinc_dust",
		"techreborn:charcoal_dust",
		"techreborn:manganese_small_dust",
		"techreborn:sapphire_gem",
		"minecraft:wooden_pickaxe",
		"minecraft:stone_button",
		"minecraft:string",
		"techreborn:saw_small_dust",
		"techreborn:granite_small_dust",
		"techreborn:calcite_dust",
		"minecraft:player_head",
		"minecraft:cooked_beef",
		"minecraft:blue_bed",
		"techreborn:galena_small_dust",
		"techreborn:saw_dust",
		"minecraft:oak_fence",
		"minecraft:diamond",
		"techreborn:sodalite_dust",
		"minecraft:dark_oak_fence",
		"techreborn:nickel_small_dust",
		"techreborn:grossular_dust",
		"techreborn:red_garnet_dust",
		"techreborn:bauxite_dust",
		"techreborn:saltpeter_small_dust",
		"techreborn:sulfur_small_dust",
		"techreborn:nickel_dust",
		"techreborn:endstone_dust",
		"techreborn:flint_small_dust",
		"minecraft:chest_minecart",
		"techreborn:ruby_small_dust",
		"minecraft:crafting_table",
		"minecraft:jungle_fence",
		"techreborn:saltpeter_dust",
		"minecraft:birch_fence_gate",
		"minecraft:spider_eye",
		"minecraft:brown_bed",
		"techreborn:electrum_dust",
		"minecraft:spruce_boat",
		"techreborn:lazurite_dust",
		"techreborn:galena_dust",
		"techreborn:tin_nugget",
		"minecraft:activator_rail",
		"minecraft:red_mushroom",
		"techreborn:sphalerite_dust",
		"minecraft:cyan_bed",
		"techreborn:titanium_nugget",
		"techreborn:chrome_dust",
		"techreborn:refined_iron_nugget",
		"techreborn:ender_eye_small_dust",
		"techreborn:brass_nugget",
		"minecraft:raw_iron",
		"techreborn:pyrite_small_dust",
		"techreborn:manganese_dust",
		"techreborn:tungsten_small_dust",
		"minecraft:saddle",
		"minecraft:spruce_leaves",
		"minecraft:player_head",
		"minecraft:cake",
		"minecraft:yellow_bed",
		"techreborn:pyrope_dust",
		"techreborn:steel_nugget",
		"techreborn:sapphire_small_dust",
		"minecraft:rabbit_hide",
		"minecraft:birch_fence",
		"techreborn:aluminum_nugget",
		"minecraft:glass_pane",
		"techreborn:sap",
		"minecraft:leather_leggings",
		"techreborn:sulfur_dust",
		"minecraft:raw_copper",
		"minecraft:brown_mushroom_block",
		"minecraft:bricks",
		"minecraft:dirt",
		"minecraft:dark_oak_sapling",
		"techreborn:spessartine_dust",
		"techreborn:spessartine_small_dust",
		"techreborn:uvarovite_small_dust",
		"minecraft:raw_gold",
		"techreborn:platinum_dust",
		"techreborn:cell",
		"techreborn:invar_nugget",
		"minecraft:birch_leaves",
		"minecraft:white_bed",
		"techreborn:ashes_dust",
		"minecraft:prismarine_shard",
		"minecraft:bow",
		"techreborn:granite_dust",
		"minecraft:redstone",
		"techreborn:electrum_nugget",
		"techreborn:diamond_dust",
		"techreborn:bronze_dust",
		"techreborn:emerald_small_dust",
		"techreborn:bronze_nugget",
		"techreborn:andesite_small_dust",
		"minecraft:detector_rail",
		"minecraft:light_blue_bed",
		"techreborn:obsidian_small_dust",
		"minecraft:bone",
		"techreborn:peridot_small_dust",
		"minecraft:golden_apple",
		"minecraft:rail",
		"techreborn:netherrack_small_dust",
		"minecraft:glowstone",
		"techreborn:hot_tungstensteel_nugget",
		"techreborn:obsidian_dust",
		"techreborn:cinnabar_dust",
		"minecraft:coal",
		"techreborn:ruby_gem",
		"techreborn:ashes_small_dust",
		"techreborn:glowstone_small_dust",
		"minecraft:birch_sapling",
		"techreborn:nickel_nugget",
		"minecraft:map",
		"techreborn:iridium_nugget",
		"minecraft:brown_mushroom",
		"minecraft:glowstone_dust",
		"minecraft:carrot",
		"techreborn:lazurite_small_dust",
		"techreborn:brass_dust",
		"techreborn:chrome_small_dust",
		"techreborn:uvarovite_dust",
		"minecraft:rotten_flesh",
		"minecraft:jungle_boat",
		"minecraft:nether_brick_fence",
		"minecraft:netherrack",
		"techreborn:sodalite_small_dust",
		"techreborn:yellow_garnet_dust",
		"techreborn:raw_tungsten",
		"techreborn:raw_nickel",
		"minecraft:dark_oak_leaves",
		"minecraft:wheat",
		"techreborn:pyrope_small_dust",
		"techreborn:invar_dust",
		"techreborn:marble_dust",
		"techreborn:aluminum_dust",
		"techreborn:flint_dust",
	]

	ScrapboxRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		OUTPUT.each { outputItem ->
			offerScrapboxRecipe {
				power 10
				time 20
				ingredients TRContent.SCRAP_BOX
				outputs outputItem
			}
		}
	}

	// Read all of the outputs from the scrapbox recipes
	static void main(String[] args) {
		Path dir = Path.of("src/main/resources/data/techreborn/recipes/scrapbox/auto")
		List<String> outputs = new ArrayList<>()
		Files.walk(dir)
			.filter { it.toString().endsWith(".json") }
			.forEach { file ->
				String content = Files.readString(file)
				JsonObject json = new JsonParser().parse(content).getAsJsonObject()
				String value = json.getAsJsonArray("results").get(0).getAsJsonObject().get("item").getAsString()
				outputs.addAll(value)
			}

		println("static List<String> OUTPUT = [")
		outputs.each { println("    \"$it\",") }
		println("]")
	}
}
