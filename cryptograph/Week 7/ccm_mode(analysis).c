/* ccm_mode.c - TinyCrypt implementation of CCM mode */

/*
 *  Copyright (C) 2017 by Intel Corporation, All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *    - Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *
 *    - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *    - Neither the name of Intel Corporation nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */

#include <tinycrypt/ccm_mode.h>
#include <tinycrypt/constants.h>
#include <tinycrypt/utils.h>

#include <stdio.h>

/*
* 해당 부분은 잘 모르겠습니다.
* 추측하기로는 아마도 메시지의 길이나 Enc에서 사용될 key들에 대한 정보나 Ctr에서 사용될 Nonce를 넣어주는 과정으로 생각됩니다.
*/
int tc_ccm_config(TCCcmMode_t c, TCAesKeySched_t sched, uint8_t *nonce,
		  unsigned int nlen, unsigned int mlen)
{

	/* input sanity check: */
	if (c == (TCCcmMode_t) 0 ||
	    sched == (TCAesKeySched_t) 0 ||
	    nonce == (uint8_t *) 0) {
		return TC_CRYPTO_FAIL;
	} else if (nlen != 13) {
		return TC_CRYPTO_FAIL; /* The allowed nonce size is: 13. See documentation.*/
	} else if ((mlen < 4) || (mlen > 16) || (mlen & 1)) {
		return TC_CRYPTO_FAIL; /* The allowed mac sizes are: 4, 6, 8, 10, 12, 14, 16.*/
	}

	c->mlen = mlen;
	c->sched = sched;
	c->nonce = nonce;

	return TC_CRYPTO_SUCCESS;
}

/*
 * Variation of CBC-MAC mode used in CCM.
 */
/* 
 * 해당 함수는 Mac then Encrypt에서 MAC을 생성하기 위한 함수로 cbc mode를 통한 MAC생성을 하는 부분입니다.
 * 따라서 Tag의 주소를 받아 M에 해당하는 data와 key들(k,k1)을 생성할 값을 받는다.
 * 그러나 dlen과 flag에 해당하는 값의 정확한 의미는 아직 잘 모르겠습니다.
*/
static void ccm_cbc_mac(uint8_t *T, const uint8_t *data, unsigned int dlen,
			unsigned int flag, TCAesKeySched_t sched)
{

	unsigned int i;
	/* 
	* 아마도 추측하기로는 flag를 통해 pading을 해야하는 지 확인을 하고
	* 해야한다면 big endian 방식을 사용하기에 첫번째 block에 하지 않을까 생각합니다.
	* 따라서 flag는 padding을 해야하는지를 알려주고 dlen은 data의 len으로 추측됩니다.
	*/
	if (flag > 0) {
		T[0] ^= (uint8_t)(dlen >> 8);
		T[1] ^= (uint8_t)(dlen);
		dlen += 2; i = 2;
	} else {
		i = 0;
	}

	while (i < dlen) {
		T[i++ % (Nb * Nk)] ^= *data++;
		if (((i % (Nb * Nk)) == 0) || dlen == i) {
			/* 
			* T에 해당하는 message를 입력으로 받아 keysecheduling을 통해 k, k1를 생성할 값을 넘겨준다.
			* 이 값들을 가지고 message를 AES로 Enc를 해서 최종적인 tag를 생성한다.
			*  여기서 CBC의 특징인 이전의 암호문이 다시 입력으로 들어가 결과가 행해지는 방식을 볼 수 있다.
			*/
			(void) tc_aes_encrypt(T, T, sched);
		}
	}
}

/*
 * Variation of CTR mode used in CCM.
 * The CTR mode used by CCM is slightly different than the conventional CTR
 * mode (the counter is increased before encryption, instead of after
 * encryption). Besides, it is assumed that the counter is stored in the last
 * 2 bytes of the nonce.
 */
