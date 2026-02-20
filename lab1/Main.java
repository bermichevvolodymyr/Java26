package lab1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {

    // Оголошення компонентів GUI
    private JTextField lat1Field, lon1Field, lat2Field, lon2Field;
    private JLabel resultLabel;
    private JButton solveButton, clearButton;

    public Main() {
        // Налаштування вікна
        setTitle("Distance Calculator");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 10, 10)); // Використовуємо сітку для розміщення

        // Створення компонентів для Точки 1
        add(new JLabel("Latitude 1 (degrees):"));
        lat1Field = new JTextField();
        add(lat1Field);

        add(new JLabel("Longitude 1 (degrees):"));
        lon1Field = new JTextField();
        add(lon1Field);

        // Створення компонентів для Точки 2
        add(new JLabel("Latitude 2 (degrees):"));
        lat2Field = new JTextField();
        add(lat2Field);

        add(new JLabel("Longitude 2 (degrees):"));
        lon2Field = new JTextField();
        add(lon2Field);

        // Кнопки
        solveButton = new JButton("Solve");
        clearButton = new JButton("Clear");
        add(solveButton);
        add(clearButton);

        // Поле для виводу результату
        add(new JLabel("Distance (meters):"));
        resultLabel = new JLabel("0.0");
        add(resultLabel);

        // Додавання логіки до кнопок
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateDistance();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
    }

    private void calculateDistance() {
        try {
            // Зчитування координат (в градусах) [cite: 7, 11]
            double lat1 = Double.parseDouble(lat1Field.getText());
            double lon1 = Double.parseDouble(lon1Field.getText());
            double lat2 = Double.parseDouble(lat2Field.getText());
            double lon2 = Double.parseDouble(lon2Field.getText());

            // Радіус Землі в метрах (R = 6371e3)
            double R = 6371000;

            // Конвертація в радіани
            double phi1 = Math.toRadians(lat1);
            double phi2 = Math.toRadians(lat2);

            // Різниця координат в радіанах [cite: 18, 19]
            double deltaPhi = Math.toRadians(lat2 - lat1);
            double deltaLambda = Math.toRadians(lon2 - lon1);

            // Формула гаверсинуса
            // a = sin²(Δφ/2) + cos(φ1) * cos(φ2) * sin²(Δλ/2) [cite: 20]
            double a = Math.pow(Math.sin(deltaPhi / 2), 2) +
                    Math.cos(phi1) * Math.cos(phi2) *
                            Math.pow(Math.sin(deltaLambda / 2), 2);

            // c = 2 * atan2(√a, √(1-a))
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            // D = R * c
            double distance = R * c;

            // Вивід результату
            resultLabel.setText(String.format("%.2f m", distance));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        lat1Field.setText("");
        lon1Field.setText("");
        lat2Field.setText("");
        lon2Field.setText("");
        resultLabel.setText("0.0");
    }

    public static void main(String[] args) {
        // Запуск GUI в потоці обробки подій
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}