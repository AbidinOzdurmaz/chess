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

                    if (chessBoard.getMoveOrder()==Color.WHITE &&
                            temp[i][j].getPiece() == Piece.PAWN &&
                            temp[i][j].getColor() == Color.WHITE &&
                            j!=7) {

                        //piyon 2 kare gitme
                        //eğer j=1 ise ve 2 önü boşsa gidebilir
                            if (j == 1 && temp[i][j+2]==null) {
                                Square[][] squares = new Square[8][8];
                                squares=copySquareArray(squares, temp);
                                squares[i][j + 2] = temp[i][j];
                                squares[i][j] = null;
                                possibleBoards.add(squares);
                            }

                            //piyon sağ çapraza yeme i = 7 olursa arrayden dışarı çıkar
                            //çaprazında bir taş varsa yiyebilir ve o taş rakibinse
                            if (i!=7 && temp[i+1][j+1]!=null && temp[i+1][j+1].getColor()==Color.BLACK){

                                    Square[][] squares = new Square[8][8];
                                    squares=copySquareArray(squares, temp);

                                    //vezir olma durumu kontrol ediliyor
                                    if(j==6){
                                        squares[i+1][j + 1] = new Square(Piece.QUEEN,Color.WHITE);
                                        squares[i][j]=null;
                                        possibleBoards.add(squares);
                                        continue;
                                    }

                                    squares[i+1][j+1] = temp[i][j];
                                    squares[i][j] = null;
                                    possibleBoards.add(squares);

                            }

                            //piyon sol çapraza yeme i = 0 olursa arrayden dışarı çıkar
                            //çaprazında bir taş varsa yiyebilir ve o taş rakibinse
                            if (i!=0 && temp[i-1][j+1]!=null && temp[i-1][j+1].getColor()==Color.BLACK){

                                        Square[][] squares = new Square[8][8];
                                        squares=copySquareArray(squares, temp);

                                        //vezir olma durumu kontrol ediliyor
                                        if(j==6){
                                            squares[i-1][j + 1] = new Square(Piece.QUEEN,Color.WHITE);
                                            squares[i][j]=null;
                                            possibleBoards.add(squares);
                                            continue;
                                        }

                                        squares[i-1][j+1] = temp[i][j];
                                        squares[i][j] = null;
                                        possibleBoards.add(squares);
                            }


                            //eğer önü boşsa gidebilir

                            if (temp[i][j+1]==null){

                                Square[][] squares = new Square[8][8];
                                squares=copySquareArray(squares, temp);

                                //eğer son kareye gelmişse vezire dönüşebilir
                                if(j==6){
                                    squares[i][j + 1] = new Square(Piece.QUEEN,Color.WHITE);
                                    squares[i][j]=null;
                                    possibleBoards.add(squares);
                                    continue;
                                }

                                squares[i][j + 1] = temp[i][j];
                                squares[i][j] = null;
                                possibleBoards.add(squares);
                            }
                    }


                    else if (chessBoard.getMoveOrder()==Color.BLACK &&
                            temp[i][j].getPiece()==Piece.PAWN && temp[i][j].getColor()==Color.BLACK) {

                                //piyon 2 kare gitme
                                //eğer j=6 ise ve 2 önü boşsa gidebilir
                                if (j==6 && temp[i][j-2]==null){
                                    Square[][] squares=new Square[8][8];
                                    squares=copySquareArray(squares, temp);
                                    squares[i][j-2] = temp[i][j];
                                    squares[i][j]=null;
                                    possibleBoards.add(squares);
                                }

                                //piyon siyaha göre sağ çapraza yeme i = 0 olursa arrayden dışarı çıkar
                                //çaprazında bir taş varsa yiyebilir ve o taş rakibinse
                                if (i!=0 && temp[i-1][j-1]!=null && temp[i-1][j-1].getColor()==Color.WHITE){

                                        Square[][] squares = new Square[8][8];
                                        squares=copySquareArray(squares, temp);

                                        //vezir olma durumu kontrol ediliyor
                                        if(j==1){
                                            squares[i-1][j - 1] = new Square(Piece.QUEEN,Color.BLACK);
                                            squares[i][j]=null;
                                            possibleBoards.add(squares);
                                            continue;
                                        }


                                        squares[i-1][j-1] = temp[i][j];
                                        squares[i][j] = null;
                                        possibleBoards.add(squares);

                                }
                                //piyon siyaha göre sol çapraza yeme i = 7 olursa arrayden dışarı çıkar
                                if (i!=7 && temp[i+1][j-1]!=null && temp[i+1][j-1].getColor()==Color.WHITE){

                                        Square[][] squares = new Square[8][8];
                                        squares=copySquareArray(squares, temp);

                                        //vezir olma durumu kontrol ediliyor
                                        if(j==1){
                                            squares[i+1][j - 1] = new Square(Piece.QUEEN,Color.BLACK);
                                            squares[i][j]=null;
                                            possibleBoards.add(squares);
                                            continue;
                                        }

                                        squares[i+1][j-1] = temp[i][j];
                                        squares[i][j] = null;
                                        possibleBoards.add(squares);

                                }

                                //eğer piyonun önü boşsa
                                if(temp[i][j-1]==null){
                                    Square[][] squares=new Square[8][8];
                                    squares=copySquareArray(squares, temp);

                                    //eğer son kareye gelmişse vezire dönüşebilir

                                    if(j==1){
                                        squares[i][j - 1] = new Square(Piece.QUEEN,Color.BLACK);
                                        squares[i][j]=null;
                                        possibleBoards.add(squares);
                                        continue;
                                    }
                                    squares[i][j-1] = temp[i][j];
                                    squares[i][j]=null;
                                    possibleBoards.add(squares);
                                }
                        }
                }
            }
        }

        chessBoard.setSquares(guess(possibleBoards));
        return chessBoard;
    }

    public Square[][] guess(List<Square[][]> possibleBoards){
        if (possibleBoards.size()>0){
            int index = r.nextInt(possibleBoards.size());
            System.out.println("index değeri " +index);
            System.out.println("Döndüreceği veri : "+possibleBoards.get(index));
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
    public Color changeMoveOrder(Color color){
        if (color==Color.BLACK){
            return Color.WHITE;
        }
        return Color.BLACK;
    }

}
