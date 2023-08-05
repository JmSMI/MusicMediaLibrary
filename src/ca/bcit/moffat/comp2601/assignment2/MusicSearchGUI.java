package ca.bcit.moffat.comp2601.assignment2;

import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.ListSelectionModel;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.KeyStroke;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Open a GUI to sort and manipulate a music_data file representing types of Music Media
 *
 * Supported Media:
 *     -Compact Disc
 *     -Audio File
 *     -Vinyl Record
 * Features:
 *     -add, edit, delete individual items in the library
 *     -view sorted library items
 *     -save changes made to the library to the music_data file
 *
 *  Requirements:
 *      -Java 11
 * @author james moffat
 * @version 1
 */
public class MusicSearchGUI extends JFrame
{
    public static final String MUSIC_DATA_PATH;
    public static final String MUSIC_DATA_BACKUP_PATH;
    public static final String FILE_MENU_STRING;
    public static final String SORT_MENU_STRING;
    public static final String HELP_MENU_STRING;
    public static final String LIBRARY_TITLE;
    public static final String SAVE_BUTTON_STRING;
    public static final String DELETE_BUTTON_STRING;
    public static final String CLOSE_BUTTON_STRING;
    public static final String CLEAR_BUTTON_STRING;

    public static final int    PREFIX;
    public static final int    JTEXTFIELD_WIDTH;

    private static final List<MusicMedia> library = new ArrayList<>();

    private final DefaultListModel<MusicMedia> jListModel;
    private final JList<MusicMedia>            jList;
    private final JScrollPane                  jScrollPane;
    private final JDialog                      jDialog;

    private boolean itemSelected;

    static
    {
        MUSIC_DATA_PATH        = "./data/music_data.txt";
        MUSIC_DATA_BACKUP_PATH = "./data/music_data_backup.txt";
        FILE_MENU_STRING       = "File";
        SORT_MENU_STRING       = "Sort";
        HELP_MENU_STRING       = "Help";
        LIBRARY_TITLE          = "Library Contents";
        PREFIX                 = 2;
        JTEXTFIELD_WIDTH       = 10;
        SAVE_BUTTON_STRING     = "Save";
        DELETE_BUTTON_STRING   = "Delete";
        CLOSE_BUTTON_STRING    = "Close";
        CLEAR_BUTTON_STRING    = "Clear";
    }

    {
        jListModel   = new DefaultListModel<>();
        jList        = new JList<>(jListModel);
        jScrollPane  = new JScrollPane();
        jDialog      = new JDialog(this);
        itemSelected = false;
    }

    /**
     * Create a Music Media object and prepare the main Frame
     */
    public MusicSearchGUI()
    {
        super("Music Library");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(getAppMenuBar());
        setVisible(true);

        read();
        createLibraryContentsDialog();
    }

    /**
     * Main method
     */
    public static void main(final String[] args)
    {
        MusicSearchGUI gui = new MusicSearchGUI();
    }

