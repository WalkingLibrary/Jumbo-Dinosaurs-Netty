package com.jumbodinosaurs.post;

import com.jumbodinosaurs.auth.exceptions.NoSuchUserException;
import com.jumbodinosaurs.auth.server.AuthToken;
import com.jumbodinosaurs.auth.server.User;
import com.jumbodinosaurs.auth.util.AuthSession;
import com.jumbodinosaurs.auth.util.AuthUtil;
import com.jumbodinosaurs.devlib.database.DataBase;
import com.jumbodinosaurs.devlib.database.DataBaseUtil;
import com.jumbodinosaurs.devlib.database.Query;
import com.jumbodinosaurs.devlib.database.exceptions.NoSuchDataBaseException;
import com.jumbodinosaurs.devlib.database.exceptions.WrongStorageFormatException;
import com.jumbodinosaurs.devlib.email.Email;
import com.jumbodinosaurs.devlib.email.EmailManager;
import com.jumbodinosaurs.devlib.email.NoSuchEmailException;
import com.jumbodinosaurs.devlib.util.WebUtil;
import com.jumbodinosaurs.devlib.util.objects.PostRequest;
import com.jumbodinosaurs.netty.handler.http.util.HTTPResponse;
import com.jumbodinosaurs.util.OptionUtil;
import com.jumbodinosaurs.util.PasswordStorage;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ChangePasswordSendCode extends PostCommand
{
    
    public static final String changePasswordUseName = "changePassword";
    
    @Override
    public HTTPResponse getResponse(PostRequest request, AuthSession authSession)
    {
        /* Process for sending a change password code
         *
         *
         * Check/Verify PostRequest Attributes
         *
         * Get the User From the AuthSession
         * Check the given email with the email on record to reduce spam possibilities
         * Check if we need to make a new changePassword AuthToken
         *
         * no ->
         * return 200 okay
         *
         * yes ->
         * Make changePassword AuthToken
         * Set changePassword Token on the user
         * Update the user in the DataBase
         * Prepare Email with Instructions
         * Send Change Password Email
         * Send 200 okay
         *
         *  */
        
        //Check/Verify PostRequest Attributes
        HTTPResponse response = new HTTPResponse();
        if(request.getEmail() == null)
        {
            response.setMessage400();
            return response;
        }
        
       
        
        //Get the User From the AuthSession
        User user = authSession.getUser();
        
        
        //Check the given email with the email on record to reduce spam possibilities
        if(!request.getEmail().equals(user.getEmail()))
        {
            //Return 200 okay to help avoid email scanning
            response.setMessage200();
            return response;
        }
        
        //Check if we need to make a new changePassword AuthToken
        AuthToken authTokenToCheck = user.getToken(changePasswordUseName);
        
        //no -> return 200 okay
        if(authTokenToCheck != null && !authTokenToCheck.hasExpired())
        {
            response.setMessage200();
            return response;
        }
        
        /*
         *
         * Make changePassword AuthToken
         * Set changePassword Token on the user
         * Update the user in the DataBase
         * Prepare Email with Instructions
         * Send Change Password Email
         * Send 200 okay
         */
    
    
       
        // Make changePassword AuthToken
        String token = AuthUtil.generateRandomString(100);
        LocalDateTime expirationDate = LocalDateTime.now();
        expirationDate = expirationDate.plusHours(2);
        AuthToken changePasswordToken;
        try
        {
            changePasswordToken = new AuthToken(changePasswordUseName,
                                                this.ip,
                                                token,
                                                expirationDate);
        }
        catch(PasswordStorage.CannotPerformOperationException e)
        {
            response.setMessage500();
            return response;
        }
    
        //Set changePassword Token on the user
        User updatedUser = user.clone();
        updatedUser.setToken(changePasswordToken);
        
        //Update the user in the DataBase
        if(AuthUtil.updateUser(authSession, updatedUser))
        {
            response.setMessage500();
            return response;
        }
        
    
        //Prepare Email with Instructions
        
        //This code is specific for jumbodinosaurs.com
        String topic = "Change Password";
        String message = "Here is the code needed to change your password on your Jumbo Dinosaurs Account\n";
        message += "Code: " + token + "\n";
        message += "Enter it at the link below to change your password\n";
        message += "https://jumbodinosaurs.com/changePassword.html";
        
        
        
        
        //Send Change Password Email
        try
        {
            WebUtil.sendEmail(getServersEmail(), user.getEmail(), topic, message);
        }
        catch(MessagingException e)
        {
            response.setMessage500();
            return response;
        }
    
        //Send 200 okay
        response.setMessage200();
        return response;
        
    }
    
    @Override
    public boolean requiresUser()
    {
        return true;
    }
}
