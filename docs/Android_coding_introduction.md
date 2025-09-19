# Androidアプリ実装手順書

本書は「機能検査工程」における製造番号とシリアル番号の組合せデータをAndroid端末で管理し、サーバーへ送信するアプリの実装手順をまとめたものです。  
対象は system_spec.md および android_app_spec.md に準拠しています。

---

## 第1章 環境構築
- [ ] Kotlin プロジェクト作成（minSdk=28）
- [ ] 主要ライブラリ導入  
  （Room, Retrofit2, OkHttp, Kotlin Coroutines, Hilt, CameraX, ZXing/ML Kit）
- [ ] HTTPS 通信確認（開発用APIエンドポイント）

---

## 第2章 プロジェクト構成
- [ ] Clean Architecture 構成を準備（presentation, domain, data）
- [ ] Hilt 初期設定（Applicationクラス、Module定義）
- [ ] 共通ユーティリティ（ログ、バリデーション関数）準備

---

## 第3章 データベース実装（Room）
- [ ] Entity作成：MfgSerialMapping, WorkSession, Outbox
- [ ] DAO作成：Insert, Query, Update, Delete
- [ ] Databaseクラス作成
- [ ] Unique制約 (mfgId + serialId) 動作確認

---

## 第4章 スキャン・入力機能
- [ ] 製造番号スキャン画面作成（CameraX）
- [ ] ZXing または ML Kit を利用したバーコード読み取り
- [ ] 手入力フォームと二重確認オプション実装
- [ ] シリアル番号スキャンと重複チェック実装

---

## 第5章 データ管理ロジック
- [ ] UseCase実装（ScanSerial, AppendMapping, CloseWorkSession）
- [ ] Repository作成（Room + Retrofit 実装）
- [ ] ViewModelで状態保持（LiveData / Flow）

---

## 第6章 一覧・編集画面
- [ ] RecyclerViewでMFG単位のシリアル一覧表示
- [ ] 削除・再スキャン機能
- [ ] メモ編集機能(option)

---

## 第7章 送信機能
- [ ] Retrofit2 APIクライアント作成
- [ ] Bulk送信実装（/api/v1/mappings/bulk）
- [ ] Outboxテーブルに送信スナップショット保存
- [ ] 送信進捗UI表示

---

## 第8章 エラー処理・再送管理
- [ ] ステータス管理（READY / SENT / ERROR）
- [ ] 再送対象エラー判定（5xx, NET_*）
- [ ] 部分失敗時の個別再送処理
- [ ] 再試行ロジック（指数バックオフ）

---

## 第9章 設定画面
- [ ] エンドポイントURL設定
- [ ] 認証トークン設定
- [ ] タイムアウト・再試行条件設定
- [ ] 連続スキャンON/OFF切替

---

## 第10章 ログ・監査
- [ ] 操作ログ（スキャン/削除/送信）
- [ ] 送信ログ（リクエストID、件数、結果）
- [ ] ローカル保存と表示UI

---

## 第11章 テスト
- [ ] ユニットテスト（UseCase, Repository）
- [ ] UIテスト（誤読・重複・オフライン動作）
- [ ] モックサーバによる通信エラー試験
- [ ] 大量データパフォーマンステスト

---

## 第12章 運用確認
- [ ] 端末DB容量制御確認
- [ ] オフライン状態での動作確認
- [ ] 自動送信（Wi-Fi時/充電中）の動作確認
- [ ] ログレベル調整・運用ドキュメント化
