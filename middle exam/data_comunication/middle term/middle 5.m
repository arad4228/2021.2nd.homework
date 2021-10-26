clear;

% Parameter Setting
Tsym = 1;
Nsym = 4;
Fs = 100;
Fc = 10;
Fe = 0;
N0 = 1.5;

% Simulation
t = [Tsym/Fs : Tsym/Fs : Tsym*Nsym];
Tmax = length(t);

% Symbol 생성
M=16;
symTable = zeros(1,16);
for i = 1:M
    realSym = 2*(mod(i-1,4)+1)-5;
    imagSym = 2*(floor((i-1)/4)+1)-5;
    symTable(i) = realSym+j*imagSym;

end

% Basis Signal 생성
phi1 = cos(2*pi*Fc*t(1:Tsym*Fs));
Es = norm(phi1);
phi1 = phi1/Es;

phi2 = -sin(2*pi*Fc*t(1:Tsym*Fs));
Es = norm(phi2);
phi2 = phi2/Es;

%% TX

bbSym = zeros(1,Nsym);

% 심볼신호 만들기
%realSymR = 2*(mod(m-1,4)+1)-5;
%imagSymR = 2*(floor((m-1)/4)+1)-5;
%bbSym = realSymR+j*imagSymR;
% theta_m = 2*pi*(m-1)/M + pi/4;
% bbSym = cos(theta_m) + j*sin(theta_m);
bbSym(1) = complex(1,1);
bbSym(2) = complex(-3,1);
bbSym(3) = complex(3,3);
bbSym(4) = complex(3,-3);

figure(2)
plot(symTable,"r*");
hold on
plot(bbSym,"bo");
axis([-4 4 -4 4]);

% Up-conversion (DAC 포함)
RFsignal = zeros(1,Tmax);
Isignal = zeros(1,Tmax);
Qsignal = zeros(1,Tmax);

for iterT = 1:Tmax
    iterSym = floor((iterT-1)/Fs)+1;
    Isignal(iterT) = real(bbSym(iterSym));
    Qsignal(iterT) = - imag(bbSym(iterSym));
    RFsignal(iterT) = real(bbSym(iterSym))*cos(2*pi*Fc*t(iterT))/Es - imag(bbSym(iterSym))*sin(2*pi*Fc*t(iterT))/Es;
end

% 참고1 - signal 보여주기
figure(3)
plot(t,Isignal);
xlim([0 4]);
ylim([-4 4]);

figure(4)
plot(t,Qsignal);
xlim([0 4]);
ylim([-4 4]);

figure(5)
plot(t,RFsignal);
xlim([0,4]);

% 참고2 - 심볼신호 보여주기
s = zeros(2,Nsym);

for i = 1:Nsym
    intStart = 1+(i-1)*Tsym*Fs;
    intEnd = i*Tsym*Fs;
    s(1,i) = sum(RFsignal(intStart:intEnd).*phi1);
    s(2,i) = sum(RFsignal(intStart:intEnd).*phi2);
end
%figure(3)
%scatter(s2(1,:),s2(2,:),'r*');
%grid on;
%axis([-4 4 -4 4]);


%% RX
% Coherent Detection
Ich = RFsignal .* cos(2*pi*(Fc+Fe)*t)/Es;
Qch = RFsignal .* sin(2*pi*(Fc+Fe)*t)/Es;

% Baseband Signal Representation
for i = 1:Nsym
    n_start = (i-1)*Tsym*Fs;
    bbSym_rx(i) = sum(Ich(n_start+1:n_start+Tsym*Fs) - j*Qch(n_start+1:n_start+Tsym*Fs));
end
sigPower = mean(abs(bbSym_rx).^2);

% Noise Insertion
noise = sqrt(N0)*randn(1,length(bbSym_rx)) + j*sqrt(N0)*randn(1,length(bbSym_rx));
bbSymN_rx = bbSym_rx+noise;
noisePower = mean(abs(noise).^2);
SNR = 10*log10(sigPower/noisePower)

% Signal Space Representation
%figure(3)
%scatter(real(bbSymN_rx), imag(bbSymN_rx));
%grid on;
%axis([-4 4 -4 4]);
%hold on;
%scatter(s(1,:),s(2,:),'r*');

% Optimal Receiver
hd_bbSym = zeros(1,Nsym);
for i= 1:Nsym
%     corr_result = bbSymN_rx(i)*conj(symTable);
%     dist_metric = -abs(bbSymN_rx(i)-symTable);
    mod_dist_metric = bbSymN_rx(i)*conj(symTable) -1/2*abs(symTable).^2;
    [dammyVal hd_index] = max(real(mod_dist_metric));
    hd_bbSym(i) = symTable(hd_index);
end

SER = sum( abs(hd_bbSym - bbSym) > 0.01) /Nsym