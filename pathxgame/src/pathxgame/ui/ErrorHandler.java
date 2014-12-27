/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathxgame.ui;

import javax.swing.JFrame;

/**
 *
 * @author Jessica
 */
public class ErrorHandler {
    // WE'LL CENTER DIALOG BOXES OVER THE WINDOW, SO WE NEED THIS
    private JFrame window;

    /**
     * This simple little class just needs the window.
     * 
     * @param initWindow 
     */
    public ErrorHandler(JFrame initWindow)
    {
        // KEEP THE WINDOW FOR LATER
        window = initWindow;
    }
}
