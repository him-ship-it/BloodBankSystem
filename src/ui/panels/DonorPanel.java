package ui.panels;

import dao.DashboardDAO;
import ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DonorPanel extends JPanel {
    private DashboardDAO dao;
    private StyledTable table;

    // Form fields
    private JTextField fName, fAge, fContact, fCity;
    private JComboBox<String> fBG, fOrgan;

    public DonorPanel(DashboardDAO dao) {
        this.dao = dao;
        setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout(16, 0));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        build();
    }

    private void build() {
        add(buildForm(), BorderLayout.WEST);
        add(buildTable(), BorderLayout.CENTER);
    }

    private JPanel buildForm() {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(300, 0));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel title = new JLabel("Add New Donor");
        title.setFont(UITheme.fontBold(14));
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        card.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 10));
        form.setOpaque(false);

        fName    = field("Full Name",    "e.g. Rahul Sharma");
        fAge     = field("Age",          "e.g. 25");
        fBG      = combo("Blood Group",  new String[]{"A+","A-","B+","B-","AB+","AB-","O+","O-"});
        fContact = field("Contact",      "e.g. 9876543210");
        fCity    = field("City",         "e.g. Mumbai");
        fOrgan   = combo("Organ Donor?", new String[]{"No", "Yes"});

        form.add(labeledField("Full Name",   fName));
        form.add(labeledField("Age",         fAge));
        form.add(labeledCombo("Blood Group", fBG));
        form.add(labeledField("Contact",     fContact));
        form.add(labeledField("City",        fCity));
        form.add(labeledCombo("Organ Donor?",fOrgan));

        card.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        JButton addBtn   = styledBtn("Register",UITheme.ACCENT_RED);
        JButton clearBtn = styledBtn("Clear",   UITheme.BG_CARD2);

        addBtn.addActionListener(e -> addDonor());
        clearBtn.addActionListener(e -> clearForm());

        btnPanel.add(addBtn); btnPanel.add(clearBtn);
        card.add(btnPanel, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildTable() {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        // Header row
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("All Donors");
        title.setFont(UITheme.fontBold(14));
        title.setForeground(UITheme.TEXT_PRIMARY);
        header.add(title, BorderLayout.WEST);

        JButton delBtn = styledBtn("Delete Selected", UITheme.ACCENT_RED);
        delBtn.addActionListener(e -> deleteDonor());
        header.add(delBtn, BorderLayout.EAST);

        card.add(header, BorderLayout.NORTH);

        table = new StyledTable(new String[]{"ID","Name","Age","Blood Group","Contact","City","Organ Donor","Registered At"});
        card.add(table.inScrollPane(), BorderLayout.CENTER);
        return card;
    }

    private void addDonor() {
        try {
            String name    = fName.getText().trim();
            int    age     = Integer.parseInt(fAge.getText().trim());
            String bg      = fBG.getSelectedItem().toString();
            String contact = fContact.getText().trim();
            String city    = fCity.getText().trim();
            boolean organ  = fOrgan.getSelectedItem().toString().equals("Yes");

            if (name.isEmpty() || city.isEmpty()) {
                showMsg("Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = dao.insertDonor(name, age, bg, contact, city, organ);
            if (ok) {
                showMsg("Donor registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                refresh();
            } else {
                showMsg("Failed to register donor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            showMsg("Age must be a number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteDonor() {
        int id = table.getSelectedId();
        if (id < 0) { showMsg("Please select a donor to delete.", "No Selection", JOptionPane.WARNING_MESSAGE); return; }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Donor #" + id + "?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = dao.deleteDonor(id);
            showMsg(ok ? "Donor deleted successfully." : "Failed to delete. Check foreign key constraints.",
                    ok ? "Deleted" : "Error", ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            if (ok) refresh();
        }
    }

    public void refresh() {
        table.clearData();
        List<Object[]> donors = dao.getAllDonors();
        for (Object[] d : donors) table.addRow(d);
    }

    private void clearForm() {
        fName.setText(""); fAge.setText(""); fContact.setText(""); fCity.setText("");
        fBG.setSelectedIndex(0); fOrgan.setSelectedIndex(0);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    private JTextField field(String name, String placeholder) {
        JTextField f = new JTextField();
        f.setBackground(UITheme.BG_CARD2);
        f.setForeground(UITheme.TEXT_PRIMARY);
        f.setCaretColor(UITheme.TEXT_PRIMARY);
        f.setFont(UITheme.fontBody(12));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return f;
    }

    private JComboBox<String> combo(String name, String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(UITheme.BG_CARD2);
        cb.setForeground(UITheme.TEXT_PRIMARY);
        cb.setFont(UITheme.fontBody(12));
        return cb;
    }

    private JPanel labeledField(String label, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        JLabel l = new JLabel(label.toUpperCase());
        l.setFont(UITheme.fontBody(10));
        l.setForeground(UITheme.TEXT_MUTED);
        p.add(l, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JPanel labeledCombo(String label, JComboBox<String> cb) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        JLabel l = new JLabel(label.toUpperCase());
        l.setFont(UITheme.fontBody(10));
        l.setForeground(UITheme.TEXT_MUTED);
        p.add(l, BorderLayout.NORTH);
        p.add(cb, BorderLayout.CENTER);
        return p;
    }

    private JButton styledBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(UITheme.TEXT_PRIMARY);
        btn.setFont(UITheme.fontBold(12));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showMsg(String msg, String title, int type) {
        JOptionPane.showMessageDialog(this, msg, title, type);
    }
}