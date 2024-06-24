package Checkers;

import java.util.HashMap;

public class Move {
    private CheckersPiece a;
    private Cell[][] board;

    // This hashmap will show every information about the valid move with the key is Integer[] with the first one is col,
    // and the second one will be row of the endpoint. The value will be String[] with the first element is "capture" or "normal"

    private HashMap<Integer[], String> valid_moves = new HashMap<>();

    public Move(CheckersPiece a, Cell[][] board) {
        this.a = a;
        this.board = board;
    }


    public HashMap<Integer[], String> all_possible() {

        char piece_color = this.a.getColour();
        int fromCol = this.a.getPosition().getX();
        int fromRow = this.a.getPosition().getY();
        Integer[] valid_check_y;
        Integer[] valid_check_x;
        char[] oponents;
        if (piece_color == 'w') {
            valid_check_y = new Integer[]{1, 1};
            valid_check_x = new Integer[]{-1, 1};
            oponents = new char[]{'b', 'B'};
        }
        else if (piece_color == 'b') {
            valid_check_y = new Integer[]{-1, -1};
            valid_check_x = new Integer[]{-1, 1};
            oponents = new char[]{'w', 'W'};
        }
        else if (piece_color == 'W'){
            valid_check_y = new Integer[]{1, 1, -1, -1};
            valid_check_x = new Integer[]{-1, 1, -1, 1};
            oponents = new char[]{'b', 'B'};
        }
        else {
            valid_check_y = new Integer[]{1, 1, -1, -1};
            valid_check_x = new Integer[]{-1, 1, -1, 1};
            oponents = new char[]{'w', 'W'};
        }

        for(int i = 0; i < valid_check_y.length; i++) {
            int y_dir = fromRow + valid_check_y[i];
            int x_dir = fromCol + valid_check_x[i];
            if (y_dir >= 8 || y_dir < 0 || x_dir >= 8 || x_dir < 0) {
                continue;
            }
            if (board[y_dir][x_dir].getPiece().getColour() == 'E') {
                Integer[] coordinate = new Integer[]{y_dir, x_dir};
                valid_moves.put(coordinate, "normal");
            }
            else if (board[y_dir][x_dir].getPiece().getColour() != oponents[0] 
                  && board[y_dir][x_dir].getPiece().getColour() != oponents[1]) {

                int check_empty_y = y_dir + valid_check_y[i];
                int check_empty_x = x_dir + valid_check_x[i];
                if (check_empty_y >= 8 || check_empty_y < 0 || check_empty_x >= 8 || check_empty_x < 0) {
                    continue;
                }
                if (board[check_empty_y][check_empty_x].getPiece().getColour() == 'E') {
                    Integer[] coordinate = new Integer[]{check_empty_y, check_empty_x};
                    valid_moves.put(coordinate, "normal");
                }
            }
            else {
                int check_empty_y = y_dir + valid_check_y[i];
                int check_empty_x = x_dir + valid_check_x[i];
                if (check_empty_y >= 8 || check_empty_y < 0 || check_empty_x >= 8 || check_empty_x < 0) {
                    continue;
                }
                if (board[check_empty_y][check_empty_x].getPiece().getColour() == 'E') {
                    Integer[] coordinate = new Integer[]{check_empty_y, check_empty_x};
                    valid_moves.put(coordinate, "capture");
                }
            }
        }
        return valid_moves;
    }
}