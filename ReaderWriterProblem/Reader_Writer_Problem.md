## Reader Writer Problem
three types of rw problems
### First readers–writers problem
rule : No reader shall be kept waiting if the share is currently opened for reading.（ **readers-preference**）
### Second readers–writers problem
The first type causes a problem: writer may starve 

rule: No writer, once added to the queue, shall be kept waiting longer than absolutely necessary.（ **writers-preference**.）

implement : use a `tryRead lock`, and a `resource lock` separately. When a reader tries to read, it must first get the `tryRead lock`, and then get the `resource lock`. When a writer tries to write, it locks `tryRead lock` if it is the first writer and just has to get the `resource lock`. A `count` and a `lock for the count` are needed to let writer cut in line. The `readCount` and `its lock` are also need as the first-type to allow read simultaneously.

### Third readers–writers problem

ensure fairness

rule:  *No thread shall be allowed to starve*; that is, the operation of obtaining a lock on the shared data will always terminate in a bounded amount of time.

implement : based on the first-type, add a queue semaphore