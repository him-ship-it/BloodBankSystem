package ui.components;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class StyledTable extends JTable {

    public StyledTable(String[] columns, Object[][] data) {
        super(new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        applyStyle();
    }

    public StyledTable(String[] columns) {
        super(new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        applyStyle();
    }

    private void applyStyle() {
        setBackground(UITheme.TABLE_ROW);
        setForeground(UITheme.TEXT_PRIMARY);
        setSelectionBackground(UITheme.TABLE_SEL);
        setSelectionForeground(UITheme.TEXT_PRIMARY);
        setFont(UITheme.fontBody(12));
        setRowHeight(36);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 1));
        setFillsViewportHeight(true);

        // Header
        JTableHeader header = getTableHeader();
        header.setBackground(UITheme.TABLE_HEADER);
        header.setForeground(UITheme.TEXT_MUTED);
        header.setFont(UITheme.fontBold(11));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));
        header.setReorderingAllowed(false);

        // Alternating rows renderer
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                                                           boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setOpaque(true);
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                if (sel) {
                    setBackground(UITheme.TABLE_SEL);
                    setForeground(UITheme.TEXT_PRIMARY);
                } else {
                    setBackground(row % 2 == 0 ? UITheme.TABLE_ROW : UITheme.TABLE_ALT);
                    setForeground(UITheme.TEXT_PRIMARY);
                }
                setFont(UITheme.fontBody(12));
                return this;
            }
        });
    }

    public void setData(Object[][] data) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.setRowCount(0);
        for (Object[] row : data) model.addRow(row);
    }

    public void addRow(Object[] row) {
        ((DefaultTableModel) getModel()).addRow(row);
    }

    public void clearData() {
        ((DefaultTableModel) getModel()).setRowCount(0);
    }

    public int getSelectedId() {
        int row = getSelectedRow();
        if (row < 0) return -1;
        Object val = getValueAt(row, 0);
        try { return Integer.parseInt(val.toString()); }
        catch (Exception e) { return -1; }
    }

    public JScrollPane inScrollPane() {
        JScrollPane sp = new JScrollPane(this);
        sp.setBackground(UITheme.TABLE_ROW);
        sp.getViewport().setBackground(UITheme.TABLE_ROW);
        sp.setBorder(BorderFactory.createEmptyBorder());
        return sp;
    }
}