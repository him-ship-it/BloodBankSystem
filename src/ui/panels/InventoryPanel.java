package ui.panels;

import dao.DashboardDAO;
import ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class InventoryPanel extends JPanel {
    private DashboardDAO dao;
    private BloodBarPanel barPanel;
    private StyledTable table;
    private JComboBox<String> fAddBG, fDedBG;
    private JTextField fAddUnits, fDedUnits;

    public InventoryPanel(DashboardDAO dao) {
        this.dao = dao;
        setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout(16, 0));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        build();
    }

    private void build() {
        add(buildControls(), BorderLayout.WEST);
        add(buildRight(), BorderLayout.CENTER);
    }

    private JPanel buildControls() {
        JPanel wrap = new JPanel(new GridLayout(2, 1, 0, 16));
        wrap.setPreferredSize(new Dimension(300, 0));
        wrap.setOpaque(false);

        // Add units card
        JPanel addCard = card("Add Blood Units");
        JPanel addForm = new JPanel(new GridLayout(0, 1, 0, 10));
        addForm.setOpaque(false);
        fAddBG    = combo(new String[]{"A+","A-","B+","B-","AB+","AB-","O+","O-"});
        fAddUnits = field();
        addForm.add(lc("Blood Group", fAddBG));
        addForm.add(lf("Units to Add", fAddUnits));
        JButton addBtn = btn("Add Units", UITheme.ACCENT_GREEN);
        addBtn.addActionListener(e -> addUnits());
        addForm.add(addBtn);
        addCard.add(addForm, BorderLayout.CENTER);

        // Deduct units card
        JPanel dedCard = card("Deduct Blood Units");
        JPanel dedForm = new JPanel(new GridLayout(0, 1, 0, 10));
        dedForm.setOpaque(false);
        fDedBG    = combo(new String[]{"A+","A-","B+","B-","AB+","AB-","O+","O-"});
        fDedUnits = field();
        dedForm.add(lc("Blood Group", fDedBG));
        dedForm.add(lf("Units to Deduct", fDedUnits));
        JButton dedBtn = btn("Deduct Units", UITheme.ACCENT_RED);
        dedBtn.addActionListener(e -> deductUnits());
        dedForm.add(dedBtn);
        dedCard.add(dedForm, BorderLayout.CENTER);

        wrap.add(addCard); wrap.add(dedCard);
        return wrap;
    }

    private JPanel buildRight() {
        JPanel wrap = new JPanel(new GridLayout(2, 1, 0, 16));
        wrap.setOpaque(false);

        // Bar chart
        JPanel barCard = card("Blood Group Stock (Visual)");
        barPanel = new BloodBarPanel();
        barCard.add(barPanel, BorderLayout.CENTER);
        wrap.add(barCard);

        // Table
        JPanel tableCard = card("Inventory Table");
        table = new StyledTable(new String[]{"Blood Group", "Units Available"});
        tableCard.add(table.inScrollPane(), BorderLayout.CENTER);
        wrap.add(tableCard);

        return wrap;
    }

    private void addUnits() {
        try {
            String bg = fAddBG.getSelectedItem().toString();
            int units = Integer.parseInt(fAddUnits.getText().trim());
            if (units <= 0) { msg("Units must be > 0."); return; }
            boolean ok = dao.addBloodUnits(bg, units);
            JOptionPane.showMessageDialog(this, ok ? units + " units of " + bg + " added!" : "Failed.",
                    ok ? "Success" : "Error", ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            fAddUnits.setText("");
            if (ok) refresh();
        } catch (NumberFormatException e) { msg("Enter a valid number of units."); }
    }

    private void deductUnits() {
        try {
            String bg = fDedBG.getSelectedItem().toString();
            int units = Integer.parseInt(fDedUnits.getText().trim());
            if (units <= 0) { msg("Units must be > 0."); return; }
            boolean ok = dao.deductBloodUnits(bg, units);
            JOptionPane.showMessageDialog(this, ok ? units + " units of " + bg + " deducted!" : "Insufficient stock!",
                    ok ? "Success" : "Error", ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            fDedUnits.setText("");
            if (ok) refresh();
        } catch (NumberFormatException e) { msg("Enter a valid number of units."); }
    }

    public void refresh() {
        Map<String, Integer> inv = dao.getBloodInventory();
        barPanel.setInventory(inv);
        table.clearData();
        for (Map.Entry<String, Integer> e : inv.entrySet())
            table.addRow(new Object[]{ e.getKey(), e.getValue() });
    }

    private void msg(String m) { JOptionPane.showMessageDialog(this,m,"Error",JOptionPane.WARNING_MESSAGE); }

    private JPanel card(String title) {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(UITheme.BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                BorderFactory.createEmptyBorder(16,16,16,16)));
        JLabel lbl = new JLabel(title);
        lbl.setFont(UITheme.fontBold(13));
        lbl.setForeground(UITheme.TEXT_PRIMARY);
        lbl.setBorder(BorderFactory.createEmptyBorder(0,0,8,0));
        p.add(lbl, BorderLayout.NORTH);
        return p;
    }

    private JTextField field() {
        JTextField f = new JTextField();
        f.setBackground(UITheme.BG_CARD2); f.setForeground(UITheme.TEXT_PRIMARY);
        f.setCaretColor(UITheme.TEXT_PRIMARY); f.setFont(UITheme.fontBody(12));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                BorderFactory.createEmptyBorder(6,10,6,10)));
        return f;
    }

    private JComboBox<String> combo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(UITheme.BG_CARD2); cb.setForeground(UITheme.TEXT_PRIMARY);
        cb.setFont(UITheme.fontBody(12)); return cb;
    }

    private JPanel lf(String label, JTextField f) {
        JPanel p = new JPanel(new BorderLayout(0,4)); p.setOpaque(false);
        JLabel l = new JLabel(label.toUpperCase()); l.setFont(UITheme.fontBody(10)); l.setForeground(UITheme.TEXT_MUTED);
        p.add(l, BorderLayout.NORTH); p.add(f, BorderLayout.CENTER); return p;
    }

    private JPanel lc(String label, JComboBox<String> cb) {
        JPanel p = new JPanel(new BorderLayout(0,4)); p.setOpaque(false);
        JLabel l = new JLabel(label.toUpperCase()); l.setFont(UITheme.fontBody(10)); l.setForeground(UITheme.TEXT_MUTED);
        p.add(l, BorderLayout.NORTH); p.add(cb, BorderLayout.CENTER); return p;
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(UITheme.TEXT_PRIMARY);
        b.setFont(UITheme.fontBold(12)); b.setBorder(BorderFactory.createEmptyBorder(8,16,8,16));
        b.setFocusPainted(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}