#include <stdio.h>
#include <stdlib.h>

int main(void)
{
	FILE* rfp, *wfp;
	int id, s1,s2, s3,s4,n;
	
	if((rfp = fopen("unix.dat","r")) == NULL)
	{
		perror("fopen: unix.dat");
		exit(1);
	}

	if((wfp =fopen("unix.scr", "w")) == NULL)
	{
		perror("fopen: unix.scr");
		exit(1);
	}
	
	fprintf(wfp,"학번 평균\n");
	while((n = fscanf(rfp,"%d %d %d %d %d", &id, &s1, &s2, &s3, &s4)) != EOF)
		fprintf(wfp,"%d: %d\n", id,(s1+s2+s3+s4)/4);

	fclose(rfp);
	return 0;
}