/*
* 여기는 MAC-then-Encrypt에 payload를 암호화 하기위한 Enc입니다.
* 따라서 여기서는 conter값에 해당하는 ctr과 key들을 생성하기 위한 sched값과 입력 데이터에 해당하는
* in, data의 길이를 나타내는 inlen, 암호화 값에 대한 크기를 알려주는 outlen을 입력받습니다.
*/
static int ccm_ctr_mode(uint8_t *out, unsigned int outlen, const uint8_t *in,
			unsigned int inlen, uint8_t *ctr, const TCAesKeySched_t sched)
{

	uint8_t buffer[TC_AES_BLOCK_SIZE];
	// nonce를 위한 값을 위한 공간.
	uint8_t nonce[TC_AES_BLOCK_SIZE];
	uint16_t block_num;
	unsigned int i;

	/* input sanity check: */
	// 해당 과정은 어떠한 과정인지 잘 모르겠습니다.
	if (out == (uint8_t *) 0 ||
	    in == (uint8_t *) 0 ||
	    ctr == (uint8_t *) 0 ||
	    sched == (TCAesKeySched_t) 0 ||
	    inlen == 0 ||
	    outlen == 0 ||
	    outlen != inlen) {
		return TC_CRYPTO_FAIL;
	}

	/* copy the counter to the nonce */
	// 전달 받은 counter의 값을 nonce로 복사
	(void) _copy(nonce, sizeof(nonce), ctr, sizeof(nonce));

	/* select the last 2 bytes of the nonce to be incremented */
	block_num = (uint16_t) ((nonce[14] << 8)|(nonce[15]));
	for (i = 0; i < inlen; ++i) {
		if ((i % (TC_AES_BLOCK_SIZE)) == 0) {
			block_num++;
			nonce[14] = (uint8_t)(block_num >> 8);
			nonce[15] = (uint8_t)(block_num);
			// nonce-based Ctr을 수행한다.
			if (!tc_aes_encrypt(buffer, nonce, sched)) {
				return TC_CRYPTO_FAIL;
			}
		}
		/* update the output */
		// output에 대한 값을 수정한다.
		*out++ = buffer[i % (TC_AES_BLOCK_SIZE)] ^ *in++;
	}

	/* update the counter */
	/* 
	* 마찬가지로 사용했던 counter의 값을 변경한다.
	* 이는 동일한 IV를 두번 사용하지 않기 위해서이다.
	*/
	ctr[14] = nonce[14]; ctr[15] = nonce[15];

	return TC_CRYPTO_SUCCESS;
}

/*
* 모든 과정은 MAC-then-encrypt의 AES-CCM에 해당합니다.
* const uint8_t *associated_data == Mac then Encrypt A(연관데이터)에 해당한다.
* unsigned int alen는 associated_data의 크기에 해당한다.
* const uint8_t *payload 아마도 packet에 payload에 해당한다 생각됩니다.
* unsigned int plen는 payload의 크기에 해당한다.
* TCCcmMode_t c는 각 AES-CCM에서 사용될 key들에 대한 정보들을 모은 객체라추정된다. c를 통해 nonce나 sched를 접근하기에
*/
int tc_ccm_generation_encryption(uint8_t *out, unsigned int olen,
				 const uint8_t *associated_data,
				 unsigned int alen, const uint8_t *payload,
				 unsigned int plen, TCCcmMode_t c)
{

	/* input sanity check: */
	// 이부분은 잘 모르겠습니다. 아마 각종 상태에 대한 점검으로 추측됩니다.
	if ((out == (uint8_t *) 0) ||
		(c == (TCCcmMode_t) 0) ||
		((plen > 0) && (payload == (uint8_t *) 0)) ||
		((alen > 0) && (associated_data == (uint8_t *) 0)) ||
		(alen >= TC_CCM_AAD_MAX_BYTES) || /* associated data size unsupported */
		(plen >= TC_CCM_PAYLOAD_MAX_BYTES) || /* payload size unsupported */
		(olen < (plen + c->mlen))) {  /* invalid output buffer size */
		return TC_CRYPTO_FAIL;
	}

	uint8_t b[Nb * Nk];
	uint8_t tag[Nb * Nk];
	unsigned int i;

	/* GENERATING THE AUTHENTICATION TAG: */

	/* formatting the sequence b for authentication: */
	// MAC을 생성하기 위한 준비로 생각됩니다.
	b[0] = ((alen > 0) ? 0x40:0) | (((c->mlen - 2) / 2 << 3)) | (1);
	for (i = 1; i <= 13; ++i) {
		b[i] = c->nonce[i - 1];
	}
	b[14] = (uint8_t)(plen >> 8);
	b[15] = (uint8_t)(plen);

	/* computing the authentication tag using cbc-mac: */
	/*
	* MAC-then-Enc이므로 MAC을 먼저 생성한다.
	* 만약 alen의 크기가 양수라는 것은 associated_data와 message에 대한 MAC을 생성하는 과정이고
	* 만약 plen의 크기가 양수라는 것은 packet에 대한 payload에 대한 MAC을 생성하는 과정이라 생각됩니다.
	* 이 두 자료를 받아 두개의 tag를 생성하는 것으로 생각됩니다.
	*/
	(void) tc_aes_encrypt(tag, b, c->sched);
	if (alen > 0) {
		ccm_cbc_mac(tag, associated_data, alen, 1, c->sched);
	}
	if (plen > 0) {
		ccm_cbc_mac(tag, payload, plen, 0, c->sched);
	}

	/* ENCRYPTION: */

	/* formatting the sequence b for encryption: */
	// 이역시 Enc를 위한 준비로 생각됩니다.
	b[0] = 1; /* q - 1 = 2 - 1 = 1 */
	b[14] = b[15] = TC_ZERO_BYTE;

	/* encrypting payload using ctr mode: */
	// payload에 해당하는 부분을 암호화 하는 곳입니다.
	ccm_ctr_mode(out, plen, payload, plen, b, c->sched);

	// conter를 초기화하는 작업에 해당한다 생각됩니다.
	b[14] = b[15] = TC_ZERO_BYTE; /* restoring initial counter for ctr_mode (0):*/

	// Mac-then-Enc에서 Enc에 해당한다. 여기서 tag와 Message를 같이 넣어 AES로 Enc한다.
	/* encrypting b and adding the tag to the output: */
	(void) tc_aes_encrypt(b, b, c->sched);
	out += plen;
	for (i = 0; i < c->mlen; ++i) {
		*out++ = tag[i] ^ b[i];
	}

	return TC_CRYPTO_SUCCESS;
}

