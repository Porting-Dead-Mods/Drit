package com.reclipse.drit;
 
import com.reclipse.drit.content.DritBlock;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
 
import static com.reclipse.drit.DritBlocks.FramLand;
 
@EventBusSubscriber(modid=DritMod.MODID)
public class DritEvents {
    @SubscribeEvent
    public static void onBlockToolModificationEvent(BlockEvent.BlockToolModificationEvent event) {
        UseOnContext ctx = event.getContext();
        if (ctx.getItemInHand().getItem() instanceof HoeItem && !event.isSimulated() && !ctx.getLevel().isClientSide) {
            if (ctx.getLevel().getBlockState(ctx.getClickedPos()).getBlock() instanceof DritBlock) {
                event.setFinalState(FramLand.get().defaultBlockState());
            }
        }
    }
}
