/**
 * StorageBlock
 *
 * @author Garrett Spicer-Davis
 */
package tterrag.craftingChisel.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
	private Block drop;

	public OmniCraftingTable()
	{
		super(Material.rock);
		setStepSound(soundTypeWood);
		setHardness(0.6f);
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
		return "tterrag.omniCraftingTable";
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float headx, float heady, float headz)
	{		
		if (!world.isRemote && (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() != CraftingChisel.chisel) || player.inventory.getCurrentItem() == null) 
		{
			player.openGui(CraftingChisel.instance, 0, world, x, y, z);
		}
		else if (!world.isRemote)
		{
			reset(world, x, y, z);
		}
		return true;
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
	
	public void setStats(World world, int x, int y, int z)
	{
		this.stepSound = ((TileOmniCraftingTable) world.getTileEntity(x, y, z)).passedBlock.stepSound;
		this.setHardness(((TileOmniCraftingTable) world.getTileEntity(x, y, z)).passedBlock.getBlockHardness(world, x, y, z));
		this.drop = ((TileOmniCraftingTable) world.getTileEntity(x, y, z)).passedBlock;
		
		/* Dirty reflection lies below, unsucessful. */
		/*
		Material mat = null;
		Field f = null;
		try
		{
			f = Block.class.getDeclaredField("blockMaterial");
			f.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField( "modifiers" );
            modifiersField.setAccessible( true );
            modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL );
            mat = (Material) f.get(world.getBlock(x, y, z));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		try
		{
			f.set(world.getBlock(x, y, z), mat);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		*/
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TileOmniCraftingTable();
	}
	
	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
	{
		this.setStats(world, x, y, z);
		super.onBlockClicked(world, x, y, z, player);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		return drop.getDrops(world, x, y, z, metadata, fortune);
	}
}