    /**
     * Creates a Music Media library and adds Music Media objects from an external data file.
     */
    public void read()
    {
        File file = new File(MUSIC_DATA_PATH);
        try
        {
            Scanner input = new Scanner(file);
            input.useDelimiter("[\\r\\n|]+"); //new lines OR carriage returns OR "|" symbol
            while(input.hasNext())
            {
                String sku = input.next();
                if(sku.substring(0, PREFIX).equalsIgnoreCase(VinylRecord.VINYLRECORD_PREFIX))
                {
                    String title  = input.next();
                    String artist = input.next();
                    int    year   = input.nextInt();
                    int    tracks = input.nextInt();
                    int    size   = input.nextInt();
                    int    weight = input.nextInt();

                    MusicMedia vinylRecord = new VinylRecord(sku, title, artist, year, tracks, weight, size);
                    library.add(vinylRecord);
                }
                else if(sku.substring(0, PREFIX).equalsIgnoreCase(AudioFile.AUDIOFILE_PREFIX))
                {
                    String title      = input.next();
                    String artist     = input.next();
                    int    year       = input.nextInt();
                    String fileName   = input.next();
                    int    resolution = input.nextInt();

                    MusicMedia audioFile = new AudioFile(sku, title, artist, year, fileName, resolution);
                    library.add(audioFile);
                }
                else if(sku.substring(0, PREFIX).equalsIgnoreCase(CompactDisc.COMPACTDISC_PREFIX))
                {
                    String title  = input.next();
                    String artist = input.next();
                    int    year   = input.nextInt();
                    int    tracks = input.nextInt();

                    MusicMedia compactDisk = new CompactDisc(sku, title, artist, year, tracks);
                    library.add(compactDisk);
                }
            }
            input.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Create a scrollpane dialog used to display the contents of the music library
     */
    public void createLibraryContentsDialog()
    {
        jDialog.setTitle(LIBRARY_TITLE);
        jDialog.setBounds(100,100,450,300);
        jDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jDialog.add(jScrollPane, BorderLayout.CENTER);

        jScrollPane.setViewportView(jList);

        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2)
                {
                    MusicMedia m = jList.getSelectedValue();
                    setItemSelected(true);
                    System.out.println("Information: click");

                    if(m.getType().equalsIgnoreCase(AudioFile.AUDIOFILE_STRING))
                    {
                        createAudioFileDialog(m);
                    }
                    else if(m.getType().equalsIgnoreCase(VinylRecord.VINYLRECORD_STRING))
                    {
                        createVinylRecordDialog(m);
                    }
                    else if(m.getType().equalsIgnoreCase(CompactDisc.COMPACTDISC_STRING))
                    {
                        createCompactDiscDialog(m);
                    }
                }
                super.mouseClicked(e);
            }
        });

        //add ok button to close component
        JPanel  buttonPane;
        JButton okButton;

        buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        okButton   = new JButton("OK");

        okButton.addActionListener(event->
                jDialog.dispose()
        );
        buttonPane.add(okButton);
        jDialog.add(buttonPane, BorderLayout.PAGE_END);
    }

    /**
     * Create a backup, then copy the library to the source file. This will overwrite the source data file.
     * Changes made to any objects in the library will be permanently written to the file.
     */
    public void save()
    {
        Path source = Path.of(MUSIC_DATA_PATH);
        Path backup = Path.of(MUSIC_DATA_BACKUP_PATH);
        try {
            //create a backup of the current source file
            Files.copy(source, backup, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Information: Backup created");

            //apply changes
            File file = new File(MUSIC_DATA_PATH);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for(MusicMedia media: library)
            {
                bw.write(media.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
            System.out.println("Information: Saved library to source");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Controls whether an object selected in the scrollpane can be modified
     * @param selected true if the item is selected
     */
    public void setItemSelected(final boolean selected)
    {
        itemSelected = selected;
    }

    /**
     * Objects that are not selected will not be modified
     * @return true if the item is selected
     */
    public boolean isItemSelected()
    {
        return itemSelected;
    }

    /**
     * Clear the contents of all text fields in the selection panel
     * @param panel a panel with text fields
     * @throws IllegalArgumentException the panel must have components in it
     */
    public void clearJTextFieldsFromPanel(JPanel panel)
    {
        if((panel == null) || (panel.getComponentCount() == 0))
        {
            throw new IllegalArgumentException("Provide a panel with components");
        }

        for(int i = 0; i < panel.getComponentCount(); i ++)
        {
            if(panel.getComponent(i) instanceof JTextField)
            {
                ((JTextField) panel.getComponent(i)).setText(null);
                System.out.println("Information: cleared field");
            }
        }
    }

    /**
     * Create an empty JDialog with a title.
     * This JDialog is used to display the details of selected (clicked) items
     *
     * @param title the title of the JDialog
     * @return a JDialog with a title
     */
    public JDialog getSelectionDialog(String title)
    {
        if((title == null) || (title.isEmpty()))
        {
            System.out.println("Warning: JDialog title is empty");
        }

        JDialog selectionDialog = new JDialog();
        selectionDialog.setTitle(title);
        selectionDialog.setLayout(new BoxLayout(selectionDialog.getContentPane(), BoxLayout.Y_AXIS));
        selectionDialog.setBounds(100,100,350,300);
        selectionDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        return selectionDialog;
    }

    /**
     * Create the Menu Bar to go into the main Frame
     * @return a JMenuBar with components for submenus and dialogs
     */
    public JMenuBar getAppMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();

        //menus
        JMenu fileMenu = new JMenu(FILE_MENU_STRING);
        JMenu sortMenu = new JMenu(SORT_MENU_STRING);
        JMenu helpMenu = new JMenu(HELP_MENU_STRING);

        //add menus
        menuBar.add(fileMenu);
        menuBar.add(sortMenu);
        menuBar.add(helpMenu);

        //items
        JMenuItem exitMenuItem   = new JMenuItem("Exit");
        JMenuItem sortTypeItem   = new JMenuItem("By Type");
        JMenuItem sortArtistItem = new JMenuItem("By Artist");
        JMenuItem sortTitleItem  = new JMenuItem("By Title");
        JMenuItem sortYearItem   = new JMenuItem("By Year");
        JMenuItem aboutMenuItem  = new JMenuItem("About");
        JMenuItem saveMenuItem   = new JMenuItem("Save Data");

        //add items
        helpMenu.add(aboutMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(exitMenuItem);
        sortMenu.add(sortTypeItem);
        sortMenu.add(sortArtistItem);
        sortMenu.add(sortTitleItem);
        sortMenu.add(sortYearItem);

        //add accelerators
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.SHIFT_DOWN_MASK));

        //add mnemonics
        helpMenu.setMnemonic(KeyEvent.VK_H);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        aboutMenuItem.setMnemonic(KeyEvent.VK_C);
        sortTypeItem.setMnemonic(KeyEvent.VK_T);
        sortArtistItem.setMnemonic(KeyEvent.VK_A);
        sortYearItem.setMnemonic(KeyEvent.VK_Y);
        sortTitleItem.setMnemonic(KeyEvent.VK_I);

        //listeners
        //confirm to save when exit
        exitMenuItem.addActionListener(event->
        {
            if(JOptionPane.showConfirmDialog(this,
            "Do you want to save changes?",
            "Save Data",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                save();
                System.exit(0);
            }
            else
            {
                System.exit(0);
            }
        });

        //choose to save all to file
        saveMenuItem.addActionListener(event->
        {
            if(JOptionPane.showConfirmDialog(this,
                    "Do you want to save changes?",
                    "Save Data",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                save();
            }
        });

        //show about information
        aboutMenuItem.addActionListener(event->
                JOptionPane.showMessageDialog(this,
                        "Assignment 2 \nby James Moffat \nA01193600",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE)
        );

        //sort library by type
        sortTypeItem.addActionListener(event->
        {
            jDialog.setVisible(true);

            jListModel.clear();
            List<MusicMedia> l = library.stream()
                    .sorted(Comparator.comparing(MusicMedia::getType))
                    .collect(Collectors.toList());
            l.forEach(jListModel::addElement);
        });

        //sort library by artist
        sortArtistItem.addActionListener(event->
        {
            jDialog.setVisible(true);

            jListModel.clear();
            List<MusicMedia> l = library.stream()
                    .sorted(Comparator.comparing(MusicMedia::getArtist))
                    .collect(Collectors.toList());
            l.forEach(jListModel::addElement);
        });

        //sort library by title
        sortTitleItem.addActionListener(event->
        {
            jDialog.setVisible(true);

            jListModel.clear();
            List<MusicMedia> l = library.stream()
                    .sorted(Comparator.comparing(MusicMedia::getTitle))
                    .collect(Collectors.toList());
            l.forEach(jListModel::addElement);
        });

        //sort library by year
        sortYearItem.addActionListener(event->
        {
            jDialog.setVisible(true);

            jListModel.clear();
            List<MusicMedia> l = library.stream()
                    .sorted(Comparator.comparing(MusicMedia::getYear))
                    .collect(Collectors.toList());
            l.forEach(jListModel::addElement);
        });
        return menuBar;
    }

    /**
     * Create a component to display detailed information about a Vinyl Record selected from the scroll pane
     * @param m the selected Music Media object to display
     * @throws IllegalArgumentException the MusicMedia object must be a VinylRecord
     */
    public void createVinylRecordDialog(final MusicMedia m)
    {
        if(!(m instanceof VinylRecord))
        {
            throw new IllegalArgumentException("invalid object class");
        }
        VinylRecord media = (VinylRecord) m;

        String[] fieldNames  = VinylRecord.VINYLRECORD_FIELDS;
        String   dialogTitle = VinylRecord.VINYLRECORD_STRING;
        int      layoutRows  = VinylRecord.VINYLRECORD_FIELDS.length;
        JDialog  jDialog     = getSelectionDialog(dialogTitle);
        JPanel   topPanel    = new JPanel();
        JPanel   bottomPanel = new JPanel();

        topPanel.setLayout(new GridLayout(layoutRows,2, 10, 5));
        topPanel.setBorder(new EmptyBorder(10,10,10,10));
        bottomPanel.setLayout(new FlowLayout());

        //fields
        JTextField skuField    = new JTextField(media.getSku(), JTEXTFIELD_WIDTH);
        JTextField titleField  = new JTextField(media.getTitle(), JTEXTFIELD_WIDTH);
        JTextField artistField = new JTextField(media.getArtist(), JTEXTFIELD_WIDTH);
        JTextField yearField   = new JTextField(Integer.toString(media.getYear()), JTEXTFIELD_WIDTH);
        JTextField tracksField = new JTextField(Integer.toString(media.getNumberOfTracks()), JTEXTFIELD_WIDTH);
        JTextField sizeField   = new JTextField(Integer.toString(media.getSizeInInches()), JTEXTFIELD_WIDTH);
        JTextField weightField = new JTextField(Integer.toString(media.getWeight()), JTEXTFIELD_WIDTH);

        //populate top panel
        topPanel.add(new JLabel(fieldNames[0], JLabel.RIGHT));
        topPanel.add(skuField);
        topPanel.add(new JLabel(fieldNames[1], JLabel.RIGHT));
        topPanel.add(titleField);
        topPanel.add(new JLabel(fieldNames[2], JLabel.RIGHT));
        topPanel.add(artistField);
        topPanel.add(new JLabel(fieldNames[3], JLabel.RIGHT));
        topPanel.add(yearField);
        topPanel.add(new JLabel(fieldNames[4], JLabel.RIGHT));
        topPanel.add(tracksField);
        topPanel.add(new JLabel(fieldNames[5], JLabel.RIGHT));
        topPanel.add(sizeField);
        topPanel.add(new JLabel(fieldNames[6], JLabel.RIGHT));
        topPanel.add(weightField);

        //buttons
        JButton save   = new JButton(SAVE_BUTTON_STRING);
        JButton delete = new JButton(DELETE_BUTTON_STRING);
        JButton clear  = new JButton(CLEAR_BUTTON_STRING);
        JButton close  = new JButton(CLOSE_BUTTON_STRING);

        //populate bottom panel
        bottomPanel.add(clear);
        bottomPanel.add(save);
        bottomPanel.add(delete);
        bottomPanel.add(close);

        //listeners
        save.addActionListener(event->
        {
            String sku    = skuField.getText();
            String title  = titleField.getText();
            String artist = artistField.getText();
            int    year   = Integer.parseInt(yearField.getText());
            int    tracks = Integer.parseInt(tracksField.getText());
            int    size   = Integer.parseInt(sizeField.getText());
            int    weight = Integer.parseInt(weightField.getText());

            if(isItemSelected())
            {
                media.setSku(sku);
                media.setArtist(artist);
                media.setTitle(title);
                media.setYear(year);
                media.setNumberOfTracks(tracks);
                media.setSizeInInches(size);
                media.setWeight(weight);
                System.out.println("Information: changed " + media);
            }
            else
            {
                MusicMedia newMedia = new VinylRecord(sku, title, artist, year, tracks, weight, size);
                library.add(newMedia);
                System.out.println("Information: created " + newMedia);
            }
        });

        delete.addActionListener(event->
        {
            if((isItemSelected()) &&
                    (JOptionPane.showConfirmDialog(null,
                            "Are you sure?",
                            "Delete",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
            {
                clearJTextFieldsFromPanel(topPanel);
                library.remove(m);
                System.out.println("deleted " + m);
            }
            else
            {
                System.out.println("Information: no object to delete");
            }
        });

        clear.addActionListener(event->
        {
            setItemSelected(false);
            clearJTextFieldsFromPanel(topPanel);
        });

        close.addActionListener(event->
                jDialog.dispose()
        );

        jDialog.add(topPanel);
        jDialog.add(bottomPanel);
        jDialog.setVisible(true);
        jDialog.pack();
    }

    /**
     * Create a component to display detailed information about a CompactDisc selected from the scroll pane
     * @param m the selected Music Media object to display
     * @throws IllegalArgumentException the MusicMedia object must be a CompactDisc
     */
    public void createCompactDiscDialog(final MusicMedia m)
    {
        if(!(m instanceof CompactDisc))
        {
            throw new IllegalArgumentException("invalid object class");
        }
        CompactDisc media = (CompactDisc) m;

        String   dialogTitle = CompactDisc.COMPACTDISC_STRING;
        int      layoutRows  = CompactDisc.COMPACTDISC_FIELDS.length;
        String[] fieldNames  = CompactDisc.COMPACTDISC_FIELDS;
        JDialog  jDialog     = getSelectionDialog(dialogTitle);
        JPanel   topPanel    = new JPanel();
        JPanel   bottomPanel = new JPanel();

        topPanel.setLayout(new GridLayout(layoutRows,2, 10, 5));
        topPanel.setBorder(new EmptyBorder(10,10,10,10));
        bottomPanel.setLayout(new FlowLayout());

        //fields
        JTextField skuField            = new JTextField(media.getSku(), JTEXTFIELD_WIDTH);
        JTextField titleField          = new JTextField(media.getTitle(), JTEXTFIELD_WIDTH);
        JTextField artistField         = new JTextField(media.getArtist(), JTEXTFIELD_WIDTH);
        JTextField yearField           = new JTextField(Integer.toString(media.getYear()), JTEXTFIELD_WIDTH);
        JTextField numberOfTracksField = new JTextField(Integer.toString(media.getNumberOfTracks()), JTEXTFIELD_WIDTH);

        //populate top panel
        topPanel.add(new JLabel(fieldNames[0], JLabel.RIGHT));
        topPanel.add(skuField);
        topPanel.add(new JLabel(fieldNames[1], JLabel.RIGHT));
        topPanel.add(titleField);
        topPanel.add(new JLabel(fieldNames[2], JLabel.RIGHT));
        topPanel.add(artistField);
        topPanel.add(new JLabel(fieldNames[3], JLabel.RIGHT));
        topPanel.add(yearField);
        topPanel.add(new JLabel(fieldNames[4], JLabel.RIGHT));
        topPanel.add(numberOfTracksField);

        //buttons
        JButton save   = new JButton(SAVE_BUTTON_STRING);
        JButton delete = new JButton(DELETE_BUTTON_STRING);
        JButton clear  = new JButton(CLEAR_BUTTON_STRING);
        JButton close  = new JButton(CLOSE_BUTTON_STRING);

        //populate bottom panel
        bottomPanel.add(clear);
        bottomPanel.add(save);
        bottomPanel.add(delete);
        bottomPanel.add(close);

        //listeners
        save.addActionListener(event->
        {
            String sku    = skuField.getText();
            String title  = titleField.getText();
            String artist = artistField.getText();
            int    year   = Integer.parseInt(yearField.getText());
            int    tracks = Integer.parseInt(numberOfTracksField.getText());

            if(isItemSelected())
            {
                media.setSku(sku);
                media.setArtist(artist);
                media.setTitle(title);
                media.setYear(year);
                media.setNumberOfTracks(tracks);
                System.out.println("Information: changed  " + media);
            }
            else
            {
                MusicMedia newMedia = new CompactDisc(sku, title, artist, year, tracks);
                library.add(newMedia);
                System.out.println("Information: created " + newMedia);
            }
        });

        delete.addActionListener(event->
        {
            if((isItemSelected()) &&
                    (JOptionPane.showConfirmDialog(null,
                            "Are you sure?",
                            "Delete",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
            {
                clearJTextFieldsFromPanel(topPanel);
                library.remove(m);
                System.out.println("Information: deleted " + m);
            }
            else
            {
                System.out.println("Information: no object to delete");
            }
        });

        clear.addActionListener(event->
        {
            setItemSelected(false);
            clearJTextFieldsFromPanel(topPanel);
        });

        close.addActionListener(event->
                jDialog.dispose()
        );

        jDialog.add(topPanel);
        jDialog.add(bottomPanel);
        jDialog.setVisible(true);
        jDialog.pack();
    }

    /**
     * Create a component to display detailed information about a AudioFile selected from the scroll pane
     * @param m the selected Music Media object to display
     * @throws IllegalArgumentException the MusicMedia object must be an AudioFile
     */
    public void createAudioFileDialog(final MusicMedia m)
    {
        if(!(m instanceof AudioFile))
        {
            throw new IllegalArgumentException("invalid object class");
        }
        AudioFile media = (AudioFile) m;

        String   dialogTitle = AudioFile.AUDIOFILE_STRING;
        int      layoutRows  = AudioFile.AUDIOFILE_FIELDS.length;
        String[] fieldNames  = AudioFile.AUDIOFILE_FIELDS;
        JDialog  jDialog     = getSelectionDialog(dialogTitle);
        JPanel   topPanel    = new JPanel();
        JPanel   bottomPanel = new JPanel();

        topPanel.setLayout(new GridLayout(layoutRows,2, 10, 5));
        topPanel.setBorder(new EmptyBorder(10,10,10,10));
        bottomPanel.setLayout(new FlowLayout());

        //fields
        JTextField skuField        = new JTextField(media.getSku(), JTEXTFIELD_WIDTH);
        JTextField titleField      = new JTextField(media.getTitle(), JTEXTFIELD_WIDTH);
        JTextField artistField     = new JTextField(media.getArtist(), JTEXTFIELD_WIDTH);
        JTextField yearField       = new JTextField(Integer.toString(media.getYear()), JTEXTFIELD_WIDTH);
        JTextField filenameField   = new JTextField(media.getFileName(), JTEXTFIELD_WIDTH);
        JTextField resolutionField = new JTextField(Integer.toString(media.getFileResolution()), JTEXTFIELD_WIDTH);

        //populate top panel
        topPanel.add(new JLabel(fieldNames[0], JLabel.RIGHT));
        topPanel.add(skuField);
        topPanel.add(new JLabel(fieldNames[1], JLabel.RIGHT));
        topPanel.add(titleField);
        topPanel.add(new JLabel(fieldNames[2], JLabel.RIGHT));
        topPanel.add(artistField);
        topPanel.add(new JLabel(fieldNames[3], JLabel.RIGHT));
        topPanel.add(yearField);
        topPanel.add(new JLabel(fieldNames[4], JLabel.RIGHT));
        topPanel.add(filenameField);
        topPanel.add(new JLabel(fieldNames[5], JLabel.RIGHT));
        topPanel.add(resolutionField);

        //buttons
        JButton save   = new JButton(SAVE_BUTTON_STRING);
        JButton delete = new JButton(DELETE_BUTTON_STRING);
        JButton clear  = new JButton(CLEAR_BUTTON_STRING);
        JButton close  = new JButton(CLOSE_BUTTON_STRING);

        //populate bottom panel
        bottomPanel.add(clear);
        bottomPanel.add(save);
        bottomPanel.add(delete);
        bottomPanel.add(close);

        //listeners
        save.addActionListener(event->
        {
            String sku         = skuField.getText();
            String title       = titleField.getText();
            String artist      = artistField.getText();
            int    year        = Integer.parseInt(yearField.getText());
            String filename    = filenameField.getText();
            int    resolution  = Integer.parseInt(resolutionField.getText());

            if(isItemSelected())
            {
                media.setSku(sku);
                media.setArtist(artist);
                media.setTitle(title);
                media.setYear(year);
                media.setFileName(filename);
                media.setFileResolution(resolution);
                System.out.println("Information: changed " + media);
            }
            else
            {
                MusicMedia newMedia = new AudioFile(sku, title, artist, year, filename, resolution);
                library.add(newMedia);
                System.out.println("Information: created " + newMedia);
            }
        });

        delete.addActionListener(event->
        {
            if((isItemSelected()) &&
                    (JOptionPane.showConfirmDialog(null,
                    "Are you sure?",
                    "Delete",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
            {
                clearJTextFieldsFromPanel(topPanel);
                library.remove(m);
                System.out.println("Information: deleted " + m);
            }
            else
            {
                System.out.println("Information: no object to delete");
            }
        });

        clear.addActionListener(event->
        {
            setItemSelected(false);
            clearJTextFieldsFromPanel(topPanel);
        });

        close.addActionListener(event->
                jDialog.dispose()
        );

        jDialog.add(topPanel);
        jDialog.add(bottomPanel);
        jDialog.setVisible(true);
        jDialog.pack();
    }
}
