/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.icesi.controllers.invisiblefriend.exceptions;

/**
 *
 * @author David Andrés Maznzano Herrera <damanzano>
 */
public class LoginErrorException extends Exception{
    public LoginErrorException(String message, Throwable cause) {
        super(message, cause);
    }
    public LoginErrorException(String message) {
        super(message);
    }
}
