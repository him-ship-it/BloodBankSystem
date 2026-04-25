package ui.components;

import java.awt.*;

public class UITheme {
    // Dark theme colors
    public static final Color BG_DARK      = new Color(13, 13, 15);
    public static final Color BG_CARD      = new Color(20, 20, 30);
    public static final Color BG_CARD2     = new Color(26, 26, 38);
    public static final Color BG_SIDEBAR   = new Color(16, 16, 24);
    public static final Color ACCENT_RED   = new Color(220, 50, 50);
    public static final Color ACCENT_GREEN = new Color(46, 200, 100);
    public static final Color ACCENT_BLUE  = new Color(52, 152, 219);
    public static final Color ACCENT_AMBER = new Color(230, 160, 20);
    public static final Color ACCENT_PURPLE= new Color(155, 89, 182);
    public static final Color TEXT_PRIMARY = new Color(240, 240, 245);
    public static final Color TEXT_MUTED   = new Color(120, 120, 155);
    public static final Color BORDER       = new Color(40, 40, 60);
    public static final Color TABLE_HEADER = new Color(22, 22, 34);
    public static final Color TABLE_ROW    = new Color(18, 18, 28);
    public static final Color TABLE_ALT    = new Color(22, 22, 34);
    public static final Color TABLE_SEL    = new Color(60, 20, 20);

    // Fonts
    public static Font fontTitle(int size)  { return new Font("Segoe UI", Font.BOLD, size); }
    public static Font fontBody(int size)   { return new Font("Segoe UI", Font.PLAIN, size); }
    public static Font fontMono(int size)   { return new Font("Consolas", Font.PLAIN, size); }
    public static Font fontBold(int size)   { return new Font("Segoe UI", Font.BOLD, size); }
}