package com.jumbodinosaurs.webserver.commands.general;


import com.jumbodinosaurs.devlib.commands.Command;
import com.jumbodinosaurs.devlib.commands.MessageResponse;
import com.jumbodinosaurs.devlib.commands.exceptions.WaveringParametersException;
import com.jumbodinosaurs.webserver.util.OptionUtil;
import com.jumbodinosaurs.webserver.util.ServerUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Statistics extends Command
{
    @Override
    public MessageResponse getExecutedMessage() throws WaveringParametersException
    {
        String outputMessage = "";
        outputMessage += "As of " + LocalDateTime.now().toString() + "\n";
        outputMessage += "Public I.P.: " + ServerUtil.host + "\n";
        //outputMessage += "Total Hits: " + OperatorConsole.totalHits + "\n";
        //outputMessage += "Hits Today: " + OperatorConsole.hitsToday + "\n";
        //outputMessage += "Exceptions: " + OperatorConsole.exceptions + "\n";
        outputMessage += "Debug Messages Will Be Shown: " + OptionUtil.isInDebugMode() + "\n";
        outputMessage += "Time for The Server: " + LocalTime.now().getHour()  + ":" + LocalTime.now().getMinute() +
                                 "\n";
        return new MessageResponse(outputMessage);
    }
    
    @Override
    public String getHelpMessage()
    {
        return "Shows a basic detailing of the web server";
    }
}
