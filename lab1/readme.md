# ПЗ-2 Бермічев Володимир ЦК-31
# Лабораторна робота №1

## Тема: Розроблення GUI в Java з використанням графічних бібліотек: AWT, Swing

### Хід роботи

1. Повторити теоретичні відомості.
2. Написати програму у вигляді GUI-додатку на Java для обчислення відстані між двома точками на поверхні Землі.
3. Відповісти на контрольні питання.

---

## Умова завдання

Написати програму у вигляді GUI-додатку на Java для обчислення відстані **D** між двома точками на поверхні Землі, координати яких задані у вигляді географічних координат:

- $\varphi_1, \varphi_2$ — географічна широта точок, в рад.
- $lat_1, lat_2$ — географічна широта точок, в град.
- $\lambda_1, \lambda_2$ — географічна довгота точок, в рад.
- $lon_1, lon_2$ — географічна довгота точок, в град.

**Hint 1.** Для розроблення графічної форми рекомендовано використати компоненти `JLabel`, `JButton` ("Solve", "Clear"), `JTextField` для ($\varphi_1, \varphi_2, \lambda_1, \lambda_2, R$) із бібліотеки Swing.

**Hint 2.** Для обчислення відстані використати **формулу гаверсинуса**:

$$R = 6371 \times 10^3$$

$$\varphi_1 = lat_1 \cdot \frac{\pi}{180}, \quad \varphi_2 = lat_2 \cdot \frac{\pi}{180}$$

$$\Delta\varphi = (lat_2 - lat_1) \cdot \frac{\pi}{180}, \quad \Delta\lambda = (lon_2 - lon_1) \cdot \frac{\pi}{180}$$

$$a = \sin^2\!\left(\frac{\Delta\varphi}{2}\right) + \cos(\varphi_1) \cdot \cos(\varphi_2) \cdot \sin^2\!\left(\frac{\Delta\lambda}{2}\right)$$

$$c = 2 \cdot \mathrm{atan2}\!\left(\sqrt{a},\, \sqrt{1-a}\right)$$

$$D = R \cdot c \quad [\text{м}]$$

---

## Код програми

**Файл:** `src/lab1/Main.java`

```java
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
```

---

## Відповіді на контрольні питання

### 1. Призначення JFC фреймворку і його архітектура

**Java Foundation Classes (JFC)** — набір бібліотек для розроблення GUI-додатків у Java. Включає: **Swing** (набір легких компонентів), **AWT** (Abstract Window Toolkit, платформозалежні компоненти), **Java 2D** (двовимірна графіка), **Accessibility** (підтримка допоміжних технологій) і **Drag and Drop**.

Архітектура побудована за патерном **MVC (Model-View-Controller)**: модель зберігає дані, представлення відображає їх, контролер обробляє події.

---

### 2. Переваги Swing перед AWT

Компоненти AWT є платформозалежними (native), тоді як Swing-компоненти — платформонезалежними (lightweight). Зовнішній вигляд AWT визначається операційною системою; Swing дозволяє гнучко налаштовувати його через механізм Look & Feel. Набір компонентів AWT обмежений, Swing розширює його такими класами як `JTable`, `JTree`, `JTabbedPane` та ін. Підтримка патерну MVC у AWT відсутня, у Swing — вбудована.

Swing малює компоненти власними засобами Java (через `Graphics`), не покладаючись на рідні елементи ОС, що забезпечує однаковий вигляд на всіх платформах.

---

### 3. Структура GUI-програми на Java

```java
import javax.swing.*;

public class Example extends JFrame {

    public Example() {
        setTitle("Example");           // заголовок вікна
        setSize(400, 300);             // розмір вікна
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());   // менеджер компонування

        JLabel label = new JLabel("Enter value:");
        JTextField field = new JTextField(10);
        JButton button = new JButton("OK");

        button.addActionListener(e -> {
            label.setText("Value: " + field.getText()); // обробка події
        });

        add(label);
        add(field);
        add(button);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Example().setVisible(true));
    }
}
```

Елементи програми: `JFrame` — головне вікно; `JLabel` — текстова мітка; `JTextField` — поле введення; `JButton` — кнопка; `ActionListener` — обробник події; `SwingUtilities.invokeLater` — запуск у потоці EDT (Event Dispatch Thread).

---

