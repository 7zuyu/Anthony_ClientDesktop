
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class ListWahanaUI extends JFrame{
    private JTable table;
    private JScrollPane scrollPane;
    public ListWahanaUI() {
        setTitle("List Wahana");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();

        // Create the table model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Nomor Wahana");
        tableModel.addColumn("Nama Wahana");
        tableModel.addColumn("Deskripsi Wahana");
        tableModel.addColumn("Harga Wahana");
        tableModel.addColumn("Stok");


        JButton backButton = new JButton("Back");
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            dispose(); // Close the ListKendaraanUI window
        });

        // Fetch vehicle data from the server
        try {
            URL url = new URL("http://localhost:7071/listwahana");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

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

                // Parse the JSON response
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("response");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject wahana = dataArray.getJSONObject(i);
                    int id = wahana.getInt("NomorWahana");
                    String namawahana = wahana.getString("NamaWahana");
                    String deskwahana = wahana.getString("DeskripsiWahana");
                    int HargaWahana = wahana.getInt("HargaWahana");
                    int stok = wahana.getInt("stok");

                    // Add the data to the table model
                    tableModel.addRow(new Object[]{id, namawahana, deskwahana, HargaWahana, stok});
                }
            } else {
                System.out.println("Failed to connect to the server. Response code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception ex) {
            System.out.println("Error occurred while connecting to the server.");
            ex.printStackTrace();
        }

        // Create the JTable and set the model
        table = new JTable(tableModel);

        // Create a scroll pane and add the table to it
        scrollPane = new JScrollPane(table);

        // Add the scroll pane to the frame
        add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ListWahanaUI().setVisible(true);
            }
        });
    }

}
