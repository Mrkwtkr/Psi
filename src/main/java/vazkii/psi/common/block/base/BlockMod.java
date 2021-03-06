/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [09/01/2016, 22:43:48 (GMT)]
 */
package vazkii.psi.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.psi.common.core.PsiCreativeTab;
import vazkii.psi.common.item.base.ItemModBlock;

public class BlockMod extends Block implements IPsiBlock {

	private final String[] variants;
	private final String bareName;

	public BlockMod(String name, Material materialIn, String... variants) {
		super(materialIn);

		if(variants.length == 0)
			variants = new String[] { name };

		bareName = name;
		this.variants = variants;

		setUnlocalizedName(name);
		setCreativeTab(PsiCreativeTab.INSTANCE);
	}

	@Override
	public Block setUnlocalizedName(String name) {
		super.setUnlocalizedName(name);
		setRegistryName(name);
		GameRegistry.registerBlock(this, ItemModBlock.class);
		return this;
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public String getBareName() {
		return bareName;
	}

	@Override
	public String[] getVariants() {
		return variants;
	}

	@Override
	public ItemMeshDefinition getCustomMeshDefinition() {
		return null;
	}

	@Override
	public EnumRarity getBlockRarity(ItemStack stack) {
		return EnumRarity.COMMON;
	}

	@Override
	public IProperty[] getIgnoredProperties() {
		return new IProperty[0];
	}

	@Override
	public Class getVariantEnum() {
		return null;
	}

}
