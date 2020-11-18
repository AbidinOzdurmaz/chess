package net.abidinozdurmaz.chess.service;

import net.abidinozdurmaz.chess.chess.ChessBoard;
import net.abidinozdurmaz.chess.chess.Color;
import net.abidinozdurmaz.chess.chess.Piece;
import net.abidinozdurmaz.chess.chess.Square;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class MovePrediction {

    Random r=new Random();

    public ChessBoard changeBoard(ChessBoard chessBoard) {
        List<Square[][]> possibleBoards=new ArrayList<>();
        Square[][] temp = chessBoard.getSquares();

        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp.length; j++) {

                if (temp[i][j]!=null) {
                    if (chessBoard.getMoveOrder()==Color.WHITE) {

                        if (temp[i][j].getPiece() == Piece.PAWN && temp[i][j].getColor() == Color.WHITE) {

                            if (j == 1) {
                                Square[][] squares = new Square[8][8];
                                copySquareArray(squares, temp);
                                squares[i][j + 2] = temp[i][j];
                                squares[i][j] = null;
                                possibleBoards.add(squares);
                            }
                            Square[][] squares = new Square[8][8];
                            copySquareArray(squares, temp);

                            squares[i][j + 1] = temp[i][j];
                            squares[i][j] = null;
                            possibleBoards.add(squares);
                        }
                    }
                    else if (chessBoard.getMoveOrder()==Color.BLACK) {

                            if (temp[i][j].getPiece()==Piece.PAWN && temp[i][j].getColor()==Color.BLACK){

                                if (j==6){
                                    Square[][] squares=new Square[8][8];
                                    copySquareArray(squares,temp);
                                    squares[i][j-2] = temp[i][j];
                                    squares[i][j]=null;
                                    possibleBoards.add(squares);
                                }
                                Square[][] squares=new Square[8][8];
                                copySquareArray(squares,temp);
                                squares[i][j-1] = temp[i][j];
                                squares[i][j]=null;
                                possibleBoards.add(squares);
                            }
                        }

                }
            }
        }

        chessBoard.setSquares(guess(possibleBoards));
        chessBoard.setMoveOrder(changeMoveOrder(chessBoard.getMoveOrder()));
        return chessBoard;
    }

    public Square[][] guess(List<Square[][]> possibleBoards){
        if (possibleBoards.size()>0){
            int index = r.nextInt(possibleBoards.size());
            return possibleBoards.get(index);
        }
        return null;
    }

    public Square[][] copySquareArray(Square[][] x,Square[][] y){
        for (int j = 0; j < x.length; j++) {
            for (int k = 0; k < x.length; k++) {
                x[j][k] = y[j][k];
            }
        }
        return x;
    }

    //hamle sırasını değiştirir
    public Color changeMoveOrder(Color color){
        if (color==Color.BLACK){
            return Color.WHITE;
        }
        return Color.BLACK;
    }

}
