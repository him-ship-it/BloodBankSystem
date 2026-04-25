package ui.panels;

import dao.DashboardDAO;
import ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RecipientPanel extends JPanel {
    private DashboardDAO dao;
    private StyledTable table;
    private JTextField fName, fAge, fContact, fHospId, fOrganNeed;
    private JComboBox<String> fBG, fUrgency;

    public RecipientPanel(DashboardDAO dao) {
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

        JLabel title = new JLabel("Add New Recipient");
        title.setFont(UITheme.fontBold(14));
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        card.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 10));
        form.setOpaque(false);

        fName      = field();
        fAge       = field();
        fBG        = combo(new String[]{"A+","A-","B+","B-","AB+","AB-","O+","O-"});
        fContact   = field();
        fHospId    = field();
        fOrganNeed = field();
        fUrgency   = combo(new String[]{"LOW","MEDIUM","HIGH","CRITICAL"});

        form.add(lf("Full Name",    fName));
        form.add(lf("Age",          fAge));
        form.add(lc("Blood Group",  fBG));
        form.add(lf("Contact",      fContact));
        form.add(lf("Hospital ID",  fHospId));
        form.add(lf("Organ Needed", fOrganNeed));
        form.add(lc("Urgency",      fUrgency));
        card.add(form, BorderLayout.CENTER);

        JPanel btns = new JPanel(new GridLayout(1, 2, 10, 0));
        btns.setOpaque(false);
        btns.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
        JButton add   = btn("Register", UITheme.ACCENT_GREEN);
        JButton clear = btn("Clear",    UITheme.BG_CARD2);
        add.addActionListener(e -> addRecipient());
        clear.addActionListener(e -> clearForm());
        btns.add(add); btns.add(clear);
        card.add(btns, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildTable() {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("All Recipients");
        title.setFont(UITheme.fontBold(14));
        title.setForeground(UITheme.TEXT_PRIMARY);
        header.add(title, BorderLayout.WEST);

        JButton del = btn("Delete Selected", UITheme.ACCENT_RED);
        del.addActionListener(e -> deleteRecipient());
        header.add(del, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        table = new StyledTable(new String[]{"ID","Name","Age","Blood Group","Contact","Hospital ID","Organ Needed","Urgency","Registered At"});
        card.add(table.inScrollPane(), BorderLayout.CENTER);
        return card;
    }

    private void addRecipient() {
        try {
            String name   = fName.getText().trim();
            int    age    = Integer.parseInt(fAge.getText().trim());
            String bg     = fBG.getSelectedItem().toString();
            String cont   = fContact.getText().trim();
            int    hospId = Integer.parseInt(fHospId.getText().trim());
            String organ  = fOrganNeed.getText().trim().isEmpty() ? "none" : fOrganNeed.getText().trim();
            String urg    = fUrgency.getSelectedItem().toString();

            if (name.isEmpty()) { JOptionPane.showMessageDialog(this,"Fill all fields.","Error",JOptionPane.WARNING_MESSAGE); return; }
            boolean ok = dao.insertRecipient(name, age, bg, cont, hospId, organ, urg);
            JOptionPane.showMessageDialog(this, ok ? "Recipient registered!" : "Failed to register.",
                    ok ? "Success" : "Error", ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            if (ok) { clearForm(); refresh(); }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,"Age and Hospital ID must be numbers.","Error",JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteRecipient() {
        int id = table.getSelectedId();
        if (id < 0) { JOptionPane.showMessageDialog(this,"Select a recipient.","No Selection",JOptionPane.WARNING_MESSAGE); return; }
        int c = JOptionPane.showConfirmDialog(this,"Delete Recipient #" + id + "?","Confirm",JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            boolean ok = dao.deleteRecipient(id);
            JOptionPane.showMessageDialog(this, ok ? "Deleted." : "Failed.",
                    ok?"Done":"Error", ok?JOptionPane.INFORMATION_MESSAGE:JOptionPane.ERROR_MESSAGE);
            if (ok) refresh();
        }
    }

    public void refresh() {
        table.clearData();
        for (Object[] r : dao.getAllRecipients()) table.addRow(r);
    }

    private void clearForm() {
        fName.setText(""); fAge.setText(""); fContact.setText("");
        fHospId.setText(""); fOrganNeed.setText("");
        fBG.setSelectedIndex(0); fUrgency.setSelectedIndex(2);
    }

    private JTextField field() {
        JTextField f = new JTextField();
        f.setBackground(UITheme.BG_CARD2);
        f.setForeground(UITheme.TEXT_PRIMARY);
        f.setCaretColor(UITheme.TEXT_PRIMARY);
        f.setFont(UITheme.fontBody(12));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                BorderFactory.createEmptyBorder(6,10,6,10)));
        return f;
    }

    private JComboBox<String> combo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(UITheme.BG_CARD2);
        cb.setForeground(UITheme.TEXT_PRIMARY);
        cb.setFont(UITheme.fontBody(12));
        return cb;
    }

    private JPanel lf(String label, JTextField f) {
        JPanel p = new JPanel(new BorderLayout(0,4)); p.setOpaque(false);
        JLabel l = new JLabel(label.toUpperCase());
        l.setFont(UITheme.fontBody(10)); l.setForeground(UITheme.TEXT_MUTED);
        p.add(l, BorderLayout.NORTH); p.add(f, BorderLayout.CENTER);
        return p;
    }

    private JPanel lc(String label, JComboBox<String> cb) {
        JPanel p = new JPanel(new BorderLayout(0,4)); p.setOpaque(false);
        JLabel l = new JLabel(label.toUpperCase());
        l.setFont(UITheme.fontBody(10)); l.setForeground(UITheme.TEXT_MUTED);
        p.add(l, BorderLayout.NORTH); p.add(cb, BorderLayout.CENTER);
        return p;
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(UITheme.TEXT_PRIMARY);
        b.setFont(UITheme.fontBold(12));
        b.setBorder(BorderFactory.createEmptyBorder(8,16,8,16));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}