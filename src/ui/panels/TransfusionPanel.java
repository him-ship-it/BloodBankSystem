package ui.panels;

import dao.DashboardDAO;
import ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TransfusionPanel extends JPanel {
    private DashboardDAO dao;
    private StyledTable donorRef, recipRef, logTable;
    private JTextField fDonorId, fRecipId, fHospId, fUnits;
    private JComboBox<String> fBG;

    public TransfusionPanel(DashboardDAO dao) {
        this.dao = dao;
        setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        build();
    }

    private void build() {
        JSplitPane top = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildForm(), buildRefs());
        top.setDividerLocation(320);
        top.setBorder(null);
        top.setBackground(UITheme.BG_DARK);

        JPanel logCard = card("Transaction History");
        logTable = new StyledTable(new String[]{"#","Type","Donor","Recipient","Hospital","Blood","Units","Date","Notes"});
        logCard.add(logTable.inScrollPane(), BorderLayout.CENTER);

        JButton delBtn = btn("Delete Selected Log", UITheme.ACCENT_RED);
        delBtn.addActionListener(e -> deleteLog());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.setOpaque(false);
        south.add(delBtn);
        logCard.add(south, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(logCard, BorderLayout.CENTER);
    }

    private JPanel buildForm() {
        JPanel card = card("Perform Blood Transfusion");
        card.setPreferredSize(new Dimension(300, 260));

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 10));
        form.setOpaque(false);

        fDonorId = field(); fRecipId = field(); fHospId = field(); fUnits = field();
        fBG = combo(new String[]{"A+","A-","B+","B-","AB+","AB-","O+","O-"});

        form.add(lf("Donor ID",    fDonorId));
        form.add(lf("Recipient ID",fRecipId));
        form.add(lf("Hospital ID", fHospId));
        form.add(lc("Blood Group", fBG));
        form.add(lf("Units",       fUnits));
        card.add(form, BorderLayout.CENTER);

        JButton go = btn("Perform Transfusion ▶", UITheme.ACCENT_RED);
        go.addActionListener(e -> perform());
        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.setBorder(BorderFactory.createEmptyBorder(12,0,0,0));
        south.add(go);
        card.add(south, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildRefs() {
        JPanel wrap = new JPanel(new GridLayout(1, 2, 12, 0));
        wrap.setOpaque(false);

        JPanel dc = card("Available Donors");
        donorRef = new StyledTable(new String[]{"ID","Name","Blood","City"});
        dc.add(donorRef.inScrollPane(), BorderLayout.CENTER);

        JPanel rc = card("Recipients");
        recipRef = new StyledTable(new String[]{"ID","Name","Blood","Urgency"});
        rc.add(recipRef.inScrollPane(), BorderLayout.CENTER);

        wrap.add(dc); wrap.add(rc);
        return wrap;
    }

    private void perform() {
        try {
            int donorId = Integer.parseInt(fDonorId.getText().trim());
            int recipId = Integer.parseInt(fRecipId.getText().trim());
            int hospId  = Integer.parseInt(fHospId.getText().trim());
            String bg   = fBG.getSelectedItem().toString();
            int units   = Integer.parseInt(fUnits.getText().trim());

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Transfuse " + units + " units of " + bg + " from Donor #" + donorId + " to Recipient #" + recipId + "?",
                    "Confirm Transfusion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean ok = dao.performTransfusion(donorId, recipId, hospId, bg, units);
                JOptionPane.showMessageDialog(this,
                        ok ? "Transfusion completed successfully!\n" + units + " units of " + bg + " transferred."
                                : "Transfusion failed!\nInsufficient blood stock or invalid IDs.",
                        ok ? "✔ Success" : "✘ Failed",
                        ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                if (ok) { clearForm(); refresh(); }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,"All ID and Unit fields must be numbers.","Error",JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteLog() {
        int id = logTable.getSelectedId();
        if (id < 0) { JOptionPane.showMessageDialog(this,"Select a log entry.","No Selection",JOptionPane.WARNING_MESSAGE); return; }
        int c = JOptionPane.showConfirmDialog(this,"Delete Log #" + id + "?","Confirm",JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            boolean ok = dao.deleteTransaction(id);
            JOptionPane.showMessageDialog(this, ok ? "Log deleted." : "Failed.",
                    ok?"Done":"Error", ok?JOptionPane.INFORMATION_MESSAGE:JOptionPane.ERROR_MESSAGE);
            if (ok) refresh();
        }
    }

    public void refresh() {
        // Donor reference table
        donorRef.clearData();
        for (Object[] d : dao.getAllDonors())
            donorRef.addRow(new Object[]{ d[0], d[1], d[3], d[5] });

        // Recipient reference table
        recipRef.clearData();
        for (Object[] r : dao.getAllRecipients())
            recipRef.addRow(new Object[]{ r[0], r[1], r[3], r[7] });

        // Transaction log
        logTable.clearData();
        for (Object[] t : dao.getAllTransactions())
            logTable.addRow(new Object[]{ t[0], t[1], t[2], t[3], t[4], t[5], t[7], t[8], t[9] });
    }

    private void clearForm() {
        fDonorId.setText(""); fRecipId.setText(""); fHospId.setText(""); fUnits.setText("");
        fBG.setSelectedIndex(0);
    }

    private JPanel card(String title) {
        JPanel p = new JPanel(new BorderLayout(0,12));
        p.setBackground(UITheme.BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                BorderFactory.createEmptyBorder(16,16,16,16)));
        JLabel lbl = new JLabel(title);
        lbl.setFont(UITheme.fontBold(13)); lbl.setForeground(UITheme.TEXT_PRIMARY);
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
        b.setFont(UITheme.fontBold(12)); b.setBorder(BorderFactory.createEmptyBorder(10,18,10,18));
        b.setFocusPainted(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}