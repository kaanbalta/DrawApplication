import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class Cekilis extends JFrame {

    private JPanel panel;
    private JTextField AramaÇubuğu;
    private JButton gözAtButton;
    private JList KazananlarListesi;
    private JButton çekilişYapButton;
    private JScrollPane scrollpane;
    private JComboBox kişisayısı;

    private String dosyaYolu;
    private ArrayList<String> katılanlar = new ArrayList<String>();
    private Set<String> kazananlar = new TreeSet<String>();
    private DefaultListModel<String> model = new DefaultListModel<>();
    private int sayı = 1;


    public void alkısla(){

        try(AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("alkçü.wav"))){

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            int duration = (int) (clip.getMicrosecondLength() / 1000);
            Timer timer = new Timer(duration, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    çekilişYapButton.setEnabled(true);
                }
            });
            timer.setRepeats(false);
            timer.start();

        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void cekilisyap(){
        katılanlar.clear();
        kazananlar.clear();
        model.clear();

        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(AramaÇubuğu.getText()),"UTF-8"))) {

            String kisi;
            while ((kisi = bufferedReader.readLine()) != null){
                katılanlar.add(kisi);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (katılanlar.size() < sayı) {
            JOptionPane.showMessageDialog(null, "Yeterli katılımcı yok. En az "  + kişisayısı + " katılımcı gerekli");
            return;
        }

        while (kazananlar.size() < sayı){
            Random random = new Random();
            int kazananIndex = random.nextInt(katılanlar.size());
            kazananlar.add(katılanlar.get(kazananIndex));

        }

        for(String kazanan : kazananlar){

            model.addElement(kazanan);

        }

    }

    public Cekilis() {
        add(panel);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        KazananlarListesi.setModel(model);


        gözAtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int i = fileChooser.showOpenDialog(null);

                if (i == JFileChooser.APPROVE_OPTION) {
                     dosyaYolu = fileChooser.getSelectedFile().getPath();
                     AramaÇubuğu.setText(dosyaYolu);
                }

            }
        });


        çekilişYapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(AramaÇubuğu.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null,"Lütfen bir çekiliş dosyası seçin");
                }
                else {
                    sayı = Integer.parseInt(kişisayısı.getSelectedItem().toString());
                    çekilişYapButton.setEnabled(false);
                    cekilisyap();
                    alkısla();


                }
            }
        });
    }

}
