package spell;

import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
public class SpellCorrector implements ISpellCorrector {
    Trie dictionary;
    ArrayList<String> editOne;

    public SpellCorrector() {
        this.dictionary = new Trie();
        this.editOne = new ArrayList<>();
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        Scanner sc = new Scanner(new File(dictionaryFileName));
        while (sc.hasNext()){
            dictionary.add(sc.next());
        }
        System.out.println("Node count: "+dictionary.getNodeCount());
        System.out.println("Word count: "+dictionary.getWordCount());
        System.out.println(dictionary.toString());
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        if (dictionary.find(inputWord)!=null){
            return inputWord.toLowerCase();
        }
        return getSimilarWord(inputWord.toLowerCase());
    }

    public String getSimilarWord (String input) {
        editOne.addAll(deletion(input));
        editOne.addAll(transposition(input));
        editOne.addAll(alteration(input));
        editOne.addAll(insertion(input));

        int max = 0;
        String ret = null;
        for (int i = 0; i < editOne.size();i++) {
            TrieNode n = (TrieNode)dictionary.find(editOne.get(i));
            if (n!=null) {
                if (n.getValue() > max) {
                    ret = editOne.get(i);
                    max = n.getValue();
                } else if (n.getValue() == max && max != 0) {
                    if (ret.compareTo(editOne.get(i)) > 0) {
                        ret = editOne.get(i);
                    }
                }
            }
        }
        if (ret == null) {
            ret = editDistanceTwo();
        }
        editOne.clear();
        return ret;
    }

    public String editDistanceTwo() {
        ArrayList<String> secondEdits = new ArrayList<>();
        for (int i = 0; i < editOne.size();i++) {
            secondEdits.addAll(deletion(editOne.get(i)));
            secondEdits.addAll(transposition(editOne.get(i)));
            secondEdits.addAll(alteration(editOne.get(i)));
            secondEdits.addAll(insertion(editOne.get(i)));
        }
        int max = 0;
        String ret = null;
        for (int i = 0; i < secondEdits.size();i++) {
            TrieNode n = (TrieNode)dictionary.find(secondEdits.get(i));
            if (n!=null) {
                if (n.getValue() > max) {
                    ret = secondEdits.get(i);
                    max = n.getValue();
                } else if (n.getValue() == max && max != 0) {
                    if (ret.compareTo(secondEdits.get(i)) > 0) {
                        ret = secondEdits.get(i);
                    }
                }
            }
        }
        return ret;
    }

    public ArrayList<String> deletion (String input) {
        ArrayList<String> ret = new ArrayList<>();
        for (int i = 0; i < input.length();i++) {
            StringBuilder temp = new StringBuilder(input);
            temp.deleteCharAt(i);
            ret.add(temp.toString());
        }
        return ret;
    }

    public ArrayList<String> transposition (String input) {
        ArrayList<String> ret = new ArrayList<>();
        for (int i = 0; i < input.length()-1;i++) {
            StringBuilder temp = new StringBuilder(input);
            char hold = input.charAt(i+1);
            temp.setCharAt(i+1,temp.charAt(i));
            temp.setCharAt(i,hold);
            ret.add(temp.toString());
        }
        return ret;
    }

    public ArrayList<String> alteration (String input) {
        ArrayList<String> ret = new ArrayList<>();
        for (int i = 0; i < input.length();i++) {
            for (int j = 0; j < 26; j++) {
                StringBuilder temp = new StringBuilder(input);
                char add = (char) (j+'a');
                temp.setCharAt(i,add);
                ret.add(temp.toString());
            }
        }
        return ret;
    }

    public ArrayList<String> insertion (String input) {
        ArrayList<String> ret = new ArrayList<>();
        for (int i = 0; i <= input.length();i++) {
            for (int j = 0; j < 26; j++) {
                StringBuilder temp = new StringBuilder(input);
                char add = (char)(j+'a');
                temp.insert(i,add);
                ret.add(temp.toString());
            }
        }
        return ret;
    }
}
