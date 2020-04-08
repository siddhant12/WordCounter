1)For efficiently reading large files of such we can split our files into smaller sizes. Then on each file, we can use the separate thread to count the number of words in each file. CPU utilization will be done more efficiently. 

2)Another way is that we can use Spring Batch which can be used to read data in chunks. We define a job which includes step that processes the target file. 
**Step has 3 parts:**
**ItemReader**: It is used to read a file in chunks.
**ItemProcessor**: It can perform operations on the file such as counting words.
**ItemWriter**: It can write the result in a file or in a database.