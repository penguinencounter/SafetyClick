package org.penguinencounter.safetyclick.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import org.penguinencounter.safetyclick.tooltip.ItemTooltipHackyReplacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ScreenOverloading {
    @Mixin(ChatScreen.class)
    public abstract static class ChatScreenMixin {
        @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V"))
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
            ItemTooltipHackyReplacement.screenInstance = (ChatScreen) (Object) this;
            ItemTooltipHackyReplacement why = new ItemTooltipHackyReplacement();
            Style style = MinecraftClient.getInstance().inGameHud.getChatHud().getText((double)mouseX, (double)mouseY);
            if (style != null && style.getClickEvent() != null && style.getHoverEvent() == null) {
                why.renderTooltip(matrices, style.getClickEvent(), mouseX, mouseY);
            }
        }
    }
    @Mixin(BookScreen.class)
    public abstract static class BookScreenMixin {
        @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V"))
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
            BookScreen that = (BookScreen) (Object) this;
            ItemTooltipHackyReplacement.screenInstance = that;
            ItemTooltipHackyReplacement why = new ItemTooltipHackyReplacement();
            Style style = that.getTextStyleAt(mouseX, mouseY);
            if (style != null && style.getClickEvent() != null && style.getHoverEvent() == null) {
                why.renderTooltip(matrices, style.getClickEvent(), mouseX, mouseY);
            }
        }
    }
}
