package view.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ActionCellRenderer extends JPanel implements TableCellRenderer {

    public ActionCellRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        setOpaque(true);

        add(createButton("👁"));
        add(createButton("✏"));
        add(createButton("🗑"));
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setMargin(new Insets(2, 6, 2, 6));
        btn.setFocusable(false);
        return btn;
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        setBackground(isSelected
                ? table.getSelectionBackground()
                : Color.WHITE);

        return this;
    }
}

