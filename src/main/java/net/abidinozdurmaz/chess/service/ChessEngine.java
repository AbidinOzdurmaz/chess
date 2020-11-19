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
public class ChessEngine {

    Random r=new Random();
    List<Square[][]> possibleBoards;


    public ChessBoard move(ChessBoard chessBoard) {

        possibleBoards=new ArrayList<>();
        Square[][] initalState = chessBoard.getSquares();
        boolean temp=true;

        for (int i = 0; i < initalState.length; i++) {
            for (int j = 0; j < initalState.length; j++) {

                if (initalState[i][j]!=null) {

                    //burada eğer taşımız siyahsa simetri alıyoruz. Döngü içerisinde yeniden çalışmasın diye
                    //temp adında geçici bir boolean kullandık

                    if (temp && chessBoard.getMoveOrder()==Color.BLACK ){
                        chessBoard.setSquares(simetriAl(chessBoard.getSquares()));
                        initalState=chessBoard.getSquares();
                        temp=false;
                    }

                    if (chessBoard.getMoveOrder()==initalState[i][j].getColor()) {

                        switch (initalState[i][j].getPiece()){

                            case PAWN:{

                                if (j!=7){

                                    //piyon 2 adım ilerleme
                                    if (j == 1 && initalState[i][j+2]==null && initalState[i][j+1]==null) {

                                        Square[][] squares=chessBoard.cloneBoard();

                                        squares[i][j + 2] = initalState[i][j];
                                        squares[i][j] = null;

                                        addPossibleBoards(chessBoard.getMoveOrder(),squares);
                                    }

                                    //piyon sağ çapraza yeme i = 7 olursa arrayden dışarı çıkar
                                    //çaprazında bir taş varsa yiyebilir ve o taş rakibinse
                                    if (i!=7 && initalState[i+1][j+1]!=null && initalState[i+1][j+1].getColor()==Color.BLACK){

                                        Square[][] squares=chessBoard.cloneBoard();


                                        //vezir olma durumu kontrol ediliyor
                                        if(j==6){
                                            squares[i+1][j + 1] = new Square(Piece.QUEEN,Color.WHITE);
                                            squares[i][j]=null;
                                            addPossibleBoards(chessBoard.getMoveOrder(),squares);
                                        }

                                        else{
                                            squares[i+1][j+1] = initalState[i][j];
                                            squares[i][j] = null;
                                            addPossibleBoards(chessBoard.getMoveOrder(),squares);
                                        }


                                    }

                                    //piyon sol çapraza yeme i = 0 olursa arrayden dışarı çıkar
                                    //çaprazında bir taş varsa yiyebilir ve o taş rakibinse
                                    if (i!=0 && initalState[i-1][j+1] != null && initalState[i-1][j+1].getColor()==Color.BLACK){

                                        Square[][] squares=chessBoard.cloneBoard();


                                        //vezir olma durumu kontrol ediliyor
                                        if(j==6){
                                            squares[i-1][j + 1] = new Square(Piece.QUEEN,Color.WHITE);
                                            squares[i][j]=null;
                                            addPossibleBoards(chessBoard.getMoveOrder(),squares);
                                        }

                                        else{
                                            squares[i-1][j+1] = initalState[i][j];
                                            squares[i][j] = null;
                                            addPossibleBoards(chessBoard.getMoveOrder(),squares);
                                        }

                                    }

                                    //eğer önü boşsa gidebilir

                                    if (initalState[i][j+1]==null){

                                        Square[][] squares=chessBoard.cloneBoard();


                                        //eğer son kareye gelmişse vezire dönüşebilir
                                        if(j==6){
                                            squares[i][j + 1] = new Square(Piece.QUEEN,Color.WHITE);
                                            squares[i][j]=null;
                                            addPossibleBoards(chessBoard.getMoveOrder(),squares);
                                        }
                                        else{
                                            squares[i][j + 1] = initalState[i][j];
                                            squares[i][j] = null;
                                            addPossibleBoards(chessBoard.getMoveOrder(),squares);
                                        }
                                    }
                                }


                                break;
                            }
                        }

                    }
                }
            }
        }
        chessBoard.setSquares(guess(possibleBoards));
        return chessBoard;
    }

    private Square[][] guess(List<Square[][]> possibleBoards){
        if (possibleBoards.size()>0){
            int index = r.nextInt(possibleBoards.size());
            System.out.println("index değeri " +index);
            System.out.println("Döndüreceği veri : "+possibleBoards.get(index));
            return possibleBoards.get(index);
        }
        return null;
    }

    private Square[][] simetriAl(Square[][] squares){

        Square[][] sq= new Square[8][8];

        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares.length; j++) {
                sq[i][j]=squares[7-i][7-j];
            }
        }
        return sq;
    }

    private void addPossibleBoards(Color color,Square[][] squares){

        if (color==Color.BLACK){
            possibleBoards.add(simetriAl(squares));
        }
        else {
            possibleBoards.add(squares);
        }

    }

}
