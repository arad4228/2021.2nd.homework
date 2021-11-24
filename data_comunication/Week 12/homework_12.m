clear;
% input bit만들기
% noise의 matrix
N0mat = [0.05:0.02:0.5];
cSNRU = zeros(length(N0mat),1);
cBERU = zeros(length(N0mat),1);

cSNRC = zeros(length(N0mat),1);
cBERC = zeros(length(N0mat),1);

cSNRR = zeros(length(N0mat),1);
cBERR = zeros(length(N0mat),1);

% bit의 갯수.
Nbit = 5000;
% modulation order
M=4;
%입력신호 만들기
inputBit = randi(2,1,Nbit) - 1;

for iN0 = 1: length(N0mat)
    N0 = N0mat(iN0);


% Parameter for Conv. coding
codeRateC = 1/2;
codeRateR = 1/3;
Nstate = 4;

% shift register
shiftRegister = [0 0 0];
% generator metrix
gMatrix = [1 0 1; 1 1 1];
% input code word
input_ccC = [inputBit shiftRegister];
codewordC = [];
codewordR = [];

for i = 1:length(input_ccC)
    % shift 연산
    shiftRegister = [input_ccC(i) shiftRegister(1:2)];
    
    % 나오는 bit 만들기
    for i=1:2
        xsum = 0;
        for k = 1:length(shiftRegister)
            % 연산은 generate metrix에 기반한다.
            xsum = xor(xsum, shiftRegister(k)*gMatrix(i,k));
        end
        codewordC = [codewordC xsum];
    end
end
% 전체 codeword의 길이
NcC = 1/codeRateC*(length(input_ccC));

% Repetition code
for i = 1:length(inputBit)
    codewordR = [codewordR inputBit(i) inputBit(i) inputBit(i)];
end
NcR = 1/codeRateR*(length(inputBit));

% Parameter Setting
Tsym = 1;
NsymU = Nbit/log2(M);
NsymC = NcC/log2(M);
NsymR = NcR/log2(M);

Fs = 100;
Fc = 10;
% N0 = 0.2;

% Simulation
tU = [Tsym/Fs : Tsym/Fs : Tsym*NsymU];
tC = [Tsym/Fs : Tsym/Fs : Tsym*NsymC];
tR = [Tsym/Fs : Tsym/Fs : Tsym*NsymR];

TmaxU = length(tU);
TmaxC = length(tC);
TmaxR = length(tR);

% Symbol 생성
symTable = zeros(1,4);
for i = 1:M
    i_m = 2*pi*(i-1)/M + pi/4;
    symTable(i) = cos(i_m) + j*sin(i_m);
end
bitTable = [0 0; 0 1; 1 0; 1 1];

% Basis Signal 생성
phi1U = cos(2*pi*Fc*tU(1:Tsym*Fs));
EsU = norm(phi1U);
phi1U = phi1U/EsU;

phi2U = -sin(2*pi*Fc*tU(1:Tsym*Fs));
EsU = norm(phi2U);
phi2U = phi2U/EsU;

phi1C = cos(2*pi*Fc*tC(1:Tsym*Fs));
EsC = norm(phi1C);
phi1C = phi1C/EsC;

phi2C = -sin(2*pi*Fc*tC(1:Tsym*Fs));
EsC = norm(phi2C);
phi2C = phi2C/EsC;

phi1R = cos(2*pi*Fc*tR(1:Tsym*Fs));
EsR = norm(phi1R);
phi1R = phi1C/EsR;

phi2R = -sin(2*pi*Fc*tR(1:Tsym*Fs));
EsR = norm(phi2R);
phi2R = phi2C/EsR;
%% TX

% 랜덤신호만들기
mU = zeros(1,NsymU);
mC = zeros(1,NsymC);
mR = zeros(1,NsymR);

% 0, 1, 2, 3
% Uncoded bit
for i= 1:NsymU
    mU(i) = 0;
    % 2진수를 바꿔주는 과정
    for k = 1:2
        mU(i) = 2*mU(i) + inputBit(2*(i-1)+k);
    end
end
% 0,1,2,3->1,2,3,4
mU = mU+1;
% Convolutional Code
for i= 1:NsymC
    mC(i) = 0;
    % 2진수를 바꿔주는 과정
    for k = 1:2
        mC(i) = 2*mC(i) + codewordC(2*(i-1)+k);
    end
end
% 0,1,2,3->1,2,3,4
mC = mC+1;

% repetition code
for i= 1:NsymR
    mR(i) = 0;
    % 2진수를 바꿔주는 과정
    for k = 1:2
        mR(i) = 2*mR(i) + codewordR(2*(i-1)+k);
    end
end
mR = mR+1;

% 심볼신호 만들기
bbSymC = symTable(mC);
bbSymU = symTable(mU);
bbSymR = symTable(mR);

