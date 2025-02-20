/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.client.compat.rei;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.fluid.FluidStack;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.util.ClientEntryStacks;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.fluid.FluidSupportProvider;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import reborncore.api.blockentity.IUpgradeable;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiBuilder;
import reborncore.client.gui.GuiSprites;
import reborncore.client.gui.config.GuiTab;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RecipeManager;
import reborncore.common.fluid.container.ItemFluidInfo;
import techreborn.TechReborn;
import techreborn.recipe.recipes.FluidGeneratorRecipe;
import techreborn.recipe.recipes.FluidReplicatorRecipe;
import techreborn.recipe.recipes.RollingMachineRecipe;
import techreborn.client.compat.rei.fluidgenerator.FluidGeneratorRecipeCategory;
import techreborn.client.compat.rei.fluidgenerator.FluidGeneratorRecipeDisplay;
import techreborn.client.compat.rei.fluidreplicator.FluidReplicatorRecipeCategory;
import techreborn.client.compat.rei.fluidreplicator.FluidReplicatorRecipeDisplay;
import techreborn.client.compat.rei.machine.*;
import techreborn.client.compat.rei.rollingmachine.RollingMachineCategory;
import techreborn.client.compat.rei.rollingmachine.RollingMachineDisplay;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;
import techreborn.init.TRContent.Machine;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static reborncore.client.gui.GuiSprites.drawSprite;

public class ReiPlugin implements REIClientPlugin {
	public static final Map<RecipeType<?>, ItemConvertible> iconMap = new HashMap<>();

	public ReiPlugin() {
		iconMap.put(ModRecipes.ALLOY_SMELTER, Machine.ALLOY_SMELTER);
		iconMap.put(ModRecipes.ASSEMBLING_MACHINE, Machine.ASSEMBLY_MACHINE);
		iconMap.put(ModRecipes.BLAST_FURNACE, Machine.INDUSTRIAL_BLAST_FURNACE);
		iconMap.put(ModRecipes.CENTRIFUGE, Machine.INDUSTRIAL_CENTRIFUGE);
		iconMap.put(ModRecipes.CHEMICAL_REACTOR, Machine.CHEMICAL_REACTOR);
		iconMap.put(ModRecipes.COMPRESSOR, Machine.COMPRESSOR);
		iconMap.put(ModRecipes.DISTILLATION_TOWER, Machine.DISTILLATION_TOWER);
		iconMap.put(ModRecipes.EXTRACTOR, Machine.EXTRACTOR);
		iconMap.put(ModRecipes.FLUID_REPLICATOR, Machine.FLUID_REPLICATOR);
		iconMap.put(ModRecipes.FUSION_REACTOR, Machine.FUSION_CONTROL_COMPUTER);
		iconMap.put(ModRecipes.GRINDER, Machine.GRINDER);
		iconMap.put(ModRecipes.IMPLOSION_COMPRESSOR, Machine.IMPLOSION_COMPRESSOR);
		iconMap.put(ModRecipes.INDUSTRIAL_ELECTROLYZER, Machine.INDUSTRIAL_ELECTROLYZER);
		iconMap.put(ModRecipes.INDUSTRIAL_GRINDER, Machine.INDUSTRIAL_GRINDER);
		iconMap.put(ModRecipes.INDUSTRIAL_SAWMILL, Machine.INDUSTRIAL_SAWMILL);
		iconMap.put(ModRecipes.ROLLING_MACHINE, Machine.ROLLING_MACHINE);
		iconMap.put(ModRecipes.SCRAPBOX, TRContent.SCRAP_BOX);
		iconMap.put(ModRecipes.SOLID_CANNING_MACHINE, Machine.SOLID_CANNING_MACHINE);
		iconMap.put(ModRecipes.VACUUM_FREEZER, Machine.VACUUM_FREEZER);
		iconMap.put(ModRecipes.WIRE_MILL, Machine.WIRE_MILL);
	}

