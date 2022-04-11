package org.penguinencounter.safetyclick.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.penguinencounter.safetyclick.tooltip.ItemTooltipHackyReplacement;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractParentElement implements Drawable {
    @Inject(
            method = "renderTextHoverEffect",
            at = {
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/gui/screen/Screen;renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V"
                    ), @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/gui/screen/Screen;renderOrderedTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V"
            )},
            cancellable = true
    )
    protected void swapTooltipMethod(MatrixStack matrices, @Nullable Style style, int x, int y, CallbackInfo ci) {
        MinecraftClient cli = MinecraftClient.getInstance();
        Screen that = (Screen) (Object) this;
        ItemTooltipHackyReplacement.screenInstance = that;
        ItemTooltipHackyReplacement why = new ItemTooltipHackyReplacement();
        if (style != null && style.getHoverEvent() != null && style.getClickEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ClickEvent clickEvent = style.getClickEvent();
            HoverEvent.ItemStackContent itemStackContent = (HoverEvent.ItemStackContent) hoverEvent.getValue(HoverEvent.Action.SHOW_ITEM);
            if (itemStackContent != null) {
                why.renderTooltip(matrices, itemStackContent.asStack(), clickEvent, x, y);
                ci.cancel();
            } else {
                HoverEvent.EntityContent entityContent = (HoverEvent.EntityContent)hoverEvent.getValue(HoverEvent.Action.SHOW_ENTITY);
                if (entityContent != null) {
                    if (MinecraftClient.getInstance().options.advancedItemTooltips) {
                        why.renderTooltip(matrices, entityContent.asTooltip(), clickEvent, x, y);
                        ci.cancel();
                    }
                } else {
                    Text text = (Text)hoverEvent.getValue(HoverEvent.Action.SHOW_TEXT);
                    if (text != null) {
                        why.renderOrderedTooltip(matrices, cli.textRenderer.wrapLines(text, Math.max(that.width / 2, 200)), clickEvent, x, y);
                        ci.cancel();
                    }
                }
            }
        }
    }
}
