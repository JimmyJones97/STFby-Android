package com.xzy.forestSystem.baidu.voicerecognition.android;

import java.io.Serializable;

public final class Candidate implements Serializable, NoProGuard {
    private static final long serialVersionUID = 1;
    final double belief;
    private String wordString;

    public void setWord(String word) {
        this.wordString = word;
    }

    public String getWord() {
        return this.wordString;
    }

    public double getBelief() {
        return this.belief;
    }

    public Candidate(String candidateWord, double beliefDegree) {
        this.wordString = candidateWord;
        this.belief = beliefDegree;
    }

    @Override // java.lang.Object
    public String toString() {
        return "[" + this.wordString + "," + this.belief + "]";
    }
}
