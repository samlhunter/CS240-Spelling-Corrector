package spell;

import java.util.Objects;

public class Trie implements ITrie {
    int nodeCount;
    int wordCount;
    TrieNode root;
    public Trie() {
        this.nodeCount=1;
        this.wordCount=0;
        this.root = new TrieNode();
    }
    @Override
    public void add(String word) {
        add_helper(root,word.toLowerCase());
    }

    public void add_helper(TrieNode n, String word) {
        if (word.equals("")){
            if(n.value==0){
                wordCount++;
            }
            n.incrementValue();
        }
        //know there is more to add
        else {
            char charToAdd = word.charAt(0);
            String next = word.substring(1);
            int index = charToAdd-'a';
            if (n.getChildren()[index]==null) {
                n.addChild(index);
                nodeCount++;
            }
            TrieNode child = (TrieNode)n.getChildren()[index];
            add_helper(child,next);
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        StringBuilder cur = new StringBuilder();
        toString_helper(root,cur,out);
        return out.toString();
    }

    public void toString_helper(TrieNode n, StringBuilder cur, StringBuilder out) {
        if (n.getValue()>0) {
            //we have a word to append
            out.append(cur);
            out.append("\n");
        }
        for (int i = 0; i < n.getChildren().length;i++) {
            INode child = n.getChildren()[i];
            if (child != null) {
                char add = (char) (i + 'a');
                cur.append(add);

                toString_helper((TrieNode)child, cur, out);

                cur.deleteCharAt(cur.length() - 1);
            }
        }
    }

    @Override
    public INode find(String word) {
        return find_helper(root,word.toLowerCase());
    }

    public INode find_helper(TrieNode n, String word) {
        if (word.equals("")){
            if (n.getValue()>0){
                return n;
            }
            else {
                return null;
            }
        }
        else {
            char nextChar = word.charAt(0);
            String nextWord = word.substring(1);
            int index = nextChar-'a';
            TrieNode nextNode = (TrieNode)n.getChildren()[index];
            if (nextNode==null){
                return null;
            }
            else {
                return (find_helper(nextNode, nextWord));
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Trie other = (Trie) o;
        if ((this.nodeCount!=other.getNodeCount())||(this.wordCount!=other.getWordCount())){
            return false;
        }
        else {
            return equals_helper(this.root,other.root);
        }
    }

    public boolean equals_helper (TrieNode n1, TrieNode n2) {
        if (n1.getValue()!=n2.getValue()) {
            return false;
        }
        else {
            for (int i = 0; i < 26; i++) {
                if ((n1.getChildren()[i]==null && n2.getChildren()[i]!=null)||(n1.getChildren()[i]!=null && n2.getChildren()[i]==null)){
                    return false;
                }
                else if (n1.getChildren()[i]!=null && n2.getChildren()[i]!=null){
                    if (!equals_helper((TrieNode)n1.getChildren()[i],(TrieNode)n2.getChildren()[i])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (nodeCount*wordCount+sumNonNullRoot());
    }

    public int sumNonNullRoot() {
        int sum = 0;
        for (int i = 0; i < 26; i++) {
            if (root.getChildren()[i]!=null) {
                sum+=i;
            }
        }
        return sum;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }
}
