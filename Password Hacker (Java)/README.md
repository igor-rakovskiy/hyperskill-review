# Stage 1 
In this stage, you will simulate a database that can store text information in a string array of fixed size 1000. Initially, each cell (or element) in the database contains an empty string. Users will be able to perform three primary actions: saving strings to cells, reading information from cells, and deleting information from cells. When a string is deleted, the corresponding cell should revert to containing an empty string.
Users can interact with the database using the following commands: set, get, and delete.
Objectives
1.	set command:
o	Syntax: set <index> <text>
o	Function: Saves the specified text to the cell at the given index (1-1000).
o	Output: If the index is out of bounds, the program should output ERROR. Otherwise, it should output OK. If the specified cell already contains information, it should be overwritten.
2.	get command:
o	Syntax: get <index>
o	Function: Retrieves the text from the cell at the given index.
o	Output: If the cell is empty or the index is out of bounds, the program should output ERROR. Otherwise, it should output the content of the cell.
3.	delete command:
o	Syntax: delete <index>
o	Function: Deletes the text from the cell at the given index.
o	Output: If the index is out of bounds, the program should output ERROR. Otherwise, it should output OK. If the cell is already empty, no other action is needed. It should just output OK.
4.	exit command:
o	Syntax: exit
o	Function: Terminates the program.
Note: The server and the client are different programs that run separately. Your server should run from the main method of the Main class in the /server package, and the client should run from the main method of the Main class in the /client package. To test your program, you should run the server first so a client can connect to the server.

