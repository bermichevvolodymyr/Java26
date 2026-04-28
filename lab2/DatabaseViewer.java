package lab2;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DatabaseViewer extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnLoad;
    private JLabel lblStatus;

    // Параметри підключення до БД
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ukraine_cities";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public DatabaseViewer() {
        setTitle("Перегляд таблиці БД MySQL");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Створюємо компоненти
        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        // Панель з кнопкою
        JPanel topPanel = new JPanel();
        btnLoad = new JButton("Завантажити дані");
        btnLoad.addActionListener(e -> loadData());
        topPanel.add(btnLoad);

        // Створюємо таблицю
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);

        // Статус бар
        lblStatus = new JLabel("Натисніть кнопку для завантаження даних");
        lblStatus.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Додаємо компоненти до вікна
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(lblStatus, BorderLayout.SOUTH);
    }

    private void loadData() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Підключаємось до БД
            lblStatus.setText("Підключення до бази даних...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Виконуємо запит
            lblStatus.setText("Завантаження даних...");
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM ua");

            // Отримуємо метадані
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Очищаємо таблицю
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            // Додаємо колонки
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            // Додаємо рядки
            int rowCount = 0;
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                tableModel.addRow(row);
                rowCount++;
            }

            lblStatus.setText("Завантажено записів: " + rowCount);

        } catch (ClassNotFoundException e) {
            lblStatus.setText("Помилка: Драйвер MySQL не знайдено!");
            JOptionPane.showMessageDialog(this,
                    "Драйвер MySQL не знайдено!\n" + e.getMessage(),
                    "Помилка", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            lblStatus.setText("Помилка підключення до БД!");
            JOptionPane.showMessageDialog(this,
                    "Помилка підключення до бази даних:\n" + e.getMessage(),
                    "Помилка", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Закриваємо з'єднання
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // Запускаємо GUI в окремому потоці
        SwingUtilities.invokeLater(() -> new DatabaseViewer());
    }
}