	@Override
	public void registerCategories(CategoryRegistry registry) {
		registry.add(new TwoInputsCenterOutputCategory<>(ModRecipes.ALLOY_SMELTER));
		registry.add(new AssemblingMachineCategory<>(ModRecipes.ASSEMBLING_MACHINE));
		registry.add(new BlastFurnaceCategory<>(ModRecipes.BLAST_FURNACE));
		registry.add(new IndustrialCentrifugeCategory<>(ModRecipes.CENTRIFUGE));
		registry.add(new TwoInputsCenterOutputCategory<>(ModRecipes.CHEMICAL_REACTOR));
		registry.add(new OneInputOneOutputCategory<>(ModRecipes.COMPRESSOR));
		registry.add(new DistillationTowerCategory<>(ModRecipes.DISTILLATION_TOWER));
		registry.add(new OneInputOneOutputCategory<>(ModRecipes.EXTRACTOR));
		registry.add(new FluidReplicatorRecipeCategory(ModRecipes.FLUID_REPLICATOR));
		registry.add(new TwoInputsCenterOutputCategory<>(ModRecipes.FUSION_REACTOR));
		registry.add(new OneInputOneOutputCategory<>(ModRecipes.GRINDER));
		registry.add(new ImplosionCompressorCategory<>(ModRecipes.IMPLOSION_COMPRESSOR));
		registry.add(new ElectrolyzerCategory<>(ModRecipes.INDUSTRIAL_ELECTROLYZER));
		registry.add(new GrinderCategory<>(ModRecipes.INDUSTRIAL_GRINDER));
		registry.add(new SawmillCategory<>(ModRecipes.INDUSTRIAL_SAWMILL));
		registry.add(new RollingMachineCategory(ModRecipes.ROLLING_MACHINE));
		registry.add(new OneInputOneOutputCategory<>(ModRecipes.SCRAPBOX));
		registry.add(new TwoInputsCenterOutputCategory<>(ModRecipes.SOLID_CANNING_MACHINE));
		registry.add(new OneInputOneOutputCategory<>(ModRecipes.VACUUM_FREEZER));
		registry.add(new OneInputOneOutputCategory<>(ModRecipes.WIRE_MILL));

		registry.add(new FluidGeneratorRecipeCategory(Machine.THERMAL_GENERATOR));
		registry.add(new FluidGeneratorRecipeCategory(Machine.GAS_TURBINE));
		registry.add(new FluidGeneratorRecipeCategory(Machine.DIESEL_GENERATOR));
		registry.add(new FluidGeneratorRecipeCategory(Machine.SEMI_FLUID_GENERATOR));
		registry.add(new FluidGeneratorRecipeCategory(Machine.PLASMA_GENERATOR));

		addWorkstations(ModRecipes.ALLOY_SMELTER, EntryStacks.of(Machine.ALLOY_SMELTER), EntryStacks.of(Machine.IRON_ALLOY_FURNACE));
		addWorkstations(ModRecipes.ASSEMBLING_MACHINE, EntryStacks.of(Machine.ASSEMBLY_MACHINE));
		addWorkstations(ModRecipes.BLAST_FURNACE, EntryStacks.of(Machine.INDUSTRIAL_BLAST_FURNACE));
		addWorkstations(ModRecipes.CENTRIFUGE, EntryStacks.of(Machine.INDUSTRIAL_CENTRIFUGE));
		addWorkstations(ModRecipes.CHEMICAL_REACTOR, EntryStacks.of(Machine.CHEMICAL_REACTOR));
		addWorkstations(ModRecipes.COMPRESSOR, EntryStacks.of(Machine.COMPRESSOR));
		addWorkstations(ModRecipes.DISTILLATION_TOWER, EntryStacks.of(Machine.DISTILLATION_TOWER));
		addWorkstations(ModRecipes.EXTRACTOR, EntryStacks.of(Machine.EXTRACTOR));
		addWorkstations(ModRecipes.FLUID_REPLICATOR, EntryStacks.of(Machine.FLUID_REPLICATOR));
		addWorkstations(ModRecipes.FUSION_REACTOR, EntryStacks.of(Machine.FUSION_CONTROL_COMPUTER));
		addWorkstations(ModRecipes.GRINDER, EntryStacks.of(Machine.GRINDER));
		addWorkstations(ModRecipes.IMPLOSION_COMPRESSOR, EntryStacks.of(Machine.IMPLOSION_COMPRESSOR));
		addWorkstations(ModRecipes.INDUSTRIAL_ELECTROLYZER, EntryStacks.of(Machine.INDUSTRIAL_ELECTROLYZER));
		addWorkstations(ModRecipes.INDUSTRIAL_GRINDER, EntryStacks.of(Machine.INDUSTRIAL_GRINDER));
		addWorkstations(ModRecipes.INDUSTRIAL_SAWMILL, EntryStacks.of(Machine.INDUSTRIAL_SAWMILL));
		addWorkstations(ModRecipes.ROLLING_MACHINE, EntryStacks.of(Machine.ROLLING_MACHINE));
		addWorkstations(ModRecipes.SOLID_CANNING_MACHINE, EntryStacks.of(Machine.SOLID_CANNING_MACHINE));
		addWorkstations(ModRecipes.VACUUM_FREEZER, EntryStacks.of(Machine.VACUUM_FREEZER));
		addWorkstations(ModRecipes.WIRE_MILL, EntryStacks.of(Machine.WIRE_MILL));
		registry.addWorkstations(CategoryIdentifier.of(TechReborn.MOD_ID, Machine.THERMAL_GENERATOR.name), EntryStacks.of(Machine.THERMAL_GENERATOR));
		registry.addWorkstations(CategoryIdentifier.of(TechReborn.MOD_ID, Machine.GAS_TURBINE.name), EntryStacks.of(Machine.GAS_TURBINE));
		registry.addWorkstations(CategoryIdentifier.of(TechReborn.MOD_ID, Machine.DIESEL_GENERATOR.name), EntryStacks.of(Machine.DIESEL_GENERATOR));
		registry.addWorkstations(CategoryIdentifier.of(TechReborn.MOD_ID, Machine.SEMI_FLUID_GENERATOR.name), EntryStacks.of(Machine.SEMI_FLUID_GENERATOR));
		registry.addWorkstations(CategoryIdentifier.of(TechReborn.MOD_ID, Machine.PLASMA_GENERATOR.name), EntryStacks.of(Machine.PLASMA_GENERATOR));
	}

