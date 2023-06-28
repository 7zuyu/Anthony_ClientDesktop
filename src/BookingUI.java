import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;

public class BookingUI extends JFrame{
    private JTextField usernameField;
    private JTextField tanggalField;
    private JComboBox<String>  nomorwahanaField;


    public BookingUI() {
        setTitle("Booking");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));
        JPanel buttonPanel = new JPanel();

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel tanggalLabel = new JLabel("Tanggal:");
        tanggalField = new JTextField();
        JLabel nomorwahanaLabel = new JLabel("Nomor Wahana:");
        nomorwahanaField = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6"});
        JButton bookButton = new JButton("Book");
        JButton backButton = new JButton("Back");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(tanggalLabel);
        panel.add(tanggalField);
        panel.add(nomorwahanaLabel);
        panel.add(nomorwahanaField);
        panel.add(new JLabel());
        buttonPanel.add(backButton);
        buttonPanel.add(bookButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String tanggal = tanggalField.getText();
                String nomorwahana = nomorwahanaField.getSelectedItem().toString();

                if (username.isEmpty() || tanggal.isEmpty() || nomorwahana.isEmpty() ) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                }
                else {
                    try {
                        // Create the JSON object for booking data
                        JSONObject bookingData = new JSONObject();
                        bookingData.put("username", username);
                        bookingData.put("tanggal", tanggal);
                        bookingData.put("nomorwahana", nomorwahana);


                        // Create the URL for booking endpoint
                        URL url = new URL("http://localhost:7071/booking");

                        // Create the HttpURLConnection
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setDoOutput(true);

                        // Send the booking data
                        OutputStream os = connection.getOutputStream();
                        os.write(bookingData.toString().getBytes());
                        os.flush();

                        // Check the response code
                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            StringBuilder responseBuilder = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                responseBuilder.append(line);
                            }
                            String response = responseBuilder.toString();
                            JOptionPane.showMessageDialog(null, "Berhasil booking! Booking ID: " + response);
                        } else {
                            JOptionPane.showMessageDialog(null, "Gagal booking. Response code: " + responseCode);
                        }

                        connection.disconnect();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error occurred while connecting to the JSON server.");
                        ex.printStackTrace();
                    }
                }
            }
        });

        backButton.addActionListener(e -> {
            dispose(); // Close the BookingUI window
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BookingUI().setVisible(true);
            }
        });
    }
}
