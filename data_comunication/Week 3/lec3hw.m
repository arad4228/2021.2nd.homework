symbol1 = -1+1j;
symbol2 = -3+3j;
symbol3 = 1-1j;

Ts = 1;
fs = 30;
t = [1/fs : 1/fs :Ts];
y1 = real(symbol1)*cos(2*pi/Ts*t) - imag(symbol1)*sin(2*pi/Ts*t);
y2 = real(symbol2)*cos(2*pi/Ts*t) - imag(symbol2)*sin(2*pi/Ts*t);
y3 = real(symbol3)*cos(2*pi/Ts*t) - imag(symbol3)*sin(2*pi/Ts*t);
figure(2)
plot(t,y1,'ro',t,y2,'bo',t,y3,'go');
title('30Hz sampling');
xlabel('time');
ylabel('value');
legend('symbol1','symbol2','symbol3')
theta1 = angle(symbol1)
theta2 = angle(symbol2)
theta3 = angle(symbol3)
