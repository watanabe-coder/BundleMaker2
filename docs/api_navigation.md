# API�݌v��

## 1. ��ʑJ�ڐ}

```mermaid
graph TD
    A[���O�C�����] -->|�F�ؐ���| B[���C�����]
    B -->|�����ԍ����̓{�^��| C[�����ԍ��X�L����]
    C -->|�X�L��������| D[�V���A���ԍ��A���X�L�������]
    D -->|�V���A���ԍ��X�L����| D
    D -->|�m�F�{�^��| E[�m�F���]
    D -->|�L�����Z��| B
    E -->|�m��| B
    E -->|�߂�| D
    B -->|�����{�^��| F
    B -->|���O�A�E�g| A
    F -->|�R�Â����R�[�h�̃A�b�v���[�h| B
```

## 2. �����{�^���������̏ڍ׋���

### 1. �f�[�^�擾
     - RoomDB �� MfgSerialMapping �e�[�u������ status='READY' �̃��R�[�h���擾����B
     - �����ԍ����ƂɃO���[�s���O�B

### 2. API���M
     - REST API ���Ăяo���A�擾�������R�[�h���O����DB�T�[�o�[�ɑ��M����B
     - 1�̐����ԍ��ɕR�Â��S�V���A���ԍ���1�g�����U�N�V�����œo�^����B
     - ���M�`��: JSON

#### ���M��
      {
    �@  "mfgId": "MFG12345",
    �@  "serials": [
    �@    {"serialId": "SN00001", "scannedAt": "2025-09-30T10:15:30"},
    �@    {"serialId": "SN00002", "scannedAt": "2025-09-30T10:16:00"}
    �@  ]
    �@}

### 3. ���M���ʂ̔���
     - ������: status='SYNCED', syncedAt���L�^�B
     - ���s��: status='FAILED', RoomDB����đ��\�ɂ���B

### 4. ���[�U�[�ʒm
     - �������������^���s�������_�C�A���O��g�[�X�g�Œʒm����B


## 3. �p�����[�^��`�i�����֘A��ǉ��j
     - status: String - ���R�[�h�̏�ԁiREADY, SYNCED, FAILED�j
     - scannedAt: DateTime - �X�L��������
     - syncedAt: DateTime - �������������i�������̂݋L�^

## 4. ��������

### 1. �I���f�}���h����
�f�[�^�擾
RoomDB ���� status='READY' �̃��R�[�h�� mfgId �P�ʂŎ擾�B

API���M�imfg�P�ʃg�����U�N�V�����j
�e mfgId ���Ƃ� JSON �`���ŃT�[�o�[�ɑ��M�B
�T�[�o�[���Ńg�����U�N�V���������B
���� �� �S�� SYNCED�A���s �� �S�� FAILED�B

���ʔ��f
�����Fstatus='SYNCED'�AsyncedAt �X�V�B�Y�����R�[�h�� RoomDB ����폜�B
���s�Fstatus='FAILED' �Ƃ��ĕێ��B

���[�U�[�ʒm
�����^���s�̌������_�C�A���O�܂��̓g�[�X�g�ŕ\���B

### 2. �o�b�N�O���E���h����
�g���K�[
WorkManager �ň��Ԋu�i��F1���Ԃ��Ɓj�Ɏ��s�B

�������e
RoomDB �� status='READY' ����� FAILED �̃��R�[�h���擾�B
�I���f�}���h�����Ɠ����菇�� mfgId ���Ƃɑ��M�B

���ʔ��f
�����Fstatus='SYNCED'�AsyncedAt �X�V�B�Y�����R�[�h�� RoomDB ����폜�B
���s�FFAILED �̂܂ܕێ����A����đ��ΏۂƂ���B

���[�U�[�ʒm
�ʏ�͒ʒm�Ȃ��B
�i�g���\��j�����t���ʒm��ǉ�����\������B

