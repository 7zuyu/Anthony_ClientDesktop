
import javax.swing.*;

import org.json.JSONObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class EditWahanaUI extends JFrame{
    private JTextField NamaWahanaField;
    private JTextField HargaWahanaField;
    private JTextField DeskripsiWahanaField;
    private JComboBox<String> NomorWahanaField;

    public EditWahanaUI() {
        setTitle("Edit Wahana");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        JPanel panelid = new JPanel();
        panel.setLayout(new GridLayout(8, 2));

        JLabel NamaWahanaLabel = new JLabel("Nama Wahana:");
        NamaWahanaField = new JTextField();
        JLabel HargaWahanaLabel = new JLabel("Harga Wahana:");
        HargaWahanaField = new JTextField();
        JLabel DeskripsiWahanaLabel = new JLabel("Deskripsi Wahana:");
        DeskripsiWahanaField = new JTextField();
        JLabel NomorWahanaLabel = new JLabel("Nomor Wahana:");
        NomorWahanaField = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6"});

        JButton editButton = new JButton("Edit");

        JLabel idwahana = new JLabel("ID Wahana= 1: JTP 1, 2: JTP 2, 3: JTP 3, 4: JTP 1+2, 5: JTP 2+3, 6: JTP 1+3");

        panel.add(NomorWahanaLabel);
        panel.add(NomorWahanaField);
        panel.add(NamaWahanaLabel);
        panel.add(NamaWahanaField);
        panel.add(HargaWahanaLabel);
        panel.add(HargaWahanaField);
        panel.add(DeskripsiWahanaLabel);
        panel.add(DeskripsiWahanaField);
        panelid.add(idwahana);
        panel.add(new JLabel());
        panel.add(editButton);

        add(panelid, BorderLayout.SOUTH);
        add(panel, BorderLayout.CENTER);
        JButton backButton = new JButton("Back");
        panel.add(backButton);

        backButton.addActionListener(e -> {
            dispose();
        });


        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String NamaWahana = NamaWahanaField.getText();
                String HargaWahana = HargaWahanaField.getText();
                String DeskripsiWahana = DeskripsiWahanaField.getText();
                String NomorWahana = NomorWahanaField.getSelectedItem().toString();

                try {
                    // Create the JSON object for editwahana data
                    JSONObject editData = new JSONObject();
                    editData.put("NamaWahana", NamaWahana);
                    editData.put("HargaWahana", HargaWahana);
                    editData.put("NomorWahana", NomorWahana);
                    editData.put("DeskripsiWahana", DeskripsiWahana);

                    // Create the URL for editwahana endpoint
                    URL url = new URL("http://localhost:7071/editwahana");

                    // Create the HttpURLConnection
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("PUT");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Send the editwahana data
                    OutputStream os = connection.getOutputStream();
                    os.write(editData.toString().getBytes());
                    os.flush();

                    // Check the response code
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        JOptionPane.showMessageDialog(null, "Berhasil mengedit data wahana!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Gagal mengedit data wahana. Response code: " + responseCode);
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
                new EditWahanaUI().setVisible(true);
            }
        });
    }



}
