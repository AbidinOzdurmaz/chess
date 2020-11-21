package net.abidinozdurmaz.chess.chess;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChessBoard {
    private Square[][] squares;
    private Color moveOrder;


    public Square[][] startingSequence() {

        squares = new Square[8][8];
        squares[0][0] = new Square(Piece.ROOK, Color.WHITE);
        squares[1][0] = new Square(Piece.KNIGHT, Color.WHITE);
        squares[2][0] = new Square(Piece.BISHOP, Color.WHITE);
        squares[3][0] = new Square(Piece.QUEEN, Color.WHITE);
        squares[4][0] = new Square(Piece.KING, Color.WHITE);
        squares[5][0] = new Square(Piece.BISHOP, Color.WHITE);
        squares[6][0] = new Square(Piece.KNIGHT, Color.WHITE);
        squares[7][0] = new Square(Piece.ROOK, Color.WHITE);
        squares[0][1] = new Square(Piece.PAWN, Color.WHITE);
        squares[1][1] = new Square(Piece.PAWN, Color.WHITE);
        squares[2][1] = new Square(Piece.PAWN, Color.WHITE);
        squares[3][1] = new Square(Piece.PAWN, Color.WHITE);
        squares[4][1] = new Square(Piece.PAWN, Color.WHITE);
        squares[5][1] = new Square(Piece.PAWN, Color.WHITE);
        squares[6][1] = new Square(Piece.PAWN, Color.WHITE);
        squares[7][1] = new Square(Piece.PAWN, Color.WHITE);


        squares[0][6] = new Square(Piece.PAWN, Color.BLACK);
        squares[1][6] = new Square(Piece.PAWN, Color.BLACK);
        squares[2][6] = new Square(Piece.PAWN, Color.BLACK);
        squares[3][6] = new Square(Piece.PAWN, Color.BLACK);
        squares[4][6] = new Square(Piece.PAWN, Color.BLACK);
        squares[5][6] = new Square(Piece.PAWN, Color.BLACK);
        squares[6][6] = new Square(Piece.PAWN, Color.BLACK);
        squares[7][6] = new Square(Piece.PAWN, Color.BLACK);
        squares[0][7] = new Square(Piece.ROOK, Color.BLACK);
        squares[1][7] = new Square(Piece.KNIGHT, Color.BLACK);
        squares[2][7] = new Square(Piece.BISHOP, Color.BLACK);
        squares[3][7] = new Square(Piece.QUEEN, Color.BLACK);
        squares[4][7] = new Square(Piece.KING, Color.BLACK);
        squares[5][7] = new Square(Piece.BISHOP, Color.BLACK);
        squares[6][7] = new Square(Piece.KNIGHT, Color.BLACK);
        squares[7][7] = new Square(Piece.ROOK, Color.BLACK);
        return squares;
    }

    public Square[][] cloneBoard() {
        Square[][] squares = new Square[8][8];
        for (int j = 0; j < squares.length; j++) {
            for (int k = 0; k < squares.length; k++) {
                squares[j][k] = this.squares[j][k];
            }
        }
        return squares;
    }


}
