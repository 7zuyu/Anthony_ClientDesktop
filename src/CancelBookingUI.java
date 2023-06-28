
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
public class CancelBookingUI extends JFrame{
    private JTextField usernameField;
    private JTextField nobookingField;

    public CancelBookingUI() {
        setTitle("Cancel Booking");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        JPanel buttonPanel = new JPanel();

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel nobookingLabel = new JLabel("Nomor Booking:");
        nobookingField = new JTextField();
        JButton cancelButton = new JButton("Cancel Booking");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(nobookingLabel);
        panel.add(nobookingField);
        panel.add(new JLabel());
        panel.add(cancelButton);

        add(panel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            dispose(); // Close the CancelBoookingUI window
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String nobooking = nobookingField.getText();

                try {
                    // Create the JSON object for cancelbooking data
                    JSONObject cancelData = new JSONObject();
                    cancelData.put("username", username);
                    cancelData.put("nobooking", nobooking);

                    // Create the URL for cancelbooking endpoint
                    URL url = new URL("http://localhost:7071/cancelbooking");

                    // Create the HttpURLConnection
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Send the cancelbooking data
                    OutputStream os = connection.getOutputStream();
                    os.write(cancelData.toString().getBytes());
                    os.flush();

                    // Check the response code
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        JOptionPane.showMessageDialog(null, "Berhasil menghapus booking!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Gagal menghapus booking. Response code: " + responseCode);
                    }

                    connection.disconnect();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error occurred while connecting to the JSON server.");
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CancelBookingUI().setVisible(true);
            }
        });
    }

}
