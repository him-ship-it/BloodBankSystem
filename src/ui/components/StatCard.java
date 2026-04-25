package ui.components;

import javax.swing.*;
import java.awt.*;

public class StatCard extends JPanel {
    private JLabel valueLabel;
    private JLabel titleLabel;
    private JLabel subLabel;
    private Color accentColor;

    public StatCard(String title, String value, String sub, Color accent) {
        this.accentColor = accent;
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JPanel content = new JPanel(new GridLayout(3, 1, 0, 4));
        content.setOpaque(false);

        titleLabel = new JLabel(title.toUpperCase());
        titleLabel.setFont(UITheme.fontBody(10));
        titleLabel.setForeground(UITheme.TEXT_MUTED);

        valueLabel = new JLabel(value);
        valueLabel.setFont(UITheme.fontBold(30));
        valueLabel.setForeground(accent);

        subLabel = new JLabel(sub);
        subLabel.setFont(UITheme.fontBody(11));
        subLabel.setForeground(UITheme.TEXT_MUTED);

        content.add(titleLabel);
        content.add(valueLabel);
        content.add(subLabel);
        add(content, BorderLayout.CENTER);
    }

    public void setValue(String val) {
        valueLabel.setText(val);
        revalidate();
        repaint();
    }

    public void setSub(String sub) {
        subLabel.setText(sub);
    }
}