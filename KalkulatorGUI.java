import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class KalkulatorGUI extends JFrame implements ActionListener {
    private JTextField display;
    private JPanel panel;

    // Constructor
    public KalkulatorGUI() {
        // Set title, size, and default close operation
        setTitle("Kalkulator");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        
        // Create display field
        display = new JTextField();
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("Arial", Font.BOLD, 24));

        // Create panel and set layout
        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 5, 10, 10)); // 4 rows, 5 columns

        // Array of button labels
        String[] buttonLabels = {
            "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "0", "+", "-",
            "*", "/", "=", "%", "Mod", "Exit" // Added "C" button for clearing
        };

        // Create buttons and add to panel
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.addActionListener(this);
            panel.add(button);
        }

        // Add components to frame
        setLayout(new BorderLayout(10, 10));
        add(display, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        // Make the frame visible
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("Exit")) {
            System.exit(0);
        } else if (command.equals("=")) {
            try {
                // Calculate the result and display it
                double result = evaluateExpression(display.getText());
                display.setText(formatResult(result)); // Format hasil sebelum ditampilkan
            } catch (Exception ex) {
                display.setText("Error");
            }
        } else if (command.equals("Mod")) { // Handle clearing
            String currentText = display.getText();
            if (!currentText.isEmpty()) {
                display.setText(currentText.substring(0, currentText.length() - 1));
            }
        } else {
            // Append button text to display
            display.setText(display.getText() + command);
            // Format display text with commas
            formatDisplayWithCommas();
        }
    }

    // Method to evaluate the expression
    private double evaluateExpression(String expression) throws ScriptException {
        // Replace 'Mod' with '%' for modulus operation
        expression = expression.replace("%", "/100"); // Handling modulus operator
        expression = expression.replace("=", ""); // Ensure '=' is not in expression
        
        // Create a JavaScript engine to evaluate the expression
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

        // Evaluate the expression
        return ((Number) engine.eval(expression)).doubleValue();
    }

    // Method to format the result with commas every three digits
    private String formatResult(double result) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(result);
    }
    
    // Method to format display text with commas
    private void formatDisplayWithCommas() {
        String currentText = display.getText().replace(",", ""); // Remove existing commas
        if (!currentText.isEmpty()) {
            double number = Double.parseDouble(currentText);
            DecimalFormat formatter = new DecimalFormat("#,###");
            display.setText(formatter.format(number));
        }
    }

    public static void main(String[] args) {
        // Run the GUI
        SwingUtilities.invokeLater(() -> new KalkulatorGUI());
    }
}