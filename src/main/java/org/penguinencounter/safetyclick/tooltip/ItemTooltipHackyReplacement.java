package org.penguinencounter.safetyclick.tooltip;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.penguinencounter.safetyclick.client.SafetyclickClient;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ItemTooltipHackyReplacement {
    public static Screen screenInstance;
    public String makeActionPreview(ClickEvent onClick) {
        StringBuilder sb = new StringBuilder();
        ClickEvent.Action click = onClick.getAction();
        switch (click) {
            case OPEN_URL -> {
                sb.append("Opens a URL: ");
                sb.append(onClick.getValue());
            }
            case OPEN_FILE -> {
                sb.append("Opens a file: ");
                sb.append(onClick.getValue());
            }
            case RUN_COMMAND -> {
                sb.append("Runs: ");
                sb.append(onClick.getValue());
            }
            case SUGGEST_COMMAND -> {
                sb.append("Suggests: ");
                sb.append(onClick.getValue());
            }
            case CHANGE_PAGE -> {
                sb.append("Goes to page: ");
                sb.append(onClick.getValue());
            }
            case COPY_TO_CLIPBOARD -> {
                sb.append("Copies to clipboard: ");
                sb.append(onClick.getValue());
            }
        }
        return sb.toString();
    }
    public void renderTooltip(MatrixStack matrices, ItemStack stack, ClickEvent onClick, int x, int y) {
        List<Text> loreAndStuff = screenInstance.getTooltipFromItem(stack);
        loreAndStuff.add(new LiteralText(makeActionPreview(onClick)));
        SafetyclickClient.LOGGER.debug("a" + loreAndStuff);
        screenInstance.renderTooltip(matrices, loreAndStuff, stack.getTooltipData(), x, y);
    }
    public void renderTooltip(MatrixStack matrices, ClickEvent onClick, int x, int y) {
        this.renderOrderedTooltip(matrices, List.of(), onClick, x, y);
    }
    public void renderTooltip(MatrixStack matrices, Text text, ClickEvent onClick, int x, int y) {
        this.renderOrderedTooltip(matrices, List.of(text.asOrderedText()), onClick, x, y);
    }
    public void renderTooltip(MatrixStack matrices, List<Text> lines, ClickEvent onClick, int x, int y) {
        this.renderOrderedTooltip(matrices, Lists.transform(lines, Text::asOrderedText), onClick, x, y);
    }
    public void renderOrderedTooltip(MatrixStack matrices, List<? extends OrderedText> lines, ClickEvent onClick, int x, int y) {
        ArrayList<OrderedText> reformat1 = new ArrayList<>(lines);
        SafetyclickClient.LOGGER.debug("b / before" + reformat1);
        reformat1.add(new LiteralText(makeActionPreview(onClick)).asOrderedText());
        SafetyclickClient.LOGGER.debug("b / after" + reformat1);
        List<OrderedText> reformat2 = List.copyOf(reformat1);
        screenInstance.renderOrderedTooltip(matrices, reformat2, x, y);
    }
}
