package pl.edu.pwr.student.uladzislauTryb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class Tanker {
    private ITanker iTanker;
    private int volume;
    private int currentWaste = 0;
    private int port;
    private final String host;
    String tankerId = "";

    private JTextField tankerNumberField;
    private JTextField tankerVolumeField;
    private JTextField tankerFillLevelField;

    private JTextArea statusTextArea;


    public Tanker(int port, int volume) throws IOException {
        this.volume = volume;
        this.port = port;
        host = InetAddress.getLocalHost().getHostAddress();

        iTanker = new ITanker(port, this);
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Tanker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Текстовое поле для отображения текущего статуса цистерны
        statusTextArea = new JTextArea();
        statusTextArea.setBorder(BorderFactory.createTitledBorder("Current Status"));
        frame.add(new JScrollPane(statusTextArea), BorderLayout.CENTER);
        statusTextArea.setText("Disabled");

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(1, 5));

        // Текстовое поле для номера цистерны (правый верхний угол)
        tankerNumberField = new JTextField("Id: -");
        tankerNumberField.setEditable(false);
        tankerNumberField.setHorizontalAlignment(SwingConstants.RIGHT);
        infoPanel.add(tankerNumberField);

        // Текстовое поле для объема цистерны (левый угол)
        tankerVolumeField = new JTextField("Volume: "+volume+"L");
        tankerVolumeField.setEditable(false);
        tankerVolumeField.setHorizontalAlignment(SwingConstants.LEFT);
        infoPanel.add(tankerVolumeField);

        // Текстовое поле для текущей заполненности цистерны (центр)
        tankerFillLevelField = new JTextField("Current waste: "+currentWaste);
        tankerFillLevelField.setEditable(false);
        tankerFillLevelField.setHorizontalAlignment(SwingConstants.CENTER);
        infoPanel.add(tankerFillLevelField);

        frame.add(infoPanel, BorderLayout.NORTH);


        // Панель для кнопок
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        // Кнопка для setReadyToServe
        JButton readyToServeButton = new JButton("Set Ready to Serve");
        readyToServeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setReadyToServe();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonPanel.add(readyToServeButton);

        // Кнопка для register
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        buttonPanel.add(registerButton);

        // Кнопка для setPumpIn
        JButton pumpInButton = new JButton("Pump In");
        pumpInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setPumpIn();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonPanel.add(pumpInButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void setReadyToServe() throws IOException {
        Client officeClient = new Client(Adresses.getOfficePort(), Adresses.getOfficeHost());
        officeClient.sendMessage("sr:" + tankerId);
        statusTextArea.setText("Enabled");
    }

    private void register() {
        try {

            Client officeClient = new Client(Adresses.getOfficePort(), Adresses.getOfficeHost() );
            tankerId = officeClient.sendMessageWithAnswear("r:" + port + "," + host);
            tankerNumberField.setText("Id:"+tankerId);
            System.out.println("Tanker registered: " + tankerId + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void setJob(int port, String host) throws IOException {
        Client client = new Client(port, host);
        currentWaste = Integer.parseInt(client.sendMessageWithAnswear("gp:" + volume));
        tankerFillLevelField.setText("Current waste: "+currentWaste);

        statusTextArea.setText("Took waste from house");

    }
    void setPumpIn() throws IOException {
        Client sewagePlant = new Client(Adresses.getSewagePlantPort(), Adresses.getSewagePlantHost() );
        sewagePlant.sendMessage("spi:" + tankerId + "," + currentWaste);
        currentWaste = 0;
        tankerFillLevelField.setText("Current waste: "+currentWaste);
        setReadyToServe();

    }

    public static void main(String[] args) throws IOException {
        new Tanker(2000,  600); // Example tanker port, office host and port
        new Tanker(2001,  600); // Example tanker port, office host and port
    }
}