% Up-conversion (DAC 포함)
RFsignalU = zeros(1,TmaxU);
RFsignalC = zeros(1,TmaxC);
RFsignalR = zeros(1,TmaxR);

for iterT = 1:TmaxU
    iterSymU = floor((iterT-1)/Fs)+1;
    RFsignalU(iterT) = real(bbSymU(iterSymU))*cos(2*pi*Fc*tU(iterT))/EsU - imag(bbSymU(iterSymU))*sin(2*pi*Fc*tU(iterT))/EsU;
end

for iterT = 1:TmaxC
    iterSymC = floor((iterT-1)/Fs)+1;
    RFsignalC(iterT) = real(bbSymC(iterSymC))*cos(2*pi*Fc*tC(iterT))/EsC - imag(bbSymC(iterSymC))*sin(2*pi*Fc*tC(iterT))/EsC;
end

for iterT = 1:TmaxR
    iterSymR = floor((iterT-1)/Fs)+1;
    RFsignalR(iterT) = real(bbSymR(iterSymR))*cos(2*pi*Fc*tR(iterT))/EsR - imag(bbSymR(iterSymR))*sin(2*pi*Fc*tR(iterT))/EsR;
end


%% RX
% Coherent Detection
IchU = RFsignalU .* cos(2*pi*(Fc)*tU)/EsU;
QchU = RFsignalU .* sin(2*pi*(Fc)*tU)/EsU;

IchC = RFsignalC .* cos(2*pi*(Fc)*tC)/EsC;
QchC = RFsignalC .* sin(2*pi*(Fc)*tC)/EsC;

IchR = RFsignalR .* cos(2*pi*(Fc)*tR)/EsR;
QchR = RFsignalR .* sin(2*pi*(Fc)*tR)/EsR;

% Baseband Signal Representation
for i = 1:NsymU
    n_start = (i-1)*Tsym*Fs;
    bbSym_rxU(i) = sum(IchU(n_start+1:n_start+Tsym*Fs) - j*QchU(n_start+1:n_start+Tsym*Fs));
end
sigPowerU = mean(abs(bbSym_rxU).^2);

for i = 1:NsymC
    n_start = (i-1)*Tsym*Fs;
    bbSym_rxC(i) = sum(IchC(n_start+1:n_start+Tsym*Fs) - j*QchC(n_start+1:n_start+Tsym*Fs));
end
sigPowerC = mean(abs(bbSym_rxC).^2);

for i = 1:NsymR
    n_start = (i-1)*Tsym*Fs;
    bbSym_rxR(i) = sum(IchR(n_start+1:n_start+Tsym*Fs) - j*QchR(n_start+1:n_start+Tsym*Fs));
end
sigPowerR = mean(abs(bbSym_rxR).^2);

% Noise Insertion
noiseU = sqrt(N0)*randn(1,length(bbSym_rxU)) + j*sqrt(N0)*randn(1,length(bbSym_rxU));
bbSymN_rxU = bbSym_rxU+noiseU;
noisePowerU = mean(abs(noiseU).^2);
SNRU = 10*log10(sigPowerU/noisePowerU)

noiseC = sqrt(N0)*randn(1,length(bbSym_rxC)) + j*sqrt(N0)*randn(1,length(bbSym_rxC));
bbSymN_rxC = bbSym_rxC+noiseC;
noisePowerC = mean(abs(noiseC).^2);
SNRC = 10*log10(sigPowerC/noisePowerC)

noiseR = sqrt(N0)*randn(1,length(bbSym_rxR)) + j*sqrt(N0)*randn(1,length(bbSym_rxR));
bbSymN_rxR = bbSym_rxR+noiseR;
noisePowerR = mean(abs(noiseR).^2);
SNRR = 10*log10(sigPowerR/noisePowerR)

% Optimal Receiver
hd_bbSymU = zeros(1,NsymU);
hd_bitU = []; 
for i= 1:NsymU
    corr_resultU = bbSymN_rxU(i)*conj(symTable);
    [dammyVal hd_indexU] = max(real(corr_resultU));
    hd_bbSymU(i) = symTable(hd_indexU);
    % bit decoding.
    hd_bitU = [hd_bitU bitTable(hd_indexU,:)];
end

hd_bbSymC = zeros(1,NsymC);
hd_bitC = []; 
for i= 1:NsymC
    corr_result = bbSymN_rxC(i)*conj(symTable);
    [dammyVal hd_indexC] = max(real(corr_result));
    hd_bbSymC(i) = symTable(hd_indexC);
    % bit decoding.
    hd_bitC = [hd_bitC bitTable(hd_indexC,:)];
end

hd_bbSymR = zeros(1,NsymR);
hd_bitR = []; 
for i= 1:NsymR
    corr_result = bbSymN_rxR(i)*conj(symTable);
    [dammyVal hd_indexR] = max(real(corr_result));
    hd_bbSymR(i) = symTable(hd_indexR);
    % bit decoding.
    hd_bitR = [hd_bitR bitTable(hd_indexR,:)];
