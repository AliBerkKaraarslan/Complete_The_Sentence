//******************************************************************************************
// CompleteTheSentence.java         Author:Ali Berk Karaarslan     Date:01.09.2023
//
// Completes The Entered Sentence
// Uses Trie Data Structure.
//
//******************************************************************************************

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class CompleteTheSentence {

    /*--- BEGINNING OF THE INNER TRIE CLASS ---*/
    class Trie {

        /*--- BEGINNING OF THE INNER TRIE NODE CLASS ---*/
        private class TrieNode{

            private char character;
            private boolean endOfSentence;

            private ArrayList<TrieNode> children = new ArrayList<>();

            private TrieNode parentNode = null;

            //Constructors
            public TrieNode(){
                character = '*';
                endOfSentence = true;
            }
            public TrieNode(char character, boolean endOfWord){
                this.character = character;
                this.endOfSentence = endOfWord;
            }

            public Iterator<TrieNode> getChildren(){return children.iterator();}
            public void addChild(TrieNode newNode){children.add(newNode);}
            public void removeChild(TrieNode node){children.remove(node);}

            public char getCharacter(){return character;}
            public void setCharacter(char character){this.character = character;}

            public TrieNode getParent(){return parentNode;}
            public void setParentNode(TrieNode parentNode) {this.parentNode = parentNode;}

            public boolean isEnd(){return endOfSentence;}
            public void setEndOfWord(boolean endOfWord){this.endOfSentence = endOfWord;}

            public int numberOfChildren(){return children.size();}

        }
        /*--- END OF THE INNER TRIE NODE CLASS ---*/

        TrieNode root = new TrieNode();
        ArrayList<String> words = new ArrayList<>();

        //Inserts the newNode as a child of the given parentNode
        private void insertNode(TrieNode parentNode, TrieNode newNode){
            parentNode.addChild(newNode);
            newNode.setParentNode(parentNode);
        }

        //Inserts the given word into the Trie
        public void insertWord(String word){

            char[] charArray = word.toLowerCase().toCharArray();
            TrieNode currentNode = root;

            //Helps to determine if it is the last character of the word
            int wordLength = charArray.length;
            int count = 0;

            boolean charAlreadyExists = false;

            //Traverses across the word
            for(char currentChar : charArray){
                count++;
                Iterator<TrieNode> children = currentNode.getChildren();

                //Checks if the currentChar exists in one of the children
                while(children.hasNext()){
                    TrieNode currentChild = children.next();

                    //If the currentChar exists, Sets the corresponding node as currentNode and exists from while loop
                    if(currentChild.getCharacter() == currentChar){

                        //If it is the last character of the word
                        if(count == wordLength)
                            currentChild.setEndOfWord(true);

                        currentNode = currentChild;
                        charAlreadyExists = true;
                        break;
                    }
                }

                //If it is a new char, makes new node and inserts it into trie
                if(!charAlreadyExists){
                    boolean isEnd = (count == wordLength);
                    TrieNode newNode = new TrieNode(currentChar, isEnd);
                    insertNode(currentNode, newNode);
                    currentNode = newNode;
                }
                charAlreadyExists = false;
            }

            //Adds the word if it does not exist
            if(!words.contains(word)) words.add(word);
        }

        //Removes the given word from trie
        public void removeWord(String word){

            TrieNode lastNode = root;
            boolean found = false;

            char[] charArray = word.toLowerCase().toCharArray();
            TrieNode currentNode = root;

            //Helps to determine if it is the last character of the word
            int wordLength = charArray.length;
            int count = 0;

            boolean charAlreadyExists = false;

            //Traverses across the word
            for (char currentChar : charArray) {
                if(!found) {
                    count++;
                    Iterator<TrieNode> children = currentNode.getChildren();

                    //Checks if the currentChar exists in one of the children
                    while (children.hasNext()) {
                        TrieNode currentChild = children.next();

                        //If the currentChar exists, Sets the corresponding node as currentNode and exists from while loop
                        if (currentChild.getCharacter() == currentChar) {

                            //If it is the last character of the word
                            if ((count == wordLength) && currentChild.isEnd()) {
                                lastNode = currentChild;
                                found = true;
                            }
                            currentNode = currentChild;
                            charAlreadyExists = true;
                            break;
                        }
                    }

                    //If word does not exist
                    if (!charAlreadyExists) break;
                    charAlreadyExists = false;
                }
                else break;
            }

            lastNode.setEndOfWord(false);

            //Deleting the word, starting from leaves
            while( !(lastNode.equals(root)) && (lastNode.numberOfChildren() == 0)){
                lastNode.getParent().removeChild(lastNode);
                lastNode = lastNode.getParent();
            }

            //Removes the word if it exists
            words.remove(word);
        }

        //Checks if the given word exists in the Trie
        public boolean isExists(String word) {

            char[] charArray = word.toLowerCase().toCharArray();
            TrieNode currentNode = root;

            //Helps to determine if it is the last character of the word
            int wordLength = charArray.length;
            int count = 0;

            boolean charAlreadyExists = false;

            //Traverses across the word
            for (char currentChar : charArray) {
                count++;
                Iterator<TrieNode> children = currentNode.getChildren();

                //Checks if the currentChar exists in one of the children
                while (children.hasNext()) {
                    TrieNode currentChild = children.next();

                    //If the currentChar exists, Sets the corresponding node as currentNode and exists from while loop
                    if (currentChild.getCharacter() == currentChar) {

                        //If it is the last character of the word
                        if (count == wordLength) {
                            return currentChild.isEnd();
                        }
                        currentNode = currentChild;
                        charAlreadyExists = true;
                        break;
                    }
                }

                if (!charAlreadyExists)
                    return false;

                charAlreadyExists = false;
            }
            return false;
        }

        //Returns the words as String
        public String toString(){
            String returnedString = "";
            for(String currentWord : words){
                returnedString = returnedString.concat(currentWord + "\n");
            }
            return returnedString;
        }
    }
    /*--- END OF THE INNER TRIE CLASS ---*/


    /*--- BEGINNING OF THE INNER GUI CLASS ---*/
    class GUI extends JFrame{

        JTextArea textArea;  //Shows the guesses
        JTextField textField;  //User enters input here
        MenuBar menuBar;  //MenuBar of the program
        int fontSize = 20;  //FontSize of the program

        public GUI(){
            setSize(500,500);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            setLocationRelativeTo(null);
            setTitle("CompleteTheSentence");

            textField = new JTextField();

            //Gets the input and update guesses
            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    //System.out.println(textField.getText());
                    guess(textField.getText());
                    gui.textArea.setText(guesses);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    //System.out.println(textField.getText());
                    guess(textField.getText());
                    gui.textArea.setText(guesses);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                }
            });

            textArea = new JTextArea(10,10);
            textArea.setText(guesses);
            textArea.setEditable(false);

            textField.setFont(new Font("Verdana", Font.PLAIN, fontSize));
            textArea.setFont(new Font("Verdana", Font.PLAIN, fontSize));

            JScrollPane scrolledText = new JScrollPane(textArea);

            menuBar = new MenuBar();
            setJMenuBar(menuBar);

            add(textField, BorderLayout.NORTH);
            add(scrolledText, BorderLayout.CENTER);

            setVisible(true);
        }

        /* MenuBar Of the program. Contains "File","Settings","Help" Sections. */
        class MenuBar extends JMenuBar {

            public MenuBar(){

                JMenu fileMenu = new JMenu("File");
                JMenuItem load = new JMenuItem("Load");
                JMenuItem showLoaded = new JMenuItem("Show Loaded Data");
                JMenuItem clearData = new JMenuItem("Clear Data");

                JMenu help = new JMenu("Help");
                JMenuItem about = new JMenuItem("About");
                JMenuItem loadInfo = new JMenuItem("Load Information");
                JMenuItem whereToStart = new JMenuItem("Where To Start");

                JMenu settings = new JMenu("Settings");
                JMenu countOfGuess = new JMenu("Guess Count");
                JMenuItem countOfGuess12 = new JMenuItem("12");
                JMenuItem countOfGuess17 = new JMenuItem("17");
                JMenuItem countOfGuess20 = new JMenuItem("20");
                JMenuItem countOfGuess30 = new JMenuItem("30");

                //If user presses the "Load" option under fileMenu
                load.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){

                        //Getting the File location
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle("Select a file to load");
                        File defaultPath = new File("C:\\Users\\Ali Berk KARAARSLAN\\Desktop");
                        fileChooser.setCurrentDirectory(defaultPath);
                        fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));

                        //File location
                        int userSelection = fileChooser.showSaveDialog(new JFrame());

                        //If File location is valid, then asks if user wants to overwrite the old data.
                        if (userSelection == JFileChooser.APPROVE_OPTION) {
                            File fileToLoad = fileChooser.getSelectedFile();
                            new OverwriteConfirmFrame(fileToLoad);  //Pops OverwriteConfirmFrame
                        }
                    }
                });

                //If user presses the "Show Loaded Data" option under fileMenu
                showLoaded.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new ShowLoadedDataFrame();
                    }
                });

                //If user presses the "Clear Data" option under fileMenu
                clearData.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        trie = new Trie();

                        //Clears the data file
                        PrintWriter out;
                        try{
                            out = new PrintWriter(new FileOutputStream(fileData.getAbsolutePath(),false));  //Writes the new sentences into corresponding file
                            out.close();
                        } catch (Exception a) {
                            System.out.println("There is an exception while clearing the file");
                        }

                        //Resets guesses
                        guess(textField.getText());
                        gui.textArea.setText(guesses);
                    }
                });

                fileMenu.add(load);
                fileMenu.add(showLoaded);
                fileMenu.add(clearData);
                add(fileMenu);

                //Setting the GuessCount option buttons.
                countOfGuess12.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        guessCount = 12;
                        //Updates the guesses
                        guess(textField.getText());
                        gui.textArea.setText(guesses);
                    }
                });
                countOfGuess17.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        guessCount = 17;
                        guess(textField.getText());
                        gui.textArea.setText(guesses);
                    }
                });
                countOfGuess20.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        guessCount = 20;
                        guess(textField.getText());
                        gui.textArea.setText(guesses);
                    }
                });
                countOfGuess30.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        guessCount = 30;
                        guess(textField.getText());
                        gui.textArea.setText(guesses);
                    }
                });

                countOfGuess.add(countOfGuess12);
                countOfGuess.add(countOfGuess17);
                countOfGuess.add(countOfGuess20);
                countOfGuess.add(countOfGuess30);

                settings.add(countOfGuess);
                add(settings);

                //If user presses "About" option under "Help" menu, then creates "AboutFrame"
                about.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new AboutFrame();
                    }
                });

                //If user presses "Load Information" option under "Help" menu then creates "LoadInfoFrame"
                loadInfo.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new LoadInfoFrame();
                    }
                });

                //If user presses "Where To Start" option under "Help" menu then creates "WhereToStartFrame"
                whereToStart.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new WhereToStartFrame();
                    }
                });

                help.add(whereToStart);
                help.add(loadInfo);
                help.add(about);
                add(help);

                setVisible(true);
            }

            //Saves the file into
            public void saveFile(File fileToSave, boolean overwrite){
                //If overwrite is true, then clears the old data and writes the new data.
                //If overwrite is false, then appends the new data under the old data.

                Scanner scan;  //Reads the new file
                PrintWriter out;  //Writes the new sentences into corresponding file

                try{
                    scan = new Scanner(new FileInputStream(fileToSave.getAbsolutePath()));  //Reads the new file
                    out = new PrintWriter(new FileOutputStream(fileData.getAbsolutePath(),!overwrite));  //Writes the new sentences into corresponding file

                    while(scan.hasNextLine()) {
                        out.println(scan.nextLine());
                    }

                    scan.close();
                    out.close();

                } catch (Exception e) {
                    System.out.println("There is an exception while reading and writing the new file.");
                }
            }
        }

        /* Shows the current loaded data */
        class ShowLoadedDataFrame extends JFrame{

            public ShowLoadedDataFrame(){
                setSize(560,400);
                setTitle("Loaded Data");
                setLocationRelativeTo(null);

                JTextArea loadedData = new JTextArea(10,10);
                loadedData.setText(trie.toString());
                loadedData.setEditable(false);

                loadedData.setFont(new Font("Verdana", Font.PLAIN, fontSize));

                JScrollPane scrolledText = new JScrollPane(loadedData);
                add(scrolledText, BorderLayout.CENTER);

                setVisible(true);
            }
        }

        /* Asks user if the file will be overwritten or not. */
        class OverwriteConfirmFrame extends JFrame{
            //Asks user if she/he wants to overwrite the old text file.
            //If user presses "Yes", clears the old data and loads the new source data.
            //If user presses "No", appends the new source data under the old data and loads it.
            //If user presses "Cancel", returns to the program

            public OverwriteConfirmFrame(File fileToLoad){
                setSize(300,170);
                setLayout(new BorderLayout());
                setTitle("Overwrite It?");
                setLocationRelativeTo(null);  //Sets pop up location to center

                JLabel text = new JLabel("Do you want to overwrite the old one?", SwingConstants.CENTER);
                add(text,BorderLayout.CENTER);

                JPanel buttons = new JPanel();
                buttons.setLayout(new FlowLayout());

                JButton yes = new JButton("Yes");
                JButton no = new JButton("No");
                JButton cancel = new JButton("Cancel");

                //Assigning buttons actions

                //If user presses yes button.
                yes.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        loadFromTextFile(true, fileToLoad.getAbsolutePath());
                        menuBar.saveFile(fileToLoad,true);
                        dispose();
                    }
                });
                //If user presses no.
                no.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        loadFromTextFile(false, fileToLoad.getAbsolutePath());
                        menuBar.saveFile(fileToLoad,false);
                        dispose();
                    }
                });
                //If user presses cancel.
                cancel.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        dispose();
                    }
                });

                //Adding the buttons and setting the layout
                buttons.add(yes);
                buttons.add(no);
                buttons.add(cancel);

                JPanel southPanel = new JPanel();
                southPanel.setLayout(new BorderLayout());

                southPanel.add(buttons,BorderLayout.CENTER);
                add(southPanel,BorderLayout.SOUTH);

                setVisible(true);
            }
        }

        /* Contains Information About Loading Data. */
        class WhereToStartFrame extends JFrame{

            public WhereToStartFrame(){

                setSize(390,150);
                setLayout(new BorderLayout());
                setLocationRelativeTo(null);
                setTitle("Information About Using The Program");

                JLabel text1 = new JLabel("Before starting, data must be loaded into program.", SwingConstants.CENTER);
                JLabel text2 = new JLabel("To load data, go to \"File -> Load\", then select the text file.", SwingConstants.CENTER);
                JLabel text3 = new JLabel("Now, program could be used.", SwingConstants.CENTER);

                //Makes texts Italic
                Font italicFont=new Font(text1.getFont().getName(),Font.ITALIC,text1.getFont().getSize());
                text1.setFont(italicFont);
                text2.setFont(italicFont);
                text3.setFont(italicFont);

                JPanel centerPanel = new JPanel();
                centerPanel.setLayout(new BorderLayout());

                centerPanel.add(text1,BorderLayout.NORTH);
                centerPanel.add(text2,BorderLayout.CENTER);
                centerPanel.add(text3,BorderLayout.SOUTH);

                add(centerPanel,BorderLayout.CENTER);

                JPanel buttons = new JPanel();
                buttons.setLayout(new FlowLayout());

                JButton close = new JButton("Close");
                close.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        dispose();
                    }
                });

                buttons.add(close);

                JPanel southPanel = new JPanel();
                southPanel.setLayout(new BorderLayout());

                southPanel.add(buttons,BorderLayout.EAST);
                add(southPanel,BorderLayout.SOUTH);

                setVisible(true);
            }
        }

        /* Contains Information About Loading Data. */
        class LoadInfoFrame extends JFrame{

            public LoadInfoFrame(){

                setSize(380,150);
                setLayout(new BorderLayout());
                setLocationRelativeTo(null);
                setTitle("Information About Loading Data");

                JLabel text1 = new JLabel("Source files must be in TXT (.txt) format.", SwingConstants.CENTER);
                JLabel text2 = new JLabel("Each line in the text file could contain only one sentence", SwingConstants.CENTER);
                JLabel text3 = new JLabel("If \"Overwrite\" option is selected, previous data will be deleted.", SwingConstants.CENTER);


                //Makes texts Italic
                Font italicFont=new Font(text1.getFont().getName(),Font.ITALIC,text1.getFont().getSize());
                text1.setFont(italicFont);
                text2.setFont(italicFont);
                text3.setFont(italicFont);

                JPanel centerPanel = new JPanel();
                centerPanel.setLayout(new BorderLayout());

                centerPanel.add(text1,BorderLayout.NORTH);
                centerPanel.add(text2,BorderLayout.CENTER);
                centerPanel.add(text3,BorderLayout.SOUTH);

                add(centerPanel,BorderLayout.CENTER);

                JPanel buttons = new JPanel();
                buttons.setLayout(new FlowLayout());

                JButton close = new JButton("Close");
                close.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        dispose();
                    }
                });

                buttons.add(close);

                JPanel southPanel = new JPanel();
                southPanel.setLayout(new BorderLayout());

                southPanel.add(buttons,BorderLayout.EAST);
                add(southPanel,BorderLayout.SOUTH);

                setVisible(true);
            }
        }

        /* Contains Information about Programmer and Program. */
        class AboutFrame extends JFrame{

            public AboutFrame(){

                setSize(320,150);
                setLayout(new BorderLayout());
                setLocationRelativeTo(null);
                setTitle("About CompleteTheSentence");

                //JLabel text1 = new JLabel("CompleteTheSentence", SwingConstants.CENTER);
                JLabel text1 = new JLabel("Completes The Entered Sentence", SwingConstants.CENTER);
                JLabel text2 = new JLabel("Made By Ali Berk Karaarslan", SwingConstants.CENTER);
                JLabel text3 = new JLabel("01.09.2023 (Made in Java)", SwingConstants.CENTER);

                //Makes texts Italic
                Font italicFont=new Font(text1.getFont().getName(),Font.ITALIC,text1.getFont().getSize());
                text1.setFont(italicFont);
                text2.setFont(italicFont);
                text3.setFont(italicFont);

                JPanel centerPanel = new JPanel();
                centerPanel.setLayout(new BorderLayout());

                centerPanel.add(text1,BorderLayout.NORTH);
                centerPanel.add(text2,BorderLayout.CENTER);
                centerPanel.add(text3,BorderLayout.SOUTH);

                add(centerPanel,BorderLayout.CENTER);

                JPanel buttons = new JPanel();
                buttons.setLayout(new FlowLayout());

                JButton close = new JButton("Close");
                close.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        dispose();
                    }
                });

                buttons.add(close);

                JPanel southPanel = new JPanel();
                southPanel.setLayout(new BorderLayout());

                southPanel.add(buttons,BorderLayout.EAST);
                add(southPanel,BorderLayout.SOUTH);

                setVisible(true);
            }
        }

    }
    /*--- END OF THE INNER GUI CLASS ---*/

    GUI gui; //gui lol
    File fileData;  //Stores all the data
    //Constructors

    //By default, loads the "data.txt". (If there is no "data.txt", then makes one.)
    public CompleteTheSentence(){
        fileData = new File("data.txt");
        //If "data.txt" does not exists
        if(!fileData.isFile()) {
            try {
                fileData.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        loadFromTextFile(true, fileData.getPath());
        gui = new GUI();
    }

    public CompleteTheSentence(Boolean overwrite, String path){
        fileData = new File(path);
        loadFromTextFile(overwrite, fileData.getAbsolutePath());
        gui = new GUI();
    }

    String guesses = "";  //Stores the guesses
    int guessCount = 12;  //How many guesses will be shown

    Trie trie = new Trie();  //Stores the words

    //Loads words from given text file
    public void loadFromTextFile(boolean overwrite, String path){

        //Clears the trie
        if(overwrite)
            trie = new Trie();

        //Loads the words into the Trie
        Scanner scan;
        try{
            scan = new Scanner(new FileInputStream(path));
            while(scan.hasNextLine()) {
                trie.insertWord(scan.nextLine());
            }
            scan.close();
        }catch (FileNotFoundException e1) {
            System.out.println("There is no file in: " + path);
        }catch (Exception e2){
            System.out.println("There is an exception");
        }
    }

    //Guesses and fills the guesses String according to currentString
    public void guess(String currentString){

        //Transforms currentString to char array to traverse the word easily
        char[] charArray = currentString.toLowerCase().toCharArray();
        Trie.TrieNode currentNode = trie.root;

        //Helps to determine if it is the last character of the word
        int sentenceLength = charArray.length;
        int count = 0;

        boolean charAlreadyExists = false;
        boolean willBeGuessed = false;

        //Traverses across the word
        for (char currentChar : charArray) {
            count++;
            Iterator<Trie.TrieNode> children = currentNode.getChildren();

            //Checks if the currentChar exists in one of the children
            while (children.hasNext()) {
                Trie.TrieNode currentChild = children.next();

                //If the currentChar exists, Sets the corresponding node as currentNode and exists from while loop
                //Means, moves to the next leaf(next char)
                if (currentChild.getCharacter() == currentChar) {
                    currentNode = currentChild;
                    charAlreadyExists = true;
                    break;
                }
            }

            //If currentString is in trie and there will be guesses
            if ((count == sentenceLength) && charAlreadyExists)
                willBeGuessed = true;

            charAlreadyExists = false;
        }

        //Finds the guesses
        if(willBeGuessed){
            HashMap<Trie.TrieNode, Integer> wordsAndDepths = findSentences(currentNode,0,new HashMap<>());
            guesses = sortSentences(wordsAndDepths);
            //System.out.println(guesses);
        }
        else{
            guesses = "";
            //System.out.println("there is no");
        }
    }

    //Finds the sentences starting from the currentNode and adds them to the corresponded HashMap with depths
    public HashMap<Trie.TrieNode, Integer> findSentences(Trie.TrieNode currentNode, int depth, HashMap<Trie.TrieNode, Integer> sentencesAndDepths){

        //If it is an endNode then adds it into the  HashMap with the depth
        if(currentNode.endOfSentence){
            sentencesAndDepths.put(currentNode,depth+1);
        }

        //Recursively calls the same method with the children
        Iterator<Trie.TrieNode> children = currentNode.getChildren();
        while (children.hasNext()) {
            Trie.TrieNode currentChild = children.next();
            findSentences(currentChild,depth+1, sentencesAndDepths);
        }
        return sentencesAndDepths;
    }

    //Sorts sentences due to their depths and returns them as Strings
    public String sortSentences(HashMap<Trie.TrieNode, Integer> sentencesAndDepths){

        String returnedString = "";

        //Sorts the values(depths)
        ArrayList<Integer> values = new ArrayList<>( sentencesAndDepths.values());
        Collections.sort(values);
        int index = 0;  //Helps to traverse values

        Iterator<Trie.TrieNode> keys = sentencesAndDepths.keySet().iterator();

        int sentenceCount = 0;

        while(index<values.size()) {
            ArrayList<Trie.TrieNode> removedNodes = new ArrayList<>();

            while (keys.hasNext()) {
                Trie.TrieNode currentNode = keys.next();

                if (sentencesAndDepths.get(currentNode).equals(values.get(index))) {
                    if(sentenceCount <guessCount)
                        returnedString = returnedString.concat(findString(currentNode) + "\n");

                    removedNodes.add(currentNode);
                    sentenceCount++;
                }
            }

            //Removes the nodes from HashMap
            for(Trie.TrieNode currNode : removedNodes)
                sentencesAndDepths.remove(currNode);

            //Resets keys Iterator
            keys = sentencesAndDepths.keySet().iterator();
            index++;
        }
        return returnedString;
    }

    //Finds the sentence which is ending with the currentNode
    public String findString(Trie.TrieNode currentNode){

        String tempString = "";
        String returnedString = "";

        //Makes the String, starting from the endNode and goes until the root
        while(!currentNode.equals(trie.root)){
            tempString = tempString.concat(""+currentNode.getCharacter());
            currentNode = currentNode.getParent();
        }

        //Reverses the string
        for(int i = tempString.length()-1; i>=0; i--){
            returnedString = returnedString.concat(""+tempString.charAt(i));
        }
        return returnedString;
    }

    public static void main(String[] args) {
        new CompleteTheSentence();
    }
}