	private void addWorkstations(Identifier identifier, EntryStack<?>... stacks) {
		CategoryRegistry.getInstance().addWorkstations(CategoryIdentifier.of(identifier), stacks);
	}

	private void addWorkstations(RecipeType<?> type, EntryStack<?>... stacks) {
		CategoryRegistry.getInstance().addWorkstations(CategoryIdentifier.of(getTypeId(type)), stacks);
	}

	private static Identifier getTypeId(RecipeType<?> type) {
		return Objects.requireNonNull(Registries.RECIPE_TYPE.getId(type));
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		final Map<RecipeType<FluidGeneratorRecipe>, Machine> fluidGenRecipes = Map.of(
			ModRecipes.THERMAL_GENERATOR, Machine.THERMAL_GENERATOR,
			ModRecipes.GAS_GENERATOR, Machine.GAS_TURBINE,
			ModRecipes.DIESEL_GENERATOR, Machine.DIESEL_GENERATOR,
			ModRecipes.SEMI_FLUID_GENERATOR, Machine.SEMI_FLUID_GENERATOR,
			ModRecipes.PLASMA_GENERATOR, Machine.PLASMA_GENERATOR
		);

		RecipeManager.getRecipeTypes("techreborn")
			.stream()
			.filter(recipeType -> !fluidGenRecipes.containsKey(recipeType))
			.forEach(rebornRecipeType -> registerMachineRecipe(registry, rebornRecipeType));

		fluidGenRecipes.forEach((recipeType, machine) -> registerFluidGeneratorDisplays(registry, recipeType, machine));
	}

	@Override
	public void registerFluidSupport(FluidSupportProvider support) {
		support.register(stack -> {
			ItemStack itemStack = stack.getValue();
			if (itemStack.getItem() instanceof ItemFluidInfo) {
				Fluid fluid = ((ItemFluidInfo) itemStack.getItem()).getFluid(itemStack);
				if (fluid != null)
					return CompoundEventResult.interruptTrue(Stream.of(EntryStacks.of(fluid)));
			}
			return CompoundEventResult.pass();
		});
	}

	@Override
	public void registerItemComparators(ItemComparatorRegistry registry) {
		registry.registerComponents(TRContent.CELL);
	}

