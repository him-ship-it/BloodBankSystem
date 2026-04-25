package ui.components;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BloodBarPanel extends JPanel {
    private Map<String, Integer> inventory;

    public BloodBarPanel() {
        setBackground(UITheme.BG_CARD);
        setPreferredSize(new Dimension(300, 280));
    }

    public void setInventory(Map<String, Integer> inv) {
        this.inventory = inv;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inventory == null || inventory.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int maxUnits = inventory.values().stream().mapToInt(Integer::intValue).max().orElse(1);
        if (maxUnits == 0) maxUnits = 1;

        int padding = 16;
        int labelWidth = 36;
        int barHeight = 18;
        int barSpacing = 12;
        int unitWidth = 60;
        int availWidth = getWidth() - padding * 2 - labelWidth - unitWidth;

        int y = padding;
        g2.setFont(UITheme.fontMono(11));

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            String group = entry.getKey();
            int units = entry.getValue();
            float ratio = (float) units / maxUnits;

            // Label
            g2.setColor(UITheme.TEXT_PRIMARY);
            g2.drawString(group, padding, y + barHeight - 3);

            // Background bar
            int barX = padding + labelWidth;
            g2.setColor(UITheme.BG_CARD2);
            g2.fillRoundRect(barX, y, availWidth, barHeight, 6, 6);

            // Filled bar
            int fillWidth = (int) (availWidth * ratio);
            Color barColor = units == 0 ? UITheme.ACCENT_RED
                    : units < 10 ? UITheme.ACCENT_AMBER
                    : UITheme.ACCENT_GREEN;
            if (fillWidth > 0) {
                g2.setColor(barColor);
                g2.fillRoundRect(barX, y, fillWidth, barHeight, 6, 6);
            }

            // Units text
            g2.setColor(UITheme.TEXT_MUTED);
            g2.drawString(units + " u", barX + availWidth + 8, y + barHeight - 3);

            y += barHeight + barSpacing;
        }
    }
}