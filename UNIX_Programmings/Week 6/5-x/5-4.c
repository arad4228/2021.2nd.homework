#include <sys/types.h>
#include <sys/times.h>
#include <limits.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>

int main(void) 
{
    	int i;
    	//time_t t;
    	struct tms mytms;
    	clock_t t1, t2;

    	if ((t1 = times(&mytms)) == -1) 
	{
        	perror("times 1");
        	exit(1);
    	}

    	for (i = 0; i < 999999; i++)
        	getpid(); //time(&t)

    	if ((t2 = times(&mytms)) == -1) 
	{
        	perror("times 2");
        	exit(1);
    	}

    	printf("Real time : %.001f sec\n", (double)(t2 - t1) / sysconf(_SC_CLK_TCK));
    	printf("User time : %.001f sec\n", (double)mytms.tms_utime / sysconf(_SC_CLK_TCK));
    	printf("System time : %.001f sec\n", (double)mytms.tms_stime / sysconf(_SC_CLK_TCK));

    	return 0;
}