### 4. Призначення Containers в AWT/Swing

**Container** — компонент, який може містити інші компоненти. Забезпечує ієрархічну організацію GUI.

Як контейнери можуть використовуватись: `JFrame` (головне вікно), `JPanel` (панель загального призначення), `JDialog` (модальне/немодальне діалогове вікно), `JScrollPane` (прокручувана область), `JTabbedPane` (вкладки), `JSplitPane` (розділений контейнер), `JInternalFrame` (вікно всередині іншого вікна).

---

### 5. Основні методи класу `Frame`

`setTitle(String title)` встановлює заголовок вікна. `setSize(int width, int height)` задає його розмір. `setVisible(boolean b)` показує або приховує вікно. `setResizable(boolean b)` дозволяє або забороняє зміну розміру користувачем. `setLocationRelativeTo(Component c)` центрує вікно відносно вказаного компонента (`null` — центр екрана). `setDefaultCloseOperation(int op)` визначає поведінку при закритті вікна. `setLayout(LayoutManager mgr)` встановлює менеджер компонування. `add(Component comp)` додає компонент до вікна. `pack()` стискає вікно до мінімального розміру, достатнього для відображення всіх компонентів. `dispose()` звільняє системні ресурси вікна.

---

### 6. Призначення `Panel`. Як додати компоненти до `Panel`?

**`JPanel`** — універсальний легкий контейнер для групування компонентів. Використовується для логічного розбиття інтерфейсу на секції, кожна з власним менеджером компонування.

```java
JPanel panel = new JPanel(new FlowLayout()); // або new GridLayout(2, 2)

JLabel label  = new JLabel("Name:");
JTextField tf = new JTextField(10);

panel.add(label);  // додавання компонентів
panel.add(tf);

frame.add(panel);  // додавання панелі до вікна
```

Панелі можна вкладати одна в одну для побудови складних макетів.

---

### 7. Призначення об'єктів `event` у `java.awt.event`. Механізм обробки подій

**Event** — об'єкт, що описує дію користувача або системи (натискання кнопки, введення тексту, рух миші тощо).

Механізм обробки подій у AWT/Swing базується на **патерні Observer (Delegation Event Model)**:

1. **Джерело події (Event Source)** — компонент, що генерує подію (наприклад, `JButton`).
2. **Об'єкт події (Event Object)** — містить інформацію про подію (наприклад, `ActionEvent`, `MouseEvent`).
3. **Слухач події (Event Listener)** — об'єкт, що реалізує інтерфейс-слухач і обробляє подію.

```java
button.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // код обробки події
    }
});
```

При натисканні кнопки JVM створює `ActionEvent` і передає його всім зареєстрованим слухачам через відповідний метод інтерфейсу.

---

### 8. Призначення інтерфейсів `XxxListener`. Типи та приклади

**`XxxListener`** — функціональні інтерфейси, що визначають методи для обробки конкретних типів подій. Компонент реєструє слухача через метод `addXxxListener()`.

`ActionListener` — обробляє натискання кнопок і клавіші Enter через метод `actionPerformed(ActionEvent e)`. `MouseListener` — реагує на дії миші: `mouseClicked`, `mousePressed`, `mouseReleased`, `mouseEntered`, `mouseExited`. `KeyListener` — обробляє натискання клавіш: `keyPressed`, `keyReleased`, `keyTyped`. `WindowListener` — відстежує події вікна (`windowClosing`, `windowOpened` та ін.). `FocusListener` — реагує на отримання та втрату фокусу компонентом (`focusGained`, `focusLost`). `ItemListener` — обробляє зміну стану `JCheckBox` або `JComboBox` через `itemStateChanged(ItemEvent e)`. `ChangeListener` — відстежує зміну стану `JSlider` або `JSpinner` через `stateChanged(ChangeEvent e)`.

```java
// ActionListener (лямбда, Java 8+)
button.addActionListener(e -> System.out.println("Clicked"));

// MouseListener
panel.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("X=" + e.getX() + " Y=" + e.getY());
    }
});

// KeyListener
field.addKeyListener(new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) calculateDistance();
    }
});
```

`MouseAdapter` і `KeyAdapter` — абстрактні адаптери з порожніми реалізаціями всіх методів інтерфейсу, що дозволяє перевизначати лише потрібні методи.
