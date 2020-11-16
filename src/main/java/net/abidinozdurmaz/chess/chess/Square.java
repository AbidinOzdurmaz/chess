package net.abidinozdurmaz.chess.chess;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Square {

    private Piece piece;
    private PieceColor pieceColor;
    private int x;
    private int y;


}
