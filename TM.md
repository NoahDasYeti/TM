## TM design process.

First I tried to understand the exact needs of the program.
    1. Add size command.
    1. Implement describe/size command.
    1. Add deeper description.

Because my program is broken up into multiple methods and 
classes, it was easier to program this sprint. That being
said, I had no idea how to use git. I looked up a youtube 
tutorial and followed the assignment instructions which
made the task trivial.

I started by making a repository and setting it up on my
pc. I also used TM to log my tasks.

Adding the first task:

I added size to the list of cases in my switch statement,
created a method for it and used Log.get(name) to get a 
temporary LogFile that i could assign the size to. Then
I realised my LogFile object didnt have a size variable
so i added one and initialized it with a default value.

After each step i used "git add ." "git commit -m" "git push"
to update my repository.

In order to implement the describe/size command i realized
that i could just make another method that calls the
original methods. Also because there are 4 argument when
implementing this command, I added an if statement in my
switch to call the right describe method.

I updated my repository.

To add a deeper description I just added an if statement to
test if a description was already added. If so, the new
description is appended to the end of the String. If not,
the default description is overwritten.

Because my program isnt based on parsing strings. description/
size command does not conflict with printing out the summary.

I used "git log > git.log" and "java TM summary > summary.log"
to create a git log and summary log for the assignment.


