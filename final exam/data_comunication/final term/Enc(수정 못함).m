function output = ccEnc(input)



%Convolutional code information
%transition (prev state, inputBit :: output_state)
codeRate = 1/2;
shiftRegister = [0 0 0];
gMatrix = [1 1 0;1 0 1];
input_cc = [0 0 1 1 0 shiftRegister];
output = [];

for i = 1 : length(input_cc);
    %shift operation
    shiftRegister = [input_cc(i) shiftRegister(1:2)];
    for i = 1 : 2
        xsum = 0;
        if i == 1
            for k = 1 : length(shiftRegister)
                xsum = xor(xsum, shiftRegister(1)*gMatrix(1,k));
            end
            output = [output xsum];
        end
        if i == 2
           for k = 1 : length(shiftRegister)
                xsum = xor(xsum, shiftRegister(1)*gMatrix(2,k));;
            end
            output = [output xsum];
        end
    end
end