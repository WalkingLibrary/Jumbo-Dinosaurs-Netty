package com.jumbodinosaurs;

import java.util.Scanner;

/* @Author WalkingLibrary
 * @Date January 28, 2019
 * This Program is a Work In Progress being designed to properly serve HTML and most commonly used Photo File types
 *  to your most commonly used web-browsers. As of the date above it can server HTML, PNG, and ICO with plans to
 *  support other Photo File types. This Program or "Server" allows you to hook your domain name up with a dynamic IP
 *  using Google's Dynamic IP API. The Server also has a few safe Guards for malicious requests from clients. It
 *  restricts all GET requests to a single Directory and only allows the files within that Directory to be requested.
 *  The Server Currently does not support Post requests.
 *
 *  Google API with arguments
 *  Arguments Should be given in this order
 *     Username Password Domain
 *  If multiple domains continue this pattern
 *
 *  https://username:password@domains.google.com/nic/update?hostname=subdomain.yourdomain.com
 *
 */
public class Main
{
    private static ServerControl controler;

    public static void main(String[] args)
    {
        Scanner userInput = new Scanner(System.in);
        if (!(args.length == 0) && (args.length % 3 == 0))
        {
            String[][] credentials = new String[args.length / 3][2];
            String[] domains = new String[args.length / 3];
            int rotation = 0;
            for (int i = 0; i < args.length; i++)
            {
                if (i % 3 == 0 && i != 0)
                {
                    rotation++;
                }
                if (i % 3 == 0)
                {
                    credentials[rotation][0] = args[i];
                }
                else if (i % 3 == 1)
                {
                    credentials[rotation][1] = args[i];
                }
                else if (i % 3 == 2)
                {
                    domains[rotation] = args[i];
                }
            }
            System.out.println("Enter Certificate Password or Press Enter if No Certificate.");
            String response = "";
            response += userInput.nextLine();
            controler = new ServerControl(credentials, domains, response);
        }
        else
        {
            System.out.println("Not Enough Arguments Given For Google API");
            System.out.println("Arguments Given: ");
            for (int i = 0; i < args.length; i++)
            {
                System.out.println("Args " + i + ": " + args[i]);
            }

            System.out.println("Start Server without a Domain? Y/N");
            String response = userInput.next();
            if (response.toLowerCase().contains("y") || response.toLowerCase().contains("yes"))
            {
                System.out.println("Enter Certificate Password or Press Enter if No Certificate.");
                response = "";
                response += userInput.nextLine();
                controler = new ServerControl(response);
            }
            System.exit(0);
        }
    }
}
