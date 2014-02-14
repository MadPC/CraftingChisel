/**
 * StorageBlock
 *
 * @author Garrett Spicer-Davis
 */
package tterrag.craftingChisel.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tterrag.craftingChisel.CraftingChisel;
import tterrag.craftingChisel.tile.TileOmniCraftingTable;

/**
 * @author Garrett Spicer-Davis
 * 
 */
public class OmniCraftingTable extends Block
{
	public IIcon[] icons = new IIcon[4];
	private CreativeTabs tab;

	public OmniCraftingTable()
	{
		super(Material.wood);
		setStepSound(soundTypeWood);
		tab = new CreativeTabs(CreativeTabs.getNextID(), "Crafting Chisel")
		{
			@Override
			public Item getTabIconItem()
			{
				return CraftingChisel.omniCraftingTable.getItem(null, 0, 0, 0);
			}
		};
		setCreativeTab(tab);
	}

	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		icons[0] = register.registerIcon("craftingchisel:craftingTable_top");
		icons[1] = register.registerIcon("craftingchisel:craftingTable_side");
		icons[2] = register.registerIcon("craftingchisel:craftingTable_front");
		icons[3] = register.registerIcon("craftingchisel:craftingTable_bottom");
	}

	@Override
	public IIcon getIcon(IBlockAccess block, int x, int y, int z, int side)
	{
		switch (side)
		{
		case 0:
			return icons[3];
		case 1:
			return icons[0];
		case 2:
			return icons[1];
		case 3:
			return icons[2];
		case 4: 
			return icons[1];
		case 5:
			return icons[2];
		default:
			return icons[3];
		}
	}
	
	public IIcon getIcon(int side)
	{
		return getIcon(null, 0, 0, 0, side);
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public int getRenderBlockPass()
	{
		return 0;
	}

	@Override
	public String getUnlocalizedName()
	{
		return "tterrag.storageBlock";
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float headx, float heady, float headz)
	{
		if (!world.isRemote && player.inventory.getCurrentItem().getItem() != CraftingChisel.chisel) player.openGui(CraftingChisel.instance, 0, world, x, y, z);
		else if (!world.isRemote) reset(world, x, y, z);
		return false;
	}
	
	private void reset(World world, int x, int y, int z)
	{
		int meta = ((TileOmniCraftingTable) world.getTileEntity(x, y, z)).blockMeta;
		world.setBlock(x, y, z, ((TileOmniCraftingTable) world.getTileEntity(x, y, z)).passedBlock);
		world.setBlockMetadataWithNotify(x, y, z, meta, 3);
	}

	@Override
	public int getRenderType()
	{
		return CraftingChisel.renderID;
	}

	@Override
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TileOmniCraftingTable();
	}
	
	public CreativeTabs getTab()
	{
		return tab;
	}
}
