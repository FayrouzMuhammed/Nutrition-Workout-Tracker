package ui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FoodDBPanel extends JPanel {
    private MainFrame parent;

    private JList<String> foodList;
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JComboBox<String> mealBox = new JComboBox<>(new String[]{"Breakfast", "Lunch", "Dinner"});
    // numeric input for amount (servings); user types a number, no spinner arrows
    private JFormattedTextField amountField = new JFormattedTextField(java.text.NumberFormat.getNumberInstance());

    // shows the unit (piece, g, ml) for the selected food
    private JLabel unitLabel = new JLabel("");

    public FoodDBPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());

        for (FoodItem f : parent.getFoodDB().getList()) {
            listModel.addElement(f.getName());
        }

        foodList = new JList<>(listModel);
        foodList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        foodList.addListSelectionListener(e -> updateUnitLabel());
        add(new JScrollPane(foodList), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.add(new JLabel("Meal"));
        bottom.add(mealBox);
        bottom.add(new JLabel("Amount (servings)"));
        amountField.setColumns(6);
        amountField.setValue(1.0);
        amountField.setToolTipText("Enter number of servings, e.g., 1, 0.5, 2");
        bottom.add(amountField);
        bottom.add(unitLabel);
        JButton btnAdd = new JButton("Add to Meal");
        btnAdd.addActionListener(this::onAdd);
        bottom.add(btnAdd);

        add(bottom, BorderLayout.SOUTH);
    }

    private void updateUnitLabel() {
        String sel = foodList.getSelectedValue();
        if (sel == null) {
            unitLabel.setText("");
            return;
        }
        FoodItem f = parent.getFoodDB().getFoodByName(sel);
        if (f == null) unitLabel.setText("");
        else unitLabel.setText("(" + f.getUnit() + ")");
    }

    private void onAdd(ActionEvent ev) {
        String sel = foodList.getSelectedValue();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Select a food first.");
            return;
        }
        FoodItem f = parent.getFoodDB().getFoodByName(sel);
        double amt;
        Object v = amountField.getValue();
        if (v instanceof Number) {
            amt = ((Number) v).doubleValue();
        } else {
            try {
                amt = Double.parseDouble(amountField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount: must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (amt <= 0) {
            JOptionPane.showMessageDialog(this, "Amount must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String meal = (String) mealBox.getSelectedItem();

        parent.getDailyLog().addFoodToMeal(meal, f, amt);
        // update the daily log UI so the user sees the new totals immediately
        parent.refreshDailyLogPanel();
        // DailyLogFileManager.saveDailyLog(parent.getDailyLog());
        JOptionPane.showMessageDialog(this, "Added " + amt + " x " + f.getName() + " to " + meal);
    }
}