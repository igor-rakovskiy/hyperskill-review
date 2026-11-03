# File Server — Stage 4/4: Save Anything

In this stage, you will write a program that sends real text and image files to the server. The server keeps the files and sends them back on request until you decide to delete them.

The server should recognize each file by its unique identifier. If the file is created successfully, the server should return an integer identifier after the code `200` and a single space. If file creation fails, no identifier is needed.

Files should be accessible using either their identifier or their filename. After the `GET` or `DELETE` command, specify whether to use the file ID or name with the keywords `BY_ID` and `BY_NAME`. For example, `GET BY_ID 12` or `GET BY_NAME filename.txt`. The same applies to `DELETE BY_ID` and `DELETE BY_NAME`. These keywords are not required for `PUT`: it saves a new file on the server, and the server returns a new file ID.

Since saving larger files may take a while, use a parallel approach. Process each client request in a thread pool while the main thread waits for another request. Access to the map of identifiers and filenames must be synchronized because different threads use it. Save this map so that it is not lost when the server restarts. ID generation must continue after a restart rather than start over.

Transfer file contents as a byte array instead of encoding them as text. Send the number of bytes before the file contents so that the receiver knows how much data to read. The sender writes the length using `writeInt()` and then the contents using `write()`. The receiver reads the length using `readInt()` and then reads the specified number of bytes using `readFully()`.

The client should prompt the user to enter an action. To upload a file, ask which file from the `client/data` folder should be saved. Then ask for the filename to use on the server. This name must not contain spaces or tabs. If the user presses Enter without entering a name, the server should generate a unique filename and return the file ID. Uploaded files must be saved in the `server/data` folder.

To download a file, ask whether the user wants to find it by ID or by name. After receiving the file, ask for the name under which it should be saved in the `client/data` folder.

To delete a file, ask whether the user wants to identify it by ID or by name, read the corresponding value, and send the request to the server.

After sending a request, the client should receive the response, print the appropriate message, disconnect, and terminate.

The server should print `Server started!` when it starts, receive requests, and respond accordingly. For `PUT`, return `200` and a unique integer identifier separated by a single space if the file is created successfully; otherwise, return `403`. For `GET`, return `200` and the file contents if the file exists; otherwise, return `404`. For `DELETE`, return `200` if the file is deleted successfully; otherwise, return `404`.

The server must continue running until it receives the `exit` command from the client. Implement this command so that the tests can stop the server. This shutdown behavior should not be allowed in a normal production environment.
