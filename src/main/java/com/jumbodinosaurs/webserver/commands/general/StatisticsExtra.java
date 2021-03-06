package com.jumbodinosaurs.webserver.commands.general;

import com.jumbodinosaurs.devlib.commands.Command;
import com.jumbodinosaurs.devlib.commands.MessageResponse;
import com.jumbodinosaurs.devlib.commands.exceptions.WaveringParametersException;
import com.jumbodinosaurs.devlib.email.Email;
import com.jumbodinosaurs.devlib.email.EmailManager;
import com.jumbodinosaurs.webserver.domain.DomainManager;
import com.jumbodinosaurs.webserver.domain.util.Domain;
import com.jumbodinosaurs.webserver.util.OptionUtil;
import com.jumbodinosaurs.webserver.util.ServerUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class StatisticsExtra extends Command
{
    @Override
    public MessageResponse getExecutedMessage() throws WaveringParametersException
    {
        
        String outputMessage = "";
        outputMessage += "As of " + LocalDateTime.now().toString() + "\n";
        outputMessage += "Public I.P.: " + ServerUtil.host + "\n";
        //outputMessage += "Total Hits: " + OperatorConsole.totalHits + "\n";
        //outputMessage += "Hits Today: " + OperatorConsole.hitsToday + "\n";
        // outputMessage += "Exceptions: " + OperatorConsole.exceptions + "\n";
        outputMessage += "Debug Messages Will Be Shown: " + OptionUtil.isInDebugMode() + "\n";
        outputMessage += "Time for The Server: " + LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + "\n";
        
        
        
        
        outputMessage += "Domains Hosted: " + "\n";
        for(Domain domain : DomainManager.getDomains())
        {
            outputMessage += domain.getDomain() + "\n";
        }
        
        
        outputMessage += "Emails In Service: " + "\n";
        for(Email email : EmailManager.getEmails())
        {
            outputMessage += email.getUsername() + "\n";
        }
        
        
        outputMessage += "White List Enabled: " + OptionUtil.isWhiteListOn() + "\n";
        
        if(OptionUtil.getWhiteList().size() > 0)
        {
            outputMessage += "White Listed I.P.s: " + "\n";
            for(String ip : OptionUtil.getWhiteList())
            {
                outputMessage += "I.P.: " + ip + "\n";
            }
        }
        else
        {
            outputMessage += "No White Listed I.P.s" + "\n";
        }
        
        return new MessageResponse(outputMessage);
    }
    
    @Override
    public String getHelpMessage()
    {
        return "Shows a basic detailing of the web server and some extra stats";
    }
    
}
