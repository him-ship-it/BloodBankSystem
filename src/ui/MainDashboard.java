package ui;

import dao.DashboardDAO;
import db.DBConnection;
import ui.components.UITheme;
import ui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainDashboard extends JFrame {

    private DashboardDAO dao = new DashboardDAO();
    private JPanel contentArea;
    private CardLayout cardLayout;

    private OverviewPanel    overviewPanel;
    private DonorPanel       donorPanel;
    private RecipientPanel   recipientPanel;
    private InventoryPanel   inventoryPanel;
    private OrganPanel       organPanel;
    private TransfusionPanel transfusionPanel;

    private JLabel statusLabel;

    public MainDashboard() {
        setTitle("Blood Bank & Organ Donor — Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 780);
        setMinimumSize(new Dimension(1100, 660));
        setLocationRelativeTo(null);

        // Dark title bar workaround
        getRootPane().putClientProperty("JRootPane.titleBarBackground", UITheme.BG_DARK);
        getRootPane().putClientProperty("JRootPane.titleBarForeground", UITheme.TEXT_PRIMARY);

        buildUI();
        setVisible(true);
        refreshCurrent();

        // Auto-refresh every 30 seconds
        Timer t = new Timer(30000, e -> refreshCurrent());
        t.start();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BG_DARK);

        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBackground(UITheme.BG_SIDEBAR);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UITheme.BORDER));

        // Logo
        JPanel logo = new JPanel(new BorderLayout());
        logo.setBackground(UITheme.BG_SIDEBAR);
        logo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        JLabel icon = new JLabel("♥");
        icon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 28));
        icon.setForeground(UITheme.ACCENT_RED);
        JLabel title = new JLabel("<html><b>Blood Bank</b><br><span style='color:#7a7a9a;font-size:10px'>Organ Donor System</span></html>");
        title.setFont(UITheme.fontBold(13));
        title.setForeground(UITheme.TEXT_PRIMARY);
        logo.add(icon, BorderLayout.WEST);
        logo.add(title, BorderLayout.CENTER);
        sidebar.add(logo, BorderLayout.NORTH);

        // Nav buttons
        JPanel nav = new JPanel(new GridLayout(6, 1, 0, 4));
        nav.setBackground(UITheme.BG_SIDEBAR);
        nav.setBorder(BorderFactory.createEmptyBorder(16, 12, 16, 12));

        String[][] navItems = {
                {"overview",    "⬛ Overview Dashboard"},
                {"donors",      "👤 Donors"},
                {"recipients",  "🏥 Recipients"},
                {"inventory",   "🩸 Blood Inventory"},
                {"organs",      "🫀 Organ Inventory"},
                {"transfusion", "🔄 Transfusion Centre"},
        };

        ButtonGroup group = new ButtonGroup();
        for (String[] item : navItems) {
            JToggleButton btn = navBtn(item[1]);
            btn.addActionListener(e -> switchPanel(item[0]));
            group.add(btn);
            nav.add(btn);
            if (item[0].equals("overview")) btn.setSelected(true);
        }

        sidebar.add(nav, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(UITheme.BG_SIDEBAR);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        boolean connected = DBConnection.testConnection();
        JLabel dbStatus = new JLabel(connected ? "● DB Connected" : "● DB Disconnected");
        dbStatus.setFont(UITheme.fontBody(11));
        dbStatus.setForeground(connected ? UITheme.ACCENT_GREEN : UITheme.ACCENT_RED);
        footer.add(dbStatus, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("⟳ Refresh");
        refreshBtn.setFont(UITheme.fontBody(10));
        refreshBtn.setBackground(UITheme.BG_CARD2);
        refreshBtn.setForeground(UITheme.TEXT_MUTED);
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> refreshCurrent());
        footer.add(refreshBtn, BorderLayout.EAST);

        sidebar.add(footer, BorderLayout.SOUTH);
        return sidebar;
    }

    private JPanel buildContent() {
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(UITheme.BG_DARK);

        overviewPanel    = new OverviewPanel(dao);
        donorPanel       = new DonorPanel(dao);
        recipientPanel   = new RecipientPanel(dao);
        inventoryPanel   = new InventoryPanel(dao);
        organPanel       = new OrganPanel(dao);
        transfusionPanel = new TransfusionPanel(dao);

        contentArea.add(overviewPanel,    "overview");
        contentArea.add(donorPanel,       "donors");
        contentArea.add(recipientPanel,   "recipients");
        contentArea.add(inventoryPanel,   "inventory");
        contentArea.add(organPanel,       "organs");
        contentArea.add(transfusionPanel, "transfusion");

        return contentArea;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(UITheme.BG_SIDEBAR);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER),
                BorderFactory.createEmptyBorder(6, 20, 6, 20)
        ));
        statusLabel = new JLabel("Ready — data loaded from MySQL");
        statusLabel.setFont(UITheme.fontBody(11));
        statusLabel.setForeground(UITheme.TEXT_MUTED);
        bar.add(statusLabel, BorderLayout.WEST);

        JLabel version = new JLabel("Blood Bank System v1.0");
        version.setFont(UITheme.fontBody(11));
        version.setForeground(UITheme.TEXT_MUTED);
        bar.add(version, BorderLayout.EAST);
        return bar;
    }

    private String currentPanel = "overview";

    private void switchPanel(String name) {
        currentPanel = name;
        cardLayout.show(contentArea, name);
        refreshCurrent();
    }

    private void refreshCurrent() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            protected Void doInBackground() {
                switch (currentPanel) {
                    case "overview"    -> overviewPanel.refresh();
                    case "donors"      -> donorPanel.refresh();
                    case "recipients"  -> recipientPanel.refresh();
                    case "inventory"   -> inventoryPanel.refresh();
                    case "organs"      -> organPanel.refresh();
                    case "transfusion" -> transfusionPanel.refresh();
                }
                return null;
            }
            protected void done() {
                statusLabel.setText("Last refreshed: " + new java.util.Date());
            }
        };
        worker.execute();
    }

    private JToggleButton navBtn(String text) {
        JToggleButton btn = new JToggleButton(text);
        btn.setFont(UITheme.fontBody(12));
        btn.setForeground(UITheme.TEXT_MUTED);
        btn.setBackground(UITheme.BG_SIDEBAR);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.addChangeListener(e -> {
            if (btn.isSelected()) {
                btn.setBackground(new Color(80, 15, 15));
                btn.setForeground(UITheme.ACCENT_RED);
            } else {
                btn.setBackground(UITheme.BG_SIDEBAR);
                btn.setForeground(UITheme.TEXT_MUTED);
            }
        });
        return btn;
    }

    public static void main(String[] args) {
        // Apply dark look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        UIManager.put("Panel.background",        UITheme.BG_DARK);
        UIManager.put("OptionPane.background",   UITheme.BG_CARD);
        UIManager.put("OptionPane.messageForeground", UITheme.TEXT_PRIMARY);
        UIManager.put("Button.background",       UITheme.BG_CARD2);
        UIManager.put("Button.foreground",       UITheme.TEXT_PRIMARY);
        UIManager.put("Label.foreground",        UITheme.TEXT_PRIMARY);
        UIManager.put("ComboBox.background",     UITheme.BG_CARD2);
        UIManager.put("ComboBox.foreground",     UITheme.TEXT_PRIMARY);
        UIManager.put("TextField.background",    UITheme.BG_CARD2);
        UIManager.put("TextField.foreground",    UITheme.TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground", UITheme.TEXT_PRIMARY);
        UIManager.put("ScrollBar.background",    UITheme.BG_DARK);
        UIManager.put("ScrollBar.thumb",         UITheme.BORDER);

        SwingUtilities.invokeLater(MainDashboard::new);
    }
}