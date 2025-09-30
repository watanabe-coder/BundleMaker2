# Androidアプリ設計仕様書

## 1. 目的・範囲
- **目的**：製造番号（MFG）とシリアル番号を組み合わせて端末内に保持し、REST APIでサーバーへ送信
- **対象端末**：キーエンス DX-A800
- **OSバージョン**：Android 13 (API 33)

## 2. 技術要件
- **言語**：Kotlin
- **主要ライブラリ**：Room、Retrofit2、Hilt、Coroutines
- **認証**：Bearerトークン

## 3. 画面構成

### ログイン画面
- ユーザーID入力欄
- ログインボタン
- エラーメッセージ表示

### メイン画面
- 製造番号入力ボタン
- シリアル番号入力ボタン
- 確認ボタン
- 同期ボタン

### 確認画面
- MFG × Serial一覧表示
- 確定ボタン
- 戻るボタン

### 入力ダイアログ
- タイトル表示
- 入力欄
- 入力完了ボタン
- キャンセルボタン

## 4. データベース（Room）

### MfgSerialMappingテーブル
- `id` (PK, Long)
- `mfgId` (Text)
- `serialId` (Text)
- `scannedAt` (Long)
- `status` (Text: DRAFT/READY/SENT/ERROR)
- `errorCode` (Text, Nullable)
- **UniqueIndex**: `(mfgId, serialId)`

## 5. API仕様

### エンドポイント



### リクエスト


### レスポンス


## 6. 基本機能

### スキャン機能
- DX-A800標準スキャナー使用
- 手入力フォールバック対応

### データ管理
- 重複チェック（mfgId + serialId）
- オフライン対応

### 送信機能
- 手動送信（同期ボタン）
- エラー時再試行

## 7. アーキテクチャ
- Clean Architecture
- Hilt DI
- Coroutines

## 8. 権限
- CAMERA

## 9. 基本ワークフロー
1. ログイン
2. 製造番号入力
3. シリアル番号入力（複数可）
4. 確認・確定
5. 送信