# Stage 2 — Simple brute force
Usually, remote databases are accessed through the internet. In this project, the database will be on your computer, but it will still be run as a separate program (we'll call it the server). The client who wants to get, create, or delete some information is a separate program too.
We will be using a socket to connect to the database (server). A socket is an interface to send and receive data between different processes. These processes can be on the same computer or different computers connected through the internet.
To connect to the server, the client must know its address, which consists of two parts: IP address and port. The local address of your computer is always "127.0.0.1". The port can be any number between 0 and 65535, but preferably greater than 1024 to avoid conflicts with well-known ports used by system processes.
The client created a new socket, which means that the client tried to connect to the server. Successful creation of a socket means that the client found the server and managed to connect to it.
After that, you can see the creation of DataInputStream and DataOutputStream objects. These are the input and output streams to the server, respectively. If you expect data from the server, you need to write input.readUTF(). This returns the String object that the server sent to the client. If you want to send data to the server, you need to write output.writeUTF(stringText), and this message will be sent to the server.
The server created a ServerSocket object that waits for client connections. When a client connects, the method server.accept() returns the Socket connection to this client.
After that, you can see the creation of DataInputStream and DataOutputStream objects. These are the input stream from and output stream to this client, respectively, now from the server side. To receive data from the client, write input.readUTF(). To send data to the client, write output.writeUTF(stringText). The server should stop after responding to the client.
Description
In this stage, you will implement the simplest connection between one server and one client. The client should send the server a message: something along the lines of Give me a record # N, where N is an arbitrary integer number. The server should reply A record # N was sent! to the client. Both the client and the server should print the received messages to the console.
Note: In this stage, we are focusing solely on establishing communication between the client and server using sockets, and printing the exchanged messages. We are not yet performing actual database operations (get, set, delete), like we did in the previous stage.
Objectives
1.	Implement a server that waits for a client connection and responds to a specific message.
2.	Implement a client that connects to the server and sends a specific message.
3.	Ensure both the client and the server print the received messages to the console.
Important: Before a client connects to the server, the server output should be: Server started!. Similarly, after the client connects to the server, the client should print Client started!.
Note: The server and the client are different programs that run separately. Your server should run from the main method of the Main class in the /server package, and the client should run from the main method of the Main class in the /client package. To test your program, you should run the server first so a client can connect to the server.
Now you need not only to try each element of the dictionary but also change the case of some letters to find the correct password.
This has increased the time of hacking greatly, so using brute force is probably not an option. Use the dictionary of standard passwords, and do not forget to try changing the cases of different letters. For example, there is the word ‘qwerty’ in the dictionary, but the cunning admin sets it to ‘qWeRTy’. Your program should make it possible to hack such passwords, too.
Objectives
In this stage, you should write a program that:
1.	Parses the command line and gets two arguments that are IP address and port.
2.	Finds the correct password using the list of typical passwords.
3.	Prints the password it found.

# Stage 3— JSON requests and exception vulnerability
For now, there is no need to save the database to a file on the hard drive, so if the server reboots, all the data in the database will be lost. The server should serve one client at a time in a loop, and the client should only send one request to the server, get one reply, and exit. After that, the server should wait for another connection from a client.
To send a request to the server, the client should get all the information through command-line arguments. There is a useful library called JCommander to parse all the arguments. It is included in our project setup, so you can use it without the need for any installation yourself. Before you get started with it, we recommend you check out a JCommander tutorial.
The arguments will be passed to the client in the following format:
java Main -t <type> -i <index> [-m <message>]
•	-t is the type of the request.
•	-i is the index of the cell.
•	-m is the value/message to save in the database (only needed for set requests).
For example: java Main -t set -i 148 -m "Here is some text to store on the server"
Objectives
1.	Implement the ability for the server to handle get, set, and delete operations with an index.
2.	Ensure the server can serve one client at a time in a loop.
3.	Since the server cannot shut down on its own and testing requires the program to stop at some point, you should implement a way to stop the server (after closing the socket), when the client sends an exit request. Note that in a normal situation, this behavior should not be allowed.
4.	The client should send requests to the server using command-line arguments. These arguments include the type of the request (set, get, or delete), the index of the cell, and, in the case of the set request, a text value/message.
Note: The server and the client are different programs that run separately. Your server should run from the main method of the /server/Main class, and the client should run from the main method of the /client/Main class.

# Stage 4 — JSON requests and exception vulnerability
Description
In this stage, you will store the database in JSON format, but keep in mind that this database will still be in memory and not saved as a file on the hard drive. To work with JSON, we recommend using the GSON library by Google. It is included in our project setup, so you can use it without the need for any installation yourself. It is also a good idea to get familiar with this library beforehand. You'd already have learned about it in before getting here. For more examples and explanations, visit here!
You should store the database as a Java JSON object. The keys should be strings (now, no more limited to integer indexes like it was in the previous stages), and the values should be strings as well.
Example of a JSON database:
{
    "key1": "some string value",
    "key2": "another string value",
    "key3": "yet another string value"
}
Note that we still use command-line arguments, but the client has to send JSON to the server and receive JSON from the server. Similarly, the server should process the received JSON and respond with JSON.
The format of the command-line arguments for the client should be as follows:
java Main -t <type> -k <key> [-v <value>]
•	-t specifies the type of request (get, set, or delete).
•	-k specifies the key.
•	-v specifies the value (only needed for set requests).
Note that based on the command-line arguments, the client should send to the server, a valid JSON which includes all the parameters needed to execute the request. Similarly, the server should respond with a valid JSON, for a valid request from a client.
Below are a few examples for the set, get, and delete requests, and the respective response from the server.
Here is what the set request format should look like:
{ "type": "set", "key": "some key", "value": "some value" }
The respective response from the server, for that set request:
{ "response": "OK" }
The get request format:
{ "type": "get", "key": "some key" }
The delete request format:
{ "type": "delete", "key": "a key that doesn't exist" }
In the case of a get request with a key stored in the database:
{ "response": "OK", "value": "some value" }
In the case of a get or delete request with a key that doesn't exist:
{ "response": "ERROR", "reason": "No such key" }
Objectives
1.	Implement a Java JSON object to store the database records.
2.	Implement the set, get, and delete requests and the OK and ERROR responses.
   
# Stage 5 — Time-based vulnerability
In this stage, you will need to improve your client and server by adding the ability to work with files. The server should store (persist) the database as a file on the hard drive, updating it only when setting a new value or deleting one. This functionality is crucial for maintaining data persistence and ensuring that the database state is saved even if the server is restarted.
To handle multiple requests efficiently, you will parallelize the server's work using executors. Each request will be parsed and handled in a separate executor's task, allowing the server to process multiple requests simultaneously. This improvement will significantly enhance the server's performance and scalability, making it capable of handling a higher load.
Implementing synchronization is essential to maintain the integrity of the database when multiple threads access the same file. By using the ReentrantReadWriteLock class, you can allow multiple threads to read the file concurrently while ensuring that only one thread can write to the file at a time. This will prevent data corruption and ensure consistent access to the database.
Additionally, you will implement the ability for the client to read a request from a file. If the -in argument is followed by a file name, the client should read the request from that file. The file will be stored in the /client/data directory. This feature allows the client to directly send pre-formatted JSON requests to the server, bypassing the need to first convert command-line arguments into JSON format and then send that JSON to the server.
One significant advantage of this feature is that it will allow us to store not just strings but also complex JSON objects as values in the future. Writing complex JSON objects directly on the command line can be tedious and error-prone. By using pre-formatted JSON files, we can easily manage and send complex data structures to the server.
Here are the examples of the input file contents:
{"type":"set","key":"name","value":"Sorabh"}
{"type":"get","key":"name"}
{"type":"delete","key":"name"}
For reading client requests from a file, you can get the path using:
path = System.getProperty("user.dir") + "/src/client/data/" + fileName;
Note that like in the previous stage, you should store the database as a JSON object. The keys and values should both be strings.
Example of the contents of a JSON database file:
{
    "key1": "some string value",
    "key2": "another string value",
    "key3": "yet another string value"
}
For working with database file, you can get the path using:
path = System.getProperty("user.dir") + "/src/server/data/db.json";
Objectives
1.	Persist the Database: The server should store the database on the hard drive in a db.json file, located in the /server/data folder. The database should be updated only after setting a new value or deleting an existing one.
2.	Parallelize Request Handling: The server should handle multiple requests simultaneously by using executors. Each request should be processed in a separate task, while the main thread waits for incoming requests.
3.	Implement Synchronization: Ensure that multiple threads can read the database file concurrently, but only one thread can write to the file at a time. This will prevent data corruption and ensure consistent access to the database.
4.	Read Requests from a File: The client should be able to read a request from a file if the -in argument is followed by a file name. The file will be stored in the /client/data directory.

# Stage 6 — Time-based vulnerability
Description
In this stage, you will enhance your database to store not just strings but any JSON types, including objects, arrays, numbers as values. This improvement will allow for more complex and nested data structures within the database.
Similarly, the key should not just be a single string as it was in the previous stage. The key should instead be in the form of an array, because now the user needs to be able to retrieve specific parts of the JSON value. For example, consider the following JSON structure, where the user wants to get only the surname of person:
{
    ... ,

    "person": {
        "name": "Adam",
        "surname": "Smith"
    }
    ...
}
To retrieve only the surname of the person, the user should provide the full path to this field in the form of a JSON array: ["person", "surname"]. If the user wants to get the full person object, they should provide ["person"].
The user should also be able to set value against different keys inside JSON objects. For example, it should be possible to set only the surname using the key ["person", "surname"] and any value against a key, including another JSON object.
Moreover, the user should be able to add new values inside other JSON objects. For example, using the key ["person", "age"] and the value 25, the person object should look like this:
{    ... ,
    "person": {
        "name": "Adam",
        "surname": "Smith",
        "age": 25
    }
    ...
}
If there are no root objects, the server should create them. For example, if the database does not have a "person1" key but the user sets the value {"id1": 12, "id2": 14} for the key ["person1", "inside1", "inside2"], then the database will have the following structure:
{
    ... ,
    "person1": {
        "inside1": {
            "inside2" : {
                "id1": 12,
                "id2": 14
            }
        }
    },
    ...
}
The deletion of objects should follow the same rules. If a user deletes the object above by the key ["person1", "inside1", "inside2"], then only "inside2" should be deleted, not "inside1" or "person1". See the example below:
{
    ... ,
    "person1": {
        "inside1": { }
    }

    ...
}
Objectives
•	Enhance JSON Storage: Modify your database to store any JSON values, not just strings.
•	Nested Key Access: Implement the ability to access and modify nested JSON values using keys in the form of JSON arrays.
•	Dynamic Object Creation: Ensure the server can dynamically create root objects if they do not exist when setting new values.
•	Selective Deletion: Implement the ability to delete nested JSON objects without affecting their parent objects.

