package AIOthello;

import java.util.ArrayList;
import java.util.List;

public class Minimax {
    

    private static boolean gameOver(int[] position) {
        return addPossibleMoves(position).size() == 0;
    }

    // Kiểm tra vị trí hiện tại có đc thêm điểm ko (1 hướng)
    private static List<Integer> moveChecking(int step, boolean vertical, int location, int[] position) {
        List<Integer> flip = new ArrayList<>();
        int value = location + step;
        if(!vertical) {
            while(value >= 0 && value <= 63 && position[value] == 1 && Math.abs((value - step) / 8 - value / 8) == 1) {
                flip.add(value);
                value += step;
                if(value < 0 || value > 63 || ((value - step) % 8 == 0 && value % 8 == 7) || ((value - step) % 8 == 7 && value % 8 == 0) || position[value] == 0) {
                    flip.clear();
                    break;
                }
                if(position[value] == 2) {
                    break;
                }
            }    
        }
        else {
            while(value >= 0 && value <= 63 && position[value] == 1 && value / 8 == location / 8) {
                flip.add(value);
                value += step;
                if(value < 0 || value > 63 || ((value - step) % 8 == 0 && value % 8 == 7) || ((value - step) % 8 == 7 && value % 8 == 0) || position[value] == 0) {
                    flip.clear();
                    break;
                }
                if(position[value] == 2) {
                    break;
                }
            } 
        }
        return flip;
    }

    // Lật 1 hàng
    private static int[] flip_single_line(int[] position, List<Integer> flip) {
        int[] current_state = new int[64];
        for(int i = 0; i < 64; i++) current_state[i] = position[i];
        for(int i = 0; i < flip.size(); i++)
            current_state[flip.get(i)] = 2;
        return current_state;
    }

    // Trả về nhánh con
    private static int[] flip_multiple_line(int location, int[] position) {
        int[] current_state = new int[64];
        for(int i = 0; i < 64; i++) current_state[i] = position[i];
        current_state = flip_single_line(current_state, moveChecking(-9, false, location, current_state));
        current_state = flip_single_line(current_state, moveChecking(-8, false, location, current_state));
        current_state = flip_single_line(current_state, moveChecking(-7, false, location, current_state));
        current_state = flip_single_line(current_state, moveChecking(-1, true, location, current_state));
        current_state = flip_single_line(current_state, moveChecking(1, true, location, current_state));
        current_state = flip_single_line(current_state, moveChecking(7, false, location, current_state));
        current_state = flip_single_line(current_state, moveChecking(8, false, location, current_state));
        current_state = flip_single_line(current_state, moveChecking(9, false, location, current_state));
        return current_state;
    }

    private static List<Integer> addPossibleMoves(int[] position) {
        List<Integer> possible_moves = new ArrayList<>();
        for(int i = 0; i < 64; i++) {    
            if(position[i] == 0) {
                int count = 0;
                count += moveChecking(-9, false, i, position).size();
                count += moveChecking(-8, false, i, position).size();
                count += moveChecking(-7, false, i, position).size();
                count += moveChecking(-1, true, i, position).size();
                count += moveChecking(1, true, i, position).size();
                count += moveChecking(7, false, i, position).size();
                count += moveChecking(8, false, i, position).size();
                count += moveChecking(9, false, i, position).size();
                if(count > 0) possible_moves.add(i);
            }
        }
        // for(int i : possible_moves) {
        //     System.out.print(i + " ");
        // }
        // System.out.print('\n');
        return possible_moves;
    }


    private static int heuristic(final int[] state) {
        int player_score = 0;
        int bot_score = 0;
        for(int i = 0; i < state.length; i++) {
            if(state[i] == 1) {
                if(i  == 0 || i == 7 || i == 56 || i == 63) {
                    player_score += 100;
                }
                else if(i % 8 == 0 || i % 8 == 7) player_score += 10;
                else player_score++;
            }
            else if(state[i] == 2) {
                if(i  == 0 || i == 7 || i == 56 || i == 63) {
                    bot_score += 100;
                }
                else if(i % 8 == 0 || i % 8 == 7) bot_score += 10;
                else bot_score++;
            }
        }
        return bot_score - player_score;
    }

    private static List<int[]> child(int[] position) {
        List<int[]> child = new ArrayList<>();

        // for(int i : position)
        // System.out.print(i);
        // System.out.print('\n');

        for(int i = 0; i < addPossibleMoves(position).size(); i++) {
            child.add(flip_multiple_line(addPossibleMoves(position).get(i), position));
        }
        return child;
        
    }

    private static int minimax(int[] position, int depth, boolean player1Turn) {
        if(depth == 0 || gameOver(position))
            return heuristic(position);

        if(player1Turn) {
            int maxValue = -1000;
            int currValue = 0;
            for(int i = 0; i < child(position).size(); i++) {
                currValue = minimax(child(position).get(i), depth-1, false);
                maxValue = Math.max(maxValue, currValue);
            }
            return maxValue;
        }
        else {
            int minValue = 1000;
            int currValue = 0;
            for(int i = 0; i < child(position).size(); i++) {
                currValue = minimax(child(position).get(i), depth-1, true);
                minValue = Math.min(minValue, currValue);
            }
            return minValue;
        }
    }

    public static int best_solution(int[] position) {
        int location = 0;
        int value = -1000;
        
        // for(int i = 0; i < child(position).size(); i++) {
        //     System.out.print(addPossibleMoves(position).get(i));
        // }
        // System.out.println("\nBest solution");
        for(int i = 0; i < child(position).size(); i++) {
        if(minimax(child(position).get(i), 0, false) >= value) {
                value = minimax(child(position).get(i), 0, false);
                location = addPossibleMoves(position).get(i);
            }
        }
        return location;
    }

    // public static void main(String[] args) {
    //     int[] position = new int[64];
    //     for(int i : position) i = 0;
        
    //     position[27] = 1;
    //     position[28] = 2;
    //     position[35] = 2;
    //     position[36] = 1;

    //     Minimax mnx = new Minimax();
    //     // System.out.println(mnx.heuristic(position));

    //     // position[28] = 1;
    //     // position[29] = 1;

    //     // System.out.println(mnx.addPossibleMoves(position));
    //     System.out.println(mnx.child(position).size());


    // }
}