	private void registerFluidGeneratorDisplays(DisplayRegistry registry, RecipeType<FluidGeneratorRecipe> generator, Machine machine) {
		Identifier identifier = Identifier.of(TechReborn.MOD_ID, machine.name);
		registry.registerRecipeFiller(FluidGeneratorRecipe.class, recipeType -> recipeType == generator, recipe -> new FluidGeneratorRecipeDisplay(recipe.value(), identifier));
	}

	private void registerMachineRecipe(DisplayRegistry registry, RecipeType<?> recipeType) {
		if (recipeType == ModRecipes.RECYCLER) {
			return;
		}

		Function<RecipeEntry<RebornRecipe>, Display> recipeDisplay = MachineRecipeDisplay::new;

		if (recipeType == ModRecipes.ROLLING_MACHINE) {
			recipeDisplay = r -> {
				RollingMachineRecipe rollingMachineRecipe = (RollingMachineRecipe) r.value();
				return new RollingMachineDisplay(new RecipeEntry<>(getTypeId(recipeType), rollingMachineRecipe.getShapedRecipe()));
			};
		}

		if (recipeType == ModRecipes.FLUID_REPLICATOR) {
			recipeDisplay = r -> {
				FluidReplicatorRecipe recipe = (FluidReplicatorRecipe) r.value();
				return new FluidReplicatorRecipeDisplay(new RecipeEntry<>(getTypeId(recipeType), recipe));
			};
		}

		registry.registerRecipeFiller(RebornRecipe.class,
			recipeType1 -> true,
			recipeEntry -> recipeEntry.value().getType() == recipeType,
			recipeDisplay
		);
	}

	@Override
	public void registerScreens(ScreenRegistry registry) {
		ExclusionZones exclusionZones = registry.exclusionZones();
		exclusionZones.register(GuiBase.class, guiBase -> {
			int height = 0;
			if (guiBase.tryAddUpgrades() && guiBase.be instanceof IUpgradeable upgradeable) {
				if (upgradeable.canBeUpgraded()) {
					height = 80;
				}
			}
			for (GuiTab slot : (List<GuiTab>) guiBase.getTabs()) {
				if (slot.enabled()) {
					height += 24;
				}
			}
			if (height > 0) {
				int width = 20;
				return Collections.singletonList(new Rectangle(guiBase.getGuiLeft() - width, guiBase.getGuiTop() + 8, width, height));
			}
			return Collections.emptyList();
		});
	}

	@Override
	public void registerExclusionZones(ExclusionZones zones) {
		zones.register(GuiBase.class, new SlotConfigExclusionZones());
	}

	public static Widget createProgressBar(int x, int y, double animationDuration, GuiBuilder.ProgressDirection direction) {
		return Widgets.createDrawableWidget((drawContext, mouseX, mouseY, delta) -> {
			drawSprite(drawContext, direction.baseSprite, x, y);
			int j = (int) ((System.currentTimeMillis() / animationDuration) % 1.0 * 16.0);
			if (j < 0) {
				j = 0;
			}

			switch (direction) {
				case RIGHT -> drawContext.drawTexture(GuiBuilder.GUI_ELEMENTS, x, y, direction.xActive, direction.yActive, j, 10);
				case LEFT -> drawContext.drawTexture(GuiBuilder.GUI_ELEMENTS, x + 16 - j, y, direction.xActive + 16 - j, direction.yActive, j, 10);
				case UP -> drawContext.drawTexture(GuiBuilder.GUI_ELEMENTS, x, y + 16 - j, direction.xActive, direction.yActive + 16 - j, 10, j);
				case DOWN -> drawContext.drawTexture(GuiBuilder.GUI_ELEMENTS, x, y, direction.xActive, direction.yActive, 10, j);
			}
		});
	}

	public static Widget createEnergyDisplay(Rectangle bounds, double energy, EntryAnimation animation, Function<TooltipContext, Tooltip> tooltipBuilder) {
		return Widgets.createSlot(bounds).entry(
			ClientEntryStacks.of(new EnergyEntryRenderer(animation, tooltipBuilder))
		).notFavoritesInteractable();
	}

	public static Widget createFluidDisplay(Rectangle bounds, EntryStack<FluidStack> fluid, EntryAnimation animation) {
		EntryStack<FluidStack> copy = fluid.copy();
		copy.withRenderer(new FluidStackRenderer(animation, copy.getRenderer()));
		return Widgets.createSlot(bounds).entry(copy);
	}

