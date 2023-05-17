package com.libertas.generics;

import com.libertas.Main;

import java.util.List;

public class Region {
    public int lineStart;
    public int lineEnd;
    public int charStart;
    public int charEnd;
    public boolean isEmpty;

    public Region(int lineStart, int lineEnd, int charStart, int charEnd) {
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.charStart = charStart;
        this.charEnd = charEnd;
        this.isEmpty = false;
    }

    public Region() {
        this.isEmpty = true;
    }
    public Region(int line, int character) {
        this.lineStart = line;
        this.lineEnd = line;
        this.charStart = character;
        this.charEnd = character;
        this.isEmpty = false;
    }

    @Override
    public String toString() {
        if (isEmpty) {
            return "Region{}";
        }
        return "Region{" +
                "lines: " + lineStart + "-" + lineEnd +
                ", characters" + charStart +"-" + charEnd;
    }

    public Region combine(Region other) {
        return new Region(Math.min(lineStart, other.lineStart), Math.max(lineEnd, other.lineEnd), Math.min(charStart, other.charStart), Math.max(charEnd, other.charEnd));
    }

    public Region combine(List<Region> other) {
        Region result = this;

        for (Region region: other) {
            result = region.combine(result);
        }

        return  result;
    }
}
