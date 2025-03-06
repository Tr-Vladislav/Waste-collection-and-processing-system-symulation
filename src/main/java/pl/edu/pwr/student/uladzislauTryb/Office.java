package pl.edu.pwr.student.uladzislauTryb;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class Office {
    private JTextArea tankListTextArea;
    private JTextField setPayOffInput;
    private JTextField getStatusInput;
    private JTextField getStatusOutput;
    private JTextArea requestTextArea;

    private IOffice iOffice;
    private int port;
    private final String host;
    private List<String> tankers; //tanker:  host,port,is_anable

    public Office(int port) throws IOException {
        tankers = new ArrayList<>();
        this.port = port;
        host = InetAddress.getLocalHost().getHostAddress();
        System.out.print(host);
        iOffice = new IOffice(port, this);
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Office");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 350);
        frame.setLayout(new GridLayout(4, 1));

        // Текстовое поле для списка цистерн
        tankListTextArea = new JTextArea();
        tankListTextArea.setBorder(BorderFactory.createTitledBorder("Tank List"));
        frame.add(new JScrollPane(tankListTextArea));

        // Поле для setPayOff
        JPanel setPayOffPanel = new JPanel(new FlowLayout());
        setPayOffPanel.setBorder(BorderFactory.createTitledBorder("Set Pay Off"));
        setPayOffInput = new JTextField(10);
        JButton setPayOffButton = new JButton("Set");
        setPayOffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int number = Integer.parseInt(setPayOffInput.getText());
                try {
                    setPayoff(number);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        setPayOffPanel.add(new JLabel("Number:"));
        setPayOffPanel.add(setPayOffInput);
        setPayOffPanel.add(setPayOffButton);
        frame.add(setPayOffPanel);

        // Поле для getStatus
        JPanel getStatusPanel = new JPanel(new FlowLayout());
        getStatusPanel.setBorder(BorderFactory.createTitledBorder("Get Status"));
        getStatusInput = new JTextField(10);
        getStatusOutput = new JTextField(10);
        getStatusOutput.setEditable(false);
        JButton getStatusButton = new JButton("Get");
        getStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int number = Integer.parseInt(getStatusInput.getText());
                int status = 0;
                try {
                    status = getStatus(number);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                getStatusOutput.setText(String.valueOf(status));
            }
        });
        getStatusPanel.add(new JLabel("Number:"));
        getStatusPanel.add(getStatusInput);
        getStatusPanel.add(getStatusButton);
        getStatusPanel.add(new JLabel("Volume:"));
        getStatusPanel.add(getStatusOutput);
        frame.add(getStatusPanel);

        // Текстовое поле для отображения запроса на вызов цистерны
        requestTextArea = new JTextArea();
        requestTextArea.setBorder(BorderFactory.createTitledBorder("Request"));
        frame.add(new JScrollPane(requestTextArea));

        frame.setVisible(true);
    }


    int register( int port, String host){
        for(String tanker: tankers){
            if(tanker.split(",")[0].equals(String.valueOf(port)) && tanker.split(",")[1].equals(host))return tankers.indexOf(tanker);
        }
        System.out.println(String.valueOf(port)+"," + host + "," + 0);
        tankers.add(String.valueOf(port)+"," + host + "," + 0);
        int number = tankers.size()-1;
        System.out.println("Tanker #" + number + " registered");
        updateTankList();
        return tankers.size()-1;
    }
    int order( int port, String host) throws IOException {
        for(int i = 0; i < tankers.size(); i++){
            String parts[] = tankers.get(i).split(",");
            if(parts[2].equals("1")){
                tankers.set(i, parts[0]+","+parts[1]+","+0);
                Client tankerClient = new Client(Integer.parseInt(parts[0]), parts[1]);
                tankerClient.sendMessage("sj:" + port + "," + host);
                updateTankList();
                return 1;
            }
        }
        return 0;
    }
    void updateTankList(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StringBuilder tankListText = new StringBuilder("Tank List:\n");
                int i=0;
                for (String tanker : tankers) {
                    String[] parts = tanker.split(",");
                    if (parts.length == 3) {
                        String port = parts[0].trim();
                        String host = parts[1].trim();
                        String isEnabled = parts[2].trim().equals("1") ? "Enabled" : "Disabled";
                        tankListText.append("Id: ").append(i).append(" Port: ").append(port).append(" Host: ").append(host).append(" - ").append(isEnabled).append("\n");
                    } else {
                        tankListText.append("Invalid entry: ").append(tanker).append("\n");
                    }
                    i++;
                }
                tankListTextArea.setText(tankListText.toString());
            }
        });
    }

    void setReadyToServe(int number){
        String[] parts = tankers.get(number).split(",");
        System.out.println(parts[0]+","+parts[1]+","+1);
        tankers.set(number, parts[0]+","+parts[1]+","+1);
        updateTankList();
    }
    void setPayoff(int number) throws IOException {
        Client sewagePlant = new Client(Adresses.getSewagePlantPort(), Adresses.getSewagePlantHost());
        sewagePlant.sendMessage("spo:"+number);
    }
    int getStatus(int number) throws IOException {
        Client sewagePlant = new Client(Adresses.getSewagePlantPort(), Adresses.getSewagePlantHost());
        int volume = Integer.parseInt(sewagePlant.sendMessageWithAnswear("gs:"+number));
        return volume;
    }

    public static void main(String[] args) throws IOException {
        new Office(1001); // Example port
    }
}
