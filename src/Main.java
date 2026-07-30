import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Wingdings is a trademark of Microsoft Corporation.
 * Copyright © 1990-1992 Microsoft Corporation. All rights reserved.
 * Created by Charles Bigelow and Kris Holmes (Type Solutions, Inc.).
 * Hand and face characters copyright © 1990 Helvetica.
 */
public class Main extends JFrame {

    private JTextArea inputArea;
    private JTextArea outputArea;
    private JComboBox<String> modeBox;

    private static final Map<Character, String> TO_WINGDINGS = new HashMap<>();
    private static final Map<String, Character> FROM_WINGDINGS = new HashMap<>();
    private static final Map<Character, String> RU_TO_EN = new HashMap<>();
    private static final Map<String, Character> EN_TO_RU = new HashMap<>();

    static {
        initializeWingdingsMaps();
        initializeTranslitMaps();
    }

    public Main() {
        setTitle("Wingdings Translator");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout());
        modeBox = new JComboBox<>(new String[]{"Русский -> Wingdings", "Wingdings -> Русский"});
        JButton translateButton = new JButton("Перевести");
        topPanel.add(modeBox);
        topPanel.add(translateButton);

        inputArea = new JTextArea(7, 40);
        outputArea = new JTextArea(7, 40);
        outputArea.setEditable(false);

        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        Font unicodeFont = new Font("Arial Unicode MS", Font.PLAIN, 16);
        inputArea.setFont(unicodeFont);
        outputArea.setFont(unicodeFont);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(new JLabel("Введите текст:"));
        mainPanel.add(new JScrollPane(inputArea));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(topPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(new JLabel("Результат:"));
        mainPanel.add(new JScrollPane(outputArea));

        add(mainPanel);

        translateButton.addActionListener(e -> handleTranslation());
    }

    private void handleTranslation() {
        String input = inputArea.getText();
        if (modeBox.getSelectedIndex() == 0) {
            outputArea.setText(translateRuToWingdings(input));
        } else {
            outputArea.setText(translateWingdingsToRu(input));
        }
    }

    public static String translateRuToWingdings(String text) {
        StringBuilder latinSb = new StringBuilder();
        for (char c : text.toLowerCase().toCharArray()) {
            latinSb.append(RU_TO_EN.getOrDefault(c, String.valueOf(c)));
        }
        StringBuilder wingdingsSb = new StringBuilder();
        for (char c : latinSb.toString().toCharArray()) {
            wingdingsSb.append(TO_WINGDINGS.getOrDefault(c, String.valueOf(c)));
        }
        return wingdingsSb.toString();
    }

    public static String translateWingdingsToRu(String text) {
        StringBuilder latinSb = new StringBuilder();
        int i = 0;
        while (i < text.length()) {
            int codePoint = text.codePointAt(i);
            String symbol = new String(Character.toChars(codePoint));
            if (FROM_WINGDINGS.containsKey(symbol)) {
                latinSb.append(FROM_WINGDINGS.get(symbol));
            } else {
                latinSb.append(symbol);
            }
            i += Character.charCount(codePoint);
        }

        String latinStr = latinSb.toString();
        StringBuilder resultRu = new StringBuilder();
        int j = 0;

        while (j < latinStr.length()) {
            boolean matched = false;
            for (int len = 4; len >= 1; len--) {
                if (j + len <= latinStr.length()) {
                    String sub = latinStr.substring(j, j + len);
                    if (EN_TO_RU.containsKey(sub)) {
                        resultRu.append(EN_TO_RU.get(sub));
                        j += len;
                        matched = true;
                        break;
                    }
                }
            }
            if (!matched) {
                resultRu.append(latinStr.charAt(j));
                j++;
            }
        }
        return resultRu.toString();
    }

    private static void initializeWingdingsMaps() {
        Object[][] pairs = {
                {'a', "♋"}, {'b', "♌"}, {'c', "♍"}, {'d', "♎"}, {'e', "♏"},
                {'f', "♐"}, {'g', "♑"}, {'h', "♒"}, {'i', "♓"}, {'j', "🙵"},
                {'k', "🖂"}, {'l', "🖃"}, {'m', "🖄"}, {'n', "🖅"}, {'o', "🖆"},
                {'p', "🏱"}, {'q', "🏲"}, {'r', "🏳"}, {'s', "🏴"}, {'t', "🕈"},
                {'u', "🕉"}, {'v', "🕊"}, {'w', "🕋"}, {'x', "🕌"}, {'y', "🕍"},
                {'z', "🕎"}, {'?', "✍"}
        };
        for (Object[] pair : pairs) {
            TO_WINGDINGS.put((Character) pair[0], (String) pair[1]);
            FROM_WINGDINGS.put((String) pair[1], (Character) pair[0]);
        }
    }

    private static void initializeTranslitMaps() {
        Object[][] pairs = {
                {'а', "a"}, {'б', "b"}, {'в', "v"}, {'г', "g"}, {'д', "d"},
                {'е', "e"}, {'ё', "yo"}, {'ж', "zh"}, {'з', "z"}, {'и', "i"},
                {'й', "jj"}, {'к', "k"}, {'л', "l"}, {'м', "m"}, {'н', "n"},
                {'о', "o"}, {'п', "p"}, {'р', "r"}, {'с', "s"}, {'т', "t"},
                {'у', "u"}, {'ф', "f"}, {'х', "kh"}, {'ц', "ts"}, {'ч', "ch"},
                {'ш', "sh"}, {'щ', "shch"}, {'ы', "ih"}, {'э', "eh"}, {'ю', "yu"},
                {'я', "ya"}, {'ь', "'"}, {'ъ', "\""}
        };
        for (Object[] pair : pairs) {
            RU_TO_EN.put((Character) pair[0], (String) pair[1]);
            EN_TO_RU.put((String) pair[1], (Character) pair[0]);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
