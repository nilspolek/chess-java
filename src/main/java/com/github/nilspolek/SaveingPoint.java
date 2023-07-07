package com.github.nilspolek;

import java.util.Arrays;

public record SaveingPoint(int[] board, boolean isWhite, boolean[] movedPices) {
    public SaveingPoint(int[] board, boolean isWhite, boolean[] movedPices){
        this.board = Arrays.copyOf(board,board.length);
        this.isWhite = isWhite;
        this.movedPices = Arrays.copyOf(movedPices,movedPices.length);
    }

    @Override
    public String toString() {
        return "&\"board\":"+Arrays.toString(board)+"|\"isWhit\":"+isWhite+"|\"movedPices\":"+Arrays.toString(movedPices);
    }
}
