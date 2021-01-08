package com.palteam.shabbik.solverAlgorithm;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class TreeWord extends TreeSet<String> {
    public TreeWord(Collection<String> c) {
        super(c);
    }

    public boolean isPrefix(String prefix) {
        String nextWord = ceiling(prefix);

        if (nextWord == null) {
            return false;
        }
        if (nextWord.equals(prefix)) {
            Set<String> tail = tailSet(nextWord, true);
            if (tail.isEmpty()) {
                return false;
            }
            nextWord = tail.iterator().next();

            //return true;
        }
        return nextWord.startsWith(prefix);
    }

    /**
     * There is a mismatch between the parameter types of vocabulary and TreeSet, so
     * force call to the upper-class method
     */
    public boolean contains(String word) {
        return super.contains(word);
    }

}
