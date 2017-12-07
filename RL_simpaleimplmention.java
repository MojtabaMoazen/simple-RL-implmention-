/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rl_simpaleimplmention;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
/**
 *
 * @author Mojtaba Moazen
 */
public class RL_simpaleimplmention {
     char[][] maze; 
     double[][] Rewards;       
     double[][] Q_table;    
     final double alpha = 0.1; 
     final int reward_final = 100;
     final int negative_rward = -10;
     final double gamma = 0.9; 
    

   


    public static void main(String args[]) {
        RL_simpaleimplmention ql = new RL_simpaleimplmention();
        ql.cacl_R_initial();
        ql.calculateQ();
        ql.print_q_table();
        ql.printdirection();
    }

    public void cacl_R_initial() {
        File f =new File("map.txt");
        Rewards =new double[9][9];
        Q_table =new double[9][9];
        maze =new char[3][3];
        try (FileInputStream file_input = new FileInputStream(f)) {
            int counter;
            int i=0;
            int j=0;

          

            
            while ((counter =  file_input.read()) != -1) {
                char c = (char) counter;
                if (c != '0' && c != 'F' && c != 'X') {
                    continue;
                }
                maze[i][j] = c;
                j++;
                if (j == 3) {
                    j = 0;
                    i++;
                }
            }

          
            for (int k = 0; k < 9; k++) {

                
                i = k / 3;
                j = k - i * 3;

                
                for (int s = 0; s < 9; s++) {
                    Rewards[k][s] = -1;
                }

             
                if (maze[i][j] != 'F') {

                    
                    int goLeft = j - 1;
                    if (goLeft >= 0) {
                        int target = i * 3 + goLeft;
                        if (maze[i][goLeft] == '0') {
                            Rewards[k][target] = 0;
                        } else if (maze[i][goLeft] == 'F') {
                            Rewards[k][target] = reward_final;
                        } else {
                            Rewards[k][target] = negative_rward;
                        }
                    }

                  
                    int goRight = j + 1;
                    if (goRight < 3) {
                        int target = i * 3 + goRight;
                        if (maze[i][goRight] == '0') {
                            Rewards[k][target] = 0;
                        } else if (maze[i][goRight] == 'F') {
                            Rewards[k][target] = reward_final;
                        } else {
                            Rewards[k][target] = negative_rward;
                        }
                    } 
                    int goUp = i - 1;
                    if (goUp >= 0) {
                        int target = goUp * 3 + j;
                        if (maze[goUp][j] == '0') {
                            Rewards[k][target] = 0;
                        } else if (maze[goUp][j] == 'F') {
                            Rewards[k][target] = reward_final;
                        } else {
                            Rewards[k][target] = negative_rward;
                        }
                    }

             
                    int goDown = i + 1;
                    if (goDown < 3) {
                        int target = goDown * 3 + j;
                        if (maze[goDown][j] == '0') {
                            Rewards[k][target] = 0;
                        } else if (maze[goDown][j] == 'F') {
                            Rewards[k][target] = reward_final;
                        } else {
                            Rewards[k][target] = negative_rward;
                        }
                    }
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void calculateQ() {
        Random rand = new Random();

        for (int train_count = 0; train_count < 5000; train_count++) {
       
            int crtState = rand.nextInt(9);

            while (!is_reached_goal(crtState)) {
                int[] actionsFromCurrentState = possibleaction_state(crtState);

                int index = rand.nextInt(actionsFromCurrentState.length);
                int nextState = actionsFromCurrentState[index];
                double r = Rewards[crtState][nextState];

                double value = Q_table[crtState][nextState] + alpha * (r + gamma *  maxQ(nextState) - Q_table[crtState][nextState]);
                Q_table[crtState][nextState] = value;

                crtState = nextState;
            }
        }
    }

    boolean is_reached_goal(int state) {
        int i = state / 3;
        int j = state - i * 3;
        if (maze[i][j]=='F'){
            return true;    
        }
        else {
            return false;
        }
    }
    void print_q_table() {
        System.out.println("Q matrix");
        for (int i = 0; i < Q_table.length; i++) {
            System.out.print("From state " + i + ":  ");
            for (int j = 0; j < Q_table[i].length; j++) {
                System.out.printf("%6.2f ", (Q_table[i][j]));
            }
            System.out.println();
        }
    }

    int[] possibleaction_state(int state) {
        ArrayList<Integer> posiible_state = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (Rewards[state][i] != -1) {//momjen ha 
                posiible_state.add(i);
            }
        }

        return posiible_state.stream().mapToInt(i -> i).toArray();
    }

    double maxQ(int nextState) {
        int[] actionsFromState = possibleaction_state(nextState);
        double maxValue = 0;
        for (int nextAction  :actionsFromState) {
            double value = Q_table[nextState][nextAction];

            if (value > maxValue)
                maxValue = value;
        }
        return maxValue;
    }

    void printdirection() {
        System.out.println("\n Print direction");
        for (int i = 0; i < 9; i++) {
            System.out.println("from  " + i + " should go to  " + getstatedirection(i));
        }
    }

    int getstatedirection(int state) {
        int[] actionsFromState = possibleaction_state(state);

        double max = 0;
        int direction = state;

      
        for (int nextState : actionsFromState) {
            double value = Q_table[state][nextState];

            if (value > max) {
                max = value;
                direction = nextState;
            }
        }
        return direction;
    }

    
    
}
