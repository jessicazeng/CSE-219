/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

/**
 *
 * @author Jessica
 */
public class Bandit {
    int Node;
    
    public Bandit(int newNode){
        Node = newNode;
    }
    
    public int getNode(){
        return Node;
    }
}