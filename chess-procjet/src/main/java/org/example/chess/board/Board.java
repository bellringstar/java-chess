package org.example.chess.board;

import static org.example.utils.StringUtils.appendNewLine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.example.chess.board.sort.PieceComparator;
import org.example.chess.pieces.Piece;
import org.example.chess.pieces.Piece.Color;
import org.example.chess.pieces.Piece.PieceFactory;
import org.example.chess.pieces.Piece.Type;

public class Board {

    protected static final int BOARD_SIZE = 8;

    private final List<Rank> board = new ArrayList<>();
    private final BoardInitializeManger boardInitializeManger;
    private final BoardScoreManager boardScoreManager;

    public Board(BoardInitializeManger boardInitializeManger, BoardScoreManager boardScoreManager) {
        this.boardInitializeManger = boardInitializeManger;
        this.boardScoreManager = boardScoreManager;
    }

    public void initialize() {
        boardInitializeManger.initialize(board);
    }

    public void initializeEmpty() {
        boardInitializeManger.initializeEmpty(board);
    }

    public void print() {
        System.out.println(showBoard());
    }

    public int pieceCount() {
        return (int) board.stream()
                .flatMap(rank -> rank.getPieces().stream())
                .filter(p -> (p.isBlack() || p.isWhite()))
                .count();
    }

    public String showBoard() {
        StringBuilder sb = new StringBuilder();
        for (Rank row : board) {
            sb.append(appendNewLine(row.getPieces().stream()
                    .map(Piece::getRepresentation)
                    .collect(Collectors.joining())));
        }
        return sb.toString();
    }

    public int countPiecesByColorAndType(Color color, Type type) {
        int count = 0;
        for (Rank rank : board) {
            if (color == Color.BLACK) {
                count += rank.countBlackPiecesWithType(type);
                continue;
            }

            if (color == Color.WHITE) {
                count += rank.countWhitePiecesWithType(type);
                continue;
            }
        }

        return count;
    }

    public Piece findPiece(String pos) {
        Position position = new Position(pos);
        int r = position.getR();
        int c = position.getC();

        return board.get(r).getPieces().get(c);
    }

    public void move(String position, Piece piece) {
        //TODO: 해당 말이 해당 위치로 이동이 가능한 말인지 확인하는 로직 필요.
        Position pos = new Position(position);
        int r = pos.getR();
        int c = pos.getC();

        Rank row = board.get(r);
        row.changePiece(c, piece);
    }

    public void move(String source, String destination) {
        //source 위치의 piece를 찾는다.
        Piece piece = findPiece(source);

        //source 위치를 빈 공간으로 변경
        move(source, PieceFactory.createBlank());

        //해당 위치에 from 위치의 piece를 놓는다.
        move(destination, piece);
    }

    public double calculatePoint(Color color) {
        return boardScoreManager.calculatePoint(board, color);
    }

    public List<Piece> findAllPiecesSortByPoint(Color color, PieceComparator comparator) {
        return boardScoreManager.findAllPiecesSortByPoint(board, color, comparator);
    }

    public static class Rank {

        private final List<Piece> pieces = new ArrayList<>();

        public void addPiece(Piece piece) {
            pieces.add(piece);
        }

        public void changePiece(int col, Piece piece) {
            pieces.set(col, piece);
        }

        public List<Piece> getPieces() {
            return pieces;
        }

        public int countWhitePiecesWithType(Type type) {
            return (int) pieces.stream()
                    .filter(piece -> piece.isWhite() && piece.getType() == type)
                    .count();
        }

        public int countBlackPiecesWithType(Type type) {
            return (int) pieces.stream()
                    .filter(piece -> piece.isBlack() && piece.getType() == type)
                    .count();
        }

        public double calculateRankPoint(Color color) {
            double points = 0.0;
            for (Piece piece : pieces) {
                if (piece.getColor() == color) {
                    points += piece.getType().getDefaultPoint();
                }
            }
            return points;
        }

        public List<Piece> findPieces(Color color) {
            List<Piece> piecesInRank = new ArrayList<>();
            for (Piece piece : pieces) {
                if (piece.getColor() == color) {
                    piecesInRank.add(piece);
                }
            }
            return piecesInRank;
        }
    }
}
