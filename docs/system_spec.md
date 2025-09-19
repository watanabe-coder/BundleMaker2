# システムワークフロー

## 1. ワークフロー

### 検査部門

#### 入力情報

- 流れてきた 製造番号
- 各ユニットに付与されている シリアル番号（複数）

#### 処理内容

- 製造番号－シリアル番号の組み合わせをユニット数分生成。
- RoomDB（Android端末内DB）に保持。

#### 出力タイミング

- 作業終了後に、端末から REST API を介してサーバーのDBへ送信。

#### 通信の方向性

- Android → サーバー のみ

---

## 2. ワークフロー図（Mermaid）

flowchart TD
    subgraph 機能検査工程
        A1[製造番号を読み取り]
        A2[ユニットごとのシリアル番号を読み取り]
        A3[製造番号－シリアル番号の組合せデータを生成]
        A4[RoomDBへ保存]
        A5[作業終了後、REST APIを通じてサーバーへ送信]
    end

    A1 --> A2 --> A3 --> A4 --> A5

## 3. ER図

erDiagram
    MFG_SERIAL_MAPPING {
        int    ID PK "連番キー (内部管理用)"
        string MFG_ID "製造番号"
        string Serial_ID "シリアル番号"
        datetime Scanned_At "読み取り日時"
        UNIQUE (MFG_ID, Serial_ID) "製造番号×シリアル番号で一意"
    }

## ポイント
- 対象は「機能検査工程」に限定。
- Android端末は 読み取り → 組合せデータ作成 → ローカル保存 → API送信 という一方向フロー。
- サーバーからAndroidへのデータ送信は発生しない。