/*
* 해당 과정은 Dec과정이다.
* 최종적인 Message가 될 uint8_t *out과 output length에 해당하는 olen으로 생각됩니다.
* const uint8_t *associated_data == Mac then Encrypt A(연관데이터)에 해당한다.
* unsigned int alen는 associated_data의 크기에 해당한다.
* const uint8_t *payload 아마도 packet에 payload에 해당한다 생각됩니다.
* unsigned int plen는 payload의 크기에 해당한다.
* TCCcmMode_t c는 각 AES-CCM에서 사용될 key들에 대한 정보들을 모은 객체라추정된다. c를 통해 nonce나 sched를 접근하기에
*/
int tc_ccm_decryption_verification(uint8_t *out, unsigned int olen,
				   const uint8_t *associated_data,
				   unsigned int alen, const uint8_t *payload,
				   unsigned int plen, TCCcmMode_t c)
{

	/* input sanity check: */
	// 이부분은 잘 모르겠습니다. 아마 각종 상태에 대한 점검으로 추측됩니다.
	if ((out == (uint8_t *) 0) ||
	    (c == (TCCcmMode_t) 0) ||
	    ((plen > 0) && (payload == (uint8_t *) 0)) ||
	    ((alen > 0) && (associated_data == (uint8_t *) 0)) ||
	    (alen >= TC_CCM_AAD_MAX_BYTES) || /* associated data size unsupported */
	    (plen >= TC_CCM_PAYLOAD_MAX_BYTES) || /* payload size unsupported */
	    (olen < plen - c->mlen)) { /* invalid output buffer size */
		return TC_CRYPTO_FAIL;
  }

	uint8_t b[Nb * Nk];
	uint8_t tag[Nb * Nk];
	unsigned int i;

	/* DECRYPTION: */
	// Decryptin에 대한 준비로 생각됩니다.
	/* formatting the sequence b for decryption: */
	b[0] = 1; /* q - 1 = 2 - 1 = 1 */
	for (i = 1; i < 14; ++i) {
		b[i] = c->nonce[i - 1];
	}
	b[14] = b[15] = TC_ZERO_BYTE; /* initial counter value is 0 */

	/* decrypting payload using ctr mode: */
	//payload에 대한 정보를 Dec합니다.
	ccm_ctr_mode(out, plen - c->mlen, payload, plen - c->mlen, b, c->sched);

	b[14] = b[15] = TC_ZERO_BYTE; /* restoring initial counter value (0) */

	/* encrypting b and restoring the tag from input: */
	/*
	* 전달 받은 암호문을 Dec해서 message와 tag로 분리하는 과정입니다.
	*/
	(void) tc_aes_encrypt(b, b, c->sched);
	for (i = 0; i < c->mlen; ++i) {
		tag[i] = *(payload + plen - c->mlen + i) ^ b[i];
	}

	/* VERIFYING THE AUTHENTICATION TAG: */

	/* formatting the sequence b for authentication: */
	// 해당부분은 잘모르겠으나 사전 MAC을 검증하기 위한 준비과정임으로 생각됩니다.
	b[0] = ((alen > 0) ? 0x40:0)|(((c->mlen - 2) / 2 << 3)) | (1);
	for (i = 1; i < 14; ++i) {
		b[i] = c->nonce[i - 1];
	}
	b[14] = (uint8_t)((plen - c->mlen) >> 8);
	b[15] = (uint8_t)(plen - c->mlen);

	/* computing the authentication tag using cbc-mac: */
	/*
	* MAC-then-Enc이므로 전달받은 MAC과 메시지를 비교하기 위해 받은 data로 MAC을 생성한다.
	* 만약 alen의 크기가 양수라는 것은 associated_data와 전달받은 message에 대한 MAC을 생성하는 과정이고
	* 만약 plen의 크기가 양수라는 것은 packet에 대한 전달받은 payload에 대한 MAC을 생성하는 과정이라 생각됩니다.
	* 이 두 자료를 받아 두개의 tag를 생성하는 것으로 생각됩니다.
	*/
	(void) tc_aes_encrypt(b, b, c->sched);
	if (alen > 0) {
		ccm_cbc_mac(b, associated_data, alen, 1, c->sched);
	}
	if (plen > 0) {
		ccm_cbc_mac(b, out, plen - c->mlen, 0, c->sched);
	}

	/* comparing the received tag and the computed one: */
	// 생성한 MAC와 전달받은 MAC을 비교해서 0이면 성공을 아니면 ㅗ (error)를 낸다.
	if (_compare(b, tag, c->mlen) == 0) {
		return TC_CRYPTO_SUCCESS;
  	} else {
		/* erase the decrypted buffer in case of mac validation failure: */
		_set(out, 0, plen - c->mlen);
		return TC_CRYPTO_FAIL;
	}
}
