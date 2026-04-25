package ui.panels;

import dao.DashboardDAO;
import ui.components.*;

import javax.swing.*;
import java.awt.*;

public class OrganPanel extends JPanel {
    private DashboardDAO dao;
    private StyledTable table;

    public OrganPanel(DashboardDAO dao) {
        this.dao = dao;
        setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        build();
    }

    private void build() {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel title = new JLabel("Organ Inventory");
        title.setFont(UITheme.fontBold(14));
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        card.add(title, BorderLayout.NORTH);

        table = new StyledTable(new String[]{"Organ ID","Organ Type","Donor ID","Blood Group","Status","Hospital ID","Harvested At"});
        card.add(table.inScrollPane(), BorderLayout.CENTER);

        JPanel infoCard = new JPanel(new BorderLayout());
        infoCard.setBackground(UITheme.BG_CARD2);
        infoCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        JLabel info = new JLabel("ℹ  To add organs, use MySQL Workbench: INSERT INTO organ_inventory (organ_type, donor_id, blood_group, status, hospital_id) VALUES ('Kidney', 1, 'B+', 'AVAILABLE', 1)");
        info.setFont(UITheme.fontBody(11));
        info.setForeground(UITheme.TEXT_MUTED);
        infoCard.add(info, BorderLayout.CENTER);

        add(card, BorderLayout.CENTER);
        add(infoCard, BorderLayout.SOUTH);
    }

    public void refresh() {
        table.clearData();
        for (Object[] o : dao.getAllOrgans()) table.addRow(o);
    }
}