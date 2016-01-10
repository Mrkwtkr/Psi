/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [09/01/2016, 17:04:30 (GMT)]
 */
package vazkii.psi.common.item;

import java.util.List;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.client.core.handler.ModelHandler;
import vazkii.psi.common.core.helper.ItemNBTHelper;
import vazkii.psi.common.item.base.ItemMod;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibItemNames;

public class ItemCAD extends ItemMod implements ICAD {

	private static final String TAG_COMPONENT_PREFIX = "component";
	
	public ItemCAD() {
		super(LibItemNames.CAD);
		setMaxStackSize(1);
	}
	
	public static void setComponent(ItemStack stack, ItemStack componentStack) {
		if(componentStack != null && componentStack.getItem() instanceof ICADComponent) {
			ICADComponent component = (ICADComponent) componentStack.getItem();
			EnumCADComponent componentType = component.getComponentType(componentStack);
			String name = TAG_COMPONENT_PREFIX + componentType.name();
			
			NBTTagCompound cmp = new NBTTagCompound();
			componentStack.writeToNBT(cmp);
			ItemNBTHelper.setCompound(stack, name, cmp);
		}
	}
	
	public static ItemStack makeCAD(ItemStack... components) {
		ItemStack stack = new ItemStack(ModItems.cad);
		for(ItemStack component : components)
			setComponent(stack, component);
		return stack;
	}

	@Override
	public ItemStack getComponentInSlot(ItemStack stack, EnumCADComponent type) {
		String name = TAG_COMPONENT_PREFIX + type.name();
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, name, true);
		
		if(cmp == null)
			return null;
		
		return ItemStack.loadItemStackFromNBT(cmp);
	}

	@Override
	public int getStatValue(ItemStack stack, EnumCADStat stat) {
		ItemStack componentStack = getComponentInSlot(stack, stat.getSourceType());
		if(componentStack != null && componentStack.getItem() instanceof ICADComponent) {
			ICADComponent component = (ICADComponent) componentStack.getItem();
			return component.getCADStatValue(componentStack, stat);
		}

		return 0;
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		// Basic Iron CAD
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssembly, 1, 0)));
		
		// Iron CAD
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssembly, 1, 0), 
				new ItemStack(ModItems.cadCore, 1, 0), 
				new ItemStack(ModItems.cadSocket, 1, 0), 
				new ItemStack(ModItems.cadBattery, 1, 0)));		

		
		// Gold CAD
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssembly, 1, 1), 
				new ItemStack(ModItems.cadCore, 1, 0), 
				new ItemStack(ModItems.cadSocket, 1, 0), 
				new ItemStack(ModItems.cadBattery, 1, 0)));	
		
		// Psimetal CAD
		// TODO Make better
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssembly, 1, 2), 
				new ItemStack(ModItems.cadCore, 1, 0), 
				new ItemStack(ModItems.cadSocket, 1, 0), 
				new ItemStack(ModItems.cadBattery, 1, 0)));	
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltipIfShift(playerIn, tooltip, () -> {
			for(EnumCADComponent componentType : EnumCADComponent.class.getEnumConstants()) {
				ItemStack componentStack = getComponentInSlot(stack, componentType);
				String name = "psimisc.none";
				if(componentStack != null)
					name = componentStack.getDisplayName();
				
				name = local(name);
				String line = EnumChatFormatting.GREEN + local(componentType.getName()) + EnumChatFormatting.GRAY + ": " + name;
				addToTooltip(tooltip, line);
				
				line = "";
				for(EnumCADStat stat : EnumCADStat.class.getEnumConstants()) {
					if(stat.getSourceType() == componentType) {
						String shrt = stat.getName();
						int statVal = getStatValue(stack, stat);
						line = " " + EnumChatFormatting.AQUA + local(shrt) + EnumChatFormatting.GRAY + ": " + statVal;
						if(!line.isEmpty())
							addToTooltip(tooltip, line);
					}
				}
				

			}
		});
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ItemMeshDefinition getCustomMeshDefinition() {
		return (ItemStack stack) -> {
			ICAD cad = (ICAD) stack.getItem();
			ItemStack assembly = cad.getComponentInSlot(stack, EnumCADComponent.ASSEMBLY);
			ModelResourceLocation loc = ModelHandler.getModelLocation(assembly); 
			return loc;
		};
	}

}