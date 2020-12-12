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

    private final Random r = new Random();
    private List<Square[][]> possibleBoards;
    private ChessBoard chessBoard;

    public ChessBoard move(ChessBoard chessBoard) {

        //burada eğer taşımız siyahsa simetri alıyoruz.
        if (chessBoard.getMoveOrder() == Color.BLACK) {
            chessBoard.setSquares(spinBoard(chessBoard.getSquares()));
        }
        possibleBoards = new ArrayList<>();
        this.chessBoard = chessBoard;


        for (int i = 0; i < chessBoard.getSquares().length; i++) {
            for (int j = 0; j < chessBoard.getSquares().length; j++) {

                if (chessBoard.getSquares()[i][j] != null) {

                    if (chessBoard.getMoveOrder() == chessBoard.getSquares()[i][j].getColor()) {

                        switch (chessBoard.getSquares()[i][j].getPiece()) {

                            case PAWN: {
                                possibleBoards.addAll(calculatePawnMoves(i, j));
                                break;
                            }

                            case KING: {
                                possibleBoards.addAll(calculateKingMoves(i, j));
                                break;
                            }

                            case KNIGHT: {
                                possibleBoards.addAll(calculateKnightMoves(i, j));
                                break;
                            }

                            case ROOK: {
                                possibleBoards.addAll(calculateRookMoves(i, j));
                                break;
                            }

                            case BISHOP: {
                                possibleBoards.addAll(calculateBishopMoves(i, j));
                                break;
                            }

                            case QUEEN: {
                                possibleBoards.addAll(calculateQueenMoves(i, j));
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

    private List<Square[][]> calculatePawnMoves(int i, int j) {

        List<Square[][]> pawnPossibleBoards = new ArrayList<>();

        if (j != 7) {

            //piyon 2 adım ilerleme
            if (j == 1 && chessBoard.getSquares()[i][j + 2] == null && chessBoard.getSquares()[i][j + 1] == null) {

                Square[][] squares = chessBoard.cloneBoard();

                squares[i][j + 2] = chessBoard.getSquares()[i][j];
                squares[i][j] = null;
                pawnPossibleBoards.add(squares);
            }

            //piyon sağ çapraza yeme i = 7 olursa arrayden dışarı çıkar
            //çaprazında bir taş varsa yiyebilir ve o taş rakibinse
            if (i != 7 && chessBoard.getSquares()[i + 1][j + 1] != null && chessBoard.getSquares()[i + 1][j + 1].getColor() != chessBoard.getMoveOrder()) {

                Square[][] squares = chessBoard.cloneBoard();

                //vezir olma durumu kontrol ediliyor
                if (j == 6) {
                    squares[i + 1][j + 1] = new Square(Piece.QUEEN, chessBoard.getMoveOrder());
                    squares[i][j] = null;
                    pawnPossibleBoards.add(squares);
                } else {
                    squares[i + 1][j + 1] = chessBoard.getSquares()[i][j];
                    squares[i][j] = null;
                    pawnPossibleBoards.add(squares);
                }
            }
            //piyon sol çapraza yeme i = 0 olursa arrayden dışarı çıkar
            //çaprazında bir taş varsa yiyebilir ve o taş rakibinse
            if (i != 0 && chessBoard.getSquares()[i - 1][j + 1] != null && chessBoard.getSquares()[i - 1][j + 1].getColor() != chessBoard.getMoveOrder()) {

                Square[][] squares = chessBoard.cloneBoard();

                //vezir olma durumu kontrol ediliyor
                if (j == 6) {
                    squares[i - 1][j + 1] = new Square(Piece.QUEEN, chessBoard.getMoveOrder());
                    squares[i][j] = null;
                    pawnPossibleBoards.add(squares);
                } else {
                    squares[i - 1][j + 1] = chessBoard.getSquares()[i][j];
                    squares[i][j] = null;
                    pawnPossibleBoards.add(squares);
                }
            }

            //eğer önü boş ise gidebilir

            if (chessBoard.getSquares()[i][j + 1] == null) {

                Square[][] squares = chessBoard.cloneBoard();

                //eğer son kareye gelmişse vezire dönüşebilir
                if (j == 6) {
                    squares[i][j + 1] = new Square(Piece.QUEEN, chessBoard.getMoveOrder());
                    squares[i][j] = null;
                    pawnPossibleBoards.add(squares);
                } else {
                    squares[i][j + 1] = chessBoard.getSquares()[i][j];
                    squares[i][j] = null;
                    pawnPossibleBoards.add(squares);
                }
            }
        }

        if (chessBoard.getMoveOrder() == Color.BLACK) {
            return spinBoard(pawnPossibleBoards);
        }
        return pawnPossibleBoards;
    }

    private List<Square[][]> calculateKingMoves(int i, int j) {
        List<Square[][]> kingPossibleBoards = new ArrayList<>();

        //şah bir hamle öne gitme
        //taş yeme durumu kontrol ediliyor
        if (j != 7 &&
                (chessBoard.getSquares()[i][j + 1] == null ||
                        chessBoard.getSquares()[i][j + 1].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i][j + 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            kingPossibleBoards.add(squares);
        }
        //şah bir hamle geriye gitme
        //taş yeme durumu kontrol ediliyor
        if (j != 0 &&
                (chessBoard.getSquares()[i][j - 1] == null ||
                        chessBoard.getSquares()[i][j - 1].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i][j - 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            kingPossibleBoards.add(squares);
        }
        //şah bir hamle sola gitme
        //taş yeme durumu kontrol ediliyor
        if (i != 0 &&
                (chessBoard.getSquares()[i - 1][j] == null ||
                        chessBoard.getSquares()[i - 1][j].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i - 1][j] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            kingPossibleBoards.add(squares);
        }
        //şah bir hamle sağa gitme
        //taş yeme durumu kontrol ediliyor
        if (i != 7 &&
                (chessBoard.getSquares()[i + 1][j] == null ||
                        chessBoard.getSquares()[i + 1][j].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i + 1][j] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            kingPossibleBoards.add(squares);
        }

        //şah sağ üst çapraz gitme
        //taş yeme durumu kontrol ediliyor
        if (i != 7 && j != 7 &&
                (chessBoard.getSquares()[i + 1][j + 1] == null ||
                        chessBoard.getSquares()[i + 1][j + 1].getColor() != chessBoard.getMoveOrder())) {
            Square[][] squares = chessBoard.cloneBoard();
            squares[i + 1][j + 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            kingPossibleBoards.add(squares);
        }
        //şah sol üst çapraz gitme
        //taş yeme durumu kontrol ediliyor
        if (i != 0 && j != 7 &&
                (chessBoard.getSquares()[i - 1][j + 1] == null ||
                        chessBoard.getSquares()[i - 1][j + 1].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i - 1][j + 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            kingPossibleBoards.add(squares);
        }
        //şah sağ alt çapraz gitme
        //taş yeme durumu kontrol ediliyor
        if (i != 7 && j != 0 &&
                (chessBoard.getSquares()[i + 1][j - 1] == null ||
                        chessBoard.getSquares()[i + 1][j - 1].getColor() != chessBoard.getMoveOrder())) {
            Square[][] squares = chessBoard.cloneBoard();
            squares[i + 1][j - 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            kingPossibleBoards.add(squares);
        }
        //şah sol alt çapraz gitme
        //taş yeme durumu kontrol ediliyor
        if (i != 0 && j != 0 &&
                (chessBoard.getSquares()[i - 1][j - 1] == null ||
                        chessBoard.getSquares()[i - 1][j - 1].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i - 1][j - 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            kingPossibleBoards.add(squares);
        }

        if (chessBoard.getMoveOrder() == Color.BLACK) {
            return spinBoard(kingPossibleBoards);
        }
        return kingPossibleBoards;
    }

    private List<Square[][]> calculateKnightMoves(int i, int j) {

        List<Square[][]> knightPossibleBoards = new ArrayList<>();

        //at 2 sağa 1 yukarı hareketi
        //burada null kontrolü yapmamızın nedeni bir sonraki kontrol de null pointer exception almamak için
        //bir sonraki kontrol ise eğer konumda kendi taşı varsa yememesi için yapılmıştır
        if (i < 6 && j != 7 &&
                (chessBoard.getSquares()[i + 2][j + 1] == null ||
                        chessBoard.getSquares()[i + 2][j + 1].getColor() != chessBoard.getMoveOrder())) {
            Square[][] squares = chessBoard.cloneBoard();
            squares[i + 2][j + 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            knightPossibleBoards.add(squares);
        }

        //at 2 sağa 1 aşağı hareketi
        //burada null kontrolü yapmamızın nedeni bir sonraki kontrol de null pointer exception almamak için
        //bir sonraki kontrol ise eğer konumda kendi taşı varsa yememesi için yapılmıştır
        if (i < 6 && j != 0 &&
                (chessBoard.getSquares()[i + 2][j - 1] == null ||
                        chessBoard.getSquares()[i + 2][j - 1].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i + 2][j - 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            knightPossibleBoards.add(squares);
        }

        //at 2 sola 1 yukarı hareketi
        //burada null kontrolü yapmamızın nedeni bir sonraki kontrol de null pointer exception almamak için
        //bir sonraki kontrol ise eğer konumda kendi taşı varsa yememesi için yapılmıştır
        if (i > 1 && j != 7 &&
                (chessBoard.getSquares()[i - 2][j + 1] == null ||
                        chessBoard.getSquares()[i - 2][j + 1].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i - 2][j + 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            knightPossibleBoards.add(squares);
        }

        //at 2 sola 1 aşağı hareketi
        //burada null kontrolü yapmamızın nedeni bir sonraki kontrol de null pointer exception almamak için
        //bir sonraki kontrol ise eğer konumda kendi taşı varsa yememesi için yapılmıştır
        if (i > 1 && j != 0 &&
                (chessBoard.getSquares()[i - 2][j - 1] == null ||
                        chessBoard.getSquares()[i - 2][j - 1].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i - 2][j - 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            knightPossibleBoards.add(squares);
        }

        //at 1 sağa 2 yukarı hareketi
        //burada null kontrolü yapmamızın nedeni bir sonraki kontrol de null pointer exception almamak için
        //bir sonraki kontrol ise eğer konumda kendi taşı varsa yememesi için yapılmıştır
        if (i != 7 && j < 6 &&
                (chessBoard.getSquares()[i + 1][j + 2] == null ||
                        chessBoard.getSquares()[i + 1][j + 2].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i + 1][j + 2] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            knightPossibleBoards.add(squares);

        }
        //at 1 sola 2 yukarı hareketi
        //burada null kontrolü yapmamızın nedeni bir sonraki kontrol de null pointer exception almamak için
        //bir sonraki kontrol ise eğer konumda kendi taşı varsa yememesi için yapılmıştır
        if (i != 0 && j < 6 &&
                (chessBoard.getSquares()[i - 1][j + 2] == null ||
                        chessBoard.getSquares()[i - 1][j + 2].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i - 1][j + 2] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            knightPossibleBoards.add(squares);
        }

        //at 1 sağa 2 aşağı hareketi
        //burada null kontrolü yapmamızın nedeni bir sonraki kontrol de null pointer exception almamak için
        //bir sonraki kontrol ise eğer konumda kendi taşı varsa yememesi için yapılmıştır
        if (i != 7 && j > 1 &&
                (chessBoard.getSquares()[i + 1][j - 2] == null ||
                        chessBoard.getSquares()[i + 1][j - 2].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i + 1][j - 2] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            knightPossibleBoards.add(squares);
        }

        //at 1 sola 2 aşağı hareketi
        //burada null kontrolü yapmamızın nedeni bir sonraki kontrol de null pointer exception almamak için
        //bir sonraki kontrol ise eğer konumda kendi taşı varsa yememesi için yapılmıştır
        if (i != 0 && j > 1 &&
                (chessBoard.getSquares()[i - 1][j - 2] == null ||
                        chessBoard.getSquares()[i - 1][j - 2].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i - 1][j - 2] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            knightPossibleBoards.add(squares);
        }

        if (chessBoard.getMoveOrder() == Color.BLACK) {
            return spinBoard(knightPossibleBoards);
        }
        return knightPossibleBoards;
    }

    private List<Square[][]> calculateRookMoves(int i, int j) {

        List<Square[][]> rookPossibleBoards = new ArrayList<>();

        //yukarı hamle
        moveUp(i, j, rookPossibleBoards);

        //aşağı hamle

        moveDown(i, j, rookPossibleBoards);

        //sağa hamle

        moveRight(i, j, rookPossibleBoards);

        //sola hamle

        moveLeft(i, j, rookPossibleBoards);


        if (chessBoard.getMoveOrder() == Color.BLACK) {
            return spinBoard(rookPossibleBoards);
        }
        return rookPossibleBoards;
    }

    private List<Square[][]> calculateBishopMoves(int i, int j) {
        List<Square[][]> bishopPossibleBoards = new ArrayList<>();

        //sağ yukarı çapraz hamle
        moveRightUpCross(i, j, bishopPossibleBoards);

        //sol yukarı çapraz hamle
        moveLeftUpCross(i, j, bishopPossibleBoards);

        //sağ aşağı çapraz hamle
        moveRightDownCross(i, j, bishopPossibleBoards);

        //sol aşağı çapraz hamle
        moveLeftDownCross(i, j, bishopPossibleBoards);


        if (chessBoard.getMoveOrder() == Color.BLACK) {
            return spinBoard(bishopPossibleBoards);
        }
        return bishopPossibleBoards;
    }

    private List<Square[][]> calculateQueenMoves(int i, int j) {
        List<Square[][]> queenPossibleBoards = new ArrayList<>();

        //yukarı hamle
        moveUp(i, j, queenPossibleBoards);

        //aşağı hamle

        moveDown(i, j, queenPossibleBoards);

        //sağa hamle

        moveRight(i, j, queenPossibleBoards);

        //sola hamle

        moveLeft(i, j, queenPossibleBoards);

        //sağ yukarı çapraz hamle
        moveRightUpCross(i, j, queenPossibleBoards);

        //sol yukarı çapraz hamle
        moveLeftUpCross(i, j, queenPossibleBoards);

        //sağ aşağı çapraz hamle
        moveRightDownCross(i, j, queenPossibleBoards);

        //sol aşağı çapraz hamle
        moveLeftDownCross(i, j, queenPossibleBoards);

        if (chessBoard.getMoveOrder() == Color.BLACK) {
            return spinBoard(queenPossibleBoards);
        }
        return queenPossibleBoards;
    }

    private Square[][] guess(List<Square[][]> possibleBoards) {
        if (possibleBoards.size() > 0) {
            int index = r.nextInt(possibleBoards.size());
            System.out.println("index değeri " + index);
            System.out.println("Döndüreceği veri : " + possibleBoards.get(index));
            return possibleBoards.get(index);
        }
        return null;
    }

    private List<Square[][]> spinBoard(List<Square[][]> squares) {

        List<Square[][]> spinBoardList = new ArrayList<>();

        for (Square[][] square : squares) {

            Square[][] sq = new Square[8][8];

            for (int i = 0; i < sq.length; i++) {
                for (int j = 0; j < 8; j++) {
                    sq[i][j] = square[7 - i][7 - j];
                }
            }
            spinBoardList.add(sq);
        }
        return spinBoardList;
    }

    private Square[][] spinBoard(Square[][] squares) {

        Square[][] sq = new Square[8][8];

        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares.length; j++) {
                sq[i][j] = squares[7 - i][7 - j];
            }
        }
        return sq;
    }

    private void moveUp(int i, int j, List<Square[][]> possibleBoards) {

        /** tahtadan çıkmaması için kontrol yapılıyor */
        
        if (j != 7) {

            /** yukarıya yapabileceği tüm hareketleri döngü sayesinde geziyoruz */
            for (int k = j + 1; k <= 7; k++) {

                /**
                 * Eğer gideceğimiz konum boş ise veya rakip taş varsa if in içerisine giriyoruz
                 * eğer iki durumda yoksa döngüye devam ettirmememiz lazım
                 * bu nedenle else break durumu söz konusu
                 */
                if (chessBoard.getSquares()[i][k] == null ||
                        chessBoard.getSquares()[i][k].getColor() != chessBoard.getMoveOrder()) {

                    /** burdaki kontrolün nedeni eğer hamle rakip taşı yiyecekse bir daha dögüye girmemeli
                      * null pointer exception yeme ihtimalini kaldırmak için null check yapıyoruz
                    */
                    if (chessBoard.getSquares()[i][k] != null &&
                            chessBoard.getSquares()[i][k].getColor() != chessBoard.getMoveOrder()) {
                        Square[][] squares = chessBoard.cloneBoard();
                        squares[i][k] = chessBoard.getSquares()[i][j];
                        squares[i][j] = null;
                        possibleBoards.add(squares);
                        break;
                    }

                    Square[][] squares = chessBoard.cloneBoard();
                    squares[i][k] = chessBoard.getSquares()[i][j];
                    squares[i][j] = null;
                    possibleBoards.add(squares);
                } else break;
            }

        }
    }

    private void moveDown(int i, int j, List<Square[][]> possibleBoards) {

        if (j != 0) {
            for (int k = j - 1; k >= 0; k--) {
                if (chessBoard.getSquares()[i][k] == null ||
                        chessBoard.getSquares()[i][k].getColor() != chessBoard.getMoveOrder()) {

                    if (chessBoard.getSquares()[i][k] != null &&
                            chessBoard.getSquares()[i][k].getColor() != chessBoard.getMoveOrder()) {
                        Square[][] squares = chessBoard.cloneBoard();
                        squares[i][k] = chessBoard.getSquares()[i][j];
                        squares[i][j] = null;
                        possibleBoards.add(squares);
                        break;
                    }

                    Square[][] squares = chessBoard.cloneBoard();
                    squares[i][k] = chessBoard.getSquares()[i][j];
                    squares[i][j] = null;
                    possibleBoards.add(squares);
                } else break;
            }
        }


    }

    private void moveRight(int i, int j, List<Square[][]> possibleBoards) {

        if (i != 7) {
            for (int k = i + 1; k <= 7; k++) {

                if (chessBoard.getSquares()[k][j] == null ||
                        chessBoard.getSquares()[k][j].getColor() != chessBoard.getMoveOrder()) {

                    if (chessBoard.getSquares()[k][j] != null &&
                            chessBoard.getSquares()[k][j].getColor() != chessBoard.getMoveOrder()) {
                        Square[][] squares = chessBoard.cloneBoard();
                        squares[k][j] = chessBoard.getSquares()[i][j];
                        squares[i][j] = null;
                        possibleBoards.add(squares);
                        break;
                    }

                    Square[][] squares = chessBoard.cloneBoard();
                    squares[k][j] = chessBoard.getSquares()[i][j];
                    squares[i][j] = null;
                    possibleBoards.add(squares);
                } else break;
            }
        }

    }

    private void moveLeft(int i, int j, List<Square[][]> possibleBoards) {

        if (i != 0) {
            for (int k = i - 1; k >= 0; k--) {
                if (chessBoard.getSquares()[k][j] == null ||
                        chessBoard.getSquares()[k][j].getColor() != chessBoard.getMoveOrder()) {

                    if (chessBoard.getSquares()[k][j] != null &&
                            chessBoard.getSquares()[k][j].getColor() != chessBoard.getMoveOrder()) {
                        Square[][] squares = chessBoard.cloneBoard();
                        squares[k][j] = chessBoard.getSquares()[i][j];
                        squares[i][j] = null;
                        possibleBoards.add(squares);
                        break;
                    }
                    Square[][] squares = chessBoard.cloneBoard();
                    squares[k][j] = chessBoard.getSquares()[i][j];
                    squares[i][j] = null;
                    possibleBoards.add(squares);
                } else break;
            }
        }
    }

    private void moveRightUpCross(int i, int j, List<Square[][]> possibleBoards) {


        if (i != 7 && j != 7) {

            /**
             * burada çapraz gitme söz konusu olduğu için ekstradan bir adet daha değişken kullanmalıyız
             * l ye ilk hamle için i+1 i atıyoruz yine l = 7 olduğunda daha fazla çapraz gidilemeyeceği için
             * döngüyü sonlandırıyoruz.
             * k nın kontrollerini döngüyü yazarken yazmıştık
             */
            int l = i + 1;
            for (int k = j + 1; k <= 7; k++, l++) {

                if (chessBoard.getSquares()[l][k] == null ||
                        chessBoard.getSquares()[l][k].getColor() != chessBoard.getMoveOrder()) {

                    if (chessBoard.getSquares()[l][k] != null &&
                            chessBoard.getSquares()[l][k].getColor() != chessBoard.getMoveOrder()) {
                        Square[][] squares = chessBoard.cloneBoard();
                        squares[l][k] = chessBoard.getSquares()[i][j];
                        squares[i][j] = null;
                        possibleBoards.add(squares);
                        break;
                    }

                    Square[][] squares = chessBoard.cloneBoard();
                    squares[l][k] = chessBoard.getSquares()[i][j];
                    squares[i][j] = null;
                    possibleBoards.add(squares);

                    if (l == 7) break;
                } else break;
            }

        }

    }

    private void moveLeftUpCross(int i, int j, List<Square[][]> possibleBoards) {

        if (i != 0 && j != 7) {
            int l = i - 1;
            for (int k = j + 1; k <= 7; k++, l--) {


                if (chessBoard.getSquares()[l][k] == null ||
                        chessBoard.getSquares()[l][k].getColor() != chessBoard.getMoveOrder()) {

                    if (chessBoard.getSquares()[l][k] != null &&
                            chessBoard.getSquares()[l][k].getColor() != chessBoard.getMoveOrder()) {
                        Square[][] squares = chessBoard.cloneBoard();
                        squares[l][k] = chessBoard.getSquares()[i][j];
                        squares[i][j] = null;
                        possibleBoards.add(squares);
                        break;
                    }

                    Square[][] squares = chessBoard.cloneBoard();
                    squares[l][k] = chessBoard.getSquares()[i][j];
                    squares[i][j] = null;
                    possibleBoards.add(squares);
                    if (l == 0) break;

                } else break;
            }

        }

    }

    private void moveRightDownCross(int i, int j, List<Square[][]> possibleBoards) {

        if (i != 7 && j != 0) {
            int l = i + 1;
            for (int k = j - 1; k >= 0; k--, l++) {

                if (chessBoard.getSquares()[l][k] == null ||
                        chessBoard.getSquares()[l][k].getColor() != chessBoard.getMoveOrder()) {

                    if (chessBoard.getSquares()[l][k] != null &&
                            chessBoard.getSquares()[l][k].getColor() != chessBoard.getMoveOrder()) {
                        Square[][] squares = chessBoard.cloneBoard();
                        squares[l][k] = chessBoard.getSquares()[i][j];
                        squares[i][j] = null;
                        possibleBoards.add(squares);
                        break;
                    }

                    Square[][] squares = chessBoard.cloneBoard();
                    squares[l][k] = chessBoard.getSquares()[i][j];
                    squares[i][j] = null;
                    possibleBoards.add(squares);
                    if (l == 7) break;
                } else break;
            }

        }

    }

    private void moveLeftDownCross(int i, int j, List<Square[][]> possibleBoards) {

        if (i != 0 && j != 0) {
            int l = i - 1;
            for (int k = j - 1; k >= 0; k--, l--) {

                if (chessBoard.getSquares()[l][k] == null ||
                        chessBoard.getSquares()[l][k].getColor() != chessBoard.getMoveOrder()) {

                    if (chessBoard.getSquares()[l][k] != null &&
                            chessBoard.getSquares()[l][k].getColor() != chessBoard.getMoveOrder()) {
                        Square[][] squares = chessBoard.cloneBoard();
                        squares[l][k] = chessBoard.getSquares()[i][j];
                        squares[i][j] = null;
                        possibleBoards.add(squares);
                        break;
                    }

                    Square[][] squares = chessBoard.cloneBoard();
                    squares[l][k] = chessBoard.getSquares()[i][j];
                    squares[i][j] = null;
                    possibleBoards.add(squares);
                    if (l == 0) break;
                } else break;
            }

        }

    }

    public int[] calculatePoints(ChessBoard chessBoard) {
        int[] points = new int[2];
        int whitePoints=0,blackPoints=0;

        for (int i = 0; i < chessBoard.getSquares().length; i++) {
            for (int j = 0; j < chessBoard.getSquares().length; j++) {
                if (chessBoard.getSquares()[i][j] != null) {

                        switch (chessBoard.getSquares()[i][j].getPiece()) {
                            case QUEEN: {
                                if (chessBoard.getMoveOrder()==Color.BLACK){
                                    blackPoints+=8;
                                }
                                else whitePoints+=8;
                                break;
                            }
                            case KING: {
                                if (chessBoard.getMoveOrder()==Color.BLACK){
                                    blackPoints+=100;
                                }
                                else whitePoints+=100;
                                break;
                            }
                            case BISHOP:
                            case KNIGHT: {
                                if (chessBoard.getMoveOrder()==Color.BLACK){
                                    blackPoints+=3;
                                }
                                else whitePoints+=3;
                                break;
                            }
                            case PAWN:{
                                if (chessBoard.getMoveOrder()==Color.BLACK){
                                    blackPoints+=1;
                                }
                                else whitePoints+=1;
                                break;
                            }
                            case ROOK:{
                                if (chessBoard.getMoveOrder()==Color.BLACK){
                                    blackPoints+=5;
                                }
                                else whitePoints+=5;
                                break;
                            }

                        }
                    }

            }
        }
        points[0]=whitePoints;
        points[1]=blackPoints;
        return points;
    }
}