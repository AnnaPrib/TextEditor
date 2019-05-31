package ui;

import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.text.StyledDocument;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.DefaultEditorKit.CutAction;
import javax.swing.text.DefaultEditorKit.CopyAction;
import javax.swing.text.DefaultEditorKit.PasteAction;
import javax.swing.text.StyledEditorKit.BoldAction;
import javax.swing.text.StyledEditorKit.ItalicAction;
import javax.swing.text.StyledEditorKit.UnderlineAction;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class TextEditor {

    private JPanel panel1;
    private JTextPane pane;
    private File file;
    private JScrollPane textScroller;
    private JToolBar noteToolBar;
    private JButton newFileButton;
    private JButton openFileButton;
    private JButton saveFileButton;

    private static final String DEFAULT_FONT_FAMILY = "SansSerif";
    private static final int DEFAULT_FONT_SIZE = 18;


    public  TextEditor(){
        pane.setDocument(getNewDocument());

        EditButtonActionListener editButtonActionListener =
                new EditButtonActionListener();

        JButton cutButton = new JButton(new CutAction());
        cutButton.setHideActionText(true);
        cutButton.setText("Cut");
        cutButton.addActionListener(editButtonActionListener);
        JButton copyButton = new JButton(new CopyAction());
        copyButton.setHideActionText(true);
        copyButton.setText("Copy");
        copyButton.addActionListener(editButtonActionListener);
        JButton pasteButton = new JButton(new PasteAction());
        pasteButton.setHideActionText(true);
        pasteButton.setText("Paste");
        pasteButton.addActionListener(editButtonActionListener);

        JButton boldButton = new JButton(new BoldAction());
        boldButton.setHideActionText(true);
        boldButton.setText("Bold");
        boldButton.addActionListener(editButtonActionListener);
        JButton italicButton = new JButton(new ItalicAction());
        italicButton.setHideActionText(true);
        italicButton.setText("Italic");
        italicButton.addActionListener(editButtonActionListener);
        JButton underlineButton = new JButton(new UnderlineAction());
        underlineButton.setHideActionText(true);
        underlineButton.setText("Underline");
        underlineButton.addActionListener(editButtonActionListener);

        noteToolBar.add(cutButton);
        noteToolBar.add(copyButton);
        noteToolBar.add(pasteButton);
        noteToolBar.add(boldButton);
        noteToolBar.add(italicButton);
        noteToolBar.add(underlineButton);

        newFileButton.setMnemonic(KeyEvent.VK_N);
        newFileButton.addActionListener(new NewFileListener());
        openFileButton.setMnemonic(KeyEvent.VK_O);
        openFileButton.addActionListener(new OpenFileListener());
        saveFileButton.setMnemonic(KeyEvent.VK_S);
        saveFileButton.addActionListener(new SaveFileListener());

        pane.setFont(new Font(DEFAULT_FONT_FAMILY, Font.PLAIN, DEFAULT_FONT_SIZE));
        panel1.setSize(400, 300);
        pane.requestFocusInWindow();
    }

    private StyledDocument getNewDocument() {

        StyledDocument doc = new DefaultStyledDocument();
        return doc;
    }

    private class EditButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            pane.requestFocusInWindow();
        }
    }

    private StyledDocument getEditorDocument() {

        StyledDocument doc = (DefaultStyledDocument) pane.getDocument();
        return doc;
    }

    private class NewFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            initEditorAttributes();
            pane.setDocument(getNewDocument());
            file = null;
        }

        private void initEditorAttributes() {

            AttributeSet attrs1 = pane.getCharacterAttributes();
            SimpleAttributeSet attrs2 = new SimpleAttributeSet(attrs1);
            attrs2.removeAttributes(attrs1);
            pane.setCharacterAttributes(attrs2, true);
        }
    }

    private class OpenFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            file = chooseFile();

            if (file == null) {

                return;
            }

            readFile(file);
        }

        private File chooseFile() {

            JFileChooser chooser = new JFileChooser();

            if (chooser.showOpenDialog(panel1) == JFileChooser.APPROVE_OPTION) {

                return chooser.getSelectedFile();
            }
            else {
                return null;
            }
        }

        private void readFile(File file) {

            StyledDocument doc = null;

            try (InputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                doc = (DefaultStyledDocument) ois.readObject();
            }
            catch (FileNotFoundException ex) {

                JOptionPane.showMessageDialog(panel1, "Input file was not found!");
                return;
            }
            catch (ClassNotFoundException | IOException ex) {

                throw new RuntimeException(ex);
            }

            pane.setDocument(doc);
        }
    }

    private class SaveFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (file == null) {

                file = chooseFile();

                if (file == null) {

                    return;
                }
            }

            DefaultStyledDocument doc = (DefaultStyledDocument) getEditorDocument();

            try (OutputStream fos = new FileOutputStream(file);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                oos.writeObject(doc);
            }
            catch (IOException ex) {

                throw new RuntimeException(ex);
            }
        }

        private File chooseFile() {

            JFileChooser chooser = new JFileChooser();

            if (chooser.showSaveDialog(panel1) == JFileChooser.APPROVE_OPTION) {

                return chooser.getSelectedFile();
            }
            else {
                return null;
            }
        }
    }

    public JComponent getRootComponent() {
        return panel1;
    }
}