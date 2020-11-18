package net.abidinozdurmaz.chess.service;


import net.abidinozdurmaz.chess.chess.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class MovePrediction {

    Random r=new Random();

    public ChessBoard changeBoard(ChessBoard chessBoard){
        List<Square[][]> possibleBoards2=new ArrayList<>();
        Square[][] temp = chessBoard.getSquares();

        for (int i = 0; i < temp.length; i++) {

            //piyonun bir adım önce çıkması

            Square[][] squares = new Square[8][8];

            squares= copySquareArray(squares,temp);

            squares[i][2]=temp[i][1];

            squares[i][1]=null;

            possibleBoards2.add(squares);

            //piyonun 2 adım öne çıkması

            Square[][] squares2 = new Square[8][8];

            squares2= copySquareArray(squares2,temp);

            squares2[i][3]=temp[i][1];

            squares2[i][1]=null;

            possibleBoards2.add(squares2);
        }

        chessBoard.setSquares(guess(possibleBoards2));
        chessBoard.setMoveOrder(changeMoveOrder(chessBoard.getMoveOrder()));
        return chessBoard;
    }

    //rastgele square döndürür
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
