package com.reclipse.drit.content;

import com.reclipse.drit.DritConfig;
import com.reclipse.drit.DritMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.GrowingPlantBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DritBlock extends Block {
	public static final BooleanProperty POWERED = BooleanProperty.create("powered");
	public static final BooleanProperty WAS_POWERED = BooleanProperty.create("was_powered");

	public DritBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(
				this.defaultBlockState()
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
		if (!pLevel.hasNeighborSignal(pPos)) {
			pLevel.setBlock(pPos, pState.setValue(POWERED, false).setValue(WAS_POWERED, false), 4);
		} else {
			pLevel.setBlock(pPos, pState.setValue(POWERED, true), 4);
		}

		this.grow(pState, pLevel, pPos, pRandom);
	}

	public void grow(@NotNull BlockState pState, ServerLevel pLevel, BlockPos pPos, @NotNull RandomSource pRandom) {
		boolean redstoneTriggered = !pState.getValue(WAS_POWERED) && pState.getValue(POWERED);
		if (DritConfig.shouldRequireRedstone && !redstoneTriggered) {
			return;
		}

		if (DritConfig.shouldRequireRedstone) {
			pLevel.setBlock(pPos, pState.setValue(WAS_POWERED, true), 4);
		}

		BlockPos basePos = pPos.above();
		BlockState baseState = pLevel.getBlockState(basePos);
		if (baseState.isAir() || baseState.getBlock() instanceof LiquidBlock) {
			return;
		}

		BlockPos tipPos = basePos;
		while (true) {
			BlockPos nextPos = tipPos.above();
			if (nextPos.getY() >= pLevel.getMaxBuildHeight()) break;
			if (!isSamePlant(baseState, pLevel.getBlockState(nextPos))) break;
			tipPos = nextPos;
		}

		if (pRandom.nextInt() % DritConfig.growthTickChance == 0) {
			if (DritConfig.randomOrAge) {
				if (pLevel.getBlockState(tipPos).getBlock() instanceof CropBlock block) {
					if (!block.isMaxAge(pLevel.getBlockState(tipPos))) {
						for (int i = 0; i < DritConfig.tickingQuantity; i++)
							pLevel.setBlockAndUpdate(tipPos, block.getStateForAge(block.getAge(pLevel.getBlockState(tipPos)) + 1));
					}
				} else {
					DritMod.LOGGER.info("Not a Crop Block, but config is set to grow by age!");
				}
			} else {
				for (int i = 0; i < DritConfig.tickingQuantity; i++)
					pLevel.getBlockState(tipPos).randomTick(pLevel, tipPos, pRandom);
			}
		}
	}

	private static boolean isSamePlant(BlockState base, BlockState next) {
		if (next.isAir() || next.getBlock() instanceof LiquidBlock) {
			return false;
		}
		Block baseBlock = base.getBlock();
		Block nextBlock = next.getBlock();
		if (baseBlock == nextBlock) {
			return true;
		}
		return baseBlock instanceof GrowingPlantBlock && nextBlock instanceof GrowingPlantBlock;
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!DritConfig.shouldRequireRedstone) {
			this.grow(level.getBlockState(pos), level, pos, random);
		}
	}
}
