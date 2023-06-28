import javax.swing.*;

import com.sun.tools.javac.Main;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginAdmin extends JFrame{
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginAdmin() {
        setTitle("Login Admin");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel, BorderLayout.CENTER);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    // Create the JSON object for login data
                    JSONObject loginData = new JSONObject();
                    loginData.put("username", username);
                    loginData.put("password", password);

                    // Create the URL for loginadmin endpoint
                    URL url = new URL("http://localhost:7071/loginadmin");

                    // Create the HttpURLConnection
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Send the login data
                    OutputStream os = connection.getOutputStream();
                    os.write(loginData.toString().getBytes());
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
                        System.out.println("Server Response: " + response);

                        // Show a message dialog based on the response
                        if (response.equals("Login Berhasil")) {
                            JOptionPane.showMessageDialog(null, "Login berhasil!");
                            // Open the main menu for admin
                            openAdminMainMenu();
                        } else {
                            JOptionPane.showMessageDialog(null, "Username atau password salah. Coba lagi.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to connect to the JSON server. Response code: " + responseCode);
                    }

                    connection.disconnect();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error occurred while connecting to the JSON server.");
                    ex.printStackTrace();
                }
            }
        });
    }

    private void openAdminMainMenu() {
        dispose();
        // Add your code here to open the main menu for admin
        JFrame mainMenuFrame = new JFrame("Main Menu");
        mainMenuFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainMenuFrame.setSize(400, 300);
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setLayout(new GridLayout(6, 1));

        // Create the menu buttons
        JButton listWahanaButton = new JButton("List Wahana");
        JButton editWahanaButton = new JButton("Edit Wahana");
        JButton listBookingButton = new JButton("List Booking");
        JButton bookingButton = new JButton("Booking");
        JButton cancelBookingButton = new JButton("Cancel Booking");
        JButton logoutButton = new JButton("Logout");

        mainMenuFrame.add(listWahanaButton);
        mainMenuFrame.add(editWahanaButton);
        mainMenuFrame.add(listBookingButton);
        mainMenuFrame.add(bookingButton);
        mainMenuFrame.add(cancelBookingButton);
        mainMenuFrame.add(logoutButton);

        listWahanaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListWahanaUI listwahana = new ListWahanaUI();
                listwahana.setVisible(true);
            }
        });

        editWahanaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditWahanaUI editwahana = new EditWahanaUI();
                editwahana.setVisible(true);
            }
        });

        listBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListBookingUI listbooking = new ListBookingUI();
                listbooking.setVisible(true);
            }
        });

        bookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BookingUI bookingUI = new BookingUI();
                bookingUI.setVisible(true);
            }
        });

        cancelBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CancelBookingUI cancelbook = new CancelBookingUI();
                cancelbook.setVisible(true);
            }
        });
        // Logout Button
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainMenuFrame.dispose();
            }
        });

        // Display the main menu frame
        mainMenuFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginAdmin().setVisible(true);
            }
        });
    }
}