end

% Trellis information
%transition
% 2개씩 나간다.
trellis_transition = zeros(Nstate,2);
trellis_transition(1,1) = 1; % 00 -> 1번째
trellis_transition(1,2) = 3; % 2기 들어오면 3으로
trellis_transition(2,1) = 1; % 01에서 0이 들어오면 1
trellis_transition(2,2) = 3; % 1이 들어오면 3으로 
trellis_transition(3,1) = 2;
trellis_transition(3,2) = 4;
trellis_transition(4,1) = 2;
trellis_transition(4,2) = 4;

% output bitstream
trellis_out = ones(Nstate,Nstate)*-1; %prev state->next state
trellis_out(1,trellis_transition(1,1)) = 0;
trellis_out(1,trellis_transition(1,2)) = 3;
trellis_out(2,trellis_transition(2,1)) = 3;
trellis_out(2,trellis_transition(2,2)) = 0;
trellis_out(3,trellis_transition(3,1)) = 1;
trellis_out(3,trellis_transition(3,2)) = 2;
trellis_out(4,trellis_transition(4,1)) = 2;
trellis_out(4,trellis_transition(4,2)) = 1;

% input bitstream
trellis_in = ones(Nstate,Nstate)*-1;
for i = 1:Nstate
    for k = 1:2 % k = 1,2 -> 0,1
        trellis_in(i,trellis_transition(i,k)) = k-1;
    end
end

% Viterbi Decoding
survivorPath = zeros(Nstate, NcC/2);
% 실제 distance를 저장
accum_metric = zeros(Nstate,1);
for i = 1:NcC/2
    if i ==1
        survivorNd = [1 0 0 0];
    else
        survivorNd = zeros(1,4);
        ndCand = transpose(find(survivorPath(:,i-1)>0));
        survivorNd(ndCand) = 1;
    end
    
    hopDist = ones(Nstate,Nstate)*-1;
    branchMetric = ones(Nstate,Nstate)*-1;
    
    for n1=1:Nstate
        for n2 = 1:Nstate
            if survivorNd(n1)>0 && length(find(trellis_transition(n1,:)==n2))>0
                path_out = [floor(trellis_out(n1,n2)/2) mod(trellis_out(n1,n2),2)];
                hopDist(n1,n2) = sum(abs(path_out-hd_bitC((i-1)*2+1:i*2)));
                branchMetric(n1,n2) = accum_metric(n1) + hopDist(n1,n2);
                
            end
        end
    end
    
    %survivor path 선정
    for n = 1:Nstate
        pathIndex = find(branchMetric(:,n)>=0);
        if length(pathIndex)>0
            [accum_metric(n) minIndex] = min(branchMetric(pathIndex,n));
            survivorPath(n,i) = pathIndex(minIndex);
        end
    end

end
% 코드를 다 풀어야한다. 뒤에서부터 시작한다.
hd_uncode = zeros(1,NcC/2);
lastNd = 1;
for i = 1:NcC/2
    hd_uncode(NcC/2 - (i-1)) = trellis_in(survivorPath(lastNd,NcC/2-(i-1)),lastNd);
    lastNd = survivorPath(lastNd,NcC/2-(i-1));
end
hd_uncode = hd_uncode(1:Nbit);

% repetition에서 원래 코드로 복구하기
o = 1;
hd_recode = zeros(1,NcR/3);
for i = 1:3:length(hd_bitR)
    result = hd_bitR(i)+hd_bitR(i+1)+hd_bitR(i+2);
    if result > 1
        hd_recode(o) = 1;
        o = o+1;
    else
        hd_recode(o) = 0;
        o = o+1;
    end
end

BERU = sum(abs(hd_bitU - inputBit)>0.01)/Nbit

SERU = sum( abs(hd_bbSymU - bbSymU) > 0.01) /NsymU

BERC = sum(abs(hd_uncode - inputBit)>0.01)/Nbit

SERC = sum( abs(hd_bbSymC - bbSymC) > 0.01) /NsymC

BERR = sum(abs(hd_recode - inputBit)>0.01)/Nbit

SERR = sum( abs(hd_bbSymR - bbSymR) > 0.01) /NsymR

cSNRU(iN0) = SNRU;
cBERU(iN0) = BERU;

cSNRC(iN0) = SNRC;
cBERC(iN0) = BERC;

cSNRR(iN0) = SNRR;
cBERR(iN0) = BERR;
end
figure(1)
plot(cSNRU,cBERU,"r");
hold on;
plot(cSNRC,cBERC,"b");
hold on;
plot(cSNRR,cBERR,"g");
hold on;
legend('Uncoded bit','Convolutional code', 'Repetition code');