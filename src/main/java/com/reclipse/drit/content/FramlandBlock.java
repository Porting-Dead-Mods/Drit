package com.reclipse.drit.content;

import com.reclipse.drit.DritBlocks;
import com.reclipse.drit.DritConfig;
import com.reclipse.drit.DritMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.FarmlandWaterManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FramlandBlock extends FarmBlock {
	public static final BooleanProperty POWERED = BooleanProperty.create("powered");
	public static final BooleanProperty WAS_POWERED = BooleanProperty.create("was_powered");

	public FramlandBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(
				(BlockState) this.defaultBlockState()
						.setValue(POWERED, false)
						.setValue(WAS_POWERED, false)
		);
	}

	@Override
	public boolean canConnectRedstone(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @Nullable Direction direction) {
		return true;
	}

	@Override
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (!level.isClientSide) {
			level.scheduleTick(pos, this, 1);
		}
		super.onPlace(state, level, pos, oldState, isMoving);
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
		level.scheduleTick(pos, this, 1);
	}


	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(POWERED, WAS_POWERED));
	}

	@Override
	public void tick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
		// Make it so that you have to redstone clock it instead of a general keep always on
		if (!pLevel.hasNeighborSignal(pPos)) {
			pLevel.setBlock(pPos, pState.setValue(POWERED, false).setValue(WAS_POWERED, false), 4);
		} else if (pLevel.hasNeighborSignal(pPos)) {
			pLevel.setBlock(pPos, pState.setValue(POWERED, true), 4);
		}

		if (!this.canSurvive(pState, pLevel, pPos)) {
			turnToDrit(null, pState, pLevel, pPos);
			return;
		}
		this.grow(pState, pLevel, pPos, pRandom);
	}

	public void grow(@NotNull BlockState pState, ServerLevel pLevel, BlockPos pPos, @NotNull RandomSource pRandom) {
		Block blockAbove = pLevel.getBlockState(pPos.above()).getBlock();

		if (!pState.getValue(WAS_POWERED) && pState.getValue(POWERED)) {
			int height = 1;
			pLevel.setBlock(pPos, pState.setValue(WAS_POWERED, true), 4);

			// Check for plants that grow > 1 block high
			while (true) {
				BlockPos currentPos = pPos.above(height);

				// Check if the block above is within the world height limit
				if (currentPos.getY() >= pLevel.getMaxBuildHeight()) break;

				if (pRandom.nextInt() % DritConfig.growthTickChance == 0) {
					if (DritConfig.randomOrAge) {
						if (pLevel.getBlockState(currentPos).getBlock() instanceof CropBlock block) {
							if (!block.isMaxAge(pLevel.getBlockState(currentPos))) {
								for (int i = 0; i < DritConfig.tickingQuantity; i++)
									pLevel.setBlockAndUpdate(currentPos, block.getStateForAge(block.getAge(pLevel.getBlockState(currentPos)) + 1));
							}
						} else {
							DritMod.LOGGER.info("Not a Crop Block, but config is set to grow by age!");
						}
					} else {
						for (int i = 0; i < DritConfig.tickingQuantity; i++)
							pLevel.getBlockState(currentPos).randomTick(pLevel, currentPos, pRandom);
					}
				}

				// Check if the block above is the same to continue
				Block nextBlock = pLevel.getBlockState(currentPos).getBlock();
				if (nextBlock.getClass().equals(blockAbove.getClass())) break;

				height++;
			}
		} else {
//			DritMod.LOGGER.info("Not powered");
		}
	}

	public static void turnToDrit(@Nullable Entity entity, BlockState state, Level level, BlockPos pos) {
		BlockState blockstate = pushEntitiesUp(state, DritBlocks.Drit.get().defaultBlockState(), level, pos);
		level.setBlockAndUpdate(pos, blockstate);
		level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, blockstate));
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		int i = (Integer)state.getValue(MOISTURE);
		if (!isNearWater(level, pos) && !level.isRainingAt(pos.above())) {
			if (i > 0) {
				level.setBlock(pos, (BlockState)state.setValue(MOISTURE, i - 1), 2);
			} else if (!shouldMaintainFarmland(level, pos)) {
				turnToDrit((Entity)null, state, level, pos);
			}
		} else if (i < 7) {
			level.setBlock(pos, (BlockState)state.setValue(MOISTURE, 7), 2);
		}

	}

	private static boolean shouldMaintainFarmland(BlockGetter level, BlockPos pos) {
		return level.getBlockState(pos.above()).is(BlockTags.MAINTAINS_FARMLAND);
	}

	private static boolean isNearWater(LevelReader level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);

		for(BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4))) {
			if (state.canBeHydrated(level, pos, level.getFluidState(blockpos), blockpos)) {
				return true;
			}
		}

		return FarmlandWaterManager.hasBlockWaterTicket(level, pos);
	}
}
