# Androidアプリ設計手順書（AI用）

## 1. 概要
- **目的**：製造番号（MFG）とシリアル番号を組み合わせ、端末内で管理し、作業終了後にREST APIでサーバー送信。
- **対象端末**：キーエンス DX-A800
- **OS**：Android 13 (API 33)
- **言語/ライブラリ**：Kotlin / Room / Retrofit2 / OkHttp / Coroutines / Hilt
- **範囲**：スキャン/入力、DB保存、送信、エラー再送

---

## 2. ディレクトリ構成
- `presentation/` : Activity / Fragment + ViewModel + State
- `domain/` : UseCase
  - `ScanMfg`
  - `ScanSerial`
  - `AppendMapping`
  - `SendMappings`
- `data/` : Repository実装（Room/Retrofit）
- `data/entity/` : Room Entity
- `di/` : Hilt Module

---

## 3. 画面ごとの実装手順

### 3.1 ログイン画面
1. ユーザーID・パスワード入力欄を作成
2. ログインボタン押下で認証処理
   - 成功 → メイン画面遷移
   - 失敗 → エラーダイアログ
3. ViewModelで状態管理（認証中、成功、失敗）
4. Retrofitで認証APIを実装（POST /login 例）

### 3.2 メイン画面
1. ボタン設置
   - 製造番号入力 → ポップアップ表示
   - シリアル番号入力 → ポップアップ表示
2. ポップアップ内
   - QR読み取り（DX-A800標準スキャナー）
   - 手入力フォールバック（二重確認オプション）
3. 入力後の処理
   - MFG開始（WorkSession）
   - Serial追加（MfgSerialMappingに保存）
   - 重複チェック・書式バリデーション
4. 確認ボタン → 確認画面へ遷移
5. 同期ボタン → SendMappings UseCase実行
6. ハンバーガーメニュー実装
   - ログアウト
   - 設定（URL、トークン、スキャン挙動）

### 3.3 確認画面
1. RoomDBからMFG×Serialを一覧表示
2. 編集/削除可能にする
3. 確定ボタン押下でRoomDBに保存
4. 戻るボタン → メイン画面へ遷移

---

## 4. ワークフロー（端末内）
1. 作業者がMFGスキャン/入力
2. WorkSession開始
3. シリアル番号を順次スキャン/入力
4. `(MFG, Serial)`組合せをDBに保存
5. 作業終了 → 送信トリガ
6. SendMappings UseCase実行
   - 成功 → 状態更新（SENT）
   - 失敗 → 再送キュー管理（指数バックオフ）
   - 部分失敗 → 成功分はSENT、失敗分はERROR

---

## 5. データモデル（Room）

### 5.1 Entity設計
- **MfgSerialMapping**
  - id: Long (PK)
  - mfgId: String
  - serialId: String
  - scannedAt: Long / Instant
  - status: Enum(DRAFT, READY, SENT, ERROR)
  - errorCode: String?
  - UniqueIndex: `(mfgId, serialId)`
- **WorkSession**
  - sessionId: Long (PK)
  - mfgId: String
  - startedAt / endedAt: Long
  - note: String?
- **Outbox**
  - outboxId: Long (PK)
  - payloadJson: String
  - createdAt / lastTriedAt: Long
  - tryCount: Int
  - state: Enum(PENDING, SENDING, DONE, FAILED)

---

## 6. バリデーション手順
1. MFG/Serial書式チェック（桁数・プレフィクス・チェックサム）
2. 端末内重複チェック (`(mfgId, serialId)`一意)
3. スキャン優先、失敗時は手入力（二重確認オプション）

---

## 7. 送信/同期処理
1. ユーザー操作「送信」または自動送信
2. MFG単位のバッチ送信
3. ページング/分割（件数多い場合）
4. 再試行ポリシー
   - ネットワーク/5xx → 指数バックオフ
   - 4xx → 恒久エラー記録
5. requestId(UUID)付与
6. 成功 → SENT、失敗 → ERROR、UIに反映

---

## 8. API仕様
- **POST /api/v1/mappings/bulk**
  - Header: `Authorization: Bearer <token>`
  - Request:
    ```json
    {
      "requestId": "uuid",
      "deviceId": "AND-12345",
      "mfgId": "MFG-0001",
      "items": [
        {"serialId":"SR-001","scannedAt":"2025-09-19T12:34:56Z"}
      ]
    }
    ```
  - Response:
    ```json
    {
      "ok": true,
      "mfgId": "MFG-0001",
      "accepted": 1,
      "rejected": []
    }
    ```

---

## 9. エラー処理
- ネットワーク: `NET_UNREACHABLE`, `TLS_ERROR`
- 認証: `AUTH_INVALID`, `AUTH_EXPIRED`
- サーバー検証: `DUPLICATE`, `FORMAT_ERROR`, `POLICY_VIOLATION`
- 再送対象: `5xx`, `NET_*`
- 再送不要: `DUPLICATE`, `FORMAT_ERROR`

---

## 10. スキャン仕様
1. DX-A800標準スキャナー使用
2. 連続スキャンON/OFF
3. バイブ/音の設定可
4. スキャン失敗 → 手入力フォールバック（二重確認可）

---

## 11. アーキテクチャ/技術
- Clean Architecture（Presentation / Domain / Data）
- DI：Hilt
- Coroutine + FlowでDB監視/送信進捗
- Retrofit2 + OkHttpでAPI通信

---

## 12. 権限・プライバシー
- CAMERA 権限
- 個人情報非扱い
- 端末内スコープ保存

---

## 13. ログ
- 操作ログ: who/when/what（PII除外）
- 送信ログ: requestId、件数、結果、リトライ回数

---

## 14. テスト手順
1. **ユニット**
   - UseCase/Repository 正常系・例外系
2. **UI**
   - スキャン誤読
   - 重複登録
   - オフライン時の挙動
3. **結合**
   - Room ↔ Repository ↔ Retrofit
4. **通信**
   - モックサーバで 2xx / 4xx / 5xx / タイムアウト

---

## 15. 設定/運用パラメータ
- エンドポイントURL、認証トークン
- 再試行回数上限
- ログレベル

---

## 16. 将来拡張
- WorkManagerを用いた送信ジョブスケジューラ強化
