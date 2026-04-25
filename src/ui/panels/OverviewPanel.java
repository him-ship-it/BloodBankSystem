package ui.panels;

import dao.DashboardDAO;
import model.DashboardStats;
import ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class OverviewPanel extends JPanel {
    private DashboardDAO dao;

    private StatCard cardDonors, cardRecipients, cardUnits, cardTx, cardOrgan, cardCritical;
    private BloodBarPanel bloodBar;
    private StyledTable recentTxTable, criticalTable, organDonorTable;

    public OverviewPanel(DashboardDAO dao) {
        this.dao = dao;
        setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        build();
    }

    private void build() {
        add(buildStatsRow(), BorderLayout.NORTH);
        add(buildMiddle(), BorderLayout.CENTER);
    }

    private JPanel buildStatsRow() {
        JPanel p = new JPanel(new GridLayout(1, 6, 14, 0));
        p.setOpaque(false);

        cardDonors    = new StatCard("Total Donors",      "0", "Registered",        UITheme.ACCENT_RED);
        cardRecipients= new StatCard("Recipients",        "0", "Awaiting care",      UITheme.ACCENT_GREEN);
        cardUnits     = new StatCard("Blood Units",       "0", "Total in stock",     UITheme.ACCENT_BLUE);
        cardTx        = new StatCard("Transactions",      "0", "Total performed",    UITheme.ACCENT_AMBER);
        cardOrgan     = new StatCard("Organ Donors",      "0", "Registered",         UITheme.ACCENT_PURPLE);
        cardCritical  = new StatCard("Critical/High",     "0", "Need urgent care",   UITheme.ACCENT_RED);

        p.add(cardDonors); p.add(cardRecipients); p.add(cardUnits);
        p.add(cardTx); p.add(cardOrgan); p.add(cardCritical);
        return p;
    }

    private JPanel buildMiddle() {
        JPanel p = new JPanel(new GridLayout(1, 3, 14, 0));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

        // Blood inventory bar
        JPanel bloodCard = makeCard("Blood Group Stock");
        bloodBar = new BloodBarPanel();
        bloodCard.add(bloodBar, BorderLayout.CENTER);
        p.add(bloodCard);

        // Recent transactions
        JPanel txCard = makeCard("Recent Transactions");
        recentTxTable = new StyledTable(new String[]{"#", "Type", "Blood", "Units", "Notes"});
        txCard.add(recentTxTable.inScrollPane(), BorderLayout.CENTER);
        p.add(txCard);

        // Critical recipients
        JPanel critCard = makeCard("Critical / High Recipients");
        criticalTable = new StyledTable(new String[]{"#", "Name", "Blood", "Organ", "Urgency"});
        critCard.add(criticalTable.inScrollPane(), BorderLayout.CENTER);
        p.add(critCard);

        return p;
    }

    private JPanel makeCard(String title) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        JLabel lbl = new JLabel(title);
        lbl.setFont(UITheme.fontBold(13));
        lbl.setForeground(UITheme.TEXT_PRIMARY);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        card.add(lbl, BorderLayout.NORTH);
        return card;
    }

    public void refresh() {
        DashboardStats stats = dao.getStats();
        cardDonors.setValue(String.valueOf(stats.totalDonors));
        cardRecipients.setValue(String.valueOf(stats.totalRecipients));
        cardUnits.setValue(String.valueOf(stats.totalBloodUnits));
        cardTx.setValue(String.valueOf(stats.totalTransactions));
        cardOrgan.setValue(String.valueOf(stats.organDonors));
        cardCritical.setValue(String.valueOf(stats.criticalRecipients));

        // Blood inventory bar
        Map<String, Integer> inv = dao.getBloodInventory();
        bloodBar.setInventory(inv);

        // Recent transactions (last 8)
        recentTxTable.clearData();
        List<Object[]> txs = dao.getAllTransactions();
        for (int i = 0; i < Math.min(8, txs.size()); i++) {
            Object[] t = txs.get(i);
            String type = t[1].toString().equals("BLOOD_TRANSFUSION") ? "Blood" : "Organ";
            recentTxTable.addRow(new Object[]{ t[0], type, t[5], t[7], t[9] });
        }

        // Critical recipients
        criticalTable.clearData();
        List<Object[]> recips = dao.getAllRecipients();
        for (Object[] r : recips) {
            String urgency = r[7].toString();
            if (urgency.equals("CRITICAL") || urgency.equals("HIGH")) {
                criticalTable.addRow(new Object[]{ r[0], r[1], r[3], r[6], r[7] });
            }
        }
    }
}