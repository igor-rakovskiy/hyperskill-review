
# Stage 1 
The first task of this project is to go to the admin's site; it will immediately give out all the secret information. Remember, as soon as you enter the site as an admin, you will automatically obtain all the private data of the site. It will get harder: the tasks of all other stages of the project will be to crack the admin password. Good luck!
Your program should connect to the server using an IP address and a port from the command line arguments. You can use socket to create this program.
Objectives
Your program will receive command line arguments in this order:
1.	IP address
2.	port
3.	message for sending
The algorithm is the following:
1.	Create a new socket.
2.	Connect to a host and a port using the socket.
3.	Send a message from the third command line argument to the host using the socket.
4.	Receive the server’s response.
5.	Print the server’s response.
6.	Close the socket.


# Stage 2 — Simple brute force
The admin of the server doesn’t hide the information that passwords vary in length and may include letters from a to z and numbers from 0 to 9. You should start with a,b,c,....,z,0,1,..aa,ab,ac,ad and continue until your password is correct. It’s very important to try all the variants of every length because otherwise your program risks never finding the password!
If the password is correct, you will receive the Connection success! message. Otherwise, you will see the Wrong password! message. The server cannot receive more than a million attempts, so if your program works indefinitely, you will see the unfortunate message Too many attempts.

Objectives
In this stage, you should write a program that:
1.	Parses the command line and gets two arguments that are IP address and port.
2.	Tries different passwords until it finds the correct one.
3.	Prints the password it found.
Note that you can connect to the server only once and then send messages many times. Don't connect to the server before sending every message.
Also, note that here and throughout the project, the password is different every time you check your code.

# Stage 3 — Dictionary-based brute force
Now you need not only to try each element of the dictionary but also change the case of some letters to find the correct password.
This has increased the time of hacking greatly, so using brute force is probably not an option. Use the dictionary of standard passwords, and do not forget to try changing the cases of different letters. For example, there is the word ‘qwerty’ in the dictionary, but the cunning admin sets it to ‘qWeRTy’. Your program should make it possible to hack such passwords, too.
Objectives
In this stage, you should write a program that:
1.	Parses the command line and gets two arguments that are IP address and port.
2.	Finds the correct password using the list of typical passwords.
3.	Prints the password it found.
Note that here and throughout the project, the password is different every time you check your code.

# Stage 4 — JSON requests and exception vulnerability
Also, now the admin has made a complex password that is guaranteed to be absent in the databases since it's randomly generated from several characters.
The server now uses JSON to send messages.
First of all, you should adjust your program so that it can send the combination of login and password in JSON format to the server. Your request should now look like this:
{
    "login": "admin",
    "password": "12345678"
}
In case of the wrong login, the response you receive looks like this:
{
    "result": "Wrong login!"
}
If you got the login right but failed to find the password, you get this:
{
    "result": "Wrong password!"
}
If your request is not in a valid json format or there is no "login" or "password" field, the response will be:
{
    "result": "Bad request!"
}
If some exception happens, you'll see this result:
{
    "result": "Exception happened during login"
}
When you finally succeed in finding both the login and the password, you'll see the following:
{
    "result": "Connection success!"
}
Use the dictionary of typical admin logins. Since you don’t know the login, you should try different variants from the dictionary the same way you did at the previous stage with the passwords.
As for passwords, they’ve become yet harder, so a simple dictionary is no longer enough. Fortunately, a vulnerability has been found: the ‘exception’ message pops up when the symbols you tried for the password match the beginning of the correct one.
Use any password when searching for the login, as the server first checks if the login is correct. Therefore, if the server responds with 'wrong password' or with an exception instead of 'wrong login', it means that the used login is correct.

Objectives

Your algorithm is the following:
1.	Try all logins with any password.
2.	When you find the login, try out every possible password of length 1.
3.	When an exception occurs, you know that you found the first letter of the password.
4.	Use the found login and the found letter to find the second letter of the password.
5.	Repeat until you receive the ‘success’ message.
Finally, your program should print the combination of login and password in JSON format. The examples show two ways of what the output can look like.

# Stage 5 — Time-based vulnerability
The admin has improved the server: the program now catches the exception and sends a simple ‘wrong password’ message to the client even when the real password starts with current symbols.
But here's the thing: the admin probably just caught this exception. We know that catching an exception takes the computer a long time, so there should be a delay in the server response when this exception takes place. You can use, for example, System.nanoTime() or System.currentTimeMillis() methods to hack the system: count the time period in which the response comes and find out which starting symbols work out for the password.
Objectives
In this stage, you should write a program that uses the time vulnerability to find the password.
•	Use the list of logins from the previous stage.
•	Output the result as you did this in the previous stage.





