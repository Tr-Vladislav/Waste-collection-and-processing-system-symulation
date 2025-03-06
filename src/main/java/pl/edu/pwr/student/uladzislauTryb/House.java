package pl.edu.pwr.student.uladzislauTryb;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;

public class House {
    private final int port;
    private final String host;
    private final int tankCapacity;
    private int currentWaste;
    private final Client office;

    private JLabel wasteLabel;
    private JTextField wasteInputField;

    public House(int port, int tankCapacity) throws IOException {
        IHouse iHouse = new IHouse(port, this);
        host = InetAddress.getLocalHost().getHostAddress();
        office = new Client(Adresses.getOfficePort(), Adresses.getOfficeHost());
        this.port = port;
        this.tankCapacity = tankCapacity;
        this.currentWaste = 0;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("House");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());

        // Поле для отображения текущего числа загрязнений
        wasteLabel = new JLabel("Current Waste: " + currentWaste);
        wasteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(wasteLabel, BorderLayout.NORTH);

        // Панель для ввода числа и кнопки для генерации загрязнений
        JPanel wasteGenerationPanel = new JPanel(new FlowLayout());
        wasteGenerationPanel.setBorder(BorderFactory.createTitledBorder("Generate Waste"));
        wasteInputField = new JTextField(10);
        wasteInputField.setText("200");
        JButton generateWasteButton = new JButton("Generate");
        generateWasteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int waste = Integer.parseInt(wasteInputField.getText());
                    simulateWasteGeneration(waste);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number.");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        wasteGenerationPanel.add(new JLabel("Amount:"));
        wasteGenerationPanel.add(wasteInputField);
        wasteGenerationPanel.add(generateWasteButton);
        frame.add(wasteGenerationPanel, BorderLayout.CENTER);

        // Кнопка для вызова очистительной машины
        JButton orderButton = new JButton("Order Cleaning Machine");
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    order();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        frame.add(orderButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public int getPumpOut(int max){
        int pumpedOut = Math.min(max, currentWaste);
        currentWaste -= pumpedOut;
        wasteLabel.setText("Current Waste: " + currentWaste);
        return pumpedOut;
    }
    private void order() throws IOException {
        office.sendMessage("o:"+port+","+host);
    }

    private void simulateWasteGeneration(int waste) throws IOException {
        currentWaste = Math.min(currentWaste + waste, tankCapacity);
        if(currentWaste >= tankCapacity){
            order();
        }
        wasteLabel.setText("Current Waste: " + currentWaste);
    }

    public static void main(String[] args) throws IOException {
        new House(12345, 1000);
    }
}
