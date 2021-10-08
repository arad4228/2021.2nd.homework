clear;
%QAM을 그려봄
% Parameter Setting
Tsym = 1;
Nsym = 1000;
% Sampling size
Fs = 100;
% Carrier frequency(HZ)
Fc = 10;
%
Fe =  0;
% Noise level
N0 = 0.3;

% Simulation
% 샘플링된 것이 Fs로 나누면 이경우 100분의 1초마다 생기는 것임.
% 이를 끝까지 가봄 Nsymbol까지
t = [Tsym/Fs : Tsym/Fs : Tsym*Nsym];
Tmax = length(t);

% Signal power는 1이라고 가정
% Symbol 생성
M = 4;
symTable = zeros(1,4);
for i = 1:M
    i_m = 2*pi*(i-1)/M + pi/4;
    symTable(i) = cos(i_m) + j*sin(i_m);
end

% basis Signal 생성
phi1 = cos(2*pi*Fc*t(1:Tsym*Fs));
Es = norm(phi1);
% Othonomal Basis 생성
phi1 = phi1/Es;

phi2 = -sin(2*pi*Fc*t(1:Tsym*Fs));
Es = norm(phi2);
% Othonomal Basis 생성
phi2 = phi2/Es;


%% TX
% 랜덤 신호 만들기
m = randi(M,1,Nsym);

% 심볼 신호 만들기
% 심볼 테이블 만들듯이 만들면 된다.
theta_m = 2*pi*(m-1)/M + pi/4;
bbSym = cos(theta_m) + j*sin(theta_m);

% Up-conversion 실어보낼껏임.(DAC 포함)
RFsignal = zeros(1,Tmax);
for iterT = 1:Tmax
    iterSym = floor((iterT-1)/Fs)+1;
    RFsignal(iterT) = real(bbSym(iterSym))*cos(2*pi*Fc*t(iterT))/Es - imag(bbSym(iterSym))*sin(2*pi*Fc*t(iterT))/Es;
end

% 참고 - Signal 보여주기
% t가 x축,
%figure(1)
%plot(t,RFsignal);
%xlim([0 2]);

% 참고 - 심볼신호 보여주기
s = zeros(2,Nsym);
for i = 1:Nsym
    % 1일때는 1부터 100까지
    intStart = 1+(i-1)*Tsym*Fs;
    %첫번째는  1이고 마지막은 100이다.
    %2번째는시작이 101이고 마지막은 200이다.
    intEnd = i*Tsym*Fs;
    s(1,i) = sum(RFsignal(intStart:intEnd).*phi1);
    s(2,i) = sum(RFsignal(intStart:intEnd).*phi2);
end
figure(2)
scatter(s(1,:),s(2,:),'r*');
grid on;
axis([-2 2 -2 2]);

%% RX

% Coherent Detection
% 구하는 것은 결국 phi1신호를곱하면 된다. error가 있을 수 있으니 그대로 곱함.
Ich = RFsignal .* cos(2*pi*(Fc+Fe)*t)/Es;
Qch = RFsignal .* sin(2*pi*(Fc+Fe)*t)/Es;

% Low Pass FIlter보단
% Baseband Signal Representation
for i = 1:Nsym
    % 1일때는 0부터이다.
    n_start = (i-1)*Tsym*Fs;
    bbSym_rx(i) = sum(Ich(n_start+1:n_start+Tsym*Fs) - j*Qch(n_start+1:n_start+Tsym*Fs));
end
%.^2은 전체 제곱에 mean을 구함.
sigPower = mean(abs(bbSym_rx).^2);

% Noise Insertion
noise = sqrt(N0)*randn(1,length(bbSym_rx)) + j*sqrt(N0)*randn(1,length(bbSym_rx));

% noise가 반영된 signal R.
bbSymN_rx = bbSym_rx+noise;
noisePower = mean(abs(noise).^2);
SNR = 10*log10(sigPower/noisePower)

% Signal Space Representation
figure(3)
scatter(real(bbSymN_rx), imag(bbSymN_rx));
grid on;
axis([-2 2 -2 2]);
hold on;
scatter(s(1,:),s(2,:),'r*');

% Optimal Receiver
hd_bbSym = zeros(1,Nsym);
for i= 1:Nsym
    corr_result = bbSymN_rx(i)*conj(symTable);
    [dammyVal hd_index] = max(real(corr_result));
    hd_bbSym(i) = symTable(hd_index);
end

% Symbol Error Rate
SER = sum( abs(hd_bbSym - bbSym) > 0.01) /Nsym