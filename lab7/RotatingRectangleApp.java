import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class RotatingRectangleApp extends JFrame {

    private DrawPanel drawPanel;
    private Timer timer;
    private double angle = 0;
    private ResourceBundle bundle;
    private Locale currentLocale;

    public RotatingRectangleApp(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);

        setTitle(bundle.getString("title"));
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        drawPanel = new DrawPanel();
        add(drawPanel, BorderLayout.CENTER);

        setJMenuBar(createMenuBar());

        timer = new Timer(30, e -> {
            angle += Math.toRadians(2);
            drawPanel.repaint();
        });
        timer.start();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu(bundle.getString("menu.file"));
        JMenuItem restartItem = new JMenuItem(bundle.getString("menu.restart"));
        restartItem.addActionListener(e -> restartApp());
        JMenuItem exitItem = new JMenuItem(bundle.getString("menu.exit"));
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(restartItem);
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu(bundle.getString("menu.help"));
        JMenuItem usageItem = new JMenuItem(bundle.getString("menu.usage"));
        usageItem.addActionListener(e -> showHelp());
        JMenuItem aboutItem = new JMenuItem(bundle.getString("menu.about"));
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(usageItem);
        helpMenu.add(aboutItem);

        JMenu languageMenu = new JMenu(bundle.getString("menu.language"));
        JMenuItem ukItem = new JMenuItem("Українська");
        ukItem.addActionListener(e -> switchLanguage(new Locale("uk")));
        JMenuItem enItem = new JMenuItem("English");
        enItem.addActionListener(e -> switchLanguage(new Locale("en")));
        JMenuItem plItem = new JMenuItem("Polski");
        plItem.addActionListener(e -> switchLanguage(new Locale("pl")));
        languageMenu.add(ukItem);
        languageMenu.add(enItem);
        languageMenu.add(plItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        menuBar.add(languageMenu);

        return menuBar;
    }

    private void switchLanguage(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);
        setJMenuBar(createMenuBar());
        revalidate();
        repaint();
        setTitle(bundle.getString("title"));
    }

    private void restartApp() {
        angle = 0;
        drawPanel.repaint();
    }

    private void showHelp() {
        JDialog helpDialog = new JDialog(this, bundle.getString("menu.usage"), true);
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setText("<html><body><h2>" + bundle.getString("title") + "</h2>"
                + "<p>" + bundle.getString("help_text") + "</p></body></html>");
        textPane.setEditable(false);
        helpDialog.add(new JScrollPane(textPane));
        helpDialog.setSize(400, 300);
        helpDialog.setLocationRelativeTo(this);
        helpDialog.setVisible(true);
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
                bundle.getString("about_text"),
                bundle.getString("menu.about"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x = 100, y = 100;
            int width = 150, height = 100;

            g2d.translate(x, y);
            g2d.rotate(angle);
            g2d.setColor(Color.BLUE);
            g2d.fillRect(0, 0, width, height);
            g2d.rotate(-angle);
            g2d.translate(-x, -y);
        }
    }

    public static void main(String[] args) {
        Locale defaultLocale = new Locale("uk");
        SwingUtilities.invokeLater(() -> new RotatingRectangleApp(defaultLocale).setVisible(true));
    }
}