	private record EnergyEntryRenderer(EntryAnimation animation, Function<TooltipContext, Tooltip> tooltipBuilder) implements Renderer {
		@Override
			public void render(DrawContext drawContext, Rectangle bounds, int mouseX, int mouseY, float delta) {
				int width = bounds.width + 2;
				int height = bounds.height + 2;
				int innerHeight = height - 2;

				drawSprite(drawContext, GuiSprites.POWER_BAR_BASE, bounds.x - 1, bounds.y - 1);
				int innerDisplayHeight;
				if (animation.animationType != EntryAnimationType.NONE) {
					innerDisplayHeight = MathHelper.ceil((System.currentTimeMillis() / (animation.duration / (float) innerHeight) % innerHeight));
					if (animation.animationType == EntryAnimationType.DOWNWARDS)
						innerDisplayHeight = innerHeight - innerDisplayHeight;
				} else innerDisplayHeight = innerHeight;
				drawSprite(drawContext, GuiSprites.POWER_BAR_OVERLAY, bounds.x, bounds.y + innerHeight - innerDisplayHeight, width - 2, innerDisplayHeight);
			}

			@Override
			@Nullable
			public Tooltip getTooltip(TooltipContext context) {
				return this.tooltipBuilder.apply(context);
			}
		}

	private record FluidStackRenderer(EntryAnimation animation, EntryRenderer<FluidStack> parent) implements EntryRenderer<FluidStack> {
		@Override
			public void render(EntryStack<FluidStack> entry, DrawContext drawContext, Rectangle bounds, int mouseX, int mouseY, float delta) {
				int width = bounds.width;
				int height = bounds.height;

				drawSprite(drawContext, GuiSprites.TANK_BACKGROUND, bounds.x - 4, bounds.y - 4);
				int innerDisplayHeight;
				if (animation.animationType != EntryAnimationType.NONE) {
					innerDisplayHeight = MathHelper.ceil((System.currentTimeMillis() / (animation.duration / (float) height) % height));
					if (animation.animationType == EntryAnimationType.DOWNWARDS)
						innerDisplayHeight = height - innerDisplayHeight;
				} else innerDisplayHeight = height;
				drawFluid(drawContext, entry.getValue().getFluid(), innerDisplayHeight, bounds.x, bounds.y, width, height);
				drawSprite(drawContext, GuiSprites.TANK_FOREGROUND, bounds.x - 1, bounds.y - 1);
			}

			public void drawFluid(DrawContext drawContext, Fluid fluid, int drawHeight, int x, int y, int width, int height) {
				y += height;

				FluidRenderHandler handler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);

				// If registry can't find it, don't render.
				if (handler == null) {
					return;
				}

				final Sprite sprite = handler.getFluidSprites(MinecraftClient.getInstance().world, BlockPos.ORIGIN, fluid.getDefaultState())[0];
				int color = FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidColor(MinecraftClient.getInstance().world, BlockPos.ORIGIN, fluid.getDefaultState());

				final int iconHeight = sprite.getContents().getHeight();
				int offsetHeight = drawHeight;

				drawContext.setShaderColor((color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, 1F);

				int iteration = 0;
				while (offsetHeight != 0) {
					final int curHeight = Math.min(offsetHeight, iconHeight);

					drawContext.drawSprite(x, y - offsetHeight, 0, width, curHeight, sprite);
					offsetHeight -= curHeight;
					iteration++;
					if (iteration > 50) {
						break;
					}
				}
				drawContext.setShaderColor(1, 1, 1, 1);
			}

			@Override
			@Nullable
			public Tooltip getTooltip(EntryStack<FluidStack> entry, TooltipContext context) {
				return parent.getTooltip(entry, context);
			}
		}

	public record EntryAnimation(EntryAnimationType animationType, long duration) {
		public static EntryAnimation upwards(long duration) {
			return new EntryAnimation(EntryAnimationType.UPWARDS, duration);
		}

		public static EntryAnimation downwards(long duration) {
			return new EntryAnimation(EntryAnimationType.DOWNWARDS, duration);
		}

		public static EntryAnimation none() {
			return new EntryAnimation(EntryAnimationType.NONE, 0);
		}
	}

	public enum EntryAnimationType {
		UPWARDS,
		DOWNWARDS,
		NONE
	}
}
